package com.example.sm_tubo_plast.genesys.fuerza_ventas.Google.service;

import android.app.Activity;
import android.app.ProgressDialog;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GetDireccionByCoordenada extends AsyncTask<Void, String, String> {

    Activity activity;
    LatLng latLng;

    MyCallback myCallback;
    ProgressDialog progressDialog=null;
    private boolean showLoading=false;
    public interface MyCallback{
        void result(String direccionName);
    }
    public GetDireccionByCoordenada(Activity activity, LatLng latLng, MyCallback myCallback) {
        this.activity = activity;
        this.latLng = latLng;
        this.myCallback = myCallback;
    }
    public void setShowLoading(boolean show){
        this.showLoading=show;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        myCallback.result("Buscando...");
        if(!showLoading)return;
        progressDialog=new ProgressDialog(activity);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Buscando dirección...");
        progressDialog.show();
    }

    @Override
    protected String doInBackground(Void... voids) {
        String direccionNombre="";
        if (latLng.latitude != 0.0 && latLng.longitude!= 0.0) {
            try {
                Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        latLng.latitude, latLng.longitude, 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);
                    direccionNombre= DirCalle.getAddressLine(0);
                }
            } catch (IOException e) {
                direccionNombre="Cargando direccion...";
                e.printStackTrace();
            }
        }
        return direccionNombre;
    }

    @Override
    protected void onPostExecute(String direccionNombre) {
        super.onPostExecute(direccionNombre);
        if(showLoading)progressDialog.dismiss();
        myCallback.result(direccionNombre);
    }
}