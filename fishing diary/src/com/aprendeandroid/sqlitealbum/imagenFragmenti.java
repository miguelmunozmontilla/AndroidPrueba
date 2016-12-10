package com.aprendeandroid.sqlitealbum;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class imagenFragmenti extends Fragment{
	
	private ImageView botonImagen = null;
	String imagenRecibido;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      
		if(savedInstanceState != null)  {
		}
		
		Bundle parametros = getArguments(); //Aqui vienen los parametro de este Layout
		if(parametros != null) {
			imagenRecibido= getArguments().getString("media");
		}
		
		
		 
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
    	View myFragmentView = inflater.inflate(R.layout.imagen_mostrar, container, false);
    	return myFragmentView;
	}

	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		
		Bitmap cameraPic = scaleBitmap(imagenRecibido, 500);
		botonImagen = (ImageView) getView().findViewById(R.id.imagencompleta);
		//botonImagen.setImageBitmap(BitmapFactory.decodeFile(imagenRecibido)); 
		botonImagen.setImageBitmap(cameraPic); 
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
}
