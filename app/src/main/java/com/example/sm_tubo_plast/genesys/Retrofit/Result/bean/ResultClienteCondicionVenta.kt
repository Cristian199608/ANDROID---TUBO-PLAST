package com.example.sm_tubo_plast.genesys.Retrofit.Result.bean

data class ResultClienteCondicionVenta(
    val codigo_cliente: String,
//    val codigo_vendedor: Int,
//    val nombre_vendedor: String,
    val condicion_pago: CondicionPago,
    val canal: String,
    val sub_canal: String,
    val ruta: String,
    val frecuencia_visita: String,
    val dia_visita: String,
    val condicion_venta: String,
    val descuento_maximo: String,
    val estado_bloqueo: String
)

data class CondicionPago(
    val codigo_codigo_pago: Int,
    val descripcion: String
)
