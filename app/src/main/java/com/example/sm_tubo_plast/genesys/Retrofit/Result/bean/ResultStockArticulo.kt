package com.example.sm_tubo_plast.genesys.Retrofit.Result.bean

data class ResultStockArticulo(
    val codigo_articulo: String,
    val codigo_almacen: String,
    val nombre_almacen: String,
    val en_stock: Int,
    val en_pedido: Int,
    val comprometido: Int,
    val disponible: Int
)
