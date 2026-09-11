package com.example.sm_tubo_plast.genesys.Retrofit.Result.bean.ResultCoprobanteBytes

data class ResultCoprobanteBytesError(
    val detail: Detail?
)

data class Detail(
    val success: Boolean,
    val codigo: String?,
    val mensaje: String?
)