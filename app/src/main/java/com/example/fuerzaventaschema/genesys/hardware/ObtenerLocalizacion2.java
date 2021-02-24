package com.example.fuerzaventaschema.genesys.hardware;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.example.fuerzaventaschema.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ObtenerLocalizacion2 implements android.location.LocationListener{
    private static final String TAG = "ObtenerLocalizacion2";
    LocationManager mlocManager;
    Activity activity;
    boolean UBICACION_OK=true;

    AlertDialog.Builder dialog;
    InterfaceUbicacion myListener;

    public ObtenerLocalizacion2(Activity activity) {
        this.activity = activity;
        if (this.activity instanceof ObtenerLocalizacion2.InterfaceUbicacion) {
            myListener = (ObtenerLocalizacion2.InterfaceUbicacion) this.activity;
        } else {
            throw new RuntimeException(this.activity.toString()
                    + " must implement ObtenerLocalizacion2.InterfaceUbicacion");
        }
        locationStart();
    }

    @Override
    public void onLocationChanged(Location location) {
        UbicacionCambiado(location);
    }
    private void UbicacionCambiado(Location location){
        String txtDir=obtener_direccion(location);
        myListener.Location_cambiado(location, txtDir, true);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        switch (status) {
            case LocationProvider.AVAILABLE:
                Log.d("debug", "LocationProvider.AVAILABLE");
                if (!UBICACION_OK){
                    myListener.Location_cambiado(null, "Obtenenido datos...\n", false);
                    UBICACION_OK=true;
                }

                break;
            case LocationProvider.OUT_OF_SERVICE:
                Toast.makeText(activity, "GPS fuera de servicio", Toast.LENGTH_SHORT).show();
                myListener.Location_cambiado(null, "GPS fuera de servicio", false);
                UBICACION_OK=false;
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                Toast.makeText(activity, "GPS temporalmente fuera de servicio", Toast.LENGTH_SHORT).show();
                myListener.Location_cambiado(null, "GPS temporalmente fuera de servicio", false);
                UBICACION_OK=false;
                break;
        }
    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(activity, "GPS Listo para utilizar...", Toast.LENGTH_SHORT).show();
        myListener.Location_cambiado(null, "GPS Listo para utilizar", true);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(activity, "GPS Desactivado", Toast.LENGTH_SHORT).show();
        myListener.Location_cambiado(null, "GPS Desactivado.", false);
        GoConfigureDispositivoGPS();
    }

    public String obtener_direccion(Location loc) {
        String direccion_larga="";
        //Obtener la direccion de la calle a partir de la latitud y la longitud
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);
                    direccion_larga=DirCalle.getAddressLine(0);
                }
                if (direccion_larga.length()==0) direccion_larga=""+loc.getLatitude()+", "+loc.getLongitude();
            } catch (IOException e) {
                direccion_larga="No se ha podido obtener la dirreción de su ubición.\nCoordenada obtenido: "+loc.getLatitude()+", "+loc.getLongitude();
                e.printStackTrace();
            }
        }
        return direccion_larga;
    }

    private void locationStart() {
        mlocManager = (LocationManager) activity.getSystemService(activity.LOCATION_SERVICE);
        try {
            final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (!gpsEnabled) {
                GoConfigureDispositivoGPS();
            }
            if (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
                return;
            }
            UbicacionCambiado(mlocManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
        } catch (Exception e) {
            Toast.makeText(activity, "No se ha podido levantar el servicio de ubicación", Toast.LENGTH_SHORT).show();
            Log.e(TAG, e.getMessage());
        }
    }
    public  void activarGPS(boolean activar){
        if (activar){
            final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (!gpsEnabled) {
                GoConfigureDispositivoGPS();
            }else {
                if (mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    if (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        return;
                    }
                    mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1,this);
                }
            }
        }else{
            mlocManager.removeUpdates(this);
        }
    }

    private void GoConfigureDispositivoGPS(){
        if (dialog==null){
            dialog=new AlertDialog.Builder(activity);
            dialog.setIcon(activity.getResources().getDrawable(R.mipmap.ic_launcher));
            dialog.setTitle("GPS Desactivado");
            dialog.setMessage("En este momento su GPS esta desactivado, para poder obtener su ubicación debe activarlo");
            dialog.setPositiveButton("Deseo ir para activar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    activity.startActivity(settingsIntent);
                    dialog=null;
                }
            });

            dialog.setNegativeButton("Omitir", null);
            dialog.show();
            myListener.Location_cambiado(null, "GPS temporalmente fuera de servicio", false);
        }

    }

    public interface InterfaceUbicacion{
        void Location_cambiado(Location ubicacion, String txtDireccion, boolean isDireccion);
    }


}
