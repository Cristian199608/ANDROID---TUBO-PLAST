package com.example.sm_tubo_plast.genesys.DAO

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.sm_tubo_plast.genesys.BEAN.ItemProducto
import com.example.sm_tubo_plast.genesys.BEAN.Pedido_detalle2
import com.example.sm_tubo_plast.genesys.datatypes.DBclasses
import com.example.sm_tubo_plast.genesys.datatypes.DBtables

@SuppressLint("Range")
class DAO_Pedido_detalle2 : DAO_Database {
    private val context: Context;
    private var  codAlmacen:String="0"
    constructor(activity: Context) : super(activity) {
        this.context = activity;
        val  dBclasses=DBclasses(activity);
        codAlmacen=dBclasses.codAlmacen;
        dBclasses.close()
    }
    fun getcodAlmacen(): String{
        return codAlmacen
    }


    //    public void UpdateItem(String codpro, double stock, double xtemp) {
    //        ContentValues up=new ContentValues();
    //        up.put("stock", stock);
    //        up.put("xtemp", xtemp);
    //        String[] arg= {codpro};
    //        SQLiteDatabase db=getWritableDatabase();
    //        long x=db.update("mta_kardex", up, "codpro = ?",  arg);
    //        Log.i(TAG, " actualizaddo "+codpro+" "+(x>0));
    //    }

    fun LimpiarTabla(oc_numero: String) {
        val db = writableDatabase;
        val where = "oc_numero = ?  "
        val args = arrayOf(oc_numero);
        val x = db.delete("" + DBtables.Pedido_detalle2.TAG, where, args)
        Log.i(TAG, " limpiado la tabla by oc_numero $oc_numero? " + " " + (x > 0));
        db.close();
    }

    fun DeleteItemByPromocion(oc_numero: String, secPromo: Int, salida_item: Int) {
        val db = writableDatabase;
        val where = "oc_numero = ? and sec_promo = ? and salida_item = ? "
        val args = arrayOf(oc_numero, secPromo.toString(), salida_item.toString());
        val x = db.delete("" + DBtables.Pedido_detalle2.TAG, where, args)
        Log.i(TAG, " limpiado la tabla? " + " " + (x > 0));
        db.close();
    }


    fun InsertItem(item: Pedido_detalle2, myDB: SQLiteDatabase?=null): Boolean {
        var STAG="InsertItem";
        val values = ContentValues()
        values.put("oc_numero",  item.oc_numero )
        values.put("sec_promo",  item.sec_promo )
        values.put("codpro",  item.codpro )
        values.put("salida_item",  item.salida_item )
        values.put("cantidad",  item.cantidad )
        values.put("precio_lista",  item.precio_lista )
        values.put("pctj_desc",  item.pctj_desc )
        values.put("pctj_extra",  item.pctj_extra )
        values.put("precio_unit",  item.precio_unit )
        values.put("precio_neto",  item.precio_neto )
        values.put("descuento",  item.descuento )
        values.put("peso_total",  item.peso_total )

        val db = myDB ?: writableDatabase
        val x = db.insert("" + DBtables.Pedido_detalle2.TAG, null, values)
        Log.i(TAG, STAG+" insertacio " + item.oc_numero + " " + (x > 0))
        if(myDB==null) db.close()
        return x > 0;
    }

    fun getCantidadUsadoByPromocion(oc_numero: String, secPromo: Int): Int {
        val rawQuery: String
        rawQuery = "select  sum(pe.cantidad) as cantidad " +
                "from " + DBtables.Pedido_detalle2.TAG + " pe "+
                "where pe.oc_numero='" + oc_numero + "' and pe.sec_promo='" + secPromo + "'";
        Log.d("ALERT-", " .rawQuery " + rawQuery)
        var lista: ArrayList<Pedido_detalle2> = ArrayList();
        var item: Pedido_detalle2? = null
        val db = readableDatabase
        val cur = db.rawQuery(rawQuery, null);
        var cantidad=0;
        if (cur.moveToNext()) {
            cantidad=cur.getInt(0);
        }
        cur.close();
        db.close();
        return cantidad;
    }
    fun getDataView(oc_numero: String, secPromo: Int, salidaItem: Int): ArrayList<Pedido_detalle2> {
        val rawQuery: String
        rawQuery = "select  " +
                "pe.oc_numero, " +
                "pe.sec_promo, " +
                "pe.salida_item, " +
                "pe.codpro, " +
                "pe.cantidad," +
                "ifnull(p.despro, pe.codpro) as despro," +
                "ifnull(um.desunimed,'¿UND???' ) as desunimed," +
                "pe.precio_lista, " +
                "pe.pctj_desc, " +
                "pe.pctj_extra, " +
                "pe.precio_unit, " +
                "pe.precio_neto, " +
                "pe.descuento, " +
                "pe.peso_total " +
                "from "+DBtables.Pedido_detalle2.TAG +" pe " +
                "left join "+DBtables.Producto.TAG+" p " +
                "on pe.codpro= p.codpro " +
                "left join "+DBtables.Unidad_medida.TAG+" um on um.codunimed=p.codunimed_almacen "+
                "where pe.oc_numero='"+oc_numero+"' and pe.sec_promo='"+secPromo+"' " +
                "and pe.salida_item="+salidaItem+ " ";
        Log.d("ALERT-", " .rawQuery "+rawQuery)
        var lista: ArrayList<Pedido_detalle2> = ArrayList();
        var item: Pedido_detalle2? = null
        val db = readableDatabase
        val cur = db.rawQuery(rawQuery, null)
        while (cur.moveToNext()) {
            item = Pedido_detalle2()
            item.oc_numero = cur.getString(cur.getColumnIndex("oc_numero"))
            item.sec_promo = cur.getInt(cur.getColumnIndex("sec_promo"))
            item.salida_item = cur.getInt(cur.getColumnIndex("salida_item"))
            item.codpro = cur.getString(cur.getColumnIndex("codpro"))
            item.cantidad = cur.getInt(cur.getColumnIndex("cantidad"));
            item.precio_lista = cur.getDouble(cur.getColumnIndex("precio_lista"));
            item.pctj_desc = cur.getDouble(cur.getColumnIndex("pctj_desc"));
            item.pctj_extra = cur.getDouble(cur.getColumnIndex("pctj_extra"));
            item.precio_unit = cur.getDouble(cur.getColumnIndex("precio_unit"));
            item.precio_neto = cur.getDouble(cur.getColumnIndex("precio_neto"));
            item.descuento = cur.getDouble(cur.getColumnIndex("descuento"));
            item.peso_total = cur.getDouble(cur.getColumnIndex("peso_total"));

            var itemProd= ItemProducto();
            itemProd.codprod=item.codpro;
            itemProd.descripcion=cur.getString(cur.getColumnIndex("despro"));
            itemProd.desunimed=cur.getString(cur.getColumnIndex("desunimed"));
            itemProd.peso=cur.getDouble(cur.getColumnIndex("peso_total"));
            item.itemProducto=itemProd;
            lista.add(item);
        }
        cur.close()
        db.close()
        return lista;
    }


    fun getDataViewPedido(oc_numero: String, secPromo: Int, txtBuscar: String): ArrayList<Pedido_detalle2> {


        val rawQuery: String
        rawQuery="SELECT * from (" +

                "SELECT pp.sec_promocion, pp.codpro_bonificacion, pp.codpro_producto,\n" +
                "p.despro, um.desunimed, " +
                "ifnull((SELECT mk.stock-xtemp  FROM "+DBtables.MTA_kardex.TAG+" mk where mk.codalm='"+codAlmacen+"' and mk.codpro=pp.codpro_producto ), 0) as stock,\n" +
                "ifnull(\n" +
                "(select pe.cantidad from "+DBtables.Pedido_detalle2.TAG+" pe " +
                "where pe.oc_numero='"+oc_numero+"' and  pp.sec_promocion= pe.sec_promo " +
                "and pe.codpro = pp.codpro_producto \n" +
                "), 0) as cantidad from promocion_producto pp \n" +
                "inner join "+DBtables.Producto.TAG+" p on pp.codpro_producto= p.codpro\n" +
                "inner join "+DBtables.Unidad_medida.TAG+" um on um.codunimed=p.codunimed_almacen\n" +
                "where pp.sec_promocion= "+secPromo+" and " +
                "(p.codpro like '%"+txtBuscar+"%' or p.despro like '%"+txtBuscar+"%' " +

                ")" +
                ") resultado order by cantidad desc, despro asc";

        Log.d("ALERT-", " .rawQuery "+rawQuery)
        var lista: ArrayList<Pedido_detalle2> = ArrayList();
        var item: Pedido_detalle2? = null
        val db = readableDatabase
        val cur = db.rawQuery(rawQuery, null)
        while (cur.moveToNext()) {
            item = Pedido_detalle2()
            item.oc_numero = oc_numero;
            item.sec_promo = cur.getInt(cur.getColumnIndex("sec_promocion"));
            item.codpro = cur.getString(cur.getColumnIndex("codpro_producto"))
            item.cantidad = cur.getInt(cur.getColumnIndex("cantidad"));

            var itemProd= ItemProducto();
            itemProd.codprod=item.codpro;
            itemProd.descripcion=cur.getString(cur.getColumnIndex("despro"));
            itemProd.desunimed=cur.getString(cur.getColumnIndex("desunimed"));
            itemProd.stock=cur.getDouble(cur.getColumnIndex("stock"));
            item.itemProducto=itemProd;
            lista.add(item);
        }
        cur.close()
        db.close()
        return lista;
    }

    fun getDataByOcnumero(oc_numero: String): ArrayList<Pedido_detalle2> {
        val rawQuery: String
        rawQuery="select * from "+DBtables.Pedido_detalle2.TAG+" pd2 " +
                "where pd2.oc_numero ='"+oc_numero+"' or 'TODOS'='"+oc_numero+"' ";

        Log.d("ALERT-", " .rawQuery "+rawQuery)
        var lista: ArrayList<Pedido_detalle2> = ArrayList();
        var item: Pedido_detalle2? = null
        val db = readableDatabase
        val cur = db.rawQuery(rawQuery, null)
        while (cur.moveToNext()) {
            item = Pedido_detalle2()
            item.oc_numero = cur.getString(cur.getColumnIndex("oc_numero"));
            item.sec_promo = cur.getInt(cur.getColumnIndex("sec_promo"));
            item.salida_item = cur.getInt(cur.getColumnIndex("salida_item"));
            item.codpro = cur.getString(cur.getColumnIndex("codpro"))
            item.cantidad = cur.getInt(cur.getColumnIndex("cantidad"));

            item.precio_lista = cur.getDouble(cur.getColumnIndex("precio_lista"));
            item.pctj_desc = cur.getDouble(cur.getColumnIndex("pctj_desc"));
            item.pctj_extra = cur.getDouble(cur.getColumnIndex("pctj_extra"));
            item.precio_unit = cur.getDouble(cur.getColumnIndex("precio_unit"));
            item.precio_neto = cur.getDouble(cur.getColumnIndex("precio_neto"));
            item.descuento = cur.getDouble(cur.getColumnIndex("descuento"));
            item.peso_total = cur.getDouble(cur.getColumnIndex("peso_total"));



            lista.add(item);
        }
        cur.close()
        db.close()
        return lista;
    }

    fun getData(codpro: String): Pedido_detalle2? {
        val rawQuery: String
        rawQuery = "select  * from "+DBtables.Pedido_detalle2.TAG +" pe --where"
        Log.d("ALERT-", " .rawQuery")
        var item: Pedido_detalle2? = null
        val db = readableDatabase
        val cur = db.rawQuery(rawQuery, null)
        while (cur.moveToNext()) {
            item = Pedido_detalle2()
            item.oc_numero = cur.getString(cur.getColumnIndex("oc_numero"))
            item.sec_promo = cur.getInt(cur.getColumnIndex("sec_promo"))
            item.codpro = cur.getString(cur.getColumnIndex("codpro"))
            item.cantidad = cur.getInt(cur.getColumnIndex("cantidad"))
        }
        cur.close()
        db.close()
        return item;
    }
//    fun InsertPromotionXCombo(oc_numero: String, secPromo: Int, salidaItem: Int, cantidadCombo: Int  ){
//        val dao_promoP = DAO_PromocionDetalleProducto(context)
//        val listaBon = dao_promoP.getDataByID(secPromo)
//        DeleteItemByPromocion(oc_numero, secPromo, salidaItem)
//        for (promocionProducto in listaBon) {
//            InsertItem(Pedido_detalle2(
//                oc_numero,
//                secPromo,
//                salidaItem,
//                promocionProducto.codpro_bonificacion,
//                (promocionProducto.cantidad!! * cantidadCombo)
//                ))
//        }
//    }

    companion object {
        private const val TAG = "DAO_Pedido_detalle2"
    }
}