package com.example.sm_tubo_plast.genesys.DAO;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.sm_tubo_plast.genesys.BEAN.Producto;
import com.example.sm_tubo_plast.genesys.datatypes.DBMta_Kardex;
import com.example.sm_tubo_plast.genesys.datatypes.DBtables;
import com.example.sm_tubo_plast.genesys.util.VARIABLES;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;

public class DAO_Producto extends SQLiteAssetHelper {
	public static final String TAG = "DAO_Producto";
    Context context;

    public DAO_Producto(Context context) {
        super(context, VARIABLES.ConfigDatabase.getDatabaseName(), null, VARIABLES.ConfigDatabase.getDatabaseVersion());
        this.context = context;
    }
    
    @SuppressLint("Range")
	public ArrayList<Producto> getAllProducts(){
    	String rawQuery = "SELECT p._id, p.codpro, p.despro, p.cod_rapido,p.ean13, " +
				"p.desc_comercial, " +
				"p._precio_base as precio_base, " +
				"p.codunimed_almacen, "
				+ "ifnull(mta.stock,0) as stock,"
				+ "ifnull(mta.xtemp,0) as xtemp,"
				+ "ifnull(mta.transito,0) as transito,"
				+ "ifnull(mta.disponible,0) as disponible "+
				"FROM producto p " +
				"left join mta_kardex mta on mta.codpro = p.codpro "+
				"order by p.despro asc";
		Log.i(TAG, rawQuery);

		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(rawQuery, null);
		ArrayList<Producto> lista = new ArrayList<>();
		cursor.moveToFirst();
		if (cursor.moveToFirst()) {
			do {
				Producto producto  = new Producto();
				producto.setCodigo(cursor.getString(1));
				producto.setDescripcion(cursor.getString(2));		
				producto.setDesc_comercial(cursor.getString(cursor.getColumnIndex("desc_comercial")));
				producto.setPrecio_base(cursor.getDouble(cursor.getColumnIndex("precio_base")));
				producto.setUnidadMedida(cursor.getString(cursor.getColumnIndex("codunimed_almacen")));

				DBMta_Kardex stockDet= new DBMta_Kardex();
				stockDet.setCodpro(producto.getCodigo());
				stockDet.setStock(cursor.getInt(cursor.getColumnIndex("stock")));
				stockDet.setXtemp(cursor.getInt(cursor.getColumnIndex("xtemp")));
				stockDet.setTransito(cursor.getInt(cursor.getColumnIndex("transito")));
				stockDet.setDisponible(cursor.getInt(cursor.getColumnIndex("disponible")));
				producto.setStockDetalle(stockDet);

				lista.add(producto);
			} while (cursor.moveToNext());

		}		
		cursor.close();
		db.close();
		return lista;
    }
    
    public Producto getInformacionProducto(String codigoProducto){
    	String rawQuery = 
    			"SELECT p.codpro," +
						"p.despro," +
						"p.codunimed as desunimed, " +
						"ifnull(null,''), " +
						"ifnull('',''), " +
						"p._precio_base, p.peso "+
    			"FROM "+ DBtables.Producto.TAG +" p "+
    			//"INNER JOIN unidad_medida u on p.codunimed = u.codunimed "+
    			//"LEFT JOIN tipoProducto tp on p.tipoProducto = tp.codigoTipo "+
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
				producto.setPrecio_base(cursor.getDouble(cursor.getColumnIndex("_precio_base")));
				producto.setPeso(cursor.getDouble(cursor.getColumnIndex("peso")));
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
