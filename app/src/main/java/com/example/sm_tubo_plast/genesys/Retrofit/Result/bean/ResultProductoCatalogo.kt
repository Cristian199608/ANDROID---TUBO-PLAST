package com.example.sm_tubo_plast.genesys.Retrofit.Result.bean

data class ResultProducto(
    val codigo_producto: String,
    val nombre_producto: String,
    val descripcion: String,
    val categoria: String,
    val subcategoria: String,
    val marca: String,
    val codigo_familia: String,
    val codigo_subfamilia: String,
    val unidad_medida: String,
    val estado: String,
    val factor_conversion: Int,
    val tipo_afectacion_igv_default: String,
    val aplica_percepcion: String,
    val valor_percepcion: String,
    val precio_base_sin_igv: String,
    val foto_url: String,
    val ficha_tecnica_pdf_url: String,
    val informacion_logistica: InformacionLogistica,
    val stock: StockData
)

data class InformacionLogistica(
    val Cantidad_Master: String,
    val Cantidad_Pallet: String,
    val volumen: String,
    val peso_unitario: String
)

data class StockData(
    val disponible: Int,
    val stock: Int,
    val transito: Int,
    val comprometido: Int,
)
