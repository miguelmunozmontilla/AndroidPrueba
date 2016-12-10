package com.aprendeandroid.sqlitealbum;

import com.aprendeandroid.sqlitealbum.R;

import android.content.Context;
import android.database.Cursor;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class MyCursorAdapter extends SimpleCursorAdapter {
	public MyCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to, int flags) {
		super(context, layout, c, from, to, flags);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		TextView titulo = (TextView) view.findViewById(R.id.txtTitle);
		titulo.setText(cursor.getString(cursor
				.getColumnIndex(MySQLiteHelper.COLUMN_TITLE)));
		//si queremos cambiar en vez de comentario que salga el lugar ser�a aqu�
		TextView comentario = (TextView) view.findViewById(R.id.txtComentario);
		comentario.setText(cursor.getString(cursor
				.getColumnIndex(MySQLiteHelper.COLUMN_COMMENT)));
		
		
		TextView modificacionDate = (TextView) view.findViewById(R.id.txtDate);
		TextView modificacionTime = (TextView) view.findViewById(R.id.txtTime);
		String date = DateFormat.format("dd/MM/yyyy kk:mm:ss", cursor.getLong(cursor
				.getColumnIndex(MySQLiteHelper.COLUMN_LAST_TIME))).toString();
		modificacionDate.setText(date.substring(0, date.indexOf(" ")));
		modificacionTime.setText(date.substring(date.indexOf(" ") + 1));
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.row, parent, false);
		bindView(v, context, cursor);
		return v;
	}

}
