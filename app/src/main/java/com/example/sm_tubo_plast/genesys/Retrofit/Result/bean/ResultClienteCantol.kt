package com.example.sm_tubo_plast.genesys.Retrofit.Result.bean

import com.google.gson.annotations.SerializedName

data class ResultClienteCantol(
    @SerializedName("codigo_cliente")
    val codigoCliente: String,

    @SerializedName("tipo_documento")
    val tipoDocumento: String,

    @SerializedName("numero_documento")
    val numeroDocumento: String,

    @SerializedName("nombre_legal")
    val nombreLegal: String,

    @SerializedName("nombre_comercial")
    val nombreComercial: String,

    @SerializedName("persona")
    val persona: String,

    @SerializedName("canal_venta")
    val canalVenta: String,

    @SerializedName("canal_venta_nombre")
    val canalVentaNombre: String,

    @SerializedName("segmento_cliente_codigo")
    val segmentoClienteCodigo: String,

    @SerializedName("segmento_cliente")
    val segmentoCliente: String,

    @SerializedName("valido")
    val valido: String,

    @SerializedName("correo_principal")
    val correoPrincipal: String,

    @SerializedName("telefono_principal")
    val telefonoPrincipal: String,

    @SerializedName("fecha_alta")
    val fechaAlta: String,

    @SerializedName("linea_credito_aprobado")
    val lineaCreditoAprobado: String,

    @SerializedName("linea_credito_disponible")
    val lineaCreditoDisponible: String,

    @SerializedName("fecha_ultima_compra")
    val fechaUltimaCompra: String,

    @SerializedName("importe_ultima_compra")
    val importeUltimaCompra: String,

    @SerializedName("departamento")
    val departamento: String,

    @SerializedName("provincia")
    val provincia: String,

    @SerializedName("distrito")
    val distrito: String
)