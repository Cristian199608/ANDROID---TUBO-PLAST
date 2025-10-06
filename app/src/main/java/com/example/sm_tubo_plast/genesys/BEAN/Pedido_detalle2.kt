package com.example.sm_tubo_plast.genesys.BEAN

import com.example.sm_tubo_plast.genesys.fuerza_ventas.PedidosActivity
import com.example.sm_tubo_plast.genesys.util.VARIABLES

class Pedido_detalle2 {

    var oc_numero:String?=null;
    var sec_promo:Int?=null;
    var item_promo:Int?=null;
    var salida_item:Int?=null;
    var codpro:String?=null;
    var cantidad:Int?=null;

    var precio_lista:Double?=null;
    var pctj_desc:Double?=null;
    var pctj_extra:Double?=null;
    var precio_unit:Double?=null;
    var precio_neto:Double?=null;
    var descuento:Double?=null;
    var peso_total:Double?=null;

    var itemProducto: ItemProducto?=null;
    constructor() {
    }//
    constructor(oc_numero: String?, sec_promo: Int?, salida_item: Int, codpro: String?, cantidad: Int?,
               precio_lista: Double?,
                pctj_desc: Double?,
                pctj_extra: Double?,
                precio_unit: Double?,
                precio_neto: Double?,
                descuento: Double?,
                peso_total: Double?) {
        this.oc_numero = oc_numero
        this.sec_promo = sec_promo
        this.item_promo = -1
        this.salida_item=salida_item
        this.codpro = codpro
        this.cantidad = cantidad
        this.precio_lista = precio_lista
        this.pctj_desc = pctj_desc
        this.pctj_extra = pctj_extra
        this.precio_unit = precio_unit
        this.precio_neto = precio_neto
        this.descuento = descuento
        this.peso_total = peso_total
    }
    //

    fun convertirMonedaTo(moneda: String, tipoCambio: Double): Boolean {
        if (moneda == PedidosActivity.MONEDA_SOLES_IN) {
            convertirMonedaToSoles(tipoCambio)
            return true
        } else if (moneda == PedidosActivity.MONEDA_DOLARES_IN) {
            convertirMonedaToDolar(tipoCambio)
            return true
        }
        return false
    }

    private fun convertirMonedaToSoles(tipoCambio: Double) {

        this.precio_lista = VARIABLES.getDoubleFormaterThreeDecimal(this.precio_lista!! * tipoCambio)
        this.precio_unit = VARIABLES.getDoubleFormaterThreeDecimal(this.precio_unit!! * tipoCambio)
        this.__calcularPreciosXCantidades()

    }

    private fun convertirMonedaToDolar(tipoCambio: Double) {
        this.precio_lista = VARIABLES.getDoubleFormaterThreeDecimal(this.precio_lista!! / tipoCambio)
        this.precio_unit = VARIABLES.getDoubleFormaterThreeDecimal(this.precio_unit!! / tipoCambio)
        this.__calcularPreciosXCantidades()
    }

    private fun __calcularPreciosXCantidades() {
        val precioNetoLista =
            VARIABLES.getDoubleFormaterThreeDecimal(this.precio_lista!! * this.cantidad!!)
        val precioNetoVenta =
            VARIABLES.getDoubleFormaterThreeDecimal(this.precio_unit!! * this.cantidad!!)
        precio_neto = precioNetoVenta;
        descuento =  VARIABLES.getDoubleFormaterThreeDecimal(precioNetoLista - precioNetoVenta)
    }

}