package com.example.fuerzaventaschema.genesys.datatypes;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.concurrent.TimeoutException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import com.example.fuerzaventaschema.genesys.BEAN.San_Visitas;
import com.example.fuerzaventaschema.genesys.DAO.DAO_RegistroBonificaciones;
import com.example.fuerzaventaschema.genesys.DAO.DAO_San_Visitas;
import com.example.fuerzaventaschema.genesys.util.GlobalVar;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

@SuppressLint("LongLogTag")
public class DBSync_soap_manager {
	final static String TAG = "SOAP_MANGER";
	//String SOAP_ACTION= "http://tempuri.org/obtenerUsuarios";
	//String METHOD_NAME="obtenerUsuarios";
	public String NAMESPACE="http://tempuri.org/";
	public String URL="http://";
	//public String URL="http://192.168.0.254/WS_SAE2/Service.asmx";

	public  String SOAP_ACTION="";
	public String METHOD_NAME="";
	//private String NOMBRE="saemoviles.com";
	private String NOMBRE= GlobalVar.NombreWEB;
	public  DBclasses dbclass=null;
	public DAO_RegistroBonificaciones DAORegistroBonificaciones;
	ArrayList<DB_RegistroBonificaciones> registroBonificaciones;
	Gson gson = new Gson();
	Context context;
	byte[] image=null;
	//
	
	SharedPreferences prefs;
	String url;
	String catalog;
	String user;
	String contrasena;
	String servicio;
	
	String url_local;
	String catalog_local;
	String user_local;
	String contrasena_local;
	String servicio_local;
	
	Boolean chk_principal_estado;
	Boolean chk_secundario_estado;
	
	
	public DBSync_soap_manager(Context context) {
		
		this.context=context;
	
		dbclass = new DBclasses(this.context);
		DAORegistroBonificaciones = new DAO_RegistroBonificaciones(this.context);
     //   URL= URL + GlobalVar.urlService;
	  
		prefs = context.getSharedPreferences("MisPreferencias",Activity.MODE_PRIVATE);
		
		chk_principal_estado = prefs.getBoolean("servicio_principal_activo", true);
		chk_secundario_estado = prefs.getBoolean("servicio_secundario_activo", true);
		
		Log.i("Prefs chk_principal_estado",""+chk_principal_estado);
		Log.i("Prefs chk_secundario_estado",""+chk_secundario_estado);
		
		url = prefs.getString("url", "0");
		catalog = prefs.getString("catalog", "0");
		user = prefs.getString("userid", "0");
		contrasena = prefs.getString("contrasenaid", "0");
		servicio = prefs.getString("servicio", "0");
		
		
		url_local = prefs.getString("url_local", "0");
		catalog_local = prefs.getString("catalog_local", "0");
		user_local = prefs.getString("userid_local", "0");
		contrasena_local = prefs.getString("contrasenaid_local", "0");
		servicio_local = prefs.getString("servicio_local", "0");
		
	}
	
	public String Imagen() {
		return "http://"+NOMBRE+"/img/";
	}
	
	/*public void ObtenerProductos() {
		this.SOAP_ACTION= "http://tempuri.org/obtenerProductos";
		this.METHOD_NAME="obtenerProductos";
	}*/
	
 public void Sync_tabla_usuarios(String url, String catalog, String user, String contrasena) throws Exception{	 
		String SOAP_ACTION= "http://tempuri.org/obtenerUsuarios_json";
		String METHOD_NAME="obtenerUsuarios_json";
		
		SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME); 
		 
		Request.addProperty("url", url); 
		Request.addProperty("catalog", catalog); 
		Request.addProperty("user", user); 
		Request.addProperty("password", contrasena);
		
		Log.d("DBSync","url "+url);
		Log.d("DBSync","catalog "+catalog);
		Log.d("DBSync","user "+user);
		Log.d("DBSync","contrasena "+contrasena);
		Log.d("DBSync","HttpTransportSE "+URL+GlobalVar.urlService);
		 
	    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    Soapenvelope.dotNet=true;
	    Soapenvelope.setOutputSoapObject(Request);
	   
	    
	    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);	    
	    long beforecall = System.currentTimeMillis();
	    
	    
	    try{ 
	    	transporte.call(SOAP_ACTION, Soapenvelope);
	    	Log.i("USUARIOS","RESPUESTA EN: "+(System.currentTimeMillis()-beforecall)+"miliseg");

	    	 SoapPrimitive result =(SoapPrimitive)Soapenvelope.getResponse();
	    	 
	    	 JSONArray jsonstring = new JSONArray(result.toString());
	    	 Log.i("USUARIOS","Registros: "+jsonstring.length());
	    	 Log.d("USUARIOS", "**************************************************************");
	    	 Log.d("Usuarios", jsonstring.toString());
	    	 
	    	 dbclass.syncUsuarios(jsonstring);
	    	 Log.i("USUARIOS", "SINCRONIZADA");
	    	
	    }catch(final Exception e){
	    	
	    	 
	    	e.printStackTrace();
	    	Log.e("USUARIOS", "NO SINCRONIZADA");
	    	throw new Exception(e);
	    }   
 
	  }
	  

 public void Sync_tabla_vendedores(String url, String catalog, String user, String contrasena) throws Exception{
		
		String SOAP_ACTION= "http://tempuri.org/obtenerVendedores_json";
		String METHOD_NAME="obtenerVendedores_json";
		
		SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);   
		Request.addProperty("url", url); 
		Request.addProperty("catalog", catalog); 
		Request.addProperty("user", user); 
		Request.addProperty("password", contrasena);  
	    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    Soapenvelope.dotNet=true;
	    Soapenvelope.setOutputSoapObject(Request);
	    
	    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);
	    //HttpTransportSE transporte=new HttpTransportSE("http://190.40.100.50/SAEMV-WEB/service.asmx");
		
	    long beforecall = System.currentTimeMillis();  
	     
	    try{
	    	transporte.call(SOAP_ACTION, Soapenvelope);
	    	Log.i("VENDEDOR","RESPUESTA EN: "+(System.currentTimeMillis()-beforecall)+"miliseg");

	    	SoapPrimitive result =(SoapPrimitive)Soapenvelope.getResponse();
	    	  
	    	JSONArray jsonstring = new JSONArray(result.toString());
	    	Log.i("VENDEDOR","Registros: "+jsonstring.length());	    	
	    	Log.d("VENDEDOR", "**************************************************************");
	    	Log.d("VENDEDOR", jsonstring.toString());
	    	dbclass.syncVendedores(jsonstring);
	        Log.i("VENDEDOR", "SINCRONIZADA");
	    	
	    }catch(Exception e){
	    	e.printStackTrace();
	    	Log.e("VENDEDOR", "NO SINCRONIZADA");
	    	throw new Exception(e);
	    } 

	}
	
 public void Sync_tabla_banco(String url, String catalog, String user, String contrasena) throws Exception{
		
		String SOAP_ACTION= "http://tempuri.org/obtenerBanco_json";
		String METHOD_NAME="obtenerBanco_json";
		
		SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);    
		Request.addProperty("url", url); 
		Request.addProperty("catalog", catalog); 
		Request.addProperty("user", user); 
		Request.addProperty("password", contrasena); 
	    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    Soapenvelope.dotNet=true;
	    Soapenvelope.setOutputSoapObject(Request);
	    
	    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);
	    
	    long beforecall = System.currentTimeMillis();
	    
	    try{
	    	transporte.call(SOAP_ACTION, Soapenvelope);
	    	Log.i("BANCO","RESPUESTA EN: "+(System.currentTimeMillis()-beforecall)+"miliseg");

	    	 SoapPrimitive result = (SoapPrimitive)Soapenvelope.getResponse();  	    	
	    	
	    	 JSONArray jsonstring = new JSONArray(result.toString());
	    	 Log.i("BANCO","Registros: "+jsonstring.length());
	    	 dbclass.syncBanco(jsonstring);
	    	 Log.i("BANCO", "SINCRONIZADA");
	    	 
	    }catch(Exception e){
	    	e.printStackTrace();
	    	Log.e("BANCO", "NO SINCRONIZADA");
	    	throw new Exception(e);
	    }  

	}
 
 
 public void Sync_tabla_turno(String url, String catalog, String user, String contrasena) throws Exception{
		
		String SOAP_ACTION= "http://tempuri.org/obtener_lista_turnos";
		String METHOD_NAME="obtener_lista_turnos";
		
		SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);    
		Request.addProperty("url", url); 
		Request.addProperty("catalog", catalog); 
		Request.addProperty("user", user); 
		Request.addProperty("password", contrasena); 
	    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    Soapenvelope.dotNet=true;
	    Soapenvelope.setOutputSoapObject(Request);
	    
	    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);
	    long beforecall = System.currentTimeMillis();
	    
	    try{
	    	transporte.call(SOAP_ACTION, Soapenvelope);
	    	Log.i("TURNO","RESPUESTA EN: "+(System.currentTimeMillis()-beforecall)+"miliseg");

	    	 SoapPrimitive result = (SoapPrimitive)Soapenvelope.getResponse();  	    	
	    	
	    	 JSONArray jsonstring = new JSONArray(result.toString());
	    	 Log.i("TURNO","Registros: "+jsonstring.length());
	    	 dbclass.syncTurno(jsonstring);
	    	 Log.i("TURNO", "SINCRONIZADO");
	    	 
	    }catch(Exception e){
	    	e.printStackTrace();
	    	Log.e("TURNO2", "NO SINCRONIZADA");
	    	throw new Exception(e);
	    }  

	}
 
 public void Sync_Bonificacion_Colores(String url, String catalog, String user, String contrasena) throws Exception{
		
		String SOAP_ACTION= "http://tempuri.org/obtener_bonificacion_colores";
		String METHOD_NAME="obtener_bonificacion_colores";
		
		SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);    
		Request.addProperty("url", url); 
		Request.addProperty("catalog", catalog); 
		Request.addProperty("user", user); 
		Request.addProperty("password", contrasena); 
	    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    Soapenvelope.dotNet=true;
	    Soapenvelope.setOutputSoapObject(Request);
	    
	    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);
	    
	    long beforecall = System.currentTimeMillis();
	    
	    try{
	    	transporte.call(SOAP_ACTION, Soapenvelope);
	    	Log.i("TURNO","RESPUESTA EN: "+(System.currentTimeMillis()-beforecall)+"miliseg");

	    	 SoapPrimitive result = (SoapPrimitive)Soapenvelope.getResponse();  	    	
	    	
	    	 JSONArray jsonstring = new JSONArray(result.toString());
	    	 Log.i("BONIFICACION COLORES","Registros: "+jsonstring.length());
	    	 dbclass.syncBonificacionColores(jsonstring);
	    	 Log.i("BONIFICACION COLORES", "SINCRONIZADO");
	    	 
	    }catch(Exception e){
	    	e.printStackTrace();
	    	Log.e("BONIF COLORES", "NO SINCRONIZADA");
	    	throw new Exception(e);
	    }  

	}
	
 /*public void Sync_tabla_cliente2(){
		
		String SOAP_ACTION= "http://tempuri.org/obtenerClientes2";
		String METHOD_NAME="obtenerClientes2";
		
		SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);         
	    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    Soapenvelope.dotNet=true;
	    Soapenvelope.setOutputSoapObject(Request);

	    
	    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);
	    
	    try{
	    	transporte.call(SOAP_ACTION, Soapenvelope);
	    	SoapPrimitive result =(SoapPrimitive)Soapenvelope.getResponse();
	    	//SoapObject result =(SoapObject)Soapenvelope.getResponse();
	    	
	    	  dbclass.getReadableDatabase().delete(DBtables.Cliente.TAG, null, null);
	    	  SQLiteDatabase db = dbclass.getWritableDatabase();
	    	 
	    	 */ 
	    	/* for (int i = 0; i <result.toString().length(); i++)
	    	 {
	    	        SoapObject ic = (SoapObject)result.getProperty(i);
	    	  
	    	        ContentValues cv = new ContentValues();
	    	        
	    	        cv.put(DBtables.Cliente.PK_CODCLI, ic.getProperty(0).toString().trim());
	    			cv.put(DBtables.Cliente.NOMCLI, ic.getProperty(1).toString());
	    			cv.put(DBtables.Cliente.RUCCLI, ic.getProperty(2).toString());
	    			cv.put(DBtables.Cliente.DIRCLI, ic.getProperty(3).toString());
	    			cv.put(DBtables.Cliente.TLFCLI, ic.getProperty(4).toString());
	    			cv.put(DBtables.Cliente.STDCLI, ic.getProperty(5).toString());
	    			cv.put(DBtables.Cliente.COMPROBANTE, ic.getProperty(6).toString());
	    			cv.put(DBtables.Cliente.CODDEP, ic.getProperty(7).toString());
	    			cv.put(DBtables.Cliente.CODPROV, ic.getProperty(8).toString());
	    			cv.put(DBtables.Cliente.UBIGEO, ic.getProperty(9).toString());
	    			cv.put(DBtables.Cliente.EMAIL, ic.getProperty(10).toString());
	    			cv.put(DBtables.Cliente.TIPO_DOCUMENTO, Integer.parseInt(ic.getProperty(11).toString()));
	    			cv.put(DBtables.Cliente.TIPO_CLIENTE, Integer.parseInt(ic.getProperty(12).toString()));
	    			cv.put(DBtables.Cliente.TIEMPO_CREDITO, ic.getProperty(13).toString());
	    			cv.put(DBtables.Cliente.LIMITE_CREDITO, ic.getProperty(14).toString());
	    			cv.put(DBtables.Cliente.PERSONA, ic.getProperty(15).toString());
	    			cv.put(DBtables.Cliente.AFECTO, ic.getProperty(16).toString());
	    			cv.put(DBtables.Cliente.SEC_VISITA, Integer.parseInt(ic.getProperty(17).toString()));
	    			cv.put(DBtables.Cliente.CONDICION_VENTA, ic.getProperty(18).toString());
	    			cv.put(DBtables.Cliente.LATITUD, ic.getProperty(19).toString());
	    			cv.put(DBtables.Cliente.LONGITUD, ic.getProperty(20).toString());
	    			
	    	        
	    			db.insert(DBtables.Cliente.TAG, null, cv);
  	       
	    	 }*//*
	    	 
	    	 db.close();
	    	 Log.i("Cliente", "sincronizada");
	    	
	    }catch(Exception e){
	    	e.printStackTrace();
	    	 Log.i("Cliente", "no sincronizada");
	    }   		
	}*/
 
 public void Sync_tabla_cliente(String url, String catalog, String user, String contrasena){
		
		String SOAP_ACTION= "http://tempuri.org/obtenerClientes";
		String METHOD_NAME="obtenerClientes";
		
		SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);   
		Request.addProperty("url", url); 
		Request.addProperty("catalog", catalog); 
		Request.addProperty("user", user); 
		Request.addProperty("password", contrasena); 
	    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    Soapenvelope.dotNet=true;
	    Soapenvelope.setOutputSoapObject(Request);
	    
	    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);
	    
	    try{
	    	transporte.call(SOAP_ACTION, Soapenvelope);
	    	
	    	SoapObject result =(SoapObject)Soapenvelope.getResponse();
	    	  
	    	  dbclass.getReadableDatabase().delete(DBtables.Cliente.TAG, null, null);
	    	  SQLiteDatabase db = dbclass.getWritableDatabase();
	    	 
	    	  
	    	 for (int i = 0; i <result.getPropertyCount(); i++)
	    	 {
	    	        SoapObject ic = (SoapObject)result.getProperty(i);
	    	  
	    	        ContentValues cv = new ContentValues();
	    	        
	    	        cv.put(DBtables.Cliente.PK_CODCLI, ic.getProperty(0).toString().trim());
	    			cv.put(DBtables.Cliente.NOMCLI, ic.getProperty(1).toString().trim());
	    			cv.put(DBtables.Cliente.RUCCLI, ic.getProperty(2).toString().trim());
	    			cv.put(DBtables.Cliente.STDCLI, ic.getProperty(3).toString().trim());
	    			cv.put(DBtables.Cliente.COMPROBANTE, ic.getProperty(4).toString().trim());
	    		
	    			cv.put(DBtables.Cliente.EMAIL, ic.getProperty(5).toString().trim());
	    			cv.put(DBtables.Cliente.TIPO_DOCUMENTO, Integer.parseInt(ic.getProperty(6).toString()));
	    			cv.put(DBtables.Cliente.TIPO_CLIENTE, Integer.parseInt(ic.getProperty(7).toString()));
	    			cv.put(DBtables.Cliente.TIEMPO_CREDITO, ic.getProperty(8).toString().trim());
	    			cv.put(DBtables.Cliente.LIMITE_CREDITO, ic.getProperty(9).toString().trim());
	    			cv.put(DBtables.Cliente.PERSONA, ic.getProperty(10).toString().trim());
	    			cv.put(DBtables.Cliente.AFECTO, ic.getProperty(11).toString().trim());
	    		
	    			cv.put(DBtables.Cliente.CONDICION_VENTA, ic.getProperty(12).toString().trim());
	    			cv.put(DBtables.Cliente.CODIGO_FAMILIAR, ic.getProperty(13).toString().trim());
	    			cv.put(DBtables.Cliente.FECHA_COMPRA, ic.getProperty(14).toString().trim());
	    			cv.put(DBtables.Cliente.MONTO_COMPRA, ic.getProperty(15).toString().trim());
	    				    	
	    			Log.d("INSERTANDO ...","cod: "+ ic.getProperty(0).toString().trim());
	    			db.insert(DBtables.Cliente.TAG, null, cv);
	       
	    	 }
	    	 
	    	 db.close();
	    	 Log.i("Cliente", "sincronizada");
	    	
	    }catch(Exception e){
	    	e.printStackTrace();
	    	 Log.i("Cliente", "no sincronizada");
	    }   		
	}
 
 public void Sync_tabla_clientexVendedor(String codven, String url, String catalog, String user, String contrasena) throws Exception{
		
		String SOAP_ACTION= "http://tempuri.org/obtenerClientesxVendedor_json";
		String METHOD_NAME="obtenerClientesxVendedor_json";
		long beforecall;
		
		SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);
		Request.addProperty("codven", codven);
		Request.addProperty("url", url); 
		Request.addProperty("catalog", catalog); 
		Request.addProperty("user", user); 
		Request.addProperty("password", contrasena); 
	    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    Soapenvelope.dotNet=true;
	    Soapenvelope.setOutputSoapObject(Request);
	    
	    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService,30000);
	    
	    beforecall = System.currentTimeMillis();
	    
	    try{
	    	transporte.call(SOAP_ACTION, Soapenvelope);
	    	
	    	Log.i("CLIENTES","RESPUESTA EN: "+(System.currentTimeMillis()-beforecall)+"miliseg");
	    	
	    	  SoapPrimitive result = (SoapPrimitive)Soapenvelope.getResponse();  
	    	  
	    	  JSONArray jsonstring = new JSONArray(result.toString());
	    	  Log.i("CLIENTES","Registros: "+jsonstring.length());
	    	  Log.d("CLIENTES", "**************************************************************");
	    	  Log.d("DBSync_soap_manager ::Sync_tabla_clientexVendedor::",jsonstring.toString());
	    	  
	    	  dbclass.syncClientexVendedor(jsonstring);
	    	  
	    	 Log.i("CLIENTES", "SINCRONIZADA");
	    	
	    }catch(Exception e){
	    	 e.printStackTrace();
	    	 Log.e("CLIENTES", "NO SINCRONIZADA");
	    	 throw new Exception(e);
	    }

	}
/*
 public void Sync_tabla_cta_ingresos(String codven, String url, String catalog, String user, String contrasena) throws Exception{
		
		String SOAP_ACTION= "http://tempuri.org/obtenerCtasIngresosxVendedor_json";
		String METHOD_NAME="obtenerCtasIngresosxVendedor_json";
		
		SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);  
		Request.addProperty("codven", codven); 
		Request.addProperty("url", url); 
		Request.addProperty("catalog", catalog); 
		Request.addProperty("user", user); 
		Request.addProperty("password", contrasena); 
	    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    Soapenvelope.dotNet=true;
	    Soapenvelope.setOutputSoapObject(Request);
	    
	    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);
	    
	    long beforecall = System.currentTimeMillis();
	    
	    try{
	    	transporte.call(SOAP_ACTION, Soapenvelope);
	    	Log.i("CTA_INGRESOS","RESPUESTA EN: "+(System.currentTimeMillis()-beforecall)+"miliseg");

	    	SoapPrimitive result = (SoapPrimitive)Soapenvelope.getResponse();

	    	JSONArray jsonstring = new JSONArray(result.toString());
	    	Log.i("CTA_INGRESOS","Registros: "+jsonstring.length());
	    	dbclass.syncCtaIngresos(jsonstring);
	    	Log.i("CTA_INGRESOS","SINCRONIZADA");
	    	 
	    }catch(Exception e){
	    	e.printStackTrace();
	    	throw new Exception(e);
	    }  

	}
 */
 
 public void Sync_tabla_ctas_xbanco(String url, String catalog, String user, String contrasena) throws Exception{
		
		String SOAP_ACTION= "http://tempuri.org/obtenerCtasxBanco_json";
		String METHOD_NAME="obtenerCtasxBanco_json";
		
		SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);        
		Request.addProperty("url", url); 
		Request.addProperty("catalog", catalog); 
		Request.addProperty("user", user); 
		Request.addProperty("password", contrasena); 
	    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    Soapenvelope.dotNet=true;
	    Soapenvelope.setOutputSoapObject(Request);
	    
	    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);
	    
	    long beforecall = System.currentTimeMillis();
	    
	    try{
	    	transporte.call(SOAP_ACTION, Soapenvelope);
	    	Log.i("CTAS_X_BANCO","RESPUESTA EN: "+(System.currentTimeMillis()-beforecall)+"miliseg");

	    	SoapPrimitive result = (SoapPrimitive)Soapenvelope.getResponse();  
	    	
	    	JSONArray jsonstring = new JSONArray(result.toString());
	    	Log.i("CTAS_X_BANCO","Registros: "+jsonstring.length());
	    	dbclass.syncCtasxBanco(jsonstring);
	    	Log.i("CTAS_X_BANCO", "SINCRONIZADA");
	    	
	    }catch(Exception e){
	    	e.printStackTrace();
	    	Log.e("CTAS_X_BANCO", "NO SINCRONIZADA");
	    	throw new Exception(e);
	    }   		

	}
 
 
 public void Sync_tabla_depositos(String codven, String url, String catalog, String user, String contrasena) throws Exception{
		
	    String SOAP_ACTION= "http://tempuri.org/obtenerDepositos_json";
		String METHOD_NAME="obtenerDepositos_json";
		
		SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);    
		Request.addProperty("url", url); 
		Request.addProperty("catalog", catalog); 
		Request.addProperty("user", user); 
		Request.addProperty("password", contrasena); 
		Request.addProperty("codven", codven); 
	    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    Soapenvelope.dotNet=true;
	    Soapenvelope.setOutputSoapObject(Request);
	    
	    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);
	    
	    long beforecall = System.currentTimeMillis();
	    
	    try{
	    	transporte.call(SOAP_ACTION, Soapenvelope);
	    	Log.i("DEPOSITOS","RESPUESTA EN: "+(System.currentTimeMillis()-beforecall)+"miliseg");

	    	SoapPrimitive result =(SoapPrimitive)Soapenvelope.getResponse();
	    	
	    	JSONArray jsonstring = new JSONArray(result.toString());
	    	Log.i("DEPOSITOS","Registros: "+jsonstring.length());
	    	dbclass.syncDepositos(jsonstring);
	    	Log.i("DEPOSITOS", "SINCRONIZADA");
	    	 
	    }catch(Exception e){
	    	e.printStackTrace();
	    	Log.e("DEPOSITOS", "NO SINCRONIZADA");
	    	throw new Exception(e);
	    }

	}
 
 
 public void Sync_tabla_direccion_cliente(String codven, String url, String catalog, String user, String contrasena ) throws Exception{
	 	ArrayList<HeaderProperty> headerPropertyArrayList = new ArrayList<HeaderProperty>();
	    headerPropertyArrayList.add(new HeaderProperty("Connection", "open"));
		String SOAP_ACTION= "http://tempuri.org/obtenerDireccionCliente_json";
		String METHOD_NAME="obtenerDireccionCliente_json";
		
		SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);     
		Request.addProperty("codven", codven);
		Request.addProperty("url", url); 
		Request.addProperty("catalog", catalog); 
		Request.addProperty("user", user); 
		Request.addProperty("password", contrasena); 
	    SoapSerializationEnvelope Soapenvelope2 =new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    Soapenvelope2.dotNet=true;
	    Soapenvelope2.setOutputSoapObject(Request);
	    
	    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService,30000);
	    //HttpTransportSE androidHttpTransport = new HttpTransportSE(URL+GlobalVar.urlService,10000);
	    
	    long beforecall = System.currentTimeMillis();
	    
	    try{
	    	//System.setProperty("http.keepAlive", "true");
	    	//Soapenvelope2.setAddAdornments(true);            
	    	transporte.call(SOAP_ACTION, Soapenvelope2,headerPropertyArrayList);
	    	//transporte.call(SOAP_ACTION, Soapenvelope2);
	    	Log.i("DIRECCION CLIENTE","RESPUESTA EN: "+(System.currentTimeMillis()-beforecall)+"miliseg");

	    	SoapPrimitive result = (SoapPrimitive)Soapenvelope2.getResponse();

	    	JSONArray jsonstring = new JSONArray(result.toString());
	    	Log.i("DIRECCION CLIENTE","Registros: "+jsonstring.length());
	    	dbclass.syncDireccionCliente(jsonstring);
	    	Log.i("DIRECCION CLIENTE", "SINCRONIZADA");
	    	 
	    }catch(Exception e){
	    	e.printStackTrace();
	    	Log.e("DIRECCION CLIENTE", "NO SINCRONIZADA");
	    	throw new Exception(e);
	    }
	}
 
 
 public void Sync_tabla_familia( String url, String catalog, String user, String contrasena) throws Exception{
	 	ArrayList<HeaderProperty> headerPropertyArrayList = new ArrayList<HeaderProperty>();
	    headerPropertyArrayList.add(new HeaderProperty("Connection", "open"));
	    String SOAP_ACTION= "http://tempuri.org/obtenerFamilia_json";
		String METHOD_NAME="obtenerFamilia_json";
		
		SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);  
		Request.addProperty("url", url); 
		Request.addProperty("catalog", catalog); 
		Request.addProperty("user", user); 
		Request.addProperty("password", contrasena); 
	    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    Soapenvelope.dotNet=true;
	    Soapenvelope.setOutputSoapObject(Request);
	    
	    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);
	    
	    long beforecall = System.currentTimeMillis();
	    
	    try{
	    	transporte.call(SOAP_ACTION, Soapenvelope,headerPropertyArrayList);
	    	Log.i("FAMILIA","RESPUESTA EN: "+(System.currentTimeMillis()-beforecall)+"miliseg");

	    	SoapPrimitive result = (SoapPrimitive)Soapenvelope.getResponse();  

	    	JSONArray jsonstring = new JSONArray(result.toString());
	    	Log.i("FAMILIA","Registros: "+jsonstring.length());
	    	dbclass.syncFamilia(jsonstring);
	    	Log.i("FAMILIA", "SINCRONIZADA");
	    	
	    }catch(Exception e){
	    	e.printStackTrace();
	    	Log.i("FAMILIA", "NO SINCRONIZADA");
	    	throw new Exception(e);
	    }
	}
 
 public void Sync_tabla_ingresos(String codven, String url, String catalog, String user, String contrasena) throws Exception{
		
		String SOAP_ACTION= "http://tempuri.org/obtenerIngresos_Movil_json";
		String METHOD_NAME="obtenerIngresos_Movil_json";
		
		Log.d("codven",""+codven);
		SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME); 
		Request.addProperty("codven",codven);
		Request.addProperty("url", url); 
		Request.addProperty("catalog", catalog); 
		Request.addProperty("user", user); 
		Request.addProperty("password", contrasena); 
	    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    Soapenvelope.dotNet=true;
	    Soapenvelope.setOutputSoapObject(Request);
	    
	    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);
	    
	    long beforecall = System.currentTimeMillis();
	    
	    try{
	    	transporte.call(SOAP_ACTION, Soapenvelope);
	    	Log.i("INGRESOS","RESPUESTA EN: "+(System.currentTimeMillis()-beforecall)+"miliseg");

	    	 SoapPrimitive result = (SoapPrimitive)Soapenvelope.getResponse();	    	  
	    
	    	 JSONArray jsonstring = new JSONArray(result.toString());
	    	 Log.i("INGRESOS","Registros: "+jsonstring.length());
	    	 dbclass.syncIngresos(jsonstring, codven);
	    	 Log.i("INGRESOS", "SINCRONIZADA");
	    	 
	    	 dbclass.forzar_actualizacion_acuenta_ctaingresos();
	    	 Log.i("SINCRONIZACION INGRESOS EXTRA","actualizacion de acuenta en cta_ingresos forzada");
	    	
	    }catch(Exception e){
	    	e.printStackTrace();
	    	Log.e("INGRESOS", "NO SINCRONIZADA");
	    	throw new Exception(e);
	    } 

	}
 
 
 public void Sync_tabla_motivo_noventa(String url, String catalog, String user, String contrasena ) throws Exception{
		
		String SOAP_ACTION= "http://tempuri.org/obtenerMotivoNoVenta_json";
		String METHOD_NAME="obtenerMotivoNoVenta_json";
		
		SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);     
		Request.addProperty("url", url); 
		Request.addProperty("catalog", catalog); 
		Request.addProperty("user", user); 
		Request.addProperty("password", contrasena); 
	    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    Soapenvelope.dotNet=true;
	    Soapenvelope.setOutputSoapObject(Request);
	    
	    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);
	    
	    long beforecall = System.currentTimeMillis();
	    
	    try{
	    	transporte.call(SOAP_ACTION, Soapenvelope);
	    	Log.i("MOTIVO NO VENTA","RESPUESTA EN: "+(System.currentTimeMillis()-beforecall)+"miliseg");

	    	SoapPrimitive result = (SoapPrimitive)Soapenvelope.getResponse();  

	    	 JSONArray jsonstring = new JSONArray(result.toString());
	    	 Log.i("MOTIVO NO VENTA", "Registros: "+jsonstring.length());
	    	 dbclass.syncMotivoNoVenta(jsonstring);
	    	 Log.i("MOTIVO NO VENTA", "SINCRONIZADA");
	    	
	    }catch(Exception e){
	    	e.printStackTrace();
	    	Log.e("MOTIVO NO VENTA", "NO SINCRONIZADA");
	    	throw new Exception(e);
	    }   		

	}
 
 
 public void Sync_tabla_mta_kardex(String codven, String url, String catalog, String user, String contrasena) throws Exception{
		
		String SOAP_ACTION="http://tempuri.org/obtenerMtaKardex_json";
		String METHOD_NAME="obtenerMtaKardex_json";
		
		SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);   
		Request.addProperty("url", url); 
		Request.addProperty("catalog", catalog); 
		Request.addProperty("user", user); 
		Request.addProperty("password", contrasena); 
		Request.addProperty("codven", codven);
	    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    Soapenvelope.dotNet=true;
	    Soapenvelope.setOutputSoapObject(Request);
	    
	    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);
	    
	    long beforecall = System.currentTimeMillis();
	    
	    try{
	    	transporte.call(SOAP_ACTION, Soapenvelope);
	    	Log.i("MTA_KARDEX","RESPUESTA EN: "+(System.currentTimeMillis()-beforecall)+"miliseg");

	    	SoapPrimitive result = (SoapPrimitive)Soapenvelope.getResponse();
	    	
	    	JSONArray jsonstring = new JSONArray(result.toString());
	    	Log.i("MTA_KARDEX","Registros: "+jsonstring.length());
	    	dbclass.syncMtaKardex(jsonstring);  
	    	Log.i("MTA_KARDEX", "SINCRONIZADA");
	    	
	    }catch(Exception e){
	    	e.printStackTrace();
	    	Log.i("MTA_KARDEX", "NO SINCRONIZADA");
	    	throw new Exception(e);
	    }  

	}
 
 
 public void Sync_tabla_pedido_cabecera(){
		
		String SOAP_ACTION= "http://tempuri.org/obtenerPedidoCabecera";
		String METHOD_NAME="obtenerPedidoCabecera";
		
		SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);         
	    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    Soapenvelope.dotNet=true;
	    Soapenvelope.setOutputSoapObject(Request);
	    
	    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);
	    
	    try{
	    	transporte.call(SOAP_ACTION, Soapenvelope);
	    	
	    	SoapObject result =(SoapObject)Soapenvelope.getResponse();
	    	  
	    	  dbclass.getReadableDatabase().delete(DBtables.Pedido_cabecera.TAG, null, null);
	    	  SQLiteDatabase db = dbclass.getWritableDatabase();
	    	 
	    	  
	    	 for (int i = 0; i <result.getPropertyCount(); i++)
	    	 {
	    	        SoapObject ic = (SoapObject)result.getProperty(i);
	    	  
	    	        ContentValues cv = new ContentValues();
	    	        
	    	        cv.put(DBtables.Pedido_cabecera.PK_OC_NUMERO, ic.getProperty(0).toString());
	    	        cv.put(DBtables.Pedido_cabecera.SITIO_ENFA, ic.getProperty(1).toString());
	    	        cv.put(DBtables.Pedido_cabecera.MONTO_TOTAL, Double.parseDouble(ic.getProperty(2).toString()));
	    	        cv.put(DBtables.Pedido_cabecera.VALOR_IGV, Double.parseDouble(ic.getProperty(3).toString()));
	    	        cv.put(DBtables.Pedido_cabecera.MONEDA, ic.getProperty(4).toString());
	    	        cv.put(DBtables.Pedido_cabecera.FECHA_OC, ic.getProperty(5).toString());
	    	        cv.put(DBtables.Pedido_cabecera.FECHA_MXE, ic.getProperty(6).toString());
	    	        cv.put(DBtables.Pedido_cabecera.COND_PAGO, ic.getProperty(7).toString());
	    	        
	    	        cv.put(DBtables.Pedido_cabecera.COD_CLI, ic.getProperty(8).toString());
	    	        cv.put(DBtables.Pedido_cabecera.COD_EMP, ic.getProperty(9).toString());
	    	        cv.put(DBtables.Pedido_cabecera.ESTADO, ic.getProperty(10).toString());
	    	        cv.put(DBtables.Pedido_cabecera.USERNAME, ic.getProperty(11).toString());
	    	        cv.put(DBtables.Pedido_cabecera.RUTA, ic.getProperty(12).toString());
	    	        cv.put(DBtables.Pedido_cabecera.OBSERV, ic.getProperty(13).toString());
	    	        cv.put(DBtables.Pedido_cabecera.COD_NOVENTA, Integer.parseInt(ic.getProperty(14).toString()));
	    	        cv.put(DBtables.Pedido_cabecera.PESO_TOTAL, Double.parseDouble(ic.getProperty(15).toString()));
	    	        cv.put(DBtables.Pedido_cabecera.FLAG, ic.getProperty(16).toString());
	    	        
	    			db.insert(DBtables.Pedido_cabecera.TAG, null, cv);
	       
	    	 }
	    	
	    	 db.close();
	    	 Log.i("Pedido_cabecera", "sincronizada");
	    	 
	    }catch(Exception e){
	    	e.printStackTrace();
	    }   		
	}
 
 public void Sync_tabla_pedido_cabeceraxVendedor(String codven, String url, String catalog, String user, String contrasena){

		String SOAP_ACTION= "http://tempuri.org/obtenerPedidoCabeceraxVendedor_k";
		String METHOD_NAME="obtenerPedidoCabeceraxVendedor_k";
		
		SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);   
		Request.addProperty("codven", codven); 
		Request.addProperty("url", url); 
		Request.addProperty("catalog", catalog); 
		Request.addProperty("user", user); 
		Request.addProperty("password", contrasena); 
	    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    Soapenvelope.dotNet=true;
	    Soapenvelope.setOutputSoapObject(Request);
	    
	    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);
    
	    long beforecall = System.currentTimeMillis();
	    
	    try{
	    	transporte.call(SOAP_ACTION, Soapenvelope);
	    	Log.i("PEDIDO_CABECERA","RESPUESTA EN: "+(System.currentTimeMillis()-beforecall)+"miliseg");
	    	SoapObject result =(SoapObject)Soapenvelope.getResponse();
	    	  
	    //	  dbclass.getReadableDatabase().delete(DBtables.Pedido_cabecera.TAG, null, null);
	    	  dbclass.EliminarPedido_cabecera_enviados(codven);
	    	  SQLiteDatabase db = dbclass.getWritableDatabase();
	    	  
	    	  db.beginTransaction();
	    	  
	    	  try{
	    		  
	 	    	 for (int i = 0; i <result.getPropertyCount(); i++)
		    	 {
		    	        SoapObject ic = (SoapObject)result.getProperty(i);
		    	  
		    	        ContentValues cv = new ContentValues();
		    	        Log.w("PEDIDO BAJANDO", ic.getProperty(0).toString().trim() + "");
		    	        
		    	        //if(existePedido(ic.getProperty(0).toString().trim())){
		    
		    	        //}
		    	        //else{
		    	        /*	DBPedido_Cabecera entityPedidoCabecera = new DBPedido_Cabecera();
		    	        	entityPedidoCabecera.setOc_numero(ic.getProperty(0).toString().trim());
		    	        	entityPedidoCabecera.setSitio_enfa(ic.getProperty(1).toString().trim());
		    	        	entityPedidoCabecera.setMonto_total(ic.getProperty(2).toString().trim());
		    	        	entityPedidoCabecera.setValor_igv(ic.getProperty(3).toString().trim());
		    	        	entityPedidoCabecera.setMoneda(ic.getProperty(4).toString().trim());
		    	        	entityPedidoCabecera.setFecha_oc(ic.getProperty(5).toString().trim());
		    	        	entityPedidoCabecera.setFecha_mxe(ic.getProperty(6).toString().trim());
		    	        	entityPedidoCabecera.setCond_pago(ic.getProperty(7).toString().trim());
		    	        	entityPedidoCabecera.setCod_cli(ic.getProperty(8).toString().trim());
		    	        	entityPedidoCabecera.setCod_emp(ic.getProperty(9).toString().trim());
		    	        	entityPedidoCabecera.setEstado(ic.getProperty(10).toString().trim());
		    	        	entityPedidoCabecera.setUsername(ic.getProperty(11).toString().trim());
		    	        	entityPedidoCabecera.setRuta(ic.getProperty(12).toString().trim());
		    	        	entityPedidoCabecera.setObserv(ic.getProperty(13).toString().trim());
		    	        	entityPedidoCabecera.setCod_noventa(Integer.parseInt(ic.getProperty(14).toString().trim()));
		    	        	entityPedidoCabecera.setPeso_total(ic.getProperty(15).toString().trim());
		    	        	entityPedidoCabecera.setFlag(ic.getProperty(16).toString().trim());
		    	        	entityPedidoCabecera.setLatitud(ic.getProperty(17).toString().trim());
		    	        	entityPedidoCabecera.setLongitud(ic.getProperty(18).toString().trim());
		    	        	entityPedidoCabecera.setCodigo_familiar(ic.getProperty(19).toString().trim());
		    	        	entityPedidoCabecera.setDT_PEDI_FECHASERVIDOR(ic.getProperty(20).toString().trim());
		    	        
		    	            dbclass.AgregarPedidoCabecera(entityPedidoCabecera);*/
		    	        	
		    	        	cv.put(DBtables.Pedido_cabecera.PK_OC_NUMERO, ic.getProperty(0).toString().trim());
			    	        cv.put(DBtables.Pedido_cabecera.SITIO_ENFA, ic.getProperty(1).toString().trim());
			    	        cv.put(DBtables.Pedido_cabecera.MONTO_TOTAL, ic.getProperty(2).toString().trim());
			    	        cv.put(DBtables.Pedido_cabecera.VALOR_IGV, ic.getProperty(3).toString().trim());
			    	        cv.put(DBtables.Pedido_cabecera.MONEDA, ic.getProperty(4).toString().trim());
			    	        cv.put(DBtables.Pedido_cabecera.FECHA_OC, ic.getProperty(5).toString().trim());
			    	        cv.put(DBtables.Pedido_cabecera.FECHA_MXE, ic.getProperty(6).toString().trim());
			    	        cv.put(DBtables.Pedido_cabecera.COND_PAGO, ic.getProperty(7).toString().trim());
			    	        cv.put(DBtables.Pedido_cabecera.COD_CLI, ic.getProperty(8).toString().trim());
			    	        cv.put(DBtables.Pedido_cabecera.COD_EMP, ic.getProperty(9).toString().trim());
			    	        cv.put(DBtables.Pedido_cabecera.ESTADO, ic.getProperty(10).toString().trim());
			    	        cv.put(DBtables.Pedido_cabecera.USERNAME, ic.getProperty(11).toString().trim());
			    	        cv.put(DBtables.Pedido_cabecera.RUTA, ic.getProperty(12).toString().trim());
			    	        cv.put(DBtables.Pedido_cabecera.OBSERV, ic.getProperty(13).toString().trim());
			    	        cv.put(DBtables.Pedido_cabecera.COD_NOVENTA, Integer.parseInt(ic.getProperty(14).toString().trim()));
			    	        cv.put(DBtables.Pedido_cabecera.PESO_TOTAL, ic.getProperty(15).toString().trim());
			    	        //cv.put(DBtables.Pedido_cabecera.FLAG, ic.getProperty(16).toString().trim());
			    	        cv.put(DBtables.Pedido_cabecera.FLAG, "E");
			    	        cv.put(DBtables.Pedido_cabecera.LATITUD,ic.getProperty(17).toString().trim());
			    	        cv.put(DBtables.Pedido_cabecera.LONGITUD,ic.getProperty(18).toString().trim());
			    	        cv.put(DBtables.Pedido_cabecera.CODIGO_FAMILIAR,ic.getProperty(19).toString());
			    	        cv.put(DBtables.Pedido_cabecera.DT_PEDI_FECHASERVIDOR,ic.getProperty(20).toString().trim());
		    	        	

		 	    			db.insert(DBtables.Pedido_cabecera.TAG, null, cv);
		    	        }
		    	 
		    	     db.setTransactionSuccessful();
	    		  
	    	  }catch(Exception ex){
	    		  ex.printStackTrace();
	    	  }
	  	      finally{
		    	  db.endTransaction();
		    	  db.close();
		      }
	    	  	 
	    	// }
	    	
	    	 //db.close();
	    	 Log.i("Pedido_cabecera", "sincronizada");
	    	 
	    }catch(Exception e){
	    	e.printStackTrace();
	    }

	}
 
 
 private boolean existePedido(String oc_numero) {
	return dbclass.existePedidoCabecera(oc_numero);
	
}
 
 private boolean existePedidoDetalle(String oc_numero, String item) {
	return dbclass.existePedidoDetalle(oc_numero, item);
	
}

public void Sync_tabla_pedido_detalle(){
		
		String SOAP_ACTION= "http://tempuri.org/obtenerPedidoDetalle";
		String METHOD_NAME="obtenerPedidoDetalle";
		
		SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);         
	    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    Soapenvelope.dotNet=true;
	    Soapenvelope.setOutputSoapObject(Request);
	    
	    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);
	    
	    try{
	    	transporte.call(SOAP_ACTION, Soapenvelope);
	    	
	    	SoapObject result =(SoapObject)Soapenvelope.getResponse();
	    	  
	    	  dbclass.getReadableDatabase().delete(DBtables.Pedido_detalle.TAG, null, null);
	    	  SQLiteDatabase db = dbclass.getWritableDatabase();
	    	 
	    	  
	    	 for (int i = 0; i <result.getPropertyCount(); i++)
	    	 {
	    	        SoapObject ic = (SoapObject)result.getProperty(i);
	    	  
	    	        ContentValues cv = new ContentValues();
	    	        
	    	        cv.put(DBtables.Pedido_detalle.PK_OC_NUMERO, ic.getProperty(0).toString());
	    	        cv.put(DBtables.Pedido_detalle.PK_EAN_ITEM, ic.getProperty(1).toString());
	    	        cv.put(DBtables.Pedido_detalle.CIP, ic.getProperty(2).toString());
	    	        cv.put(DBtables.Pedido_detalle.PRECIO_BRUTO, Double.parseDouble(ic.getProperty(3).toString()));
	    	        cv.put(DBtables.Pedido_detalle.PRECIO_NETO, Double.parseDouble(ic.getProperty(4).toString()));
	    	        cv.put(DBtables.Pedido_detalle.CANTIDAD, Integer.parseInt(ic.getProperty(5).toString()));
	    	        cv.put(DBtables.Pedido_detalle.TIPO_PRODUCTO, ic.getProperty(6).toString());
	    	        cv.put(DBtables.Pedido_detalle.UNIDAD_MEDIDA, Integer.parseInt(ic.getProperty(7).toString()));
	    	        cv.put(DBtables.Pedido_detalle.PESO_BRUTO, Double.parseDouble(ic.getProperty(8).toString()));
	    	        cv.put(DBtables.Pedido_detalle.FLAG, ic.getProperty(9).toString());
	    	        
	    			db.insert(DBtables.Pedido_detalle.TAG, null, cv);
	       
	    	 }
	    	
	    	 db.close();
	    	 Log.i("Pedido_detalle", "sincronizada");
	    	 
	    }catch(Exception e){
	    	e.printStackTrace();
	    }   		
	}
 
 
 public void Sync_tabla_politica_cliente( String codven, String url, String catalog, String user, String contrasena) throws Exception{
		
		String SOAP_ACTION= "http://tempuri.org/obtenerPoliticaCliente_json";
		String METHOD_NAME="obtenerPoliticaCliente_json";
		
		SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);    
		Request.addProperty("url", url); 
		Request.addProperty("catalog", catalog); 
		Request.addProperty("user", user); 
		Request.addProperty("password", contrasena); 
		Request.addProperty("codven", codven);
	    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    Soapenvelope.dotNet=true;
	    Soapenvelope.setOutputSoapObject(Request);
	    
	    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);
	    
	    long beforecall = System.currentTimeMillis();
	    
	    try{
	    	transporte.call(SOAP_ACTION, Soapenvelope);
	    	Log.i("POLITICA_CLIENTE","RESPUESTA EN: "+(System.currentTimeMillis()-beforecall)+"miliseg");

	    	SoapPrimitive result = (SoapPrimitive)Soapenvelope.getResponse();

	    	JSONArray jsonstring = new JSONArray(result.toString());
	    	Log.i("POLITICA_CLIENTE","Registros: "+jsonstring.length());
	    	dbclass.syncPoliticaCliente(jsonstring);
	    	Log.i("POLITICA_CLIENTE", "SINCRONIZADA");
	    	
	    }catch(Exception e){
	    	e.printStackTrace();
	    	Log.e("POLITICA_CLIENTE", "NO SINCRONIZADA");
	    	throw new Exception(e);
	    } 

	}
 
 public void Sync_tabla_politica_vendedor(String url, String catalog, String user, String contrasena) throws Exception{
		
		String SOAP_ACTION= "http://tempuri.org/obtenerPoliticaVendedor_json";
		String METHOD_NAME= "obtenerPoliticaVendedor_json";
		
		SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);
		Request.addProperty("url", url); 
		Request.addProperty("catalog", catalog); 
		Request.addProperty("user", user); 
		Request.addProperty("password", contrasena); 
	    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    Soapenvelope.dotNet=true;
	    Soapenvelope.setOutputSoapObject(Request);
	    
	    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);
	    
	    long beforecall = System.currentTimeMillis();
	    
	    try{
	    	transporte.call(SOAP_ACTION, Soapenvelope);
	    	Log.i("POLITICA_VENDEDOR","RESPUESTA EN: "+(System.currentTimeMillis()-beforecall)+"miliseg");

	    	 SoapPrimitive result = (SoapPrimitive)Soapenvelope.getResponse();   

	    	 JSONArray jsonstring = new JSONArray(result.toString());
	    	 Log.i("POLITICA_VENDEDOR","Registros: "+jsonstring.length());
	    	 dbclass.syncPoliticaVendedor(jsonstring);
	    	 Log.i("POLITICA_VENDEDOR", "SINCRONIZADA");
	    	
	    }catch(Exception e){
	    	e.printStackTrace();
	    	Log.i("POLITICA_VENDEDOR", "NO SINCRONIZADA");
	    	throw new Exception(e);
	    }  

	}
 
 public void Sync_tabla_politica_precio2(String codven, String url, String catalog, String user, String contrasena) throws Exception{
		
		String SOAP_ACTION= "http://tempuri.org/obtenerPoliticaPrecio2_json";
		String METHOD_NAME="obtenerPoliticaPrecio2_json";
		long beforecall;
		
		SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);     
		Request.addProperty("url", url); 
		Request.addProperty("catalog", catalog); 
		Request.addProperty("user", user); 
		Request.addProperty("password", contrasena);
		Request.addProperty("codven", codven); 
	    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    Soapenvelope.dotNet=true;
	    Soapenvelope.setOutputSoapObject(Request);
	    
	    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);
	    
	    beforecall = System.currentTimeMillis();
	    
	    try{
	    	transporte.call(SOAP_ACTION, Soapenvelope);
	    	
	    	Log.i("POLITICA_PRECIO2","RESPUESTA EN: "+(System.currentTimeMillis()-beforecall)+"miliseg");
	    	
	    	 SoapPrimitive result = (SoapPrimitive)Soapenvelope.getResponse();      	
	    	
	    	 JSONArray jsonstring = new JSONArray(result.toString());
	    	 Log.i("POLITICA_PRECIO2","Registros: "+jsonstring.length());
	    	 dbclass.syncPoliticaPrecio2(jsonstring);
	    	 Log.i("POLITICA_PRECIO2", "SINCRONIZADA");
	    	 
	    }catch(Exception e){
	    	e.printStackTrace();
	    	Log.e("POLITICA_PRECIO2", "NO SINCRONIZADA");
	    	throw new Exception(e);
	    }   

	}
 
 
 public void Sync_tabla_producto(String codven, String url, String catalog, String user, String contrasena) throws Exception{
	 
		String SOAP_ACTION= "http://tempuri.org/obtenerProductos_json";
		String METHOD_NAME="obtenerProductos_json";
		//ImageLoader imageLoader= new ImageLoader(null);  
		
		SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME); 
		Request.addProperty("url", url); 
		Request.addProperty("catalog", catalog); 
		Request.addProperty("user", user); 
		Request.addProperty("password", contrasena);
		Request.addProperty("codven", codven);
		
		
	    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    Soapenvelope.dotNet=true;
	    Soapenvelope.setOutputSoapObject(Request);
	    	    
	    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService,20000);
	    
	    long beforecall = System.currentTimeMillis();
	    
	    try{
	    	
	    	transporte.call(SOAP_ACTION, Soapenvelope);
	    	Log.i("PRODUCTO","RESPUESTA EN: "+(System.currentTimeMillis()-beforecall)+"miliseg");
	    	
	    	 SoapPrimitive result =(SoapPrimitive)Soapenvelope.getResponse();
	    	  
	    	 JSONArray jsonstring = new JSONArray(result.toString());
	    	 Log.d("DBSync_soap_manager ::Sync_tabla_producto::",jsonstring.toString());
	    	 Log.i("PRODUCTO","Registros: "+jsonstring.length());
	    	 Log.i("PRODUCTO","Registros: "+jsonstring);
	    	 dbclass.syncProducto(jsonstring);
	    	 Log.i("PRODUCTO", "SINCRONIZADA");
	    	
	    }catch(Exception e){
	    	e.printStackTrace();
	    	Log.e("PRODUCTO", "NO SINCRONIZADA");
	    	throw new Exception(e);
	    }  

	}
 
 
 public void Sync_tabla_promocion_clientes(String url, String catalog, String user, String contrasena) throws Exception{
		
		String SOAP_ACTION= "http://tempuri.org/obtenerPromocionClientes_json";
		String METHOD_NAME="obtenerPromocionClientes_json";
		
		SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);
		Request.addProperty("url", url); 
		Request.addProperty("catalog", catalog); 
		Request.addProperty("user", user); 
		Request.addProperty("password", contrasena); 
	    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    Soapenvelope.dotNet=true;
	    Soapenvelope.setOutputSoapObject(Request);
	    
	    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);
	    
	    long beforecall = System.currentTimeMillis();
	    
	    try{
	    	transporte.call(SOAP_ACTION, Soapenvelope);
	    	Log.i("PROMOCION_CLIENTES","RESPUESTA EN: "+(System.currentTimeMillis()-beforecall)+"miliseg");

	    	 SoapPrimitive result = (SoapPrimitive)Soapenvelope.getResponse();  
	    	 
	    	 JSONArray jsonstring = new JSONArray(result.toString());
	    	 Log.i("PROMOCION CLIENTES", "Registros: "+jsonstring.length());
	    	 dbclass.syncPromocionClientes(jsonstring);
	    	 Log.i("PROMOCION CLIENTES", "SINCRONIZADA");
	    	 
	    }catch(Exception e){
	    	e.printStackTrace();
	    	Log.e("PROMOCION CLIENTES", "NO SINCRONIZADA");
	    	throw new Exception(e);
	    }   

	}
 
 public void Sync_tabla_promocion_vendedor(String url, String catalog, String user, String contrasena) throws Exception{
		
		String SOAP_ACTION= "http://tempuri.org/obtenerPromocionVendedor_json";
		String METHOD_NAME="obtenerPromocionVendedor_json";
		
		SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);
		Request.addProperty("url", url); 
		Request.addProperty("catalog", catalog); 
		Request.addProperty("user", user); 
		Request.addProperty("password", contrasena); 
	    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    Soapenvelope.dotNet=true;
	    Soapenvelope.setOutputSoapObject(Request);
	    
	    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);
	    
	    long beforecall = System.currentTimeMillis();
	    
	    try{
	    	transporte.call(SOAP_ACTION, Soapenvelope);
	    	Log.i("PROMOCION_VENDEDOR","RESPUESTA EN: "+(System.currentTimeMillis()-beforecall)+"miliseg");
	    	
	    	SoapPrimitive result = (SoapPrimitive)Soapenvelope.getResponse();
	    	
	    	JSONArray jsonstring = new JSONArray(result.toString());
	    	Log.i("PROMOCION_VENDEDOR","Registros: "+jsonstring.length());
	    	dbclass.syncPromocionVendedor(jsonstring);
	    	Log.i("PROMOCION_VENDEDOR","SINCRONIZADA");
	    	 
	    }catch(Exception e){
	    	e.printStackTrace();
	    	Log.e("PROMOCION_VENDEDOR","NO SINCRONIZADA");
	    	throw new Exception(e);
	    }  

	}
 
 public void Sync_tabla_promocion_politica(String url, String catalog, String user, String contrasena) throws Exception{
		
		String SOAP_ACTION= "http://tempuri.org/obtenerPromocionPolitica_json";
		String METHOD_NAME="obtenerPromocionPolitica_json";
		
		SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);
		Request.addProperty("url", url); 
		Request.addProperty("catalog", catalog); 
		Request.addProperty("user", user); 
		Request.addProperty("password", contrasena); 
	    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    Soapenvelope.dotNet=true;
	    Soapenvelope.setOutputSoapObject(Request);
	    
	    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);
	      
	    long beforecall = System.currentTimeMillis();
	    
	    try{
	    	transporte.call(SOAP_ACTION, Soapenvelope);
	    	Log.i("PROMOCION_POLITICA","RESPUESTA EN: "+(System.currentTimeMillis()-beforecall)+"miliseg");

	    	SoapPrimitive result = (SoapPrimitive)Soapenvelope.getResponse();	    	
	    	
	    	JSONArray jsonstring = new JSONArray(result.toString());
	    	Log.i("PROMOCION_POLITICA","Registros: "+jsonstring.length());
	    	dbclass.syncPromocionPolitica(jsonstring);
	    	Log.i("PROMOCION_POLITICA", "SINCRONIZADA");
	    	 
	    }catch(Exception e){
	    	e.printStackTrace();
	    	Log.i("PROMOCION_POLITICA", "NO SINCRONIZADA");
	    	throw new Exception(e);
	    }   

	}
 
 
 public void Sync_tabla_sub_familia(String url, String catalog, String user, String contrasena) throws Exception{
		
		String SOAP_ACTION= "http://tempuri.org/obtenerSubFamilia_json";
		String METHOD_NAME="obtenerSubFamilia_json";
		
		SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME); 
		Request.addProperty("url", url); 
		Request.addProperty("catalog", catalog); 
		Request.addProperty("user", user); 
		Request.addProperty("password", contrasena);
	    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    Soapenvelope.dotNet=true;
	    Soapenvelope.setOutputSoapObject(Request);
	    
	    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);
	   
	    long beforecall = System.currentTimeMillis();
	    
	    try{
	    	transporte.call(SOAP_ACTION, Soapenvelope);
	    	Log.i("SUB_FAMILIA","RESPUESTA EN: "+(System.currentTimeMillis()-beforecall)+"miliseg");

	    	SoapPrimitive result = (SoapPrimitive)Soapenvelope.getResponse();  
	    
	    	 JSONArray jsonstring = new JSONArray(result.toString()); 
	    	 Log.i("SUB_FAMILIA","Registros: "+jsonstring.length());
	    	 dbclass.syncSubFamilia(jsonstring);
	    	 Log.i("SUB_FAMILIA", "SINCRONIZADA");
	    	
	    }catch(Exception e){
	    	e.printStackTrace();
	    	Log.e("SUB_FAMILIA", "NO SINCRONIZADA");
	    	throw new Exception(e);
	    }  

	}
 
 
 public void Sync_tabla_promocion_detalle( String url, String catalog, String user, String contrasena){
		
		String SOAP_ACTION= "http://tempuri.org/obtenerPromocionDetalle";
		String METHOD_NAME="obtenerPromocionDetalle";
		
		SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME); 
		Request.addProperty("url", url); 
		Request.addProperty("catalog", catalog); 
		Request.addProperty("user", user); 
		Request.addProperty("password", contrasena); 
	    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    Soapenvelope.dotNet=true;
	    Soapenvelope.setOutputSoapObject(Request);
	    
	    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);
	    
	    try{
	    	transporte.call(SOAP_ACTION, Soapenvelope);
	    	
	    	SoapObject result =(SoapObject)Soapenvelope.getResponse();
	    	  
	    	  dbclass.getReadableDatabase().delete(DBtables.Promocion_Detalle.TAG, null, null);
	    	  SQLiteDatabase db = dbclass.getWritableDatabase();
	    	 
	    	  
	    	 for (int i = 0; i <result.getPropertyCount(); i++)
	    	 {
	    	        SoapObject ic = (SoapObject)result.getProperty(i);
	    	  
	    	        ContentValues cv = new ContentValues();
	    	        
	    	        cv.put(DBtables.Promocion_Detalle.SECUENCIA, Integer.parseInt(ic.getProperty(0).toString()));
	    	        cv.put(DBtables.Promocion_Detalle.GENERAL, Integer.parseInt(ic.getProperty(1).toString()));
	    	        cv.put(DBtables.Promocion_Detalle.PROMOCION, ic.getProperty(2).toString().trim());
	    	        cv.put(DBtables.Promocion_Detalle.CODALM, ic.getProperty(3).toString().trim());
	    	        cv.put(DBtables.Promocion_Detalle.TIPO, ic.getProperty(4).toString().trim());
	    	        cv.put(DBtables.Promocion_Detalle.ITEM, Integer.parseInt(ic.getProperty(5).toString()));
	    	        cv.put(DBtables.Promocion_Detalle.AGRUPADO, Integer.parseInt(ic.getProperty(6).toString()));
	    	        cv.put(DBtables.Promocion_Detalle.ENTRADA, ic.getProperty(7).toString().trim());
	    	        cv.put(DBtables.Promocion_Detalle.MONTO, Double.parseDouble(ic.getProperty(8).toString()));
	    	        cv.put(DBtables.Promocion_Detalle.CONDICION, ic.getProperty(9).toString().trim());
	    	        cv.put(DBtables.Promocion_Detalle.CANT_CONDICION, Integer.parseInt(ic.getProperty(10).toString()));
	    	        cv.put(DBtables.Promocion_Detalle.SALIDA, ic.getProperty(11).toString().trim());
	    	        cv.put(DBtables.Promocion_Detalle.CANT_PROMOCION, Integer.parseInt(ic.getProperty(12).toString()));
	    	        cv.put(DBtables.Promocion_Detalle.MAX_PEDIDO, Integer.parseInt(ic.getProperty(13).toString()));
	    	        cv.put(DBtables.Promocion_Detalle.TOTAL_AGRUPADO, Integer.parseInt(ic.getProperty(14).toString()));
	    	        
	    			db.insert(DBtables.Promocion_Detalle.TAG, null, cv);
	       
	    	 }
	    	
	    	 db.close();
	    	 Log.i("Promocion_detalle", "sincronizada");
	    	 
	    }catch(Exception e){
	    	e.printStackTrace();
	    }   		
	}
 
 
 public void Sync_tabla_unidad_medida(String url, String catalog, String user, String contrasena) throws Exception{
		
		String SOAP_ACTION= "http://tempuri.org/obtenerUnidadMedida_json";
		String METHOD_NAME="obtenerUnidadMedida_json";
		
		SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME); 
		Request.addProperty("url", url); 
		Request.addProperty("catalog", catalog); 
		Request.addProperty("user", user); 
		Request.addProperty("password", contrasena); 
	    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    Soapenvelope.dotNet=true;
	    Soapenvelope.setOutputSoapObject(Request);
	    
	    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);
	    
	    long beforecall = System.currentTimeMillis();
	    
	    try{
	    	transporte.call(SOAP_ACTION, Soapenvelope);
	    	Log.i("UNIDAD_MEDIDA","RESPUESTA EN: "+(System.currentTimeMillis()-beforecall)+"miliseg");

	    	 SoapPrimitive result = (SoapPrimitive)Soapenvelope.getResponse();  	    
	    	
	    	 JSONArray jsonstring = new JSONArray(result.toString());
	    	 Log.i("UNIDAD_MEDIDA","Registros: "+jsonstring.length());
	    	 dbclass.syncUnidadMedida(jsonstring);
	    	 Log.i("UNIDAD_MEDIDA", "SINCRONIZADA");
	    	 
	    }catch(Exception e){
	    	e.printStackTrace();
	    	Log.i("UNIDAD_MEDIDA", "NO SINCRONIZADA");
	    	throw new Exception(e);
	    } 

	}
 
 //ENVIAR DATOS A LA WEB SERVICE
 public void actualizarCtaIngresos() {

	   	DBclasses obj_dbclasses= new DBclasses(context);
	 
		try 
		{
			String SOAP_ACTION= "http://tempuri.org/actualizarCtasIngresos";
			String METHOD_NAME="actualizarCtasIngresos";
			   ArrayList<DBCta_Ingresos>  lista_cta_ingresos= new ArrayList<DBCta_Ingresos>();
				lista_cta_ingresos= obj_dbclasses.getCtas_Ingresos();
				
			
				
				Iterator<DBCta_Ingresos> it=lista_cta_ingresos.iterator();
				  
				  while ( it.hasNext() ) { 
					  Object objeto = it.next(); 
					  DBCta_Ingresos cta = (DBCta_Ingresos)objeto;
					  
						PropertyInfo pi = new PropertyInfo();
						pi.setName("db_cta_ingresos");
						pi.setValue(cta);
						pi.setType(cta.getClass());
					  
						SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);				
						
						request.addProperty(pi);
						//
						request.addProperty("url", url); 
						request.addProperty("catalog", catalog); 
						request.addProperty("user", user); 
						request.addProperty("password", contrasena); 
						//
						
						SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
						envelope.dotNet = true; 
						envelope.setOutputSoapObject(request);
						envelope.addMapping(NAMESPACE, "DBCta_Ingresos", cta.getClass());

						HttpTransportSE transporte = new HttpTransportSE(URL+GlobalVar.urlService);
						transporte.call(SOAP_ACTION, envelope);

						SoapPrimitive resultado_xml =(SoapPrimitive)envelope.getResponse();
						String res = resultado_xml.toString();
						
						if(res.equals("1"))
						{
							 Log.i("CTA INGRESOS", "datos subidos al servidor");
						}
						else
						{
							Log.i("CTA INGRESOS", "Error al actualizar datos"); 
						}
					
				}		
	  
		
		} 
		catch (Exception e) 
		{
			Log.i("CTA INGRESOS", "Error al ENVIAR DATOS AL SERVIDOR"); 
		} 		
	}
 

 public int actualizarIngresos() throws Exception {

		String SOAP_ACTION= "http://tempuri.org/actualizarIngresos_json";
		String METHOD_NAME="actualizarIngresos_json";
		
		ArrayList<DBIngresos>  lista_ingresos= new ArrayList<DBIngresos>();
	    lista_ingresos = dbclass.getTodosIngresos_json_flagp();
	    
	    if(lista_ingresos.size() == 0){
	    	return  0;
	    }
	    
	    Gson gson = new Gson();
	    String cadena = gson.toJson(lista_ingresos);
	    
	    Log.i("ENVIO INGRESOS PENDIENTES","JSON: "+cadena.toString());
	    
	    
	    	SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
	    	
	    	if(GlobalVar.id_servicio == GlobalVar.INTERNET){
	    		
	    		request.addProperty("cadena",cadena);
	        	request.addProperty("url", url); 
	    		request.addProperty("catalog", catalog); 
	    		request.addProperty("user", user); 
	    		request.addProperty("password", contrasena);
	    		
	    	}
	    	else if(GlobalVar.id_servicio == GlobalVar.LOCAL){
	    		
	    		request.addProperty("cadena",cadena);
	        	request.addProperty("url", url_local); 
	    		request.addProperty("catalog", catalog_local); 
	    		request.addProperty("user", user_local); 
	    		request.addProperty("password", contrasena_local);
	    		
	    	}
			
		    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
		    Soapenvelope.dotNet=true;
		    Soapenvelope.setOutputSoapObject(request);
		    
		    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);

		    long beforecall = System.currentTimeMillis();
		    
		    try{
		    	transporte.call(SOAP_ACTION, Soapenvelope);
		    	Log.i("ENVIO INGRESOS PENDIENTES","RESPUESTA EN: "+(System.currentTimeMillis()-beforecall)+"miliseg");
		    	
		    	SoapPrimitive resultado_xml =(SoapPrimitive)Soapenvelope.getResponse();
				String res = resultado_xml.toString();
				
				Log.i("ENVIO INGRESOS PENDIENTES","Respuesta: "+res);
				
				JSONArray jsonstring = new JSONArray(res);
				dbclass.guardar_respuesta_ingresos_flag(jsonstring);
				
		    }
		    catch(JSONException ex){
		    	Log.w("SYNC MANAGER", "actualizarObjPedido",ex);
		    	throw new JsonParseException(ex.getMessage());
		    	//throw new JSONException(ex.getMessage());
		    }
		    catch(Exception ex){
		    	Log.w("SYNC MANAGER", "actualizarObjPedido",ex);
		    	throw new Exception(ex);
		    }
		
	  return 1;
 }
 

 public String actualizarIngresos2(DBIngresos dbingreso) throws Exception {
	   	
	   	String SOAP_ACTION= "http://tempuri.org/actualizarIngresos_json";
		String METHOD_NAME="actualizarIngresos_json";
		
		String flag = "";
	 
		ArrayList<DBIngresos> lista = new ArrayList<DBIngresos>();
		lista.add(dbingreso);
		
		Gson gson = new Gson();
	    String cadena = gson.toJson(lista);
	    
	    Log.i("ENVIO INGRESOS", "JSON: "+cadena.toString());
	    
	    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

    	if(GlobalVar.id_servicio == GlobalVar.INTERNET){
    		
    		request.addProperty("cadena",cadena);
        	request.addProperty("url", url); 
    		request.addProperty("catalog", catalog); 
    		request.addProperty("user", user); 
    		request.addProperty("password", contrasena);
    		
    	}
    	else if(GlobalVar.id_servicio == GlobalVar.LOCAL){
    		
    		request.addProperty("cadena",cadena);
        	request.addProperty("url", url_local); 
    		request.addProperty("catalog", catalog_local); 
    		request.addProperty("user", user_local); 
    		request.addProperty("password", contrasena_local);
    		
    	}
		
	    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    Soapenvelope.dotNet=true;
	    Soapenvelope.setOutputSoapObject(request);
	    
	    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);

	    long beforecall = System.currentTimeMillis();
		
	    try{
	    	transporte.call(SOAP_ACTION, Soapenvelope);
	    	Log.i("ENVIO INGRESOS","RESPUESTA EN: "+(System.currentTimeMillis()-beforecall)+"miliseg");
	    	
	    	SoapPrimitive resultado_xml =(SoapPrimitive)Soapenvelope.getResponse();
			String res = resultado_xml.toString();
	    	
			Log.i("ENVIO INGRESOS","Respuesta: "+res);
			
			JSONArray jsonstring = new JSONArray(res);
			flag = dbclass.guardar_respuesta_ingresos_flag(jsonstring);
			
	    }
	    catch(JSONException ex){
	    	ex.printStackTrace();
	    	throw new JsonParseException(ex.getMessage());
	    }
	    catch(Exception ex){
	    	ex.printStackTrace();
			throw new Exception(ex);
	    }
	    
	 return flag;
}
 
 
public int actualizarDepositos() throws Exception{
     
	   String SOAP_ACTION= "http://tempuri.org/actualizarDepositos_json";
	   String METHOD_NAME="actualizarDepositos_json";
	   ArrayList<DBDepositos>  lista_depositos= new ArrayList<DBDepositos>();
	   
	   lista_depositos = dbclass.getDepositos_envio_pendientes();
	   
	    if(lista_depositos.size() == 0){
	    	return  0;
	    }
	   
	    Gson gson = new Gson();
	    String cadena = gson.toJson(lista_depositos);
	   
	    Log.i("ENVIO DEPOSITOS PENDIENTES", "JSON: "+cadena.toString());
	   
  	    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
  	    
    	if(GlobalVar.id_servicio == GlobalVar.INTERNET){
    		
    		request.addProperty("cadena",cadena);
        	request.addProperty("url", url); 
    		request.addProperty("catalog", catalog); 
    		request.addProperty("user", user); 
    		request.addProperty("password", contrasena);
    		
    	}
    	else if(GlobalVar.id_servicio == GlobalVar.LOCAL){
    		
    		request.addProperty("cadena",cadena);
        	request.addProperty("url", url_local); 
    		request.addProperty("catalog", catalog_local); 
    		request.addProperty("user", user_local); 
    		request.addProperty("password", contrasena_local);
    		
    	}
		
	    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    Soapenvelope.dotNet=true;
	    Soapenvelope.setOutputSoapObject(request);
	    
	    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);

	    long beforecall = System.currentTimeMillis();
	    
	    try{
	    	transporte.call(SOAP_ACTION, Soapenvelope);
	    	Log.i("ENVIO DEPOSITOS PENDIENTES","RESPUESTA EN: "+(System.currentTimeMillis()-beforecall)+"miliseg");
	    	
	    	SoapPrimitive resultado_xml =(SoapPrimitive)Soapenvelope.getResponse();
			String res = resultado_xml.toString();
	    	
			Log.i("ENVIO DEPOSITOS PENDIENTES","Respuesta: "+res);
			
			JSONArray jsonstring = new JSONArray(res);
			dbclass.guardar_respuesta_depositos_flag(jsonstring);
			
	    }
	    catch(JSONException ex){
	    	ex.printStackTrace();
	    	throw new JsonParseException(ex.getMessage());
	    }
	    catch(Exception ex){
	    	ex.printStackTrace();
			throw new Exception(ex);
	    }
 
	    return 1;
}
 
 
public String actualizarDepositos2(String secuencia) throws Exception{
        
		   String SOAP_ACTION= "http://tempuri.org/actualizarDepositos_json";
		   String METHOD_NAME="actualizarDepositos_json";
		   ArrayList<DBDepositos>  lista_depositos= new ArrayList<DBDepositos>();
		   
		   String flag = "";
		   
		   lista_depositos = dbclass.getDepositosxSec(secuencia);
		   
		   Gson gson = new Gson();
		   String cadena = gson.toJson(lista_depositos);
		   
		    Log.i("ENVIO DEPOSITO", "JSON: "+cadena.toString());
		   
	    	SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
	    	
	    	if(GlobalVar.id_servicio == GlobalVar.INTERNET){
	    		
	    		request.addProperty("cadena",cadena);
	        	request.addProperty("url", url); 
	    		request.addProperty("catalog", catalog); 
	    		request.addProperty("user", user); 
	    		request.addProperty("password", contrasena);
	    		
	    	}
	    	else if(GlobalVar.id_servicio == GlobalVar.LOCAL){
	    		
	    		request.addProperty("cadena",cadena);
	        	request.addProperty("url", url_local); 
	    		request.addProperty("catalog", catalog_local); 
	    		request.addProperty("user", user_local); 
	    		request.addProperty("password", contrasena_local);
	    		
	    	}
			
		    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
		    Soapenvelope.dotNet=true;
		    Soapenvelope.setOutputSoapObject(request);
		    
		    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);

		    long beforecall = System.currentTimeMillis();
		    
		    try{
		    	transporte.call(SOAP_ACTION, Soapenvelope);
		    	Log.i("ENVIO DEPOSITO","RESPUESTA EN: "+(System.currentTimeMillis()-beforecall)+"miliseg");
		    	
		    	SoapPrimitive resultado_xml =(SoapPrimitive)Soapenvelope.getResponse();
				String res = resultado_xml.toString();
		    	
				Log.i("ENVIO DEPOSITO","Respuesta: "+res);
				
				JSONArray jsonstring = new JSONArray(res);
				flag = dbclass.guardar_respuesta_depositos_flag(jsonstring);
				
		    }
		    catch(JSONException ex){
		    	ex.printStackTrace();
		    	throw new JsonParseException(ex.getMessage());
		    }
		    catch(Exception ex){
		    	ex.printStackTrace();
				throw new Exception(ex);
		    }
	   
	 return flag;
 }
 
 public int actualizarPedido_cabecera(boolean internet,boolean local) {

	   	DBclasses obj_dbclasses= new DBclasses(context);
	   	int valor=0;

	   	
	   	String SOAP_ACTION= "http://tempuri.org/actualizarPedidoCabecera";
		String METHOD_NAME="actualizarPedidoCabecera";
		ArrayList<DBPedido_Cabecera>  lista_dbPedido_cabecera= new ArrayList<DBPedido_Cabecera>();
	    lista_dbPedido_cabecera= obj_dbclasses.getTodosPedidosCabecera();
	   	
	 
	 if(local && GlobalVar.servicio_secundario_activo){
		 
			try 
			{
				
					Iterator<DBPedido_Cabecera> it=lista_dbPedido_cabecera.iterator();
				
					  while ( it.hasNext() ) { 
						  valor++;
						  
						  Object objeto = it.next(); 
						  DBPedido_Cabecera ped_cabecera = (DBPedido_Cabecera)objeto; 
						  
					  if(ped_cabecera.getFlag().equals("N") || ped_cabecera.getFlag().equals("P")
							  || ped_cabecera.getFlag().equals("X") || ped_cabecera.getFlag().equals("Y") ){
							  
							  PropertyInfo pi = new PropertyInfo();
								pi.setName("db_pedido_cabecera");
								pi.setValue(ped_cabecera);
								pi.setType(ped_cabecera.getClass());
							  
								SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
								request.addProperty(pi);
								//
								request.addProperty("url", url_local); 
								request.addProperty("catalog", catalog_local); 
								request.addProperty("user", user_local); 
								request.addProperty("password", contrasena_local);
								//
								
								SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
								envelope.dotNet = true; 
								envelope.setOutputSoapObject(request);
								envelope.addMapping(NAMESPACE, "DBPedido_Cabecera", ped_cabecera.getClass());

								HttpTransportSE transporte = new HttpTransportSE(URL+GlobalVar.direccion_servicio_local);
								transporte.call(SOAP_ACTION, envelope);

								SoapPrimitive resultado_xml =(SoapPrimitive)envelope.getResponse();
								String res = resultado_xml.toString();
								Log.w("HORA SERVIDOR", res+"-" + res.length());
								
								if(res.length()<=22)
								{
									
									 Log.i("PEDIDO_CABECERA (SECUNDARIO)", "datos subidos al servidor");
									 //cambiar estado a "E" (enviado)
									 obj_dbclasses.cambiar_flag_pedido_cabecera(ped_cabecera.getOc_numero(), "E");
									 obj_dbclasses.updateHoraServidorPedidoCabecera(ped_cabecera.getOc_numero(), res);
								}
								else
								{
									 obj_dbclasses.cambiar_flag_pedido_cabecera(ped_cabecera.getOc_numero(), "P");
									Log.i("PEDIDO_CABECERA (SECUNDARIO)", "Error al actualizar datos"); 
								}
							  
						  }else{
							  Log.i("PEDIDO_CABECERA (SECUNDARIO)", "item subido anteriormente");
						  }
							
						
					}				  				 	  
			
			} 
			catch (Exception e) 
			{
				Log.i("PEDIDO_CABECERA (SECUNDARIO)", "Error al ENVIAR DATOS AL SERVIDOR");
				
			} 
		 
	 }
	   	
	 if(internet && GlobalVar.servicio_principal_activo){
		 
			try 
			{				
					
					Iterator<DBPedido_Cabecera> it=lista_dbPedido_cabecera.iterator();
				
					  while ( it.hasNext() ) { 
						  valor++;
						  
						  Object objeto = it.next(); 
						  DBPedido_Cabecera ped_cabecera = (DBPedido_Cabecera)objeto; 
						  
					  if(ped_cabecera.getFlag().equals("N") || ped_cabecera.getFlag().equals("P")
							  || ped_cabecera.getFlag().equals("X") || ped_cabecera.getFlag().equals("Y") ){
							  
							  PropertyInfo pi = new PropertyInfo();
								pi.setName("db_pedido_cabecera");
								pi.setValue(ped_cabecera);
								pi.setType(ped_cabecera.getClass());
							  
								SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
								request.addProperty(pi);
								//
								request.addProperty("url", url); 
								request.addProperty("catalog", catalog); 
								request.addProperty("user", user); 
								request.addProperty("password", contrasena);
								//
								
								SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
								envelope.dotNet = true; 
								envelope.setOutputSoapObject(request);
								envelope.addMapping(NAMESPACE, "DBPedido_Cabecera", ped_cabecera.getClass());

								HttpTransportSE transporte = new HttpTransportSE(URL+GlobalVar.urlService);
								transporte.call(SOAP_ACTION, envelope);

								SoapPrimitive resultado_xml =(SoapPrimitive)envelope.getResponse();
								String res = resultado_xml.toString();
								Log.w("HORA SERVIDOR", res+"-" + res.length());
								
								if(res.length()<=22)
								{
									
									 Log.i("PEDIDO_CABECERA (PRINCIPAL)", "datos subidos al servidor");
									 //cambiar estado a "E" (enviado)
									 obj_dbclasses.cambiar_flag_pedido_cabecera(ped_cabecera.getOc_numero(), "E");
									 obj_dbclasses.updateHoraServidorPedidoCabecera(ped_cabecera.getOc_numero(), res);
								}
								else
								{
									 obj_dbclasses.cambiar_flag_pedido_cabecera(ped_cabecera.getOc_numero(), "P");
									Log.i("PEDIDO_CABECERA (PRINCIPAL)", "Error al actualizar datos"); 
								}
							  
						  }else{
							  Log.i("PEDIDO_CABECERA (PRINCIPAL)", "item subido anteriormente");
						  }
							
						
					}				  				 	  
			
			} 
			catch (Exception e) 
			{
				Log.i("PEDIDO_CABECERA_LOCAL (PRINCIPAL)", "Error al ENVIAR DATOS AL SERVIDOR");
				
			} 
		 
	 }

		
		return valor;
		
	}


public int  actualizarPedido_detalle(boolean internet, boolean local) {

	   	DBclasses obj_dbclasses= new DBclasses(context);
	    int valor=0;
	    
	    String SOAP_ACTION= "http://tempuri.org/actualizarPedidoDetalle";
		String METHOD_NAME="actualizarPedidoDetalle";
		ArrayList<DBPedido_Detalle>  lista_dbPedido_detalle= new ArrayList<DBPedido_Detalle>();
	    lista_dbPedido_detalle= obj_dbclasses.getPedidosDetalle();
	    
	    if(local && GlobalVar.servicio_secundario_activo){
	    	
			try 
			{
				
					Iterator<DBPedido_Detalle> it=lista_dbPedido_detalle.iterator();
								  
					  while ( it.hasNext() ) { 
						  
						  valor++;
						  Object objeto = it.next(); 
						  DBPedido_Detalle ped_detalle = (DBPedido_Detalle)objeto; 
						  
						  if(ped_detalle.getFlag().equals("N") || ped_detalle.getFlag().equals("P") 
								  || ped_detalle.getFlag().equals("X") || ped_detalle.getFlag().equals("Y")){
							  
							  PropertyInfo pi = new PropertyInfo();
								pi.setName("db_pedido_detalle");
								pi.setValue(ped_detalle);
								pi.setType(ped_detalle.getClass());
							  
								SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
								request.addProperty(pi);
								//
								request.addProperty("url", url_local); 
								request.addProperty("catalog", catalog_local); 
								request.addProperty("user", user_local); 
								request.addProperty("password", contrasena_local);
								//
								
								SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
								envelope.dotNet = true; 
								envelope.setOutputSoapObject(request);
								envelope.addMapping(NAMESPACE, "DBPedido_Detalle", ped_detalle.getClass());

								HttpTransportSE transporte = new HttpTransportSE(URL+GlobalVar.direccion_servicio_local);
								transporte.call(SOAP_ACTION, envelope);

								SoapPrimitive resultado_xml =(SoapPrimitive)envelope.getResponse();
								String res = resultado_xml.toString();
								
								if(res.equals("1"))
								{
									 obj_dbclasses.cambiar_estado_pedido_detalle(ped_detalle.getOc_numero(),
											 ped_detalle.getCip(), "E");
									 Log.i("PEDIDO_DETALLE (SECUNDARIO)", "datos subidos al servidor");
								}
								else
								{
									Log.i("PEDIDO_DETALLE (SECUNDARIO)", "Error al actualizar datos"); 
								}	
							  
						  }else{
							  Log.i("PEDIDO_DETALLE (SECUNDARIO)", "item subido anteriormente");
						  }
									
					}		
					  
			} 
			catch (Exception e) 
			{
				Log.i("PEDIDO_DETALLE (SECUNDARIO)", "Error al ENVIAR DATOS AL SERVIDOR"); 
			} 
	    	
	    }
	    
	    
	    if(internet && GlobalVar.servicio_principal_activo){
	    	
			try 
			{
				
					Iterator<DBPedido_Detalle> it=lista_dbPedido_detalle.iterator();
								  
					  while ( it.hasNext() ) { 
						  
						  valor++;
						  Object objeto = it.next(); 
						  DBPedido_Detalle ped_detalle = (DBPedido_Detalle)objeto; 
						  
						  if(ped_detalle.getFlag().equals("N") || ped_detalle.getFlag().equals("P") 
								  || ped_detalle.getFlag().equals("X") || ped_detalle.getFlag().equals("Y")){
							  
							  PropertyInfo pi = new PropertyInfo();
								pi.setName("db_pedido_detalle");
								pi.setValue(ped_detalle);
								pi.setType(ped_detalle.getClass());
							  
								SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
								request.addProperty(pi);
								//
								request.addProperty("url", url); 
								request.addProperty("catalog", catalog); 
								request.addProperty("user", user); 
								request.addProperty("password", contrasena);
								//
								
								SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
								envelope.dotNet = true; 
								envelope.setOutputSoapObject(request);
								envelope.addMapping(NAMESPACE, "DBPedido_Detalle", ped_detalle.getClass());

								HttpTransportSE transporte = new HttpTransportSE(URL+GlobalVar.urlService);
								transporte.call(SOAP_ACTION, envelope);

								SoapPrimitive resultado_xml =(SoapPrimitive)envelope.getResponse();
								String res = resultado_xml.toString();
								
								if(res.equals("1"))
								{
									 obj_dbclasses.cambiar_estado_pedido_detalle(ped_detalle.getOc_numero(),
											 ped_detalle.getCip(), "E");
									 Log.i("PEDIDO_DETALLE (PRINCIPAL)", "datos subidos al servidor");
								}
								else
								{
									Log.i("PEDIDO_DETALLE (PRINCIPAL)", "Error al actualizar datos"); 
								}	
							  
						  }else{
							  Log.i("PEDIDO_DETALLE (PRINCIPAL)", "item subido anteriormente");
						  }
									
					}		
					  
			} 
			catch (Exception e) 
			{
				Log.i("PEDIDO_DETALLE (PRINCIPAL)", "Error al ENVIAR DATOS AL SERVIDOR"); 
			} 
	    	
	    }
		
		return valor;
	}


public int actualizarPedido_cabecera2(DBPedido_Cabecera ped_cabecera) throws Exception {

	    DBclasses obj_dbclasses= new DBclasses(context);
		String SOAP_ACTION= "http://tempuri.org/actualizarPedidoCabecera";
		String METHOD_NAME="actualizarPedidoCabecera";
				
	           try{
					  
				    PropertyInfo pi = new PropertyInfo();
					pi.setName("db_pedido_cabecera");
					pi.setValue(ped_cabecera);
					pi.setType(ped_cabecera.getClass());
				  
					SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
					request.addProperty(pi);
					//

			    	if(GlobalVar.id_servicio == GlobalVar.INTERNET){
			    		
			        	request.addProperty("url", url); 
			    		request.addProperty("catalog", catalog); 
			    		request.addProperty("user", user); 
			    		request.addProperty("password", contrasena);
			    		
			    	}
			    	else if(GlobalVar.id_servicio == GlobalVar.LOCAL){
			    		
			        	request.addProperty("url", url_local); 
			    		request.addProperty("catalog", catalog_local); 
			    		request.addProperty("user", user_local); 
			    		request.addProperty("password", contrasena_local);
			    		
			    	}
					
					//
					
					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
					envelope.dotNet = true; 
					envelope.setOutputSoapObject(request);
					envelope.addMapping(NAMESPACE, "DBPedido_Cabecera", ped_cabecera.getClass());

					HttpTransportSE transporte = new HttpTransportSE(URL+GlobalVar.urlService);
					transporte.call(SOAP_ACTION, envelope);

					SoapPrimitive resultado_xml =(SoapPrimitive)envelope.getResponse();
					String res = resultado_xml.toString();

			        if(res.length()<=22)
					{
						 Log.i("PEDIDO_CABECERA", "datos subidos al servidor");
						 //cambiar estado a "E" (enviado)
						 obj_dbclasses.cambiar_flag_pedido_cabecera(ped_cabecera.getOc_numero(), "E");
						 obj_dbclasses.updateHoraServidorPedidoCabecera(ped_cabecera.getOc_numero(), res);
					}
					else
					{
						Log.i("PEDIDO_CABECERA", "item subido anteriormente"); 
					}	
			        
		     }catch (Exception e) 
						{
							Log.i("PEDIDO_CABECERA", "Error al ENVIAR DATOS AL SERVIDOR");
							throw new Exception(e);
						}       

	return 1;
	
}


 public int actualizarPedido_detalle2(DBPedido_Detalle ped_detalle) throws Exception {

    DBclasses obj_dbclasses= new DBclasses(context);
    String SOAP_ACTION= "http://tempuri.org/actualizarPedidoDetalle";
	String METHOD_NAME="actualizarPedidoDetalle";

		
        try{
			  
				  PropertyInfo pi = new PropertyInfo();
					pi.setName("db_pedido_detalle");
					pi.setValue(ped_detalle);
					pi.setType(ped_detalle.getClass());
				  
					SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
					request.addProperty(pi);
					//

			    	if(GlobalVar.id_servicio == GlobalVar.INTERNET){
			    		
			        	request.addProperty("url", url); 
			    		request.addProperty("catalog", catalog); 
			    		request.addProperty("user", user); 
			    		request.addProperty("password", contrasena);
			    		
			    	}
			    	else if(GlobalVar.id_servicio == GlobalVar.LOCAL){
			    		
			        	request.addProperty("url", url_local); 
			    		request.addProperty("catalog", catalog_local); 
			    		request.addProperty("user", user_local); 
			    		request.addProperty("password", contrasena_local);
			    		
			    	}
					
					//
					
					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
					envelope.dotNet = true; 
					envelope.setOutputSoapObject(request);
					envelope.addMapping(NAMESPACE, "DBPedido_Detalle", ped_detalle.getClass());

					HttpTransportSE transporte = new HttpTransportSE(URL+GlobalVar.urlService);
					transporte.call(SOAP_ACTION, envelope);

					SoapPrimitive resultado_xml =(SoapPrimitive)envelope.getResponse();
					String res = resultado_xml.toString();
					
					if(res.equals("1"))
					{
						 obj_dbclasses.cambiar_estado_pedido_detalle(ped_detalle.getOc_numero(),
								 ped_detalle.getCip(), "E");
						 Log.i("PEDIDO_DETALLE ", "datos subidos al servidor");
					}
					else
					{
						Log.i("PEDIDO_DETALLE", "Error al actualizar datos"); 
					}				
	}catch (Exception e) 
     {
 	  Log.i("PEDIDO_DETALLE", "Error al ENVIAR DATOS AL SERVIDOR");
 	  throw new Exception(e);
     } 

          return 1;
  }
 
 
 /*public void  actualizarPedido_devolucion() {

	   	DBclasses obj_dbclasses= new DBclasses(context);
	   	
		try 
		{
			String SOAP_ACTION= "http://tempuri.org/actualizarPedidoDevolucion";
			String METHOD_NAME="actualizarPedidoDevolucion";
			ArrayList<DBPedido_Devolucion>  lista_dbPedido_devolucion= new ArrayList<DBPedido_Devolucion>();
		    lista_dbPedido_devolucion= obj_dbclasses.getPedidosDevolucion();
				
			
				
				Iterator<DBPedido_Devolucion> it=lista_dbPedido_devolucion.iterator();
							  
				  while ( it.hasNext() ) { 
					  		
					  Object objeto = it.next(); 
					  DBPedido_Devolucion ped_devolucion = (DBPedido_Devolucion)objeto; 
					  
					  if(ped_devolucion.getFlag().equals("N")){
						  
						  PropertyInfo pi = new PropertyInfo();
							pi.setName("db_pedido_devolucion");
							pi.setValue(ped_devolucion);
							pi.setType(ped_devolucion.getClass());
						  
							SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
							request.addProperty(pi);
							
							SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
							envelope.dotNet = true; 
							envelope.setOutputSoapObject(request);
							envelope.addMapping(NAMESPACE, "DBPedido_Devolucion", ped_devolucion.getClass());

							HttpTransportSE transporte = new HttpTransportSE(URL+GlobalVar.urlService);
							transporte.call(SOAP_ACTION, envelope);

							SoapPrimitive resultado_xml =(SoapPrimitive)envelope.getResponse();
							String res = resultado_xml.toString();
							
							if(res.equals("1"))
							{
								 obj_dbclasses.cambiar_estado_pedido_devolucion(ped_devolucion.getOc_numero(),
										 ped_devolucion.getCip(), "E");
								 Log.i("PEDIDO_DEVOLUCION", "datos subidos al servidor");
							}
							else
							{
								Log.i("PEDIDO_DEVOLUCION", "Error al actualizar datos"); 
							}	
						  
					  }else{
						  Log.i("PEDIDO_DEVOLUCION", "item subido anteriormente");
					  }
								
				}		
				  
		} 
		catch (Exception e) 
		{
			Log.i("PEDIDO_DEVOLUCION", "Error al ENVIAR DATOS AL SERVIDOR"); 
		} 
		
	}*/
 
 
 public void Sync_tabla_locales(String url, String catalog, String user, String contrasena ) throws Exception{
		
		String SOAP_ACTION= "http://tempuri.org/obtenerLocales_json";
		String METHOD_NAME="obtenerLocales_json";
	
		
		SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);
		Request.addProperty("url", url); 
		Request.addProperty("catalog", catalog); 
		Request.addProperty("user", user); 
		Request.addProperty("password", contrasena); 
	    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    Soapenvelope.dotNet=true;
	    Soapenvelope.setOutputSoapObject(Request);
	    
	    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);
	    
	    long beforecall = System.currentTimeMillis();
	    
	    try{
	    	transporte.call(SOAP_ACTION, Soapenvelope);
	    	Log.i("LOCALES","RESPUESTA EN: "+(System.currentTimeMillis()-beforecall)+"miliseg");

	    	SoapPrimitive result = (SoapPrimitive)Soapenvelope.getResponse();  
	    	
	    	JSONArray jsonstring = new JSONArray(result.toString());   
	    	Log.i("LOCALES","Registros: "+jsonstring.length());
	    	dbclass.syncLocales(jsonstring);
	    	Log.i("LOCALES", "SINCRONIZADO");
	    	
	    }catch(Exception e){
	    	e.printStackTrace();
	    	Log.i("LOCALES", "NO SINCRONIZADO");
	    	throw new Exception(e);
	    } 

	}

 
 public void Sync_tabla_vehiculos(){
		
		String SOAP_ACTION= "http://tempuri.org/obtenerVehiculos";
		String METHOD_NAME="obtenerVehiculos";
	
		
		SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);         
	    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    Soapenvelope.dotNet=true;
	    Soapenvelope.setOutputSoapObject(Request);
	    
	    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);
	    
	    try{
	    	transporte.call(SOAP_ACTION, Soapenvelope);
	    	
	    	SoapObject result =(SoapObject)Soapenvelope.getResponse();
	    	  
	    	  dbclass.getReadableDatabase().delete(DBtables.Vehiculo.TAG, null, null);
	    	  SQLiteDatabase db = dbclass.getWritableDatabase();
	    	 
	    	 
	    	  
	    	
	    	    
	    	 for (int i = 0; i <result.getPropertyCount(); i++)
	    	 {
	    	        SoapObject ic = (SoapObject)result.getProperty(i);
	    	  
	    	        ContentValues cv = new ContentValues();
	    	        
	    	        cv.put(DBtables.Vehiculo.PK_CODIGO_VEHICULO, ic.getProperty(0).toString().trim());
	    	        cv.put(DBtables.Vehiculo.DESCRIPCION, ic.getProperty(1).toString().trim());
	    	        cv.put(DBtables.Vehiculo.PLACA_VEHICULO, ic.getProperty(2).toString().trim());
	    	      
	    			db.insert(DBtables.Vehiculo.TAG, null, cv);
	       
	    	 }
	    	 
	    	 db.close();
	    	 Log.i("VEHICULOS", "sincronizado ");
	    	
	    }catch(Exception e){
	    	e.printStackTrace();
	    }   		
	}

 
 
 
 public void Sync_tabla_choferes(){
		
		String SOAP_ACTION= "http://tempuri.org/obtenerChoferes";
		String METHOD_NAME="obtenerChoferes";
	
		
		SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);         
	    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    Soapenvelope.dotNet=true;
	    Soapenvelope.setOutputSoapObject(Request);
	    
	    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);
	    
	    try{
	    	transporte.call(SOAP_ACTION, Soapenvelope);
	    	
	    	SoapObject result =(SoapObject)Soapenvelope.getResponse();
	    	  
	    	  dbclass.getReadableDatabase().delete(DBtables.Locales.TAG, null, null);
	    	  SQLiteDatabase db = dbclass.getWritableDatabase();
	    	 
	    
	    	 for (int i = 0; i <result.getPropertyCount(); i++)
	    	 {
	    	        SoapObject ic = (SoapObject)result.getProperty(i);
	    	  
	    	        ContentValues cv = new ContentValues();
	    	        
	    	        cv.put(DBtables.Chofer.PK_CODIGO_CHOFER, ic.getProperty(0).toString().trim());  
	    	        cv.put(DBtables.Chofer.NOMBRE_CHOFER, ic.getProperty(1).toString().trim()); 
	    	        cv.put(DBtables.Chofer.NRO_BREVETE, ic.getProperty(2).toString().trim()); 
	    	        cv.put(DBtables.Chofer.CATEGORIA, ic.getProperty(3).toString().trim()); 
	    	        cv.put(DBtables.Chofer.USECOD, ic.getProperty(4).toString().trim()); 
	    	        
	    			db.insert(DBtables.Chofer.TAG, null, cv);
	       
	    	 }
	    	 
	    	 db.close();
	    	 Log.i("CHOFER", "sincronizado ");
	    	
	    }catch(Exception e){
	    	e.printStackTrace();
	    }   		
	}
 
 
 public void Sync_tabla_factura1(){
		
		String SOAP_ACTION= "http://tempuri.org/obtenerFactura1";
		String METHOD_NAME="obtenerFactura1";
	
		
		SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);         
	    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    Soapenvelope.dotNet=true;
	    Soapenvelope.setOutputSoapObject(Request);
	    
	    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);
	    
	    try{
	    	transporte.call(SOAP_ACTION, Soapenvelope);
	    	
	    	SoapObject result =(SoapObject)Soapenvelope.getResponse();
	    	  
	    	  dbclass.getReadableDatabase().delete(DBtables.Factura1.TAG, null, null);
	    	  SQLiteDatabase db = dbclass.getWritableDatabase();
	    	 
	    
	    	 for (int i = 0; i <result.getPropertyCount(); i++)
	    	 {
	    	        SoapObject ic = (SoapObject)result.getProperty(i);
	    	  
	    	        ContentValues cv = new ContentValues();
	    	        
	    	        cv.put(DBtables.Factura1.PK_SECUENCIA, ic.getProperty(0).toString().trim());  
	    	        cv.put(DBtables.Factura1.CODCLI, ic.getProperty(1).toString().trim()); 
	    	        cv.put(DBtables.Factura1.CODFORPAG, ic.getProperty(2).toString().trim()); 
	    	        cv.put(DBtables.Factura1.TIEMPO_CREDITO, ic.getProperty(3).toString().trim()); 
	    	        cv.put(DBtables.Factura1.TOTAL, ic.getProperty(4).toString().trim()); 
	    	        cv.put(DBtables.Factura1.ACUENTA, ic.getProperty(5).toString().trim()); 
	    	        cv.put(DBtables.Factura1.IGV, ic.getProperty(6).toString().trim()); 
	    	        cv.put(DBtables.Factura1.SALDO, ic.getProperty(7).toString().trim()); 
	    	        cv.put(DBtables.Factura1.FECFAC, ic.getProperty(8).toString().trim()); 
	    	        cv.put(DBtables.Factura1.ESTADO, ic.getProperty(9).toString().trim()); 
	    	        cv.put(DBtables.Factura1.NUMERO_GUIA, ic.getProperty(10).toString().trim()); 
	    	        cv.put(DBtables.Factura1.NUMERO_FACTURA, ic.getProperty(11).toString().trim()); 
	    	        cv.put(DBtables.Factura1.SUB_TOTAL, ic.getProperty(12).toString().trim()); 
	    	        cv.put(DBtables.Factura1.SERIE_GUIA, ic.getProperty(13).toString().trim()); 
	    	        cv.put(DBtables.Factura1.SERIE_FACTURA, ic.getProperty(14).toString().trim()); 
	    	        cv.put(DBtables.Factura1.CODVEN, ic.getProperty(15).toString().trim()); 
	    	        cv.put(DBtables.Factura1.TIPO_DOCUMENTO, ic.getProperty(16).toString().trim()); 
	    	        cv.put(DBtables.Factura1.DEPOSITO, ic.getProperty(17).toString().trim());
	    	        cv.put(DBtables.Factura1.ID_LOCAL, ic.getProperty(18).toString().trim()); 
	    	        cv.put(DBtables.Factura1.USECOD, ic.getProperty(19).toString().trim()); 
	    	        
	    			db.insert(DBtables.Factura1.TAG, null, cv);
	       
	    	 }
	    	 
	    	 db.close();
	    	 Log.i("factura 1", "sincronizado ");
	    	
	    }catch(Exception e){
	    	e.printStackTrace();
	    }   		
	}

 public void Sync_tabla_factura2(){
		
		String SOAP_ACTION= "http://tempuri.org/obtenerFactura2";
		String METHOD_NAME="obtenerFactura2";
	
		
		SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);         
	    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    Soapenvelope.dotNet=true;
	    Soapenvelope.setOutputSoapObject(Request);
	    
	    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);
	    
	    try{
	    	transporte.call(SOAP_ACTION, Soapenvelope);
	    	
	    	SoapObject result =(SoapObject)Soapenvelope.getResponse();
	    	  
	    	  dbclass.getReadableDatabase().delete(DBtables.Factura2.TAG, null, null);
	    	  SQLiteDatabase db = dbclass.getWritableDatabase();
	    	 
	    
	    	 for (int i = 0; i <result.getPropertyCount(); i++)
	    	 {
	    	        SoapObject ic = (SoapObject)result.getProperty(i);
	    	  
	    	        ContentValues cv = new ContentValues();
	    	        
	    	        cv.put(DBtables.Factura2.PK_SECUENCIA, ic.getProperty(0).toString().trim());  
	    	        cv.put(DBtables.Factura2.CODPRO, ic.getProperty(1).toString().trim()); 
	    	        cv.put(DBtables.Factura2.ITEM, ic.getProperty(2).toString().trim()); 
	    	        cv.put(DBtables.Factura2.CANTIDAD, ic.getProperty(3).toString().trim()); 
	    	        cv.put(DBtables.Factura2.TOTAL, ic.getProperty(4).toString().trim()); 
	    	        cv.put(DBtables.Factura2.PREVEN, ic.getProperty(5).toString().trim()); 
	    	        cv.put(DBtables.Factura2.TOTAL_REAL, ic.getProperty(6).toString().trim()); 
	    	       
	    			db.insert(DBtables.Factura2.TAG, null, cv);
	       
	    	 }
	    	 
	    	 db.close();
	    	 Log.i("factura 2", "sincronizado ");
	    	
	    }catch(Exception e){
	    	e.printStackTrace();
	    }   		
	}
 
 public void Sync_tabla_liquidacionPreventa1(){
		
		String SOAP_ACTION= "http://tempuri.org/obtenerLiquidacion1";
		String METHOD_NAME="obtenerLiquidacion1";
		SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);         
	    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    Soapenvelope.dotNet=true;
	    Soapenvelope.setOutputSoapObject(Request);
	   
	    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);
	    try{
	    	transporte.call(SOAP_ACTION, Soapenvelope);
	    	
	    	SoapObject result =(SoapObject)Soapenvelope.getResponse();
	    	  
	    	  dbclass.getReadableDatabase().delete(DBtables.Liquidacion_Preventa1.TAG, null, null);
	    	  SQLiteDatabase db = dbclass.getWritableDatabase();
	    	 
	    
	    	 for (int i = 0; i <result.getPropertyCount(); i++)
	    	 {
	    	        SoapObject ic = (SoapObject)result.getProperty(i);
	    	  
	    	        ContentValues cv = new ContentValues();
	    	        
	    	        cv.put(DBtables.Liquidacion_Preventa1.PK_SECUENCIA, ic.getProperty(0).toString().trim());  
	    	        cv.put(DBtables.Liquidacion_Preventa1.CODIGO_CHOFER, ic.getProperty(1).toString().trim()); 
	    	        cv.put(DBtables.Liquidacion_Preventa1.CODIGO_VEHICULO, ic.getProperty(2).toString().trim()); 
	    	        cv.put(DBtables.Liquidacion_Preventa1.FECHA_REGISTRO, ic.getProperty(3).toString().trim()); 
	    	        cv.put(DBtables.Liquidacion_Preventa1.ESTADO, ic.getProperty(4).toString().trim()); 
	    	        cv.put(DBtables.Liquidacion_Preventa1.OBSERVACION, ic.getProperty(5).toString().trim()); 
	    	        cv.put(DBtables.Liquidacion_Preventa1.TOTAL_LIQUID, ic.getProperty(6).toString().trim()); 
	    	        cv.put(DBtables.Liquidacion_Preventa1.TOTAL_CONTADO, ic.getProperty(7).toString().trim()); 
	    	        cv.put(DBtables.Liquidacion_Preventa1.TOTAL_CREDITO, ic.getProperty(8).toString().trim()); 
	    	        cv.put(DBtables.Liquidacion_Preventa1.TOTAL_COBRANZA, ic.getProperty(9).toString().trim()); 
	    	        cv.put(DBtables.Liquidacion_Preventa1.CODALM, ic.getProperty(10).toString().trim()); 
	    	        cv.put(DBtables.Liquidacion_Preventa1.ID_LOCAL, ic.getProperty(11).toString().trim()); 
	    	        cv.put(DBtables.Liquidacion_Preventa1.TOTAL_DEPOSITO, ic.getProperty(12).toString().trim()); 
	    			
	    	        db.insert(DBtables.Liquidacion_Preventa1.TAG, null, cv);
	    	 }
	    	 
	    	 db.close();
	    	 Log.i("Liquidacion Preventa 1", "sincronizado ");
	    	
	    }catch(Exception e){
	    	e.printStackTrace();
	    }   		
	}
 
 public void Sync_tabla_liquidacionPreventa2(){
		
		String SOAP_ACTION= "http://tempuri.org/obtenerLiquidacion2";
		String METHOD_NAME="obtenerLiquidacion2";
		SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);         
	    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    Soapenvelope.dotNet=true;
	    Soapenvelope.setOutputSoapObject(Request);
	   
	    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);
	    try{
	    	transporte.call(SOAP_ACTION, Soapenvelope);
	    	
	    	SoapObject result =(SoapObject)Soapenvelope.getResponse();
	    	  
	    	  dbclass.getReadableDatabase().delete(DBtables.Liquidacion_Preventa2.TAG, null, null);
	    	  SQLiteDatabase db = dbclass.getWritableDatabase();
	    	 
	    
	    	 for (int i = 0; i <result.getPropertyCount(); i++)
	    	 {
	    	        SoapObject ic = (SoapObject)result.getProperty(i);
	    	  
	    	        ContentValues cv = new ContentValues();
	    	        
	    	        cv.put(DBtables.Liquidacion_Preventa2.PK_SECUENCIA, ic.getProperty(0).toString().trim());  
	    	        cv.put(DBtables.Liquidacion_Preventa2.CODPRO, ic.getProperty(1).toString().trim()); 
	    	        cv.put(DBtables.Liquidacion_Preventa2.CANTIDAD_FACT, ic.getProperty(2).toString().trim()); 
	    	        cv.put(DBtables.Liquidacion_Preventa2.CANTIDAD_DEV, ic.getProperty(3).toString().trim()); 
	    	        cv.put(DBtables.Liquidacion_Preventa2.CANTIDAD_VEN, ic.getProperty(4).toString().trim()); 
	    	     
	    	        db.insert(DBtables.Liquidacion_Preventa2.TAG, null, cv);
	    	 }
	    	 
	    	 db.close();
	    	 Log.i("Liquidacion Preventa 2", "sincronizado ");
	    	
	    }catch(Exception e){
	    	e.printStackTrace();
	    }   		
	}

 public void Sync_tabla_liquidacionPreventa3(){
		
		String SOAP_ACTION= "http://tempuri.org/obtenerLiquidacion3";
		String METHOD_NAME="obtenerLiquidacion3";
		SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);         
	    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    Soapenvelope.dotNet=true;
	    Soapenvelope.setOutputSoapObject(Request);
	   
	    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);
	    try{
	    	transporte.call(SOAP_ACTION, Soapenvelope);
	    	
	    	SoapObject result =(SoapObject)Soapenvelope.getResponse();
	    	  
	    	  dbclass.getReadableDatabase().delete(DBtables.Liquidacion_Preventa3.TAG, null, null);
	    	  SQLiteDatabase db = dbclass.getWritableDatabase();
	    	 
	    
	    	 for (int i = 0; i <result.getPropertyCount(); i++)
	    	 {
	    	        SoapObject ic = (SoapObject)result.getProperty(i);
	    	  
	    	        ContentValues cv = new ContentValues();
	    	        
	    	        cv.put(DBtables.Liquidacion_Preventa3.PK_SECUENCIA, ic.getProperty(0).toString().trim());  
	    	        cv.put(DBtables.Liquidacion_Preventa3.SEC_GUIA_DOCUMENTOS, ic.getProperty(1).toString().trim()); 
	    	        cv.put(DBtables.Liquidacion_Preventa3.TIPO_DOC, ic.getProperty(2).toString().trim()); 
	    	        cv.put(DBtables.Liquidacion_Preventa3.SEC_DOC, ic.getProperty(3).toString().trim()); 
	    	        cv.put(DBtables.Liquidacion_Preventa3.MANUAL, ic.getProperty(4).toString().trim()); 
	    	     
	    	        db.insert(DBtables.Liquidacion_Preventa3.TAG, null, cv);
	    	 }
	    	 
	    	 db.close();
	    	 Log.i("Liquidacion Preventa 3", "sincronizado ");
	    	
	    }catch(Exception e){
	    	e.printStackTrace();
	    }   		
	}
 
 
 public void Sync_tabla_NotaCredito1(){
		
		String SOAP_ACTION= "http://tempuri.org/obtenerNotaCredito1";
		String METHOD_NAME="obtenerNotaCredito1";
		SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);         
	    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    Soapenvelope.dotNet=true;
	    Soapenvelope.setOutputSoapObject(Request);
	   
	    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);
	    try{
	    	transporte.call(SOAP_ACTION, Soapenvelope);
	    	
	    	SoapObject result =(SoapObject)Soapenvelope.getResponse();
	    	  
	    	  dbclass.getReadableDatabase().delete(DBtables.NotaCredito1.TAG, null, null);
	    	  SQLiteDatabase db = dbclass.getWritableDatabase();
	    	 
	    
	    	 for (int i = 0; i <result.getPropertyCount(); i++)
	    	 {
	    	        SoapObject ic = (SoapObject)result.getProperty(i);
	    	  
	    	        ContentValues cv = new ContentValues();
	    	        
	    	        cv.put(DBtables.NotaCredito1.PK_SECUENCIA, ic.getProperty(0).toString().trim());  
	    	        cv.put(DBtables.NotaCredito1.SERIE_FACTURA, ic.getProperty(1).toString().trim()); 
	    	        cv.put(DBtables.NotaCredito1.NUMERO_FACTURA, ic.getProperty(2).toString().trim()); 
	    	        cv.put(DBtables.NotaCredito1.SERIE_NOTA_CREDITO, ic.getProperty(3).toString().trim()); 
	    	        cv.put(DBtables.NotaCredito1.NUMERO_NOTA_CREDITO, ic.getProperty(4).toString().trim()); 
	    	        cv.put(DBtables.NotaCredito1.CODCLI, ic.getProperty(5).toString().trim()); 
	    	        cv.put(DBtables.NotaCredito1.FECHA, ic.getProperty(6).toString().trim()); 
	    	        cv.put(DBtables.NotaCredito1.OBSERVACION, ic.getProperty(7).toString().trim()); 
	    	        cv.put(DBtables.NotaCredito1.MONTO, ic.getProperty(8).toString().trim()); 
	    	        cv.put(DBtables.NotaCredito1.ESTADO, ic.getProperty(9).toString().trim()); 
	    	        cv.put(DBtables.NotaCredito1.CODTIPNOT, ic.getProperty(10).toString().trim()); 
	    	        cv.put(DBtables.NotaCredito1.SUB_TOTAL, ic.getProperty(11).toString().trim()); 
	    	        cv.put(DBtables.NotaCredito1.TIPO_DOC, ic.getProperty(12).toString().trim()); 
	    	        cv.put(DBtables.NotaCredito1.CODVEN, ic.getProperty(13).toString().trim()); 
	    	        cv.put(DBtables.NotaCredito1.ID_LOCAL, ic.getProperty(14).toString().trim()); 
	    	        cv.put(DBtables.NotaCredito1.USECOD, ic.getProperty(15).toString().trim()); 
	    	        cv.put(DBtables.NotaCredito1.PREVENTA, ic.getProperty(16).toString().trim()); 
	    	        
	    	        db.insert(DBtables.NotaCredito1.TAG, null, cv);
	    	 }
	    	 
	    	 db.close();
	    	 Log.i("Nota Credtio 1", "sincronizado ");
	    	
	    }catch(Exception e){
	    	e.printStackTrace();
	    }   		
	}

 
 public void Sync_tabla_NotaCredito2(){
		
		String SOAP_ACTION= "http://tempuri.org/obtenerNotaCredito2";
		String METHOD_NAME="obtenerNotaCredito2";
		SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);         
	    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    Soapenvelope.dotNet=true;
	    Soapenvelope.setOutputSoapObject(Request);
	   
	    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);
	    try{
	    	transporte.call(SOAP_ACTION, Soapenvelope);
	    	
	    	SoapObject result =(SoapObject)Soapenvelope.getResponse();
	    	  
	    	  dbclass.getReadableDatabase().delete(DBtables.NotaCredito2.TAG, null, null);
	    	  SQLiteDatabase db = dbclass.getWritableDatabase();
	    	 
	    
	    	 for (int i = 0; i <result.getPropertyCount(); i++)
	    	 {
	    	        SoapObject ic = (SoapObject)result.getProperty(i);
	    	  
	    	        ContentValues cv = new ContentValues();
	    	        
	    	        cv.put(DBtables.NotaCredito2.PK_SECUENCIA, ic.getProperty(0).toString().trim());  
	    	        cv.put(DBtables.NotaCredito2.ITEM, ic.getProperty(1).toString().trim()); 
	    	        cv.put(DBtables.NotaCredito2.CODPRO, ic.getProperty(2).toString().trim()); 
	    	        cv.put(DBtables.NotaCredito2.CANTIDAD_DEVUELTA, ic.getProperty(3).toString().trim()); 
	    	        cv.put(DBtables.NotaCredito2.PRECIO_UNITARIO, ic.getProperty(4).toString().trim()); 
	    	        cv.put(DBtables.NotaCredito2.TOTAL, ic.getProperty(5).toString().trim()); 
	    	       
	    	        db.insert(DBtables.NotaCredito2.TAG, null, cv);
	    	 }
	    	 
	    	 db.close();
	    	 Log.i("Nota Credito 2", "sincronizado ");
	    	
	    }catch(Exception e){
	    	e.printStackTrace();
	    }   		
	}
 public void close(){
	 dbclass.close();
	 
 }

public void Sync_tabla_pedido_detallexVendedor(String codven, String url, String catalog, String user, String contrasena) {
	// TODO Auto-generated method stub
	String SOAP_ACTION= "http://tempuri.org/obtenerPedidoDetallexVendedor_k";
	String METHOD_NAME="obtenerPedidoDetallexVendedor_k";
	
	SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);        
	Request.addProperty("codven", codven); 
	Request.addProperty("url", url); 
	Request.addProperty("catalog", catalog); 
	Request.addProperty("user", user); 
	Request.addProperty("password", contrasena); 
    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
    Soapenvelope.dotNet=true;
    Soapenvelope.setOutputSoapObject(Request);
    
    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);
    
    long beforecall = System.currentTimeMillis();
    
    try{
    	transporte.call(SOAP_ACTION, Soapenvelope);
    	Log.i("PEDIDO_DETALLE","RESPUESTA EN: "+(System.currentTimeMillis()-beforecall)+"miliseg");
    	// dbclass.getReadableDatabase().delete(DBtables.Pedido_detalle.TAG, null, null);
    	dbclass.EliminarPedido_detalle_enviados(codven);
    	SQLiteDatabase db = dbclass.getWritableDatabase();
    	
    	SoapObject result =(SoapObject)Soapenvelope.getResponse();
  
    	 db.beginTransaction();
    	 
    	 try{
    		 
        	 for (int i = 0; i <result.getPropertyCount(); i++)
        	 {
        	        
        		 
        		 SoapObject ic = (SoapObject)result.getProperty(i);
        		 
        		 //if(existePedidoDetalle(ic.getProperty(0).toString().trim(), ic.getProperty(2).toString().trim())){
      	        	
      	         //}
        		 //else{
        	     
        		    ContentValues cv = new ContentValues();
        		 
        		    /*DBPedido_Detalle entityPedidoDetale = new DBPedido_Detalle();

        	        
        	        entityPedidoDetale.setOc_numero(ic.getProperty(0).toString().trim());
        	        entityPedidoDetale.setEan_item("");
        	        entityPedidoDetale.setCip(ic.getProperty(2).toString().trim());
        	        entityPedidoDetale.setPrecio_bruto((ic.getProperty(3).toString().trim()));
        	        entityPedidoDetale.setPrecio_neto((ic.getProperty(4).toString().trim()));
        	        entityPedidoDetale.setCantidad(Integer.parseInt(ic.getProperty(5).toString().trim()));
        	        entityPedidoDetale.setTipo_producto(ic.getProperty(6).toString().trim());
        	        entityPedidoDetale.setUnidad_medida(Integer.parseInt(ic.getProperty(7).toString().trim()));
        	        entityPedidoDetale.setPeso_bruto((ic.getProperty(8).toString().trim()));
        	        entityPedidoDetale.setFlag(ic.getProperty(9).toString().trim());
        	        entityPedidoDetale.setCod_politica(ic.getProperty(10).toString().trim());
        	
        	        dbclass.AgregarPedidoDetalle(entityPedidoDetale);*/
        		 
        		     cv.put(DBtables.Pedido_detalle.PK_OC_NUMERO, ic.getProperty(0).toString().trim());
        		     cv.put(DBtables.Pedido_detalle.PK_EAN_ITEM, "");
        		     cv.put(DBtables.Pedido_detalle.CIP, ic.getProperty(2).toString().trim());
        		     cv.put(DBtables.Pedido_detalle.PRECIO_BRUTO, ic.getProperty(3).toString().trim());
        		     cv.put(DBtables.Pedido_detalle.PRECIO_NETO, ic.getProperty(4).toString().trim());
        		     cv.put(DBtables.Pedido_detalle.CANTIDAD, Integer.parseInt(ic.getProperty(5).toString().trim()));
        		     cv.put(DBtables.Pedido_detalle.TIPO_PRODUCTO, ic.getProperty(6).toString().trim());
        		     cv.put(DBtables.Pedido_detalle.UNIDAD_MEDIDA, Integer.parseInt(ic.getProperty(7).toString().trim()));
        		     cv.put(DBtables.Pedido_detalle.PESO_BRUTO, ic.getProperty(8).toString().trim());
        		     cv.put(DBtables.Pedido_detalle.FLAG, ic.getProperty(9).toString().trim());
        		     cv.put(DBtables.Pedido_detalle.COD_POLITICA, ic.getProperty(10).toString().trim());
        	                
        			db.insert(DBtables.Pedido_detalle.TAG, null, cv);
        	        Log.w("BAJAR PEDIDO DETALLE", ic.getPropertyCount()+"__"+ic.getProperty(0).toString().trim()+"__"+ic.getProperty(2).toString().trim()+"__"+ic.getProperty(10).toString().trim());
        	        
      	        //}
        	 }
        	
        	 db.setTransactionSuccessful();
    		 
    	 }catch(Exception ex){
    		 ex.printStackTrace();
    	 }
    	 finally{
    	     db.endTransaction();
    	     db.close();
    	 }

    	 Log.i("Pedido_detalle", "sincronizada");
    	 
    }catch(Exception e){
    	e.printStackTrace();
    }

}

public String peticion_modificar(String oc_numero) throws Exception{
	 
	String SOAP_ACTION= "http://tempuri.org/modificarPedido";
	String METHOD_NAME="modificarPedido";
	
	String flag="";

	SoapObject request=new SoapObject(NAMESPACE, METHOD_NAME);
	request.addProperty("oc_numero", oc_numero);
	
	//
	if(GlobalVar.id_servicio == GlobalVar.INTERNET){
		
    	request.addProperty("serv", url);
		request.addProperty("catalog", catalog); 
		request.addProperty("user", user); 
		request.addProperty("password", contrasena);
		
	}
	else if(GlobalVar.id_servicio == GlobalVar.LOCAL){
		
    	request.addProperty("serv", url_local);
		request.addProperty("catalog", catalog_local); 
		request.addProperty("user", user_local); 
		request.addProperty("password", contrasena_local);
		
	}
	//
	
    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
    Soapenvelope.dotNet=true;
    Soapenvelope.setOutputSoapObject(request);
    
    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);
    
    try{
    	transporte.call(SOAP_ACTION, Soapenvelope);
    	
    	SoapPrimitive result =(SoapPrimitive)Soapenvelope.getResponse();
       	  
    	  flag = result.toString();    
    	 
    	 Log.i("MODIFICAR_FLAG", ""+flag);
    	
    }catch(Exception e){
    	e.printStackTrace();
    	throw new Exception(e);
    } 
 
    return flag;
}

public void Sync_tabla_configuracion(String url, String catalog, String user, String contrasena) throws Exception{
	
	ArrayList<HeaderProperty> headerPropertyArrayList = new ArrayList<HeaderProperty>();
    headerPropertyArrayList.add(new HeaderProperty("Connection", "open"));
	
	String SOAP_ACTION= "http://tempuri.org/obtenerConfiguracion_json";
	String METHOD_NAME="obtenerConfiguracion_json";
	
	SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);   
	Request.addProperty("url", url); 
	Request.addProperty("catalog", catalog); 
	Request.addProperty("user", user); 
	Request.addProperty("password", contrasena); 
    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
    Soapenvelope.dotNet=true;
    Soapenvelope.setOutputSoapObject(Request);
    
    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);
    
    long beforecall = System.currentTimeMillis();
    
    try{
    	transporte.call(SOAP_ACTION, Soapenvelope, headerPropertyArrayList);
    	Log.i("CONFIGURACION","RESPUESTA EN: "+(System.currentTimeMillis()-beforecall)+"miliseg");

    	SoapPrimitive result = (SoapPrimitive)Soapenvelope.getResponse();  
    	
    	JSONArray jsonstring = new JSONArray(result.toString());
    	Log.i("CONFIGURACION", "Registros: "+jsonstring.length());
    	dbclass.syncConfiguracion(jsonstring);
    	Log.i("CONFIGURACION", "SINCRONIZADA");
    	
    }catch(Exception e){
    	e.printStackTrace();
    	Log.e("CONFIGURACION", "NO SINCRONIZADA");
    	throw new Exception(e);
    } 

}

public void Sync_tabla_AsignacionCuota1(String codven,String url, String catalog, String user, String contrasena) {
	// TODO Auto-generated method stub
	String SOAP_ACTION= "http://tempuri.org/obtenerAsignacionCuota1";
	String METHOD_NAME="obtenerAsignacionCuota1";
	
	SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);  
	Request.addProperty("codven", codven); 
	Request.addProperty("url", url); 
	Request.addProperty("catalog", catalog); 
	Request.addProperty("user", user); 
	Request.addProperty("password", contrasena); 
	
    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
    Soapenvelope.dotNet=true;
    Soapenvelope.setOutputSoapObject(Request);
    
    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);
    
    try{
    	transporte.call(SOAP_ACTION, Soapenvelope);
    	 dbclass.getReadableDatabase().delete(DBtables.AsignacionCuota1.TAG, null, null);
    	SoapObject result =(SoapObject)Soapenvelope.getResponse();
 
    	  SQLiteDatabase db = dbclass.getWritableDatabase();
    	 
    	  
    	 for (int i = 0; i <result.getPropertyCount(); i++)
    	 {
    	        SoapObject ic = (SoapObject)result.getProperty(i);
    	  
    	        ContentValues cv = new ContentValues();
    	        
    	        cv.put(DBtables.AsignacionCuota1.SECUENCIA, Integer.parseInt(ic.getProperty(0).toString()));
    	        cv.put(DBtables.AsignacionCuota1.FECHA_INICIO, ic.getProperty(1).toString());
    	        cv.put(DBtables.AsignacionCuota1.TOTAL_DIAS, Integer.parseInt(ic.getProperty(2).toString()));
    	        cv.put(DBtables.AsignacionCuota1.FECHA_FIN, (ic.getProperty(3).toString()));
    	        cv.put(DBtables.AsignacionCuota1.ESTADO, (ic.getProperty(4).toString()));
    	        cv.put(DBtables.AsignacionCuota1.N_CLIENTES, Integer.parseInt(ic.getProperty(5).toString()));
    	        cv.put(DBtables.AsignacionCuota1.N_VENDEDORES, Integer.parseInt(ic.getProperty(6).toString()));
    	        cv.put(DBtables.AsignacionCuota1.ID_LOCAL, (ic.getProperty(7).toString()));
    	        cv.put(DBtables.AsignacionCuota1.ID_PC, Integer.parseInt(ic.getProperty(8).toString()));
    	        cv.put(DBtables.AsignacionCuota1.USECOD, Integer.parseInt(ic.getProperty(9).toString()));
    	        cv.put(DBtables.AsignacionCuota1.NOMCUOTA, (ic.getProperty(10).toString()));
    	        cv.put(DBtables.AsignacionCuota1.DESCRIPCION, (ic.getProperty(11).toString()));
    	        cv.put(DBtables.AsignacionCuota1.TOTAL_CUOTA, (ic.getProperty(12).toString()));
    	        cv.put(DBtables.AsignacionCuota1.FECHA_OPERACION, (ic.getProperty(13).toString()));
    	        cv.put(DBtables.AsignacionCuota1.TOTAL_PROYECCION, (ic.getProperty(14).toString()));
    	        cv.put(DBtables.AsignacionCuota1.PROYECCION_DESDE, (ic.getProperty(15).toString()));
    	        cv.put(DBtables.AsignacionCuota1.PROYECCION_HASTA, (ic.getProperty(16).toString()));
    	        cv.put(DBtables.AsignacionCuota1.PORCENTAJE_CRECIMIENTO, (ic.getProperty(17).toString()));
    	        
    			db.insert(DBtables.AsignacionCuota1.TAG, null, cv);
       
    	 }
    	
    	 db.close();
    	 Log.i("ASIGNACION_CUOTA1", "sincronizada");
    	 
    }catch(Exception e){
    	e.printStackTrace();
    }   	
}


public void Sync_tabla_CuotaProducto(String codven, String url, String catalog, String user, String contrasena) {
	// TODO Auto-generated method stub
	String SOAP_ACTION= "http://tempuri.org/obtenerCuotaProducto";
	String METHOD_NAME="obtenerCuotaProducto";
	
	SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);        
	Request.addProperty("codven", codven); 
	Request.addProperty("url", url); 
	Request.addProperty("catalog", catalog); 
	Request.addProperty("user", user); 
	Request.addProperty("password", contrasena); 
    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
    Soapenvelope.dotNet=true;
    Soapenvelope.setOutputSoapObject(Request);
    
    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);
    
    try{
    	transporte.call(SOAP_ACTION, Soapenvelope);
    	 dbclass.getReadableDatabase().delete(DBtables.CuotaProducto.TAG, null, null);
    	SoapObject result =(SoapObject)Soapenvelope.getResponse();
 
    	  SQLiteDatabase db = dbclass.getWritableDatabase();
    	 
    	  
    	 for (int i = 0; i <result.getPropertyCount(); i++)
    	 {
    	        SoapObject ic = (SoapObject)result.getProperty(i);
    	  
    	        ContentValues cv = new ContentValues();
    	        
    	        cv.put(DBtables.CuotaProducto.SECUENCIA, Integer.parseInt(ic.getProperty(0).toString()));
    	        cv.put(DBtables.CuotaProducto.CODPRO, ic.getProperty(1).toString());
    	        cv.put(DBtables.CuotaProducto.CUOTA, (ic.getProperty(2).toString()));
    	        cv.put(DBtables.CuotaProducto.CANTIDAD, (ic.getProperty(3).toString()));
    	       
    			db.insert(DBtables.CuotaProducto.TAG, null, cv);
       
    	 }
    	
    	 db.close();
    	 Log.i("CUOTA PRODUCTO", "sincronizada");
    	 
    }catch(Exception e){
    	e.printStackTrace();
    }   	
}

public void Sync_tabla_CuotaVendedor(String codven, String url, String catalog, String user, String contrasena) throws Exception {

		String SOAP_ACTION= "http://tempuri.org/obtenerCuotaVendedor_json";
		String METHOD_NAME="obtenerCuotaVendedor_json";
		long beforecall;
		
		SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);
		Request.addProperty("codven", codven);
		Request.addProperty("url", url); 
		Request.addProperty("catalog", catalog); 
		Request.addProperty("user", user); 
		Request.addProperty("password", contrasena); 
	    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    Soapenvelope.dotNet=true;
	    Soapenvelope.setOutputSoapObject(Request);
	    
	    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);
	    
	    beforecall = System.currentTimeMillis();
	    
	    try{
	    	transporte.call(SOAP_ACTION, Soapenvelope);
	    	
	    	Log.i("CUOTA_VENDEDOR","RESPUESTA EN: "+(System.currentTimeMillis()-beforecall)+"miliseg");
	    	
	    	  SoapPrimitive result = (SoapPrimitive)Soapenvelope.getResponse();  
	    	  
	    	  JSONArray jsonstring = new JSONArray(result.toString());
	    	  Log.i("CUOTA_VENDEDOR","Registros: "+jsonstring.length());
	    	  
	    	  dbclass.syncCuotaVendedor(jsonstring);
	
	    	 Log.i("CUOTA_VENDEDOR", "SINCRONIZADA");
	    	
	    }catch(Exception e){
	    	 e.printStackTrace();
	    	 Log.e("CUOTA_VENDEDOR", "NO SINCRONIZADA");
	    	 throw new Exception(e);
	    }
	
}

public void Sync_tabla_RegistroVentas1(String codven,String url, String catalog, String user, String contrasena) {
	// TODO Auto-generated method stub
	String SOAP_ACTION= "http://tempuri.org/obtenerRegistroVentas1";
	String METHOD_NAME="obtenerRegistroVentas1";
	
	SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME); 
	Request.addProperty("url", url); 
	Request.addProperty("catalog", catalog); 
	Request.addProperty("user", user); 
	Request.addProperty("password", contrasena); 
	Request.addProperty("codven", codven); 
    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
    Soapenvelope.dotNet=true;
    Soapenvelope.setOutputSoapObject(Request);
    
    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);
    
    try{
    	transporte.call(SOAP_ACTION, Soapenvelope);
    	 dbclass.getReadableDatabase().delete(DBtables.RegistroVentas1.TAG, null, null);
    	SoapObject result =(SoapObject)Soapenvelope.getResponse();
 
    	  SQLiteDatabase db = dbclass.getWritableDatabase();
    	 
    	  
    	 for (int i = 0; i <result.getPropertyCount(); i++)
    	 {
    	        SoapObject ic = (SoapObject)result.getProperty(i);
    	  
    	        ContentValues cv = new ContentValues();
    	        
    	        cv.put(DBtables.RegistroVentas1.TIPO_DOCUMENTO, (ic.getProperty(0).toString()));
    	        cv.put(DBtables.RegistroVentas1.SECUENCIA, Integer.parseInt(ic.getProperty(1).toString()));
    	        cv.put(DBtables.RegistroVentas1.FECFAC, (ic.getProperty(2).toString()));
    	        cv.put(DBtables.RegistroVentas1.CODCLI, (ic.getProperty(3).toString()));
    	        cv.put(DBtables.RegistroVentas1.TIPO_CLIENTE, Integer.parseInt(ic.getProperty(4).toString()));
    	        cv.put(DBtables.RegistroVentas1.CODVEN, (ic.getProperty(5).toString()));
    	        cv.put(DBtables.RegistroVentas1.SERIE_DOCUMENTO, (ic.getProperty(6).toString()));
    	        cv.put(DBtables.RegistroVentas1.NUMERO_DOCUMENTO, (ic.getProperty(7).toString()));
    	        cv.put(DBtables.RegistroVentas1.SUB_TOTAL, (ic.getProperty(8).toString()));
    	        cv.put(DBtables.RegistroVentas1.IGV, (ic.getProperty(9).toString()));
    	        cv.put(DBtables.RegistroVentas1.TOTAL, (ic.getProperty(10).toString()));
    	        cv.put(DBtables.RegistroVentas1.MOTIVO, (ic.getProperty(11).toString()));
    	       
    			db.insert(DBtables.RegistroVentas1.TAG, null, cv);
    	 }
    	
    	 db.close();
    	 Log.i("REGISTRO_VENTAS1", "sincronizada");
    	 
    }catch(Exception e){
    	e.printStackTrace();
    }   	
}

public void Sync_tabla_RegistroVentas2(String codven,String url, String catalog, String user, String contrasena) {
	// TODO Auto-generated method stub
	String SOAP_ACTION= "http://tempuri.org/obtenerRegistroVentas2";
	String METHOD_NAME="obtenerRegistroVentas2";
	
	SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);
	Request.addProperty("url", url); 
	Request.addProperty("catalog", catalog); 
	Request.addProperty("user", user); 
	Request.addProperty("password", contrasena); 
	Request.addProperty("codven", codven); 
    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
    Soapenvelope.dotNet=true;
    Soapenvelope.setOutputSoapObject(Request);
    
    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);
    
    try{
    	transporte.call(SOAP_ACTION, Soapenvelope);
    	 dbclass.getReadableDatabase().delete(DBtables.RegistroVentas2.TAG, null, null);
    	SoapObject result =(SoapObject)Soapenvelope.getResponse();
 
    	  SQLiteDatabase db = dbclass.getWritableDatabase();
    	 
    	  
    	 for (int i = 0; i <result.getPropertyCount(); i++)
    	 {
    	        SoapObject ic = (SoapObject)result.getProperty(i);
    	  
    	        ContentValues cv = new ContentValues();
    	        
    	        cv.put(DBtables.RegistroVentas2.TIPO_DOCUMENTO, (ic.getProperty(0).toString()));
    	        cv.put(DBtables.RegistroVentas2.SECUENCIA, Integer.parseInt(ic.getProperty(1).toString()));
    	        cv.put(DBtables.RegistroVentas2.CODPRO, (ic.getProperty(2).toString()));
    	        cv.put(DBtables.RegistroVentas2.CANTIDAD, (ic.getProperty(3).toString()));
    	        cv.put(DBtables.RegistroVentas2.PREVEN, (ic.getProperty(4).toString()));
    	        cv.put(DBtables.RegistroVentas2.TOTAL, (ic.getProperty(5).toString()));
    	        cv.put(DBtables.RegistroVentas2.SECUENCIAORIGEN, (ic.getProperty(6).toString()));
    	       
    			db.insert(DBtables.RegistroVentas2.TAG, null, cv);
       
    	 }
    	
    	 db.close();
    	 Log.i("REGISTRO_VENTAS2", "sincronizada");
    	 
    }catch(Exception e){
    	e.printStackTrace();
    }   	
}


public void Sync_tabla_Zona(String codven, String url, String catalog, String user, String contrasena) throws Exception {
	// TODO Auto-generated method stub
	String SOAP_ACTION= "http://tempuri.org/obtenerZona_json";
	String METHOD_NAME="obtenerZona_json";
	
	SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);        
	Request.addProperty("codven", codven); 
	Request.addProperty("url", url); 
	Request.addProperty("catalog", catalog); 
	Request.addProperty("user", user); 
	Request.addProperty("password", contrasena); 
    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
    Soapenvelope.dotNet=true;
    Soapenvelope.setOutputSoapObject(Request);
    
    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);
    
    long beforecall = System.currentTimeMillis();
    
    try{
    	transporte.call(SOAP_ACTION, Soapenvelope);
    	 Log.i("ZONA","RESPUESTA EN: "+(System.currentTimeMillis()-beforecall)+"miliseg");

    	SoapPrimitive result =(SoapPrimitive)Soapenvelope.getResponse();
    	
    	JSONArray jsonstring = new JSONArray(result.toString());
    	Log.i("ZONA","Registros: "+jsonstring.length());
    	dbclass.syncZona(jsonstring);
    	
    	Log.i("ZONA", "SINCRONIZADA");
    	 
    }catch(Exception e){
    	e.printStackTrace();
    	Log.e("ZONA", "NO SINCRONIZADA");
    	throw new Exception(e);
    } 

}

public void Sync_tabla_Zona_XY(String codven, String url, String catalog, String user, String contrasena) throws Exception {
	// TODO Auto-generated method stub
	String SOAP_ACTION= "http://tempuri.org/obtenerZonaXY_json";
	String METHOD_NAME="obtenerZonaXY_json";
	
	SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);
	Request.addProperty("codven", codven); 
	Request.addProperty("url", url); 
	Request.addProperty("catalog", catalog); 
	Request.addProperty("user", user); 
	Request.addProperty("password", contrasena); 
    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
    Soapenvelope.dotNet=true;
    Soapenvelope.setOutputSoapObject(Request);
    
    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);
    
    long beforecall = System.currentTimeMillis();
    
    try{
    	transporte.call(SOAP_ACTION, Soapenvelope);
    	 Log.i("ZONA_XY","RESPUESTA EN: "+(System.currentTimeMillis()-beforecall)+"miliseg");
    	
    	SoapPrimitive result =(SoapPrimitive)Soapenvelope.getResponse();
    	
    	 JSONArray jsonstring = new JSONArray(result.toString());
    	 Log.i("ZONA_XY","Registros: "+jsonstring.length());
    	 dbclass.syncZonaXY(jsonstring);
    	 
    	 Log.i("ZONA_XY", "SINCRONIZADA");
    	 
    }catch(Exception e){
    	e.printStackTrace();
    	Log.e("ZONA_XY", "NO SINCRONIZADA");
    	throw new Exception(e);
    } 

}



public void Sync_tabla_ZnfProgramacionClientes(String codven, String url, String catalog, String user, String contrasena) throws Exception {
	// TODO Auto-generated method stub
	String SOAP_ACTION= "http://tempuri.org/obtenerZonaPrograClientes_json";
	String METHOD_NAME="obtenerZonaPrograClientes_json";
	
	SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);        
	Request.addProperty("codven", codven); 
	Request.addProperty("url", url); 
	Request.addProperty("catalog", catalog); 
	Request.addProperty("user", user); 
	Request.addProperty("password", contrasena); 
    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
    Soapenvelope.dotNet=true;
    Soapenvelope.setOutputSoapObject(Request);
    
    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService,20000);
    
    long beforecall = System.currentTimeMillis();
    
    try{
    	transporte.call(SOAP_ACTION, Soapenvelope);
    	Log.i("ZNF PROGRAMACION CLIENTES","RESPUESTA EN: "+(System.currentTimeMillis()-beforecall)+"miliseg");
    	 
    	 SoapPrimitive result = (SoapPrimitive)Soapenvelope.getResponse();

    	 JSONArray jsonstring = new JSONArray(result.toString());
    	 Log.i("ZNF PROGRAMACION CLIENTES","Registros: "+jsonstring.length());
    	 dbclass.syncZnfProgramacionClientes(jsonstring);
    	 
    	 Log.i("ZNF PROGRAMACION CLIENTES", "SINCRONIZADA");
    	 
    }catch(Exception e){
    	e.printStackTrace();
    	Log.e("ZNF PROGRAMACION CLIENTES", "NO SINCRONIZADA");
    	throw new Exception(e);
    }   

}


public void Sync_tabla_Ruta(String codven,String url, String catalog, String user, String contrasena) throws Exception {
	// TODO Auto-generated method stub
	String SOAP_ACTION= "http://tempuri.org/obtenerRuta_json";
	String METHOD_NAME="obtenerRuta_json";
	
	SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);        
	Request.addProperty("codven", codven); 
	Request.addProperty("url", url); 
	Request.addProperty("catalog", catalog); 
	Request.addProperty("user", user); 
	Request.addProperty("password", contrasena); 
    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
    Soapenvelope.dotNet=true;
    Soapenvelope.setOutputSoapObject(Request);
    
    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);
    
    long beforecall = System.currentTimeMillis();
    
    try{
    	transporte.call(SOAP_ACTION, Soapenvelope);
    	Log.i("RUTA","RESPUESTA EN: "+(System.currentTimeMillis()-beforecall)+"miliseg");

    	SoapPrimitive result =(SoapPrimitive)Soapenvelope.getResponse();
    	
    	JSONArray jsonstring = new JSONArray(result.toString());
    	Log.i("RUTA", "Registros: "+jsonstring.length());
    	dbclass.syncRuta(jsonstring);
    	Log.i("RUTA", "SINCRONIZADA");
    	 
    }catch(Exception e){
    	e.printStackTrace();
    	Log.e("RUTA", "NO SINCRONIZADA");
    	throw new Exception(e);
    }
    
}


public void Sync_tabla_RegistrosGenerales(String url, String catalog, String user, String contrasena ){
	
	String SOAP_ACTION= "http://tempuri.org/obtenerRegistrosGenerales";
	String METHOD_NAME="obtenerRegistrosGenerales";

	
	SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);
	Request.addProperty("url", url); 
	Request.addProperty("catalog", catalog); 
	Request.addProperty("user", user); 
	Request.addProperty("password", contrasena); 
    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
    Soapenvelope.dotNet=true;
    Soapenvelope.setOutputSoapObject(Request);
    
    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);
    
    try{
    	transporte.call(SOAP_ACTION, Soapenvelope);
    	
    	SoapObject result =(SoapObject)Soapenvelope.getResponse();
    	  
    	  dbclass.getReadableDatabase().delete(DBtables.RegistrosGenerales.TAG, null, null);
    	  SQLiteDatabase db = dbclass.getWritableDatabase();
    	 
    	 
    	  
    	
    	    
    	 for (int i = 0; i <result.getPropertyCount(); i++)
    	 {
    	        SoapObject ic = (SoapObject)result.getProperty(i);
    	  
    	        ContentValues cv = new ContentValues();
    	        
    	        cv.put(DBtables.RegistrosGenerales.SECUENCIA, Integer.parseInt(ic.getProperty(0).toString().trim()));  
    	        cv.put(DBtables.RegistrosGenerales.DESCRIPCION, ic.getProperty(1).toString().trim());  
    	        cv.put(DBtables.RegistrosGenerales.ESTADO, ic.getProperty(2).toString().trim());  
    	        cv.put(DBtables.RegistrosGenerales.ADJETIVO, ic.getProperty(3).toString().trim());  
    	        cv.put(DBtables.RegistrosGenerales.SEC_REF, Integer.parseInt( ic.getProperty(4).toString().trim()));  
    	        db.insert(DBtables.RegistrosGenerales.TAG, null, cv);
       
    	 }
    	 
    	 db.close();
    	 Log.i("REGISTROS GENERALES", "sincronizado ");
    	
    }catch(Exception e){
    	e.printStackTrace();
    }   		
}

public void Sync_tabla_PromocionDetalle( String codven,String url, String catalog, String user, String contrasena) throws Exception{
	
	String SOAP_ACTION= "http://tempuri.org/obtenerPromocionDetalle_json";
	String METHOD_NAME="obtenerPromocionDetalle_json";
	
	SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME); 
	Request.addProperty("url", url); 
	Request.addProperty("catalog", catalog); 
	Request.addProperty("user", user); 
	Request.addProperty("password", contrasena); 
	Request.addProperty("codven", codven);
    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
    Soapenvelope.dotNet=true;
    Soapenvelope.setOutputSoapObject(Request);
    
    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);
    
    long beforecall = System.currentTimeMillis();
    
    try{
    	transporte.call(SOAP_ACTION, Soapenvelope);
    	Log.i("PROMOCION_DETALLE","RESPUESTA EN: "+(System.currentTimeMillis()-beforecall)+"miliseg");

    	 SoapPrimitive result = (SoapPrimitive)Soapenvelope.getResponse();
    	
    	 JSONArray jsonstring = new JSONArray(result.toString());
    	 Log.i("PROMOCION_DETALLE","Registros: "+jsonstring.length()); 
    	 dbclass.syncPromocionDetalle(jsonstring);
    	 Log.i("PROMOCION_DETALLE", "SINCRONIZADA");
    	 
    }catch(Exception e){
    	e.printStackTrace();
    	Log.i("PROMOCION_DETALLE", "NO SINCRONIZADA");
    	throw new Exception(e);
    }   

}

public void verificarDepositosEnviados() throws Exception{
 
	try 
	{
		String SOAP_ACTION= "http://tempuri.org/verificarDepositos";
		String METHOD_NAME="verificarDepositos";
		ArrayList<DBDepositos>  list_Depositos= new ArrayList<DBDepositos>();
	    list_Depositos = dbclass.getDepositos();
			
			Iterator<DBDepositos> it=list_Depositos.iterator();
		
			  while ( it.hasNext() ) { 
				  
				  Object objeto = it.next(); 
				  DBDepositos entityDeposito = (DBDepositos)objeto; 
				  
  
					  PropertyInfo pi = new PropertyInfo();
						pi.setName("entityDeposito");
						pi.setValue(entityDeposito);
						pi.setType(entityDeposito.getClass());
					  
						SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
						request.addProperty(pi);

				    	if(GlobalVar.id_servicio == GlobalVar.INTERNET){
				    		
				        	request.addProperty("url", url); 
				    		request.addProperty("catalog", catalog); 
				    		request.addProperty("user", user); 
				    		request.addProperty("password", contrasena);
				    		
				    	}
				    	else if(GlobalVar.id_servicio == GlobalVar.LOCAL){
				    		
				        	request.addProperty("url", url_local); 
				    		request.addProperty("catalog", catalog_local); 
				    		request.addProperty("user", user_local); 
				    		request.addProperty("password", contrasena_local);
				    		
				    	}
						
						SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
						envelope.dotNet = true; 
						envelope.setOutputSoapObject(request);
						envelope.addMapping(NAMESPACE, "DBDepositos", entityDeposito.getClass());

						HttpTransportSE transporte = new HttpTransportSE(URL+GlobalVar.urlService);
						transporte.call(SOAP_ACTION, envelope);

						SoapPrimitive resultado_xml =(SoapPrimitive)envelope.getResponse();
						String res = resultado_xml.toString();
						Log.w("HORA SERVIDOR", res+"-" + res.length());
						if(res.equals("2"))
						{
							Log.i("DEPOSITOS", "Datos Correctos en el servidor");
							 //cambiar estado a "E" (enviado)d
							 dbclass.updateFlagDeposito(entityDeposito.getSecuencia(), "E");
						}
						else if(res.equals("1")){
							Log.i("DEPOSITOS", "Si existe pero  no coinciden atributos");
							 dbclass.updateFlagDeposito(entityDeposito.getSecuencia(), "I");
						}
						else if(res.equals("0")){
							Log.i("DEPOSITOS", "No Existe OC en el servidor");
							 dbclass.updateFlagDeposito(entityDeposito.getSecuencia(), "P");
						}
						else
						{
							
							Log.i("DEPOSITOS", "ERROR EN LA VERIFICACION");
							throw new Exception();
						}
	
			}				  				 	  
	
	} 
	catch (Exception e) 
	{
		Log.i("DEPOSITOS", "Error al ENVIAR DATOS AL SERVIDOR" );
		throw new Exception(e);
	} 
	
}

public void verificarPedidosEnviados() throws Exception {

   	DBclasses obj_dbclasses= new DBclasses(context);
 
	try 
	{
		String SOAP_ACTION= "http://tempuri.org/verificarPedidoCabecera_k";
		String METHOD_NAME="verificarPedidoCabecera_k";
		ArrayList<DBPedido_Cabecera>  lista_dbPedido_cabecera= new ArrayList<DBPedido_Cabecera>();
	    
		//lista_dbPedido_cabecera= obj_dbclasses.getTodosPedidosCabecera();
		lista_dbPedido_cabecera= obj_dbclasses.getTodosPedidosCabecera_paraVerificacion();
		
			
			Iterator<DBPedido_Cabecera> it=lista_dbPedido_cabecera.iterator();
		
			  while ( it.hasNext() ) { 
				  
				  Object objeto = it.next(); 
				  DBPedido_Cabecera ped_cabecera = (DBPedido_Cabecera)objeto; 
				  
  
					  PropertyInfo pi = new PropertyInfo();
						pi.setName("db_pedido_cabecera");
						pi.setValue(ped_cabecera);
						pi.setType(ped_cabecera.getClass());
					  
						SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
						request.addProperty(pi);
						//
						
				    	if(GlobalVar.id_servicio == GlobalVar.INTERNET){
				    		
				        	request.addProperty("url", url); 
				    		request.addProperty("catalog", catalog); 
				    		request.addProperty("user", user); 
				    		request.addProperty("password", contrasena);
				    		
				    	}
				    	else if(GlobalVar.id_servicio == GlobalVar.LOCAL){
				    		
				        	request.addProperty("url", url_local); 
				    		request.addProperty("catalog", catalog_local); 
				    		request.addProperty("user", user_local); 
				    		request.addProperty("password", contrasena_local);
				    		
				    	}
						

						//
						
						SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
						envelope.dotNet = true; 
						envelope.setOutputSoapObject(request);
						envelope.addMapping(NAMESPACE, "DBPedido_Cabecera", ped_cabecera.getClass());

						HttpTransportSE transporte = new HttpTransportSE(URL+GlobalVar.urlService);
						transporte.call(SOAP_ACTION, envelope);

						SoapPrimitive resultado_xml =(SoapPrimitive)envelope.getResponse();
						String res = resultado_xml.toString();
						
						Log.w("HORA SERVIDOR", res+"-" + res.length());
						if(res.equals("2"))
						{
							Log.i("PEDIDO_CABECERA", "Datos Correctos en el servidor");
							 //cambiar Flag a "E" (enviado)
							 obj_dbclasses.updateFlagPedidoCabecera(ped_cabecera.getOc_numero(), "E");
						}
						else if(res.equals("1")){
							Log.i("PEDIDO_CABECERA", "Si existe pero  no coinciden atributos");
							//cambio Flag a "I" (incompleto o atributos diferentes)
							 obj_dbclasses.updateFlagPedidoCabecera(ped_cabecera.getOc_numero(), "I");
						}
						else if(res.equals("0")){
							Log.i("PEDIDO_CABECERA", "No Existe OC en el servidor");
							//cambio Flag a "P" (Pendiente de envio si no se encuentra en el servidor)
							 obj_dbclasses.updateFlagPedidoCabecera(ped_cabecera.getOc_numero(), "P");							 
						}
						else
						{
							
							Log.i("PEDIDO_CABECERA", "ERROR EN LA VERIFICACION");
							throw new Exception();
						}
	
			}				  				 	  
	
	} 
	catch (Exception e) 
	{
		Log.e("PEDIDO_CABECERA", "Error al ENVIAR DATOS AL SERVIDOR" );
		e.printStackTrace();
		throw new Exception(e);
	} 
	
}


public int verificarPedidoEnviado_xocnumero(String oc_numero) {


   	DBclasses obj_dbclasses= new DBclasses(context);
   	int valor=0;
 
	try 
	{
		String SOAP_ACTION= "http://tempuri.org/verificarPedidoCabecera_k";
		String METHOD_NAME="verificarPedidoCabecera_k";
		DBPedido_Cabecera ped_cabecera = new DBPedido_Cabecera();
	    
		//lista_dbPedido_cabecera= obj_dbclasses.getTodosPedidosCabecera();
		ped_cabecera = obj_dbclasses.getPedidoCabecera_paraVerificacion(oc_numero);
		
  
					    PropertyInfo pi = new PropertyInfo();
						pi.setName("db_pedido_cabecera");
						pi.setValue(ped_cabecera);
						pi.setType(ped_cabecera.getClass());
					  
						SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
						request.addProperty(pi);
						//
						
						if(GlobalVar.servicio_principal_activo){
							request.addProperty("url", url); 
							request.addProperty("catalog", catalog); 
							request.addProperty("user", user); 
							request.addProperty("password", contrasena);
						}
						else{
							request.addProperty("url", url_local); 
							request.addProperty("catalog", catalog_local); 
							request.addProperty("user", user_local); 
							request.addProperty("password", contrasena_local);
						}
						

						//
						
						SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
						envelope.dotNet = true; 
						envelope.setOutputSoapObject(request);
						envelope.addMapping(NAMESPACE, "DBPedido_Cabecera", ped_cabecera.getClass());

						HttpTransportSE transporte = new HttpTransportSE(URL+GlobalVar.urlService);
						transporte.call(SOAP_ACTION, envelope);

						SoapPrimitive resultado_xml =(SoapPrimitive)envelope.getResponse();
						String res = resultado_xml.toString();
						
						Log.w("HORA SERVIDOR", res+"-" + res.length());
						if(res.equals("2"))
						{
							Log.i("PEDIDO_CABECERA", "Datos Correctos en el servidor");

						}
						else if(res.equals("1")){
							Log.i("PEDIDO_CABECERA", "Si existe pero  no coinciden atributos");

						}
						else if(res.equals("0")){
							Log.i("PEDIDO_CABECERA", "No Existe OC en el servidor");

						}
						else
						{
							
							Log.i("PEDIDO_CABECERA", "ERROR EN LA VERIFICACION"); 
						}
						
						return Integer.parseInt(res);
	
	} 
	catch (Exception e) 
	{
		Log.i("PEDIDO_CABECERA", "Error al ENVIAR DATOS AL SERVIDOR" );
		//3 indica si hubio algun error en el envio
		return 3;
		
	}
	
	
}



public void verificarCobranzaEnviados() throws Exception{
 
	try 
	{
		String SOAP_ACTION= "http://tempuri.org/verificarCobranza";
		String METHOD_NAME="verificarCobranza";
		ArrayList<DBIngresos>  lista_ingresos= new ArrayList<DBIngresos>();
	    lista_ingresos= dbclass.getIngresos_paraVerificacion();

			
			Iterator<DBIngresos> it=lista_ingresos.iterator();
		
			  while ( it.hasNext() ) { 
				  
				  Object objeto = it.next(); 
				  DBIngresos entityIngresos = (DBIngresos)objeto; 
				  
  
					  PropertyInfo pi = new PropertyInfo();
						pi.setName("entityIngresos");
						pi.setValue(entityIngresos);
						pi.setType(entityIngresos.getClass());
					  
						SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
						request.addProperty(pi);
						
				    	if(GlobalVar.id_servicio == GlobalVar.INTERNET){
				    		
				        	request.addProperty("url", url); 
				    		request.addProperty("catalog", catalog); 
				    		request.addProperty("user", user); 
				    		request.addProperty("password", contrasena);
				    		
				    	}
				    	else if(GlobalVar.id_servicio == GlobalVar.LOCAL){
				    		
				        	request.addProperty("url", url_local); 
				    		request.addProperty("catalog", catalog_local); 
				    		request.addProperty("user", user_local); 
				    		request.addProperty("password", contrasena_local);
				    		
				    	}
						
						SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
						envelope.dotNet = true; 
						envelope.setOutputSoapObject(request);
						envelope.addMapping(NAMESPACE, "DBIngresos", entityIngresos.getClass());

						HttpTransportSE transporte = new HttpTransportSE(URL+GlobalVar.urlService);
						transporte.call(SOAP_ACTION, envelope);

						SoapPrimitive resultado_xml =(SoapPrimitive)envelope.getResponse();
						String res = resultado_xml.toString();
						Log.w("HORA SERVIDOR", res+"-" + res.length());
						
						if(res.equals("2"))
						{
							 Log.i("INGRESOS", "Datos Correctos en el servidor");
							 //cambiar estado a "E" (enviado)d
							 dbclass.updateIngresos(""+entityIngresos.getSecuencia(),""+entityIngresos.getSecitm(), "E");
						}
						else if(res.equals("1")){
							 Log.i("INGRESOS", "Si existe pero  no coinciden atributos");
							 dbclass.updateIngresos(""+entityIngresos.getSecuencia(),""+entityIngresos.getSecitm(),"I");
						}
						else if(res.equals("0")){
							 Log.i("INGRESOS", "No Existe OC en el servidor");
							 dbclass.updateIngresos(""+entityIngresos.getSecuencia(),""+entityIngresos.getSecitm(), "P");
						}
						else
						{
							
							Log.i("INGRESOS", "ERROR EN LA VERIFICACION"); 
							throw new Exception();
						}
	
			}				  				 	  
	
	} 
	catch (Exception e) 
	{
		Log.i("INGRESOS", "Error al ENVIAR DATOS AL SERVIDOR" );
		throw new Exception(e);
	} 
	
}

public void  actualizarPedido_devolucion() {

   	DBclasses obj_dbclasses= new DBclasses(context);
   	
	try 
	{
		String SOAP_ACTION= "http://tempuri.org/actualizarPedidoDevolucion";
		String METHOD_NAME="actualizarPedidoDevolucion";
		ArrayList<DBPedido_Devolucion>  lista_dbPedido_devolucion= new ArrayList<DBPedido_Devolucion>();
	    lista_dbPedido_devolucion= obj_dbclasses.getPedidosDevolucion();
			
		
			
			Iterator<DBPedido_Devolucion> it=lista_dbPedido_devolucion.iterator();
						  
			  while ( it.hasNext() ) { 
				  		
				  Object objeto = it.next(); 
				  DBPedido_Devolucion ped_devolucion = (DBPedido_Devolucion)objeto; 
				  
				  if(ped_devolucion.getFlag().equals("N")){
					  
					  PropertyInfo pi = new PropertyInfo();
						pi.setName("db_pedido_devolucion");
						pi.setValue(ped_devolucion);
						pi.setType(ped_devolucion.getClass());
					  
						SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
						request.addProperty(pi);
						//
						request.addProperty("url", url); 
						request.addProperty("catalog", catalog); 
						request.addProperty("user", user); 
						request.addProperty("password", contrasena);
						//
						
						SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
						envelope.dotNet = true; 
						envelope.setOutputSoapObject(request);
						envelope.addMapping(NAMESPACE, "DBPedido_Devolucion", ped_devolucion.getClass());

						HttpTransportSE transporte = new HttpTransportSE(URL+GlobalVar.urlService);
						transporte.call(SOAP_ACTION, envelope);

						SoapPrimitive resultado_xml =(SoapPrimitive)envelope.getResponse();
						String res = resultado_xml.toString();
						
						if(res.equals("1"))
						{
							 obj_dbclasses.cambiar_estado_pedido_devolucion(ped_devolucion.getOc_numero(),
									 ped_devolucion.getCip(), "E");
							 Log.i("PEDIDO_DEVOLUCION", "datos subidos al servidor");
						}
						else
						{
							Log.i("PEDIDO_DEVOLUCION", "Error al actualizar datos"); 
						}	
					  
				  }else{
					  Log.i("PEDIDO_DEVOLUCION", "item subido anteriormente");
				  }
							
			}		
			  
	} 
	catch (Exception e) 
	{
		Log.i("PEDIDO_DEVOLUCION", "Error al ENVIAR DATOS AL SERVIDOR"); 
	} 
	
}


public void Sync_tabla_Servidor(String url, String catalog, String user, String contrasena) throws Exception{
	
	String SOAP_ACTION= "http://tempuri.org/obtenerTodoServicios_conf";
	//String METHOD_NAME="obtenerTodoServidor2";
	String METHOD_NAME="obtenerTodoServicios_conf";

	SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);
	
	DB_Empresa emp = new DB_Empresa();
	emp = dbclass.getEmpresa();
	
	Request.addProperty("ruc", emp.getRuc());
	
    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
    Soapenvelope.dotNet=true;
    Soapenvelope.setOutputSoapObject(Request);
    
    //HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);
    HttpTransportSE transporte=new HttpTransportSE("http://saemoviles.com/Service.asmx");
	 //Log.i("***VERIFICANDO GLOBAL VAR", ""+URL+"--" + GlobalVar.urlService+"--" + GlobalVar.NombreWEB);
    
    
    try{
    	transporte.call(SOAP_ACTION, Soapenvelope);
    	
    	SoapObject result =(SoapObject)Soapenvelope.getResponse();
    	  
    	  dbclass.getReadableDatabase().delete(DBtables.Servidor.TAG, null, null);
    	  SQLiteDatabase db = dbclass.getWritableDatabase();
    	  
    	  db.beginTransaction();
    	  
    	  try{
    		  
    	    	 for (int i = 0; i <result.getPropertyCount(); i++)
    	    	 {
    	    	        SoapObject ic = (SoapObject)result.getProperty(i);
    	    	  
    	    	        ContentValues cv = new ContentValues();
    	    	        
    	    	        cv.put(DBtables.Servidor.TX_SERV_servicioWEB, ic.getProperty(0).toString().trim());  
    	    	        cv.put(DBtables.Servidor.TX_SERV_servidorBD, ic.getProperty(1).toString().trim());  
    	    	        cv.put(DBtables.Servidor.TX_SERV_nombreBD, ic.getProperty(2).toString().trim());  
    	    	        cv.put(DBtables.Servidor.TX_SERV_usuario, ic.getProperty(3).toString().trim());  
    	    	        cv.put(DBtables.Servidor.TX_SERV_contrasena, ic.getProperty(4).toString().trim());  
    	    	        cv.put(DBtables.Servidor.IN_SERV_item, ic.getProperty(5).toString().trim());  
    	    	        cv.put(DBtables.Servidor.IN_SERV_codigo_ID, ic.getProperty(6).toString().trim());  
    	    	      
    	    	        
    	    			db.insert(DBtables.Servidor.TAG, null, cv);
    	    			Log.i("Servicio "+i,"ServicioWeb--> "+ic.getProperty(0).toString().trim());
    	    			Log.i("Servicio "+i,"ServidorBD--> "+ic.getProperty(1).toString().trim());
    	    			Log.i("Servicio "+i,"NombreBD--> "+ic.getProperty(2).toString().trim());
    	    			Log.i("Servicio "+i,"UsuarioBD--> "+ic.getProperty(3).toString().trim());
    	    			Log.i("Servicio "+i,"Contrase�aBD--> "+ic.getProperty(4).toString().trim());
    	    			Log.i("Servicio "+i,"Item--> "+ic.getProperty(5).toString().trim());
    	    			Log.i("Servicio "+i,"CodigoID--> "+ic.getProperty(6).toString().trim());
    	       
    	    	 }
    	    	 
    	    	 db.setTransactionSuccessful();
    		  
    	  }
    	  catch(Exception ex){
    		   ex.printStackTrace();
    		   throw new Exception(ex);
    	  }
    	  finally{
    	    	db.endTransaction();
    	    	db.close();
    	  }

    	 Log.i("Servidor", "sincronizado ");
    	
    }catch(Exception e){
    	e.printStackTrace();
    	throw new Exception(e);
    }   	

}


public String getFlagDeposito(String secuencia) throws Exception{
	 
	String SOAP_ACTION= "http://tempuri.org/getFlagDeposito";
	String METHOD_NAME="getFlagDeposito";
	
	String flag="";
	
	SoapObject request=new SoapObject(NAMESPACE, METHOD_NAME);
	request.addProperty("secuencia", secuencia);
	//

    if(GlobalVar.id_servicio == GlobalVar.INTERNET){
		
    	request.addProperty("url", url); 
		request.addProperty("catalog", catalog); 
		request.addProperty("user", user); 
		request.addProperty("password", contrasena);
		
	}
	else if(GlobalVar.id_servicio == GlobalVar.LOCAL){
		
    	request.addProperty("url", url_local); 
		request.addProperty("catalog", catalog_local); 
		request.addProperty("user", user_local); 
		request.addProperty("password", contrasena_local);
		
	}  
	
	//
    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
    Soapenvelope.dotNet=true;
    Soapenvelope.setOutputSoapObject(request);
    
    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);
    
    try{
    	transporte.call(SOAP_ACTION, Soapenvelope);
    	
    	SoapPrimitive result =(SoapPrimitive)Soapenvelope.getResponse();
       	  
    	 flag = result.toString();
          	 
    	 Log.i("MODIFICAR_FLAG_DEPOSITO", ""+flag);
    	
    }catch(Exception e){
    	e.printStackTrace();
    	throw new Exception(e);
    } 
 
    return flag;
}

public String getFlagIngresos(String secuencia, String item) throws Exception{
	 
	String SOAP_ACTION= "http://tempuri.org/getFlagIngresos";
	String METHOD_NAME="getFlagIngresos";
	
	String flag="";
	
	SoapObject request=new SoapObject(NAMESPACE, METHOD_NAME);
	request.addProperty("secuencia", secuencia);
	request.addProperty("item", item);
	//
	if(GlobalVar.id_servicio == GlobalVar.INTERNET){
		
    	request.addProperty("url", url); 
		request.addProperty("catalog", catalog); 
		request.addProperty("user", user); 
		request.addProperty("password", contrasena);
		
	}
	else if(GlobalVar.id_servicio == GlobalVar.LOCAL){
		
    	request.addProperty("url", url_local); 
		request.addProperty("catalog", catalog_local); 
		request.addProperty("user", user_local); 
		request.addProperty("password", contrasena_local);
		
	}
	//
	
    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
    Soapenvelope.dotNet=true;
    Soapenvelope.setOutputSoapObject(request);
    
    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);
    
    try{
    	transporte.call(SOAP_ACTION, Soapenvelope);
    	
    	SoapPrimitive result =(SoapPrimitive)Soapenvelope.getResponse();
    	  	  
    	     flag = result.toString();
    	 
    	 Log.i("MODIFICAR_FLAG_INGRESO", ""+flag);
    	
    }catch(Exception e){
    	e.printStackTrace();
    	throw new Exception(e);
    } 
 
    return flag;
}


public int getEmpresa(String ruc){
	
	String SOAP_ACTION= "http://tempuri.org/getCountRuc";
	String METHOD_NAME="getCountRuc";
	int valor=0;
	SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);     
	Request.addProperty("ruc", ruc); 

    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
    Soapenvelope.dotNet=true;
    Soapenvelope.setOutputSoapObject(Request);
    
    HttpTransportSE transporte=new HttpTransportSE("http://saemoviles.com/Service.asmx");
    
    try{
    	transporte.call(SOAP_ACTION, Soapenvelope);
    	
    	SoapObject result =(SoapObject)Soapenvelope.getResponse();
    	  
    	  dbclass.getReadableDatabase().delete(DBtables.Empresa.TAG, null, null);
    	  SQLiteDatabase db = dbclass.getWritableDatabase();
    	 
    	  
    	 for (int i = 0; i <result.getPropertyCount(); i++)
    	 {
    	        SoapObject ic = (SoapObject)result.getProperty(i);
    	  
    	        ContentValues cv = new ContentValues();
    	        
    	        cv.put(DBtables.Empresa.TX_EMPR_NOMBRE, (ic.getProperty(0).toString().trim())); 
    	        cv.put(DBtables.Empresa.TX_EMPR_RUC, (ic.getProperty(1).toString().trim())); 
    	        cv.put(DBtables.Empresa.TX_EMPR_NOMBREBD, ic.getProperty(2).toString().trim()); 
    	        cv.put(DBtables.Empresa.TX_EMPR_USUARIO, (ic.getProperty(3).toString().trim())); 
    	        cv.put(DBtables.Empresa.TX_EMPR_CONTRASENA, (ic.getProperty(4).toString().trim()));
    	        cv.put(DBtables.Empresa.TX_EMPR_LATITUD, (ic.getProperty(5).toString().trim())); 
    	        cv.put(DBtables.Empresa.TX_EMPR_LONGITUD, (ic.getProperty(6).toString().trim())); 
    	       String empresa = ic.getProperty(0).toString().trim();
    	       if(empresa.equals("error")){
    	    	   valor=0;
    	       }
    	       else{
    	    	   valor=1;
    	       }
    			db.insert(DBtables.Empresa.TAG, null, cv);
               
    	 }
    	
    	 db.close();
    	 Log.i("Empresa **", "sincronizada");
    	 
    }catch(Exception e){
    	e.printStackTrace();
    	 Log.i("Empresa **", "sincronizada:" + e);
    	 valor=0;
    }   		
    return valor;
}

public int loginOnline(String usuario, String clave, String bd,
		String usuario2, String contrasena) {
	// TODO Auto-generated method stub
	
	String SOAP_ACTION= "http://tempuri.org/getConfiguracionxUsuario";
	String METHOD_NAME="getConfiguracionxUsuario";
	int valor=0;
	SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);     
	Request.addProperty("server", "localhost");
	Request.addProperty("bd", bd); 
	Request.addProperty("usuario", usuario); 
	Request.addProperty("clave", clave); 
	Request.addProperty("user", usuario2); 
	Request.addProperty("pass", contrasena); 

    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
    Soapenvelope.dotNet=true;
    Soapenvelope.setOutputSoapObject(Request);
    
    HttpTransportSE transporte=new HttpTransportSE("http://saemoviles.com/Service.asmx");
    
    try{
    	transporte.call(SOAP_ACTION, Soapenvelope);
    	
    	SoapObject result =(SoapObject)Soapenvelope.getResponse();
    	  
    	 dbclass.getReadableDatabase().delete(DBtables.Servidor.TAG, null, null);
   	  SQLiteDatabase db = dbclass.getWritableDatabase();
   
   	    
   	 for (int i = 0; i <result.getPropertyCount(); i++)
   	 {
   	        SoapObject ic = (SoapObject)result.getProperty(i);
   	  
   	        ContentValues cv = new ContentValues();
   	        
   	        cv.put(DBtables.Servidor.TX_SERV_servicioWEB, ic.getProperty(0).toString().trim());  
   	        cv.put(DBtables.Servidor.TX_SERV_servidorBD, ic.getProperty(1).toString().trim());  
   	        cv.put(DBtables.Servidor.TX_SERV_nombreBD, ic.getProperty(2).toString().trim());  
   	        cv.put(DBtables.Servidor.TX_SERV_usuario, ic.getProperty(3).toString().trim());  
   	        cv.put(DBtables.Servidor.TX_SERV_contrasena, ic.getProperty(4).toString().trim());  
   	        cv.put(DBtables.Servidor.IN_SERV_item, ic.getProperty(5).toString().trim());  
   	        cv.put(DBtables.Servidor.IN_SERV_codigo_ID, ic.getProperty(6).toString().trim());  
   	      
   	        
   			db.insert(DBtables.Servidor.TAG, null, cv);
         valor=1;
   	 }
    	
    	 db.close();
    	 Log.i("Servidor **", "sincronizada");
    	 
    }catch(Exception e){
    	e.printStackTrace();
    	 Log.i("Servidor **", "sincronizada:" + e);
    	 valor=0;
    }   	
    
    return valor;
}


public String eliminarIngresoxSecuencia(String secuencia, String secitm) throws Exception{
	 
	String SOAP_ACTION= "http://tempuri.org/eliminarIngresoxSecuencia";
	String METHOD_NAME="eliminarIngresoxSecuencia";
	
	String flag="";
	
	SoapObject request=new SoapObject(NAMESPACE, METHOD_NAME);
	request.addProperty("secuencia", secuencia);
	request.addProperty("secitm", secitm);
	
	//
	if(GlobalVar.id_servicio == GlobalVar.INTERNET){
		
    	request.addProperty("url", url); 
		request.addProperty("catalog", catalog); 
		request.addProperty("user", user); 
		request.addProperty("password", contrasena);
		
	}
	else if(GlobalVar.id_servicio == GlobalVar.LOCAL){
		
    	request.addProperty("url", url_local); 
		request.addProperty("catalog", catalog_local); 
		request.addProperty("user", user_local); 
		request.addProperty("password", contrasena_local);
		
	}
	//
	
    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
    Soapenvelope.dotNet=true;
    Soapenvelope.setOutputSoapObject(request);
    
    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);
    
    try{
    	
    	transporte.call(SOAP_ACTION, Soapenvelope);
    	
    	SoapPrimitive result =(SoapPrimitive)Soapenvelope.getResponse();
    	  
    	flag = result.toString();
        
    	Log.i("ELIMINAR INGRESO", ""+flag);
    	
    }catch(Exception e){
    	e.printStackTrace();
    	throw new Exception(e);
    } 
 
    return flag;
}

public String eliminarPedidoCabeceraxOc(String oc){
	 
	String SOAP_ACTION= "http://tempuri.org/eliminarPedidoCabeceraxOc";
	String METHOD_NAME="eliminarPedidoCabeceraxOc";
	
	String flag="";
	
	SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);
	Request.addProperty("oc", oc);
	//
	Request.addProperty("url", url); 
	Request.addProperty("catalog", catalog); 
	Request.addProperty("user", user); 
	Request.addProperty("password", contrasena);
	//
	
    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
    Soapenvelope.dotNet=true;
    Soapenvelope.setOutputSoapObject(Request);
    
    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);
    
    try{
    	transporte.call(SOAP_ACTION, Soapenvelope);
    	
    	SoapPrimitive result =(SoapPrimitive)Soapenvelope.getResponse();
    	  
    	  //dbclass.getReadableDatabase().delete(DBtables.Ctas_xbanco.TAG, null, null);
    	  //SQLiteDatabase db = dbclass.getWritableDatabase();
    	 
       	  
    	        flag = result.toString();
       
    	 
    	 Log.i("ELIMINAR INGRESO", ""+flag);
    	
    }catch(Exception e){
    	e.printStackTrace();
    	flag="error";
    } 
 
    return flag;
}

public String eliminarDepositosXSecuencia(String secuencia) throws Exception{
	 
	String SOAP_ACTION= "http://tempuri.org/eliminarDepositoxSecuencia";
	String METHOD_NAME="eliminarDepositoxSecuencia";
	
	String resp="";
	
	SoapObject request=new SoapObject(NAMESPACE, METHOD_NAME);
	request.addProperty("secuencia", secuencia);
	
	//
	if(GlobalVar.id_servicio == GlobalVar.INTERNET){
		
    	request.addProperty("url", url); 
		request.addProperty("catalog", catalog); 
		request.addProperty("user", user); 
		request.addProperty("password", contrasena);
		
	}
	else if(GlobalVar.id_servicio == GlobalVar.LOCAL){

    	request.addProperty("url", url_local); 
		request.addProperty("catalog", catalog_local); 
		request.addProperty("user", user_local); 
		request.addProperty("password", contrasena_local);
		
	}
	//
	
    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
    Soapenvelope.dotNet=true;
    Soapenvelope.setOutputSoapObject(request);
    
    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);
    
    try{
    	transporte.call(SOAP_ACTION, Soapenvelope);
    	
    	SoapPrimitive result =(SoapPrimitive)Soapenvelope.getResponse();

    	resp = result.toString();
       
        Log.i("ELIMINAR DEPOSITO", ""+resp);
    	
    }catch(Exception e){
    	e.printStackTrace();
    	throw new Exception(e);
    } 
 
    return resp;
}

public void Sync_tabla_vendedoresOnline(String url, String catalog, String user, String contrasena){
	
	String SOAP_ACTION= "http://tempuri.org/obtenerVendedores";
	String METHOD_NAME="obtenerVendedores";
	
	SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);   
	Request.addProperty("url", url); 
	Request.addProperty("catalog", catalog); 
	Request.addProperty("user", user); 
	Request.addProperty("password", contrasena); 
    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
    Soapenvelope.dotNet=true;
    Soapenvelope.setOutputSoapObject(Request);
    
    HttpTransportSE transporte=new HttpTransportSE(URL+"saemoviles.com/Service.asmx");
    
    try{
    	transporte.call(SOAP_ACTION, Soapenvelope);
    	
    	SoapObject result =(SoapObject)Soapenvelope.getResponse();
    	  
    	  dbclass.getReadableDatabase().delete(DBtables.Vendedor.TAG, null, null);
    	  SQLiteDatabase db = dbclass.getWritableDatabase();
    	 
    	  
    	 for (int i = 0; i <result.getPropertyCount(); i++)
    	 {
    	        SoapObject ic = (SoapObject)result.getProperty(i);
    	  
    	        ContentValues cv = new ContentValues();
    	        
    	        cv.put(DBtables.Vendedor.CODVEN, ic.getProperty(0).toString().trim());
    			cv.put(DBtables.Vendedor.NOMVEN, ic.getProperty(1).toString().trim());
    			cv.put(DBtables.Vendedor.FK_CODUSER, ic.getProperty(2).toString().trim());
    			cv.put(DBtables.Vendedor.FLG_MODIFICAPRECIO, ic.getProperty(3).toString().trim());
    	        
    			db.insert(DBtables.Vendedor.TAG, null, cv);
	       
    	 }
    	 
    	 db.close();
    	 Log.i("Vendedores", "sincronizada");
    	
    }catch(Exception e){
    	e.printStackTrace();
    }   		
}

public void Sync_tabla_usuarios_Online(String url, String catalog, String user, String contrasena){
	
	String SOAP_ACTION= "http://tempuri.org/obtenerUsuarios";
	String METHOD_NAME="obtenerUsuarios";
	
	SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME); 
	Request.addProperty("url", url); 
	Request.addProperty("catalog", catalog); 
	Request.addProperty("user", user); 
	Request.addProperty("password", contrasena); 
    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
    Soapenvelope.dotNet=true;
    Soapenvelope.setOutputSoapObject(Request);
    
    
    HttpTransportSE transporte=new HttpTransportSE(URL+"saemoviles.com/Service.asmx");
    
    try{
    	transporte.call(SOAP_ACTION, Soapenvelope);
    	SoapObject  result =(SoapObject)Soapenvelope.getResponse();
    	 
    	  
    	  dbclass.getReadableDatabase().delete(DBtables.Usuarios.TAG, null, null);
    	  SQLiteDatabase db = dbclass.getWritableDatabase();
    	  
    	 for (int i = 0; i <result.getPropertyCount(); i++)
    	 {
    	
    	        SoapObject ic = (SoapObject)result.getProperty(i);
 
    	        ContentValues cv = new ContentValues();
    	       
    	        cv.put(DBtables.Usuarios.PK_USECOD, Integer.parseInt(ic.getProperty(0).toString()));
    			cv.put(DBtables.Usuarios.USEPAS, ic.getProperty(1).toString().trim());
    			cv.put(DBtables.Usuarios.USENAM, ic.getProperty(2).toString().trim());
    			cv.put(DBtables.Usuarios.USEUSR, ic.getProperty(3).toString().trim());
    			cv.put(DBtables.Usuarios.USESGL, ic.getProperty(4).toString().trim());
    			cv.put(DBtables.Usuarios.CODIGOC2DM, ic.getProperty(5).toString().trim());
    			
    			db.insert(DBtables.Usuarios.TAG, null, cv);
    	        	    	     	       
    	 }
    	 
    	 db.close();
    	 Log.i("Usuarios", "sincronizada");
    	
    }catch(Exception e){
    	e.printStackTrace();
    }   
  }


public int actualizarObjPedido() throws Exception{
	
   	String SOAP_ACTION= "http://tempuri.org/actualizarObjPedido_json";
	String METHOD_NAME="actualizarObjPedido_json";
	
	ArrayList<DB_ObjPedido>  lista_obj_pedido= new ArrayList<DB_ObjPedido>();
    lista_obj_pedido = dbclass.getTodosObjPedido_json_flagp();
    
    if(lista_obj_pedido.size() == 0){
    	return  0;
    }
    
    for(int i=0; i< lista_obj_pedido.size(); i++){
    	//Seteo del detalle del pedido por el oc_numero
    	ArrayList<DBPedido_Detalle> detalles = new ArrayList<DBPedido_Detalle>();
    	detalles = dbclass.getPedido_detalles(lista_obj_pedido.get(i).getOc_numero());    	
    	lista_obj_pedido.get(i).setDetalles(detalles);
    	
    	//Seteo del registro de bonificaciones por el oc_numero
    	registroBonificaciones = new ArrayList<DB_RegistroBonificaciones>();
    	registroBonificaciones = DAORegistroBonificaciones.getRegistroBonificaciones(lista_obj_pedido.get(i).getOc_numero());
    	   
    	
    	
    	ArrayList<DB_RegistroBonificaciones> pendientesUsados = new ArrayList<DB_RegistroBonificaciones>();
    	for (int j = 0; j < registroBonificaciones.size(); j++) {
    		DB_RegistroBonificaciones registroUsado = DAORegistroBonificaciones.getRegistroBonificacion(registroBonificaciones.get(j).getCodigoAnterior());
    		if (registroUsado.getOc_numero()!=null) {//Si se uso algun pendiente
    			boolean isRegistrado = false;
    			for (int k = 0; k < registroBonificaciones.size(); k++) {//Verifico que ese registro ya no este agregado
    				if (registroBonificaciones.get(k).getCodigoRegistro().equals(registroUsado.getCodigoRegistro())) {
    					isRegistrado = true;
    				}
    			}
    			if (isRegistrado==false) {
    				Log.e(TAG, "Agregando a registroBonificaciones el pendiente Usado: "+registroUsado.getCodigoRegistro());
					pendientesUsados.add(registroUsado);					
				}
			}    		 
		}
    	
    	Log.e(TAG, "registro bonificaciones size: "+registroBonificaciones.size());
    	if (!pendientesUsados.isEmpty()) {
    		registroBonificaciones.addAll(pendientesUsados);
    		Log.e(TAG, "registro bonificaciones con pendientes size: "+registroBonificaciones.size());
		}
    	
    	lista_obj_pedido.get(i).setRegistroBonificaciones(registroBonificaciones);
		ArrayList<San_Visitas> listaVisitas= new ArrayList<>();//DAO_San_Visitas.getSan_VisitasByOc_numero(dbclass, lista_obj_pedido.get(i).getOc_numero());
		lista_obj_pedido.get(i).setSan_visitas(listaVisitas);
    }
    
    Gson gson = new Gson();
    String cadena = gson.toJson(lista_obj_pedido);
    
    Log.i("ENVIO PENDIENTES","JSON: "+cadena.toString());
    //Log.v("ENVIO PENDIENTES",""+cadena.length());
    
        	
    	SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
    	

		request.addProperty("cadena",cadena);
		request.addProperty("serv", url);
		request.addProperty("catalog", catalog);
		request.addProperty("user", user);
		request.addProperty("password", contrasena);
    		

	    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    Soapenvelope.dotNet=true;
	    Soapenvelope.setOutputSoapObject(request);
	    
	    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);

	    long beforecall = System.currentTimeMillis();
	    
	    try{
	    	transporte.call(SOAP_ACTION, Soapenvelope);
	    	Log.i("ENVIO PEDIDOS PENDIENTES","RESPUESTA EN: "+(System.currentTimeMillis()-beforecall)+"miliseg");
	    	
	    	SoapPrimitive resultado_xml =(SoapPrimitive)Soapenvelope.getResponse();
			String res = resultado_xml.toString();
			
			Log.i("ENVIO PEDIDOS PENDIENTES","Respuesta: "+res);
			
			JSONArray jsonstring = new JSONArray(res);
			dbclass.guardar_respuesta_objpedido_flag(jsonstring);
			
	    }
	    catch(JSONException ex){
	    	Log.w("SYNC MANAGER", "actualizarObjPedido",ex);
	    	throw new JsonParseException(ex.getMessage());
	    	//throw new JSONException(ex.getMessage());
	    }
	    catch(Exception ex){
	    	Log.w("SYNC MANAGER", "actualizarObjPedido",ex);
	    	throw new Exception(ex);
	    }
	
	    return 1;
}

public String actualizarObjPedido_directo(String Oc_numero) throws Exception{
   	
   	String SOAP_ACTION= "http://tempuri.org/actualizarObjPedido_json";
	String METHOD_NAME="actualizarObjPedido_json";
	
	String flag = "";
	
	ArrayList<DB_ObjPedido>  lista_obj_pedido= new ArrayList<DB_ObjPedido>();
    lista_obj_pedido = dbclass.getObjPedido_jsons(Oc_numero);
    
    for(int i=0; i< lista_obj_pedido.size(); i++){
    	//Seteo del detalle del pedido por el oc_numero
    	ArrayList<DBPedido_Detalle> detalles = new ArrayList<DBPedido_Detalle>();
    	detalles = dbclass.getPedido_detalles(lista_obj_pedido.get(i).getOc_numero());    	
    	lista_obj_pedido.get(i).setDetalles(detalles);
    	
    	//Seteo del registro de bonificaciones por el oc_numero
    	ArrayList<DB_RegistroBonificaciones> registroBonificaciones = new ArrayList<>();
    	registroBonificaciones = DAORegistroBonificaciones.getRegistroBonificaciones(lista_obj_pedido.get(i).getOc_numero());
    	//Se deben agregar los regisros pendientes que han sido usados para que sean actualizados
    	
    	ArrayList<DB_RegistroBonificaciones> pendientesUsados = new ArrayList<DB_RegistroBonificaciones>();
    	for (int j = 0; j < registroBonificaciones.size(); j++) {
    		DB_RegistroBonificaciones registroUsado = DAORegistroBonificaciones.getRegistroBonificacion(registroBonificaciones.get(j).getCodigoAnterior());
    		if (registroUsado.getOc_numero()!=null) {//Si se uso algun pendiente
    			boolean isRegistrado = false;
    			for (int k = 0; k < registroBonificaciones.size(); k++) {//Verifico que ese registro ya no este agregado
    				if (registroBonificaciones.get(k).getCodigoRegistro().equals(registroUsado.getCodigoRegistro())) {
    					isRegistrado = true;
    				}
    			}
    			if (isRegistrado==false) {
    				Log.e(TAG, "Agregando a registroBonificaciones el pendiente Usado: "+registroUsado.getCodigoRegistro());
					pendientesUsados.add(registroUsado);					
				}
			}
		}
    	
    	Log.e(TAG, "registro bonificaciones size: "+registroBonificaciones.size());
    	if (!pendientesUsados.isEmpty()) {
    		registroBonificaciones.addAll(pendientesUsados);
    		Log.e(TAG, "registro bonificaciones con pendientes size: "+registroBonificaciones.size());
		}
    	
    	lista_obj_pedido.get(i).setRegistroBonificaciones(registroBonificaciones);
        ArrayList<San_Visitas> listaVisitas= DAO_San_Visitas.getSan_VisitasByOc_numero(dbclass, lista_obj_pedido.get(i).getOc_numero());
        lista_obj_pedido.get(i).setSan_visitas(listaVisitas);

    }
    
    Gson gson = new Gson();
    String cadena = gson.toJson(lista_obj_pedido);
    
    Log.i("ENVIO PEDIDO","JSON: "+cadena.toString());
    
    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
    	
    	
    	if(GlobalVar.id_servicio == GlobalVar.INTERNET){
    		
    		request.addProperty("cadena",cadena);
        	request.addProperty("serv", url);
    		request.addProperty("catalog", catalog); 
    		request.addProperty("user", user); 
    		request.addProperty("password", contrasena);
    		
    	}
    	else if(GlobalVar.id_servicio == GlobalVar.LOCAL){
    		
    		request.addProperty("cadena",cadena);
        	request.addProperty("serv", url_local);
    		request.addProperty("catalog", catalog_local); 
    		request.addProperty("user", user_local); 
    		request.addProperty("password", contrasena_local);
    		
    	}
    	
		
	    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    Soapenvelope.dotNet=true;
	    Soapenvelope.setOutputSoapObject(request);
	    
	    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);

	    long beforecall = System.currentTimeMillis();
	    
	    try{
	    	transporte.call(SOAP_ACTION, Soapenvelope);
	    	Log.i("ENVIO PEDIDO","RESPUESTA EN: "+(System.currentTimeMillis()-beforecall)+"miliseg");
	    	
	    	SoapPrimitive resultado_xml =(SoapPrimitive)Soapenvelope.getResponse();
			String res = resultado_xml.toString();
	    	
			Log.i("ENVIO PEDIDO","Respuesta: "+res);
			
			JSONArray jsonstring = new JSONArray(res);
			flag = dbclass.guardar_respuesta_objpedido_flag(jsonstring);
			
	    }
	    catch(JSONException ex){
	    	ex.printStackTrace();
	    	throw new JsonParseException(ex.getMessage());
	    }
	    catch(Exception ex){
	    	ex.printStackTrace();
	    	throw new Exception(ex);
	    }
	    
	    
	    actualizarDetallePromocion(Oc_numero);
	    actualizarDetalleEntrega(Oc_numero);
	    
	return flag; 
}

	public String actualizarObjPedido_San_visitas(DB_ObjPedido pedido_Cabecera) {



		String S_TAG="actualizarObjPedido_San_visitas:: ";
		String SOAP_ACTION = "http://tempuri.org/GuardarSanVisitas_json";
		String METHOD_NAME = "GuardarSanVisitas_json";


		Gson gson = new Gson();
		ArrayList<DB_ObjPedido> lista=new ArrayList<>();
		lista.add(pedido_Cabecera);
		String cadena = gson.toJson(lista);

		Log.i(TAG, S_TAG+"ENVIO PEDIDO"+ " JSON: " + cadena.toString());

		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
		try {


			request.addProperty("cadena", cadena);
			request.addProperty("url", url);
			request.addProperty("catalog", catalog);
			request.addProperty("user", user);
			request.addProperty("password", contrasena);


			SoapSerializationEnvelope Soapenvelope = new SoapSerializationEnvelope( SoapEnvelope.VER11);
			Soapenvelope.dotNet = true;
			Soapenvelope.setOutputSoapObject(request);

			HttpTransportSE transporte = new HttpTransportSE(URL + GlobalVar.urlService);

			long beforecall = System.currentTimeMillis();


			transporte.call(SOAP_ACTION, Soapenvelope);
			Log.i(TAG, S_TAG+"ENVIO PEDIDO "+  "RESPUESTA EN: " + (System.currentTimeMillis() - beforecall) + "miliseg");

			SoapPrimitive resultado_xml = (SoapPrimitive) Soapenvelope .getResponse();
			String res = resultado_xml.toString();

			Log.i(TAG, S_TAG+"ENVIO PEDIDO "+ "Respuesta: " + res);


			return res;
		} catch (Exception ex) {
			ex.printStackTrace();
			return "error@"+ ex.getMessage();
		}
	}

//ENVIANDO DETALLE ENTREGA AL SERVIDOR
public String actualizarDetalleEntrega(String oc_numero) throws Exception {

	
			String SOAP_ACTION= "http://tempuri.org/actualizarDetalleEntrega_json";
			String METHOD_NAME="actualizarDetalleEntrega_json";
			   ArrayList<DB_Detalle_Entrega>  lista_detalle= new ArrayList<DB_Detalle_Entrega>();
				lista_detalle= dbclass.obtenerListadoDetalleEntregaTotal(oc_numero);
				
				if(lista_detalle.size()>0){
				Iterator<DB_Detalle_Entrega> it=lista_detalle.iterator();
				  				 
					  Object objeto = it.next(); 
					  DB_Detalle_Entrega cta = (DB_Detalle_Entrega)objeto;
					  
						PropertyInfo pi = new PropertyInfo();
						pi.setName("db_detalle_entrega");
						pi.setValue(cta);
						pi.setType(cta.getClass());
					  
						SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
						
						Gson gson=new Gson();
						String cadena=gson.toJson(lista_detalle);
						Log.w("LISTA DETALLE:",lista_detalle.toString());
						Log.w("CADENA",cadena);
						
						//
						request.addProperty("cadena", cadena); 
						request.addProperty("url", url); 
						request.addProperty("catalog", catalog); 
						request.addProperty("user", user); 
						request.addProperty("password", contrasena); 
						//
						
						SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
						envelope.dotNet = true; 
						envelope.setOutputSoapObject(request);
						envelope.addMapping(NAMESPACE, "DB_Detalle_Entrega", cta.getClass());

						HttpTransportSE transporte = new HttpTransportSE(URL+GlobalVar.urlService);
						transporte.call(SOAP_ACTION, envelope);
						try 
						{
						SoapPrimitive resultado_xml =(SoapPrimitive)envelope.getResponse();
						String res = resultado_xml.toString();
						
						if(res.equals("1"))
						{
							 Log.i("DETALLE ENTREGA", "datos subidos al servidor");
						}
						else
						{
							Log.i("DETALLE ENTREGA", "Error al actualizar datos"); 
						}
		
		} 
		catch (Exception e) 
		{
			Log.i("DETALLE ENTREGA", "Error al ENVIAR DATOS AL SERVIDOR"); 
			e.printStackTrace();
		} 	}
		
	return "1";
}

//ENVIANDO DETALLE PROMOCION AL SERVIDOR
public String actualizarDetallePromocion(String oc_numero) throws Exception {

	
			String SOAP_ACTION= "http://tempuri.org/actualizarDetallePromocion_json";
			String METHOD_NAME="actualizarDetallePromocion_json";
			   ArrayList<DB_Pedido_Detalle_Promocion>  lista_promo= new ArrayList<DB_Pedido_Detalle_Promocion>();
				lista_promo= dbclass.obtenerListadoDetallePromocionTotal(oc_numero);
				
				if(lista_promo.size()>0){
				Iterator<DB_Pedido_Detalle_Promocion> it=lista_promo.iterator();
				  				 
					  Object objeto = it.next(); 
					  DB_Pedido_Detalle_Promocion cta = (DB_Pedido_Detalle_Promocion)objeto;
					  
						PropertyInfo pi = new PropertyInfo();
						pi.setName("db_detalle_promo");
						pi.setValue(cta);
						pi.setType(cta.getClass());
					  
						SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
						
						Gson gson=new Gson();
						String cadena=gson.toJson(lista_promo);
						//Log.w("LISTA PROMOCION:",lista_promo.toString());
						Log.w("CADENA",cadena);
						
						//
						request.addProperty("cadena", cadena); 
						request.addProperty("url", url); 
						request.addProperty("catalog", catalog); 
						request.addProperty("user", user); 
						request.addProperty("password", contrasena); 
						//
						
						SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
						envelope.dotNet = true; 
						envelope.setOutputSoapObject(request);
						envelope.addMapping(NAMESPACE, "DB_Pedido_Detalle_Promocion", cta.getClass());

						HttpTransportSE transporte = new HttpTransportSE(URL+GlobalVar.urlService);
						transporte.call(SOAP_ACTION, envelope);
						try 
						{
						SoapPrimitive resultado_xml =(SoapPrimitive)envelope.getResponse();
						String res = resultado_xml.toString();
						
						if(res.equals("1"))
						{
							 Log.i("DETALLE PROMOCION", "datos subidos al servidor");
						}
						else
						{
							Log.i("DETALLE PROMOCION", "Error al actualizar datos"); 
						}
		
		} 
		catch (Exception e) 
		{
			Log.i("DETALLE PROMOCION", "Error al ENVIAR DATOS AL SERVIDOR"); 
			e.printStackTrace();
		} 	
				}
	return "1";
}

public void Sync_tabla_ObjPedido(String codven, String url, String catalog, String user, String contrasena) throws Exception{
	
	String SOAP_ACTION= "http://tempuri.org/obtenerObjpedido_json";
	String METHOD_NAME="obtenerObjpedido_json";
	long beforecall;
	
	SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);
	Request.addProperty("codven", codven);
	Request.addProperty("url", url); 
	Request.addProperty("catalog", catalog); 
	Request.addProperty("user", user); 
	Request.addProperty("password", contrasena); 
    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
    Soapenvelope.dotNet=true;
    Soapenvelope.setOutputSoapObject(Request);
    
    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);
    
    beforecall = System.currentTimeMillis();
    
    try{
    	transporte.call(SOAP_ACTION, Soapenvelope);
    	
    	Log.d("OBJ_PEDIDO","RESPUESTA EN: "+(System.currentTimeMillis()-beforecall)+"miliseg");
    	
    	  SoapPrimitive result = (SoapPrimitive)Soapenvelope.getResponse();  
    	  Log.d("SYNC_OBJ_PEDIDO",""+result.toString());
    	  JSONArray jsonstring = new JSONArray(result.toString());
    	  Log.d("OBJ_PEDIDO","Registros: "+jsonstring.length());
    	  Log.d("OBJ_PEDIDO","Registros: "+result.toString());
    	  dbclass.syncObjPedido(jsonstring,codven);

    	 Log.d("OBJ_PEDIDO", "SINCRONIZADA");
    	
    }catch(Exception e){
    	 e.printStackTrace();
    	 Log.e("OBJ_PEDIDO", "NO SINCRONIZADA");
    	 throw new Exception(e);
    }

}


public void actualizarLogSincro(String codven, String fec_ini, String fec_device, String descp) throws Exception{   	
   	
	String SOAP_ACTION= "http://tempuri.org/actualizar_LogSincro";
	String METHOD_NAME="actualizar_LogSincro";
	
	DB_LogSincro sincro = new DB_LogSincro();	
	sincro.setCodven(codven);
	sincro.setFec_ini(fec_ini);
	sincro.setFec_device(fec_device);
	sincro.setDescp(descp);
	
    Gson gson = new Gson();
    String cadena = gson.toJson(sincro);
    
    Log.i("ENVIO LOG_SINCRO","JSON: "+cadena.toString());    	
    	SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);    	
    	
    	if(GlobalVar.id_servicio == GlobalVar.INTERNET){    		
    		request.addProperty("cadena",cadena);
        	request.addProperty("server", url); 
    		request.addProperty("database", catalog); 
    		request.addProperty("uid", user); 
    		request.addProperty("password", contrasena);
    		Log.i("ENVIO LOG_SINCRO","TOMANDO DATOS DE SERVIDOR PRINCIPAL, server: "+url);    		
    	}else if(GlobalVar.id_servicio == GlobalVar.LOCAL){    		
    		request.addProperty("cadena",cadena);
        	request.addProperty("server", url_local); 
    		request.addProperty("database", catalog_local); 
    		request.addProperty("uid", user_local); 
    		request.addProperty("password", contrasena_local);
    		Log.i("ENVIO LOG_SINCRO","TOMANDO DATOS DE SERVIDOR PRINCIPAL, server: "+url_local);    		
    	}
		
	    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    Soapenvelope.dotNet=true;
	    Soapenvelope.setOutputSoapObject(request);
	    
	    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);
	    Log.i("ENVIO LOG_SINCRO","ENVIANDO A: "+GlobalVar.urlService);

	    long beforecall = System.currentTimeMillis();
	    
	    try{
	    	transporte.call(SOAP_ACTION, Soapenvelope);
	    	Log.i("ENVIO LOG_SINCRO","RESPUESTA EN: "+(System.currentTimeMillis()-beforecall)+"miliseg");	    	
	    	SoapPrimitive resultado_xml =(SoapPrimitive)Soapenvelope.getResponse();
			int res = Integer.parseInt(resultado_xml.toString());	    	
			Log.i("ENVIO LOG_SINCRO","Respuesta: "+res);			
	    }
	    catch(Exception ex){
	    	ex.printStackTrace();
	    	throw new Exception(ex);
	    }	    
}


public String obtenerHoraServBD(String url, String catalog, String user, String contrasena) throws Exception{
	
	String SOAP_ACTION= "http://tempuri.org/obtener_hora_servbd";
	String METHOD_NAME="obtener_hora_servbd";
	long beforecall;
	
	String datetime = "";
	
	SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);		
	Request.addProperty("url", url); 
	Request.addProperty("catalog", catalog); 
	Request.addProperty("user", user); 
	Request.addProperty("password", contrasena);
    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
    Soapenvelope.dotNet=true;
    Soapenvelope.setOutputSoapObject(Request);
    
    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);
    
    beforecall = System.currentTimeMillis();
    
    try{
    	transporte.call(SOAP_ACTION, Soapenvelope);
    	
    	Log.i("OBTENER_HORA_SERVBD","RESPUESTA EN: "+(System.currentTimeMillis()-beforecall)+"miliseg");
    	
    	  SoapPrimitive result = (SoapPrimitive)Soapenvelope.getResponse();  
    	  datetime = result.toString();
    	  return datetime;
    	
    }catch(Exception e){
    	 e.printStackTrace();
    	 Log.e("OBTENER_HORA_SERVBD", "NO SINCRONIZADA");
    	 throw new Exception(e);
    }
    
    
}


public int enviarPosicionVendedor(DB_PosicionVendedor posVendedor) throws Exception{
	
   	String SOAP_ACTION= "http://tempuri.org/guardarIntervalosPosicionVendedor";
	String METHOD_NAME="guardarIntervalosPosicionVendedor";
	
	String transport = "";
	
	//String SOAP_ACTION= "http://tempuri.org/Probar_conexion_sistema";
	//String METHOD_NAME="Probar_conexion_sistema";
    
    Gson gson = new Gson();
    String cadena = gson.toJson(posVendedor);
    
    Log.i("ENVIAR POSICION VENDEDOR JSON",""+cadena);
    
    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
    	
    	if(chk_principal_estado){
    		
    		request.addProperty("cadena",cadena);
        	request.addProperty("server", url); 
    		request.addProperty("database", catalog); 
    		request.addProperty("uid", user); 
    		request.addProperty("password", contrasena);
    		
    		//transport = URL+GlobalVar.direccion_servicio;
    		transport = URL+servicio;
    		
    		Log.i("PARAMETROS",""+url+"-"+catalog+"-"+user+"-"+contrasena+"-"+transport);
    		//Log.i("PARAMETROS",""+servicio);
    		//Log.i("PARAMETROS","internet? "+GlobalVar.id_servicio);
    		
    	}
    	else{
    		
    		request.addProperty("cadena",cadena);
        	request.addProperty("server", url_local); 
    		request.addProperty("database", catalog_local); 
    		request.addProperty("uid", user_local); 
    		request.addProperty("password", contrasena_local);
    		
    		transport = URL+servicio_local;
    		
    		Log.i("PARAMETROS",""+url_local+"-"+catalog_local+"-"+user_local+"-"+contrasena_local+"-"+transport);
    		//Log.i("PARAMETROS",""+servicio_local);
    		//Log.i("PARAMETROS","internet? "+GlobalVar.id_servicio);
    		
    	}
		
	    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    Soapenvelope.dotNet=true;
	    Soapenvelope.setOutputSoapObject(request);
	    
	    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);

	    long beforecall = System.currentTimeMillis();
	    
	    try{
	    	transporte.call(SOAP_ACTION, Soapenvelope);
	    	Log.i("ENVIO POSICION VENDEDOR","RESPUESTA EN: "+(System.currentTimeMillis()-beforecall)+"miliseg");
	    	
	    	SoapPrimitive resultado_xml =(SoapPrimitive)Soapenvelope.getResponse();
			String res = resultado_xml.toString();
			
			Log.i("ENVIO POSICION VENDEDOR","Respuesta: "+res);			
			
	    }
	    catch(Exception ex){
	    	Log.w("SYNC SOAP MANAGER", "POSICION VENDEDOR"+ex);
	    	throw new Exception(ex);
	    }
	
	    return 1;
}


public String obtenerListaInventariosProgramados() throws Exception{
	
   	String SOAP_ACTION= "http://tempuri.org/obtener_lista_inventarios_programados";
	String METHOD_NAME="obtener_lista_inventarios_programados";
	
	String resultado = "";
	String transport = "";
	
	Log.i("obtenerListaIventariosProgramados","inicio");
	
    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
    	
    	if(chk_principal_estado){
    		
        	request.addProperty("server", url); 
    		request.addProperty("database", "kadosh_ii"); 
    		request.addProperty("uid", user); 
    		request.addProperty("password", contrasena);
    		
    		//transport = URL+GlobalVar.direccion_servicio;
    		transport = URL+servicio;
    		
    		Log.i("PARAMETROS",""+url+"-"+"kadosh_ii"+"-"+user+"-"+contrasena+"-"+transport);
    		//Log.i("PARAMETROS",""+servicio);
    		//Log.i("PARAMETROS","internet? "+GlobalVar.id_servicio);
    		
    	}
    	else{
    		
        	request.addProperty("server", url_local); 
    		request.addProperty("database", "kadosh_ii"); 
    		request.addProperty("uid", user_local); 
    		request.addProperty("password", contrasena_local);
    		
    		transport = URL+servicio_local;
    		
    		Log.i("PARAMETROS",""+url_local+"-"+"kadosh_ii"+"-"+user_local+"-"+contrasena_local+"-"+transport);
    		
    	}
		
	    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    Soapenvelope.dotNet=true;
	    Soapenvelope.setOutputSoapObject(request);
	    
	    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);

	    long beforecall = System.currentTimeMillis();
	    
	    try{
	    	transporte.call(SOAP_ACTION, Soapenvelope);
	    	Log.i("LISTA INVENTARIOS PROGRAMADOS","RESPUESTA EN: "+(System.currentTimeMillis()-beforecall)+"miliseg");
	    	
	    	SoapPrimitive result =(SoapPrimitive)Soapenvelope.getResponse();
	    	//JSONArray jsonstring = new JSONArray(result.toString());
	    	resultado = result.toString();
			
			Log.i("LISTA INVENTARIOS PROGRAMADOS","Respuesta: "+resultado);
			
			
			
	    }
	    catch(Exception ex){
	    	Log.w("SYNC SOAP MANAGER", "LISTA INVENTARIOS PROGRAMADOS "+ex);
	    	throw new Exception(ex);
	    }
	
	    return resultado;
}

public int obtenerListaAlmacenes() throws Exception{
	
   	String SOAP_ACTION= "http://tempuri.org/obtener_lista_almacenes";
	String METHOD_NAME="obtener_lista_almacenes";
	
	int resultado = 1;
	String transport = "";
	
	Log.i("obtener_lista_almacenes","inicio");
	
    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
    	
    	if(chk_principal_estado){
    		
    		request.addProperty("server", url); 
    		request.addProperty("database", catalog); 
    		request.addProperty("uid", user); 
    		request.addProperty("password", contrasena);
    		
    		//transport = URL+GlobalVar.direccion_servicio;
    		transport = URL+servicio;
    		
    		Log.i("PARAMETROS",""+url+"-"+"kadosh_ii"+"-"+user+"-"+contrasena+"-"+transport);
    		//Log.i("PARAMETROS",""+servicio);
    		//Log.i("PARAMETROS","internet? "+GlobalVar.id_servicio);
    		
    	}
    	else{
    		
        	request.addProperty("server", url_local); 
    		request.addProperty("database", "kadosh_ii"); 
    		request.addProperty("uid", user_local); 
    		request.addProperty("password", contrasena_local);
    		
    		transport = URL+servicio_local;
    		
    		Log.i("PARAMETROS",""+url_local+"-"+"kadosh_ii"+"-"+user_local+"-"+contrasena_local+"-"+transport);
    		
    	}
		
	    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    Soapenvelope.dotNet=true;
	    Soapenvelope.setOutputSoapObject(request);
	    
	    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);

	    long beforecall = System.currentTimeMillis();
	    
	    try{
	    	transporte.call(SOAP_ACTION, Soapenvelope);
	    	Log.i("LISTA ALMACENES","RESPUESTA EN: "+(System.currentTimeMillis()-beforecall)+"miliseg");
	    	
	    	SoapPrimitive result =(SoapPrimitive)Soapenvelope.getResponse();
	    	JSONArray jsonstring = new JSONArray(result.toString());
			
			Log.i("LISTA ALMACENES","Respuesta: "+resultado);
			
			dbclass.sync_almacenes(jsonstring);

	    }
	    catch(Exception ex){
	    	Log.w("SYNC SOAP MANAGER", "LISTA ALMACENES "+ex);
	    	throw new Exception(ex);
	    }
	
	    return resultado;
}

public int obtenerListaDistribucionAlmacenes() throws Exception{
	
   	String SOAP_ACTION= "http://tempuri.org/obtener_lista_distribucionAlmacen";
	String METHOD_NAME="obtener_lista_distribucionAlmacen";
	
	int resultado = 1;
	String transport = "";
	
	Log.i("obtener_lista_distribucionAlmacen","inicio");
	
    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
    	
    	if(chk_principal_estado){
    		
        	request.addProperty("server", url); 
    		//request.addProperty("database", "kadosh_ii"); 
    		//request.addProperty("uid", user); 
    		//request.addProperty("password", contrasena);
    		
    		request.addProperty("database", catalog); 
    		request.addProperty("uid", user); 
    		request.addProperty("password", contrasena);
    		
    		//transport = URL+GlobalVar.direccion_servicio;
    		transport = URL+servicio;
    		
    		Log.i("PARAMETROS",""+url+"-"+"kadosh_ii"+"-"+user+"-"+contrasena+"-"+transport);
    		//Log.i("PARAMETROS",""+servicio);
    		//Log.i("PARAMETROS","internet? "+GlobalVar.id_servicio);
    		
    	}
    	else{
    		
        	request.addProperty("server", url_local); 
    		request.addProperty("database", "kadosh_ii"); 
    		request.addProperty("uid", user_local); 
    		request.addProperty("password", contrasena_local);
    		
    		transport = URL+servicio_local;
    		
    		Log.i("PARAMETROS",""+url_local+"-"+"kadosh_ii"+"-"+user_local+"-"+contrasena_local+"-"+transport);
    		
    	}
		
	    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    Soapenvelope.dotNet=true;
	    Soapenvelope.setOutputSoapObject(request);
	    
	    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);

	    long beforecall = System.currentTimeMillis();
	    
	    try{
	    	transporte.call(SOAP_ACTION, Soapenvelope);
	    	Log.i("LISTA DISTRIBUCION ALMACENES","RESPUESTA EN: "+(System.currentTimeMillis()-beforecall)+"miliseg");
	    	
	    	SoapPrimitive result =(SoapPrimitive)Soapenvelope.getResponse();
	    	JSONArray jsonstring = new JSONArray(result.toString());	    	
			
			Log.i("LISTA DISTRIBUCION ALMACENES","Respuesta: "+resultado);
			
			dbclass.sync_distribucion_almacenes(jsonstring);
	
	    }
	    catch(Exception ex){
	    	Log.w("SYNC SOAP MANAGER", "LISTA DISTRIBUCION ALMACENES "+ex);
	    	throw new Exception(ex);
	    }
	
	    return resultado;
}


public void obtenerStockActualizado(String codven, String kardex, String codalm) throws Exception{

	String SOAP_ACTION= "http://tempuri.org/actualizar_StockMtaKardex";
	String METHOD_NAME= "actualizar_StockMtaKardex";
	
	SoapObject request=new SoapObject(NAMESPACE, METHOD_NAME);
	
	request.addProperty("codven", codven);
	request.addProperty("kardex", kardex);
	request.addProperty("codalm", codalm);
	
	Log.i("obtenerStockActualizado",""+codven+"-"+kardex+"-"+codalm);
	
	if(GlobalVar.id_servicio == GlobalVar.INTERNET){
		
    	request.addProperty("serv", url); 
		request.addProperty("catalog", catalog); 
		request.addProperty("uid", user); 
		request.addProperty("password", contrasena);
		
		Log.i("obtenerStockActualizado",""+url+"-"+catalog+"-"+user+"-"+contrasena);
		
	}
	else if(GlobalVar.id_servicio == GlobalVar.LOCAL){
		
    	request.addProperty("serv", url_local); 
		request.addProperty("catalog", catalog_local); 
		request.addProperty("uid", user_local); 
		request.addProperty("password", contrasena_local);
		
		Log.i("obtenerStockActualizado",""+url_local+"-"+catalog_local+"-"+user_local+"-"+contrasena_local);
		
	}
	
	
    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
    Soapenvelope.dotNet=true;
    Soapenvelope.setOutputSoapObject(request);
    
    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);
    
    long beforecall = System.currentTimeMillis();
    
    try{
    	transporte.call(SOAP_ACTION, Soapenvelope);
    	Log.i("obtenerStockActualizado","RESPUESTA EN: "+(System.currentTimeMillis()-beforecall)+"miliseg");

    	 SoapPrimitive result = (SoapPrimitive)Soapenvelope.getResponse();   

    	 JSONArray jsonstring = new JSONArray(result.toString());
    	 Log.i("obtenerStockActualizado","Registros: "+jsonstring.length());
    	 
    	 dbclass.actualizarStock(jsonstring);
    	
    }catch(Exception e){
    	e.printStackTrace();
    	Log.i("obtenerStockActualizado", "NO SINCRONIZADA");
    	throw new Exception(e);
    }
	
}


public void actualizarUltimaActualizacionVendedor(String codven, String kardex, String codalm) throws Exception{

	String SOAP_ACTION= "http://tempuri.org/actualizar_TbActualizacionStockVendedor";
	String METHOD_NAME= "actualizar_TbActualizacionStockVendedor";
	
	SoapObject request=new SoapObject(NAMESPACE, METHOD_NAME);
	
	request.addProperty("codven", codven);
	request.addProperty("kardex", kardex);
	request.addProperty("codalm", codalm);
	
	Log.i("actualizarUltimaActualizacionVendedor",""+codven+"-"+kardex+"-"+codalm);
	
	if(GlobalVar.id_servicio == GlobalVar.INTERNET){
		
    	request.addProperty("serv", url); 
		request.addProperty("catalog", catalog); 
		request.addProperty("uid", user); 
		request.addProperty("password", contrasena);
		
		Log.i("actualizarUltimaActualizacionVendedor",""+url+"-"+catalog+"-"+user+"-"+contrasena);
		
	}
	else if(GlobalVar.id_servicio == GlobalVar.LOCAL){
		
    	request.addProperty("serv", url_local); 
		request.addProperty("catalog", catalog_local); 
		request.addProperty("uid", user_local); 
		request.addProperty("password", contrasena_local);
		
		Log.i("actualizarUltimaActualizacionVendedor",""+url_local+"-"+catalog_local+"-"+user_local+"-"+contrasena_local);
		
	}
	
    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
    Soapenvelope.dotNet=true;
    Soapenvelope.setOutputSoapObject(request);
    
    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);
    
    long beforecall = System.currentTimeMillis();
    
    try{
    	transporte.call(SOAP_ACTION, Soapenvelope);
    	Log.i("actualizarUltimaActualizacionVendedor","RESPUESTA EN: "+(System.currentTimeMillis()-beforecall)+"miliseg");

    	 SoapPrimitive result = (SoapPrimitive)Soapenvelope.getResponse();   

    	 int resp = Integer.parseInt(result.toString());
    	 Log.i("actualizarUltimaActualizacionVendedor","Resp: "+resp);
    	
    }catch(Exception e){
    	e.printStackTrace();
    	Log.i("obtenerStockActualizado", "NO SINCRONIZADA");
    	throw new Exception(e);
    }
	
}

public int actualizarRegistroBonificaciones() throws Exception{
	
   	String SOAP_ACTION= "http://tempuri.org/actualizarObjRegistroBonificaciones_json";
	String METHOD_NAME="actualizarObjRegistroBonificaciones_json";
	//La lista se obtiene al registrar el objPedido
    if(registroBonificaciones.size() == 0){
    	return  0;
    }
        
    String cadena = gson.toJson(registroBonificaciones);
    
    
    Log.i("DBSync_soap_manager :actualizarRegistroBonificaciones:","JSON: "+cadena.toString());
        	
    	SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
    	
    	if(GlobalVar.id_servicio == GlobalVar.INTERNET){
    		
    		request.addProperty("cadena",cadena);
        	request.addProperty("url", url); 
    		request.addProperty("catalog", catalog); 
    		request.addProperty("user", user); 
    		request.addProperty("password", contrasena);
    		
    	}
    	else if(GlobalVar.id_servicio == GlobalVar.LOCAL){
    		
    		request.addProperty("cadena",cadena);
        	request.addProperty("url", url_local); 
    		request.addProperty("catalog", catalog_local); 
    		request.addProperty("user", user_local); 
    		request.addProperty("password", contrasena_local);
    		
    	}
		
	    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    Soapenvelope.dotNet=true;
	    Soapenvelope.setOutputSoapObject(request);
	    
	    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);

	    long beforecall = System.currentTimeMillis();
	    
	    try{
	    	transporte.call(SOAP_ACTION, Soapenvelope);
	    	Log.i("ENVIO REGISTRO BONIFICACIONES","RESPUESTA EN: "+(System.currentTimeMillis()-beforecall)+"miliseg");
	    	
	    	SoapPrimitive resultado_xml =(SoapPrimitive)Soapenvelope.getResponse();
			String res = resultado_xml.toString();
			
			Log.i("ENVIO REGISTRO BONIFICACIONES","Respuesta: "+res);
			
			JSONArray jsonstring = new JSONArray(res);
			Log.e("ENVIO REGISTRO BONIFICACIONES", jsonstring.toString());
			
	    }
	    
	    catch(JSONException ex){
	    	Log.w("DBSync_soap_manager :actualizarRegistroBonificaciones:",ex);
	    	throw new JsonParseException(ex.getMessage());
	    	//throw new JSONException(ex.getMessage());
	    }
	    catch(Exception ex){
	    	Log.w("DBSync_soap_manager :actualizarRegistroBonificaciones:",ex);
	    	throw new Exception(ex);
	    }
	
	    return 1;
}

	public void Sync_tabla_formasPago(String codigoVendedor, String url, String catalog, String user, String contrasena) throws Exception {
		
		String SOAP_ACTION= "http://tempuri.org/obtenerFormasPago_json";
		String METHOD_NAME="obtenerFormasPago_json";
		
		SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);
		Request.addProperty("codven", codigoVendedor);
		Request.addProperty("url", url); 
		Request.addProperty("catalog", catalog); 
		Request.addProperty("user", user); 
		Request.addProperty("password", contrasena); 
	    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    Soapenvelope.dotNet=true;
	    Soapenvelope.setOutputSoapObject(Request);
	    
	    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);
	    
	    long beforecall = System.currentTimeMillis();
	    
	    try{
	    	transporte.call(SOAP_ACTION, Soapenvelope);
	    	Log.i("Sync_tabla_formasPago","RESPUESTA EN: "+(System.currentTimeMillis()-beforecall)+"miliseg");
	
	    	SoapPrimitive result = (SoapPrimitive)Soapenvelope.getResponse();
	
	    	JSONArray jsonstring = new JSONArray(result.toString());
	    	Log.i("Sync_tabla_formasPago","Registros: "+jsonstring.length());
	    	dbclass.syncFormasPago(jsonstring);
	    	Log.i("Sync_tabla_formasPago","SINCRONIZADA");    	 
	    }catch(Exception e){
	    	e.printStackTrace();
	    	throw new Exception(e);
	    }			 
	}
	
	public void Sync_tabla_Nro_letras(String codCli, String cond_ven) throws Exception {
		
//		String SOAP_ACTION= "http://tempuri.org/obtenerTBNroLetras_json";
//		String METHOD_NAME="obtenerTBNroLetras_json";
//
//		SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);
//		Request.addProperty("codCli", codCli);
//		Request.addProperty("cond_ven", cond_ven);
//		Request.addProperty("url", url);
//		Request.addProperty("catalog", catalog);
//		Request.addProperty("user", user);
//		Request.addProperty("password", contrasena);
//	    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
//	    Soapenvelope.dotNet=true;
//	    Soapenvelope.setOutputSoapObject(Request);
//	    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);
//
//	    long beforecall = System.currentTimeMillis();
//	    try{
//	    	transporte.call(SOAP_ACTION, Soapenvelope);
//	    	Log.i("Sync_tabla_Nro_letras","RESPUESTA EN: "+(System.currentTimeMillis()-beforecall)+"miliseg");
//	    	SoapPrimitive result = (SoapPrimitive)Soapenvelope.getResponse();
//
//	    	JSONArray jsonstring = new JSONArray(result.toString());
//	    	Log.i("Sync_tabla_Nro_letras","Registros: "+jsonstring.length());
//	    	dbclass.syncNroLetras(jsonstring);
//	    	Log.i("Sync_tabla_Nro_letras","SINCRONIZADA");
//	    }catch(Exception e){
//	    	e.printStackTrace();
//	    	throw new Exception(e);
//	    }
	}
	
/*----------------------------------------------- CHEMA --------------------------------------------------------*/

	public void Sync_tabla_registrosGeneralesMovil(String url, String catalog, String user, String contrasena ){	
		String SOAP_ACTION= "http://tempuri.org/obtenerRegistrosGeneralesMovil";
		String METHOD_NAME="obtenerRegistrosGeneralesMovil";
		
		SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME); 
		Request.addProperty("url", url); 
		Request.addProperty("catalog", catalog); 
		Request.addProperty("user", user); 
		Request.addProperty("password", contrasena); 
	    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    Soapenvelope.dotNet=true;
	    Soapenvelope.setOutputSoapObject(Request);
	    
	    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);	    
	    try{
	    	transporte.call(SOAP_ACTION, Soapenvelope);
	    	SoapPrimitive result =(SoapPrimitive)Soapenvelope.getResponse();
	    	JSONArray jsonstring = new JSONArray(result.toString());
	    	dbclass.sincronizar_registrosGeneralesMovil(jsonstring);	    	
	    	Log.i("SOAP MANAGER", "Sync_tabla_registrosGeneralesMovil done");	    	
	    } catch(Exception e){
	    	Log.e("Sync_tabla_registrosGeneralesMovil", e.getMessage());
	    	e.printStackTrace();
	    }
	}
	
	public void Sync_tabla_lugarEntrega(String codigoVendedor, String url, String catalog, String user, String contrasena ) throws Exception{	
		String SOAP_ACTION= "http://tempuri.org/obtenerLugarEntrega_json";
		String METHOD_NAME="obtenerLugarEntrega_json";
		
		SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);
		Request.addProperty("codven", codigoVendedor);
		Request.addProperty("url", url); 
		Request.addProperty("catalog", catalog);
		Request.addProperty("user", user);
		Request.addProperty("password", contrasena); 
	    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    Soapenvelope.dotNet=true;
	    Soapenvelope.setOutputSoapObject(Request);
	    
	    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService,25000);	    
	    
	    try{
	    	transporte.call(SOAP_ACTION, Soapenvelope);
	    	SoapPrimitive result =(SoapPrimitive)Soapenvelope.getResponse();
	    	JSONArray jsonstring = new JSONArray(result.toString());
	    	dbclass.sincronizar_lugarEntrega(jsonstring);
	    	Log.i(TAG, "Sync_tabla_lugarEntrega done");
	    }catch(Exception e){
	    	e.printStackTrace();
	    	throw new Exception(e);
	    }   		
	}
	
	public void Sync_tabla_obra(String codigoVendedor, String url, String catalog, String user, String contrasena ) throws Exception{	
		String SOAP_ACTION= "http://tempuri.org/obtenerObras";
		String METHOD_NAME="obtenerObras";
		
		SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);
		Request.addProperty("codven", codigoVendedor);
		Request.addProperty("url", url); 
		Request.addProperty("catalog", catalog); 
		Request.addProperty("user", user); 
		Request.addProperty("password", contrasena); 
	    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    Soapenvelope.dotNet=true;
	    Soapenvelope.setOutputSoapObject(Request);
	    
	    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);
	    
	    try{
	    	transporte.call(SOAP_ACTION, Soapenvelope);
	    	SoapPrimitive result =(SoapPrimitive)Soapenvelope.getResponse();
	    	JSONArray jsonstring = new JSONArray(result.toString());
	    	dbclass.sincronizar_obra(jsonstring);	    	
	    	Log.i(TAG, "Sync_tabla_obra done");
	    }catch(Exception e){
	    	e.printStackTrace();
	    	throw new Exception(e);
	    }
	}
	
	public void Sync_tabla_transporte(String codigoVendedor, String url, String catalog, String user, String contrasena ) throws Exception{
		String SOAP_ACTION= "http://tempuri.org/obtenerTransporte_json";
		String METHOD_NAME="obtenerTransporte_json";
		
		SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);
		Request.addProperty("codven", codigoVendedor);
		Request.addProperty("url", url); 
		Request.addProperty("catalog", catalog); 
		Request.addProperty("user", user); 
		Request.addProperty("password", contrasena); 
	    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    Soapenvelope.dotNet=true;
	    Soapenvelope.setOutputSoapObject(Request);
	    	    
	    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService,25000);
	    try{
	    	transporte.call(SOAP_ACTION, Soapenvelope);
	    	SoapPrimitive result =(SoapPrimitive)Soapenvelope.getResponse();
	    	JSONArray jsonstring = new JSONArray(result.toString());
	    	dbclass.sincronizar_transporte(jsonstring);	    	
	    	Log.i(TAG, "Sync_tabla_transporte done");
	    }catch(Exception e){
	    	e.printStackTrace();
	    	throw new Exception(e);
	    }		
	}
	
	public void Sync_tabla_almacenes(String url, String catalog, String user, String contrasena) throws Exception{
		String SOAP_ACTION= "http://tempuri.org/obtener_lista_almacenes";
		String METHOD_NAME="obtener_lista_almacenes";
		
		SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);		
		Request.addProperty("url", url); 
		Request.addProperty("catalog", catalog); 
		Request.addProperty("user", user); 
		Request.addProperty("password", contrasena); 
	    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    Soapenvelope.dotNet=true;
	    Soapenvelope.setOutputSoapObject(Request);
	    
	    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);	    
	    try{
	    	transporte.call(SOAP_ACTION, Soapenvelope);
	    	SoapPrimitive result =(SoapPrimitive)Soapenvelope.getResponse();
	    	JSONArray jsonstring = new JSONArray(result.toString());
	    	dbclass.sync_almacenes(jsonstring);	    	
	    	Log.i(TAG, "Sync_tabla_almacenes done");
	    }catch(Exception e){
	    	e.printStackTrace();
	    	throw new Exception(e);
	    } 		
	}
	
	
	
	public void Sync_tabla_grupoProducto(String url, String catalog, String user, String contrasena) throws Exception{
		String SOAP_ACTION= "http://tempuri.org/obtenerGrupoProducto_json";
		String METHOD_NAME="obtenerGrupoProducto_json";
		
		SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);		
		Request.addProperty("url", url); 
		Request.addProperty("catalog", catalog); 
		Request.addProperty("user", user); 
		Request.addProperty("password", contrasena); 
	    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    Soapenvelope.dotNet=true;
	    Soapenvelope.setOutputSoapObject(Request);
	    
	    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);	    
	    try{
	    	transporte.call(SOAP_ACTION, Soapenvelope);
	    	SoapPrimitive result =(SoapPrimitive)Soapenvelope.getResponse();
	    	JSONArray jsonstring = new JSONArray(result.toString());
	    	dbclass.sincronizar_grupoProducto(jsonstring);	    	
	    	Log.i(TAG, "Sync_tabla_grupoProducto done");
	    }catch(Exception e){
	    	e.printStackTrace();
	    	throw new Exception(e);
	    } 		
	}
	
	public String sincro_obtenerPrecioProducto_json(
		String codigoCliente, String codigoProducto, String codigoMoneda, String codigoCondicionVenta, String cantidad,	String fechaPedido,
		String flagDescuento, String descuento, String codigoSucursal, String codigoLugarEntrega, String codigoTipoDespacho  ) throws Exception{
		
		String SOAP_ACTION= "http://tempuri.org/obtenerPrecioProducto_json";
		String METHOD_NAME="obtenerPrecioProducto_json";
		
		SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);
		Request.addProperty("codigoCliente", codigoCliente);
		Request.addProperty("codigoProducto", codigoProducto); 
		Request.addProperty("codigoMoneda", codigoMoneda); 
		Request.addProperty("codigoCondicionVenta", codigoCondicionVenta); 
		Request.addProperty("cantidad", cantidad);
		Request.addProperty("fechaPedido", fechaPedido);
		Request.addProperty("flagDescuento", flagDescuento);
		Request.addProperty("descuento", descuento);
		Request.addProperty("codigoSucursal", codigoSucursal);
		Request.addProperty("codigoLugarEntrega", codigoLugarEntrega);
		Request.addProperty("codigoTipoDespacho", codigoTipoDespacho);
		
		Request.addProperty("url", url); 
		Request.addProperty("catalog", catalog); 
		Request.addProperty("user", user); 
		Request.addProperty("password", contrasena);
		
		Log.d("method", "obtenerPrecioProducto_json");
		Log.d("codigoCliente", codigoCliente);
		Log.d("codigoProducto", codigoProducto); 
		Log.d("codigoMoneda", codigoMoneda); 
		Log.d("codigoCondicionVenta", codigoCondicionVenta); 
		Log.d("cantidad", cantidad);
		Log.d("fechaPedido", fechaPedido);
		Log.d("flagDescuento", flagDescuento);
		Log.d("descuento", descuento);
		Log.d("codigoSucursal", codigoSucursal);
		Log.d("codigoLugarEntrega", codigoLugarEntrega);
		Log.d("codigoTipoDespacho", codigoTipoDespacho);
		/*
		Log.d("url", url); 
		Log.d("catalog", catalog); 
		Log.d("user", user); 
		Log.d("password", contrasena);
		*/
		SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    Soapenvelope.dotNet=true;
	    Soapenvelope.setOutputSoapObject(Request);
	    
	    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);	    
	    long beforecall = System.currentTimeMillis();
	    
	    try{
	    	transporte.call(SOAP_ACTION, Soapenvelope);
	    	Log.i(TAG,"RESPUESTA EN: "+(System.currentTimeMillis()-beforecall)+"miliseg");	    	
	    	 SoapPrimitive result =(SoapPrimitive)Soapenvelope.getResponse();	    	 	    	 
	    	 Log.i(TAG+":sincro_obtenerPrecioProducto_json:", "SINCRONIZADA");	    	 
	    	 return result.toString();
	    }catch(Exception e){
	    	e.printStackTrace();
	    	Log.e(TAG+":sincro_obtenerPrecioProducto_json:", "NO SINCRONIZADA");
	    	return "";
	    }  

	}
	
	//nuevo  precio pvp
	public String sincro_obtenerPrecioProductoPVP_json(
			String codigoCliente, String codigoProducto, String codigoMoneda, String codigoCondicionVenta, String cantidad,	String fechaPedido,
			String flagDescuento, String descuento, String codigoSucursal, String codigoLugarEntrega, String codigoTipoDespacho  ) throws Exception{
			
			String SOAP_ACTION= "http://tempuri.org/obtenerPrecioProductoPVP_json";
			String METHOD_NAME="obtenerPrecioProductoPVP_json";
			
			SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);
			Request.addProperty("codigoCliente", codigoCliente);
			Request.addProperty("codigoProducto", codigoProducto); 
			Request.addProperty("codigoMoneda", codigoMoneda); 
			Request.addProperty("codigoCondicionVenta", codigoCondicionVenta); 
			Request.addProperty("cantidad", cantidad);
			Request.addProperty("fechaPedido", fechaPedido);
			Request.addProperty("flagDescuento", flagDescuento);
			Request.addProperty("descuento", descuento);
			Request.addProperty("codigoSucursal", codigoSucursal);
			Request.addProperty("codigoLugarEntrega", codigoLugarEntrega);
			Request.addProperty("codigoTipoDespacho", codigoTipoDespacho);
			
			Request.addProperty("url", url); 
			Request.addProperty("catalog", catalog); 
			Request.addProperty("user", user); 
			Request.addProperty("password", contrasena);
			
			Log.d("method", "obtenerPrecioProductoPVP_json");
			Log.d("codigoCliente", codigoCliente);
			Log.d("codigoProducto", codigoProducto); 
			Log.d("codigoMoneda", codigoMoneda); 
			Log.d("codigoCondicionVenta", codigoCondicionVenta); 
			Log.d("cantidad", cantidad);
			Log.d("fechaPedido", fechaPedido);
			Log.d("flagDescuento", flagDescuento);
			Log.d("descuento", descuento);
			Log.d("codigoSucursal", codigoSucursal);
			Log.d("codigoLugarEntrega", codigoLugarEntrega);
			Log.d("codigoTipoDespacho", codigoTipoDespacho);
			/*
			Log.d("url", url); 
			Log.d("catalog", catalog); 
			Log.d("user", user); 
			Log.d("password", contrasena);
			*/
			SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
		    Soapenvelope.dotNet=true;
		    Soapenvelope.setOutputSoapObject(Request);
		    
		    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);	    
		    long beforecall = System.currentTimeMillis();
		    
		    try{
		    	transporte.call(SOAP_ACTION, Soapenvelope);
		    	Log.i(TAG,"RESPUESTA EN: "+(System.currentTimeMillis()-beforecall)+"miliseg");	    	
		    	 SoapPrimitive result =(SoapPrimitive)Soapenvelope.getResponse();	    	 	    	 
		    	 Log.i(TAG+":sincro_obtenerPrecioProductoPVP_json:", "SINCRONIZADA");	    	 
		    	 return result.toString();
		    }catch(Exception e){
		    	e.printStackTrace();
		    	Log.e(TAG+":sincro_obtenerPrecioProductoPVP_json:", "NO SINCRONIZADA");
		    	return "";
		    }  

		}
	
	//nuevo listado precios
	
	public String sincro_obtenerListadoPrecios_json(
			String codigoCliente, String codigoProducto, String codigoMoneda, String codigoCondicionVenta,
			String flagDescuento, String codigoSucursal, String codigoLugarEntrega, String codigoTipoDespacho  ) throws Exception{
			
			String SOAP_ACTION= "http://tempuri.org/obtenerListadoPrecios_json";
			String METHOD_NAME="obtenerListadoPrecios_json";
			
			SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);
			Request.addProperty("codigoCliente", codigoCliente);
			Request.addProperty("codigoProducto", codigoProducto); 
			Request.addProperty("codigoMoneda", codigoMoneda); 
			Request.addProperty("codigoCondicionVenta", codigoCondicionVenta); 
			Request.addProperty("flagDescuento", flagDescuento);
			Request.addProperty("codigoSucursal", codigoSucursal);
			Request.addProperty("codigoLugarEntrega", codigoLugarEntrega);
			Request.addProperty("codigoTipoDespacho", codigoTipoDespacho);
			
			Request.addProperty("url", url); 
			Request.addProperty("catalog", catalog); 
			Request.addProperty("user", user); 
			Request.addProperty("password", contrasena);
			
			Log.d("method", "obtenerListadoPrecios_json");
			Log.d("codigoCliente", codigoCliente);
			Log.d("codigoProducto", codigoProducto); 
			Log.d("codigoMoneda", codigoMoneda); 
			Log.d("codigoCondicionVenta", codigoCondicionVenta); 
			Log.d("flagDescuento", flagDescuento);
			Log.d("codigoSucursal", codigoSucursal);
			Log.d("codigoLugarEntrega", codigoLugarEntrega);
			Log.d("codigoTipoDespacho", codigoTipoDespacho);
			/*
			Log.d("url", url); 
			Log.d("catalog", catalog); 
			Log.d("user", user); 
			Log.d("password", contrasena);
			*/
			SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
		    Soapenvelope.dotNet=true;
		    Soapenvelope.setOutputSoapObject(Request);
		    
		    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);	    
		    long beforecall = System.currentTimeMillis();
		    
		    try{
		    	transporte.call(SOAP_ACTION, Soapenvelope);
		    	Log.i(TAG,"RESPUESTA EN: "+(System.currentTimeMillis()-beforecall)+"miliseg");	    	
		    	 SoapPrimitive result =(SoapPrimitive)Soapenvelope.getResponse();	    	 	    	 
		    	 Log.i(TAG+":sincro_obtenerListadoPrecios_json:", "SINCRONIZADA");	    	 
		    	 return result.toString();
		    }catch(Exception e){
		    	e.printStackTrace();
		    	Log.e(TAG+":sincro_obtenerListadoPrecios_json:", "NO SINCRONIZADA");
		    	return "";
		    }  

		}
	

	public String sincro_obtenerStockProducto_json(String codigoProducto) throws Exception{
			
			String SOAP_ACTION= "http://tempuri.org/obtenerStockProducto_json";
			String METHOD_NAME="obtenerStockProducto_json";
			
			SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);			
			Request.addProperty("codigoProducto", codigoProducto);			
			Request.addProperty("url", url); 
			Request.addProperty("catalog", catalog); 
			Request.addProperty("user", user); 
			Request.addProperty("password", contrasena);
			
			Log.d("codigoProducto", codigoProducto);						
			Log.d("url", url); 
			Log.d("catalog", catalog); 
			Log.d("user", user); 
			Log.d("password", contrasena);
			
			SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
		    Soapenvelope.dotNet=true;
		    Soapenvelope.setOutputSoapObject(Request);
		    
		    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);		    
		    long beforecall = System.currentTimeMillis();
		    
		    try{
		    	transporte.call(SOAP_ACTION, Soapenvelope);
		    	Log.i(TAG,"RESPUESTA EN: "+(System.currentTimeMillis()-beforecall)+"miliseg");	    	
		    	 SoapPrimitive result =(SoapPrimitive)Soapenvelope.getResponse();	    	 	    	 
		    	 Log.i(TAG+":sincro_obtenerStockProducto_json:", result.toString());	    	 
		    	 return result.toString();
		    }catch(Exception e){
		    	e.printStackTrace();
		    	Log.e(TAG+":sincro_obtenerStockProducto_json:", "NO SINCRONIZADA");
		    	return "";
		    }  

		}
	
	public void Sync_tabla_moneda(String url, String catalog, String user, String contrasena) throws Exception{
		String SOAP_ACTION= "http://tempuri.org/obtenerMoneda_json";
		String METHOD_NAME="obtenerMoneda_json";
		
		SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);		
		Request.addProperty("url", url); 
		Request.addProperty("catalog", catalog); 
		Request.addProperty("user", user); 
		Request.addProperty("password", contrasena); 
	    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    Soapenvelope.dotNet=true;
	    Soapenvelope.setOutputSoapObject(Request);
	    
	    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);	    
	    try{
	    	transporte.call(SOAP_ACTION, Soapenvelope);
	    	SoapPrimitive result =(SoapPrimitive)Soapenvelope.getResponse();
	    	JSONArray jsonstring = new JSONArray(result.toString());
	    	Log.i("Sync_tabla_moneda","Registros: "+jsonstring.length());
	    	dbclass.sincronizar_moneda(jsonstring);	    	
	    	Log.i(TAG, "Sync_tabla_moneda done");
	    }catch(Exception e){
	    	e.printStackTrace();
	    	throw new Exception(e);
	    } 		
	}
	
	public void Sync_tabla_tipoProducto(String url, String catalog, String user, String contrasena) throws Exception{
		String SOAP_ACTION= "http://tempuri.org/obtenerTipoProducto_json";
		String METHOD_NAME="obtenerTipoProducto_json";
		
		SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);		
		Request.addProperty("url", url); 
		Request.addProperty("catalog", catalog); 
		Request.addProperty("user", user); 
		Request.addProperty("password", contrasena); 
	    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    Soapenvelope.dotNet=true;
	    Soapenvelope.setOutputSoapObject(Request);
	    
	    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);	    
	    try{
	    	transporte.call(SOAP_ACTION, Soapenvelope);
	    	SoapPrimitive result =(SoapPrimitive)Soapenvelope.getResponse();
	    	JSONArray jsonstring = new JSONArray(result.toString());
	    	dbclass.sincronizar_tipoProducto(jsonstring);	    	
	    	Log.i(TAG, "sincronizar_tipoProducto done");
	    }catch(Exception e){
	    	e.printStackTrace();
	    	throw new Exception(e);
	    } 		
	}
	
	public void Sync_tabla_promocion_ubigeo(String codven,String url, String catalog, String user, String contrasena) throws Exception{
		
		String SOAP_ACTION= "http://tempuri.org/obtenerPromocionUbigeo_json";
		String METHOD_NAME="obtenerPromocionUbigeo_json";
		
		SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);
		Request.addProperty("url", url); 
		Request.addProperty("catalog", catalog); 
		Request.addProperty("user", user); 
		Request.addProperty("password", contrasena); 
		Request.addProperty("codven", codven);
	    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    Soapenvelope.dotNet=true;
	    Soapenvelope.setOutputSoapObject(Request);
	    
	    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService,20000);
	    
	    long beforecall = System.currentTimeMillis();
	    
	    try{
	    	transporte.call(SOAP_ACTION, Soapenvelope);
	    	Log.i("PROMOCION_UBIGEO","RESPUESTA EN: "+(System.currentTimeMillis()-beforecall)+"miliseg");
	    	
	    	SoapPrimitive result = (SoapPrimitive)Soapenvelope.getResponse();
	    	
	    	JSONArray jsonstring = new JSONArray(result.toString());
	    	Log.i("PROMOCION_UBIGEO","Registros: "+jsonstring.length());
	    	dbclass.syncPromocionUbigeo(jsonstring);
	    	Log.i("PROMOCION_UBIGEO","SINCRONIZADA");
	    	 
	    }catch(Exception e){
	    	e.printStackTrace();
	    	Log.e("PROMOCION_UBIGEO","NO SINCRONIZADA");
	    	throw new Exception(e);
	    }  

	}
/*--------------------------------------------------------------------------------------------------------*/

	 
	 public void Sync_tabla_cta_ingresos_x_cliente(String codigoVendedor, String codigoCliente, String url, String catalog, String user, String contrasena) throws Exception{

		 String S_TAG="Sync_tabla_cta_ingresos_x_cliente:: ";
			String SOAP_ACTION= "http://tempuri.org/obtenerCtaIngresosxCliente_json";
			String METHOD_NAME="obtenerCtaIngresosxCliente_json";
			
			SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);
			Request.addProperty("codigoCliente", codigoCliente); 
			Request.addProperty("codigoVendedor",codigoVendedor ); 
			Request.addProperty("url", url); 
			Request.addProperty("catalog", catalog); 
			Request.addProperty("user", user); 
			Request.addProperty("password", contrasena); 
			
		    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER12);
		    Soapenvelope.dotNet=true;
		    Soapenvelope.setOutputSoapObject(Request);
		    
		    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService,50000);	   
		    long beforecall = System.currentTimeMillis();
		    
		    try{
		    	transporte.call(SOAP_ACTION, Soapenvelope);
		    	Log.i("CTA_INGRESOS CLIENTE",S_TAG+"RESPUESTA EN: "+(System.currentTimeMillis()-beforecall)+"miliseg");

		    	SoapPrimitive result = (SoapPrimitive)Soapenvelope.getResponse();

		    	JSONArray jsonstring = new JSONArray(result.toString());
		    	Log.i("CTA_INGRESOS CLIENTE",S_TAG+"Registros: "+jsonstring.length());
		    	
		    	dbclass.syncCtaIngresos(jsonstring, codigoVendedor);
		    	Log.i("CTA_INGRESOS CLIENTE","SINCRONIZADA");
		    }catch(Exception e){
		    	e.printStackTrace();
		    	throw new Exception(e);
		    }  

		}
	 
	 
	 public void Sync_tabla_cta_ingresos_resumen(String codven, String url, String catalog, String user, String contrasena) throws Exception{
			
//			String SOAP_ACTION= "http://tempuri.org/obtenerCtasIngresosxVendedor_json";
//			String METHOD_NAME="obtenerCtasIngresosxVendedor_json";
//
//			SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);
//			Request.addProperty("codven", codven);
//			Request.addProperty("url", url);
//			Request.addProperty("catalog", catalog);
//			Request.addProperty("user", user);
//			Request.addProperty("password", contrasena);
//
//		    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
//		    Soapenvelope.dotNet=true;
//		    Soapenvelope.setOutputSoapObject(Request);
//
//		    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);
//
//		    long beforecall = System.currentTimeMillis();
//
//		    try{
//		    	transporte.call(SOAP_ACTION, Soapenvelope);
//		    	Log.i("CTA_INGRESOS RESUMEN","RESPUESTA EN: "+(System.currentTimeMillis()-beforecall)+"miliseg");
//
//		    	SoapPrimitive result = (SoapPrimitive)Soapenvelope.getResponse();
//
//		    	JSONArray jsonstring = new JSONArray(result.toString());
//		    	Log.i("CTA_INGRESOS RESUMEN","Registros: "+jsonstring.length());
//
//		    	dbclass.syncCta_ingresos_resumen(jsonstring);
//		    	Log.i("CTA_INGRESOS RESUMEN","SINCRONIZADA");
//
//		    }catch(Exception e){
//		    	e.printStackTrace();
//		    	throw new Exception(e);
//		    }

		}
	 
	 public void Sync_tabla_RegistroBonificacionesPendientes(String codven, String url, String catalog, String user, String contrasena) throws Exception{
			
			String SOAP_ACTION= "http://tempuri.org/obtenerRegistroBonificacion_json";
			String METHOD_NAME="obtenerRegistroBonificacion_json";
			long beforecall;
			
			SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);
			Request.addProperty("codigoVendedor", codven);
			Request.addProperty("url", url); 
			Request.addProperty("catalog", catalog); 
			Request.addProperty("user", user); 
			Request.addProperty("password", contrasena); 
		    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
		    Soapenvelope.dotNet=true;
		    Soapenvelope.setOutputSoapObject(Request);
		    
		    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);		    
		    beforecall = System.currentTimeMillis();
		    
		    try{
		    	transporte.call(SOAP_ACTION, Soapenvelope);
		    	
		    	Log.d("Sync_tabla_RegistroBonificacionesPendientes","RESPUESTA EN: "+(System.currentTimeMillis()-beforecall)+"miliseg");
		    	
		    	  SoapPrimitive result = (SoapPrimitive)Soapenvelope.getResponse();  
		    	  Log.d("Sync_tabla_RegistroBonificacionesPendientes",""+result.toString());
		    	  JSONArray jsonstring = new JSONArray(result.toString());
		    	  Log.d("Sync_tabla_RegistroBonificacionesPendientes","Registros: "+jsonstring.length());		    	  
		    	  dbclass.syncRegistroBonificacionPendientes(jsonstring,codven);
		    	 Log.d("OBJ_PEDIDO", "SINCRONIZADA");
		    	
		    }catch(Exception e){
		    	 e.printStackTrace();
		    	 Log.e("OBJ_PEDIDO", "NO SINCRONIZADA");
		    	 throw new Exception(e);
		    }
		}
	 
	public boolean isServicioActivo(String empresa, String ruc) throws Exception {
		String SOAP_ACTION = "http://tempuri.org/obtenerEstadoEmpresa";							 
		String METHOD_NAME = "obtenerEstadoEmpresa";
		
		SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
		Request.addProperty("ruc", ruc);
		Request.addProperty("razonSocial", empresa);

		SoapSerializationEnvelope Soapenvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		Soapenvelope.dotNet = true;
		Soapenvelope.setOutputSoapObject(Request);

		HttpTransportSE transporte = new HttpTransportSE("http://saemoviles.com/SAE201510V1/service.asmx");
		try {
			transporte.call(SOAP_ACTION, Soapenvelope);
			SoapPrimitive result = (SoapPrimitive) Soapenvelope.getResponse();			
			String resultado = result.toString();
			Log.d(TAG, "isServicioActivo = "+resultado);			
			
			if (resultado.equals("1")) {
				return true;
			}
		} catch (Exception e) {			
			Log.e("isServicioActivo", ""+e.getMessage());
			e.printStackTrace();
			throw new Exception(e);
		}
		return false;     
	}
	
	public void Sync_tabla_motivo(String url, String catalog, String user, String contrasena) throws Exception{
		
		String SOAP_ACTION= "http://tempuri.org/obtenerMotivo_json";
		String METHOD_NAME="obtenerMotivo_json";
		
		SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);		
		Request.addProperty("url", url); 
		Request.addProperty("catalog", catalog); 
		Request.addProperty("user", user); 
		Request.addProperty("password", contrasena); 
		
	    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    Soapenvelope.dotNet=true;
	    Soapenvelope.setOutputSoapObject(Request);
	    
	    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);
	    
	    long beforecall = System.currentTimeMillis();
	    
	    try{
	    	transporte.call(SOAP_ACTION, Soapenvelope);
	    	Log.i("Sync_tabla_motivo","RESPUESTA EN: "+(System.currentTimeMillis()-beforecall)+"miliseg");

	    	SoapPrimitive result = (SoapPrimitive)Soapenvelope.getResponse();

	    	JSONArray jsonstring = new JSONArray(result.toString());
	    	Log.i("Sync_tabla_motivo","Registros: "+jsonstring.length());
	    	
	    	dbclass.syncMotivo(jsonstring);
	    	Log.i("Sync_tabla_motivo","SINCRONIZADA");
	    	 
	    }catch(Exception e){
	    	e.printStackTrace();
	    	throw new Exception(e);
	    }
	}
	
	public void Sync_tabla_expectativa(String url, String catalog, String user, String contrasena) throws Exception{
			
			String SOAP_ACTION= "http://tempuri.org/obtenerExpectativa_json";
			String METHOD_NAME="obtenerExpectativa_json";
			
			SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);		
			Request.addProperty("url", url);
			Request.addProperty("catalog", catalog);
			Request.addProperty("user", user);
			Request.addProperty("password", contrasena);
			
		    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
		    Soapenvelope.dotNet=true;
		    Soapenvelope.setOutputSoapObject(Request);
		    
		    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);
		    
		    long beforecall = System.currentTimeMillis();
		    
		    try{
		    	transporte.call(SOAP_ACTION, Soapenvelope);
		    	Log.i("Sync_tabla_expectativa","RESPUESTA EN: "+(System.currentTimeMillis()-beforecall)+"miliseg");
	
		    	SoapPrimitive result = (SoapPrimitive)Soapenvelope.getResponse();
	
		    	JSONArray jsonstring = new JSONArray(result.toString());
		    	Log.i("Sync_tabla_expectativa","Registros: "+jsonstring.length());
		    	
		    	dbclass.syncExpectativa(jsonstring);
		    	Log.i("Sync_tabla_expectativa","SINCRONIZADA");
		    	 
		    }catch(Exception e){
		    	e.printStackTrace();
		    	throw new Exception(e);
		    }
		}
	
	public String sincro_obtenerDocumentosDevolucion_json(String codigoProducto,String lote, String ruc) throws Exception{
		
		String SOAP_ACTION= "http://tempuri.org/obtenerDocumentosDevolucion_json";
		String METHOD_NAME="obtenerDocumentosDevolucion_json";
		
		SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME);			
		Request.addProperty("codigoProducto", codigoProducto);
		Request.addProperty("lote", lote);	
		Request.addProperty("ruc", ruc);	
		Request.addProperty("url", url); 
		Request.addProperty("catalog", catalog); 
		Request.addProperty("user", user); 
		Request.addProperty("password", contrasena);
							
		Log.d("url", url); 
		Log.d("catalog", catalog); 
		Log.d("user", user); 
		Log.d("password", contrasena);
		
		SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    Soapenvelope.dotNet=true;
	    Soapenvelope.setOutputSoapObject(Request);
	    
	    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);	    
	    try{
	    	transporte.call(SOAP_ACTION, Soapenvelope);	    		    	
	    	SoapPrimitive result =(SoapPrimitive)Soapenvelope.getResponse();
	    	return result.toString();
	    }catch(Exception e){
	    	e.printStackTrace();
	    	Log.e(TAG+":sincro_obtenerDocumentosDevolucion_json:", "NO SINCRONIZADA");
	    	return "";
	    }  

	}
	
	public void Sync_tabla_productoNoDescuento(String url, String catalog, String user, String contrasena) throws Exception{
		String SOAP_ACTION= "http://tempuri.org/obtenerProductoNoDescuento_json";
		String METHOD_NAME="obtenerProductoNoDescuento_json";
			
		SoapObject Request=new SoapObject(NAMESPACE, METHOD_NAME); 
		Request.addProperty("url", url); 
		Request.addProperty("catalog", catalog); 
		Request.addProperty("user", user); 
		Request.addProperty("password", contrasena); 
	    SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    Soapenvelope.dotNet=true;
	    Soapenvelope.setOutputSoapObject(Request);
	    
	    HttpTransportSE transporte=new HttpTransportSE(URL+GlobalVar.urlService);
	    	    
	    try{
	    	transporte.call(SOAP_ACTION, Soapenvelope);
	    	SoapPrimitive result = (SoapPrimitive)Soapenvelope.getResponse();  	    
	    	
	    	JSONArray jsonstring = new JSONArray(result.toString());
	    	Log.i("productoNoDescuento","Registros: "+jsonstring.length());
	    	dbclass.syncProductoNoDescuento(jsonstring);
	    	Log.i("productoNoDescuento", "SINCRONIZADA");
	    	 
	    }catch(Exception e){
	    	e.printStackTrace();
	    	Log.i("productoNoDescuento", "NO SINCRONIZADA");
	    	throw new Exception(e);
	    } 

	}

    public String actualizarDireccionCliente(String codcli, String item) throws Exception { //**Nuevo Localizacion

        String SOAP_ACTION = "http://tempuri.org/actualizarDireccionClienteGiro";
        String METHOD_NAME = "actualizarDireccionClienteGiro";
        //String METHOD_NAME = "geolocalizarCliente_json";

        String flag = "";

        ArrayList<DB_DireccionClientes> lista_direccion_cliente = new ArrayList<DB_DireccionClientes>();
        lista_direccion_cliente = dbclass.getLocalizacionesCliente(codcli, item);

        if (lista_direccion_cliente.size() == 0) {
            return "Nada por actualizar";
        }

        Gson gson = new Gson();
        String cadena = gson.toJson(lista_direccion_cliente);
        Log.i("ENVIO PENDIENTES", "JSON SIZE: " + cadena.length());
        Log.i("ENVIO PENDIENTES", "JSON: " + cadena);

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);




        request.addProperty("cadena", cadena);
        request.addProperty("url", url);
        request.addProperty("catalog", catalog);
        request.addProperty("user", user);
        request.addProperty("password", contrasena);

        Log.v("SOAP", "cadena:" + cadena);
        Log.v("SOAP", "url:" + url);
        Log.v("SOAP", "catalog:" + catalog);


        SoapSerializationEnvelope Soapenvelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        Soapenvelope.dotNet = true;
        Soapenvelope.setOutputSoapObject(request);

        HttpTransportSE transporte = new HttpTransportSE(URL
                + GlobalVar.urlService);

        long beforecall = System.currentTimeMillis();

        try {
            transporte.call(SOAP_ACTION, Soapenvelope);
            Log.i("actualizarDireccionCliente", "RESPUESTA EN: " + (System.currentTimeMillis() - beforecall) + "miliseg");

            SoapPrimitive resultado_xml = (SoapPrimitive) Soapenvelope.getResponse();
            flag = resultado_xml.toString();
            Log.i("actualizarDireccionCliente", "Respuesta: " + flag);
        } catch (Exception ex) {
            Log.w("SYNC MANAGER", "actualizarDireccionCliente", ex);
            throw new Exception(ex);
        }
        return flag;
    }
	
	
	
}
