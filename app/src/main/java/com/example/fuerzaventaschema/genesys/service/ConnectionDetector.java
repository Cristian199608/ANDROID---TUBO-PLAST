package com.example.fuerzaventaschema.genesys.service;



import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.params.HttpConnectionParams;

import com.example.fuerzaventaschema.genesys.util.GlobalVar;
import com.example.fuerzaventaschema.genesys.datatypes.DB_Servidor;
import com.example.fuerzaventaschema.genesys.datatypes.DBclasses;
import com.example.fuerzaventaschema.genesys.util.GlobalVar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

@SuppressLint("LongLogTag")
public class ConnectionDetector {
	
	private Context _context;
	
	public ConnectionDetector(Context context){
		this._context = context;
	}

	public boolean isConnectingToInternet(){
		ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
		Log.i("isConnectingToInternet","comprobando...");  
		if (connectivity != null) 
		  {
			  NetworkInfo info = connectivity.getActiveNetworkInfo();
			  if (info!= null &&  info.isConnectedOrConnecting()) {
				    Log.i("isConnectingToInternet","true");
			        return true;
			    }
			    Log.i("isConnectingToInternet","false");
			    return false;
		  }
		  Log.i("isConnectingToInternet","false");
		  return false;
		  
		
	}
	
	public  boolean hasActiveInternetConnection(Context context) {
	    if (isConnectingToInternet()) {
	    	
	    	String servicio = "";
	    	
	    	//if(GlobalVar.direccion_servicio!=null){
	    		servicio = GlobalVar.urlService;
	    	//}else{
	    		//servicio = "www.google.com";
	    	//}
	    	
	        try {
	            //HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
	        	HttpURLConnection urlc = (HttpURLConnection) (new URL("http://"+servicio).openConnection());
	            urlc.setRequestProperty("User-Agent", "Test");
	            urlc.setRequestProperty("Connection", "close");
	            urlc.setConnectTimeout(3000);
	            Log.i("CONEXION INTERNET","conectando con..."+GlobalVar.urlService);
	            urlc.connect();
	            return (urlc.getResponseCode() == 200);
	        } catch (IOException e) {
	            Log.i("CONEXION INTERNET", "Error checking internet connection", e);
	        }
	    } else {
	        Log.e("CONEXION INTERNET", "No network available!");
	    }
	    return false;
	}
	
	
	public  boolean hasActiveInternetConnection2(){
	    if (isConnectingToInternet()) {
	    	
	    	//String servicio = "saemoviles.com/Service.asmx";
	    	String servicio = "www.google.com";	    	
	    	
	        try {
	            //HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
	        	HttpURLConnection urlc = (HttpURLConnection) (new URL("http://"+servicio).openConnection());
	            urlc.setRequestProperty("User-Agent", "Test");
	            urlc.setRequestProperty("Connection", "close");
	            urlc.setConnectTimeout(5000); 
	            Log.i("CONEXION A INTERNET","conectando...");
	            urlc.connect();
	            return (urlc.getResponseCode() == 200);
	        } catch (IOException e) {
	            Log.e("CONEXION A INTERNET", "Error checking internet connection", e);
	        }
	    } else {
	        Log.d("CONEXION A INTERNET", "No network available!");
	    }
	    return false;
	}
	

	public  boolean hasActiveDominio(){
    	String servicio = "saemoviles.com/Service.asmx";    	
        try {
        	HttpURLConnection urlc = (HttpURLConnection) (new URL("http://"+servicio).openConnection());
            urlc.setRequestProperty("User-Agent", "Test");
            urlc.setRequestProperty("Connection", "close");
            urlc.setConnectTimeout(5000); 
            Log.i("CONEXION A "+servicio,"conectando...");
            urlc.connect();
            Log.i("CONEXION A SAEMOVILES", ""+(urlc.getResponseCode() == 200));
            return (urlc.getResponseCode() == 200);
        } catch (IOException e) {
            Log.e("CONEXION A SAEMOVILES", "Error checking internet connection", e);
        }	    
	    return false;
	}


public  boolean hasActiveInternetConnection_local(Context context) {
    if (isConnectingToInternet()) {
    	
    	String servicio_local = "";
    	servicio_local = GlobalVar.direccion_servicio_local;
    	
    	
    	
        try {
            //HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
        	HttpURLConnection urlc = (HttpURLConnection) (new URL("http://"+servicio_local).openConnection());
            urlc.setRequestProperty("User-Agent", "Test");
            urlc.setRequestProperty("Connection", "close");
            urlc.setConnectTimeout(3000);
            Log.i("CONEXION LOCAL", "conectando...");
            urlc.connect();
            return (urlc.getResponseCode() == 200);
        } catch (IOException e) {
            Log.e("CONEXION LOCAL", "Error checking internet connection (local)", e);
        }
    } else {
        Log.d("CONEXION LOCAL", "No network available! (local)");
    }
    return false;
}
	
}


