package com.example.sm_tubo_plast.genesys.Retrofit;

import android.app.Activity;

import com.example.sm_tubo_plast.genesys.session.SessionManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofilClientCantol {
    private static Retrofit retrofit;
    public static final String BASE_URL_CANTOL = "http://181.224.227.52:8088/";

    public static class UrlPeticiones{
        public static String catalogoProducto=BASE_URL_CANTOL+"ComercialCantol/productos/catalogo";
        public static String listaCliente(String codigVendedor){
            return BASE_URL_CANTOL+"ComercialCantol/vendedor/comercial_clientes/"+codigVendedor;
        }
        public static String listaLugarEntregaCliente(String codigVendedor){
            return BASE_URL_CANTOL+"ComercialCantol/vendedor/comercial_sucursales/"+codigVendedor;
        }

        public static String getListaStockByProducto(String codigoProd){
            return BASE_URL_CANTOL+"ComercialCantol/productos/"+codigoProd+"/stock";
        }
        public static String getListaEstadoCuentasXCobrar(String codigoVendedor){
            return BASE_URL_CANTOL+"ComercialCantol/general/cuentas-x-cobrar/"+codigoVendedor;
        }

        public static String getListaCondicionVentaCliente(String codigoCliente){
            return BASE_URL_CANTOL+"ComercialCantol/vendedor/comercial_info/"+codigoCliente;
        }

        public static String getListaObrasCliente(String codigoCliente){
            return BASE_URL_CANTOL+"ComercialCantol/general/obras/"+codigoCliente;
        }
        public static String getListaTransportes(){
            return BASE_URL_CANTOL+"ComercialCantol/general/transportistas";
        }

        public static String getListaPreciosProducto(String codpro){
            return BASE_URL_CANTOL+"ComercialCantol/productos/"+codpro+"/precios";
        }
        public static String getListaPromociones(){
            return BASE_URL_CANTOL+"ComercialCantol/promociones/vigente";
        }

        public static String getCondicionVenta(){
            return BASE_URL_CANTOL+"ComercialCantol/general/condiciones-venta";
        }
        public static String getConsultaComprobanteCliente(){
            return BASE_URL_CANTOL+"ComercialCantol/general/comprobantes-x-cliente";
        }

        public static String getConsultaDescargaBytePDF(){
            return BASE_URL_CANTOL+"ComercialCantol/comprobantes/descargar";
        }
    }

    public static RequestBody createBodyJson(String jsonRequest){
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                jsonRequest);
        return body;
    }
    public static Retrofit getRetrofitInstance() {
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_CANTOL)
                .addConverterFactory(GsonConverterFactory
                        .create(gson)).client(okHttpClient)
                .build();
        //
        return retrofit;
    }

    public static Retrofit getRetrofitInstanceCantolWithToken(final Activity activity) {


        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        String token=new SessionManager(activity).getToken();
                        Request newRequest = chain.request().newBuilder()
                                .addHeader("Authorization", token)
                                .build();
                        return chain.proceed(newRequest);
                    }
                })
                .build();


        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .setLenient()
                .create();

        retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl("http://38.25.1.50:4391/saemoviles/vista/")
                .addConverterFactory(GsonConverterFactory
                        .create(gson)).client(okHttpClient)
                .build();
        //
        return retrofit;
    }
}
