package com.aprendeandroid.sqlitealbum;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnActionExpandListener;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

//public class DBListFragment extends CustomListFragment implements 
//			OnQueryTextListener, OnCloseListener, LoaderManager.LoaderCallbacks<Cursor> ,, no funciona OnCloseListener  en >= 3.0
public class DBListFragment extends CustomListFragment implements
		OnQueryTextListener, LoaderManager.LoaderCallbacks<Cursor> { //hay que implementar los callbacks del Loader
	
	
	private MyCursorAdapter adapter;
	private Hoja selectedHoja = null;
	private SearchView mSearchView;
	private String mCurFilter = null;
	
	//Este no tiene la base de datos, porque Cursor Loader 
	//se encarga de operar
	
	//1¼ Content provider permite que esta base de datos sea compartida 
		//con otras app voluntariamente, mediaStore, Diccionario, contactos...
	//2» Content provider usa LoaderManager para hacer operaciones de 
		//base de datos en segundoPlano (usa hilos), y es lo recomendado 
		
		
	
	//---------------------------------- CICLO VIDA DEL FRAGMENT
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		//Constructor especial del adapter para relaccionarse con cursores
		//usa los campos de row.xml
		//la imagen la voy a intentar poner solo al generar el diálogo
		adapter = new MyCursorAdapter(getActivity(), R.layout.row, null,
						new String[] { MySQLiteHelper.COLUMN_LAST_TIME, MySQLiteHelper.COLUMN_TITLE, MySQLiteHelper.COLUMN_COMMENT },
						new int[] {R.id.txtTime, R.id.txtTitle, R.id.txtComentario },
						CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		
		setListAdapter(adapter);

		registerForContextMenu(getListView());
		
		//Aqui la carga de la base de datos lo hace CursorLoader
		getActivity().getLoaderManager().initLoader(0, null, this);//recarga lista 
		//la primera vez va con initLoader
		
		
	}

	
	//------------------------------ BLOQUE AÑADIR HOJA --> 
	public void addHoja() {
		selectedHoja = null;
		//showEditDialog();
		
		getActivity().getLoaderManager().restartLoader(0, null, this);
	}

	//------------------------------ BLOQUE BORRAR HOJA
	public void deleteHoja(int position) {
		ContentResolver cr = getActivity().getContentResolver();
		
		Cursor cursor = adapter.getCursor();
		
		int colId = cursor.getColumnIndex(MySQLiteHelper.COLUMN_ID); //id
		long id = cursor.getLong(colId); 
		
		Uri uri = ContentUris.withAppendedId(AlbumContentProvider.CONTENT_URI,id);
		
		cr.delete(uri, null, null);
		
		getActivity().getLoaderManager().restartLoader(0, null, this);
	}
	
	//------------------------------ BLOQUE EDITAR HOJA
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		index = position;
		itemId = id;
		if (isResumed()) {
			Cursor cursor = (Cursor) adapter.getItem(index);
			selectedHoja = Hoja.cursorToHoja(cursor);
			
			/*cojo los datos de la bbdd para pasarselos al fragment para rellenar así como el id para actualizar*/
			int colId = cursor.getColumnIndex(MySQLiteHelper.COLUMN_ID);
			String imagen = selectedHoja.getImagen();
			long ident = cursor.getLong(colId);
			String titulo = selectedHoja.getTitle();
			String comentario = selectedHoja.getComentario();
			String lugar = selectedHoja.getLoc();
			Float peso = selectedHoja.getPeso();
			Float longitud = selectedHoja.getLongitud();
			String cebo = selectedHoja.getCebo();
			float c_long = selectedHoja.getC_Long();
			float c_lat = selectedHoja.getC_Lat();
				
			FragmentManager fm = getFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
    		
			nuevo_fragmento nuevo = new nuevo_fragmento();
    		
			Bundle parametro = new Bundle();

			parametro.putString("tituloenv",titulo);
			parametro.putString("comentarioenv",comentario);
			parametro.putLong("numfila",ident);
			parametro.putString("imagenfila",imagen);
			parametro.putString("lugarenv",lugar);
			parametro.putFloat("pesoenv",peso);
			parametro.putFloat("longitudenv",longitud);
			parametro.putString("ceboenv",cebo);
			parametro.putFloat("c_longenv",c_long);
			parametro.putFloat("c_latenv",c_lat);

			nuevo.setArguments(parametro);
    		
			FragmentTransaction transaction = getFragmentManager().beginTransaction();

				// Replace whatever is in the fragment_container view with this fragment,
				// and add the transaction to the back stack so the user can navigate back
			transaction.replace(R.id.listPlace, nuevo);
			transaction.addToBackStack(null);

				// Commit the transaction
			transaction.commit();		
			ft.commit();
				
			//showEditDialog();
    		selectedHoja = null;
    		getActivity().getLoaderManager().restartLoader(0, null, this); 
		}
	}
	
	
	
	//------------------------------ BLOQUE DIALOGO EDITOR/CREADOR HOJA	
	//viejo
	
	public void onFinishEditDialog(boolean result, String editedName, String editedComentario) {
		// Inserta o actualiza una Hoja
		Uri uri;
		if (result) {
			if (selectedHoja == null) { // adding
				ContentResolver cr = getActivity().getContentResolver();
				
				//creamos los valores
				ContentValues values = new ContentValues();
				values.put(MySQLiteHelper.COLUMN_TITLE, editedName);
				values.put(MySQLiteHelper.COLUMN_COMMENT, editedComentario);

				cr.insert(AlbumContentProvider.CONTENT_URI, values);
				getActivity().getLoaderManager().restartLoader(0, null, this);
			} 
			else { // editing
				selectedHoja.setTitle(editedName);
				selectedHoja.setComentario(editedComentario);
				mCurFilter = null;

				ContentResolver cr = getActivity().getContentResolver();
				Cursor cursor = adapter.getCursor();
				int colId = cursor.getColumnIndex(MySQLiteHelper.COLUMN_ID);
				long id = cursor.getLong(colId);
				uri = ContentUris.withAppendedId(AlbumContentProvider.CONTENT_URI, id);

				ContentValues values = new ContentValues();
				values.put(MySQLiteHelper.COLUMN_TITLE, editedName);
				values.put(MySQLiteHelper.COLUMN_COMMENT, editedComentario);

				cr.update(uri, values, null, null);
				getActivity().getLoaderManager().restartLoader(0, null, this); //this es es listener
			}
		}
		selectedHoja = null;
	}
	
	
	
	//------------------------------ BLOQUE BUSQUEDA Y FILTRADO NOMBRE HOJA
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Place an action bar item for searching.
		final MenuItem item = menu.findItem(R.id.menu_search);
		final MenuItem itemAnadir = menu.findItem(R.id.menu_anadir);
		final MenuItem itemEnable = menu.findItem(R.id.menu_enable);
		
		itemAnadir.setVisible(false);
		itemEnable.setVisible(false);

		mSearchView = (SearchView) item.getActionView();
		mSearchView.setOnQueryTextListener(this);
		// mSearchView.setOnCloseListener(this); // no funciona en >= 3.0
		item.setOnActionExpandListener(new OnActionExpandListener() {

			@Override
			public boolean onMenuItemActionCollapse(MenuItem item) {
				mSearchView.setQuery(null, true);
				return true; // Return true to collapse action view
			}

			@Override
			public boolean onMenuItemActionExpand(MenuItem item) {
				// TODO Auto-generated method stub
				return true;
			}
		});
	}
	
	//la forma de hablar con la base de datos cambia, se hace mediante el loader
	@Override
	public boolean onQueryTextChange(String newText) {
		String newFilter = !TextUtils.isEmpty(newText) ? newText : null;//cuando escribimos en bara de busqueda
		
		if (mCurFilter == null && newFilter == null) {
			return true;
		}
		if (mCurFilter != null && mCurFilter.equals(newFilter)) {
			return true;
		}
		
		mCurFilter = newFilter;
		getLoaderManager().restartLoader(0, null, this);//recarga lista
		//el resto de veces se hace con restartLoader
		return true;
	}
	
	
	@Override
	public boolean onQueryTextSubmit(String query) {
		// Don't care about this.
		return true;
	}

	
	//---------------------------ACTUACION DEL LoaderCallbacks<Cursor>
	

	//Todo esto lo hace cada vez que llamamos por getActivity().getLoaderManager().restartLoader(0, null, this);
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {

		String select = null;
		if (mCurFilter != null && !mCurFilter.trim().equals("")) { //filtro de busqueda
			select = MySQLiteHelper.COLUMN_TITLE + " LIKE " + '"'+ mCurFilter + '%' + '"'; // % es necesario para que funcione like
		}
		return new CursorLoader(getActivity(),
				AlbumContentProvider.CONTENT_URI, AlbumContentProvider.PROJECTION, select, null, AlbumContentProvider.TITLES_SORT_ORDER);
	}

	//Cuando termina de recorrer la base de datos
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		// Save the new hoja to the database
		adapter.swapCursor(data);//quita el viejo cursor y pone el nuevo
		adapter.notifyDataSetChanged();//actualizamos la lista
	}

	//Si se hace reset y se recarga la lista
	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		adapter.swapCursor(null);//se limpia el cursor
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getActivity().getLoaderManager().restartLoader(0, null, this);
	}
	
	public void actualizar(){
		getActivity().getLoaderManager().restartLoader(0, null, this);
	}
	
}
