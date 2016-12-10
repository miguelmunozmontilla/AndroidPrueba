package com.aprendeandroid.sqlitealbum;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MenuActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu2);
		
		final Button btnCapturas = (Button)findViewById(R.id.btnCapturas);
		final Button btnInfo = (Button)findViewById(R.id.btnInfo);
		final Button btnMap = (Button)findViewById(R.id.btnMap);
        
	       //Implementamos el evento “click” del botón
	        btnCapturas.setOnClickListener(new OnClickListener() {
	             @Override
	             public void onClick(View v) {
	                  //Creamos el Intent
	                  Intent intent = new Intent(MenuActivity.this, SqlActivity.class);
	                  
	                  //Iniciamos la nueva actividad
	                  startActivity(intent);
	             }
	        });
	        
	      //Implementamos el evento “click” del botón
	        btnInfo.setOnClickListener(new OnClickListener() {
	             @Override
	             public void onClick(View v) {
	                  //Creamos el Intent
	                  Intent intent = new Intent(MenuActivity.this, InfoActivity.class);
	                  
	                  //Iniciamos la nueva actividad
	                  startActivity(intent);
	             }
	        });
	        
	        btnMap.setOnClickListener(new OnClickListener() {
	             @Override
	             public void onClick(View v) {
	                  //Creamos el Intent
	            	 Intent intent = new Intent(MenuActivity.this, MapActivity.class);
	            	 intent.putExtra("c_longitud", -3.69);
	 				 intent.putExtra("c_latitud", 40.41);
	                  //Iniciamos la nueva actividad
	                  startActivity(intent);            	 
	            	 
	             }
	        });
	}

	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		
		
		return super.onOptionsItemSelected(item);
	}
}
