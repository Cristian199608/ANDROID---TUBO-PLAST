package com.example.sm_tubo_plast.genesys.Retrofit.Result.bean

data class ResultPromocionDetalle(
    val Codigo_Promocion: String,
    val Descripcion: String,
    val vendedor: List<Any>,
    val cliente: List<Any>,
    val zona: List<Any>,
    val ubigeo: List<Any>,
    val categoria_cliente: List<Any>,
    val canal_cliente: List<Any>,
    val condicion_venta: List<Any>,
    val condicion: String,
    val tipo: String,
    val aplica_acumulado: Boolean,
    val aplica_obligatorio: Boolean,
    val mecanica: List<Mecanica>,
    val tipo_promocion: String,
    val categoria_precio_destino: String,
    val bonificacion: Bonificacion,
    val max_pedido: Int
)

data class Mecanica(
    val codigo_familia: String,
    val descripcion_familia: String,
    val codigo_subfamilia: String,
    val descripcion_subfamilia: String,
    val codpro: String,
    val descripcion_producto: String,
    val cantidad: Int,
    val monto: Int,
    val codigo_unidad_medida: String
)

data class Bonificacion(
    val codpro: String,
    val descripcion_producto: String,
    val cantidad: Int,
    val monto: Int,
    val codigo_unidad_medida: String
)
