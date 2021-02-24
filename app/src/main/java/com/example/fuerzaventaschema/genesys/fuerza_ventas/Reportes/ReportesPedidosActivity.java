package com.example.fuerzaventaschema.genesys.fuerza_ventas.Reportes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.example.fuerzaventaschema.R;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.channels.FileChannel;
import java.nio.charset.CodingErrorAction;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.fuerzaventaschema.genesys.DAO.DAO_Pedido;
import com.example.fuerzaventaschema.genesys.DAO.DAO_RegistroBonificaciones;
import com.example.fuerzaventaschema.genesys.datatypes.DBCta_Ingresos;
import com.example.fuerzaventaschema.genesys.datatypes.DBMotivo_noventa;
import com.example.fuerzaventaschema.genesys.datatypes.DBPedido_Cabecera;
import com.example.fuerzaventaschema.genesys.datatypes.DBPedido_Detalle;
import com.example.fuerzaventaschema.genesys.datatypes.DBSync_soap_manager;
import com.example.fuerzaventaschema.genesys.datatypes.DB_Almacenes;
import com.example.fuerzaventaschema.genesys.datatypes.DB_ObjPedido;
import com.example.fuerzaventaschema.genesys.datatypes.DB_RegistroBonificaciones;
import com.example.fuerzaventaschema.genesys.datatypes.DBclasses;
import com.example.fuerzaventaschema.genesys.fuerza_ventas.CH_DevolucionesActivity;
import com.example.fuerzaventaschema.genesys.fuerza_ventas.ClientesActivity.Clientes_LazyAdapter;
import com.example.fuerzaventaschema.genesys.fuerza_ventas.ClientesActivity.no_ventaAdapter;
import com.example.fuerzaventaschema.genesys.fuerza_ventas.ClientesActivity.no_ventaAdapter.ViewHolder;
import com.example.fuerzaventaschema.genesys.fuerza_ventas.Dialog.DialogFragment_enviarCotizacion;
import com.example.fuerzaventaschema.genesys.fuerza_ventas.PedidosActivity;
import com.example.fuerzaventaschema.genesys.fuerza_ventas.PedidosActivityVentana2;
import com.example.fuerzaventaschema.genesys.service.ConnectionDetector;
import com.example.fuerzaventaschema.genesys.util.GlobalFunctions;
import com.example.fuerzaventaschema.genesys.util.GlobalVar;
import com.example.fuerzaventaschema.genesys.util.VARIABLES;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.LabeledIntent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.RadioGroup.OnCheckedChangeListener;

import me.piruin.quickaction.ActionItem;
import me.piruin.quickaction.QuickAction;

@SuppressLint("LongLogTag")
public class ReportesPedidosActivity extends FragmentActivity {

    public static final String TAG  = "ReportesPedidosActivity";
    public static final String KEY_TOTAL = "total";
    public static final String KEY_NOMCLIENTE = "nombrecliente";
    public static final String KEY_TIPOPAGO = "tipopago";
    public static final String KEY_NUMOC = "numoc";
    public static final String KEY_ESTADO = "estado";
    public static final String KEY_MENSAJE = "mensaje";
    public static final String KEY_TOTAL_PERCEPCION = "totalPercepcion";
    public static final String KEY_MONEDA = "moneda";

    public String numfactura;
    public String saldo = "0";
    String total = "0";
    double peso = 0;
    String sec_cta_ingresos;
    String codcli;
    String nomcli;
    String acuenta_total;
    Dialog dialogo;
    String numOc, codven;
    DBMotivo_noventa item;
    DBclasses obj_dbclasses;
    ArrayList<HashMap<String, String>> pedidos = new ArrayList<HashMap<String, String>>();
    ArrayList<DB_RegistroBonificaciones> registroBonificaciones;
    Gson gson = new Gson();
    DAO_RegistroBonificaciones DAORegistroBonificaciones;
    ListView list;
    // ReportesPedidosAdapter adapter_pedido;
    PedidosActivityVentana2 ob = new PedidosActivityVentana2();
    LayoutInflater inflater;
    private static final int ID_MODIFICAR = 1;
    private static final int ID_DETALLE = 2;
    private static final int ID_DELETE = 0;
    private static final int ID_ANULAR = 3;
    private static final int ID_FORZAR = 4;
    private static final int ID_ENVIAR_CORREO = 5;
    private static final int ID_GENERAR_PEDIDO = 6;

    DecimalFormat formateador;
    ArrayList<HashMap<String, String>> searchResults;
    public String oc_numero = "";
    public String cond_pago = "";
    ReportesPedidos_Adapter adapter;
    private int mSelectedRow = 0;
    int selectedPosition1 = 0;
    public double montoTotal_soles;
    public double montoTotal_dolares;
    DBPedido_Cabecera itemCabecera;
    ArrayList<DBPedido_Cabecera> lista_pedidos;
    TextView tv_montoTotal_soles, btn_peso, tv_totalPedidos, tv_paqueteDatos,
            tv_montoTotal_dolares;
    ConnectionDetector connection;
    int contador = 0;
    public String globalFlag = "";
    String fecha2 = "";

    int item_direccion;
    String tipoRegistro;
    ConnectionDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportes_pedidos);


        DAORegistroBonificaciones = new DAO_RegistroBonificaciones(
                getApplicationContext());
        obj_dbclasses = new DBclasses(getApplicationContext());
        cd = new ConnectionDetector(ReportesPedidosActivity.this);

        tv_montoTotal_soles = (TextView) findViewById(R.id.rpt_pedidos_txtTotal);
        btn_peso = (TextView) findViewById(R.id.rpt_pedidos_txtTotal_peso);
        tv_totalPedidos = (TextView) findViewById(R.id.rpt_pedidos_txtTotal_pedidos);

        lista_pedidos = new ArrayList<DBPedido_Cabecera>();
        tv_paqueteDatos = (TextView) findViewById(R.id.tv_paqueteDatos);
        tv_montoTotal_dolares = (TextView) findViewById(R.id.rpt_pedidos_tvTotalDolares);
        fecha2 = obj_dbclasses.getFecha2();

        Log.d("ALERT - REPORTE - fecha:", fecha2);

        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        SharedPreferences prefs = getSharedPreferences("MisPreferencias",
                Context.MODE_PRIVATE);
        codven = prefs.getString("codven", "por_defecto");

        Log.d("ALERT - REPORTE - cod vend:", codven);

        list = (ListView) findViewById(R.id.lv_reportes_pedidos);

        formateador = GlobalFunctions.formateador();

        // cargarPedidos("","");
        new asyncBuscar().execute("", "");

        tv_totalPedidos.setText("Cantidad:" + contador);
        cargarListView();

        connection = new ConnectionDetector(getApplicationContext());

        ActionItem modificarItem = new ActionItem(ID_MODIFICAR, "Modificar",R.drawable.icon_edit_file_24dp);
        ActionItem acceptItem = new ActionItem(ID_DETALLE, "Ver Detalle",R.drawable.icon_show_file_24dp);
        ActionItem deleteItem = new ActionItem(ID_DELETE, "Eliminar",R.drawable.icon_delete_file_24dp);
        ActionItem anularItem = new ActionItem(ID_ANULAR, "Anular",R.drawable.icon_cancel_file_24dp);
        ActionItem forzarEnvio = new ActionItem(ID_FORZAR, "Forzar Envio",R.drawable.ic_devolucion);
        ActionItem enviarCorreo = new ActionItem(ID_ENVIAR_CORREO, "Enviar por correo",R.drawable.icon_message_read_24dp);
        ActionItem generarPedido = new ActionItem(ID_GENERAR_PEDIDO, "Generar pedido",R.drawable.icon_inspection);

        final QuickAction mQuickAction = new QuickAction(this);
        final QuickAction mQuickActionCotizacion = new QuickAction(this);

        mQuickAction.addActionItem(modificarItem);
        mQuickAction.addActionItem(acceptItem);
        mQuickAction.addActionItem(anularItem);
        //mQuickAction.addActionItem(forzarEnvio);

        mQuickActionCotizacion.addActionItem(modificarItem);
        mQuickActionCotizacion.addActionItem(acceptItem);
        mQuickActionCotizacion.addActionItem(anularItem);
        //mQuickActionCotizacion.addActionItem(forzarEnvio);
        mQuickActionCotizacion.addActionItem(enviarCorreo);
        mQuickActionCotizacion.addActionItem(generarPedido);


        // setup the action item click listener
        mQuickAction
                .setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {

                    @Override
                    public void onItemClick(ActionItem item) {
                        int actionId=item.getActionId();
                        if (actionId == ID_ANULAR) {

                            async_getFlagPedido con = new async_getFlagPedido();
                            con.execute(oc_numero, "MOTIVO");

                        }
                        /*
                         * else if(actionId == ID_DELETE){ async_getFlagPedido
                         * con= new async_getFlagPedido();
                         * con.execute(oc_numero,"ELIMINAR"); }
                         */
                        else if (actionId == ID_MODIFICAR) {
                            if (obj_dbclasses.getTipoRegistro(oc_numero).equals("DEVOLUCION")) {
                                Toast.makeText(getApplicationContext(), "Accion no permitida", Toast.LENGTH_SHORT).show();
                            }else{
                                async_getFlagPedido con = new async_getFlagPedido();

                                if (obj_dbclasses.obtenerCodNoVentaxOC(oc_numero) > 0) {
                                    con.execute(oc_numero, "MOTIVO");
                                } else if (obj_dbclasses.obtenerCodNoVentaxOC(oc_numero) == 0) {
                                    con.execute(oc_numero, "PEDIDO-MODIFICAR");
                                }
                            }

                        }

                        else if (actionId == ID_FORZAR) {

                            asyncVerificacion_individual verif = new asyncVerificacion_individual();
                            verif.execute(oc_numero);

                        }

                        else if (actionId == ID_DETALLE) {
                            if (obj_dbclasses.getTipoRegistro(oc_numero).equals("DEVOLUCION")) {
                                SharedPreferences prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
                                String codven = prefs.getString("codven", "");

                                final Intent ipedido = new Intent(getApplicationContext(), CH_DevolucionesActivity.class);
                                ipedido.putExtra("codigoVendedor", codven);
                                ipedido.putExtra("nombreCliente", nomcli);
                                ipedido.putExtra("origen", "REPORTES-PEDIDO");
                                ipedido.putExtra("OC_NUMERO", oc_numero);
                                startActivity(ipedido);

                            } else {
                                async_getFlagPedido con = new async_getFlagPedido();
                                con.execute(oc_numero, "PEDIDO-DETALLE");
                            }


                        } else if(actionId == ID_ENVIAR_CORREO){
                            if (obj_dbclasses.getFlagPedido(oc_numero).equals("E")) {

                            }else{
                                Toast.makeText(getApplicationContext(), "Antes debe enviar el pedido", Toast.LENGTH_SHORT).show();
                            }

                            DialogFragment_enviarCotizacion dialog_preferencias = new DialogFragment_enviarCotizacion(getParent(), obj_dbclasses, oc_numero,codven);
                            dialog_preferencias.show(getSupportFragmentManager(), "dialogPreferencias");
                            dialog_preferencias.setCancelable(false);

                        }
                    }
                });



        mQuickActionCotizacion
                .setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
                    @Override
                    public void onItemClick(ActionItem item) {
                        int actionId=item.getActionId();

                        if (actionId == ID_ANULAR) {

                            async_getFlagPedido con = new async_getFlagPedido();
                            con.execute(oc_numero, "MOTIVO");

                        }
                        /*
                         * else if(actionId == ID_DELETE){ async_getFlagPedido
                         * con= new async_getFlagPedido();
                         * con.execute(oc_numero,"ELIMINAR"); }
                         */
                        else if (actionId == ID_MODIFICAR) {
                            if (obj_dbclasses.getTipoRegistro(oc_numero).equals("DEVOLUCION")) {
                                Toast.makeText(getApplicationContext(), "Accion no permitida", Toast.LENGTH_SHORT).show();
                            }else{
                                async_getFlagPedido con = new async_getFlagPedido();

                                if (obj_dbclasses.obtenerCodNoVentaxOC(oc_numero) > 0) {
                                    con.execute(oc_numero, "MOTIVO");
                                } else if (obj_dbclasses.obtenerCodNoVentaxOC(oc_numero) == 0) {
                                    con.execute(oc_numero, "PEDIDO-MODIFICAR");
                                }
                            }
                        }

                        else if (actionId == ID_FORZAR) {

                            asyncVerificacion_individual verif = new asyncVerificacion_individual();
                            verif.execute(oc_numero);

                        }

                        else if (actionId == ID_DETALLE) {
                            if (obj_dbclasses.getTipoRegistro(oc_numero).equals("DEVOLUCION")) {
                                SharedPreferences prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
                                String codven = prefs.getString("codven", "");

                                final Intent ipedido = new Intent(getApplicationContext(), CH_DevolucionesActivity.class);
                                ipedido.putExtra("codigoVendedor", codven);
                                ipedido.putExtra("nombreCliente", nomcli);
                                ipedido.putExtra("origen", "REPORTES-PEDIDO");
                                ipedido.putExtra("OC_NUMERO", oc_numero);
                                startActivity(ipedido);

                            } else {
                                async_getFlagPedido con = new async_getFlagPedido();
                                con.execute(oc_numero, "PEDIDO-DETALLE");
                            }


                        } else if(actionId == ID_ENVIAR_CORREO){
                            if (obj_dbclasses.getFlagPedido(oc_numero).equals("E")) {
                                DialogFragment_enviarCotizacion dialog_preferencias = new DialogFragment_enviarCotizacion(getParent(), obj_dbclasses, oc_numero,codven);
                                dialog_preferencias.show(getSupportFragmentManager(), "dialogPreferencias");
                                dialog_preferencias.setCancelable(false);
                            }else{
                                Toast.makeText(getApplicationContext(), "Primero debe enviar la cotización", Toast.LENGTH_SHORT).show();
                            }



                        } else if(actionId == ID_GENERAR_PEDIDO){
                            DBPedido_Cabecera cabecera = obj_dbclasses.getRegistroPedidoCabecera(oc_numero);
                            String pedidoAnterior = cabecera.getOc_numero();
                            String numOc = obj_dbclasses.obtenerMaxNumOc(codven);
                            String fecha_configuracion = obj_dbclasses.getCambio("Fecha");
                            String nuevoOc_numero = codven + PedidosActivity.calcularSecuencia(numOc,fecha_configuracion);
                            cabecera.setOc_numero(nuevoOc_numero);
                            cabecera.setPedidoAnterior(pedidoAnterior);
                            cabecera.setTipoRegistro(PedidosActivity.TIPO_PEDIDO);//Se indica que se guardar como pedido
                            DAO_Pedido DAOPedidoDetalle = new DAO_Pedido(getApplicationContext());
                            DAOPedidoDetalle.ClonarPedido(cabecera);//Se guarda referencia del pedido anterior
                            GlobalFunctions.showCustomToast(ReportesPedidosActivity.this, "Nuevo pedido Generado ! "+nuevoOc_numero, GlobalFunctions.TOAST_DONE);
                            new asyncBuscar().execute("", "");
                        }
                    }
                });



        list.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                mSelectedRow = position; // set the selected row



                String numoc = ((TextView) view.findViewById(R.id.rpt_pedido_tv_oc_numero)).getText().toString();
                String nomc = ((TextView) view.findViewById(R.id.rpt_pedido_tv_cliente)).getText().toString();
                String tipo = ((TextView) view.findViewById(R.id.tv_tipoRegistro)).getText().toString();

                if (tipo.equals("C")) {
                    tipoRegistro = PedidosActivity.TIPO_COTIZACION;
                    mQuickActionCotizacion.show(view);
                }else if (tipo.equals("D")){
                    tipoRegistro = PedidosActivity.TIPO_DEVOLUCION;
                    mQuickAction.show(view);
                }else{
                    tipoRegistro = PedidosActivity.TIPO_PEDIDO;
                    mQuickAction.show(view);
                }

                oc_numero = numoc;
                Log.w("OC_NUMERO", oc_numero);
                nomcli = nomc;
                codcli = obj_dbclasses.obtenerCodigoCliente(nomcli);
                item_direccion = obj_dbclasses.obtenerSitioXocnumero(oc_numero);

            }
        });
    }

    private void cargarListView() {
        // TODO Auto-generated method stub

        LayoutInflater inflater = LayoutInflater.from(this);
        View emptyView = inflater
                .inflate(R.layout.list_empty_view_unused, null);
        emptyView.setVisibility(View.GONE);
        ((ViewGroup) list.getParent()).addView(emptyView);
        list.setEmptyView(emptyView);

        adapter = new ReportesPedidos_Adapter(getApplicationContext(),R.layout.item_reporte_pedido, pedidos);

        // adapter_pedido=new ReportesPedidosAdapter(this, pedidos);
        list.setAdapter(adapter);

        if (pedidos.isEmpty()) {
            btn_peso.setVisibility(View.INVISIBLE);
            tv_montoTotal_soles.setVisibility(View.INVISIBLE);
            tv_montoTotal_dolares.setVisibility(View.INVISIBLE);
            tv_totalPedidos.setVisibility(View.INVISIBLE);

        } else {
            ((ViewGroup) list.getParent()).removeView(emptyView);
            tv_totalPedidos.setText("Cantidad: " + contador);
            emptyView.setVisibility(View.GONE);
            tv_montoTotal_soles.setText("Total(S/.) "
                    + formateador.format(redondear(montoTotal_soles)));
            tv_montoTotal_dolares.setText("Total($.) "
                    + formateador.format(redondear(montoTotal_dolares)));
            btn_peso.setText("Peso(Kg) " + formateador.format(redondear(peso)));

            btn_peso.setVisibility(View.VISIBLE);
            tv_montoTotal_soles.setVisibility(View.VISIBLE);
            tv_montoTotal_dolares.setVisibility(View.VISIBLE);
            tv_totalPedidos.setVisibility(View.VISIBLE);
        }
    }

    public double CalcularPaqueteDatos() {
        double cantidadDatos = 0.0;
        int cantidadDatosAux = 0;
        ArrayList<DB_ObjPedido> lista_obj_pedido = new ArrayList<DB_ObjPedido>();
        lista_obj_pedido = obj_dbclasses.getTodosObjPedido_json_flagp();

        if (lista_obj_pedido.size() == 0) {
            Log.d("", "No hay datos para sincronizar");
            return 0;
        }
        int cantidadBonificacion = 0;
        for (int i = 0; i < lista_obj_pedido.size(); i++) {
            // Seteo del detalle del pedido por el oc_numero
            ArrayList<DBPedido_Detalle> detalles = new ArrayList<DBPedido_Detalle>();
            detalles = obj_dbclasses.getPedido_detalles(lista_obj_pedido.get(i)
                    .getOc_numero());
            lista_obj_pedido.get(i).setDetalles(detalles);

            // Seteo del registro de bonificaciones por el oc_numero
            registroBonificaciones = new ArrayList<DB_RegistroBonificaciones>();

            registroBonificaciones = DAORegistroBonificaciones
                    .getRegistroBonificaciones(lista_obj_pedido.get(i)
                            .getOc_numero());
            cantidadBonificacion = cantidadBonificacion
                    + (gson.toJson(registroBonificaciones).length());
            /*
             * if (registroBonificaciones.isEmpty()) {
             *
             * }else{ cantidadBonificacion = cantidadBonificacion +
             * (gson.toJson(registroBonificaciones).length()); }
             */
        }

        String cadena = gson.toJson(lista_obj_pedido);
        Log.d("", "Cantidad de datos pedidos: " + cadena.length());
        cantidadDatosAux = (cadena.length()) + cantidadBonificacion;
        Log.d("", "Cantidad de datos: " + cantidadDatosAux);
        cantidadDatos = (cantidadDatosAux / 1024.0);
        double kilobytes = Math.rint(cantidadDatos * 100) / 100;

        cantidadDatos = (kilobytes / 1024.0);
        double megabytes = Math.rint(cantidadDatos * 10000) / 10000;

        return megabytes;
    }

    public void cargarPedidos(String tipo, String valor) {
        lista_pedidos.clear();
        contador = 0;
        peso = 0;
        montoTotal_soles = 0;
        montoTotal_dolares = 0;

        if (tipo.equalsIgnoreCase("cliente")) {
            lista_pedidos = obj_dbclasses
                    .getPedidosCabeceraxNombreCliente(valor);
        } else if (tipo.equalsIgnoreCase("documento")) {
            lista_pedidos = obj_dbclasses.getPedidosCabeceraxDocumento(valor);
        } else {
            lista_pedidos = obj_dbclasses.getPedidosCabecera();
        }

        Iterator<DBPedido_Cabecera> it = lista_pedidos.iterator();
        Log.w("Elementos de la lista", "tamaño: " + lista_pedidos.size());
        while (it.hasNext()) {
            Object objeto = it.next();
            DBPedido_Cabecera cta = (DBPedido_Cabecera) objeto;

            HashMap<String, String> map = new HashMap<String, String>();
            map.put(KEY_NOMCLIENTE,"" + obj_dbclasses.obtenerNomcliXCodigo(cta.getCod_cli()));
            map.put(KEY_TIPOPAGO, "" + obtenerCond_Pago(cta.getCond_pago()));

            if (cta.getCod_noventa() > 0) {
                map.put(KEY_TOTAL, obj_dbclasses.obtenerMotivoxCodigo(cta.getCod_noventa()));
            } else {
                Log.d("getMonto_total", ">" + cta.getMonto_total());
                Log.d("getPercepcion_total", ">" + cta.getPercepcion_total());
                map.put(KEY_TOTAL, formateador.format(Double.parseDouble(cta.getMonto_total())+ Double.parseDouble(cta.getPercepcion_total())));
            }

            map.put(KEY_NUMOC, cta.getOc_numero());
            map.put(KEY_ESTADO, cta.getEstado());
            map.put(KEY_MENSAJE, cta.getMensaje());
            map.put(KEY_MONEDA, cta.getMoneda());
            map.put("flag", cta.getFlag());
            map.put("tipoRegistro", cta.getTipoRegistro());
            map.put("pedidoAnterior", cta.getPedidoAnterior());
            map.put("latitud", cta.getLatitud());

            if (cta.getFlag().equals("P")) {
                pedidos.add(map);
                if (cta.getMoneda().equals(PedidosActivity.MONEDA_PEN)) {
                    montoTotal_soles = montoTotal_soles+ Double.parseDouble(cta.getMonto_total());
                } else {
                    montoTotal_dolares = montoTotal_dolares+ Double.parseDouble(cta.getMonto_total());
                }
                peso = peso + Double.parseDouble(cta.getPeso_total());
                contador = contador + 1;
            } else {
                Log.w("fecha_oc", cta.getFecha_oc());
                pedidos.add(map);
                if (cta.getMoneda().equals(PedidosActivity.MONEDA_PEN)) {
                    montoTotal_soles = montoTotal_soles+ Double.parseDouble(cta.getMonto_total());
                } else {
                    montoTotal_dolares = montoTotal_dolares+ Double.parseDouble(cta.getMonto_total());
                }
                peso = peso + Double.parseDouble(cta.getPeso_total());
                contador = contador + 1;
            }
        }

    }


    private boolean pedidoHoy(String fecha_oc) {
        // TODO Auto-generated method stub
        Log.w("PEDIDO CABECERA MES DIA_PEDIDO EXISTE",
                fecha_oc.substring(0, 2) + "-" + fecha_oc.substring(3, 5) + '-'
                        + fecha_oc.substring(6, 10) + "/fecha-conf->/"
                        + getDia() + '-' + getMes() + '-' + getYear());
        if (fecha_oc.substring(6, 10).equals(getYear())
                && fecha_oc.substring(3, 5).equals(getMes())
                && fecha_oc.substring(0, 2).equals(getDia())) {
            return true;
        } else
            return false;
    }

    private boolean pedidoDay(String oc_numero2) {
        // TODO Auto-generated method stub\
        Log.w("PEDIDO CABECERA MES DIA", oc_numero2.substring(0, 2) + "-"
                + oc_numero2.substring(3, 5) + getMes() + getDia());
        if (oc_numero2.substring(3, 5).equals(getMes())
                && oc_numero2.substring(0, 2).equals(getDia())) {
            return true;
        } else
            return false;
    }

    public String getDia() {
        return fecha2.substring(0, 2);
    }

    public String getMes() {
        return fecha2.substring(3, 5);
    }

    public String getYear() {
        return fecha2.substring(6, 10);
    }

    public String obtenerCond_Pago(String cond) {
        String cond_pag = "";
        if (cond.equals("01")) {
            cond_pag = "Contado";
        } else if (cond.equals("02")) {
            cond_pag = "Credito";
        } else if (cond.equals("07")) {
            cond_pag = "Transferencia";
        } else
            cond_pag = "";
        return cond_pag;
    }

    public BigDecimal redondear(double val) {
        String r = val + "";
        BigDecimal big = new BigDecimal(r);
        big = big.setScale(2, RoundingMode.HALF_UP);
        return big;
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Alternativa 1
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_reportespedidos, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.menu_reportespedidos:
//
//                GlobalFunctions.backupdDatabase();
//                new asyncEnviarPendientes().execute("");
//
//                return true;
//            case R.id.menu_reportespedidos_verificar:
//                new asyncVerificarPedidos().execute("");
//                return true;
//
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    class asyncEnviarPendientes extends AsyncTask<String, String, String> {

        String user, pass;
        ProgressDialog pDialog;

        protected void onPreExecute() {
            // para el progress dialog
            pDialog = new ProgressDialog(ReportesPedidosActivity.this);
            pDialog.setMessage("enviando pendientes...\n\n"
                    + GlobalVar.urlService);
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... params) {
            // obtnemos usr y pass
            String mensaje = "";
            DBSync_soap_manager soap_manager = new DBSync_soap_manager(
                    getApplicationContext());

            ConnectionDetector connection = new ConnectionDetector(
                    getApplicationContext());

            if (connection.hasActiveInternetConnection(getApplicationContext())) {

                int respuesta, respuesta2;

                try {
                    respuesta = soap_manager.actualizarObjPedido();
                    // respuesta2 =
                    // soap_manager.actualizarRegistroBonificaciones();

                    if (respuesta == 0) {
                        mensaje = "No hay pedidos pendientes";
                    } else if (respuesta == 1) {
                        mensaje = "Envio completo";
                    }

                } catch (JsonParseException e) {
                    mensaje = "No se pudo verificar";
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                    mensaje = "Error al enviar";
                }

            } else {
                mensaje = "Sin conexion al servidor";
            }

            pedidos.clear();
            cargarPedidos("", "");

            return mensaje;
        }

        protected void onPostExecute(String mensaje) {

            Toast toast = Toast.makeText(getApplicationContext(), mensaje,
                    Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

            pDialog.dismiss();// ocultamos progess dialog.
            Log.e("onPostExecute= Enviado", "" + mensaje);

            ((ReportesPedidos_Adapter) list.getAdapter())
                    .notifyDataSetChanged();

            tv_totalPedidos.setText("Cantidad:" + contador);

        }

    }

    public class ReportesPedidos_Adapter extends
            ArrayAdapter<HashMap<String, String>> {
        Activity context;
        private ArrayList<HashMap<String, String>> data;
        private ArrayList<HashMap<String, String>> mOriginalValues;

        public ReportesPedidos_Adapter(Context context, int textViewResourceId,ArrayList<HashMap<String, String>> Strings) {
            super(context, textViewResourceId, Strings);

        }

        private class ViewHolder {
            TextView nomcliente, tipopago, total, numoc, estado, moneda, tv_pedidoAnterior, labell, edtObservacion_pedido;
            ImageView foto;
            TextView tv_tipoRegistro, imgCampanaYellow;
        }

        ViewHolder viewHolder;

        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {

                convertView = inflater.inflate(R.layout.item_reporte_pedido,null);
                viewHolder = new ViewHolder();

                // cache the views
                viewHolder.nomcliente = (TextView) convertView.findViewById(R.id.rpt_pedido_tv_cliente);
                viewHolder.tipopago = (TextView) convertView.findViewById(R.id.rpt_pedido_tv_tipopago);
                viewHolder.total = (TextView) convertView.findViewById(R.id.rpt_pedido_tv_total);
                viewHolder.numoc = (TextView) convertView.findViewById(R.id.rpt_pedido_tv_oc_numero);
                viewHolder.estado = (TextView) convertView.findViewById(R.id.rpt_pedido_tv_estado);
                viewHolder.foto = (ImageView) convertView.findViewById(R.id.item_reporte_pedido_flecha);
                viewHolder.moneda = (TextView) convertView.findViewById(R.id.tv_monedaReporte);
                viewHolder.tv_tipoRegistro = (TextView) convertView.findViewById(R.id.tv_tipoRegistro);
                viewHolder.tv_pedidoAnterior = (TextView) convertView.findViewById(R.id.rpt_pedido_tv_pedidoAnterior);
                viewHolder.labell = (TextView) convertView.findViewById(R.id.labell);
                viewHolder.imgCampanaYellow = (TextView) convertView.findViewById(R.id.imgCampanaYellow);
                viewHolder.edtObservacion_pedido = (TextView) convertView.findViewById(R.id.edtObservacion_pedido);
                convertView.setTag(viewHolder);

            } else
                viewHolder = (ViewHolder) convertView.getTag();

            if (pedidos.get(position).get("flag").toString().equals("E")) {
                viewHolder.foto.setBackgroundColor(Color.rgb(46, 178, 0));
            } else if (pedidos.get(position).get("flag").toString().equals("I")) {
                viewHolder.foto.setBackgroundColor(Color.rgb(255, 255, 0));
            } else if (pedidos.get(position).get("flag").toString().equals("P")) {
                viewHolder.foto.setBackgroundColor(Color.rgb(255, 30, 30));
                viewHolder.estado.setTextColor(Color.rgb(255, 30, 30));
            } else if (pedidos.get(position).get("flag").toString().equals("T")) {
                viewHolder.foto.setBackgroundColor(Color.rgb(75, 0, 130));
                viewHolder.estado.setTextColor(Color.rgb(75, 0, 130));
            }

            String moneda = "" + pedidos.get(position).get("moneda").toString();
            if (moneda.equals(PedidosActivity.MONEDA_PEN)) {
                viewHolder.moneda.setText("S/.");
            } else {
                viewHolder.moneda.setText("$.");
            }

            String estado = pedidos.get(position).get("estado").toString();

            if (estado.equals("A")) {
                viewHolder.nomcliente.setTextColor(Color.parseColor("#FF0000"));
                viewHolder.moneda.setText("");
            } else {
                viewHolder.nomcliente.setTextColor(Color.parseColor("#040404"));
            }
            viewHolder.nomcliente.setText(pedidos.get(position)
                    .get("nombrecliente").toString());
            viewHolder.tipopago.setText(pedidos.get(position).get("tipopago")
                    .toString());
            viewHolder.total.setText(pedidos.get(position).get("total")
                    .toString());
            viewHolder.numoc.setText(pedidos.get(position).get("numoc").toString());
            viewHolder.estado.setText(pedidos.get(position).get("mensaje")
                    .toString());
            if (pedidos.get(position).get("tipoRegistro").toString().equals(PedidosActivity.TIPO_COTIZACION)) {
                viewHolder.tv_tipoRegistro.setText("C");
                viewHolder.tv_tipoRegistro.setBackgroundColor(getResources().getColor(R.color.indigo_500));

                if (pedidos.get(position).get("pedidoAnterior").toString().equals("")) {
                    viewHolder.labell.setVisibility(View.INVISIBLE);
                    viewHolder.tv_pedidoAnterior.setVisibility(View.INVISIBLE);
                }else{
                    viewHolder.tv_pedidoAnterior.setText(pedidos.get(position).get("pedidoAnterior").toString());
                    viewHolder.numoc.setTextColor(getResources().getColor(R.color.indigo_500));
                }

                convertView.setBackgroundResource(R.drawable.selector_reporte_cotizacion);
            }else if (pedidos.get(position).get("tipoRegistro").toString().equals(PedidosActivity.TIPO_DEVOLUCION)) {
                viewHolder.tv_tipoRegistro.setText("D");
                viewHolder.tv_tipoRegistro.setBackgroundColor(getResources().getColor(R.color.brown_500));
                viewHolder.labell.setVisibility(View.INVISIBLE);
                viewHolder.tv_pedidoAnterior.setVisibility(View.INVISIBLE);
                convertView.setBackgroundResource(R.drawable.selector_reporte_devolucion);
            }else{
                viewHolder.tv_tipoRegistro.setText("P");
                viewHolder.tv_tipoRegistro.setBackgroundColor(getResources().getColor(R.color.teal_500));
                viewHolder.labell.setVisibility(View.INVISIBLE);
                viewHolder.tv_pedidoAnterior.setVisibility(View.INVISIBLE);
                convertView.setBackgroundResource(R.drawable.selector_reporte_pedido);
            }
            boolean isRequired=obj_dbclasses.RequiereValidacionPorDescuento(pedidos.get(position).get("numoc").toString());
            viewHolder.imgCampanaYellow.setVisibility(isRequired?View.VISIBLE:View.GONE);
            if (VARIABLES.IsLatitudValido(pedidos.get(position).get("latitud"))){
                viewHolder.edtObservacion_pedido.setText("");
            }else{
                viewHolder.edtObservacion_pedido.setText("Pedido sin posición");
            }


            OnClickCustom(viewHolder.imgCampanaYellow);
            return convertView;
        }

    }
    private void OnClickCustom(View view){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(ReportesPedidosActivity.this,"Producto guardado con observación, pues el descuento supera a 3%", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });
    }

    public class no_ventaAdapter extends ArrayAdapter<DBMotivo_noventa> {

        Activity context;
        ArrayList<DBMotivo_noventa> datos;

        public no_ventaAdapter(Activity context,
                               ArrayList<DBMotivo_noventa> datos) {

            super(context, android.R.layout.simple_list_item_single_choice,
                    datos);
            this.context = context;
            this.datos = datos;
        }

        public View getView(int position, View convertview, ViewGroup parent) {

            View item = convertview;
            ViewHolder holder;

            if (item == null) {
                LayoutInflater inflater = context.getLayoutInflater();
                item = inflater.inflate(
                        android.R.layout.simple_list_item_single_choice, null);

                holder = new ViewHolder();

                holder.des_noventa = (TextView) item
                        .findViewById(android.R.id.text1);

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

    public void crearDialogo_noventa() {

        dialogo = new Dialog(this);
        numOc = obj_dbclasses.obtenerNumOC(codven);
        dialogo.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialogo.setContentView(R.layout.dialog_motivo_noventa);
        dialogo.setCancelable(false);

        final ListView lstNoventa_motivo = (ListView) dialogo
                .findViewById(R.id.dialog_motivo_noventa_lstNo_venta);
        Button btnAceptar = (Button) dialogo
                .findViewById(R.id.dialog_motivo_noventa_btnAceptar);
        Button btnCancelar = (Button) dialogo
                .findViewById(R.id.dialog_motivo_noventa_btnCancelar);
        lstNoventa_motivo.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        ArrayList<DBMotivo_noventa> motivos = new ArrayList<DBMotivo_noventa>();
        motivos = obj_dbclasses.obtenerMotivo_noventa();
        no_ventaAdapter adaptador = new no_ventaAdapter(this, motivos);
        lstNoventa_motivo.setAdapter(adaptador);

        lstNoventa_motivo
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> adapter, View arg1,
                                            int position, long id) {

                        selectedPosition1 = position;

                    }
                });

        btnAceptar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                item = (DBMotivo_noventa) lstNoventa_motivo.getAdapter()
                        .getItem(selectedPosition1);

                dialogo.dismiss();

                AlertDialog.Builder builder = new AlertDialog.Builder(
                        ReportesPedidosActivity.this);
                builder.setTitle("Importante");
                builder.setMessage("Se guardaran todos los datos");
                builder.setCancelable(false);
                builder.setIcon(R.drawable.ic_alert);
                builder.setPositiveButton("Enviar al servidor",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo1, int id) {

                                new asyncGuardarMotivo().execute("", "");
                            }

                        });
                builder.setNegativeButton("Guardar Localmente",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo1, int id) {

                                guardarCabeceraPedido(item.getCod_noventa());

                                pedidos.clear();
                                cargarPedidos("", "");
                                ((ReportesPedidos_Adapter) list.getAdapter())
                                        .notifyDataSetChanged();
                                dialogo.dismiss();
                            }
                        });
                builder.create().show();

            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialogo.dismiss();
            }
        });

        dialogo.show();

    }

    class asyncGuardarMotivo extends AsyncTask<String, String, String> {

        String user, pass;
        private ProgressDialog pDialog;

        protected void onPreExecute() {
            // para el progress dialog
            pDialog = new ProgressDialog(ReportesPedidosActivity.this);
            pDialog.setMessage("Guardando motivo de no venta....");
            pDialog.setIndeterminate(false);

            pDialog.show();
        }

        protected String doInBackground(String... params) {

            String valor = "ok";

            DBSync_soap_manager soap_manager = new DBSync_soap_manager(
                    getApplicationContext());

            guardarCabeceraPedido(item.getCod_noventa());

            if (connection.hasActiveInternetConnection(getApplicationContext())) {

                try {
                    soap_manager.actualizarObjPedido_directo(oc_numero);
                } catch (JsonParseException ex) {
                    // exception al parsear json
                    valor = "error_2";
                } catch (Exception ex) {
                    // cualquier otra exception al enviar los datos
                    valor = "error_3";
                }

            } else {
                // Sin conexion al servidor
                valor = "error_1";

            }

            pedidos.clear();
            cargarPedidos("", "");

            return valor;
        }

        protected void onPostExecute(String result) {

            pDialog.dismiss();// ocultamos progess dialog.
            Log.e("onPostExecute= DEPOSITOs", "" + result);

            String mensaje = "";

            if (result.equals("ok")) {
                mensaje = "Envio Correcto";
            } else if (result.equals("error_1")) {
                mensaje = "Sin conexion al Servidor";
            } else if (result.equals("error_2")) {
                mensaje = "Error en el envio";
            } else if (result.equals("error_3")) {
                mensaje = "No se pudo verificar";
            }

            Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_LONG)
                    .show();

            ((ReportesPedidos_Adapter) list.getAdapter())
                    .notifyDataSetChanged();
            dialogo.dismiss();

        }

    }

    public void guardarCabeceraPedido(int cod_noventa) {

        itemCabecera = new DBPedido_Cabecera();

        itemCabecera.setOc_numero(oc_numero);

        itemCabecera.setEstado("A");
        itemCabecera.setCodigo_familiar("0");
        itemCabecera.setCod_noventa(cod_noventa);
        itemCabecera.setDT_PEDI_FECHASERVIDOR("0");
        itemCabecera.setFlag("P");
        itemCabecera.setObserv("");
        itemCabecera.setCond_pago("0");

        obj_dbclasses.Actualizar_pedido_cabecera2(itemCabecera);

    }

    class async_getFlagPedido extends AsyncTask<String, String, String> {

        ProgressDialog pDialog;
        String tipo = "";

        @Override
        protected void onPreExecute() {
            // para el progress dialog

            pDialog = new ProgressDialog(ReportesPedidosActivity.this);
            pDialog.setMessage("Verificando...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            String ocnumero = params[0].toString();
            tipo = params[1].toString();

            String ret = "";

            DBSync_soap_manager sm = new DBSync_soap_manager(
                    ReportesPedidosActivity.this);

            if (connection.hasActiveInternetConnection(getApplicationContext())) {

                try {
                    ret = sm.peticion_modificar(ocnumero);
                } catch (Exception e) {
                    e.printStackTrace();
                    // Error en el envio
                    ret = "error_2";
                }

            } else {
                // Sin conexion al Servidor
                ret = "error_1";
            }

            return ret;
        }

        @Override
        protected void onPostExecute(String result) {

            pDialog.dismiss();// ocultamos progess dialog.

            if (result.equals("error_1")) {

                // Sin conexion al Servidor
                // No se pudo verificar asi que no se continua con la accion
                // seleccionada
                AlertDialog.Builder alerta = new AlertDialog.Builder(
                        ReportesPedidosActivity.this);
                alerta.setTitle("Sin conexion al servidor");
                alerta.setMessage("Por el momento no es posible completar la accion");
                alerta.setCancelable(false);
                alerta.setPositiveButton("OK", null);
                alerta.show();

            }

            else if (result.equals("error_2")) {

                // Error en el envio
                // No se pudo verificar asi que no se continua con la accion
                // seleccionada
                AlertDialog.Builder alerta = new AlertDialog.Builder(
                        ReportesPedidosActivity.this);
                alerta.setMessage("Por el momento no es posible completar la accion");
                alerta.setCancelable(false);
                alerta.setPositiveButton("OK", null);
                alerta.show();

            } else {

                if (tipo.equals("PEDIDO")) {

                    if (result.equals("T")) {
                        // el pedido ya ha sido transferido y no se puede
                        // modificar
                        AlertDialog.Builder alerta = new AlertDialog.Builder(
                                ReportesPedidosActivity.this);
                        alerta.setTitle("MODIFICAR");
                        alerta.setMessage("El pedido ya ha sido tranferido\nComuniquese con el administrador");
                        alerta.setCancelable(false);
                        alerta.setPositiveButton("OK", null);
                        alerta.show();
                    } else if (!result.equals("T")) {
                        // Se procede a modificar
                        updatePedido();
                    }

                } else if (tipo.equals("MOTIVO")) {

                    if (result.equals("T")) {
                        // el pedido ya ha sido transferido y no se puede
                        // modificar
                        AlertDialog.Builder alerta = new AlertDialog.Builder(
                                ReportesPedidosActivity.this);
                        alerta.setTitle("MODIFICAR");
                        alerta.setMessage("El pedido ya ha sido tranferido\nComuniquese con el administrador");
                        alerta.setCancelable(false);
                        alerta.setPositiveButton("OK", null);
                        alerta.show();
                    } else if (!result.equals("T")) {
                        // Se procede a modificar
                        crearDialogo_noventa();
                    }

                } else if (tipo.equals("PEDIDO-DETALLE")) {

                    String codcli = obj_dbclasses.obtenerCodigoCliente(nomcli);

                    int cod = obj_dbclasses.obtenerMotivoxCliente(codcli,
                            item_direccion);
                    Log.w("CODIGO_NOVENTA", "" + cod);

                    SharedPreferences prefs = getSharedPreferences(
                            "MisPreferencias", Context.MODE_PRIVATE);
                    String codven = prefs.getString("codven", "");



                    final Intent ipedido = new Intent(
                            getApplicationContext(), PedidosActivity.class);
                    ipedido.putExtra("codigoVendedor", codven);
                    ipedido.putExtra("nombreCliente", nomcli);
                    ipedido.putExtra("origen", "REPORTES-PEDIDO");
                    ipedido.putExtra("OC_NUMERO", oc_numero);
                    ipedido.putExtra("tipoRegistro", tipoRegistro);

                    startActivity(ipedido);
                    // obj_dbclasses.actualizarEstadoCabeceraPedido(oc_numero,
                    // "I");
                    ReportesPedidosActivity.this.finish();

                } else if (tipo.equals("PEDIDO-MODIFICAR")) {
                    String codcli = obj_dbclasses.obtenerCodigoCliente(nomcli);

                    int cod = obj_dbclasses.obtenerMotivoxCliente(codcli,
                            item_direccion);
                    Log.w("CODIGO_NOVENTA", "" + cod);

                    SharedPreferences prefs = getSharedPreferences(
                            "MisPreferencias", Context.MODE_PRIVATE);
                    String codven = prefs.getString("codven", "");

                    final Intent ipedido;
                    if (tipoRegistro.equals(PedidosActivity.TIPO_COTIZACION)) {
                        ipedido = new Intent(getApplicationContext(), PedidosActivity.class);
                    }else if (tipoRegistro.equals(PedidosActivity.TIPO_DEVOLUCION)) {
                        ipedido = new Intent(getApplicationContext(), CH_DevolucionesActivity.class);
                    }else{
                        ipedido = new Intent(getApplicationContext(), PedidosActivity.class);
                    }


                    ipedido.putExtra("codigoVendedor", codven);
                    ipedido.putExtra("nombreCliente", nomcli);
                    ipedido.putExtra("origen", "REPORTES-MODIFICAR");
                    ipedido.putExtra("OC_NUMERO", oc_numero);
                    ipedido.putExtra("tipoRegistro", tipoRegistro);

                    startActivity(ipedido);
                    // obj_dbclasses.actualizarEstadoCabeceraPedido(oc_numero,
                    // "I");
                    ReportesPedidosActivity.this.finish();

                }

            }

        }

    }

    class asyncVerificarPedidos extends AsyncTask<String, String, String> {

        String user, pass;
        ProgressDialog pDialog;

        protected void onPreExecute() {
            // para el progress dialog
            pDialog = new ProgressDialog(ReportesPedidosActivity.this);
            pDialog.setMessage("Verificando con Servidor...\n\n"
                    + GlobalVar.urlService);
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... params) {
            // obtnemos usr y pass
            String mensaje = "";

            DBSync_soap_manager soap_manager = new DBSync_soap_manager(
                    getApplicationContext());

            ConnectionDetector connection = new ConnectionDetector(
                    getApplicationContext());

            if (connection.hasActiveInternetConnection(getApplicationContext())) {

                try {
                    soap_manager.verificarPedidosEnviados();
                } catch (Exception e) {
                    mensaje = "Hay problemas para verificar...";
                }

                mensaje = "Verificacion completa";
            } else {
                mensaje = "Sin conexion al servidor";
            }

            pedidos.clear();
            cargarPedidos("", "");
            return mensaje;

        }

        /*
         * Una vez terminado doInBackground segun lo que halla ocurrido pasamos
         * a la sig. activity o mostramos error
         */

        /*
         * ROJO: pendiente AMARILLO: volver a sincronizar, diferente VERDE: Ya
         * ha sido enviado MORADO: Ya fue enviado y transferido
         */
        protected void onPostExecute(String mensaje) {

            Toast toast = Toast.makeText(getApplicationContext(), mensaje,
                    Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

            pDialog.dismiss();// ocultamos progess dialog.
            Log.e("onPostExecute= Enviado", "" + mensaje);

            ((ReportesPedidos_Adapter) list.getAdapter())
                    .notifyDataSetChanged();

            tv_totalPedidos.setText("Cantidad:" + contador);

        }

    }

    public void updatePedido() {

        String codcli = obj_dbclasses.obtenerCodigoCliente(nomcli);

        int cod = obj_dbclasses.obtenerMotivoxCliente(codcli, item_direccion);
        Log.w("CODIGO_NOVENTA", "" + cod);

        SharedPreferences prefs = getSharedPreferences("MisPreferencias",
                Context.MODE_PRIVATE);
        String codven = prefs.getString("codven", "");

        String vista = obj_dbclasses.obtenerVistaxOrdenCompra(oc_numero);
        Log.d("ReportesPedidosActivity ::updatePedido::", vista);
        if (vista.equals("Vista 1")) {

            final Intent ipedido = new Intent(getApplicationContext(),
                    PedidosActivityVentana2.class);

            ipedido.putExtra("codigoVendedor", codven);
            ipedido.putExtra("nombreCliente", nomcli);
            ipedido.putExtra("origen", "REPORTES");
            ipedido.putExtra("OC_NUMERO", oc_numero);

            startActivity(ipedido);
            // obj_dbclasses.actualizarEstadoCabeceraPedido(oc_numero, "I");
            ReportesPedidosActivity.this.finish();

        } else if (vista.equals("Vista 2")) {

            final Intent ipedido = new Intent(getApplicationContext(),
                    PedidosActivity.class);
            ipedido.putExtra("codigoVendedor", codven);
            ipedido.putExtra("nombreCliente", nomcli);
            ipedido.putExtra("origen", "REPORTES");
            ipedido.putExtra("OC_NUMERO", oc_numero);

            startActivity(ipedido);
            // obj_dbclasses.actualizarEstadoCabeceraPedido(oc_numero, "I");
            ReportesPedidosActivity.this.finish();

        } else {
            Toast.makeText(getApplicationContext(),
                    "Configure el tipo de visualizacion en Preferencias", Toast.LENGTH_SHORT)
                    .show();
        }

    }

    public void dialogoEliminarRegistro() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                ReportesPedidosActivity.this);
        alertDialog.setTitle("Eliminar");
        alertDialog.setMessage("Se eliminará permanentemente");
        alertDialog.setIcon(R.drawable.ic_eliminar);
        alertDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // obj_dbclasses.deletePedidoCabeceraxOc(oc_numero);
                        obj_dbclasses.updateFlagPedidoCabecera(oc_numero, "A");
                        asyncEliminarPedidos con = new asyncEliminarPedidos();
                        con.execute("");

                    }
                });
        alertDialog.setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {

                    }
                });
        alertDialog.show();
    }

    class asyncEliminarPedidos extends AsyncTask<String, String, String> {

        String user, pass;
        ProgressDialog pDialog;

        protected void onPreExecute() {
            // para el progress dialog
            pDialog = new ProgressDialog(ReportesPedidosActivity.this);
            pDialog.setMessage("ELiminando....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... params) {
            // obtnemos usr y pass
            String valor = "";
            DBSync_soap_manager soap_manager = new DBSync_soap_manager(
                    getApplicationContext());

            ConnectionDetector connection = new ConnectionDetector(
                    getApplicationContext());
            if (connection.hasActiveInternetConnection(getApplicationContext()) == true) {

                try {
                    String calor = soap_manager
                            .eliminarPedidoCabeceraxOc(oc_numero);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                valor = "E";
            } else {
                valor = "X";
            }

            return valor;

        }

        /*
         * Una vez terminado doInBackground segun lo que halla ocurrido pasamos
         * a la sig. activity o mostramos error
         */
        protected void onPostExecute(String result) {

            pedidos.clear();
            cargarPedidos("", "");
            cargarListView();
            pDialog.dismiss();// ocultamos progess dialog.
            Log.e("onPostExecute= Enviado", "" + result);
            ((ReportesPedidos_Adapter) list.getAdapter())
                    .notifyDataSetChanged();
            tv_totalPedidos.setText("Cantidad:" + contador);

        }

    }

    Dialog bdialog;

    EditText edt_buscar;
    Button btn_buscar, btn_cancelar;
    String tipofiltro = "cliente";

    public void poputMenu(View view){
        final PopupMenu popupMenu = new PopupMenu(ReportesPedidosActivity.this, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_reportespedidos, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_reportespedidos:

                        GlobalFunctions.backupdDatabase();
                        new asyncEnviarPendientes().execute("");
                        return true;
                    case R.id.menu_reportespedidos_verificar:
                        new asyncVerificarPedidos().execute("");
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.show();
    }

    protected void showCustomDialog() {

        bdialog = new Dialog(ReportesPedidosActivity.this,
                android.R.style.Theme_Translucent);
        bdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        bdialog.setCancelable(true);
        bdialog.setContentView(R.layout.dialog_buscar);

        RadioGroup rb_filtro;

        edt_buscar = (EditText) bdialog.findViewById(R.id.reportes_edt_search);
        btn_buscar = (Button) bdialog.findViewById(R.id.reportes_btn_buscar);
        btn_cancelar = (Button) bdialog
                .findViewById(R.id.reportes_btn_cancelar);
        rb_filtro = (RadioGroup) bdialog
                .findViewById(R.id.reportes_rg_tipoFiltro);
        btn_buscar.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new asyncBuscar().execute(tipofiltro, edt_buscar.getText()
                        .toString());
            }
        });
        btn_cancelar.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                bdialog.dismiss();
            }
        });

        bdialog.show();

        rb_filtro.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                // TODO Auto-generated method stub

                switch (arg1) {
                    case R.id.reportes_rb_Cliente:
                        tipofiltro = "cliente";
                        edt_buscar.setText("");
                        edt_buscar.setInputType(InputType.TYPE_CLASS_TEXT);
                        break;
                    case R.id.reportes_rb_Documento:
                        edt_buscar.setText("");
                        tipofiltro = "documento";
                        edt_buscar.setInputType(InputType.TYPE_CLASS_NUMBER);

                    default:
                        break;
                }
            }
        });


    }

    class asyncBuscar extends AsyncTask<String, String, String> {

        String user, pass;
        ProgressDialog pDialog;
        double cantidadDatos;

        protected void onPreExecute() {
            // para el progress dialog
            pDialog = new ProgressDialog(ReportesPedidosActivity.this);
            pDialog.setMessage("Buscando....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... params) {
            // obtnemos usr y pass
            String valor = params[0].toString();
            String valor2 = params[1].toString();

            try {
                pedidos.clear();
                cargarPedidos(valor, valor2);
                cantidadDatos = CalcularPaqueteDatos();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return "E";
        }

        protected void onPostExecute(String result) {
            ((ReportesPedidos_Adapter) list.getAdapter()).notifyDataSetChanged();
            cargarListView();
            pDialog.dismiss();// ocultamos progess dialog.
            Log.e("onPostExecute= Enviado", "" + result);
            tv_paqueteDatos.setText(cantidadDatos + " MB");

        }



    }


    class asyncVerificacion_individual extends
            AsyncTask<String, String, Integer> {

        ProgressDialog pDialog;

        protected void onPreExecute() {
            // para el progress dialog
            pDialog = new ProgressDialog(ReportesPedidosActivity.this);
            pDialog.setMessage("Verificando con Servidor...\n\n"
                    + GlobalVar.urlService);
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected Integer doInBackground(String... params) {

            String oc_numero = params[0];
            int result = -1;

            DBSync_soap_manager soap_manager = new DBSync_soap_manager(
                    getApplicationContext());

            ConnectionDetector connection = new ConnectionDetector(
                    getApplicationContext());

            if (connection.hasActiveInternetConnection(getApplicationContext())) {

                result = soap_manager
                        .verificarPedidoEnviado_xocnumero(oc_numero);

            } else {
                result = -1;
            }

            return result;

        }

        protected void onPostExecute(Integer result) {

            String mensaje = "";
            String pregunta = "\ndesea forzar el envio?";

            pDialog.dismiss();
            Log.i("asyncVerificacion_individual onpostexecute", "" + result);

            if (result == 0) {
                Toast.makeText(getApplicationContext(),
                        "No Existe OC en el servidor", Toast.LENGTH_LONG)
                        .show();
                mensaje = "No Existe OC en el servidor";
            } else if (result == 1) {
                Toast.makeText(getApplicationContext(),
                        "Si existe pero  no coinciden atributos",
                        Toast.LENGTH_LONG).show();
                mensaje = "Si existe pero  no coinciden atributos";
            } else if (result == 2) {
                Toast.makeText(getApplicationContext(),
                        "Datos Correctos en el servidor", Toast.LENGTH_LONG)
                        .show();
                mensaje = "Datos Correctos en el servidor";
            } else if (result == 3) {
                Toast.makeText(getApplicationContext(),
                        "Error al enviar datos", Toast.LENGTH_LONG).show();
                mensaje = "Error al enviar datos";
            } else if (result == -1) {
                Toast.makeText(getApplicationContext(),
                        "No hubo comunicacion con el servidor",
                        Toast.LENGTH_LONG).show();
                mensaje = "No hubo comunicacion con el servidor";
            }

            AlertDialog.Builder aviso = new AlertDialog.Builder(
                    ReportesPedidosActivity.this);
            aviso.setMessage(mensaje + pregunta);
            aviso.setCancelable(false);
            aviso.setPositiveButton("Si",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            pDialog.dismiss();

                            //
                            AlertDialog.Builder ped_password = new AlertDialog.Builder(
                                    ReportesPedidosActivity.this);
                            ped_password.setCancelable(false);
                            ped_password.setMessage("Ingrese su contraseña");

                            final EditText txt_password = new EditText(
                                    ReportesPedidosActivity.this);
                            txt_password.setInputType(InputType.TYPE_CLASS_TEXT
                                    | InputType.TYPE_TEXT_VARIATION_PASSWORD);

                            ped_password.setView(txt_password);
                            ped_password.setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                            // verificar la contraseña
                                            String password = txt_password
                                                    .getText().toString();
                                            Verificacion_forzar_envio verif = new Verificacion_forzar_envio();
                                            verif.execute(password);
                                        }
                                    });

                            ped_password.setNegativeButton("Cancelar",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {

                                        }
                                    });

                            ped_password.show();
                            //

                        }
                    });

            aviso.setNegativeButton("Cancelar",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

            aviso.show();

        }

    }

    class asyncEnvioForzado extends AsyncTask<String, String, String> {

        ProgressDialog pDialog;

        protected void onPreExecute() {
            // para el progress dialog
            pDialog = new ProgressDialog(ReportesPedidosActivity.this);
            pDialog.setMessage("Enviando...\n\n" + GlobalVar.urlService);
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... params) {

            String oc_numero = params[0];
            String ret = "";

            DBSync_soap_manager soap_manager = new DBSync_soap_manager(
                    getApplicationContext());

            if (connection.hasActiveInternetConnection(getApplicationContext())) {

                DBPedido_Cabecera ped_cab = obj_dbclasses
                        .getPedido_cabecera(oc_numero);
                ArrayList<DBPedido_Detalle> lista_ped_detalle = obj_dbclasses
                        .getPedido_detalles(oc_numero);

                try {

                    soap_manager.actualizarPedido_cabecera2(ped_cab);

                    for (DBPedido_Detalle obj : lista_ped_detalle) {
                        soap_manager.actualizarPedido_detalle2(obj);
                    }

                    ret = "ok";

                } catch (Exception ex) {
                    ret = "error_2";
                }

            } else {
                ret = "error_1";
            }

            pedidos.clear();
            cargarPedidos("", "");

            return ret;

        }

        protected void onPostExecute(String result) {

            pDialog.dismiss();
            String mensaje = "";

            if (result.equals("ok")) {
                mensaje = "Enviado";
            } else if (result.equals("error_1")) {
                mensaje = "Sin conexion al Servidor";
            } else {
                mensaje = "Envio con errores";
            }

            AlertDialog.Builder informe = new AlertDialog.Builder(
                    ReportesPedidosActivity.this);
            informe.setMessage(mensaje);
            informe.setCancelable(false);
            informe.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            dialog.dismiss();
                        }
                    });

            informe.show();

        }

    }

    class Verificacion_forzar_envio extends AsyncTask<String, String, Boolean> {

        ProgressDialog pDialog;

        protected void onPreExecute() {
            // para el progress dialog
            pDialog = new ProgressDialog(ReportesPedidosActivity.this);
            pDialog.setMessage("Validando contraseña...");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected Boolean doInBackground(String... params) {

            String password = params[0];

            boolean resp = obj_dbclasses.verificar_contrasenia_forzar_envio(password.trim());

            return resp;

        }

        protected void onPostExecute(Boolean result) {

            pDialog.dismiss();

            if (result) {
                asyncEnvioForzado task = new asyncEnvioForzado();
                task.execute(oc_numero);
            } else {
                Toast.makeText(getApplicationContext(),
                        "Contraseña incorrecta", Toast.LENGTH_LONG).show();
            }

        }

    }



}
