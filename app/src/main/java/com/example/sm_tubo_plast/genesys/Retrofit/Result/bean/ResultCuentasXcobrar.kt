package com.example.sm_tubo_plast.genesys.Retrofit.Result.bean

data class ResultCuentasXcobrar(
    val codigo_cliente: String,
    val tipo_documento: String,
    val serie_documento: String,
    val numero_documento: String,
    val documento_referencia: String,
    val total: String,
    val saldo: String,
    val fecha_emision: String,
    val fecha_vencimiento: String,
    val observaciones: String
)
