package com.example.sm_tubo_plast.genesys.BEAN

class LogEnvioPedido
{
    var estado:String?=null;
    var codigo:Int?=null;
    var mensaje:String?=null;
    var oc_numero:String?=null;
    var fecha_procesamiento:String?=null;

    fun isEnvioExitoso():Boolean{
        return this.estado.equals("ACEPTADO", true)
    }
}