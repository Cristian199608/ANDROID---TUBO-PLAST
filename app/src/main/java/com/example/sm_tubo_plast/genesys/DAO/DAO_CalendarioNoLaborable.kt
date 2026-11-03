package com.example.sm_tubo_plast.genesys.DAO

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.sm_tubo_plast.genesys.BEAN.CalendarioNoLaborable
import com.example.sm_tubo_plast.genesys.BEAN.LogEnvioPedido
import com.example.sm_tubo_plast.genesys.datatypes.DBclasses
import com.example.sm_tubo_plast.genesys.datatypes.DBtables
import java.sql.SQLException

@SuppressLint("LongLogTag")
class DAO_CalendarioNoLaborable {
    companion object{
        const val TAG="DAO_CalendarioNoLaborable";
    }
    var dBclasses: DBclasses;
    constructor(dBclasses: DBclasses){
        this.dBclasses=dBclasses;
    }

    fun deleteAll() {
        try {
            val db = dBclasses.writableDatabase
            db.delete(""+DBtables.CalendarioNoLaborable.TAG,  null, null)
            db.close()
            Log.i(TAG, "deleteAll:: Datos Limpiados "+DBtables.CalendarioNoLaborable.TAG);
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    fun insertAll(lista: ArrayList<CalendarioNoLaborable>):Boolean {
        var STAG="insertItem";
        val db = dBclasses.writableDatabase
        try {
            var values = ContentValues();
            lista.forEach { item->
                values = ContentValues()
                values.put("id",  item.id )
                values.put("fecha",  item.fecha )
                val x = db.insert("" + DBtables.CalendarioNoLaborable.TAG, null, values)
                Log.i(TAG, STAG+" insercion " + item.fecha + " " + (x > 0))
            }
            db.close()
            return true;
        } catch (e: Exception) {
            e.printStackTrace()
            db.close()
            return false;
        }
    }

    fun getData(): ArrayList<CalendarioNoLaborable> {
        val rawQuery: String
        rawQuery = "select  noLaborable.id ,noLaborable.fecha  " +
                "from "+DBtables.CalendarioNoLaborable.TAG +" noLaborable ";
        Log.d(TAG, "getData:: SQL: "+rawQuery)
        var lista=ArrayList<CalendarioNoLaborable>();
        //-----------------------------------------------------------------------------------------------
        val db = dBclasses.readableDatabase
        val cur = db.rawQuery(rawQuery, null)
        while (cur.moveToNext()) {
            lista.add(
                CalendarioNoLaborable(
                cur.getInt(cur.getColumnIndex("id")),
                cur.getString(cur.getColumnIndex("fecha"))
            ));
        }
        cur.close()
        db.close()
        return lista;
    }


}