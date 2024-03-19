package com.example.sm_tubo_plast.genesys.fuerza_ventas.Reportes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.crashlitics.domain.CrashlyticsUseCases;
import com.example.sm_tubo_plast.genesys.BEAN.DataCabeceraPDF;
import com.example.sm_tubo_plast.genesys.BEAN.ReportePedidoCabeceraBEAN;
import com.example.sm_tubo_plast.genesys.BEAN.ReportePedidoDetallePDF;
import com.example.sm_tubo_plast.genesys.BEAN_API.CotizacionCabeceraApi;
import com.example.sm_tubo_plast.genesys.BEAN_API.CotizacionDetalleApi;
import com.example.sm_tubo_plast.genesys.CreatePDF.PDF;

import com.example.sm_tubo_plast.genesys.DAO.DAO_Pedido;
import com.example.sm_tubo_plast.genesys.DAO.DAO_RegistroBonificaciones;
import com.example.sm_tubo_plast.genesys.DAO.DAO_RegistrosGeneralesMovil;
import com.example.sm_tubo_plast.genesys.DAO.DAO_ReportePedido;
import com.example.sm_tubo_plast.genesys.DAO.DAO_San_Visitas;
import com.example.sm_tubo_plast.genesys.adapters.ReportesPedidosCabeceraRecyclerView;
import com.example.sm_tubo_plast.genesys.datatypes.DBMotivo_noventa;
import com.example.sm_tubo_plast.genesys.datatypes.DBPedido_Cabecera;
import com.example.sm_tubo_plast.genesys.datatypes.DBPedido_Detalle;
import com.example.sm_tubo_plast.genesys.datatypes.DBSync_soap_manager;
import com.example.sm_tubo_plast.genesys.datatypes.DB_ObjPedido;
import com.example.sm_tubo_plast.genesys.datatypes.DB_RegistroBonificaciones;
import com.example.sm_tubo_plast.genesys.datatypes.DBclasses;
import com.example.sm_tubo_plast.genesys.domain.IReportePedidoCabecera;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.CH_DevolucionesActivity;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.ClientesActivity;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.Dialog.DialogFragment_enviarCotizacion;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.GestionVisita3Activity;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.PedidosActivity;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.PedidosActivityVentana2;
import com.example.sm_tubo_plast.genesys.service.ConnectionDetector;
import com.example.sm_tubo_plast.genesys.service.WS_Cotizaciones;
import com.example.sm_tubo_plast.genesys.session.SessionManager;
import com.example.sm_tubo_plast.genesys.util.CustomDateTimePicker;
import com.example.sm_tubo_plast.genesys.util.EditTex.ACG_EditText;
import com.example.sm_tubo_plast.genesys.util.GlobalFunctions;
import com.example.sm_tubo_plast.genesys.util.GlobalVar;
import com.example.sm_tubo_plast.genesys.util.SnackBar.UtilViewSnackBar;
import com.example.sm_tubo_plast.genesys.util.UtilView;
import com.example.sm_tubo_plast.genesys.util.UtilViewMensaje;
import com.example.sm_tubo_plast.genesys.util.VARIABLES;
import com.example.sm_tubo_plast.genesys.util.recyclerView.RecyclerViewCustom;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import me.piruin.quickaction.ActionItem;
import me.piruin.quickaction.QuickAction;

@SuppressLint("LongLogTag")
public class ReportesPedidosCotizacionYVisitaActivity extends FragmentActivity {

    public static final String TAG  = "ReportesPedidosActivity";
    public static final String KEY_TOTAL = "total";
    public static final String KEY_NOMCLIENTE = "nombrecliente";
    public static final String KEY_CODCLI = "codcli";
    public static final String KEY_TIPOPAGO = "tipopago";
    public static final String KEY_NUMOC = "numoc";
    public static final String KEY_ESTADO = "estado";
    public static final String KEY_MENSAJE = "mensaje";
    public static final String KEY_TOTAL_PERCEPCION = "totalPercepcion";
    public static final String KEY_MONEDA = "moneda";
    public static final String KEY_NOVENTA = "KEY_NOVENTA";

    public static final String DATOS_PREVENTA = "PREVENTA";
    public static final String DATOS_GESTION_VISITAS = "GESTION_VISITAS";

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
    DAO_ReportePedido dao_reportePedido;
    DAO_RegistrosGeneralesMovil dao_registrosGeneralesMovil;

    ArrayList<IReportePedidoCabecera> IreportecabeceraLista=new ArrayList<>();
    WS_Cotizaciones ws_cotizaciones=null;
    ArrayList<DB_RegistroBonificaciones> registroBonificaciones;
    Gson gson = new Gson();
    DAO_RegistroBonificaciones DAORegistroBonificaciones;

    RecyclerViewCustom myRecyclerViewPedidoCabcera;
    ReportesPedidosCabeceraRecyclerView adapterRecyclerView;
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
    private static final int ID_GENERAR_PDF = 7;
    private static final int ID_CAMBIAR_MONEDA = 8;

    DecimalFormat formateador;
    ArrayList<HashMap<String, String>> searchResults;
    public String oc_numero = "";
    public String cond_pago = "";
    //ReportesPedidos_Adapter adapter;
    private int mSelectedRow = 0;
    int selectedPosition1 = 0;
    public double montoTotal_soles;
    public double montoTotal_dolares;
    public double totalPrecioKiloDolar;
    DBPedido_Cabecera itemCabecera;
    ArrayList<DBPedido_Cabecera> lista_pedidos;
    TextView tv_montoTotal_soles, btn_peso, tv_totalPedidos, tv_precioKiloDolar,
            tv_montoTotal_dolares;
    EditText edtBuscarPedidos;
    ImageView imgBuscarDatos;
    ProgressDialog pDialog;

    LinearLayout layoutBuscarPedidoCoti;
    LinearLayout layoutFooter;
    RecyclerView recyclerViewDatosOnLine;
    ImageView ib_seleccionar_cliente;
    String codcliPrincipal="";
    TextView txtNombresCliente, txtFechaDesde, txtFechaHasta;
    CheckBox chkBuscarEnLinea;

    ConnectionDetector connection;
    int contador = 0;
    public String globalFlag = "";
    String fecha2 = "";

    String TIPO_VISTA = "";

    int item_direccion;
    String tipoRegistro;
    ConnectionDetector cd;

    QuickAction mQuickAction ;
    QuickAction mQuickActionCotizacion;
    QuickAction mQuickActionCotizacionOnLine;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            TIPO_VISTA = bundle.getString("TIPO_VISTA", ""+ DATOS_GESTION_VISITAS);
        }
        if (TIPO_VISTA.equals(DATOS_PREVENTA)){
            setContentView(R.layout.activity_reportes_pedidos_y_cotizacion);
        }else{
            setContentView(R.layout.activity_reportes_visitas);
        }

        DAORegistroBonificaciones = new DAO_RegistroBonificaciones(  getApplicationContext());
        obj_dbclasses = new DBclasses(getApplicationContext());
        dao_registrosGeneralesMovil = new DAO_RegistrosGeneralesMovil(getApplicationContext());
        cd = new ConnectionDetector(ReportesPedidosCotizacionYVisitaActivity.this);
        tv_montoTotal_soles = (TextView) findViewById(R.id.rpt_pedidos_txtTotal);
        btn_peso = (TextView) findViewById(R.id.rpt_pedidos_txtTotal_peso);
        tv_totalPedidos = (TextView) findViewById(R.id.rpt_pedidos_txtTotal_pedidos);

        lista_pedidos = new ArrayList<DBPedido_Cabecera>();
        tv_precioKiloDolar = (TextView) findViewById(R.id.tv_precioKiloDolar);
        tv_montoTotal_dolares = (TextView) findViewById(R.id.rpt_pedidos_tvTotalDolares);
        edtBuscarPedidos = (EditText) findViewById(R.id.edtBuscarPedidos);
        imgBuscarDatos =  findViewById(R.id.imgBuscarDatos);
        layoutFooter =  findViewById(R.id.layoutFooter);

        if (TIPO_VISTA.equals(DATOS_PREVENTA)){
            chkBuscarEnLinea =  findViewById(R.id.chkBuscarEnLinea);
            layoutBuscarPedidoCoti =  findViewById(R.id.layoutBuscarPedidoCoti);
            ib_seleccionar_cliente =  findViewById(R.id.ib_seleccionar_cliente);
            txtNombresCliente =  findViewById(R.id.txtNombresCliente);
            txtFechaDesde =  findViewById(R.id.txtFechaDesde);
            txtFechaHasta =  findViewById(R.id.txtFechaHasta);
            recyclerViewDatosOnLine =  findViewById(R.id.recyclerViewDatosOnLine);

            recyclerViewDatosOnLine.setLayoutManager(new LinearLayoutManager(this));
            layoutBuscarPedidoCoti.setVisibility(View.GONE);
        }

        fecha2 = obj_dbclasses.getFecha2();

        Log.d("ALERT - REPORTE - fecha:", fecha2);

        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        codven = new SessionManager(this).getCodigoVendedor();

        Log.d("ALERT - REPORTE - cod vend:", codven);

        myRecyclerViewPedidoCabcera = (RecyclerViewCustom) findViewById(R.id.myRecyclerViewPedidoCabcera);

        formateador = GlobalFunctions.formateador();

        // cargarPedidos("","");


        tv_totalPedidos.setText("Cantidad:" + contador);

        connection = new ConnectionDetector(getApplicationContext());



        ActionItem modificarItem = new ActionItem(ID_MODIFICAR, "Modificar",R.drawable.icon_edit_file_24dp);
        ActionItem cambiarMoneda = new ActionItem(ID_CAMBIAR_MONEDA, "Cambiar Moneda",R.drawable.icon_edit_file_24dp);
        ActionItem acceptItem = new ActionItem(ID_DETALLE, "Ver Detalle",R.drawable.icon_show_file_24dp);
        ActionItem deleteItem = new ActionItem(ID_DELETE, "Eliminar",R.drawable.icon_delete_file_24dp);
        ActionItem anularItem = new ActionItem(ID_ANULAR, "Anular",R.drawable.icon_cancel_file_24dp);
        ActionItem forzarEnvio = new ActionItem(ID_FORZAR, "Forzar Envio",R.drawable.ic_devolucion);
        //ActionItem enviarCorreo = new ActionItem(ID_ENVIAR_CORREO, "Enviar por correo",R.drawable.icon_message_read_24dp);
        ActionItem generarPedido = new ActionItem(ID_GENERAR_PEDIDO, "Generar pedido",R.drawable.icon_inspection);
        ActionItem generarPdf = new ActionItem(ID_GENERAR_PDF, "Generar PDF",R.drawable.ic_pdf_24);

        mQuickAction = new QuickAction(this);
        mQuickActionCotizacion = new QuickAction(this);
        mQuickActionCotizacionOnLine = new QuickAction(this);


        mQuickAction.addActionItem(modificarItem);
        mQuickAction.addActionItem(cambiarMoneda);
        mQuickAction.addActionItem(acceptItem);
        mQuickAction.addActionItem(anularItem);
        //mQuickAction.addActionItem(forzarEnvio);
        if (TIPO_VISTA.equals(DATOS_PREVENTA)) {
            mQuickAction.addActionItem(generarPdf);
        }

        mQuickActionCotizacion.addActionItem(modificarItem);
        mQuickActionCotizacion.addActionItem(cambiarMoneda);
        mQuickActionCotizacion.addActionItem(acceptItem);
        mQuickActionCotizacion.addActionItem(anularItem);
        //mQuickActionCotizacion.addActionItem(forzarEnvio);
        //mQuickActionCotizacion.addActionItem(enviarCorreo);
        mQuickActionCotizacion.addActionItem(generarPedido);
        if (TIPO_VISTA.equals(DATOS_PREVENTA)) {
            mQuickActionCotizacion.addActionItem(generarPdf);
        }

        mQuickActionCotizacionOnLine.addActionItem(generarPdf);


        // setup the action item click listener
        mQuickAction  .setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {

                    @Override
                    public void onItemClick(ActionItem item) {
                        int actionId=item.getActionId();
                        ReportePedidoCabeceraBEAN itemData= getInstancePedidoCabceraLocal();
                        if(itemData==null) {
                            return;
                        }

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


                            int cod_noventa=Integer.parseInt(""+  itemData.getMotivo_noventa());
                            if (cod_noventa>0 && GlobalVar.CODIGO_VISITA_CLIENTE==cod_noventa){
                                UtilViewMensaje.MENSAJE_simple(ReportesPedidosCotizacionYVisitaActivity.this, "Modificar?",
                                        "Para modificar, dirijase desde el menu principal, AGENDA-> seleccione la actividad" );
                                return;
                            }

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
                        else if(actionId==ID_CAMBIAR_MONEDA){
                            requestCambiarMoneda(itemData.getTipoRegistro());
                        }
                        else if (actionId == ID_FORZAR) {

                            asyncVerificacion_individual verif = new asyncVerificacion_individual();
                            verif.execute(oc_numero);

                        }

                        else if (actionId == ID_DETALLE) {
                            int cod_noventa=Integer.parseInt(""+  itemData.getMotivo_noventa());
                            if (cod_noventa>0 && GlobalVar.CODIGO_VISITA_CLIENTE==cod_noventa){
                                if (DAO_San_Visitas.GetVisitasByOc_numero(obj_dbclasses, oc_numero, GestionVisita3Activity.VISITA_PLANIFICADA).size()>0) {
                                    Intent intent=new Intent(ReportesPedidosCotizacionYVisitaActivity.this, GestionVisita3Activity.class);
                                    intent.putExtra("ID_RRHH", "-1");
                                    intent.putExtra("CODIGO_CRM", "");
                                    intent.putExtra("NOMBRE_INSTI", "");
                                    intent.putExtra("OC_NUMERO", ""+oc_numero);
                                    intent.putExtra("COD_VEND", codven);
                                    intent.putExtra("isPLANIFICADA", false);//FALSE PARA NO MODIFICAR
                                    intent.putExtra("TIPO_GESTION", "nada");//FALSE PARA NO MODIFICAR
                                    intent.putExtra("ORIGEN", TAG);
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(ReportesPedidosCotizacionYVisitaActivity.this, "Ninguna acción", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                if (obj_dbclasses.getTipoRegistro(oc_numero).equals("DEVOLUCION")) {
                                    String codven = new SessionManager(ReportesPedidosCotizacionYVisitaActivity.this).getCodigoVendedor();
    
                                    final Intent ipedido = new Intent(getApplicationContext(), CH_DevolucionesActivity.class);
                                    ipedido.putExtra("codigoVendedor", codven);
                                    ipedido.putExtra("nombreCliente", nomcli);
                                    ipedido.putExtra("origen", "REPORTES-PEDIDO");
                                    ipedido.putExtra("OC_NUMERO", oc_numero);
                                    startActivity(ipedido);
    
                                } else {
                                    verPedidoDetalle(itemData);
                                }
                            }




                        } else if(actionId == ID_ENVIAR_CORREO){
                            if (obj_dbclasses.getFlagPedido(oc_numero).equals("E")) {

                            }else{
                                Toast.makeText(getApplicationContext(), "Antes debe enviar el pedido", Toast.LENGTH_SHORT).show();
                            }

                            DialogFragment_enviarCotizacion dialog_preferencias = new DialogFragment_enviarCotizacion(getParent(), obj_dbclasses, oc_numero,codven);
                            dialog_preferencias.show(getSupportFragmentManager(), "dialogPreferencias");
                            dialog_preferencias.setCancelable(false);

                        }else if(actionId == ID_GENERAR_PDF){
                            GenerarPdfDocument(oc_numero);
                        }
                    }
                });



        mQuickActionCotizacion
                .setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
                    @Override
                    public void onItemClick(ActionItem item) {
                        int actionId=item.getActionId();

                        ReportePedidoCabeceraBEAN itemData= getInstancePedidoCabceraLocal();
                        if(itemData==null) {
                            return;
                        }

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
                                String codven = new SessionManager(ReportesPedidosCotizacionYVisitaActivity.this).getCodigoVendedor();

                                final Intent ipedido = new Intent(getApplicationContext(), CH_DevolucionesActivity.class);
                                ipedido.putExtra("codigoVendedor", codven);
                                ipedido.putExtra("nombreCliente", nomcli);
                                ipedido.putExtra("origen", "REPORTES-PEDIDO");
                                ipedido.putExtra("OC_NUMERO", oc_numero);
                                startActivity(ipedido);

                            } else {
                                verPedidoDetalle(itemData);
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
                            String _tipoRegistro = PedidosActivity.TIPO_PEDIDO;
                            if (clonarPedido(false,_tipoRegistro)!=null) {
                                new asyncBuscar().execute("", "");
                            }
                        }
                        else if(actionId==ID_CAMBIAR_MONEDA){
                            requestCambiarMoneda(itemData.getTipoRegistro());
                        }else if(actionId == ID_GENERAR_PDF){
                            GenerarPdfDocument(oc_numero);
                        }
                    }
                });


        mQuickActionCotizacionOnLine
                .setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
                    @Override
                    public void onItemClick(ActionItem item) {
                        int actionId=item.getActionId();
                        if(actionId == ID_GENERAR_PDF){

                            ws_cotizaciones.getDataDetalles(oc_numero, (success, dataDetalle) ->{
                                if(!success){
                                    UtilView.showCustomToast(
                                            ReportesPedidosCotizacionYVisitaActivity.this,
                                            "Error, al obtener los detalles de la cotización. Vuele a intentarlo",
                                            UtilView.TOAST_ERROR);
                                    return;
                                }
                                if(dataDetalle.size() > 0){
                                    //metemos los mappers, para cabecera y detalle
                                    CotizacionCabeceraApi cotizacionCabeceraApi =(CotizacionCabeceraApi) IreportecabeceraLista.get(mSelectedRow);
                                    DataCabeceraPDF dataCabeceraPDF=cotizacionCabeceraApi.dataApiToObjetDataPDF();
                                    ArrayList<ReportePedidoDetallePDF> listaDetPDF=new ArrayList<>();
                                    for (CotizacionDetalleApi cotizacionDetalleApi : dataDetalle) {
                                        listaDetPDF.add(cotizacionDetalleApi.dataApiToObjetDataDetallePDF());
                                    }

                                    GenerarPdf(dataCabeceraPDF, listaDetPDF);
                                }else{
                                    UtilView.showCustomToast(
                                            ReportesPedidosCotizacionYVisitaActivity.this,
                                            "No se puede generar el PDF, no se encontraron detalles",
                                            UtilView.TOAST_ERROR);
                                    Toast.makeText(getApplicationContext(), "No se puede generar el PDF, no se encontraron detalles", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }
        });

        configEventsCotizacionesYPedido();

        imgBuscarDatos.setOnClickListener(v -> {
            if (chkBuscarEnLinea!=null) {
                if (chkBuscarEnLinea.isChecked()) {
                    ws_cotizaciones=null;
                    buscarCotizacionesOnline();
                }else new asyncBuscar().execute("", "");
            }else new asyncBuscar().execute("", "");

        });
        imgBuscarDatos.performClick();

    }
    private ReportePedidoCabeceraBEAN getInstancePedidoCabceraLocal(){
        if(!(IreportecabeceraLista.get(mSelectedRow) instanceof ReportePedidoCabeceraBEAN))
        {
            UtilView.showCustomToast(ReportesPedidosCotizacionYVisitaActivity.this,
                    "No se encontro la instancia del pedido",UtilView.TOAST_ERROR);
            return null;
        }
        return (ReportePedidoCabeceraBEAN)IreportecabeceraLista.get(mSelectedRow);
    }
    private void  requestCambiarMoneda(String tipo_registro){
        new AlertDialog.Builder(ReportesPedidosCotizacionYVisitaActivity.this)
                .setTitle("Cambio de moneda")
                .setMessage("Al cambiar la moneda se generarà otro "+tipo_registro+"  ¿Desea cambiar la moneda del pedido?")
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Aceptar", (dialog, which) -> {
                    async_getFlagPedido con = new async_getFlagPedido();
                    con.execute(oc_numero, "CAMBIAR-MONEDA");
                })
                .show().create();
    }
    private void onCLikItemListData(int position, View view){

        mSelectedRow = position; // set the selected row
        codcli="";
        String numoc = "";
        String nomc ="";
        String tipo = "";
        IReportePedidoCabecera  IreporteCabecera = IreportecabeceraLista.get(position);
        if(IreporteCabecera instanceof  ReportePedidoCabeceraBEAN){
            ReportePedidoCabeceraBEAN reportePedidoCabeceraBEAN = (ReportePedidoCabeceraBEAN) IreporteCabecera;
            codcli = reportePedidoCabeceraBEAN.getCodcli();
            numoc = reportePedidoCabeceraBEAN.getNumoc();
            nomc = reportePedidoCabeceraBEAN.getNomcli();
            tipo= reportePedidoCabeceraBEAN.getTipoRegistro();


            if (obj_dbclasses.getClientexCodigo(codcli)==null) {
                UtilView.MENSAJE_simple(ReportesPedidosCotizacionYVisitaActivity.this, "Error", "Cliente no encontrado");
                return;
            }
            tipoRegistro = tipo;
            if (tipoRegistro.equals(PedidosActivity.TIPO_COTIZACION)) {
                mQuickActionCotizacion.show(view);
            }
             else if (tipoRegistro.equals(PedidosActivity.TIPO_DEVOLUCION)){
                tipoRegistro = PedidosActivity.TIPO_DEVOLUCION;
                mQuickAction.show(view);
            }else{
                tipoRegistro = PedidosActivity.TIPO_PEDIDO;
                mQuickAction.show(view);
            }

            oc_numero = numoc;
            Log.w("OC_NUMERO", oc_numero);
            nomcli = nomc;
            item_direccion = obj_dbclasses.obtenerSitioXocnumero(oc_numero);

        }
        else if(IreporteCabecera instanceof  CotizacionCabeceraApi){
            CotizacionCabeceraApi cotizacionCabeceraApi = (CotizacionCabeceraApi) IreporteCabecera;
            codcli = cotizacionCabeceraApi.getCodcli();
            numoc = cotizacionCabeceraApi.getCodigo_pedido();
            nomc = cotizacionCabeceraApi.getNomcli();
            tipo= cotizacionCabeceraApi.getTipotipo_registro();

            oc_numero = numoc;
            Log.w("OC_NUMERO", oc_numero);
            nomcli = nomc;
            mQuickActionCotizacionOnLine.show(view);
        }



    }
    private void configEventsCotizacionesYPedido(){

        if (!TIPO_VISTA.equals(DATOS_PREVENTA)){
            return;
        }
        chkBuscarEnLinea.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                layoutBuscarPedidoCoti.setVisibility(View.VISIBLE);
                layoutFooter.setVisibility(View.GONE);
                edtBuscarPedidos.setText("");
                edtBuscarPedidos.setHint("COW-000000NNNCP");
            }else{
                edtBuscarPedidos.setText("");
                edtBuscarPedidos.setHint("Buscar...");
                layoutBuscarPedidoCoti.setVisibility(View.GONE);
                layoutFooter.setVisibility(View.VISIBLE);
            }
            IreportecabeceraLista.clear();
            if (adapterRecyclerView!=null) {
                adapterRecyclerView.notifyDataSetChanged();
            }
            imgBuscarDatos.performClick();//buscar datos

        });
        new ACG_EditText(this, edtBuscarPedidos).OnListen(texto->{
            if (texto.length()>0){
                layoutBuscarPedidoCoti.setAlpha(0.3f);
            }else layoutBuscarPedidoCoti.setAlpha(1f);
        });
        if (ib_seleccionar_cliente!=null) {
            ib_seleccionar_cliente.setOnClickListener(v -> {
                Intent intent=new Intent(this, ClientesActivity.class);
                intent.putExtra("codven", codven);
                intent.putExtra("ORIGEN", TAG);
                intent.putExtra("REQUEST_TYPPE", ClientesActivity.REQUEST_SELECCION_CLIENTE);
                startActivityForResult(intent, ClientesActivity.REQUEST_SELECCION_CLIENTE_CODE);
            });
        }
        String fecha=VARIABLES.GET_FECHA_DIA_INICIO_MES_STRING_dd_mm_yyy();
        txtFechaDesde.setText("de: "+fecha);
        txtFechaDesde.setHint(fecha);
        CustomDateTimePicker.requestDialog(this, "de: ", txtFechaDesde, fecha);

        String fechaHasta=VARIABLES.GET_FECHA_ACTUAL_STRING_dd_mm_yyy();
        txtFechaHasta.setText("a: "+fechaHasta);
        txtFechaHasta.setHint(fechaHasta);
        CustomDateTimePicker.requestDialog(this, "de ", txtFechaHasta, fechaHasta);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode== ClientesActivity.REQUEST_SELECCION_CLIENTE_CODE){
                codcliPrincipal = data.getStringExtra("codcli");
                String nomcli 	= data.getStringExtra("nomcli");
                txtNombresCliente.setText(nomcli);
            }
        }
    }

    private void GenerarPdfDocument(String oc_numero) {
        dao_reportePedido=new DAO_ReportePedido(getApplicationContext());
        ArrayList<DataCabeceraPDF> lista=dao_reportePedido.getCabecera(""+oc_numero);
        if (lista.size()==0){
            return;
        }
        ArrayList<ReportePedidoDetallePDF> listaDetalle= dao_reportePedido.getAllDataByCodigo( oc_numero);
        GenerarPdf(lista.get(0), listaDetalle);
    }

    private void GenerarPdf(DataCabeceraPDF dataCab, ArrayList<ReportePedidoDetallePDF> listaDetalle ) {

        AlertDialog.Builder elegir = new AlertDialog.Builder(ReportesPedidosCotizacionYVisitaActivity.this);
        elegir.setTitle("Seleccionar");
        elegir.setMessage("Seleccione el tipo de envío");
        elegir.setCancelable(true);
        elegir.setPositiveButton("Cliente", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Generate_pdf_by_ocumero( 1, dataCab, listaDetalle);//1 Cliente
            }
        });
        elegir.setNegativeButton("Interno", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                 Generate_pdf_by_ocumero( 2, dataCab, listaDetalle);//2 =interno
            }
        });
        elegir.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        elegir.create().show();

    }
    private void Generate_pdf_by_ocumero( int tipoEnvio, DataCabeceraPDF dataCabecera, ArrayList<ReportePedidoDetallePDF> listaDetalle) {


        pDialog = new ProgressDialog(ReportesPedidosCotizacionYVisitaActivity.this);
        pDialog.setMessage("Generando PDF...");
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);
        pDialog.show();

        String tipo_de_cambio  = obj_dbclasses.getCambio("Tipo_cambio");
        double tasaCambioSolesToDolar  = Double.parseDouble(tipo_de_cambio);
        String nombreArchivo=dataCabecera.getTipoRegistro()+"-"+dataCabecera.getOc_numero()+".pdf";
//        String nombreArchivo=dataCabecera.getOc_numero()+".pdf";
        new AsyncTask<Void, String, String>(){
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    PDF.createPdf(getApplicationContext(),
                            nombreArchivo,
                            /*dataCabecera.getOc_numero(),
                            dataCabecera.getTipoRegistro(),
                            dataCabecera.getRuccli(),
                            dataCabecera.getCodven(),
                            dataCabecera.getNomcli(),
                            dataCabecera.getTelefono(),
                            dataCabecera.getNomven(),
                            dataCabecera.getDireccion(),
                            dataCabecera.getEmail_cliente(),
                            dataCabecera.getEmail_vendedor(),
                            dataCabecera.getDesforpag(),
                            dataCabecera.getMonto_total(),
                            dataCabecera.getValor_igv(),
                            dataCabecera.getSubtotal(),
                            dataCabecera.getPeso_total(),
                            dataCabecera.getFecha_oc(),
                            dataCabecera.getFecha_mxe(),*/
                            dataCabecera,
                            listaDetalle,
                            tasaCambioSolesToDolar,
                            tipoEnvio);
                    return "Generado";
                } catch (FileNotFoundException e) {
                    //aqui quiero enviar un error
                    e.printStackTrace();
                    return null;
                }


            }

            @Override
            protected void onPostExecute(String unused) {
                super.onPostExecute(unused);
                pDialog.dismiss();
                pDialog=null;
                if(unused==null){
                    UtilView.MENSAJE_simple(getApplicationContext(), "Error", "No se ha podido generar el pdf. Vuelva a intentarlo");
                    return;
                }
                Intent intent = new Intent(getApplicationContext(), ViewPdfActivity.class);
                intent.putExtra("oc_numero", dataCabecera.getOc_numero());
                intent.putExtra("nombreArchivo", nombreArchivo);
                startActivity(intent);

            }
        }.execute();


    }
    private void cargarListView() {
        // TODO Auto-generated method stub

        LayoutInflater inflater = LayoutInflater.from(this);
        View emptyView = inflater .inflate(R.layout.list_empty_view_unused, null);
       // emptyView.setVisibility(View.GONE);
        emptyView.setPadding(50,150,50,50);
        ///((ViewGroup) list.getParent()).addView(emptyView);
        emptyView.setClickable(false);

        adapterRecyclerView= new ReportesPedidosCabeceraRecyclerView(this, obj_dbclasses, IreportecabeceraLista);
        myRecyclerViewPedidoCabcera.setAdapter(adapterRecyclerView);


//((ReportesPedidos_Adapter) list.getAdapter()).notifyDataSetChanged();
        // adapter_pedido=new ReportesPedidosAdapter(this, pedidos);



        if (IreportecabeceraLista.size()==0 && IreportecabeceraLista.size()==0) {
            btn_peso.setVisibility(View.INVISIBLE);
            tv_montoTotal_soles.setVisibility(View.INVISIBLE);
            tv_montoTotal_dolares.setVisibility(View.INVISIBLE);
            tv_totalPedidos.setVisibility(View.INVISIBLE);
            tv_precioKiloDolar.setVisibility(View.INVISIBLE);

        } else {
            //list.getEmptyView().setVisibility(View.GONE);
            //list.getEmptyView().setVisibility(View.GONE);
            tv_totalPedidos.setText("Cantidad: " + contador);
            tv_montoTotal_soles.setText("Total(S/.) "
                    + formateador.format(redondear(montoTotal_soles)));
            tv_montoTotal_dolares.setText("Total($.) "
                    + formateador.format(redondear(montoTotal_dolares)));
            btn_peso.setText("Peso(Kg) " + formateador.format(redondear(peso)));
            tv_precioKiloDolar.setText(""+VARIABLES.getDoubleFormaterThowDecimal(totalPrecioKiloDolar) );
            btn_peso.setVisibility(View.VISIBLE);
            tv_montoTotal_soles.setVisibility(View.VISIBLE);
            tv_montoTotal_dolares.setVisibility(View.VISIBLE);
            tv_totalPedidos.setVisibility(View.VISIBLE);
        }

        onListenCLickRecyclerView();
    }
    private void onListenCLickRecyclerView(){
        adapterRecyclerView.setOnClick((position, view)->{
            onCLikItemListData(position, view);
        });
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
        IreportecabeceraLista.clear();
        contador = 0;
        peso = 0;
        montoTotal_soles = 0;
        montoTotal_dolares = 0;
        double tipoCambio = Double.parseDouble(obj_dbclasses.getCambio("Tipo_cambio"));
        totalPrecioKiloDolar = 0;

        if (tipo.equalsIgnoreCase("cliente")) {
            lista_pedidos = obj_dbclasses
                    .getPedidosCabeceraxNombreCliente(valor);
        } else if (tipo.equalsIgnoreCase("documento")) {
            lista_pedidos = obj_dbclasses.getPedidosCabeceraxDocumento(valor);
        } else {
            lista_pedidos = obj_dbclasses.getPedidosCabecera(
                    TIPO_VISTA,
                    edtBuscarPedidos.getText().toString());
        }

        Iterator<DBPedido_Cabecera> it = lista_pedidos.iterator();
        Log.w("Elementos de la lista", "tamaño: " + lista_pedidos.size());
        while (it.hasNext()) {
            Object objeto = it.next();
            DBPedido_Cabecera cta = (DBPedido_Cabecera) objeto;


            ReportePedidoCabeceraBEAN reportePedidoCabeceraBEAN = new ReportePedidoCabeceraBEAN();
            HashMap<String, String> map = new HashMap<String, String>();
            String nombre_cliente= cta.getCliente().getNombre();
            //nombre_cliente=nombre_cliente.replace("TPLAST-VISITA", "VISITA CREADO DESDE SIDIGE");
            map.put(KEY_NOMCLIENTE,"" + (nombre_cliente.length()==0?"RUC: "+cta.getCod_cli():nombre_cliente));
            reportePedidoCabeceraBEAN.setNomcli((nombre_cliente.length()==0?"RUC: "+cta.getCod_cli():nombre_cliente));
            map.put(KEY_CODCLI,"" + cta.getCod_cli());
            reportePedidoCabeceraBEAN.setCodcli(cta.getCod_cli());
            map.put(KEY_TIPOPAGO, "" + obtenerCond_Pago(cta.getCond_pago()));

            reportePedidoCabeceraBEAN.setTipopago(obtenerCond_Pago(cta.getCond_pago()));

            if (cta.getCod_noventa() > 0) {
                map.put(KEY_TOTAL, obj_dbclasses.obtenerMotivoxCodigo(cta.getCod_noventa()));
                reportePedidoCabeceraBEAN.setTotal(obj_dbclasses.obtenerMotivoxCodigo(cta.getCod_noventa()));
            } else {
                Log.d("getMonto_total", ">" + cta.getMonto_total());
                Log.d("getPercepcion_total", ">" + cta.getPercepcion_total());
                map.put(KEY_TOTAL, formateador.format(Double.parseDouble(cta.getMonto_total())+ Double.parseDouble(cta.getPercepcion_total())));
                reportePedidoCabeceraBEAN.setTotal(formateador.format(Double.parseDouble(cta.getMonto_total())+ Double.parseDouble(cta.getPercepcion_total())));
            }

            map.put(KEY_NUMOC, cta.getOc_numero());
            reportePedidoCabeceraBEAN.setNumoc(cta.getOc_numero());
            map.put(KEY_ESTADO, cta.getEstado());
            reportePedidoCabeceraBEAN.setEstado(cta.getEstado());
            map.put(KEY_MENSAJE, cta.getMensaje());
            reportePedidoCabeceraBEAN.setMensaje(cta.getMensaje());
            map.put(KEY_MONEDA, cta.getMoneda());
            reportePedidoCabeceraBEAN.setMoneda(cta.getMoneda());
            map.put("flag", cta.getFlag());
            reportePedidoCabeceraBEAN.setFlag(cta.getFlag());
            map.put("tipoRegistro", cta.getTipoRegistro());
            reportePedidoCabeceraBEAN.setTipoRegistro(cta.getTipoRegistro());
            map.put("pedidoAnterior", cta.getPedidoAnterior());
            reportePedidoCabeceraBEAN.setPedidoAnterior(cta.getPedidoAnterior());
            map.put("latitud", cta.getLatitud());
            reportePedidoCabeceraBEAN.setLatitud(cta.getLatitud());
            map.put(KEY_NOVENTA, ""+cta.getCod_noventa());
            reportePedidoCabeceraBEAN.setMotivo_noventa(cta.getCod_noventa());


           // pedidos.add(map);
            Log.w("fecha_oc", cta.getFecha_oc());
            IreportecabeceraLista.add(reportePedidoCabeceraBEAN);
            if (cta.getCod_noventa() ==0) {
                double pesoItem= Double.parseDouble(cta.getPeso_total());
                peso = peso + pesoItem;
                double tipoCambioPK = 1;
                if (cta.getMoneda().equals(PedidosActivity.MONEDA_SOLES_IN)) {
                    montoTotal_soles = montoTotal_soles+ Double.parseDouble(cta.getMonto_total());
                    tipoCambioPK= tipoCambio;
                } else {
                    montoTotal_dolares = montoTotal_dolares+ Double.parseDouble(cta.getMonto_total());
                    tipoCambioPK=1;
                }
                totalPrecioKiloDolar += (Double.parseDouble(cta.getSubTotal())/ (pesoItem>0?pesoItem:0))/tipoCambioPK;
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
        String cond_pag=dao_registrosGeneralesMovil.getDescrCondicionVentaByCod(cond);

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

        protected void onPreExecute() {
            // para el progress dialog
            pDialog = new ProgressDialog(ReportesPedidosCotizacionYVisitaActivity.this);
            pDialog.setMessage("enviando pendientes...\n\n"
                    + GlobalVar.urlService);
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... params) {
            // obtnemos usr y pass
            String mensaje = "";
            DBSync_soap_manager soap_manager = new DBSync_soap_manager(  getApplicationContext());

            ConnectionDetector connection = new ConnectionDetector( getApplicationContext());

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

            return mensaje;
        }

        protected void onPostExecute(String mensaje) {

            Toast toast = Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

            pDialog.dismiss();// ocultamos progess dialog.
            pDialog=null;
            Log.e("onPostExecute= Enviado", "" + mensaje);
            refresPedidoOrVisitas();
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
            TextView nomcliente, tipopago, total, numoc, estado, moneda, tv_pedidoAnterior, labell, edtFechavisita, edtObservacion_pedido;
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
                viewHolder.edtFechavisita = (TextView) convertView.findViewById(R.id.edtFechavisita);
                convertView.setTag(viewHolder);

            } else
                viewHolder = (ViewHolder) convertView.getTag();

//            if (IreportecabeceraLista.get(position).get("flag").toString().equals("E")) {
//                viewHolder.foto.setBackgroundColor(Color.rgb(46, 178, 0));
//            } else if (IreportecabeceraLista.get(position).get("flag").toString().equals("I")) {
//                viewHolder.foto.setBackgroundColor(Color.rgb(255, 255, 0));
//            } else if (IreportecabeceraLista.get(position).get("flag").toString().equals("P")) {
//                viewHolder.foto.setBackgroundColor(Color.rgb(255, 30, 30));
//                viewHolder.estado.setTextColor(Color.rgb(255, 30, 30));
//            } else if (IreportecabeceraLista.get(position).get("flag").toString().equals("T")) {
//                viewHolder.foto.setBackgroundColor(Color.rgb(75, 0, 130));
//                viewHolder.estado.setTextColor(Color.rgb(75, 0, 130));
//            }
//
//            String moneda = "" + IreportecabeceraLista.get(position).get("moneda").toString();
//            if (moneda.equals(PedidosActivity.MONEDA_PEN)) {
//                viewHolder.moneda.setText("S/.");
//            } else {
//                viewHolder.moneda.setText("$.");
//            }

//            String estado = IreportecabeceraLista.get(position).get("estado").toString();
//            int codnoventa = Integer.parseInt(IreportecabeceraLista.get(position).get(KEY_NOVENTA).toString());
//
//            viewHolder.edtFechavisita.setText("");
//            if (codnoventa==GlobalVar.CODIGO_VISITA_CLIENTE){
//                San_Visitas san=DAO_San_Visitas.getSan_VisitaByOc_numero(obj_dbclasses,  IreportecabeceraLista.get(position).get(KEY_NUMOC).toString());
//
//                if (san!=null) {
//                    viewHolder.nomcliente.setTextColor(Color.parseColor("#040404"));
//                    viewHolder.edtFechavisita.setTextColor(getResources().getColor(R.color.teal_600));
//                    viewHolder.edtFechavisita.setText("Visitado: "+san.getFecha_Ejecutada());
//                }
//            }
//            else if (estado.equals("A")) {
//                viewHolder.nomcliente.setTextColor(Color.parseColor("#FF0000"));
//                viewHolder.moneda.setText("");
//            } else {
//                viewHolder.nomcliente.setTextColor(Color.parseColor("#040404"));
//            }
//            viewHolder.nomcliente.setText(IreportecabeceraLista.get(position)
//                    .get("nombrecliente").toString());
//            viewHolder.tipopago.setText(IreportecabeceraLista.get(position).get("tipopago")
//                    .toString());
//            viewHolder.total.setText(IreportecabeceraLista.get(position).get("total")
//                    .toString());
//            viewHolder.numoc.setText(IreportecabeceraLista.get(position).get("numoc").toString());
//            viewHolder.estado.setText(IreportecabeceraLista.get(position).get("mensaje")
//                    .toString());
//            if (IreportecabeceraLista.get(position).get("tipoRegistro").toString().equals(PedidosActivity.TIPO_COTIZACION)) {
//                viewHolder.tv_tipoRegistro.setText("C");
//                viewHolder.tv_tipoRegistro.setBackgroundColor(getResources().getColor(R.color.indigo_500));
//
//                if (IreportecabeceraLista.get(position).get("pedidoAnterior").toString().equals("")) {
//                    viewHolder.labell.setVisibility(View.INVISIBLE);
//                    viewHolder.tv_pedidoAnterior.setVisibility(View.INVISIBLE);
//                }else{
//                    viewHolder.tv_pedidoAnterior.setText(IreportecabeceraLista.get(position).get("pedidoAnterior").toString());
//                    viewHolder.numoc.setTextColor(getResources().getColor(R.color.indigo_500));
//                }
//
//                convertView.setBackgroundResource(R.drawable.selector_reporte_cotizacion);
//            }else if (IreportecabeceraLista.get(position).get("tipoRegistro").toString().equals(PedidosActivity.TIPO_DEVOLUCION)) {
//                viewHolder.tv_tipoRegistro.setText("D");
//                viewHolder.tv_tipoRegistro.setBackgroundColor(getResources().getColor(R.color.brown_500));
//                viewHolder.labell.setVisibility(View.INVISIBLE);
//                viewHolder.tv_pedidoAnterior.setVisibility(View.INVISIBLE);
//                convertView.setBackgroundResource(R.drawable.selector_reporte_devolucion);
//            }else{
//                viewHolder.tv_tipoRegistro.setText("P");
//                viewHolder.tv_tipoRegistro.setBackgroundColor(getResources().getColor(R.color.teal_500));
//                viewHolder.labell.setVisibility(View.INVISIBLE);
//                viewHolder.tv_pedidoAnterior.setVisibility(View.INVISIBLE);
//                convertView.setBackgroundResource(R.drawable.selector_reporte_pedido);
//            }
//            boolean isRequired=obj_dbclasses.RequiereValidacionPorDescuento(IreportecabeceraLista.get(position).get("numoc").toString());
//            viewHolder.imgCampanaYellow.setVisibility(isRequired?View.VISIBLE:View.GONE);
//            if (VARIABLES.IsLatitudValido(IreportecabeceraLista.get(position).get("latitud"))){
//                viewHolder.edtObservacion_pedido.setText("");
//            }else{
//                if (IreportecabeceraLista.get(position).get(KEY_NOVENTA).toString().equals(""+GlobalVar.CODIGO_VISITA_CLIENTE)){
//                    viewHolder.edtObservacion_pedido.setText("");
//                }else{
//                    viewHolder.edtObservacion_pedido.setText("Pedido sin posición");
//                }
//            }


            OnClickCustom(viewHolder.imgCampanaYellow);
            return convertView;
        }

    }
    private void OnClickCustom(View view){
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(ReportesPedidosCotizacionYVisitaActivity.this,"Producto guardado con observación, pues el descuento supera a 3%", Toast.LENGTH_LONG);
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
        numOc = obj_dbclasses.obtenerMaxNumOc(codven);
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
                .setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> adapter, View arg1,
                                            int position, long id) {

                        selectedPosition1 = position;

                    }
                });

        btnAceptar.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                item = (DBMotivo_noventa) lstNoventa_motivo.getAdapter()
                        .getItem(selectedPosition1);

                dialogo.dismiss();

                Builder builder = new Builder(
                        ReportesPedidosCotizacionYVisitaActivity.this);
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
                                refresPedidoOrVisitas();
                                dialogo.dismiss();

                            }
                        });
                builder.create().show();

            }
        });

        btnCancelar.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialogo.dismiss();
            }
        });

        dialogo.show();

    }

    private void refresPedidoOrVisitas(){
        IreportecabeceraLista.clear();
        cargarPedidos("", "");
        adapterRecyclerView.notifyDataSetChanged();
        cargarListView();
    }
    class asyncGuardarMotivo extends AsyncTask<String, String, String> {

        String user, pass;
        private ProgressDialog pDialog;

        protected void onPreExecute() {
            // para el progress dialog
            pDialog = new ProgressDialog(ReportesPedidosCotizacionYVisitaActivity.this);
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

            IreportecabeceraLista.clear();
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

            Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_LONG)  .show();
            dialogo.dismiss();
            refresPedidoOrVisitas();
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

            pDialog = new ProgressDialog(ReportesPedidosCotizacionYVisitaActivity.this);
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

            if(tipo.equals("CAMBIAR-MONEDA")){
                return "ok";
            }
            String ret = "";

            DBSync_soap_manager sm = new DBSync_soap_manager(
                    ReportesPedidosCotizacionYVisitaActivity.this);

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

            ReportePedidoCabeceraBEAN itemData= getInstancePedidoCabceraLocal();
            if (itemData==null) {
                return;
            }
            if (result.equals("error_1")) {

                // Sin conexion al Servidor
                // No se pudo verificar asi que no se continua con la accion
                // seleccionada
                Builder alerta = new Builder(
                        ReportesPedidosCotizacionYVisitaActivity.this);
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
                Builder alerta = new Builder(
                        ReportesPedidosCotizacionYVisitaActivity.this);
                alerta.setMessage("Por el momento no es posible completar la accion");
                alerta.setCancelable(false);
                alerta.setPositiveButton("OK", null);
                alerta.show();

            } else {

                if (tipo.equals("PEDIDO")) {

                    if (result.equals("T")) {
                        // el pedido ya ha sido transferido y no se puede
                        // modificar
                        Builder alerta = new Builder(
                                ReportesPedidosCotizacionYVisitaActivity.this);
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
                        Builder alerta = new Builder(
                                ReportesPedidosCotizacionYVisitaActivity.this);
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


                    verPedidoDetalle(itemData);

                } else if (tipo.equals("PEDIDO-MODIFICAR")) {
                    if (result.equals("T")) {
                        // el pedido ya ha sido transferido y no se puede
                        // modificar
                        Builder alerta = new Builder(
                                ReportesPedidosCotizacionYVisitaActivity.this);
                        alerta.setTitle("MODIFICAR");
                        alerta.setMessage("El pedido ya ha sido tranferido\nComuniquese con el administrador");
                        alerta.setCancelable(false);
                        alerta.setPositiveButton("OK", null);
                        alerta.show();
                        return;
                    }

                    if (itemData.getTipoRegistro().equals(PedidosActivity.TIPO_COTIZACION)) {
                        DBPedido_Cabecera newPedido = clonarPedido(false, itemData.getTipoRegistro());
                        if(newPedido!=null){
                            startPedidoModificar(newPedido.getOc_numero(), newPedido.getTipoRegistro());
                        }
                    }else{
                        startPedidoModificar(itemData.getNumoc(), itemData.getTipoRegistro());
                    }

                }else if(tipo.equals("CAMBIAR-MONEDA")){
                    //debemos cambiar la moneda y clonar el pedido
                    DBPedido_Cabecera newPedido = clonarPedido(true, itemData.getTipoRegistro());
                    if(newPedido!=null){
                        if(newPedido.getTipoRegistro().equals(PedidosActivity.TIPO_COTIZACION)) {
                            startPedidoModificar(newPedido.getOc_numero(), newPedido.getTipoRegistro());
                        }else{
                            new asyncBuscar().execute("", "");
                        }
                    }
                }

            }

        }

    }
    private void startPedidoModificar(String _oc_numero, String _tipoRegistro){
        String codcli = obj_dbclasses.obtenerCodigoCliente(nomcli);

        int cod = obj_dbclasses.obtenerMotivoxCliente(codcli,
                item_direccion);
        Log.w("CODIGO_NOVENTA", "" + cod);

        String codven = new SessionManager(ReportesPedidosCotizacionYVisitaActivity.this).getCodigoVendedor();

        final Intent ipedido;
        if (_tipoRegistro.equals(PedidosActivity.TIPO_COTIZACION)) {
            ipedido = new Intent(getApplicationContext(), PedidosActivity.class);
        }else if (_tipoRegistro.equals(PedidosActivity.TIPO_DEVOLUCION)) {
            ipedido = new Intent(getApplicationContext(), CH_DevolucionesActivity.class);
        }else{
            ipedido = new Intent(getApplicationContext(), PedidosActivity.class);
        }


        ipedido.putExtra("codigoVendedor", codven);
        ipedido.putExtra("nombreCliente", nomcli);
        ipedido.putExtra("codcli", codcli);
        ipedido.putExtra("origen", "REPORTES-MODIFICAR");
        ipedido.putExtra("OC_NUMERO", _oc_numero);
        ipedido.putExtra("tipoRegistro", _tipoRegistro);

        startActivity(ipedido);
        // obj_dbclasses.actualizarEstadoCabeceraPedido(oc_numero,
        // "I");
        ReportesPedidosCotizacionYVisitaActivity.this.finish();
    }


    private DBPedido_Cabecera clonarPedido(boolean cambiarMoneda, String tipoRegistro) {

        DBPedido_Cabecera cabecera = obj_dbclasses.getRegistroPedidoCabecera(oc_numero);
        double tipocambio =  Double.parseDouble(obj_dbclasses.getCambio("Tipo_cambio"));

        String pedidoAnterior = cabecera.getOc_numero();
        String fechaOc = cabecera.getFecha_oc();
        String numOc = obj_dbclasses.obtenerMaxNumOc(codven);
        String fecha_configuracion = obj_dbclasses.getCambio("Fecha");
        String nuevoOc_numero = codven + PedidosActivity.calcularSecuencia(numOc,fecha_configuracion);
        cabecera.setOc_numero(nuevoOc_numero);
        cabecera.setFecha_oc(fechaOc);
        cabecera.setPedidoAnterior(pedidoAnterior);
        cabecera.setTipoRegistro(tipoRegistro);
        DAO_Pedido DAOPedidoDetalle = new DAO_Pedido(getApplicationContext());
        if(cambiarMoneda){
            if (!cabecera.convertirMonedaFrom(tipocambio)) {
                GlobalFunctions.showCustomToast(ReportesPedidosCotizacionYVisitaActivity.this, "Error al realizar la conversiòn de la moneda! "+nuevoOc_numero, GlobalFunctions.TOAST_ERROR);
                return null;
            }
        }

        DAOPedidoDetalle.ClonarPedido(cabecera, cambiarMoneda, tipocambio, obj_dbclasses);//Se guarda referencia del pedido anterior
        GlobalFunctions.showCustomToast(ReportesPedidosCotizacionYVisitaActivity.this, "Nuevo pedido Generado ! "+nuevoOc_numero, GlobalFunctions.TOAST_DONE);
        return cabecera;

    }

    private void verPedidoDetalle(ReportePedidoCabeceraBEAN itemdata) {
        String codcli = itemdata.getCodcli();

        int cod = obj_dbclasses.obtenerMotivoxCliente(codcli,
                item_direccion);
        Log.w("CODIGO_NOVENTA", "" + cod);

        String codven = new SessionManager(ReportesPedidosCotizacionYVisitaActivity.this).getCodigoVendedor();



        final Intent ipedido = new Intent(
                getApplicationContext(), PedidosActivity.class);
        ipedido.putExtra("codigoVendedor", codven);
        ipedido.putExtra("nombreCliente", nomcli);
        ipedido.putExtra("codcli", codcli);
        ipedido.putExtra("codcli", itemdata.getCodcli());
        ipedido.putExtra("origen", "REPORTES-PEDIDO");
        ipedido.putExtra("OC_NUMERO", oc_numero);
        ipedido.putExtra("tipoRegistro", tipoRegistro);

        startActivity(ipedido);
        // obj_dbclasses.actualizarEstadoCabeceraPedido(oc_numero,
        // "I");

    }

    class asyncVerificarPedidos extends AsyncTask<String, String, String> {

        String user, pass;
        ProgressDialog pDialog;

        protected void onPreExecute() {
            // para el progress dialog
            pDialog = new ProgressDialog(ReportesPedidosCotizacionYVisitaActivity.this);
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

            IreportecabeceraLista.clear();
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



            tv_totalPedidos.setText("Cantidad:" + contador);


        }

    }

    public void updatePedido() {

        ReportePedidoCabeceraBEAN itemData=getInstancePedidoCabceraLocal();
        if(itemData==null) return;
        String codcli = obj_dbclasses.obtenerCodigoCliente(nomcli);

        int cod = obj_dbclasses.obtenerMotivoxCliente(codcli, item_direccion);
        Log.w("CODIGO_NOVENTA", "" + cod);

        String codven = new SessionManager(ReportesPedidosCotizacionYVisitaActivity.this).getCodigoVendedor();

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
            ReportesPedidosCotizacionYVisitaActivity.this.finish();

        } else if (vista.equals("Vista 2")) {

            final Intent ipedido = new Intent(getApplicationContext(),
                    PedidosActivity.class);
            ipedido.putExtra("codigoVendedor", codven);
            ipedido.putExtra("nombreCliente", nomcli);
            ipedido.putExtra("codcli", itemData.getCodcli());
            ipedido.putExtra("codcli", itemData.getCodcli());
            ipedido.putExtra("origen", "REPORTES");
            ipedido.putExtra("OC_NUMERO", oc_numero);

            startActivity(ipedido);
            // obj_dbclasses.actualizarEstadoCabeceraPedido(oc_numero, "I");
            ReportesPedidosCotizacionYVisitaActivity.this.finish();

        } else {
            Toast.makeText(getApplicationContext(),
                    "Configure el tipo de visualizacion en Preferencias", Toast.LENGTH_SHORT)
                    .show();
        }

    }

    public void dialogoEliminarRegistro() {
        final Builder alertDialog = new Builder(
                ReportesPedidosCotizacionYVisitaActivity.this);
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
            pDialog = new ProgressDialog(ReportesPedidosCotizacionYVisitaActivity.this);
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

            IreportecabeceraLista.clear();
            cargarPedidos("", "");
            cargarListView();
            pDialog.dismiss();// ocultamos progess dialog.
            Log.e("onPostExecute= Enviado", "" + result);

            tv_totalPedidos.setText("Cantidad:" + contador);

        }

    }

    Dialog bdialog;

    EditText edt_buscar;
    Button btn_buscar, btn_cancelar;
    String tipofiltro = "cliente";

    public void poputMenu(View view){
        final PopupMenu popupMenu = new PopupMenu(ReportesPedidosCotizacionYVisitaActivity.this, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_reportespedidos, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_reportespedidos:

                        GlobalFunctions.backupdDatabaseFromExternalView(ReportesPedidosCotizacionYVisitaActivity.this);
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

        bdialog = new Dialog(ReportesPedidosCotizacionYVisitaActivity.this,
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
            pDialog = new ProgressDialog(ReportesPedidosCotizacionYVisitaActivity.this);
            pDialog.setMessage("Buscando....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... params) {
            // obtnemos usr y pass
            String valor = params[0].toString();
            String valor2 = params[1].toString();

            try {
                IreportecabeceraLista.clear();
                cargarPedidos(valor, valor2);
                cantidadDatos = CalcularPaqueteDatos();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return "E";
        }

        protected void onPostExecute(String result) {

            cargarListView();
            pDialog.dismiss();// ocultamos progess dialog.
            Log.e("onPostExecute= Enviado", "" + result);
            tv_precioKiloDolar.setText("P. Kilo ($) \n"+VARIABLES.getDoubleFormaterThowDecimal(totalPrecioKiloDolar) );
            if (IreportecabeceraLista.size()==0){
                UtilViewSnackBar.SnackBarWarning(ReportesPedidosCotizacionYVisitaActivity.this, myRecyclerViewPedidoCabcera, "No hay datos para mostrar");
                Toast.makeText(ReportesPedidosCotizacionYVisitaActivity.this, "No hay datos para mostrar", Toast.LENGTH_SHORT).show();
            }

        }



    }

    class asyncObtenerPedidosOnLine extends AsyncTask<String, String, String> {

        String user, pass;
        ProgressDialog pDialog;
        double cantidadDatos;

        protected void onPreExecute() {
            // para el progress dialog
            pDialog = new ProgressDialog(ReportesPedidosCotizacionYVisitaActivity.this);
            pDialog.setMessage("Buscando....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... params) {
            // obtnemos usr y pass
            String valor = params[0].toString();
            String valor2 = params[1].toString();

            try {
                IreportecabeceraLista.clear();
                cargarPedidos(valor, valor2);
                cantidadDatos = CalcularPaqueteDatos();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return "E";
        }

        protected void onPostExecute(String result) {

            cargarListView();
            pDialog.dismiss();// ocultamos progess dialog.
            Log.e("onPostExecute= Enviado", "" + result);
            //tv_precioKiloDolar.setText(cantidadDatos + " MB");
            if (IreportecabeceraLista.size()==0){
                UtilViewSnackBar.SnackBarWarning(ReportesPedidosCotizacionYVisitaActivity.this, myRecyclerViewPedidoCabcera, "No hay datos para mostrar");
                Toast.makeText(ReportesPedidosCotizacionYVisitaActivity.this, "No hay datos para mostrar", Toast.LENGTH_SHORT).show();
            }

        }



    }


    class asyncVerificacion_individual extends
            AsyncTask<String, String, Integer> {

        ProgressDialog pDialog;

        protected void onPreExecute() {
            // para el progress dialog
            pDialog = new ProgressDialog(ReportesPedidosCotizacionYVisitaActivity.this);
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

            Builder aviso = new Builder(
                    ReportesPedidosCotizacionYVisitaActivity.this);
            aviso.setMessage(mensaje + pregunta);
            aviso.setCancelable(false);
            aviso.setPositiveButton("Si",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            pDialog.dismiss();

                            //
                            Builder ped_password = new Builder(
                                    ReportesPedidosCotizacionYVisitaActivity.this);
                            ped_password.setCancelable(false);
                            ped_password.setMessage("Ingrese su contraseña");

                            final EditText txt_password = new EditText(
                                    ReportesPedidosCotizacionYVisitaActivity.this);
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
            pDialog = new ProgressDialog(ReportesPedidosCotizacionYVisitaActivity.this);
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

            IreportecabeceraLista.clear();
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

            Builder informe = new Builder(
                    ReportesPedidosCotizacionYVisitaActivity.this);
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
            pDialog = new ProgressDialog(ReportesPedidosCotizacionYVisitaActivity.this);
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
//d
    private void buscarCotizacionesOnline(){
        String codigo_pedido=edtBuscarPedidos.getText().toString();
        String fecha_desde=txtFechaDesde.getHint().toString();
        String fecha_hasta=txtFechaHasta.getHint().toString();


        //validar campos antes de buscar.

        if(fecha_desde.isEmpty() || fecha_hasta.isEmpty()){
            UtilView.showCustomToast(ReportesPedidosCotizacionYVisitaActivity.this,"Debe ingresar las fechas de busqueda", UtilView.TOAST_ERROR);
            return;
        }
        if(ws_cotizaciones==null){
            ws_cotizaciones = new WS_Cotizaciones(ReportesPedidosCotizacionYVisitaActivity.this);
            IreportecabeceraLista.clear();
            if(adapterRecyclerView!=null) adapterRecyclerView.clearDataAndReset();
        }
        int desde=ws_cotizaciones.desde;
        int hasta=ws_cotizaciones.desde+ws_cotizaciones.nro_item;

        if(adapterRecyclerView==null) {
            adapterRecyclerView = new ReportesPedidosCabeceraRecyclerView(this, obj_dbclasses, IreportecabeceraLista);
            myRecyclerViewPedidoCabcera.setAdapter(adapterRecyclerView);
            onListenCLickRecyclerView();
        }

        adapterRecyclerView.addFooterView();
        ws_cotizaciones.getDataCabeceras(codigo_pedido, codcliPrincipal, fecha_desde, fecha_hasta, desde, hasta, (success, data) -> {
            adapterRecyclerView.removeFooterView();
            if(!success){
                UtilView.showCustomToast(ReportesPedidosCotizacionYVisitaActivity.this,"Error, no se pudo obtener los datos de bùsqueda", UtilView.TOAST_ERROR);
                myRecyclerViewPedidoCabcera.setEmptyText("Error, no se pudo obtener los datos de bùsqueda");
                return;
            }
            ws_cotizaciones.desde+=data.size();
            setAdapterDataCotizacionOnLine(data);
        });
    }
    private void setAdapterDataCotizacionOnLine(ArrayList<CotizacionCabeceraApi> dataLinea){

        IreportecabeceraLista.addAll(dataLinea);
        adapterRecyclerView.notifyDataSetChanged();

        if(dataLinea.size()==0 || dataLinea.size()<=ws_cotizaciones.nro_item){
            adapterRecyclerView.setCallbackLoadMoreData(null);
            return;
        }
        if(IreportecabeceraLista.size()==0){
            myRecyclerViewPedidoCabcera.setEmptyText("No se encontraron datos");
        }
        Log.i(TAG, "setAdapterDataCotizacionOnLine size lista new "+dataLinea.size()+". Total size "+IreportecabeceraLista.size());
        adapterRecyclerView.setCallbackLoadMoreData(()->{
            adapterRecyclerView.setCallbackLoadMoreData(null);
            buscarCotizacionesOnline();
        });

    }
}
