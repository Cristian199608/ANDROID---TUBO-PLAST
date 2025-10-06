package com.example.sm_tubo_plast.genesys.DAO

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.example.sm_tubo_plast.genesys.BEAN.PromocionDetalleProducto
import com.example.sm_tubo_plast.genesys.datatypes.DBtables

@SuppressLint("Range")
class DAO_PromocionDetalleProducto(context: Context?) : DAO_Database(context) {


    fun LimpiarTabla() {
        val db = writableDatabase
        val x = db.delete("" + DBtables.PromocionDetalle_producto.TAG, null, null)
        Log.i(TAG, " limpiado la tabla? " + " " + (x > 0));
        db.close();
    }

    fun InsertItem(item: PromocionDetalleProducto) {
        val values = ContentValues()
        values.put("sec_promocion",  item.sec_promocion )
        values.put("codpro_bonificacion",  item.codpro_bonificacion )
        values.put("codpro_producto",  item.codpro_producto )
        values.put("cantidad",  item.cantidad )
        val db = writableDatabase
        val x = db.insert("" + DBtables.PromocionDetalle_producto.TAG, null, values)
        Log.i(TAG, " insertacio " + item.sec_promocion + " " + (x > 0))
    }

    @Throws(java.lang.Exception::class)
    fun PoblarData(lista : ArrayList<PromocionDetalleProducto> ): Unit {
        LimpiarTabla();
        val db = writableDatabase
        try {
            db.beginTransaction()
            lista.forEach {item->
                val values = ContentValues()
                values.put("sec_promocion",  item.sec_promocion )
                values.put("codpro_bonificacion",  item.codpro_bonificacion )
                values.put("codpro_producto",  item.codpro_producto )
                values.put("cantidad",  item.cantidad )
                val x = db.insert("" + DBtables.PromocionDetalle_producto.TAG, null, values)
                if (x.toInt()==0) {
                    throw java.lang.Exception("Error al intentar registrar producto promocion sec_promocion "+item.sec_promocion);
                };
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
        }catch (e: java.lang.Exception) {
            db.endTransaction();
            db.close();
            e.printStackTrace()
            throw java.lang.Exception(e)
        }
    }


    fun getData(): ArrayList<PromocionDetalleProducto>? {
        val rawQuery: String
        rawQuery = "select  * from "+ DBtables.PromocionDetalle_producto.TAG+" pr "
        Log.d("ALERT-", " .rawQuery")
        var lista: ArrayList<PromocionDetalleProducto> = ArrayList();
        var item: PromocionDetalleProducto? = null
        val db = readableDatabase
        val cur = db.rawQuery(rawQuery, null)
        while (cur.moveToNext()) {
            item = PromocionDetalleProducto()
            item.sec_promocion = cur.getInt(cur.getColumnIndex("sec_promocion"))
            item.codpro_bonificacion = cur.getString(cur.getColumnIndex("codpro_bonificacion"))
            item.codpro_producto = cur.getString(cur.getColumnIndex("codpro_producto"))
            item.cantidad = cur.getInt(cur.getColumnIndex("cantidad"))
            lista.add(item);
        }
        cur.close()
        db.close()
        return lista;
    }

    fun getDataByID(secuenciaPromo:Int): ArrayList<PromocionDetalleProducto>  {
        val rawQuery: String
        rawQuery = "select  * from "+ DBtables.PromocionDetalle_producto.TAG+" pr " +
                "where pr.sec_promocion = "+secuenciaPromo+"";
        Log.d("ALERT-", " .rawQuery")
        var lista: ArrayList<PromocionDetalleProducto> = ArrayList();
        var item: PromocionDetalleProducto? = null
        val db = readableDatabase
        val cur = db.rawQuery(rawQuery, null)
        while (cur.moveToNext()) {
            item = PromocionDetalleProducto()
            item.sec_promocion = cur.getInt(cur.getColumnIndex("sec_promocion"))
            item.codpro_bonificacion = cur.getString(cur.getColumnIndex("codpro_bonificacion"))
            item.codpro_producto = cur.getString(cur.getColumnIndex("codpro_producto"))
            item.cantidad = cur.getInt(cur.getColumnIndex("cantidad"))
            lista.add(item);
        }
        cur.close()
        db.close()
        return lista;
    }




    companion object {
        private const val TAG = "DAO_PoliticaTipoCliente"
    }
}