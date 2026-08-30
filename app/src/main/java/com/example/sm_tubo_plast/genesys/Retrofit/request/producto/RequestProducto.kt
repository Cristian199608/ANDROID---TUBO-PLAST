package com.example.sm_tubo_plast.genesys.Retrofit.request.producto

import android.util.Log
import com.example.sm_tubo_plast.genesys.Retrofit.request.RequestCantol
import com.example.sm_tubo_plast.genesys.Retrofit.request.RequestCliente
import okhttp3.MediaType
import okhttp3.RequestBody

class RequestProducto{
    companion object{
        fun catalogo(baseUrl:String, codven: String, desproLike: String): String {
            var jsonBody= "{\n" +
                    "    \"url\":\"$baseUrl\",\n" +
                    "    \"data\":{\n" +
                    "        \"fecha_actualizacion\": \"\",\n" +//20260629
                    "        \"categoria\": \"\",\n" +
                    "        \"marca\": \"\",\n" +
                    "        \"estado\": \"Y\",\n" +
                    "        \"articulo\": \"$desproLike\",\n" +
                    "        \"vendedor\": \"$codven\"\n" +
                    "    }\n" +
                    "}";
            Log.i("RequestProducto: ", "PETICION DATA $jsonBody")
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