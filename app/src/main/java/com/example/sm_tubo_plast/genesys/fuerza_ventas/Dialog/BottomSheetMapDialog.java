package com.example.sm_tubo_plast.genesys.fuerza_ventas.Dialog;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.Google.service.GetDireccionByCoordenada;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.Google.service.GetGeoreferenciaByDireccion;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.Google.util.UtilMarker;
import com.example.sm_tubo_plast.genesys.hardware.LocationApiGoogle;
import com.example.sm_tubo_plast.genesys.hardware.Permiso_Adroid;
import com.example.sm_tubo_plast.genesys.hardware.RequestPermisoUbicacion;
import com.example.sm_tubo_plast.genesys.hardware.TaskCheckUbicacion;
import com.example.sm_tubo_plast.genesys.util.SnackBar.UtilViewSnackBar;
import com.example.sm_tubo_plast.genesys.util.UtilView;
import com.example.sm_tubo_plast.genesys.util.UtilViewMensaje;
import com.example.sm_tubo_plast.genesys.util.VARIABLES;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.maps.android.SphericalUtil;

public class BottomSheetMapDialog extends BottomSheetDialogFragment implements OnMapReadyCallback {
    private static final String TAG = "BottomSheetMapDialog";
    public static final int PERMISO_PARA_ACCEDER_A_LOCALIZACION = Permiso_Adroid.PERMISO_PARA_ACCEDER_A_LOCALIZACION;


    private int LIMITE_RADIOS_DRAGGABLE=0;

    private GoogleMap mMap;
    private Marker marker;
    private Circle circleLimiteMove;
    private LatLng currentLocationDefault = new LatLng(-12.0464, -77.0428); // Ubicación inicial (Ej: Lima, Perú)
    private LatLng currentLocationMarker = null;
    private LatLng currentLocationGPS = null;
    private OnLocationSelectedListener listener;

    public interface OnLocationSelectedListener {
        void onLocationSelected(LatLng latLng);
        void onCancel();
    }

    public static BottomSheetMapDialog newInstance(int limiteRadio) {
        BottomSheetMapDialog fragment = new BottomSheetMapDialog();
        Bundle args = new Bundle();
        args.putInt("limite_radio", limiteRadio);
        fragment.setArguments(args);
        return fragment;
    }
    public void setOnLocationSelectedListener(OnLocationSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new BottomSheetDialog(requireContext(), R.style.Theme_Dialog_Translucent);
    }

    LocationApiGoogle locationApiGoogle;
    TaskCheckUbicacion taskCheckUbicacion;
    GetDireccionByCoordenada utilDireccionFromCoord=null;

    private BottomSheetBehavior<View> bottomSheetBehavior;
    TextView tvDireccionMapa;
    CheckBox swCambiarToSatelital;
    ImageView btnCancel;
    Button btnConfirm;

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialog_bottom_sheet_move_marker, container, false);

        // Inicializar el fragmento de Google Maps
        SupportMapFragment mapFragment =  getFragmentMapa();
        mapFragment.getMapAsync(this);

//        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
//        if (mapFragment != null) {
//            Toast.makeText(getActivity(), "map iniciado", Toast.LENGTH_SHORT).show();
//            mapFragment.getMapAsync(this);
//        }
        // Botón de Confirmar ubicación
        swCambiarToSatelital = view.findViewById(R.id.swCambiarToSatelital);
        tvDireccionMapa = view.findViewById(R.id.tvDireccionMapa);
        btnCancel = view.findViewById(R.id.btnCancel);
        btnConfirm = view.findViewById(R.id.btnConfirm);
        if (getArguments()!=null) {
            LIMITE_RADIOS_DRAGGABLE= getArguments().getInt("limite_radio", 0);
        }
        configEventos();
        desahabledBottomSheeetDraggable(view);
        return view;

    }

    private SupportMapFragment getFragmentMapa(){
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager()
                        .findFragmentByTag("MAP_FRAGMENT_DRAG");
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.map, mapFragment, "MAP_FRAGMENT_DRAG")
                    .commit();
        }
        return mapFragment;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Agregar marcador inicial y permitir arrastrar
        if(currentLocationMarker ==null){
            marker = getMarkerCustom(currentLocationDefault);
            setDireccionNombre(currentLocationDefault);
            moveCameraEnfoqueMapa(currentLocationDefault);
        }
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        setMyUbicacionEnabled();
        star_Check_Permiso_Ubicacion();
        configEventosMap();
    }

    private void moveCameraEnfoqueMapa(LatLng latLngx) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngx, 17));
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy limpiar recursos");
        if (locationApiGoogle != null) {
            if (locationApiGoogle.fusedLocationClient != null && locationApiGoogle.locationCallback != null) {
                locationApiGoogle.fusedLocationClient.removeLocationUpdates(locationApiGoogle.locationCallback);
            }
        }
        super.onDestroy();
    }
    private void setMyUbicacionEnabled() {
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
    }
    private void configEventos() {
        // Obtener el BottomSheetBehavior
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onCancel();
                dismiss();
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null && marker != null) {//
                    if(validarDatos()){
                        listener.onLocationSelected(currentLocationMarker);
                        dismiss(); // Cierra el BottomSheetDialog
                    }
                }
            }
        });
        tvDireccionMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modificarDireccion();
            }
        });
    }

    private void modificarDireccion(){
        final UtilView.AlertViewSimpleConEdittext dd=new UtilView.AlertViewSimpleConEdittext(getActivity());
        dd.titulo="Dirección";
        dd.mensaje="Ingrese la dirección aquí";
        dd.min_caracteres=10;
        dd.hint="direccion, distrito, provincia, departamento";
        dd.texto_cargado=tvDireccionMapa.getText().toString().trim();
        dd.cancelable=false;
        dd.start(new UtilView.AlertViewSimpleConEdittext.Listener() {
            @Override
            public String resultOK(String descripcion) {
                if(descripcion==null)return null;
                //buscar georeferencia by ubicacion
                GetGeoreferenciaByDireccion getGeoreferenciaByDireccion = new GetGeoreferenciaByDireccion(
                        getActivity(),
                        descripcion,
                        new GetGeoreferenciaByDireccion.MyCallback() {
                            @Override
                            public void result(LatLng latLng) {
                                if(latLng==null){
                                    UtilViewSnackBar.SnackBarDanger(null, tvDireccionMapa, "Error al obtener la georeferencia");
                                    return;
                                }
                                if(latLng.latitude==0.0){
                                    UtilViewSnackBar.SnackBarDanger(null, tvDireccionMapa, "No se ha encontrado la georeferencia");
                                    return;
                                }
                                setMarkerBy(latLng);
                                moveCameraEnfoqueMapa(latLng);
                            }
                        }
                );
                getGeoreferenciaByDireccion.execute();
                return null;
            }

            @Override
            public String resultBucle(String s) {
                modificarDireccion();
                return null;
            }
        });
    }

    private void desahabledBottomSheeetDraggable(final View view){
        view.post(new Runnable() {
            @Override
            public void run() {
                View parent = (View) view.getParent();
                if (parent != null) {
                    bottomSheetBehavior = BottomSheetBehavior.from(parent);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    bottomSheetBehavior.setDraggable(false);
                }
            }
        });
    }

    private void configEventosMap(){
        configMapClick();
        configChangeMapStyle();
        configDraggableMarker();
    }

    private void configChangeMapStyle() {
        swCambiarToSatelital.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (mMap==null) return;
                mMap.setMapType(b?GoogleMap.MAP_TYPE_SATELLITE:GoogleMap.MAP_TYPE_NORMAL);
            }
        });
    }

    private void configMapClick() {
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                setMarkerBy(latLng);
            }
        });
    }

    private void configDraggableMarker() {
        // Evento cuando el marcador cambia de posición
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            public void onMarkerDragStart(Marker marker) {
                if (bottomSheetBehavior != null) {
                    bottomSheetBehavior.setDraggable(false); // Deshabilitar arrastre del BottomSheet
                }
            }
            @Override
            public void onMarkerDrag(Marker marker) {
                // Se ejecuta mientras se mueve el marcador
            }
            @Override
            public void onMarkerDragEnd(Marker marker) {
                currentLocationMarker = marker.getPosition();
                setDireccionNombre(currentLocationMarker);
            }
        });
    }

    private void setMarkerBy(LatLng latLng){
        currentLocationMarker = latLng;
        if(marker!=null){marker.remove(); marker=null;}
        marker = getMarkerCustom(currentLocationMarker);
        setDireccionNombre(currentLocationMarker);
    }
    private Marker getMarkerCustom(LatLng ubicacion){
        MarkerOptions markerOptions = new MarkerOptions()
                .position(ubicacion)
                .zIndex(100)
                .icon(BitmapDescriptorFactory.fromBitmap(
                                new UtilMarker(getActivity())
                                        .getMarkerIconWithLabel(null)
                        )
                )
                .title("Ubicación seleccionada")
                .draggable(true);

        return mMap.addMarker(markerOptions);
    }
    private void setDireccionNombre(LatLng locationx){
        utilDireccionFromCoord=new GetDireccionByCoordenada(getActivity(), locationx, new GetDireccionByCoordenada.MyCallback() {
            @Override
            public void result(String direccionName) {
                tvDireccionMapa.setText(direccionName);
            }
        });
        utilDireccionFromCoord.setShowLoading(false);
        utilDireccionFromCoord.execute();
    }

    private void startUbicacionApiGoogle() {
        locationApiGoogle = new LocationApiGoogle(getActivity(), new LocationApiGoogle.Listener() {
            @Override
            public void onConnected(Bundle bundle) {

                taskCheckUbicacion = new TaskCheckUbicacion(getActivity(), new TaskCheckUbicacion.MyListener() {
                    @Override
                    public void result(boolean isOk) {
                        if (isOk) {
                            locationApiGoogle.ForzarUltimaUbicacion();
                            locationApiGoogle.StartLocationTo3SecondsCallback();

                        } else {
                            locationApiGoogle.checkGPSActivate();
                        }
                    }
                });

            }

            @Override
            public void onConnectionSuspended(int i) {

            }

            @Override
            public void onConnectionFailed(ConnectionResult location) {

            }

            @Override
            public void LastLocation(Location location) {
                if (location != null) {
                    LatLng latLngThis=new LatLng(location.getLatitude(), location.getLongitude());
                    if(currentLocationGPS==null){
                        currentLocationMarker = latLngThis;
                        moveCameraEnfoqueMapa(latLngThis);
                        if(marker!=null){
                            marker.remove();
                            marker=null;
                        }
                        marker=getMarkerCustom(currentLocationMarker);
                        setDireccionNombre(currentLocationMarker);
                    }
                    currentLocationGPS=latLngThis;
                    setLimiteCircuferencia();
                    Log.i(TAG, "StartUbicacionApiGoogle:: LastLocation:: Latitude : " + location.getLatitude() + "Longitude : " + location.getLongitude());
                }
            }
        });
        locationApiGoogle.ApiLocationGoogleConectar();
    }
    private void setLimiteCircuferencia(){
        if(circleLimiteMove!=null)circleLimiteMove.remove();
        CircleOptions circleOptions = new CircleOptions()
                .center(currentLocationGPS)
                .radius(LIMITE_RADIOS_DRAGGABLE)
                .strokeColor(getResources().getColor(R.color.green_800))
                .strokeWidth(7)
                .fillColor(Color.argb(32, 33, 150, 243));
        circleLimiteMove = mMap.addCircle(circleOptions);
    }
    private void star_Check_Permiso_Ubicacion() {
        new RequestPermisoUbicacion(getActivity(), PERMISO_PARA_ACCEDER_A_LOCALIZACION, new RequestPermisoUbicacion.MyListener() {
            @Override
            public void Result(int isConcedido) {
                if (Permiso_Adroid.IS_PERMISO_DENEGADO == isConcedido) {
                    UtilViewMensaje.MENSAJE_simple(getActivity(), "Permiso denegado", "No podras acceder a la ubicación");
                } else if (Permiso_Adroid.IS_PERMISO_CONCEDIDO == isConcedido) {
                    startUbicacionApiGoogle();
                }
            }
        });
    }

    private boolean validarDatos() {
        if(currentLocationGPS ==null){
            UtilViewSnackBar.SnackBarDanger(null, swCambiarToSatelital, "Error, parece que no tienes acceso a gps");
            return false;
        }
        boolean isInside=isMarkerInsideCircle(circleLimiteMove.getCenter(), currentLocationMarker, circleLimiteMove.getRadius());
        if(!isInside){
            UtilViewSnackBar.SnackBarDanger(getActivity(), swCambiarToSatelital, "Estas fuera del límite establecido");
        }
        return isInside;
    }
    public static boolean isMarkerInsideCircle(LatLng center, LatLng marker, double radiusInMeters) {
        double distance = SphericalUtil.computeDistanceBetween(center, marker);
        return distance <= radiusInMeters;
    }

    @Override
    public void onRequestPermissionsResult( int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Toast.makeText(getActivity(), " permiso is "+grantResults[0], Toast.LENGTH_SHORT).show();
        if (requestCode == PERMISO_PARA_ACCEDER_A_LOCALIZACION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i("Permiso", "Permiso concedido");
                Toast.makeText(getActivity(), "Permiso aceptado", Toast.LENGTH_SHORT).show();
                star_Check_Permiso_Ubicacion();
            } else {
                Toast.makeText(getActivity(), "Sin permiso a ubcación", Toast.LENGTH_SHORT).show();
            }
        }
    }
}