package com.example.sm_tubo_plast.genesys.Retrofit.Result.bean

import com.google.gson.annotations.SerializedName

data class ResultClienteLugarEntrega(
    val codigo_cliente: String,
    val codigo_punto_entrega: String,
    val nombre_comercial: String,
    val direccion: String,
    val referencia: String,
    val zona: String,
    val distrito: String,
    val provincia: String,
    val departamento: String,
    val latitud: String,
    val longitud: String,
    val telefono: String,
    val contacto: String,
    val cargo_contacto: String
)
