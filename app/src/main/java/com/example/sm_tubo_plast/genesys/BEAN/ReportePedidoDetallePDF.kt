package com.example.sm_tubo_plast.genesys.BEAN

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
    var pesoTotalProducto: Double
)