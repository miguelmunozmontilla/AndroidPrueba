package com.aprendeandroid.sqlitealbum;

import java.io.File;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class nuevo_fragmento extends Fragment{
	
	private int enable=0;
	int nueva=0;
	int calculogps=0;
	
	private LocationManager locManager;
	private LocationListener locListener;
	private EditText lblLatitud = null; 
	private EditText lblLongitud = null;
	
	String tituloRecibido;	
	String comentarioRecibido;
	String lugarRecibido;
	Float pesoRecibido;
	Float longitudRecibido;
	Long numFila;
	String ceboRecibido;
	float c_longRecibido;
	float c_latRecibido;
	Uri uri;
	String uriImagen=null;
	String Imagenrecibida;
	private String image_path = null;

	final int THUMBSIZE = 256;
	private EditText mEditText = null;
	private EditText mEditComentario = null;
	private ImageView botonImagen = null;
	private EditText mEditLugar = null;
	private EditText mEditPeso = null;
	private EditText mEditLongitud = null;
	private EditText mEditCebo = null;
	private Button botonGaleria = null;
	private Button botonNueva = null;
	private Button botonGps = null;
	private Button botonGpsFin = null;
	private Button botonMapa = null;
	private LinearLayout coordLayout = null;
	
	private final int RC_HACER_FOTO = 1;
	private final int SELECT_PHOTO = 2;
	
	MenuItem itemAnadir;
	MenuItem itemNueva;
	MenuItem itemBuscar;
	
	long numero;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      
		if(savedInstanceState != null)  {
		}
		
		Bundle parametros = getArguments(); //Aqui vienen los parametro de este Layout
		if(parametros != null) {
			nueva=1;
			tituloRecibido= getArguments().getString("tituloenv");
			comentarioRecibido= getArguments().getString("comentarioenv");
			numFila= getArguments().getLong("numfila");
			uriImagen = getArguments().getString("imagenfila");
			lugarRecibido = getArguments().getString("lugarenv");
			pesoRecibido = getArguments().getFloat("pesoenv");
			longitudRecibido = getArguments().getFloat("longitudenv");
			ceboRecibido = getArguments().getString("ceboenv");
			c_longRecibido = getArguments().getFloat("c_longenv");
			c_latRecibido = getArguments().getFloat("c_latenv");
		}
		
		setHasOptionsMenu(true);
		
		 
    }
	
	/*para añadir twopane basta con hacer una funcion para añadir nuevo o modificar cogiendo el codigo a partir del 
	  onclick, para así poder llamarlo directamnte desde sqlactivity mediante nuevo_twopane.nombrefuncion()*/
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
    	View myFragmentView = inflater.inflate(R.layout.fragment_edit_name, container, false);   	 	
    	return myFragmentView;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		ImageView view;
		Bitmap galleryPic = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(uriImagen), 
                THUMBSIZE, THUMBSIZE);
		view = (ImageView) getView().findViewById(R.id.imagen_fila);
		
		if (uriImagen != null) {
			view.setImageBitmap(galleryPic);
			//view.setImageURI(imageUri);
		} 
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		mEditText = (EditText) getView().findViewById(R.id.txt_your_name);
		mEditComentario = (EditText) getView().findViewById(R.id.txt_your_comentario);
		botonImagen = (ImageView) getView().findViewById(R.id.imagen_fila);
		botonGaleria = (Button) getView().findViewById(R.id.galeria_foto);
		botonNueva = (Button) getView().findViewById(R.id.nueva_foto);
		mEditLugar = (EditText) getView().findViewById(R.id.txt_your_place);
		mEditPeso = (EditText) getView().findViewById(R.id.txt_peso);
 	    mEditLongitud = (EditText) getView().findViewById(R.id.txt_longitud);
 	    mEditCebo = (EditText) getView().findViewById(R.id.txt_cebo);
 	    lblLatitud = (EditText) getView().findViewById(R.id.txt_c_lat); 
 		lblLongitud = (EditText) getView().findViewById(R.id.txt_c_long);
 		botonGps = (Button) getView().findViewById(R.id.gps_calcular);
 		botonGpsFin = (Button) getView().findViewById(R.id.gps_finalizar);
 		botonMapa = (Button) getView().findViewById(R.id.botonMapa);
    	coordLayout = (LinearLayout) getView().findViewById(R.id.coordenadas_Layout);
 		
		//por si ponemos botón
		//Button b = (Button) getView().findViewById(R.id.aceptarbtn);
		
    	if(nueva==0){    		
        
    		/*b.setOnClickListener(new View.OnClickListener()
   	 		{
   	             @Override
   	             public void onClick(View myFragmentView)
   	             {
   	     			String name=mEditText.getText().toString();
	            	String comment=mEditComentario.getText().toString();
   	     			
   	            	if (mEditText.getText().length()==0){
   	     				Toast toast = Toast.makeText(getActivity(), "Debes introducir un título",
   	     						Toast.LENGTH_LONG);
   	     				toast.show();
   	     			}
   	     			else{
   	     				ContentResolver cr = getActivity().getContentResolver();
   	     			
   					//creamos los valores
   	     				ContentValues values = new ContentValues();
   						values.put(MySQLiteHelper.COLUMN_TITLE, name);
   						values.put(MySQLiteHelper.COLUMN_COMMENT, comment);
   						values.put(MySQLiteHelper.COLUMN_IMAGE, uriImagen);

   						cr.insert(AlbumContentProvider.CONTENT_URI, values);
   						getFragmentManager().popBackStackImmediate();
   						
   	     			}
   	            		
   	             } 
   	 		}); */
   	 	}
    	else{
    		mEditText.setText(tituloRecibido);
        	mEditComentario.setText(comentarioRecibido);
        	mEditLugar.setText(lugarRecibido);
        	mEditCebo.setText(ceboRecibido);
        	if(pesoRecibido!=0)
        		mEditPeso.setText("" + pesoRecibido);
        	if(longitudRecibido!=0)
        		mEditLongitud.setText("" + longitudRecibido);
        	if(c_longRecibido!=0)
        		lblLongitud.setText("" + c_longRecibido);
        	if(c_latRecibido!=0)
        		lblLatitud.setText("" + c_latRecibido);
        	
        	if(c_longRecibido!=0 && c_latRecibido!=0){
        		coordLayout.setVisibility(View.VISIBLE);
        		botonMapa.setVisibility(View.VISIBLE);
        	}
        	
        	if (uriImagen!=null){
        		//botonImagen.setImageURI(Uri.parse(uriImagen));
        		botonImagen.setImageBitmap(BitmapFactory.decodeFile(uriImagen));
        	}
        	
        	//por si ponemos botón
        	/*b.setOnClickListener(new View.OnClickListener()
   	 		{
   	             @Override
   	             public void onClick(View myFragmentView)
   	             {	/*Con botón aceptar, se hace por menú ahora*/
   	            	 /*
   	     			String name=mEditText.getText().toString();
	            	String comment=mEditComentario.getText().toString();
   	     			
   	            	if (mEditText.getText().length()==0){
   	     				Toast toast = Toast.makeText(getActivity(), "Debes introducir un título",
   	     						Toast.LENGTH_LONG);
   	     				toast.show();
   	     			}
   	     			else{
   	     			uri = ContentUris.withAppendedId(AlbumContentProvider.CONTENT_URI, numFila);
   	     			ContentResolver cr = getActivity().getContentResolver();
   	     			
   					ContentValues values = new ContentValues();
   					values.put(MySQLiteHelper.COLUMN_TITLE, name);
   					values.put(MySQLiteHelper.COLUMN_COMMENT, comment);
   					values.put(MySQLiteHelper.COLUMN_IMAGE, uriImagen);

   					cr.update(uri, values, null, null);
   					
   	            	getFragmentManager().popBackStackImmediate();
   					//this es es listener
   					
   	     			}
   	             } 
   	 		}); */
    	}
    	
    	botonGps.setOnClickListener(new View.OnClickListener()
 		{
    		@Override
            public void onClick(View myFragmentView)
            {
    			coordLayout.setVisibility(View.VISIBLE);
    			calculogps=1;
    			comenzarLocalizacion();
            }
 		});
    	
    	botonGpsFin.setOnClickListener(new View.OnClickListener()
 		{
    		@Override
            public void onClick(View myFragmentView)
            {
    			coordLayout.setVisibility(View.GONE);
    			locManager.removeUpdates(locListener);
            }
 		});
    	
    	/*Pulsar para subir imagen de galería*/
    	botonGaleria.setOnClickListener(new View.OnClickListener()
	 		{
    		
    		 @Override
	             public void onClick(View myFragmentView)
	             {
    			 	Intent i;
    				i = new Intent(Intent.ACTION_PICK);
    				i.setType("image/*");
    				startActivityForResult(i, SELECT_PHOTO);
    				
	             }
    		
	 		});
    	
    	botonMapa.setOnClickListener(new View.OnClickListener()
 		{
		
		 @Override
             public void onClick(View myFragmentView)
             {
				Intent i = new Intent(getActivity(), MapActivity.class);
				i.putExtra("c_longitud", c_longRecibido);
				i.putExtra("c_latitud", c_latRecibido);
				getActivity().startActivity(i); 
             }
		
 		});
    	
    	/*Pulsar para subir imagen desde la cámara*/
    	botonNueva.setOnClickListener(new View.OnClickListener()
	 		{
    		
    		 @Override
	             public void onClick(View myFragmentView)
	             {
    			 	Intent i;
    				i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    				image_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tmp_image.jpg";
    				File tmpFile = new File(image_path); //Creamos el objeto File que representa el fichero temporal
    				
    				Uri uri = Uri.fromFile(tmpFile); //pasamos el path del fichero a URI
    				i.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri); //y se la pasamos al Intent para lanzar la camara
    				// fin para high resolution

    				startActivityForResult(i, RC_HACER_FOTO);
    				
	             }
    		
	 		});

    	/*Pulsar para ver imagen pantalla completa*/
    	botonImagen.setOnClickListener(new View.OnClickListener()
	 		{
    		
    		 @Override
	             public void onClick(View myFragmentView)
	             {
    			 	FragmentManager fm = getFragmentManager();
    				FragmentTransaction ft = fm.beginTransaction();
    	    		
    				imagenFragmenti nuevo = new imagenFragmenti();
    	    		
    				Bundle parametro = new Bundle();

    				parametro.putString("media",uriImagen);

    				nuevo.setArguments(parametro);
    	    		
    				FragmentTransaction transaction = getFragmentManager().beginTransaction();

    					// Replace whatever is in the fragment_container view with this fragment,
    					// and add the transaction to the back stack so the user can navigate back
    				transaction.replace(R.id.listPlace, nuevo);
    				transaction.addToBackStack(null);

    					// Commit the transaction
    				transaction.commit();		
    				ft.commit();
    			 	
	             }
    		
	 		});
    	/*controlar con acabar cálculo de gps si va hacia atrás pero no lo pilla 
    	 * poner algo delante de getView que referencia al fragment?*/
    	getView().setOnKeyListener( new OnKeyListener()
    	{
    	    @Override
    	    public boolean onKey( View v, int keyCode, KeyEvent event )
    	    {
    	        if( keyCode == KeyEvent.KEYCODE_BACK )
    	        {
    	        	if(calculogps==1)
    	        		finalizarGps();
    	        }
    	        return false;
    	    }
    	} );
    	
	}
	
	/*-----------Bloque tratamiento imagen--------------*/
	
	@SuppressWarnings("static-access")
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Si retorna un resultado correcto.
		
		// Dependiendo de la actividad llamada que ha retornado el resultado.
		if(requestCode==SELECT_PHOTO) {
			ImageView view;
			Uri imageUri=null;
			
			if (resultCode == getActivity().RESULT_OK) {
				// Media.getBitmap(this.getContentResolver(), imageUri); puede
				// dar out of memory
				view = (ImageView) getView().findViewById(R.id.imagen_fila);
				imageUri = data.getData();
				
				uriImagen = getPathFromUri(imageUri);
				//Bitmap galleryPic = scaleBitmap(getPathFromUri(imageUri), 300);
				Bitmap galleryPic = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(getPathFromUri(imageUri)), 
	                    THUMBSIZE, THUMBSIZE);
				
				if (imageUri != null) {
					view.setImageBitmap(galleryPic);
					//view.setImageURI(imageUri);
				} else {
					Toast toast = Toast.makeText(getActivity(), "Imagen fallida",
							Toast.LENGTH_LONG);
					toast.show();
				}
			}

		}
		
		if(requestCode==RC_HACER_FOTO) {
			ImageView view;
			
			if (resultCode == getActivity().RESULT_OK) {
				// Media.getBitmap(this.getContentResolver(), imageUri); puede
				// dar out of memory
				view = (ImageView) getView().findViewById(R.id.imagen_fila);
				Bitmap cameraPic = scaleBitmap(image_path, view.getHeight());
				
				uriImagen=image_path;
				//view.setImageBitmap(BitmapFactory.decodeFile(uriImagen));
				//botonImagen.setImageBitmap(BitmapFactory.decodeFile(uriImagen));
				
				if (cameraPic != null) {
					view.setImageBitmap(cameraPic);
				} else {
					Toast toast = Toast.makeText(getActivity(), "Foto fallida",
							Toast.LENGTH_LONG);
					toast.show();
				}
				
			}

		}
	}
	 
	private String getPathFromUri(Uri uri) { 
		String path = "";
		String[] projection = { MediaColumns.DATA };
		Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
		try {
			int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
			if (cursor.moveToFirst())
				path = cursor.getString(column_index);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		cursor.close();
		return path;
	}
	
	
	private Bitmap scaleBitmap(String image_path, int maxDimension) {
		Bitmap scaledBitmap;
		
		BitmapFactory.Options op = new BitmapFactory.Options();
		op.inJustDecodeBounds = true; // solo devuelve las dimensiones, no carga bitmap
		scaledBitmap = BitmapFactory.decodeFile(image_path, op); //en op est‡n las dimensiones

		// usamos Math.max porque es mejor que la imagen sea un poco mayor que el
		// control donde se muestra, que un poco menor. Ya que si es menor el control
		// la agranda para ajustarla y se podria pixelar un poco.
		if ((maxDimension < op.outHeight) || (maxDimension < op.outWidth)) {
			// cada dimensiÃ³n de la imagen se dividir por op.inSampleSize al cargar
			op.inSampleSize = Math.round(Math.max((float) op.outHeight / (float) maxDimension,(float) op.outWidth / (float) maxDimension)); //calculamos la proporcion de la escala para que no deforme la imagen y entre en las dimensiones fijadas en la vista
		}

		op.inJustDecodeBounds = false; // ponemos a false op...
		scaledBitmap = BitmapFactory.decodeFile(image_path, op); //...para que ya el bitmap se cargue realmente
		
		return scaledBitmap;
	}
	
	/*---------------------------------------------*/
	/*recoger la referencia del item añadir para que no aparezca cuando pulsamos disabled*/
	public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
	    itemAnadir = menu.findItem(R.id.menu_anadir);
	    itemNueva = menu.findItem(R.id.menu_add);
	    itemBuscar = menu.findItem(R.id.menu_search);
	    
	    itemNueva.setVisible(false);
	    itemBuscar.setVisible(false);
	}
	
	/* Opción del menú para activar/desactivar edición*/
	@Override
	public boolean onOptionsItemSelected(MenuItem item) { 
		
	    switch (item.getItemId()) {
	     
	        case R.id.menu_enable:
	        	if(enable==0){
	        		enable=1;
	        		item.setTitle("disabled");
	        		mEditText.setFocusable(false);
	        		mEditText.setEnabled(false);
	        		mEditText.setCursorVisible(false);
	        		mEditComentario.setFocusable(false);
	        		mEditComentario.setEnabled(false);
	        		mEditComentario.setCursorVisible(false);
	        		mEditLugar.setFocusable(false);
	        		mEditLugar.setEnabled(false);
	        		mEditLugar.setCursorVisible(false);
	        		mEditPeso.setFocusable(false);
	        		mEditPeso.setEnabled(false);
	        		mEditPeso.setCursorVisible(false);
	        		mEditLongitud.setFocusable(false);
	        		mEditLongitud.setEnabled(false);
	        		mEditLongitud.setCursorVisible(false);
	        		mEditCebo.setFocusable(false);
	        		mEditCebo.setEnabled(false);
	        		mEditCebo.setCursorVisible(false);
	        		botonImagen.setEnabled(false);
	        		itemAnadir.setVisible(false);
	        		}
	        	else{
	        		enable=0;
	        		item.setTitle("enabled");
	        		mEditText.setFocusableInTouchMode(true);
	        		mEditText.setEnabled(true);
	        		mEditText.setCursorVisible(true);
	        		mEditComentario.setFocusableInTouchMode(true);
	        		mEditComentario.setEnabled(true);
	        		mEditComentario.setCursorVisible(true);
	        		mEditLugar.setFocusableInTouchMode(true);
	        		mEditLugar.setEnabled(true);
	        		mEditLugar.setCursorVisible(true);
	        		mEditPeso.setFocusableInTouchMode(true);
	        		mEditPeso.setEnabled(true);
	        		mEditPeso.setCursorVisible(true);
	        		mEditLongitud.setFocusableInTouchMode(true);
	        		mEditLongitud.setEnabled(true);
	        		mEditLongitud.setCursorVisible(true);
	        		mEditCebo.setFocusableInTouchMode(true);
	        		mEditCebo.setEnabled(true);
	        		mEditCebo.setCursorVisible(true);
	        		botonImagen.setEnabled(true);
	        		itemAnadir.setVisible(true);
	        		}
	            return true;
	            
	        case R.id.menu_anadir:
	        		if(nueva==0){
	        			   mEditText = (EditText) getView().findViewById(R.id.txt_your_name);
	        	    	   mEditComentario = (EditText) getView().findViewById(R.id.txt_your_comentario);
	        	    	   botonImagen = (ImageView) getView().findViewById(R.id.imagen_fila);
	        	    	   mEditLugar = (EditText) getView().findViewById(R.id.txt_your_place);
	        	    	   mEditPeso = (EditText) getView().findViewById(R.id.txt_peso);
	        	    	   mEditLongitud = (EditText) getView().findViewById(R.id.txt_longitud);
	        	    	   mEditCebo = (EditText) getView().findViewById(R.id.txt_cebo);
	        	    	   
	        	    	   Float Peso;
	        	    	   Float Longitud;
	        	    	   float c_long;
	        	    	   float c_lat;
	        	    	   
	        	    	   String name=mEditText.getText().toString();
	        	    	   String comment=mEditComentario.getText().toString();
	        	    	   String Lugar=mEditLugar.getText().toString();
	        	    	   String Cebo=mEditCebo.getText().toString();
	        	    	   if(mEditPeso.getText().toString().length()==0)
	        	    		   Peso=(float) 0;
	        	    	   else
	        	    		   Peso=Float.valueOf(mEditPeso.getText().toString());
	        	    	   if(mEditLongitud.getText().toString().length()==0)
	        	    		   Longitud=(float) 0;
	        	    	   else
	        	    		   Longitud=Float.valueOf(mEditLongitud.getText().toString());
	        	    	   
	        	    	   if(lblLatitud.getText().toString().length()==0 || lblLatitud.getText().toString()=="calculando")
	        	    		   c_lat=(float) 0;
	        	    	   else
	        	    		   c_lat=Float.valueOf(lblLatitud.getText().toString());	        	    	   
	        	    	   if(lblLongitud.getText().toString().length()==0 || lblLongitud.getText().toString()=="calculando")
	        	    		   c_long=(float) 0;
	        	    	   else
	        	    		   c_long=Float.valueOf(lblLongitud.getText().toString());

	        	    	   if (calculogps==1)
	        	    	   finalizarGps();
	        	    	   
	        	    	   if (mEditText.getText().length()==0){
	        	    		   Toast toast = Toast.makeText(getActivity(), "Debes introducir un título",
	     						Toast.LENGTH_LONG);
	        	    		   toast.show();
	        	    	   }
	        	    	   else{
	        	    		   ContentResolver cr = getActivity().getContentResolver();
	     			
	        	    		   //creamos los valores
	        	    		   ContentValues values = new ContentValues();
	        	    		   values.put(MySQLiteHelper.COLUMN_TITLE, name);
	        	    		   values.put(MySQLiteHelper.COLUMN_COMMENT, comment);
	        	    		   values.put(MySQLiteHelper.COLUMN_IMAGE, uriImagen);
		        			   values.put(MySQLiteHelper.COLUMN_LUGAR, Lugar);
		        			   values.put(MySQLiteHelper.COLUMN_PESO, Peso);
		        			   values.put(MySQLiteHelper.COLUMN_LONGITUD, Longitud);
		        			   values.put(MySQLiteHelper.COLUMN_CEBO, Cebo);
		        			   values.put(MySQLiteHelper.COLUMN_C_LONG, c_long);
		        			   values.put(MySQLiteHelper.COLUMN_C_LAT, c_lat);

	        	    		   cr.insert(AlbumContentProvider.CONTENT_URI, values);	
	        	    		   
	        	    		   coordLayout.setVisibility(View.GONE);
	        	    		   
	        	    		   getFragmentManager().popBackStackImmediate();
	        	    	   }
	        		}
	        		else{
	        			Float Peso;
	        	    	Float Longitud;
	        	    	float c_long;
	        	    	float c_lat;
	        			
	        			String name=mEditText.getText().toString();
	        			String comment=mEditComentario.getText().toString();
	        			String Lugar=mEditLugar.getText().toString();
	        			String Cebo=mEditCebo.getText().toString();
	        			if(mEditPeso.getText().toString().length()==0)
	        	    		   Peso=(float) 0;
	        	    	   else
	        	    		   Peso=Float.valueOf(mEditPeso.getText().toString());
	        	    	   if(mEditLongitud.getText().toString().length()==0)
	        	    		   Longitud=(float) 0;
	        	    	   else
	        	    		   Longitud=Float.valueOf(mEditLongitud.getText().toString());
	        	    	   
	        	    	   if(lblLatitud.getText().toString().length()==0 || lblLatitud.getText().toString()=="calculando")
	        	    		   c_lat=(float) 0;
	        	    	   else
	        	    		   c_lat=Float.valueOf(lblLatitud.getText().toString());	        	    	   
	        	    	   if(lblLongitud.getText().toString().length()==0 || lblLongitud.getText().toString()=="calculando")
	        	    		   c_long=(float) 0;
	        	    	   else
	        	    		   c_long=Float.valueOf(lblLongitud.getText().toString());
	        	    	   if (calculogps==1)
	        	    		   finalizarGps();
	
	        			if (mEditText.getText().length()==0){
	        				Toast toast = Toast.makeText(getActivity(), "Debes introducir un título",
	        						Toast.LENGTH_LONG);
	        				toast.show();
	        			}
	        			else{
	        				uri = ContentUris.withAppendedId(AlbumContentProvider.CONTENT_URI, numFila);
	        				ContentResolver cr = getActivity().getContentResolver();
		
	        				ContentValues values = new ContentValues();
	        				values.put(MySQLiteHelper.COLUMN_TITLE, name);
	        				values.put(MySQLiteHelper.COLUMN_COMMENT, comment);
	        				values.put(MySQLiteHelper.COLUMN_IMAGE, uriImagen);
		        			values.put(MySQLiteHelper.COLUMN_LUGAR, Lugar);
		        			values.put(MySQLiteHelper.COLUMN_PESO, Peso);
		        			values.put(MySQLiteHelper.COLUMN_LONGITUD, Longitud);
		        			values.put(MySQLiteHelper.COLUMN_CEBO, Cebo);
		        			values.put(MySQLiteHelper.COLUMN_C_LONG, c_long);
		        			values.put(MySQLiteHelper.COLUMN_C_LAT, c_lat);

	        				cr.update(uri, values, null, null);
	        				
	            			coordLayout.setVisibility(View.GONE);
	        				
	        				getFragmentManager().popBackStackImmediate();
	        			}
	        		}
	        	return true;
	        	
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}


	//Coordenadas y demás

	private void comenzarLocalizacion()
	{
		//Obtenemos una referencia al LocationManager
		locManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
	
		//Obtenemos la última posición conocida
		Location loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	
		//Mostramos la última posición conocida
		mostrarPosicion(loc);
	
		//Nos registramos para recibir actualizaciones de la posición
		locListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				mostrarPosicion(location);
			}
			public void onProviderDisabled(String provider){
    		}
			public void onProviderEnabled(String provider){
			}
			public void onStatusChanged(String provider, int status, Bundle extras){
				Log.i("", "Provider Status: " + status);
			}
		};
	
		locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 0, locListener);
	}
 
	private void mostrarPosicion(Location loc) {
		if(loc != null)
		{
			lblLatitud.setText(String.valueOf(loc.getLatitude()));
			lblLongitud.setText(String.valueOf(loc.getLongitude()));
			botonMapa.setVisibility(View.VISIBLE);
			Log.i("", String.valueOf(loc.getLatitude() + " - " + String.valueOf(loc.getLongitude())));
		}
		else
		{
			lblLatitud.setText("calculando");
			lblLongitud.setText("calculando");
		}
	}
	
	/*controlar con acabar cálculo de gps si va hacia atrás
	public void onBackPressed() {
		finalizarGps();
	}*/
	
	public void finalizarGps() {
		calculogps=0;
		locManager.removeUpdates(locListener);
	}

}
