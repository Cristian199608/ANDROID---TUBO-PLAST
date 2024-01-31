package com.example.sm_tubo_plast.genesys.BEAN

data class PedidoCabeceraRecalcular(
    var Oc_numero: String,
    var peso_total: Double,
    var subtotal: Double,
    var IGV: Double,
    var total: Double,
    var percepcion: Double,
    var totalSujetoPercepcion: Double
) {
    constructor(
        oc_numero: String,
        peso_total: Double,
        subtotal: Double,
        IGV: Double,
        total: Double,
        percepcion: Double,
        totalSujetoPercepcion: Double,
        descuento: Double,
        descuentoPercent: Double
    ) : this(oc_numero, peso_total, subtotal, IGV, total, percepcion, totalSujetoPercepcion) {

    }
}
