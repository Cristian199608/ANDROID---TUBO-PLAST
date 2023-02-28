
package com.example.sm_tubo_plast.genesys.fuerza_ventas.Reportes;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.datatypes.DBclasses;

import java.util.ArrayList;
import java.util.Arrays;

public class ReportesActivity extends TabActivity {

    private static final String SEARCH_TRANSPORT = "Search_Transport";
    private static final String TAB_COBRANZAS = "TabCobranzas";
    private static final String TAB_DEPOSITOS = "TabDepositos";
    private static final String TAB_PEDIDOS = "TabPedidos";
    public static final int POI_TAB_INDEX = 0;
    public static final int ADDRESS_TAB_INDEX = 1;
    public static final int LOCATION_TAB_INDEX = 2;
    public static final int TRANSPORT_TAB_INDEX = 3;
    public static final int HISTORY_TAB_INDEX = 4;
    public static final String TAB_INDEX_EXTRA = "TAB_INDEX_EXTRA";

    protected static final int POSITION_CURRENT_LOCATION = 1;
    protected static final int POSITION_LAST_MAP_VIEW = 2;
    protected static final int POSITION_FAVORITES = 3;
    protected static final int POSITION_ADDRESS = 4;

    private static final int GPS_TIMEOUT_REQUEST = 1000;
    private static final int GPS_DIST_REQUEST = 5;
    private static final int GPS_ACCURACY = 50;
    DBclasses _helper;
    private static final int REQUEST_FAVORITE_SELECT = 1;
    private static final int REQUEST_ADDRESS_SELECT = 2;

    public static final String SEARCH_LAT = "net.osmand.search_lat"; //$NON-NLS-1$
    public static final String SEARCH_LON = "net.osmand.search_lon"; //$NON-NLS-1$

    Button searchPOIButton;
    private TabSpec addressSpec;
    private TabSpec search_poi;

    private boolean searchAroundCurrentLocation = false;

    private static boolean searchOnLine = false;
    private LocationListener locationListener = null;
    private ArrayAdapter<String> spinnerAdapter;
    private Spinner spinner;
    String origen="";


    public interface SearchActivityChild {


    }

    private View getTabIndicator(int imageId, int stringId){
        View r = getLayoutInflater().inflate(R.layout.search_main_tab_header, getTabHost(), false);
        ImageView tabImage = (ImageView) r.findViewById(R.id.TabImage);
        tabImage.setImageResource(imageId);
        //tabImage.setBackgroundResource(R.drawable.tab_icon_background);
        tabImage.setContentDescription(getString(stringId));
        return r;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportes);

        _helper = new DBclasses(getApplicationContext());

        Button backButton = (Button) findViewById(R.id.search_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                ReportesActivity.this.finish();
            }
        });

        Bundle bundle = getIntent().getExtras();

        origen=""+ bundle.getString("ORIGEN");

        final TextView tabinfo  = (TextView) findViewById(R.id.textViewADesc);
        spinner = (Spinner) findViewById(R.id.SpinnerLocation);

        spinnerAdapter = new ArrayAdapter<String>(this, R.layout.my_spinner_text,
                new ArrayList<String>(Arrays.asList(new String[]{
                        getString(R.string.search_position_undefined),
                        getString(R.string.search_position_current_location),
                        getString(R.string.search_position_map_view),
                        getString(R.string.search_position_favorites),
                        getString(R.string.search_position_address)
                }))
        ) {
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View dropDownView = super.getDropDownView(position,
                        convertView, parent);
                if (dropDownView instanceof TextView) {
                    ((TextView) dropDownView).setTextColor(getResources()
                            .getColor(R.color.color_black));
                }
                return dropDownView;
            }
        };
        spinnerAdapter.setDropDownViewResource(R.layout.my_spinner_text);


        TabWidget tabs = (TabWidget) findViewById(android.R.id.tabs);
        tabs.setBackgroundResource(R.drawable.tab_icon_background);
        TabHost host = getTabHost();
        Log.d("","here1!!");

        search_poi = host.newTabSpec(TAB_PEDIDOS).setIndicator(getTabIndicator(R.drawable.icono_pedido3_selector, R.string.hello_world));
        setSearchPOIContent();
        host.addTab(	search_poi);

        addressSpec = 	host.newTabSpec(TAB_DEPOSITOS). setIndicator(getTabIndicator(R.drawable.icono_deposito3_selector, R.string.lblclientes));
        setAddressSpecContent();
        host.addTab(addressSpec);

        host.addTab(host.newTabSpec(TAB_COBRANZAS).setIndicator(getTabIndicator(R.drawable.icono_cobranza3_selector, R.string.app_name)).setContent(createIntent(ReportesCobranzaActivity.class))); //$NON-NLS-1$

        TabSpec transportTab = host.newTabSpec(SEARCH_TRANSPORT).setIndicator(getTabIndicator(R.drawable.icono_liquidacion3_selector, R.string.menu_settings)).setContent(createIntent(LiquidacionActivity.class));
        host.addTab(transportTab);


        //Tabs del mismo ancho en cualquier pantalla
        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();

        host.getTabWidget().getChildAt(0).setLayoutParams(new
                LinearLayout.LayoutParams((width/4), LinearLayout.LayoutParams.WRAP_CONTENT));

        host.getTabWidget().getChildAt(1).setLayoutParams(new
                LinearLayout.LayoutParams((width/4), LinearLayout.LayoutParams.WRAP_CONTENT));

        host.getTabWidget().getChildAt(2).setLayoutParams(new
                LinearLayout.LayoutParams((width/4), LinearLayout.LayoutParams.WRAP_CONTENT));

        host.getTabWidget().getChildAt(3).setLayoutParams(new
                LinearLayout.LayoutParams((width/4), LinearLayout.LayoutParams.WRAP_CONTENT));

        //

        if(origen.equals("COBRANZA_MODIFICAR")){
            host.setCurrentTab(2);
        }

        host.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

            public void onTabChanged(String tabId) {
                if (TAB_PEDIDOS.equals(tabId)) {
                    //tv_numPedidos.setText("Cant. Pedidos: "+ _helper.getPedidosCabecera().size());
                    tabinfo.setText(R.string.lblpedidosdia);
                } else	if (TAB_DEPOSITOS.equals(tabId)) {
                    //tv_numPedidos.setText("");
                    tabinfo.setText(R.string.lbldepositos);
                } else	if (TAB_COBRANZAS.equals(tabId)) {
                    //tv_numPedidos.setText("");
                    tabinfo.setText(R.string.lblcobranzas);
                } else	if (SEARCH_TRANSPORT.equals(tabId)) {
                    //tv_numPedidos.setText("");
                    tabinfo.setText(R.string.lblproductos);
                }
                //else	if (SEARCH_FAVORITES.equals(tabId)) {
                //	tabinfo.setText(R.string.lbldevoluciones);
                //}
                //	else	if (SEARCH_HISTORY.equals(tabId)) {
                //	tabinfo.setText(R.string.search_position_address);
                //}
            }
        });


        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);


        /* ***************ENVIAR MENSAJE DE SINCRONIZACION************** */
        SharedPreferences preferencias_configuracion;
        preferencias_configuracion = getSharedPreferences("preferencias_configuracion", Context.MODE_PRIVATE);
        boolean sincronizacionCorrecta = preferencias_configuracion.getBoolean("preferencias_sincronizacionCorrecta", false);
        if (sincronizacionCorrecta == false) {
            AlertDialog.Builder alerta = new AlertDialog.Builder(ReportesActivity.this);
            alerta.setTitle("Sincronizacion incompleta");
            alerta.setMessage("Los datos estan incompletos, sincronice correctamente");
            alerta.setIcon(R.drawable.icon_warning);
            alerta.setCancelable(false);
            alerta.setPositiveButton("OK", null);
            alerta.show();
        }
        /****************************************************************/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }



    public void startSearchCurrentLocation(){
        if(locationListener == null){
            locationListener = new LocationListener() {

                public void onStatusChanged(String provider, int status, Bundle extras) {}

                public void onProviderEnabled(String provider) {}

                public void onProviderDisabled(String provider) {}

                public void onLocationChanged(Location location) {


                }
            };
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            for(String provider : locationManager.getAllProviders()){
                locationManager.requestLocationUpdates(provider, GPS_TIMEOUT_REQUEST, GPS_DIST_REQUEST, locationListener);
            }
        }

    }

    public void endSearchCurrentLocation(){
        if (locationListener != null) {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            locationManager.removeUpdates(locationListener);
            locationListener = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        TabHost host = getTabHost();
        Intent intent = getIntent();
        int tabIndex = 0;
        if (intent != null) {
            if(intent.hasExtra(TAB_INDEX_EXTRA)){
                tabIndex = intent.getIntExtra(TAB_INDEX_EXTRA, POI_TAB_INDEX);
                host.setCurrentTab(tabIndex);
            }
            double lat = intent.getDoubleExtra(SEARCH_LAT, 0);
            double lon = intent.getDoubleExtra(SEARCH_LON, 0);
            if (lat != 0 || lon != 0) {

            }
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        endSearchCurrentLocation();
    }






    public boolean isSearchAroundCurrentLocation() {
        return searchAroundCurrentLocation;
    }

    private Intent createIntent(Class<? extends Activity> cl){
        Intent intent = new Intent(this, cl);
        return intent;
    }

    public void startSearchAddressOffline(){
        searchOnLine = false;
        getTabHost().setCurrentTab(0);
        setAddressSpecContent();
        getTabHost().setCurrentTab(1);
    }

    public void startSearchAddressOnline(){
        searchOnLine = true;
        getTabHost().setCurrentTab(0);
        setAddressSpecContent();
        getTabHost().setCurrentTab(1);
    }


    public void setSearchPOIContent() {
        search_poi.setContent(new Intent(this, ReportesPedidosCotizacionYVisitaActivity.class));
    }

    public void setAddressSpecContent() {
        if (searchOnLine) {
            addressSpec.setContent(createIntent(ReportesDepositoActivity.class));
        } else {
            addressSpec.setContent(createIntent(ReportesDepositoActivity.class));
        }
    }

}
