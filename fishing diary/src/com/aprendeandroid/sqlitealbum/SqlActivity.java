package com.aprendeandroid.sqlitealbum;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.aprendeandroid.sqlitealbum.CustomListFragment.ListItemSelectedListener;


public class SqlActivity extends Activity implements ListItemSelectedListener{
		
	private DBListFragment listFrag;
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sql);

		createListFragment();	
			
	}

	private void createListFragment() {
		listFrag = new DBListFragment();
		
		Bundle parametros = new Bundle();
		
		parametros.putInt("listLayoutId", R.layout.list_fragment);
		parametros.putInt("emptyViewId", android.R.id.empty);
		
		listFrag.setArguments(parametros);
		
		FragmentManager fm = getFragmentManager();
		/**
		 * Starting a fragment transaction to dynamically add fragment to the
		 * application
		 */
		FragmentTransaction ft = fm.beginTransaction();

		/** Adding fragment to the fragment transaction */
		ft.add(R.id.listPlace, listFrag, "LIST");
		
		
		/** The contact dialog fragment is effectively added and opened */
		ft.commit();

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    getMenuInflater().inflate(R.menu.activity_menu, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.menu_add:
	        	FragmentManager fm = getFragmentManager();
	    		FragmentTransaction ft = fm.beginTransaction();
	    		
	    		nuevo_fragmento nuevo = new nuevo_fragmento();
	    		
	            FragmentTransaction transaction = getFragmentManager().beginTransaction();

	            // Replace whatever is in the fragment_container view with this fragment,
	            // and add the transaction to the back stack so the user can navigate back
	            transaction.replace(R.id.listPlace, nuevo);
	            transaction.addToBackStack(null);

	            // Commit the transaction
	            transaction.commit();		
	    		ft.commit();
	    		
	    		listFrag.addHoja();
	            return true;
	        
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
	                                ContextMenuInfo menuInfo) {
	    super.onCreateContextMenu(menu, v, menuInfo);
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.context_menu, menu);
	}	

	public boolean onContextItemSelected(MenuItem item) {
	    AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	    switch (item.getItemId()) {
	        case R.id.menu_delete:
	        	listFrag.deleteHoja(info.position);
	            return true;
	        default:
	            return super.onContextItemSelected(item);
	    }
	}	

	
	
	@Override
	public void onListItemSelected(int index, String tag, long id) {
		// TODO Auto-generated method stub
		
	}


}
