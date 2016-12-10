package com.aprendeandroid.sqlitealbum;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class InfoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);
		
		InputStream iFile = getResources().openRawResource(R.raw.datos);

        try {
            TextView helpText = (TextView) findViewById(R.id.TextView_HelpText);
            
            String strFile = inputStreamToString(iFile);
            helpText.setText(strFile);
        } 
        catch (Exception e) {

        }
	}
	
		
	// Leemos el fichero de texto
	private String inputStreamToString(InputStream is) throws IOException {
	        StringBuffer sBuffer = new StringBuffer();

	        BufferedReader dataIO = new BufferedReader(new InputStreamReader(is));        
	        String strLine = null;

	        while ((strLine = dataIO.readLine()) != null) {
	            sBuffer.append(strLine + "\n");
	        }

	        dataIO.close();
	        is.close();

	        return sBuffer.toString();
	    }

}
