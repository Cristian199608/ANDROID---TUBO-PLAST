package com.example.sm_tubo_plast.genesys.Retrofit.Result.bean.ResultPedido

data class ResultPedidoSap(
    val resultado: Resultado,
    val pedido: Pedido
)

data class Resultado(
    val estado: String,
    val codigo: String,
    val mensaje: String
)

data class Pedido(
    val oc_numero: String,
    val numero_pedido_sap: Int,
    val docentry: Int,
    val fecha_procesamiento: String
)