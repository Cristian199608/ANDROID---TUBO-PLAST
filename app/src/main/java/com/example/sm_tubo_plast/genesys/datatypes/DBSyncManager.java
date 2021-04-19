/*******************************************************************************
 * Copyright 2012 Gianrico D'Angelis  -- gianrico.dangelis@gmail.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.example.sm_tubo_plast.genesys.datatypes;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.thetransactioncompany.jsonrpc2.JSONRPC2Error;
import com.thetransactioncompany.jsonrpc2.JSONRPC2ParseException;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DBSyncManager {
	
	private static final String rpcUri = "emng/syncmanager.rpc.php";
	private static final String url = "http://saemoviles.com/Service1.svc/";
	private Context context=null;
	DBclasses dbc = null;
	
	public DBSyncManager(Context c){
		context = c;
		dbc = new DBclasses(c);
	}
	
	public void close(){
		dbc.close();
	}
	
	public void syncProductos(){
		
		String method = "getMenus";
		String id = "syncmenus-1";
		JSONRPC2Request reqOut = new JSONRPC2Request(method, id);
		String jsonString = reqOut.toString();
		//Log.i(DBUsuarios.TAG,jsonString);
		HTTPHelper httph = new HTTPHelper(rpcUri,context);
		httph.setBody(jsonString);
		
		JSONRPC2Response respIn = null;

		try {
			respIn = JSONRPC2Response.parse(httph.executePost());
		} catch (JSONRPC2ParseException e) {
		//	Log.e(EasymenuNGActivity.TAG,"Error parsing JSONRPC2 response string: "+e.getMessage());
		}

		// Check for success or error
		if (respIn.indicatesSuccess()) {
			
			//try {
				//dbc.syncProductos(new JSONArray((String)respIn.getResult()));
			//} catch (JSONException e) {
	//			Log.e(EasymenuNGActivity.TAG,"JSON exception: "+e.getMessage());
			//}
		}
		else {

			printError(respIn.getError());
			
		}
	}
	
	public void syncUsuarios(){
		
		
		String method = "getItems";
		String id = "syncitems-1";
		JSONRPC2Request reqOut = new JSONRPC2Request(method, id);
		String jsonString = reqOut.toString();
	//	Log.i(EasymenuNGActivity.TAG,jsonString);
		HTTPHelper httph = new HTTPHelper(rpcUri,context);
		httph.setBody(jsonString);
		
		JSONRPC2Response respIn = null;
		
		try {
			respIn = JSONRPC2Response.parse(httph.executePost());
		} catch (JSONRPC2ParseException e) {
	//		Log.e(EasymenuNGActivity.TAG,"Error parsing JSONRPC2 response string: "+e.getMessage());
		}

		// Check for success or error
		if (respIn.indicatesSuccess()) {
			
			//try {
				//dbc.syncUsuarios(new JSONArray((String)respIn.getResult()));
			//} catch (JSONException e) {
	//			Log.e(EasymenuNGActivity.TAG,"JSON exception: "+e.getMessage());
			//}
		}
		else {

			printError(respIn.getError());
			
		}
		
	} 
	
	public void syncVendedor(){
		
		String method = "getMenulists";
		String id = "syncmenulists-1";
		JSONRPC2Request reqOut = new JSONRPC2Request(method, id);
		String jsonString = reqOut.toString();
//		Log.i(EasymenuNGActivity.TAG,jsonString);
		HTTPHelper httph = new HTTPHelper(rpcUri,context);
		httph.setBody(jsonString);
		
		JSONRPC2Response respIn = null;
		
		try {
			respIn = JSONRPC2Response.parse(httph.executePost());
		} catch (JSONRPC2ParseException e) {
//			Log.e(EasymenuNGActivity.TAG,"Error parsing JSONRPC2 response string: "+e.getMessage());
		}

		// Check for success or error
		if (respIn.indicatesSuccess()) {
			
			//try {
				//dbc.syncVendedor(new JSONArray((String)respIn.getResult()));
			//} catch (JSONException e) {
//				Log.e(EasymenuNGActivity.TAG,"JSON exception: "+e.getMessage());
			//}
		}
		else {

			printError(respIn.getError());
			
		}
	} 
	
/*	public void syncImages(){
		
		String method = "getImages";
		String id = "syncimages-1";
		JSONRPC2Request reqOut = new JSONRPC2Request(method, id);
		String jsonString = reqOut.toString();
//		Log.i(EasymenuNGActivity.TAG,jsonString);
		HTTPHelper httph = new HTTPHelper(rpcUri,context);
		httph.setBody(jsonString);
		
		JSONRPC2Response respIn = null;
		
		
		try {
			respIn = JSONRPC2Response.parse(httph.executePost());
		} catch (JSONRPC2ParseException e) {
//			Log.e(EasymenuNGActivity.TAG,"Error parsing JSONRPC2 response string: "+e.getMessage());
		}

		// Check for success or error
		if (respIn.indicatesSuccess()) {
			
			try {
				//Prepare fo image sync
				deleteRecursive(context.getFilesDir());
				dbc.syncImagesTable(new JSONArray((String)respIn.getResult()));
				
			} catch (JSONException e) {
//				Log.e(EasymenuNGActivity.TAG,"JSON exception: "+e.getMessage());
			}
		}
		else {

			printError(respIn.getError());
			
		}
		
		
	} */
	
	public void syncClientes(){
		
		String method = "getCategories";
		String id = "synccategories-1";
		JSONRPC2Request reqOut = new JSONRPC2Request(method, id);
		String jsonString = reqOut.toString();
//		Log.i(EasymenuNGActivity.TAG,jsonString);
		HTTPHelper httph = new HTTPHelper(rpcUri,context);
		httph.setBody(jsonString);
		
		JSONRPC2Response respIn = null;
		
		try {
			respIn = JSONRPC2Response.parse(httph.executePost());
		} catch (JSONRPC2ParseException e) {
//			Log.e(EasymenuNGActivity.TAG,"Error parsing JSONRPC2 response string: "+e.getMessage());
		}

		// Check for success or error
		if (respIn.indicatesSuccess()) {
			
			//try {
				//dbc.syncClientes(new JSONArray((String)respIn.getResult()));
			//} catch (JSONException e) {
//				Log.e(EasymenuNGActivity.TAG,"JSON exception: "+e.getMessage());
			//}
		}
		else {

			printError(respIn.getError());
			
		}
		
	} 
	
	public void syncUnidad_Medida(){
		
		String method = "getConfig";
		String id = "syncconfig-1";
		JSONRPC2Request reqOut = new JSONRPC2Request(method, id);
		String jsonString = reqOut.toString();
//		Log.i(EasymenuNGActivity.TAG,jsonString);
		HTTPHelper httph = new HTTPHelper(rpcUri,context);
		httph.setBody(jsonString);
		
		JSONRPC2Response respIn = null;
		
		try {
			respIn = JSONRPC2Response.parse(httph.executePost());
		} catch (JSONRPC2ParseException e) {
//			Log.e(EasymenuNGActivity.TAG,"Error parsing JSONRPC2 response string: "+e.getMessage());
		}

		// Check for success or error
		if (respIn.indicatesSuccess()) {
			
			//try {
				//dbc.syncUnidad_Medida(new JSONArray((String)respIn.getResult()));
			//} catch (JSONException e) {
//				Log.e(EasymenuNGActivity.TAG,"JSON exception: "+e.getMessage());
			//}
		}
		else {

			printError(respIn.getError());
			
		}
		
	} 
	
	public void syncFullMenu(){
		syncClientes();
		syncProductos();
		syncVendedor();
		syncUnidad_Medida();
		syncUsuarios();
		//syncImages();
	}
	
	public void syncData(){
	/*	syncMenus();
		syncItems();
		syncCategories();
		syncMenulists();*/
	}
	
	private void printError(JSONRPC2Error err){
//		Log.e(EasymenuNGActivity.TAG,"The request failed :");
//		Log.e(EasymenuNGActivity.TAG,"\terror.code    : " + err.getCode());
//		Log.e(EasymenuNGActivity.TAG,"\terror.message : " + err.getMessage());
//		Log.e(EasymenuNGActivity.TAG,"\terror.data    : " + err.getData());
	}

	private void deleteRecursive(File fileOrDirectory) {
	    for (File child : fileOrDirectory.listFiles(new MyFileFilter()))
	    	child.delete();
	}
	
	private class MyFileFilter implements FileFilter{

		@Override
		public boolean accept(File pathname) {
			return pathname.getName().endsWith(".img");
		}
		
	}
	
	private static String convertStreamToString(InputStream is) {
        /*
         * To convert the InputStream to String we use the BufferedReader.readLine()
         * method. We iterate until the BufferedReader return null which means
         * there's no more data to read. Each line will appended to a StringBuilder
         * and returned as String.
         */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
 
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
 
	
	public JSONArray connect(String url)
    {JSONArray jsonArray=null;
 
        HttpClient httpclient = new DefaultHttpClient();
 
        // Prepare a request object
        HttpGet httpget = new HttpGet(url); 
 
        // Execute the request
        HttpResponse response;
        try {
            response = httpclient.execute(httpget);
            // Examine the response status
            Log.i("Praeda",response.getStatusLine().toString());
 
            // Get hold of the response entity
            HttpEntity entity = response.getEntity();
            // If the response does not enclose an entity, there is no need
            // to worry about connection release
 
            if (entity != null) {
 
                // A Simple JSON Response Read
                InputStream instream = entity.getContent();
                String result= convertStreamToString(instream);
                Log.i("Praeda",result);
          //      ArrayList<String> stringArray = new ArrayList<String>();
               jsonArray = new JSONArray(result);
               
               /* for(int i = 0, count = jsonArray.length(); i< count; i++)
                {
                	HashMap<String, String> map = new HashMap<String, String>();
                    try {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        stringArray.add(jsonObject.toString());

                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }*/
                // A Simple JSONObject Creation
          //     JSONObject json=new JSONObject(result);
         //      Log.i("Praeda","<jsonobject>\n"+json.toString()+"\n</jsonobject>");
 
                // A Simple JSONObject Parsing
          //      JSONArray nameArray=json.names();
          //      JSONArray valArray=json.toJSONArray(nameArray);
            /*    for(int i=0;i<valArray.length();i++)
                {
                    Log.i("Praeda","<jsonname"+i+">\n"+nameArray.getString(i)+"\n</jsonname"+i+">\n"
                            +"<jsonvalue"+i+">\n"+valArray.getString(i)+"\n</jsonvalue"+i+">");
                }
 
                // A Simple JSONObject Value Pushing
                json.put("sample key", "sample value");
                Log.i("Praeda","<jsonobject>\n"+json.toString()+"\n</jsonobject>");
 */
                // Closing the input stream will trigger connection release
                instream.close();
                
             
            }
 
 
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		return jsonArray;
    }
	
	
	 public void Sync_tabla_politica_precio2(){
			
		 JSONArray json_precios=connect(url+"/getpoliticaprecio2/1");
		    
		    try{
		    	
		    	  
		    	  dbc.getReadableDatabase().delete(DBtables.Politica_precio2.TAG, null, null);
		    	  SQLiteDatabase db = dbc.getWritableDatabase();
		    	 
		    	  for(int i = 0, count = json_precios.length(); i< count; i++)
	                {
	                
	                    try {
	                        JSONObject jsonObject = json_precios.getJSONObject(i);
	                        ContentValues cv = new ContentValues();
	                        cv.put(DBtables.Politica_precio2.PK_SECUENCIA,jsonObject.getString(DBtables.Politica_precio2.PK_SECUENCIA)); 
			    	        cv.put(DBtables.Politica_precio2.ITEM, jsonObject.getString(DBtables.Politica_precio2.ITEM)); 
			    	        cv.put(DBtables.Politica_precio2.PK_CODPRO, jsonObject.getString(DBtables.Politica_precio2.PK_CODPRO)); 
			    	        cv.put(DBtables.Politica_precio2.PREPRO, jsonObject.getString(DBtables.Politica_precio2.PREPRO)); 
			    	        cv.put(DBtables.Politica_precio2.PREPRO_UNIDAD, jsonObject.getString(DBtables.Politica_precio2.PREPRO_UNIDAD)); 
			    	        
			    			db.insert(DBtables.Politica_precio2.TAG, null, cv);
	                   
	                    }
	                    catch (JSONException e) {
	                        e.printStackTrace();
	                    }
	                }
		    	  db.close();
			    	 Log.i("politica_precio2", "sincronizada");
		    
		    	 
		    }catch(Exception e){
		    	e.printStackTrace();
		    }   		
		}
	 

}
