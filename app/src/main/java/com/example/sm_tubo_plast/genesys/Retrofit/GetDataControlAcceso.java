package com.example.sm_tubo_plast.genesys.Retrofit;

import com.example.sm_tubo_plast.genesys.Retrofit.Result.DataRetrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetDataControlAcceso {

    @GET("controller/control_acceso_controller.php")
    Call<DataRetrofit> getControlAcceso( @Query("function") String function, @Query("ruc_empresa") String ruc_empresa);


}
