package com.example.sm_tubo_plast.genesys.fuerza_ventas.Google.service;

import android.app.Activity;
import android.app.ProgressDialog;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Locale;

public class GetGeoreferenciaByDireccion extends AsyncTask<Void, LatLng, LatLng> {

    Activity activity;
    String direccion;

    MyCallback myCallback;
    ProgressDialog progressDialog=null;
    private boolean showLoading=false;
    public interface MyCallback{
        void result(LatLng latlng);
    }
    public GetGeoreferenciaByDireccion(Activity activity, String direccion, MyCallback myCallback) {
        this.activity = activity;
        this.direccion = direccion;
        this.myCallback = myCallback;
    }

    public void setShowLoading(boolean show){
        this.showLoading=show;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
            if(!showLoading)return;
        progressDialog=new ProgressDialog(activity);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Buscando geoferencia...");
        progressDialog.show();
    }

    @Override
    protected LatLng doInBackground(Void... voids) {
        Geocoder geocoder = new Geocoder(activity, Locale.getDefault());

        try {
            List<Address> direcciones = geocoder.getFromLocationName(direccion, 1);
            if (direcciones != null && !direcciones.isEmpty()) {
                Address direccionEncontrada = direcciones.get(0);
                double latitud = direccionEncontrada.getLatitude();
                double longitud = direccionEncontrada.getLongitude();
                Log.d("Coordenadas", "Latitud: " + latitud + ", Longitud: " + longitud);
                return new LatLng(latitud, longitud);
            } else {
                Log.d("Coordenadas", "No se encontraron resultados para la dirección.");
                return new LatLng(0,0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Coordenadas", "Error al obtener coordenadas: " + e.getMessage());
            return null;
        }
    }

    @Override
    protected void onPostExecute(LatLng latLng) {
        super.onPostExecute(latLng);
        if(showLoading)progressDialog.dismiss();
        myCallback.result(latLng);
    }
}