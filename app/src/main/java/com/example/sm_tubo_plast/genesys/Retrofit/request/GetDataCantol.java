package com.example.sm_tubo_plast.genesys.Retrofit.request;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GetDataCantol {

    @Headers("Content-Type: application/json")
    @POST("ComercialCantol/seguridad/")
    Call<Object> getLoginToken(@Body RequestBody jsonBody) ;


    @Headers("Content-Type: application/json")
    @POST("ws_cantol/cliente.php")
    Call<Object> getCliente(@Body RequestBody jsonBody) ;
}
