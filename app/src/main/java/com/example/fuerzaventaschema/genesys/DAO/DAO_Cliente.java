package com.example.fuerzaventaschema.genesys.DAO;

import java.util.ArrayList;


import com.example.fuerzaventaschema.genesys.BEAN.Cliente;
import com.example.fuerzaventaschema.genesys.BEAN.LugarEntrega;
import com.example.fuerzaventaschema.genesys.BEAN.Obra;
import com.example.fuerzaventaschema.genesys.BEAN.Sucursal;
import com.example.fuerzaventaschema.genesys.BEAN.Transporte;
import com.example.fuerzaventaschema.genesys.datatypes.DB_DireccionClientes;
import com.example.fuerzaventaschema.genesys.datatypes.DBtables;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

@SuppressLint("LongLogTag")
public class DAO_Cliente extends SQLiteAssetHelper {
	public static final String TAG = "DAO_Cliente";
    public static final String DATABASE_NAME = "fuerzaventas";
    private static final int DATABASE_VERSION = 1;
    Context context;

    public DAO_Cliente(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }
    
    public String[] obtenerDirecciones_cliente(String nomCliente) {
		String rawQuery;
		rawQuery = "select direccion,item from direccion_cliente inner join cliente "
				+ "on direccion_cliente.codcli=cliente.codcli where cliente.nomcli='"
				+ nomCliente + "'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(rawQuery, null);

		String[] direcciones;
		String[] items;

		if (cursor.getCount() != 0) {

			direcciones = new String[cursor.getCount()];
			items = new String[cursor.getCount()];
			cursor.moveToFirst();

			if (cursor.moveToFirst()) {
				int i = 0;
				do {
					direcciones[i] = cursor.getString(0);
					items[i] = cursor.getString(1);
					i++;
				} while (cursor.moveToNext());
			}

		} else {
			direcciones = new String[] { "sin datos" };
		}

		cursor.close();
		db.close();

		return direcciones;
	}
    
    public String getDireccionFiscal(String codigoCliente) {
		String rawQuery = "SELECT direccionFiscal FROM cliente WHERE codcli like '"+codigoCliente+"'";
		Log.i(TAG, rawQuery);

		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(rawQuery, null);
		
		String direccion = "Sin direccion";		
		cursor.moveToFirst();
		if (cursor.moveToFirst()) {
			do {
				direccion = cursor.getString(0);
			} while (cursor.moveToNext());

		}
		cursor.close();
		db.close();
		return direccion;
	}
    
    public String getLimiteCredito(String codigoCliente) {
		String rawQuery = "SELECT ifnull(limite_credito,0) FROM cliente WHERE codcli like '"+codigoCliente+"'";
		Log.i(TAG, rawQuery);

		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(rawQuery, null);
		
		String item = "0.0";		
		cursor.moveToFirst();
		if (cursor.moveToFirst()) {
			do {
				item = cursor.getString(0);
			} while (cursor.moveToNext());

		}
		cursor.close();
		db.close();
		if (item.equals("")) {
			return "0.0";
		}
		return item;
	}
    

    public ArrayList<Sucursal> getSucursales(String codigoCliente) {
		String rawQuery = "select des_corta,item,docAdicional from direccion_cliente inner join cliente "
				+ "on direccion_cliente.codcli=cliente.codcli where cliente.codcli='"+ codigoCliente + "'";
		Log.i(TAG+":getSucursales", rawQuery);

		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(rawQuery, null);
		
		ArrayList<Sucursal> lista = new ArrayList<Sucursal>();		
		cursor.moveToFirst();
		if (cursor.moveToFirst()) {
			do {
				Sucursal item = new Sucursal();
				item.setDescripcionCorta(cursor.getString(0));
				item.setItem(cursor.getString(1));
				item.setDocAdicional(cursor.getString(2));
				Log.d(TAG, "Descripcion Corta: "+cursor.getString(0)+"  "+cursor.getString(1));
				lista.add(item);
			} while (cursor.moveToNext());

		}
		cursor.close();
		db.close();
		return lista;
	}
    
    public ArrayList<LugarEntrega> getPuntoEntrega(String codigoCliente, String itemSucursal) {
		String rawQuery = "SELECT * FROM lugarEntrega WHERE codigoCliente like '"+codigoCliente+"' and itemSucursal like '"+itemSucursal+"' order by direccionEntrega";
		Log.i(TAG, rawQuery);

		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(rawQuery, null);
		
		ArrayList<LugarEntrega> lista = new ArrayList<LugarEntrega>();		
		cursor.moveToFirst();
		if (cursor.moveToFirst()) {
			do {
				LugarEntrega item = new LugarEntrega();
				item.setCodigoCliente(cursor.getString(0));
				item.setItemSucursal(cursor.getString(1));
				item.setCodigoLugar(cursor.getString(2));
				item.setDireccion(cursor.getString(3));
				item.setIndicadorDespacho(cursor.getString(4));
				item.setIndicadorCobranza(cursor.getString(5));
				item.setDireccionEntrega(cursor.getString(6));
				lista.add(item);				
				Log.d(TAG, "getPuntoEntrega add: Sucursal:"+cursor.getString(1)+" - Lugar:"+cursor.getString(2)+" - Direccion:"+cursor.getString(3));
			} while (cursor.moveToNext());

		}
		cursor.close();
		db.close();
		return lista;
	}
        
    public ArrayList<Obra> getObras(String codigoCliente) {
		String rawQuery = "SELECT * FROM obra WHERE codigoCliente like '"+codigoCliente+"' ";
		Log.i(TAG, rawQuery);

		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(rawQuery, null);
		
		ArrayList<Obra> lista = new ArrayList<Obra>();		
		cursor.moveToFirst();
		if (cursor.moveToFirst()) {
			do {
				Obra item = new Obra();
				item.setCodigoCliente(cursor.getString(0));
				item.setCodigoVendedor(cursor.getString(1));
				item.setCodigoObra(cursor.getString(2));
				item.setObra(cursor.getString(3));
				//Log.d(TAG, cursor.getString(2) +"   "+cursor.getString(3));
				lista.add(item);				
			} while (cursor.moveToNext());

		}
		Log.d(TAG, rawQuery);
		cursor.close();
		db.close();
		return lista;
	}
    
    public ArrayList<Transporte> getTransportes(String codigoCliente){
    	String rawQuery = "SELECT * FROM transporte WHERE codigoCliente like '"+codigoCliente+"' ";
		Log.i(TAG, rawQuery);

		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(rawQuery, null);
		
		ArrayList<Transporte> lista = new ArrayList<Transporte>();		
		cursor.moveToFirst();
		if (cursor.moveToFirst()) {
			do {
				Transporte item = new Transporte();
				item.setCodigoCliente(cursor.getString(0));
				item.setItemSucursal(cursor.getString(1));
				item.setCodigoTransporte(cursor.getString(2));
				item.setDescripcion(cursor.getString(3));				
				lista.add(item);				
			} while (cursor.moveToNext());

		}
		cursor.close();
		db.close();
		return lista;
    }
    
    public String getMonedaDocumento(String codigoCliente) {
		String rawQuery = "SELECT monedaDocumento FROM cliente WHERE codcli like '"+codigoCliente+"'";
		Log.i(TAG, rawQuery);

		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(rawQuery, null);
		
		String valor = "";		
		cursor.moveToFirst();
		if (cursor.moveToFirst()) {
			do {
				valor = cursor.getString(0);
			} while (cursor.moveToNext());

		}
		Log.d(TAG, "getMonedaDocumento "+valor);
		cursor.close();
		db.close();
		return valor;
	}
    
    public String getComprobante(String codigoCliente){
    	String rawQuery = "SELECT comprobante FROM cliente WHERE codcli like '"+codigoCliente+"'";
		Log.i(TAG, rawQuery);

		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(rawQuery, null);
		
		String valor = "";		
		cursor.moveToFirst();
		if (cursor.moveToFirst()) {
			do {
				valor = cursor.getString(0);
			} while (cursor.moveToNext());

		}
		Log.d(TAG, "getTipoDocumento "+valor);
		cursor.close();
		db.close();
		return valor;
    }
    
    public String getFlagMsPack(String codigoCliente){
    	String rawQuery = "SELECT flagMsPack FROM cliente WHERE codcli like '"+codigoCliente+"'";
		Log.i(TAG, rawQuery);

		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(rawQuery, null);
		
		String valor = "0";		
		cursor.moveToFirst();
		if (cursor.moveToFirst()) {
			do {
				valor = cursor.getString(0);
			} while (cursor.moveToNext());

		}
		Log.d(TAG, "getFlagMsPack "+valor);
		cursor.close();
		db.close();
		return valor;
		//return "1";
    }
    
    public Cliente getInformacionCliente(String codigoCliente){
    	String rawQuery = 
    			"SELECT codcli,DescSector,nomcli,DireccionFiscal,Giro,telefono,DescCanal, "+ 
    			"CASE WHEN monedaLimCred='E' THEN 'Moneda Extranjera' ELSE 'Moneda Nacional' END, "+
    			"limite_credito,DescUnidNeg, "+
    			"CASE WHEN monedaDocumento='A' THEN 'Ambas Monedas' WHEN monedaDocumento='E' THEN 'Moneda Extranjera' WHEN monedaDocumento='N' THEN 'Moneda Nacional' ELSE '' END "+    			
    			"FROM cliente "+
    			"WHERE codcli like '"+codigoCliente+"'";
		Log.i(TAG, rawQuery);

		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(rawQuery, null);
		Cliente cliente = null;
		cursor.moveToFirst();
		if (cursor.moveToFirst()) {
			do {
				cliente = new Cliente();
				cliente.setCodigoCliente(cursor.getString(0));
				cliente.setSector(cursor.getString(1));
				cliente.setNombre(cursor.getString(2));
				cliente.setDireccionFiscal(cursor.getString(3));
				cliente.setGiro(cursor.getString(4));
				cliente.setTelefono(cursor.getString(5));
				cliente.setCanal(cursor.getString(6));
				cliente.setMonedaCredito(cursor.getString(7));
				cliente.setLimiteCredito(cursor.getString(8));
				cliente.setUnidadNegocio(cursor.getString(9));
				cliente.setMonedaDocumento(cursor.getString(10));
			} while (cursor.moveToNext());

		}		
		cursor.close();
		db.close();
		return cliente;
    }

	public DB_DireccionClientes getDireccionClienteByItem(String codigoCliente, String item) {
		String rawQuery;
		rawQuery = "select codcli,item,direccion,telefono,coddep,codprv,ubigeo,des_corta,latitud,longitud " +
				"from "+ DBtables.Direccion_cliente.TAG
				+ " where direccion_cliente.codcli='" +codigoCliente + "' and direccion_cliente.item='"+item+"'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);

		DB_DireccionClientes dbdireccion=null;
		if (cur.moveToNext()) {

			dbdireccion = new DB_DireccionClientes();
			dbdireccion.setCodcli(cur.getString(cur.getColumnIndex("codcli")));
			dbdireccion.setItem(cur.getString(cur.getColumnIndex("item")));
			dbdireccion.setDireccion(cur.getString(cur.getColumnIndex("direccion")));
			dbdireccion.setTelefono(cur.getString(cur.getColumnIndex("telefono")));
			dbdireccion.setCoddep(cur.getString(cur.getColumnIndex("coddep")));
			dbdireccion.setCodprv(cur.getString(cur.getColumnIndex("codprv")));
			dbdireccion.setUbigeo(cur.getString(cur.getColumnIndex("ubigeo")));
			dbdireccion.setDes_corta(cur.getString(cur.getColumnIndex("des_corta")));
			dbdireccion.setLatitud(cur.getString(cur.getColumnIndex("latitud")));
			dbdireccion.setLongitud(cur.getString(cur.getColumnIndex("longitud")));

		}
		cur.close();
		db.close();

		return dbdireccion;
	}

	public  Cursor getClienteDirrectionAll(String texto_buscar){
		String rawQuery =
				"SELECT c.codcli,ruccli,nomcli, " +
						"ifnull(d.direccion,'*No tiene direccion') as direccion ,d.telefono, " +
						"latitud, "+
						"longitud "+
						"FROM cliente c "+
						"inner JOIN "+ DBtables.Direccion_cliente.TAG +" d ON c.codcli = d.codcli "+
						"WHERE d.latitud!=0 and (c.nomcli like '"+texto_buscar+"' "+
						"or d.telefono like '"+texto_buscar+"' "+
						"or d.direccion like '"+texto_buscar+"') ";
		Log.i(TAG, rawQuery);

		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(rawQuery, null);

		return cursor;
	}

	public  int getClienteDirrectionAll(){
		String rawQuery =
				"SELECT count(*) from cliente ";
		Log.i(TAG, rawQuery);

		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(rawQuery, null);
		int cantidad=0;
		if (cursor.moveToNext()){
			cantidad=cursor.getInt(0);
		}
		cursor.close();
		db.close();
		return cantidad;
	}
    
}
