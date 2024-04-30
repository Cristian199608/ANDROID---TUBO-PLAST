package com.example.sm_tubo_plast.genesys.Retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofilClient {
    private static Retrofit retrofit;
    public static final String BASE_URL_GODADDY = "https://acgenesys.com/servicios/";
    public static Retrofit getRetrofitInstanceGODDADY() {
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .setLenient()
                .create();

        retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl(BASE_URL_GODADDY)
                .addConverterFactory(GsonConverterFactory
                        .create(gson)).client(okHttpClient)
                .build();
        //
        return retrofit;
    }
}
