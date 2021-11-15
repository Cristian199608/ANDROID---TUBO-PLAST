package com.example.sm_tubo_plast.genesys.hardware;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.example.sm_tubo_plast.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

public class LocationApiGoogle implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "LocationApiGoogle";

    public  static final long UPDATE_INTERVAL_3_segundos = 3000;// = 3 seconds
    public  static final long FASTEST_INTERVAL_3_segundos = 3000; // = 3 seconds


    Activity activity;
    LocationManager mlocManager;
    public GoogleApiClient googleApiClient;
    public FusedLocationProviderClient fusedLocationClient;
    public LocationCallback locationCallback;
    LocationRequest locationRequest;
    Location location;
    Listener mylistener;

    public LocationApiGoogle(Activity activity, Listener listener) {
        this.activity = activity;
        this.mylistener = listener;
        googleApiClient = new GoogleApiClient.Builder(activity)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mlocManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
    }

    public void StartLocationCallback(long UPDATE_INTERVAL, long FASTEST_INTERVAL, int TIPO_PRIORIDAD) {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(TIPO_PRIORIDAD);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                List<Location> locationList = locationResult.getLocations();
                if (locationList.size() > 0) {
                    //The last location in the list is the newest
                    location = locationList.get(locationList.size() - 1);
                    Log.i("LocationApiGoogle", "Location: " + location.getLatitude() + " " + location.getLongitude());
                    mylistener.LastLocation(location);
                }
            }
        };
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    public void ForzarUltimaUbicacion() {
        fusedLocationClient = new FusedLocationProviderClient(activity);
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(activity, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    Log.i(TAG, "onLocationChanged2:: Latitude : " + location.getLatitude() + "\nLongitude : " + location.getLongitude());
                }
                mylistener.LastLocation(location);
            }
        });
    }

    public void checkGPSActivate() {

        AlertDialog.Builder   dialog=new AlertDialog.Builder(activity);
        dialog.setIcon(activity.getResources().getDrawable(R.mipmap.ic_launcher_round));
        dialog.setTitle("GPS Desactivado");
        dialog.setMessage("En este momento su GPS esta desactivado, para poder obtener su ubicación debe activarlo");
        dialog.setPositiveButton("ir para activar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                activity.startActivity(settingsIntent);
            }
        });
        dialog.setNegativeButton("Omitir", null);


        dialog.show();

    }


    public  void ApiLocationGoogleConectar(){
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mylistener.onConnected(bundle);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mylistener.onConnectionSuspended(i);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        mylistener.onConnectionFailed(connectionResult);
    }

    public interface Listener{
        void LastLocation(Location location);
        void onConnected(Bundle bundle);
        void onConnectionSuspended(int i);
        void onConnectionFailed(ConnectionResult location);
    }



    public static boolean checkPlayServices(Activity activity, int PLAY_SERVICES_RESOLUTION_REQUEST) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(activity);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(activity, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST);
            } else {
                activity.finish();
            }
            return false;
        }
        return true;
    }

}
