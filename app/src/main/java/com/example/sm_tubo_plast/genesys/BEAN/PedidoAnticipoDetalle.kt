package com.example.sm_tubo_plast.genesys.BEAN

import android.database.sqlite.SQLiteDatabase




class PedidoAnticipoDetalle
{
    var oc_numero:String?=null;
    var serie_doc:String?=null;
    var numero_doc:String?=null;
    var monto:Double?=null;

    fun getPkValue(): String{
        return "numero $numero_doc, serie_doc $serie_doc, numero doc $numero_doc";
    }

    companion object{
        fun obtenerAnticiposPedido(lista: ArrayList<PedidoAnticipoDetalle>): HashMap<String, Double>? {
            val resultado: HashMap<String, Double> = HashMap()
            lista.forEach {
                val key = "${it.serie_doc}|${it.numero_doc}"
                resultado[key] = it.monto!!
            }
            return resultado
        }
    }
}