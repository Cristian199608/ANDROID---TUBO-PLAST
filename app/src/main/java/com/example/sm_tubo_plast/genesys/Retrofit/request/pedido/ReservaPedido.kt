package com.example.sm_tubo_plast.genesys.Retrofit.request.pedido

data class ReservaPedido(
    val articulo: String,
    val cantidad: Int,
    val canal: String,
    val vendedor: String,
    val pedido: String,
    val vendedor_nombre: String,
    val moneda: String,
    val almacen: String
)
