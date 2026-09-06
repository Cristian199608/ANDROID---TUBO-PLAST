package com.example.sm_tubo_plast.genesys.Retrofit.Result.bean

class ResultComprobantesDTO (
    var estado:String,
    val comprobantes: ArrayList<ResultComprobante>,
)

data class ResultComprobante(
    val tipo_documento: String,
    val serie: String,
    val numero: String,
    val fecha_emision: String,
    val cliente: String,
    val ruc: String,
    val importe_total: Double,
    val moneda: String,
    val estado: String
)