package com.example.sm_tubo_plast.genesys.Retrofit.request.producto

import com.example.sm_tubo_plast.genesys.Retrofit.request.RequestCantol
import com.example.sm_tubo_plast.genesys.Retrofit.request.RequestCliente
import okhttp3.MediaType
import okhttp3.RequestBody

class RequestProducto{
    companion object{
        fun catalogo(): String {

            var jsonBody = "{\n" +
                    "  \"fecha_actualizacion\": \"\",\n" +//20260629
                    "  \"categoria\": \"\",\n" +
                    "  \"marca\": \"\",\n" +
                    "  \"estado\": \"Y\"\n" +
                    "}";
            return jsonBody;
        }

        fun getListaStock(baseUrl:String): String {
            var jsonBody = "{\n" +
                    "\"url\":\"$baseUrl\""+
                    "}";
            return jsonBody;
        }
        fun getDataByUrl(baseUrl: String): String = RequestCliente.getBaseUrl(baseUrl)
    }
}