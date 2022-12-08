package com.example.sm_tubo_plast.genesys.DAO;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.sm_tubo_plast.genesys.datatypes.DB_PromocionDetalle;
import com.example.sm_tubo_plast.genesys.util.VARIABLES;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;

@SuppressLint("LongLogTag")
public class DAO_PromocionDetalle  extends SQLiteAssetHelper{
	
	private static final String TAG = "DAO_PromocionDetalle";

	public static final String KEY_ROWID = "_id";

	public DAO_PromocionDetalle(Context context) {
		super(context, VARIABLES.ConfigDatabase.getDatabaseName(), null, VARIABLES.ConfigDatabase.getDatabaseVersion());
		
	}
	

	public boolean isPromocionAcumuladoPuro(DB_PromocionDetalle itemPromocion){
		boolean flag = false;
		
		String rawQuery = "SELECT MAX(total_agrupado) from promocion_detalle "
						+ "WHERE secuencia like '"+itemPromocion.getSecuencia()+"' and item like "+itemPromocion.getItem();

		Log.d(TAG + " :isPromocionAcumuladoPuro:",rawQuery);
		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		
		cur.moveToFirst();
		int cantidad=0;
		while (!cur.isAfterLast()) {
			cantidad = cur.getInt(0);			
			cur.moveToNext();
		}
		cur.close();
		db.close();
		
		if (cantidad == 1 ) {
			flag = true;
		}
		
		Log.d(TAG + " :isPromocionAcumuladoPuro:","isPromocionAcumuladoPuro: "+flag);
		
		return flag;
	}
	
	public ArrayList<DB_PromocionDetalle> getAcumuladosAND(DB_PromocionDetalle item){
		String rawQuery = "SELECT * from promocion_detalle "
						+ "WHERE agrupado = "
						+ "	(SELECT MIN(agrupado) from promocion_detalle where secuencia like '"+item.getSecuencia()+"' and item like "+item.getItem()+" ) "
						+ "and secuencia like '"+item.getSecuencia()+"' and item like "+item.getItem();
		
		//Log.d(TAG + " :getAcumuladosAND:",rawQuery);
		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		cur.moveToFirst();
		
		ArrayList<DB_PromocionDetalle> lista_agrupados= new ArrayList<DB_PromocionDetalle>();
		DB_PromocionDetalle itemPromocion = new DB_PromocionDetalle();
		
		Log.d("BDclasses ::obtenerListaAgrupados::secuencia: ","Lista de Acumulados AND");
		while (!cur.isAfterLast()) {
			
			itemPromocion = new DB_PromocionDetalle();
			
			itemPromocion.setSecuencia(cur.getInt(0));
			itemPromocion.setGeneral(cur.getInt(1));
			itemPromocion.setPromocion(cur.getString(2));
			itemPromocion.setCodalm(cur.getString(3));
			itemPromocion.setTipo(cur.getString(4));
			itemPromocion.setItem(cur.getInt(5));
			itemPromocion.setAgrupado(cur.getInt(6));
			itemPromocion.setEntrada(cur.getString(7));
			itemPromocion.setTipo_unimed_entrada(cur.getString(8));
			itemPromocion.setMonto_minimo(cur.getString(9));
			itemPromocion.setMonto_maximo(cur.getString(10));
			itemPromocion.setMonto(cur.getString(11));
			itemPromocion.setCondicion(cur.getString(12));
			itemPromocion.setCant_condicion(cur.getInt(13));
			itemPromocion.setSalida(cur.getString(14));
			itemPromocion.setTipo_unimed_salida(cur.getString(15));
			itemPromocion.setCant_promocion(cur.getInt(16));
			itemPromocion.setMax_pedido(cur.getInt(17));
			itemPromocion.setTotal_agrupado(cur.getInt(18));
			itemPromocion.setTipo_promocion(cur.getString(19));
			itemPromocion.setAcumulado(cur.getInt(22));
			Log.d("BDclasses ::obtenerListaAgrupados::secuencia: "+item.getSecuencia(), "Entrada(codigoProducto)-> "+itemPromocion.getEntrada());
			lista_agrupados.add(itemPromocion);
			cur.moveToNext();			
		}		
		return lista_agrupados;
	}
	
	public ArrayList<DB_PromocionDetalle> getAcumuladosOR(DB_PromocionDetalle item){
		String rawQuery = "SELECT * from promocion_detalle "
						+ "WHERE agrupado <> "
						+ "	(SELECT MIN(agrupado) from promocion_detalle where secuencia like '"+item.getSecuencia()+"' and item like "+item.getItem()+" ) "
						+ "and secuencia like '"+item.getSecuencia()+"' and item like "+item.getItem();
		
		//Log.d(TAG + " :getAcumuladosAND:",rawQuery);
		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		cur.moveToFirst();
		
		ArrayList<DB_PromocionDetalle> lista_agrupados= new ArrayList<DB_PromocionDetalle>();
		DB_PromocionDetalle itemPromocion = new DB_PromocionDetalle();
		
		
		Log.d("BDclasses ::obtenerListaAgrupados::secuencia: ","Lista de Acumulados OR");
		while (!cur.isAfterLast()) {
			
			itemPromocion = new DB_PromocionDetalle();
			
			itemPromocion.setSecuencia(cur.getInt(0));
			itemPromocion.setGeneral(cur.getInt(1));
			itemPromocion.setPromocion(cur.getString(2));
			itemPromocion.setCodalm(cur.getString(3));
			itemPromocion.setTipo(cur.getString(4));
			itemPromocion.setItem(cur.getInt(5));
			itemPromocion.setAgrupado(cur.getInt(6));
			itemPromocion.setEntrada(cur.getString(7));
			itemPromocion.setTipo_unimed_entrada(cur.getString(8));
			itemPromocion.setMonto_minimo(cur.getString(9));
			itemPromocion.setMonto_maximo(cur.getString(10));
			itemPromocion.setMonto(cur.getString(11));
			itemPromocion.setCondicion(cur.getString(12));
			itemPromocion.setCant_condicion(cur.getInt(13));
			itemPromocion.setSalida(cur.getString(14));
			itemPromocion.setTipo_unimed_salida(cur.getString(15));
			itemPromocion.setCant_promocion(cur.getInt(16));
			itemPromocion.setMax_pedido(cur.getInt(17));
			itemPromocion.setTotal_agrupado(cur.getInt(18));
			itemPromocion.setTipo_promocion(cur.getString(19));
			itemPromocion.setAcumulado(cur.getInt(22));
			Log.d("BDclasses ::obtenerListaAgrupados::secuencia: "+item.getSecuencia(), "Entrada(codigoProducto)-> "+itemPromocion.getEntrada());
			lista_agrupados.add(itemPromocion);
			cur.moveToNext();			
		}		
		return lista_agrupados;
	}
	
	public ArrayList<DB_PromocionDetalle> getPromociones_xPeso(String codigoCliente,String codigoVendedor, String codigoPolitica){
		String rawQuery = 
				"SELECT secuencia,promocion,tipo,condicion,cant_condicion, ifnull(grupo,''),ifnull(familia,''),subfamilia,descuento "+ 
				"FROM promocion_detalle WHERE tipo like 'P' "+
				"and ('"+ codigoCliente+ "' in (select codcli from promocion_clientes where sec_promocion=promocion_detalle.secuencia) or general like '0') "+
				"and ('"+ codigoVendedor+ "' in (select codven from promocion_vendedor where sec_promocion=promocion_detalle.secuencia) or vendedor like '0') "+
				"and ('"+ codigoPolitica+ "' in(select sec_politica from promocion_politica where sec_promocion=promocion_detalle.secuencia) or politica like '0') ";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		cur.moveToFirst();
		
		ArrayList<DB_PromocionDetalle> lista= new ArrayList<DB_PromocionDetalle>();
		DB_PromocionDetalle item = new DB_PromocionDetalle();
		
		
		Log.d(TAG, "getPromociones_xPeso");
		while (!cur.isAfterLast()) {
			item = new DB_PromocionDetalle();
			item.setSecuencia(cur.getInt(0));
			item.setPromocion(cur.getString(1));
			item.setTipo(cur.getString(2));
			item.setCondicion(cur.getString(3));
			item.setCant_condicion(cur.getInt(4));
			item.setGrupo(cur.getString(5));
			item.setFamilia(cur.getString(6));
			item.setSubfamilia(cur.getString(7));
			item.setDescuento(cur.getString(8));
			lista.add(item);
			cur.moveToNext();	
		}
		return lista;
	}
	

}
