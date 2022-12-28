package com.example.sm_tubo_plast.genesys.Retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofilClient {
    private static Retrofit retrofit;
    public static Retrofit getRetrofitInstanceSERVIDOR() {
        String BASE_URL="http://190.187.25.123:4390/ws_control_acceso_app/";
//        String BASE_URL="http://192.168.0.55/ws_control_acceso_app/";
//
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();

        Gson gson = new GsonBuilder() .setDateFormat("yyyy-MM-dd HH:mm:ss").setLenient().create();

        retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory
                 .create(gson)).client(okHttpClient)
                .build();

        return retrofit;
    }
}
