package com.aprendeandroid.sqlitealbum;

import android.database.Cursor;

/*Comentario genérico*/

public class Hoja {

	private long id;
	private String title;
	private long lastTime;
	private String comentario;
	private String imagen;
	private String loc;
	private float peso;
	private float longitud;
	private String cebo;
	private float c_long;
	private float c_lat;

	public Hoja(long id, long lastTime, String comment, String comentario, String imagen,
			String loc, float peso, float longitud, String cebo, float c_long, float c_lat) {
		this.id = id;
		this.title = comment;
		this.lastTime = lastTime;
		this.comentario = comentario;
		this.imagen = imagen;
		this.loc = loc;
		this.peso = peso;
		this.longitud = longitud;
		this.cebo = cebo;
		this.c_long = c_long;
		this.c_lat = c_lat;
	}

	public long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}
	
	public long getLastTime() {
		return lastTime;
	}
	
	public String getComentario() {
		return comentario;
	}
	
	public String getImagen() {
		return imagen;
	}
	
	public String getLoc() {
		return loc;
	}
	
	public float getPeso() {
		return peso;
	}
	
	public float getLongitud() {
		return longitud;
	}
	
	public String getCebo() {
		return cebo;
	}
	
	public float getC_Long() {
		return c_long;
	}
	
	public float getC_Lat() {
		return c_lat;
	}

	public void setTitle(String comment) {
		this.title = comment;
	}
	
	public void setLastTime(long lastTime) {
		this.lastTime = lastTime;
	}
	
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	
	public void setImagen(String imagen) {
		this.imagen = imagen;
	}
	
	public void setLoc(String loc) {
		this.loc = loc;
	}
	
	public void setPeso(long peso) {
		this.peso = peso;
	}
	
	public void setLongitud(long longitud) {
		this.longitud = longitud;
	}
	
	public void setCebo(String cebo) {
		this.cebo = cebo;
	}
	
	public void setC_Long(float c_long) {
		this.c_long = c_long;
	}
	
	public void setC_Lat(float c_lat) {
		this.c_lat = c_lat;
	}

	public static Hoja cursorToHoja(Cursor cursor) {
		Hoja hoja = new Hoja(cursor.getLong(0), cursor.getLong(1),
				cursor.getString(2), cursor.getString(3), cursor.getString(4),
				cursor.getString(5),cursor.getFloat(6),cursor.getFloat(7),cursor.getString(8),
				cursor.getFloat(9),cursor.getFloat(10));
		return hoja;
	}
}
