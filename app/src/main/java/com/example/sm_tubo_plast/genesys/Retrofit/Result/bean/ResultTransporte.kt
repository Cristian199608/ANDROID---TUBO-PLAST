package com.example.sm_tubo_plast.genesys.Retrofit.Result.bean

data class ResultTransporte(
val codigo_transportista: String,
val nombre: String,
val ruc: String,
val telefono: String,
val email: String,
val activo: String,
val sucursal: List<SucursalTransportista>
)
data class SucursalTransportista(
    val codigo_sucursal: String,
    val nombre: String,
    val direccion: String,
    val departamento: String,
    val provincia: String,
    val distrito: String
)