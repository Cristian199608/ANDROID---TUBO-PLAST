package com.example.sm_tubo_plast.genesys.datatypes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.BEAN.Cliente;
import com.example.sm_tubo_plast.genesys.BEAN.Expectativa;
import com.example.sm_tubo_plast.genesys.BEAN.ItemProducto;
import com.example.sm_tubo_plast.genesys.BEAN.Motivo;
import com.example.sm_tubo_plast.genesys.BEAN.PedidoCabeceraRecalcular;
import com.example.sm_tubo_plast.genesys.BEAN.ResumenVentaTipoProducto;
import com.example.sm_tubo_plast.genesys.BEAN.San_Opciones;
import com.example.sm_tubo_plast.genesys.BEAN.San_Visitas;
import com.example.sm_tubo_plast.genesys.CreatePDF.model.CTA_INGRESOSPDF;
import com.example.sm_tubo_plast.genesys.DAO.DAO_RegistroBonificaciones;
import com.example.sm_tubo_plast.genesys.DAO.DAO_San_Visitas;
import com.example.sm_tubo_plast.genesys.adapters.ModelDevolucionProducto;
import com.example.sm_tubo_plast.genesys.datatypes.DBtables.Direccion_cliente;
import com.example.sm_tubo_plast.genesys.datatypes.DBtables.Pedido_cabecera;
import com.example.sm_tubo_plast.genesys.datatypes.DBtables.Pedido_detalle;
import com.example.sm_tubo_plast.genesys.datatypes.DBtables.Politica_precio2;
import com.example.sm_tubo_plast.genesys.datatypes.DBtables.Producto;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.PedidosActivity;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.SincronizarActivity;
import com.example.sm_tubo_plast.genesys.service.ConnectionDetector;
import com.example.sm_tubo_plast.genesys.session.SessionManager;
import com.example.sm_tubo_plast.genesys.util.GlobalFunctions;
import com.example.sm_tubo_plast.genesys.util.GlobalVar;
import com.example.sm_tubo_plast.genesys.util.VARIABLES;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

//import android.test.IsolatedContext;

@SuppressLint("LongLogTag")
public class DBclasses extends SQLiteAssetHelper {

	private static final String TAG = "DBclasses";
	public static final String KEY_ROWID = "_id";
	public String codAlmacen = "01";
	// flag for Internet connection status
	Boolean isInternetPresent = false;

	// Connection detector class
	ConnectionDetector cd;
	Gson gson = new Gson();
	private Context _context;
	Calendar calendar = Calendar.getInstance();

	public DBclasses(Context context) {
		super(context, VARIABLES.ConfigDatabase.getDatabaseName(), null, VARIABLES.ConfigDatabase.getDatabaseVersion());
		// super(context, "fuerzaventas_backup", null, DATABASE_VERSION);
		_context = context;
	}

	/*
	 * @Override public void onCreate(SQLiteDatabase db) {
	 * //Log.w(EasymenuNGActivity.TAG+"."+TAG,"Creating the database");
	 * db.execSQL(Cliente.CREATE_STATEMENT);
	 * db.execSQL(Cta_ingresos.CREATE_STATEMENT);
	 * db.execSQL(Familia.CREATE_STATEMENT);
	 * db.execSQL(Ingresos.CREATE_STATEMENT);
	 * db.execSQL(MTA_kardex.CREATE_STATEMENT);
	 * db.execSQL(Pedido_cabecera.CREATE_STATEMENT);
	 * db.execSQL(Pedido_detalle.CREATE_STATEMENT);
	 * db.execSQL(Politica_precio2.CREATE_STATEMENT);
	 * db.execSQL(Politica_cliente.CREATE_STATEMENT);
	 * db.execSQL(Producto.CREATE_STATEMENT);
	 * db.execSQL(Promocion_clientes.CREATE_STATEMENT);
	 * db.execSQL(Sub_familia.CREATE_STATEMENT);
	 * db.execSQL(Tb_promocion_detalle.CREATE_STATEMENT);
	 * db.execSQL(Unidad_medida.CREATE_STATEMENT);
	 * db.execSQL(Vendedor.CREATE_STATEMENT);
	 * db.execSQL(Usuarios.CREATE_STATEMENT);
	 * db.execSQL(Direccion_cliente.CREATE_STATEMENT);
	 * 
	 * 
	 * BufferedReader br = new BufferedReader(new
	 * InputStreamReader(_context.getResources
	 * ().openRawResource(R.raw.emngsql)));
	 * 
	 * String line = null;
	 * 
	 * try { while((line = br.readLine())!=null){ db.execSQL(line);
	 * Log.i("EasymenuNG","Line "+line); } } catch (IOException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } }
	 */

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
				+ newVersion + ", which will destroy all old data");
		/*
		 * db.execSQL("DROP TABLE IF EXISTS " + Cliente.TAG);
		 * db.execSQL("DROP TABLE IF EXISTS " + Cta_ingresos.TAG);
		 * db.execSQL("DROP TABLE IF EXISTS " + Familia.TAG);
		 * db.execSQL("DROP TABLE IF EXISTS " + Ingresos.TAG);
		 * db.execSQL("DROP TABLE IF EXISTS " + MTA_kardex.TAG);
		 * db.execSQL("DROP TABLE IF EXISTS " + Pedido_cabecera.TAG);
		 * db.execSQL("DROP TABLE IF EXISTS " + Pedido_detalle.TAG);
		 * db.execSQL("DROP TABLE IF EXISTS " + Politica_precio2.TAG);
		 * db.execSQL("DROP TABLE IF EXISTS " + Politica_cliente.TAG);
		 * db.execSQL("DROP TABLE IF EXISTS " + Producto.TAG);
		 * db.execSQL("DROP TABLE IF EXISTS " + Promocion_clientes.TAG);
		 * db.execSQL("DROP TABLE IF EXISTS " + Sub_familia.TAG);
		 * db.execSQL("DROP TABLE IF EXISTS " + Tb_promocion_detalle.TAG);
		 * db.execSQL("DROP TABLE IF EXISTS " + Unidad_medida.TAG);
		 * db.execSQL("DROP TABLE IF EXISTS " + Vendedor.TAG);
		 * db.execSQL("DROP TABLE IF EXISTS " + Usuarios.TAG);
		 * db.execSQL("DROP TABLE IF EXISTS " + Direccion_cliente.TAG);
		 * db.execSQL("DROP TABLE IF EXISTS " + Banco.TAG);
		 * db.execSQL("DROP TABLE IF EXISTS " + Motivo_noventa.TAG);
		 * db.execSQL("DROP TABLE IF EXISTS " + Ctas_xbanco.TAG);
		 * db.execSQL("DROP TABLE IF EXISTS " + Depositos.TAG);
		 * db.execSQL("DROP TABLE IF EXISTS " + Politica_vendedor.TAG);
		 * db.execSQL("DROP TABLE IF EXISTS " + Pedido_devolucion.TAG);
		 */

		onCreate(db);
		db.close();
	}
	public void EliminaOldDatabase(){


		String S_TAG="EliminaOldDatabase:: ";
		try {
			for (String dbOld : VARIABLES.ConfigDatabase.getDatabaseNameOld()) {
				_context.deleteDatabase(dbOld);
				Log.i(TAG, S_TAG+" se ha eliminado el anterior db "+ dbOld);
			}
		} catch (Exception e) {
			Log.e(TAG, S_TAG+"Ohh no se pudo eliminar la base de datos antiguo "+ VARIABLES.ConfigDatabase.getDatabaseNameOld().toString());
			e.printStackTrace();
		}
	}
	public Cursor fetchProductsByName(String inputText) throws SQLException {
		Log.w(TAG, inputText);
		SQLiteDatabase mDb = getReadableDatabase();
		Cursor mCursor = null;
		String rawQuery;
		Cursor cur = null;

		if (inputText == null || inputText.length() == 0) {
			rawQuery = "select _id, producto.codpro, despro, cod_rapido, (stock - xtemp) as stock, ean13 from producto inner join mta_kardex on "
					+ " producto.codpro = mta_kardex.codpro where codalm ='"
					+ codAlmacen + "'";
			Log.e("ALERT-1-", " " + rawQuery);
			SQLiteDatabase db = getReadableDatabase();
			cur = db.rawQuery(rawQuery, null);

			mCursor = mDb.query("producto", new String[] { KEY_ROWID, "codpro",
					"despro", "cod_rapido" }, null, null, null, null, null);

		} else {

			rawQuery = "select  _id,producto.codpro, despro, cod_rapido, (stock - xtemp) as stock, ean13 from producto inner join "
					+ " mta_kardex on  producto.codpro = mta_kardex.codpro where codalm ='"
					+ codAlmacen + "' and  despro like '%" + inputText + "%'  ";

			Log.d("ALERT2", "X'C " + rawQuery);

			SQLiteDatabase db = getReadableDatabase();
			cur = db.rawQuery(rawQuery, null);

			mCursor = mDb.query(true, "producto", new String[] { KEY_ROWID,
					"codpro", "despro", "cod_rapido" }, "despro" + " like '%"
					+ inputText + "%'", null, null, null, null, null);

		}

		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		if (cur != null) {
			cur.moveToFirst();
		}
		return cur;

	}

	public  String getVendedorByCodven(String codVen) {

		String vendedor_name=""+codVen;
		String rawQuery; //SELECT nomven from vendedor  WHERE codven='000110'
		//rawQuery = "SELECT vendedor.nomven from vendedor  WHERE vendedor.codven= 000110";

		//rawQuery = "select * from usuarios  " + "where usuarios.useusr='"+ codVen+"'";
		rawQuery = "select  nomven from vendedor  where codven='"+codVen+"'";
		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		if (cur.moveToNext()) {
			vendedor_name=(cur.getString(cur.getColumnIndex("nomven")));
		}
		cur.close();
		db.close();
		return vendedor_name;
	}

	public Cursor fetchProductsByCodigo(String inputText) throws SQLException {
		Log.w(TAG, inputText);
		SQLiteDatabase mDb = getReadableDatabase();
		Cursor mCursor = null;
		String rawQuery;
		Cursor cur = null;

		if (inputText == null || inputText.length() == 0) {
			rawQuery = "select  _id,producto.codpro, despro, cod_rapido, "
					+ "ifnull((select (stock - xtemp ) from mta_kardex where codpro=mta_kardex.codpro and codalm ='"+codAlmacen+"') , '0') as stock, ean13 "
					+ "from producto ";
			Log.e("ALERT-1-", " " + rawQuery);
			SQLiteDatabase db = getReadableDatabase();
			cur = db.rawQuery(rawQuery, null);

			mCursor = mDb.query("producto", new String[] { KEY_ROWID, "codpro",
					"despro", "cod_rapido" }, null, null, null, null, null);

		} else {
/*
			rawQuery = 
					"select  _id,producto.codpro, despro, cod_rapido, (stock - xtemp) as stock, ean13 from producto inner join "
					+ " mta_kardex on  producto.codpro = mta_kardex.codpro where codalm ='"
					+ codAlmacen
					+ "' and  producto.codpro like '%"
					+ inputText
					+ "%'  ";*/
			
			rawQuery =
					"select  _id,producto.codpro, despro, cod_rapido, "
					+ "ifnull((select (stock - xtemp ) from mta_kardex where codpro=mta_kardex.codpro and codalm ='"+codAlmacen+"') , '0') as stock, ean13 "
					+ "from producto  where  producto.codpro like '"+inputText+"' ";

			SQLiteDatabase db = getReadableDatabase();
			cur = db.rawQuery(rawQuery, null);

			mCursor = mDb.query(true, "producto", new String[] { KEY_ROWID,
					"codpro", "despro", "cod_rapido" }, "codpro" + " like '%"
					+ inputText + "%'", null, null, null, null, null);

		}

		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		if (cur != null) {
			cur.moveToFirst();
		}
		return cur;

	}

	public Cursor fetchAllProducts() {
		SQLiteDatabase mDb = getReadableDatabase();

		String rawQuery;

		/*rawQuery = "select _id, producto.codpro, despro, cod_rapido, (stock - xtemp) as stock, ean13 from producto inner join mta_kardex on "
				+ " producto.codpro = mta_kardex.codpro where codalm ='"
				+ codAlmacen + "'";*/
		
		//Mostrar todos los productos		
		rawQuery = "select _id, producto.codpro, despro, cod_rapido,"
				+ "ifnull((SELECT (stock - xtemp) from mta_kardex where codpro=producto.codpro),0) as stock, ean13 "
				+ "from producto";
		Log.e("ALERT 3--", " " + rawQuery);

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);

		/*
		 * Cursor mCursor = mDb.query("producto", new String[] {
		 * KEY_ROWID,"codpro","despro" , "cod_rapido"}, null, null, null, null,
		 * null);
		 */

		/*
		 * if (mCursor != null) { mCursor.moveToFirst(); }
		 */
		if (cur != null) {
			cur.moveToFirst();
		}
		// return mCursor;
		return cur;
	}

	public ArrayList<DBUsuarios> getUsuarios(String user, String pass) {

		String rawQuery;

		rawQuery = "select * from usuarios  " + "where usuarios.useusr ='"
				+ user + "' and usepas = '" + pass + "'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		ArrayList<DBUsuarios> list_DBUsuarios = new ArrayList<DBUsuarios>();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			DBUsuarios objDBUsuarios = new DBUsuarios();
			objDBUsuarios.setUsecod(cur.getInt(0));
			objDBUsuarios.setUsepas(cur.getString(1));
			objDBUsuarios.setUsenam(cur.getString(2));
			objDBUsuarios.setUseusr(cur.getString(3));
			objDBUsuarios.setUsesgl(cur.getString(4));

			list_DBUsuarios.add(objDBUsuarios);
			cur.moveToNext();
		}
		cur.close();
		db.close();
		return list_DBUsuarios;
	}

	public String[] getUndmedidas(String codpro) {
		String rawQuery = "select unidad_medida.desunimed,(select unidad_medida.desunimed from "
				+ "unidad_medida inner join producto on unidad_medida.codunimed=producto.codunimed_almacen "
				+ "where producto.codpro='"
				+ codpro
				+ "') from unidad_medida inner "
				+ "join producto on unidad_medida.codunimed=producto.codunimed where "
				+ "producto.codpro='" + codpro + "'";
		
		
		
		//Log.d("BDclasses ::getUndmedidas::Query -->",rawQuery);
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(rawQuery, null);

		String[] datos = new String[2];
		String[] datos2 = new String[1];

		if (cursor.moveToFirst()) {
			int i = 0;
			do {
				datos[i] = cursor.getString(0);
				datos[i + 1] = cursor.getString(1);
			} while (cursor.moveToNext());
		}else{
			cursor.close();
			db.close();
			String vacio[] = {""};
			return vacio;
		}

		cursor.close();
		db.close();

		if (datos[0].equals(datos[1])) {
			datos2[0] = datos[1];
			return datos2;

		} else {

			return datos;
		}
	}

	public ArrayList<DBClientes> getClientes(String buscar) {

		String codven = new SessionManager(_context).getCodigoVendedor();
		String rawQuery;

		int dia = getDiaConfiguracion();

		if (buscar.equals("visita")) {
			/*
			 * rawQuery=
			 * "select DISTINCT(cliente.codcli),nomcli,ruccli,stdcli,comprobante,email,tipo_documento, "
			 * +
			 * "tipo_cliente, tiempo_credito,limite_credito,persona,afecto, condicion_venta , latitud, longitud from "
			 * +
			 * " cliente inner join direccion_cliente on direccion_cliente.codcli=cliente.codcli where direccion_cliente.orden > 0 "
			 * ;
			 */
			/*
			 * rawQuery=
			 * "select (cliente.codcli),nomcli,ruccli,stdcli,comprobante,email,tipo_documento, "
			 * +
			 * "tipo_cliente, tiempo_credito,limite_credito,persona,afecto, condicion_venta , latitud, longitud from "
			 * +
			 * " cliente inner join direccion_cliente on direccion_cliente.codcli=cliente.codcli order by orden"
			 * ;
			 */

			rawQuery = "SELECT  (direccion_cliente.codcli), nomcli, ruccli ,"
					+ "  fecha_compra, monto_compra "
					+ " FROM cliente inner join"
					+ "direccion_cliente on  direccion_cliente.codcli=cliente.codcli where direccion_cliente.codven='"
					+ codven + "'  ORDER BY CAST(ORDEN as integer)";
		} else
			// rawQuery= "select * from cliente";
			rawQuery = "SELECT  distinct(cliente.codcli), nomcli,  ruccli ,"
					+ " fecha_compra, monto_compra, znf_programacion_clientes.item_dircli "
					+ "  FROM cliente inner join znf_programacion_clientes on"
					+ "  znf_programacion_clientes.codcli=cliente.codcli where  znf_programacion_clientes.codven='"
					+ codven + "'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		ArrayList<DBClientes> dbclientes = new ArrayList<DBClientes>();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			DBClientes dbc = new DBClientes();
			dbc.setCodcli(cur.getString(0));
			dbc.setNomcli(cur.getString(1));
			dbc.setRuccli(cur.getString(2));
			dbc.setFecha_compra(cur.getString(3));
			dbc.setMonto_compra(cur.getString(4));
			dbc.setAfecto(cur.getString(5));

			dbclientes.add(dbc);
			cur.moveToNext();
		}
		cur.close();
		db.close();

		return dbclientes;

	}

	public DBClientes getClientexCodigo(String codigo) {

		String rawQuery;

		int dia = getDiaConfiguracion();

		rawQuery = "SELECT  codcli, nomcli, ruccli ,"
				+ "  fecha_compra, monto_compra "
				+ " FROM cliente  where codcli='" + codigo + "'  ";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		DBClientes dbc = null;
		cur.moveToFirst();

		while (!cur.isAfterLast()) {
			dbc=new DBClientes();
			dbc.setCodcli(cur.getString(0));
			dbc.setNomcli(cur.getString(1));
			dbc.setRuccli(cur.getString(2));
			dbc.setFecha_compra(cur.getString(3));
			dbc.setMonto_compra(cur.getString(4));

			cur.moveToNext();
		}
		cur.close();
		db.close();

		return dbc;

	}

	public ArrayList<DB_ZnfProgramacionClientes> getProgramacionxDia() {

		String codven = new SessionManager(_context).getCodigoVendedor();
		String rawQuery;

		int dia = getDiaConfiguracion();

		rawQuery = "select codcli,item_dircli,n_dia  from znf_programacion_clientes where codven='"
				+ codven
				+ "' and n_dia='"
				+ dia
				+ "' and codcli in(select codcli from cliente) order by orden_r, orden_z, orden_c";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		ArrayList<DB_ZnfProgramacionClientes> dbZona = new ArrayList<DB_ZnfProgramacionClientes>();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			DB_ZnfProgramacionClientes dbc = new DB_ZnfProgramacionClientes();
			dbc.setCodcli(cur.getString(0));
			dbc.setItem_dircli(cur.getInt(1));
			dbc.setN_dias(cur.getInt(2));

			dbZona.add(dbc);
			cur.moveToNext();
		}
		cur.close();
		db.close();

		return dbZona;

	}

	public ArrayList<HashMap<String, Object>> getProgramacionxDia2( String txtBusqueda,
																	int start,
																	int  end, int bucle,
																	String fecha_formateado,
																	boolean soloAnulados,
																	String tipo_cliente_valor
																	 ) {

		String codven = new SessionManager(_context).getCodigoVendedor();
		String rawQuery;

		int dia = 0; //getDiaConfiguracion();
		String addwhere="";
		if (fecha_formateado.length()>0){
			addwhere=" and (" +
					"'"+fecha_formateado+"' in  (select  strftime('%Y-%m-%d',max(datetime(sv.Fecha_Ejecutada)) ) from SAN_VISITAS sv where sv.id_rrhh=cliente.codcli) "+
					" or '"+fecha_formateado+"' in  (select  strftime('%Y-%m-%d',max(datetime(sv.Fecha_planificada)) ) from SAN_VISITAS sv where sv.id_rrhh=cliente.codcli) " +
					") ";
		}
		if (soloAnulados){
			addwhere+=" and cliente.stdcli='0' ";
		}else addwhere+=" and cliente.stdcli='1' ";

		if (tipo_cliente_valor!=null){
			addwhere+=" and cliente.sistema='"+tipo_cliente_valor+"' ";
		}

		rawQuery = "select * from (select distinct(nomcli) as nomcli,ruccli,fecha_compra,monto_compra,cliente.codcli,item_dircli,n_dia,ifnull(observacion,'') as observacion, " +
				"ifnull((" +
				"  dc.direccion), 'Sin dirección') as direccion," +
				"cliente.codcli as codcli," +
				"cliente.stdcli as estado_cli," +
				"ifnull(motivo, '') as motivoBajaCliente," +
				"cliente.sistema, " +
				"cliente.moneda_ultima_compra " +
				" from "
				+ "znf_programacion_clientes inner join cliente on znf_programacion_clientes.codcli=cliente.codcli "
				+"inner join "+Direccion_cliente.TAG+" dc on   dc.codcli=cliente.codcli and dc.item=item_dircli "
				+" left join cliente_estado ce on ce.codcli= cliente.codcli "
				+ "where znf_programacion_clientes.codven='"  + codven+"' "
				+"and (nomcli like '%"+txtBusqueda+"%' or ruccli like '%"+txtBusqueda+"%' or direccion like '%"+txtBusqueda+"%' ) "
				+""+addwhere+""
				+ " limit "+start+", "+end+" "
				+") result "
				+ "order by nomcli asc  ";

		SQLiteDatabase db = getReadableDatabase();

		Cursor cur = db.rawQuery(rawQuery, null);
		Log.i(TAG, "listart cliente: query_> "+rawQuery);
		ArrayList<HashMap<String, Object>> dbcliente = new ArrayList<HashMap<String, Object>>();
		if (cur.isClosed() && bucle>50){
			bucle--;
			return getProgramacionxDia2( txtBusqueda, start, end, bucle, fecha_formateado, soloAnulados, tipo_cliente_valor);
		}
		int coint=cur.getCount();
		db.close();

		while (cur.moveToNext()) {

			HashMap<String, Object> dbc = new HashMap<String, Object>();

			dbc.put("name", cur.getString(0));
			dbc.put("codcli", cur.getString(cur.getColumnIndex("codcli")));
			dbc.put("ruc", cur.getString(1));
			dbc.put("observacion", cur.getString(7));
			dbc.put("fecha", cur.getString(2));
			dbc.put("monto", cur.getString(3));
			dbc.put("moneda_ultima_compra", cur.getString(cur.getColumnIndex("moneda_ultima_compra")));
			dbc.put("codigo", cur.getString(4));
			dbc.put("item_direccion", cur.getInt(5));
			dbc.put("n_dia", cur.getInt(6));
			dbc.put("direccion", cur.getString(8));

			int estado_pedido = obtenerPedidosXCodcli(dbc.get("codigo").toString(),dbc.get("item_direccion").toString());
			String estado_localizacion = getEstadoDireccionCliente(dbc.get("codigo").toString(), dbc.get("item_direccion").toString());

			dbc.put("estado_localizacion", estado_localizacion);
			dbc.put("estado_pedido", estado_pedido);
			dbc.put("estado_cli", cur.getString(cur.getColumnIndex("estado_cli")));
			dbc.put("motivoBajaCliente", cur.getString(cur.getColumnIndex("motivoBajaCliente")));
			dbc.put("sistema", cur.getString(cur.getColumnIndex("sistema")));

			dbcliente.add(dbc);
		}
		cur.close();


		return dbcliente;

	}


	public ArrayList<DB_ZnfProgramacionClientes> getDemasClientes() {

		String codven = new SessionManager(_context).getCodigoVendedor();
		String rawQuery;

		int dia = getDiaConfiguracion();

		rawQuery = "SELECT  distinct(cliente.codcli),znf_programacion_clientes.item_dircli,znf_programacion_clientes.n_dia FROM cliente inner "
				+ "join znf_programacion_clientes on znf_programacion_clientes.codcli=cliente.codcli where "
				+ "znf_programacion_clientes.codven='"
				+ codven
				+ "' and cliente.codcli not in (select codcli from "
				+ "znf_programacion_clientes where codven='"
				+ codven
				+ "' and n_dia='" + dia + "' )";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		ArrayList<DB_ZnfProgramacionClientes> dbZona = new ArrayList<DB_ZnfProgramacionClientes>();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			DB_ZnfProgramacionClientes dbc = new DB_ZnfProgramacionClientes();
			dbc.setCodcli(cur.getString(0));
			dbc.setItem_dircli(cur.getInt(1));
			dbc.setN_dias(cur.getInt(2));

			dbZona.add(dbc);
			cur.moveToNext();
		}
		cur.close();
		db.close();

		return dbZona;

	}

	public ArrayList<HashMap<String, Object>> getDemasClientes2() {

		String codven = new SessionManager(_context).getCodigoVendedor();
		String rawQuery;

		int dia = getDiaConfiguracion();

		rawQuery = "select distinct(cliente.codcli),nomcli,ruccli,fecha_compra,monto_compra,"
				+ "znf_programacion_clientes.item_dircli,znf_programacion_clientes.n_dia,ifnull(observacion,''), " +
				"ifnull((" +
				"select dc.direccion from "+ Direccion_cliente.TAG+" dc where dc.codcli=cliente.codcli and dc.item=item_dircli), 'Sin dirección') as direccion " +
				" from "
				+ "cliente inner join znf_programacion_clientes on znf_programacion_clientes.codcli=cliente.codcli "
				+ "where znf_programacion_clientes.codven='"
				+ codven
				+ "' and cliente.codcli "+
				"order by nomcli asc";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		ArrayList<HashMap<String, Object>> dbcliente = new ArrayList<HashMap<String, Object>>();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			HashMap<String, Object> dbc = new HashMap<String, Object>();

			dbc.put("name", cur.getString(1));
			dbc.put("ruc", cur.getString(2));
			dbc.put("observacion", cur.getString(7));
			dbc.put("fecha", cur.getString(3));
			dbc.put("monto", cur.getString(4));
			dbc.put("codigo", cur.getString(0));
			dbc.put("item_direccion", cur.getInt(5));
			dbc.put("n_dia", cur.getInt(6));
			dbc.put("direccion", cur.getString(8));

			dbcliente.add(dbc);
			cur.moveToNext();
		}
		cur.close();
		db.close();

		return dbcliente;

	}

	public String getObservacionCliente(String codcli) {
		// TODO Auto-generated method stub
		String rawQuery;
		rawQuery = "select ifnull(observacion,'') from cliente where codcli='"
				+ codcli + "'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		cur.moveToFirst();
		String observacion = "";
		if (cur.moveToFirst()) {

			do {
				observacion = cur.getString(0);
			} while (cur.moveToNext());

		}
		cur.close();
		db.close();
		return observacion;
	}

	public int getDiaConfiguracion() {
		// TODO Auto-generated method stub
		String rawQuery;
		rawQuery = "select valor from configuracion where nombre='dia'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		cur.moveToFirst();
		int dia = 0;
		if (cur.moveToFirst()) {

			do {
				dia = cur.getInt(0);
			} while (cur.moveToNext());

		}
		cur.close();
		db.close();
		return dia;
	}

	public int getSecPoliticaConfiguracion() {
		// TODO Auto-generated method stub
		String rawQuery;
		rawQuery = "select valor from configuracion where nombre='sec_politica'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		cur.moveToFirst();
		int dia = 0;
		if (cur.moveToFirst()) {

			do {
				dia = cur.getInt(0);
			} while (cur.moveToNext());

		}
		cur.close();
		db.close();
		return dia;
	}

	public int getCountPoliticaCliente(String codcli) {
		// TODO Auto-generated method stub
		String rawQuery;
		rawQuery = "select count(*) from politica_cliente where codcli='"
				+ codcli + "'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		cur.moveToFirst();
		int count = 0;
		if (cur.moveToFirst()) {

			do {
				count = cur.getInt(0);
			} while (cur.moveToNext());

		}
		cur.close();
		db.close();
		return count;
	}

	public ArrayList<DBClientes> getClientesxCodigo(String codcli) {

		String rawQuery;

		rawQuery = "select * from cliente where codcli='" + codcli + "'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		ArrayList<DBClientes> dbclientes = new ArrayList<DBClientes>();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			DBClientes dbc = new DBClientes();
			dbc.setCodcli(cur.getString(0));
			dbc.setNomcli(cur.getString(1));
			dbc.setRuccli(cur.getString(2));

			dbc.setStdcli(cur.getString(3));
			dbc.setComprobante(cur.getString(4));

			dbc.setEmail(cur.getString(5));
			dbc.setTipo_documento(cur.getInt(6));
			dbc.setTipo_cliente(cur.getInt(7));
			dbc.setTiempo_credito(cur.getInt(8));
			dbc.setLimite_credito(cur.getFloat(9));
			dbc.setPersona(cur.getString(10));
			dbc.setAfecto(cur.getString(11));

			dbc.setCondicion_venta(cur.getString(12));

			dbclientes.add(dbc);
			cur.moveToNext();
		}
		cur.close();
		db.close();

		return dbclientes;

	}

	public ArrayList<DBCta_Ingresos> VerificarCtasXCobrar(String cl) {
		String rawQuery;

		// rawQuery=
		// "Select * from cta_ingresos where cta_ingresos.codcli='"+cl+"'";
		//--PERCEPCIONES DE FACTURA PF NO SE DEBEN MOSTRAR--
		rawQuery = "Select secuencia,"
					+ "case codmon when 1 then 'S/.' " 
								+ "when 2 then '$.' "
					+ "end as codmon, "
					+ "ifnull((select rm.valor from registrosGeneralesMovil rm where rm.codigoDescripcion=coddoc limit 1), 'Documento '||coddoc) as coddoc, "
					+"case serie_doc when  '' then '' "
												   + "else (serie_doc||'') "
					+ "end as serie_doc, "
					+ "numero_factura,  total, "
				    + "acuenta,saldo,feccom,codcli,username,fecha_vencimiento,codven, "
				    + "round(saldo_virtual,2) saldo_virtual,coddoc,Observaciones,forma, cc_flag, Estado_Cobranza  as tipo,NroUnicoBanco "
				    + "from cta_ingresos "
				    + "where cta_ingresos.codcli='"+ cl + "' and coddoc <> 'PF'  order by cc_flag desc";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		ArrayList<DBCta_Ingresos> dbcta_ingresos = new ArrayList<DBCta_Ingresos>();
		cur.moveToFirst();
		
		while (!cur.isAfterLast()) {

			DBCta_Ingresos dbcta = new DBCta_Ingresos();

			dbcta.setSecuencia(cur.getString(0));// N item
			dbcta.setCodmon(cur.getString(1));
			dbcta.setCoddoc(cur.getString(2));// Tipo de Documento

			dbcta.setSerie_doc(cur.getString(3));
			dbcta.setNumero_factura(cur.getString(4));
			dbcta.setTotal(cur.getString(5));
			dbcta.setAcuenta(cur.getString(6));// No dan ese dato
			dbcta.setSaldo(cur.getString(7));// Como el total casi siempre es el saldo
			dbcta.setFeccom(cur.getString(8));// fecha de vencimiento
			dbcta.setCodcli(cur.getString(9));// ruc del cliente
			dbcta.setUsername(cur.getString(10));// codigo del vendedor
			dbcta.setFecha_vencimiento(cur.getString(11));// fecha de vencimiento
			dbcta.setCodven(cur.getString(12));
			dbcta.setSaldo_virtual(cur.getString(13));
			dbcta.setTipo(cur.getString(14));
			
			dbcta.setObservaciones((cur.getString(15)));
			
			dbcta.setForma((cur.getString(16)));
			dbcta.setCc_flag((cur.getString(17)));
			dbcta.setEstado_cobranza((cur.getString(18)));
			dbcta.setNroUnicoBanco((cur.getString(19)));			
			//Log.i("Enviando parametros : numfactura:",(cur.getString(15))+", serie:"+(cur.getString(3))+", tipo:"+(cur.getString(14)));
			
			dbcta_ingresos.add(dbcta);
			cur.moveToNext();
		}
		cur.close();
		db.close();
		return dbcta_ingresos;

	}

	public ArrayList<DBIngresos> ObtenerDetalleCtaXCobrar(String numfactura) {
		String rawQuery;

		rawQuery = "select * from  ingresos inner join cta_ingresos  on  cta_ingresos.secuencia=ingresos.secuencia  where cta_ingresos.secuencia=(select cta_ingresos.secuencia from cta_ingresos where cta_ingresos.numero_factura='"
				+ numfactura + "')";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		ArrayList<DBIngresos> ingresos = new ArrayList<DBIngresos>();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			DBIngresos dbing = new DBIngresos();
			dbing.setSecuencia(cur.getString(0));
			dbing.setSecitm(cur.getString(1));
			dbing.setFecpag(cur.getString(2));
			dbing.setTotal(cur.getString(3));
			dbing.setAcuenta(cur.getString(4));
			dbing.setSaldo(cur.getString(5));
			dbing.setCodcue(cur.getString(6));
			dbing.setNumope(cur.getString(7));
			dbing.setCodforpag(cur.getString(8));
			dbing.setTipo_cambio(cur.getString(9));
			dbing.setCodmon(cur.getString(10));
			dbing.setMonto_afecto(cur.getString(11));
			dbing.setUsername(cur.getString(12));
			dbing.setFecoperacion(cur.getString(13));
			dbing.setFlag(cur.getString(14));

			ingresos.add(dbing);
			cur.moveToNext();
		}
		cur.close();
		db.close();
		return ingresos;

	}

	public String[] getAllClient(String codven) {
		String rawQuery;
		rawQuery = "select nomcli from cliente where codcli in(select codcli from znf_programacion_clientes where codven='"
				+ codven + "')";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);

		cur.moveToFirst();
		String[] str = new String[cur.getCount()];
		if (cur.moveToFirst()) {

			int i = 0;
			do {
				str[i] = cur.getString(0);
				i++;
			} while (cur.moveToNext());

		}
		cur.close();
		db.close();
		return str;
	}

	// metodo para cargar direccion del cliente
	

	public String getCodVendedor(String user, String pass) {
		String rawQuery;
		rawQuery = "Select codven from vendedor inner join usuarios on "
				+ "vendedor.coduser=usuarios.usecod where usuarios.useusr='"
				+ user + "' and usuarios.usepas='" + pass + "'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		cur.moveToFirst();
		String codigo = "";
		if (cur.moveToFirst()) {

			do {
				codigo = cur.getString(0);
			} while (cur.moveToNext());

		}
		cur.close();
		db.close();
		return codigo;
	}

	public String getNombreUsuarioXcodvend(String codven) {
		String rawQuery;
		rawQuery = "select usuarios.usenam from usuarios inner join "
				+ "vendedor on usuarios.usecod=vendedor.coduser where codven='"
				+ codven + "'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		cur.moveToFirst();
		String usrname = "";
		if (cur.moveToFirst()) {

			do {
				usrname = cur.getString(0);
			} while (cur.moveToNext());

		}
		cur.close();
		db.close();
		return usrname;
	}


	public ItemProducto[] getProductosXcliente(String codigoCliente,
											   String descripcion) {

		int sec_politica = getSecPoliticaConfiguracion();

		String rawQuery;

		rawQuery = "select * from "
				+ "("
				+ "select politica_precio2.secuencia,"
				+ "producto.codpro,"
				+ "producto.despro,"
				+ "politica_precio2.prepro,"
				+ "politica_precio2.prepro_unidad,"
				+ "producto.percepcion,"
				+ "producto.factor_conversion,"
				+ "producto.peso,"
				+ "ifnull(mta_kardex.stock-mta_kardex.xtemp,0) as stock,"
				+ "producto.cod_rapido, "
				+ "producto.afecto, "
				+ "producto.estado,"
				+ "producto.desc_comercial "
				+ "from producto "
				+ "left join politica_precio2 on producto.codpro = politica_precio2.codpro "
				+ "left join mta_kardex on mta_kardex.codpro = producto.codpro "				
				+ "where " // politica_precio2.secuencia=0 and "
				+ " producto.despro like '%"
				+ descripcion
				+ "%' "
				+

				"union all select "
				+ "politica_precio2.secuencia,"
				+ "producto.codpro,"
				+ "producto.despro,"
				+ "politica_precio2.prepro,"
				+ "politica_precio2.prepro_unidad,"
				+ "producto.percepcion,"
				+ "producto.factor_conversion,"
				+ "producto.peso,"
				+ "ifnull(mta_kardex.stock-mta_kardex.xtemp,0) as stock,"
				+ "producto.cod_rapido, "
				+ "producto.afecto, "
				+ "producto.estado,"
				+ "producto.desc_comercial "
				+ "from producto "
				+ "left join politica_precio2 on producto.codpro = politica_precio2.codpro "
				+ "left join mta_kardex on mta_kardex.codpro = producto.codpro "
				+ "where politica_precio2.secuencia=(select ifnull((select sec_politica from politica_cliente where codcli='"
				+ codigoCliente
				+ "'),'"
				+ sec_politica
				+ "')) "
				+ "and producto.despro like '%" + descripcion + "%' " + ") " +

				"group by codpro order by despro";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(rawQuery, null);

		ItemProducto[] productos = new ItemProducto[cursor.getCount()];

		if (cursor.moveToFirst()) {
			int i = 0;
			do {

				productos[i] = new ItemProducto();
				productos[i].setSec_politica(cursor.getString(0));
				productos[i].setCodprod(cursor.getString(1));
				productos[i].setDescripcion(cursor.getString(2));
				productos[i].setPrecio(cursor.getDouble(3));
				productos[i].setPrecioUnidad(cursor.getDouble(4));
				productos[i].setPercepcion(cursor.getDouble(5));
				productos[i].setFact_conv(cursor.getInt(6));
				productos[i].setPeso(cursor.getDouble(7));
				productos[i].setStock(cursor.getInt(8));
				productos[i].setCodProveedor(cursor.getString(9));
				productos[i].setAfecto(cursor.getString(10));
				productos[i].setEstado(cursor.getString(11));
				productos[i].setDesc_comercial(cursor.getString(cursor.getColumnIndex("desc_comercial")));
				i++;
				Log.i("DBclasses ::getProductosXcliente::",
						"CodProd: " + cursor.getString(1) + "\nDescripcion: "
								+ cursor.getString(2) + "\nPercepcion: "
								+ cursor.getDouble(5));
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();

		return productos;
	}


	public ItemProducto[] getProductosXclienteYdescrip_comercial(String codigoCliente,
											   String descripcion) {

		int sec_politica = getSecPoliticaConfiguracion();

		String rawQuery;

		rawQuery = "select * from "
				+ "("
				+ "select politica_precio2.secuencia,"
				+ "producto.codpro,"
				+ "producto.despro,"
				+ "politica_precio2.prepro,"
				+ "politica_precio2.prepro_unidad,"
				+ "producto.percepcion,"
				+ "producto.factor_conversion,"
				+ "producto.peso,"
				+ "ifnull(mta_kardex.stock-mta_kardex.xtemp,0) as stock,"
				+ "producto.cod_rapido, "
				+ "producto.afecto, "
				+ "producto.estado,"
				+"producto.desc_comercial "
				+ "from producto "
				+ "left join politica_precio2 on producto.codpro = politica_precio2.codpro "
				+ "left join mta_kardex on mta_kardex.codpro = producto.codpro "
				+ "where " // politica_precio2.secuencia=0 and "
				+ " producto.desc_comercial like '%"
				+ descripcion
				+ "%' "
				+

				"union all select "
				+ "politica_precio2.secuencia,"
				+ "producto.codpro,"
				+ "producto.despro,"
				+ "politica_precio2.prepro,"
				+ "politica_precio2.prepro_unidad,"
				+ "producto.percepcion,"
				+ "producto.factor_conversion,"
				+ "producto.peso,"
				+ "ifnull(mta_kardex.stock-mta_kardex.xtemp,0) as stock,"
				+ "producto.cod_rapido, "
				+ "producto.afecto, "
				+ "producto.estado,"
				+ "producto.desc_comercial "
				+ "from producto "
				+ "left join politica_precio2 on producto.codpro = politica_precio2.codpro "
				+ "left join mta_kardex on mta_kardex.codpro = producto.codpro "
				+ "where politica_precio2.secuencia=(select ifnull((select sec_politica from politica_cliente where codcli='"
				+ codigoCliente
				+ "'),'"
				+ sec_politica
				+ "')) "
				+ "and producto.desc_comercial like '%" + descripcion + "%' " + ") " +

				"group by codpro order by despro";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(rawQuery, null);

		ItemProducto[] productos = new ItemProducto[cursor.getCount()];

		if (cursor.moveToFirst()) {
			int i = 0;
			do {

				productos[i] = new ItemProducto();
				productos[i].setSec_politica(cursor.getString(0));
				productos[i].setCodprod(cursor.getString(1));
				productos[i].setDescripcion(cursor.getString(2));
				productos[i].setPrecio(cursor.getDouble(3));
				productos[i].setPrecioUnidad(cursor.getDouble(4));
				productos[i].setPercepcion(cursor.getDouble(5));
				productos[i].setFact_conv(cursor.getInt(6));
				productos[i].setPeso(cursor.getDouble(7));
				productos[i].setStock(cursor.getInt(8));
				productos[i].setCodProveedor(cursor.getString(9));
				productos[i].setAfecto(cursor.getString(10));
				productos[i].setEstado(cursor.getString(11));
				productos[i].setDesc_comercial(cursor.getString(cursor.getColumnIndex("desc_comercial")));
				i++;
				Log.i("DBclasses ::getProductosXcliente::",
						"CodProd: " + cursor.getString(1) + "\nDescripcion: "
								+ cursor.getString(2) + "\nPercepcion: "
								+ cursor.getDouble(5));
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();

		return productos;
	}


	public ItemProducto[] getProductosXcliente_codpro(String codigoCliente,
			String codigoProducto) {

		int sec_politica = getSecPoliticaConfiguracion();

		String rawQuery;

		rawQuery =

		"select * from "
				+ "("
				+ "select "
				+ "politica_precio2.secuencia,"
				+ "producto.codpro,"
				+ "producto.despro,"
				+ "politica_precio2.prepro,"
				+ "politica_precio2.prepro_unidad,"
				+ "producto.percepcion,"
				+ "producto.factor_conversion,"
				+ "producto.peso,"
				+ "ifnull(mta_kardex.stock-mta_kardex.xtemp,0) as stock,"
				+ "producto.cod_rapido, "
				+ "producto.afecto, "
				+ "producto.estado,"
				+ "producto.desc_comercial "
				+ "from producto "
				+ "left join politica_precio2 on producto.codpro = politica_precio2.codpro "
				+ "left join mta_kardex on mta_kardex.codpro = producto.codpro "
				+ "where " // politica_precio2.secuencia=0 and "
				+ " producto.codpro like '%"
				+ codigoProducto
				+ "%' "
				+

				"union all select "
				+ "politica_precio2.secuencia,"
				+ "producto.codpro,"
				+ "producto.despro,"
				+ "politica_precio2.prepro,"
				+ "politica_precio2.prepro_unidad,"
				+ "producto.percepcion,"
				+ "producto.factor_conversion,"
				+ "producto.peso,"
				+ "ifnull(mta_kardex.stock-mta_kardex.xtemp,0) as stock,"
				+ "producto.cod_rapido, "
				+ "producto.afecto, "
				+ "producto.estado,"
				+ "producto.desc_comercial "
				+ "from producto "
				+ "left join politica_precio2 on producto.codpro = politica_precio2.codpro "
				+ "left join mta_kardex on mta_kardex.codpro = producto.codpro "
				+ "where politica_precio2.secuencia=(select ifnull((select sec_politica from politica_cliente where codcli='"
				+ codigoCliente
				+ "'),'"
				+ sec_politica
				+ "')) "
				+ "and producto.codpro like '%"
				+ codigoProducto
				+ "%' "
				+ ") "
				+ "group by codpro order by codpro";
		
		Log.d(TAG+ " rawQuery: ", rawQuery);
		
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(rawQuery, null);

		ItemProducto[] productos = new ItemProducto[cursor.getCount()];

		if (cursor.moveToFirst()) {
			int i = 0;
			do {
				productos[i] = new ItemProducto();
				productos[i].setSec_politica(""+cursor.getString(0));
				productos[i].setCodprod(cursor.getString(1));
				productos[i].setDescripcion(cursor.getString(2));
				productos[i].setPrecio(0.0);//cursor.getDouble(3));
				productos[i].setPrecioUnidad(0.0);//cursor.getDouble(4));
				productos[i].setPercepcion(0.0);//cursor.getDouble(5));
				productos[i].setFact_conv(cursor.getInt(6));
				productos[i].setPeso(cursor.getDouble(7));
				productos[i].setStock(0);//cursor.getInt(8));
				productos[i].setCodProveedor(""+cursor.getString(9));
				productos[i].setAfecto(cursor.getString(10));
				productos[i].setEstado(cursor.getString(11));
				productos[i].setDesc_comercial(cursor.getString(cursor.getColumnIndex("desc_comercial")));
				i++;
				Log.i("DBclasses ::getProductosXcliente::",
						"CodProd: " + cursor.getString(1) + 
						"\nDescripcion: "+ cursor.getString(2) +
						"\nAfecto: " + cursor.getString(10) +
						"\nPercepcion: " + cursor.getDouble(5));
			} while (cursor.moveToNext());
		}else{
			rawQuery = "";
			
			
		}

		cursor.close();
		db.close();

		return productos;

	}

	public ItemProducto[] getProductosXProveedor(String codigoCliente,
			String codigoProveedor) {
		// TODO Auto-generated method stub

		int sec_politica = getSecPoliticaConfiguracion();

		String rawQuery;
		Log.i("PRODUCTOS_PROVEEDOR", codigoCliente + "-" + codigoProveedor);

		rawQuery = "select * from "
				+ "("
				+ "select "
				+ "politica_precio2.secuencia,"
				+ "producto.codpro,"
				+ "producto.despro,"
				+ "politica_precio2.prepro,"
				+ "politica_precio2.prepro_unidad,"
				+ "producto.percepcion,"
				+ "producto.factor_conversion,"
				+ "producto.peso,"
				+ "ifnull(mta_kardex.stock-mta_kardex.xtemp,0) as stock,"
				+ "producto.cod_rapido, "
				+ "producto.afecto, "
				+ "producto.estado,"
				+ "producto.desc_comercial "
				+ "from producto "
				+ "inner join politica_precio2 on producto.codpro = politica_precio2.codpro "
				+ "left join mta_kardex on mta_kardex.codpro = producto.codpro "
				+ "where politica_precio2.secuencia=0 "
				+ "and producto.cod_rapido like '%"
				+ codigoProveedor
				+ "%' "
				+

				"union all select "
				+ "politica_precio2.secuencia,"
				+ "producto.codpro,"
				+ "producto.despro,"
				+ "politica_precio2.prepro,"
				+ "politica_precio2.prepro_unidad,"
				+ "producto.percepcion,"
				+ "producto.factor_conversion,"
				+ "producto.peso,"
				+ "ifnull(mta_kardex.stock-mta_kardex.xtemp,0) as stock,"
				+ "producto.cod_rapido, "
				+ "producto.afecto, "
				+ "producto.estado,"
				+ "producto.desc_comercial "
				+ "from producto "
				+ "inner join politica_precio2 on producto.codpro = politica_precio2.codpro "
				+ "left join mta_kardex on mta_kardex.codpro = producto.codpro "
				+ "where politica_precio2.secuencia=(select ifnull((select sec_politica from politica_cliente where codcli='"
				+ codigoCliente
				+ "'),'"
				+ sec_politica
				+ "')) "
				+ "and producto.cod_rapido like '%"
				+ codigoProveedor
				+ "%' "
				+ ") " + "group by codpro order by despro";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(rawQuery, null);

		ItemProducto[] productos = new ItemProducto[cursor.getCount()];

		if (cursor.moveToFirst()) {
			int i = 0;
			do {
				productos[i] = new ItemProducto();
				productos[i].setSec_politica(cursor.getString(0));
				productos[i].setCodprod(cursor.getString(1));
				productos[i].setDescripcion(cursor.getString(2));
				productos[i].setPrecio(cursor.getDouble(3));
				productos[i].setPrecioUnidad(cursor.getDouble(4));
				productos[i].setPercepcion(cursor.getDouble(5));
				productos[i].setFact_conv(cursor.getInt(6));
				productos[i].setPeso(cursor.getDouble(7));
				productos[i].setStock(cursor.getInt(8));
				productos[i].setCodProveedor(cursor.getString(9));
				productos[i].setAfecto(cursor.getString(10));
				productos[i].setEstado(cursor.getString(11));
				productos[i].setDesc_comercial(cursor.getString(cursor.getColumnIndex("desc_comercial")));
				i++;

				Log.i("DBclasses ::getProductosXProveedor::",
						"CodProd: " + cursor.getString(1) + "\nDescripcion: "
								+ cursor.getString(2) + "\nPercepcion: "
								+ cursor.getDouble(5));
			} while (cursor.moveToNext());
		}

		cursor.close();
		db.close();

		return productos;
	}

	public ItemProducto[] obtener_polPrecXvend(String codven, String codpro) {

		String rawQuery;
		/*
		 * rawQuery=
		 * "select politica_precio2.secuencia,politica_precio2.prepro,politica_precio2.prepro_unidad from "
		 * +
		 * "politica_precio2 inner join politica_vendedor on politica_precio2.secuencia=0 and codpro='"
		 * +codpro+"' " +
		 * "union select politica_precio2.secuencia,politica_precio2.prepro,politica_precio2.prepro_unidad from "
		 * + "politica_precio2 inner join politica_vendedor " +
		 * "on politica_precio2.secuencia=politica_vendedor.sec_politica " +
		 * "where codven='"+codven+"' and codpro='"+codpro+"'";
		 */
		/*
		 * rawQuery=
		 * "	select producto.factor_conversion, prepro, prepro_unidad from politica_precio2 inner join producto"
		 * +
		 * "	on producto.codpro= politica_precio2.codpro where producto.codpro='"
		 * +codpro+"'";
		 */

		rawQuery = "select producto.factor_conversion, prepro, prepro_unidad, politica_precio2.secuencia from politica_precio2 inner join"
				+ " producto on producto.codpro = politica_precio2. codpro inner join"
				+ " politica_vendedor on politica_vendedor.sec_politica=politica_precio2.secuencia "
				+ " where producto.codpro='"
				+ codpro
				+ "'  and codven='"
				+ codven + "'";

		/*
		 * rawQuery=
		 * "select producto.factor_conversion, prepro, prepro_unidad, politica_precio2.secuencia from politica_precio2 inner join "
		 * +
		 * "producto on producto.codpro = politica_precio2. codpro where politica_precio2.secuencia="
		 * +
		 * "isnull((select top(1) sec_politica from politica_cliente where codcli='001127' and sec_politica>0),0) "
		 * + "and producto.codpro='"+codpro+
		 * "' union select producto.factor_conversion, prepro, prepro_unidad, "
		 * +
		 * "politica_precio2.secuencia from politica_precio2 inner join producto on producto.codpro = "
		 * +
		 * "politica_precio2. codpro inner join politica_vendedor on politica_vendedor.sec_politica="
		 * + "politica_precio2.secuencia where producto.codpro='"+codpro+
		 * "'  and codven='"+codven+"' order by secuencia";
		 */

		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(rawQuery, null);
		ItemProducto[] productos;
		if (cursor.getCount() == 0) {
			productos = new ItemProducto[1];
			productos[0] = new ItemProducto();
			productos[0].setFact_conv(0);
			productos[0].setPrecio(0.0);
			productos[0].setPrecioUnidad(0.0);
			productos[0].setSec_politica("0");
		} else {
			productos = new ItemProducto[cursor.getCount()];
		}
		cursor.moveToFirst();
		if (cursor.moveToFirst()) {
			int i = 0;
			do {

				productos[i] = new ItemProducto();
				// uso fact_conv para almacenar la secuencia
				productos[i].setFact_conv(cursor.getInt(0));
				// productos[i].setCodprod(cursor.getString(0));
				// productos[i].setDescripcion(cursor.getString(1));
				productos[i].setPrecio(cursor.getDouble(1));
				productos[i].setPrecioUnidad(cursor.getDouble(2));
				productos[i].setSec_politica(cursor.getString(3));
				// productos[i].setFact_conv(cursor.getInt(4));
				// productos[i].setPeso(cursor.getDouble(5));
				// productos[i].setStock(cursor.getInt(6));
				i++;

			} while (cursor.moveToNext());

		}

		cursor.close();
		db.close();
		return productos;

	}

	// nuevo metodo para lista de precios
	public ItemProducto[] obtener_polPrecXvend2(String codven, String codpro,
			String codcli) {

		int sec_politica = getSecPoliticaConfiguracion();

		String rawQuery;

		/*
		 * rawQuery=
		 * "select producto.factor_conversion, prepro, prepro_unidad, politica_precio2.secuencia from politica_precio2 inner join "
		 * +
		 * "producto on producto.codpro = politica_precio2. codpro where politica_precio2.secuencia="
		 * +
		 * "ifnull((select sec_politica from politica_cliente where codcli='"+codcli
		 * +"' and sec_politica>0 limit 1),0) " +
		 * "and producto.codpro='"+codpro+
		 * "' union all select producto.factor_conversion, prepro, prepro_unidad, "
		 * +
		 * "politica_precio2.secuencia from politica_precio2 inner join producto on producto.codpro = "
		 * +
		 * "politica_precio2. codpro inner join politica_vendedor on politica_vendedor.sec_politica="
		 * + "politica_precio2.secuencia where producto.codpro='"+codpro+
		 * "' and codven='"+codven+"' " +
		 * "and politica_vendedor.sec_politica != (select sec_politica from politica_cliente where codcli='"
		 * +codcli+"' and sec_politica>0 limit 1)";
		 */

		rawQuery = "select producto.factor_conversion, prepro, prepro_unidad, politica_precio2.secuencia "
				+ "from politica_precio2 inner join producto "
				+ "on producto.codpro = politica_precio2.codpro "
				+ "where politica_precio2.secuencia=(select ifnull((select sec_politica from politica_cliente where codcli='"
				+ codcli
				+ "'),'"
				+ sec_politica
				+ "')) "
				+ "and producto.codpro='"
				+ codpro
				+ "' "
				+

				"union all "
				+

				"select producto.factor_conversion, prepro, prepro_unidad,politica_precio2.secuencia "
				+ "from politica_precio2 inner join producto "
				+ "on producto.codpro = politica_precio2.codpro "
				+ "inner join politica_vendedor "
				+ "on politica_vendedor.sec_politica=politica_precio2.secuencia "
				+ "where producto.codpro='"
				+ codpro
				+ "' and codven='"
				+ codven
				+ "' "
				+ "and politica_vendedor.sec_politica != (select ifnull((select sec_politica from politica_cliente where codcli='"
				+ codcli + "'),'" + sec_politica + "')) ";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(rawQuery, null);
		ItemProducto[] productos;
		if (cursor.getCount() == 0) {
			productos = new ItemProducto[1];
			productos[0] = new ItemProducto();
			productos[0].setFact_conv(0);
			productos[0].setPrecio(0.0);
			productos[0].setPrecioUnidad(0.0);
			productos[0].setSec_politica("0");
		} else {
			productos = new ItemProducto[cursor.getCount()];
		}
		cursor.moveToFirst();
		if (cursor.moveToFirst()) {
			int i = 0;
			do {

				productos[i] = new ItemProducto();
				// uso fact_conv para almacenar la secuencia
				productos[i].setFact_conv(cursor.getInt(0));
				// productos[i].setCodprod(cursor.getString(0));
				// productos[i].setDescripcion(cursor.getString(1));
				productos[i].setPrecio(cursor.getDouble(1));
				productos[i].setPrecioUnidad(cursor.getDouble(2));
				productos[i].setSec_politica(cursor.getString(3));
				// productos[i].setFact_conv(cursor.getInt(4));
				// productos[i].setPeso(cursor.getDouble(5));
				// productos[i].setStock(cursor.getInt(6));
				i++;

			} while (cursor.moveToNext());

		}

		cursor.close();
		db.close();
		return productos;

	}

	// fin

	public String obtenerCodigoCliente(String nomcli) {
		String rawQuery;
		rawQuery = "select codcli from cliente where nomcli='" + nomcli + "'";
		Log.d(TAG, rawQuery);
		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		String codcli = "";
		cur.moveToFirst();
		if (cur.moveToFirst()) {
			do {
				codcli = cur.getString(0);
			} while (cur.moveToNext());

		}
		cur.close();
		db.close();
		Log.e(TAG, "codcli: "+codcli);
		return codcli;
	}

	public int obtenerPercepcionCliente(String codcli) {
		String rawQuery;
		rawQuery = "select AfectoPercepcion from cliente where codcli='"
				+ codcli + "'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		int percepcion = 0;
		cur.moveToFirst();
		if (cur.moveToFirst()) {
			do {
				percepcion = cur.getInt(0);
				
			} while (cur.moveToNext());

		}
		cur.close();
		db.close();
		return percepcion;
	}

	public int obtenerPercepcionEspecialCliente(String codcli) {
		String rawQuery;
		rawQuery = "select PercepcionEspecial from cliente where codcli='"
				+ codcli + "'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		int percepcionEspecial = 0;
		cur.moveToFirst();
		if (cur.moveToFirst()) {
			do {
				percepcionEspecial = cur.getInt(0);
				
			} while (cur.moveToNext());

		}
		cur.close();
		db.close();
		return percepcionEspecial;
	}

	public String obtenerValorPercepcionCliente(String codcli) {
		String rawQuery;
		rawQuery = "select percepcion from cliente where codcli='"+codcli+"'";
		SQLiteDatabase db = getReadableDatabase();

		Cursor cur = db.rawQuery(rawQuery, null);

		String percepcion = "0";

		cur.moveToFirst();
		if (cur.moveToFirst()) {
			do {

				if (cur.getString(0) == null) {
					percepcion = "0.0";
				} else {
					percepcion = cur.getString(0);
				}
				
			} while (cur.moveToNext());

		}
		cur.close();
		db.close();
		Log.d(TAG,rawQuery+" --> "+percepcion);
		return percepcion;
	}

	public int obtenerSitioXocnumero(String oc_numero) {
		String rawQuery;
		rawQuery = "select sitio_enfa from pedido_cabecera where oc_numero='"
				+ oc_numero + "'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		int sitio = 0;
		cur.moveToFirst();
		if (cur.moveToFirst()) {
			do {
				sitio = cur.getInt(0);
			} while (cur.moveToNext());

		}
		cur.close();
		db.close();
		return sitio;
	}


	public int getNextNroItemPedido(String oc_numero){
		SQLiteDatabase db = getWritableDatabase();

		String subQuery =
				"SELECT max(cast(item as INTEGER)) FROM pedido_detalle "
						+ "where item > 0 and oc_numero = '"+oc_numero+"'";

		Cursor curAux = db.rawQuery(subQuery, null);
		curAux.moveToFirst();
		int nro_item=0;
		while (!curAux.isAfterLast()) {
			Log.d("", "nro item obtenido de la consulta: "+curAux.getInt(0));
			nro_item = curAux.getInt(0);
			curAux.moveToNext();
		}
		curAux.close();
		nro_item++;
		return nro_item;
	}

	public void AgregarPedidoDetalle(DBPedido_Detalle item) {

		int nro_item=getNextNroItemPedido(item.getOc_numero());
		AgregarPedidoDetallePrincipal(item, nro_item);
	}

	public void AgregarPedidoDetallePrincipal(DBPedido_Detalle item, int nro_item) {

		try {
			SQLiteDatabase db = getWritableDatabase();


			item.setItem(nro_item);


			Log.d("BDclasses","Agregando nuevo detalle, con los datos: ");
			Log.d("BDclasses :AgregarPedidoDetalle:","Oc_numero "+item.getOc_numero());
			Log.d("BDclasses :AgregarPedidoDetalle:","item "+item.getItem());
			Log.d("BDclasses :AgregarPedidoDetalle:","codigo "+item.getCip());
			Log.d("BDclasses :AgregarPedidoDetalle:","secuencia "+item.getSec_promo());

			ContentValues Nreg = new ContentValues();
			Nreg.put("oc_numero", item.getOc_numero());
			Nreg.put("ean_item", item.getEan_item());
			Nreg.put("cip", item.getCip());
			Nreg.put("precio_bruto", item.getPrecio_bruto());
			Nreg.put("precio_neto", item.getPrecio_neto());
			Nreg.put("percepcion", item.getPercepcion());
			Nreg.put("cantidad", item.getCantidad());
			Nreg.put("tipo_producto", item.getTipo_producto());
			Nreg.put("unidad_medida", item.getUnidad_medida());
			Nreg.put("peso_bruto", item.getPeso_bruto());
			Nreg.put("flag", item.getFlag());
			Nreg.put("cod_politica", item.getCod_politica());
			Nreg.put("sec_promo", item.getSec_promo());
			Nreg.put("item_promo", item.getItem_promo());
			Nreg.put("agrup_promo", item.getAgrup_promo());
			Nreg.put("item", item.getItem());
			Nreg.put("precioLista", item.getPrecioLista());
			Nreg.put("descuento", item.getDescuento());
			Nreg.put("porcentaje_desc", item.getPorcentaje_desc());
			Nreg.put("porcentaje_desc_extra", item.getPorcentaje_desc_extra());
			Nreg.put("lote", item.getLote());

			Nreg.put("motivoDevolucion", item.getMotivoDevolucion());
			Nreg.put("Expectativa", item.getExpectativa());
			Nreg.put("Envase", item.getEnvase());
			Nreg.put("Contenido", item.getContenido());
			Nreg.put("Proceso", item.getProceso());
			Nreg.put("observacionDevolucion", item.getObservacionDevolucion());
			Nreg.put("tipoDocumento", item.getTipoDocumento());
			Nreg.put("serieDevolucion", item.getSerieDevolucion());
			Nreg.put("numeroDevolucion", item.getNumeroDevolucion());
			Nreg.put("sec_promo_prioridad", item.getSec_promo_prioridad());
			Nreg.put("item_promo_prioridad", item.getItem_promo_prioridad());

			db.insert("pedido_detalle", null, Nreg);
			db.close();

			Gson gson = new Gson();
			Log.e("(DBclasses)AgregarPedidoDetalle","detallePedido: ITEM AGREGADO\n" + gson.toJson(item));

		} catch (Exception e) {
			Log.i("PEDIDO_DETALLES", "Error registro insertado");
		}

	}
	
	
	
	public void AgregarPedidoDevolucion(DBPedido_Devolucion item) {

		try {
			SQLiteDatabase db = getWritableDatabase();

			ContentValues Nreg = new ContentValues();
			Nreg.put("oc_numero", item.getOc_numero());
			Nreg.put("ean_item", item.getEan_item());
			Nreg.put("cip", item.getCip());
			Nreg.put("cantidad", item.getCantidad());
			Nreg.put("unidad_medida", item.getUnidad_medida());
			Nreg.put("flag", item.getFlag());

			db.insert("pedido_devolucion", null, Nreg);
			db.close();
			Log.i("PEDIDO_DEVOLUCION", "ITEM INSERTADO");

		} catch (Exception e) {
			Log.i("PEDIDO_DEVOLUCION", "Error registro insertado");
		}

	}
	
	public ArrayList<DB_Detalle_Entrega> obtenerListadoDetalleEntrega(String oc_numero,String cip) {

		String rawQuery;

		rawQuery = 
				"select *from pedido_detalle_entrega" 				
				+ " where oc_numero='" + oc_numero + "' and cip='"+ cip +"' order by Nro_Entrega";

		Log.d("Dbclasses :obtenerListadoDetalleEntrega:","___________________________________");
		Log.d("Dbclasses :obtenerListadoDetalleEntrega:",rawQuery);
		Log.d("Dbclasses :obtenerListadoDetalleEntrega:","Obteniendo listado de entregas del oc_numero-> "+oc_numero);
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(rawQuery, null);
		ArrayList<DB_Detalle_Entrega> arreglo= new ArrayList<DB_Detalle_Entrega>();
		DB_Detalle_Entrega[] entregas = new DB_Detalle_Entrega[cursor.getCount()];

		if (cursor.moveToFirst()) {
			int i = 0;
			do {
				entregas[i] = new DB_Detalle_Entrega();
				
				entregas[i].setOc_numero(cursor.getString(0));
				entregas[i].setCip(cursor.getString(1));
				entregas[i].setNroEntrega(cursor.getInt(2));
				entregas[i].setDt_fechaEntrega(cursor.getString(3));
				entregas[i].setCantidad(cursor.getInt(4));
				entregas[i].setCodTurno(cursor.getString(5));
				entregas[i].setEstado(cursor.getString(6));
				
				arreglo.add(entregas[i]);
			
				Log.w("Dbclasses :obtenerListadoDetalleEntrega::","NRO_ENTREGA "+i+"  "+entregas[i].getNroEntrega());
				Log.w("Dbclasses :obtenerListadoDetalleEntrega::","FECHA_ENTREGA "+entregas[i].getDt_fechaEntrega());
				Log.w("Dbclasses :obtenerListadoDetalleEntrega::","CODTURNO "+entregas[i].getCodTurno());
				
				//Log.d("DBclasses ::obtenerListadoProductos_pedido::","getPercepcion "+productos[i].getPercepcionPedido());
				i++;

			} while (cursor.moveToNext());
		}
		Log.d("Dbclasses :obtenerListadoDetalleEntrega:","___________________________________");
		cursor.close();
		db.close();

		return arreglo;
	}
	
	public ArrayList<DB_Detalle_Entrega> obtenerListadoDetalleEntregaTotal(String oc_numero) {

		String rawQuery;

		rawQuery = 
				"select *from pedido_detalle_entrega" 				
				+ " where oc_numero='" + oc_numero + "'";

		Log.d("Dbclasses :obtenerListadoDetalleEntrega:","___________________________________");
		Log.d("Dbclasses :obtenerListadoDetalleEntrega:",rawQuery);
		Log.d("Dbclasses :obtenerListadoDetalleEntrega:","Obteniendo listado de entregas del oc_numero-> "+oc_numero);
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(rawQuery, null);

		DB_Detalle_Entrega[] entregas = new DB_Detalle_Entrega[cursor.getCount()];
		ArrayList<DB_Detalle_Entrega> entregasTotal = new ArrayList<DB_Detalle_Entrega>();
		if (cursor.moveToFirst()) {
			int i = 0;
			do {
				entregas[i] = new DB_Detalle_Entrega();
				
				entregas[i].setOc_numero(cursor.getString(0));
				entregas[i].setCip(cursor.getString(1));
				entregas[i].setNroEntrega(cursor.getInt(2));
				entregas[i].setDt_fechaEntrega(cursor.getString(3));
				entregas[i].setCantidad(cursor.getInt(4));
				entregas[i].setCodTurno(cursor.getString(5));
				entregas[i].setEstado(cursor.getString(6));
				entregasTotal.add(entregas[i]);
				Log.w("Dbclasses :obtenerListadoDetalleEntregaTOTAL::","NRO_ENTREGA "+i+"  "+entregas[i].getNroEntrega());
				Log.w("Dbclasses :obtenerListadoDetalleEntregaTOTAL::","FECHA_ENTREGA "+entregas[i].getDt_fechaEntrega());
				Log.w("Dbclasses :obtenerListadoDetalleEntregaTOTAL::","CODTURNO "+entregas[i].getCodTurno());
				
				//Log.d("DBclasses ::obtenerListadoProductos_pedido::","getPercepcion "+productos[i].getPercepcionPedido());
				i++;

			} while (cursor.moveToNext());
		}
		Log.d("Dbclasses :obtenerListadoDetalleEntrega:","___________________________________");
		cursor.close();
		db.close();

		return entregasTotal;
	}
	
	public ArrayList<DB_Pedido_Detalle_Promocion> obtenerListadoDetallePromocion(String oc_numero, String cip,String sec) {

		String rawQuery;
		
		rawQuery = 
				"select producto,cc_artic, 0 from bonificacion_colores b where "
				+ "b.cc_artic not in(select cc_artic from pedido_detalle_promocion p where oc_numero='"+oc_numero+"') and secuencia='"+sec+"' "
				//+ "secuencia='"+sec+"' " 
				+ "GROUP BY b.Producto "
				+ "union select descripcion,cc_artic,cantidad from pedido_detalle_promocion "
				+ "where oc_numero='"+oc_numero+"' and cip='"+cip+"' and secuencia='"+sec+"'";

		Log.d("Dbclasses :obtenerListadoDetallePromocion:","___________________________________");
		Log.d("Dbclasses :obtenerListadoDetallePromocion:",rawQuery);
		Log.d("Dbclasses :obtenerListadoDetallePromocion:","Obteniendo listado de promocion del oc_numero-> "+oc_numero);
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(rawQuery, null);

		DB_Pedido_Detalle_Promocion[] promos = new DB_Pedido_Detalle_Promocion[cursor.getCount()];
		ArrayList<DB_Pedido_Detalle_Promocion> promosTotal = new ArrayList<DB_Pedido_Detalle_Promocion>();
		if (cursor.moveToFirst()) {
			int i = 0;
			do {
				promos[i] = new DB_Pedido_Detalle_Promocion();
				
				/*promos[i].setOc_numero(cursor.getString(0));
				promos[i].setCip(cursor.getString(1));
				promos[i].setCc_artic(cursor.getString(2));
				promos[i].setSecuencia(cursor.getString(3));
				promos[i].setCantidad(cursor.getInt(4));
				promos[i].setDescripcion(cursor.getString(5));*/
				
				promos[i].setDescripcion(cursor.getString(0));
				promos[i].setCc_artic(cursor.getString(1));
				promos[i].setCantidad(cursor.getInt(2));
				
				
				promosTotal.add(promos[i]);
				Log.w("Dbclasses :obtenerListadoDetallePROMO::","NRO_ENTREGA "+i+"  "+promos[i].getCc_artic());
				Log.w("Dbclasses :obtenerListadoDetallePROMO::","FECHA_ENTREGA "+promos[i].getSecuencia());
				Log.w("Dbclasses :obtenerListadoDetallePROMO::","DESCRIPCION"+promos[i].getDescripcion());
				
				i++;

			} while (cursor.moveToNext());
		}
		Gson json= new Gson();
		Log.d("Dbclasses :obtenerListadoDetallePROMO:",""+json.toJson(promosTotal));
		cursor.close();
		db.close();

		return promosTotal;
	}
	
	public ArrayList<DB_Pedido_Detalle_Promocion> obtenerListadoDetallePromocionTotal(String oc_numero) {

		String rawQuery;

		rawQuery = 
				"select *from pedido_detalle_promocion" 				
				+ " where oc_numero='" + oc_numero + "'";

		Log.d("Dbclasses :obtenerListadoDetallePromocion:","___________________________________");
		Log.d("Dbclasses :obtenerListadoDetallePromocion:",rawQuery);
		Log.d("Dbclasses :obtenerListadoDetallePromocion:","Obteniendo listado de promocion del oc_numero-> "+oc_numero);
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(rawQuery, null);

		DB_Pedido_Detalle_Promocion[] promos = new DB_Pedido_Detalle_Promocion[cursor.getCount()];
		ArrayList<DB_Pedido_Detalle_Promocion> promosTotal = new ArrayList<DB_Pedido_Detalle_Promocion>();
		if (cursor.moveToFirst()) {
			int i = 0;
			do {
				promos[i] = new DB_Pedido_Detalle_Promocion();
				
				promos[i].setOc_numero(cursor.getString(0));
				promos[i].setCip(cursor.getString(1));
				promos[i].setCc_artic(cursor.getString(2));
				promos[i].setSecuencia(cursor.getString(3));
				promos[i].setCantidad(cursor.getInt(4));
				
				promosTotal.add(promos[i]);
				Log.w("Dbclasses :obtenerListadoDetallePROMOTOTAL::","NRO_ENTREGA "+i+"  "+promos[i].getCc_artic());
				Log.w("Dbclasses :obtenerListadoDetallePROMOTOTAL::","FECHA_ENTREGA "+promos[i].getSecuencia());
				Log.w("Dbclasses :obtenerListadoDetallePROMOTOTAL::","CODTURNO "+promos[i].getCantidad());
				
				i++;

			} while (cursor.moveToNext());
		}
		Gson json= new Gson();
		Log.d("Dbclasses :obtenerListadoDetallePROMO:",""+json.toJson(promosTotal));
		cursor.close();
		db.close();

		return promosTotal;
	}
	
	public int getCantidad_Detalle_Entrega(String oc_numero,String cip) {

		String rawQuery;

		rawQuery = "select count(*) from pedido_detalle_entrega where oc_numero='"
				+ oc_numero + "' and cip='"+ cip +"'";

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

	public void AgregarDetalleEntrega(DB_Detalle_Entrega item,String oc_numero,String cip,String nroEntrega) {
		
		String where = "oc_numero=? and cip=? and Nro_Entrega=?";
		String[] args = { oc_numero,cip,nroEntrega };

		try {
			SQLiteDatabase db = getWritableDatabase();
			db.delete(DBtables.Detalle_Entrega.TAG, where, args);
			
			db.close();

			Log.i("eliminando detalle de entrega anterior", "detalle eliminado: " +oc_numero);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		

		try {
			SQLiteDatabase db = getWritableDatabase();

			ContentValues Nreg = new ContentValues();
			Nreg.put(DBtables.Detalle_Entrega.OC_NUMERO, item.getOc_numero());
			Nreg.put(DBtables.Detalle_Entrega.CIP, item.getCip());
			Nreg.put(DBtables.Detalle_Entrega.NRO_ENTREGA, item.getNroEntrega());
			Nreg.put(DBtables.Detalle_Entrega.FECHA_ENTREGA, item.getDt_fechaEntrega());
			Nreg.put(DBtables.Detalle_Entrega.CANTIDAD, item.getCantidad());
			Nreg.put(DBtables.Detalle_Entrega.CODTURNO, item.getCodTurno());
			Nreg.put(DBtables.Detalle_Entrega.ESTADO, item.getEstado());

			db.insert(DBtables.Detalle_Entrega.TAG, null, Nreg);
			db.close();
			Log.i("PEDIDO_DETALLE_ENTREGA", "ITEM INSERTADO");

		} catch (Exception e) {
			Log.i("PEDIDO_DETALLE_ENTREGA", "Error registro insertado");
		}

	}
	
	public void eliminar_detalle_entrega(String oc_numero,String cip) {

		String where = "oc_numero=? and cip=?";
		String[] args = { oc_numero,cip };

		try {
			SQLiteDatabase db = getWritableDatabase();
			db.delete(DBtables.Detalle_Entrega.TAG, where, args);
			
			db.close();

			Log.i("eliminar_detalle_entrega", "entrega eliminada: OC= " +oc_numero+" CIP= "+cip);

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}


	public void AgregarPedidoCabecera(DBPedido_Cabecera cabecera) {
		
		try {
			SQLiteDatabase db = getWritableDatabase();

			ContentValues Nreg = new ContentValues();
			Nreg.put("oc_numero", cabecera.getOc_numero());
			Nreg.put("sitio_enfa", cabecera.getSitio_enfa());
			Nreg.put("monto_total", cabecera.getMonto_total());
			Nreg.put("subTotal", cabecera.getSubTotal());
			Nreg.put("valor_igv", cabecera.getValor_igv());
			Nreg.put("moneda", cabecera.getMoneda());
			Nreg.put("fecha_oc", cabecera.getFecha_oc());
			Nreg.put("fecha_mxe", cabecera.getFecha_mxe());
			Nreg.put("cond_pago", cabecera.getCond_pago());
			Nreg.put("nro_letra", cabecera.getNroletra());
			Nreg.put("cod_cli", cabecera.getCod_cli());
			Nreg.put("cod_emp", cabecera.getCod_emp());
			Nreg.put("estado", cabecera.getEstado());
			Nreg.put("username", cabecera.getUsername());
			Nreg.put("ruta", cabecera.getRuta());
			Nreg.put("observacion", cabecera.getObservacion());
			Nreg.put("cod_noventa", cabecera.getCod_noventa());
			Nreg.put("peso_total", cabecera.getPeso_total());
			Nreg.put("flag", cabecera.getFlag());
			Nreg.put("latitud", cabecera.getLatitud());
			Nreg.put("longitud", cabecera.getLongitud());
			Nreg.put("codigo_familiar", cabecera.getCodigo_familiar());
			Nreg.put("DT_PEDI_FECHASERVIDOR",cabecera.getDT_PEDI_FECHASERVIDOR());
			
			Nreg.put("numeroOrdenCompra", cabecera.getNumeroOrdenCompra());
			Nreg.put("CodTurno", cabecera.getCodTurno());
			Nreg.put("codigoPrioridad", cabecera.getCodigoPrioridad());
			Nreg.put("codigoSucursal", cabecera.getCodigoSucursal());
			Nreg.put("codigoPuntoEntrega", cabecera.getCodigoPuntoEntrega());
			Nreg.put("codigoTipoDespacho", cabecera.getCodigoTipoDespacho());
			Nreg.put("flagEmbalaje", cabecera.getFlagEmbalaje());
			Nreg.put("flagPedido_Anticipo", cabecera.getFlagPedido_Anticipo());
			Nreg.put("codigoTransportista", cabecera.getCodigoTransportista());
			Nreg.put("codigoAlmacen", cabecera.getCodigoAlmacen());
			Nreg.put(DBtables.Pedido_cabecera.OBSERVACION2, cabecera.getObservacion2());
			Nreg.put(DBtables.Pedido_cabecera.OBSERVACION3, cabecera.getObservacion3());
			Nreg.put("observacionDescuento", cabecera.getObservacionDescuento());
			Nreg.put("observacionTipoProducto", cabecera.getObservacionTipoProducto());
			Nreg.put("flagDescuento", cabecera.getFlagDescuento());
			Nreg.put("codigoObra", cabecera.getCodigoObra());
			Nreg.put("flagDespacho", cabecera.getFlagDespacho());
			Nreg.put("docAdicional", cabecera.getDocAdicional());
			Nreg.put("tipoDocumento", cabecera.getTipoDocumento());
			Nreg.put(DBtables.Pedido_cabecera.TIPO_REGISTRO, cabecera.getTipoRegistro());
			Nreg.put(DBtables.Pedido_cabecera.DIAS_VIGENCIA, cabecera.getDiasVigencia());
			
			//_okkk if (cabecera.getTipoRegistro().equals(PedidosActivity.TIPO_DEVOLUCION)) {
                if (cabecera.getTipoRegistro().equals("5")) {
                    //El campo recojo se guarda en observacion2 temporalmente
                    Nreg.put(DBtables.Pedido_cabecera.OBSERVACION2, cabecera.getCodigoRecojo());
                    //El campo generaCambio se guarda en observacion3 temporalmente
                    Nreg.put(DBtables.Pedido_cabecera.OBSERVACION3, cabecera.getCodigoGeneraCambio());
                    Nreg.put(DBtables.Pedido_cabecera.OBSERVACION4, cabecera.getObservacion4());
                }
			Nreg.put(DBtables.Pedido_cabecera.CODIGO_TURNO, cabecera.getCodTurno());
			
			Log.e("oc_numero", cabecera.getOc_numero());
			Log.e("sitio_enfa", cabecera.getSitio_enfa());
			Log.e("monto_total", cabecera.getMonto_total());
			Log.e("valor_igv", cabecera.getValor_igv());
			Log.e("moneda", cabecera.getMoneda());
			Log.e("fecha_oc", cabecera.getFecha_oc());
			Log.e("fecha_mxe", cabecera.getFecha_mxe());
			Log.e("cond_pago", cabecera.getCond_pago());
			Log.e("cod_cli", cabecera.getCod_cli());
			Log.e("cod_emp", cabecera.getCod_emp());
			Log.e("estado", cabecera.getEstado());
			Log.e("username", cabecera.getUsername());
			Log.e("ruta", cabecera.getRuta());
			Log.e("observacion", cabecera.getObserv());
			Log.e("cod_noventa", ""+cabecera.getCod_noventa());
			Log.e("peso_total", cabecera.getPeso_total());
			Log.e("flag", cabecera.getFlag());
			Log.e("latitud", cabecera.getLatitud());
			Log.e("longitud", cabecera.getLongitud());
			Log.e("codigo_familiar", cabecera.getCodigo_familiar());
            if (cabecera.getTipoRegistro().equals("5")) {
                Log.e("tipoRegistro","tipoRegistro:"+cabecera.getTipoRegistro());
                Log.e("observacion2 ",cabecera.getObservacion2());
                Log.e("observacion3 ",cabecera.getObservacion3());
                Log.e("observacion4 ",cabecera.getObservacion4());
            }

			db.insert("pedido_cabecera", null, Nreg);
			db.close();
			Log.w("PEDIDO_CABECERA", "registro insertado");

		} catch (Exception e) {
			e.printStackTrace();
			Log.i("PEDIDO_CABECERA", "Error registro insertado");
		}
	}

	public void cambiar_flag_pedido_cabecera(String oc_numero, String flag) {

		String where = "oc_numero = ?";
		String[] args = { oc_numero };

		try {

			SQLiteDatabase db = getWritableDatabase();

			ContentValues reg = new ContentValues();
			reg.put("flag", flag);

			db.update("pedido_cabecera", reg, where, args);
			db.close();

			Log.i("CAMBIAR ESTADO PEDIDO CABECERA", "estado cambiado"
					+ oc_numero + "- estado" + flag);

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public void cambiar_estado_pedido_detalle(String oc_numero, String codpro,
			String estado) {

		String where = "oc_numero = ? and cip=?";
		String[] args = { oc_numero, codpro };

		try {

			SQLiteDatabase db = getWritableDatabase();

			ContentValues reg = new ContentValues();
			reg.put("flag", estado);

			db.update("pedido_detalle", reg, where, args);
			db.close();

			Log.i("CAMBIAR ESTADO PEDIDO DETALLE", "estado cambiado");

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public void cambiar_estado_pedido_detalle(String oc_numero, String estado) {

		String where = "oc_numero = ?";
		String[] args = { oc_numero };

		try {

			SQLiteDatabase db = getWritableDatabase();

			ContentValues reg = new ContentValues();
			reg.put("flag", estado);

			db.update("pedido_detalle", reg, where, args);
			db.close();

			Log.i("CAMBIAR ESTADO PEDIDO DETALLE TODOS", "estado cambiado a '"
					+ estado + "'");

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public void Actualizar_pedido_cabecera(DBPedido_Cabecera cabecera) {

		String where = "oc_numero = ? ";
		String[] args = { cabecera.getOc_numero() };

		try {

			SQLiteDatabase db = getWritableDatabase();

			ContentValues reg = new ContentValues();
			reg.put("estado", cabecera.getEstado());			
			reg.put("codigo_familiar", cabecera.getCodigo_familiar());
			reg.put("flag", cabecera.getFlag());
			reg.put("tipoVista", cabecera.getTipoVista());
			
			db.update("pedido_cabecera", reg, where, args);
			db.close();
			Log.d("DBclasses ::Actualizar_pedido_cabecera::",""+cabecera.getTipoVista());
			Log.i("ACTUALIZAR PEDIDO CABECERA", "pedido_cabecera actualizada");

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

	}

	public void Actualizar_pedido_cabecera2(DBPedido_Cabecera cabecera) {

		String where = "oc_numero = ?";
		String[] args = { cabecera.getOc_numero() };

		try {

			SQLiteDatabase db = getWritableDatabase();

			ContentValues reg = new ContentValues();
			reg.put("monto_total", cabecera.getMonto_total());
			reg.put("flag", "P");
			reg.put("codigo_familiar", cabecera.getCodigo_familiar());
			reg.put("DT_PEDI_FECHASERVIDOR",
					cabecera.getDT_PEDI_FECHASERVIDOR());
			reg.put("estado", cabecera.getEstado());
			reg.put("cod_noventa", cabecera.getCod_noventa());
			reg.put("cond_pago", cabecera.getCond_pago());
			reg.put("observacion", cabecera.getObserv());
			db.update("pedido_cabecera", reg, where, args);
			db.close();

			Log.i("ACTUALIZAR PEDIDO CABECERA", "pedido_cabecera actualizada"
					+ cabecera.getOc_numero());

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

	}

	public void actualizarDepositos(DBDepositos dbdepositos, String secuencia) {

		String where = "secuencia = ?";

		String[] args = { secuencia };
		try {

			SQLiteDatabase db = getWritableDatabase();

			ContentValues reg = new ContentValues();
			reg.put("id_banco", dbdepositos.getId_banco());
			reg.put("id_num_cta", dbdepositos.getId_num_cta());
			reg.put("fecha", dbdepositos.getFecha());
			reg.put("num_ope", dbdepositos.getNum_ope());
			reg.put("moneda", dbdepositos.getMoneda());
			reg.put("monto", dbdepositos.getMonto());
			reg.put("estado", "M");
			reg.put("codven", dbdepositos.getCodven());
			reg.put("bi_depo_flag", dbdepositos.getBI_DEPO_FLAG());

			db.update("depositos", reg, where, args);
			db.close();

			Log.i("ACTUALIZAR DEPOSITOS", "DEPOSITOS actualizada - "
					+ secuencia + " Flag: " + dbdepositos.getBI_DEPO_FLAG());

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

	}

	public void eliminar_pedido(String oc_numero) {

		String where = "oc_numero=?";
		String[] args = { oc_numero };

		try {
			SQLiteDatabase db = getWritableDatabase();
			db.delete("pedido_cabecera", where, args);
			db.delete("pedido_detalle", where, args);
			db.close();

			Log.i("eliminar pedido", "pedido eliminado: " +oc_numero);

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void eliminar_pedido_devolucion(String oc_numero) {

		String where = "oc_numero=?";
		String[] args = { oc_numero };

		try {
			SQLiteDatabase db = getWritableDatabase();
			db.delete("pedido_devolucion", where, args);
			db.close();

			Log.i("eliminar pedido devolucion", "pedido devolucion eliminado");

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void actualizarClienteXRuta(String cliente) {

		String where = "codcli = ?";
		String[] args = { cliente };

		try {

			SQLiteDatabase db = getWritableDatabase();

			ContentValues reg = new ContentValues();
			reg.put("stdcli", 0);

			db.update("direccion_cliente", reg, where, args);
			db.close();

			Log.i("DBCLASSES", "secuencia visita modificada");

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public void actualizarEstadoCliente(String cliente, String estado) {

		String where = "codcli = ?";
		String[] args = { cliente };

		try {

			SQLiteDatabase db = getWritableDatabase();

			ContentValues reg = new ContentValues();
			reg.put("stdcli", estado);

			db.update("cliente", reg, where, args);
			db.close();

			Log.i("ACTUALIZARESTADOCLIENTE", "stdcli modificado =" + estado
					+ cliente);

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}


	public String obtenerMaxNumOc(String codven) {

		String rawQuery;
		rawQuery = "select max(pedido_cabecera.oc_numero) from pedido_cabecera where pedido_cabecera.cod_emp='"
				+ codven + "' and oc_numero !='TPLAST'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		String num_oc = "0";

		cur.moveToFirst();
		if (cur.moveToFirst()) {
			do {
				num_oc = cur.getString(0);
			} while (cur.moveToNext());

		}
		if (num_oc == null || num_oc.trim().length() == 0) {
			num_oc = "adfad";
		}

		cur.close();
		db.close();
		return num_oc;
	}


	public String obtenerSecuenciaDetalleIngresos(String secuencia) {

		String rawQuery;
		rawQuery = "select MAX(ingresos.secitm) from ingresos inner join cta_ingresos where ingresos.secuencia=(select cta_ingresos.secuencia "
				+ " from cta_ingresos where cta_ingresos.numero_factura='"
				+ secuencia + "')";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		String secitem = "0";

		cur.moveToFirst();
		if (cur.moveToFirst()) {
			do {
				secitem = cur.getString(0);
			} while (cur.moveToNext());

		}
		if (secitem == null || secitem.trim().length() == 0) {
			secitem = "0";
		}
		cur.close();
		db.close();
		return secitem;
	}
	
	public void actualizarItemsDetalle(String oc_numero){
		SQLiteDatabase db = getWritableDatabase();
		
		String subQuery =
				"SELECT count(*) FROM pedido_detalle "
				+ "where item > 0 and oc_numero = '"+oc_numero+"'";
					
		Cursor curAux = db.rawQuery(subQuery, null);			
		curAux.moveToFirst();
		int nro_item=0;
		while (!curAux.isAfterLast()) {
			Log.d("", "nro item obtenido de la consulta: "+curAux.getInt(0));
			nro_item = curAux.getInt(0);
			curAux.moveToNext();
		}
		curAux.close();
		
		for (int i = 0; i < nro_item; i++) {
			String rawQuery =
					"update pedido_detalle "
					+ "set item = "+(i+1)
					+ " where cip=''";
		}
		// Por terminar, lo mas optimo seria actualizar por el nuero de la fila, pero eso no existe 
	}
	public int obtenerCantidadPedidoDetalle(String oc_numero){
		SQLiteDatabase db = getReadableDatabase();
		String rawQuery = "select count(*) from Pedido_detalle where oc_numero = '"+oc_numero+"'";
		Cursor cur = db.rawQuery(rawQuery, null);
		int cantidad = 0;
		while (cur.moveToNext()) {
			cantidad = cur.getInt(0);
		}
		cur.close();
		db.close();
		return cantidad;
	}

	public ItemProducto[] obtenerListadoProductos_pedido(String oc_numero) {

		String whereAdd;
		whereAdd =  "and pedido_detalle.oc_numero='" + oc_numero + "' " +
				"and pedido_detalle.cod_politica <> 'ELIM'  ";
		Log.d("Dbclasses :obtenerListadProductos_pedido:","Obteniendo listado de pedidos del oc_numero-> "+oc_numero);
		ItemProducto[] productos= obtenerListadoProductos_pedidoMAIN(whereAdd);
		return productos;
	}

	public ItemProducto obtenerListadoProductos_pedidoBY(String oc_numero, String codpro, int item) {

		String whereAdd;
		whereAdd =  "and pedido_detalle.oc_numero='" + oc_numero + "' " +
				"and pedido_detalle.cod_politica <> 'ELIM' " +
				"and pedido_detalle.cip='"+codpro+"' " +
				"and pedido_detalle.item = "+item+" ";
		Log.d("Dbclasses :obtenerListadoProductos_pedidoBY:","Obteniendo listado de pedidos del oc_numero-> "+oc_numero);
		ItemProducto[] productos= obtenerListadoProductos_pedidoMAIN(whereAdd);
		return productos[0];
	}

	public ItemProducto[] obtenerListadoProductos_pedidoMAIN(String whereAdd) {

		String sqlRAW =
				"select "
						+ "pedido_detalle.item,"
						+ "producto.despro,"
						+ "producto.factor_conversion,"
						+ "pedido_detalle.cip,"
						+ "pedido_detalle.precio_neto,"
						+ "pedido_detalle.cantidad,"
						+ "pedido_detalle.tipo_producto,"
						+ "pedido_detalle.peso_bruto,"
						+ "pedido_detalle.percepcion,"
						+ "unidad_medida.desunimed,"
						+ "producto.cod_rapido,"
						+ "ifNULL(producto.percepcion,'0'), "
						+ "pedido_detalle.unidad_medida, "
						+ "pedido_detalle.precio_bruto, "
						+ "pedido_detalle.precioLista, "
						+ "pedido_detalle.descuento, "
						+ "tp.color, "
						+ "producto.grupo, "
						+ "producto.familia, "
						+ "producto.sub_familia, "
						+ "producto.afecto, "
						+ "pedido_detalle.porcentaje_desc, "
						+ "pedido_detalle.porcentaje_desc_extra, "
						+ "producto._precio_base, "
						+ "pedido_detalle.sec_promo, "
						+ "pedido_detalle.sec_promo_prioridad "
						+ "from "+ Pedido_detalle.TAG+" "
						+ "inner join "+ Producto.TAG+" "
						+ "on ((producto.codpro = pedido_detalle.cip) OR ((substr(pedido_detalle.cip,2,length(pedido_detalle.cip))) =  producto.codpro)) "
						+ "inner join unidad_medida "
						+ "on pedido_detalle.unidad_medida=unidad_medida.codunimed "
						+ "left join tipoProducto tp on producto.tipoProducto = tp.codigoTipo "
						+ "where 5 = 5 "+whereAdd+" "
						+ " order by item asc";


		Log.d(TAG, "::obtenerListadoProductos_pedidoMAIN:___________________________________");
		Log.d(TAG, "::obtenerListadoProductos_pedidoMAIN: "+sqlRAW);
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(sqlRAW, null);

		ItemProducto[] productos = new ItemProducto[cursor.getCount()];

		if (cursor.moveToFirst()) {
			int i = 0;
			do {
				productos[i] = new ItemProducto();
				productos[i].setItem(cursor.getInt(0));
				productos[i].setDescripcion(cursor.getString(1));
				productos[i].setFact_conv(cursor.getInt(2));
				productos[i].setCodprod(cursor.getString(3));
				productos[i].setSubtotal(cursor.getDouble(4));
				productos[i].setCantidad(cursor.getInt(5));
				productos[i].setTipo(cursor.getString(6));
				productos[i].setPeso(cursor.getDouble(7));
				productos[i].setPercepcionPedido(cursor.getDouble(8));
				productos[i].setDesunimed(cursor.getString(9));
				productos[i].setCodProveedor(cursor.getString(10));
				productos[i].setPercepcion(cursor.getDouble(11));
				productos[i].setCodunimed(cursor.getString(12));
				productos[i].setPrecio(cursor.getDouble(13));
				productos[i].setPrecioLista(cursor.getDouble(14));
				productos[i].setDescuento(cursor.getDouble(15));
				productos[i].setFlagTipo(cursor.getString(16));
				productos[i].setGrupo(cursor.getString(17));
				productos[i].setFamilia(cursor.getString(18));
				productos[i].setSubfamilia(cursor.getString(19));
				productos[i].setAfecto(cursor.getString(20));
				productos[i].setPorcentaje_desc(cursor.getDouble(cursor.getColumnIndex("porcentaje_desc")));
				productos[i].setPorcentaje_desc_extra(cursor.getDouble(cursor.getColumnIndex("porcentaje_desc_extra")));
				productos[i].setPrecio_base(cursor.getDouble(cursor.getColumnIndex("_precio_base")));
				productos[i].setSec_promo(cursor.getString(cursor.getColumnIndex("sec_promo")));
				productos[i].setSec_promo_prioridad(cursor.getInt(cursor.getColumnIndex("sec_promo_prioridad")));

				Log.w("DBclasses ::obtenerListadoProductos_pedido::","Item "+i+"  "+productos[i].getItem());
				Log.w("DBclasses ::obtenerListadoProductos_pedido::","Codprod "+productos[i].getCodprod());
				Log.w("DBclasses ::obtenerListadoProductos_pedido::","Tipo "+productos[i].getTipo());

				//Log.d("DBclasses ::obtenerListadoProductos_pedido::","getPercepcion "+productos[i].getPercepcionPedido());
				i++;

			} while (cursor.moveToNext());
		}
		Log.d("Dbclasses :obtenerListadProductos_pedido:","___________________________________");
		cursor.close();
		db.close();

		return productos;
	}

	public ArrayList<ItemProducto> obtenerListadoDevoluciones_pedido(
			String oc_numero) {

		String rawQuery;

		rawQuery = "select producto.despro, producto.factor_conversion, pedido_devolucion.cip, pedido_devolucion.cantidad,"
				+ "unidad_medida.desunimed from pedido_devolucion inner join producto on "
				+ "producto.codpro = pedido_devolucion.cip inner join unidad_medida on "
				+ "pedido_devolucion.unidad_medida=unidad_medida.codunimed "
				+ "where oc_numero='" + oc_numero + "'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(rawQuery, null);

		ArrayList<ItemProducto> devoluciones = new ArrayList<ItemProducto>();

		if (cursor.moveToFirst()) {
			do {
				ItemProducto producto = new ItemProducto();
				producto.setDescripcion(cursor.getString(0));
				producto.setFact_conv(cursor.getInt(1));
				producto.setCodprod(cursor.getString(2));
				producto.setCantidad(cursor.getInt(3));
				producto.setDesunimed(cursor.getString(4));

				devoluciones.add(producto);

			} while (cursor.moveToNext());
		}

		cursor.close();
		db.close();

		return devoluciones;
	}

	public void modificarItempedido(int nCantidad, double peso, double precio,
			String codprod, String oc_numero) {

		String where = "cip = ? and oc_numero = ?";
		String[] args = { codprod, oc_numero };

		try {

			SQLiteDatabase db = getWritableDatabase();

			ContentValues reg = new ContentValues();
			reg.put("cantidad", nCantidad);
			reg.put("precio_bruto", precio);
			reg.put("precio_neto", precio * nCantidad);
			reg.put("peso_bruto", peso * nCantidad);
			reg.put("flag", "N");

			db.update("pedido_detalle", reg, where, args);
			db.close();

			Log.i("DBCLASSES_PESO", "" + peso * nCantidad);

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

	}

	public void modificarItempedido(int nCantidad, double peso, double precio,
			String codprod, String oc_numero, String unidadmedida,
			String ean_item, String flag, String politica, String subTotal,
			double percepcion) {

		String where = "cip = ? and oc_numero = ?";
		String[] args = { codprod, oc_numero };

		try {

			SQLiteDatabase db = getWritableDatabase();

			ContentValues reg = new ContentValues();
			reg.put("cantidad", nCantidad);
			reg.put("precio_bruto", precio);
			reg.put("precio_neto", subTotal);
			reg.put("peso_bruto", peso * nCantidad);
			reg.put("flag", flag);
			reg.put("unidad_medida", unidadmedida);
			reg.put("ean_item", ean_item);
			reg.put("cod_politica", politica);
			reg.put("percepcion", percepcion);
			
			db.update("pedido_detalle", reg, where, args);
			db.close();

			Log.i("DBCLASSES_PESO", "" + peso * nCantidad);

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

	}

	public void modificarItempedido(String oc_numero, String codprod,
			int nCantidad, String subTotal, String pesoTotal,
			double percepcionxCantidad) {

		String where = "cip = ? and oc_numero = ?";
		String[] args = { codprod, oc_numero };

		try {

			SQLiteDatabase db = getWritableDatabase();

			ContentValues reg = new ContentValues();
			reg.put("cantidad", nCantidad);
			reg.put("precio_neto", subTotal);
			reg.put("peso_bruto", pesoTotal);
			reg.put("percepcion", percepcionxCantidad);
			
			db.update("pedido_detalle", reg, where, args);
			db.close();

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

	}
	
	
	public void modificarItemDevolucion(int nCantidad, String codprod,
			String oc_numero) {

		String where = "cip = ? and oc_numero = ?";
		String[] args = { codprod, oc_numero };

		try {

			SQLiteDatabase db = getWritableDatabase();

			ContentValues reg = new ContentValues();
			reg.put("cantidad", nCantidad);

			db.update("pedido_devolucion", reg, where, args);
			db.close();

			Log.i("MODIFICAR ITEM DEVOLUCION", "" + nCantidad);

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

	}
	public boolean modificarCantidadItemPedido(int nCantidad,
											   int nroItem,
											   String codprod,
											   String oc_numero
												) {

		String where = "item =? and cip = ? and oc_numero = ?";
		String[] args = { ""+nroItem, codprod, oc_numero };

		try {
			String codproReal=codprod;
			if(codproReal.charAt(0)=='B')codproReal=codproReal.substring(1);
			double peso=getPesoProducto(codproReal);
			DBPedido_Detalle dnPedidoItem = getPedidosDetalleEntity(oc_numero, codproReal, nroItem);
			SQLiteDatabase db = getWritableDatabase();
			if(dnPedidoItem==null) return false;
			//---------------------//recalcular peso, monto subtotal, y monto de dscto--------------------------------------------------------------------------
			double precioLista = Double.parseDouble(dnPedidoItem.getPrecioLista());
			double precioUnit = Double.parseDouble(dnPedidoItem.getPrecio_bruto());

			double montoDscUnit  = VARIABLES.getDoubleFormaterThreeDecimal(precioLista-precioUnit);

			ContentValues reg = new ContentValues();
			reg.put("cantidad", nCantidad);
			reg.put("precio_neto", VARIABLES.getDoubleFormaterFourDecimal(precioUnit*nCantidad));
			reg.put("peso_bruto", VARIABLES.getDoubleFormaterFourDecimal(peso*nCantidad));
			reg.put("descuento", VARIABLES.getDoubleFormaterThreeDecimal(montoDscUnit*nCantidad));
			db.update("pedido_detalle", reg, where, args);

			db.close();
			Log.i("MODIFICAR ITEM DEVOLUCION", "" + nCantidad);
			return  true;
		} catch (SQLException ex) {
			ex.printStackTrace();
			return false;
		}

	}

	public double getPrecioParcialPedidoDetalle(
											   int nroItem,
											   String codprod,
											   String oc_numero)
	{
			SQLiteDatabase db=getReadableDatabase();

			String sql="Select precio_bruto from pedido_detalle where oc_numero='"+oc_numero+"' and cip= '"+codprod+"' and item= "+nroItem;
			Cursor cursor =db.rawQuery(sql,null, null);
			double precio_bruto=0;
			if (cursor.moveToNext()) {
				precio_bruto = cursor.getDouble(0);
			}
			cursor.close();
			return precio_bruto;

	}
	public double getPesoProducto(
			String codprod)
	{
		SQLiteDatabase db=getReadableDatabase();

		String sql="Select peso from producto where codpro='"+codprod+"' ";
		Cursor cursor =db.rawQuery(sql,null, null);
		double peso=0;
		if (cursor.moveToNext()) {
			peso = cursor.getDouble(0);
		}
		cursor.close();
		return peso;

	}


	public void EliminarItemPedido(String codprod, int item, String oc_numero) {
		String where= "";
		String[] args = null;
		if(item==-1){
			where = "cip = ? and oc_numero = ?";// pk(oc_numero, cip)
			 args = new String[]{codprod, oc_numero};
		}
		else {
			where = "cip = ? and item= ? and oc_numero = ?";// pk(oc_numero, cip, item)
			args = new String[]{ codprod, ""+item, oc_numero };
		}

		try {

			SQLiteDatabase db = getWritableDatabase();
			int ret = db.delete("pedido_detalle", where, args);
			db.close();
			
			Log.i("ELIMINAR ITEM PEDIDO",oc_numero+"  "+codprod);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public void EliminarItemPedido2(String codprod, String oc_numero,
			String sec_promo, String item_promo) {

		String where = "cip = ? and oc_numero = ? and ean_item = ? and cod_politica= ? and cod_politica <> 'ELIM'";
		String[] args = { codprod, oc_numero, item_promo, sec_promo };

		try {

			SQLiteDatabase db = getWritableDatabase();
			int ret = db.delete("pedido_detalle", where, args);
			db.close();

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public void cambiarEstadoEliminado(String codprod, String oc_numero) {

		String where = "cip = ? and oc_numero = ?";
		String[] args = { codprod, oc_numero };

		try {

			SQLiteDatabase db = getWritableDatabase();
			ContentValues reg = new ContentValues();
			reg.put("ean_item", "E");
			db.update("pedido_detalle", reg, where, args);
			db.close();
			Log.i("Pedido_CABECERA", "Modificado ean item = Eliminado");
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public void EliminarItemDevolucion(String codprod, String oc_numero) {

		String where = "cip = ? and oc_numero = ?";
		String[] args = { codprod, oc_numero };

		try {

			SQLiteDatabase db = getWritableDatabase();
			db.delete("pedido_devolucion", where, args);
			db.close();

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public void GuardarMontoPesoPercepcion_Pedido(double monto_total,
			double percepcion_total, double peso_total,
			double totalSujetoPercepcion, String oc_numero) {

		String where = "oc_numero = ?";
		String[] args = { oc_numero };

		try {

			SQLiteDatabase db = getWritableDatabase();

			ContentValues valor = new ContentValues();
			valor.put(DBtables.Pedido_cabecera.MONTO_TOTAL, monto_total);
			valor.put(DBtables.Pedido_cabecera.PERCEPCION_TOTAL,percepcion_total);
			valor.put(DBtables.Pedido_cabecera.PESO_TOTAL, peso_total);
			valor.put(DBtables.Pedido_cabecera.TOTAL_SUJETO_PERCEPCION,totalSujetoPercepcion);

			db.update("pedido_cabecera", valor, where, args);
			db.close();

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	public void guardarPedidoTotales(PedidoCabeceraRecalcular dataRecalcular) {
		String where = "oc_numero like ?";
		String[] args = { dataRecalcular.getOc_numero() };

		try {
			SQLiteDatabase db = getWritableDatabase();
			ContentValues valor = new ContentValues();
			valor.put(DBtables.Pedido_cabecera.SUBTOTAL, dataRecalcular.getSubtotal());
			valor.put(DBtables.Pedido_cabecera.VALOR_IGV, dataRecalcular.getIGV());
			valor.put(DBtables.Pedido_cabecera.MONTO_TOTAL, dataRecalcular.getTotal());
			valor.put(DBtables.Pedido_cabecera.PERCEPCION_TOTAL,dataRecalcular.getPercepcion());
			valor.put(DBtables.Pedido_cabecera.PESO_TOTAL, dataRecalcular.getPeso_total());
			valor.put(DBtables.Pedido_cabecera.TOTAL_SUJETO_PERCEPCION,dataRecalcular.getTotalSujetoPercepcion());

			db.update("pedido_cabecera", valor, where, args);
			db.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public ArrayList<DBClientes> getClientesXRuta() {
		String codven = new SessionManager(_context).getCodigoVendedor();
		String rawQuery;

		int dia = getDiaConfiguracion();

		/*
		 * rawQuery=
		 * "select codcli,nomcli,ruccli,stdcli,comprobante,email,tipo_documento, "
		 * +
		 * "tipo_cliente, tiempo_credito,limite_credito,persona,afecto, ifnull(cliente.sec_visita,0) as secuencia, condicion_venta from  cliente where sec_visita > 0 order by cliente.sec_visita ASC"
		 * ;
		 */

		/*
		 * rawQuery="SELECT  (direccion_cliente.codcli), nomcli, ruccli ," +
		 * "  stdcli ,  comprobante," +
		 * "   email ,   tipo_documento ,   tipo_cliente ," +
		 * "  tiempo_credito,   limite_credito  ," +
		 * "   persona, afecto , condicion_venta, latitud, longitud " +
		 * " FROM cliente inner join direccion_cliente on  direccion_cliente.codcli=cliente.codcli where stdcli='S'"
		 * ;
		 */

		/*
		 * rawQuery=
		 * "select DISTINCT(cliente.codcli),nomcli,ruccli,stdcli,comprobante,email,tipo_documento, "
		 * +
		 * "tipo_cliente, tiempo_credito,limite_credito,persona,afecto, condicion_venta , latitud, longitud from "
		 * +
		 * " cliente inner join direccion_cliente on direccion_cliente.codcli=cliente.codcli where direccion_cliente.orden > 0 "
		 * ;
		 */

		rawQuery = "select cliente.codcli,nomcli,ruccli,stdcli,comprobante,email,tipo_documento,tipo_cliente,"
				+ "tiempo_credito,limite_credito,persona,afecto,condicion_venta from znf_programacion_clientes "
				+ "inner join cliente on znf_programacion_clientes.codcli=cliente.codcli where codven='"
				+ codven
				+ "' "
				+ "and n_dia='"
				+ dia
				+ "' and znf_programacion_clientes.codcli in(select codcli from cliente) "
				+ "and (select COUNT(*) from pedido_cabecera where cod_cli=cliente.codcli) = 0  "
				+ "order by orden_r, orden_z, orden_c";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		ArrayList<DBClientes> dbclientes = new ArrayList<DBClientes>();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			DBClientes dbc = new DBClientes();
			dbc.setCodcli(cur.getString(0));
			dbc.setNomcli(cur.getString(1));
			dbc.setRuccli(cur.getString(2));

			dbc.setStdcli(cur.getString(3));
			dbc.setComprobante(cur.getString(4));

			dbc.setEmail(cur.getString(5));
			dbc.setTipo_documento(cur.getInt(6));
			dbc.setTipo_cliente(cur.getInt(7));
			dbc.setTiempo_credito(cur.getInt(8));
			dbc.setLimite_credito(cur.getFloat(9));
			dbc.setPersona(cur.getString(10));
			dbc.setAfecto(cur.getString(11));

			dbc.setCondicion_venta(cur.getString(12));

			dbclientes.add(dbc);
			cur.moveToNext();
		}
		cur.close();
		db.close();
		return dbclientes;

	}

	public String getProductoXCodigo(String producto_salida) {

		String rawQuery;
		rawQuery = "select producto.despro from  producto where producto.codpro='"
				+ producto_salida + "'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		String secitem = "";

		cur.moveToFirst();
		if (cur.moveToFirst()) {
			do {
				secitem = cur.getString(0);
			} while (cur.moveToNext());

		}
		cur.close();
		db.close();
		return secitem;
	}

	// prueba codunimed
	public String obtener_codXdesunimed(String codigoProducto, String descripcionUnidadMedida) {

		//String rawQuery = "select codunimed from unidad_medida where desunimed='"+ desunimed + "'";
		
		String rawQuery = 
				"select codunimed "+
				"from unidad_medida "+
				"where desunimed = '"+descripcionUnidadMedida+"' "+
				"and ( (codunimed = (SELECT codunimed from producto where codpro='"+codigoProducto+"')) or "+
					  "(codunimed = (SELECT codunimed_almacen from producto where codpro='"+codigoProducto+"'))"+
					")";
		
		
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(rawQuery, null);
		String codunimed = "0";

		cursor.moveToFirst();
		if (cursor.moveToFirst()) {
			do {
				codunimed = cursor.getString(0);
			} while (cursor.moveToNext());

		}
		cursor.close();
		db.close();

		return codunimed;
	}

	public String obtener_desunimedXcod(String cod) {

		String rawQuery = "select desunimed from unidad_medida where codunimed='"
				+ cod + "'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(rawQuery, null);
		String desunimed = "0";

		cursor.moveToFirst();
		if (cursor.moveToFirst()) {
			do {
				desunimed = cursor.getString(0);
			} while (cursor.moveToNext());

		}
		cursor.close();
		db.close();

		return desunimed;
	}

	public int obtener_codunimedXprod(String codpro) {

		String rawQuery = "select codunimed from producto where codpro='"
				+ codpro + "'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(rawQuery, null);
		int codunimed = 0;

		cursor.moveToFirst();
		if (cursor.moveToFirst()) {
			do {
				codunimed = cursor.getInt(0);
			} while (cursor.moveToNext());

		}
		cursor.close();
		db.close();

		return codunimed;
	}

	public String obtener_condPagoXcliente(String codcli) {

		String rawQuery = "select cliente.condicion_venta from cliente where codcli='"
				+ codcli + "'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(rawQuery, null);
		String condPago = "";

		cursor.moveToFirst();
		if (cursor.moveToFirst()) {
			do {
				condPago = cursor.getString(0);
			} while (cursor.moveToNext());

		}
		cursor.close();
		db.close();

		return condPago;
	}

	public void actualizar_stock_xtemp(int cantidad, String codpro) {

		// String where ="codpro = ?";
		// String[] args = {codpro};

		try {

			SQLiteDatabase db = getWritableDatabase();

			String sql = "update mta_kardex set xtemp = xtemp + ? where codpro = ?";

			db.execSQL(sql, new Object[] { cantidad, codpro });
			/*
			 * ContentValues valor = new ContentValues(); valor.put("xtemp",
			 * cantidad);
			 * 
			 * db.update("mta_kardex", valor, where, args); db.close();
			 */

			Log.i("DBCLASSES CANTIDAD", "" + cantidad);

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public void guardar_Stock(String codpro) {

		try {

			String rawQuery = "select stock, xtemp from mta_kardex where codpro='"
					+ codpro + "'";

			String where = "codpro=?";
			String[] args = { codpro };

			SQLiteDatabase db = getReadableDatabase();

			Cursor cursor = db.rawQuery(rawQuery, null);

			int stock = 0;
			int xtemp = 0;

			cursor.moveToFirst();
			if (cursor.moveToFirst()) {
				do {
					stock = cursor.getInt(0);
					xtemp = cursor.getInt(1);

					ContentValues value = new ContentValues();
					value.put("stock", stock - xtemp);
					db.update("mta_kardex", value, where, args);

					value.clear();
					value.put("xtemp", 0);
					db.update("mta_kardex", value, where, args);

				} while (cursor.moveToNext());

			}

			db.close();

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public ArrayList<DBBanco> getBancos() {

		String rawQuery;

		rawQuery = "select * from banco";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		ArrayList<DBBanco> dbbanco = new ArrayList<DBBanco>();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			DBBanco dbr = new DBBanco();
			dbr.setCodban(cur.getString(0));
			dbr.setNomban(cur.getString(1));

			dbbanco.add(dbr);
			cur.moveToNext();
		}
		cur.close();
		db.close();
		return dbbanco;
	}
	
	public ArrayList<DB_Turno> getTurnos() {
        String rawQuery = "SELECT * FROM turno";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur = db.rawQuery(rawQuery, null);
        cur.moveToFirst();
        ArrayList<DB_Turno> lista = new ArrayList<>();
        if (cur.moveToFirst()) {
            do {
            	DB_Turno item = new DB_Turno();
            	item.setCodTurno(cur.getString(0));
            	item.setTurno(cur.getString(1));
            	lista.add(item);
            } while (cur.moveToNext());
        }
        cur.close();
        db.close();
        return lista;
    }

	public ArrayList<DBCtas_xbanco> getCtas_xBanco(String codban) {

		String rawQuery;

		rawQuery = "select * from ctas_xbanco where codban='" + codban + "'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		ArrayList<DBCtas_xbanco> dbctas_xbanco = new ArrayList<DBCtas_xbanco>();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			DBCtas_xbanco dbr = new DBCtas_xbanco();
			dbr.setSecuencia(cur.getString(0));
			dbr.setCodban(cur.getString(1));
			dbr.setItem(cur.getInt(2));
			
			dbr.setCodmon(cur.getString(3));
			Log.d("COBRANZA", "moneda"+cur.getString(3) );
			if(cur.getString(3).equals("1")){
				dbr.setCta_cte("S/. "+cur.getString(4));
			}else if(cur.getString(3).equals("2")){
				dbr.setCta_cte("$. "+cur.getString(4));
			}
			
			
			dbr.setEstado(cur.getString(5));
			
			dbr.setCuenta(cur.getString(6));

			dbctas_xbanco.add(dbr);
			cur.moveToNext();
		}
		cur.close();

		return dbctas_xbanco;// d
	}


	public void guardarDetalle_Ingreso(DBIngresos ingresos) {

		try {
			SQLiteDatabase db = getWritableDatabase();

			ContentValues Nreg = new ContentValues();
			Nreg.put("secuencia", ingresos.getSecuencia());
			Nreg.put("secitm", ingresos.getSecitm());
			Nreg.put("fecpag", ingresos.getFecpag());
			Nreg.put("total", ingresos.getTotal());
			Nreg.put("acuenta", ingresos.getAcuenta());
			Nreg.put("saldo", ingresos.getSaldo());
			Nreg.put("codcue", ingresos.getCodcue());
			Nreg.put("numope", ingresos.getNumope());
			Nreg.put("codforpag", ingresos.getCodforpag());
			Nreg.put("tipo_cambio", ingresos.getTipo_cambio());
			Nreg.put("codmon", ingresos.getCodmon());
			Nreg.put("monto_afecto", ingresos.getMonto_afecto());
			Nreg.put("username", ingresos.getUsername());
			Nreg.put("fecoperacion", ingresos.getFecoperacion());
			Nreg.put("flag", ingresos.getFlag());
			Nreg.put("latitud", ingresos.getLatitud());
			Nreg.put("longitud", ingresos.getLongitu());
			Nreg.put("DT_INGR_FECHASERVIDOR",
					ingresos.getDT_INGR_FECHASERVIDOR());
			Nreg.put("estado", ingresos.getEstado());
			
			Log.d("LETRA",""+ingresos.getObservacion());
			Nreg.put("observacion", ingresos.getObservacion());
			Nreg.put("codcli", ingresos.getCodcli());
			
			Nreg.put("tipoDoc", ingresos.getTipoDoc());
			Nreg.put("serie", ingresos.getSerie());
			Nreg.put("numero", ingresos.getNumero());
			Nreg.put("codigoBanco", ingresos.getCodigoBanco());

			
			db.insert("ingresos", null, Nreg);
			db.close();
			Log.i("INGRESOS", "registro insertado ingresos");

		} catch (Exception e) {
			Log.i("INGRESOS", "Error registro insertado");
		}
	}

	
	public String getSecIngresosxFactura(String numfactura) {
		// TODO Auto-generated method stub
		String rawQuery;
		rawQuery = "SELECT secuencia from cta_ingresos where numero_factura='"
				+ numfactura + "'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		String secitem = "";

		cur.moveToFirst();
		if (cur.moveToFirst()) {
			do {
				secitem = cur.getString(0);
			} while (cur.moveToNext());

		}
		cur.close();
		db.close();
		return secitem;
	}

	public String getSecitm(String sec_ingresos) {

		String rawQuery;
		// rawQuery="SELECT COUNT(ingresos.secitm) FROM ingresos where secuencia='"+sec_ingresos+"'";
		// rawQuery =
		// "select ifnull(max(substr(rtrim(secitm),2,1)),0) from ingresos where substr(rtrim(secitm),1,1)<>'S' and secuencia = "+sec_ingresos+"";

		rawQuery = "select ifnull(MAX(SUBSTR(secitm,2,21)),'0') from ingresos where substr(rtrim(secitm),1,1)<>'S' and secuencia = '"
				+ sec_ingresos + "'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		String max = "0";

		cur.moveToFirst();
		if (cur.moveToFirst()) {
			do {
				max = cur.getString(0);
			} while (cur.moveToNext());

		}
		cur.close();
		db.close();
		return max;
	}

	public void actualizarCabecera_Ingreso(String sec_ingresos, String saldo,
			double acuenta) {

		String where = "secuencia= ?";
		String[] args = { sec_ingresos };

		try {

			SQLiteDatabase db = getWritableDatabase();

			ContentValues valor = new ContentValues();
			valor.put("saldo", saldo);
			db.update("cta_ingresos", valor, where, args);

			valor.clear();
			valor.put("acuenta", acuenta);
			db.update("cta_ingresos", valor, where, args);

			db.close();

			Log.i("DBCLASSES CANTIDAD", "acuenta" + acuenta + "    saldo"
					+ saldo);

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

	}

	public ArrayList<DBMotivo_noventa> obtenerMotivo_noventa() {

		String rawQuery;
		rawQuery = "select * from motivo_noventa where cod_noventa!="+ GlobalVar.CODIGO_VISITA_CLIENTE;

		ArrayList<DBMotivo_noventa> motivo_noventa = new ArrayList<DBMotivo_noventa>();

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);

		cur.moveToFirst();
		if (cur.moveToFirst()) {
			do {
				DBMotivo_noventa obj = new DBMotivo_noventa();
				obj.setCod_noventa(cur.getInt(0));
				obj.setDes_noventa(cur.getString(1));
				motivo_noventa.add(obj);
			} while (cur.moveToNext());

		}
		cur.close();
		db.close();
		return motivo_noventa;

	}

	public void Anular_pedido(String oc_numero, int cod_noventa) {

		String where = "oc_numero = ?";
		String[] args = { oc_numero };

		try {

			SQLiteDatabase db = getWritableDatabase();

			ContentValues valor = new ContentValues();
			valor.put("cod_noventa", cod_noventa);
			valor.put("estado", "A");

			db.update("pedido_cabecera", valor, where, args);
			db.close();

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

	}

	public void guardarDepositos(DBDepositos dbdepositos) {

		try {
			SQLiteDatabase db = getWritableDatabase();

			ContentValues Nreg = new ContentValues();
			Nreg.put("secuencia", dbdepositos.getSecuencia());
			Nreg.put("id_banco", dbdepositos.getId_banco());
			Nreg.put("id_num_cta", dbdepositos.getId_num_cta());
			Nreg.put("fecha", dbdepositos.getFecha());
			Nreg.put("num_ope", dbdepositos.getNum_ope());
			Nreg.put("moneda", dbdepositos.getMoneda());
			Nreg.put("monto", dbdepositos.getMonto());
			Nreg.put("estado", dbdepositos.getEstado());
			Nreg.put("codven", dbdepositos.getCodven());
			Nreg.put("DT_DEPO_FECHASERVIDOR",
					dbdepositos.getDT_DEPO_FECHASERVIDOR());
			Nreg.put("BI_DEPO_FLAG", dbdepositos.getBI_DEPO_FLAG());
			Nreg.put("TXT_DEPO_FECHA_REGISTRO",
					dbdepositos.getTXT_DEPO_FECHA_REGISTRO());

			db.insert("depositos", "secuencia", Nreg);
			db.close();
			Log.i("DEPOSITOS", "Deposito registrado correctamente  ****");

		} catch (Exception e) {
			Log.i("DEPOSITOS", "Error al guardar DEPOSITO ****");
		}
	}

	public ArrayList<DBPedido_Cabecera> getPedidosCabecera(String tipo_vista, String txtbuscar) {
		eliminarNulos();
		String rawQuery;
		String codven = new SessionManager(_context).getCodigoVendedor();

		String addWHere="";
		if (tipo_vista.equals("PREVENTA")){
			addWHere="and pc.flag <> 'A' and pc.tipoRegistro IN (" +
					"'"+ PedidosActivity.TIPO_PEDIDO+ "'," +
					"'"+ PedidosActivity.TIPO_COTIZACION+ "'" +
					") ";
		}else{
			addWHere="and (pc.cod_noventa ='"+GlobalVar.CODIGO_VISITA_CLIENTE+"') ";
		}

		if (txtbuscar.length()>0){
			txtbuscar="%"+txtbuscar.replace(" ", "%")+"%";
			addWHere+="and c.nomcli like '"+txtbuscar+"' ";

		}

		rawQuery = "select pc.cod_cli,pc.monto_total,pc.flag, pc.cond_pago, ifnull(pc.nro_letra,0) , " +
					"pc.cod_noventa, pc.oc_numero, pc.peso_total, pc.fecha_oc, pc.estado, pc.percepcion_total, "+
					"pc.moneda," +
					"pc.tipoRegistro, ifnull(pc.pedidoAnterior,''), " +
					"pc.latitud," +
					"ifnull(c.nomcli, '') AS nomcli, " +
					"subTotal as sub_total "+
					"from pedido_cabecera pc left join "+
					"cliente c on c.codcli= pc.cod_cli "+
					"where pc.oc_numero <> 0 " +
				addWHere+" "+
				"and pc.cod_cli !='TPLAST-VISITA' and pc.cod_emp='"+codven+"' " +
				"order by pc.oc_numero DESC";

		Log.d("QUERY REPORTE", " :::::> " + rawQuery);

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		ArrayList<DBPedido_Cabecera> lista_pedidos = new ArrayList<DBPedido_Cabecera>();
		cur.moveToFirst();
		Log.d("QUERY REPORTE", " :::::> size lista " + cur.getCount());
		while (!cur.isAfterLast()) {

			DBPedido_Cabecera dbpedido = new DBPedido_Cabecera();			
			dbpedido.setCod_cli(cur.getString(0));
			dbpedido.setMonto_total(cur.getString(1));
			dbpedido.setFlag(cur.getString(2));
			dbpedido.setCond_pago(cur.getString(3));
			dbpedido.setNroletra(cur.getString(4));
			dbpedido.setCod_noventa(cur.getInt(5));
			dbpedido.setOc_numero(cur.getString(6));
			dbpedido.setPeso_total(cur.getString(7));
			dbpedido.setFecha_oc(cur.getString(8));
			dbpedido.setEstado(cur.getString(9));
			dbpedido.setPercepcion_total(cur.getString(10));
			dbpedido.setMoneda(cur.getString(11));
			dbpedido.setTipoRegistro(cur.getString(12));
			dbpedido.setPedidoAnterior(cur.getString(13));
			dbpedido.setSubTotal(cur.getString(cur.getColumnIndex("sub_total")));
			dbpedido.setLatitud(cur.getString(cur.getColumnIndex("latitud")));
				Cliente cliente =new Cliente();
				cliente.setCodigoCliente(dbpedido.getCod_cli());
				cliente.setNombre(cur.getString(cur.getColumnIndex("nomcli")));
			dbpedido.setCliente(cliente);

			lista_pedidos.add(dbpedido);
			cur.moveToNext();
		}
		cur.close();
		db.close();
		return lista_pedidos;// d
	}

	public ArrayList<DBPedido_Cabecera> getPedidosCabeceraxCliente(String codcli) {
		String rawQuery;
		rawQuery = "select * from pedido_cabecera  where cod_cli='" + codcli
				+ "'";
		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		ArrayList<DBPedido_Cabecera> lista_pedidos = new ArrayList<DBPedido_Cabecera>();
		cur.moveToFirst();
		while (!cur.isAfterLast()) {

			DBPedido_Cabecera dbpedido = new DBPedido_Cabecera();
			dbpedido.setOc_numero(cur.getString(0));
			dbpedido.setSitio_enfa(cur.getString(1));
			dbpedido.setMonto_total(cur.getString(2));
			dbpedido.setValor_igv(cur.getString(3));
			dbpedido.setMoneda(cur.getString(4));
			dbpedido.setFecha_oc(cur.getString(5));
			dbpedido.setFecha_mxe(cur.getString(6));
			dbpedido.setCond_pago(cur.getString(7));
			dbpedido.setCod_cli(cur.getString(8));
			dbpedido.setCod_emp(cur.getString(9));
			dbpedido.setEstado(cur.getString(10));
			dbpedido.setUsername(cur.getString(11));
			dbpedido.setRuta(cur.getString(12));
			dbpedido.setObserv(cur.getString(13));
			dbpedido.setCod_noventa(cur.getInt(14));
			dbpedido.setPeso_total(cur.getString(15));
			lista_pedidos.add(dbpedido);
			cur.moveToNext();
		}
		cur.close();
		db.close();
		return lista_pedidos;
	}

	public String obtenerNomcliXCodigo(String cod_cli) {
		// TODO Auto-generated method stub
		String rawQuery;
		rawQuery = "select nomcli from cliente where codcli='" + cod_cli.trim()
				+ "'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		String nomcli = cod_cli;

		cur.moveToFirst();
		if (cur.moveToFirst()) {
			do {
				nomcli = cur.getString(0);
			} while (cur.moveToNext());

		}
		cur.close();
		db.close();
		return nomcli;

	}

	public ArrayList<DBPedido_Cabecera> getPedidosCabeceraXCodigo(String cod_cabecera) {
		// TODO Auto-generated method stub

		String rawQuery;

		rawQuery = "select oc_numero,sitio_enfa,monto_total,valor_igv,moneda,fecha_oc,fecha_mxe,cond_pago,cod_cli,cod_emp,estado,username,ruta,observacion,cod_noventa,ifnull(codigo_familiar,''),dt_pedi_fechaservidor,percepcion_total from pedido_cabecera where oc_numero='"
				+ cod_cabecera + "'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		ArrayList<DBPedido_Cabecera> lista_pedidos = new ArrayList<DBPedido_Cabecera>();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			DBPedido_Cabecera dbpedido = new DBPedido_Cabecera();
			dbpedido.setOc_numero(cur.getString(0));
			dbpedido.setSitio_enfa(cur.getString(1));
			dbpedido.setMonto_total(cur.getString(2));
			dbpedido.setValor_igv(cur.getString(3));
			dbpedido.setMoneda(cur.getString(4));
			dbpedido.setFecha_oc(cur.getString(5));
			dbpedido.setFecha_mxe(cur.getString(6));
			dbpedido.setCond_pago(cur.getString(7));
			dbpedido.setCod_cli(cur.getString(8));
			dbpedido.setCod_emp(cur.getString(9));
			dbpedido.setEstado(cur.getString(10));
			dbpedido.setUsername(cur.getString(11));
			dbpedido.setRuta(cur.getString(12));
			dbpedido.setObserv(cur.getString(13));
			dbpedido.setCod_noventa(cur.getInt(14));
			dbpedido.setCodigo_familiar(cur.getString(15));
			dbpedido.setDT_PEDI_FECHASERVIDOR(cur.getString(16));
			dbpedido.setPercepcion_total(cur.getString(17));

			lista_pedidos.add(dbpedido);
			cur.moveToNext();
		}
		cur.close();
		db.close();
		return lista_pedidos;
	}

	

	

	public ArrayList<DBTb_Promocion_Detalle> obtenerProdConPromociones(
			String oc_numero) {

		String rawQuery = "select tb_promocion_detalle.*,pedido_detalle.cantidad,pedido_detalle.precio_bruto from tb_promocion_detalle inner join pedido_detalle on "
				+ "tb_promocion_detalle.entrada=pedido_detalle.cip "
				+ "where pedido_detalle.oc_numero='" + oc_numero + "'";

		ArrayList<DBTb_Promocion_Detalle> detalles_promo = new ArrayList<DBTb_Promocion_Detalle>();

		try {

			SQLiteDatabase db = getReadableDatabase();
			Cursor cursor = db.rawQuery(rawQuery, null);

			cursor.moveToFirst();
			if (cursor.moveToFirst()) {
				do {
					DBTb_Promocion_Detalle detalle = new DBTb_Promocion_Detalle();
					detalle = new DBTb_Promocion_Detalle();
					detalle.setSecuencia(cursor.getInt(0));
					detalle.setGeneral(cursor.getInt(1));
					detalle.setPromocion(cursor.getString(2));
					detalle.setCodalm(cursor.getString(3));
					detalle.setTipo(cursor.getString(4));
					detalle.setItem(cursor.getInt(5));
					detalle.setAgrupado(cursor.getInt(6));
					detalle.setEntrada(cursor.getString(7));
					detalle.setMonto(cursor.getDouble(8));
					detalle.setCondicion(cursor.getString(9));
					detalle.setCant_condicion(cursor.getInt(10));
					detalle.setSalida(cursor.getString(11));
					detalle.setCant_promocion(cursor.getInt(12));
					detalle.setMax_pedido(cursor.getInt(13));
					detalle.setTotal_agrupado(cursor.getInt(14));
					detalle.setCant_comp(cursor.getInt(15));
					detalle.setPrec_comp(cursor.getDouble(16));
					detalles_promo.add(detalle);
				} while (cursor.moveToNext());

			}

			cursor.close();
			db.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return detalles_promo;
	}

	

	public String getDepositos_Max() {
		// TODO Auto-generated method stub

		String rawQuery;
		rawQuery = "select max(depositos.secuencia) from depositos";
		String sec = "0";
		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);

		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			sec = (cur.getString(0));

			cur.moveToNext();
		}
		if (sec == null || sec.trim().length() == 0) {
			sec = "null";
		}
		cur.close();
		db.close();
		return sec;// d
	}

	public ArrayList<DBDepositos> getDepositos_envio_pendientes() {
		// TODO Auto-generated method stub

		String rawQuery;

		rawQuery = "select * from depositos where BI_DEPO_FLAG in ('P','I')";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		ArrayList<DBDepositos> lista_pedidos = new ArrayList<DBDepositos>();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			DBDepositos dbdepositos = new DBDepositos();
			dbdepositos.setSecuencia(cur.getString(0));
			dbdepositos.setId_banco(cur.getString(1));
			dbdepositos.setId_num_cta(cur.getInt(2));
			dbdepositos.setFecha(cur.getString(3));
			dbdepositos.setNum_ope(cur.getString(4));
			dbdepositos.setMoneda(cur.getString(5));
			dbdepositos.setMonto(cur.getString(6));
			dbdepositos.setEstado(cur.getString(7));
			dbdepositos.setCodven(cur.getString(8));
			dbdepositos.setDT_DEPO_FECHASERVIDOR(cur.getString(9));
			dbdepositos.setBI_DEPO_FLAG(cur.getString(10));
			dbdepositos.setTXT_DEPO_FECHA_REGISTRO(cur.getString(11));

			Log.w("(dbclasses)GETDEPOSITOS_ENVIO_PENDIENTES",
					"FECHA_REGISTRO: " + cur.getString(11));

			lista_pedidos.add(dbdepositos);
			cur.moveToNext();

		}
		cur.close();
		db.close();
		return lista_pedidos;// d
	}

	public String obtenerNombreBanco(String id_banco) {
		// TODO Auto-generated method stub
		String rawQuery;
		rawQuery = "select nomban from banco where codban='" + id_banco + "'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		String nombanco = "";

		cur.moveToFirst();
		if (cur.moveToFirst()) {
			do {
				nombanco = cur.getString(0);
			} while (cur.moveToNext());

		}
		cur.close();
		db.close();
		return nombanco;

	}

	public ArrayList<DBCta_Ingresos> getCtas_Ingresos() {
		String rawQuery;

		rawQuery = "select c.secuencia, c.codmon, c.coddoc, c.serie_doc,c.numero_factura, c.total , "
				+ "c.acuenta, c.saldo, c.feccom, c.codcli,c.username,c.codven from cta_ingresos as c  "
				+ "inner join ingresos as i on c.secuencia=i.secuencia where i.flag='P' or i.flag='E'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		ArrayList<DBCta_Ingresos> dbcta_ingresos = new ArrayList<DBCta_Ingresos>();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			DBCta_Ingresos dbcta = new DBCta_Ingresos();
			dbcta.setSecuencia(cur.getString(0));
			dbcta.setCodmon(cur.getString(1));
			dbcta.setCoddoc(cur.getString(2));
			dbcta.setSerie_doc(cur.getString(3));
			dbcta.setNumero_factura(cur.getString(4));
			dbcta.setTotal(cur.getString(5));
			dbcta.setAcuenta(cur.getString(6));
			dbcta.setSaldo(cur.getString(7));
			dbcta.setFeccom(cur.getString(8));
			dbcta.setCodcli(cur.getString(9));
			dbcta.setUsername(cur.getString(10));
			dbcta.setFecoperacion(cur.getString(11));

			dbcta_ingresos.add(dbcta);
			cur.moveToNext();
		}
		cur.close();
		db.close();
		return dbcta_ingresos;

	}

	public int getProductoXNombre(String string) {
		// TODO Auto-generated method stub
		String rawQuery;
		rawQuery = "select count(despro) from producto where despro='" + string
				+ "'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		int a = 0;

		cur.moveToFirst();
		if (cur.moveToFirst()) {
			do {
				a = cur.getInt(0);
			} while (cur.moveToNext());

		}
		cur.close();
		db.close();
		return a;
	}

	public ArrayList<DBCtas_xbanco> getCtas_xBanco() {

		String rawQuery;

		rawQuery = "select * from ctas_xbanco";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		ArrayList<DBCtas_xbanco> dbctas_xbanco = new ArrayList<DBCtas_xbanco>();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			DBCtas_xbanco dbr = new DBCtas_xbanco();
			dbr.setSecuencia(cur.getString(0));
			dbr.setCodban(cur.getString(1));
			dbr.setItem(cur.getInt(2));
			dbr.setCodmon(cur.getString(3));
			dbr.setCta_cte(cur.getString(4));
			dbr.setEstado(cur.getString(5));

			dbctas_xbanco.add(dbr);
			cur.moveToNext();
		}
		cur.close();
		db.close();
		return dbctas_xbanco;// d
	}

	public ArrayList<DBCta_Ingresos> getCtas_Ingresos(String secuencia) {
		String rawQuery;

		rawQuery = "Select * from cta_ingresos where secuencia='" + secuencia
				+ "'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		ArrayList<DBCta_Ingresos> dbcta_ingresos = new ArrayList<DBCta_Ingresos>();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			DBCta_Ingresos dbcta = new DBCta_Ingresos();
			dbcta.setSecuencia(cur.getString(0));
			dbcta.setCodmon(cur.getString(1));
			dbcta.setCoddoc(cur.getString(2));
			dbcta.setSerie_doc(cur.getString(3));
			dbcta.setNumero_factura(cur.getString(4));
			dbcta.setTotal(cur.getString(5));
			dbcta.setAcuenta(cur.getString(6));
			dbcta.setSaldo(cur.getString(7));
			dbcta.setFeccom(cur.getString(8));
			dbcta.setCodcli(cur.getString(9));
			dbcta.setUsername(cur.getString(10));
			dbcta.setFecoperacion(cur.getString(11));

			dbcta_ingresos.add(dbcta);
			cur.moveToNext();
		}
		cur.close();
		db.close();
		return dbcta_ingresos;

	}

	// ///////////////////////////////METODOS DE SINCRONIZACION
	// (JSON)//////////////////////////////

	public int syncClientexVendedor(JSONArray jArray,  String fecha,int start) throws JSONException {

		JSONObject jsonData = null;
		ContentValues cv = new ContentValues();

		if (start==0 && fecha.equals("TODOS")){
			getReadableDatabase().delete(DBtables.Cliente.TAG, null, null);
		}
		SQLiteDatabase db = getWritableDatabase();

		db.beginTransaction();

		try {

			for (int i = 0; i < jArray.length(); i++) {

				jsonData = jArray.getJSONObject(i);
				cv.put(DBtables.Cliente._ID, -1);
				cv.put(DBtables.Cliente.PK_CODCLI, jsonData.getString("codcli").trim());
				cv.put(DBtables.Cliente.NOMCLI, jsonData.getString("nomcli").trim());
				cv.put(DBtables.Cliente.RUCCLI, jsonData.getString("ruccli").trim());
				cv.put(DBtables.Cliente.STDCLI, jsonData.getString("stdcli").trim());
				cv.put(DBtables.Cliente.COMPROBANTE,jsonData.getString("comprobante").trim());
				cv.put(DBtables.Cliente.EMAIL, jsonData.getString("email").trim());
				cv.put(DBtables.Cliente.TIPO_DOCUMENTO,jsonData.getString("tipo_documento").trim());
				cv.put(DBtables.Cliente.TIPO_CLIENTE,jsonData.getString("tipo_cliente").trim());
				cv.put(DBtables.Cliente.TIEMPO_CREDITO,jsonData.getString("tiempo_credito").trim());
				cv.put(DBtables.Cliente.LIMITE_CREDITO,jsonData.getString("limite_credito").trim());
				cv.put(DBtables.Cliente.PERSONA, jsonData.getString("persona").trim());
				cv.put(DBtables.Cliente.AFECTO, jsonData.getString("afecto").trim());
				cv.put(DBtables.Cliente.CONDICION_VENTA,jsonData.getString("condicion_venta").trim());
				cv.put(DBtables.Cliente.CODIGO_FAMILIAR,jsonData.getString("codigo_familiar").trim());
				cv.put(DBtables.Cliente.FECHA_COMPRA,jsonData.getString("fecha_compra").trim());
				cv.put(DBtables.Cliente.MONTO_COMPRA,jsonData.getString("monto_compra").trim());

				cv.put(DBtables.Cliente.DIRECCION_FISCAL, jsonData.getString(DBtables.Cliente.DIRECCION_FISCAL).trim());
				cv.put(DBtables.Cliente.MONEDA_DOCUMENTO, jsonData.getString(DBtables.Cliente.MONEDA_DOCUMENTO).trim());
				cv.put(DBtables.Cliente.MONEDA_LIMITE_CREDITO, jsonData.getString(DBtables.Cliente.MONEDA_LIMITE_CREDITO).trim());
				cv.put(DBtables.Cliente.FLAG_MSPACK, jsonData.getString(DBtables.Cliente.FLAG_MSPACK).trim());
				cv.put(DBtables.Cliente.TELEFONO, jsonData.getString(DBtables.Cliente.TELEFONO).trim());
				// campo extra de la tabla "cliente" del android, le pongo ("")
				// por defecto
				// cada vez que se sincronice
				cv.put(DBtables.Cliente.OBSERVACION, "");

				if (jsonData.has(DBtables.Cliente.AFECTO_PERCEPCION)) {
					cv.put(DBtables.Cliente.AFECTO_PERCEPCION, jsonData.getString(DBtables.Cliente.AFECTO_PERCEPCION).trim());
					// Log.d("DBclasses ::syncClientexVendedor::",jsonData.getString(DBtables.Cliente.AFECTO_PERCEPCION).trim());
				} else {
					cv.put(DBtables.Cliente.AFECTO_PERCEPCION, "0");
				}

				if (jsonData.has(DBtables.Cliente.PERCEPCION_ESPECIAL)) {
					cv.put(DBtables.Cliente.PERCEPCION_ESPECIAL, jsonData.getString(DBtables.Cliente.PERCEPCION_ESPECIAL).trim());
					// Log.d("DBclasses ::syncClientexVendedor::",jsonData.getString(DBtables.Cliente.PERCEPCION_ESPECIAL).trim());
				} else {
					cv.put(DBtables.Cliente.PERCEPCION_ESPECIAL, "0");
				}

				if (jsonData.has(DBtables.Cliente.PERCEPCION)) {
					cv.put(DBtables.Cliente.PERCEPCION,jsonData.getString(DBtables.Cliente.PERCEPCION));
					// Log.d("DBclasses ::syncClientexVendedor::","PERCEPCION:"+jsonData.getString(DBtables.Cliente.PERCEPCION));
				} else {
					cv.put(DBtables.Cliente.PERCEPCION, "0");
				}

				cv.put(DBtables.Cliente.FLAGCOBRANZA,jsonData.getString("flagCobranza").trim());



				cv.put(DBtables.Cliente.SECTOR, jsonData.getString(DBtables.Cliente.SECTOR).trim());
				cv.put(DBtables.Cliente.GIRO, 	jsonData.getString(DBtables.Cliente.GIRO).trim());
				cv.put(DBtables.Cliente.CANAL, 	jsonData.getString(DBtables.Cliente.CANAL).trim());
				cv.put(DBtables.Cliente.UNIDAD_NEGOCIO,	jsonData.getString(DBtables.Cliente.UNIDAD_NEGOCIO).trim());
				cv.put(DBtables.Cliente.RUBRO_CLIENTE,	jsonData.getString(DBtables.Cliente.RUBRO_CLIENTE).trim());
				cv.put(DBtables.Cliente.DISPONIBLE_CREDITO,	jsonData.getString(DBtables.Cliente.DISPONIBLE_CREDITO).trim());
				cv.put(DBtables.Cliente.codven_asginados,	jsonData.getString(DBtables.Cliente.codven_asginados).trim());
				cv.put(DBtables.Cliente.sistema,	jsonData.getString(DBtables.Cliente.sistema).trim());
				cv.put(DBtables.Cliente.moneda_ultima_compra,	jsonData.getString(DBtables.Cliente.moneda_ultima_compra).trim());

				long ix= db.insertWithOnConflict(DBtables.Cliente.TAG, null, cv,SQLiteDatabase.CONFLICT_IGNORE);
				Log.i("CLIENTE",
						"syncClientexVendedor:: i= " + i + " CODCLI: "
								+ jsonData.getString("codcli").trim()+" is Insert "+(ix>0));
			}

			db.setTransactionSuccessful();

		} catch (JSONException e) {
			Log.e(DBclasses.TAG, "JSON Exception CLIENTE:" + e.getMessage());
			throw new JSONException(e.getMessage());
		}
		catch (Exception e) {
			Log.e(DBclasses.TAG, "JSON Exception CLIENTE:" + e.getMessage());
			throw new JSONException(e.getMessage());
		}finally {
			db.endTransaction();
			db.close();
		}

		return  jArray.length();

	}


	public int syncClienteContactoxVendedor(JSONArray jArray, String fecha, int start) throws JSONException {

		JSONObject jsonData = null;
		SQLiteDatabase db = getWritableDatabase();
		db.beginTransaction();


		if (start==0 && fecha.equals("TODOS")){
			String where = "flag <> ?";
			String[] args = { "P"};
			db.delete(DBtables.CLiente_Contacto.TAG, where, args);
		}


		try {

			for (int i = 0; i < jArray.length(); i++) {

				jsonData = jArray.getJSONObject(i);
				ContentValues cv = new ContentValues();
				cv.put(DBtables.CLiente_Contacto.codcli, jsonData.getString("codcli").trim());
				cv.put(DBtables.CLiente_Contacto.id_contacto, jsonData.getInt("id_contacto"));
				cv.put(DBtables.CLiente_Contacto.nombre_contacto, jsonData.getString("nombre_contacto").trim());
				cv.put(DBtables.CLiente_Contacto.dni, jsonData.getString("dni").trim());
				cv.put(DBtables.CLiente_Contacto.telefono, jsonData.getString("telefono").trim());
				cv.put(DBtables.CLiente_Contacto.celular, jsonData.getString("celular").trim());
				cv.put(DBtables.CLiente_Contacto.email, jsonData.getString("email").trim());
				cv.put(DBtables.CLiente_Contacto.cargo, jsonData.getString("cargo").trim());
				cv.put(DBtables.CLiente_Contacto.estado, jsonData.getString("estado").trim());
				cv.put(DBtables.CLiente_Contacto.fec_nacimiento, jsonData.getString("fec_nacimiento").trim());
				cv.put(DBtables.CLiente_Contacto.flag, jsonData.getString("flag").trim());


				//db.insert(DBtables.CLiente_Contacto.TAG, null, cv);
				db.insertWithOnConflict(DBtables.CLiente_Contacto.TAG, null, cv,SQLiteDatabase.CONFLICT_IGNORE);
				Log.i("CLIENTE","i= " + i + " CODCLI: "+ jsonData.getString("codcli").trim());
			}

			db.setTransactionSuccessful();

		} catch (JSONException e) {
			Log.e(DBclasses.TAG, "JSON Exception CLIENTE contacto:" + e.getMessage());
			throw new JSONException(e.getMessage());
		} finally {
			db.endTransaction();
			db.close();
		}

		return  jArray.length();

	}

	public int syncDireccionCliente(JSONArray jArray, String fecha, int start) {

		JSONObject jsonData = null;
		ContentValues cv = new ContentValues();

		if(start==0 && fecha.equals("TODOS")){
			getReadableDatabase().delete(DBtables.Direccion_cliente.TAG, null, null);
		}
		SQLiteDatabase db = getWritableDatabase();

		db.beginTransaction();

		try {

			for (int i = 0; i < jArray.length(); i++) {

				jsonData = jArray.getJSONObject(i);
				cv.put(DBtables.Direccion_cliente.PK_CODCLI, jsonData.getString("codcli").trim());
				cv.put(DBtables.Direccion_cliente.PK_ITEM,jsonData.getString("item").trim());
				cv.put(DBtables.Direccion_cliente.DIRECCION, jsonData.getString("direccion").trim());
				cv.put(DBtables.Direccion_cliente.TELEFONO,jsonData.getString("telefono").trim());
				cv.put(DBtables.Direccion_cliente.CODDEP,jsonData.getString("coddep").trim());
				cv.put(DBtables.Direccion_cliente.CODPRV,jsonData.getString("codprv").trim());
				cv.put(DBtables.Direccion_cliente.UBIGEO,jsonData.getString("ubigeo").trim());
				cv.put(DBtables.Direccion_cliente.DES_CORTA, jsonData.getString("des_corta").trim());
				cv.put(DBtables.Direccion_cliente.LATITUD,jsonData.getString("latitud").trim());
				cv.put(DBtables.Direccion_cliente.LONGITUD,jsonData.getString("longitud").trim());
				cv.put(DBtables.Direccion_cliente.DOC_ADICIONAL,jsonData.getString("docAdicional").trim());
				cv.put(DBtables.Direccion_cliente.ESTADO,jsonData.getString("estado").trim());
				cv.put(Direccion_cliente.altitud,jsonData.getString("altitud").trim());

//				db.insert(DBtables.Direccion_cliente.TAG, null, cv);
				db.insertWithOnConflict(DBtables.Direccion_cliente.TAG, null, cv,SQLiteDatabase.CONFLICT_IGNORE);
				Log.i("DIRECCION CLIENTE", "i= " + i + " CODCLI: "+ jsonData.getString("codcli").trim() + " ITEM: "+ jsonData.getString("item").trim()+" DOC:"+jsonData.getString("docAdicional").trim());
			}

			db.setTransactionSuccessful();

		} catch (JSONException e) {
			Log.e(DBclasses.TAG,
					"JSON Exception DIRECCION CLIENTE:" + e.getMessage());
		} finally {
			db.endTransaction();
			db.close();
		}
	return  jArray.length();
	}

	public void syncMotivoNoVenta(JSONArray jArray) {

		JSONObject jsonData = null;
		ContentValues cv = new ContentValues();

		getReadableDatabase().delete(DBtables.Motivo_noventa.TAG, null, null);
		SQLiteDatabase db = getWritableDatabase();

		db.beginTransaction();

		try {

			for (int i = 0; i < jArray.length(); i++) {

				jsonData = jArray.getJSONObject(i);
				cv.put(DBtables.Motivo_noventa.COD_NOVENTA,jsonData.getString("cod_noventa").trim());
				cv.put(DBtables.Motivo_noventa.DES_NOVENTA,jsonData.getString("des_noventa").trim());

				db.insert(DBtables.Motivo_noventa.TAG, null, cv);
				Log.i("MOTIVO NO VENTA", "i= " + i + " COD_NOVENTA: "+ jsonData.getString("cod_noventa").trim());
			}

			db.setTransactionSuccessful();

		} catch (JSONException e) {
			Log.e(DBclasses.TAG,
					"JSON Exception MOTIVO NO VENTA:" + e.getMessage());
		} finally {
			db.endTransaction();
			db.close();
		}

	}

	public void syncLocales(JSONArray jArray) {

		JSONObject jsonData = null;
		ContentValues cv = new ContentValues();

		getReadableDatabase().delete(DBtables.Locales.TAG, null, null);
		SQLiteDatabase db = getWritableDatabase();

		db.beginTransaction();

		try {

			for (int i = 0; i < jArray.length(); i++) {

				jsonData = jArray.getJSONObject(i);
				cv.put(DBtables.Locales.PK_ID_LOCAL,
						jsonData.getString("id_local").trim());
				cv.put(DBtables.Locales.DES_LOCAL,
						jsonData.getString("des_local").trim());
				cv.put(DBtables.Locales.DIRECCION,
						jsonData.getString("direccion").trim());
				cv.put(DBtables.Locales.CODDEP, jsonData.getString("coddep")
						.trim());
				cv.put(DBtables.Locales.CODPROV, jsonData.getString("codprov")
						.trim());
				cv.put(DBtables.Locales.UBIGEO, jsonData.getString("ubigeo")
						.trim());
				cv.put(DBtables.Locales.TLF, jsonData.getString("tlf").trim());
				cv.put(DBtables.Locales.EMAIL, jsonData.getString("email")
						.trim());
				cv.put(DBtables.Locales.ESTADO, jsonData.getString("estado")
						.trim());
				cv.put(DBtables.Locales.BG_COLOR, jsonData
						.getString("bg_color").trim());
				cv.put(DBtables.Locales.TXT_COLOR,
						jsonData.getString("txt_color").trim());
				cv.put(DBtables.Locales.LATITUD, jsonData.getString("latitud")
						.trim());
				cv.put(DBtables.Locales.LONGITUD, jsonData
						.getString("longitud").trim());

				db.insert(DBtables.Locales.TAG, null, cv);
				Log.i("LOCALES", "i= " + i + " ID_LOCAL: "
						+ jsonData.getString("id_local").trim()
						+ " DES_LOCAL: "
						+ jsonData.getString("des_local").trim());
			}

			db.setTransactionSuccessful();

		} catch (JSONException e) {
			Log.e(DBclasses.TAG, "JSON Exception LOCALES:" + e.getMessage());
		} finally {
			db.endTransaction();
			db.close();
		}
	}

	public void syncConfiguracion(JSONArray jArray) throws JSONException {

		JSONObject jsonData = null;
		ContentValues cv = new ContentValues();

		String where = "nombre not in('mensaje_licencia_uso', 'clave_forzar')";

		getReadableDatabase().delete(DBtables.Configuracion.TAG, where, null);
		SQLiteDatabase db = getWritableDatabase();

		db.beginTransaction();

		try {

			for (int i = 0; i < jArray.length(); i++) {



				jsonData = jArray.getJSONObject(i);
				cv.put(DBtables.Configuracion.NOMBRE,
						jsonData.getString("nombre").trim());
				cv.put(DBtables.Configuracion.VALOR, jsonData
						.getString("valor").trim());

				db.insert(DBtables.Configuracion.TAG, null, cv);
				Log.i("CONFIGURACION", "i= " + i + " NOMBRE: "
						+ jsonData.getString("nombre").trim() + " VALOR: "
						+ jsonData.getString("valor").trim());
			}

			db.setTransactionSuccessful();

		} catch (JSONException e) {
			Log.e(DBclasses.TAG,
					"JSON Exception CONFIGURACION:" + e.getMessage());
			throw new JSONException(e.getMessage());
		} finally {
			db.endTransaction();
			db.close();
		}

	}

	public int syncZnfProgramacionClientes(JSONArray jArray, String fecha, int start) {

		JSONObject jsonData = null;
		ContentValues cv = new ContentValues();

		if (start==0 && fecha.equals("TODOS")){
			getReadableDatabase().delete(DBtables.ZnfProgramacionClientes.TAG,null, null);
		}

		SQLiteDatabase db = getWritableDatabase();
		db.beginTransaction();

		try {

			for (int i = 0; i < jArray.length(); i++) {

				jsonData = jArray.getJSONObject(i);
				cv.put(DBtables.ZnfProgramacionClientes.SECUENCIA, jsonData
						.getString("secuencia").trim());
				cv.put(DBtables.ZnfProgramacionClientes.CODVEN, jsonData
						.getString("codven").trim());
				cv.put(DBtables.ZnfProgramacionClientes.FECHA, jsonData
						.getString("fecha").trim());
				cv.put(DBtables.ZnfProgramacionClientes.TIPO, jsonData
						.getString("tipo").trim());
				cv.put(DBtables.ZnfProgramacionClientes.FRECUENCIA, jsonData
						.getString("frecuencia").trim());
				cv.put(DBtables.ZnfProgramacionClientes.N_DIA, jsonData
						.getString("n_dia").trim());
				cv.put(DBtables.ZnfProgramacionClientes.SEC_RUTA, jsonData
						.getString("sec_ruta").trim());
				cv.put(DBtables.ZnfProgramacionClientes.SEC_ZONA, jsonData
						.getString("sec_zona").trim());
				cv.put(DBtables.ZnfProgramacionClientes.CODCLI, jsonData
						.getString("codcli").trim());
				cv.put(DBtables.ZnfProgramacionClientes.ITEM_DIRCLI, jsonData
						.getString("item_dircli").trim());
				cv.put(DBtables.ZnfProgramacionClientes.ORDEN_R, jsonData
						.getString("orden_r").trim());
				cv.put(DBtables.ZnfProgramacionClientes.ORDEN_Z, jsonData
						.getString("orden_z").trim());
				cv.put(DBtables.ZnfProgramacionClientes.ORDEN_C, jsonData.getString("orden_c").trim());
				cv.put(DBtables.ZnfProgramacionClientes.cartera_sidige, jsonData.getString("cartera_sidige").trim());

				//db.insert(DBtables.ZnfProgramacionClientes.TAG, null, cv);
				db.insertWithOnConflict(DBtables.ZnfProgramacionClientes.TAG, null, cv,SQLiteDatabase.CONFLICT_IGNORE);
				Log.i("ZNF PROGRAMACION CLIENTES", "i= " + i + " secuencia: "
						+ jsonData.getString("secuencia").trim() + " CODCLI: "
						+ jsonData.getString("codcli").trim());
			}

			db.setTransactionSuccessful();

		} catch (JSONException e) {
			Log.e(DBclasses.TAG, "JSON Exception ZNF PROGRAMACION CLIENTES:"
					+ e.getMessage());
		} finally {
			db.endTransaction();
			db.close();
		}
	return  jArray.length();
	}

	public void syncZona(JSONArray jArray) {

		JSONObject jsonData = null;
		ContentValues cv = new ContentValues();

		getReadableDatabase().delete(DBtables.Zona.TAG, null, null);
		SQLiteDatabase db = getWritableDatabase();

		db.beginTransaction();

		try {

			for (int i = 0; i < jArray.length(); i++) {

				jsonData = jArray.getJSONObject(i);
				cv.put(DBtables.Zona.SEC_ZONA, jsonData.getString("sec_zona")
						.trim());
				cv.put(DBtables.Zona.DESCRIPCION,
						jsonData.getString("descripcion").trim());
				cv.put(DBtables.Zona.ESTADO, jsonData.getString("estado")
						.trim());
				cv.put(DBtables.Zona.COLOR, jsonData.getString("color").trim());

				db.insert(DBtables.Zona.TAG, null, cv);
				Log.i("ZONA",
						"i= " + i + " SEC_ZONA: "
								+ jsonData.getString("sec_zona").trim()
								+ " DESCRIPCION: "
								+ jsonData.getString("descripcion").trim());
			}

			db.setTransactionSuccessful();

		} catch (JSONException e) {
			Log.e(DBclasses.TAG, "JSON Exception ZONA:" + e.getMessage());
		} finally {
			db.endTransaction();
			db.close();
		}

	}

	public void syncZonaXY(JSONArray jArray) {

		JSONObject jsonData = null;
		ContentValues cv = new ContentValues();

		getReadableDatabase().delete(DBtables.ZonaXY.TAG, null, null);
		SQLiteDatabase db = getWritableDatabase();

		db.beginTransaction();

		try {

			for (int i = 0; i < jArray.length(); i++) {

				jsonData = jArray.getJSONObject(i);
				cv.put(DBtables.ZonaXY.CODIGO_ZONA,
						jsonData.getString("codigo_zona").trim());
				cv.put(DBtables.ZonaXY.ORDEN, jsonData.getString("orden")
						.trim());
				cv.put(DBtables.ZonaXY.LATITUD, jsonData.getString("latitud")
						.trim());
				cv.put(DBtables.ZonaXY.LONGITUD, jsonData.getString("longitud")
						.trim());

				db.insert(DBtables.ZonaXY.TAG, null, cv);
				Log.i("ZONA_XY", "i= " + i + " CODIGO_ZONA: "
						+ jsonData.getString("codigo_zona").trim());
			}

			db.setTransactionSuccessful();

		} catch (JSONException e) {
			Log.e(DBclasses.TAG, "JSON Exception ZONA_XY:" + e.getMessage());
		} finally {
			db.endTransaction();
			db.close();
		}

	}

	public void syncRuta(JSONArray jArray) {

		JSONObject jsonData = null;
		ContentValues cv = new ContentValues();

		getReadableDatabase().delete(DBtables.Ruta.TAG, null, null);
		SQLiteDatabase db = getWritableDatabase();

		db.beginTransaction();

		try {

			for (int i = 0; i < jArray.length(); i++) {

				jsonData = jArray.getJSONObject(i);
				cv.put(DBtables.Ruta.SEC_RUTA, jsonData.getString("sec_ruta")
						.trim());
				cv.put(DBtables.Ruta.CODIGO, jsonData.getString("codigo")
						.trim());
				cv.put(DBtables.Ruta.DESCRIPCION,
						jsonData.getString("descripcion").trim());
				cv.put(DBtables.Ruta.ESTADO, jsonData.getString("estado")
						.trim());

				db.insert(DBtables.Ruta.TAG, null, cv);
				Log.i("RUTA",
						"i= " + i + " SEC_RUTA: "
								+ jsonData.getString("sec_ruta").trim());
			}

			db.setTransactionSuccessful();

		} catch (JSONException e) {
			Log.e(DBclasses.TAG, "JSON Exception RUTA:" + e.getMessage());
		} finally {
			db.endTransaction();
			db.close();
		}

	}

	public void syncVendedores(JSONArray jArray) {

		JSONObject jsonData = null;
		ContentValues cv = new ContentValues();

		getReadableDatabase().delete(DBtables.Vendedor.TAG, null, null);
		SQLiteDatabase db = getWritableDatabase();

		db.beginTransaction();

		try {

			for (int i = 0; i < jArray.length(); i++) {

				jsonData = jArray.getJSONObject(i);
				cv.put(DBtables.Vendedor.CODVEN, jsonData.getString("codven").trim());
				cv.put(DBtables.Vendedor.NOMVEN, jsonData.getString("nomven").trim());
				cv.put(DBtables.Vendedor.FK_CODUSER,jsonData.getString("coduser").trim());
				cv.put(DBtables.Vendedor.FLG_MODIFICAPRECIO, jsonData.getString("flg_modificaPrecio").trim());
				cv.put(DBtables.Vendedor.EMAIL, jsonData.getString(DBtables.Vendedor.EMAIL).trim());
				cv.put(DBtables.Vendedor.telefono, jsonData.getString(DBtables.Vendedor.telefono).trim());
				cv.put(DBtables.Vendedor.text_area, jsonData.getString(DBtables.Vendedor.text_area).trim());

				db.insert(DBtables.Vendedor.TAG, null, cv);
				Log.i("VENDEDOR",
						"i= " + i + " CODVEN: "
								+ jsonData.getString("codven").trim());
			}

			db.setTransactionSuccessful();

		} catch (JSONException e) {
			Log.e(DBclasses.TAG, "JSON Exception VENDEDOR:" + e.getMessage());
		} finally {
			db.endTransaction();
			db.close();
		}

	}

	public void syncUsuarios(JSONArray jArray) {

		JSONObject jsonData = null;
		ContentValues cv = new ContentValues();

		getReadableDatabase().delete(DBtables.Usuarios.TAG, null, null);
		SQLiteDatabase db = getWritableDatabase();

		db.beginTransaction();

		try {

			for (int i = 0; i < jArray.length(); i++) {

				jsonData = jArray.getJSONObject(i);
				cv.put(DBtables.Usuarios.PK_USECOD, jsonData
						.getString("usecod").trim());
				cv.put(DBtables.Usuarios.USEPAS, jsonData.getString("usepas")
						.trim());
				cv.put(DBtables.Usuarios.USENAM, jsonData.getString("usenam")
						.trim());
				cv.put(DBtables.Usuarios.USEUSR, jsonData.getString("useusr")
						.trim());
				cv.put(DBtables.Usuarios.USESGL, jsonData.getString("usesgl").trim());
				cv.put(DBtables.Usuarios.codigoRol, jsonData.getString("codigoRol").trim());

				db.insert(DBtables.Usuarios.TAG, null, cv);
				Log.i("USUARIOS",
						"i= " + i + " USECOD: "
								+ jsonData.getString("usecod").trim());
			}

			db.setTransactionSuccessful();

		} catch (JSONException e) {
			Log.e(DBclasses.TAG, "JSON Exception USUARIOS:" + e.getMessage());
		} finally {
			db.endTransaction();
			db.close();
		}

	}

	public void syncDepositos(JSONArray jArray) {

		JSONObject jsonData = null;
		ContentValues cv = new ContentValues();

		// getReadableDatabase().delete(DBtables.Usuarios.TAG, null, null);

		EliminarDepositos_enviados();

		SQLiteDatabase db = getWritableDatabase();

		db.beginTransaction();

		try {

			for (int i = 0; i < jArray.length(); i++) {

				jsonData = jArray.getJSONObject(i);
				cv.put(DBtables.Depositos.SECUENCIA,
						jsonData.getString("secuencia").trim());
				cv.put(DBtables.Depositos.ID_BANCO,
						jsonData.getString("id_banco").trim());
				cv.put(DBtables.Depositos.ID_NUM_CTA,
						jsonData.getString("id_num_cta").trim());
				cv.put(DBtables.Depositos.FECHA, jsonData.getString("fecha")
						.trim());
				cv.put(DBtables.Depositos.NUM_OPE, jsonData
						.getString("num_ope").trim());
				cv.put(DBtables.Depositos.MONEDA, jsonData.getString("moneda")
						.trim());
				cv.put(DBtables.Depositos.MONTO, jsonData.getString("monto")
						.trim());
				cv.put(DBtables.Depositos.ESTADO, jsonData.getString("estado")
						.trim());
				cv.put(DBtables.Depositos.CODVEN, jsonData.getString("codven")
						.trim());
				cv.put(DBtables.Depositos.DT_DEPO_FECHASERVIDOR, jsonData
						.getString("DT_DEPO_FECHASERVIDOR").trim());
				// vs old cv.put(DBtables.Depositos.BI_DEPO_FLAG, "E");
				cv.put(DBtables.Depositos.BI_DEPO_FLAG,
						jsonData.getString("BI_DEPO_FLAG").trim());
				cv.put(DBtables.Depositos.TXT_DEPO_FECHA_REGISTRO, jsonData
						.getString("TXT_DEPO_FECHA_REGISTRO").trim());

				db.insert(DBtables.Depositos.TAG, null, cv);
				Log.i("DEPOSITOS", "i= " + i + " SECUENCIA: "
						+ jsonData.getString("secuencia").trim());
			}

			db.setTransactionSuccessful();

		} catch (JSONException e) {
			Log.e(DBclasses.TAG, "JSON Exception DEPOSITOS:" + e.getMessage());
		} finally {
			db.endTransaction();
			db.close();
		}

	}

	public void syncProducto(JSONArray jArray) {

		JSONObject jsonData = null;
		ContentValues cv = new ContentValues();

		getReadableDatabase().delete(DBtables.Producto.TAG, null, null);

		SQLiteDatabase db = getWritableDatabase();

		db.beginTransaction();

		try {

			for (int i = 0; i < jArray.length(); i++) {

				jsonData = jArray.getJSONObject(i);
				cv.put(DBtables.Producto.CODPRO, jsonData.getString("codpro").trim());
				cv.put(DBtables.Producto.DESPRO, jsonData.getString("despro").trim());
				cv.put(DBtables.Producto.ABREVPRO,jsonData.getString("abrevpro").trim());
				cv.put(DBtables.Producto.GRUPO, jsonData.getString("grupo").trim());
				cv.put(DBtables.Producto.FK_FAMILIA,jsonData.getString("familia").trim());
				cv.put(DBtables.Producto.FK_SUB_FAMILIA,jsonData.getString("sub_familia").trim());
				cv.put(DBtables.Producto.EAN13, jsonData.getString("ean13").trim());
				cv.put(DBtables.Producto.COD_RAPIDO,jsonData.getString("cod_rapido").trim());
				cv.put(DBtables.Producto.CODUNIMED,jsonData.getString("codunimed").trim());
				cv.put(DBtables.Producto.CODUNIMED_ALMACEN,jsonData.getString("codunimed_almacen").trim());
				cv.put(DBtables.Producto.FACTOR_CONVERSION,jsonData.getString("factor_conversion").trim());
				cv.put(DBtables.Producto.AFECTO, jsonData.getString("afecto").trim());
				cv.put(DBtables.Producto.ESTADO, jsonData.getString("estado").trim());
				cv.put(DBtables.Producto.PESO, jsonData.getString("peso").trim());
				// cv.put(DBtables.Producto.FOTO, jsonData.getString("foto"));
				cv.put(DBtables.Producto.FOTO, "producto_default.png");
				cv.put(DBtables.Producto.LINEA_NEGOCIO,jsonData.getString("linea_negocio").trim());

				if (jsonData.has(DBtables.Producto.PERCEPCION)) {
					cv.put(DBtables.Producto.PERCEPCION,
							jsonData.getString(DBtables.Producto.PERCEPCION)
									.trim());
				} else {
					cv.put(DBtables.Producto.PERCEPCION, "0");
				}

				cv.put(DBtables.Producto.TIPO_PRODUCTO,jsonData.getString(DBtables.Producto.TIPO_PRODUCTO).trim());
				cv.put(DBtables.Producto._PRECIO_BASE,jsonData.getDouble(DBtables.Producto._PRECIO_BASE));
				cv.put(DBtables.Producto.desc_comercial,jsonData.getString(Producto.desc_comercial).trim());


				db.insert(DBtables.Producto.TAG, null, cv);
				Log.i("PRODUCTO",
						"i= " + i + " CODPRO: "
								+ jsonData.getString("codpro").trim());
			}

			db.setTransactionSuccessful();

		} catch (JSONException e) {
			Log.e(DBclasses.TAG, "JSON Exception PRODUCTO:" + e.getMessage());
		} finally {
			db.endTransaction();
			db.close();
		}

	}

	public void syncMtaKardex(JSONArray jArray) {

		JSONObject jsonData = null;
		ContentValues cv = new ContentValues();

		getReadableDatabase().delete(DBtables.MTA_kardex.TAG, null, null);

		SQLiteDatabase db = getWritableDatabase();

		db.beginTransaction();

		try {

			for (int i = 0; i < jArray.length(); i++) {

				jsonData = jArray.getJSONObject(i);
				cv.put(DBtables.MTA_kardex.PK_KARDEX,
						jsonData.getString("kardex").trim());
				cv.put(DBtables.MTA_kardex.PK_CODALM,
						jsonData.getString("codalm").trim());
				cv.put(DBtables.MTA_kardex.PK_CODPRO,
						jsonData.getString("codpro").trim());
				cv.put(DBtables.MTA_kardex.STOCK, jsonData.getString("stock")
						.trim());
				cv.put(DBtables.MTA_kardex.XTEMP, jsonData.getString("xtemp")
						.trim());

				db.insert(DBtables.MTA_kardex.TAG, null, cv);
				// Log.i("MTA_KARDEX","i= "+i+" CODPRO: "+jsonData.getString("codpro").trim());
			}

			db.setTransactionSuccessful();

		} catch (JSONException e) {
			Log.e(DBclasses.TAG, "JSON Exception MTA_KARDEX:" + e.getMessage());
		} finally {
			db.endTransaction();
			db.close();
		}

	}

	public void syncPromocionClientes(JSONArray jArray) {

		JSONObject jsonData = null;
		ContentValues cv = new ContentValues();

		getReadableDatabase().delete(DBtables.Promocion_clientes.TAG, null,
				null);

		SQLiteDatabase db = getWritableDatabase();

		db.beginTransaction();

		try {

			for (int i = 0; i < jArray.length(); i++) {

				jsonData = jArray.getJSONObject(i);
				cv.put(DBtables.Promocion_clientes.SEC_PROMOCION, jsonData
						.getString("sec_promocion").trim());
				cv.put(DBtables.Promocion_clientes.CODCLI,
						jsonData.getString("codcli").trim());

				db.insert(DBtables.Promocion_clientes.TAG, null, cv);
				Log.i("PROMOCION CLIENTES", "i= " + i + " SEC_PROMOCION: "
						+ jsonData.getString("sec_promocion").trim()
						+ " CODCLI:" + jsonData.getString("codcli").trim());
			}

			db.setTransactionSuccessful();

		} catch (JSONException e) {
			Log.e(DBclasses.TAG,
					"JSON Exception PROMOCION CLIENTES:" + e.getMessage());
		} finally {
			db.endTransaction();
			db.close();
		}

	}

	public void syncPromocionVendedor(JSONArray jArray) {

		JSONObject jsonData = null;
		ContentValues cv = new ContentValues();

		getReadableDatabase().delete(DBtables.Promocion_Vendedor.TAG, null,
				null);

		SQLiteDatabase db = getWritableDatabase();

		db.beginTransaction();

		try {

			for (int i = 0; i < jArray.length(); i++) {

				jsonData = jArray.getJSONObject(i);
				cv.put(DBtables.Promocion_Vendedor.SEC_PROMOCION, jsonData
						.getString("sec_promocion").trim());
				cv.put(DBtables.Promocion_Vendedor.CODVEN,
						jsonData.getString("codven").trim());

				db.insert(DBtables.Promocion_Vendedor.TAG, null, cv);
				Log.i("PROMOCION VENDEDOR", "i= " + i + " SEC_PROMOCION: "
						+ jsonData.getString("sec_promocion") + " CODVEN:"
						+ jsonData.getString("codven").trim());
			}

			db.setTransactionSuccessful();

		} catch (JSONException e) {
			Log.e(DBclasses.TAG,
					"JSON Exception PROMOCION VENDEDOR:" + e.getMessage());
		} finally {
			db.endTransaction();
			db.close();
		}

	}

	public void syncPromocionPolitica(JSONArray jArray) {

		JSONObject jsonData = null;
		ContentValues cv = new ContentValues();

		getReadableDatabase().delete(DBtables.Promocion_Politica.TAG, null,
				null);

		SQLiteDatabase db = getWritableDatabase();

		db.beginTransaction();

		try {

			for (int i = 0; i < jArray.length(); i++) {

				jsonData = jArray.getJSONObject(i);
				cv.put(DBtables.Promocion_Politica.SEC_PROMOCION, jsonData
						.getString("sec_promocion").trim());
				cv.put(DBtables.Promocion_Politica.SEC_POLITICA, jsonData
						.getString("sec_politica").trim());

				db.insert(DBtables.Promocion_Politica.TAG, null, cv);
				Log.i("PROMOCION POLITICA", "i= " + i + " SEC_PROMOCION: "
						+ jsonData.getString("sec_promocion").trim()
						+ " SEC_POLITICA:"
						+ jsonData.getString("sec_politica").trim());
			}

			db.setTransactionSuccessful();

		} catch (JSONException e) {
			Log.e(DBclasses.TAG,
					"JSON Exception PROMOCION POLITICA:" + e.getMessage());
		} finally {
			db.endTransaction();
			db.close();
		}

	}

	public void syncPromocionDetalle(JSONArray jArray) {

		JSONObject jsonData = null;
		ContentValues cv = new ContentValues();

		getReadableDatabase()
				.delete(DBtables.Promocion_Detalle.TAG, null, null);

		SQLiteDatabase db = getWritableDatabase();

		db.beginTransaction();

		try {

			for (int i = 0; i < jArray.length(); i++) {

				jsonData = jArray.getJSONObject(i);
				cv.put(DBtables.Promocion_Detalle.SECUENCIA, jsonData.getString("secuencia").trim());
				cv.put(DBtables.Promocion_Detalle.GENERAL,jsonData.getString("general").trim());
				cv.put(DBtables.Promocion_Detalle.PROMOCION, jsonData.getString("promocion").trim());
				cv.put(DBtables.Promocion_Detalle.CODALM,jsonData.getString("codalm").trim());
				cv.put(DBtables.Promocion_Detalle.TIPO,jsonData.getString("tipo").trim());
				cv.put(DBtables.Promocion_Detalle.ITEM,jsonData.getString("item").trim());
				cv.put(DBtables.Promocion_Detalle.AGRUPADO,jsonData.getString("agrupado").trim());
				cv.put(DBtables.Promocion_Detalle.ENTRADA,jsonData.getString("entrada").trim());
				cv.put(DBtables.Promocion_Detalle.TIPO_UNIMED_ENTRADA, jsonData.getString("tipo_unimed_entrada").trim());
				cv.put(DBtables.Promocion_Detalle.MONTO_MINIMO, jsonData.getString("monto_minimo").trim());
				cv.put(DBtables.Promocion_Detalle.MONTO_MAXIMO, jsonData.getString("monto_maximo").trim());
				cv.put(DBtables.Promocion_Detalle.MONTO,jsonData.getString("monto").trim());
				cv.put(DBtables.Promocion_Detalle.CONDICION, jsonData.getString("condicion").trim());
				cv.put(DBtables.Promocion_Detalle.CANT_CONDICION, jsonData.getString("cant_condicion").trim());
				cv.put(DBtables.Promocion_Detalle.SALIDA,jsonData.getString("salida").trim());
				cv.put(DBtables.Promocion_Detalle.TIPO_UNIMED_SALIDA, jsonData.getString("tipo_unimed_salida").trim());
				cv.put(DBtables.Promocion_Detalle.CANT_PROMOCION, jsonData.getString("cant_promocion").trim());
				cv.put(DBtables.Promocion_Detalle.MAX_PEDIDO, jsonData.getString("max_pedido").trim());
				cv.put(DBtables.Promocion_Detalle.TOTAL_AGRUPADO, jsonData.getString("total_agrupado").trim());
				cv.put(DBtables.Promocion_Detalle.TIPO_PROMOCION, jsonData.getString("tipo_promocion").trim());
				cv.put(DBtables.Promocion_Detalle.VENDEDOR,jsonData.getString("vendedor").trim());
				cv.put(DBtables.Promocion_Detalle.POLITICA,jsonData.getString("politica").trim());
				cv.put(DBtables.Promocion_Detalle.ACUMULADO, jsonData.getString("acumulado").trim());
				cv.put(DBtables.Promocion_Detalle.GRUPO, jsonData.getString(DBtables.Promocion_Detalle.GRUPO).trim());
				cv.put(DBtables.Promocion_Detalle.FAMILIA, jsonData.getString(DBtables.Promocion_Detalle.FAMILIA).trim());
				cv.put(DBtables.Promocion_Detalle.SUBFAMILIA, jsonData.getString(DBtables.Promocion_Detalle.SUBFAMILIA).trim());
				cv.put(DBtables.Promocion_Detalle.DESCUENTO, jsonData.getString(DBtables.Promocion_Detalle.DESCUENTO).trim());
				cv.put(DBtables.Promocion_Detalle.UBIGEO, jsonData.getString(DBtables.Promocion_Detalle.UBIGEO).trim());

				if (jsonData.has(DBtables.Promocion_Detalle.ACUMULADO)) {
					cv.put(DBtables.Promocion_Detalle.ACUMULADO, jsonData.getString(DBtables.Promocion_Detalle.ACUMULADO)
							.trim());
				} else {
					cv.put(DBtables.Promocion_Detalle.ACUMULADO, "0");
				}
				if (jsonData.has(DBtables.Promocion_Detalle.prioridad)) {
					cv.put(DBtables.Promocion_Detalle.prioridad, jsonData.getInt(DBtables.Promocion_Detalle.prioridad));
				}else cv.put(DBtables.Promocion_Detalle.prioridad, 0);

				db.insert(DBtables.Promocion_Detalle.TAG, null, cv);
				Log.i("PROMOCION DETALLE", "i= " + i + " SECUENCIA: "
						+ jsonData.getString("secuencia").trim());
			}

			db.setTransactionSuccessful();

		} catch (JSONException e) {
			Log.e(DBclasses.TAG,
					"JSON Exception PROMOCION DETALLE:" + e.getMessage());
		} finally {
			db.endTransaction();
			db.close();
		}

	}

	public void syncFamilia(JSONArray jArray) {

		JSONObject jsonData = null;
		ContentValues cv = new ContentValues();

		getReadableDatabase().delete(DBtables.Familia.TAG, null, null);

		SQLiteDatabase db = getWritableDatabase();

		db.beginTransaction();

		try {

			for (int i = 0; i < jArray.length(); i++) {

				jsonData = jArray.getJSONObject(i);
				cv.put(DBtables.Familia.SECUENCIA,
						jsonData.getString("secuencia").trim());
				cv.put(DBtables.Familia.FAMILIA, jsonData.getString("familia")
						.trim());

				db.insert(DBtables.Familia.TAG, null, cv);
				Log.i("FAMILIA", "i= " + i + " SECUENCIA: "
						+ jsonData.getString("secuencia").trim() + " FAMILIA: "
						+ jsonData.getString("familia").trim());
			}

			db.setTransactionSuccessful();

		} catch (JSONException e) {
			Log.e(DBclasses.TAG, "JSON Exception FAMILIA:" + e.getMessage());
		} finally {
			db.endTransaction();
			db.close();
		}

	}

	public void syncSubFamilia(JSONArray jArray) {

		JSONObject jsonData = null;
		ContentValues cv = new ContentValues();

		getReadableDatabase().delete(DBtables.Sub_familia.TAG, null, null);

		SQLiteDatabase db = getWritableDatabase();

		db.beginTransaction();

		try {

			for (int i = 0; i < jArray.length(); i++) {

				jsonData = jArray.getJSONObject(i);
				cv.put(DBtables.Sub_familia.SECUENCIA,
						jsonData.getString("secuencia").trim());
				cv.put(DBtables.Sub_familia.SUB_FAMILIA,
						jsonData.getString("sub_familia").trim());
				cv.put(DBtables.Sub_familia.DES_SUBFAM,
						jsonData.getString("des_subfam").trim());

				db.insert(DBtables.Sub_familia.TAG, null, cv);
				Log.i("SUB_FAMILIA", "i= " + i + " SECUENCIA: "
						+ jsonData.getString("secuencia").trim()
						+ " SUB_FAMILIA: "
						+ jsonData.getString("sub_familia").trim());
			}

			db.setTransactionSuccessful();

		} catch (JSONException e) {
			Log.e(DBclasses.TAG, "JSON Exception SUB_FAMILIA:" + e.getMessage());
		} finally {
			db.endTransaction();
			db.close();
		}

	}

	public void syncUnidadMedida(JSONArray jArray) {

		JSONObject jsonData = null;
		ContentValues cv = new ContentValues();

		getReadableDatabase().delete(DBtables.Unidad_medida.TAG, null, null);

		SQLiteDatabase db = getWritableDatabase();

		db.beginTransaction();

		try {

			for (int i = 0; i < jArray.length(); i++) {

				jsonData = jArray.getJSONObject(i);
				cv.put(DBtables.Unidad_medida.CODUNIMED,
						jsonData.getString("codunimed").trim());
				cv.put(DBtables.Unidad_medida.DESUNIMED,
						jsonData.getString("desunimed").trim());

				db.insert(DBtables.Unidad_medida.TAG, null, cv);
				Log.i("UNIDAD_MEDIDA", "i= " + i + " CODUNIMED: "
						+ jsonData.getString("codunimed").trim()
						+ " DESUNIMED: "
						+ jsonData.getString("desunimed").trim());
			}

			db.setTransactionSuccessful();

		} catch (JSONException e) {
			Log.e(DBclasses.TAG,
					"JSON Exception UNIDAD_MEDIDA:" + e.getMessage());
		} finally {
			db.endTransaction();
			db.close();
		}

	}

	public int syncPoliticaCliente(JSONArray jArray, int start) {

		JSONObject jsonData = null;
		ContentValues cv = new ContentValues();

		if (start==0){
			getReadableDatabase().delete(DBtables.Politica_cliente.TAG, null, null);
		}

		SQLiteDatabase db = getWritableDatabase();

		db.beginTransaction();

		try {

			for (int i = 0; i < jArray.length(); i++) {

				jsonData = jArray.getJSONObject(i);
				cv.put(DBtables.Politica_cliente.SEC_POLITICA, jsonData
						.getString("sec_politica").trim());
				cv.put(DBtables.Politica_cliente.CODCLI,
						jsonData.getString("codcli").trim());

				db.insert(DBtables.Politica_cliente.TAG, null, cv);
				// Log.i("POLITICA_CLIENTE","i= "+i+" SEC_POLITICA: "+jsonData.getString("sec_politica").trim()+" CODCLI: "+jsonData.getString("codcli").trim());
			}

			db.setTransactionSuccessful();

		} catch (JSONException e) {
			Log.e(DBclasses.TAG,
					"JSON Exception POLITICA_CLIENTE:" + e.getMessage());
		} finally {
			db.endTransaction();
			db.close();
		}

		return jArray.length();
	}

	public void syncPoliticaPrecio2(JSONArray jArray) {

		JSONObject jsonData = null;
		ContentValues cv = new ContentValues();

		getReadableDatabase().delete(DBtables.Politica_precio2.TAG, null, null);

		SQLiteDatabase db = getWritableDatabase();

		db.beginTransaction();

		try {

			for (int i = 0; i < jArray.length(); i++) {

				jsonData = jArray.getJSONObject(i);
				cv.put(DBtables.Politica_precio2.PK_SECUENCIA, jsonData
						.getString("secuencia").trim());
				cv.put(DBtables.Politica_precio2.ITEM,
						jsonData.getString("item").trim());
				cv.put(DBtables.Politica_precio2.PK_CODPRO,
						jsonData.getString("codpro").trim());
				cv.put(DBtables.Politica_precio2.PREPRO,
						jsonData.getString("prepro").trim());
				cv.put(DBtables.Politica_precio2.PREPRO_UNIDAD, jsonData
						.getString("prepro_unidad").trim());

				db.insert(DBtables.Politica_precio2.TAG, null, cv);
				// Log.i("POLITICA_PRECIO2","i= "+i+" SECUENCIA: "+jsonData.getString("secuencia").trim()+" ITEM: "+jsonData.getString("item").trim());
			}

			db.setTransactionSuccessful();

		} catch (JSONException e) {
			Log.e(DBclasses.TAG,
					"JSON Exception POLITICA_PRECIO2:" + e.getMessage());
		} finally {
			db.endTransaction();
			db.close();
		}

	}

	public void syncPoliticaVendedor(JSONArray jArray) {

		JSONObject jsonData = null;
		ContentValues cv = new ContentValues();

		getReadableDatabase()
				.delete(DBtables.Politica_vendedor.TAG, null, null);

		SQLiteDatabase db = getWritableDatabase();

		db.beginTransaction();

		try {

			for (int i = 0; i < jArray.length(); i++) {

				jsonData = jArray.getJSONObject(i);
				cv.put(DBtables.Politica_vendedor.CODVEN,
						jsonData.getString("codven").trim());
				cv.put(DBtables.Politica_vendedor.SEC_POLITICA, jsonData
						.getString("sec_politica").trim());
				cv.put(DBtables.Politica_vendedor.FLG_PREDETERMINADO, jsonData
						.getString("flg_predeterminado").trim());

				db.insert(DBtables.Politica_vendedor.TAG, null, cv);
				Log.i("POLITICA_VENDEDOR", "i= " + i + " CODVEN: "
						+ jsonData.getString("codven").trim()
						+ " SEC_POLITICA: "
						+ jsonData.getString("sec_politica").trim());
			}

			db.setTransactionSuccessful();

		} catch (JSONException e) {
			Log.e(DBclasses.TAG,
					"JSON Exception POLITICA_VENDEDOR:" + e.getMessage());
		} finally {
			db.endTransaction();
			db.close();
		}

	}


	public void syncIngresos(JSONArray jArray, String codven) {

		JSONObject jsonData = null;
		ContentValues cv = new ContentValues();
		EliminarIngresos_enviados(codven);
		
		SQLiteDatabase db = getWritableDatabase();
		db.beginTransaction();

		try {

			for (int i = 0; i < jArray.length(); i++) {

				jsonData = jArray.getJSONObject(i);
				cv.put(DBtables.Ingresos.PK_SECUENCIA,jsonData.getString("secuencia").trim());
				cv.put(DBtables.Ingresos.PK_SECITM, jsonData.getString("secitm").trim());
				cv.put(DBtables.Ingresos.FECPAG, jsonData.getString("fecpag").trim());
				cv.put(DBtables.Ingresos.TOTAL, jsonData.getString("total").trim());
				cv.put(DBtables.Ingresos.ACUENTA, jsonData.getString("acuenta").trim());
				cv.put(DBtables.Ingresos.SALDO, jsonData.getString("saldo").trim());
				cv.put(DBtables.Ingresos.CODCUE, jsonData.getString("codcue").trim());
				cv.put(DBtables.Ingresos.NUMOPE, jsonData.getString("numope").trim());
				cv.put(DBtables.Ingresos.CODFORPAG,	jsonData.getString("codforpag").trim());
				cv.put(DBtables.Ingresos.TIPO_CAMBIO,jsonData.getString("tipo_cambio").trim());
				cv.put(DBtables.Ingresos.CODMON, jsonData.getString("codmon").trim());
				cv.put(DBtables.Ingresos.MONTO_AFECTO,jsonData.getString("monto_afecto").trim());
				cv.put(DBtables.Ingresos.USERNAME,jsonData.getString("username").trim());
				cv.put(DBtables.Ingresos.FECOPERACION,jsonData.getString("fecoperacion").trim());
				cv.put(DBtables.Ingresos.FLAG, "E");
				cv.put(DBtables.Ingresos.LATITUD, jsonData.getString("latitud")	.trim());
				cv.put(DBtables.Ingresos.LONGITUD,	jsonData.getString("longitud").trim());
				cv.put(DBtables.Ingresos.DT_INGR_FECHASERVIDOR, jsonData.getString("DT_INGR_FECHASERVIDOR").trim());
				cv.put(DBtables.Ingresos.ESTADO, jsonData.getString("estado").trim());
				
				cv.put(DBtables.Ingresos.OBSERVACION, jsonData.getString("observacion") .trim());
				cv.put(DBtables.Ingresos.CODCLI, jsonData.getString("codcli") .trim());
				 
				cv.put(DBtables.Ingresos.TIPO_DOCUMENTO, jsonData.getString("tipoDoc") .trim());
				cv.put(DBtables.Ingresos.SERIE, jsonData.getString("serie") .trim());
				cv.put(DBtables.Ingresos.NUMERO, jsonData.getString("numero") .trim());
				cv.put(DBtables.Ingresos.CODIGO_BANCO, jsonData.getString("codigoBanco") .trim());
				
				db.insert(DBtables.Ingresos.TAG, null, cv);
				Log.i("INGRESOS", "i= " + i + " SECUENCIA: "+ jsonData.getString("secuencia").trim() + " SECITM: "	+ jsonData.getString("secitm").trim());
			
			}

			db.setTransactionSuccessful();

		} catch (JSONException e) {
			Log.e(DBclasses.TAG, "JSON Exception INGRESOS:" + e.getMessage());
		} finally {
			db.endTransaction();
			db.close();
		}

	}
 
	public void syncCtaIngresos(JSONArray jArray,String codven) {

		JSONObject jsonData = null;
		ContentValues cv = new ContentValues();

		getReadableDatabase().delete(DBtables.Cta_ingresos.TAG, null, null);//eliminar solo del cliente

		Log.d("DOCUMENTOS", "Se limpio la BD insertar N�"+jArray.length());
		SQLiteDatabase db = getWritableDatabase();

		db.beginTransaction();

		try {

			String [] documento;
			String tipoDocumento ;
			
			Log.d("CTA_INGRESOS",""+jArray);
			
			for (int i = 0; i < jArray.length(); i++) {

				jsonData = jArray.getJSONObject(i);
				 
				tipoDocumento = jsonData.getString("tipoDocumento").trim();
				
				cv.put(DBtables.Cta_ingresos.PK_SECUENCIA, jsonData.getString("secuencia").trim());
				cv.put(DBtables.Cta_ingresos.CODMON, jsonData.getString("moneda").trim());
				cv.put(DBtables.Cta_ingresos.CODDOC, tipoDocumento );
				cv.put(DBtables.Cta_ingresos.FECCOM, jsonData.getString("fechaDocumento").trim());
				cv.put(DBtables.Cta_ingresos.TOTAL, jsonData.getString("total") .trim());
				cv.put(DBtables.Cta_ingresos.CODCLI, jsonData.getString("codigoCliente").trim());
				cv.put(DBtables.Cta_ingresos.USERNAME, jsonData.getString("userName").trim());
				cv.put(DBtables.Cta_ingresos.FECHA_DESPACHO, jsonData.getString("fechaDespacho") .trim());
				cv.put(DBtables.Cta_ingresos.FECHA_VENCIMIENTO, jsonData.getString("fechaVencimiento") .trim());
				cv.put(DBtables.Cta_ingresos.PLAZO, jsonData.getString("plazo") .trim());
				cv.put(DBtables.Cta_ingresos.OBSERVACIONES, jsonData.getString("observacion") .trim()); ///Almacenar la variable forma de pago 
				
				cv.put(DBtables.Cta_ingresos.SERIE_DOC, jsonData.getString("serie").trim() ); 
				cv.put(DBtables.Cta_ingresos.NUMERO_FACTURA, jsonData.getString("numeroDocumento").trim() ); 
				 
				cv.put(DBtables.Cta_ingresos.FECOPERACION, "");
				cv.put(DBtables.Cta_ingresos.CODVEN, codven );
				cv.put(DBtables.Cta_ingresos.ACUENTA, "0" );
				cv.put(DBtables.Cta_ingresos.SALDO, jsonData.getString("saldo") .trim());
				cv.put(DBtables.Cta_ingresos.SALDO_VIRTUAL, jsonData.getString("saldo") .trim());
				
				cv.put(DBtables.Cta_ingresos.FORMA, jsonData.getString("forma") .trim());
				cv.put(DBtables.Cta_ingresos.CC_FLAG, jsonData.getString("cc_flag") .trim());
				cv.put(DBtables.Cta_ingresos.ESTADO_COBRANZA, jsonData.getString("Estado_Cobranza").trim());
				cv.put(DBtables.Cta_ingresos.NRO_UNICO_BANCO, jsonData.getString("NroUnicoBanco").trim());
				 
				db.insert(DBtables.Cta_ingresos.TAG, null, cv);
				Log.i("CTA_INGRESOS", "i= " + i + " SECUENCIA: "+ jsonData.getString("secuencia").trim()+ " SERIE_DOC: "+jsonData.getString("numeroDocumento")+" CODVEN: "+ codven+ " NRO BANCO:"+jsonData.getString("NroUnicoBanco"));
			}

			db.setTransactionSuccessful();

		} catch (JSONException e) {
			Log.e(DBclasses.TAG,
					"JSON Exception CTA_INGRESOS:" + e.getMessage());
		} finally {
			db.endTransaction();
			db.close();
		}

	}

	public void syncBanco(JSONArray jArray) {

		JSONObject jsonData = null;
		ContentValues cv = new ContentValues();

		getReadableDatabase().delete(DBtables.Banco.TAG, null, null);

		SQLiteDatabase db = getWritableDatabase();

		db.beginTransaction();

		try {

			for (int i = 0; i < jArray.length(); i++) {

				jsonData = jArray.getJSONObject(i);
				cv.put(DBtables.Banco.CODBAN, jsonData.getString("codban")
						.trim());
				cv.put(DBtables.Banco.NOMBAN, jsonData.getString("nomban")
						.trim());

				db.insert(DBtables.Banco.TAG, null, cv);
				Log.i("BANCO",
						"i= " + i + " CODBAN: "
								+ jsonData.getString("codban").trim()
								+ " NOMBAN: "
								+ jsonData.getString("nomban").trim());
			}

			db.setTransactionSuccessful();

		} catch (JSONException e) {
			Log.e(DBclasses.TAG, "JSON Exception BANCO:" + e.getMessage());
		} finally {
			db.endTransaction();
			db.close();
		}

	}
	
	

	public void syncCtasxBanco(JSONArray jArray) {

		JSONObject jsonData = null;
		ContentValues cv = new ContentValues();

		getReadableDatabase().delete(DBtables.Ctas_xbanco.TAG, null, null);

		SQLiteDatabase db = getWritableDatabase();

		db.beginTransaction();

		try {

			for (int i = 0; i < jArray.length(); i++) {

				jsonData = jArray.getJSONObject(i);
				cv.put(DBtables.Ctas_xbanco.SECUENCIA,
						jsonData.getString("secuencia").trim());
				cv.put(DBtables.Ctas_xbanco.CODBAN, jsonData
						.getString("codban").trim());
				cv.put(DBtables.Ctas_xbanco.ITEM, jsonData.getString("item")
						.trim());
				cv.put(DBtables.Ctas_xbanco.CODMON, jsonData .getString("codmon").trim());
				cv.put(DBtables.Ctas_xbanco.CTA_CTE,jsonData.getString("cta_cte").trim());
				cv.put(DBtables.Ctas_xbanco.ESTADO, jsonData .getString("estado").trim());
				cv.put(DBtables.Ctas_xbanco.CUENTA, jsonData .getString("cuenta").trim());

				db.insert(DBtables.Ctas_xbanco.TAG, null, cv);
				Log.i("CTAS_X_BANCO", "i= " + i + " CODBAN: "
						+ jsonData.getString("codban").trim() + " CTA_CTE: "
						+ jsonData.getString("cta_cte").trim());
			}

			db.setTransactionSuccessful();

		} catch (JSONException e) {
			Log.e(DBclasses.TAG,
					"JSON Exception CTAS_X_BANCO:" + e.getMessage());
		} finally {
			db.endTransaction();
			db.close();
		}

	}

	// Devuelve el flag enviado, para poder mostrar un mensaje mas detallado
	// sobre la insercion del pedido en el servidor
	// Lo devuelto solo se toma en cuenta en los envios unitarios
	// Lo devuelto no se toma en cuenta en los envios masivos (Envio de
	// pendientes en reportes)
	public String guardar_respuesta_objpedido_flag(JSONArray jArray)
			throws JSONException {

		JSONObject jsonData = null;
		String flag = "";

		ContentValues cv = new ContentValues();

		SQLiteDatabase db = getWritableDatabase();

		String where = "oc_numero=?";

		db.beginTransaction();

		try {

			for (int i = 0; i < jArray.length(); i++) {

				jsonData = jArray.getJSONObject(i);

				//
				flag = jsonData.getString("flag").trim();
				if (flag.length() > 3) {
					flag = "P";	
					//Toast.makeText(_context, "Error al enviar pendientes", Toast.LENGTH_SHORT).show();
				}
				//VERIFICAR EL FLAG PARA MOSTRAR MENSAJE EN CASO DE ERROR
				//

				cv.put(DBtables.Pedido_cabecera.FLAG, flag);
				cv.put(DBtables.Pedido_cabecera.DT_PEDI_FECHASERVIDOR, jsonData.getString("DT_PEDI_FECHASERVIDOR").trim());

				String[] args = { jsonData.getString("oc_numero").trim() };

				db.update(DBtables.Pedido_cabecera.TAG, cv, where, args);
				Log.i("PEDIDO_CABECERA",
						"i= "
								+ i
								+ " OC_NUMERO: "+ jsonData.getString("oc_numero").trim()
								+ " FLAG CAMBIADO A: ("+ jsonData.getString("flag").trim()
								+ ") FECHA SERVIDOR: "+ jsonData.getString("DT_PEDI_FECHASERVIDOR").trim());

			}

			db.setTransactionSuccessful();

		} catch (JSONException e) {
			Log.e(DBclasses.TAG,
					"JSON Exception RESPUESTA DE PEDIDOS:" + e.getMessage());
			throw new JSONException(e.getMessage());
		} finally {
			db.endTransaction();
			db.close();
		}

		return flag;
	}

	// Devuelve el flag enviado, para poder mostrar un mensaje mas detallado
	// sobre la insercion del pedido en el servidor
	// Lo devuelto solo se toma en cuenta en los envios unitarios
	// Lo devuelto no se toma en cuenta en los envios masivos (Envio de
	// pendientes en reportes)
	public String guardar_respuesta_depositos_flag(JSONArray jArray)
			throws JSONException {

		JSONObject jsonData = null;
		String flag = "";

		ContentValues cv = new ContentValues();

		SQLiteDatabase db = getWritableDatabase();

		String where = "secuencia=?";

		db.beginTransaction();

		try {

			for (int i = 0; i < jArray.length(); i++) {

				jsonData = jArray.getJSONObject(i);

				//
				flag = jsonData.getString("BI_DEPO_FLAG").trim();
				//

				cv.put(DBtables.Depositos.BI_DEPO_FLAG,
						jsonData.getString("BI_DEPO_FLAG").trim());
				cv.put(DBtables.Depositos.DT_DEPO_FECHASERVIDOR, jsonData
						.getString("DT_DEPO_FECHASERVIDOR").trim());

				String[] args = { jsonData.getString("secuencia").trim() };

				db.update(DBtables.Depositos.TAG, cv, where, args);
				Log.i("DEPOSITOS",
						"i= "
								+ i
								+ " SECUENCIA: "
								+ jsonData.getString("secuencia").trim()
								+ " FLAG CAMBIADO A: ("
								+ jsonData.getString("BI_DEPO_FLAG").trim()
								+ ") FECHA SERVIDOR: "
								+ jsonData.getString("DT_DEPO_FECHASERVIDOR")
										.trim());

			}

			db.setTransactionSuccessful();

		} catch (JSONException e) {
			Log.e(DBclasses.TAG,
					"JSON Exception RESPUESTA DE DEPOSITOS:" + e.getMessage());
			throw new JSONException(e.getMessage());
		} finally {
			db.endTransaction();
			db.close();
		}

		return flag;
	}

	// Devuelve el flag enviado, para poder mostrar un mensaje mas detallado
	// sobre la insercion del pedido en el servidor
	// Lo devuelto solo se toma en cuenta en los envios unitarios
	// Lo devuelto no se toma en cuenta en los envios masivos (Envio de
	// pendientes en reportes)
	public String guardar_respuesta_ingresos_flag(JSONArray jArray)
			throws JSONException {

		JSONObject jsonData = null;
		String flag = "";

		ContentValues cv = new ContentValues();

		SQLiteDatabase db = getWritableDatabase();

		String where = "secuencia=? and secitm=?";

		db.beginTransaction();

		try {

			for (int i = 0; i < jArray.length(); i++) {

				jsonData = jArray.getJSONObject(i);

				//
				flag = jsonData.getString("flag").trim();
				//

				cv.put(DBtables.Ingresos.FLAG, jsonData.getString("flag")
						.trim());
				cv.put(DBtables.Ingresos.DT_INGR_FECHASERVIDOR, jsonData
						.getString("DT_INGR_FECHASERVIDOR").trim());

				String[] args = { jsonData.getString("secuencia").trim(),
						jsonData.getString("secitm").trim() };

				db.update(DBtables.Ingresos.TAG, cv, where, args);
				Log.i("INGRESOS",
						"i= "
								+ i
								+ " SECUENCIA: "
								+ jsonData.getString("secuencia").trim()
								+ " ITM: "
								+ jsonData.getString("secitm")
								+ " FLAG CAMBIADO A: ("
								+ jsonData.getString("flag").trim()
								+ ") FECHA SERVIDOR: "
								+ jsonData.getString("DT_INGR_FECHASERVIDOR")
										.trim());

			}

			db.setTransactionSuccessful();

		} catch (JSONException e) {
			Log.e(DBclasses.TAG,
					"JSON Exception RESPUESTA DE INGRESOS:" + e.getMessage());
			throw new JSONException(e.getMessage());
		} finally {
			db.endTransaction();
			db.close();
		}

		return flag;
	}

	public void LlenarTablaSanOpciones(ArrayList<San_Opciones> lista){
		final String STAG = "LlenarTablaSanOpciones:: ";

		ContentValues cv = new ContentValues();
		getReadableDatabase().delete(DBtables.San_Opciones.TAG, null, null);
		SQLiteDatabase db = getWritableDatabase();
		db.beginTransaction();
		try {
			for (San_Opciones opciones:lista) {
				cv.put(DBtables.San_Opciones.id_opcion, opciones.getId_opcion());
				cv.put(DBtables.San_Opciones.codigo_crm, opciones.getCodigo_crm());
				cv.put(DBtables.San_Opciones.instancia, opciones.getInstancia());
				cv.put(DBtables.San_Opciones.opciones, opciones.getOpciones());

				long a=db.insert(DBtables.San_Opciones.TAG,null, cv);
			}
			db.setTransactionSuccessful();

		} catch (Exception e) {

			Log.i(TAG,STAG+"Se encontraron errores: "+e.getMessage());
		} finally {
			db.endTransaction();
			db.close();
		}
	}


	public int syncObjPedido(JSONArray jArray, String codven, int start)	throws JSONException, SQLiteException {
		JSONObject jsonData = null;
		ContentValues cv = new ContentValues();

		if (start==0){
			EliminarRegistro_bonificaciones_enviados(codven);
			EliminarPedido_detalle_enviados(codven);
			EliminarSanVisitas(codven);
			EliminarPedido_cabecera_enviados(codven);
		}

		SQLiteDatabase db = getWritableDatabase();

		db.beginTransaction();

		try {
			for (int i = 0; i < jArray.length(); i++) {
				jsonData = jArray.getJSONObject(i);

				cv = new ContentValues();
				if(!verificarPedidoTieneCabecera(db, jsonData.getString("oc_numero").trim()) || jsonData.getString("oc_numero").contains("TPLAST") ){
					Log.d("DBclasses ::syncObjPedido::","**************************************************************");
					cv.put(DBtables.Pedido_cabecera.PK_OC_NUMERO, jsonData.getString("oc_numero").trim());
					cv.put(DBtables.Pedido_cabecera.SITIO_ENFA,	jsonData.getString("sitio_enfa").trim());
					cv.put(DBtables.Pedido_cabecera.MONTO_TOTAL, jsonData.getString("monto_total").trim());
					cv.put(DBtables.Pedido_cabecera.PERCEPCION_TOTAL, jsonData.getString("percepcion_total").trim());
					cv.put(DBtables.Pedido_cabecera.VALOR_IGV, jsonData.getString("valor_igv").trim());
					cv.put(DBtables.Pedido_cabecera.MONEDA, jsonData.getString("moneda").trim());
					cv.put(DBtables.Pedido_cabecera.FECHA_OC, jsonData.getString("fecha_oc").trim());
					cv.put(DBtables.Pedido_cabecera.FECHA_MXE, jsonData.getString("fecha_mxe").trim());
					cv.put(DBtables.Pedido_cabecera.COND_PAGO, jsonData.getString("cond_pago").trim());
					cv.put(DBtables.Pedido_cabecera.COD_CLI, jsonData.getString("cod_cli").trim());
					cv.put(DBtables.Pedido_cabecera.COD_EMP, jsonData.getString("cod_emp").trim());
					cv.put(DBtables.Pedido_cabecera.ESTADO,	jsonData.getString("estado").trim());
					cv.put(DBtables.Pedido_cabecera.USERNAME, jsonData.getString("username").trim());
					cv.put(DBtables.Pedido_cabecera.RUTA, jsonData.getString("ruta").trim());
					cv.put(DBtables.Pedido_cabecera.OBSERV,	jsonData.getString("observ").trim());
					cv.put(DBtables.Pedido_cabecera.COD_NOVENTA, jsonData.getString("cod_noventa").trim());
					cv.put(DBtables.Pedido_cabecera.PESO_TOTAL, jsonData.getString("peso_total").trim());
					cv.put(DBtables.Pedido_cabecera.FLAG, "E");
					cv.put(DBtables.Pedido_cabecera.LATITUD,jsonData.getString("latitud").trim());
					cv.put(DBtables.Pedido_cabecera.LONGITUD,jsonData.getString("longitud").trim());
					cv.put(DBtables.Pedido_cabecera.CODIGO_FAMILIAR, jsonData.getString("codigo_familiar").trim());
					cv.put(DBtables.Pedido_cabecera.DT_PEDI_FECHASERVIDOR, jsonData.getString("DT_PEDI_FECHASERVIDOR").trim());
					cv.put(DBtables.Pedido_cabecera.TOTAL_SUJETO_PERCEPCION, jsonData.getString("totalSujetoPercepcion").trim());
					cv.put(DBtables.Pedido_cabecera.NRO_LETRA, jsonData.getString("nroletra").trim());
					cv.put(DBtables.Pedido_cabecera.OBSERVACION4, jsonData.getString("observacion4").trim());

					cv.put(DBtables.Pedido_cabecera.TIPO_VISTA, "Vista 2");

					cv.put(DBtables.Pedido_cabecera.NRO_ORDEN_COMPRA, 		jsonData.getString(DBtables.Pedido_cabecera.NRO_ORDEN_COMPRA).trim());
					cv.put(DBtables.Pedido_cabecera.CODIGO_PRIORIDAD, 		jsonData.getString(DBtables.Pedido_cabecera.CODIGO_PRIORIDAD).trim());
					cv.put(DBtables.Pedido_cabecera.CODIGO_SUCURSAL, 		jsonData.getString(DBtables.Pedido_cabecera.CODIGO_SUCURSAL).trim());
					cv.put(DBtables.Pedido_cabecera.CODIGO_PUNTO_ENTREGA, 	jsonData.getString(DBtables.Pedido_cabecera.CODIGO_PUNTO_ENTREGA).trim());
					cv.put(DBtables.Pedido_cabecera.CODIGO_TIPO_DESPACHO, 	jsonData.getString(DBtables.Pedido_cabecera.CODIGO_TIPO_DESPACHO).trim());
					cv.put(DBtables.Pedido_cabecera.FLAG_EMBALAJE, 			jsonData.getString(DBtables.Pedido_cabecera.FLAG_EMBALAJE).trim());
					cv.put(DBtables.Pedido_cabecera.FLAG_PEDIDOANTICIPO, 	jsonData.getString(DBtables.Pedido_cabecera.FLAG_PEDIDOANTICIPO).trim());
					cv.put(DBtables.Pedido_cabecera.CODIGO_TRANSPORTISTA, 	jsonData.getString(DBtables.Pedido_cabecera.CODIGO_TRANSPORTISTA).trim());
					cv.put(DBtables.Pedido_cabecera.CODIGO_ALMACEN, 		jsonData.getString(DBtables.Pedido_cabecera.CODIGO_ALMACEN).trim());
					cv.put(DBtables.Pedido_cabecera.OBSERVACION2, 			jsonData.getString(DBtables.Pedido_cabecera.OBSERVACION2).trim());
					cv.put(DBtables.Pedido_cabecera.OBSERVACION3, 			jsonData.getString(DBtables.Pedido_cabecera.OBSERVACION3).trim());
					cv.put(DBtables.Pedido_cabecera.OBSERVACION_DESCUENTO, 	jsonData.getString(DBtables.Pedido_cabecera.OBSERVACION_DESCUENTO).trim());
					cv.put(DBtables.Pedido_cabecera.OBSERVACION_TIPO_PROD, 	jsonData.getString(DBtables.Pedido_cabecera.OBSERVACION_TIPO_PROD).trim());
					cv.put(DBtables.Pedido_cabecera.FLAG_DESCUENTO, 		jsonData.getString(DBtables.Pedido_cabecera.FLAG_DESCUENTO).trim());
					cv.put(DBtables.Pedido_cabecera.CODIGO_OBRA, 			jsonData.getString(DBtables.Pedido_cabecera.CODIGO_OBRA).trim());
					cv.put(DBtables.Pedido_cabecera.FLAG_DESPACHO, 			jsonData.getString(DBtables.Pedido_cabecera.FLAG_DESPACHO).trim());
					cv.put(DBtables.Pedido_cabecera.DOC_ADICIONAL, 			jsonData.getString(DBtables.Pedido_cabecera.DOC_ADICIONAL).trim());
					cv.put(DBtables.Pedido_cabecera.SUBTOTAL, 				jsonData.getString(DBtables.Pedido_cabecera.SUBTOTAL).trim());
					cv.put(DBtables.Pedido_cabecera.TIPODOCUMENTO, 			jsonData.getString(DBtables.Pedido_cabecera.TIPODOCUMENTO).trim());

					cv.put(DBtables.Pedido_cabecera.TIPO_REGISTRO, 			jsonData.getString(DBtables.Pedido_cabecera.TIPO_REGISTRO).trim());
					cv.put(DBtables.Pedido_cabecera.DIAS_VIGENCIA, 			jsonData.getString(DBtables.Pedido_cabecera.DIAS_VIGENCIA).trim());
					cv.put(DBtables.Pedido_cabecera.PEDIDO_ANTERIOR, 		jsonData.getString(DBtables.Pedido_cabecera.PEDIDO_ANTERIOR).trim());
					cv.put(DBtables.Pedido_cabecera.CODIGO_TURNO, 			jsonData.getString(DBtables.Pedido_cabecera.CODIGO_TURNO).trim());

					//cv.put(DBtables.Pedido_cabecera.NRO_LETRA, 				jsonData.getString(DBtables.Pedido_cabecera.NRO_LETRA).trim());
					//cv.put(DBtables.Pedido_cabecera.OBSERVACION4, 			jsonData.getString(DBtables.Pedido_cabecera.OBSERVACION4).trim());

					long insert=db.insert(DBtables.Pedido_cabecera.TAG, null, cv);
					Log.d("DBclasses ::syncObjPedido::","**************************************************************");
					Log.i("syncObjPedido Cab",	"OCNUMERO: " + jsonData.getString("oc_numero").trim());

					if(insert>0){
						/* Insertando detalle del pedido */
						JSONArray json_detalles = new JSONArray(jsonData.getString("detalles"));

						for (int j = 0; j < json_detalles.length(); j++) {

							JSONObject jsonData_det = json_detalles.getJSONObject(j);
							ContentValues cv2 = new ContentValues();

							cv2.put(DBtables.Pedido_detalle.PK_OC_NUMERO, jsonData_det.getString("oc_numero").trim());
							cv2.put(DBtables.Pedido_detalle.PK_EAN_ITEM, jsonData_det.getString("ean_item").trim());
							cv2.put(DBtables.Pedido_detalle.CIP, jsonData_det.getString("cip").trim());
							cv2.put(DBtables.Pedido_detalle.PRECIO_BRUTO, jsonData_det	.getString("precio_bruto").trim());
							cv2.put(DBtables.Pedido_detalle.PRECIO_NETO, jsonData_det.getString("precio_neto").trim());
							cv2.put(DBtables.Pedido_detalle.PERCEPCION, jsonData_det.getString("percepcion").trim());
							cv2.put(DBtables.Pedido_detalle.CANTIDAD, jsonData_det.getString("cantidad").trim());
							cv2.put(DBtables.Pedido_detalle.TIPO_PRODUCTO, jsonData_det.getString("tipo_producto").trim());
							cv2.put(DBtables.Pedido_detalle.UNIDAD_MEDIDA, jsonData_det.getString("unidad_medida").trim());
							cv2.put(DBtables.Pedido_detalle.PESO_BRUTO, jsonData_det	.getString("peso_bruto").trim());
							cv2.put(DBtables.Pedido_detalle.FLAG, jsonData_det.getString("flag").trim());
							cv2.put(DBtables.Pedido_detalle.COD_POLITICA, jsonData_det.getString("cod_politica").trim());
							cv2.put(DBtables.Pedido_detalle.SEC_PROMO, jsonData_det.getString("sec_promo").trim());
							cv2.put(DBtables.Pedido_detalle.ITEM_PROMO, jsonData_det.getString("item_promo").trim());
							cv2.put(DBtables.Pedido_detalle.AGRUP_PROMO, jsonData_det.getString("agrup_promo").trim());
							//cv2.put(DBtables.Pedido_detalle.ITEM, jsonData_det.getString("item").trim());
							//item no existe en la tabla detalle pedido, se setea un correlativo
							cv2.put(DBtables.Pedido_detalle.ITEM, jsonData_det.getInt("item"));
							cv2.put(DBtables.Pedido_detalle.PRECIO_LISTA, jsonData_det.getString("precioLista").trim());
							cv2.put(DBtables.Pedido_detalle.DESCUENTO, jsonData_det.getString("descuento").trim());
							cv2.put(DBtables.Pedido_detalle.PORCENTAJE_DESC, jsonData_det.getDouble("porcentaje_desc"));
							cv2.put(DBtables.Pedido_detalle.porcentaje_desc_extra, jsonData_det.getDouble("porcentaje_desc_extra"));
							cv2.put(DBtables.Pedido_detalle.LOTE, jsonData_det.getString("lote").trim());

							cv2.put(DBtables.Pedido_detalle.MOTIVO_DEVOLUCION, jsonData_det.getString(DBtables.Pedido_detalle.MOTIVO_DEVOLUCION).trim());
							cv2.put(DBtables.Pedido_detalle.EXPECTATIVA, jsonData_det.getString(DBtables.Pedido_detalle.EXPECTATIVA).trim());
							cv2.put(DBtables.Pedido_detalle.ENVASE, jsonData_det.getString(DBtables.Pedido_detalle.ENVASE).trim());
							cv2.put(DBtables.Pedido_detalle.CONTENIDO, jsonData_det.getString(DBtables.Pedido_detalle.CONTENIDO).trim());
							cv2.put(DBtables.Pedido_detalle.PROCESO, jsonData_det.getString(DBtables.Pedido_detalle.PROCESO).trim());
							cv2.put(DBtables.Pedido_detalle.OBSERVACION_DEVOL, jsonData_det.getString(DBtables.Pedido_detalle.OBSERVACION_DEVOL).trim());
							cv2.put(DBtables.Pedido_detalle.TIPO_DOCUMENTO, jsonData_det.getString(DBtables.Pedido_detalle.TIPO_DOCUMENTO).trim());
							cv2.put(DBtables.Pedido_detalle.SERIE_DEVOLUCION, jsonData_det.getString(DBtables.Pedido_detalle.SERIE_DEVOLUCION).trim());
							cv2.put(DBtables.Pedido_detalle.NUMERO_DEVOLUCION, jsonData_det.getString(DBtables.Pedido_detalle.NUMERO_DEVOLUCION).trim());
							cv2.put(DBtables.Pedido_detalle.sec_promo_prioridad, jsonData_det.getInt(DBtables.Pedido_detalle.sec_promo_prioridad));
							cv2.put(DBtables.Pedido_detalle.item_promo_prioridad, jsonData_det.getInt(DBtables.Pedido_detalle.sec_promo_prioridad));

							db.insert(DBtables.Pedido_detalle.TAG, null, cv2);

							Log.i("syncObjPedido det", "OCNUMERO: "	+ jsonData_det.getString("oc_numero").trim()	+ " CIP: " + jsonData_det.getString("cip").trim());
						}

						/*Insertando Registro bonificaciones del pedido*/

						JSONArray json_registroBonificaciones = new JSONArray(jsonData.getString("bonificaciones"));
						for (int j = 0; j < json_registroBonificaciones.length(); j++) {

							JSONObject jsonData_reg = json_registroBonificaciones.getJSONObject(j);
							ContentValues cv2 = new ContentValues();

							cv2.put(DBtables.TB_registro_bonificaciones.OC_NUMERO, jsonData_reg.getString("oc_numero"));
							cv2.put(DBtables.TB_registro_bonificaciones.ITEM, jsonData_reg.getInt("item"));
							cv2.put(DBtables.TB_registro_bonificaciones.SECUENCIA, jsonData_reg.getInt("secuenciaPromocion"));
							cv2.put(DBtables.TB_registro_bonificaciones.AGRUPADO, jsonData_reg.getInt("agrupado"));
							cv2.put(DBtables.TB_registro_bonificaciones.ENTRADA, jsonData_reg.getString("entrada"));
							cv2.put(DBtables.TB_registro_bonificaciones.TIPO_UNIMED_ENTRADA, jsonData_reg.getInt("tipo_unimed_entrada"));
							cv2.put(DBtables.TB_registro_bonificaciones.UNIMED_ENTRADA, jsonData_reg.getString("unimedEntrada"));
							cv2.put(DBtables.TB_registro_bonificaciones.CANTIDAD_ENTRADA, jsonData_reg.getInt("cantidadEntrada"));
							cv2.put(DBtables.TB_registro_bonificaciones.MONTO_ENTRADA, jsonData_reg.getString("montoEntrada"));
							cv2.put(DBtables.TB_registro_bonificaciones.SALIDA, jsonData_reg.getString("salida"));
							cv2.put(DBtables.TB_registro_bonificaciones.TIPO_UNIMED_SALIDA, jsonData_reg.getInt("tipo_unimed_salida"));
							cv2.put(DBtables.TB_registro_bonificaciones.CANTIDAD_SALIDA, jsonData_reg.getInt("cantidadSalida"));
							cv2.put(DBtables.TB_registro_bonificaciones.ACUMULADO, jsonData_reg.getInt("acumulado"));
							cv2.put(DBtables.TB_registro_bonificaciones.CANTIDAD_DISPONIBLE, jsonData_reg.getInt("cantidadDisponible"));
							cv2.put(DBtables.TB_registro_bonificaciones.MONTO_DISPONIBLE, jsonData_reg.getString("montoDisponible"));
							cv2.put(DBtables.TB_registro_bonificaciones.FLAG, jsonData_reg.getInt("flagUltimo"));

							cv2.put(DBtables.TB_registro_bonificaciones.CODIGO_REGISTRO, jsonData_reg.getString(DBtables.TB_registro_bonificaciones.CODIGO_REGISTRO));
							if (jsonData_reg.getInt(DBtables.TB_registro_bonificaciones.CANTIDAD_TOTAL) == 0) {
								//Si es cero no se registra y queda como null para no afectar los algoritmos de obtener registro nulo usado
							}else{
								cv2.put(DBtables.TB_registro_bonificaciones.CANTIDAD_TOTAL, jsonData_reg.getString(DBtables.TB_registro_bonificaciones.CANTIDAD_TOTAL));
							}
							cv2.put(DBtables.TB_registro_bonificaciones.SALDO_ANTERIOR, jsonData_reg.getString(DBtables.TB_registro_bonificaciones.SALDO_ANTERIOR));
							cv2.put(DBtables.TB_registro_bonificaciones.CANTIDAD_ENTREGADA, jsonData_reg.getString(DBtables.TB_registro_bonificaciones.CANTIDAD_ENTREGADA));
							cv2.put(DBtables.TB_registro_bonificaciones.SALDO, jsonData_reg.getString(DBtables.TB_registro_bonificaciones.SALDO));
							cv2.put(DBtables.TB_registro_bonificaciones.CODIGO_ANTERIOR, jsonData_reg.getString(DBtables.TB_registro_bonificaciones.CODIGO_ANTERIOR));
							cv2.put(DBtables.TB_registro_bonificaciones.CODIGO_VENDEDOR, jsonData_reg.getString(DBtables.TB_registro_bonificaciones.CODIGO_VENDEDOR));
							cv2.put(DBtables.TB_registro_bonificaciones.CODIGO_CLIENTE, jsonData_reg.getString(DBtables.TB_registro_bonificaciones.CODIGO_CLIENTE));

							db.insert(DBtables.TB_registro_bonificaciones.TAG, null, cv2);

							Log.i("syncObjPedido det", "OCNUMERO: "	+  jsonData_reg.getString("oc_numero").trim()	+ " ENTRADA: " + jsonData_reg.getString("entrada"));
						}

						JSONArray json_visitas = new JSONArray(jsonData.getString("san_visitas"));
						final Type malla = new TypeToken<ArrayList<San_Visitas>>() {}.getType();
						final ArrayList<San_Visitas> lista = gson.fromJson(json_visitas.toString(), malla);

						for (San_Visitas sanVisita:lista){
							cv = new ContentValues();
							if (!DAO_San_Visitas.isSan_VisitasByOc_visitadoAndOc_numero_visitar(db, sanVisita.getOc_numero_visitado(), sanVisita.getOc_numero_visitar())) {
								cv.put(DBtables.San_Visitas.Grupo_Campaña,  sanVisita.getGrupo_Campaña());
								cv.put(DBtables.San_Visitas.Cod_Promotor,  sanVisita.getCod_Promotor());
								cv.put(DBtables.San_Visitas.Promotor,  sanVisita.getPromotor());
								cv.put(DBtables.San_Visitas.Cod_Colegio,  sanVisita.getCod_Colegio());
								cv.put(DBtables.San_Visitas.descripcion_Colegio,  sanVisita.getDescripcion_Colegio());
								cv.put(DBtables.San_Visitas.Ejecutivo_Descripcion,  sanVisita.getEjecutivo_Descripcion());
								cv.put(DBtables.San_Visitas.cargo_Descripción,  sanVisita.getCargo_Descripción());
								cv.put(DBtables.San_Visitas.Fecha_planificada,  sanVisita.getFecha_planificada());
								cv.put(DBtables.San_Visitas.Fecha_Ejecutada,  sanVisita.getFecha_Ejecutada());
								cv.put(DBtables.San_Visitas.Fecha_proxima_visita,   sanVisita.getFecha_proxima_visita());
								cv.put(DBtables.San_Visitas.Hora_inicio_ejecución,  sanVisita.getHora_inicio_ejecución());
								cv.put(DBtables.San_Visitas.Hora_Fin_Ejecución,  sanVisita.getHora_Fin_Ejecución());
								cv.put(DBtables.San_Visitas.fecha_de_modificación,  sanVisita.getFecha_de_modificación());
								cv.put(DBtables.San_Visitas.Estado,  sanVisita.getEstado());
								cv.put(DBtables.San_Visitas.Actividad,  sanVisita.getActividad());
								cv.put(DBtables.San_Visitas.Detalle,  sanVisita.getDetalle());
								cv.put(DBtables.San_Visitas.Actividad_Proxima,  sanVisita.getActividad_Proxima());
								cv.put(DBtables.San_Visitas.Detalle_Proximo,  sanVisita.getDetalle_Proximo());
								cv.put(DBtables.San_Visitas.Comentario_actividad,  sanVisita.getComentario_actividad());
								cv.put(DBtables.San_Visitas.tipo_visita,  ""+sanVisita.getTipo_visita());
								cv.put(DBtables.San_Visitas.id_rrhh, sanVisita.getId_rrhh());
								cv.put(DBtables.San_Visitas.latitud,  ""+sanVisita.getLatitud());
								cv.put(DBtables.San_Visitas.longitud,  ""+sanVisita.getLongitud());
								cv.put(DBtables.San_Visitas.distancia,  sanVisita.getDistancia());
								cv.put(DBtables.San_Visitas.oc_numero_visitado,  ""+sanVisita.getOc_numero_visitado());
								cv.put(DBtables.San_Visitas.oc_numero_visitar,  ""+sanVisita.getOc_numero_visitar());
								cv.put(DBtables.San_Visitas.item,  ""+sanVisita.getItem());
								cv.put(DBtables.San_Visitas.id_contacto,  ""+sanVisita.getId_contacto());
								cv.put(DBtables.San_Visitas.altitud,  ""+sanVisita.getAltitud());
								cv.put(DBtables.San_Visitas.descripcion_anulacion,  ""+sanVisita.getDescripcion_anulacion());

								long a =db.insert(DBtables.San_Visitas.TAG,null, cv);
							}
							//Log.d(TAG, STAG+" is insertado "+a);

						}
					}

				}

				
			}

			db.setTransactionSuccessful();

		} catch (JSONException e) {
			Log.e(DBclasses.TAG,	"JSON Exception RESPUESTA DE PEDIDOS:" + e.getMessage());
			throw new JSONException(e.getMessage());
		} catch (SQLiteException e) {
			Log.e(DBclasses.TAG,	"SQLITE Exception RESPUESTA DE PEDIDOS:" + e.getMessage());
			throw new SQLiteException(e.getMessage());
		} finally {
			db.endTransaction();
			db.close();
		}
		return jArray.length();

	}

	public void syncCuotaVendedor(JSONArray jArray) {

		JSONObject jsonData = null;
		ContentValues cv = new ContentValues();

		getReadableDatabase().delete(DBtables.CuotaVendedor.TAG, null, null);

		SQLiteDatabase db = getWritableDatabase();

		db.beginTransaction();

		try {

			for (int i = 0; i < jArray.length(); i++) {

				jsonData = jArray.getJSONObject(i);
				cv.put(DBtables.CuotaVendedor.SECUENCIA,
						jsonData.getString("secuencia").trim());
				cv.put(DBtables.CuotaVendedor.CODVEN,
						jsonData.getString("codven").trim());
				cv.put(DBtables.CuotaVendedor.CUOTA, jsonData
						.getString("cuota").trim());
				cv.put(DBtables.CuotaVendedor.NOMVEN,
						jsonData.getString("nomven").trim());
				cv.put(DBtables.CuotaVendedor.VENTAS,
						jsonData.getString("ventas").trim());
				cv.put(DBtables.CuotaVendedor.DEVOLUCIONES,
						jsonData.getString("devoluciones").trim());
				cv.put(DBtables.CuotaVendedor.VENTAS_EFECTIVAS, jsonData
						.getString("ventas_efectivas").trim());
				cv.put(DBtables.CuotaVendedor.PORCENTAJEPARTICIPACION, jsonData
						.getString("porcentajeParticipacion").trim());
				cv.put(DBtables.CuotaVendedor.PORCENTAJE_AVANCE, jsonData
						.getString("porcentaje_Avance").trim());
				cv.put(DBtables.CuotaVendedor.NOMCUOTA,
						jsonData.getString("nomcuota").trim());

				db.insert(DBtables.CuotaVendedor.TAG, null, cv);
				Log.i("CUOTA_VENDEDOR", "i= " + i + " secuencia: "
						+ jsonData.getString("secuencia").trim()
						+ " NOMCUOTA: " + jsonData.getString("nomcuota").trim());
			}

			db.setTransactionSuccessful();

		} catch (JSONException e) {
			Log.e(DBclasses.TAG,
					"JSON Exception CUOTA_VENDEDOR:" + e.getMessage());
		} finally {
			db.endTransaction();
			db.close();
		}

	}

	public void sync_almacenes(JSONArray jArray) {

		JSONObject jsonData = null;
		ContentValues cv = new ContentValues();

		getReadableDatabase().delete(DBtables.Almacenes.TAG, null, null);

		SQLiteDatabase db = getWritableDatabase();

		db.beginTransaction();

		try {

			for (int i = 0; i < jArray.length(); i++) {

				jsonData = jArray.getJSONObject(i);
				cv.put(DBtables.Almacenes.CODIGO_ALMACEN,jsonData.getString("codalm").trim());
				cv.put(DBtables.Almacenes.DESCRIPCION,jsonData.getString("desalm").trim());
				cv.put(DBtables.Almacenes.ID_LOCAL,jsonData.getString("id_local").trim());
				cv.put(DBtables.Almacenes.DIRECCION,jsonData.getString("direccionAlmacen").trim());
				cv.put(DBtables.Almacenes.DESCRIPCION_CORTA,jsonData.getString("descripcionCorta").trim());

				db.insert(DBtables.Almacenes.TAG, null, cv);
				Log.i("ALMACENES", "i= " + i + " COD_ALM: "
						+ jsonData.getString("codalm").trim());
			}

			db.setTransactionSuccessful();

		} catch (JSONException e) {
			Log.e(DBclasses.TAG, "JSON Exception ALMACENES:" + e.getMessage());
		} finally {
			db.endTransaction();
			db.close();
		}

	}

	public ArrayList<DB_Almacenes> obtenerListaAlmacenes() {
		String rawQuery;
		ArrayList<DB_Almacenes> lista_almacenes = new ArrayList<DB_Almacenes>();
		rawQuery = "select a.codalm, a.desalm, a.id_local, l.des_local from almacenes a "
				+ "inner join locales l on a.id_local = l.id_local";

		SQLiteDatabase db = getReadableDatabase();
		Log.d("DBclasses", "obtenerListaAlmacenes");
		Cursor cur = db.rawQuery(rawQuery, null);

		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			DB_Almacenes objAlmacen = new DB_Almacenes();
			objAlmacen.setCodalm(cur.getString(0));
			objAlmacen.setDesalm(cur.getString(1));
			objAlmacen.setId_local(cur.getString(2));
			objAlmacen.setLocal(cur.getString(3));
			Log.d("DBclasses",
					objAlmacen.getCodalm() + "-" + objAlmacen.getDesalm() + "-"
							+ objAlmacen.getLocal());
			lista_almacenes.add(objAlmacen);
			cur.moveToNext();
		}
		cur.close();
		db.close();

		return lista_almacenes;
	}

	public void sync_distribucion_almacenes(JSONArray jArray) {

		JSONObject jsonData = null;
		ContentValues cv = new ContentValues();

		getReadableDatabase().delete(DBtables.DistribucionAlmacenes.TAG, null,
				null);

		SQLiteDatabase db = getWritableDatabase();

		db.beginTransaction();

		try {

			for (int i = 0; i < jArray.length(); i++) {

				jsonData = jArray.getJSONObject(i);
				cv.put(DBtables.DistribucionAlmacenes.TX_ID_LOCAL, jsonData
						.getString("id_local").trim());
				cv.put(DBtables.DistribucionAlmacenes.TX_CODALM, jsonData
						.getString("codalm").trim());
				cv.put(DBtables.DistribucionAlmacenes.IN_ID_DISALM, jsonData
						.getString("id_disalm").trim());
				cv.put(DBtables.DistribucionAlmacenes.TX_DESCRIPCION, jsonData
						.getString("descripcion").trim());

				db.insert(DBtables.DistribucionAlmacenes.TAG, null, cv);
				Log.i("DISTRIBUCION ALMACENES", "i= " + i + " ID_DISALM: "
						+ jsonData.getString("id_disalm").trim());
			}

			db.setTransactionSuccessful();

		} catch (JSONException e) {
			Log.e(DBclasses.TAG,
					"JSON Exception DISTRIBUCION ALMACENES:" + e.getMessage());
		} finally {
			db.endTransaction();
			db.close();
		}

	}

	public void actualizarStock(JSONArray jArray) {

		JSONObject jsonData = null;
		ContentValues cv = new ContentValues();

		SQLiteDatabase db = getWritableDatabase();

		db.beginTransaction();

		try {

			for (int i = 0; i < jArray.length(); i++) {

				jsonData = jArray.getJSONObject(i);
				cv.put(DBtables.MTA_kardex.STOCK, jsonData.getString("stock")
						.trim());
				cv.put(DBtables.MTA_kardex.XTEMP, jsonData.getString("xtemp")
						.trim());

				// db.insert(DBtables.Almacenes.TAG, null, cv);
				String where = DBtables.MTA_kardex.PK_KARDEX + "=? and "
						+ DBtables.MTA_kardex.PK_CODALM + "=? and "
						+ DBtables.MTA_kardex.PK_CODPRO + "=?";

				String[] whereArgs = new String[] {
						jsonData.getString(DBtables.MTA_kardex.PK_KARDEX),
						jsonData.getString(DBtables.MTA_kardex.PK_CODALM),
						jsonData.getString(DBtables.MTA_kardex.PK_CODPRO) };

				db.update(DBtables.MTA_kardex.TAG, cv, where, whereArgs);

			}

			db.setTransactionSuccessful();

		} catch (JSONException e) {
			Log.e(DBclasses.TAG,
					"JSON Exception ActualizarStock:" + e.getMessage());
		} finally {
			db.endTransaction();
			db.close();
		}

	}

	// //////////////////////////////FIN METODOS DE SINCRONIZACION
	// (JSON)//////////////////////////
	// ///////////////////////////////////////////////////////////////////////////////////////////

	public ArrayList<DBCta_Ingresos> getCtas_IngresosXSaldo() {
		String rawQuery;

		rawQuery = "Select * from cta_ingresos  where cta_ingresos.saldo > 0 GROUP BY codcli";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		ArrayList<DBCta_Ingresos> dbcta_ingresos = new ArrayList<DBCta_Ingresos>();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			DBCta_Ingresos dbcta = new DBCta_Ingresos();
			dbcta.setSecuencia(cur.getString(0));
			dbcta.setCodmon(cur.getString(1));
			dbcta.setCoddoc(cur.getString(2));
			dbcta.setSerie_doc(cur.getString(3));
			dbcta.setNumero_factura(cur.getString(4));
			dbcta.setTotal(cur.getString(5));
			dbcta.setAcuenta(cur.getString(6));
			dbcta.setSaldo(cur.getString(7));
			dbcta.setFeccom(cur.getString(8));
			dbcta.setCodcli(cur.getString(9));
			dbcta.setUsername(cur.getString(10));
			dbcta.setFecoperacion(cur.getString(11));
			dbcta_ingresos.add(dbcta);
			cur.moveToNext();
		}
		cur.close();
		db.close();
		return dbcta_ingresos;
	}

	public String getFecha() {
		String fecha = "";

		int dia = calendar.get(Calendar.DAY_OF_MONTH);
		int mes = calendar.get(Calendar.MONTH) + 1;
		int anio = calendar.get(Calendar.YEAR);

		String _dia = "" + dia;
		String _mes = "" + mes;

		if (dia <= 9) {
			_dia = "0" + dia;
		}

		if (mes <= 9) {
			_mes = "0" + mes;
		}

		fecha = _dia + "/" + _mes + "/" + anio;

		return fecha;
	}
	public ArrayList<DBPedido_Detalle> getPedidosDetallexOc_numero(String oc_numero, int item) {

		String rawQuery;

		rawQuery = "select * from pedido_detalle where oc_numero='"+oc_numero+"' and item <= "+item;
		Log.d("", rawQuery);

		SQLiteDatabase db = getReadableDatabase();

		Cursor cur = db.rawQuery(rawQuery, null);
		ArrayList<DBPedido_Detalle> lista_pedidos_detalles = new ArrayList<DBPedido_Detalle>();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			DBPedido_Detalle dbdetalle = new DBPedido_Detalle();
			dbdetalle.setOc_numero(cur.getString(0));
			dbdetalle.setEan_item(cur.getString(1));
			dbdetalle.setCip(cur.getString(2));
			dbdetalle.setPrecio_bruto(cur.getString(3));
			dbdetalle.setPrecio_neto(cur.getString(4));
			dbdetalle.setPercepcion(cur.getString(5));
			dbdetalle.setCantidad(cur.getInt(6));
			dbdetalle.setTipo_producto(cur.getString(7));
			dbdetalle.setUnidad_medida(cur.getString(8));
			dbdetalle.setPeso_bruto(cur.getString(9));
			dbdetalle.setFlag(cur.getString(10));
			dbdetalle.setCod_politica(cur.getString(11));			
			dbdetalle.setSec_promo(cur.getString(12));
			
			if(cur.getString(12).equals("")){
				dbdetalle.setItem_promo(0);
			}else{
				dbdetalle.setItem_promo(Integer.parseInt(cur.getString(12)));
			}
			
			dbdetalle.setItem_promo(cur.getInt(13));
			dbdetalle.setAgrup_promo(Integer.parseInt(cur.getString(14)));

			lista_pedidos_detalles.add(dbdetalle);
			cur.moveToNext();
		}
		cur.close();
		db.close();

		return lista_pedidos_detalles;
	}
	public ArrayList<DBPedido_Detalle> getPedidosDetallexOc_numero(String oc_numero) {

		String rawQuery;

		rawQuery = "select " +
				"pd.oc_numero\n" +
				",pd.ean_item\n" +
				",pd.cip\n" +
				",pd.precio_bruto\n" +
				",sum(pd.precio_neto) as precio_neto\n" +
				",pd.percepcion\n" +
				",sum(pd.cantidad) as cantidad\n" +
				",pd.tipo_producto\n" +
				",pd.unidad_medida\n" +
				",pd.peso_bruto\n" +
				",pd.flag\n" +
				",pd.cod_politica\n" +
				",pd.sec_promo\n" +
				",pd.item_promo\n" +
				",pd.agrup_promo\n" +/*
				",pd.item\n" +
				",pd.precioLista\n" +
				",pd.porcentaje_desc\n" +
				",pd.descuento\n" +
				",pd.lote\n" +
				",pd.motivoDevolucion\n" +
				",pd.Expectativa\n" +
				",pd.Envase\n" +
				",pd.Contenido\n" +
				",pd.Proceso\n" +
				",pd.observacionDevolucion\n" +
				",pd.tipoDocumento\n" +
				",pd.serieDevolucion\n" +
				",pd.numeroDevolucion\n" +
				",pd.porcentaje_desc_extra" +*/
				" from pedido_detalle pd " +
				"where pd.oc_numero='"+oc_numero+"' " +
				"and pd.tipo_producto='V' " +
				"group by pd.oc_numero, pd.cip ";

		SQLiteDatabase db = getReadableDatabase();

		Cursor cur = db.rawQuery(rawQuery, null);
		ArrayList<DBPedido_Detalle> lista_pedidos_detalles = new ArrayList<DBPedido_Detalle>();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			DBPedido_Detalle dbdetalle = new DBPedido_Detalle();
			dbdetalle.setOc_numero(cur.getString(0));
			dbdetalle.setEan_item(cur.getString(1));
			dbdetalle.setCip(cur.getString(2));
			dbdetalle.setPrecio_bruto(cur.getString(3));
			dbdetalle.setPrecio_neto(cur.getString(4));
			dbdetalle.setPercepcion(cur.getString(5));
			dbdetalle.setCantidad(cur.getInt(6));
			dbdetalle.setTipo_producto(cur.getString(7));
			dbdetalle.setUnidad_medida(cur.getString(8));
			dbdetalle.setPeso_bruto(cur.getString(9));
			dbdetalle.setFlag(cur.getString(10));
			dbdetalle.setCod_politica(cur.getString(11));			
			dbdetalle.setSec_promo(cur.getString(12));
			
			if(cur.getString(12).equals("")){
				dbdetalle.setItem_promo(0);
			}else{
				dbdetalle.setItem_promo(cur.getInt(13));
			}
			dbdetalle.setAgrup_promo(Integer.parseInt(cur.getString(14)));
			
			lista_pedidos_detalles.add(dbdetalle);
			cur.moveToNext();
		}
		cur.close();
		db.close();

		return lista_pedidos_detalles;
	}
	
	public ArrayList<DBPedido_Detalle> getPedidosDetalle() {

		String rawQuery;

		rawQuery = "select * from pedido_detalle";

		SQLiteDatabase db = getReadableDatabase();

		Cursor cur = db.rawQuery(rawQuery, null);
		ArrayList<DBPedido_Detalle> lista_pedidos_detalles = new ArrayList<DBPedido_Detalle>();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			DBPedido_Detalle dbdetalle = new DBPedido_Detalle();
			dbdetalle.setOc_numero(cur.getString(0));
			dbdetalle.setEan_item(cur.getString(1));
			dbdetalle.setCip(cur.getString(2));
			dbdetalle.setPrecio_bruto(cur.getString(3));
			dbdetalle.setPrecio_neto(cur.getString(4));
			dbdetalle.setCantidad(cur.getInt(5));
			dbdetalle.setTipo_producto(cur.getString(6));
			dbdetalle.setUnidad_medida(cur.getString(7));
			dbdetalle.setPeso_bruto(cur.getString(8));
			dbdetalle.setFlag(cur.getString(9));
			dbdetalle.setCod_politica(cur.getString(10));
			dbdetalle.setSec_promo(cur.getString(11));
			dbdetalle.setItem_promo(Integer.parseInt(cur.getString(12)));
			dbdetalle.setAgrup_promo(Integer.parseInt(cur.getString(13)));

			lista_pedidos_detalles.add(dbdetalle);
			cur.moveToNext();
		}
		cur.close();
		db.close();

		return lista_pedidos_detalles;
	}

	public DBPedido_Detalle getPedidosDetalleEntity(String oc, String codpro) {
		String rawQuery;
		rawQuery = " oc_numero='" + oc+ "' and cip='" + codpro + "'";
		return getPedidosDetalleEntityMain(rawQuery);
	}

	public DBPedido_Detalle getPedidosDetalleEntity(String oc, String codpro, int item) {
		String rawQuery;
		rawQuery = " oc_numero='" + oc+ "' and cip='" + codpro + "' and item ="+item;
		return getPedidosDetalleEntityMain(rawQuery);
	}
	private DBPedido_Detalle getPedidosDetalleEntityMain(String addWhere) {

		String rawQuery;

		rawQuery = "select * from "+ Pedido_detalle.TAG+" " +
				"where 5=5 and "+addWhere;
		Log.d("getPedidosDetalleEntity",rawQuery);
		SQLiteDatabase db = getReadableDatabase();

		Cursor cur = db.rawQuery(rawQuery, null);
		DBPedido_Detalle dbdetalle = new DBPedido_Detalle();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {
			Log.e("entra al while",""+dbdetalle.getOc_numero());
			dbdetalle.setOc_numero(cur.getString(0));
			dbdetalle.setEan_item(cur.getString(1));
			dbdetalle.setCip(cur.getString(2));
			dbdetalle.setPrecio_bruto(cur.getString(3));
			dbdetalle.setPrecio_neto(cur.getString(4));
			dbdetalle.setPercepcion(cur.getString(5));
			dbdetalle.setCantidad(cur.getInt(6));
			dbdetalle.setTipo_producto(cur.getString(7));
			dbdetalle.setUnidad_medida(cur.getString(8));
			dbdetalle.setPeso_bruto(cur.getString(9));
			dbdetalle.setFlag(cur.getString(10));
			dbdetalle.setCod_politica(cur.getString(11));
			dbdetalle.setItem(cur.getInt(15));
			dbdetalle.setPrecioLista(cur.getString(cur.getColumnIndex("precioLista")));
			dbdetalle.setPorcentaje_desc(cur.getDouble(cur.getColumnIndex("porcentaje_desc")));
			dbdetalle.setPorcentaje_desc_extra(cur.getDouble(cur.getColumnIndex("porcentaje_desc_extra")));
Log.e("getPedidosDetalleEntity","Oc_numero: "+cur.getString(0));
		Log.e("getPedidosDetalleEntity","Ean_item: "+cur.getString(1));
		Log.e("getPedidosDetalleEntity","Cip: "+cur.getString(2));
		Log.e("getPedidosDetalleEntity","Precio_bruto; "+cur.getString(3));
		Log.e("getPedidosDetalleEntity","Precio_neto: "+cur.getString(4));
		Log.e("getPedidosDetalleEntity","Percepcion: "+cur.getString(5));		
		Log.e("getPedidosDetalleEntity","Cantidad: "+cur.getInt(6));		
		Log.e("getPedidosDetalleEntity","Tipo_producto: "+cur.getString(7));
		Log.e("getPedidosDetalleEntity","Unidad_medida: "+cur.getString(8));
		Log.e("getPedidosDetalleEntity","Peso_bruto: "+cur.getString(9));
		Log.e("getPedidosDetalleEntity","Flag: "+cur.getString(10));
		Log.e("getPedidosDetalleEntity","Cod_politica: "+cur.getString(11));
		Log.e("getPedidosDetalleEntity","Item: "+cur.getInt(15));
			cur.moveToNext();
		}
		
		
		
		cur.close();
		db.close();
		
		
		
		return dbdetalle;
	}

	public ArrayList<DBPedido_Devolucion> getPedidosDevolucion() {

		String rawQuery;

		rawQuery = "select * from pedido_devolucion";

		SQLiteDatabase db = getReadableDatabase();

		Cursor cur = db.rawQuery(rawQuery, null);
		ArrayList<DBPedido_Devolucion> lista_pedido_devolucion = new ArrayList<DBPedido_Devolucion>();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			DBPedido_Devolucion dbdevolucion = new DBPedido_Devolucion();
			dbdevolucion.setOc_numero(cur.getString(0));
			dbdevolucion.setEan_item(cur.getString(1));
			dbdevolucion.setCip(cur.getString(2));
			dbdevolucion.setCantidad(cur.getInt(3));
			dbdevolucion.setUnidad_medida(cur.getString(4));
			dbdevolucion.setFlag(cur.getString(5));

			lista_pedido_devolucion.add(dbdevolucion);
			cur.moveToNext();
		}
		cur.close();
		db.close();

		return lista_pedido_devolucion;
	}

	public ArrayList<DBIngresos> getIngresos() {

		String rawQuery;

		rawQuery = "select * from ingresos where flag<>'A'";

		SQLiteDatabase db = getReadableDatabase();

		Cursor cur = db.rawQuery(rawQuery, null);
		ArrayList<DBIngresos> lista_ingresos = new ArrayList<DBIngresos>();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			DBIngresos dbingresos = new DBIngresos();
			dbingresos.setSecuencia(cur.getString(0));
			dbingresos.setSecitm(cur.getString(1));
			dbingresos.setFecpag(cur.getString(2));
			dbingresos.setTotal(cur.getString(3));
			dbingresos.setAcuenta(cur.getString(4));
			dbingresos.setSaldo(cur.getString(5));
			dbingresos.setCodcue(cur.getString(6));
			dbingresos.setNumope(cur.getString(7));
			dbingresos.setCodforpag(cur.getString(8));
			dbingresos.setTipo_cambio(cur.getString(9));
			dbingresos.setCodmon(cur.getString(10));
			dbingresos.setMonto_afecto(cur.getString(11));
			dbingresos.setUsername(cur.getString(12));
			dbingresos.setFecoperacion(cur.getString(13));
			dbingresos.setFlag(cur.getString(14));
			dbingresos.setLatitud(cur.getString(15));
			dbingresos.setLongitu(cur.getString(16));
			dbingresos.setDT_INGR_FECHASERVIDOR(cur.getString(17));

			Log.w("SECUENCIA INGRESOS REPORTE", cur.getString(1));
			lista_ingresos.add(dbingresos);
			cur.moveToNext();
		}
		cur.close();
		db.close();

		return lista_ingresos;
	}

	// Agregado el 07/08/2013 para solucionar problema de ventana cobranzas del
	// modulo reportes
	// Se mostraba todos los ingresos sin excepcion
	public ArrayList<DBIngresos> getIngresos_reportes() {

		String fechaConfig = getFecha2().trim();
		String rawQuery;

		rawQuery = "select * from ingresos where substr(secitm,1,1)<>'S' and substr(fecpag,1,10)= '"
				+ fechaConfig + "' order by fecpag desc";
		
		Log.d("Reporte cobranza", rawQuery );

		SQLiteDatabase db = getReadableDatabase();

		Cursor cur = db.rawQuery(rawQuery, null);
		ArrayList<DBIngresos> lista_ingresos = new ArrayList<DBIngresos>();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			DBIngresos dbingresos = new DBIngresos();
			dbingresos.setSecuencia(cur.getString(0));
			dbingresos.setSecitm(cur.getString(1));
			dbingresos.setFecpag(cur.getString(2));
			dbingresos.setTotal(cur.getString(3));
			dbingresos.setAcuenta(cur.getString(4));
			dbingresos.setSaldo(cur.getString(5));
			dbingresos.setCodcue(cur.getString(6));
			dbingresos.setNumope(cur.getString(7));
			dbingresos.setCodforpag(cur.getString(8));
			dbingresos.setTipo_cambio(cur.getString(9));
			dbingresos.setCodmon(cur.getString(10));
			dbingresos.setMonto_afecto(cur.getString(11));
			dbingresos.setUsername(cur.getString(12));
			dbingresos.setFecoperacion(cur.getString(13));
			dbingresos.setFlag(cur.getString(14));
			dbingresos.setLatitud(cur.getString(15));
			dbingresos.setLongitu(cur.getString(16));
			dbingresos.setDT_INGR_FECHASERVIDOR(cur.getString(17));
			dbingresos.setEstado(cur.getString(18));
			dbingresos.setObservacion(cur.getString(19));
			dbingresos.setCodcli(cur.getString(20));

			Log.w("SECUENCIA INGRESOS REPORTE", cur.getString(1));
			lista_ingresos.add(dbingresos);
			cur.moveToNext();
		}
		cur.close();
		db.close();

		return lista_ingresos;
	}

	public ArrayList<DBIngresos> getIngresos_paraVerificacion() {

		String fechaConfig = getFecha2().trim();
		String rawQuery;

		rawQuery = "select * from ingresos where substr(secitm,1,1)<>'S' and substr(fecpag,1,10)= '"
				+ fechaConfig + "' and estado<>'L' order by fecpag desc";

		SQLiteDatabase db = getReadableDatabase();

		Cursor cur = db.rawQuery(rawQuery, null);
		ArrayList<DBIngresos> lista_ingresos = new ArrayList<DBIngresos>();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			DBIngresos dbingresos = new DBIngresos();
			dbingresos.setSecuencia(cur.getString(0));
			dbingresos.setSecitm(cur.getString(1));
			dbingresos.setFecpag(cur.getString(2));
			dbingresos.setTotal(cur.getString(3));
			dbingresos.setAcuenta(cur.getString(4));
			dbingresos.setSaldo(cur.getString(5));
			dbingresos.setCodcue(cur.getString(6));
			dbingresos.setNumope(cur.getString(7));
			dbingresos.setCodforpag(cur.getString(8));
			dbingresos.setTipo_cambio(cur.getString(9));
			dbingresos.setCodmon(cur.getString(10));
			dbingresos.setMonto_afecto(cur.getString(11));
			dbingresos.setUsername(cur.getString(18));// guardo el estado para
														// la verificacion en el
														// servidor
			dbingresos.setFecoperacion(cur.getString(13));
			dbingresos.setFlag(cur.getString(14));
			dbingresos.setLatitud(cur.getString(15));
			dbingresos.setLongitu(cur.getString(16));
			dbingresos.setDT_INGR_FECHASERVIDOR(cur.getString(17));
			dbingresos.setEstado(cur.getString(18));

			Log.w("SECUENCIA INGRESOS REPORTE", cur.getString(1));
			lista_ingresos.add(dbingresos);
			cur.moveToNext();
		}
		cur.close();
		db.close();

		return lista_ingresos;
	}

	// metodo para el envio de pendientes con el metodo json
	// Obtiene todos los pedidos con FLAG = P
	public ArrayList<DB_ObjPedido> getTodosObjPedido_json_flagp() {
		Gson gson = new Gson();
		String rawQuery;

		rawQuery = "select * from pedido_cabecera where flag in('P','I')";

		SQLiteDatabase db = getReadableDatabase();

		Cursor cur = db.rawQuery(rawQuery, null);

		ArrayList<DB_ObjPedido> lista_pedidos = new ArrayList<DB_ObjPedido>();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			DB_ObjPedido dbpedido = new DB_ObjPedido();
			dbpedido.setOc_numero(cur.getString(0));
			dbpedido.setSitio_enfa(cur.getString(1));
			dbpedido.setMonto_total(cur.getString(2));
			dbpedido.setPercepcion_total(cur.getString(3));
			dbpedido.setValor_igv(cur.getString(4));
			dbpedido.setMoneda(cur.getString(5));
			dbpedido.setFecha_oc(cur.getString(6));
			dbpedido.setFecha_mxe(cur.getString(7));
			dbpedido.setCond_pago(cur.getString(8));
			dbpedido.setCod_cli(cur.getString(9));
			dbpedido.setCod_emp(cur.getString(10));
			dbpedido.setEstado(cur.getString(11));
			dbpedido.setUsername(cur.getString(12));
			dbpedido.setRuta(cur.getString(13));
			dbpedido.setObserv(cur.getString(14));
			dbpedido.setCod_noventa(cur.getInt(15));
			dbpedido.setPeso_total(cur.getString(16));
			dbpedido.setFlag(cur.getString(17));
			dbpedido.setLatitud(cur.getString(18));
			dbpedido.setLongitud(cur.getString(19));
			dbpedido.setCodigo_familiar(cur.getString(20));
			dbpedido.setTotalSujetoPercepcion(cur.getString(22));

			dbpedido.setNumeroOrdenCompra(""+cur.getString(24));
			dbpedido.setCodigoPrioridad(""+cur.getString(25));
			dbpedido.setCodigoSucursal(""+cur.getString(26));
			dbpedido.setCodigoPuntoEntrega(""+cur.getString(27));
			dbpedido.setCodigoTipoDespacho(""+cur.getString(28));
			dbpedido.setFlagEmbalaje(""+cur.getString(29));
			dbpedido.setFlagPedido_Anticipo(""+cur.getString(30));
			dbpedido.setCodigoTransportista(""+cur.getString(31));
			dbpedido.setCodigoAlmacen(""+cur.getString(32));
			dbpedido.setObservacion2(""+cur.getString(33));
			dbpedido.setObservacion3(""+cur.getString(34));
			dbpedido.setObservacionDescuento(""+cur.getString(35));
			dbpedido.setObservacionTipoProducto(""+cur.getString(36));
			dbpedido.setFlagDescuento(cur.getString(37));
			dbpedido.setCodigoObra(cur.getString(38));
			dbpedido.setFlagDespacho(cur.getString(39));
			dbpedido.setDocAdicional(cur.getString(40));
			dbpedido.setSubtotal(cur.getString(41));
			dbpedido.setTipoDocumento(cur.getString(42));

			dbpedido.setTipoRegistro(cur.getString(43));
			dbpedido.setDiasVigencia(cur.getString(44));
			if (cur.getString(45)==null){
				dbpedido.setPedidoAnterior("");
			}else{
				dbpedido.setPedidoAnterior(cur.getString(45));
			}
			dbpedido.setCodTurno(cur.getString(46));
			dbpedido.setNroletra(cur.getString(47));
			dbpedido.setObservacion4(cur.getString(48));

			lista_pedidos.add(dbpedido);
			cur.moveToNext();
		}
		cur.close();
		db.close();
		return lista_pedidos;
	}

	public ArrayList<DBIngresos> getTodosIngresos_json_flagp() {

		String fechaConfig = getFecha2().trim();
		String rawQuery;

		rawQuery = "select * from ingresos where flag in ('P','I') and substr(secitm,1,1)<>'S' and substr(fecpag,1,10)= '"
				+ fechaConfig + "' order by fecpag desc";

		SQLiteDatabase db = getReadableDatabase();

		Cursor cur = db.rawQuery(rawQuery, null);

		ArrayList<DBIngresos> lista_ingresos = new ArrayList<DBIngresos>();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			DBIngresos dbingresos = new DBIngresos();
			dbingresos.setSecuencia(cur.getString(0));
			dbingresos.setSecitm(cur.getString(1));
			dbingresos.setFecpag(cur.getString(2));
			dbingresos.setTotal(cur.getString(3));
			dbingresos.setAcuenta(cur.getString(4));
			dbingresos.setSaldo(cur.getString(5));
			dbingresos.setCodcue(cur.getString(6));
			dbingresos.setNumope(cur.getString(7));
			dbingresos.setCodforpag(cur.getString(8));
			dbingresos.setTipo_cambio(cur.getString(9));
			dbingresos.setCodmon(cur.getString(10));
			dbingresos.setMonto_afecto(cur.getString(11));
			dbingresos.setUsername(cur.getString(12));
			dbingresos.setFecoperacion(cur.getString(13));
			dbingresos.setFlag(cur.getString(14));
			dbingresos.setLatitud(cur.getString(15));
			dbingresos.setLongitu(cur.getString(16));
			dbingresos.setDT_INGR_FECHASERVIDOR(cur.getString(17));
			dbingresos.setEstado(cur.getString(18));
			dbingresos.setObservacion(cur.getString(19));
			dbingresos.setCodcli(cur.getString(20));
			
			dbingresos.setTipoDoc(cur.getString(21));
			dbingresos.setSerie(cur.getString(22));
			dbingresos.setNumero(cur.getString(23));
			dbingresos.setCodigoBanco(cur.getString(24));

			lista_ingresos.add(dbingresos);
			cur.moveToNext();
		}
		cur.close();
		db.close();
		return lista_ingresos;
	}

	// Metodo para el envio de pedido (PedidoActivity)
	public ArrayList<DB_ObjPedido> 	getObjPedido_jsons(String oc_numero) {

		String rawQuery;
		String[] args = { oc_numero };

		rawQuery = "select * from pedido_cabecera where oc_numero like ?";
		Log.i(TAG+":getObjPedido_json:", rawQuery+"\n "+oc_numero);

		SQLiteDatabase db = getReadableDatabase();

		Cursor cur = db.rawQuery(rawQuery, args);

		ArrayList<DB_ObjPedido> lista_pedidos = new ArrayList<DB_ObjPedido>();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			DB_ObjPedido dbpedido = new DB_ObjPedido();
			dbpedido.setOc_numero(cur.getString(0));
			dbpedido.setSitio_enfa(cur.getString(1));
			dbpedido.setMonto_total(cur.getString(2));
			dbpedido.setPercepcion_total(cur.getString(3));
			dbpedido.setValor_igv(cur.getString(4));
			dbpedido.setMoneda(cur.getString(5));
			dbpedido.setFecha_oc(cur.getString(6));
			dbpedido.setFecha_mxe(cur.getString(7));
			dbpedido.setCond_pago(cur.getString(8));
			dbpedido.setCod_cli(cur.getString(9));
			dbpedido.setCod_emp(cur.getString(10));
			dbpedido.setEstado(cur.getString(11));
			dbpedido.setUsername(cur.getString(12));
			dbpedido.setRuta(cur.getString(13));
			dbpedido.setObserv(cur.getString(14));
			dbpedido.setCod_noventa(cur.getInt(15));
			dbpedido.setPeso_total(cur.getString(16));
			dbpedido.setFlag(cur.getString(17));
			dbpedido.setLatitud(cur.getString(18));
			dbpedido.setLongitud(cur.getString(19));
			dbpedido.setCodigo_familiar(cur.getString(20));
			dbpedido.setTotalSujetoPercepcion(cur.getString(22));
			
			dbpedido.setNumeroOrdenCompra(""+cur.getString(24));
			dbpedido.setCodigoPrioridad(""+cur.getString(25));
			dbpedido.setCodigoSucursal(""+cur.getString(26));
			dbpedido.setCodigoPuntoEntrega(""+cur.getString(27));
			dbpedido.setCodigoTipoDespacho(""+cur.getString(28));
			dbpedido.setFlagEmbalaje(""+cur.getString(29));
			dbpedido.setFlagPedido_Anticipo(""+cur.getString(30));
			dbpedido.setCodigoTransportista(""+cur.getString(31));
			dbpedido.setCodigoAlmacen(""+cur.getString(32));
			dbpedido.setObservacion2(""+cur.getString(33));
			dbpedido.setObservacion3(""+cur.getString(34));
			dbpedido.setObservacionDescuento(""+cur.getString(35));
			dbpedido.setObservacionTipoProducto(""+cur.getString(36));
			dbpedido.setFlagDescuento(cur.getString(37));
			dbpedido.setCodigoObra(cur.getString(38));
			dbpedido.setFlagDespacho(cur.getString(39));
			dbpedido.setDocAdicional(cur.getString(40));
			dbpedido.setSubtotal(cur.getString(41));			
			dbpedido.setTipoDocumento(cur.getString(42));
			
			dbpedido.setTipoRegistro(cur.getString(43));
			dbpedido.setDiasVigencia(cur.getString(44));
			if (cur.getString(45)==null){
				dbpedido.setPedidoAnterior("");
			}else{
				dbpedido.setPedidoAnterior(cur.getString(45));
			}
			dbpedido.setCodTurno(cur.getString(46));
			dbpedido.setNroletra(cur.getString(47));
			dbpedido.setObservacion4(cur.getString(48));
			
			Log.e("NumeroOrdenCompra", ""+cur.getString(24));
			Log.e("CodigoPrioridad", ""+cur.getString(25));
			Log.e("CodigoSucursal", ""+cur.getString(26));
			Log.e("CodigoPuntoEntrega", ""+cur.getString(27));
			Log.e("CodigoTipoDespacho", ""+cur.getString(28));
			Log.e("FlagEmbalaje", ""+cur.getString(29));
			Log.e("FlagPedido_Anticipo", ""+cur.getString(30));
			Log.e("CodigoTransportista", ""+cur.getString(31));
			Log.e("CodigoAlmacen", ""+cur.getString(32));
			Log.e("tObservacion2", ""+cur.getString(33));
			Log.e("tObservacion3", ""+cur.getString(34));
			Log.e("ObservacionDescuento", ""+cur.getString(35));
			Log.e("ObservacionTipoProducto", ""+cur.getString(36));
			Log.e("setFlagDescuento", ""+cur.getString(37));
			Log.e("setCodigoObra", ""+cur.getString(38));
			Log.e("setFlagDespacho", ""+cur.getString(39));
			Log.e("setDocAdicional",""+cur.getString(40));
			Log.e("setSubtotal",""+cur.getString(41));
			Log.e("setTipoRegistro",""+cur.getString(43));
			Log.e("setDiasVigencia",""+cur.getString(44));
			Log.e("setPedidoAnterior",""+cur.getString(45));
			Log.e("setCodigoTurno",""+cur.getString(46));
			Log.e("setNroLetra ",""+cur.getString(47));
			Log.e("setObservacion3=4 ",""+cur.getString(48));

			lista_pedidos.add(dbpedido);
			cur.moveToNext();
		}
		cur.close();
		db.close();
		return lista_pedidos;
	}

	public ArrayList<DBPedido_Cabecera> getTodosPedidosCabecera_paraVerificacion() {

		String rawQuery;

		rawQuery = "select *,"
							+ "(select count(*) from pedido_detalle where oc_numero=pedido_cabecera.oc_numero),"
							+ "(select count(*) from registro_bonificaciones where oc_numero=pedido_cabecera.oc_numero) "
							+ " from pedido_cabecera where flag <> 'A' or cod_noventa = "+GlobalVar.CODIGO_VISITA_CLIENTE;

		SQLiteDatabase db = getReadableDatabase();

		Cursor cur = db.rawQuery(rawQuery, null);

		ArrayList<DBPedido_Cabecera> lista_pedidos = new ArrayList<DBPedido_Cabecera>();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			DBPedido_Cabecera dbpedido = new DBPedido_Cabecera();
			dbpedido.setOc_numero(cur.getString(0));
			dbpedido.setSitio_enfa(cur.getString(1));
			dbpedido.setMonto_total(cur.getString(2));
			dbpedido.setPercepcion_total(cur.getString(3));
			dbpedido.setValor_igv(cur.getString(4));
			dbpedido.setMoneda(cur.getString(5));
			dbpedido.setFecha_oc(cur.getString(6));
			dbpedido.setFecha_mxe(cur.getString(7));
			dbpedido.setCond_pago(cur.getString(8));
			dbpedido.setCod_cli(cur.getString(9));
			dbpedido.setCod_emp(cur.getString(10));
			dbpedido.setEstado(cur.getString(11));
			dbpedido.setUsername(cur.getString(12));
			dbpedido.setRuta(cur.getString(13));
			dbpedido.setObserv(cur.getString(14));
			dbpedido.setCod_noventa(cur.getInt(15));
			dbpedido.setPeso_total(Integer.toString(cur.getInt(16)));
			dbpedido.setFlag(cur.getString(17));
			dbpedido.setLatitud(cur.getString(18));
			dbpedido.setLongitud(cur.getString(19)); 
			dbpedido.setCodigo_familiar(cur.getString(20));
			//Fecha servidor 21
			dbpedido.setTotalSujetopercepcion(cur.getString(22));
			//Tipo de vista 23
			
			Log.w("getTodosPedidosCabecera_paraVerificacion",	"OC: " + dbpedido.getOc_numero() + " cant detalles: "+cur.getString(24)+" cant reg bonificacion: "+cur.getString(25) );

			lista_pedidos.add(dbpedido);
			cur.moveToNext();
		}
		cur.close();
		db.close();
		return lista_pedidos;
	}

	public DBPedido_Cabecera getPedidoCabecera_paraVerificacion(String oc_numero) {

		String rawQuery;

		rawQuery = "select *,(select count(*) from pedido_detalle where oc_numero=pedido_cabecera.oc_numero) from pedido_cabecera where oc_numero='"
				+ oc_numero + "'";

		SQLiteDatabase db = getReadableDatabase();

		Cursor cur = db.rawQuery(rawQuery, null);

		DBPedido_Cabecera dbpedido = new DBPedido_Cabecera();
		cur.moveToFirst();

		if (cur.moveToFirst()) {

			do {

				dbpedido.setOc_numero(cur.getString(0));
				dbpedido.setSitio_enfa(cur.getString(1));
				dbpedido.setMonto_total(cur.getString(2));
				dbpedido.setValor_igv(cur.getString(3));
				dbpedido.setMoneda(cur.getString(4));
				dbpedido.setFecha_oc(cur.getString(5));
				dbpedido.setFecha_mxe(cur.getString(6));
				dbpedido.setCond_pago(cur.getString(7));
				dbpedido.setCod_cli(cur.getString(8));
				dbpedido.setCod_emp(cur.getString(9));
				dbpedido.setEstado(cur.getString(10));
				dbpedido.setUsername(cur.getString(11));
				dbpedido.setRuta(cur.getString(12));
				dbpedido.setObserv(cur.getString(13));
				dbpedido.setCod_noventa(cur.getInt(14));
				dbpedido.setPeso_total(Integer.toString(cur.getInt(21))); // se
																			// guarda
																			// la
																			// cantidad
																			// de
																			// detalles,
																			// solo
																			// para
																			// la
																			// verificacion
				dbpedido.setFlag(cur.getString(16));
				dbpedido.setLatitud(cur.getString(17));
				dbpedido.setLongitud(cur.getString(18));

				Log.w("getPedidoCabecera_paraVerificacion X oc", "OC: "
						+ dbpedido.getOc_numero() + " cant detalles: "
						+ dbpedido.getPeso_total() + "");

			} while (cur.moveToNext());

		}

		cur.close();
		db.close();

		return dbpedido;
	}

	public ArrayList<DBProductos> getProductos() {
		String rawQuery;

		rawQuery = "select * from producto";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		ArrayList<DBProductos> lista_productos = new ArrayList<DBProductos>();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			DBProductos dbproductos = new DBProductos();
			dbproductos.setCodpro(cur.getString(1));
			dbproductos.setDespro(cur.getString(2));
			dbproductos.setAbrevpro(cur.getString(3));
			dbproductos.setGrupo(cur.getString(4));
			dbproductos.setFamilia(cur.getString(5));
			dbproductos.setSub_familia(cur.getString(6));
			dbproductos.setEan13(cur.getString(7));
			dbproductos.setCod_rapido(cur.getString(8));
			dbproductos.setCodunimed(cur.getString(9));
			dbproductos.setCodunimed_almacen(cur.getString(10));
			dbproductos.setFactor_conversion(cur.getDouble(11));
			dbproductos.setAfecto(cur.getString(12));
			dbproductos.setEstado(cur.getString(13));
			dbproductos.setPeso(cur.getDouble(14));
			dbproductos.setFoto(cur.getString(15));

			lista_productos.add(dbproductos);
			cur.moveToNext();
		}
		cur.close();
		db.close();
		return lista_productos;
	}

	public ArrayList<DBProductos> getCodpro() {
		String rawQuery;

		rawQuery = "select _id, codpro from producto";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		ArrayList<DBProductos> lista_productos = new ArrayList<DBProductos>();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			DBProductos dbproductos = new DBProductos();
			dbproductos.setCodpro(cur.getString(1));

			lista_productos.add(dbproductos);
			cur.moveToNext();
		}
		cur.close();
		db.close();
		return lista_productos;
	}

	public ArrayList<DBProductos> getCodproxSubFamilia(String sub_familia,
			String pistaBusqueda) {

		String rawQuery;
		Log.w("CARGARPRECIOS() ", sub_familia + "-");
		if (pistaBusqueda != null) {
			rawQuery = "select _id, codpro from producto where sub_familia='"
					+ sub_familia + "' and despro like '%" + pistaBusqueda
					+ "%'";
		Log.d(" consulta:"," "+rawQuery);

		} else {
			rawQuery = "select _id, codpro from producto where sub_familia='"
					+ sub_familia + "' ";
			Log.d(" consulta :"," "+rawQuery);

		}
		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		ArrayList<DBProductos> lista_productos = new ArrayList<DBProductos>();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			DBProductos dbproductos = new DBProductos();
			dbproductos.setCodpro(cur.getString(1));

			lista_productos.add(dbproductos);
			cur.moveToNext();
		}
		Log.w("CARGARPRECIOS() ", "cantidad " + lista_productos.size());
		cur.close();
		db.close();
		return lista_productos;
	}

	public Cursor getProductosCursor() {
		SQLiteDatabase mDb = getReadableDatabase();

		String rawQuery;

		rawQuery = "select _id, despro from producto";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);

		if (cur != null) {
			cur.moveToFirst();
		}
		// return mCursor;
		return cur;
	}

	public Cursor getProductosCursorxFamilia(String familia,
			String pistaBusqueda) {
		SQLiteDatabase mDb = getReadableDatabase();

		String rawQuery;
		Log.w("PRODUCTOS_ADAPTER", familia + "-");
		if (pistaBusqueda != null) {
			rawQuery = "select _id, despro from producto where sub_familia='"
					+ familia + "' and despro like '%" + pistaBusqueda + "%'";
		} else {
			rawQuery = "select _id, despro from producto where sub_familia='"
					+ familia + "'";

		}
		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);

		if (cur != null) {
			cur.moveToFirst();
		}
		// return mCursor;
		Log.w("PRODUCTOS_ADAPTER_CURSOS", "cantidad: " + cur.getCount());
		return cur;
	}

	public ArrayList<DBProductos> getProductosxCodpro(String codpro) {
		String rawQuery;

		rawQuery = "select * from producto where codpro='" + codpro + "'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		ArrayList<DBProductos> lista_productos = new ArrayList<DBProductos>();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			DBProductos dbproductos = new DBProductos();

			dbproductos.setCodpro(cur.getString(1));
			dbproductos.setDespro(cur.getString(2));
			dbproductos.setAbrevpro(cur.getString(3));
			dbproductos.setGrupo(cur.getString(4));
			dbproductos.setFamilia(cur.getString(5));
			dbproductos.setSub_familia(cur.getString(6));
			dbproductos.setEan13(cur.getString(7));
			dbproductos.setCod_rapido(cur.getString(8));
			dbproductos.setCodunimed(cur.getString(9));
			dbproductos.setCodunimed_almacen(cur.getString(10));
			dbproductos.setFactor_conversion(cur.getDouble(11));
			dbproductos.setAfecto(cur.getString(12));
			dbproductos.setEstado(cur.getString(13));
			dbproductos.setPeso(cur.getDouble(14));
			dbproductos.setFoto(cur.getString(15));

			lista_productos.add(dbproductos);
			cur.moveToNext();
		}
		cur.close();
		db.close();
		return lista_productos;
	}

	public void updateIngresos(String secuencia, String secitm, String flag) {
		String where = "secuencia = ? and secitm = ?";
		String[] args = { secuencia, secitm };

		try {

			SQLiteDatabase db = getWritableDatabase();

			ContentValues reg = new ContentValues();
			reg.put("flag", flag);

			db.update("ingresos", reg, where, args);
			db.close();

			Log.i("CAMBIAR FLAG INGRESOS", "flag cambiado a: " + flag);

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

	}

	public int obtenerPedidosXCodcli(String codcli, String sitio_enfa) {
		Log.w("CODCLI_SITIO", codcli + "__" + sitio_enfa);
		int i = 0;
		String rawQuery = "Select estado, flag, cod_noventa from pedido_cabecera  where cod_cli='"
				+ codcli + "' and sitio_enfa=" + sitio_enfa;
		rawQuery = "Select estado, flag, cod_noventa from pedido_cabecera  where cod_cli='"
				+ codcli + "' and sitio_enfa= "+sitio_enfa+" and cod_noventa != '"+GlobalVar.CODIGO_VISITA_CLIENTE+"'";
		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);

		//cur.moveToFirst();
		if (cur.moveToFirst()) {
			do {
				if (cur.getString(0).equals("G")) {
					i = 1;
				}
				else if (cur.getString(0).equals("A")) {
					i = 2;
				}
			} while (cur.moveToNext());

		}
		cur.close();
		db.close();
		return i;
	}

	public String getFotoxNombre(String name) {
		// TODO Auto-generated method stub
		String rawQuery;
		String foto = "";
		rawQuery = "Select foto from producto where despro='" + name + "'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);

		cur.moveToFirst();
		if (cur.moveToFirst()) {
			do {
				foto = cur.getString(0);
			} while (cur.moveToNext());
		}
		cur.close();
		db.close();

		return foto;
	}

	public int getCantidadDatos_pedido_cabecera(String oc_numero) {

		String rawQuery;

		rawQuery = "select count(*) from pedido_cabecera where flag='N' and oc_numero='"
				+ oc_numero + "'";

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

	public int getCantidadDatos_pedido_detalle(String oc_numero) {

		String rawQuery;

		rawQuery = "select count(*) from pedido_detalle where flag='N' and oc_numero='"
				+ oc_numero + "'";

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

	public ArrayList<DBPedido_Cabecera> getPedido_cabeceraXflag(String oc_numero) {

		String rawQuery;

		rawQuery = "select * from pedido_cabecera where flag='N' and oc_numero='"
				+ oc_numero + "'";

		ArrayList<DBPedido_Cabecera> lista = new ArrayList<DBPedido_Cabecera>();

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		cur.moveToFirst();

		if (cur.moveToFirst()) {

			do {
				DBPedido_Cabecera item = new DBPedido_Cabecera();
				item.setOc_numero(cur.getString(0));
				item.setSitio_enfa(cur.getString(1));
				item.setMonto_total(cur.getString(2));
				item.setValor_igv(cur.getString(3));
				item.setMoneda(cur.getString(4));
				item.setFecha_oc(cur.getString(5));
				item.setFecha_mxe(cur.getString(6));
				item.setCond_pago(cur.getString(7));
				item.setCod_cli(cur.getString(8));
				item.setCod_emp(cur.getString(9));
				item.setEstado(cur.getString(10));
				item.setUsername(cur.getString(11));
				item.setRuta(cur.getString(12));
				item.setObserv(cur.getString(13));
				item.setCod_noventa(cur.getInt(14));
				item.setPeso_total(cur.getString(15));
				item.setFlag(cur.getString(16));
				item.setLatitud(cur.getString(17));
				item.setLongitud(cur.getString(18));
				item.setCodigo_familiar(cur.getString(19));
				lista.add(item);
				Log.w("GetPedidoCabecera_x_Flag",
						"codfmiliar:" + cur.getString(19));
			} while (cur.moveToNext());

		}

		cur.close();
		db.close();

		return lista;

	}

	public DBPedido_Cabecera getPedido_cabecera(String oc_numero) {

		String rawQuery;

		rawQuery = "select * from pedido_cabecera where oc_numero='"
				+ oc_numero + "'";

		DBPedido_Cabecera item =null;

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		cur.moveToFirst();

		if (cur.moveToFirst()) {

			do {
				item=new DBPedido_Cabecera();
				item.setOc_numero(cur.getString(0));
				item.setSitio_enfa(cur.getString(1));
				item.setMonto_total(cur.getString(2));
				item.setPercepcion_total(cur.getString(3));
				item.setValor_igv(cur.getString(4));
				item.setMoneda(cur.getString(5));
				item.setFecha_oc(cur.getString(6));
				item.setFecha_mxe(cur.getString(7));
				item.setCond_pago(cur.getString(8));
				item.setCod_cli(cur.getString(9));
				item.setCod_emp(cur.getString(10));
				item.setEstado(cur.getString(11));
				item.setUsername(cur.getString(12));
				item.setRuta(cur.getString(13));
				item.setObserv(cur.getString(14));
				item.setCod_noventa(cur.getInt(15));
				item.setPeso_total(cur.getString(16));
				item.setFlag(cur.getString(17));
				item.setLatitud(cur.getString(18));
				item.setLongitud(cur.getString(19));
				item.setCodigo_familiar(cur.getString(20));
				item.setTipoRegistro(cur.getString(cur.getColumnIndex("tipoRegistro")));

				Log.w("GetPedidoCabecera_x_Flag",
						"codfmiliar:" + cur.getString(19));
			} while (cur.moveToNext());

		}

		cur.close();
		db.close();

		return item;

	}

	public ArrayList<DBPedido_Detalle> getPedido_detalleXflag(String oc_numero) {

		String rawQuery;

		rawQuery = "select * from pedido_detalle where flag='N' and oc_numero='"
				+ oc_numero + "'";

		ArrayList<DBPedido_Detalle> lista = new ArrayList<DBPedido_Detalle>();

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		cur.moveToFirst();

		if (cur.moveToFirst()) {

			do {
				DBPedido_Detalle dbdetalle = new DBPedido_Detalle();
				dbdetalle.setOc_numero(cur.getString(0));
				dbdetalle.setEan_item(cur.getString(1));
				dbdetalle.setCip(cur.getString(2));
				dbdetalle.setPrecio_bruto(cur.getString(3));
				dbdetalle.setPrecio_neto(cur.getString(4));
				dbdetalle.setCantidad(cur.getInt(5));
				dbdetalle.setTipo_producto(cur.getString(6));
				dbdetalle.setUnidad_medida(cur.getString(7));
				dbdetalle.setPeso_bruto(cur.getString(8));
				dbdetalle.setFlag(cur.getString(9));
				dbdetalle.setCod_politica(cur.getString(10));

				lista.add(dbdetalle);

			} while (cur.moveToNext());

		}

		cur.close();
		db.close();

		return lista;

	}

	public ArrayList<DBPedido_Detalle> getPedido_detalles(String oc_numero) {
		String rawQuery;

		rawQuery = "select * from pedido_detalle where oc_numero='" + oc_numero+ "'";

		ArrayList<DBPedido_Detalle> lista = new ArrayList<DBPedido_Detalle>();
		SQLiteDatabase db = getReadableDatabase();

		Cursor cur = db.rawQuery(rawQuery, null);
		cur.moveToFirst();

		if (cur.moveToFirst()) {

			do {
				DBPedido_Detalle dbdetalle = new DBPedido_Detalle();
				dbdetalle.setOc_numero(cur.getString(0));
				dbdetalle.setEan_item(cur.getString(1));
				dbdetalle.setCip(cur.getString(2));
				dbdetalle.setItem(cur.getInt(cur.getColumnIndex("item")));
				dbdetalle.setPrecio_bruto(cur.getString(3));
				dbdetalle.setPrecio_neto(cur.getString(4));
				dbdetalle.setPercepcion(cur.getString(5));
				dbdetalle.setCantidad(cur.getInt(6));
				dbdetalle.setTipo_producto(cur.getString(7));
				dbdetalle.setUnidad_medida(cur.getString(8));
				dbdetalle.setPeso_bruto(cur.getString(9));
				dbdetalle.setFlag(cur.getString(10));
				dbdetalle.setCod_politica(cur.getString(11));
				dbdetalle.setSec_promo(cur.getString(12));
				dbdetalle.setItem_promo(Integer.parseInt(cur.getString(13)));
				dbdetalle.setAgrup_promo(Integer.parseInt(cur.getString(14)));
				dbdetalle.setPrecioLista(cur.getString(16));
				dbdetalle.setLote(""+cur.getString(17));
				dbdetalle.setDescuento(cur.getString(18));
				dbdetalle.setMotivoDevolucion(cur.getString(19));
				dbdetalle.setExpectativa(cur.getString(20));
				dbdetalle.setEnvase(cur.getString(21));
				dbdetalle.setContenido(cur.getString(22));
				dbdetalle.setProceso(cur.getString(23));
				dbdetalle.setObservacionDevolucion(cur.getString(24));
				dbdetalle.setTipoDocumento(cur.getString(25));
				dbdetalle.setSerieDevolucion(cur.getString(26));
				dbdetalle.setNumeroDevolucion(cur.getString(27));
				dbdetalle.setPorcentaje_desc(cur.getDouble(cur.getColumnIndex("porcentaje_desc")));
				dbdetalle.setPorcentaje_desc_extra(cur.getDouble(cur.getColumnIndex("porcentaje_desc_extra")));
				dbdetalle.setSec_promo_prioridad(cur.getInt(cur.getColumnIndex("sec_promo_prioridad")));
				dbdetalle.setItem_promo_prioridad(cur.getInt(cur.getColumnIndex("item_promo_prioridad")));
				Log.d(TAG, "setPrecio_bruto:"+cur.getString(3));
				Log.d(TAG, "setPrecio_neto:"+cur.getString(4));
				lista.add(dbdetalle);

			} while (cur.moveToNext());

		}
		cur.close();
		db.close();
		return lista;
	}

	public ArrayList<ListaCtaIngresos> getCtas_IngresosxCliente() {
		String rawQuery;

		// rawQuery=
		// "Select * from cta_ingresos  where cta_ingresos.saldo > 0 GROUP BY codcli";
		// rawQuery= "Select * from cta_ingresos where GROUP BY codcli";
		rawQuery = "select  SUM(total), codcli, COUNT(codcli) from cta_ingresos group by codcli";
		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		ArrayList<ListaCtaIngresos> dbcta_ingresos = new ArrayList<ListaCtaIngresos>();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			ListaCtaIngresos lista = new ListaCtaIngresos();
			lista.setTotal(cur.getString(0));
			lista.setCodcli(cur.getString(1));
			lista.setCantidad(cur.getString(2));

			dbcta_ingresos.add(lista);
			cur.moveToNext();
		}
		cur.close();
		db.close();
		return dbcta_ingresos;
	}

	public ArrayList<HashMap<String, String>> getCtas_IngresosxCliente2() {
		String rawQuery;

		// rawQuery =
		// "select  SUM( case coddoc when '04' then -total else total end), codcli, COUNT(codcli),"
		// +
		// "ifnull((select nomcli from cliente where codcli=cta_ingresos.codcli),'sin nombre'),"
		// +
		// "SUM(acuenta),SUM(case coddoc when '04' then -saldo else saldo end) from cta_ingresos group by codcli";

		/*
		 * rawQuery =
		 * "select SUM( case coddoc when '04' then -total else total end), " +
		 * "cta_ingresos.codcli, COUNT(cta_ingresos.codcli),ifnull(cliente.nomcli,'Sin nombre'),"
		 * +
		 * "SUM(acuenta),SUM(case coddoc when '04' then -saldo else saldo end),"
		 * +
		 * "SUM(case coddoc when '04' then -saldo_virtual else saldo_virtual end) from cta_ingresos "
		 * +
		 * "left join cliente on cta_ingresos.codcli=cliente.codcli group by cta_ingresos.codcli,"
		 * + "cliente.nomcli order by cliente.nomcli";
		 */

		String fechaConfig = getFecha2().trim();

		rawQuery = "select "
				+ "SUM(case coddoc when '04' then -cta_ingresos.total else cta_ingresos.total end),"
				+ "cta_ingresos.codcli,"
				+ "COUNT(cta_ingresos.codcli),"
				+ "ifnull(cliente.nomcli,'Sin nombre'),"
				+ "ifnull((select SUM(acuenta) from ingresos where secuencia in(select secuencia from cta_ingresos where codcli=cliente.codcli) and secitm like '%M%' and estado not in('A','L')),0),"
				+ "SUM(case coddoc when '04' then -cta_ingresos.saldo else cta_ingresos.saldo end),"
				+ "SUM(case coddoc when '04' then -cta_ingresos.saldo_virtual else cta_ingresos.saldo_virtual end) "
				+ "from cta_ingresos "
				+ "left join cliente on cta_ingresos.codcli=cliente.codcli "
				+ "group by cta_ingresos.codcli,cliente.codcli,cliente.nomcli "
				+ "order by cliente.nomcli";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);

		ArrayList<HashMap<String, String>> lista = new ArrayList<HashMap<String, String>>();

		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			HashMap<String, String> obj = new HashMap<String, String>();
			// obj.put("codcli", cur.getString(1));
			obj.put("cliente", cur.getString(3));
			obj.put("total",
					""
							+ GlobalFunctions.redondear(Double.parseDouble(cur
									.getString(0))));
			obj.put("cantidad", "" + cur.getString(2));
			obj.put("total_acuenta",
					""
							+ GlobalFunctions.redondear(Double.parseDouble(cur
									.getString(4))));
			obj.put("total_saldo",
					""
							+ GlobalFunctions.redondear(Double.parseDouble(cur
									.getString(5))));
			obj.put("total_saldo_virtual",
					""
							+ GlobalFunctions.redondear(Double.parseDouble(cur
									.getString(6))));

			Log.w("Cobranzas codcli-nomcli",
					cur.getString(1) + "-" + cur.getString(3));

			lista.add(obj);
			cur.moveToNext();
		}
		cur.close();
		db.close();
		return lista;
	}

	private String getTotalDeuda(String codcli, int tipo) {

		String rawQuery1, rawQuery2;

		rawQuery1 = "Select  SUM(total) from  cta_ingresos  where codcli='"
				+ codcli + "'";
		rawQuery2 = "Select  SUM(acuenta) from  cta_ingresos  where codcli='"
				+ codcli + "'";

		String total = "";
		String acuenta = "";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur1 = db.rawQuery(rawQuery1, null);
		Cursor cur2 = db.rawQuery(rawQuery2, null);
		cur1.moveToFirst();

		if (cur1.moveToFirst()) {

			do {
				total = cur1.getString(0);

			} while (cur1.moveToNext());

		}

		cur1.close();
		cur2.moveToFirst();
		if (cur2.moveToFirst()) {

			do {
				acuenta = cur2.getString(0);

			} while (cur2.moveToNext());

		}
		cur2.close();
		db.close();

		if (tipo == 1) {
			return total;
		} else
			return acuenta;

	}

	public int getCantidad_pedido_devolucion(String oc_numero) {

		String rawQuery;

		rawQuery = "select count(*) from pedido_devolucion where oc_numero='"
				+ oc_numero + "'";

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

	public String getClientesxMotivoNoventa(String codcli) {
		// TODO Auto-generated method stub
		String rawQuery;

		rawQuery = "select cod_noventa from pedido_cabecera where cod_cli='"
				+ codcli + "'";

		String cant_datos = "";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		cur.moveToFirst();

		if (cur.moveToFirst()) {

			do {
				cant_datos = cur.getString(0);
			} while (cur.moveToNext());

		}
		if (cant_datos == null || cant_datos.trim().length() == 0) {
			cant_datos = "v";
		}

		cur.close();
		db.close();

		return cant_datos;
	}

	public String obtenerPedidosXCodcli2(String codcli) {
		String rawQuery;

		rawQuery = "Select cod_noventa from pedido_cabecera  where cod_cli='"
				+ codcli + "'";

		String cant_datos = "";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		cur.moveToFirst();

		if (cur.moveToFirst()) {

			do {
				cant_datos = cur.getString(0);
			} while (cur.moveToNext());

		}
		if (cant_datos == null || cant_datos.trim().length() == 0) {
			cant_datos = "100";
		}
		cur.close();
		db.close();

		return cant_datos;
	}

	public String getFechaEvento(String codcli) {
		String rawQuery;
		rawQuery = "Select fecha_oc from pedido_cabecera  where cod_cli='"
				+ codcli + "'";
		String fecha = "";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		cur.moveToFirst();
		if (cur.moveToFirst()) {
			do {
				fecha = cur.getString(0);
			} while (cur.moveToNext());

		}
		if (fecha == null || fecha.trim().length() == 0) {
			fecha = "v";
		}
		cur.close();
		db.close();

		return fecha;
	}

	public ArrayList<DBClientes> getClientesxEvento() {
		String rawQuery;

		rawQuery = "select  codcli,nomcli,ruccli,stdcli,comprobante,email,tipo_documento, "
				+ " tipo_cliente, tiempo_credito,limite_credito,persona,afecto, sec_visita, condicion_venta, latitud, longitud from  cliente  inner join pedido_cabecera on cliente.codcli=pedido_cabecera.cod_cli ";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		ArrayList<DBClientes> dbclientes = new ArrayList<DBClientes>();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			DBClientes dbc = new DBClientes();
			dbc.setCodcli(cur.getString(0));
			dbc.setNomcli(cur.getString(1));
			dbc.setRuccli(cur.getString(2));

			dbc.setStdcli(cur.getString(3));
			dbc.setComprobante(cur.getString(4));

			dbc.setEmail(cur.getString(5));
			dbc.setTipo_documento(cur.getInt(6));
			dbc.setTipo_cliente(cur.getInt(7));
			dbc.setTiempo_credito(cur.getInt(8));
			dbc.setLimite_credito(cur.getFloat(9));
			dbc.setPersona(cur.getString(10));
			dbc.setAfecto(cur.getString(11));

			dbc.setCondicion_venta(cur.getString(12));

			dbclientes.add(dbc);
			cur.moveToNext();
		}
		cur.close();
		db.close();
		return dbclientes;

	}

	public ArrayList<DBUsuarios> obtenerUsuariosxC2DM() {
		String rawQuery;

		rawQuery = "select * from usuarios";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		ArrayList<DBUsuarios> dbusuarios = new ArrayList<DBUsuarios>();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			DBUsuarios dbr = new DBUsuarios();
			dbr.setUsecod(cur.getInt(0));
			dbr.setUsepas(cur.getString(1));
			dbr.setUsenam(cur.getString(2));
			dbr.setUseusr(cur.getString(3));
			dbr.setUsesgl(cur.getString(4));
			dbr.setCodigoc2dm(cur.getString(5));
			dbusuarios.add(dbr);
			cur.moveToNext();
		}
		cur.close();
		db.close();
		return dbusuarios;
	}

	public void cambiar_estado_pedido_devolucion(String oc_numero,
			String codpro, String estado) {

		String where = "oc_numero = ? and cip=?";
		String[] args = { oc_numero, codpro };

		SQLiteDatabase db = getWritableDatabase();

		try {

			ContentValues reg = new ContentValues();
			reg.put("flag", estado);

			db.update("pedido_devolucion", reg, where, args);

			Log.i("CAMBIAR ESTADO PEDIDO DEVOLUCION", "estado cambiado");

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			db.close();
		}

	}

	public DBPedido_Cabecera obtener_datos_PedCab_modificar(String oc_numero) {

		String rawQuery = "select sitio_enfa,fecha_mxe,cond_pago,cod_cli,observacion,codigo_familiar from pedido_cabecera where oc_numero = '"
				+ oc_numero + "'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(rawQuery, null);
		DBPedido_Cabecera pc = new DBPedido_Cabecera();

		cursor.moveToFirst();
		if (cursor.moveToFirst()) {
			do {
				pc.setSitio_enfa(cursor.getString(0));
				pc.setFecha_mxe(cursor.getString(1));
				pc.setCond_pago(cursor.getString(2));
				pc.setCod_cli(cursor.getString(3));
				pc.setObserv(cursor.getString(4));
				pc.setCodigo_familiar(cursor.getString(5));
			} while (cursor.moveToNext());

		}
		cursor.close();
		db.close();

		return pc;
	}
	
	

	
	public String getStockxProducto(String codpro, String codAlm) {
		// TODO Auto-generated method stub
		String rawQuery;
		rawQuery = "select (stock-xtemp) as stock from mta_kardex where codpro='"
				+ codpro + "' and codalm='" + codAlm + "'";
		Log.d("ALERT-", " ." + rawQuery);
		String stock = "";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		cur.moveToFirst();
		if (cur.moveToFirst()) {
			do {

				stock = cur.getString(0);
				Log.d("ALERT2P :", " ." + stock);
			} while (cur.moveToNext());

		}
		if (stock == null || stock.trim().length() == 0) {
			stock = "0";
		}
		cur.close();
		db.close();

		return stock;
	}

	public ArrayList<DBLocales> getLocales() {
		// TODO Auto-generated method stub
		String rawQuery = "select * from locales";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		ArrayList<DBLocales> dblocales = new ArrayList<DBLocales>();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			DBLocales dbr = new DBLocales();
			dbr.setId_local(cur.getString(0));
			dbr.setDes_local(cur.getString(1));
			dbr.setDireccion(cur.getString(2));
			dbr.setCoddep(cur.getString(3));
			dbr.setCodprov(cur.getString(4));
			dbr.setUbigeo(cur.getString(5));
			dbr.setTlf(cur.getString(6));
			dbr.setEmail(cur.getString(7));
			dbr.setEstado(cur.getString(8));
			dbr.setBg_color(cur.getInt(9));
			dbr.setTxt_color(cur.getInt(10));
			dbr.setLatitud(cur.getString(11));
			dbr.setLongitud(cur.getString(12));
			dblocales.add(dbr);
			cur.moveToNext();
		}
		cur.close();
		db.close();
		return dblocales;
	}

	public String[] getPlacas() {
		String rawQuery;

		rawQuery = "select placa_vehiculo from vehiculo";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(rawQuery, null);

		String[] placas;

		if (cursor.getCount() != 0) {

			placas = new String[cursor.getCount()];
			cursor.moveToFirst();

			if (cursor.moveToFirst()) {
				int i = 0;
				do {
					placas[i] = cursor.getString(0);
					i++;
				} while (cursor.moveToNext());
			}

		} else {
			placas = new String[] { "sin datos" };
		}

		cursor.close();
		db.close();

		return placas;

	}

	public ArrayList<DB_Factura1> getFactura1() {
		String rawQuery = "select * from factura1";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		ArrayList<DB_Factura1> dbfactura1 = new ArrayList<DB_Factura1>();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			DB_Factura1 dbr = new DB_Factura1();
			dbr.setSecuencia(cur.getInt(0));
			dbr.setCodcli(cur.getString(1));
			dbr.setCodforpag(cur.getString(2));
			dbr.setTiempo_credito(cur.getInt(3));
			dbr.setTotal(cur.getString(4));
			dbr.setAcuenta(cur.getString(5));
			dbr.setIgv(cur.getString(6));
			dbr.setSaldo(cur.getString(7));
			dbr.setFecfac(cur.getString(8));
			dbr.setEstado(cur.getString(9));
			dbr.setNumero_guia(cur.getString(10));
			dbr.setNumero_factura(cur.getString(11));
			dbr.setSub_total(cur.getString(12));
			dbr.setSerie_guia(cur.getString(8));
			dbr.setSerie_factura(cur.getString(9));
			dbr.setCodven(cur.getString(10));
			dbr.setTipo_documento(cur.getString(11));
			dbr.setDeposito(cur.getString(12));
			dbr.setId_local(cur.getString(9));
			dbr.setUsecod(cur.getInt(10));

			dbfactura1.add(dbr);
			cur.moveToNext();
		}
		cur.close();
		db.close();
		return dbfactura1;
	}

	public void actualizarFlagDeposito(String flag, String secuencia) {

		String where = "secuencia = ?";
		String[] args = { secuencia };

		try {

			SQLiteDatabase db = getWritableDatabase();

			ContentValues reg = new ContentValues();
			reg.put("BI_DEPO_FLAG", flag);

			db.update("depositos", reg, where, args);
			db.close();

			Log.i("DEPOSITOS", "Flag modificado: " + secuencia + "- flag:"
					+ flag);

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public ArrayList<DBDepositos> getDepositos() {
		// TODO Auto-generated method stub

		String rawQuery;

		rawQuery = "select * from depositos where estado <> 'A'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		ArrayList<DBDepositos> lista_depositos = new ArrayList<DBDepositos>();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			DBDepositos dbdepositos = new DBDepositos();
			dbdepositos.setSecuencia(cur.getString(0));
			dbdepositos.setId_banco(cur.getString(1));
			dbdepositos.setId_num_cta(cur.getInt(2));
			dbdepositos.setFecha(cur.getString(3));
			dbdepositos.setNum_ope(cur.getString(4));
			dbdepositos.setMoneda(cur.getString(5));
			dbdepositos.setMonto(cur.getString(6));
			dbdepositos.setEstado(cur.getString(7));
			dbdepositos.setCodven(cur.getString(8));
			dbdepositos.setDT_DEPO_FECHASERVIDOR(cur.getString(9));
			dbdepositos.setBI_DEPO_FLAG(cur.getString(10));

			lista_depositos.add(dbdepositos);
			cur.moveToNext();
		}
		cur.close();
		db.close();
		return lista_depositos;// d
	}

	public ArrayList<DBDepositos> getDepositosxSec(String secuencia) {
		// TODO Auto-generated method stub
		String rawQuery;

		rawQuery = "select * from depositos where secuencia='" + secuencia
				+ "'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		ArrayList<DBDepositos> lista_depositos = new ArrayList<DBDepositos>();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			DBDepositos dbdepositos = new DBDepositos();
			dbdepositos.setSecuencia(cur.getString(0));
			dbdepositos.setId_banco(cur.getString(1));
			dbdepositos.setId_num_cta(cur.getInt(2));
			dbdepositos.setFecha(cur.getString(3));
			dbdepositos.setNum_ope(cur.getString(4));
			dbdepositos.setMoneda(cur.getString(5));
			dbdepositos.setMonto(cur.getString(6));
			dbdepositos.setEstado(cur.getString(7));
			dbdepositos.setCodven(cur.getString(8));
			dbdepositos.setDT_DEPO_FECHASERVIDOR(cur.getString(9));
			dbdepositos.setBI_DEPO_FLAG(cur.getString(10));
			dbdepositos.setTXT_DEPO_FECHA_REGISTRO(cur.getString(11));

			lista_depositos.add(dbdepositos);
			cur.moveToNext();
		}
		cur.close();
		db.close();
		return lista_depositos;// d
	}

	public ArrayList<DB_DireccionClientes> getDirecciones() {
		// TODO Auto-generated method stub
		String rawQuery;

		rawQuery = "select * from direccion_cliente";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		ArrayList<DB_DireccionClientes> lista_direcciones = new ArrayList<DB_DireccionClientes>();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			DB_DireccionClientes dbdireccion = new DB_DireccionClientes();
			dbdireccion.setCodcli(cur.getString(0));
			dbdireccion.setItem(cur.getString(1));
			dbdireccion.setDireccion(cur.getString(2));
			dbdireccion.setTelefono(cur.getString(3));
			dbdireccion.setCoddep(cur.getString(4));
			dbdireccion.setCodprv(cur.getString(5));
			dbdireccion.setUbigeo(cur.getString(6));
			dbdireccion.setDes_corta(cur.getString(7));

			dbdireccion.setLatitud(cur.getString(8));
			dbdireccion.setLongitud(cur.getString(9));
			if (cur.getString(9).length() > 2) {
				Log.w("LATITUD", cur.getString(9) + "");
			}

			lista_direcciones.add(dbdireccion);
			cur.moveToNext();
		}
		cur.close();
		db.close();
		return lista_direcciones;// d
	}

	public ArrayList<DB_DireccionClientes> getDireccionesxCliente(String codcli) {
		// TODO Auto-generated method stub
		String rawQuery;

		rawQuery = "select * from direccion_cliente where codcli='" + codcli
				+ "'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		ArrayList<DB_DireccionClientes> lista_direcciones = new ArrayList<DB_DireccionClientes>();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			DB_DireccionClientes dbdireccion = new DB_DireccionClientes();
			dbdireccion.setCodcli(cur.getString(0));
			dbdireccion.setItem(cur.getString(1));
			dbdireccion.setDireccion(cur.getString(2));
			dbdireccion.setTelefono(cur.getString(3));
			dbdireccion.setCoddep(cur.getString(4));
			dbdireccion.setCodprv(cur.getString(5));
			dbdireccion.setUbigeo(cur.getString(6));
			dbdireccion.setDes_corta(cur.getString(7));

			dbdireccion.setLatitud(cur.getString(8));
			dbdireccion.setLongitud(cur.getString(9));
			if (cur.getString(9).length() > 2) {
				Log.w("LATITUD", cur.getString(9) + "");
			}

			lista_direcciones.add(dbdireccion);
			cur.moveToNext();
		}
		cur.close();
		db.close();
		return lista_direcciones;// d
	}

	public String obtenerCodPro_xNombre(String nomprod) {
		// TODO Auto-generated method stub
		String rawQuery;
		rawQuery = "Select codpro from producto where despro='" + nomprod + "'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		cur.moveToFirst();
		String codigo = "";
		if (cur.moveToFirst()) {

			do {
				codigo = cur.getString(0);
			} while (cur.moveToNext());

		}
		cur.close();
		db.close();
		return codigo;
	}

	public String[] getFamiliaxCodpro(String codpro) {

		String rawQuery = "select familia from familia where secuencia='"
				+ codpro + "'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(rawQuery, null);

		String[] datos = new String[1];

		if (cursor.moveToFirst()) {
			int i = 0;
			do {
				datos[i] = cursor.getString(0);

			} while (cursor.moveToNext());
		}

		cursor.close();
		db.close();

		return datos;

	}

	public String[] getSubFamiliaxCodigo(String codpro) {

		String rawQuery = "select des_subfam from sub_familia where sub_familia='"
				+ codpro + "'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(rawQuery, null);

		String[] datos = new String[1];

		if (cursor.moveToFirst()) {
			int i = 0;
			do {
				datos[i] = cursor.getString(0);

			} while (cursor.moveToNext());
		}

		if (datos[0] == null || datos[0].length() == 0) {
			datos[0] = "0";
		}
		cursor.close();
		db.close();

		return datos;

	}

	public ArrayList<DBFamilia> getFamilia() {

		String rawQuery = "select * from familia";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		ArrayList<DBFamilia> lista_familias = new ArrayList<DBFamilia>();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			DBFamilia db_familia = new DBFamilia();
			db_familia.setSecuencia(cur.getString(0));
			db_familia.setFamilia(cur.getString(1));
			lista_familias.add(db_familia);
			cur.moveToNext();
		}
		cur.close();
		db.close();

		return lista_familias;
	}

	public ArrayList<DBSub_Familia> getSubFamilia(String familia) {

		String rawQuery = "select sub_familia,des_subfam from sub_familia where secuencia=?";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, new String[] { familia });
		ArrayList<DBSub_Familia> lista_Subfamilias = new ArrayList<DBSub_Familia>();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			DBSub_Familia subFamilia = new DBSub_Familia();
			subFamilia.setSub_familia(cur.getString(0));
			subFamilia.setDes_familia(cur.getString(1));
			lista_Subfamilias.add(subFamilia);
			cur.moveToNext();
		}
		cur.close();
		db.close();

		return lista_Subfamilias;
	}

	public ArrayList<ListaPrecios> getPreciosxCodpro(String codpro) {
		// TODO Auto-generated method stub
		String rawQuery;

		rawQuery = "select prepro_unidad from politica_precio2 where codpro='"
				+ codpro + "'";

		Log.d("politica ",""+rawQuery);
		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		ArrayList<ListaPrecios> lista_precios = new ArrayList<ListaPrecios>();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			ListaPrecios db_precios = new ListaPrecios();
			db_precios.setPrecio1(cur.getString(0));
			Log.d("politica ",""+db_precios.getPrecio1()+"---");
			
			lista_precios.add(db_precios);
			cur.moveToNext();
		}
		cur.close();
		db.close();
		return lista_precios;// d
	}

	public String getTelefonoxCliente(String codcli) {
		// TODO Auto-generated method stub
		String rawQuery;

		rawQuery = "select telefono from direccion_cliente where codcli='"
				+ codcli + "'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		String telefono = "";
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			telefono = (cur.getString(0));

			cur.moveToNext();
		}
		cur.close();
		db.close();
		return telefono;// d
	}

	public String obtenerMotivoxCodigo(int cod_noventa) {
		// TODO Auto-generated method stub
		String rawQuery;

		rawQuery = "select des_noventa from motivo_noventa where cod_noventa='"
				+ cod_noventa + "'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		String descripcion = "";
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			descripcion = (cur.getString(0));

			cur.moveToNext();
		}
		cur.close();
		db.close();
		return descripcion;// d
	}

	public int obtenerMotivoxCliente(String codcli, int item_direccion) {
		// TODO Auto-generated method stub
		String rawQuery;

		rawQuery = "select cod_noventa from pedido_cabecera where cod_cli='"
				+ codcli + "' and sitio_enfa=" + item_direccion
				+ " and oc_numero<>0";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		int descripcion = -1;
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			descripcion = (cur.getInt(0));

			cur.moveToNext();
		}
		cur.close();
		db.close();
		return descripcion;// d
	}

	public boolean existePedidoCabeceraXcodcli_item(String codcli, int item) {
		// TODO Auto-generated method stub
		String rawQuery;
		rawQuery = "select oc_numero from pedido_cabecera where cod_cli='"
				+ codcli + "' and sitio_enfa=" + item + "";
		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		boolean existe = false;
		cur.moveToFirst();

		while (!cur.isAfterLast()) {
			existe = true;
			cur.moveToNext();
		}

		cur.close();
		db.close();
		return existe;
	}

	public int obtenerMotivoxCliente2(String codcli) {
		// TODO Auto-generated method stub
		String rawQuery;

		rawQuery = "select cod_noventa from pedido_cabecera where cod_cli='"
				+ codcli + "' and oc_numero<>0";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		int descripcion = -1;
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			descripcion = (cur.getInt(0));

			cur.moveToNext();
		}
		cur.close();
		db.close();
		return descripcion;// d
	}

	public int obtenerCodNoVentaxOC(String oc_numero) {
		// TODO Auto-generated method stub
		String rawQuery;

		rawQuery = "select cod_noventa from pedido_cabecera where oc_numero='"
				+ oc_numero + "'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		int cod_no_venta = -1;
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			cod_no_venta = (cur.getInt(0));
			cur.moveToNext();
		}
		cur.close();
		db.close();
		return cod_no_venta;// d
	}

	public String obtenerDireccionxCliente2(String codcli, String sitio_enfa) {
		// TODO Auto-generated method stub
		String rawQuery;

		rawQuery = "select direccion from direccion_cliente where codcli ='" + codcli + "' and item = '" + sitio_enfa + "'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		String descripcion = "";
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			descripcion = (cur.getString(0));

			cur.moveToNext();
		}

		cur.close();
		db.close();
		return descripcion;// d
	}

	public String obtenerDireccionxOC(String oc_numero) {
		// TODO Auto-generated method stub
		String rawQuery;

		rawQuery = "select direccion from direccion_cliente where codcli = (select cod_cli from pedido_cabecera where oc_numero='"
				+ oc_numero
				+ "') and item = (select sitio_enfa from pedido_cabecera where oc_numero='"
				+ oc_numero + "')";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		String direccion = "";
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			direccion = (cur.getString(0));

			cur.moveToNext();
		}

		cur.close();
		db.close();
		return direccion;
	}

	public String obtenerCodigoDireccionxCliente2(String codcli) {
		// TODO Auto-generated method stub
		String rawQuery;

		rawQuery = "select item from direccion_cliente where codcli='" + codcli
				+ "'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		String descripcion = "";
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			descripcion = (cur.getString(0));

			cur.moveToNext();
		}

		cur.close();
		db.close();
		return descripcion;// d
	}

	public String obtenerOCxCliente(String codcli, int item_direccion) {
		// TODO Auto-generated method stub

		String rawQuery;

		rawQuery = "select oc_numero from pedido_Cabecera where cod_cli='"
				+ codcli + "' and sitio_enfa=" + item_direccion + "";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		String descripcion = "";
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			descripcion = (cur.getString(0));

			cur.moveToNext();
		}

		cur.close();
		db.close();

		return descripcion;
	}

	public void cambiarEstadoEliminados(String oc_numero) {
		// TODO Auto-generated method stub
		String rawQuery;

		rawQuery = "update pedido_detalle set ean_item='A' where oc_numero='"
				+ oc_numero + "'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);

		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			cur.moveToNext();
		}
		cur.close();
		db.close();

	}

	public void EliminarItemsxEstado(String oc_numero) {
		// TODO Auto-generated method stub

		String where = "ean_item=?";
		String[] args = { "E" };

		try {
			SQLiteDatabase db = getWritableDatabase();

			db.delete("pedido_detalle", where, args);
			db.close();

			Log.i("eliminar items por estado pedido_detalle",
					"pedido eliminado");

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void EliminarDetallePedido(String oc) {
		// TODO Auto-generated method stub
		String where = "oc_numero=?";
		String[] args = { oc };

		try {
			SQLiteDatabase db = getWritableDatabase();

			db.delete("pedido_detalle", where, args);
			db.close();

			Log.i("eliminar pedido_detalle", "pedido eliminado");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String obtenerOcxCliente(String codcli) {
		// TODO Auto-generated method stub
		String rawQuery;

		rawQuery = "select oc_numero from pedido_Cabecera where cod_cli='"+ codcli + "'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		String descripcion = "";
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			descripcion = (cur.getString(0));

			cur.moveToNext();
		}

		cur.close();
		db.close();

		return descripcion;
	}

	public String obtenerAcuentaTotalxCta(int secuencia) {
		// TODO Auto-generated method stub
		String rawQuery;

		rawQuery = "select SUM(monto_Afecto) from ingresos where secuencia='"
				+ secuencia + "' and ingresos.flag <> 'A'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		String descripcion = "0";
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			descripcion = (cur.getString(0));

			cur.moveToNext();
		}
		if (descripcion == null || descripcion.length() == 0) {
			descripcion = "0";
		}
		cur.close();
		db.close();

		return descripcion;
	}

	public String getEstadoPedido(String codcli) {
		// TODO Auto-generated method stub
		String rawQuery;

		rawQuery = "select estado from pedido_cabecera where cod_cli='"
				+ codcli + "'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		String descripcion = "0";
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			descripcion = (cur.getString(0));

			cur.moveToNext();
		}
		if (descripcion == null || descripcion.length() == 0) {
			descripcion = "0";
		}
		cur.close();
		db.close();

		return descripcion;
	}

	public ArrayList<DBIngresos> ObtenerDetalleCtaXCobrar2(String secuen) {
		// TODO Auto-generated method stub
		String rawQuery;

		rawQuery = "select * from ingresos where secuencia='"
				+ secuen
				+ "' and estado not in('A','L') order by substr(secitm,1,1) desc,substr(secitm,2) asc";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		ArrayList<DBIngresos> ingresos = new ArrayList<DBIngresos>();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			DBIngresos dbing = new DBIngresos();
			dbing.setSecuencia(cur.getString(0));
			dbing.setSecitm(cur.getString(1));
			dbing.setFecpag(cur.getString(2));
			dbing.setTotal(cur.getString(3));
			dbing.setAcuenta(cur.getString(4));
			dbing.setSaldo(cur.getString(5));
			dbing.setCodcue(cur.getString(6));
			dbing.setNumope(cur.getString(7));
			dbing.setCodforpag(cur.getString(8));
			dbing.setTipo_cambio(cur.getString(9));
			dbing.setCodmon(cur.getString(10));
			dbing.setMonto_afecto(cur.getString(11));
			dbing.setUsername(cur.getString(12));
			dbing.setFecoperacion(cur.getString(13));
			dbing.setFlag(cur.getString(14));

			ingresos.add(dbing);
			cur.moveToNext();
		}
		cur.close();
		db.close();
		return ingresos;
	}

	public String getCodmonxFactura(String numfactura) {
		// TODO Auto-generated method stub
		String rawQuery;

		rawQuery = "select codmon from cta_ingresos where numero_factura='"
				+ numfactura + "'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		String descripcion = "0";
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			descripcion = (cur.getString(0));

			cur.moveToNext();
		}
		if (descripcion == null || descripcion.length() == 0) {
			descripcion = "0";
		}
		cur.close();
		db.close();

		return descripcion;
	}

	public String getCambio(String Nombre) {
		// TODO Auto-generated method stub
		String rawQuery;

		rawQuery = "select valor from configuracion where nombre='" + Nombre
				+ "'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		String descripcion = "0";
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			descripcion = (cur.getString(0));

			cur.moveToNext();
		}
		if (descripcion == null || descripcion.length() == 0) {
			descripcion = "0";
		}
		cur.close();
		db.close();
		Log.d(TAG, "limite obtenido de la Base de datos: "+descripcion);
		return descripcion;
	}

	public String getFlagVendedor(String codven) {
		// TODO Auto-generated method stub
		String rawQuery;

		rawQuery = "select flg_modificaPrecio from vendedor where codven='"
				+ codven + "'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		String descripcion = "0";
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			descripcion = (cur.getString(0));

			cur.moveToNext();
		}
		if (descripcion == null || descripcion.length() == 0) {
			descripcion = "0";
		}
		cur.close();
		db.close();
		Log.w("FLAG VENDEDOR", "valor" + descripcion);
		return descripcion;
	}

	public int getContarClientesPedido(String codcli) {
		// TODO Auto-generated method stub
		String rawQuery;

		// rawQuery="select count(cod_cli) from pedido_cabecera where cod_cli='"+codcli+"'";
		rawQuery = "select count(cod_cli) from pedido_cabecera";
		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		int cantidad = 0;
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			cantidad = (cur.getInt(0));

			cur.moveToNext();
		}

		cur.close();
		db.close();

		return cantidad;
	}

	public void actualizarEstadoCabeceraPedido(String oc_numero, String estado) {

		String where = "oc_numero = ?";
		String[] args = { oc_numero };

		try {

			SQLiteDatabase db = getWritableDatabase();

			ContentValues reg = new ContentValues();
			reg.put("estado", estado);

			db.update("pedido_cabecera", reg, where, args);
			db.close();

			Log.i("PedidoCabecera actualizar estado",
					"pedido_cabecera actualizada" + oc_numero + " " + estado);

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

	}

	public boolean existeDetallePedido(String codpro, String oc_numero) {
		// TODO Auto-generated method stub
		String rawQuery;
		rawQuery = "select oc_numero from pedido_detalle where oc_numero='"
				+ oc_numero + "' and cip='" + codpro + "'";
		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		boolean existe = false;
		cur.moveToFirst();

		while (!cur.isAfterLast()) {
			existe = true;
			cur.moveToNext();
		}

		cur.close();
		db.close();
		return existe;
	}

	public String getCuota(String codven) {
		// TODO Auto-generated method stub
		String rawQuery;
		rawQuery = "select cuota from cuota_vendedor where codven='" + codven
				+ "' ;";
		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		String cuota = "";
		cur.moveToFirst();

		while (!cur.isAfterLast()) {
			cuota = cur.getInt(0) + "";
			cur.moveToNext();
		}

		cur.close();
		db.close();
		return cuota;
	}

	public String getAvance(String codven) {
		// TODO Auto-generated method stub
		String rawQuery;
		rawQuery = "select SUM(total) from registro_ventas1 where codven='"
				+ codven + "' and tipo_documento='02' ;";
		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		String cuota = "";
		cur.moveToFirst();

		while (!cur.isAfterLast()) {
			cuota = cur.getInt(0) + "";
			cur.moveToNext();
		}

		cur.close();
		db.close();
		return cuota;
	}

	public ArrayList<DBSub_Familia> getSubFamilias(String codfam) {
		// TODO Auto-generated method stub
		String rawQuery = "select * from sub_familia where secuencia='"
				+ codfam + "'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		ArrayList<DBSub_Familia> lista_familias = new ArrayList<DBSub_Familia>();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			DBSub_Familia db_familia = new DBSub_Familia();
			db_familia.setSecuencia(cur.getString(0));
			db_familia.setSub_familia(cur.getString(1));
			db_familia.setDes_familia(cur.getString(2));
			lista_familias.add(db_familia);
			cur.moveToNext();
		}
		cur.close();
		db.close();

		return lista_familias;
	}

	public void guardarObservacion(String obser, String codcli) {
		// TODO Auto-generated method stub
		try {
			SQLiteDatabase db = getWritableDatabase();

			ContentValues Nreg = new ContentValues();

			Nreg.put("oc_numero", "0");
			Nreg.put("sitio_enfa", "0");
			Nreg.put("monto_total", "0");
			Nreg.put("valor_igv", "0");
			Nreg.put("moneda", "0");
			Nreg.put("fecha_oc", "0");
			Nreg.put("fecha_mxe", "0");
			Nreg.put("cond_pago", "0");
			Nreg.put("cod_cli", codcli + "");
			Nreg.put("cod_emp", "0");
			Nreg.put("estado", "0");
			Nreg.put("username", "0");
			Nreg.put("ruta", "0");
			Nreg.put("observacion", obser);
			Nreg.put("cod_noventa", "0");
			Nreg.put("peso_total", "0");
			Nreg.put("flag", "0");
			Nreg.put("latitud", "0");
			Nreg.put("longitud", "0");

			db.insert("pedido_cabecera", null, Nreg);
			db.close();
			Log.i("PEDIDO_CABECERA", "OBSERVACION REGISTRADA");

		} catch (Exception e) {
			Log.i("PEDIDO_CABECERA", "Error OBSERVACION REGISTRO");
		}
	}

	public void guardarObservacion2(String obser, String codcli) {
		// TODO Auto-generated method stub
		String where = "codcli = ?";
		String[] args = { codcli };

		try {

			SQLiteDatabase db = getWritableDatabase();

			ContentValues reg = new ContentValues();
			reg.put("observacion", obser);

			db.update("cliente", reg, where, args);
			db.close();

			Log.i("Cliente", "campo observacion actualizado en tabla cliente");

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

	}

	public String getObservacionxCliente(String nomcli) {
		// TODO Auto-generated method stub
		String rawQuery;
		rawQuery = "select observacion from pedido_cabecera where cod_cli='"
				+ nomcli + "' and oc_numero='0' ;";
		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		String cuota = "";
		cur.moveToFirst();

		while (!cur.isAfterLast()) {
			cuota = cur.getString(0) + "";
			cur.moveToNext();
		}

		cur.close();
		db.close();
		return cuota;
	}

	public ItemProducto[] getProductosXean13(String codcli, String ean13) {
		// TODO Auto-generated method stub
		String rawQuery;
		Log.w("PRODUCTOS_EAN13", codcli + "-" + ean13);
		rawQuery = "select * from( select politica_precio2.secuencia, producto.codpro , producto.despro, politica_precio2.prepro, politica_precio2.prepro_unidad, producto.factor_conversion, producto.peso, ifnull(mta_kardex.stock-mta_kardex.xtemp,0) as stock, producto.cod_rapido "
				+ " from producto inner join politica_precio2 on producto.codpro = politica_precio2.codpro "
				+ "left join mta_kardex on mta_kardex.codpro = producto.codpro where politica_precio2.secuencia=0 "
				+ "and producto.ean13 like '%"
				+ ean13
				+ "%' union all select politica_precio2.secuencia, producto.codpro, producto.despro, politica_precio2.prepro, politica_precio2.prepro_unidad, "
				+ "producto.factor_conversion, producto.peso, ifnull(mta_kardex.stock-mta_kardex.xtemp,0) as stock, producto.cod_rapido from producto "
				+ "inner join politica_precio2 on producto.codpro = politica_precio2.codpro left "
				+ "join mta_kardex on mta_kardex.codpro = producto.codpro inner join politica_cliente "
				+ "on politica_precio2.secuencia=politica_cliente.sec_politica "
				+ "where politica_cliente.codcli='"
				+ codcli
				+ "' and producto.ean13 like '%"
				+ ean13
				+ "%') group by codpro order by despro";

		Log.d("BDclasses ::getProductosXean13::", rawQuery);
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(rawQuery, null);

		ItemProducto[] productos = new ItemProducto[cursor.getCount()];

		if (cursor.moveToFirst()) {
			int i = 0;
			Log.d("DBclasses ::getProductosXean13::", "cursor.moveToFirst");
			do {

				productos[i] = new ItemProducto();
				productos[i].setSec_politica(cursor.getString(0));
				productos[i].setCodprod(cursor.getString(1));
				productos[i].setDescripcion(cursor.getString(2));
				productos[i].setPrecio(cursor.getDouble(3));
				productos[i].setPrecioUnidad(cursor.getDouble(4));
				productos[i].setFact_conv(cursor.getInt(5));
				productos[i].setPeso(cursor.getDouble(6));
				productos[i].setStock(cursor.getInt(7));
				productos[i].setCodProveedor(cursor.getString(8));

				Log.d("DBclasses ::getProductosXean13::",
						productos[i].getSec_politica() + "\n"
								+ productos[i].getCodprod() + "\n"
								+ productos[i].getDescripcion() + "\n"
								+ productos[i].getPrecio() + "\n"
								+ productos[i].getPrecioUnidad() + "\n"
								+ productos[i].getFact_conv() + "\n"
								+ productos[i].getPeso() + "\n"
								+ productos[i].getStock() + "\n"
								+ productos[i].getCodProveedor());

				i++;
			} while (cursor.moveToNext());
		}

		cursor.close();
		db.close();

		return productos;
	}

	public ArrayList<DBClientes> getFamiliasxCliente(String codcli) {
		// TODO Auto-generated method stub;
		String rawQuery;

		rawQuery = "select * from cliente where codigo_familiar='" + codcli
				+ "'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		ArrayList<DBClientes> dbclientes = new ArrayList<DBClientes>();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			DBClientes dbc = new DBClientes();
			dbc.setCodcli(cur.getString(0));
			dbc.setNomcli(cur.getString(1));
			dbc.setRuccli(cur.getString(2));

			dbc.setStdcli(cur.getString(3));
			dbc.setComprobante(cur.getString(4));

			dbc.setEmail(cur.getString(5));
			dbc.setTipo_documento(cur.getInt(6));
			dbc.setTipo_cliente(cur.getInt(7));
			dbc.setTiempo_credito(cur.getInt(8));
			dbc.setLimite_credito(cur.getFloat(9));
			dbc.setPersona(cur.getString(10));
			dbc.setAfecto(cur.getString(11));

			dbc.setCondicion_venta(cur.getString(12));
			dbc.setCodigo_familiar(cur.getString(13));

			dbclientes.add(dbc);
			cur.moveToNext();
		}
		cur.close();
		db.close();

		return dbclientes;

	}

	public String getFechaUltimaCompraxCliente(String codcli) {
		// TODO Auto-generated method stubselect Max(fecfac) from
		// registro_Ventas1 where codcli=6;
		String rawQuery;

		rawQuery = "select Max(fecfac) from registro_Ventas1 where codcli='"
				+ codcli + "'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		String descripcion = "";
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			descripcion = (cur.getString(0));

			cur.moveToNext();
		}
		if (descripcion == null || descripcion.length() == 0) {
			descripcion = "--";
		}
		cur.close();
		db.close();

		return descripcion;
	}

	public String getMontoUltimaCompraCliente(String codcli) {
		// TODO Auto-generated method stub

		String rawQuery;

		rawQuery = "select  Max(sub_total) from registro_Ventas1 where fecfac=(select MAX(fecfac) from registro_ventas1 where  codcli='"
				+ codcli + "') and codcli='" + codcli + "'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		String descripcion = "";
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			descripcion = (cur.getString(0));

			cur.moveToNext();
		}
		if (descripcion == null || descripcion.length() == 0) {
			descripcion = "--";
		}
		cur.close();
		db.close();

		return descripcion;
	}
	
	public int getFactorConversion(String codigoProducto){
		String rawQuery = 
				"select factor_conversion from producto where codpro like '"+codigoProducto+"' ";
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
		
		return cantidad; 

	}
	
	public ArrayList<DB_PromocionDetalle> getPromocionesXProducto2(	String codpro, int tipo_unimed_entrada, String codcli,	String codven, String cod_politica, String codigoUbigeo) {	
		
		String rawQuery;
			
		rawQuery = "select * from promocion_detalle where entrada like '"+ codpro+ "' and tipo_unimed_entrada like '"+ tipo_unimed_entrada+ "' "
				+ "and ('"+ codcli+ "' in (select codcli from promocion_clientes where sec_promocion=promocion_detalle.secuencia) or general like '0') "
				+ "and ('"+ codven+ "' in (select codven from promocion_vendedor where sec_promocion=promocion_detalle.secuencia) or vendedor like '0') "
				+ "and ('"+ cod_politica+ "' in(select sec_politica from promocion_politica where sec_promocion=promocion_detalle.secuencia) or politica like '0') "
				+ "and ('"+ codigoUbigeo+ "' in (select codigoUbigeo from promocion_ubigeo where sec_promocion like promocion_detalle.secuencia) or ubigeo like '0') ";
		
		Log.d("DBclasses ::getPromocionesXProducto2::","rawQuery -> "+rawQuery);
 
		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		ArrayList<DB_PromocionDetalle> tb_promocion_detalle = new ArrayList<DB_PromocionDetalle>();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			DB_PromocionDetalle dbpromo = new DB_PromocionDetalle();
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
			dbpromo.setPrioridad(cur.getInt(cur.getColumnIndex("prioridad")));

			String prueba1 = cur.getString(20);
			String prueba2 = cur.getString(21);

			tb_promocion_detalle.add(dbpromo);
			Log.d("", "agregando a la lista de promocion: "+dbpromo.getEntrada()); 
			cur.moveToNext();
		}
		cur.close();
		db.close();
		return tb_promocion_detalle;
	}

	public String getNombreUnidad(String codpro) {

		String rawQuery = "select unidad_medida.desunimed from producto inner join unidad_medida on producto.codunimed= unidad_medida.codunimed "
				+ "where producto.codpro='" + codpro + "'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		cur.moveToFirst();
		String unidadMedida = "";
		if (cur.moveToFirst()) {

			do {
				unidadMedida = cur.getString(0);
			} while (cur.moveToNext());

		}
		cur.close();
		db.close();
		return unidadMedida;
	}

	public String getNombreUnidadAlmacen(String codpro) {

		String rawQuery = "select unidad_medida.desunimed from producto inner join unidad_medida on producto.codunimed_almacen= unidad_medida.codunimed "
				+ "where producto.codpro='" + codpro + "'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		cur.moveToFirst();
		String unidadMedida = "";
		if (cur.moveToFirst()) {

			do {
				unidadMedida = cur.getString(0);
			} while (cur.moveToNext());

		}
		cur.close();
		db.close();
		return unidadMedida;
	}

	public ArrayList<DB_PromocionDetalle> getPromocionesXSecuencia(int sec,int itm) {
		// TODO Auto-generated method stub
		String rawQuery;

		/*
		 * rawQuery=
		 * "select * from tb_promocion_detalle left join promocion_clientes ON "
		 * +
		 * "tb_promocion_detalle.secuencia=promocion_clientes.sec_promocion WHERE "
		 * + "tb_promocion_detalle.entrada='"+codpro+
		 * "' and tb_promocion_detalle.general=0 "+
		 * "or promocion_clientes.codcli='"+codcli+"'";
		 */

		// rawQuery="select * from promocion_detalle where secuencia= (select secuencia from promocion_detalle where entrada='"+cip+"')";
		rawQuery = "select * from promocion_detalle where secuencia='" + sec
				+ "' and item='" + itm + "'";
		Log.d("DBclasses ::getPromocionesXSecuencia::", "rawQuery-> "+rawQuery);

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		ArrayList<DB_PromocionDetalle> tb_promocion_detalle = new ArrayList<DB_PromocionDetalle>();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			DB_PromocionDetalle dbpromo = new DB_PromocionDetalle();
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
			/*Log.d("DBclasses ::getPromocionesXSecuencia::", "Item: "+cur.getInt(5));
			Log.d("DBclasses ::getPromocionesXSecuencia::", "Agrupado: "+cur.getInt(6));
			Log.d("DBclasses ::getPromocionesXSecuencia::", "Total_agrupado: "+cur.getInt(18));
			Log.d("DBclasses ::getPromocionesXSecuencia::", "--------------------------------------");*/
			tb_promocion_detalle.add(dbpromo);
			cur.moveToNext();
		}
		cur.close();
		db.close();
		return tb_promocion_detalle;
	}

	public double getMontoTotalCabecera(String oc_numero) {
		// TODO Auto-generated method stub
		String rawQuery;
		rawQuery = "select monto_total from pedido_cabecera where oc_numero='"
				+ oc_numero + "'";
		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		double total = 0;
		cur.moveToFirst();

		while (!cur.isAfterLast()) {
			total = cur.getDouble(0);
			cur.moveToNext();
		}
		Log.w("MONTO TOTAL CABCERA PROMOCION", total + "-");
		cur.close();
		db.close();
		return total;

	}

	public boolean deletePedidoCabeceraxOc(String oc_numero, SQLiteDatabase _db) {

		String where = "oc_numero=?";
		String[] args = { oc_numero };

		try {

			SQLiteDatabase db = _db==null?getWritableDatabase():_db;
			int dleted=db.delete("pedido_cabecera", where, args);
			db.delete("pedido_detalle", where, args);
			if (_db==null) {
				db.close();
			}
			Log.i("eliminar pedido", "pedido eliminado");
			return dleted>0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public void updateHoraServidorPedidoCabecera(String oc_numero,
			String hora_servidor) {
		// TODO Auto-generated method stub
		String where = "oc_numero = ?";
		String[] args = { oc_numero };

		try {

			SQLiteDatabase db = getWritableDatabase();

			ContentValues reg = new ContentValues();
			reg.put("DT_PEDI_FECHASERVIDOR", hora_servidor);

			db.update("pedido_cabecera", reg, where, args);
			db.close();

			Log.i("PedidoCabecera actualizar HORA SERVIDOR",
					"pedido_cabecera actualizada");

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

	}

	public void updateEstadoPedidoCabecera(String oc_numero, String estado) {
		// TODO Auto-generated method stub
		String where = "oc_numero = ?";
		String[] args = { oc_numero };

		try {

			SQLiteDatabase db = getWritableDatabase();

			ContentValues reg = new ContentValues();
			reg.put("estado", estado);

			db.update("pedido_cabecera", reg, where, args);
			db.close();

			Log.i("CAMBIAR ESTADO PEDIDO CABECERA", "estado cambiado"
					+ oc_numero + "- estado" + estado);

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public void updateHoraServidorDeposito(String secuencia, String res) {
		// TODO Auto-generated method stub
		String where = "secuencia = ?";
		String[] args = { secuencia };

		try {

			SQLiteDatabase db = getWritableDatabase();

			ContentValues reg = new ContentValues();
			reg.put("DT_DEPO_FECHASERVIDOR", res);

			db.update("depositos", reg, where, args);
			db.close();

			Log.i("DEPOSITOS", "FECHA SERVIDOR: " + secuencia + "- estado:"
					+ res);

		} catch (SQLException ex) {
			Log.i("ERROR: Actualizar DEPOSITOS", ex + "");
			ex.printStackTrace();
		}
	}

	public void updateHoraServidorIngresos(String secuencia, String secitm,
			String res) {
		// TODO Auto-generated method stub
		String where = "secuencia = ? and secitm = ?";
		String[] args = { secuencia, secitm };

		try {

			SQLiteDatabase db = getWritableDatabase();

			ContentValues reg = new ContentValues();
			reg.put("DT_INGR_FechaServidor", res);

			db.update("ingresos", reg, where, args);
			db.close();

			Log.i("CAMBIAR ESTADO INGRESOS", "estado cambiado" + res);

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

	}

	public String[] getOC() {
		// TODO Auto-generated method stub

		String rawQuery = "select oc_numero from pedido_cabecera";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(rawQuery, null);

		String[] datos;

		if (cursor.getCount() != 0) {

			datos = new String[cursor.getCount()];
			cursor.moveToFirst();

			if (cursor.moveToFirst()) {
				int i = 0;
				do {
					datos[i] = cursor.getString(0);
					i++;
				} while (cursor.moveToNext());
			}

		} else {
			datos = new String[] { "sin datos" };
		}

		cursor.close();
		db.close();

		return datos;
	}

	public void updateFlagPedidoCabecera(String oc_numero, String flag) {
		// TODO Auto-generated method stub
		String where = "oc_numero = ?";
		String[] args = { oc_numero };

		try {

			SQLiteDatabase db = getWritableDatabase();

			ContentValues reg = new ContentValues();
			reg.put("flag", flag);

			db.update("pedido_cabecera", reg, where, args);
			db.close();

			Log.i("CAMBIAR FLAG PEDIDO CABECERA", "FLAG cambiado" + oc_numero
					+ "- FLAG" + flag);

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public String getItemDireccionxNombre(String nomDireccion, String codcli) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		String rawQuery;
		rawQuery = "select item from direccion_cliente where direccion='"
				+ nomDireccion + "' and codcli='" + codcli + "'";
		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		String total = "0";
		cur.moveToFirst();

		while (!cur.isAfterLast()) {
			total = cur.getString(0);
			cur.moveToNext();
		}
		Log.w("MONTO TOTAL CABCERA PROMOCION", total + "-");
		cur.close();
		db.close();
		return total;
	}

	public void updateFlagDeposito(String secuencia, String flag) {
		// TODO Auto-generated method stub
		String where = "secuencia = ?";
		String[] args = { secuencia };

		try {

			SQLiteDatabase db = getWritableDatabase();

			ContentValues reg = new ContentValues();
			reg.put("BI_DEPO_Flag", flag);

			db.update("depositos", reg, where, args);
			db.close();

			Log.i("CAMBIAR FLAG DEPOSITOS", "FLAG cambiado" + secuencia
					+ "- FLAG" + flag);

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public void updateEstadoDeposito(String secuencia, String estado) {
		// TODO Auto-generated method stub
		String where = "secuencia = ?";
		String[] args = { secuencia };

		try {

			SQLiteDatabase db = getWritableDatabase();

			ContentValues reg = new ContentValues();
			reg.put("estado", estado);

			db.update("depositos", reg, where, args);
			db.close();

			Log.i("CAMBIAR ESTADO DEPOSITOS", "Estado cambiado" + secuencia
					+ "- ESTADO" + estado);

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public String getCodcliIngresos(String secuencia) {
		// TODO Auto-generated method stub
		String rawQuery;
		rawQuery = "select codcli from cta_ingresos where secuencia='"
				+ secuencia + "'";
		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		String total = "0";
		cur.moveToFirst();

		while (!cur.isAfterLast()) {
			total = cur.getString(0);
			cur.moveToNext();
		}
		Log.w("CODCLI X CUENTA INGRESOS", total + "-" + secuencia);
		cur.close();
		db.close();
		return total;
	}

	public String getMontoTotalCobranzaXmoneda(String codmon) {

		String fechaConfig = getFecha2().trim();

		String rawQuery;
		rawQuery = "Select ifnull(SUM(ingresos.monto_afecto),0) from ingresos inner join cta_ingresos "
				+ "on ingresos.secuencia=cta_ingresos.secuencia where flag<>'A' "
				+ "and substr(secitm,1,1)<>'S' and substr(fecpag,1,10)= '"
				+ fechaConfig
				+ "' "
				+ "and cta_ingresos.codmon='"
				+ codmon
				+ "'";
		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		String total = "0";
		cur.moveToFirst();

		while (!cur.isAfterLast()) {
			total = cur.getString(0);
			cur.moveToNext();
		}
		Log.w("Ingresos total X moneda", "moneda: " + codmon + ", total: "
				+ total);
		cur.close();
		db.close();
		return total;
		// TODO Auto-generated method stub

	}

	public String getMontoTotalDeposito(String moneda) {
		// TODO Auto-generated method stub
		String rawQuery;
		rawQuery = "Select ifnull(SUM(depositos.monto),0) from depositos where moneda='"
				+ moneda + "'";
		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		String total = "0";
		cur.moveToFirst();

		while (!cur.isAfterLast()) {
			total = cur.getString(0);
			cur.moveToNext();
		}

		Log.w("Depositos TOTAL X moneda", "moneda: " + moneda + ", total: "
				+ total);
		cur.close();
		db.close();
		return total;
	}

	public boolean existeDevolucion(String oc_numero) {
		// TODO Auto-generated method stub
		String rawQuery;
		rawQuery = "select oc_numero from pedido_devolucion where oc_numero='"
				+ oc_numero + "'";
		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		boolean existe = false;
		cur.moveToFirst();

		while (!cur.isAfterLast()) {
			existe = true;
			cur.moveToNext();
		}

		cur.close();
		db.close();
		return existe;
	}

	public void deleteDepositos(String secuencia) {
		// TODO Auto-generated method stub
		String where = "secuencia=?";
		String[] args = { secuencia };

		try {
			SQLiteDatabase db = getWritableDatabase();
			db.delete("depositos", where, args);
			db.delete("depositos", where, args);
			db.close();

			Log.i("eliminar DEPOSITOS", "Deposito eliminado");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void eliminar_Ingreso(String secuencia, String secitm) {

		String where = "secuencia=? and  secitm=?";
		String[] args = { secuencia, secitm };

		try {
			SQLiteDatabase db = getWritableDatabase();
			db.delete("ingresos", where, args);
			db.close();

			Log.i("eliminar ingreso", "ingreso eliminado sec: " + secuencia
					+ " secitm: " + secitm);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
 

	public void updateIngresos2(DBIngresos ingresos) {
		// TODO Auto-generated method stub

		String where = "secuencia = ? and secitm = ?";
		String[] args = { "" + ingresos.getSecuencia(),
				"" + ingresos.getSecitm() };

		try {

			SQLiteDatabase db = getWritableDatabase();

			ContentValues Nreg = new ContentValues();
			// Nreg.put("secuencia", ingresos.getSecuencia());
			// Nreg.put("secitm", ingresos.getSecitm());
			Nreg.put("fecpag", ingresos.getFecpag());
			Nreg.put("total", ingresos.getTotal());
			Nreg.put("acuenta", ingresos.getAcuenta());
			Nreg.put("saldo", ingresos.getSaldo());
			Nreg.put("codcue", ingresos.getCodcue());
			Nreg.put("numope", ingresos.getNumope());
			Nreg.put("codforpag", ingresos.getCodforpag());
			Nreg.put("tipo_cambio", ingresos.getTipo_cambio());
			Nreg.put("codmon", ingresos.getCodmon());
			Nreg.put("monto_afecto", ingresos.getMonto_afecto());
			Nreg.put("username", ingresos.getUsername());
			Nreg.put("fecoperacion", ingresos.getFecoperacion());
			Nreg.put("flag", ingresos.getFlag());
			Nreg.put("latitud", ingresos.getLatitud());
			Nreg.put("longitud", ingresos.getLongitu());
			Nreg.put("DT_INGR_FECHASERVIDOR",
					ingresos.getDT_INGR_FECHASERVIDOR());
			Nreg.put("estado", ingresos.getEstado());
			Nreg.put("observacion", ingresos.getObservacion());
			Nreg.put("codcli", ingresos.getCodcli());

			Nreg.put("tipoDoc", ingresos.getTipoDoc());
			Nreg.put("serie", ingresos.getSerie());
			Nreg.put("numero", ingresos.getNumero());
			Nreg.put("codigoBanco", ingresos.getCodigoBanco());

			
			db.update("ingresos", Nreg, where, args);
			db.close();

			Log.i("ACTUALIZAR INGRESOS", "pedido_cabecera actualizada flag: "
					+ ingresos.getFlag());

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	
	public DBIngresos getIngresos2(String secuencia, String secitm) {
		// TODO Auto-generated method stub

		String rawQuery;

		rawQuery = "select * from ingresos where secuencia='" + secuencia
				+ "' " + "   and secitm='" + secitm + "'";

		SQLiteDatabase db = getReadableDatabase();
		DBIngresos dbingresos = new DBIngresos();
		Cursor cur = db.rawQuery(rawQuery, null);
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			dbingresos.setSecuencia(cur.getString(0));
			dbingresos.setSecitm(cur.getString(1));
			dbingresos.setFecpag(cur.getString(2));
			dbingresos.setTotal(cur.getString(3));
			dbingresos.setAcuenta(cur.getString(4));
			dbingresos.setSaldo(cur.getString(5));
			dbingresos.setCodcue(cur.getString(6));
			dbingresos.setNumope(cur.getString(7));
			dbingresos.setCodforpag(cur.getString(8));
			dbingresos.setTipo_cambio(cur.getString(9));
			dbingresos.setCodmon(cur.getString(10));
			dbingresos.setMonto_afecto(cur.getString(11));
			dbingresos.setUsername(cur.getString(12));
			dbingresos.setFecoperacion(cur.getString(13));
			dbingresos.setFlag(cur.getString(14));
			dbingresos.setLatitud(cur.getString(15));
			dbingresos.setLongitu(cur.getString(16));
			dbingresos.setDT_INGR_FECHASERVIDOR(cur.getString(17));
			dbingresos.setEstado(cur.getString(18));

			cur.moveToNext();
		}
		cur.close();
		db.close();

		return dbingresos;
	}

	public String getFacturaXSecuencia(String sec_ingresos) {
		// TODO Auto-generated method stub
		String rawQuery;
		rawQuery = "Select numero_factura from cta_ingresos where secuencia='"
				+ sec_ingresos + "'";
		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		String total = "0";
		cur.moveToFirst();

		while (!cur.isAfterLast()) {
			total = cur.getString(0);
			cur.moveToNext();
		}
		if (total == null || total.trim().length() == 0) {
			total = "0";
		}
		Log.w("FACTURA", "" + total);
		cur.close();
		db.close();
		return total;
	}

	public CharSequence getSaldo_virtualxSecuencia(String secuencia) {
		// TODO Auto-generated method stub
		String rawQuery;
		rawQuery = "Select saldo_virtual from cta_ingresos where secuencia='"
				+ secuencia + "'";
		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		String total = "0";
		cur.moveToFirst();

		while (!cur.isAfterLast()) {
			total = cur.getString(0);
			cur.moveToNext();
		}
		if (total == null || total.trim().length() == 0) {
			total = "0";
			Log.w("SALDO FORZADO", "" + total + secuencia);
		}
		Log.w("SALDO", "" + total);
		cur.close();
		db.close();
		return total;
	}

	public DBCta_Ingresos getCtaIngresos(String sec_ingresos) {
		// TODO Auto-generated method stub
		String rawQuery;

		rawQuery = "Select * from cta_ingresos where secuencia='"
				+ sec_ingresos + "'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		DBCta_Ingresos dbcta = new DBCta_Ingresos();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			dbcta.setSecuencia(cur.getString(0));
			dbcta.setCodmon(cur.getString(1));
			dbcta.setCoddoc(cur.getString(2));
			dbcta.setSerie_doc(cur.getString(3));
			dbcta.setNumero_factura(cur.getString(4));
			dbcta.setTotal(cur.getString(5));
			dbcta.setAcuenta(cur.getString(6));
			dbcta.setSaldo(cur.getString(7));
			dbcta.setFeccom(cur.getString(8));
			dbcta.setCodcli(cur.getString(9));
			dbcta.setUsername(cur.getString(10));
			dbcta.setFecoperacion(cur.getString(11));
			dbcta.setSaldo_virtual(cur.getString(13));

			cur.moveToNext();
		}
		cur.close();
		db.close();
		return dbcta;
	}

	public ArrayList<DB_Servidor> getServicios() {
		// TODO Auto-generated method stub
		String rawQuery;

		//rawQuery = "select * from Servidor order by IN_SERV_codigo_ID";
		String appVersion = _context.getString(R.string.APP_VERSION);
		//rawQuery = "select * from Servidor where (select valor from configuracion where nombre like '"+appVersion+"') like IN_SERV_codigo_ID ";
		rawQuery = "select * from Servidor";
		Log.d(TAG, rawQuery);

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		ArrayList<DB_Servidor> al_servidor = new ArrayList<DB_Servidor>();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			DB_Servidor dbpromo = new DB_Servidor();
			dbpromo.setTX_SERV_servicioWeb(cur.getString(0));
			dbpromo.setTX_SERV_servidorBD(cur.getString(1));
			dbpromo.setTX_SERV_nombreBD(cur.getString(2));
			dbpromo.setTX_SERV_usuario(cur.getString(3));
			dbpromo.setTX_SERV_contrasena(cur.getString(4));
			dbpromo.setIN_SERV_item(Integer.parseInt(cur.getString(5)));
			dbpromo.setIN_SERV_codigo_ID(cur.getInt(6));
			Log.d("DBclasses:getServicios:", "ServicioWeb: "+cur.getString(0));
			al_servidor.add(dbpromo);
			cur.moveToNext();
		}
		cur.close();
		db.close();
		return al_servidor;
	}

	public DB_Servidor getEntityServidorxCodigo(String codigo) {
		// TODO Auto-generated method stub
		String rawQuery;

		rawQuery = "select * from Servidor where IN_SERV_codigo_ID='" + codigo
				+ "'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		DB_Servidor dbpromo = new DB_Servidor();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			dbpromo.setTX_SERV_servicioWeb(cur.getString(0));
			dbpromo.setTX_SERV_servidorBD(cur.getString(1));
			dbpromo.setTX_SERV_nombreBD(cur.getString(2));
			dbpromo.setTX_SERV_usuario(cur.getString(3));
			dbpromo.setTX_SERV_contrasena(cur.getString(4));
			dbpromo.setIN_SERV_item(Integer.parseInt(cur.getString(5)));
			dbpromo.setIN_SERV_codigo_ID(cur.getInt(6));

			cur.moveToNext();
		}
		cur.close();
		db.close();
		return dbpromo;
	}

	public String getCodmonxSecuencia(String secuencia) {
		// TODO Auto-generated method stub
		String rawQuery;

		rawQuery = "select codmon from cta_ingresos where secuencia='"
				+ secuencia + "'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		String descripcion = "0";
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			descripcion = (cur.getString(0));

			cur.moveToNext();
		}
		if (descripcion == null || descripcion.length() == 0) {
			descripcion = "0";
		}
		cur.close();
		db.close();

		return descripcion;
	}

	public ItemProducto[] getPoliticaCLiente(String codven, String codprod,
			String codcli) {
		String rawQuery = "SELECT producto.factor_conversion, politica_precio2.prepro, "
				+ " politica_precio2.prepro_unidad, politica_precio2.secuencia "
				+ " FROM"
				+ " producto "
				+ " INNER JOIN politica_precio2 ON producto.codpro = politica_precio2.codpro"
				+ " INNER JOIN politica_cliente ON politica_precio2.secuencia = politica_cliente.sec_politica"
				+ " WHERE "
				+ " politica_precio2.codpro ='"
				+ codprod
				+ "' and politica_cliente.codcli='" + codcli + "'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(rawQuery, null);
		ItemProducto[] productos;
		if (cursor.getCount() == 0) {
			productos = new ItemProducto[1];
			productos[0] = new ItemProducto();
			productos[0].setFact_conv(0);
			productos[0].setPrecio(0.0);
			productos[0].setPrecioUnidad(0.0);
			productos[0].setSec_politica("-1");
		} else {
			productos = new ItemProducto[cursor.getCount()];
		}
		cursor.moveToFirst();
		if (cursor.moveToFirst()) {
			int i = 0;
			do {

				productos[i] = new ItemProducto();
				// uso fact_conv para almacenar la secuencia
				productos[i].setFact_conv(cursor.getInt(0));
				// productos[i].setCodprod(cursor.getString(0));
				// productos[i].setDescripcion(cursor.getString(1));
				productos[i].setPrecio(cursor.getDouble(1));
				productos[i].setPrecioUnidad(cursor.getDouble(2));
				productos[i].setSec_politica(cursor.getString(3));
				// productos[i].setFact_conv(cursor.getInt(4));
				// productos[i].setPeso(cursor.getDouble(5));
				// productos[i].setStock(cursor.getInt(6));
				i++;

			} while (cursor.moveToNext());
		}

		cursor.close();
		db.close();
		return productos;
	}

	public String getUnidadMedidaxCodigo(String codunimed) {
		// TODO Auto-generated method stub
		String rawQuery;
		rawQuery = "Select desunimed from unidad_medida where codunimed='"
				+ codunimed + "'";
		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		String total = "0";
		cur.moveToFirst();

		while (!cur.isAfterLast()) {
			total = cur.getString(0);
			cur.moveToNext();
		}
		if (total == null || total.trim().length() == 0) {
			total = "0";
		}
		Log.w("MONTO TOTAL", "" + total);
		cur.close();
		db.close();
		return total;
	}

	public String getRuc() {
		// TODO Auto-generated method stub
		String rawQuery;
		rawQuery = "Select TX_EMPR_RUC from Empresa";
		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		String total = "0";
		cur.moveToFirst();

		while (!cur.isAfterLast()) {
			total = cur.getString(0);
			cur.moveToNext();
		}
		if (total == null || total.trim().length() == 0) {
			total = "0";
		}
		Log.w("RUC EMPRESA", "" + total);
		cur.close();
		db.close();
		return total;
	}

	public double getPrecioxCodproPolitica(String codprod, String cod_politica) {
		// TODO Auto-generated method stub
		double total = 0;
		try {
			String rawQuery;
			rawQuery = "select prepro from politica_precio2 where codpro='"
					+ codprod + "' and secuencia='" + cod_politica + "'";
			SQLiteDatabase db = getReadableDatabase();
			Cursor cur = db.rawQuery(rawQuery, null);

			cur.moveToFirst();

			while (!cur.isAfterLast()) {
				total = cur.getDouble(0);
				cur.moveToNext();
			}

			Log.w("RUC EMPRESA", "" + total);
			cur.close();
			db.close();

		} catch (Exception e) {

		}
		return total;

	}

	public double getPrecioUnidadxCodproPolitica(String codprod,
			String cod_politica) {
		// TODO Auto-generated method stub
		double total = 0;
		try {
			String rawQuery;
			rawQuery = "select prepro_unidad from politica_precio2 where codpro='"
					+ codprod + "' and secuencia='" + cod_politica + "'";
			SQLiteDatabase db = getReadableDatabase();
			Cursor cur = db.rawQuery(rawQuery, null);

			cur.moveToFirst();

			while (!cur.isAfterLast()) {
				total = cur.getDouble(0);
				cur.moveToNext();
			}

			Log.w("RUC EMPRESA", "" + total);
			cur.close();
			db.close();

		} catch (Exception e) {

		}
		return total;

	}

	public DB_Empresa getEmpresa() {
		// TODO Auto-generated method stub
		String rawQuery;

		rawQuery = "select * from Empresa";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		DB_Empresa entity = new DB_Empresa();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			entity.setEmpresa(cur.getString(1));
			entity.setRuc(cur.getString(2));
			entity.setBd(cur.getString(3));
			entity.setUsuario(cur.getString(4));
			entity.setClave(cur.getString(5));
			cur.moveToNext();
		}
		cur.close();
		db.close();
		return entity;
	}

	public void updateFlagIngreso_Delete(String secuencia, String secitm,
			String flag) {
		// TODO Auto-generated method stub
		String where = "secuencia= ? and secitm=?";
		String[] args = { secuencia, secitm };

		try {

			SQLiteDatabase db = getWritableDatabase();

			ContentValues reg = new ContentValues();
			reg.put("flag", flag);

			db.update("ingresos", reg, where, args);
			db.close();

			Log.i("CAMBIAR ESTADO INGRESOS", "estado cambiado" + secuencia
					+ "- estado" + secitm);

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public ArrayList<DBIngresos> getIngresosXCliente(String nomcli) {

		String fechaConfig = getFecha2();
		String rawQuery;

		rawQuery = "select * FROM ingresos"
				+ "  INNER JOIN cta_ingresos ON ingresos.secuencia = cta_ingresos.secuencia"
				+ "  INNER JOIN cliente ON cta_ingresos.codcli = cliente.codcli "
				+ "	where cliente.nomcli like '%" + nomcli
				+ "%' and ingresos.flag <> 'A' "
				+ "and substr(secitm,1,1)<>'S' and substr(fecpag,1,10)= '"
				+ fechaConfig + "' order by fecpag desc";

		SQLiteDatabase db = getReadableDatabase();

		Cursor cur = db.rawQuery(rawQuery, null);
		ArrayList<DBIngresos> lista_ingresos = new ArrayList<DBIngresos>();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			DBIngresos dbingresos = new DBIngresos();
			dbingresos.setSecuencia(cur.getString(0));
			dbingresos.setSecitm(cur.getString(1));
			dbingresos.setFecpag(cur.getString(2));
			dbingresos.setTotal(cur.getString(3));
			dbingresos.setAcuenta(cur.getString(4));
			dbingresos.setSaldo(cur.getString(5));
			dbingresos.setCodcue(cur.getString(6));
			dbingresos.setNumope(cur.getString(7));
			dbingresos.setCodforpag(cur.getString(8));
			dbingresos.setTipo_cambio(cur.getString(9));
			dbingresos.setCodmon(cur.getString(10));
			dbingresos.setMonto_afecto(cur.getString(11));
			dbingresos.setUsername(cur.getString(12));
			dbingresos.setFecoperacion(cur.getString(13));
			dbingresos.setFlag(cur.getString(14));
			dbingresos.setLatitud(cur.getString(15));
			dbingresos.setLongitu(cur.getString(16));
			dbingresos.setDT_INGR_FECHASERVIDOR(cur.getString(17));
			dbingresos.setEstado(cur.getString(18));

			Log.w("SECUENCIA INGRESOS REPORTE", cur.getString(1));
			lista_ingresos.add(dbingresos);
			cur.moveToNext();
		}
		cur.close();
		db.close();

		return lista_ingresos;
	}

	public ArrayList<DBIngresos> getIngresosXFactura(String factura) {

		String fechaConfig = getFecha2();
		String rawQuery;

		rawQuery = "select * FROM ingresos"
				+ "  INNER JOIN cta_ingresos ON ingresos.secuencia = cta_ingresos.secuencia"
				+ "	where cta_ingresos.numero_factura like '%" + factura
				+ "' and ingresos.flag <> 'A' "
				+ "and substr(secitm,1,1)<>'S' and substr(fecpag,1,10)= '"
				+ fechaConfig + "' order by fecpag desc";

		SQLiteDatabase db = getReadableDatabase();

		Cursor cur = db.rawQuery(rawQuery, null);
		ArrayList<DBIngresos> lista_ingresos = new ArrayList<DBIngresos>();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			DBIngresos dbingresos = new DBIngresos();
			dbingresos.setSecuencia(cur.getString(0));
			dbingresos.setSecitm(cur.getString(1));
			dbingresos.setFecpag(cur.getString(2));
			dbingresos.setTotal(cur.getString(3));
			dbingresos.setAcuenta(cur.getString(4));
			dbingresos.setSaldo(cur.getString(5));
			dbingresos.setCodcue(cur.getString(6));
			dbingresos.setNumope(cur.getString(7));
			dbingresos.setCodforpag(cur.getString(8));
			dbingresos.setTipo_cambio(cur.getString(9));
			dbingresos.setCodmon(cur.getString(10));
			dbingresos.setMonto_afecto(cur.getString(11));
			dbingresos.setUsername(cur.getString(12));
			dbingresos.setFecoperacion(cur.getString(13));
			dbingresos.setFlag(cur.getString(14));
			dbingresos.setLatitud(cur.getString(15));
			dbingresos.setLongitu(cur.getString(16));
			dbingresos.setDT_INGR_FECHASERVIDOR(cur.getString(17));
			dbingresos.setEstado(cur.getString(18));

			Log.w("SECUENCIA INGRESOS REPORTE", cur.getString(1));
			lista_ingresos.add(dbingresos);
			cur.moveToNext();
		}
		cur.close();
		db.close();

		return lista_ingresos;
	}

	public String getCodVendedor_Only() {
		String rawQuery;
		rawQuery = "Select codven from vendedor ";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		cur.moveToFirst();
		String codigo = "";
		if (cur.moveToFirst()) {

			do {
				codigo = cur.getString(0);
			} while (cur.moveToNext());

		}
		cur.close();
		db.close();
		return codigo;
	}

	public ArrayList<DBPedido_Cabecera> getPedidosCabeceraxNombreCliente(
			String nomcli) {
		String rawQuery;

		String codven = new SessionManager(_context).getCodigoVendedor();

		// rawQuery =
		// "select * from pedido_cabecera  where substr(fecha_oc,1,10) >= '"+getFecha()+"'"
		// + " group by cod_cli";
		// rawQuery =
		// "select  cod_cli,  SUM(monto_total), flag, cond_pago, cod_noventa from pedido_cabecera   group by cod_cli";
		rawQuery = "select  cod_cli,  monto_total, flag, cond_pago, cod_noventa, oc_numero, peso_total, estado, percepcion_total, " +
				"pedido_cabecera.latitud from pedido_cabecera "
				+ "   inner join cliente on cliente.codcli= pedido_cabecera.cod_cli where oc_numero<>0  and flag <> 'A' and "
				+ "   cod_emp='"
				+ codven
				+ "' and cliente.nomcli like '%"
				+ nomcli + "%' order by oc_numero DESC";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		ArrayList<DBPedido_Cabecera> lista_pedidos = new ArrayList<DBPedido_Cabecera>();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			DBPedido_Cabecera dbpedido = new DBPedido_Cabecera();
			/*
			 * dbpedido.setOc_numero(cur.getString(0));
			 * dbpedido.setSitio_enfa(cur.getString(1));
			 * dbpedido.setMonto_total(cur.getString(2));
			 * dbpedido.setValor_igv(cur.getString(3));
			 * dbpedido.setMoneda(cur.getString(4));
			 * dbpedido.setFecha_oc(cur.getString(5));
			 * dbpedido.setFecha_mxe(cur.getString(6));
			 * dbpedido.setCond_pago(cur.getString(7));
			 * dbpedido.setCod_cli(cur.getString(8));
			 * dbpedido.setCod_emp(cur.getString(9));
			 * dbpedido.setEstado(cur.getString(10));
			 * dbpedido.setUsername(cur.getString(11));
			 * dbpedido.setRuta(cur.getString(12));
			 * dbpedido.setObserv(cur.getString(13));
			 * dbpedido.setCod_noventa(cur.getInt(14));
			 * dbpedido.setPeso_total(cur.getString(15));
			 */
			dbpedido.setCod_cli(cur.getString(0));
			dbpedido.setMonto_total(cur.getString(1));
			dbpedido.setFlag(cur.getString(2));
			dbpedido.setCond_pago(cur.getString(3));
			dbpedido.setCod_noventa(cur.getInt(4));
			dbpedido.setOc_numero(cur.getString(5));
			dbpedido.setPeso_total(cur.getString(6));
			dbpedido.setEstado(cur.getString(7));
			dbpedido.setPercepcion_total(cur.getString(8));
			dbpedido.setLatitud(cur.getString(cur.getColumnIndex("latitud")));
			//dbpedido.setCodTurno(cur.getString(46));
			

			lista_pedidos.add(dbpedido);
			cur.moveToNext();
		}
		cur.close();
		db.close();
		return lista_pedidos;// d
	}

	public ArrayList<DBPedido_Cabecera> getPedidosCabeceraxDocumento(String oc) {
		String rawQuery;

		String codven = new SessionManager(_context).getCodigoVendedor();

		// rawQuery =
		// "select * from pedido_cabecera  where substr(fecha_oc,1,10) >= '"+getFecha()+"'"
		// + " group by cod_cli";
		// rawQuery =
		// "select  cod_cli,  SUM(monto_total), flag, cond_pago, cod_noventa from pedido_cabecera   group by cod_cli";
		rawQuery = "select  pc.cod_cli,  pc.monto_total, pc.flag, pc.cond_pago, pc.cod_noventa, " +
				"pc.oc_numero, pc.peso_total, pc.estado, pc.percepcion_total, " +
				"pc.latitud from pedido_cabecera pc"
				+ "where pc.oc_numero<>0  and pc.flag <> 'A' and pc.cod_emp='"
				+ codven
				+ "' and pc.oc_numero like '%"
				+ oc
				+ "' order by pc.oc_numero DESC";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		ArrayList<DBPedido_Cabecera> lista_pedidos = new ArrayList<DBPedido_Cabecera>();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			DBPedido_Cabecera dbpedido = new DBPedido_Cabecera();
			dbpedido.setCod_cli(cur.getString(0));
			dbpedido.setMonto_total(cur.getString(1));
			dbpedido.setFlag(cur.getString(2));
			dbpedido.setCond_pago(cur.getString(3));
			dbpedido.setCod_noventa(cur.getInt(4));
			dbpedido.setOc_numero(cur.getString(5));
			dbpedido.setPeso_total(cur.getString(6));
			dbpedido.setEstado(cur.getString(7));
			dbpedido.setPercepcion_total(cur.getString(8));
			dbpedido.setLatitud(cur.getString(cur.getColumnIndex("latitud")));

			lista_pedidos.add(dbpedido);
			cur.moveToNext();
		}
		cur.close();
		db.close();
		return lista_pedidos;// d
	}

	public String getSerieCtaIngresos(int secuencia) {
		// TODO Auto-generated method stub
		String rawQuery;
		rawQuery = "Select serie_doc  from cta_ingresos where secuencia='"
				+ secuencia + "'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		cur.moveToFirst();
		String codigo = "";
		if (cur.moveToFirst()) {

			do {
				codigo = cur.getString(0);
			} while (cur.moveToNext());

		}
		cur.close();
		db.close();
		return codigo;
	}

	public String getTipodocumentoCtaIngresos(String secuencia) {
		// TODO Auto-generated method stub
		String rawQuery;
		rawQuery = "select desdoc from tipo_documento where codcoc=(select coddoc from cta_ingresos where secuencia='"
				+ secuencia + "')";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		cur.moveToFirst();
		String tipo = "";
		if (cur.moveToFirst()) {

			do {
				tipo = cur.getString(0);
			} while (cur.moveToNext());

		}
		cur.close();
		db.close();
		return tipo;
	}

	public String getLimiteCreditoXCliente(String codcli) {
		// TODO Auto-generated method stub
		String rawQuery;
		rawQuery = "Select limite_credito  from cliente where codcli='"
				+ codcli + "'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		cur.moveToFirst();
		String codigo = "";
		if (cur.moveToFirst()) {

			do {
				codigo = cur.getString(0);
			} while (cur.moveToNext());

		}
		cur.close();
		db.close();
		return codigo;
	}

	public boolean existePedidoCabecera(String oc_numero) {
		// TODO Auto-generated method stub
		String rawQuery;
		rawQuery = "select oc_numero from pedido_cabecera where oc_numero='"
				+ oc_numero + "'";
		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		boolean existe = false;
		cur.moveToFirst();

		while (!cur.isAfterLast()) {
			existe = true;
			cur.moveToNext();
		}

		cur.close();
		db.close();
		return existe;
	}

	public boolean existePedidoDetalle(String oc_numero, String item) {
		// TODO Auto-generated method stub
		String rawQuery;
		rawQuery = "select oc_numero from pedido_detalle where oc_numero='"
				+ oc_numero + "' and cip='" + item + "'";
		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		boolean existe = false;
		cur.moveToFirst();

		while (!cur.isAfterLast()) {
			existe = true;
			cur.moveToNext();
		}

		cur.close();
		db.close();
		return existe;
	}
	
	public ArrayList<DB_PromocionDetalle> obtenerListaAcumulados(int item,int secuencia){
		String rawQuery =
				"select " +
				 "prd.secuencia\n" +
						",prd.general\n" +
						",prd.promocion\n" +
						",prd.codalm\n" +
						",prd.tipo\n" +
						",prd.item\n" +
						",prd.agrupado\n" +
						",prd.entrada\n" +
						",prd.tipo_unimed_entrada\n" +
						",prd.monto_minimo\n" +
						",prd.monto_maximo\n" +
						",prd.monto\n" +
						",prd.condicion\n" +
						",prd.cant_condicion\n" +
						",prd.salida\n" +
						",prd.tipo_unimed_salida\n" +
						",prd.cant_promocion\n" +
						",prd.max_pedido\n" +
						",prd.total_agrupado\n" +
						",prd.tipo_promocion\n" +
						",prd.acumulado\n" +
						",prd.prioridad\n" +
						"from promocion_detalle prd where prd.item like "+item+" and prd.secuencia like '"+secuencia+"' and prd.acumulado like '1' ";
		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		cur.moveToFirst();
		
		ArrayList<DB_PromocionDetalle> lista_agrupados= new ArrayList<DB_PromocionDetalle>();
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
			itemPromocion.setPrioridad(cur.getInt(cur.getColumnIndex("prioridad")));
			/**/
			/**/			
			itemPromocion.setAcumulado(cur.getInt(20));
			Log.d("BDclasses ::obtenerListaAgrupados::secuencia: "+secuencia, "Entrada(codigoProducto)-> "+itemPromocion.getEntrada());
			lista_agrupados.add(itemPromocion);
			cur.moveToNext();			
		}		
		return lista_agrupados;
	}
	
	

	
	public ArrayList<DB_PromocionDetalle> obtenerListaAgrupados(int secuencia,int item, int agrupado){
		
		String rawQuery=
				"SELECT * FROM promocion_detalle "+
				"WHERE secuencia like '"+secuencia+"' and item like "+item+" and agrupado like '"+agrupado+"'";
		
		Log.d("BDclasses :obtenerListaAgrupados:","rawQuery-> "+rawQuery);
		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		cur.moveToFirst();
		
		
		
		ArrayList<DB_PromocionDetalle> lista_agrupados= new ArrayList<DB_PromocionDetalle>();
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
			itemPromocion.setPrioridad(cur.getInt(cur.getColumnIndex("prioridad")));
			/**/
			/**/			
			itemPromocion.setAcumulado(cur.getInt(22));
			Log.d("BDclasses ::obtenerListaAgrupados::secuencia: "+secuencia, "Entrada(codigoProducto)-> "+itemPromocion.getEntrada());
			lista_agrupados.add(itemPromocion);
			cur.moveToNext();
			
		}
		 
		return lista_agrupados;
		
	}
		
	public DBPedido_Detalle existePedidoDetalle2(String oc_numero,	String codpro, String cod_unimed) {

		String rawQuery;
		rawQuery = "select * from pedido_detalle where oc_numero='" + oc_numero
				+ "' and cip='" + codpro + "' and unidad_medida='" + cod_unimed+ "' " +
				"and (" +
				"	case when length(sec_promo)>0 then cast(sec_promo as int) else 0 end > 0 " +
				"	or case when length(sec_promo_prioridad)>0 then cast(sec_promo_prioridad as int) else 0 end > 0 " +
				")";
		Log.d("DBclasses :DBPedido_Detalle_existePedidoDetalle2:", "rawQuery-> "+rawQuery);
		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);

		/*
		 * boolean existe=false; cur.moveToFirst();
		 * 
		 * while(!cur.isAfterLast()){ existe=true; cur.moveToNext(); }
		 * 
		 * cur.close(); db.close(); return existe;
		 */

		DBPedido_Detalle dbdetalle = null;

		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			dbdetalle = new DBPedido_Detalle();

			dbdetalle.setOc_numero(cur.getString(0));
			dbdetalle.setEan_item(cur.getString(1));
			dbdetalle.setCip(cur.getString(2));
			dbdetalle.setPrecio_bruto(cur.getString(3));
			dbdetalle.setPrecio_neto(cur.getString(4));
			dbdetalle.setPercepcion(cur.getString(5));
			dbdetalle.setCantidad(cur.getInt(6));
			dbdetalle.setTipo_producto(cur.getString(7));
			dbdetalle.setUnidad_medida(cur.getString(8));
			dbdetalle.setPeso_bruto(cur.getString(9));
			dbdetalle.setFlag(cur.getString(10));
			dbdetalle.setCod_politica(cur.getString(11));
			dbdetalle.setSec_promo(cur.getString(12));
			dbdetalle.setItem_promo(cur.getInt(13));
			dbdetalle.setAgrup_promo(cur.getInt(14));
			Log.d("DBclasses :DBPedido_Detalle_existePedidoDetalle2:", "dbdetalle.getCip "+dbdetalle.getCip()+" cantidad->"+dbdetalle.getCantidad());
			cur.moveToNext();
		}
		cur.close();
		db.close();

		return dbdetalle;
	}

	public String getFecha2() {
		// TODO Auto-generated method stub
		String rawQuery;
		rawQuery = "select valor from configuracion where nombre='Fecha'";
		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		cur.moveToFirst();
		String fecha = "";
		if (cur.moveToFirst()) {

			do {
				fecha = cur.getString(0);
			} while (cur.moveToNext());

		}
		cur.close();
		db.close();
		return fecha;
	}
	
	public void actualizarItem_promo(String oc_numero, String codigo,String cod_unimed, int cantidad) {
		Log.e(TAG, "Actualizando codigo: "+codigo);
		Log.e(TAG, "Actualizando cantidad: "+cantidad);
		Log.e(TAG, "Actualizando cod_unimed: "+cod_unimed);		
		
		String where = "oc_numero = ? and cip=? and unidad_medida=? and tipo_producto in('C','M')";
		String[] args = { oc_numero, codigo, cod_unimed };

		try {

			SQLiteDatabase db = getWritableDatabase();

			ContentValues reg = new ContentValues();
			reg.put("cantidad", cantidad);			
			
			db.update("pedido_detalle", reg, where, args);
			db.close();

			Log.i("actualizar_item_promo", "item_promo_actualizado");

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

	}
	
	public void actualizarItem_promo(String oc_numero, String codigo,String cod_unimed, int cantidad, String secuenciaPromocion) {

		String where = "oc_numero = ? and cip=? and unidad_medida=? and tipo_producto in('C','M')";
		String[] args = { oc_numero, codigo, cod_unimed };

		try {

			SQLiteDatabase db = getWritableDatabase();

			ContentValues reg = new ContentValues();
			reg.put("cantidad", cantidad);
			reg.put("sec_promo", secuenciaPromocion);
			db.update("pedido_detalle", reg, where, args);
			db.close();

			Log.i("actualizar_item_promo", "item_promo_actualizado");

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

	}
	
	//Metodo de prueba debido al error al no acumular montos en las promociones
	public void actualizarItem_promocion(String oc_numero, String codigo, String cod_unimed, int cantidad, String codigoSecuencia){
		try{
			Log.d("DBclasses :actualizarItem_promocion:","Actualizando en cosulta con cantidad-> "+cantidad);
			String sql = 
					"update pedido_detalle"
					+ " set "
					+ " cantidad = "+cantidad+","
					+ DBtables.Pedido_detalle.SEC_PROMO+" = " + codigoSecuencia
					+ " where oc_numero='"+ oc_numero + "' and cip='"+codigo+"' and unidad_medida='"+cod_unimed+"' and tipo_producto in('C','M')";

			SQLiteDatabase db = getWritableDatabase();
			db.execSQL(sql);
			db.close();
			
		} catch(SQLException ex){
			ex.printStackTrace();
		}
		
	}

	public boolean verificar_dependencia_promocion(String entrada, String salida) {
		// TODO Auto-generated method stub
		String rawQuery;
		rawQuery = "select * from promocion_detalle where entrada='" + entrada
				+ "' and salida='" + salida + "'";
		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		cur.moveToFirst();
		boolean existe = false;

		while (!cur.isAfterLast()) {
			existe = true;
			cur.moveToNext();
		}

		cur.close();
		db.close();
		return existe;
	}

	public String obtener_codunimedXtipo_unimed_salida(int unimed_salida,
			String codigo) {
		String rawQuery;

		if (unimed_salida == 0) {
			rawQuery = "select codunimed_almacen from producto where codpro='"+codigo+"' OR codpro = (substr('"+codigo+"',2,length('"+codigo+"')))";
		} else {
			rawQuery = "select codunimed from producto where codpro='"+codigo+"' OR codpro = (substr('"+codigo+"',2,length('"+codigo+"')))";
		}

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		String codunimed = "1";
		cur.moveToFirst();

		while (!cur.isAfterLast()) {
			codunimed = cur.getString(0);
			cur.moveToNext();
		}
		return codunimed;
	}

	// saber si el detalle_pedido se guardo con codunimed � codunimed_almacen
	// devuelve 0 si es codunimed_almacen
	// devuelve 1 si es codunimed
	public int isCodunimed_Almacen(String codpro, String cod_und_medida) {

		String rawQuery;

		rawQuery = "select codunimed,codunimed_almacen from producto where codpro='" + codpro + "'";

		String codunimed = "";
		String codunimed_almacen = "";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);

		cur.moveToFirst();

		while (!cur.isAfterLast()) {
			codunimed = cur.getString(0);
			codunimed_almacen = cur.getString(1);
			cur.moveToNext();
		}
		/*
		Log.v("(DBCLASSES)isCodunimedAlmacen", "codpro=" + codpro
				+ "---codunimed=" + codunimed + "----codunimed-almacen="
				+ codunimed_almacen + "---codunimed_enviado=" + cod_und_medida
				+ "");
		*/
		if (cod_und_medida.equals(codunimed) && cod_und_medida.equals(codunimed_almacen)) {
			Log.v("(DBCLASSES)isCodunimedAlmacen", "retorna: 0");
			return 0;
		} else if (cod_und_medida.equals(codunimed)) {
			Log.v("(DBCLASSES)isCodunimedAlmacen", "retorna: 1 unidad mayor");//retorna unidad mayor
			return 1;
		} else if (cod_und_medida.equals(codunimed_almacen)) {
			Log.v("(DBCLASSES)isCodunimedAlmacen", "retorna: 0 unidad minima"); //retorna unidad minima
			return 0;
		}

		Log.v("(DBCLASSES)isCodunimedAlmacen", "retorna: -1");
		return -1;
	}

	public void eliminar_item_promo(String oc_numero) {
		Log.d("","Eliminando item promocion del oc_numero: "+oc_numero);
		// Se eliminan todos las promociones excepto las marcadas como 'ELIM',
		// estas
		// seran eliminadas al guardar el pedido
		String where = "oc_numero=? and tipo_producto in('C','M') and cod_politica <> 'ELIM'";
		String[] args = { oc_numero };

		try {
			SQLiteDatabase db = getWritableDatabase();
			db.delete("pedido_detalle", where, args);
			db.close();

			Log.i("BDclasses :eliminar_item_promo:", "item_promo eliminados (where):"+where);

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void EliminarPedido_cabecera_enviados(String codven) {
		// TODO Auto-generated method stub

		String where = "flag <> ? or cod_emp <> ?";
		String[] args = { "P", codven };

		try {
			SQLiteDatabase db = getWritableDatabase();

			db.delete("pedido_cabecera", where, args);
			db.close();

			Log.i("eliminar pedido_cabecera_al_sincronizar",	"pedido_cabecera eliminados todos menos flag (P)");

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void EliminarPedido_detalle_enviados(String codven) {
		// TODO Auto-generated method stub

		String where = "oc_numero in (select oc_numero from pedido_cabecera where flag <> ? or cod_emp <> ?)";
		String[] args = { "P", codven };

		try {
			SQLiteDatabase db = getWritableDatabase();

			int pr = db.delete("pedido_detalle", where, args);
			db.close();

			Log.i("eliminar pedido_detalle_al_sincronizar",
					"eliminados los detalles de las cabeceras con estado <> 'P'");

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void EliminarSanVisitas(String codven) {
		// TODO Auto-generated method stub

		String where= "(oc_numero_visitado  \n" +
				"\t\t\t\tnot  in ( select oc_numero from pedido_cabecera where flag = ? and cod_emp = ? )  \n" +
				"\t\t\t\t) \n" +
				"\t\t\t\tand ( oc_numero_visitar  not in (select oc_numero from pedido_cabecera where flag = ? and cod_emp = ?) ) \n" ;

		String[] args = { "P", codven, "P", codven};

		try {
			SQLiteDatabase db = getWritableDatabase();

			int pr = db.delete(""+DBtables.San_Visitas.TAG, where, args);
			db.close();

			Log.i("eliminar pedido_detalle_al_sincronizar",
					"eliminados los detalles de las cabeceras con estado <> 'P'");

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	public void EliminarRegistro_bonificaciones_enviados(String codven) {
		// TODO Auto-generated method stub

		String where = "oc_numero in (select oc_numero from pedido_cabecera where flag <> ? or cod_emp <> ?)";
		String[] args = { "P", codven };

		try {
			SQLiteDatabase db = getWritableDatabase();

			int pr = db.delete("registro_bonificaciones", where, args);
			db.close();

			Log.i("DBclasses :EliminarRegistro_bonificaciones_enviados:", 	"Eliminados los registros bonificacion de las cabeceras con estado <> 'P'");

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public String[] Obtener_localizacion(String codcli, int item_dir) {

		String rawQuery;

		rawQuery = "select latitud,longitud from direccion_cliente where codcli='"
				+ codcli + "' and item=" + item_dir + "";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(rawQuery, null);

		String[] localizacion;

		if (cursor.getCount() != 0) {

			localizacion = new String[2];
			cursor.moveToFirst();

			if (cursor.moveToFirst()) {
				do {
					localizacion[0] = cursor.getString(0);// latitud
					localizacion[1] = cursor.getString(1);// longitud
				} while (cursor.moveToNext());
			}

		} else {
			localizacion = new String[] { "0", "0" };
		}

		cursor.close();
		db.close();

		return localizacion;
	}

	public void EliminarDepositos_enviados() {
		// TODO Auto-generated method stub

		String where = "BI_DEPO_FLAG <> ?";
		String[] args = { "P" };

		try {
			SQLiteDatabase db = getWritableDatabase();

			db.delete("depositos", where, args);
			db.close();

			Log.i("eliminar depositos_al_sincronizar",
					"depositos eliminados todos menos flag (P)");

		} catch (SQLException e) {
			e.printStackTrace();
			Log.i("eliminar depositos_al_sincronizar", "error al eliminar");
		}

	}

	public void EliminarIngresos_enviados(String codven) {
		// TODO Auto-generated method stub

		String where = "flag <> ? or substr(secitm,1,1) <> ? or username <> ? or estado = ?";
		String[] args = { "P", "M", codven, "A" };

		try {
			SQLiteDatabase db = getWritableDatabase();

			db.delete("ingresos", where, args);
			db.close();

			Log.i("eliminar ingresos_al_sincronizar",
					"ingresos eliminados todos menos flag (P)");

		} catch (SQLException e) {
			e.printStackTrace();
			Log.i("eliminar ingresos_al_sincronizar", "error al eliminar");
		}

	}

	public DB_Servidor getServicioLocal() {
		// TODO Auto-generated method stub
		String rawQuery;

		// in_serv_codigo_id=2 ---> local
		rawQuery = "select * from servidor where in_serv_item=1 and in_serv_codigo_id=2";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		DB_Servidor dbservidor = new DB_Servidor();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			dbservidor.setTX_SERV_servicioWeb(cur.getString(0));
			dbservidor.setTX_SERV_servidorBD(cur.getString(1));
			dbservidor.setTX_SERV_nombreBD(cur.getString(2));
			dbservidor.setTX_SERV_usuario(cur.getString(3));
			dbservidor.setTX_SERV_contrasena(cur.getString(4));
			dbservidor.setIN_SERV_item(Integer.parseInt(cur.getString(5)));
			dbservidor.setIN_SERV_codigo_ID(cur.getInt(6));

			cur.moveToNext();
		}
		cur.close();
		db.close();
		return dbservidor;
	}

	public DB_Servidor getServicioInternet() {
		// TODO Auto-generated method stub
		String rawQuery;

		// in_serv_codigo_id=2 ---> local
		rawQuery = "select * from servidor where in_serv_item=1 and in_serv_codigo_id=1";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		DB_Servidor dbservidor = new DB_Servidor();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			dbservidor.setTX_SERV_servicioWeb(cur.getString(0));
			dbservidor.setTX_SERV_servidorBD(cur.getString(1));
			dbservidor.setTX_SERV_nombreBD(cur.getString(2));
			dbservidor.setTX_SERV_usuario(cur.getString(3));
			dbservidor.setTX_SERV_contrasena(cur.getString(4));
			dbservidor.setIN_SERV_item(Integer.parseInt(cur.getString(5)));
			dbservidor.setIN_SERV_codigo_ID(cur.getInt(6));

			cur.moveToNext();
		}
		cur.close();
		db.close();
		return dbservidor;
	}

	public ArrayList<DBPedido_Cabecera> getTodosPedidosCabecera() {

		String rawQuery;

		rawQuery = "select * from pedido_cabecera where flag <> 'A'";

		SQLiteDatabase db = getReadableDatabase();

		Cursor cur = db.rawQuery(rawQuery, null);

		ArrayList<DBPedido_Cabecera> lista_pedidos = new ArrayList<DBPedido_Cabecera>();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			DBPedido_Cabecera dbpedido = new DBPedido_Cabecera();
			dbpedido.setOc_numero(cur.getString(0));
			dbpedido.setSitio_enfa(cur.getString(1));
			dbpedido.setMonto_total(cur.getString(2));
			dbpedido.setValor_igv(cur.getString(3));
			dbpedido.setMoneda(cur.getString(4));
			dbpedido.setFecha_oc(cur.getString(5));
			dbpedido.setFecha_mxe(cur.getString(6));
			dbpedido.setCond_pago(cur.getString(7));
			dbpedido.setCod_cli(cur.getString(8));
			dbpedido.setCod_emp(cur.getString(9));
			dbpedido.setEstado(cur.getString(10));
			dbpedido.setUsername(cur.getString(11));
			dbpedido.setRuta(cur.getString(12));
			dbpedido.setObserv(cur.getString(13));
			dbpedido.setCod_noventa(cur.getInt(14));
			dbpedido.setPeso_total(cur.getString(15));
			dbpedido.setFlag(cur.getString(16));
			dbpedido.setLatitud(cur.getString(17));
			dbpedido.setLongitud(cur.getString(18));
			dbpedido.setCodTurno(cur.getString(46));
			dbpedido.setNroletra(cur.getString(47));

			lista_pedidos.add(dbpedido);
			cur.moveToNext();
		}
		cur.close();
		db.close();
		return lista_pedidos;
	}

	public void eliminar_depositoXsec(String secuencia) {

		String where = "secuencia=?";
		String[] args = { secuencia };

		try {
			SQLiteDatabase db = getWritableDatabase();
			db.delete("depositos", where, args);
			db.close();

			Log.i("(DBCLASSES)eliminar_depositoXsec", "deposito eliminado sec:"
					+ secuencia);

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public String obtenerMensajePedido(String oc_numero) {
		// TODO Auto-generated method stub
		String rawQuery;

		rawQuery = "select ifnull(mensaje,'') from pedido_cabecera where oc_numero='"
				+ oc_numero + "'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		String mensaje = "";
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			mensaje = (cur.getString(0));

			cur.moveToNext();
		}
		cur.close();
		db.close();
		return mensaje;
	}

	public boolean verificar_contrasenia_forzar_envio(String password) {
		String rawQuery;
		rawQuery = "select usepas from usuarios where  usecod = 1";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		boolean existe = false;
		String pass = "";

		cur.moveToFirst();
		if (cur.moveToFirst()) {
			do {
				pass = cur.getString(0);
				existe = true;
			} while (cur.moveToNext());

		}
		cur.close();
		db.close();

		if (existe) {
			if (password.equals(pass.trim())) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}

	}

	public ArrayList<HashMap<String, String>> getCuotasxVendedor() {

		String rawQuery;

		rawQuery = "select secuencia,nomcuota from cuota_vendedor";

		SQLiteDatabase db = getReadableDatabase();

		Cursor cursor = db.rawQuery(rawQuery, null);

		ArrayList<HashMap<String, String>> cuotas = new ArrayList<HashMap<String, String>>();

		cursor.moveToFirst();

		if (cursor.getCount() != 0) {

			cursor.moveToFirst();

			if (cursor.moveToFirst()) {
				do {

					HashMap<String, String> data = new HashMap<String, String>();
					data.put("SECUENCIA", Integer.toString(cursor.getInt(0)));
					data.put("NOMCUOTA", cursor.getString(1));

					cuotas.add(data);

				} while (cursor.moveToNext());
			}

		}

		cursor.close();
		db.close();

		return cuotas;
	}

	public DB_CuotaVendedor getDatosCuotaVendedorXsec(int secuencia) {

		String rawQuery;

		rawQuery = "select cuota,ventas,devoluciones,ventas_efectivas,porcentajeParticipacion,porcentaje_Avance from cuota_vendedor where secuencia="
				+ secuencia + "";

		SQLiteDatabase db = getReadableDatabase();

		Cursor cursor = db.rawQuery(rawQuery, null);

		DB_CuotaVendedor cuota = new DB_CuotaVendedor();

		cursor.moveToFirst();

		if (cursor.moveToFirst()) {
			do {

				cuota.setCuota(cursor.getString(0));
				cuota.setVentas(cursor.getString(1));
				cuota.setDevoluciones(cursor.getString(2));
				cuota.setVentas_efectivas(cursor.getString(3));
				cuota.setPorcentajeParticipacion(cursor.getString(4));
				cuota.setPorcentaje_Avance(cursor.getString(5));

			} while (cursor.moveToNext());
		}

		cursor.close();
		db.close();

		return cuota;
	}

	public String obtenerUsernameByCodven(String codven) {
		// TODO Auto-generated method stub
		String rawQuery;

		rawQuery = "select usenam from usuarios where usecod=(select coduser from vendedor where codven='"
				+ codven + "') limit 1";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		String username = "";
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			username = (cur.getString(0));

			cur.moveToNext();
		}
		cur.close();
		db.close();
		return username;
	}

	public void cambiar_estado_ingreso(String secuencia, String secitm,
			String estado) {

		String where = "secuencia = ? and secitm = ?";
		String[] args = { secuencia, secitm };

		try {

			SQLiteDatabase db = getWritableDatabase();

			ContentValues reg = new ContentValues();
			reg.put("estado", estado);

			db.update(DBtables.Ingresos.TAG, reg, where, args);
			db.close();

			Log.i("CAMBIAR ESTADO PEDIDO CABECERA", "estado cambiado"
					+ secuencia + "," + secitm + "- estado" + estado);

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public String obtenerEstadoIngreso(String secuencia, String secitm) {
		// TODO Auto-generated method stub
		String rawQuery;

		rawQuery = "select estado from ingresos where secuencia='" + secuencia + "' and secitm='" + secitm + "'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		String estado = "0";
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			estado = (cur.getString(0));
			cur.moveToNext();

		}

		if (estado == null || estado.length() == 0) {
			estado = "0";
		}
		cur.close();
		db.close();

		return estado;
	}

	public void forzar_actualizacion_acuenta_ctaingresos() {

		SQLiteDatabase db = getWritableDatabase();

		String sql = "update cta_ingresos set acuenta=ifnull((select SUM(monto_afecto) from ingresos "
				+ "where secuencia=cta_ingresos.secuencia and estado not in ('A','L')),0), "
				+ "saldo_virtual=total-(ifnull((select SUM(monto_afecto) from ingresos where secuencia=cta_ingresos.secuencia "
				+ "and estado not in ('A','L')),0)) where (select count(secuencia) from ingresos "
				+ "where secuencia=cta_ingresos.secuencia and secitm like 'M%') <> 0 and (select count(secuencia) "
				+ "from ingresos where secuencia=cta_ingresos.secuencia and secitm like 's%') = 0";

		db.execSQL(sql);
		db.close();

	}

	public void actualizarPedidoDetalle(String oc_numero, String cip,
			String secuencia) {
		Log.d("actualizarPedidoDetalle","Actualizando el registro detalle de los pedidos con la secuencia generada...");
		String where = "oc_numero = ? and cip=? and tipo_producto in('V')";
		String[] args = { oc_numero, cip };

		try {

			SQLiteDatabase db = getWritableDatabase();

			ContentValues reg = new ContentValues();
			reg.put(DBtables.Pedido_detalle.SEC_PROMO, secuencia);

			db.update("pedido_detalle", reg, where, args);
			db.close();

			Log.d("actualizarPedidoDetalle","Registro detalle de los pedidos actualizado con secuencia-> "+secuencia);

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

	}

	public void actualizarTodosPedidoDetalle(String oc_numero,
			String codigoSecuencia) {

		String where = "oc_numero = ? and tipo_producto in('V')";
		String[] args = { oc_numero };

		try {

			SQLiteDatabase db = getWritableDatabase();

			ContentValues reg = new ContentValues();
			reg.put(DBtables.Pedido_detalle.SEC_PROMO, codigoSecuencia);

			db.update("pedido_detalle", reg, where, args);
			db.close();

			Log.i("actualizar_todos_pedido_detalle",
					"todos_item_pedido_detalle_actualizado");

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

	}

	public void actualizarTodosPedidoDetalle2(int secuencia, int item,
			String codigoSecuencia, String ocnumero) {

		String sql = "update pedido_detalle set sec_promo=sec_promo || '-' || '"
				+ codigoSecuencia
				+ "' "
				+ "where oc_numero='"
				+ ocnumero
				+ "' and cip in (select entrada from promocion_detalle where secuencia="
				+ secuencia
				+ " and item="
				+ item
				+ ") "
				+ "and tipo_producto = 'V'";

		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(sql);
		db.close();

	}

	public void resetCampoSecPromTodosPedidoDetalle2(int secuencia, int item,
			String ocnumero) {

		String sql = "update pedido_detalle set sec_promo=''"
				+ "where oc_numero='"
				+ ocnumero
				+ "' and cip in (select entrada from promocion_detalle where secuencia="
				+ secuencia + " and item=" + item + ") "
				+ "and tipo_producto = 'V'";

		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(sql);
		db.close();

	}

	public void marcar_item_promo_como_eliminado(String codpro, String oc_numero) {

		String where = "cip= ? and oc_numero = ?";
		String[] args = { codpro, oc_numero };

		try {

			SQLiteDatabase db = getWritableDatabase();

			ContentValues reg = new ContentValues();
			reg.put("cod_politica", "ELIM");

			db.update("pedido_detalle", reg, where, args);
			// db.delete("pedido_detalle", where, args);
			db.close();

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

	}

	// Eliminar las promociones agregadas al pedido y marcadas como ELIM
	public void eliminar_item_promo_marcados(String oc_numero) {
		Log.d(TAG, "eliminar_item_promo_marcados como ELIM");
		String where = "oc_numero = ? and cod_politica = 'ELIM'";
		String[] args = { oc_numero };

		try {

			SQLiteDatabase db = getWritableDatabase();

			db.delete("pedido_detalle", where, args);
			db.close();

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

	}

	public void eliminar_item_promo_dependientesss(String codpro, String oc_numero) {

		/*
		 * SQLiteDatabase db = getWritableDatabase();
		 * 
		 * String sql = "delete from pedido_detalle " +
		 * "where oc_numero="+oc_numero+" "+ "and tipo_producto in ('C','M') " +
		 * "and cip in (select salida from promocion_detalle where entrada='"
		 * +codpro+"')";
		 * 
		 * 
		 * db.execSQL(sql);
		 * 
		 * Log.i("ELIMINAR DEPENDIENTES DE: ", ""+codpro);
		 * 
		 * db.close();
		 */

		String where = "oc_numero = ? and tipo_producto in ('C','M') and cip in (select salida from promocion_detalle where entrada= ?)";
		String[] args = { oc_numero, codpro };

		try {

			SQLiteDatabase db = getWritableDatabase();

			int ret = db.delete("pedido_detalle", where, args);
			db.close();

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

	}

	public ArrayList<DBProductos> obtener_productos_inventario(String filtro) {

		String rawQuery = "select codpro,despro,cod_rapido from producto where codpro like '"
				+ filtro
				+ "' or "
				+ "cod_rapido like '"
				+ filtro
				+ "' or despro like '" + filtro + "'";

		SQLiteDatabase db = getReadableDatabase();

		Cursor cur = db.rawQuery(rawQuery, null);
		ArrayList<DBProductos> lista_productos = new ArrayList<DBProductos>();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			DBProductos prod = new DBProductos();
			prod.setCodpro(cur.getString(0));
			prod.setDespro(cur.getString(1));
			prod.setCod_rapido(cur.getString(2));

			lista_productos.add(prod);
			cur.moveToNext();
		}
		cur.close();
		db.close();
		return lista_productos;

	}

	public ArrayList<DBProductos> obtener_productos_inventario_xcodpro(
			String filtro) {

		String rawQuery = "select codpro,despro,cod_rapido from producto where codpro = '"
				+ filtro + "'";

		SQLiteDatabase db = getReadableDatabase();

		Cursor cur = db.rawQuery(rawQuery, null);
		ArrayList<DBProductos> lista_productos = new ArrayList<DBProductos>();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			DBProductos prod = new DBProductos();
			prod.setCodpro(cur.getString(0));
			prod.setDespro(cur.getString(1));
			prod.setCod_rapido(cur.getString(2));

			lista_productos.add(prod);
			cur.moveToNext();
		}
		cur.close();
		db.close();
		return lista_productos;

	}

	public ArrayList<DBProductos> obtener_productos_inventario_xean13(
			String filtro) {

		String rawQuery = "select codpro,despro,cod_rapido from producto where ean13 = '"
				+ filtro + "'";

		SQLiteDatabase db = getReadableDatabase();

		Cursor cur = db.rawQuery(rawQuery, null);
		ArrayList<DBProductos> lista_productos = new ArrayList<DBProductos>();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			DBProductos prod = new DBProductos();
			prod.setCodpro(cur.getString(0));
			prod.setDespro(cur.getString(1));
			prod.setCod_rapido(cur.getString(2));

			lista_productos.add(prod);
			cur.moveToNext();
		}
		cur.close();
		db.close();
		return lista_productos;

	}

	public ArrayList<DBLocales> obtener_lista_locales(String id_local) {

		String rawQuery = "select id_local,des_local from locales where id_local=?";

		String[] args = { id_local };

		SQLiteDatabase db = getReadableDatabase();

		Cursor cur = db.rawQuery(rawQuery, args);
		ArrayList<DBLocales> lista_locales = new ArrayList<DBLocales>();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			DBLocales loc = new DBLocales();
			loc.setId_local(cur.getString(0));
			loc.setDes_local(cur.getString(1));

			lista_locales.add(loc);
			cur.moveToNext();
		}
		cur.close();
		db.close();
		return lista_locales;

	}

	public ArrayList<DB_Almacenes> obtener_lista_almacenes(String id_local) {

		String rawQuery = "select * from almacenes where id_local like '"
				+ id_local + "'";

		SQLiteDatabase db = getReadableDatabase();

		Cursor cur = db.rawQuery(rawQuery, null);
		ArrayList<DB_Almacenes> lista_almacenes = new ArrayList<DB_Almacenes>();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			DB_Almacenes alm = new DB_Almacenes();
			alm.setCodalm(cur.getString(0));
			alm.setDesalm(cur.getString(1));
			alm.setId_local(cur.getString(2));

			lista_almacenes.add(alm);
			cur.moveToNext();
		}
		cur.close();
		db.close();
		return lista_almacenes;

	}

	public ArrayList<DB_DistribucionAlmacen> obtener_lista_distribucion(
			String id_local, String codalm) {

		String rawQuery = "select * from distribucion_almacenes where id_local=? and codalm=?";

		String[] args = { id_local, codalm };

		SQLiteDatabase db = getReadableDatabase();

		Cursor cur = db.rawQuery(rawQuery, args);
		ArrayList<DB_DistribucionAlmacen> lista_distribucion = new ArrayList<DB_DistribucionAlmacen>();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			DB_DistribucionAlmacen dist = new DB_DistribucionAlmacen();
			dist.setId_local(cur.getString(0));
			dist.setCodalm(cur.getString(1));
			dist.setId_disalm(cur.getInt(2));
			dist.setDescripcion(cur.getString(3));

			lista_distribucion.add(dist);
			cur.moveToNext();
		}
		cur.close();
		db.close();
		return lista_distribucion;

	}

	/*
	 * Metodo para obtener lista de productos para el formato de pedidos para el
	 * proyecto helatonys
	 */
	public ItemProducto[] obtenerListaProductosPedidos2(String codcli,
			String categoria, String subCategoria, String txtBusqueda) {

		int sec_politica = getSecPoliticaConfiguracion();

		String rawQuery;

		rawQuery = "select * from( select politica_precio2.secuencia, producto.codpro , producto.despro, politica_precio2.prepro, "
				+ "politica_precio2.prepro_unidad, producto.factor_conversion, producto.peso, ifnull(mta_kardex.stock-mta_kardex.xtemp,0) as stock, "
				+ "producto.cod_rapido,producto.codunimed_almacen,producto.percepcion "
				+ "from producto inner join politica_precio2 on producto.codpro = politica_precio2.codpro "
				+ "left join mta_kardex on mta_kardex.codpro = producto.codpro where politica_precio2.secuencia=0 "
				+ "and producto.familia like '%"
				+ categoria
				+ "%' and producto.sub_familia like '%"
				+ subCategoria
				+ "%' "
				+ "and (producto.despro like '%"
				+ txtBusqueda
				+ "%' or producto.codpro like '%"
				+ txtBusqueda
				+ "%') "
				+

				"union all "
				+

				"select politica_precio2.secuencia, producto.codpro, producto.despro, politica_precio2.prepro, politica_precio2.prepro_unidad, "
				+ "producto.factor_conversion, producto.peso, ifnull(mta_kardex.stock-mta_kardex.xtemp,0) as stock, "
				+ "producto.cod_rapido,producto.codunimed_almacen,producto.percepcion from producto "
				+ "inner join politica_precio2 on producto.codpro = politica_precio2.codpro left "
				+ "join mta_kardex on mta_kardex.codpro = producto.codpro "
				+ "where politica_precio2.secuencia=(select ifnull((select sec_politica "
				+ "from politica_cliente where codcli='"
				+ codcli
				+ "'),'"
				+ sec_politica
				+ "')) "
				+ "and producto.familia like '%"
				+ categoria
				+ "%' and producto.sub_familia like '%"
				+ subCategoria
				+ "%' "
				+ "and (producto.despro like '%"
				+ txtBusqueda
				+ "%' or producto.codpro like '%"
				+ txtBusqueda
				+ "%')) group by codpro order by despro";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(rawQuery, null);

		ItemProducto[] productos = new ItemProducto[cursor.getCount()];

		if (cursor.moveToFirst()) {
			int i = 0;
			do {

				productos[i] = new ItemProducto();
				productos[i].setSec_politica(cursor.getString(0));
				productos[i].setCodprod(cursor.getString(1));
				productos[i].setDescripcion(cursor.getString(2));
				productos[i].setPrecio(cursor.getDouble(3));
				productos[i].setPrecioUnidad(cursor.getDouble(4));
				productos[i].setFact_conv(cursor.getInt(5));
				productos[i].setPeso(cursor.getDouble(6));
				productos[i].setStock(cursor.getInt(7));
				productos[i].setCodProveedor(cursor.getString(8));
				productos[i].setCodunimed(cursor.getString(9));
				productos[i].setCantidad(0);
				productos[i].setPercepcion(cursor.getDouble(10));
				i++;
				// Log.i("getProductosPedidos2", cursor.getString(8)
				// +"codigoProveedor" );
			} while (cursor.moveToNext());
		}

		cursor.close();
		db.close();

		return productos;

	}

	// Metodo para guardar todos los detalles de pedido para el formato de
	// pedido para el proyecto helatonys (pedidos2Activity)
	public void guardarDetallesPedido(ArrayList<DBPedido_Detalle> pedidoDetalles)
			throws IllegalStateException, SQLException {

		ContentValues cv = new ContentValues();
		SQLiteDatabase db = getWritableDatabase();

		db.beginTransaction();

		try {
			for (DBPedido_Detalle detalle : pedidoDetalles) {
				cv.put(DBtables.Pedido_detalle.PK_OC_NUMERO, detalle
						.getOc_numero().trim());
				cv.put(DBtables.Pedido_detalle.PK_EAN_ITEM, detalle
						.getEan_item().trim());
				cv.put(DBtables.Pedido_detalle.CIP, detalle.getCip().trim());
				cv.put(DBtables.Pedido_detalle.PRECIO_BRUTO, detalle
						.getPrecio_bruto().trim());
				cv.put(DBtables.Pedido_detalle.PRECIO_NETO, detalle
						.getPrecio_neto().trim());
				cv.put(DBtables.Pedido_detalle.PERCEPCION, detalle
						.getPercepcion().trim());
				cv.put(DBtables.Pedido_detalle.CANTIDAD, detalle.getCantidad());
				cv.put(DBtables.Pedido_detalle.TIPO_PRODUCTO, detalle
						.getTipo_producto().trim());
				cv.put(DBtables.Pedido_detalle.UNIDAD_MEDIDA,
						detalle.getUnidad_medida());
				cv.put(DBtables.Pedido_detalle.PESO_BRUTO, detalle
						.getPeso_bruto().trim());
				cv.put(DBtables.Pedido_detalle.FLAG, detalle.getFlag().trim());
				cv.put(DBtables.Pedido_detalle.COD_POLITICA, detalle
						.getCod_politica().trim());
				cv.put(DBtables.Pedido_detalle.SEC_PROMO, detalle
						.getSec_promo().trim());
				cv.put(DBtables.Pedido_detalle.ITEM_PROMO,
						detalle.getItem_promo());
				cv.put(DBtables.Pedido_detalle.AGRUP_PROMO,
						detalle.getAgrup_promo());

				db.insertOrThrow(DBtables.Pedido_detalle.TAG, null, cv);
			}

			db.setTransactionSuccessful();
		} catch (IllegalStateException e) {
			throw new IllegalStateException(e.getMessage());
		} catch (SQLException sqe) {
			throw new SQLException();
		} finally {
			db.endTransaction();
			db.close();
		}

	}

	// Metodos Usados en la ventana 2 PedidosActivity

	public void GuardarMontoPeso_Pedido(double monto_total, double peso_total,
			String oc_numero) {

		String where = "oc_numero = ?";
		String[] args = { oc_numero };

		try {

			SQLiteDatabase db = getWritableDatabase();

			ContentValues valor = new ContentValues();
			valor.put("monto_total", monto_total);
			valor.put("peso_total", peso_total);

			db.update("pedido_cabecera", valor, where, args);
			db.close();

			Log.i("PESO TOTAL", "" + peso_total);

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public String obtenerVistaxOrdenCompra(String oc_numero) {
		String rawQuery = "select tipoVista from pedido_cabecera where oc_numero = '"+oc_numero+"' ";

		SQLiteDatabase db = getReadableDatabase();

		Cursor cur = db.rawQuery(rawQuery, null);
		String tipoVista = "no_vista";
		cur.moveToFirst();
		Log.d("DBclasses ::obtenerVistaxOrdenCompra:Query:",""+rawQuery);
		
		while (!cur.isAfterLast()) {			
			tipoVista = cur.getString(0);
			Log.d("DBclasses ::obtenerVistaxOrdenCompra::","---"+tipoVista);
			cur.moveToNext();
		}
		Log.d("DBclasses ::obtenerVistaxOrdenCompra::",""+tipoVista);
		if(tipoVista.equals("no_vista")){
			tipoVista="Vista 2";
		}
		cur.close();
		db.close();	
		
		return tipoVista;
	}
	
	public void syncFormasPago(JSONArray jArray) {
		JSONObject jsonData = null;
		ContentValues cv = new ContentValues();

		getReadableDatabase().delete(DBtables.FormaPago.TAG, null, null);
		SQLiteDatabase db = getWritableDatabase();

		db.beginTransaction();

		try {
			for (int i = 0; i < jArray.length(); i++) {
				jsonData = jArray.getJSONObject(i);
				
				cv.put(DBtables.FormaPago.CODIGO, jsonData.getString("codforpag").trim());
				cv.put(DBtables.FormaPago.DESCRIPCION, jsonData.getString("desforpag").trim());
				cv.put(DBtables.FormaPago.CODIGO_CLIENTE, jsonData.getString("codigoCliente").trim());
				cv.put(DBtables.FormaPago.FLAG_TIPO, jsonData.getString("flagTipo").trim());
								
				db.insert(DBtables.FormaPago.TAG, null, cv);
				Log.i("syncFormasPago", "i= " + i + " CODIGO: " + jsonData.getString("codforpag").trim() + " DESC: "+ jsonData.getString("desforpag").trim());
			}

			db.setTransactionSuccessful();

		} catch (JSONException e) {
			Log.e(DBclasses.TAG,
					"JSON Exception syncFormasPago:" + e.getMessage());
		} finally {
			db.endTransaction();
			db.close();
		}

	}
	
	public void syncNroLetras(JSONArray jArray) {
		JSONObject jsonData = null;
		ContentValues cv = new ContentValues();

		//getReadableDatabase().delete(DBtables.FormaPago.TAG, null, null);
		getReadableDatabase().delete(DBtables.NroLetras.TAG, null, null);
		
				
		SQLiteDatabase db = getWritableDatabase();

		db.beginTransaction();

		try {
			for (int i = 0; i < jArray.length(); i++) {
				jsonData = jArray.getJSONObject(i);
				
				cv.put(DBtables.NroLetras.NRO_LETRAS, jsonData.getString("NroLetras").trim());
				cv.put(DBtables.NroLetras.DESCRIPCION, jsonData.getString("Descripcion").trim());
								
				db.insert(DBtables.NroLetras.TAG, null, cv);
				Log.i("syncNroLetras", "i= " + i + " CODIGO: " + jsonData.getString("NroLetras").trim() + 
						" DESC: "+ jsonData.getString("Descripcion").trim());
			}

			db.setTransactionSuccessful();

		} catch (JSONException e) {
			Log.e(DBclasses.TAG,
					"JSON Exception syncNroLetras:" + e.getMessage());
		} finally {
			db.endTransaction();
			db.close();
		}
	}
	
	public void eliminarAll_Nroletras() {
		int filas=0;
		try {
			filas=getReadableDatabase().delete(DBtables.NroLetras.TAG, null, null);
			Log.i(TAG, "Se han eliminado todo los registros de la tabla "+DBtables.NroLetras.TAG+", filas eliminadas "+filas);
		} catch (Exception e) {
			Log.e(TAG, "Error al intentar eliminar todo los registros de la tabla "+DBtables.NroLetras.TAG+", filas eliminadas "+filas);
		}
		
	}
	
	
	public String getNombreVendedor(String codigoVendedor){
		String rawQuery;		
		rawQuery = "SELECT nomven FROM vendedor WHERE codven like '"+codigoVendedor+"'";
		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		
		String nombre = "No encontrado";		
		cur.moveToFirst();
		while (!cur.isAfterLast()) {			
			nombre = cur.getString(0);			
			cur.moveToNext();
		}
		Log.w(TAG+":getNombreVendedor:",nombre);
		cur.close();
		db.close();
		return nombre;
	}
	public String getNombreEmpresa(){
		String rawQuery;		
		rawQuery = "SELECT TX_EMPR_NOMBRE FROM Empresa";
		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		
		String nombre = "Empresa";		
		cur.moveToFirst();
		while (!cur.isAfterLast()) {			
			nombre = cur.getString(0);			
			cur.moveToNext();
		}
		Log.w(TAG+":getNombreEmpresa:",nombre);
		cur.close();
		db.close();
		return nombre;
	}
	public String getEmailConfiguracion(){
		String rawQuery;		
		rawQuery = "SELECT valor FROM configuracion WHERE nombre like 'correo'";
		
		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		
		String correo = "";		
		cur.moveToFirst();
		Log.w(TAG+"raw query:",rawQuery);
		while (!cur.isAfterLast()) {			
			correo = cur.getString(0);			
			cur.moveToNext();
		}
		Log.w(TAG,"Luego del query");
		Log.w(TAG+":getEmailConfiguracion:",""+correo);
		cur.close();
		db.close();
		return correo;
	}
	
	public String getRucEmpresa(){
		String rawQuery;		
		rawQuery = "SELECT "+DBtables.Empresa.TX_EMPR_RUC+" FROM Empresa";
		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		
		String ruc = "";
		cur.moveToFirst();
		while (!cur.isAfterLast()) {
			ruc = cur.getString(0);
			cur.moveToNext();
		}
		Log.w(TAG+":getRucEmpresa:",ruc);
		cur.close();
		db.close();
		return ruc;
	}
	
	/*--------------------------------------------------- CHEMA ----------------------------------------------------------*/
	public void sincronizar_registrosGeneralesMovil(JSONArray jArray) throws JSONException {

		JSONObject jsonData = null;
		ContentValues cv = new ContentValues();

		getReadableDatabase().delete(DBtables.RegistrosGeneralesMovil.TAG, null, null);
    	SQLiteDatabase db = getWritableDatabase();

		db.beginTransaction();

		try {
			
			for (int i = 0; i < jArray.length(); i++) {
	    		jsonData = jArray.getJSONObject(i);
	    	        
		        cv.put(DBtables.RegistrosGeneralesMovil.CODIGO, 		jsonData.getString(DBtables.RegistrosGeneralesMovil.CODIGO).trim());  
		        cv.put(DBtables.RegistrosGeneralesMovil.DESCRIPCION, 	jsonData.getString(DBtables.RegistrosGeneralesMovil.DESCRIPCION).trim());  
		        cv.put(DBtables.RegistrosGeneralesMovil.CODIGO_VALOR, 	jsonData.getString(DBtables.RegistrosGeneralesMovil.CODIGO_VALOR).trim());  
		        cv.put(DBtables.RegistrosGeneralesMovil.VALOR, 			jsonData.getString(DBtables.RegistrosGeneralesMovil.VALOR).trim());	          
		        db.insert(DBtables.RegistrosGeneralesMovil.TAG, null, cv);
		        
		        Log.i(TAG+":sincronizar_registrosGeneralesMovil:", i+" "+jsonData.getString(DBtables.RegistrosGeneralesMovil.DESCRIPCION).trim()+"  "+jsonData.getString(DBtables.RegistrosGeneralesMovil.VALOR));
	    	 }
	    	db.setTransactionSuccessful();
	    	
		} catch (JSONException e) {
			Log.e(DBclasses.TAG, "JSON Exception CLIENTE:" + e.getMessage());
			throw new JSONException(e.getMessage());
		} finally {
			db.endTransaction();
			db.close();
		}
	}
	
	public int sincronizar_lugarEntrega(JSONArray jArray, String fecha, int start) throws JSONException {

		JSONObject jsonData = null;
		ContentValues cv = new ContentValues();

		if (start==0 && fecha.equals("TODOS")){
			getReadableDatabase().delete(DBtables.LugarEntrega.TAG, null, null);
		}
    	SQLiteDatabase db = getWritableDatabase();

		db.beginTransaction();

		try {
			
			for (int i = 0; i < jArray.length(); i++) {
	    		jsonData = jArray.getJSONObject(i);
	    	        
		        cv.put(DBtables.LugarEntrega.CODIGO_CLIENTE, 	jsonData.getString(DBtables.LugarEntrega.CODIGO_CLIENTE).trim());  
		        cv.put(DBtables.LugarEntrega.ITEM_SUCURSAL, 	jsonData.getString(DBtables.LugarEntrega.ITEM_SUCURSAL).trim());  
		        cv.put(DBtables.LugarEntrega.CODIGO_LUGAR, 		jsonData.getString(DBtables.LugarEntrega.CODIGO_LUGAR).trim());  
		        cv.put(DBtables.LugarEntrega.DIRECCION, 		jsonData.getString(DBtables.LugarEntrega.DIRECCION).trim());
		        cv.put(DBtables.LugarEntrega.INDICADOR_DESPACHO,jsonData.getString(DBtables.LugarEntrega.INDICADOR_DESPACHO).trim());
		        cv.put(DBtables.LugarEntrega.INDICADOR_COBRANZA,jsonData.getString(DBtables.LugarEntrega.INDICADOR_COBRANZA).trim());
		        cv.put(DBtables.LugarEntrega.DIRECCION_ENTREGA, jsonData.getString(DBtables.LugarEntrega.DIRECCION_ENTREGA).trim());
		        cv.put(DBtables.LugarEntrega.CODIGO_UBIGEO, 	jsonData.getString(DBtables.LugarEntrega.CODIGO_UBIGEO).trim());
		        cv.put(DBtables.LugarEntrega.CODIGO_DISTRITO, 	jsonData.getString(DBtables.LugarEntrega.CODIGO_DISTRITO).trim());
		       
		        //db.insert(DBtables.LugarEntrega.TAG, null, cv);
				db.insertWithOnConflict(DBtables.LugarEntrega.TAG, null, cv,SQLiteDatabase.CONFLICT_IGNORE);

		        Log.i(TAG+":sincronizar_lugarEntrega:", i+" "+jsonData.getString(DBtables.LugarEntrega.CODIGO_LUGAR).trim()+"  "+jsonData.getString(DBtables.LugarEntrega.DIRECCION_ENTREGA).trim());
	    	 }
	    	db.setTransactionSuccessful();
	    	
		} catch (JSONException e) {
			Log.e(TAG+":sincronizar_lugarEntrega:", "JSON Exception:" + e.getMessage());
			throw new JSONException(e.getMessage());
		} finally {
			db.endTransaction();
			db.close();
		}

		return  jArray.length();
	}
	
	public void sincronizar_obra(JSONArray jArray) throws JSONException {

		JSONObject jsonData = null;
		ContentValues cv = new ContentValues();

		getReadableDatabase().delete(DBtables.Obra.TAG, null, null);
    	SQLiteDatabase db = getWritableDatabase();

		db.beginTransaction();

		try {
			
			for (int i = 0; i < jArray.length(); i++) {
	    		jsonData = jArray.getJSONObject(i);
	    	        
		        cv.put(DBtables.Obra.CODIGO_CLIENTE, jsonData.getString(DBtables.Obra.CODIGO_CLIENTE).trim());  
		        cv.put(DBtables.Obra.CODIGO_VENDEDOR,jsonData.getString(DBtables.Obra.CODIGO_VENDEDOR).trim());  
		        cv.put(DBtables.Obra.CODIGO_OBRA,	 jsonData.getString(DBtables.Obra.CODIGO_OBRA).trim());  
		        cv.put(DBtables.Obra.OBRA, 			 jsonData.getString(DBtables.Obra.OBRA).trim());		        
		        db.insert(DBtables.Obra.TAG, null, cv);
		        
		        Log.i(TAG+":sincronizar_obra:", i+" "+jsonData.getString(DBtables.Obra.CODIGO_OBRA).trim()+"  "+jsonData.getString(DBtables.Obra.OBRA).trim());
	    	 }
	    	db.setTransactionSuccessful();
	    	
		} catch (JSONException e) {
			Log.e(TAG+":sincronizar_obra:", "JSON Exception:" + e.getMessage());
			throw new JSONException(e.getMessage());
		} finally {
			db.endTransaction();
			db.close();
		}

	}
	
	public int sincronizar_transporte(JSONArray jArray,String fecha, int start) throws JSONException {

		JSONObject jsonData = null;
		ContentValues cv = new ContentValues();

		if (start==0 && fecha.equals("TODOS")){
			getReadableDatabase().delete(DBtables.Transporte.TAG, null, null);
		}
    	SQLiteDatabase db = getWritableDatabase();

		db.beginTransaction();

		try {
			
			for (int i = 0; i < jArray.length(); i++) {
	    		jsonData = jArray.getJSONObject(i);
	    	        
		        cv.put(DBtables.Transporte.CODIGO_CLIENTE, 			jsonData.getString(DBtables.Transporte.CODIGO_CLIENTE).trim());  
		        cv.put(DBtables.Transporte.ITEM_SUCURSAL,			jsonData.getString(DBtables.Transporte.ITEM_SUCURSAL).trim());  
		        cv.put(DBtables.Transporte.CODIGO_TRANSPORTE,		jsonData.getString(DBtables.Transporte.CODIGO_TRANSPORTE).trim());  
		        cv.put(DBtables.Transporte.DESCRIPCION_TRANSPORTE, 	jsonData.getString(DBtables.Transporte.DESCRIPCION_TRANSPORTE).trim());		        
		        //db.insert(DBtables.Transporte.TAG, null, cv);
				db.insertWithOnConflict(DBtables.Transporte.TAG, null, cv,SQLiteDatabase.CONFLICT_IGNORE);
		        
		        Log.i(TAG+":sincronizar_transporte:", i+" "+jsonData.getString(DBtables.Transporte.CODIGO_TRANSPORTE).trim()+"  "+jsonData.getString(DBtables.Transporte.DESCRIPCION_TRANSPORTE).trim());
	    	 }
	    	db.setTransactionSuccessful();
	    	
		} catch (JSONException e) {
			Log.e(TAG+":sincronizar_transporte:", "JSON Exception:" + e.getMessage());
			throw new JSONException(e.getMessage());
		} finally {
			db.endTransaction();
			db.close();
		}
	return jArray.length();
	}
	
	
	public void sincronizar_grupoProducto(JSONArray jArray) throws JSONException {

		JSONObject jsonData = null;
		ContentValues cv = new ContentValues();

		getReadableDatabase().delete(DBtables.GrupoProducto.TAG, null, null);
    	SQLiteDatabase db = getWritableDatabase();

		db.beginTransaction();

		try {
			
			for (int i = 0; i < jArray.length(); i++) {
	    		jsonData = jArray.getJSONObject(i);
	    	        
		        cv.put(DBtables.GrupoProducto.CODIGO_GRUPO,	jsonData.getString(DBtables.GrupoProducto.CODIGO_GRUPO).trim());  
		        cv.put(DBtables.GrupoProducto.DESCRIPCION,	jsonData.getString(DBtables.GrupoProducto.DESCRIPCION).trim());		        
		        db.insert(DBtables.GrupoProducto.TAG, null, cv);
		        
		        Log.i(TAG+":sincronizar_grupoProducto:", i+" "+jsonData.getString(DBtables.GrupoProducto.CODIGO_GRUPO).trim()+" - "+jsonData.getString(DBtables.GrupoProducto.DESCRIPCION).trim());
	    	 }
	    	db.setTransactionSuccessful();
	    	
		} catch (JSONException e) {
			Log.e(TAG+":sincronizar_grupoProducto:", "JSON Exception:" + e.getMessage());
			throw new JSONException(e.getMessage());
		} finally {
			db.endTransaction();
			db.close();
		}
	}
	
	public String getAlmacenDescripcionResumen(String codigoAlmacen) {
		String rawQuery = "select descripcionCorta from almacenes where codalm = '"+codigoAlmacen+"' ";
		SQLiteDatabase db = getReadableDatabase();

		Cursor cur = db.rawQuery(rawQuery, null);
		String valor = "";
		cur.moveToFirst();
				
		while (!cur.isAfterLast()) {			
			valor = cur.getString(0);			
			cur.moveToNext();
		}
		Log.d("DBclasses ::getAlmacenDescripcion::",""+valor);
		
		cur.close();
		db.close();	
		
		return valor;
	}
	
	public ArrayList<DBPedido_Cabecera> getPedidoCabecera_toReporte(String cod_cabecera) {
		// TODO Auto-generated method stub

		String rawQuery;

		rawQuery = 
				"SELECT " 
				+ "oc_numero,"
				+ "monto_total,"
				+ "percepcion_total,"				
				+ "f.desforpag,"
				+ "ifnull(m.descripcion,''),"
				+ "p.fecha_oc,"
				+ "p.fecha_mxe "
				+ "FROM pedido_cabecera p "
				+ "INNER JOIN forma_pago f ON p.cond_pago = f.codforpag "
				+ "LEFT JOIN moneda m ON p.moneda = m.codigoMoneda "
				+ "where oc_numero = '"+cod_cabecera+"' and f.codigoCliente = cod_cli";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		ArrayList<DBPedido_Cabecera> lista_pedidos = new ArrayList<DBPedido_Cabecera>();
		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			DBPedido_Cabecera dbpedido = new DBPedido_Cabecera();
			dbpedido.setOc_numero(cur.getString(0));			
			dbpedido.setMonto_total(cur.getString(1));
			dbpedido.setPercepcion_total(cur.getString(2));
			dbpedido.setCond_pago(cur.getString(3));
			dbpedido.setMoneda(cur.getString(4));
			dbpedido.setFecha_oc(cur.getString(5));
			dbpedido.setFecha_mxe(cur.getString(6));			
			lista_pedidos.add(dbpedido);
			cur.moveToNext();
		}
		cur.close();
		db.close();
		return lista_pedidos;
	}
	
	public DBPedido_Cabecera getRegistroPedidoCabecera(String oc_numero) {

		String rawQuery = "select * from pedido_cabecera where oc_numero = '"+ oc_numero + "'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(rawQuery, null);
		DBPedido_Cabecera pc = new DBPedido_Cabecera();

		cursor.moveToFirst();
		if (cursor.moveToFirst()) {
			do {				
				pc.setOc_numero(cursor.getString(0));
				pc.setSitio_enfa(cursor.getString(1));
				pc.setMonto_total(cursor.getString(2));
				pc.setPercepcion_total(cursor.getString(3));
				pc.setValor_igv(cursor.getString(4));
				pc.setMoneda(cursor.getString(5));
				pc.setFecha_oc(cursor.getString(6));
				pc.setFecha_mxe(cursor.getString(7));
				pc.setCond_pago(cursor.getString(8));
				pc.setCod_cli(cursor.getString(9));
				pc.setCod_emp(cursor.getString(10));
				pc.setEstado(cursor.getString(11));
				pc.setUsername(cursor.getString(12));
				pc.setRuta(cursor.getString(13));
				pc.setObservacion(cursor.getString(14));
				pc.setCod_noventa(cursor.getInt(15));
				pc.setPeso_total(cursor.getString(16));
				pc.setFlag(cursor.getString(17));
				pc.setLatitud(cursor.getString(18));
				pc.setLongitud(cursor.getString(19));
				pc.setCodigo_familiar(cursor.getString(20));
				pc.setDT_PEDI_FECHASERVIDOR(cursor.getString(21));
				pc.setTotalSujetopercepcion(cursor.getString(22));
				//23 tipo_vista
				pc.setNumeroOrdenCompra(cursor.getString(24));
				pc.setCodigoPrioridad(cursor.getString(25));
				pc.setCodigoSucursal(cursor.getString(26));
				pc.setCodigoPuntoEntrega(cursor.getString(27));
				pc.setCodigoTipoDespacho(cursor.getString(28));
				pc.setFlagEmbalaje(cursor.getString(29));
				pc.setFlagPedido_Anticipo(cursor.getString(30));
				pc.setCodigoTransportista(cursor.getString(31));
				pc.setCodigoAlmacen(cursor.getString(32));				
				pc.setObservacion2(cursor.getString(33));
				pc.setObservacion3(cursor.getString(34));
				pc.setObservacionDescuento(cursor.getString(35));
				pc.setObservacionTipoProducto(cursor.getString(36));
				
				pc.setFlagDescuento(cursor.getString(37));
				pc.setCodigoObra(cursor.getString(38));
				pc.setFlagDespacho(cursor.getString(39));
				pc.setDocAdicional(cursor.getString(40));
				pc.setSubTotal(cursor.getString(41));
				pc.setTipoDocumento(cursor.getString(42));
				pc.setTipoRegistro(cursor.getString(43));
				pc.setDiasVigencia(cursor.getString(44));		
				//String ccd=cursor.getString(cursor.getColumnIndex("nro_letra"));
				//pc.setPedidoAnterior(cursor.getString(45));	
				pc.setCodTurno(cursor.getString(46));
				pc.setNroletra(cursor.getString(47));
				pc.setObservacion4(cursor.getString(48));
				
			} while (cursor.moveToNext());

		}
		cursor.close();
		db.close();

		return pc;
	}
	
	public void sincronizar_moneda(JSONArray jArray) throws JSONException {

		JSONObject jsonData = null;
		ContentValues cv = new ContentValues();

		getReadableDatabase().delete(DBtables.Moneda.TAG, null, null);
    	SQLiteDatabase db = getWritableDatabase();

		db.beginTransaction();

		try {
			
			for (int i = 0; i < jArray.length(); i++) {
	    		jsonData = jArray.getJSONObject(i);
	    	        
		        cv.put(DBtables.Moneda.CODIGO_MONEDA,	jsonData.getString(DBtables.Moneda.CODIGO_MONEDA).trim());  
		        cv.put(DBtables.Moneda.DESCRIPCION,	jsonData.getString(DBtables.Moneda.DESCRIPCION).trim());
		        cv.put(DBtables.Moneda.CODIGO_EQUIVALENTE,	jsonData.getString(DBtables.Moneda.CODIGO_EQUIVALENTE).trim());
		        db.insert(DBtables.Moneda.TAG, null, cv);
		        
		        Log.i(TAG+":sincronizar_moneda:", "("+i+") "+jsonData.getString(DBtables.Moneda.CODIGO_MONEDA).trim()+" - "+jsonData.getString(DBtables.Moneda.DESCRIPCION).trim());
	    	 }
	    	db.setTransactionSuccessful();
	    	
		} catch (JSONException e) {
			Log.e(TAG+":sincronizar_moneda:", "JSON Exception:" + e.getMessage());
			throw new JSONException(e.getMessage());
		} finally {
			db.endTransaction();
			db.close();
		}
	}
	
	public void sincronizar_tipoProducto(JSONArray jArray) throws JSONException {

		JSONObject jsonData = null;
		ContentValues cv = new ContentValues();

		getReadableDatabase().delete(DBtables.TipoProducto.TAG, null, null);
    	SQLiteDatabase db = getWritableDatabase();

		db.beginTransaction();

		try {
			
			for (int i = 0; i < jArray.length(); i++) {
	    		jsonData = jArray.getJSONObject(i);
	    	        
		        cv.put(DBtables.TipoProducto.CODIGO_TIPO,	jsonData.getString(DBtables.TipoProducto.CODIGO_TIPO).trim());  
		        cv.put(DBtables.TipoProducto.DESCRIPCION,	jsonData.getString(DBtables.TipoProducto.DESCRIPCION).trim());
		        cv.put(DBtables.TipoProducto.COLOR,			jsonData.getString(DBtables.TipoProducto.COLOR).trim());
		        db.insert(DBtables.TipoProducto.TAG, null, cv);
		        
		        Log.i(TAG+":sincronizar_tipoProducto:", i+" "+jsonData.getString(DBtables.TipoProducto.CODIGO_TIPO).trim()+" - "+jsonData.getString(DBtables.TipoProducto.DESCRIPCION).trim());
	    	 }
	    	db.setTransactionSuccessful();
	    	
		} catch (JSONException e) {
			Log.e(TAG+":sincronizar_tipoProducto:", "JSON Exception:" + e.getMessage());
			throw new JSONException(e.getMessage());
		} finally {
			db.endTransaction();
			db.close();
		}
	}
	
	public void updatePedidoDetalle(String oc_numero, ItemProducto item){
		String where = "oc_numero = ? and cip = ?";
		String[] args = { oc_numero , item.getCodprod() };

		try {
			SQLiteDatabase db = getWritableDatabase();
			ContentValues valor = new ContentValues();
			valor.put(DBtables.Pedido_detalle.DESCUENTO, item.getDescuento());
			valor.put(DBtables.Pedido_detalle.PRECIO_BRUTO, item.getPrecio());
			valor.put(DBtables.Pedido_detalle.PRECIO_NETO, item.getSubtotal());
			valor.put(DBtables.Pedido_detalle.PERCEPCION, item.getPercepcion());
			
			db.update(DBtables.Pedido_detalle.TAG, valor, where, args);
			db.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	public String getCodigoDistrito(String codigoCliente, String codigoSucursal, String codigoLugarEntrega) {
		String rawQuery = "select codigoDistrito from lugarEntrega where codigoCliente like '"+codigoCliente+"' and itemSucursal like '"+codigoSucursal+"' and codigoLugar like '"+codigoLugarEntrega+"'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(rawQuery, null);
		String codigoDistrito = "";

		cursor.moveToFirst();
		if (cursor.moveToFirst()) {
			do {				
				codigoDistrito = cursor.getString(0);
			} while (cursor.moveToNext());

		}
		cursor.close();
		db.close();

		return codigoDistrito;
	}
	public String getCodigoUbigeo(String codigoCliente, String codigoSucursal, String codigoLugarEntrega) {
		String rawQuery = "select codigoUbigeo from lugarEntrega where codigoCliente like '"+codigoCliente+"' and itemSucursal like '"+codigoSucursal+"' and codigoLugar like '"+codigoLugarEntrega+"'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(rawQuery, null);
		String codigoUbigeo = "";

		cursor.moveToFirst();
		if (cursor.moveToFirst()) {
			do {				
				codigoUbigeo = cursor.getString(0);
			} while (cursor.moveToNext());

		}
		cursor.close();
		db.close();

		return codigoUbigeo;
	}
	
	public void syncPromocionUbigeo(JSONArray jArray) {

		JSONObject jsonData = null;
		ContentValues cv = new ContentValues();

		getReadableDatabase().delete(DBtables.Promocion_ubigeo.TAG, null,null);
		SQLiteDatabase db = getWritableDatabase();

		db.beginTransaction();
		try {
			for (int i = 0; i < jArray.length(); i++) {
				jsonData = jArray.getJSONObject(i);
				cv.put(DBtables.Promocion_ubigeo.SEC_PROMOCION, jsonData.getString(DBtables.Promocion_ubigeo.SEC_PROMOCION).trim());
				cv.put(DBtables.Promocion_ubigeo.CODIGO_UBIGEO,jsonData.getString(DBtables.Promocion_ubigeo.CODIGO_UBIGEO).trim());

				db.insert(DBtables.Promocion_ubigeo.TAG, null, cv);
				Log.i("PROMOCION UBIGEO", "i= " + i + " SEC_PROMOCION: "+ jsonData.getString("sec_promocion") + " CODIGO_UBIGEO:"+ jsonData.getString(DBtables.Promocion_ubigeo.CODIGO_UBIGEO).trim());
			}

			db.setTransactionSuccessful();

		} catch (JSONException e) {
			Log.e(DBclasses.TAG,"JSON Exception PROMOCION UBIGEO:" + e.getMessage());
		} finally {
			db.endTransaction();
			db.close();
		}

	}
	
	/*--------------------------------------------------- CHEMA ----------------------------------------------------------*/
	

	public ArrayList<HashMap<String, String>> getCtas_Ingresos_ResumenxCliente(String codvendedor) {
		String rawQuery;

		String fechaConfig = getFecha2().trim();
		
		rawQuery = "select cl.codcli, "
				+ "ifnull(cl.nomcli,'Sin nombre') as nombre, "
				+ "ifnull( ( select ci.total from cta_ingresos_resumen ci where ci.codmon =1 and ci.codcli= cl.codcli and Forma like '%Cobrar%' ),0 ) as totalSoles, "
				+ "ifnull( (select ci.total from cta_ingresos_resumen ci where ci.codmon =2 and ci.codcli= cl.codcli  and Forma like '%Cobrar%' ),0 ) as totalDolares, "
				+ "case afecto when 1 then ( select count(ci.secuencia) from cta_ingresos_resumen ci where ci.codcli = cl.codcli ) else 0 end "
				+ "as cantidad , (select count(ci.secuencia) as cant_entrega from cta_ingresos_resumen ci where ci.forma like '%Entregar%' "
				+ "and ci.codcli=cl.codcli) as cant_entregar, "
				+ "(select count(ci.secuencia) as cant_entrega from cta_ingresos_resumen ci where ci.forma like '%Aceptar%' "
				+ "and ci.codcli=cl.codcli) as cant_aceptar,flagCobranza, "
				+ "ifnull( ( select ci.saldo from cta_ingresos_resumen ci where ci.codmon =1 and ci.codcli= cl.codcli and Forma like '%Cobrar%' ),0 ) as totalSaldoSoles, "
				+ "ifnull( (select ci.saldo from cta_ingresos_resumen ci where ci.codmon =2 and ci.codcli= cl.codcli  and Forma like '%Cobrar%' ),0 ) as totalSaldoDolares "
				+ "from cliente cl where ((totalSoles!=0 or totalDolares !=0) or (totalSaldoSoles!=0 or totalSaldoDolares !=0)) " +
				"and cl.codcli in (" +
				" select znf.codcli from znf_programacion_clientes znf where znf.codven='"+codvendedor+"' ) "
				+ "order by flagCobranza desc";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);

		Log.i(TAG, "getCtas_Ingresos_ResumenxCliente:: SQL: "+rawQuery);
		ArrayList<HashMap<String, String>> lista = new ArrayList<HashMap<String, String>>();

		cur.moveToFirst();

		while (!cur.isAfterLast()) {

			HashMap<String, String> obj = new HashMap<String, String>();
			// obj.put("codcli", cur.getString(1));
			obj.put("codigo", cur.getString(0));
			obj.put("cliente", cur.getString(1));
			obj.put("total",""+ GlobalFunctions.redondear(Double.parseDouble(cur.getString(2)))); // Deuda en Soles
			obj.put("totalSaldoSoles",""+ GlobalFunctions.redondear(Double.parseDouble(cur.getString(cur.getColumnIndex("totalSaldoSoles"))))); // Deuda saldo en Soles
			obj.put("total_acuenta",""+ GlobalFunctions.redondear(Double.parseDouble(cur.getString(3)))); // Deuda en Dolares
			obj.put("totalSaldoDolares",""+ GlobalFunctions.redondear(Double.parseDouble(cur.getString(cur.getColumnIndex("totalSaldoDolares"))))); // Deuda saldo en Dolares
			obj.put("cantidad", cur.getString(4));// Cantidad de cobranzas
													// obligatorias
			obj.put("entregar", cur.getString(5));
			obj.put("aceptar", cur.getString(6));
			obj.put("asignado", cur.getString(7));

			Log.w("Cobranzas codcli-nomcli",
					cur.getString(0) + "-" + cur.getString(1)+"-"+cur.getString(7));

			lista.add(obj);
			cur.moveToNext();
		}
		cur.close();
		db.close();
		return lista;
	}
	

	public void syncCta_ingresos_resumen(JSONArray jArray) {

		JSONObject jsonData = null;
		ContentValues cv = new ContentValues();

		getReadableDatabase().delete(DBtables.cta_ingresos_resumen.TAG, null,null);

		SQLiteDatabase db = getWritableDatabase();

		db.beginTransaction();

		try {

			for (int i = 0; i < jArray.length(); i++) {

				jsonData = jArray.getJSONObject(i);
				cv.put(DBtables.cta_ingresos_resumen.PK_SECUENCIA, jsonData.getString("secuencia").trim());
				cv.put(DBtables.cta_ingresos_resumen.CODMON, jsonData.getString("codmon").trim());
				cv.put(DBtables.cta_ingresos_resumen.CODDOC, jsonData.getString("coddoc").trim());
				cv.put(DBtables.cta_ingresos_resumen.SERIE_DOC, jsonData.getString("serie_doc").trim());
				cv.put(DBtables.cta_ingresos_resumen.NUMERO_FACTURA, jsonData .getString("numero_factura").trim());
				cv.put(DBtables.cta_ingresos_resumen.TOTAL, jsonData.getString("total") .trim());
				cv.put(DBtables.cta_ingresos_resumen.ACUENTA, jsonData.getString("acuenta").trim());
				cv.put(DBtables.cta_ingresos_resumen.SALDO, jsonData.getString("saldo") .trim());
				cv.put(DBtables.cta_ingresos_resumen.FECCOM, jsonData.getString("feccom").trim());
				cv.put(DBtables.cta_ingresos_resumen.CODCLI, jsonData.getString("codcli").trim());
				cv.put(DBtables.cta_ingresos_resumen.USERNAME, jsonData.getString("username").trim());
				cv.put(DBtables.cta_ingresos_resumen.FECOPERACION, jsonData.getString("fecoperacion").trim());
				cv.put(DBtables.cta_ingresos_resumen.CODVEN, jsonData.getString("codven").trim());
				cv.put(DBtables.cta_ingresos_resumen.SALDO_VIRTUAL, jsonData.getString("saldo_virtual").trim());
				cv.put(DBtables.cta_ingresos_resumen.FORMA, jsonData.getString("forma") .trim());

				db.insert(DBtables.cta_ingresos_resumen.TAG, null, cv);
				
				Log.i("CTA_INGRESOS", "i= " + i + " SECUENCIA: "
						+ jsonData.getString("secuencia").trim()
						+ " SERIE_DOC: "
						+ jsonData.getString("serie_doc").trim() + "  CODVEN: "
						+ jsonData.getString("codven").trim());
			}

			db.setTransactionSuccessful();

		} catch (JSONException e) {
			Log.e(DBclasses.TAG, "JSON Exception CTA_INGRESOS RESUMEN:" + e.getMessage());
		} finally {
			db.endTransaction();
			db.close();
		}

	}
		
	public void syncRegistroBonificacionPendientes(JSONArray jArray, String codven)	throws JSONException, SQLiteException {
		DAO_RegistroBonificaciones DAOBonificaciones = new DAO_RegistroBonificaciones(this._context);
		
		ArrayList<String> listaRegistros = DAOBonificaciones.getCodigoRegistros(codven);
		
		JSONObject jsonData_reg = null;
		ContentValues cv2 = new ContentValues();

		//EliminarRegistro_bonificaciones_enviados(codven);
		SQLiteDatabase db = getWritableDatabase();

		db.beginTransaction();

		try {

			for (int i = 0; i < jArray.length(); i++) {

				jsonData_reg = jArray.getJSONObject(i);
				Log.d("DBclasses ::syncRegistroBonificacionPendientes::","**************************************************************");				
				boolean existe = false;
				
				for (int j = 0; j < listaRegistros.size(); j++) {
					if (listaRegistros.get(j).equals(jsonData_reg.getString("codigoRegistro").trim())) {
						existe = true;
					}
				}				
				
				if (existe==false) {
					cv2.put(DBtables.TB_registro_bonificaciones.OC_NUMERO, jsonData_reg.getString("oc_numero"));
					cv2.put(DBtables.TB_registro_bonificaciones.ITEM, jsonData_reg.getInt("item"));
					cv2.put(DBtables.TB_registro_bonificaciones.SECUENCIA, jsonData_reg.getInt("secuenciaPromocion"));
					cv2.put(DBtables.TB_registro_bonificaciones.AGRUPADO, jsonData_reg.getInt("agrupado"));
					cv2.put(DBtables.TB_registro_bonificaciones.ENTRADA, jsonData_reg.getString("entrada"));
					cv2.put(DBtables.TB_registro_bonificaciones.TIPO_UNIMED_ENTRADA, jsonData_reg.getInt("tipo_unimed_entrada"));
					cv2.put(DBtables.TB_registro_bonificaciones.UNIMED_ENTRADA, jsonData_reg.getString("unimedEntrada"));
					cv2.put(DBtables.TB_registro_bonificaciones.CANTIDAD_ENTRADA, jsonData_reg.getInt("cantidadEntrada"));
					cv2.put(DBtables.TB_registro_bonificaciones.MONTO_ENTRADA, jsonData_reg.getString("montoEntrada"));
					cv2.put(DBtables.TB_registro_bonificaciones.SALIDA, jsonData_reg.getString("salida"));
					cv2.put(DBtables.TB_registro_bonificaciones.TIPO_UNIMED_SALIDA, jsonData_reg.getInt("tipo_unimed_salida"));
					cv2.put(DBtables.TB_registro_bonificaciones.CANTIDAD_SALIDA, jsonData_reg.getInt("cantidadSalida"));
					cv2.put(DBtables.TB_registro_bonificaciones.ACUMULADO, jsonData_reg.getInt("acumulado"));
					cv2.put(DBtables.TB_registro_bonificaciones.CANTIDAD_DISPONIBLE, jsonData_reg.getInt("cantidadDisponible"));
					cv2.put(DBtables.TB_registro_bonificaciones.MONTO_DISPONIBLE, jsonData_reg.getString("montoDisponible"));
					cv2.put(DBtables.TB_registro_bonificaciones.FLAG, jsonData_reg.getInt("flagUltimo"));
					
					cv2.put(DBtables.TB_registro_bonificaciones.CODIGO_REGISTRO, jsonData_reg.getString(DBtables.TB_registro_bonificaciones.CODIGO_REGISTRO));
					cv2.put(DBtables.TB_registro_bonificaciones.CANTIDAD_TOTAL, jsonData_reg.getString(DBtables.TB_registro_bonificaciones.CANTIDAD_TOTAL));
					cv2.put(DBtables.TB_registro_bonificaciones.SALDO_ANTERIOR, jsonData_reg.getString(DBtables.TB_registro_bonificaciones.SALDO_ANTERIOR));
					cv2.put(DBtables.TB_registro_bonificaciones.CANTIDAD_ENTREGADA, jsonData_reg.getString(DBtables.TB_registro_bonificaciones.CANTIDAD_ENTREGADA));
					cv2.put(DBtables.TB_registro_bonificaciones.SALDO, jsonData_reg.getString(DBtables.TB_registro_bonificaciones.SALDO));
					cv2.put(DBtables.TB_registro_bonificaciones.CODIGO_ANTERIOR, jsonData_reg.getString(DBtables.TB_registro_bonificaciones.CODIGO_ANTERIOR));
					cv2.put(DBtables.TB_registro_bonificaciones.CODIGO_VENDEDOR, jsonData_reg.getString(DBtables.TB_registro_bonificaciones.CODIGO_VENDEDOR));
					cv2.put(DBtables.TB_registro_bonificaciones.CODIGO_CLIENTE, jsonData_reg.getString(DBtables.TB_registro_bonificaciones.CODIGO_CLIENTE));
					
					db.insert(DBtables.TB_registro_bonificaciones.TAG, null, cv2);
					Log.i("DBclasses ::syncRegistroBonificacionPendientes::","CodigoRegistro: " + jsonData_reg.getString("codigoRegistro").trim()+" insertado");
				}else{
					Log.i("DBclasses ::syncRegistroBonificacionPendientes::","CodigoRegistro: " + jsonData_reg.getString("codigoRegistro").trim()+" ya existe");
				}
				Log.d("DBclasses ::syncRegistroBonificacionPendientes::","**************************************************************");								
			}

			db.setTransactionSuccessful();

		} catch (JSONException e) {
			Log.e(DBclasses.TAG,	"JSON Exception RESPUESTA DE PEDIDOS:" + e.getMessage());
			throw new JSONException(e.getMessage());
		} catch (SQLiteException e) {
			Log.e(DBclasses.TAG,	"SQLITE Exception RESPUESTA DE PEDIDOS:" + e.getMessage());
			throw new SQLiteException(e.getMessage());
		} finally {
			db.endTransaction();
			db.close();
		}

	}
	
	public void EliminarRegistroBonificacion(String oc) {
		// TODO Auto-generated method stub
		String where = "oc_numero=?";
		String[] args = { oc };

		try {
			SQLiteDatabase db = getWritableDatabase();

			db.delete(DBtables.TB_registro_bonificaciones.TAG, where, args);
			db.close();

			Log.i("EliminarRegistroBonificacion", "registro "+oc+" eliminado");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void eliminarNulos(){
		String where = "cod_noventa <1  and (percepcion_total ISNULL or monto_total ISNULL) ";
		String[] args = { "E" };

		try {
			SQLiteDatabase db = getWritableDatabase();
			db.delete("pedido_cabecera", where, null);
			db.close();

			Log.i("eliminarNulos","pedido eliminado");

		} catch (SQLException e) {
			e.printStackTrace();
		}
		//String query = "delete from pedido_cabecera where percepcion_total ISNULL or monto_total ISNULL";aa
	}
	
	public void syncMotivo(JSONArray jArray) {

		JSONObject jsonData = null;
		ContentValues cv = new ContentValues();

		getReadableDatabase().delete(DBtables.Motivo.TAG, null,null);

		SQLiteDatabase db = getWritableDatabase();

		db.beginTransaction();

		try {
			for (int i = 0; i < jArray.length(); i++) {
				jsonData = jArray.getJSONObject(i);
				cv.put(DBtables.Motivo.CODIGO_MOTIVO, jsonData.getString(DBtables.Motivo.CODIGO_MOTIVO).trim());
				cv.put(DBtables.Motivo.MOTIVO, jsonData.getString(DBtables.Motivo.MOTIVO).trim());
				//Log.i(TAG, "syncMotivo "+DBtables.Motivo.MOTIVO+":"+jsonData.getString(DBtables.Motivo.MOTIVO).trim());
				db.insert(DBtables.Motivo.TAG, null, cv);				
			}
			db.setTransactionSuccessful();
		} catch (JSONException e) {
			Log.e(DBclasses.TAG, "JSON Exception TABLA MOTIVO:" + e.getMessage());
		} finally {
			db.endTransaction();
			db.close();
		}
	}
	
	public void syncExpectativa(JSONArray jArray) {

		JSONObject jsonData = null;
		ContentValues cv = new ContentValues();

		getReadableDatabase().delete(DBtables.Expectativa.TAG, null,null);

		SQLiteDatabase db = getWritableDatabase();

		db.beginTransaction();

		try {

			for (int i = 0; i < jArray.length(); i++) {

				jsonData = jArray.getJSONObject(i);
				cv.put(DBtables.Expectativa.CODIGO_EXPECTATIVA, jsonData.getString(DBtables.Expectativa.CODIGO_EXPECTATIVA).trim());
				cv.put(DBtables.Expectativa.EXPECTATIVA, jsonData.getString(DBtables.Expectativa.EXPECTATIVA).trim());
				
				db.insert(DBtables.Expectativa.TAG, null, cv);				
			}

			db.setTransactionSuccessful();

		} catch (JSONException e) {
			Log.e(DBclasses.TAG, "JSON Exception TABLA EXPECTATIVA:" + e.getMessage());
		} finally {
			db.endTransaction();
			db.close();
		}
	}

	public ArrayList<Motivo> getMotivos() {

		String rawQuery = "select * from motivo";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(rawQuery, null);
		ArrayList<Motivo> lista = new ArrayList<>();
		
		cursor.moveToFirst();
		if (cursor.moveToFirst()) {
			do {
				Motivo item = new Motivo();
				item.setCodigo(cursor.getString(0));
				item.setMotivo(cursor.getString(1));
				lista.add(item);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return lista;
	}
	public ArrayList<Expectativa> getExpectativas() {

		String rawQuery = "select * from expectativa";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(rawQuery, null);
		ArrayList<Expectativa> lista = new ArrayList<>();
		
		cursor.moveToFirst();
		if (cursor.moveToFirst()) {
			do {
				Expectativa item = new Expectativa();
				item.setCodigo(cursor.getString(0));
				item.setExpectativa(cursor.getString(1));
				lista.add(item);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return lista;
	}
	
	public ArrayList<ModelDevolucionProducto> getListadoProductos_devolucion(String oc_numero) {

		String rawQuery = 
			"select cip,p.despro,cantidad,lote,m.motivo,e.expectativa,tipoDocumento,serieDevolucion,numeroDevolucion,peso,tipo_producto from pedido_detalle pd "+
		    "inner join producto p on pd.cip=p.codpro "+
			"inner join Motivo m on pd.motivoDevolucion = m.codigoMotivo "+
			"inner join Expectativa e on pd.Expectativa = e.codigoExpectativa "+
			"where oc_numero like '"+oc_numero+"'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(rawQuery, null);
		ArrayList<ModelDevolucionProducto> lista = new ArrayList<>();
		
		cursor.moveToFirst();
		if (cursor.moveToFirst()) {
			do {
				ModelDevolucionProducto item = new ModelDevolucionProducto();
				item.setCodigoProducto(cursor.getString(0));
				item.setDescripcionProducto(cursor.getString(1));
				item.setCantidad(cursor.getString(2));
				item.setLote(cursor.getString(3));
				//item.setFechaDocumento(cursor.getString(4));
				item.setMotivo(cursor.getString(4));
				item.setExpectativa(cursor.getString(5));
				item.setTipoDocumento(cursor.getString(6));
				item.setSerie(cursor.getString(7));
				item.setNumero(cursor.getString(8));
				item.setPeso(cursor.getString(9));
				item.setTipo(cursor.getString(10));
				lista.add(item);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return lista;
	}
	
	public String getTipoRegistro(String oc_numero) {

		String rawQuery = 
			"select tipoRegistro from pedido_cabecera where oc_numero like '"+oc_numero+"'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(rawQuery, null);		
		String item = "";
		cursor.moveToFirst();
		if (cursor.moveToFirst()) {
			do {
				item = cursor.getString(0);				
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return item;
	}
	
	public void syncProductoNoDescuento(JSONArray jArray) {
		JSONObject jsonData = null;
		ContentValues cv = new ContentValues();
		getReadableDatabase().delete(DBtables.ProductoNoDescuento.TAG, null, null);

		SQLiteDatabase db = getWritableDatabase();
		db.beginTransaction();

		try {
			for (int i = 0; i < jArray.length(); i++) {
				jsonData = jArray.getJSONObject(i);
				cv.put(DBtables.ProductoNoDescuento.SECUENCIA,jsonData.getString(DBtables.ProductoNoDescuento.SECUENCIA).trim());
				cv.put(DBtables.ProductoNoDescuento.CODIGO_PRODUCTO,jsonData.getString(DBtables.ProductoNoDescuento.CODIGO_PRODUCTO).trim());

				db.insert(DBtables.ProductoNoDescuento.TAG, null, cv);
				Log.i("syncProductoNoDescuento"," codigoProducto: "+ jsonData.getString(DBtables.ProductoNoDescuento.CODIGO_PRODUCTO).trim());
			}
			db.setTransactionSuccessful();

		} catch (JSONException e) {
			Log.e(DBclasses.TAG,"JSON Exception ProductoNoDescuento:" + e.getMessage());
		} finally {
			db.endTransaction();
			db.close();
		}
	}
	
	public String getFlagPedido(String oc_numero) {
		String rawQuery;

		rawQuery = "select flag from pedido_cabecera where oc_numero='"+ oc_numero + "'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		String flag = "";
		cur.moveToFirst();

		while (!cur.isAfterLast()) {
			flag = (cur.getString(0));
			cur.moveToNext();
		}		
		cur.close();
		db.close();

		return flag;
	}
	
	public String getVendedorEmail(String codigoVendedor) {
		String rawQuery;

		rawQuery = "select email from vendedor where codven='"+ codigoVendedor + "'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		String email = "";
		cur.moveToFirst();

		while (!cur.isAfterLast()) {
			email = (cur.getString(0));
			cur.moveToNext();
		}		
		cur.close();
		db.close();

		return email;
	}
	
	public void syncTurno(JSONArray jArray) {

		JSONObject jsonData = null;
		ContentValues cv = new ContentValues();

		getReadableDatabase().delete(DBtables.Turno.TAG, null, null);

		SQLiteDatabase db = getWritableDatabase();

		db.beginTransaction();

		try {

			for (int i = 0; i < jArray.length(); i++) {

				jsonData = jArray.getJSONObject(i);
				cv.put(DBtables.Turno.CODTURNO, jsonData.getString("CodTurno")
						.trim());
				cv.put(DBtables.Turno.TURNO, jsonData.getString("Turno")
						.trim());

				db.insert(DBtables.Turno.TAG, null, cv);
				Log.i("TURNO",
						"i= " + i + " CODTURNO: "
								+ jsonData.getString("CodTurno").trim()
								+ " TURNO: "
								+ jsonData.getString("Turno").trim());
			}

			db.setTransactionSuccessful();

		} catch (JSONException e) {
			Log.e(DBclasses.TAG, "JSON Exception TURNO:" + e.getMessage());
		} finally {
			db.endTransaction();
			db.close();
		}

	}
	
	public void syncBonificacionColores(JSONArray jArray) {

		JSONObject jsonData = null;
		ContentValues cv = new ContentValues();

		getReadableDatabase().delete(DBtables.BonificacionColores.TAG, null, null);

		SQLiteDatabase db = getWritableDatabase();

		db.beginTransaction();

		try {

			for (int i = 0; i < jArray.length(); i++) {

				jsonData = jArray.getJSONObject(i);
				cv.put(DBtables.BonificacionColores.SECUENCIA, jsonData.getString("secuencia").trim());
				cv.put(DBtables.BonificacionColores.ITEM, jsonData.getString("item").trim());
				cv.put(DBtables.BonificacionColores.CIP, jsonData.getString("cc_artic").trim());
				cv.put(DBtables.BonificacionColores.PRODUCTO, jsonData.getString("Producto").trim());

				db.insert(DBtables.BonificacionColores.TAG, null, cv);
				
				Log.i("BONIFICACION COLORES",
						"i= " + i + "SECUENCIA: "
								+ jsonData.getString("secuencia").trim()
								+ " PRODUCTO: "
								+ jsonData.getString("Producto").trim());
			}

			db.setTransactionSuccessful();

		} catch (JSONException e) {
			Log.e(DBclasses.TAG, "JSON Exception BONIFICACION COLORES:" + e.getMessage());
		} finally {
			db.endTransaction();
			db.close();
		}

	}

	public DBPolitica_Precio2 GetPoliticaPrecio2ByCliente(String codcli, String codpro, double valor_cambio ) {



		DBPolitica_Precio2 politica_precio2=null;

		try {
			SQLiteDatabase db = getWritableDatabase();
			String sql="select pr2.secuencia, pr2.item, pr2.codpro, " +
                    "round ((select p._precio_base from producto p where p.codpro=pr2.codpro)* "+valor_cambio+", 4) as _precio_base, " +
                    "round (pr2.prepro_unidad*"+valor_cambio+", 4) as prepro_unidad, " +
                    "round (pr2.prepro*"+valor_cambio+", 4) as prepro " +
                    "from "+ Politica_precio2.TAG+" pr2 where pr2.codpro='"+codpro+"' " +
					"and pr2.secuencia in (select pc.sec_politica from politica_cliente pc where pc.codcli='"+codcli+"')";
			Cursor cursor=db.rawQuery(sql, null);
			if (cursor.moveToNext()){
				politica_precio2=new DBPolitica_Precio2();
				politica_precio2.setSecuencia(cursor.getInt(cursor.getColumnIndex("secuencia")));
				politica_precio2.setItem(cursor.getInt(cursor.getColumnIndex("item")));
				politica_precio2.setCodpro(cursor.getString(cursor.getColumnIndex("codpro")));
				politica_precio2.setPrepo_unidad(cursor.getDouble(cursor.getColumnIndex("prepro_unidad")));
				politica_precio2.setPrepro(cursor.getDouble(cursor.getColumnIndex("prepro_unidad")));
				politica_precio2.setPrecio_base(cursor.getDouble(cursor.getColumnIndex("_precio_base")));
			}
			cursor.close();
			db.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
			politica_precio2=null;
		}
		return politica_precio2;

	}




	public boolean RequiereValidacionPorDescuento(String oc_numero) {

		String rawQuery;
		rawQuery = "select max(descuento) from pedido_detalle where  oc_numero='"+ oc_numero + "' ";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		boolean isRequirido=false;
		if (cur.moveToNext()){
			isRequirido=cur.getDouble(0)>3.0;
		}

		cur.close();
		db.close();
		return isRequirido;
	}
	public boolean AgregarPedidoCabecera_raiz(DB_ObjPedido cabecera, SQLiteDatabase DB) {

		try {
			SQLiteDatabase db = (DB!=null?DB:getWritableDatabase());

			ContentValues Nreg = new ContentValues();
			Nreg.put("oc_numero", cabecera.getOc_numero());
			Nreg.put("sitio_enfa", cabecera.getSitio_enfa());
			Nreg.put("monto_total", cabecera.getMonto_total());
			Nreg.put("subTotal", cabecera.getSubtotal());
			Nreg.put("percepcion_total", cabecera.getMonto_total());
			Nreg.put("valor_igv", cabecera.getValor_igv());
			Nreg.put("moneda", cabecera.getMoneda());
			Nreg.put("fecha_oc", cabecera.getFecha_oc());
			Nreg.put("fecha_mxe", cabecera.getFecha_mxe());
			Nreg.put("cond_pago", cabecera.getCond_pago());
			Nreg.put("cod_cli", cabecera.getCod_cli());
			Nreg.put("cod_emp", cabecera.getCod_emp());
			Nreg.put("estado", cabecera.getEstado());
			Nreg.put("username", cabecera.getUsername());
			Nreg.put("ruta", cabecera.getRuta());
			Nreg.put("observacion", cabecera.getObserv());
			Nreg.put("cod_noventa", cabecera.getCod_noventa());
			Nreg.put("peso_total", cabecera.getPeso_total());
			Nreg.put("flag", cabecera.getFlag());
			Nreg.put("latitud", cabecera.getLatitud());
			Nreg.put("longitud", cabecera.getLongitud());
			Nreg.put("codigo_familiar", cabecera.getCodigo_familiar());
			Nreg.put("DT_PEDI_FECHASERVIDOR", cabecera.getDT_PEDI_FECHASERVIDOR());
			Nreg.put(DBtables.Pedido_cabecera.TIPO_REGISTRO, cabecera.getTipoRegistro());
			Nreg.put(Pedido_cabecera.TOTAL_SUJETO_PERCEPCION, cabecera.getTotalSujetoPercepcion());

			Nreg.put(Pedido_cabecera.CODIGO_SUCURSAL, cabecera.getCodigoSucursal());
			Nreg.put(Pedido_cabecera.NRO_ORDEN_COMPRA, cabecera.getNumeroOrdenCompra());
			Nreg.put(Pedido_cabecera.CODIGO_PRIORIDAD, cabecera.getCodigoPrioridad());
			Nreg.put(Pedido_cabecera.CODIGO_PUNTO_ENTREGA, cabecera.getCodigoPuntoEntrega());
			Nreg.put(Pedido_cabecera.CODIGO_TIPO_DESPACHO, cabecera.getCodigoTipoDespacho());
			Nreg.put(Pedido_cabecera.CODIGO_SUCURSAL, cabecera.getCodigoSucursal());
			Nreg.put(Pedido_cabecera.FLAG_EMBALAJE, cabecera.getFlagEmbalaje());
			Nreg.put(Pedido_cabecera.FLAG_PEDIDOANTICIPO, cabecera.getFlagPedido_Anticipo());
			Nreg.put(Pedido_cabecera.CODIGO_TRANSPORTISTA, cabecera.getCodigoTransportista());
			Nreg.put(Pedido_cabecera.CODIGO_ALMACEN, cabecera.getCodigoAlmacen());
			Nreg.put(Pedido_cabecera.OBSERVACION2, cabecera.getObservacion2());
			Nreg.put(Pedido_cabecera.OBSERVACION3, cabecera.getObservacion3());
			Nreg.put(Pedido_cabecera.OBSERVACION_DESCUENTO, cabecera.getObservacionDescuento());
			Nreg.put(Pedido_cabecera.OBSERVACION_TIPO_PROD, cabecera.getObservacionTipoProducto());

			Nreg.put(Pedido_cabecera.TIPO_VISTA, "");
			Nreg.put(Pedido_cabecera.FLAG_DESCUENTO, "");
			Nreg.put(Pedido_cabecera.CODIGO_OBRA, "");
			Nreg.put(Pedido_cabecera.DOC_ADICIONAL, "");
			Nreg.put(Pedido_cabecera.TIPODOCUMENTO, "");
			Nreg.put(Pedido_cabecera.DIAS_VIGENCIA, "");
			Nreg.put(Pedido_cabecera.PEDIDO_ANTERIOR, "");
			Nreg.put(Pedido_cabecera.CODIGO_TURNO, "");
			Nreg.put(Pedido_cabecera.NRO_LETRA, "");
			Nreg.put(Pedido_cabecera.OBSERVACION4, "");




			Log.i("PEDIDO_CABECERA", "flag" + cabecera.getFlag());
			long a=db.insert("pedido_cabecera", null, Nreg);
			if (DB==null){
				db.close();
			}

			Gson gson=new Gson();
			Log.i("PEDIDO_CABECERA", "registro insertado "+gson.toJson(cabecera));
			return (a>0);
		} catch (Exception e) {
			Log.i("PEDIDO_CABECERA", "Error registro insertado");
			return false;
		}
	}

	public boolean verificarPedidoTieneCabecera(SQLiteDatabase _db, String oc_numero) {
		String rawQuery;
		rawQuery = "SELECT * FROM pedido_cabecera WHERE oc_numero= '"+oc_numero+"'";

		SQLiteDatabase db = (_db!=null?_db:getReadableDatabase());
		Cursor cur = db.rawQuery(rawQuery, null);
		boolean hayData=cur.getCount()>0;
		cur.close();
		if (_db==null){
			db.close();
		}
		return hayData;
	}

	public String obtenerCodigoSucursalCliente(String codcli,String codigoVendedor) {
		//String rawQuery = "select item from direccion_cliente where codcli like '" + codcli + "' order by 1 desc";
		//El item direccion no es el mismo que el de la zonificacion, por lo tanto a veces no pinta el cliente, Se debe obtener el item Sucursal desde la zonificacion
		String rawQuery = "select item_dircli from znf_programacion_clientes where codven like '"+codigoVendedor+"' and codcli like '"+codcli+"' LIMIT 1";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		String item = "";
		cur.moveToFirst();
		if (cur.moveToFirst()) {
			do {
				item = cur.getString(0);
			} while (cur.moveToNext());

		}
		cur.close();
		db.close();
		return item;
	}

    public String obtenerEstadoSucursalCliente (String codcli, String sucursal ) {
        String rawQuery = "select ifnull(estado,'') from direccion_cliente where codcli='" + codcli + "' and item = '"+sucursal+"'";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur = db.rawQuery(rawQuery, null);
        String estado = "";
        cur.moveToFirst();
        if (cur.moveToFirst()) {
            do {
                estado = cur.getString(0);
            } while (cur.moveToNext());

        }
        cur.close();
        db.close();
        return estado;
    }

	public ArrayList<DB_DireccionClientes> obtenerDirecciones_cliente2(String codigoCliente) {

		String rawQuery;

		rawQuery = "select item,direccion, latitud, longitud, altitud, telefono from "+DBtables.Direccion_cliente.TAG+" where codcli='"
				+ codigoCliente + "'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(rawQuery, null);

		ArrayList<DB_DireccionClientes> lista = new ArrayList<DB_DireccionClientes>();

		if (cursor.getCount() != 0) {
			cursor.moveToFirst();

			if (cursor.moveToFirst()) {
				do {
					DB_DireccionClientes item = new DB_DireccionClientes();
					item.setItem(cursor.getString(0));
					item.setDireccion(cursor.getString(1));
					item.setLatitud(cursor.getString(2));
					item.setLongitud(cursor.getString(3));
					item.setAltitud(cursor.getDouble(4));
					item.setTelefono(cursor.getString(cursor.getColumnIndex("telefono")));
					lista.add(item);
				} while (cursor.moveToNext());
			}

		}

		cursor.close();
		db.close();

		return lista;
	}
	public ArrayList<DB_RegistrosGenerales> getGiroCliente() {
		return getRegistrosGenerales("Giro Cliente");
	}
	public ArrayList<DB_RegistrosGenerales> getRegistrosGenerales(String adjetivo) {
		String rawQuery;
		rawQuery = "select * from registros_generales where adjetivo like '"+adjetivo+"' order by 2";

		SQLiteDatabase db = getReadableDatabase();
		ArrayList<DB_RegistrosGenerales> lista = new ArrayList<>();
		Cursor cur = db.rawQuery(rawQuery, null);
		cur.moveToFirst();
		while (!cur.isAfterLast()) {
			DB_RegistrosGenerales item = new DB_RegistrosGenerales();
			item.setSecuencia(cur.getInt(0));
			item.setDescripcion(cur.getString(1));
			item.setEstado(cur.getString(2));
			item.setAdjetivo(cur.getString(3));
			lista.add(item);
			cur.moveToNext();
		}
		cur.close();
		db.close();
		return lista;
	}

	public void updateGeolocalizacionCliente(String codigoCliente, String codigoSucursal, double lat, double lng, double altitud) {

		try {

			SQLiteDatabase db = getWritableDatabase();

			ContentValues Nreg = new ContentValues();
			String where = "codcli like ? and item like ?";
			String[] args = { codigoCliente, codigoSucursal };

			Nreg.put("latitud", lat);
			Nreg.put("longitud", lng);
			Nreg.put("altitud", altitud);
			Nreg.put("estado", "P");

			db.update("direccion_cliente", Nreg, where, args);
			db.close();
			Log.w("updateGeolocalizacionCliente", " codigoCliente: "+ codigoCliente + ", codigoSucursal "+ codigoSucursal);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}


	public String getEstadoDireccionCliente(String codigoCliente, String item) {
		String rawQuery;
		rawQuery = "SELECT ifnull(estado,'') FROM "+DBtables.Direccion_cliente.TAG+" WHERE codcli like '"+codigoCliente+"' and item like '"+item+"'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);

		String estado = "";
		cur.moveToFirst();
		while (!cur.isAfterLast()) {
			estado = cur.getString(0);
			cur.moveToNext();
		}
		Log.v(TAG, estado);
		cur.close();
		db.close();
		return estado;
	}

	public ArrayList<DB_DireccionClientes> getLocalizacionesCliente(String codcli, String item){ //**Nuevo Localizacion
		String rawQuery= "";

		if(codcli.equals("ninguno")){
			rawQuery= "select dc.codcli, ifnull(item,''), ifnull(latitud,''), ifnull(longitud,''), estado, altitud "
					+ "from direccion_cliente dc inner join cliente c on dc.codcli = c.codcli where length(latitud) >= 4";
		}else{
			rawQuery= "select dc.codcli, ifnull(item,''), ifnull(latitud,''), ifnull(longitud,''), estado, altitud "
					+ "from direccion_cliente dc inner join cliente c on dc.codcli = c.codcli where  dc.codcli ='"+codcli+"' and item ='"+item+"'";
		}

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		ArrayList<DB_DireccionClientes> lista_direcciones = new ArrayList<DB_DireccionClientes>();
		cur.moveToFirst();
		while (!cur.isAfterLast()) {

			DB_DireccionClientes dbdireccion = new DB_DireccionClientes();
			dbdireccion.setCodcli(cur.getString(0));
			dbdireccion.setItem(cur.getString(1));
			dbdireccion.setLatitud(cur.getString(2));
			dbdireccion.setLongitud(cur.getString(3));
			dbdireccion.setEstado(cur.getString(4));
            dbdireccion.setGiroCliente(0);//no nos importa el giro
            dbdireccion.setAltitud(cur.getDouble(cur.getColumnIndex("altitud")));//no nos importa el giro
			lista_direcciones.add(dbdireccion);
			cur.moveToNext();
		}
		cur.close();
		db.close();
		return lista_direcciones;
	}

	public String getServicioCreacionCliente() {
		// TODO Auto-generated method stub
		String rawQuery;

		rawQuery = "select valor from configuracion where nombre='crear_cliente'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		String respuesta = "";
		cur.moveToFirst();

		while (!cur.isAfterLast()) {
			respuesta = (cur.getString(0));
			cur.moveToNext();
		}

		cur.close();
		db.close();

		return respuesta;
	}
	public  String getConfiguracionByName(String name, String valorDef) {
		String rawQuery;
		rawQuery = "SELECT valor FROM configuracion WHERE nombre like '"+name+"'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);

		String valor = valorDef;
		cur.moveToFirst();
		Log.w(TAG+"raw query:",rawQuery);
		while (!cur.isAfterLast()) {
			valor = cur.getString(0);
			cur.moveToNext();
		}
		Log.w(TAG,"Luego del query");
		Log.w(TAG+":getConfiguracionByName:",""+valor);
		cur.close();
		db.close();
		return valor;
	}

	public  String getRegistrosGeneralesMovilByCodigo(String codigo, String valorDef) {
		String rawQuery;
		rawQuery = "SELECT descripcion  FROM "+DBtables.RegistrosGeneralesMovil.TAG+" WHERE codigoDescripcion like '"+codigo+"'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);

		String valor = valorDef;
		cur.moveToFirst();
		Log.w(TAG+"raw query:",rawQuery);
		while (!cur.isAfterLast()) {
			valor = cur.getString(0);
			cur.moveToNext();
		}
		Log.w(TAG,"Luego del query");
		Log.w(TAG+":getRegistrosGeneralesMovilByCodigo:",""+valor);
		cur.close();
		db.close();
		return valor;
	}

	public  ArrayList<ResumenVentaTipoProducto> getPedidoResumenByTipoProducto (String oc_numero, double igv, double tipoCambio) {
		String rawQuery;
		rawQuery = "SELECT " +
				" tipoProducto, sum(peso_bruto) as pesoTotal, sum(precio_neto) as sutTotal, " +
				" (sum(precio_neto)/sum(peso_bruto)) / "+tipoCambio+" as pkDolar,\n" +
				" sum(precio_neto) * "+igv+" as igvTotal from (\n" +
				"select  \n" +
				"ifnull((select tp.descripcion from tipoProducto tp where tp.codigoTipo=p.tipoProducto), 'OTROS') as tipoProducto,\n" +
				"pd.peso_bruto, pd.precio_neto, pd.cantidad\n" +
				" from pedido_detalle pd   \n" +
				"inner join producto p on (pd.cip=p.codpro OR ((substr(pd.cip,2,length(pd.cip))) =  p.codpro))\n" +
				"where pd.oc_numero='"+oc_numero+"'\n" +
				") res group by tipoProducto " +
				"order by tipoProducto asc";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);

		ArrayList<ResumenVentaTipoProducto> lista=new ArrayList<>();
		ResumenVentaTipoProducto item=null;
		while (cur.moveToNext()){
			item=new ResumenVentaTipoProducto(
				cur.getString(cur.getColumnIndex("tipoProducto")),
				cur.getDouble(cur.getColumnIndex("pesoTotal")),
				cur.getDouble(cur.getColumnIndex("sutTotal")),
				cur.getDouble(cur.getColumnIndex("pkDolar")),
				cur.getDouble(cur.getColumnIndex("igvTotal"))
			);
			lista.add(item);
		}
		cur.close();
		db.close();
		return lista;
	}

	public  String getCodigoNivelByUserandPass(String user, String pass) {
		String rawQuery = "SELECT codigoRol  FROM "+DBtables.Usuarios.TAG+" " +
				"WHERE useusr = '"+user+"' and usepas ='"+pass+"'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);

		Log.w(TAG+"raw query:",rawQuery);
		String valor="";
		while (cur.moveToNext()) {
			valor = cur.getString(0);
		}
		Log.w(TAG,"Luego del query");
		Log.w(TAG+":getCodigoNivelByUserandPass:",""+valor);
		cur.close();
		db.close();
		return valor;
	}

	public ArrayList<CTA_INGRESOSPDF> getCTA_INGRESOSPDF(String codcli) {
		String rawQuery = "select " +
				"ci.serie_doc, " +
				"ci.numero_factura, " +
				"ci.feccom,  " +
				"ci.fecha_despacho, " +
				"ci.fecha_vencimiento, " +
				"m.codigoEquivalente, " +
				"ci.total," +
				"c.codcli, " +
				"c.nomcli, " +
				"ifnull((select " +
				"_x.saldo " +
				"from " +
				"cta_ingresos _x " +
				"where " +
				"_x.fecha_vencimiento < date('now') " +
				"and " +
				"_x.secuencia = ci.secuencia ), '') as deuda, " +
				"ifnull((select " +
				"_x.saldo " +
				"from " +
				"cta_ingresos _x " +
				"where " +
				"_x.fecha_vencimiento >= date('now') " +
				"and " +
				"_x.secuencia = ci.secuencia ), '') as obligacion " +
				"from " +
				"cta_ingresos ci " +
				"inner join " +
				"moneda m  " +
				"on " +
				"m.codigoMoneda = ci.codmon " +
				"inner join " +
				"cliente  c " +
				"on " +
				"c.codcli = ci.codcli " +
				"where " +
				"ci.codcli= '"+codcli+"'";
		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		cur.moveToFirst();
		ArrayList<CTA_INGRESOSPDF> cta_ingresos = new ArrayList<>();
		while (!cur.isAfterLast())
		{
			CTA_INGRESOSPDF cta = new CTA_INGRESOSPDF();
			cta.setSerie_doc(cur.getString(cur.getColumnIndex("serie_doc")));
			cta.setNumero_factura(cur.getString(cur.getColumnIndex("numero_factura")));
			cta.setFeccom(cur.getString(cur.getColumnIndex("feccom")));
			cta.setFecha_despacho(cur.getString(cur.getColumnIndex("fecha_despacho")));
			cta.setFecha_vencimiento(cur.getString(cur.getColumnIndex("fecha_vencimiento")));
			cta.setCodigo_equivalente(cur.getString(cur.getColumnIndex("codigoEquivalente")));
			cta.setTotal(cur.getString(cur.getColumnIndex("total")));
			cta.setCodcli(cur.getString(cur.getColumnIndex("codcli")));
			cta.setNomcli(cur.getString(cur.getColumnIndex("nomcli")));
			cta.setDeuda(cur.getString(cur.getColumnIndex("deuda")));
			cta.setObligacion(cur.getString(cur.getColumnIndex("obligacion")));
			cta_ingresos.add(cta);
			cur.moveToNext();
		}
		cur.close();
		db.close();
		return cta_ingresos;
	}

	public String getMaxDate(String codcli) {
		String rawQuery = "select max(fecha_despacho) as max_fecha from cta_ingresos where codcli= '"+codcli+"'";
		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		cur.moveToFirst();
		@SuppressLint("Range") String max_fecha = cur.getString(cur.getColumnIndex("max_fecha"));
		cur.close();
		db.close();
		return max_fecha;
	}
	public String getMinDate(String codcli) {
		String rawQuery = "select min(fecha_despacho) as min_fecha from cta_ingresos where codcli= '"+codcli+"'";
		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		cur.moveToFirst();
		@SuppressLint("Range") String max_fecha = cur.getString(cur.getColumnIndex("min_fecha"));
		cur.close();
		db.close();
		return max_fecha;
	}
	public boolean  isRegistradoProducto(String numoc, String cip) {
		return !isNotRegistradoProducto(numoc, cip);
	}

	public boolean isNotRegistradoProducto(String numoc, String cip) {
		String S_TAG="getCantidad_produtuctosX_oc_numero:: ";
		boolean agregar=true;
		try {
			String SQL="select * from pedido_detalle where oc_numero='"+numoc+"' and cip = '"+cip+"'";
			SQLiteDatabase dbRead=getReadableDatabase();
			Cursor cur=dbRead.rawQuery(SQL, null, null);
			if (cur.getCount()>0) {
				agregar=false;
			}
			cur.close();
			dbRead.close();
		}catch (Exception e){
			agregar=false;
			e.printStackTrace();
		}
		return  agregar;
	}
	public boolean isNotRegistradoProductoEItem(String numoc, String cip, int item) {
		String S_TAG="getCantidad_produtuctosX_oc_numero:: ";
		boolean agregar=true;
		try {
			String SQL="select * from pedido_detalle " +
					"where oc_numero='"+numoc+"' and cip = '"+cip+"' and item= '"+item+"'";
			SQLiteDatabase dbRead=getReadableDatabase();
			Cursor cur=dbRead.rawQuery(SQL, null, null);
			if (cur.getCount()>0) {
				agregar=false;
			}
			cur.close();
			dbRead.close();
		}catch (Exception e){
			agregar=false;
			e.printStackTrace();
		}
		return  agregar;
	}

	public void cambiarRutaHttpServicioWeb() {
		SQLiteDatabase db=getWritableDatabase();
		db.execSQL("update servidor set TX_SERV_servicioWEB='200.60.7.172/ws_tubo_plast/service.asmx' " +
				"where TX_SERV_servicioWEB like '%200.60.105.44%'");
	}
	public String getNroVersion(){
		String valorDef = "(Preguntar al administrador)";
		return getConfiguracionByName("nro_version_app",valorDef );
	}
	public int isVersionSuperior(String cadena_version){
		String[] arrSplit = cadena_version.split(" ");
		String __v =arrSplit[0].replace("v", "");
		String[] version =__v.split("\\.");
		String nroVersion=version[0]+(version.length>1?version[1]:"0")+ (version.length>2?version[2]:"0");
		return Integer.parseInt(nroVersion);
	}

	public boolean isVersionSuperior(final Activity activity){

		boolean isSuperior=false;
		try{
			String version_app = activity.getResources().getString(R.string.APP_NRPO_VERSION).trim();
			String version_actual= getNroVersion();
			int _version_app=isVersionSuperior(version_app.toLowerCase());
			int _version_actual=isVersionSuperior(version_actual.toLowerCase());
			isSuperior=_version_app>=_version_actual;

		}catch (Exception e){
			e.printStackTrace();
		}
		return isSuperior;
	}

	public int getPreferenciasByName(String name){
		String rawQuery;
		rawQuery = "SELECT valor FROM configuracion WHERE nombre like '"+name+"'";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);

		int estado = 1;
		cur.moveToFirst();
		Log.w(TAG+"raw query:",rawQuery);
		while (!cur.isAfterLast()) {
			estado = cur.getInt(0);
			cur.moveToNext();
		}
		Log.w(TAG,"Luego del query");
		Log.w(TAG+":getPreferenciasByName: estado",""+estado);
		cur.close();
		db.close();
		return estado;
	}
	public boolean VersionAppActualizadoCheck(final Activity activity){
		String mensaje="";

		if (!getNroVersion().equalsIgnoreCase(""+activity.getResources().getString(R.string.APP_NRPO_VERSION))
				&& !isVersionSuperior(activity) ){
			mensaje="Hay una nueva version disponible para esta aplicación(OTROS).\n\nVersión actual: " + getNroVersion() + "";
		}
		if (mensaje.length()>0){
			int estado=getPreferenciasByName("validar nro_version_app");
			if (estado==1){
				mensaje=mensaje.replace("(OTROS)", ", por ello es necesario actualizar");
			}else mensaje=mensaje.replace("(OTROS)", ", Sin embargo no es necesarió actualizar");


			new AlertDialog.Builder(activity)
					.setCancelable(false)
					.setIcon(activity.getResources().getDrawable(R.drawable.alert))
					.setMessage(mensaje)
					.setPositiveButton("Omitir", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					})
					.setNegativeButton("Obtener", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							descargarApp(activity);
						}
					})
					.show();

			if (estado==1){
				return false;
			}else return true;
		}else return true;
	}

	public void descargarApp( final Activity activity){
		LinearLayout.LayoutParams paramT = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 50);
		LinearLayout.LayoutParams paramT2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 10);

		final WebView myWebVew=new WebView(activity);
		myWebVew.setLayoutParams(paramT);


		myWebVew.setWebViewClient(new WebViewClient());


		WebSettings webSettings=myWebVew.getSettings();
		webSettings.setJavaScriptEnabled(true);

		myWebVew.getSettings().setSupportMultipleWindows(true);
		myWebVew.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		myWebVew.getSettings().setAllowFileAccess(true);
		myWebVew.getSettings().setJavaScriptEnabled(true);
		myWebVew.getSettings().setBuiltInZoomControls(true);
		myWebVew.getSettings().setDisplayZoomControls(false);
		myWebVew.getSettings().setLoadWithOverviewMode(true);
		myWebVew.getSettings().setUseWideViewPort(true);
		if (Build.VERSION.SDK_INT >= 19) {
			myWebVew.setLayerType(View.LAYER_TYPE_HARDWARE, null);
		}
		else if(Build.VERSION.SDK_INT >=11 && Build.VERSION.SDK_INT < 19) {
			myWebVew.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		}


		myWebVew.loadUrl(getConfiguracionByName("url_apk", "https://www.acgenesys.com")+"?nro_version="+activity.getResources().getString(R.string.APP_NRPO_VERSION));
		final SwipeRefreshLayout swipe_refresh=new SwipeRefreshLayout(activity);
		swipe_refresh.setLayoutParams(paramT2);
		swipe_refresh.setColorSchemeColors(
				activity.getResources().getColor(R.color.s1),
				activity.getResources().getColor(R.color.s2),
				activity.getResources().getColor(R.color.s3),
				activity.getResources().getColor(R.color.s4)
		);
		swipe_refresh.addView(myWebVew);
		myWebVew.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int progress) {
				swipe_refresh.setRefreshing(true);
				if (progress == 100) {
					swipe_refresh.setRefreshing(false);
				}
			}
		});

		swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				myWebVew.reload();
			}
		});
		new AlertDialog.Builder(activity)
				.setCancelable(false)

				.setView(swipe_refresh)
				.setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				})
				.setNeutralButton("Abrir Con Web", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String url=(getConfiguracionByName("url_apk", "https://www.acgenesys.com")+"?nro_version="+activity.getResources().getString(R.string.APP_NRPO_VERSION));;
						Uri uri=Uri.parse(""+url);
						Intent intent=new Intent(Intent.ACTION_VIEW, uri);
						activity.startActivity(intent);
					}
				})
				.show();
	}

	public void recalcularItemPedidoDetalle(String oc_numero) {
		SQLiteDatabase db = getWritableDatabase();
		String rawQuery = "SELECT item FROM "+ Pedido_detalle.TAG+" pd where pd.oc_numero='"+oc_numero+"' " +
				" order by cast(item as INTEGER) ASc ";
		Cursor cur = db.rawQuery(rawQuery, null);
		int index=1;
		while (cur.moveToNext()) {
			int itemActual=(cur.getInt(0));
			String where="oc_numero=? and item=?";
			String[] args = new String[]{oc_numero, ""+itemActual};
			ContentValues pedidItem = new ContentValues();
			pedidItem.put("item", index);
			db.update(Pedido_detalle.TAG, pedidItem, where, args);
			index++;
		}
		cur.close();
		db.close();
	}

	public boolean isCarteraSidigeOrIsLibre(String codcli){
		String sql="select * from znf_programacion_clientes znf " +
				"where (znf.cartera_sidige ='SI' or length(znf.cartera_sidige) =0 )" +//blanco = no esta en sidige, solo en saemovil
				"and znf.codcli= '"+codcli+"' " ;
		SQLiteDatabase db=getReadableDatabase();
		Cursor cur=db.rawQuery(sql, null, null);

		boolean isSI= cur.getCount()>0;
		cur.close();
		db.close();
		return isSI;
	}

	public void setDataPruebas() {
		SQLiteDatabase db = getWritableDatabase();
//		String sql = "update " + DBtables.Promocion_Detalle.TAG + " set  " +
//				"prioridad=1, " +
//				"agrupado=1, " +
//				"total_agrupado=1 " +
//				 "where secuencia in ('7','8') ";
		String sql="DELETE FROM registro_bonificaciones  where oc_numero='V3225082903' AND codigoVendedor is null ";
		db.execSQL(sql);
		db.close();

	}
}

