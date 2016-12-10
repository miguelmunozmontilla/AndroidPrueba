package com.aprendeandroid.sqlitealbum;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends FragmentActivity {

	private GoogleMap mapa = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		
		Bundle bundle = getIntent().getExtras();
		
		mapa = ((SupportMapFragment) getSupportFragmentManager()
				   .findFragmentById(R.id.mapLayout)).getMap();
		
		CameraUpdate camUpd2 = CameraUpdateFactory.newLatLngZoom(new LatLng(bundle.getFloat("c_latitud"), bundle.getFloat("c_longitud")), 16F);
		mapa.animateCamera(camUpd2);
			
		mapa.addMarker(new MarkerOptions().position(new LatLng(bundle.getFloat("c_latitud"), bundle.getFloat("c_longitud"))).title("Captura"));
	}

	
}
