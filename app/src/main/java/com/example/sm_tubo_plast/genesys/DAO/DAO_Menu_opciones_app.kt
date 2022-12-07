package com.example.sm_tubo_plast.genesys.DAO

import android.content.ContentValues
import android.content.Context
import android.database.SQLException
import android.util.Log
import com.example.sm_tubo_plast.genesys.BEAN.Cliente_estado
import com.example.sm_tubo_plast.genesys.BEAN.Menu_opciones_app
import com.example.sm_tubo_plast.genesys.BEAN.Roles_accesos_app
import com.example.sm_tubo_plast.genesys.datatypes.DBtables
import com.example.sm_tubo_plast.genesys.util.VARIABLES
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper
import java.util.ArrayList

class DAO_Menu_opciones_app(var context: Context) : SQLiteAssetHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        const val TAG = "DAO_ClienteEstado"
        const val DATABASE_NAME = VARIABLES.DATABASA_NAME
        private const val DATABASE_VERSION = VARIABLES.DATABASA_VERSION
    }
    fun DeleteAll() {
        try {
            val db = writableDatabase
            db.delete(""+DBtables.Menu_opciones_app.TAG,  null, null)
            db.close()
            Log.i("DeleteAll", "Datos Limpiados "+DBtables.Menu_opciones_app.TAG);
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }
    fun InsertItem(item: Menu_opciones_app) {
        var STAG="InsertItem";
        val values = ContentValues()
        values.put("codigoOpcion",  item.codigoOpcion )
        values.put("pantallas",  item.pantallas )
        values.put("opciones",  item.opciones )
        values.put("descripcion",  item.descripcion )
        val db = writableDatabase
        val x = db.insert("" + DBtables.Menu_opciones_app.TAG, null, values)
        Log.i(TAG, STAG+" insertacio " + item.codigoOpcion + " " + (x > 0))
    }

    fun getData(nombrePantalla:String, nombreOpcion: String): ArrayList<Menu_opciones_app> {
        val rawQuery: String
        rawQuery = "select  codigoOpcion ,pantallas ,opciones ,descripcion  " +
                "from "+DBtables.Menu_opciones_app.TAG +" me " +
                "where pantallas='"+nombrePantalla+"' and opciones like '"+nombreOpcion+"' "
        Log.d("ALERT-", " .rawQuery")
        val db = readableDatabase
        val cur = db.rawQuery(rawQuery, null)
        var lista =  ArrayList<Menu_opciones_app>();
        var item: Menu_opciones_app? = null
        while (cur.moveToNext()) {
            item = Menu_opciones_app()
            item.codigoOpcion = cur.getString(cur.getColumnIndex("codigoOpcion"))
            item.pantallas = cur.getString(cur.getColumnIndex("pantallas"))
            item.opciones = cur.getString(cur.getColumnIndex("opciones"))
            item.descripcion = cur.getString(cur.getColumnIndex("descripcion"))
            lista.add(item);
        }
        cur.close()
        db.close()
        return lista;
    }
    fun getOpcionPermitido(nombrePantalla:String, nombreOpcion: String): Boolean {
        val rawQuery: String
        rawQuery = "select  codigoOpcion ,pantallas ,opciones ,descripcion  " +
                "from "+DBtables.Menu_opciones_app.TAG +" me " +
                "where pantallas='"+nombrePantalla+"' and opciones like '"+nombreOpcion+"' "
        Log.d("ALERT-", " .rawQuery")
        val db = readableDatabase
        val cur = db.rawQuery(rawQuery, null)
        var lista =  ArrayList<Menu_opciones_app>();
        var item: Menu_opciones_app? = null
        while (cur.moveToNext()) {
            item = Menu_opciones_app()
            item.codigoOpcion = cur.getString(cur.getColumnIndex("codigoOpcion"))
            item.pantallas = cur.getString(cur.getColumnIndex("pantallas"))
            item.opciones = cur.getString(cur.getColumnIndex("opciones"))
            item.descripcion = cur.getString(cur.getColumnIndex("descripcion"))
            lista.add(item);
        }
        cur.close()
        db.close()
        return lista.size>0;
    }






}