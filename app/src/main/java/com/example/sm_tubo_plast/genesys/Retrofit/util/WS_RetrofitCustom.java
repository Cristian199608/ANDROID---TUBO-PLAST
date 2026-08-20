package com.example.sm_tubo_plast.genesys.Retrofit.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;

import com.example.sm_tubo_plast.genesys.hardware.ConectividadInternet;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WS_RetrofitCustom {
    private static final String TAG = "WS_RetrofitCustom";
    private Activity activity;
    private boolean isLogin;

    public WS_RetrofitCustom(Activity activity) {
        this.activity = activity;
    }

    public void StartPeticion(Call<Object> taskService, final MyListener myListener) {
        ConectividadInternet mInterner = new ConectividadInternet(activity);

        myListener.StartFinish(false);
        if (!mInterner.isConnectingToInternet()) {
            myListener.StartFinish(true);
            ResultTryCatch(myListener, false, "Sin conexión a internet", null);
        } else {
            try {
                taskService.enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        myListener.StartFinish(true);
                        if (response.isSuccessful()) {
                            ResultTryCatch(myListener, true, "", response.body());
                            //Log.i("", "mirar " + new Gson().toJson(response))
                        } else {
                            Log.i(TAG, "Error "+new Gson().toJson(response));
                            int statusCode = response.code();
                            if (isLogin) {
                                ResultTryCatch(myListener, false, "Usuario o contraseña incorrecto", null);
                            } else {
                                ResultTryCatch(myListener, false, "El servidor ha devuelto un mensaje de error", null);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        myListener.StartFinish(true);
                        ResultTryCatch(myListener, false, "Error al invocar al servicio", null);
                    }
                });
            } catch (Exception Exx) {
                Exx.printStackTrace();
                myListener.StartFinish(true);
                ResultTryCatch(myListener, false, "Error, no se ha podido conectarse al servicio web. Vuelva a intentar", null);
            }
        }
    }

    public static  ArrayList<Object> StartPeticionYetBackground(Call<Object> taskService) throws Exception {
        ArrayList<Object> listRes=new ArrayList<>();
        Response<Object> response = taskService.execute();
        if (response.isSuccessful()) {
            listRes.add(response.body());
        } else {
            listRes.add("El servidor ha devuelto un mensaje de error");
        }
        return listRes;
    }

    private void ResultTryCatch(MyListener listen, boolean bool, String string, Object objef) {
        try {
            listen.Result(bool, string, objef);
        } catch (final Exception ex) {
            ex.printStackTrace();
            new AlertDialog.Builder(activity)
                    .setTitle("Upp!")
                    .setMessage("Algo salió mal en la petición")
                    .setPositiveButton("Ver el error", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            new AlertDialog.Builder(activity)
                                    .setTitle("Error")
                                    .setMessage(ex.getMessage())
                                    .setPositiveButton("Entendido", null)
                                    .show();
                        }
                    })
                    .show();
        }
    }

    public void setIsLogin(boolean isLogin) {
        this.isLogin=isLogin;
    }

    public interface MyListener {
        void Result(boolean isOk, String mensaje, Object data);
        void StartFinish(boolean isFinish);
    }
}
