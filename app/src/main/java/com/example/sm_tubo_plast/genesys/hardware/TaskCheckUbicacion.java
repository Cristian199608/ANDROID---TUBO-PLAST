package com.example.sm_tubo_plast.genesys.hardware;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;

public class TaskCheckUbicacion  {

    private static final int UN_MIN = 20 * 60;
    private static final int VEINTE_MIN = UN_MIN * 20;
    private static final int METROS = 10000; //10KM
    LocationManager locationManager;
    Activity activity;
    MyListener myListener;
    MyLocationListener providerListener_GPS;

    public TaskCheckUbicacion(Activity activity, MyListener myListener) {
        this.activity = activity;
        this.myListener = myListener;
        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        providerListener_GPS = new MyLocationListener();
        myListener.result(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER));
        RequestLocationUpdates();

    }
    public void RemoveLocation(){
        if (providerListener_GPS!=null){
            locationManager.removeUpdates(providerListener_GPS);
        }
    }
    public void RequestLocationUpdates(){
        if (providerListener_GPS!=null){
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    ) {
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, VEINTE_MIN, METROS, providerListener_GPS);
        }
    }

    class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {
            myListener.result(true);
        }

        @Override
        public void onProviderDisabled(String provider) {
            myListener.result(false);
        }
    }

    public interface MyListener{
        void result(boolean isOk);
    }
}