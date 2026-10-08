package com.example.sm_tubo_plast.genesys.DAO

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.sm_tubo_plast.genesys.BEAN.LogEnvioPedido
import com.example.sm_tubo_plast.genesys.datatypes.DBclasses
import com.example.sm_tubo_plast.genesys.datatypes.DBtables

class DAO_LogEnvioPedidoSAP {
    companion object{
        const val TAG="DAO_LogEnvioPedidoSAP";
    }
    var dBclasses: DBclasses;
    constructor(dBclasses: DBclasses){
        this.dBclasses=dBclasses;
    }

    fun deleteAllEnviados(codven: String) {
        try {
            val where = """ oc_numero in (
            select oc_numero from pedido_cabecera where flag not in (?) or cod_emp <> ?)"""
            val args = arrayOf("P", codven)

            val db = dBclasses.writableDatabase
            db.delete(""+DBtables.LogEnvioPedido.TAG,  where, args)
            db.close()
            Log.i(TAG, "deleteAll:: Datos Limpiados "+DBtables.LogEnvioPedido.TAG);
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun insertItem(db:SQLiteDatabase, item: LogEnvioPedido):Boolean {
        var STAG="insertItem";
        //val db = dBclasses.writableDatabase
        try {
            val values = ContentValues()
            values.put("estado",  item.estado )
            values.put("codigo",  item.codigo )
            values.put("mensaje",  item.mensaje )
            values.put("oc_numero",  item.oc_numero )
            values.put("fecha_procesamiento",  item.fecha_procesamiento )
            val x = db.insertOrThrow("" + DBtables.LogEnvioPedido.TAG, null, values)
            Log.i(TAG, STAG+" insercion " + item.oc_numero + " " + (x > 0))
            return true;
        } catch (e: Exception) {
            e.printStackTrace()
            return false;
        }
    }

}