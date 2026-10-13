package com.example.sm_tubo_plast.genesys.DAO

import android.annotation.SuppressLint
import android.content.ContentValues
import android.util.Log
import com.example.sm_tubo_plast.genesys.BEAN.DescuentoPreciosProducto
import com.example.sm_tubo_plast.genesys.datatypes.DBclasses
import com.example.sm_tubo_plast.genesys.datatypes.DBtables
import java.sql.SQLException

@SuppressLint("LongLogTag")
class DAO_DescuentoPreciosProducto {
    companion object{
        const val TAG="DAODescuentoPreciosProducto";
    }
    var dBclasses:DBclasses;
    constructor(dbClass:DBclasses){
        this.dBclasses=dbClass;
    }

    fun deleteAll() {
        try {
            val db = dBclasses.writableDatabase
            db.delete(""+DBtables.DescuentoPreciosProducto.TAG,  null, null)
            db.close()
            Log.i(TAG, "deleteAll:: Datos Limpiados "+DBtables.DescuentoPreciosProducto.TAG);
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    fun insertAll(lista: ArrayList<DescuentoPreciosProducto>):Boolean {
        var STAG="insertAll";

        val db = dBclasses.writableDatabase
        var isOK=false;
        try {
            db.beginTransaction()
            lista.forEach {item->
                val values = ContentValues()
                values.put("secuencia",  item.secuencia )
                values.put("familia",  item.familia )
                values.put("grupo",  item.grupo )
                values.put("porcentaje",  item.porcentaje )
                val x = db.insertOrThrow("" + DBtables.DescuentoPreciosProducto.TAG, null, values)
                //Log.i(TAG, STAG+" insercion " + item.put_pk + " " + (x > 0))
            }
            db.setTransactionSuccessful();
            isOK=true;
        } catch (e: Exception) {
            isOK=false;
            e.printStackTrace()
        } finally {
            db.endTransaction() // Finaliza la transacción
            db.close();
        }
        return isOK;
    }

    fun getData(familiaProd:String, grupoCliente:String): DescuentoPreciosProducto? {
        val rawQuery: String
        rawQuery = "select  dePrecios.secuencia ,dePrecios.familia ,dePrecios.grupo ,dePrecios.porcentaje  " +
                "from "+DBtables.DescuentoPreciosProducto.TAG +" dePrecios "+
                "where dePrecios.familia= '$familiaProd' "+
                "and dePrecios.grupo= '$grupoCliente' ";

        Log.d(TAG, "getData:: SQL: "+rawQuery)
        var item: DescuentoPreciosProducto? = null
        val db = dBclasses.readableDatabase
        val cur = db.rawQuery(rawQuery, null)
        while (cur.moveToNext()) {
            item = DescuentoPreciosProducto()
            item.secuencia = cur.getInt(cur.getColumnIndex("secuencia"))
            item.familia = cur.getString(cur.getColumnIndex("familia"))
            item.grupo = cur.getString(cur.getColumnIndex("grupo"))
            item.porcentaje = cur.getDouble(cur.getColumnIndex("porcentaje"))
        }
        cur.close()
        db.close()
        return item;
    }


}