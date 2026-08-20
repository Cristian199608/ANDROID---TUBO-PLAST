package com.example.sm_tubo_plast.genesys.Retrofit.Result.bean

import com.google.gson.annotations.SerializedName

data class ResultPrecioArticulo(
    @SerializedName("codigo articulo")
    val codigoArticulo: String,

    @SerializedName("lista precios codigo")
    val listaPreciosCodigo: Int,

    @SerializedName("lista precios nombre")
    val listaPreciosNombre: String,

    @SerializedName("precio unitario")
    val precioUnitario: String,

    @SerializedName("precio total")
    val precioTotal: String
)
