package com.example.sm_tubo_plast.genesys.BEAN_API

import com.example.sm_tubo_plast.genesys.BEAN.ReportePedidoDetallePDF

class CotizacionDetalleApi
{
    var codigo_pedido:String?=null;
    var item:String?=null;
    var cantidad:Int?=null;
    var codigo_producto:String?=null;
    var descr_producto:String?=null;
    var prct_descuento:Double?=null;
    var prct_descuento_extra:Double?=null;
    var precio_venta:Double?=null;
    var prct_igv:Int?=null;
    var totart:Double?=null;
    var peso_total:Double?=null
    get() {
        return if (field != null) {
            field
        } else {
            1.0
        }
    }
    var unida_medida:String?=null
    get() {
        return if (field != null) {
            field
        } else {
            "UND?"
        }
    }

    fun dataApiToObjetDataDetallePDF(): ReportePedidoDetallePDF {
        val reporteDetaPDF= ReportePedidoDetallePDF(
            oc_numero = this.codigo_pedido!!,
            item = this.item!!,
            codpro = this.codigo_producto!!,
            cantidad = this.cantidad!!,
            unidad_medida = this.unida_medida!!,
            despro = this.descr_producto!!,
            precio_bruto = this.precio_venta.toString(),
            precio_neto = this.totart.toString(),
            porcentaje_desc = this.prct_descuento.toString(),
            porcentaje_desc_extra = this.prct_descuento_extra!!.toDouble(),
            pesoTotalProducto = this.peso_total!!

        );
        return reporteDetaPDF;
    }
}