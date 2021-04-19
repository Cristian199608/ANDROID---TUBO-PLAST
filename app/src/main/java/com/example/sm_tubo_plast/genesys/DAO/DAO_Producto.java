package com.example.sm_tubo_plast.genesys.DAO;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.sm_tubo_plast.genesys.BEAN.Producto;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;

public class DAO_Producto extends SQLiteAssetHelper {
	public static final String TAG = "DAO_Producto";
    public static final String DATABASE_NAME = "fuerzaventas";
    private static final int DATABASE_VERSION = 1;
    Context context;

    public DAO_Producto(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }
    
    public ArrayList<Producto> getAllProducts(){
    	String rawQuery = "SELECT _id, codpro, despro, cod_rapido,ean13 FROM producto";
		Log.i(TAG, rawQuery);

		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(rawQuery, null);
		ArrayList<Producto> lista = new ArrayList<>();
		cursor.moveToFirst();
		if (cursor.moveToFirst()) {
			do {
				Producto producto = new Producto();
				producto = new Producto();
				producto.setCodigo(cursor.getString(1));
				producto.setDescripcion(cursor.getString(2));		
				lista.add(producto);
			} while (cursor.moveToNext());

		}		
		cursor.close();
		db.close();
		return lista;
    }
    
    public Producto getInformacionProducto(String codigoProducto){
    	String rawQuery = 
    			"SELECT codpro,despro,desunimed, ifnull(descripcion,''), ifnull(color,'') "+
    			"FROM producto p "+
    			"INNER JOIN unidad_medida u on p.codunimed = u.codunimed "+
    			"LEFT JOIN tipoProducto tp on p.tipoProducto = tp.codigoTipo "+
    			"WHERE codpro like '"+codigoProducto+"'";
		Log.i(TAG, rawQuery);

		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(rawQuery, null);
		Producto producto = null;
		cursor.moveToFirst();
		if (cursor.moveToFirst()) {
			do {
				producto = new Producto();
				producto.setCodigo(cursor.getString(0));
				producto.setDescripcion(cursor.getString(1));
				producto.setUnidadMedida(cursor.getString(2));
				producto.setTipoProducto(cursor.getString(3));
				producto.setColor(cursor.getString(4));				
			} while (cursor.moveToNext());

		}		
		cursor.close();
		db.close();
		return producto;
    }
    
    public boolean disableDescuento(String codigoProdicto){
    	String rawQuery = "SELECT * FROM productoNoDescuento WHERE codigoProducto='"+codigoProdicto+"'";
    	SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(rawQuery, null);
		boolean flag = false;
		cursor.moveToFirst();
		if (cursor.getCount()>=1) {
			flag = true;
		}
		cursor.close();
		db.close();
		Log.i(TAG, "disableDescuento "+codigoProdicto+" "+flag);
		return flag;    	
    }
}
