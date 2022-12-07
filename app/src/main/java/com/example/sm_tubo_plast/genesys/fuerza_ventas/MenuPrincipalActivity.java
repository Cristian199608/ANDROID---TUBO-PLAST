package com.example.sm_tubo_plast.genesys.fuerza_ventas;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.databinding.ActivityMenuPrincipalBinding;
import com.example.sm_tubo_plast.genesys.AccesosPerfil.AccesosOpciones;
import com.example.sm_tubo_plast.genesys.AccesosPerfil.ITiposDeAccesoMenuPrinicipal;
import com.example.sm_tubo_plast.genesys.AccesosPerfil.OptionMenuPrinicipal;
import com.example.sm_tubo_plast.genesys.datatypes.DBclasses;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.Reportes.ReportesActivity;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.Reportes.ReportesWebVentasVendedorActivity;
import com.example.sm_tubo_plast.genesys.session.SessionManager;
import com.example.sm_tubo_plast.genesys.util.GlobalFunctions;
import com.example.sm_tubo_plast.genesys.util.GlobalVar;
import com.example.sm_tubo_plast.genesys.util.UtilView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class MenuPrincipalActivity extends AppCompatActivity {

    String codven, url,catalog, userid, contrasenaid,servicio,servicio_local;
    boolean check_web,check_local;
    SessionManager session;
    String nomcli="";
    String origen="MENU";
    private String fecha_configuracion;
    private TextView txt_fecha_conf;

    double valorIGV;
    double limiteDescuento;
    DBclasses database;

    AccesosOpciones.OptionMenuPrincipal accesoMenuPrincipal;


    //Parametros de configuracion
    SharedPreferences preferencias_configuracion;
    SharedPreferences.Editor editor_preferencias;
    ActivityMenuPrincipalBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivityMenuPrincipalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        database = new DBclasses(getApplicationContext());
        // Session Manager Class
        session = new SessionManager(getApplicationContext());
        AdministrarAccesos();


        valorIGV = Double.parseDouble(database.getCambio("IGV"));
        limiteDescuento = Double.parseDouble(database.getCambio("limiteDescuento"));

        txt_fecha_conf = (TextView)findViewById(R.id.menu_principal_txtFechaConfiguracion);
        TextView tv_welcomeUser = (TextView)findViewById(R.id.tv_welcomeUser);

        DBclasses obj_dbclass = new DBclasses(getApplicationContext());
        fecha_configuracion = obj_dbclass.getFecha2();
        if(!fecha_configuracion.equals("")){
            txt_fecha_conf.setText(fecha_configuracion);
        }
        else{
            txt_fecha_conf.setText("Sin fecha");
        }
        //Preferencias de Configuracion
        preferencias_configuracion = getSharedPreferences("preferencias_configuracion", Context.MODE_PRIVATE);
        editor_preferencias = preferencias_configuracion.edit();
        editor_preferencias.putFloat("valorIGV", (float) valorIGV);
        editor_preferencias.putFloat("limiteDescuento", (float) limiteDescuento);
        editor_preferencias.commit();

        SharedPreferences prefs =  getSharedPreferences("MisPreferencias",Context.MODE_PRIVATE);
        final SharedPreferences preferencias_configuracion = getSharedPreferences("preferencias_configuracion", Context.MODE_PRIVATE);
        codven = prefs.getString("codven", "por_defecto");
        url = prefs.getString("url", "0");
        catalog = prefs.getString("catalog", "0");
        userid = prefs.getString("userid", "0");
        contrasenaid = prefs.getString("contrasenaid", "0");
        servicio = prefs.getString("servicio","0");
        servicio_local = prefs.getString("servicio_local","0");

        String vendedor_name=database.getVendedorByCodven(codven);
        tv_welcomeUser.setText("Bienvenido "+ vendedor_name);

        GlobalVar.servicio_principal_activo = prefs.getBoolean("servicio_principal_activo", true);
        GlobalVar.servicio_secundario_activo = prefs.getBoolean("servicio_secundario_activo", true);

        //Toast.makeText(getApplicationContext(), "principal "+GlobalVar.servicio_principal_activo, Toast.LENGTH_SHORT).show();
        //Toast.makeText(getApplicationContext(), "secundario "+GlobalVar.servicio_secundario_activo, Toast.LENGTH_SHORT).show();

        //Obteniendo las preferencias de configuracion
        String preferenciasJSON = preferencias_configuracion.getString("preferenciasJSON", "no_preferencias");
        boolean stockAutoSeleccionado;
        if(preferenciasJSON.equals("no_preferencias")){

            stockAutoSeleccionado = false;

        }else{
            Gson gson = new Gson();
            Type listType = new TypeToken<Map<String, Object>>(){}.getType();
            Map<String, Object> mapObject2 = gson.fromJson(preferenciasJSON, listType);
            stockAutoSeleccionado = (Boolean) mapObject2.get("preferencias_stock");
        }


        GlobalVar.autoActualizacionStock = stockAutoSeleccionado;
        //GlobalVar.autoActualizacionStock =prefs.getBoolean(SincronizacionConfiguracion.AUTO_ACTUALIZACION_STOCK, false);
        Log.i("MENU_PRINCIPAL_ACTIVITY","autoActualizacionStock= "+GlobalVar.autoActualizacionStock);

        GlobalVar.NombreWEB = GlobalFunctions.obtenerNombreWEB(servicio);
        GlobalVar.direccion_servicio = servicio;
        GlobalVar.direccion_servicio_local = servicio_local;
        if(GlobalVar.servicio_principal_activo){
            GlobalVar.id_servicio = GlobalVar.INTERNET;
            GlobalVar.urlService = GlobalVar.direccion_servicio;
        }
        else{
            GlobalVar.id_servicio = GlobalVar.LOCAL;
            GlobalVar.urlService = GlobalVar.direccion_servicio_local;
        }


        if (url == "0" || servicio == "0") {

            // Al logearse directamente, sin sincronizar (uso de datos locales)
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                    MenuPrincipalActivity.this);
            alertDialog.setCancelable(false);
            alertDialog.setTitle("Alerta");
            alertDialog
                    .setMessage("No tiene asignado un servidor asociado. Presiones Aceptar para asignar uno");
            alertDialog.setIcon(R.drawable.ic_alert);
            alertDialog.setPositiveButton("Aceptar",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            Intent i = new Intent(getApplicationContext(),
                                    SincronizarActivity.class);
                            i.putExtra("ORIGEN", "MENU");
                            // agregado 06-07-2013 (solucion para las ventanas
                            // multiples al salir de la aplicacion)
                            finish();
                            //
                            startActivity(i);

                        }
                    });
            alertDialog.show();
        }


        onCreateMainMenu(getWindow(),this);
        Window window=getWindow();
        final Activity activity= this;

        View mostrarAgenda= window.findViewById(R.id.ly_agenda);
        mostrarAgenda.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String idAlmacenSeleccionado;
                String vistaSeleccionado;
                boolean stockAuto;

                /* Obtenemos las preferencias guardadas anteriormente */
                String preferenciasJSON = preferencias_configuracion.getString("preferenciasJSON", "no_preferencias");
                Log.d("preferenciasJSON",preferenciasJSON);

                if(preferenciasJSON.equals("no_preferencias")){
                    vistaSeleccionado = "Vista 2";
                    stockAuto = false;
                    idAlmacenSeleccionado = "0";
                }else{
                    Gson gson = new Gson();
                    Type listType = new TypeToken<Map<String, Object>>(){}.getType();

                    Map<String, Object> mapObject2 = gson.fromJson(preferenciasJSON, listType);
                    vistaSeleccionado = (String)mapObject2.get("preferencias_vista");
                    stockAuto = (Boolean) mapObject2.get("preferencias_stock");
                    List<String> almacenAsociado = (List<String>) mapObject2.get("preferencias_almacen");
                    idAlmacenSeleccionado = almacenAsociado.get(0);
                    Log.d("DATOS:","\nvista_Seleccionado: "+vistaSeleccionado+"\nstock: "+stockAuto+"\nalmacen_seleccionado: "+almacenAsociado);
                }



                if(vistaSeleccionado.equals("Vista 1")){

                    final Intent ipedido = new Intent(activity,PedidosActivityVentana2.class);

                    ipedido.putExtra("codigoVendedor",codven);
                    ipedido.putExtra("nombreCliente",nomcli);
                    ipedido.putExtra("origen", origen);

                    activity.startActivityForResult(ipedido, 0);

                }else if(vistaSeleccionado.equals("Vista 2")){

                    final Intent ipedido = new Intent(activity,ActividadCampoAgendadoActivity.class);
                    ipedido.putExtra("codven",codven);
                    ipedido.putExtra("NOMCLI",nomcli);
                    ipedido.putExtra("ORIGEN", origen);



                    activity.startActivityForResult(ipedido, 0);

                }else{
                    Toast.makeText(getApplicationContext(), "Configure el tipo de visualizacion en Preferencias", Toast.LENGTH_SHORT).show();
                }

					 /*final Intent icliente = new Intent(activity,ClientesActivity.class);
					 icliente.putExtra("codven",codven);
					 //
                     activity.startActivityForResult(icliente, 0);*/

            }
        });

        View mostrarClientes= window.findViewById(R.id.ly_clientes);
        mostrarClientes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                final Intent icliente = new Intent(activity,ClientesActivity.class);
                icliente.putExtra("codven",codven);
                // ipedido.putExtra("codven",codven);
                activity.startActivityForResult(icliente, 0);
            }
        });

        /*View mostrarDepositos= window.findViewById(R.id.ly_deposito);
        mostrarDepositos.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                final Intent icliente = new Intent(activity,DepositosActivity.class);
                // ipedido.putExtra("codven",codven);
                activity.startActivityForResult(icliente, 0);
            }
        });*/

        View mostrarReportes= window.findViewById(R.id.ly_reportes);
        mostrarReportes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                final Intent ireportes = new Intent(activity, ReportesActivity.class);
                ireportes.putExtra("ORIGEN", "MENU_PRINCIPAL");
                // ipedido.putExtra("codven",codven);
                activity.startActivityForResult(ireportes, 0);
            }
        });

        View mostrarSincronizar= window.findViewById(R.id.iv_sincronizar);
        mostrarSincronizar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                final Intent isincronizar = new Intent(activity,SincronizarActivity.class);
                // ipedido.putExtra("codven",codven);
                isincronizar.putExtra("ORIGEN", "MENU");
                //agregado 06-07-2013 (solucion para las ventanas multiples al salir de la aplicacion)
                finish();
                //
                activity.startActivityForResult(isincronizar, 0);
                //activity.startActivity(isincronizar);
            }
        });


        View mostrarCobranza= window.findViewById(R.id.ly_cobranza);
        mostrarCobranza.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                final Intent icobranza = new Intent(activity,CobranzaActivity2.class);
                // ipedido.putExtra("codven",codven);
                activity.startActivityForResult(icobranza, 0);
            }
        });

        View mostrarProductos= window.findViewById(R.id.ly_productos);
        mostrarProductos.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                final Intent iproductos = new Intent(activity, ProductosCursorAdapter.class);
                // ipedido.putExtra("codven",codven);
                activity.startActivityForResult(iproductos, 0);

            }
        });
        /* ***************ENVIAR MENSAJE DE SINCRONIZACION************** */
        boolean sincronizacionCorrecta = preferencias_configuracion.getBoolean("preferencias_sincronizacionCorrecta", false);
        if (sincronizacionCorrecta == false) {
            android.app.AlertDialog.Builder alerta = new android.app.AlertDialog.Builder(this);
            alerta.setTitle("Sincronizacion incompleta");
            alerta.setMessage("Los datos estan incompletos, sincronice correctamente");
            alerta.setIcon(R.drawable.icon_warning);
            alerta.setCancelable(false);
            alerta.setPositiveButton("Ir a Sincronizar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mostrarSincronizar.performClick();
                }
            });
            alerta.show();
        }
        /****************************************************************/


        View mostrarEstadisticas= window.findViewById(R.id.iv_estadisticas);
        mostrarEstadisticas.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

//             final Intent iestadisticas = new Intent(activity,EstadisticasActivity.class);
                final Intent iestadisticas = new Intent(activity, ReportesWebVentasVendedorActivity.class);
                iestadisticas.putExtra("codven",codven);
                activity.startActivityForResult(iestadisticas, 0);
            }
        });

        View mostrarSeguimientoOP= window.findViewById(R.id.iv_seguimiento_pedido);
        mostrarSeguimientoOP.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
//                final Intent icliente = new Intent(activity,CH_ClientesDevolucion.class);
//                icliente.putExtra("codven",codven);
//                activity.startActivityForResult(icliente, 0);
                final Intent icliente = new Intent(activity,SeguimientoPedidoActivity.class);
                icliente.putExtra("codven",codven);
                activity.startActivityForResult(icliente, 0);
            }
        });

        UtilView.Efectos(this, mostrarClientes, R.color.white);
        UtilView.Efectos(this, mostrarAgenda, R.color.white);

        UtilView.Efectos(this, mostrarProductos, R.color.white);
        UtilView.Efectos(this, mostrarSeguimientoOP, R.color.white);
        UtilView.Efectos(this, mostrarCobranza, R.color.white);
        UtilView.Efectos(this, mostrarReportes, R.color.white);
        UtilView.Efectos(this, mostrarEstadisticas, R.color.white);
        UtilView.Efectos(this, mostrarSincronizar, R.color.white);

    }

    private void AdministrarAccesos() {
        accesoMenuPrincipal=new AccesosOpciones.OptionMenuPrincipal(this);
        OptionMenuPrinicipal optiones=accesoMenuPrincipal.accesoOptionMenuPrincipal();
        /*binding.lContainerMenuCotizacionAndPedido.setAlpha(0.3f);
        for (int i = 0; i < binding.lContainerMenuCotizacionAndPedido.getChildCount(); i++) {
            binding.lContainerMenuCotizacionAndPedido.getChildAt(i).setOnClickListener(null);
        }*/
        binding.lContainerMenuCliente.setVisibility(optiones.getMenuCliente()?View.VISIBLE:View.GONE);
        binding.lContainerMenuCotizacionAndPedido.setVisibility(optiones.getMenuCotizacionAndPedido()?View.VISIBLE:View.GONE);
        binding.lContainerMenuAgenda.setVisibility(optiones.getMenuAgenda()?View.VISIBLE:View.GONE);
        binding.lContainerMenuProducto.setVisibility(optiones.getMenuProducto()?View.VISIBLE:View.GONE);
        binding.lContainerMenuSeguimientoOp.setVisibility(optiones.getMenuSeguimientoOp()?View.VISIBLE:View.GONE);
        binding.lContainerMenuVentas.setVisibility(optiones.getMenuVentas()?View.VISIBLE:View.GONE);
        binding.lContainerMenuCuentasXCobrar.setVisibility(optiones.getMenuCuentasXCobrar()?View.VISIBLE:View.GONE);
        binding.lContainerMenuEstadistica.setVisibility(optiones.getMenuEstadistica()?View.VISIBLE:View.GONE);
        binding.lContainerMenuConsultaFacturas.setVisibility(optiones.getMenuConsultaFacturas()?View.VISIBLE:View.GONE);
        binding.lContainerMenuReportes.setVisibility(optiones.getMenuReportes()?View.VISIBLE:View.GONE);
        binding.lContainerMenuCuotaVentas.setVisibility(optiones.getMenuCuotaVentas()?View.VISIBLE:View.GONE);
        binding.lContainerMenuSincronizar.setVisibility(optiones.getMenuSincronizar()?View.VISIBLE:View.GONE);
    }

    public void NOFunciona(View view){
        Snackbar snackbar= Snackbar.make(
                view,
                "Esta opción no esta disponible",
                BaseTransientBottomBar.LENGTH_SHORT
        );
        snackbar.setBackgroundTint(getResources().getColor(R.color.red_600));
        snackbar.show();

    }

    public static void onCreateMainMenu(Window window, final Activity activity){
        View mostrar = (View) window.findViewById(R.id.ly_agenda);
        View mostrarCliente = (View) window.findViewById(R.id.ly_clientes);
//        View mostrarDepoitos = (View) window.findViewById(R.id.ly_deposito);
        View mostrarReportes = (View) window.findViewById(R.id.ly_reportes);
        View mostrarSincronizar = (View) window.findViewById(R.id.iv_sincronizar);
        View mostrarCobranza = (View) window.findViewById(R.id.ly_cobranza);
        View mostrarProductos= (View) window.findViewById(R.id.ly_productos);
        View mostrarEstadisticas= (View) window.findViewById(R.id.ly_estadisticas);
        //View mostrarSeguimientoOP= (View) window.findViewById(R.id.ly_devoluciones);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Cerrando Aplicacion")
                .setMessage("seguro que quieres salir de la aplicación?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }
}
