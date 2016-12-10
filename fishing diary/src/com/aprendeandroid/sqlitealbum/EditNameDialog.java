package com.aprendeandroid.sqlitealbum;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class EditNameDialog extends DialogFragment {

//    private EditText mEditText;
    EditNameDialogListener listener;

    public EditNameDialog() {
        // Empty constructor required for DialogFragment
    }
    
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
//        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View layout = inflater.inflate(R.layout.fragment_edit_name, (ViewGroup) getActivity().findViewById(R.id.root));
        final EditText mEditText = (EditText) layout.findViewById(R.id.txt_your_name);
        final EditText mEditComentario = (EditText) layout.findViewById(R.id.txt_your_comentario);
        //final ImageView imagen = (ImageView) layout.findViewById(R.id.imagen_fila);
        mEditText.setText(listener.getNameToEdit());
        mEditComentario.setText(listener.getComentarioToEdit());
        
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(layout);
        // Now configure the AlertDialog
        builder.setTitle("Edit title");
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // We forcefully dismiss and remove the Dialog, so it
                // cannot be used again (no cached info)
            	listener.onFinishEditDialog(false, null, null);
            	EditNameDialog.this.getDialog().cancel();
            }
        });
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	String input = mEditText.getText().toString().trim();
            	String comment = mEditComentario.getText().toString().trim();
            	if(!input.equals(""))
            		listener.onFinishEditDialog(true, input, comment);
            	
                EditNameDialog.this.getDialog().cancel();
            }
        });
        // Create the AlertDialog and return it
        AlertDialog passwordDialog = builder.create();
        return passwordDialog;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			listener = (EditNameDialogListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement EditNameDialogListener in Activity");
		}
	}
    
    public static interface EditNameDialogListener { 
    	public void onFinishEditDialog(boolean result, String editedName, String editedComentario);
    	public String getNameToEdit();
    	public String getComentarioToEdit();
    }
    
    public void enableDisable(){
    	
    }
    
}