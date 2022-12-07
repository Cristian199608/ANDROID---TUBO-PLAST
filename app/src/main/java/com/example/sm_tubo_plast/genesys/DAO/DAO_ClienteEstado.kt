package com.example.sm_tubo_plast.genesys.DAO

import android.content.ContentValues
import android.content.Context
import android.database.SQLException
import android.util.Log
import com.example.sm_tubo_plast.genesys.BEAN.Cliente_estado
import com.example.sm_tubo_plast.genesys.datatypes.DBtables
import com.example.sm_tubo_plast.genesys.util.VARIABLES
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper

class DAO_ClienteEstado(var context: Context) : SQLiteAssetHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        const val TAG = "DAO_ClienteEstado"
        const val DATABASE_NAME = VARIABLES.DATABASA_NAME
        private const val DATABASE_VERSION = VARIABLES.DATABASA_VERSION
    }

    fun InsertItem(item: Cliente_estado) {
        var STAG="InsertItem";
        val values = ContentValues()
        values.put("codcli",  item.codcli )
        values.put("estado",  item.estado )
        values.put("motivo",  item.motivo )
        values.put("codven",  item.codven )
        values.put("fec_server",  item.fec_server )
        val db = writableDatabase
        val x = db.insert("" + DBtables.Cliente_estado.TAG, null, values)
        Log.i(TAG, STAG+" insertacio " + item.codcli + " " + (x > 0))
    }
    fun DeleteAll() {
        try {
            val db = writableDatabase
            db.delete(""+DBtables.Cliente_estado.TAG,  null, null)
            db.close()
            Log.i("DeleteAll", "Datos Limpiados "+DBtables.Cliente_estado.TAG);
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    fun DeleteItemByCod( codcli:String) {
        val where = "codcli = ? "
        val args = arrayOf(""+codcli)
        try {
            val db = writableDatabase
            db.delete(""+DBtables.Cliente_estado.TAG, where, args)
            db.close()
            Log.i("eliminarNulos", "pedido eliminado")
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        //String query = "delete from pedido_cabecera where percepcion_total ISNULL or monto_total ISNULL";aa
    }
    fun UpdateEstadoCliente( codcli:String, estado: String) {
        val where = "codcli = ? "
        val args = arrayOf(""+codcli)
        try {
            val db = writableDatabase
            var reg=ContentValues();
            reg.put("stdcli", estado)
            db.update(""+DBtables.Cliente.TAG, reg, where, args)
            db.close()
            Log.i("eliminarNulos", "pedido eliminado")
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        //String query = "delete from pedido_cabecera where percepcion_total ISNULL or monto_total ISNULL";aa
    }

    fun InsertOrReplaceItem(item: Cliente_estado): Boolean {
        DeleteItemByCod(""+item.codcli);
        InsertItem(item);
        UpdateEstadoCliente(item.codcli!!, item.estado!!);
        return true;
    }

}