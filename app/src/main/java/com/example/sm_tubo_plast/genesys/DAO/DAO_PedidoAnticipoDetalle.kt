package com.example.sm_tubo_plast.genesys.DAO

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.sm_tubo_plast.genesys.BEAN.PedidoAnticipoDetalle
import com.example.sm_tubo_plast.genesys.datatypes.DBclasses
import com.example.sm_tubo_plast.genesys.datatypes.DBtables

@SuppressLint("LongLogTag")
class DAO_PedidoAnticipoDetalle {
    companion object{
        const val TAG="DAO_PedidoAnticipoDetalle";
    }
    var dBclasses:DBclasses;
    constructor(dBclasses:DBclasses){
        this.dBclasses=dBclasses;
    }

    fun deleteAllEnviados(codven: String) {
        try {
            val where = """ oc_numero not in (
            select oc_numero from pedido_cabecera where flag not in (?) or cod_emp <> ?)"""
            val args = arrayOf("P", codven)

            val db = dBclasses.writableDatabase
            db.delete(""+DBtables.PedidoAnticipoDetalle.TAG,  where, args)
            db.close()
            Log.i(TAG, "deleteAll:: Datos Limpiados "+DBtables.PedidoAnticipoDetalle.TAG);
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun deleteBy( ocNum: String) {
        try {
            val where = """ oc_numero = ? """
            val args = arrayOf(ocNum)

            val db = dBclasses.writableDatabase
            db.delete(""+DBtables.PedidoAnticipoDetalle.TAG,  where, args)
            db.close()
            Log.i(TAG, "deleteAll:: Datos Limpiados "+DBtables.PedidoAnticipoDetalle.TAG);
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun insertAll(_db: SQLiteDatabase?,lista: ArrayList<PedidoAnticipoDetalle>) : Boolean{
        var STAG="insertAll";

        val db = _db?:dBclasses.writableDatabase
        if(_db==null)db.beginTransaction()
        var ok:Boolean=false;
        try {
            lista.forEach {item->
                val values = ContentValues()
                values.put("oc_numero",  item.oc_numero )
                values.put("serie_doc",  item.serie_doc )
                values.put("numero_doc",  item.numero_doc )
                values.put("monto",  item.monto )
                val x = db.insert("" + DBtables.PedidoAnticipoDetalle.TAG, null, values)
                Log.i(TAG, STAG+" insercion " + item.getPkValue() + " " + (x > 0))
            }
            if(_db==null){
                db.setTransactionSuccessful();
            }
            ok=true;
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if(_db==null){
                db.endTransaction() // Finaliza la transacción
                db.close()
            }
        }
        return ok;
    }

    fun getDataBy(ocNum:String): ArrayList<PedidoAnticipoDetalle> {
        val rawQuery: String
        rawQuery = "select  peAnticipo.oc_numero" +
                ",peAnticipo.serie_doc " +
                ",peAnticipo.numero_doc " +
                ",peAnticipo.monto  " +
                "from "+DBtables.PedidoAnticipoDetalle.TAG +" peAnticipo "+
                "where peAnticipo.oc_numero= '$ocNum'";
        Log.d(TAG, "getData:: SQL: "+rawQuery)

        var lista: ArrayList<PedidoAnticipoDetalle> = ArrayList();
        var item: PedidoAnticipoDetalle? = null
        val db = dBclasses.readableDatabase
        val cur = db.rawQuery(rawQuery, null)
        while (cur.moveToNext()) {
            item = PedidoAnticipoDetalle()
            item.oc_numero = cur.getString(cur.getColumnIndex("oc_numero"))
            item.serie_doc = cur.getString(cur.getColumnIndex("serie_doc"))
            item.numero_doc = cur.getString(cur.getColumnIndex("numero_doc"))
            item.monto = cur.getDouble(cur.getColumnIndex("monto"))
            lista.add(item);
        }
        cur.close()
        db.close()
        return lista;
    }


}