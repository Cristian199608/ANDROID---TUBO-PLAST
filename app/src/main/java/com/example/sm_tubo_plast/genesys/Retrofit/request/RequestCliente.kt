package com.example.sm_tubo_plast.genesys.Retrofit.request

import android.util.Log

class RequestCliente {
    companion object{
        fun getBaseUrl(baseUrl : String): String {
            var jsonBody = "{\n" +
                    "\"url\":\"$baseUrl\""+
                    "}";
            return jsonBody;
        }
        fun lista(baseUrl: String): String {
            return getBaseUrl(baseUrl);
        }
        fun listaLugarEntrega(baseUrl: String): String {
            return getBaseUrl(baseUrl);
        }

        fun listaCuentaXcobrar(baseUrl: String): String = getBaseUrl(baseUrl)
        fun listaCondicionVenta(baseUrl: String): String = getBaseUrl(baseUrl)

        fun listaComprobantexCliente(baseUrl:String,
                                     codcli: String,
                                     fechaStartYYMMDD: String,
                                     fechaEndYYMMDD: String):String{
            var jsonBody= "{\n" +
                    "    \"url\":\"$baseUrl\",\n" +
                    "    \"data\":{\n" +
                    "  \"cod_cliente\": \"$codcli\",\n" +
                    "  \"fecha_desde\": \"$fechaStartYYMMDD\",\n" +
                    "  \"fecha_hasta\": \"$fechaEndYYMMDD\"\n" +
                    "   }\n" +
                    "}";
            Log.i("RequestCLiente: ", "PETICION DATA $jsonBody")
            return jsonBody;
        }

        fun descargaComprobanteBytes(baseUrl:String,
                                     tipoDocumento: String,
                                     serieDoc: String,
                                     numDoc: String):String{
            var jsonBody= "{\n" +
                    "    \"url\":\"$baseUrl\",\n" +
                    "    \"method\":\"POST\",\n" +
                    "    \"data\": {\n" +
                    "       \"numero_documento_emisor\": \"20514364665\",\n" +
                    "       \"tipo_documento\": \"$tipoDocumento\",\n" +
                    "       \"serie\": \"$serieDoc\",\n" +
                    "       \"numero\": \"$numDoc\"\n" +
                    "   }"+
                    "}";
            Log.i("RequestCLiente: ", "PETICION DATA $jsonBody")
            return jsonBody;
        }
    }
}