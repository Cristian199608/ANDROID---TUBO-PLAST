package com.example.sm_tubo_plast.genesys.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.sm_tubo_plast.genesys.BEAN.ItemProducto;
import com.example.sm_tubo_plast.genesys.BEAN.model_bonificacion;
import com.example.sm_tubo_plast.genesys.adapters.ModelBonificacionPendiente;
import com.example.sm_tubo_plast.genesys.datatypes.DB_Pedido_Detalle_Promocion;
import com.example.sm_tubo_plast.genesys.datatypes.DB_PromocionDetalle;
import com.example.sm_tubo_plast.genesys.datatypes.DB_RegistroBonificaciones;
import com.example.sm_tubo_plast.genesys.datatypes.DBtables;
import com.example.sm_tubo_plast.genesys.datatypes.DBtables.TB_registro_bonificaciones;
import com.example.sm_tubo_plast.genesys.service.ConnectionDetector;
import com.example.sm_tubo_plast.genesys.util.GlobalFunctions;
import com.example.sm_tubo_plast.genesys.util.VARIABLES;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.Calendar;

@SuppressLint("LongLogTag")
public class DAO_RegistroBonificaciones extends SQLiteAssetHelper {

	private static final String TAG = "DAO_RegistroBonificaciones";

	public static final String KEY_ROWID = "_id";
	public String codAlmacen = "01";
	// flag for Internet connection status
	Boolean isInternetPresent = false;

	// Connection detector class
	ConnectionDetector cd;

	private Context _context;
	Calendar calendar = Calendar.getInstance();

	public DAO_RegistroBonificaciones(Context context) {
		super(context, VARIABLES.ConfigDatabase.getDatabaseName(), null, VARIABLES.ConfigDatabase.getDatabaseVersion());
		// TODO Auto-generated constructor stub
	}
	
	public void Eliminar_RegistroBonificacion(String oc_numero, int secuencia,int item,String entrada, int agrupado){
		String where = "oc_numero=? and secuenciaPromocion like ? and item like ? and entrada like ? and agrupado like ?";
				
		String[] args = { oc_numero, String.valueOf(secuencia), String.valueOf(item), entrada, String.valueOf(agrupado) };

		try {
			SQLiteDatabase db = getWritableDatabase();
			db.delete("registro_bonificaciones", where, args);			
			db.close();

			Log.e("ELIMINAR REGISTRO BONIFICACION", "oc_numero: "+oc_numero+" secuencia:"+secuencia+" item:"+item+" entrada:"+entrada+" agrupado:"+agrupado);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void Eliminar_RegistroBonificacion_Dependencias(String oc_numero,int secuencia, int item, int agrupado){
		String where = "oc_numero=? and secuenciaPromocion like ? and item like ? and (agrupado=? or acumulado like 1)";
		String [] args= {oc_numero, String.valueOf(secuencia), String.valueOf(item), String.valueOf(agrupado) };
		
		try {
			SQLiteDatabase db = getWritableDatabase();
			db.delete("registro_bonificaciones", where, args);			
			db.close();

			Log.e("ELIMINAR REGISTRO BONIFICACION", "oc_numero: "+oc_numero+" secuencia:"+secuencia+" item:"+secuencia+" agrupado:"+agrupado);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
		
	public int Recalcular_cantidadEntrada(String oc_numero,int secuencia,int acumulado){
		if(acumulado == 1){
			
		}else{
			Log.e("","RECALCULAR SALIDA --> NO ES ACUMULADO!!");
		}
						
		String rawQuery =
				"SELECT sum("
				+ "("
				+ "	CASE (tipo_unimed_entrada)"
				+ "	WHEN 0 THEN "
				+ "		cantidadEntrada"
				+ "	WHEN 1 THEN "
				+ "		(SELECT (cantidadEntrada * factor_conversion) from producto where codpro=entrada)"
				+ "	END"
				+ ")"+
				")"
				+ "	from registro_bonificaciones"
				+ "	where oc_numero ='"+oc_numero+"' and secuenciaPromocion="+secuencia+" and acumulado like 1";

		Log.i("DAO_RegistroBonificaciones :Recalcular_Salida:",rawQuery);
		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		
		int cantidadEntrada=0;
		cur.moveToFirst();
		
		while (!cur.isAfterLast()) {
			cantidadEntrada = cur.getInt(0);
			
			cur.moveToNext();
		}
		cur.close();
		db.close();
		Log.i("DAO_RegistroBonificaciones :Recalcular_cantidadEntrada:",""+cantidadEntrada);
		return cantidadEntrada;
	}
	
	public double Recalcular_montoEntrada(String oc_numero,int secuencia,int acumulado){
		if(acumulado == 1){
			
		}else{
			Log.e("","RECALCULAR SALIDA --> NO ES ACUMULADO!!");
		}
		
		String rawQuery = "SELECT sum(montoEntrada) from registro_bonificaciones "
				+ "where oc_numero='"+oc_numero+"' and secuenciaPromocion="+secuencia+" and acumulado like 1";

		Log.i("DAO_RegistroBonificaciones :Recalcular_montoEntrada:",rawQuery);
		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		
		double montoEntrada = 0.0;
		cur.moveToFirst();
		
		while (!cur.isAfterLast()) {
			montoEntrada = cur.getDouble(0);
			
			cur.moveToNext();
		}
		cur.close();
		db.close();
		Log.i("DAO_RegistroBonificaciones :Recalcular_Salida:",""+montoEntrada);
		return montoEntrada;
	}
	
	public void ActualizarRegistrosAcumulados2(String oc_numero, int secuencia,int cantidadSalida){
		String where = "oc_numero = ? and secuenciaPromocion like ? ";
		String[] args = { oc_numero, String.valueOf(secuencia) };

		try {

			SQLiteDatabase db = getWritableDatabase();

			ContentValues reg = new ContentValues();					
			reg.put("cantidadSalida", 0);			
			
			db.update("registro_bonificaciones", reg, where, args);
			db.close();

			Log.e("DAO_RegistroBonificaciones :ActualizarRegistrosAcumulado:", "(0)oc_numero: "+oc_numero);
			Log.e("DAO_RegistroBonificaciones :ActualizarRegistrosAcumulado:", "(0)secuencia: "+secuencia);
			

		} catch (SQLException ex) {
			Log.e("DAO_RegistroBonificaciones :ActualizarRegistrosAcumulado:","Exception");
			ex.printStackTrace();
		}
		
		
		String rawQuery = "SELECT entrada,agrupado from registro_bonificaciones "
				+ "where oc_numero='"+oc_numero+"' and secuenciaPromocion="+secuencia+" and acumulado like 1";

		Log.i("DAO_RegistroBonificaciones :ActualizarRegistrosAcumulado:",rawQuery);
		
		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		
		String entrada="";
		int agrupado=0;
		
		cur.moveToLast();
		
		while (!cur.isAfterLast()) {
			entrada = cur.getString(0);
			agrupado = cur.getInt(1);
			cur.moveToNext();
		}
		
		
		
		
		String where2 = "oc_numero = ? and secuenciaPromocion like ? and entrada like ? and agrupado like ?";
		String[] args2 = { oc_numero, String.valueOf(secuencia), entrada, String.valueOf(agrupado) };

		try {

			ContentValues reg = new ContentValues();			
			reg.put("cantidadSalida", cantidadSalida);
			
			db.update("registro_bonificaciones", reg, where2, args2);
			db.close();

			Log.e("DAO_RegistroBonificaciones :ActualizarRegistrosAcumulado:", "oc_numero: "+oc_numero);
			Log.e("DAO_RegistroBonificaciones :ActualizarRegistrosAcumulado:", "secuencia: "+secuencia);
			Log.e("DAO_RegistroBonificaciones :ActualizarRegistrosAcumulado:", "entrada: "+entrada);
			Log.e("DAO_RegistroBonificaciones :ActualizarRegistrosAcumulado:", "agrupado: "+agrupado);			
			Log.e("DAO_RegistroBonificaciones :ActualizarRegistrosAcumulado:", "cantidadSa: "+cantidadSalida);

		} catch (SQLException ex) {
			Log.e("DAO_RegistroBonificaciones :ActualizarRegistrosAcumulado:","Exception");
			ex.printStackTrace();
		}

	}
	
	public void ActualizarRegistrosAcumulado(String oc_numero, int secuencia, int item, String entrada, int agrupado, int cantidadBonificada, int cantidadDisponible, double montoDisponible){
		String where = "oc_numero = ? and secuenciaPromocion like ? and item like ? and entrada like ? and agrupado like ?";
		String[] args2 = { oc_numero, String.valueOf(secuencia), String.valueOf(item), entrada, String.valueOf(agrupado) };
		SQLiteDatabase db = getReadableDatabase();
				
		try {
			ContentValues reg = new ContentValues();			
			reg.put("cantidadSalida", cantidadBonificada);
			reg.put("cantidadDisponible", cantidadDisponible);
			reg.put("montoDisponible", GlobalFunctions.redondear_toDouble(montoDisponible));
			
			db.update("registro_bonificaciones", reg, where, args2);
			db.close();

			Log.e("DAO_RegistroBonificaciones :ActualizarRegistrosAcumulado:", "oc_numero: "+oc_numero);
			Log.e("DAO_RegistroBonificaciones :ActualizarRegistrosAcumulado:", "secuencia: "+secuencia);
			Log.e("DAO_RegistroBonificaciones :ActualizarRegistrosAcumulado:", "entrada: "+entrada);
			Log.e("DAO_RegistroBonificaciones :ActualizarRegistrosAcumulado:", "agrupado: "+agrupado);			
			Log.e("DAO_RegistroBonificaciones :ActualizarRegistrosAcumulado:", "cantidadSa: "+cantidadBonificada);

		} catch (SQLException ex) {
			Log.e("DAO_RegistroBonificaciones :ActualizarRegistrosAcumulado:","Exception");
			ex.printStackTrace();
		}
	}
	
	public String getSalida_fromRegistroBonificaciones(String oc_numero, int secuencia, String entrada, int agrupado){
		String rawQuery = "SELECT salida from registro_bonificaciones "
						+ "where oc_numero='"+oc_numero+"' and secuenciaPromocion like "+secuencia+" and entrada like '"+entrada+"' and agrupado like "+agrupado;
		
		Log.d("DBclasses :getCodpro_fromRegistroBonificaciones:",rawQuery);
		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		
		String salida="";
		cur.moveToFirst();
		
		while (!cur.isAfterLast()) {
			salida = cur.getString(0);
			
			cur.moveToNext();
		}
		cur.close();
		db.close();
		Log.d("DAO_RegistroBonificaciones :getSalida_fromRegistroBonificaciones:","producto anterior: "+salida);
		return salida;		
	}
	
	public int getcantidadSalidaPromocion(String oc_numero, int secuencia, int item){
		String rawQuery = "SELECT sum(cantidadSalida) from registro_bonificaciones "
				+ "where oc_numero='"+oc_numero+"' and secuenciaPromocion like "+secuencia+" and item like "+item;

		Log.d("DBclasses :getcantidadSalidaPromocion:",rawQuery);
		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		
		int cantidadSalida=0;
		cur.moveToFirst();
		
		while (!cur.isAfterLast()) {
			cantidadSalida = cur.getInt(0);			
			cur.moveToNext();
		}
		cur.close();
		db.close();
		Log.d("DAO_RegistroBonificaciones :getcantidadSalidaPromocion:","cantidadSalida: "+cantidadSalida);
		return cantidadSalida;
	}
	
	public int getCantidadSalida(String oc_numero, int secuencia, String entrada, int agrupado){
		String rawQuery = "SELECT cantidadSalida from registro_bonificaciones "
						+ "where oc_numero='"+oc_numero+"' and secuenciaPromocion like "+secuencia+" and entrada like '"+entrada+"' and agrupado like "+agrupado;
		
		Log.d("DBclasses :getCodpro_fromRegistroBonificaciones:",rawQuery);
		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		
		int cantidadSalida=0;
		cur.moveToFirst();
		
		while (!cur.isAfterLast()) {
			cantidadSalida = cur.getInt(0);			
			cur.moveToNext();
		}
		cur.close();
		db.close();
		Log.d("DAO_RegistroBonificaciones :getCantidadSalida:","cantidadSalida: "+cantidadSalida);
		return cantidadSalida;		
	}
	
	public DB_PromocionDetalle getPromocionDetalle(int secuencia, String entrada, int agrupado){
		String rawQuery = "SELECT * from promocion_detalle "
						+ "where secuencia like '"+secuencia+"' and entrada like '"+entrada+"' and agrupado like '"+agrupado+"'";
		
		Log.d("DBclasses :getPromocionDetalle:",rawQuery);
				
		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		DB_PromocionDetalle dbpromo = new DB_PromocionDetalle();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {
						
			dbpromo.setSecuencia(cur.getInt(0));
			dbpromo.setGeneral(cur.getInt(1));
			dbpromo.setPromocion(cur.getString(2));
			dbpromo.setCodalm(cur.getString(3));
			dbpromo.setTipo(cur.getString(4));
			dbpromo.setItem(Integer.parseInt(cur.getString(5)));
			dbpromo.setAgrupado(cur.getInt(6));
			dbpromo.setEntrada(cur.getString(7));
			dbpromo.setTipo_unimed_entrada(cur.getString(8));
			dbpromo.setMonto_minimo(cur.getString(9));
			dbpromo.setMonto_maximo(cur.getString(10));
			dbpromo.setMonto(cur.getString(11));
			dbpromo.setCondicion(cur.getString(12));
			dbpromo.setCant_condicion(cur.getInt(13));
			dbpromo.setSalida(cur.getString(14));
			dbpromo.setTipo_unimed_salida(cur.getString(15));
			dbpromo.setCant_promocion(cur.getInt(16));
			dbpromo.setMax_pedido(cur.getInt(17));
			dbpromo.setTotal_agrupado(cur.getInt(18));
			dbpromo.setTipo_promocion(cur.getString(19));
			dbpromo.setAcumulado(cur.getInt(22));

			
			Log.d(TAG, "setSecuencia:"+cur.getInt(0));
            Log.d(TAG, "setGeneral:"+cur.getInt(1));
            Log.d(TAG, "setPromocion:"+cur.getString(2));
            Log.d(TAG, "setCodalm:"+cur.getString(3));
            Log.d(TAG, "setTipo:"+cur.getString(4));
            Log.d(TAG, "setItem:"+Integer.parseInt(cur.getString(5)));
            Log.d(TAG, "setAgrupado:"+cur.getInt(6));
            Log.d(TAG, "setEntrada:"+cur.getString(7));
            Log.d(TAG, "setTipo_unimed_entrada:"+cur.getString(8));
            Log.d(TAG, "setMonto_minimo:"+cur.getString(9));
            Log.d(TAG, "setMonto_maximo:"+cur.getString(10));
            Log.d(TAG, "setMonto:"+cur.getString(11));
            Log.d(TAG, "setCondicion:"+cur.getString(12));
            Log.d(TAG, "setCant_condicion:"+cur.getInt(13));
            Log.d(TAG, "setSalida:"+cur.getString(14));
            Log.d(TAG, "setTipo_unimed_salida:"+cur.getString(15));
            Log.d(TAG, "setCant_promocion:"+cur.getInt(16));
            Log.d(TAG, "setMax_pedido:"+cur.getInt(17));
            Log.d(TAG, "setTotal_agrupado:"+cur.getInt(18));
            Log.d(TAG, "setTipo_promocion:"+cur.getString(19));
            Log.d(TAG, "setAcumulado:"+cur.getInt(22));
			cur.moveToNext();
		}
		cur.close();
		db.close();
		return dbpromo;
	}
	
	public void Eliminar_RegistroBonificacion_xSalida(String oc_numero,String salida){
		String where = "oc_numero=? and salida=?";		
		String[] args = { oc_numero, salida};

		try {
			SQLiteDatabase db = getWritableDatabase();
			db.delete("registro_bonificaciones", where, args);			
			db.close();

			Log.i("ELIMINAR REGISTRO BONIFICACION X SALIDA", "oc_numero: "+oc_numero+"  salida: "+salida);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
		
	public void Eliminar_RegistrosBonificacion(String oc_numero){
		String where = "oc_numero=? ";
		String[] args = { oc_numero};

		try {
			SQLiteDatabase db = getWritableDatabase();
			db.delete("registro_bonificaciones", where, args);			
			db.close();

			Log.i("ELIMINAR REGISTRO BONIFICACION", "oc_numero: "+oc_numero);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean esAgrupado_acumulado(DB_PromocionDetalle promocionDetalle) {

		String rawQuery = "select COUNT(*) from promocion_detalle "
				+ "where secuencia like " + promocionDetalle.getSecuencia()
				+ " and (agrupado like " + promocionDetalle.getAgrupado()
				+ " or acumulado like 1) ";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);

		cur.moveToFirst();
		int cantidad = 0;
		while (!cur.isAfterLast()) {
			cantidad = cur.getInt(0);

			cur.moveToNext();
		}
		cur.close();
		db.close();

		if (cantidad > 1) {
			return true;
		} else {
			return false;
		}

	}
	
	public boolean esAgrupado(DB_PromocionDetalle promocionDetalle) {

		String rawQuery = "select COUNT(*) from promocion_detalle "
				+ "where secuencia like " + promocionDetalle.getSecuencia()
				+ " and agrupado like " + promocionDetalle.getAgrupado();

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);

		cur.moveToFirst();
		int cantidad = 0;
		while (!cur.isAfterLast()) {
			cantidad = cur.getInt(0);

			cur.moveToNext();
		}
		cur.close();
		db.close();

		if (cantidad > 1) {
			return true;
		} else {
			return false;
		}

	}
	
	public boolean esAcumulado(int secuencia) {

		String rawQuery = "select COUNT(*) from promocion_detalle "
				+ "where secuencia like " + secuencia
				+ " and  acumulado like 1";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);

		cur.moveToFirst();
		int cantidad = 0;
		while (!cur.isAfterLast()) {
			cantidad = cur.getInt(0);

			cur.moveToNext();
		}
		cur.close();
		db.close();

		if (cantidad >= 1) {
			return true;
		} else {
			return false;
		}

	}
	
	public boolean estaRegistradoAcumulado_1(DB_PromocionDetalle itemPromocion) {
		if(itemPromocion.getAcumulado() ==1 && itemPromocion.getCondicion().equals("1")){
			
			/* Si algun item dentro de la secuencia acumulable no tiene la misma condicion(cosa que no debe ser) se vera acfectada */
			String rawQuery = "SELECT count(*) from registro_bonificaciones "
							+ "where secuenciaPromocion like '"+itemPromocion.getSecuencia()+"' and item like "+itemPromocion.getItem()
							+ " and acumulado like 1";
			
			SQLiteDatabase db = getReadableDatabase();
			Cursor cur = db.rawQuery(rawQuery, null);

			cur.moveToFirst();
			int cantidad = 0;
			while (!cur.isAfterLast()) {
				cantidad = cur.getInt(0);

				cur.moveToNext();
			}
			cur.close();
			db.close();

			if (cantidad >= 1) {
				return true;
			} else {
				return false;
			}			
		}else{
			return false;
		}
	}
	
	public boolean estaRegistradoAcumulado_3(DB_PromocionDetalle itemPromocion) {
		if(itemPromocion.getAcumulado() ==1 ){			
			/* Si algun item dentro de la secuencia acumulable no tiene la misma condicion(cosa que no debe ser) se vera acfectada */
			String rawQuery = "SELECT count(*) from registro_bonificaciones "
							+ "where secuenciaPromocion like '"+itemPromocion.getSecuencia()+"' and item like "+itemPromocion.getItem()
							+ " and acumulado like 1";
			
			SQLiteDatabase db = getReadableDatabase();
			Cursor cur = db.rawQuery(rawQuery, null);

			cur.moveToFirst();
			int cantidad = 0;
			while (!cur.isAfterLast()) {
				cantidad = cur.getInt(0);

				cur.moveToNext();
			}
			cur.close();
			db.close();

			if (cantidad >= 1) {
				return true;
			} else {
				return false;
			}			
		}else{
			return false;
		}
	}
		
	public model_bonificacion getProductoxCodigo(int cantidad, String codigoProducto, String entrada){
			
		String rawQuery =
				"select "
				+ "politica_precio2.secuencia,"
				+ "producto.codpro,"
				+ "producto.despro,"
				+ "politica_precio2.prepro,"
				+ "politica_precio2.prepro_unidad,"
				+ "producto.factor_conversion,"
				+ "ifnull(mta_kardex.stock-mta_kardex.xtemp,0) as stock "
				
				+ "from producto "
				+ "inner join politica_precio2 on producto.codpro = politica_precio2.codpro "
				+ "left join mta_kardex on mta_kardex.codpro = producto.codpro "
				+ "where politica_precio2.secuencia=3 "
				+ "and producto.codpro like '"+codigoProducto+"' ";
		
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(rawQuery, null);
		Log.d(TAG, rawQuery);
		
		model_bonificacion bonificacion = new model_bonificacion();

		if (cursor.moveToFirst()) {
			
			do {
				bonificacion.setCodigo(cursor.getString(1));
				bonificacion.setDescripcion(cursor.getString(2));
				bonificacion.setPrecio(cursor.getString(3));
				bonificacion.setUnidadMedida(cursor.getString(4));
				bonificacion.setFactorConversion(cursor.getInt(5));
				bonificacion.setStock(cursor.getInt(6));							
				bonificacion.setCantidad(cantidad);
				bonificacion.setEntrada(entrada);
			} while (cursor.moveToNext());
		}

		cursor.close();
		db.close();

		return bonificacion;
	
	}
	
	public model_bonificacion getModelBonificacion(int cantidad,String codigoProducto, String entrada){
		
		String rawQuery =
				"SELECT "+ 
				"producto.codpro,"+
				"producto.despro,"+				
				"'--.--',"+
				"producto.codunimed,"+
				"producto.factor_conversion "+
				"FROM producto "+ 
				"WHERE producto.codpro LIKE '"+codigoProducto+"'";						
		
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(rawQuery, null);
		Log.d(TAG, rawQuery);
		
		model_bonificacion bonificacion = new model_bonificacion();

		if (cursor.moveToFirst()) {
			
			do {
				bonificacion.setCodigo(cursor.getString(0));
				bonificacion.setDescripcion(cursor.getString(1));
				bonificacion.setPrecio(cursor.getString(2));
				bonificacion.setUnidadMedida(cursor.getString(3));
				bonificacion.setFactorConversion(cursor.getInt(4));											
				bonificacion.setCantidad(cantidad);
				bonificacion.setEntrada(entrada);
			} while (cursor.moveToNext());
		}

		cursor.close();
		db.close();

		return bonificacion;
	
	}
	
	public ArrayList<DB_RegistroBonificaciones> ObtenerRegistroBonificaciones(){
		String rawQuery = "select * from registro_bonificaciones";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		ArrayList<DB_RegistroBonificaciones> listaRegistroBonificaciones = new ArrayList<DB_RegistroBonificaciones>();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			DB_RegistroBonificaciones registro = new DB_RegistroBonificaciones();
			registro.setOc_numero(cur.getString(0));
			registro.setItem(cur.getInt(1));
			registro.setSecuenciaPromocion(cur.getInt(2));
			registro.setAgrupado(cur.getInt(3));
			registro.setEntrada(cur.getString(4));
			registro.setTipo_unimedEntrada(cur.getInt(5));
			registro.setUnimedEntrada(cur.getString(6));
			registro.setCantidadEntrada(cur.getInt(7));
			registro.setMontoEntrada(cur.getDouble(8));
			registro.setSalida(cur.getString(9));
			registro.setTipo_unimedSalida(cur.getInt(10));
			registro.setCantidadSalida(cur.getInt(11));
			registro.setAcumulado(cur.getInt(12));
			registro.setCantidadDisponible(cur.getInt(13));
			registro.setMontoDisponible(cur.getDouble(14));
			registro.setFlagUltimo(cur.getInt(15));
			
			listaRegistroBonificaciones.add(registro);
			cur.moveToNext();
		}
		cur.close();
		db.close();
		return listaRegistroBonificaciones;
	}
	
	public int getCantidadDisponible(String oc_numero, String entrada){
		//no consultar la minima, sino consultar la q tenga el flag de ultimo = 1
		String rawQuery =
				"SELECT cantidadDisponible "+
				"FROM registro_bonificaciones "+ 
				"WHERE oc_numero like '"+oc_numero+"' and entrada like '"+entrada+"' and flagUltimo like 1";
				
		
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(rawQuery, null);
		int cantidadDisponible = -1;
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			cantidadDisponible = cursor.getInt(0);
			cursor.moveToNext();
		}
		Log.i("DBclasses :getCantidadDisponible:",rawQuery);
		Log.i("DBclasses :getCantidadDisponible:","cantidadDisponible: "+cantidadDisponible);
		
		cursor.close();
		db.close();
		
		return cantidadDisponible;
	}
	
	public double getMontoDisponible(String oc_numero, String entrada){
		String rawQuery =
				"SELECT montoDisponible "+
				"FROM registro_bonificaciones "+
				"WHERE oc_numero = '"+oc_numero+"' and entrada like '"+entrada+"' and flagUltimo like 1";
		
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(rawQuery, null);
		double montoDisponible = -1;
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			montoDisponible = cursor.getDouble(0);
			cursor.moveToNext();			
		}
		Log.i("DBclasses :getMontoDisponible:","montoDisponible: "+montoDisponible);
		
		cursor.close();
		db.close();
		
		return montoDisponible;
	}
	
	public DB_RegistroBonificaciones getRegistroBonificacion(String oc_numero,int secuencia, String entrada, int agrupado){
		String rawQuery = "select * from registro_bonificaciones where oc_numero = '"+oc_numero+"' and secuenciaPromocion like "+secuencia+"  and entrada like '"+entrada+"' and agrupado like "+agrupado;
		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		
		DB_RegistroBonificaciones registro = new DB_RegistroBonificaciones();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {
			
			registro.setOc_numero(cur.getString(0));
			registro.setItem(cur.getInt(1));
			registro.setSecuenciaPromocion(cur.getInt(2));
			registro.setAgrupado(cur.getInt(3));
			registro.setEntrada(cur.getString(4));
			registro.setTipo_unimedEntrada(cur.getInt(5));
			registro.setUnimedEntrada(cur.getString(6));
			registro.setCantidadEntrada(cur.getInt(7));
			registro.setMontoEntrada(cur.getDouble(8));
			registro.setSalida(cur.getString(9));
			registro.setTipo_unimedSalida(cur.getInt(10));
			registro.setCantidadSalida(cur.getInt(11));
			registro.setAcumulado(cur.getInt(12));
			
			cur.moveToNext();
		}
		cur.close();
		db.close();
		return registro;
	}
	
	public ArrayList<DB_RegistroBonificaciones> getRegistroBonificaciones_xSecuencia(String oc_numero, int secuencia, int item, int acumulado){

		String rawQuery = "select * from registro_bonificaciones "
						+ "where oc_numero = '"+oc_numero+"' and secuenciaPromocion like "+secuencia+" and item like "+item+" and acumulado like "+acumulado;
		
		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		Log.d("getRegistroBonificaciones_xSecuencia",rawQuery);
		ArrayList<DB_RegistroBonificaciones> registroBonificacion = new ArrayList<DB_RegistroBonificaciones>();
		
		cur.moveToFirst();

		while (!cur.isAfterLast()) {
			DB_RegistroBonificaciones registro = new DB_RegistroBonificaciones();
			registro.setOc_numero(cur.getString(0));
			registro.setItem(cur.getInt(1));
			registro.setSecuenciaPromocion(cur.getInt(2));
			registro.setAgrupado(cur.getInt(3));
			registro.setEntrada(cur.getString(4));
			registro.setTipo_unimedEntrada(cur.getInt(5));
			registro.setUnimedEntrada(cur.getString(6));
			registro.setCantidadEntrada(cur.getInt(7));
			registro.setMontoEntrada(cur.getDouble(8));
			registro.setSalida(cur.getString(9));
			registro.setTipo_unimedSalida(cur.getInt(10));
			registro.setCantidadSalida(cur.getInt(11));
			registro.setAcumulado(cur.getInt(12));
			registro.setCantidadDisponible(cur.getInt(13));
			registro.setMontoDisponible(cur.getDouble(14));
			registro.setFlagUltimo(cur.getInt(15));
			
			registroBonificacion.add(registro);
			cur.moveToNext();
		}
		cur.close();
		db.close();
		return registroBonificacion;
	}
	
	public ArrayList<DB_RegistroBonificaciones> getRegistroBonificaciones_xEntrada(String oc_numero, String entrada){

		String rawQuery = "select * from registro_bonificaciones where oc_numero = '"+oc_numero+"' and entrada like '"+entrada+"'";
		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		Log.d("getRegistroBonificaciones",rawQuery);
		ArrayList<DB_RegistroBonificaciones> registroBonificacion = new ArrayList<DB_RegistroBonificaciones>();
		
		cur.moveToFirst();

		while (!cur.isAfterLast()) {
			DB_RegistroBonificaciones registro = new DB_RegistroBonificaciones();
			registro.setOc_numero(cur.getString(0));
			registro.setItem(cur.getInt(1));
			registro.setSecuenciaPromocion(cur.getInt(2));
			registro.setAgrupado(cur.getInt(3));
			registro.setEntrada(cur.getString(4));
			registro.setTipo_unimedEntrada(cur.getInt(5));
			registro.setUnimedEntrada(cur.getString(6));
			registro.setCantidadEntrada(cur.getInt(7));
			registro.setMontoEntrada(cur.getDouble(8));
			registro.setSalida(cur.getString(9));
			registro.setTipo_unimedSalida(cur.getInt(10));
			registro.setCantidadSalida(cur.getInt(11));
			registro.setAcumulado(cur.getInt(12));
			
			registroBonificacion.add(registro);
			cur.moveToNext();
		}
		cur.close();
		db.close();
		return registroBonificacion;
	}
	
	public void AgregarRegistroBonificacion(String oc_numero,int itemBonificacion,int item,int secuencia,int agrupado, String entrada,int tipoUnimedEntrada , String unimedEntrada, int cantidadEntrada,double montoEntrada, String salida, int tipoUnimedSalida, int cantidadSalida, int acumulado, int cantidadDisponible, double montoDisponible,String codigoVendedor){
		Actualizar_Flag(oc_numero, entrada);		
		try {
			SQLiteDatabase db = getWritableDatabase();

			ContentValues Nreg = new ContentValues();
			Nreg.put("oc_numero", oc_numero);
			Nreg.put(TB_registro_bonificaciones.CODIGO_REGISTRO, oc_numero+itemBonificacion);
			Nreg.put("item", item);
			Nreg.put("secuenciaPromocion", secuencia);
			Nreg.put("agrupado", agrupado);
			Nreg.put("entrada", entrada);
			Nreg.put("tipo_unimed_entrada", tipoUnimedEntrada);
			Nreg.put("unimedEntrada", unimedEntrada);
			Nreg.put("cantidadEntrada", cantidadEntrada);
			Nreg.put("montoEntrada", montoEntrada);
			Nreg.put("salida", salida);
			Nreg.put("tipo_unimed_salida", tipoUnimedSalida);
			Nreg.put("cantidadSalida", cantidadSalida);
			Nreg.put("acumulado", acumulado);
			Nreg.put("cantidadDisponible", cantidadDisponible);
			Nreg.put("montoDisponible", GlobalFunctions.redondear_toDouble(montoDisponible));
			Nreg.put("flagUltimo", 1);
			Nreg.put("codigoVendedor",codigoVendedor);

			db.insert("registro_bonificaciones", null, Nreg);
			db.close();
			Log.i("REGISTRO BONIFICACIONES", "ITEM INSERTADO");

		} catch (Exception e) {
			Log.i("REGISTRO BONIFICACIONES", "Error registro insertado");
		}

	}
	
	public void Actualizar_Flag(String oc_numero, String entrada){
		String where = "oc_numero = ? and entrada like ?";
		String[] args = { oc_numero, entrada};

		try {
			SQLiteDatabase db = getWritableDatabase();
			ContentValues reg = new ContentValues();
			reg.put("flagUltimo", 0);			
			db.update("registro_bonificaciones", reg, where, args);
			db.close();

			Log.i("DAO_RegistroBonificaciones :Actualizar_Flag:", "0");

		} catch (SQLException ex) {
			Log.e("DAO_RegistroBonificaciones :Actualizar_Flag:", ex.getMessage());			
			ex.printStackTrace();
		}
	}
	public void Actualizar_RegistroBonificacion(String oc_numero,int secuencia,int item,int agrupado,String entrada,int tipoUnimedEntrada, String unimedEntrada, int cantidadEntrada, double montoEntrada, String salida,int unimedSalida, int cantidadSalida, int acumulado,int cantidadDisponible, double montoDisponible){
		String where = "oc_numero = ? and secuenciaPromocion like ? and item like "+item+" and entrada like ? and agrupado like ?";
		String[] args = { oc_numero, String.valueOf(secuencia), entrada, String.valueOf(agrupado) };

		try {

			SQLiteDatabase db = getWritableDatabase();

			ContentValues reg = new ContentValues();
			//reg.put("secuenciaPromocion", secuencia);
			//reg.put("agrupado", agrupado);
			reg.put("tipo_unimed_entrada", tipoUnimedEntrada);
			reg.put("unimedEntrada", unimedEntrada);
						
			reg.put("cantidadEntrada", cantidadEntrada);
			reg.put("montoEntrada", montoEntrada);
			reg.put("salida", salida);
			reg.put("tipo_unimed_salida", unimedSalida);
			reg.put("cantidadSalida", cantidadSalida);
			
			reg.put("cantidadDisponible", cantidadDisponible);
			reg.put("montoDisponible", GlobalFunctions.redondear_toDouble(montoDisponible));
			

			db.update("registro_bonificaciones", reg, where, args);
			db.close();

			Log.e("UPDATE REGISTRO BONIFICACIONES", "SE ESTA ACTUALIZANDO N REGISTRO BONIFICACION !!!!!!!!!!!");

		} catch (SQLException ex) {
			Log.d("DAO_RegistroBonificaciones :Actualizar_RegistroBonificacion:","Exception");
			ex.printStackTrace();
		}
	}
	
	public void Actualizar_RegistroBonificacion(String codigoRegistro,int cantidadTotal,int saldoAnterior,int cantidadEntregada,int saldo,String codigoAnterior,String codigoVendedor, String codigoCliente){
		String where = "codigoRegistro = ?";
		String[] args = { codigoRegistro };

		try {

			SQLiteDatabase db = getWritableDatabase();
			ContentValues reg = new ContentValues();

			reg.put(TB_registro_bonificaciones.CANTIDAD_TOTAL, cantidadTotal);
			reg.put(TB_registro_bonificaciones.SALDO_ANTERIOR, saldoAnterior);
			reg.put(TB_registro_bonificaciones.CANTIDAD_ENTREGADA, cantidadEntregada);
			reg.put(TB_registro_bonificaciones.SALDO, saldo);
			reg.put(TB_registro_bonificaciones.CODIGO_ANTERIOR, codigoAnterior);
			reg.put(TB_registro_bonificaciones.CODIGO_VENDEDOR, codigoVendedor);
			reg.put(TB_registro_bonificaciones.CODIGO_CLIENTE, codigoCliente);
			

			db.update("registro_bonificaciones", reg, where, args);
			db.close();

			Log.e("Actualizar_RegistroBonificacion", "Bonificacion modificada pendientes");

		} catch (SQLException ex) {
			Log.d("DAO_RegistroBonificaciones :Actualizar_RegistroBonificacion:","Exception");
			ex.printStackTrace();
		}
	}
	
	public void Actualizar_RegistroBonificacion(String codigoRegistro,int saldoAnterior,int saldo){
		//Verificar si se debe mantener el codigoAnterior
		String where = "codigoRegistro = ?";
		String[] args = { codigoRegistro };

		try {

			SQLiteDatabase db = getWritableDatabase();
			ContentValues reg = new ContentValues();
			
			reg.put(TB_registro_bonificaciones.SALDO_ANTERIOR, saldoAnterior);			
			reg.put(TB_registro_bonificaciones.SALDO, saldo);
			
			db.update("registro_bonificaciones", reg, where, args);
			db.close();

			Log.e("Actualizar_RegistroBonificacion", "Bonificacion actualizada con saldoAnterior:"+saldoAnterior+" y saldo:"+saldo);

		} catch (SQLException ex) {
			Log.d("DAO_RegistroBonificaciones :Actualizar_RegistroBonificacion:","Exception");
			ex.printStackTrace();
		}
	}
	
	public void Actualizar_RegistroBonificacion(String codigoRegistro,int saldoAnterior,int saldo,String codigoAnterior){
		//Verificar si se debe mantener el codigoAnterior
		String where = "codigoRegistro = ?";
		String[] args = { codigoRegistro };

		try {

			SQLiteDatabase db = getWritableDatabase();
			ContentValues reg = new ContentValues();
			
			reg.put(TB_registro_bonificaciones.SALDO_ANTERIOR, saldoAnterior);			
			reg.put(TB_registro_bonificaciones.SALDO, saldo);
			reg.put(TB_registro_bonificaciones.CODIGO_ANTERIOR, codigoAnterior);
			
			db.update("registro_bonificaciones", reg, where, args);
			db.close();

			Log.e("Actualizar_RegistroBonificacion", "Bonificacion actualizada con saldoAnterior:"+saldoAnterior+" y saldo:"+saldo);

		} catch (SQLException ex) {
			Log.d("DAO_RegistroBonificaciones :Actualizar_RegistroBonificacion:","Exception");
			ex.printStackTrace();
		}
	}
	
	public void Actualizar_saldoBonificacion(String codigoRegistro,int saldo){
		String where = "codigoRegistro = ?";
		String[] args = { codigoRegistro };
		//Se actualiza el saldo del registro anterior a 0 para ser enviado 
		try {
			SQLiteDatabase db = getWritableDatabase();
			ContentValues reg = new ContentValues();
			reg.put(TB_registro_bonificaciones.SALDO, saldo);
			db.update("registro_bonificaciones", reg, where, args);
			db.close();

			Log.e("Actualizar_RegistroBonificacion", codigoRegistro+" actualizado con saldo "+saldo);

		} catch (SQLException ex) {
			Log.d("DAO_RegistroBonificaciones :Actualizar_RegistroBonificacion:","Exception");
			ex.printStackTrace();
		}
	}
	
	public boolean VerificarRegistroBonificacion(String oc_numero,int secuencia,int item, String entrada,int agrupado){
		boolean flag = false;
		String rawQuery = "SELECT * from registro_bonificaciones "
						+ "where oc_numero='"+oc_numero+"' and secuenciaPromocion like "+secuencia+" and item like "+item+" and entrada like '"+entrada+"' and agrupado like "+agrupado; 
		
		Log.e("DBclasses :VerificarRegistroBonificaion:",rawQuery);
		

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);		
		cur.moveToFirst();

		while (!cur.isAfterLast()) {
			flag=true;
			cur.moveToNext();
		}
		cur.close();
		db.close();
		Log.i("DBclasses :VerificarRegistroBonificaion:",""+flag);
		return flag;
	}
	
	public ItemProducto obtener_datosProdPromocion(String codpro) {

		String rawQuery = "select producto.despro, producto.factor_conversion, producto.peso, unidad_medida.desunimed "
				+ "from producto inner join unidad_medida on producto.codunimed = unidad_medida.codunimed "
				+ "where producto.codpro='" + codpro + "'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(rawQuery, null);

		ItemProducto obj = new ItemProducto();

		cursor.moveToFirst();
		if (cursor.moveToFirst()) {
			do {
				obj.setDescripcion(cursor.getString(0));
				obj.setFact_conv(cursor.getInt(1));
				obj.setPeso(cursor.getDouble(2));
				obj.setDesunimed(cursor.getString(3));
			} while (cursor.moveToNext());

		}
		cursor.close();
		db.close();

		return obj;

	}
	
	public int obtenerCantidadBonificacion(String oc_numero, String salida){
		int cantidad = 0;
		String rawQuery = "SELECT sum(cantidadSalida) from registro_bonificaciones where oc_numero='"+oc_numero+"' and salida like '"+salida+"'"; 
		
		Log.i("DBclasses :obtenerCantidadBonificacion:",rawQuery);
		

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);		
		cur.moveToFirst();

		while (!cur.isAfterLast()) {
			cantidad = cur.getInt(0);
			cur.moveToNext();
		}
		cur.close();
		db.close();
		Log.i("DBclasses :obtenerCantidadBonificacion:","cantidad: "+cantidad);
		return cantidad;
	}
	
	public int getCantidadDatos_registro_bonificaciones(String oc_numero) {

		String rawQuery;

		rawQuery = "select count(*) from registro_bonificaciones where flag='N' and oc_numero='"	+ oc_numero + "'";

		int cant_datos = 0;

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		cur.moveToFirst();

		if (cur.moveToFirst()) {
			do {
				cant_datos = cur.getInt(0);
			} while (cur.moveToNext());

		}

		cur.close();
		db.close();

		return cant_datos;

	}

	public ArrayList<DB_RegistroBonificaciones> getRegistro_bonificacionesXflag(String oc_numero) {

		String rawQuery;

		rawQuery = "select * from registro_bonificaciones where flag='N' and oc_numero='" + oc_numero + "'";

		ArrayList<DB_RegistroBonificaciones> lista = new ArrayList<DB_RegistroBonificaciones>();

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		cur.moveToFirst();

		if (cur.moveToFirst()) {

			do {
				DB_RegistroBonificaciones item = new DB_RegistroBonificaciones();
				item.setOc_numero(cur.getString(0));
				item.setItem(cur.getInt(1));
				item.setSecuenciaPromocion(cur.getInt(2));
				item.setAgrupado(cur.getInt(3));
				item.setEntrada(cur.getString(4));
				item.setTipo_unimedEntrada(cur.getInt(5));
				item.setUnimedEntrada(cur.getString(6));
				item.setCantidadEntrada(cur.getInt(7));
				item.setMontoEntrada(cur.getDouble(8));
				item.setSalida(cur.getString(9));
				item.setTipo_unimedSalida(cur.getInt(10));
				item.setCantidadSalida(cur.getInt(11));
				item.setAcumulado(cur.getInt(12));
				item.setCantidadDisponible(cur.getInt(13));
				item.setMontoDisponible(cur.getDouble(14));
				item.setFlagUltimo(cur.getInt(15));
				lista.add(item);
				Log.i("DAO_RegistroBonificaciones","getRegistro_bonificacionesXflag:  " + cur.getString(0));
			} while (cur.moveToNext());			
		}
		cur.close();
		db.close();

		return lista;
	}
	
	public ArrayList<DB_RegistroBonificaciones> getRegistroBonificaciones(String oc_numero) {
		String rawQuery;

		rawQuery = "select * from registro_bonificaciones where oc_numero='" + oc_numero + "'";

		ArrayList<DB_RegistroBonificaciones> lista = new ArrayList<DB_RegistroBonificaciones>();
		try{
			SQLiteDatabase db = getReadableDatabase();
			Cursor cur = db.rawQuery(rawQuery, null);			
			cur.moveToFirst();

			while (!cur.isAfterLast()) {
				DB_RegistroBonificaciones item = new DB_RegistroBonificaciones();
				item.setOc_numero(cur.getString(0));
				item.setItem(cur.getInt(1));
				item.setSecuenciaPromocion(cur.getInt(2));
				item.setAgrupado(cur.getInt(3));
				item.setEntrada(cur.getString(4));
				item.setTipo_unimedEntrada(cur.getInt(5));
				item.setUnimedEntrada(cur.getString(6));
				item.setCantidadEntrada(cur.getInt(7));
				item.setMontoEntrada(cur.getDouble(8));
				item.setSalida(cur.getString(9));
				item.setTipo_unimedSalida(cur.getInt(10));
				item.setCantidadSalida(cur.getInt(11));
				item.setAcumulado(cur.getInt(12));
				item.setCantidadDisponible(cur.getInt(13));
				item.setMontoDisponible(cur.getDouble(14));
				item.setFlagUltimo(cur.getInt(15));
				if (cur.getString(16)==null) {
					item.setCodigoRegistro("");
				}else{
					item.setCodigoRegistro(cur.getString(16));
				}				
				item.setCantidadTotal(cur.getInt(17));
				item.setSaldoAnterior(cur.getInt(18));
				item.setCantidadEntregada(cur.getInt(19));
				item.setSaldo(cur.getInt(20));				
				if (cur.getString(21)==null) {
					item.setCodigoAnterior("");
				}else{
					item.setCodigoAnterior(cur.getString(21));
				}
				item.setCodigoVendedor(cur.getString(22));
				item.setCodigoCliente(cur.getString(23));
				
				lista.add(item);
				Log.i("DAO_RegistroBonificaciones","getRegistroBonificaciones:  " + cur.getString(0));
				cur.moveToNext();
			}			
			cur.close();
			db.close();
			
		}catch (Exception e) {
			Log.e("DAO_registroBonificacioens :getRegistroBonificaciones:", e.getMessage());
		}
		return lista;
	}
	
	public ArrayList<DB_PromocionDetalle> getListaEscalables(int item,int secuencia){
		String rawQuery =
				"select * from promocion_detalle where secuencia like '"+secuencia+"' and acumulado like '2' ";
		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		cur.moveToFirst();
		
		ArrayList<DB_PromocionDetalle> listaEscalables= new ArrayList<DB_PromocionDetalle>();
		DB_PromocionDetalle itemPromocion = new DB_PromocionDetalle();
		
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
			/**/
			/**/			
			itemPromocion.setAcumulado(cur.getInt(22));
			Log.d("BDclasses ::obtenerListaAgrupados::secuencia: "+secuencia, "Entrada(codigoProducto)-> "+itemPromocion.getEntrada());
			listaEscalables.add(itemPromocion);
			cur.moveToNext();
			
		}
		
		return listaEscalables;
	}
	
	public String getUltimoRegistroBonificacion(String oc_numero,String codigoSalida){
		String rawQuery =
				"select codigoRegistro from registro_bonificaciones where oc_numero like '"+oc_numero+"' and salida like '"+codigoSalida+"' ";
		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		cur.moveToFirst();
		String codigoRegistro = "";
		while (!cur.isAfterLast()) {
			codigoRegistro = cur.getString(0);			
			Log.d("BDclasses ::getUltimoRegistroBonificacion::","por ocnumero codigoRegistro: "+codigoRegistro);			
			cur.moveToNext();			
		}		
		return codigoRegistro;
	}
	
	public int getSaldoPendiente(String codigoCliente, String codigoSalida){
		String rawQuery = "SELECT saldo from registro_bonificaciones WHERE codigoCliente like '"+codigoCliente+"' AND salida like '"+codigoSalida+"' AND cantidadTotal IS NOT NULL";
		Log.d(TAG,rawQuery);
		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		cur.moveToFirst();
		int saldo = 0;
		while (!cur.isAfterLast()) {
			saldo = cur.getInt(0);			
			Log.d("BDclasses ::getSaldoPendiente::","saldo: "+saldo);			
			cur.moveToNext();			
		}		
		return saldo;
	}
	
	public String getRegistroAnteriorPendiente(String codigoCliente, String codigoSalida){
		String rawQuery = "SELECT codigoRegistro from registro_bonificaciones WHERE codigoCliente like '"+codigoCliente+"' AND salida like '"+codigoSalida+"' AND cantidadTotal IS NOT NULL";
		Log.i(TAG,rawQuery);
		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		cur.moveToFirst();
		String codigo = "";
		while (!cur.isAfterLast()) {
			codigo = cur.getString(0);			
			Log.i(TAG+"::getSaldoPendiente::","getRegistroAnteriorPendiente: " + codigo);			
			cur.moveToNext();			
		}		
		return codigo;
	}
	
	public ArrayList<String> getRegistroAnterior(String oc_numero, String codigoSalida){
		String rawQuery = "select max(codigoRegistro),saldoAnterior,cantidadEntregada,codigoAnterior from registro_bonificaciones where oc_numero like '"+oc_numero+"' and salida like '"+codigoSalida+"' and (cantidadEntregada <> '' OR cantidadEntregada IS NOT NULL) and cantidadTotal IS NOT NULL";
		Log.e("getRegistroAnterior", rawQuery);
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(rawQuery, null);
		cursor.moveToFirst();
		ArrayList<String> lista = new ArrayList<String>();
		while (!cursor.isAfterLast()) {
			lista.add(cursor.getString(0));//CodigoRegistro
			lista.add(cursor.getString(1));//saldoAnterior
			lista.add(cursor.getString(2));//cantidadEntregada
			lista.add(cursor.getString(3));//codigoAnterior
			Log.d("getRegistroAnterior", "codigoRegistro "+cursor.getString(0));
			Log.d("getRegistroAnterior", "saldoAnterior "+cursor.getString(1));
			Log.d("getRegistroAnterior", "cantidadEntregada "+cursor.getString(2));
			cursor.moveToNext();			
		}		
		return lista;
	}
	
	public int getCantidadEntregada(String oc_numero, String codigoSalida){
		String rawQuery = "SELECT sum(cantidadEntregada) from registro_bonificaciones WHERE oc_numero like '"+oc_numero+"' and salida like '"+codigoSalida+"' and cantidadTotal IS NOT NULL";
		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		cur.moveToFirst();
		int cantidad = 0;
		while (!cur.isAfterLast()) {
			cantidad = cur.getInt(0);			
			Log.d("BDclasses ::getCantidadEntregada::","cantidad: " + cantidad);			
			cur.moveToNext();			
		}		
		return cantidad;
	}
	

	public int getCantidadSalidaSecuencia(String oc_numero, int secuencia, int item, int agrupado){
		String rawQuery = "SELECT sum(cantidadSalida) FROM registro_bonificaciones "
						+ "WHERE oc_numero like '"+oc_numero+"' AND secuenciaPromocion like '"+secuencia+"' AND item like '"+item+"' AND agrupado like '"+agrupado+"'";
		Log.i(TAG,rawQuery);
		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		cur.moveToFirst();
		int cantidad = 0;
		while (!cur.isAfterLast()) {
			cantidad = cur.getInt(0);			
			Log.d(TAG,"getCantidadSalidaSecuencia:: cantidad: " + cantidad);			
			cur.moveToNext();			
		}		
		return cantidad;
	}
	
	public DB_RegistroBonificaciones getRegistroBonificacion(String codigoRegistro) {
		String rawQuery;

		rawQuery = "select * from registro_bonificaciones where codigoRegistro='" + codigoRegistro + "'";

		DB_RegistroBonificaciones item = new DB_RegistroBonificaciones();
		try{
			SQLiteDatabase db = getReadableDatabase();
			Cursor cur = db.rawQuery(rawQuery, null);			
			cur.moveToFirst();

			while (!cur.isAfterLast()) {				
				item.setOc_numero(cur.getString(0));
				item.setItem(cur.getInt(1));
				item.setSecuenciaPromocion(cur.getInt(2));
				item.setAgrupado(cur.getInt(3));
				item.setEntrada(cur.getString(4));
				item.setTipo_unimedEntrada(cur.getInt(5));
				item.setUnimedEntrada(cur.getString(6));
				item.setCantidadEntrada(cur.getInt(7));
				item.setMontoEntrada(cur.getDouble(8));
				item.setSalida(cur.getString(9));
				item.setTipo_unimedSalida(cur.getInt(10));
				item.setCantidadSalida(cur.getInt(11));
				item.setAcumulado(cur.getInt(12));
				item.setCantidadDisponible(cur.getInt(13));
				item.setMontoDisponible(cur.getDouble(14));
				item.setFlagUltimo(cur.getInt(15));
				item.setCodigoRegistro(cur.getString(16));
				item.setCantidadTotal(cur.getInt(17));
				item.setSaldoAnterior(cur.getInt(18));
				item.setCantidadEntregada(cur.getInt(19));				
				item.setSaldo(cur.getInt(20));				
				if (cur.getString(21)==null) {
					item.setCodigoAnterior("");
				}else{
					item.setCodigoAnterior(cur.getString(21));
				}
				item.setCodigoVendedor(cur.getString(22));
				item.setCodigoCliente(cur.getString(23));
				
				Log.d("DAO_RegistroBonificaciones","getRegistroBonificacion:  " + cur.getString(0)+ " - "+ item.getCodigoRegistro() +" saldo: "+cur.getInt(20));
				cur.moveToNext();
			}			
			cur.close();
			db.close();
			
		}catch (Exception e) {
			Log.e("DAO_registroBonificacioens :getRegistroBonificaciones:", e.getMessage());
		}
		return item;
	}
	
	public int getSaldoAnterior(String oc_numero,String codigoSalida) {
		String rawQuery;

		rawQuery = "select saldoAnterior from registro_bonificaciones where oc_numero like '"+oc_numero+"' and salida like '" + codigoSalida + "' and cantidadTotal IS NOT NULL";
		Log.i(TAG, rawQuery);
		int saldoAnterior = 0;
		try{
			SQLiteDatabase db = getReadableDatabase();
			Cursor cur = db.rawQuery(rawQuery, null);			
			cur.moveToFirst();

			while (!cur.isAfterLast()) {				
				saldoAnterior = cur.getInt(0);
				cur.moveToNext();
			}			
			cur.close();
			db.close();
			Log.d(TAG, "getSaldoAnterior: "+saldoAnterior);
		}catch (Exception e) {
			Log.e(TAG+":DB_RegistroBonificaciones:", e.getMessage());
		}
		return saldoAnterior;
	}
	
	public String getCodigoRegistroAnterior(String oc_numero,String codigoSalida) {
		String rawQuery;

		rawQuery = "select codigoAnterior from registro_bonificaciones where oc_numero like '"+oc_numero+"' and salida like '" + codigoSalida + "' and cantidadTotal IS NOT NULL";
		Log.i(TAG, rawQuery);
		String codigoAnterior = "";
		try{
			SQLiteDatabase db = getReadableDatabase();
			Cursor cur = db.rawQuery(rawQuery, null);			
			cur.moveToFirst();

			while (!cur.isAfterLast()) {				
				codigoAnterior = cur.getString(0);
				cur.moveToNext();
			}
			Log.d(TAG, "getCodigoRegistroAnterior: "+codigoAnterior);
			cur.close();
			db.close();
			
		}catch (Exception e) {
			
			Log.e(TAG+":getCodigoRegistroAnterior:", e.getMessage());
			e.printStackTrace();
		}
		return codigoAnterior;
	}
	
	public ArrayList<DB_RegistroBonificaciones> getRegistrosUsanPendientes(String oc_numero) {
		String rawQuery;

		rawQuery = "select * from registro_bonificaciones where oc_numero like '"+oc_numero+"' and saldoAnterior > 0 and (codigoAnterior <> '' OR codigoAnterior IS NOT NULL)";
		
		ArrayList<DB_RegistroBonificaciones> lista = new ArrayList<>();		
		try{
			SQLiteDatabase db = getReadableDatabase();
			Cursor cur = db.rawQuery(rawQuery, null);			
			cur.moveToFirst();

			while (!cur.isAfterLast()) {
				DB_RegistroBonificaciones item = new DB_RegistroBonificaciones();
				item.setOc_numero(cur.getString(0));
				item.setItem(cur.getInt(1));
				item.setSecuenciaPromocion(cur.getInt(2));
				item.setAgrupado(cur.getInt(3));
				item.setEntrada(cur.getString(4));
				item.setTipo_unimedEntrada(cur.getInt(5));
				item.setUnimedEntrada(cur.getString(6));
				item.setCantidadEntrada(cur.getInt(7));
				item.setMontoEntrada(cur.getDouble(8));
				item.setSalida(cur.getString(9));
				item.setTipo_unimedSalida(cur.getInt(10));
				item.setCantidadSalida(cur.getInt(11));
				item.setAcumulado(cur.getInt(12));
				item.setCantidadDisponible(cur.getInt(13));
				item.setMontoDisponible(cur.getDouble(14));
				item.setFlagUltimo(cur.getInt(15));
				item.setCodigoRegistro(cur.getString(16));
				item.setCantidadTotal(cur.getInt(17));
				item.setSaldoAnterior(cur.getInt(18));
				item.setCantidadEntregada(cur.getInt(19));				
				item.setSaldo(cur.getInt(20));				
				if (cur.getString(21)==null) {
					item.setCodigoAnterior("");
				}else{
					item.setCodigoAnterior(cur.getString(21));
				}
				item.setCodigoVendedor(cur.getString(22));				
				lista.add(item);
				Log.i("DAO_RegistroBonificaciones","getRegistroBonificacion:  " + cur.getString(0)+" saldo: "+cur.getInt(20));
				cur.moveToNext();
			}			
			cur.close();
			db.close();
			
		}catch (Exception e) {
			Log.e("DAO_registroBonificacioens :getRegistroBonificaciones:", e.getMessage());
		}
		return lista;
	}
	
	public ArrayList<DB_RegistroBonificaciones> getRegistrosUsanPendientesNulos(String oc_numero) {
		String rawQuery;

		rawQuery = "select * from registro_bonificaciones where oc_numero like '"+oc_numero+"' and cantidadEntregada > 0 and (codigoAnterior <> '' OR codigoAnterior IS NOT NULL) and cantidadTotal IS NULL";
		
		ArrayList<DB_RegistroBonificaciones> lista = new ArrayList<>();		
		try{
			SQLiteDatabase db = getReadableDatabase();
			Cursor cur = db.rawQuery(rawQuery, null);			
			cur.moveToFirst();

			while (!cur.isAfterLast()) {
				DB_RegistroBonificaciones item = new DB_RegistroBonificaciones();
				item.setOc_numero(cur.getString(0));
				item.setItem(cur.getInt(1));
				item.setSecuenciaPromocion(cur.getInt(2));
				item.setAgrupado(cur.getInt(3));
				item.setEntrada(cur.getString(4));
				item.setTipo_unimedEntrada(cur.getInt(5));
				item.setUnimedEntrada(cur.getString(6));
				item.setCantidadEntrada(cur.getInt(7));
				item.setMontoEntrada(cur.getDouble(8));
				item.setSalida(cur.getString(9));
				item.setTipo_unimedSalida(cur.getInt(10));
				item.setCantidadSalida(cur.getInt(11));
				item.setAcumulado(cur.getInt(12));
				item.setCantidadDisponible(cur.getInt(13));
				item.setMontoDisponible(cur.getDouble(14));
				item.setFlagUltimo(cur.getInt(15));
				item.setCodigoRegistro(cur.getString(16));
				item.setCantidadTotal(cur.getInt(17));
				item.setSaldoAnterior(cur.getInt(18));
				item.setCantidadEntregada(cur.getInt(19));				
				item.setSaldo(cur.getInt(20));				
				if (cur.getString(21)==null) {
					item.setCodigoAnterior("");
				}else{
					item.setCodigoAnterior(cur.getString(21));
				}
				item.setCodigoVendedor(cur.getString(22));				
				lista.add(item);
				Log.i("DAO_RegistroBonificaciones","getRegistroBonificacion:  " + cur.getString(0)+" saldo: "+cur.getInt(20));
				cur.moveToNext();
			}			
			cur.close();
			db.close();
			
		}catch (Exception e) {
			Log.e("DAO_registroBonificacioens :getRegistroBonificaciones:", e.getMessage());
		}
		return lista;
	}
	
	public ArrayList<ModelBonificacionPendiente> getListaBonificacionesPendientes(String oc_numero,String codigoCliente) {
		String rawQuery;

		rawQuery = "SELECT (SELECT promocion FROM promocion_detalle WHERE secuencia like b.secuenciaPromocion ),b.salida,p.despro,b.saldo,p.codunimed ,b.codigoRegistro "
				 + "FROM registro_bonificaciones b "
				 + "INNER JOIN producto p ON b.salida=('B'||p.codpro) "
				 + "WHERE saldo > 0 AND oc_numero <> '"+oc_numero+"' and codigoCliente like '"+codigoCliente+"'";
			
		ArrayList<ModelBonificacionPendiente> lista = new ArrayList<>();		
		try{
			SQLiteDatabase db = getReadableDatabase();
			Cursor cur = db.rawQuery(rawQuery, null);			
			cur.moveToFirst();

			while (!cur.isAfterLast()) {				
				ModelBonificacionPendiente item = new ModelBonificacionPendiente();
				item.setPromocion(cur.getString(0));
				item.setCodigoSalida(cur.getString(1));
				item.setDescripcionSalida(cur.getString(2));
				item.setSaldo(cur.getInt(3));
				item.setUnidadMedida(cur.getString(4));
				item.setCodigoRegistro(cur.getString(5));
				
				lista.add(item);
				Log.i(TAG,"getListaBonificacionesPendientes:  " + cur.getString(1)+" saldo: "+cur.getInt(3));
				cur.moveToNext();
			}			
			cur.close();
			db.close();
			
		}catch (Exception e) {
			Log.e("DAO_registroBonificacioens :getRegistroBonificaciones:", e.getMessage());
			e.printStackTrace();
		}
		return lista;
	}
	
	public void AgregarRegistroBonificacionPendiente(String oc_numero,String codigoRegistro,String codigoSalida,String codigoAnterior,int cantidadEntregada,String codigoVendedor, String codigoCliente){				
		try {
			SQLiteDatabase db = getWritableDatabase();

			ContentValues Nreg = new ContentValues();
			Nreg.put(TB_registro_bonificaciones.OC_NUMERO, oc_numero);
			Nreg.put(TB_registro_bonificaciones.SALIDA, codigoSalida);
			Nreg.put(TB_registro_bonificaciones.CODIGO_ANTERIOR, codigoAnterior);
			Nreg.put(TB_registro_bonificaciones.CANTIDAD_ENTREGADA, cantidadEntregada);
			Nreg.put(TB_registro_bonificaciones.CODIGO_VENDEDOR, codigoVendedor);
			Nreg.put(TB_registro_bonificaciones.CODIGO_CLIENTE, codigoCliente);
			
			db.insert(TB_registro_bonificaciones.TAG, null, Nreg);
			db.close();
			Log.i(TAG+":AgregarRegistroBonificacionPendiente", "ITEM PENDIENTE INSERTADO");

		} catch (Exception e) {
			Log.i("REGISTRO BONIFICACIONES", "Error registro insertado");
		}

	}
	
	public ArrayList<DB_RegistroBonificaciones> getPendientesAgregados(String oc_numero) {
		String rawQuery;

		rawQuery = "SELECT oc_numero,salida,cantidadEntregada,codigoAnterior FROM registro_bonificaciones "
				 + "WHERE oc_numero LIKE '"+oc_numero+"' AND codigoAnterior IS NOT NULL AND codigoAnterior <>'' AND cantidadTotal IS NULL";
			
		ArrayList<DB_RegistroBonificaciones> lista = new ArrayList<>();		
		try{
			SQLiteDatabase db = getReadableDatabase();
			Cursor cur = db.rawQuery(rawQuery, null);			
			cur.moveToFirst();

			while (!cur.isAfterLast()) {				
				DB_RegistroBonificaciones item = new DB_RegistroBonificaciones();
				item.setOc_numero(cur.getString(0));
				item.setSalida(cur.getString(1));
				item.setCantidadEntregada(cur.getInt(2));
				item.setCodigoAnterior(cur.getString(3));				
				lista.add(item);
				Log.i(TAG,"getPendientesAgregados:  " + cur.getString(1)+" cantidadEntregada: "+cur.getInt(2));
				cur.moveToNext();
			}
			Log.i(TAG,"getPendientesAgregados:"+lista.size());
			cur.close();
			db.close();
			
		}catch (Exception e) {
			Log.e(TAG+":getPendientesAgregados:", e.getMessage());
			e.printStackTrace();
		}
		return lista;
	}
	
	public DB_RegistroBonificaciones getPendienteAgregado(String oc_numero,String codigoSalida) {
		String rawQuery;

		rawQuery = "SELECT oc_numero,salida,cantidadEntregada,codigoAnterior FROM registro_bonificaciones "
				 + "WHERE oc_numero LIKE '"+oc_numero+"' AND salida like '"+codigoSalida+"' AND codigoAnterior IS NOT NULL AND codigoAnterior <>'' AND cantidadTotal IS NULL";
		Log.d(TAG+":getPendienteAgregado:", rawQuery);
		DB_RegistroBonificaciones item = new DB_RegistroBonificaciones();
		try{
			SQLiteDatabase db = getReadableDatabase();
			Cursor cur = db.rawQuery(rawQuery, null);			
			cur.moveToFirst();
			
			while (!cur.isAfterLast()) {
				item.setOc_numero(cur.getString(0));
				item.setSalida(cur.getString(1));
				item.setCantidadEntregada(cur.getInt(2));
				item.setCodigoAnterior(cur.getString(3));
				Log.i(TAG,"getPendienteAgregado:  " + cur.getString(1)+" cantidadEntregada: "+cur.getInt(2));
				cur.moveToNext();
			}
			cur.close();
			db.close();		
		}catch (Exception e) {
			Log.e(TAG+":getPendientesAgregados:", e.getMessage());
			e.printStackTrace();
		}
		return item;
	}
	
	public void Eliminar_PendienteAgregado(String oc_numero,String salida){
		String where = "oc_numero=? and salida like ? AND codigoAnterior IS NOT NULL AND codigoAnterior <>'' AND cantidadTotal IS NULL  ";
		String[] args = { oc_numero, salida};

		try {
			SQLiteDatabase db = getWritableDatabase();
			db.delete("registro_bonificaciones", where, args);			
			db.close();

			Log.i("Eliminar_PendienteAgregado", "oc_numero: "+oc_numero+" salidaPendiente: "+salida);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void ActualizarRegistroBonificacionPendiente(String oc_numero,String codigoSalida, int cantidadEntregada){
		String where = "oc_numero=? and salida like ? AND codigoAnterior IS NOT NULL AND codigoAnterior <>'' AND cantidadTotal IS NULL  ";
		String[] args = { oc_numero, codigoSalida};
		try {
			SQLiteDatabase db = getWritableDatabase();
			ContentValues reg = new ContentValues();
			reg.put(TB_registro_bonificaciones.CANTIDAD_ENTREGADA, cantidadEntregada);
			db.update("registro_bonificaciones", reg, where, args);
			db.close();

			Log.e("ActualizarRegistroBonificacionPendiente", codigoSalida+" actualizado con cantidadEntregada "+cantidadEntregada);
		} catch (SQLException ex) {
			Log.d("DAO_RegistroBonificaciones :Actualizar_RegistroBonificacion:","Exception");
			ex.printStackTrace();
		}
	}
	/*
	public boolean existeRegistroBonificacion(String codigoRegistro) {
		String rawQuery;
		rawQuery = "select oc_numero from registro_bonificaciones where codigoRegistro like '"+ codigoRegistro + "'";
		boolean existe = false;
		
		try {
			SQLiteDatabase db = getReadableDatabase();
			Cursor cur = db.rawQuery(rawQuery, null);
			cur.moveToFirst();

			while (!cur.isAfterLast()) {
				existe = true;
				cur.moveToNext();
			}

			cur.close();
			db.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return existe;
	}
	*/
	
	public ArrayList<String> getCodigoRegistros(String codigoVendedor) {
		String rawQuery;
		rawQuery = "select codigoRegistro from registro_bonificaciones where codigoVendedor like '"+ codigoVendedor + "' AND codigoRegistro IS NOT NULL AND codigoRegistro <> ''";
		ArrayList<String> lista = new ArrayList<>(); 
		
		try {
			SQLiteDatabase db = getReadableDatabase();
			Cursor cur = db.rawQuery(rawQuery, null);
			cur.moveToFirst();

			while (!cur.isAfterLast()) {
				lista.add(cur.getString(0));
				Log.d(TAG,"getCodigoRegistros: " + cur.getString(0));
				cur.moveToNext();
			}

			cur.close();
			db.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return lista;
	}
	
	public ArrayList<DB_Pedido_Detalle_Promocion> getBonificacionColores(String secuencia) {
		String rawQuery;

		rawQuery = "SELECT Producto,cc_artic from bonificacion_colores where secuencia ="+secuencia+" GROUP BY Producto";
		ArrayList<DB_Pedido_Detalle_Promocion> lista = new ArrayList<DB_Pedido_Detalle_Promocion>();	
		DB_Pedido_Detalle_Promocion row;
		try{
			SQLiteDatabase db = getReadableDatabase();
			Cursor cur = db.rawQuery(rawQuery, null);			
			cur.moveToFirst();

			while (!cur.isAfterLast()) {	
				row=new DB_Pedido_Detalle_Promocion();
				row.setDescripcion(cur.getString(0));
				row.setCc_artic(cur.getString(1));
				lista.add(row);
				Log.i(TAG,"Producto:  " + cur.getString(0));
				cur.moveToNext();
			}
			Log.i(TAG,"TOTAL:"+lista.size());
			cur.close();
			db.close();
			
		}catch (Exception e) {
			Log.e(TAG+":bonif Colores:", e.getMessage());
			e.printStackTrace();
		}
		return lista;
	}
	
	public String getSecuenciaporEntrada(String salida, String oc_numero){
			//String cip=salida.substring(1);
			String rawQuery="select secuenciaPromocion as secuenciapromocion from "+ TB_registro_bonificaciones.TAG+" where salida = '"+salida+"' and oc_numero='"+oc_numero+"' ";
			Log.i(TAG,"Secuencia por Entrada:  " + rawQuery);
			String secuenciapromocion="";

			try{
				SQLiteDatabase db = getReadableDatabase();
				Cursor cur = db.rawQuery(rawQuery, null);
				 if (cur.moveToNext()){
                     secuenciapromocion=cur.getString(cur.getColumnIndex("secuenciapromocion"));
                 }
				Log.i(TAG,"getSecuenciaporEntrada:: secuencia:"+secuenciapromocion);
				cur.close();
				db.close();

			}catch (Exception e) {
				Log.e(TAG+":Secuencia por Entrada:", e.getMessage());
				e.printStackTrace();
			}
			return secuenciapromocion;
	}
	
	public boolean validarColorBono(String secuencia){
		
		String rawQuery="select count (*) from bonificacion_colores where secuencia='"+ secuencia + "'";
		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);

		cur.moveToFirst();
		int cantidad = 0;
		while (!cur.isAfterLast()) {
			cantidad = cur.getInt(0);

			cur.moveToNext();
		}
		cur.close();
		db.close();

		if (cantidad > 1) {
			return true;
		} else {
			return false;
		}
	}

	public void AgregarPedidoDetallePromo(DB_Pedido_Detalle_Promocion item,String oc_numero,String cip, String cc_artic) {
		
		String where = "oc_numero=? and cip=? and cc_artic=?";
		String[] args = { oc_numero, cip, cc_artic };

		try {
			SQLiteDatabase db = getWritableDatabase();
			db.delete(DBtables.Pedido_Detalle_Promocion.TAG, where, args);
			
			db.close();

			Log.i("eliminando detalle de PROMOCION anterior", "Pedido detalle promocion eliminado: " +oc_numero);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		

		try {
			SQLiteDatabase db = getWritableDatabase();

			ContentValues Nreg = new ContentValues();
			Nreg.put(DBtables.Pedido_Detalle_Promocion.OC_NUMERO, item.getOc_numero());
			Nreg.put(DBtables.Pedido_Detalle_Promocion.CIP, item.getCip());
			Nreg.put(DBtables.Pedido_Detalle_Promocion.CC_ARTIC, item.getCc_artic());
			Nreg.put(DBtables.Pedido_Detalle_Promocion.SECUENCIA, item.getSecuencia());
			Nreg.put(DBtables.Pedido_Detalle_Promocion.CANTIDAD, item.getCantidad());
			Nreg.put(DBtables.Pedido_Detalle_Promocion.DESCRIPCION, item.getDescripcion());
			

			db.insert(DBtables.Pedido_Detalle_Promocion.TAG, null, Nreg);
			db.close();
			Log.i("PEDIDO_DETALLE_PROMOCION", "ITEM INSERTADO" +item.getOc_numero()+" cant "+item.getCantidad());

		} catch (Exception e) {
			Log.i("PEDIDO_DETALLE_PROMOCION", "Error registro insertado");
		}

	}
	
	public void eliminar_detalle_promocion(String oc_numero,String cip) {

		String where = "oc_numero=? and cip=?";
		String[] args = { oc_numero,cip};

		try {
			SQLiteDatabase db = getWritableDatabase();
			db.delete(DBtables.Pedido_Detalle_Promocion.TAG, where, args);
			
			db.close();

			Log.i("eliminar_detalle_promocion", "promo eliminada: OC= " +oc_numero+" CIP= "+cip);

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}
