package com.example.sm_tubo_plast.genesys.CreatePDF.model

class ReportePedidoDetallePDF(
    var oc_numero:String,
    var item:String,
    var codpro: String,
    var cantidad: Int,
    var unidad_medida: String,
    var despro: String,
    var precio_bruto: String,
    var precio_neto: String,
    var porcentaje_desc: String,
    var porcentaje_desc_extra: Double,
    var pesoTotalProducto: Double,
    var montoDsctTotal: Double
){

    companion object{
        //crear un metodo para que ordene los datos de la lista, por oc_numero y item, donde reciba una lista
        fun orderOcNumeroAndItem(lista: ArrayList<ReportePedidoDetallePDF>): List<ReportePedidoDetallePDF>{
            var listaOrdenada = lista.sortedWith(compareBy({it.oc_numero}, {it.item.toInt()}))
            return listaOrdenada
        }
    }



}