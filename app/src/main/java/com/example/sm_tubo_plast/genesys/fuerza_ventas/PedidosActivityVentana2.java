package com.example.sm_tubo_plast.genesys.fuerza_ventas;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Display;
import android.view.InflateException;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.BEAN.ItemProducto;
import com.example.sm_tubo_plast.genesys.DAO.DAO_Pedido;
import com.example.sm_tubo_plast.genesys.DAO.DAO_RegistroBonificaciones;
import com.example.sm_tubo_plast.genesys.adapters.PedidoAdapter;
import com.example.sm_tubo_plast.genesys.datatypes.DBClientes;
import com.example.sm_tubo_plast.genesys.datatypes.DBMotivo_noventa;
import com.example.sm_tubo_plast.genesys.datatypes.DBPedido_Cabecera;
import com.example.sm_tubo_plast.genesys.datatypes.DBPedido_Detalle;
import com.example.sm_tubo_plast.genesys.datatypes.DBSync_soap_manager;
import com.example.sm_tubo_plast.genesys.datatypes.DBTb_Promocion_Detalle;
import com.example.sm_tubo_plast.genesys.datatypes.DB_PromocionDetalle;
import com.example.sm_tubo_plast.genesys.datatypes.DBclasses;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.Reportes.ReportesActivity;
import com.example.sm_tubo_plast.genesys.service.ConnectionDetector;
import com.example.sm_tubo_plast.genesys.util.GlobalFunctions;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Map;

@SuppressLint("LongLogTag")
public class PedidosActivityVentana2 extends AppCompatActivity {

    ArrayList<DB_PromocionDetalle> al_PromocionDetalle = new ArrayList<DB_PromocionDetalle>();

    private static final int Menu_Agregar = 1;
    private static final int Menu_Guardar = 2;
    private static final int Menu_Eliminar = 3;
    private static final int Menu_Cancelar = 4;
    private static final int Menu_Modificar = 5;
    private LocationListener Loclistener;

    private boolean GUARDAR_ACTIVO = false;
    private boolean AGREGAR_ITEM = true;
    private static final int Fecha_Dialog_ID = 1;
    private AlertDialog.Builder dialogo1;
    private LocationManager locationManager;
    private String provider = null, numOc;
    double lat, lng;
    int item_direccion = 0;

    //Array para traer todas las promociones
    ArrayList<DBTb_Promocion_Detalle> promociones = new ArrayList<DBTb_Promocion_Detalle>();
    //Array para comparar los productos del pedido con el Array promociones
    ArrayList<DB_PromocionDetalle> item_promo = new ArrayList<DB_PromocionDetalle>();
    //Array para guardar los productos a los que ya se le han asignado una promocion 
    ArrayList<DBTb_Promocion_Detalle> producto_registrado = new ArrayList<DBTb_Promocion_Detalle>();

    // array que guarda temporalmente los productos que se agregan por promocion
    ArrayList<DBPedido_Detalle> productos_promocion = new ArrayList<DBPedido_Detalle>();

    // fecha de la tabla configuracion
    String fecha_configuracion;

    // Fecha Actual
    int anio_act;
    int mes_act;
    int dia_act;

    int pos;

    String Oc_numero;
    double Sum_monto_total = 0.0;
    TabHost tabs;
    DBclasses dbclass;
    int dia, mes, anio;
    String cliente;
    String secuenciacli;
    String codven;
    String codcli;
    String origen;
    String pedido = "pedido";
    int selectedPosition1 = -1;
    int selectedPosition2 = -1;
    String[] clientes;
    String[] direcciones;
    String[] nomcli_mapa;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    Button btn_codFamilia;
    DBClientes clienteEntity;
    ProgressDialog pDialog;

    int clienteTienePercepcion;
    int clienteTienePercepcionEspecial;
    String clienteValorPercepcion;

    //variable prueba
    boolean swt = true;
    String nomcli;
    ArrayList<ItemProducto> productos = new ArrayList<ItemProducto>();

    Calendar calendar = Calendar.getInstance();
    Button btn_agregar, btn_guardar, btn_eliminar;
    private TextView txtTotal;
    private TextView txtTotal_peso;
    private TextView txtPercepcionTotal;
    private TextView txtTotalPedido;
    private ListView lstProductos;
    private EditText edtFechaEntrega, edtNroPedido, edtObservacion, edtLimiteCredito;
    private Spinner spnDireccion, spnFormaPago;
    AutoCompleteTextView autocomplete;
    String cod_familiar = "";
    String cod_cliente, nombre_familiar, nombre_cliente;

    DAO_RegistroBonificaciones DAO_registroBonificaciones;
    DAO_Pedido DAOPedidoDetalle;
    ;
    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            anio = year;
            mes = monthOfYear;
            dia = dayOfMonth;

            edtFechaEntrega.setText("");

            if (anio > anio_act) {
                ColocarFecha(dia, mes, anio);
            } else if (anio == anio_act && mes + 1 > mes_act) {
                ColocarFecha(dia, mes, anio);
            } else if (anio == anio_act && mes + 1 == mes_act && dia >= dia_act) {
                ColocarFecha(dia, mes, anio);
            } else {
                edtFechaEntrega.setError("Ingrese fecha valida");
                colocarFechaActual();
                // error();
            }
        }

        public void error() {
            Toast.makeText(getApplicationContext(), "Fecha Invalida",
                    Toast.LENGTH_SHORT).show();
        }
    };

    //
    public View getTabIndicador(String indicador) {
//        View r = getLayoutInflater().inflate(R.layout.tab_ped_act,
//                (TabHost) findViewById(android.R.id.tabhost), false);
//        TextView txtview = (TextView) r
//                .findViewById(R.id.tab_ped_act_txt_indicador);
        TextView txtview = new TextView(this);
        View r = new View(this);
        txtview.setText(indicador);
        return txtview;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos_ventana2);


        //Preferencias de Configuracion
        // --preferencias_configuracion = getSharedPreferences("preferencias_configuracion", Context.MODE_PRIVATE);

        dbclass = new DBclasses(getApplicationContext());
        Bundle bundle = getIntent().getExtras();
        codven = bundle.getString("codven");
        nomcli = bundle.getString("NOMCLI");
        origen = bundle.getString("ORIGEN");
        DAO_registroBonificaciones = new DAO_RegistroBonificaciones(getApplicationContext());
        DAOPedidoDetalle = new DAO_Pedido(getApplicationContext());
        // Log.i("PEDIDOSACTIVITY","ONCREATE: codven="+codven+" nomcli="+nomcli+" origen="+origen);

        edtLimiteCredito = (EditText) findViewById(R.id.edtLimiteCredito);
        edtFechaEntrega = (EditText) findViewById(R.id.edtFechaEntrega);
        lstProductos = (ListView) findViewById(R.id.lstProductos);
        spnDireccion = (Spinner) findViewById(R.id.spnDireccion);
        edtNroPedido = (EditText) findViewById(R.id.edtNroPedido);
        spnFormaPago = (Spinner) findViewById(R.id.spnFormaPago);
        autocomplete = (AutoCompleteTextView) findViewById(R.id.autocomplete);
        edtObservacion = (EditText) findViewById(R.id.edtObservacion);
        txtTotal = (TextView) findViewById(R.id.pedidolyt_txtTotal);
        txtTotal_peso = (TextView) findViewById(R.id.pedidolyt_txtTotal_peso);
        txtPercepcionTotal = (TextView) findViewById(R.id.pedidolyt_txtPercepcionTotal);
        txtTotalPedido = (TextView) findViewById(R.id.pedidolyt_txtTotal_pedido);
        btn_codFamilia = (Button) findViewById(R.id.pedido_lyt_btnFamilia);
        btn_agregar = (Button) findViewById(R.id.pedido_lyt_btnAgregar); // Para
        // agregar
        // Productos
        // al
        // pedido
        // btn_guardar=(ImageButton)findViewById(R.id.pedido_lyt_btnGuardar);
        // btn_eliminar=(ImageButton)findViewById(R.id.pedido_lyt_btnEliminar);

        // btn_guardar.setEnabled(false);

        cd = new ConnectionDetector(this);

        // Get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        Location location = null;

        if (provider != null) {
            Log.w("Provider Pedido Activity", "Provider: " + provider
                    + " has been selected.");

            if (locationManager.isProviderEnabled(provider)) {
                Log.w("Provider Pedido Activity", "provider: " + provider
                        + " Habilitado");
                // location = locationManager.getLastKnownLocation(provider);
            } else {
                Log.w("Provider Pedido Activity", "provider: " + provider
                        + " deshabilitado");
                // Intent myIntent = new
                // Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                // startActivity(myIntent);
            }
        } else {
            Log.w("Provider Pedido Activity", "Provider: " + provider
                    + " has been selected.");
        }

        //
        Loclistener = new LocationListener() {

            public void onLocationChanged(Location location) {
                Log.w("Localizacion cambio", "");
                obtenerLocalizacion(location);
                // Toast.makeText(getApplicationContext(),"localizacion cambio Lat:"+lat+", Lng:"+lng+"",
                // Toast.LENGTH_LONG).show();
            }

            public void onStatusChanged(String provider, int status,
                                        Bundle extras) {
                // TODO Auto-generated method stub

            }

            public void onProviderEnabled(String provider) {
                Log.w("Provider", "Provider Habilitado");
            }

            public void onProviderDisabled(String provider) {
                Log.w("Provider", "Provider Deshabilitado");
            }

        };
        //

        obtenerLocalizacion(location);

        // obtener la fecha de la tabla configuracion
        fecha_configuracion = dbclass.getCambio("Fecha");

        // Guardar la fecha
        /*
         * anio_act=calendar.get(Calendar.YEAR);
         * mes_act=calendar.get(Calendar.MONTH);
         * dia_act=calendar.get(Calendar.DAY_OF_MONTH);
         */

        mes_act = Integer.parseInt(fecha_configuracion.substring(3, 5));
        dia_act = Integer.parseInt(fecha_configuracion.substring(0, 2));
        anio_act = Integer.parseInt(fecha_configuracion.substring(6, 10));

        autocomplete.setText(nomcli);

        if (origen.equals("REPORTES")) {
            autocomplete.setEnabled(false);
            autocomplete.setFocusable(false);
            Oc_numero = bundle.getString("OC_NUMERO");
            DBPedido_Cabecera ped_cab = dbclass
                    .obtener_datos_PedCab_modificar(Oc_numero);

            autocomplete.setText(nomcli);

            mostrarDirecciones(nomcli);

            for (int i = 0; i < direcciones.length; i++) {
                if (direcciones[i].equals(ped_cab.getSitio_enfa())) {
                    spnDireccion.setSelection(i);
                    break;
                }
            }

            String condPago = ped_cab.getCond_pago();
            int position = 0;
            if (condPago.equals("01")) {
                position = 0;
            } else if (condPago.equals("02")) {
                position = 1;
            }

            spnFormaPago.setSelection(position);

            edtNroPedido.setText(Oc_numero);
            edtFechaEntrega.setText(ped_cab.getFecha_mxe());
            // ped_cab.getCond_pago();

            if (ped_cab.getObserv().equals("sin observaciones")) {

            } else {
                edtObservacion.setText("" + ped_cab.getObserv());
            }

            cod_familiar = ped_cab.getCodigo_familiar();

            mostrarListaProductos();

        } else if (origen.equals("CLIENTES_MAPA")) {
            autocomplete.setText(nomcli);
            // numOc = dbclass.obtenerNumOC(codven);
            numOc = dbclass.obtenerMaxNumOc(codven);
            edtNroPedido.setText(codven + calcularSecuencia(numOc));
            colocarFechaActual();
            mostrarDirecciones(autocomplete.getText().toString());
        } else if (origen.equals("CLIENTESAC")) {
            autocomplete.setText(nomcli);
            codcli = obtenerCodigoCliente(autocomplete.getText().toString());
            numOc = dbclass.obtenerOcxCliente(codcli);
            edtNroPedido.setText(numOc);
            colocarFechaActual();
            mostrarDirecciones(autocomplete.getText().toString());
            autocomplete.setEnabled(false);
            if (dbclass.VerificarCtasXCobrar(codcli).size() > 0) {
                Log.w("CLIENTE CON DEUDA", codcli
                        + "al venir de ActivityCliente");
                crearDialogo_cuentaXcobrar();
            }
        } else {
            item_direccion = bundle.getInt("DIRECCION");
            Log.i("PEDIDOSACTIVITY", "item_direccion=" + item_direccion);
            numOc = dbclass.obtenerMaxNumOc(codven);
            edtNroPedido.setText(codven + calcularSecuencia(numOc));
            colocarFechaActual();
            mostrarDirecciones(autocomplete.getText().toString());
            spnDireccion.setSelection(item_direccion);
            if (nomcli.length() > 1) {
                autocomplete.setText(nomcli);
            } else {
                cargarClientesXRuta();
            }

            codcli = obtenerCodigoCliente(autocomplete.getText().toString());

            Log.w("DIRECCION_ viene de ClientesActivity", codcli + "-"
                    + item_direccion);

            if (dbclass.VerificarCtasXCobrar(codcli).size() > 0) {
                Log.w("CLIENTE CON DEUDA", codcli
                        + "al venir de ActivityCliente");
                crearDialogo_cuentaXcobrar();
            }
        }

        //
	       /*spnDireccion.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					// TODO Auto-generated method stub
					onLocationChanged(null);
				}
		   });*/
        //


        registerForContextMenu(lstProductos);

        codcli = obtenerCodigoCliente(autocomplete.getText().toString());

        //Variables necesarias para el tema de percepcion

        clienteValorPercepcion = dbclass.obtenerValorPercepcionCliente(codcli);
        clienteTienePercepcion = dbclass.obtenerPercepcionCliente(codcli);
        clienteTienePercepcionEspecial = dbclass.obtenerPercepcionEspecialCliente(codcli);


        Log.i("PedidosActivityVentana2 ::else line 435::", "tienePercepcion= " + clienteTienePercepcion +
                "; tienePercepcionEsp= " + clienteTienePercepcionEspecial + "; valorPercepcion= " + clienteValorPercepcion);


        //promociones=dbclass.obtenerPromociones(codcli);
        clientes = new String[dbclass.getAllClient(codven).length];
        clientes = dbclass.getAllClient(codven);

        //ArrayAdapter<String> adaptador=new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,clientes);
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, R.layout.autocomplete_textview, clientes);
        autocomplete.setAdapter(adaptador);

        autocomplete.setThreshold(1);
        autocomplete.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // TODO Auto-generated method stub
                codcli = obtenerCodigoCliente(autocomplete.getText().toString());
                cliente = autocomplete.getText().toString();
                if (actionId != 5) {
                    if (dbclass.VerificarCtasXCobrar(codcli).size() > 0) {
                        Log.w("CLIENTE CON DEUDA", codcli + "autocompleteEvent");
                        crearDialogo_cuentaXcobrar();
                    }
                    mostrarDirecciones(autocomplete.getText().toString());
                    clienteTienePercepcion = dbclass.obtenerPercepcionCliente(codcli);
                    clienteTienePercepcionEspecial = dbclass.obtenerPercepcionEspecialCliente(codcli);
                    clienteValorPercepcion = dbclass.obtenerValorPercepcionCliente(codcli);
                    Log.d("->clienteTienePercepcion", "" + clienteTienePercepcion);
                    Log.d("->clienteTienePercepcionEspecial", "" + clienteTienePercepcionEspecial);
                    Log.d("->clienteValorPercepcion", "" + clienteValorPercepcion);
                    return true;
                }
                return false;
            }
        });

        //Evento al clickear sobre el nombre del autocomplete
        autocomplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adap, View view,
                                    int pos, long id) {

                String nombre = (String) adap.getItemAtPosition(pos);
                codcli = obtenerCodigoCliente(nombre);

                if (dbclass.VerificarCtasXCobrar(codcli).size() > 0) {
                    Log.w("CLIENTE CON DEUDA", codcli + "autocomplete al hacer click");
                    crearDialogo_cuentaXcobrar();
                }

                mostrarDirecciones(nombre);
                clienteTienePercepcion = dbclass.obtenerPercepcionCliente(codcli);
                clienteTienePercepcionEspecial = dbclass.obtenerPercepcionEspecialCliente(codcli);
                clienteValorPercepcion = dbclass.obtenerValorPercepcionCliente(codcli);
                Log.d("->clienteTienePercepcion", "" + clienteTienePercepcion);
                Log.d("->clienteTienePercepcionEspecial", "" + clienteTienePercepcionEspecial);
                Log.d("->clienteValorPercepcion", "" + clienteValorPercepcion);

            }
        });


        //mostrar los tabs
        tabs = (TabHost) findViewById(android.R.id.tabhost);
        tabs.setup();

        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();

        TabHost.TabSpec spec = tabs.newTabSpec("mitab1");
        spec.setContent(R.id.tab1);
        //spec.setIndicator("Pedido",getResources().getDrawable(android.R.drawable.ic_btn_speak_now));
        //spec.setIndicator("Pedido");
        spec.setIndicator(getTabIndicador("PEDIDO"));
        tabs.addTab(spec);

        spec = tabs.newTabSpec("mitab2");
        spec.setContent(R.id.tab2);
        //spec.setIndicator("Productos",getResources().getDrawable(android.R.drawable.ic_dialog_map));
        //spec.setIndicator("Producto");
        spec.setIndicator(getTabIndicador("PRODUCTO"));
        tabs.addTab(spec);

        tabs.setCurrentTab(0);

        tabs.getTabWidget().getChildAt(0).setLayoutParams(new
                LinearLayout.LayoutParams((width / 2), LinearLayout.LayoutParams.WRAP_CONTENT));

        tabs.getTabWidget().getChildAt(1).setLayoutParams(new
                LinearLayout.LayoutParams((width / 2), LinearLayout.LayoutParams.WRAP_CONTENT));

        btn_codFamilia.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (dbclass.getFamiliasxCliente(codcli).size() == 0) {
                    Toast.makeText(getApplicationContext(),
                            "No tiene clientes asociados", Toast.LENGTH_SHORT)
                            .show();
                } else
                    crearDialogo_CodigoFamiliar();
            }
        });

        btn_agregar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                abrir_ventana_agregar();

            }
        });

        edtLimiteCredito.setText("S/." + dbclass.getLimiteCreditoXCliente(codcli));
    }


    //Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (origen.equals("REPORTES")) {
            menu.add(Menu.NONE, Menu_Agregar, Menu.NONE, "MenuAgregar").setIcon(R.drawable.agregarv3).setTitle("Agregar");
            menu.add(Menu.NONE, Menu_Modificar, Menu.NONE, "Modificar").setIcon(R.drawable.modificarv3).setTitle("Modificar");
            //menu.add(Menu.NONE, Menu_Cancelar, Menu.NONE, "Cancelar").setIcon(R.drawable.cancel).setTitle("Cancelar");
        } else {
            menu.add(Menu.NONE, Menu_Agregar, Menu.NONE, "MenuAgregar").setIcon(R.drawable.agregarv3).setTitle("Agregar");
            menu.add(Menu.NONE, Menu_Guardar, Menu.NONE, "MenuGuardar").setIcon(R.drawable.guardarv3).setTitle("Guardar");
            menu.add(Menu.NONE, Menu_Eliminar, Menu.NONE, "MenuEliminar").setIcon(R.drawable.cancel).setTitle("Anular");
        }


        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (origen.equals("REPORTES")) {

        } else {
            if (GUARDAR_ACTIVO) {
                menu.getItem(1).setEnabled(true);
            } else {
                menu.getItem(1).setEnabled(false);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case Menu_Cancelar:

                if (origen.equals("REPORTES")) {
                    dbclass.actualizarEstadoCabeceraPedido(Oc_numero, "G");
                    finish();
                } else {
                    //dbclass.cambiarEstadoEliminados(Oc_numero);
                    PedidosActivityVentana2.this.finish();
                    Intent intent2 = new Intent(PedidosActivityVentana2.this, ReportesActivity.class);
                    intent2.putExtra("ORIGEN", "PEDIDOS");
                    startActivity(intent2);

                    Log.w("Menu Cancelar", "se cancelo");
                }

                return true;

            case Menu_Modificar:

                actualizar_pedido_cabecera();
                crear_dialogo_guardar_modificar("Se modificara el pedido");

                Log.w("Menu Modificar", "se modific�");

                return true;

            case Menu_Agregar:

                return abrir_ventana_agregar();

            case Menu_Guardar:

                actualizar_pedido_cabecera();
                dbclass.eliminar_item_promo_marcados(Oc_numero);
                crear_dialogo_guardar_modificar("Se guardaran todos los datos");

                return true;

            case Menu_Eliminar:

                if (dbclass.getPedidosCabeceraxCliente(codcli).size() > 0) {
                    //Toast.makeText(getApplicationContext(), "El cliente seleccionado ya tiene un pedido o motivo de no venta", Toast.LENGTH_SHORT).show();
                    //return true;
                }

                AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
                builder3.setTitle("Importante");
                builder3.setMessage("Esta seguro que desea Anular este pedido?");
                builder3.setCancelable(false);

                builder3.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {

                        crearDialogo_noventa();

                    }
                });
                builder3.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {

                    }
                });

                builder3.create().show();

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    //Menu contextual listview
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();

        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) menuInfo;

        ItemProducto prod = (ItemProducto) lstProductos.getAdapter().getItem(info.position);

        if (prod.getTipo().equals("V")) {

            TextView txt = (TextView) lstProductos.getAdapter().getView(info.position, null, null)
                    .findViewById(R.id.adapter_txtDescripcion);

            menu.setHeaderTitle(txt.getText().toString());

            inflater.inflate(R.menu.context_menu_pedido, menu);

        } else {

            TextView txt = (TextView) lstProductos.getAdapter().getView(info.position, null, null)
                    .findViewById(R.id.adapter_txtDescripcion);

            menu.setHeaderTitle(txt.getText().toString());

            inflater.inflate(R.menu.context_menu_pedido2, menu);

        }

    }




    private boolean abrir_ventana_agregar() {

        if (swt == true) {
            Oc_numero = edtNroPedido.getText().toString();
            if (autocomplete.getText().toString().matches("") || dbclass.obtenerCodigoCliente(autocomplete.getText().toString()).equals("")) {
                autocomplete.setError("Cliente incorrecto");
                return false;
            }

            guardarCabeceraPedido();
        }

        autocomplete.setEnabled(false);
        autocomplete.setFocusable(false);


        Intent intent = new Intent(PedidosActivityVentana2.this, Pedidos2Activity.class);
        intent.putExtra("codcli", codcli);
        intent.putExtra("codven", codven);
        intent.putExtra("ocNumero", Oc_numero);

        intent.putExtra(Pedidos2Activity.CLIENTE_TIENE_PERCEPCION, clienteTienePercepcion);
        intent.putExtra(Pedidos2Activity.CLIENTE_TIENE_PERCEPCION_ESPECIAL, clienteTienePercepcionEspecial);
        intent.putExtra(Pedidos2Activity.CLIENTE_VALOR_PERCEPCION, clienteValorPercepcion);

        Log.d("PedidosActivityVentana2 ::abrir_ventana_agregar::", "clienteTienePercepcion: " + clienteTienePercepcion + "\nclienteTienePercepcionEspecial: " + clienteTienePercepcionEspecial + "\nclienteValorPercepcion: " + clienteValorPercepcion);
        startActivityForResult(intent, 1);

        return true;
    }

    public void aceptar() {
        cliente = dbclass.obtenerNomcliXCodigo(codcli);
        Intent i = new Intent(getApplicationContext(), CuentasXCobrarActivity2.class);
        i.putExtra("CODCLI", codcli);
        i.putExtra("NOMCLI", cliente);
        i.putExtra("ORIGEN", "clienteac");
        startActivity(i);
    }

    public void cancelar() {
        Toast t = Toast.makeText(this, "Realizar Pedido.", Toast.LENGTH_SHORT);
        t.show();
    }


    //Picker Dialog funcion del boton en layout 
    public void Mostrar_date(View view) {

        dia = calendar.get(Calendar.DAY_OF_MONTH);
        mes = calendar.get(Calendar.MONTH);
        anio = calendar.get(Calendar.YEAR);

        showDialog(Fecha_Dialog_ID);
    }

    public void ColocarFecha(int day, int month, int year) {

        String _dia, _mes;

        //toma la fecha de la tabla configuracion
        //String fecha_configuracion = dbclass.getCambio("Fecha");

        //se cambia las variables globales(dia,mes) que traen la fecha actual del dispositivo
        //por unas que contengan la fecha de la tabla configuracion (dia1,mes1)

        //int mes1 = Integer.parseInt(fecha_configuracion.substring(3, 5));
        //int dia1 = Integer.parseInt(fecha_configuracion.substring(0, 2));

        if (day > 0 && day < 10) {
            _dia = "0" + day;
        } else {
            _dia = "" + day;
        }

        if ((month + 1) < 10) {
            _mes = "0" + (month + 1);
        } else {
            _mes = "" + (month + 1);
        }

        edtFechaEntrega.setText(new StringBuilder()
                .append(_dia).append("/")
                .append(_mes)
                .append("/")
                .append(anio));

    }

    @Override
    protected Dialog onCreateDialog(int id) {

        switch (id) {
            case Fecha_Dialog_ID:
                return new DatePickerDialog(this, dateSetListener, anio, mes, dia);

            default:
                return null;
        }
    }


    public void mostrarDirecciones(String nomcli) {

        DBclasses dbhelper = new DBclasses(getApplicationContext());


        //direcciones = dbclass.obtenerDirecciones_cliente(nomcli);

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(getApplicationContext(),
                android.R.layout.simple_spinner_item, direcciones);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDireccion.setAdapter(adapter);

        mostrarCondPago();
    }


    public String calcularSecuencia(String oc) {
        String cero = "0";
        String orden = "";

        //obtengo la fecha de la tabla configuracion
        //String fecha_configuracion = dbclass.getCambio("Fecha");

        //String mes_actual=(calendar.get(Calendar.MONTH)+1)+"";
        //String dia_actual=calendar.get(Calendar.DAY_OF_MONTH)+"";

        String mes_actual = fecha_configuracion.substring(3, 5);
        String dia_actual = fecha_configuracion.substring(0, 2);

        int secactual = 0;

        if (oc.length() < 10) {
            secactual = 1;
            if (mes_actual.length() < 2) {
                mes_actual = cero + mes_actual;
            }
            if (dia_actual.length() < 2) {
                dia_actual = cero + dia_actual;
            }
            orden = mes_actual + dia_actual + cero + secactual;
            return orden;
        } else {
            int diat = Integer.parseInt(oc.substring(8, 10));
            int mest = Integer.parseInt(oc.substring(6, 8));
            int sectem = Integer.parseInt(oc.substring(10, 12));

            if (Integer.parseInt(mes_actual) <= mest) {
                if (Integer.parseInt(dia_actual) > diat) {
                    secactual = 1;
                } else
                    secactual = sectem + 1;

            } else {
                secactual = 1;
            }
        }

        if (mes_actual.length() < 2) {
            mes_actual = cero + mes_actual;
        }
        if (dia_actual.length() < 2) {
            dia_actual = cero + dia_actual;
        }
        if (secactual < 10) {
            orden = mes_actual + dia_actual + cero + secactual;
        } else {
            orden = mes_actual + dia_actual + secactual;
        }

        return orden;
    }


    public String obtenerCodigoCliente(String nomcli) {

        String codcli = dbclass.obtenerCodigoCliente(nomcli);
        return codcli;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                tabs.setCurrentTab(1);

                //String org=data.getStringExtra("TIPO");
    			/*
    			//prueba codUnimed
    			String desunimed=data.getStringExtra("desunimed");
    			String unidad_medida=dbclass.obtener_codXdesunimed(desunimed);
    			
    			int fact_conv=data.getIntExtra("fact_conv", 0);
    			String sec_politica = data.getStringExtra("sec_politica");
    			final String codprod=data.getStringExtra("Codigo");
    			final double precio=data.getDoubleExtra("Precio", 0.0);
    			double peso=data.getDoubleExtra("peso", 0.0);
    			int cantidad=data.getIntExtra("Cantidad", 0);
    			double subtotal=Math.round((precio*cantidad)*100)/100.0;
    			double subtotal_peso=Math.round((peso*cantidad)*100)/100.0;
    			
    			Log.w("PRECIO - CODIGOPRODUCTO", precio+ "-"+ codprod);
    			
    		if(org.equals("AGREGAR")){	
    			//
    			if(lstProductos.getAdapter()!=null){
    				AGREGAR_ITEM=true;
    				
    			 for(int i=0;i<lstProductos.getAdapter().getCount();i++){
    				ItemProducto item = (ItemProducto)lstProductos.getAdapter().getItem(i);
    				if(codprod.equals(item.getCodprod())){
    				//if(codprod.equals(item.getCodprod()) && unidad_medida.equals(dbclass.obtener_codXdesunimed(item.getDesunimed()))){
    					AGREGAR_ITEM=false;
    					Toast.makeText(getApplicationContext(), "producto ya registrado",
    							Toast.LENGTH_SHORT).show();
    				}
    			  }
    			}
    			//
    			
		    			if(AGREGAR_ITEM){
		    				
		    				DBPedido_Detalle itemDetalle =new DBPedido_Detalle();
		        	        itemDetalle.setOc_numero(edtNroPedido.getText().toString());
		        	        itemDetalle.setCip(codprod);
		        	        itemDetalle.setEan_item("");
		        	        itemDetalle.setPrecio_bruto(precio+"");
		        	        itemDetalle.setPrecio_neto(Double.toString(subtotal));
		        	        itemDetalle.setCantidad(cantidad);
		        	        itemDetalle.setTipo_producto("V");
		        	        itemDetalle.setUnidad_medida(Integer.parseInt(unidad_medida));
		        	        itemDetalle.setPeso_bruto(Double.toString(subtotal_peso));
		        	        itemDetalle.setFlag("N");
		        	        itemDetalle.setCod_politica(sec_politica);
		        	        dbclass.AgregarPedidoDetalle(itemDetalle);
		        	        
		        	        dbclass.actualizar_stock_xtemp(cantidad*fact_conv, codprod); 
		        	        
		        	        //verificarPromocionXMonto();
		    			}
    			}
    			else{
    				Log.w("PRECIO - CODIGOPRODUCTO", precio+ "-"+ codprod);
    				dbclass.modificarItempedido(cantidad, peso, precio, codprod, Oc_numero, unidad_medida, "", "N", sec_politica, ""+subtotal);
					Log.w("MODIFICAR DETALLE", "detalle modificado");
					dbclass.actualizar_stock_xtemp(cantidad*fact_conv, codprod);
    			}*/

                if (data.getStringExtra("TIPO") != null) {
                    String Tipo = data.getStringExtra("TIPO");

                    if (Tipo.equals("MODIFICAR")) {

                        Bundle bundle = data.getExtras();

                        final String codprod = data.getStringExtra("Codigo");
                        int cantidad = data.getIntExtra("Cantidad", 0);
                        final double precio = data.getDoubleExtra("Precio", 0.0);
                        double peso = data.getDoubleExtra("peso", 0.0);

                        //double subTotal = precio*cantidad;
                        //double pesoTotal = peso*cantidad;        				
                        double subtotal = Math.round((precio * cantidad) * 100) / 100.0;
                        double subtotal_peso = Math.round((peso * cantidad) * 100) / 100.0;
                        final double percepcion = data.getDoubleExtra("percepcion", 0.0);
                        double precioPercepcion = data.getDoubleExtra("precioPercepcion", 0.0);
                        double percepcionxCantidad = precioPercepcion * cantidad;

                        Log.d("PedidosActivityVentana2 ::onActivityResult::",
                                "CodigoProducto: " + codprod +
                                        "\nPrecio: " + precio +
                                        "\nCantidad: " + cantidad +
                                        "\nPercepcion: " + percepcion +
                                        //"\nFactor conv: "+fact_conv+
                                        //"\nSecuencia Pol: "+sec_politica+
                                        "\nPeso: " + peso +
                                        "\nSubtotal_peso: " + subtotal_peso +
                                        "\nSubtotal: " + subtotal +
                                        "\nPpercepcionxCantidad: " + percepcionxCantidad +
                                        "\nPrecioPercepcion: " + precioPercepcion);

                        dbclass.modificarItempedido(Oc_numero, codprod, cantidad, Double.toString(subtotal), Double.toString(subtotal_peso), percepcionxCantidad);
                    }

                }

                mostrarListaProductos();
            }
        }
    }

    private void verificarPromocionXMonto() {
        // TODO Auto-generated methosd stub

    }


    public void guardarCabeceraPedido() {

        DBPedido_Cabecera itemCabecera = new DBPedido_Cabecera();

        String text = spnFormaPago.getSelectedItem().toString();
        String form = "";

        String observ = "";

        if (text.equals("Contado")) {
            form = "01";
            pos = 0;
        } else if (text.equals("Credito")) {
            form = "02";
            pos = 1;
        }
    		   	    
    	/*else{
    		form="07";
    		pos=2;
    	}*/


        if (edtObservacion.getText().toString().length() == 0) {
            observ = "sin observaciones";
        } else {
            observ = edtObservacion.getText().toString();
        }

        itemCabecera.setOc_numero(edtNroPedido.getText().toString());
        itemCabecera.setSitio_enfa(dbclass.getItemDireccionxNombre(spnDireccion.getSelectedItem().toString(), codcli));
        itemCabecera.setCodigo_familiar(cod_familiar);

        String dia = "" + dia_act;
        String mes = "" + mes_act;
        String fecha = "";

        if (dia_act <= 9) {
            dia = "0" + dia_act;
        }

        if (mes_act + 1 <= 9) {
            mes = "0" + (mes_act + 1);
        }


        fecha = "" + dia + "/" + mes + "/" + anio_act + " " + getHoraActual();

        //itemCabecera.setFecha_oc(""+anio_act+mes+dia);
        itemCabecera.setFecha_oc(fecha_configuracion + " " + getHoraActual());

        itemCabecera.setFecha_mxe(edtFechaEntrega.getText().toString());
        itemCabecera.setMoneda("S");
        itemCabecera.setEstado("I");
        itemCabecera.setValor_igv("0.0");
        itemCabecera.setUsername(dbclass.getNombreUsuarioXcodvend(codven));
        itemCabecera.setRuta("ruta");
        itemCabecera.setCond_pago(form);
        itemCabecera.setCod_cli(codcli);
        itemCabecera.setCod_emp(codven);
        itemCabecera.setObserv(observ);
        itemCabecera.setCod_noventa(0);
        itemCabecera.setPeso_total("0.0");
        itemCabecera.setFlag("P");
        itemCabecera.setLatitud(lat + "");
        itemCabecera.setLongitud(lng + "");
        itemCabecera.setCod_noventa(0);

        if (origen.equals("CLIENTESAC")) {
            dbclass.Actualizar_pedido_cabecera(itemCabecera);
        } else if (origen.equals("REPORTES")) {
            dbclass.Actualizar_pedido_cabecera(itemCabecera);
        } else
            dbclass.AgregarPedidoCabecera(itemCabecera);
        //dbclass.actualizarEstadoCliente(codcli, "P");

        swt = false;
    }

    public void actualizar_pedido_cabecera() {

        DBPedido_Cabecera itemCabecera = new DBPedido_Cabecera();
        itemCabecera.setOc_numero(Oc_numero);
        itemCabecera.setSitio_enfa(dbclass.getItemDireccionxNombre(spnDireccion.getSelectedItem().toString(), codcli));
        itemCabecera.setFecha_mxe(edtFechaEntrega.getText().toString());

        String text = spnFormaPago.getSelectedItem().toString();
        String form = "";

        String observ = "";

        if (text.equals("Contado")) {
            form = "01";
            pos = 0;
        } else if (text.equals("Credito")) {
            form = "02";
            pos = 1;
        } else {
            form = "07";
            pos = 2;
        }

        itemCabecera.setCond_pago(form);

        if (edtObservacion.getText().toString().length() == 0) {
            observ = "sin observaciones";
        } else {
            observ = edtObservacion.getText().toString();
        }

        itemCabecera.setObserv(observ);


        String dia = "" + dia_act;
        String mes = "" + mes_act;
        String fecha = "";

        if (dia_act <= 9) {
            dia = "0" + dia_act;
        }

        if (mes_act + 1 <= 9) {
            mes = "0" + (mes_act + 1);
        }

        SharedPreferences preferencias_configuracion;
        preferencias_configuracion = getSharedPreferences("preferencias_configuracion", Context.MODE_PRIVATE);
        String preferenciasJSON = preferencias_configuracion.getString("preferenciasJSON", "no_preferencias");
        String vistaSeleccionado;
        Log.d("PedidosActivity :preferenciasJSON:", preferenciasJSON);

        if (preferenciasJSON.equals("no_preferencias")) {
            vistaSeleccionado = "Vista 2";
        } else {
            Gson gson = new Gson();
            Type listType = new TypeToken<Map<String, Object>>() {
            }.getType();

            Map<String, Object> mapObject2 = gson.fromJson(preferenciasJSON, listType);
            vistaSeleccionado = (String) mapObject2.get("preferencias_vista");
            Log.d("PedidosActivity: DATOS:", "\nvista_Seleccionado: " + vistaSeleccionado);
        }


        fecha = "" + dia + "/" + mes + "/" + anio_act + " " + getHoraActual();
        itemCabecera.setMoneda("S");
        itemCabecera.setEstado("G");
        itemCabecera.setValor_igv("0.0");
        itemCabecera.setUsername(dbclass.getNombreUsuarioXcodvend(codven));
        itemCabecera.setFecha_oc(fecha_configuracion + " " + getHoraActual());
        itemCabecera.setFlag("P");
        itemCabecera.setLatitud(lat + "");
        itemCabecera.setLongitud(lng + "");
        itemCabecera.setCodigo_familiar(cod_familiar);
        itemCabecera.setTipoVista(vistaSeleccionado);
        dbclass.Actualizar_pedido_cabecera(itemCabecera);
        //dbclass.actualizarEstadoCliente(codcli, "P");

    }

    public void mostrarListaProductos() {

        productos.clear();
        ItemProducto[] producto;

        //entidad para verificar las promociones
        DBPedido_Detalle producto_a_verificar;

        Oc_numero = edtNroPedido.getText().toString();
        producto = dbclass.obtenerListadoProductos_pedido(Oc_numero);
        Log.i("PEDIDOS_ACTIVITY", "Numero de items= " + producto.length);
        double total = 0.0d;
        double totalPercepcion = 0.0d;
        double peso_total = 0.0d;
        double totalSujetoPercepcion = 0.0d;

        if (producto.length > 0) {
            GUARDAR_ACTIVO = true;
            //btn_guardar.setEnabled(true);
        } else {
            GUARDAR_ACTIVO = false;
            //btn_guardar.setEnabled(false);
        }

        for (int i = 0; i < producto.length; i++) {
            if (producto[i].getTipo().equals("V")) {
                productos.add(producto[i]);
            }
        }


        //solo agregar mientras el origen se diferente a 'reportes'
        //if(!(origen.equals("REPORTES"))){

        //Necesario para actualizar las cantidades de las promociones
        dbclass.eliminar_item_promo(Oc_numero);

        //Necesario para resetear las secuencias de promocion en los pedido_detalles's de tipo venta(V)
        dbclass.actualizarTodosPedidoDetalle(Oc_numero, "");

        item_promo.clear();

        for (int i = 0; i < productos.size(); i++) {

            producto_a_verificar = dbclass.getPedidosDetalleEntity(Oc_numero, productos.get(i).getCodprod());
            getProductoSalida(producto_a_verificar);

        }


        productos.clear();
        producto = dbclass.obtenerListadoProductos_pedido(Oc_numero);

        for (int i = 0; i < producto.length; i++) {

            productos.add(producto[i]);

            total = total + producto[i].getSubtotal();
            totalPercepcion = totalPercepcion + producto[i].getPercepcionPedido();
            peso_total = peso_total + producto[i].getPeso();

            if (producto[i].getPercepcionPedido() != 0) {
                totalSujetoPercepcion = totalSujetoPercepcion + producto[i].getSubtotal();
            }

        }

        //dbclass.GuardarMontoPesoPercepcion_Pedido(total, totalPercepcion, peso_total, Oc_numero);
        //dbclass.guardar_Stock(producto[i].getCodprod());

        PedidoAdapter adaptador = new PedidoAdapter(this, productos);


        lstProductos.setAdapter(adaptador);

        //total=Math.round(total*100)/100.0;
        //peso_total=Math.round(peso_total*100)/100.0;

        txtTotal.setText("Total:(S/.) " + Math.round(total * 100) / 100.0);
        txtTotal_peso.setText("Peso:(Kg) " + Math.round(peso_total * 100) / 100.0);
        txtPercepcionTotal.setText("Percep:(S/.) " + Math.round(totalPercepcion * 100) / 100.0);

        txtTotalPedido.setText("Total Pedido:(S/.) " + Math.round((total + totalPercepcion) * 100) / 100.0);

        dbclass.GuardarMontoPesoPercepcion_Pedido(total, totalPercepcion, peso_total, totalSujetoPercepcion, Oc_numero);
    }


    public void cargarClientesXRuta() {
        ArrayList<DBClientes> lista_clientes_ruta = new ArrayList<DBClientes>();
        int i = 0;
        lista_clientes_ruta = dbclass.getClientesXRuta();
        String[] lclientes = new String[lista_clientes_ruta.size()];
        Iterator<DBClientes> it = lista_clientes_ruta.iterator();


        while (it.hasNext()) {
            Object objeto = it.next();
            DBClientes cta = (DBClientes) objeto;

            lclientes[i] = cta.getNomcli();

            i++;

        }

        if (lclientes.length > 0) {

            autocomplete.setText(lclientes[0]);
            codcli = obtenerCodigoCliente(lclientes[0]);

            mostrarDirecciones(lclientes[0]);
        } else {
            autocomplete.setHint("Ingrese cliente");
        }

    }

    public void colocarFechaActual() {

        //int mes_actual=(calendar.get(Calendar.MONTH)+1);

        //int dia_propuesto=(calendar.get(Calendar.DAY_OF_MONTH)+1);

        //int anio_actual=calendar.get(Calendar.YEAR);

        int mes_actual = Integer.parseInt(fecha_configuracion.substring(3, 5));
        int dia_propuesto = Integer.parseInt(fecha_configuracion.substring(0, 2)) + 1;
        int anio_actual = Integer.parseInt(fecha_configuracion.substring(6, 10));


        Calendar mycal = new GregorianCalendar(anio_actual, mes_actual - 1, 1);
        int daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);

        if (dia_propuesto > daysInMonth) {
            dia_propuesto = 1;

            if ((mes_actual - 1) == Calendar.DECEMBER) {
                mes_actual = Calendar.JANUARY + 1;
                anio_actual = anio_actual + 1;
            } else {
                mes_actual = mes_actual + 1;
            }

        }

        String _dia_propuesto, _mes_actual;

        if (dia_propuesto > 0 && dia_propuesto < 10) {
            _dia_propuesto = "0" + dia_propuesto;
        } else {
            _dia_propuesto = "" + dia_propuesto;
        }

        if (mes_actual < 10) {
            _mes_actual = "0" + (mes_actual);
        } else {
            _mes_actual = "" + (mes_actual);
        }

        edtFechaEntrega.setText(new StringBuilder()
                .append(_dia_propuesto).append("/")
                .append(_mes_actual)
                .append("/")
                .append(anio_actual));
    }


    public void mostrarCondPago() {
        codcli = obtenerCodigoCliente(autocomplete.getText().toString());
        String condPago = dbclass.obtener_condPagoXcliente(codcli);

        Log.i("CONDPAGO", "" + condPago);
        Log.i("CODCLI", "" + codcli);

        int position = 0;
        if (condPago.equals("01")) {
            position = 0;
        } else if (condPago.equals("2")) {
            position = 1;
        } else if (condPago.equals("07")) {
            position = 2;
        }
        spnFormaPago.setSelection(position);
    }

    public String getHoraActual() {
        Calendar calendar1 = Calendar.getInstance();

        String hora = "" + calendar1.get(Calendar.HOUR_OF_DAY);
        String min = "" + calendar1.get(Calendar.MINUTE);
        String seg = "" + calendar1.get(Calendar.SECOND);

        if (hora.length() < 2) {
            hora = "0" + hora;
        }

        if (min.length() < 2) {
            min = "0" + min;
        }

        if (seg.length() < 2) {
            seg = "0" + seg;
        }

        return hora + ":" + min + ":" + seg;
    }


    public void crearDialogo_cuentaXcobrar() {

        //Dialogo Deudas
        dialogo1 = new AlertDialog.Builder(this);
        dialogo1.setTitle("Importante");
        dialogo1.setMessage("El cliente seleccionado mantiene una deuda �Desea realizar el cobro?");
        dialogo1.setCancelable(false);

        dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                aceptar();
            }
        });
        dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                cancelar();
            }
        });

        dialogo1.show();
    }

    public void crearDialogo_noventa() {

        final Dialog dialogo = new Dialog(this);

        dialogo.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialogo.setContentView(R.layout.dialog_motivo_noventa);
        dialogo.setCancelable(false);


        final ListView lstNoventa_motivo = (ListView) dialogo.findViewById(R.id.dialog_motivo_noventa_lstNo_venta);
        Button btnAceptar = (Button) dialogo.findViewById(R.id.dialog_motivo_noventa_btnAceptar);
        Button btnCancelar = (Button) dialogo.findViewById(R.id.dialog_motivo_noventa_btnCancelar);
        lstNoventa_motivo.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        ArrayList<DBMotivo_noventa> motivos = new ArrayList<DBMotivo_noventa>();
        motivos = dbclass.obtenerMotivo_noventa();
        dbclass.close();
        no_ventaAdapter adaptador = new no_ventaAdapter(this, motivos);
        lstNoventa_motivo.setAdapter(adaptador);

        lstNoventa_motivo.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View arg1,
                                    int position, long id) {

                selectedPosition1 = position;

            }
        });

        btnAceptar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (selectedPosition1 != -1) {

                    DBMotivo_noventa item =
                            (DBMotivo_noventa) lstNoventa_motivo.getAdapter().getItem(selectedPosition1);

                    if (swt == true) {

                        guardarCabeceraPedido();
                        //dbclass.actualizarEstadoCliente(cliente, "M");
                    }

                    dbclass.Anular_pedido(edtNroPedido.getText().toString(), item.getCod_noventa());

                    dialogo.dismiss();

                    new async_envio_pedido().execute();

                } else {
                    Toast.makeText(getApplicationContext(), "Seleccione uno", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialogo.cancel();
            }
        });


        dialogo.show();

    }


    @Override
    public void onBackPressed() {
        if (origen.equals("REPORTES")) {

        } else {

            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Pedido")
                    .setMessage("Se perderan todos los datos")
                    .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dbclass.eliminar_pedido(Oc_numero);
                            //dbclass.actualizarEstadoCliente(codcli, "S");
                            finish();
                        }

                    })
                    .setNegativeButton("Cancelar", null)
                    .show();


        }
    }


    public class no_ventaAdapter extends ArrayAdapter<DBMotivo_noventa> {

        Activity context;
        ArrayList<DBMotivo_noventa> datos;

        public no_ventaAdapter(Activity context, ArrayList<DBMotivo_noventa> datos) {

            super(context, android.R.layout.simple_list_item_single_choice, datos);
            this.context = context;
            this.datos = datos;
        }

        public View getView(int position, View convertview, ViewGroup parent) {

            View item = convertview;
            ViewHolder holder;

            if (item == null) {
                LayoutInflater inflater = context.getLayoutInflater();
                item = inflater.inflate(android.R.layout.simple_list_item_single_choice, null);

                holder = new ViewHolder();

                holder.des_noventa = (TextView) item.findViewById(android.R.id.text1);

                item.setTag(holder);

            } else {

                holder = (ViewHolder) item.getTag();

            }

            holder.des_noventa.setText(datos.get(position).getDes_noventa());

            return item;
        }


        public class ViewHolder {
            TextView des_noventa;
        }
    }


    class async_envio_pedido extends AsyncTask<Void, Integer, String> {

        protected void onPreExecute() {
            //para el progress dialog

            //int datos1 = dbclass.getCantidadDatos_pedido_cabecera(Oc_numero);
            //int datos2 = dbclass.getCantidadDatos_pedido_detalle(Oc_numero);

            pDialog = new ProgressDialog(PedidosActivityVentana2.this);
            pDialog.setIcon(R.drawable.ic_send);
            pDialog.setMessage("Sincronizando....");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected String doInBackground(Void... params) {

            DBSync_soap_manager soap_manager = new DBSync_soap_manager(getApplicationContext());

            String valor = "";

            if (cd.hasActiveInternetConnection(getApplicationContext())) {

                try {
                    valor = soap_manager.actualizarObjPedido_directo(Oc_numero);
                } catch (JsonParseException ex) {
                    //exception al parsear json
                    valor = "error_2";
                } catch (Exception ex) {
                    //cualquier otra exception al enviar los datos
                    valor = "error_3";
                    Log.i("PEDIDOS_ACTIVITY", ex.getMessage());
                }


            } else {
                //Sin conexion al servidor
                valor = "error_1";
            }


            return valor;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int progreso = values[0].intValue();

            pDialog.setProgress(progreso);
        }

        /*Una vez terminado doInBackground segun lo que halla ocurrido 
        pasamos a la sig. activity
        o mostramos error*/
        @Override
        protected void onPostExecute(String result) {

            pDialog.dismiss();//ocultamos progess dialog.
            //AlertDialog alertDialog = new AlertDialog.Builder(PedidosActivity.this).create();
            //alertDialog.setCancelable(false);

            if (result.equals("E")) {

                crear_dialogo_post_envio("ENVIO CORRECTO", "El pedido fue ingresado al Servidor", R.drawable.check);

            } else if (result.equals("I")) {

                crear_dialogo_post_envio("ATENCION", "No se pudieron guardar todos los datos", R.drawable.alert);

            } else if (result.equals("P")) {

                crear_dialogo_post_envio("ATENCION", "El servidor no pudo ingresar este pedido", R.drawable.ic_alert);

            } else if (result.equals("T")) {

                crear_dialogo_post_envio("ATENCION", "Este pedido ya se encuentra en proceso de facturacion \nComuniquese con el administrador \nLos cambios se guardaran localmente", R.drawable.ic_alert);

            } else if (result.equals("error_1")) {

                crear_dialogo_post_envio("SIN CONEXION", "Es probable que no tenga acceso a la red \nEl pedido se guardo localmente", R.drawable.ic_alert);

            } else if (result.equals("error_2")) {

                crear_dialogo_post_envio("ATENCION", "El pedido fue enviado pero no se pudo verificar \nVerifique manualmente", R.drawable.alert);

            } else if (result.equals("error_3")) {

                crear_dialogo_post_envio("ERROR AL ENVIAR", "No se pudo Enviar este pedido \nSe guardo localmente", R.drawable.ic_alert);

            }

            Log.e("onPostExecute=", "" + result);

        }

    }



    protected void onResume() {
        super.onResume();
        if (provider != null && locationManager.isProviderEnabled(provider)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(provider, 1000, 0, Loclistener);
            Log.w("Location","Pedido de actualizacion de ubicacion a provider: "+provider);
        }
    }

    /* Remove the locationlistener updates when Activity is paused */

    protected void onPause() {
        super.onPause();

        locationManager.removeUpdates(Loclistener);
    }

    public void obtenerLocalizacion(Location location){
        if(location != null){
            lat = (double) (location.getLatitude());
            lng = (double) (location.getLongitude());
        }else{
		 
		 /*String[] localizacion = new String[2];
		 
		 int itm_dir = spnDireccion.getSelectedItemPosition();
		 localizacion = dbclass.Obtener_localizacion(codcli, itm_dir);
		 
		 lat = Double.parseDouble(localizacion[0]);//
		 lng = Double.parseDouble(localizacion[1]);//*/

            //if(lat == 0.0 || lng == 0.0){
            if(provider != null){
                Location loc = locationManager.getLastKnownLocation(provider);
                if(loc != null){
                    lat = (double)(loc.getLatitude());
                    lng = (double)(loc.getLongitude());
                }
            }
            //}

        }
        Log.w("Localizacion","Lat:"+lat+", Lng:"+lng+"");
    }


    protected void setMenuBackground(){
        // Log.d(TAG, "Enterting setMenuBackGround");  
        getLayoutInflater().setFactory( new LayoutInflater.Factory() {
            public View onCreateView(String name, Context context, AttributeSet attrs) {
                if ( name.equalsIgnoreCase( "com.android.internal.view.menu.IconMenuItemView" ) ) {
                    try { // Ask our inflater to create the view  
                        LayoutInflater f = getLayoutInflater();
                        final View view = f.createView( name, null, attrs );
                        /* The background gets refreshed each time a new item is added the options menu.
                         * So each time Android applies the default background we need to set our own
                         * background. This is done using a thread giving the background change as runnable
                         * object */
                        new Handler().post(new Runnable() {
                            public void run () {
                                // sets the background color   
                                view.setBackgroundResource( R.color.color_white);
                                // sets the text color              
                                ((TextView) view).setTextColor(Color.BLACK);
                                // sets the text size              
                                ((TextView) view).setTextSize(18);
                            }
                        } );
                        return view;
                    }
                    catch ( InflateException e ) {}
                    catch ( ClassNotFoundException e ) {}
                }
                return null;
            }
        });
    }

    //M�todo que retorna el producto salida si se cumple las condiciones para la promocion
    public ItemProducto getProductoSalida(DBPedido_Detalle itemDetalle){


        if(VerificarSiTienePromocion(itemDetalle.getCip(),itemDetalle.getUnidad_medida(),itemDetalle.getCod_politica())){

            //Recorrer registros promociones del producto
            Log.w("GETPRODUCTOSALIDA", "Si tiene promocion"+itemDetalle.getCip());
            Iterator<DB_PromocionDetalle> it=al_PromocionDetalle.iterator();

            while ( it.hasNext() ) {
                Object objeto = it.next();

                DB_PromocionDetalle promoEntity = (DB_PromocionDetalle)objeto;
                //Si la promocion es solo para un producto
                if(promoEntity.total_agrupado==1){

                    int  cantidadProducto = verificar_condicionXtipo(promoEntity, itemDetalle);

                    //
                    if(cantidadProducto > 0){

                        DBPedido_Detalle dbdetalle_temporal = dbclass.existePedidoDetalle2(Oc_numero,promoEntity.getSalida(),dbclass.obtener_codunimedXtipo_unimed_salida(Integer.parseInt(promoEntity.getTipo_unimed_salida()),promoEntity.getSalida()));


                        if(dbdetalle_temporal != null){

                            if(!dbdetalle_temporal.getCod_politica().equals("ELIM")){

                                dbclass.actualizarItem_promo(
                                        Oc_numero,
                                        promoEntity.getSalida(),
                                        dbclass.obtener_codunimedXtipo_unimed_salida(Integer.parseInt(promoEntity.getTipo_unimed_salida()),
                                                promoEntity.getSalida()),
                                        //dbclass.getPedidosDetalleEntity(Oc_numero,promoEntity.getSalida()).getCantidad()+cantidadProducto
                                        dbdetalle_temporal.getCantidad()+cantidadProducto,
                                        dbdetalle_temporal.getSec_promo()+
                                                GlobalFunctions.codigo_secuencia_promocion(promoEntity.getSecuencia(), promoEntity.getItem(), promoEntity.getAgrupado())
                                );

                            }

                        }else{
                            agregarProductoxPromocion(
                                    promoEntity.getSalida(),
                                    promoEntity.getTipo_unimed_salida(),
                                    cantidadProducto,Integer.toString(promoEntity.getSecuencia()),
                                    promoEntity.getItem(),
                                    promoEntity.getAgrupado()
                            );
                        }

                        //Actualizar la secuencia del producto que genero la promocion
                        //Se debe guardar el codigo de salida de la promo seguido de la cantidad

                        String codigoSecuenciaPromocionPV = GlobalFunctions.codigo_secuencia_promocion_productoVenta(Integer.parseInt(promoEntity.getSalida()), cantidadProducto);

                        dbclass.actualizarPedidoDetalle(Oc_numero, itemDetalle.getCip(),itemDetalle.getSec_promo()+codigoSecuenciaPromocionPV);

                    }else{
                        dbclass.EliminarItemPedido2(promoEntity.getSalida(), Oc_numero,Integer.toString(promoEntity.getSecuencia()),Integer.toString(promoEntity.getItem()));
                    }
                    //

                }


                //if(cumpleCondicion(promoEntity , itemDetalle ) != null){
                //  return cumpleCondicion(promoEntity , itemDetalle );
                //}

                //}
                else{
                    ArrayList<DB_PromocionDetalle> secuenciasPromociones = new ArrayList<DB_PromocionDetalle>();

                    //Obtener todos los promocion_detalles's de la misma secuencia e item 
                    secuenciasPromociones = dbclass.getPromocionesXSecuencia(promoEntity.getSecuencia(),promoEntity.getItem());
                    int secuencias= secuenciasPromociones.size();

                    if(promoEntity.getAcumulado() == 0){

                        //
                        int cont=0;

                        Iterator<DB_PromocionDetalle> it2 = secuenciasPromociones.iterator();

                        int cantidad = -99;
                        //ItemProducto[] producto;
                        //Oc_numero = edtNroPedido.getText().toString();
                        //producto = dbclass.obtenerListadoProductos_pedido(Oc_numero);

                        DB_PromocionDetalle promoEntity2 = null;

                        if(!promocion_registrada(secuenciasPromociones.get(0),item_promo)){

                            //registro la promocion, para saber luego que ya fue consultada
                            item_promo.add(secuenciasPromociones.get(0));

                            while ( it2.hasNext() ) {
                                Object objeto2 = it2.next();
                                promoEntity2 = (DB_PromocionDetalle)objeto2;

                                //se obtiene la lista de items seleccionados
                                int aux;

                                aux = cumpleCondicion(promoEntity2 ,productos);

                                if(cantidad < 0){
                                    cantidad = aux;
                                }

                                if(aux>0){
                                    cont++;

                                    if(aux<cantidad){
                                        cantidad = aux;
                                    }

                                }else{
                                    break;
                                }

                            }

                        }


                        if(promoEntity2 != null){
                            //Si cumple las condiciones
                            if(secuencias==cont){

                                DBPedido_Detalle dbdetalle_temporal = dbclass.existePedidoDetalle2(Oc_numero,promoEntity2.getSalida(),dbclass.obtener_codunimedXtipo_unimed_salida(Integer.parseInt(promoEntity2.getTipo_unimed_salida()),promoEntity2.getSalida()));

                                if(dbdetalle_temporal != null){

                                    if(!dbdetalle_temporal.getCod_politica().equals("ELIM")){

                                        dbclass.actualizarItem_promo(
                                                Oc_numero,
                                                promoEntity2.getSalida(),
                                                dbclass.obtener_codunimedXtipo_unimed_salida(Integer.parseInt(promoEntity2.getTipo_unimed_salida()),
                                                        promoEntity2.getSalida()),
                                                cantidad,
                                                dbdetalle_temporal.getSec_promo()+"-"+
                                                        GlobalFunctions.codigo_secuencia_promocion(promoEntity2.getSecuencia(), promoEntity2.getItem(), promoEntity2.getAgrupado())
                                        );

                                    }


                                }else{
                                    agregarProductoxPromocion(
                                            promoEntity2.getSalida(),
                                            promoEntity2.getTipo_unimed_salida(),
                                            cantidad ,
                                            Integer.toString(promoEntity2.getSecuencia()),
                                            promoEntity2.getItem(),
                                            promoEntity2.getAgrupado()
                                    );
                                }

                                String codigoSecuenciaPV = GlobalFunctions.codigo_secuencia_promocion_productoVenta(Integer.parseInt(promoEntity2.getSalida()), cantidad);
                                dbclass.actualizarTodosPedidoDetalle2(promoEntity2.getSecuencia(), promoEntity2.getItem(),codigoSecuenciaPV, Oc_numero);

                            }else{
                                dbclass.EliminarItemPedido2(promoEntity2.getSalida(), Oc_numero, Integer.toString(promoEntity.getSecuencia()), Integer.toString(promoEntity.getItem()));
                                dbclass.resetCampoSecPromTodosPedidoDetalle2(promoEntity2.getSecuencia(), promoEntity2.getItem(), Oc_numero);
                            }
                        }
                        //
                    }
                    else{

                        //
                        int cantidad_promocion = 0;
                        int sum_cant = 0;
                        double sum_monto = 0.0d;

                        Iterator<DB_PromocionDetalle> it2 = secuenciasPromociones.iterator();

                        DB_PromocionDetalle promoEntity2 = null;

                        if(!promocion_registrada(secuenciasPromociones.get(0),item_promo)){

                            //registro la promocion, para saber luego que ya fue consultada
                            item_promo.add(secuenciasPromociones.get(0));

                            while ( it2.hasNext() ) {
                                Object objeto2 = it2.next();
                                promoEntity2 = (DB_PromocionDetalle)objeto2;

                                //se obtiene la lista de items seleccionados
                                int index = verificarSiExisteProducto(promoEntity2.getEntrada(),
                                        Integer.parseInt(promoEntity2.getTipo_unimed_entrada()), productos);

                                if(index != -1){
                                    DBPedido_Detalle itemDet = dbclass.getPedidosDetalleEntity(Oc_numero, productos.get(index).getCodprod());
                                    sum_cant += itemDet.getCantidad();
                                    sum_monto += Double.parseDouble(itemDet.getPrecio_neto());

                                    cantidad_promocion = cumpleCondicionAcumulado(promoEntity2, itemDetalle, sum_cant, sum_monto);

                                    if(promoEntity2.getCondicion().equals("1")){

                                        if(sum_cant >= promoEntity2.getCant_condicion()){
                                            cantidad_promocion = promoEntity2.getCant_promocion();
                                            break;
                                        }

                                    }

                                }


                            }



                        }
                        //

                        if(promoEntity2 != null){
                            //
                            //Si cumple las condiciones
                            if(cantidad_promocion != 0){

                                DBPedido_Detalle dbdetalle_temporal = dbclass.existePedidoDetalle2(Oc_numero,promoEntity2.getSalida(),dbclass.obtener_codunimedXtipo_unimed_salida(Integer.parseInt(promoEntity2.getTipo_unimed_salida()),promoEntity2.getSalida()));

                                if(dbdetalle_temporal != null){

                                    if(!dbdetalle_temporal.getCod_politica().equals("ELIM")){

                                        dbclass.actualizarItem_promo(
                                                Oc_numero,
                                                promoEntity2.getSalida(),
                                                dbclass.obtener_codunimedXtipo_unimed_salida(Integer.parseInt(promoEntity2.getTipo_unimed_salida()),
                                                        promoEntity2.getSalida()),
                                                cantidad_promocion,
                                                dbdetalle_temporal.getSec_promo()+"-"+
                                                        GlobalFunctions.codigo_secuencia_promocion(promoEntity2.getSecuencia(), promoEntity2.getItem(), promoEntity2.getAgrupado())
                                        );

                                    }


                                }else{
                                    agregarProductoxPromocion(
                                            promoEntity2.getSalida(),
                                            promoEntity2.getTipo_unimed_salida(),
                                            cantidad_promocion,
                                            Integer.toString(promoEntity2.getSecuencia()),
                                            promoEntity2.getItem(),
                                            promoEntity2.getAgrupado()
                                    );
                                }

                                //String codigoSecuenciaPV = GlobalFunctions.codigo_secuencia_promocion_productoVenta(Integer.parseInt(promoEntity2.getSalida()), cantidad_promocion);							
                                //dbclass.actualizarTodosPedidoDetalle2(promoEntity2.getSecuencia(), promoEntity2.getItem(),codigoSecuenciaPV, Oc_numero);

                            }else{
                                dbclass.EliminarItemPedido2(promoEntity2.getSalida(), Oc_numero, Integer.toString(promoEntity.getSecuencia()), Integer.toString(promoEntity.getItem()));
                                dbclass.resetCampoSecPromTodosPedidoDetalle2(promoEntity2.getSecuencia(), promoEntity2.getItem(), Oc_numero);
                            }
                            //
                        }

                    }

                }
            }

            //item_promo.clear();

        }
        else
            Log.w("GETPRODUCTOSALIDA", "No tiene promocion"+itemDetalle.getCip());
        return null;

    }

    private int cumpleCondicionAcumulado(DB_PromocionDetalle promoEntity2,DBPedido_Detalle itemDetalle,int sum_cant, double sum_monto) {

        int cantidadProducto = 0;

        String condicion = promoEntity2.getCondicion();

        if(promoEntity2.getTipo().equals("C")){

            if(sum_cant >= promoEntity2.getCant_condicion()){
                if(condicion.equals("1")){
                    cantidadProducto = promoEntity2.getCant_promocion();
                }else if(condicion.equals("3")){
                    cantidadProducto = (sum_cant/promoEntity2.getCant_condicion())*promoEntity2.getCant_promocion();
                }
            }

        }
        else if(promoEntity2.getTipo().equals("M")){

            if(sum_monto >= Double.parseDouble(promoEntity2.getMonto())){
                if(condicion.equals("1")){
                    cantidadProducto = promoEntity2.getCant_promocion();
                }else if(condicion.equals("3")){
                    int aux = (int)(sum_monto/Double.parseDouble(promoEntity2.getMonto()));
                    cantidadProducto = aux*promoEntity2.getCant_promocion();
                }
            }

        }


        if(cantidadProducto > 0){

            //Actualizar la secuencia del producto que genero la promocion
            //Se debe guardar el codigo de salida de la promo seguido de la cantidad

            String codigoSecuenciaPromocion = GlobalFunctions.codigo_secuencia_promocion_productoVenta(Integer.parseInt(promoEntity2.getSalida()), cantidadProducto);

            if(!itemDetalle.getSec_promo().equals("")){
                codigoSecuenciaPromocion = itemDetalle.getSec_promo()+"-"+codigoSecuenciaPromocion;
            }

            dbclass.actualizarPedidoDetalle(Oc_numero, itemDetalle.getCip(),codigoSecuenciaPromocion);
        }

        return cantidadProducto;

    }

    private int cumpleCondicion(DB_PromocionDetalle promoEntity2, ArrayList<ItemProducto> productos) {
        // TODO Auto-generated method stub
        int cantidadProducto=0;
        int index;

        String condicion = promoEntity2.getCondicion();

        index = verificarSiExisteProducto(promoEntity2.getEntrada(), Integer.parseInt(promoEntity2.getTipo_unimed_entrada()), productos);

        if( index != -1){

            DBPedido_Detalle itemDetalle = dbclass.getPedidosDetalleEntity(Oc_numero, productos.get(index).getCodprod());

            if(promoEntity2.getTipo().equals("C")){

                if(itemDetalle.getCantidad() >= promoEntity2.getCant_condicion()){
                    if(condicion.equals("1")){
                        cantidadProducto = promoEntity2.getCant_promocion();
                    }else if(condicion.equals("3")){
                        cantidadProducto = (itemDetalle.getCantidad()/promoEntity2.getCant_condicion())*promoEntity2.getCant_promocion();
                    }
                    else if(condicion.equals("2")){
                        if(itemDetalle.getCantidad() <= promoEntity2.getCant_condicion()){
                            cantidadProducto = promoEntity2.getCant_promocion();
                        }

                    }
                }
                else{
                    if(condicion.equals("2")){
                        cantidadProducto = promoEntity2.getCant_promocion();
                    }
                }
            }
            else if(promoEntity2.getTipo().equals("M")){
                if(Double.parseDouble(itemDetalle.getPrecio_neto()) >= Double.parseDouble(promoEntity2.getMonto())){
                    if(condicion.equals("1")){
                        cantidadProducto = promoEntity2.getCant_promocion();
                    }else if(condicion.equals("3")){
                        int aux = (int)(Double.parseDouble(itemDetalle.getPrecio_neto())/Double.parseDouble(promoEntity2.getMonto()));
                        cantidadProducto = aux*promoEntity2.getCant_promocion();
                    }
                    else if(condicion.equals("2")){
                        if(itemDetalle.getCantidad() <= promoEntity2.getCant_condicion()){
                            cantidadProducto = promoEntity2.getCant_promocion();
                        }
                    }
                }
                else{
                    if(condicion.equals("2")){
                        cantidadProducto = promoEntity2.getCant_promocion();
                    }
                }
            }


            if(cantidadProducto > 0){

                //Actualizar la secuencia del producto que genero la promocion
                //Se debe guardar el codigo de salida de la promo seguido de la cantidad

                String codigoSecuenciaPromocion = GlobalFunctions.codigo_secuencia_promocion_productoVenta(Integer.parseInt(promoEntity2.getSalida()), cantidadProducto);

                if(!itemDetalle.getSec_promo().equals("")){
                    codigoSecuenciaPromocion = itemDetalle.getSec_promo()+"-"+codigoSecuenciaPromocion;
                }

                dbclass.actualizarPedidoDetalle(Oc_numero, itemDetalle.getCip(),codigoSecuenciaPromocion);
            }

        }


        return cantidadProducto;
    }


    private int verificarSiExisteProducto(String entrada, int tipo_und_med, ArrayList<ItemProducto> productos) {
        for(int i=0; i<productos.size();i++){
            String cod= productos.get(i).getCodprod();

            if(cod.equals(entrada)){

                //String codunimed = dbclass.obtener_codXdesunimed(productos.get(i).getDesunimed());
                //int tipo_unidad_medida = dbclass.isCodunimed_Almacen(cod, Integer.parseInt(codunimed));
                int tipo_unidad_medida = dbclass.isCodunimed_Almacen(cod, (productos.get(i).getCodunimed()));

                if(tipo_unidad_medida == tipo_und_med){
                    return i;
                }


            }

        }
        return -1;
    }


/*private void cumpleCondicion(DB_PromocionDetalle promoEntity, DBPedido_Detalle itemDetalle) {
	int cantidadProducto=0;
	
	cantidadProducto = verificar_condicionXtipo(promoEntity, itemDetalle);
	
	if(cantidadProducto != -99){
		
		if(dbclass.existePedidoDetalle(Oc_numero,promoEntity.getSalida())){
			dbclass.actualizarItem_promo(Oc_numero, promoEntity.getSalida(), dbclass.obtener_codunimedXtipo_unimed_salida(Integer.parseInt(promoEntity.getTipo_unimed_salida()),promoEntity.getSalida()),promoEntity.getCant_promocion());
		}else{
			agregarProductoxPromocion(promoEntity.getSalida(), promoEntity.getTipo_unimed_salida(), cantidadProducto,Integer.toString(promoEntity.getSecuencia()),promoEntity.getItem(),promoEntity.getAgrupado());
		}
		
	}else{
		dbclass.EliminarItemPedido2(promoEntity.getSalida(), Oc_numero,Integer.toString(promoEntity.getSecuencia()),Integer.toString(promoEntity.getItem()));
	}

	
}*/


    private void agregarProductoxPromocion(String salida,
                                           String tipo_unimed_salida, int cantidadProducto, String sec_promo, int item_promo, int agrup_promo) {
        // TODO Auto-generated method stub

        DBPedido_Detalle itemDetalle =new DBPedido_Detalle();
        itemDetalle.setOc_numero(edtNroPedido.getText().toString());
        itemDetalle.setCip(salida);
        itemDetalle.setEan_item("");
        itemDetalle.setPrecio_bruto("0");
        itemDetalle.setPrecio_neto("0");
        itemDetalle.setPercepcion("0.0");
        itemDetalle.setCantidad(cantidadProducto);
        itemDetalle.setTipo_producto("C");
        itemDetalle.setUnidad_medida((dbclass.obtener_codunimedXtipo_unimed_salida(Integer.parseInt(tipo_unimed_salida), salida)));
        itemDetalle.setPeso_bruto("0");
        itemDetalle.setFlag("N");

        itemDetalle.setSec_promo(GlobalFunctions.codigo_secuencia_promocion(Integer.parseInt(sec_promo), item_promo, agrup_promo));

        //itemDetalle.setItem_promo(item_promo);
        //itemDetalle.setAgrup_promo(agrup_promo);

        dbclass.AgregarPedidoDetalle(itemDetalle);
        Log.i("(PedidoActivity)agregarProductoxPromocion","promocion agregada codpro: "+itemDetalle.toString());

    }

    private int verificar_condicionXtipo(DB_PromocionDetalle promoEntity, DBPedido_Detalle itemDetalle){

        int cantidad = 0;

        if(promoEntity.getTipo().equals("C")){
            if(itemDetalle.getCantidad() >= promoEntity.getCant_condicion()){
                if(promoEntity.getCondicion().equals("3")){
                    cantidad = (itemDetalle.getCantidad()/promoEntity.getCant_condicion())*promoEntity.getCant_promocion();
                }else if(promoEntity.getCondicion().equals("1")){
                    cantidad = promoEntity.getCant_promocion();
                }
                else if(promoEntity.getCondicion().equals("2")){
                    if(itemDetalle.getCantidad() <= promoEntity.getCant_condicion()){
                        cantidad = promoEntity.getCant_promocion();
                    }
                }
            }
            else{
                if(promoEntity.getCondicion().equals("2")){
                    cantidad = promoEntity.getCant_promocion();
                }
            }
        }
        else if(promoEntity.getTipo().equals("M")){
            if(Double.parseDouble(itemDetalle.getPrecio_neto()) >= Double.parseDouble(promoEntity.getMonto())){
                if(promoEntity.getCondicion().equals("3")){
                    int aux = (int)(Double.parseDouble(itemDetalle.getPrecio_neto())/Double.parseDouble(promoEntity.getMonto()));
                    cantidad = aux*promoEntity.getCant_promocion();
                }else if(promoEntity.getCondicion().equals("1")){
                    cantidad = promoEntity.getCant_promocion();
                }
                else if(promoEntity.getCondicion().equals("2")){
                    if(itemDetalle.getCantidad() <= promoEntity.getCant_condicion()){
                        cantidad = promoEntity.getCant_promocion();
                    }
                }
            }
            else{
                if(promoEntity.getCondicion().equals("2")){
                    cantidad = promoEntity.getCant_promocion();
                }
            }
        }

        return cantidad;
    }


    private boolean promocion_registrada(DB_PromocionDetalle promocion,ArrayList<DB_PromocionDetalle> promociones_registradas){

        Iterator<DB_PromocionDetalle> it = promociones_registradas.iterator();

        while(it.hasNext()){
            Object objeto = it.next();
            DB_PromocionDetalle item = (DB_PromocionDetalle)objeto;

            if(item.getSecuencia() == promocion.getSecuencia()){
                if(item.getItem() == promocion.getItem()){
                    return true;
                }
            }

        }

        return false;
    }


    private boolean VerificarSiTienePromocion(String codpro, String und_medida_prod, String cod_politica) {
        // TODO Auto-generated method stub

        int tipo_unidad_medida = dbclass.isCodunimed_Almacen(codpro, und_medida_prod);
        //int tipo_unidad_medida = 0; 

        //verifica si hubo concidencia entre la unidad de medida del itemDetalle
        //y las unidades de media del producto
        if(tipo_unidad_medida != -1){
            //al_PromocionDetalle = dbclass.getPromocionesXProducto2(codpro,tipo_unidad_medida,codcli,codven,cod_politica);

            if(al_PromocionDetalle.size()==0){
                return false;
            }
            else
                return true;
        }else{

            Log.v("VerificarSiTienepromocion", "valor devuelto = -1, no hay concidencia }" +
                    "entre unidad_medida y tipo_unida_medida_entrada ");

            return false;
        }

    }


    //Retorna verdadero si el producto est� incluido en alguna promocion
    private String[] tienePromocion(String codpro) {
        // TODO Auto-generated method stub
        String[] cantidad= new String[promociones.size()];
        int i=0;
        for(DBTb_Promocion_Detalle obj : promociones) {
            if(obj.getEntrada().equals(codpro)){
                cantidad[i]=obj.getSecuencia()+"";
                i++;
            }
        }
        return cantidad;
    }




    Dialog dialogo_codFamiliar ;

    public void crearDialogo_CodigoFamiliar(){

        dialogo_codFamiliar= new Dialog(this);

        dialogo_codFamiliar.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialogo_codFamiliar.setContentView(R.layout.dialog_codigofamiliar);
        dialogo_codFamiliar.setCancelable(false);


        final ListView lstClientes = (ListView)dialogo_codFamiliar.findViewById(R.id.dialog_codigofamiliar_lvClientes);
        Button btnAceptar = (Button)dialogo_codFamiliar.findViewById(R.id.dialog_codigofamiliar_btnAceptar);
        Button btnCancelar = (Button)dialogo_codFamiliar.findViewById(R.id.dialog_codigofamiliar_btnCancelar);
        lstClientes.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        ArrayList<DBClientes> al_clientes = dbclass.getFamiliasxCliente(codcli);

        AdapterDialogCodFamiliar adaptador = new AdapterDialogCodFamiliar(this, al_clientes);
        lstClientes.setAdapter(adaptador);

        lstClientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View arg1,
                                    int position, long id) {

                selectedPosition2 = position;
                Log.w("ListClientesONclick", "position"+selectedPosition2);
            }
        });

        btnAceptar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                clienteEntity= (DBClientes)lstClientes.getAdapter().getItem(selectedPosition2);

                final Handler handle = new Handler();
                handle.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        // new asyncGuardarMotivo().execute("","");	
                        guardarCodFamiliar();
                        guardarCodcli();
                        asignarNombreCabecera();
                        dialogo_codFamiliar.dismiss();
                    }

                    private void asignarNombreCabecera() {
                        // TODO Auto-generated method stub
                        autocomplete.setText(nombre_familiar);
                    }

                    private void guardarCodcli() {
                        // TODO Auto-generated method stub
                        nombre_cliente = autocomplete.getText().toString();
                    }

                    private void guardarCodFamiliar() {
                        // TODO Auto-generated method stub
                        nombre_familiar = clienteEntity.getNomcli().toString();
                        cod_familiar = dbclass.obtenerCodigoCliente(nombre_familiar);
                    }
                }, 1000);





            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialogo_codFamiliar.dismiss();
            }
        });

        dialogo_codFamiliar.show();

    }


    public class AdapterDialogCodFamiliar extends ArrayAdapter<DBClientes>{

        Activity context;
        ArrayList<DBClientes> datos;

        public AdapterDialogCodFamiliar(Activity context, ArrayList<DBClientes> al_clientes) {

            super(context, android.R.layout.simple_list_item_single_choice);
            this.context=context;
            this.datos=al_clientes;
        }

        @Override
        public int getCount() {
            //getCount() represents how many items are in the list
            return datos.size();
        }

        @Override
        public DBClientes getItem(int position) {
            return datos.get(position);
        }


        @Override
        public View getView(int position, View convertview, ViewGroup parent){

            View item=convertview;
            ViewHolder holder;

            if(item==null){
                LayoutInflater inflater=context.getLayoutInflater();
                item=inflater.inflate(android.R.layout.simple_list_item_single_choice, null);

                holder=new ViewHolder();

                holder.des_cliente=(TextView)item.findViewById(android.R.id.text1);

                item.setTag(holder);

            }else{

                holder=(ViewHolder)item.getTag();

            }

            holder.des_cliente.setText(datos.get(position).getNomcli());

            return item;
        }


        public class ViewHolder{
            TextView des_cliente;
        }
    }


    private void crear_dialogo_post_envio(String titulo, String mensaje, int icon){

        AlertDialog.Builder dialogo2 = new AlertDialog.Builder(PedidosActivityVentana2.this);
        dialogo2.setTitle(titulo);
        dialogo2.setMessage(mensaje);
        dialogo2.setIcon(icon);
        dialogo2.setCancelable(false);
        dialogo2.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {

                if(origen.equals("REPORTES")){

                    finish();
                    Intent intent2 = new Intent(PedidosActivityVentana2.this, ReportesActivity.class );
                    intent2.putExtra("ORIGEN", "PEDIDOS");
                    startActivity(intent2);

                }else{
                    crear_dialogo_otro_pedido();
                }

            }
        });

        dialogo2.show();

    }


    private void crear_dialogo_otro_pedido(){

        AlertDialog.Builder alerta = new AlertDialog.Builder(PedidosActivityVentana2.this);
        alerta.setCancelable(false);
        alerta.setMessage("Desea realizar otro pedido para este cliente?");

        alerta.setPositiveButton("Si", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

                finish();

                Intent i=new Intent(PedidosActivityVentana2.this, PedidosActivityVentana2.class);
                i.putExtra("codven",codven);
                i.putExtra("NOMCLI",nomcli);
                i.putExtra("ORIGEN", "clientesac");
                i.putExtra("DIRECCION", item_direccion);
                startActivity(i);

            }
        });

        alerta.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                finish();

                Intent intent2 = new Intent(PedidosActivityVentana2.this, ReportesActivity.class );
                intent2.putExtra("ORIGEN", "PEDIDOS");
                startActivity(intent2);

            }
        });

        alerta.show();

    }


    private void crear_dialogo_guardar_modificar(String mensaje){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Importante");
        builder.setMessage(mensaje);
        builder.setIcon(R.drawable.ic_alert);
        builder.setCancelable(false);
        builder.setPositiveButton("Enviar al servidor", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {

                new async_envio_pedido().execute();
            }

        });
        builder.setNegativeButton("Guardar Localmente", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {

                if(origen.equals("REPORTES")){

                    finish();
                    Intent intent2 = new Intent(PedidosActivityVentana2.this, ReportesActivity.class );
                    intent2.putExtra("ORIGEN", "PEDIDOS");
                    startActivity(intent2);

                }else{
                    crear_dialogo_otro_pedido();
                }

            }
        });
        builder.create().show();

    }

}

