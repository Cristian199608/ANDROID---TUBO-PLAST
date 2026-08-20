package com.example.sm_tubo_plast.genesys.Retrofit.request

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
    }
}