package com.example.sm_tubo_plast.genesys.fuerza_ventas;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.BEAN.ViewSeguimientoPedido;
import com.example.sm_tubo_plast.genesys.BEAN.ViewSeguimientoPedidoDetalle;
import com.example.sm_tubo_plast.genesys.CreatePDF.PDFSeguimientoOp;
import com.example.sm_tubo_plast.genesys.adapters.AdapterViewSeguimientoOp;
import com.example.sm_tubo_plast.genesys.adapters.AdapterViewSeguimientoOpDetalle;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.Reportes.ViewPdfActivity;
import com.example.sm_tubo_plast.genesys.service.WS_SeguimientoOP;
import com.example.sm_tubo_plast.genesys.session.SessionManager;
import com.example.sm_tubo_plast.genesys.util.CustomDateTimePicker;
import com.example.sm_tubo_plast.genesys.util.EditTex.ACG_EditText;
import com.example.sm_tubo_plast.genesys.util.SnackBar.UtilViewSnackBar;
import com.example.sm_tubo_plast.genesys.util.UtilViewMensaje;
import com.example.sm_tubo_plast.genesys.util.VARIABLES;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Pattern;

public class SeguimientoPedidoActivity extends AppCompatActivity {
    public static final String TAG = "SeguimientoPedidoActivity";
    public static final String SEGUIMIENTO_PEDIDO_GENERADO ="OP_GENERADO";
    public static final String SEGUIMIENTO_PEDIDO_ENTREGADO ="OP_ENTREGADO";
    public static final String SEGUIMIENTO_PEDIDO_DEVOLUCION ="OP_DEVOLUCION";
    public static final String CODCLI_TODOS ="TODOS";

    String codven="", codcliSeleccionado=CODCLI_TODOS;
    ImageButton ib_seleccionar_cliente, ib_limpiar_cliente, ib_numero_pedido,ib_buscar_op, ibVerUltimaBusqueda;
    TextView txtNombresCliente, tvVerPdf;
    EditText inputSearch_documento, edtOrdenCompra, edtNroPedido;
    RadioButton rbtnVerTodos, rbtnVerPendientes;
    RecyclerView recyclerViewDetails;
    AdapterViewSeguimientoOpDetalle adapterViewSeguimientoOpDetalle;
    Button btnMostrarOcultarResumen;

    AdapterViewSeguimientoOp adapterViewSeguimientoOp=null;
    ViewSeguimientoPedido viewSeguimientoPedido=null;
    ArrayList<ViewSeguimientoPedido> listaViewSeguimientoPedido=new ArrayList<>();
    ArrayList<ViewSeguimientoPedidoDetalle> listaDetalleOps=new ArrayList<>();
    Dialog dialogoBusquedaOps =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seguimiento_pedido);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("SEGUIMIENTO DE PEDIDO");
        codven= new SessionManager(this).getCodigoVendedor();

        edtOrdenCompra=findViewById(R.id.edtOrdenCompra);
        edtNroPedido=findViewById(R.id.edtNroPedido);
        txtNombresCliente=findViewById(R.id.txtNombresCliente);
        ib_seleccionar_cliente=findViewById(R.id.ib_seleccionar_cliente);
        ib_limpiar_cliente=findViewById(R.id.ib_limpiar_cliente);
        ib_numero_pedido=findViewById(R.id.ib_numero_pedido);
        ib_buscar_op=findViewById(R.id.ib_buscar_op);
        ibVerUltimaBusqueda=findViewById(R.id.ibVerUltimaBusqueda);
        recyclerViewDetails =findViewById(R.id.recyclerView);
        btnMostrarOcultarResumen=findViewById(R.id.btnMostrarOcultarResumen);
        inputSearch_documento=findViewById(R.id.inputSearch_documento);
        rbtnVerTodos=findViewById(R.id.rbtnVerTodos);
        rbtnVerPendientes=findViewById(R.id.rbtnVerPendientes);
        tvVerPdf=findViewById(R.id.tvVerPdf);

        //-----------------------------------------------------------------------------------------------
        ibVerUltimaBusqueda.setVisibility(View.GONE);


        recyclerViewDetails.setLayoutManager(new LinearLayoutManager(this));

        ib_seleccionar_cliente.setOnClickListener(v -> {
            Intent intent=new Intent(this, ClientesActivity.class);
            intent.putExtra("codven", codven);
            intent.putExtra("ORIGEN", TAG);
            intent.putExtra("REQUEST_TYPPE", ClientesActivity.REQUEST_SELECCION_CLIENTE);
            startActivityForResult(intent , ClientesActivity.REQUEST_SELECCION_CLIENTE_CODE);
        });
        ib_limpiar_cliente.setOnClickListener(v -> {
            limpiarFormularioAfterChangedCliente();
        });

        ib_numero_pedido.setOnClickListener(view->{
            StartBusquedaOrdenCompraPedido(view, "");
        });

        ib_buscar_op.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTryDialogOpsUltimoSelecconado(v, edtNroPedido.getText().toString());
            }
        });
        ibVerUltimaBusqueda.setOnClickListener(vi->{
            openDialogusquedaOpsUltimo();
        });
        tvVerPdf.setOnClickListener(v -> {
            if(listaDetalleOps.size()==0){
                UtilViewSnackBar.SnackBarDanger(this, v, "No hay dato del detalle.");
                return;
            }
            if(viewSeguimientoPedido==null){
                UtilViewMensaje.MENSAJE_simple(this,
                        "Falta datos de resumen",
                        "Toque en el boton "+btnMostrarOcultarResumen.getText().toString().toUpperCase()
                                +" para obtener los datos de resumen. Despues intente generar el pdf");
                return;
            }

            try {
                String nombreArchivo=""+viewSeguimientoPedido.getCoddoc()+"-"+viewSeguimientoPedido.getNum_op()+".pdf";
                new PDFSeguimientoOp().createPDFCabeceraDetalle(nombreArchivo, viewSeguimientoPedido,  listaDetalleOps);
                Intent i = new Intent(this, ViewPdfActivity.class);
                i.putExtra("nombreArchivo", nombreArchivo);
                startActivity(i);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        GestionarResumenCabecera();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode== ClientesActivity.REQUEST_SELECCION_CLIENTE_CODE){
                limpiarFormularioAfterChangedCliente();
                codcliSeleccionado 	= data.getStringExtra("codcli");
                String nomcli 	= data.getStringExtra("nomcli");
                txtNombresCliente.setText(nomcli);
            }
        }
    }
    public void limpiarFormularioAfterChangedCliente(){
        codcliSeleccionado=CODCLI_TODOS;
        txtNombresCliente.setText("");
        edtOrdenCompra.setText("");
        edtNroPedido.setText("");
        inputSearch_documento.setText("");
        ibVerUltimaBusqueda.setVisibility(View.GONE);

        viewSeguimientoPedido=null;
        listaViewSeguimientoPedido.clear();
        adapterViewSeguimientoOp=null;

        if (adapterViewSeguimientoOpDetalle!=null) {
            listaDetalleOps.clear();
            adapterViewSeguimientoOpDetalle.clearData();
        }
    }
    public void Minimazar_Maximizar(View view){
        ToggleButton ib_min_max = findViewById(R.id.ib_min_max);

        findViewById(R.id.layoutOrdenCompra).setVisibility(ib_min_max.isChecked()?View.VISIBLE:View.GONE);
        findViewById(R.id.layoutCliente).setVisibility(ib_min_max.isChecked()?View.VISIBLE:View.GONE);
    }


//    public void  GestionarBusquedaOrdenCompraPedido(View view){
//        FragmentManager fragmentManager=getSupportFragmentManager();
//        DialogFragment newFragment = new MyAlertDialogFragment();
//
//        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
//        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//        fragmentTransaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();
//
//        newFragment.show(getSupportFragmentManager(), "dialog");
//    }


    public void openDialogusquedaOpsUltimo(){
        if(dialogoBusquedaOps!=null){
            dialogoBusquedaOps.show();
            mostrarOcultarFiltrosDialogResultadoOps(true);

            adapterViewSeguimientoOp.clearDataAndReset();
            adapterViewSeguimientoOp.addData(listaViewSeguimientoPedido);

        }
    }
    public void openTryDialogOpsUltimoSelecconado(View v, String reqestNumero_op){
        boolean isFinded=false;
        if(dialogoBusquedaOps!=null){
            for (ViewSeguimientoPedido seguimientoPedido : listaViewSeguimientoPedido) {
                if(seguimientoPedido.getNum_op().trim().equals(reqestNumero_op.trim())){
                    adapterViewSeguimientoOp.clearDataAndReset();
                    adapterViewSeguimientoOp.addData(new ArrayList<>(Collections.singleton(seguimientoPedido)) );
                    isFinded=true;
//                }else{
//                    viewSeguimientoPedido=null;
//                    listaViewSeguimientoPedido.clear();
//                    listaDetalleOps.clear();
//                    recyclerViewDetails.setVisibility(View.GONE);
                }

            }
        }
        if(isFinded){
            dialogoBusquedaOps.show();
            mostrarOcultarFiltrosDialogResultadoOps(false);
            return;
        }
        StartBusquedaOrdenCompraPedido(v, reqestNumero_op);
    }
    private void mostrarOcultarFiltrosDialogResultadoOps(boolean showR){
        if(showR){
            dialogoBusquedaOps.findViewById(R.id.l1).setVisibility(View.VISIBLE);
            dialogoBusquedaOps.findViewById(R.id.l2).setVisibility(View.VISIBLE);
        }else{
            dialogoBusquedaOps.findViewById(R.id.l1).setVisibility(View.GONE);
            dialogoBusquedaOps.findViewById(R.id.l2).setVisibility(View.GONE);
        }
    }

    public void  StartBusquedaOrdenCompraPedido(View view, String reqestNumero_op){
        ibVerUltimaBusqueda.setVisibility(View.GONE);
        dialogoBusquedaOps=null;
        //-----------------------------------------------------------------------------------------------
        dialogoBusquedaOps = new Dialog(this);
        String codcliRequest=codcliSeleccionado;
        if(reqestNumero_op.length()==0){
            if (codcliRequest==null){
                UtilViewSnackBar.SnackBarWarning(this,view, "Seleccione un cliente");
                return;
            }
            else if (codcliRequest.length()==0){
                UtilViewSnackBar.SnackBarWarning(this,view, "Seleccione un cliente válido");
                return;
            }
        }else codcliRequest="";

        String finalCodcliRequest = codcliRequest;

        dialogoBusquedaOps.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialogoBusquedaOps.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogoBusquedaOps.setContentView(R.layout.dialogo_orden_compra_pedido);

        Window window = dialogoBusquedaOps.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        //wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);

        dialogoBusquedaOps.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);


        dialogoBusquedaOps.setCancelable(true);
        EditText _edtOrdenCompra= dialogoBusquedaOps.findViewById(R.id._edtOrdenCompra);
        TextView txtFechaDesde= dialogoBusquedaOps.findViewById(R.id.txtFechaDesde);
        TextView txtFechaHasta= dialogoBusquedaOps.findViewById(R.id.txtFechaHasta);
        ImageButton ibBuscarOrdenCompra= dialogoBusquedaOps.findViewById(R.id.ibBuscarOrdenCompra);
        Button txt_cancelar= dialogoBusquedaOps.findViewById(R.id.txt_cancelar);
        RecyclerView recyclerViewDialog= dialogoBusquedaOps.findViewById(R.id.recyclerView);
        recyclerViewDialog.setLayoutManager(new LinearLayoutManager(this));

        _edtOrdenCompra.setText( this.edtOrdenCompra.getText().toString());

        if (reqestNumero_op.length()>0) {
            mostrarOcultarFiltrosDialogResultadoOps(false);
        }

        txt_cancelar.setOnClickListener(v -> dialogoBusquedaOps.dismiss());
        GestionaOnclickFecha("de ", txtFechaDesde);
        GestionaOnclickFecha("a ", txtFechaHasta);


        WS_SeguimientoOP ws_seguimientoOP=new WS_SeguimientoOP(this);
        ArrayList<ViewSeguimientoPedido> dataMov=new ArrayList<>();
        adapterViewSeguimientoOp=new AdapterViewSeguimientoOp(SeguimientoPedidoActivity.this,  dataMov,null);
        recyclerViewDialog.setAdapter(adapterViewSeguimientoOp);
        //-------------------------------OnClikRecycler view----------------------------------------------------------------
        adapterViewSeguimientoOp.setOnClickListenerOP(v1 -> {
            codcliSeleccionado=dataMov.get(v1.getId()).getCodcli();
            txtNombresCliente.setText(dataMov.get(v1.getId()).getNomcli());
            viewSeguimientoPedido=dataMov.get(v1.getId());
            edtNroPedido.setText(dataMov.get(v1.getId()).getNum_op());
            edtOrdenCompra.setText(dataMov.get(v1.getId()).getOrden_compra());
            dialogoBusquedaOps.dismiss();
            BuscarOpervidor();
        });

        //-----------------------------------------------------------------------------------------------
        viewSeguimientoPedido=null;
        listaViewSeguimientoPedido.clear();
        listaDetalleOps.clear();
        recyclerViewDetails.setVisibility(View.GONE);
        //-----------------------------------------------------------------------------------------------


        ibBuscarOrdenCompra.setOnClickListener(v -> {//0199631, 0198238

            String tipoFiltro= rbtnVerTodos.isChecked()?"TODOS":"PENDIENTES";
            String textOrdenCompra=_edtOrdenCompra.getText().toString();
            String textFechaDesde= txtFechaDesde.getHint().toString();
            String textFechaHasta= txtFechaHasta.getHint().toString();
            String textRequestNumeroOp=""+reqestNumero_op;

            ws_seguimientoOP.desde=1;
            adapterViewSeguimientoOp.clearDataAndReset();
            listaViewSeguimientoPedido=new ArrayList<>();
            startBusquedaOnlineOp(
                    adapterViewSeguimientoOp,
                    ws_seguimientoOP,
                    finalCodcliRequest,
                    textOrdenCompra,
                    textFechaDesde,
                    textFechaHasta,
                    textRequestNumeroOp,
                    tipoFiltro);

        });
        dialogoBusquedaOps.create();
        dialogoBusquedaOps.show();

        ibBuscarOrdenCompra.performClick();
    }
    private void startBusquedaOnlineOp(AdapterViewSeguimientoOp adapterViewSeguimientoOp,
                                        WS_SeguimientoOP ws_seguimientoOP,
                                       String finalCodcliRequest,
                                       String textOrdenCompra,
                                       String textFechaDesde,
                                       String textFechaHasta,
                                       String textRequestNumeroOp,
                                       String tipoFiltro){

        adapterViewSeguimientoOp.addFooterView();
        ws_seguimientoOP.GetOrdenCompraPedido(finalCodcliRequest,
                textOrdenCompra,
                textFechaDesde,
                textFechaHasta,
                textRequestNumeroOp,
                tipoFiltro,
                new WS_SeguimientoOP.MyListener() {
                    @Override
                    public void Reult(boolean isOnLineData, ArrayList<ViewSeguimientoPedido> _resultData) {
                        adapterViewSeguimientoOp.addData(_resultData);
                        if(isOnLineData)listaViewSeguimientoPedido.addAll(_resultData);
                        ws_seguimientoOP.desde=adapterViewSeguimientoOp.getDataOriginal().size()+1;
                        adapterViewSeguimientoOp.removeFooterView();

                        if(listaViewSeguimientoPedido.size()>1){
                            ibVerUltimaBusqueda.setVisibility(View.VISIBLE); }
                        else {
                            listaViewSeguimientoPedido.clear();
                            ibVerUltimaBusqueda.setVisibility(View.GONE);
                        }

                        if(_resultData.size()==0 || _resultData.size()<= ws_seguimientoOP.nro_item){
                            return;
                        }
                        adapterViewSeguimientoOp.setCallbackLoadMoreData(() -> {
                            ws_seguimientoOP.setDataCargada(null);//Para cargar mas data del servidor
                            adapterViewSeguimientoOp.setCallbackLoadMoreData(null);
                            startBusquedaOnlineOp(
                                    adapterViewSeguimientoOp,
                                    ws_seguimientoOP,
                                    finalCodcliRequest,
                                    textOrdenCompra,
                                    textFechaDesde,
                                    textFechaHasta,
                                    textRequestNumeroOp,
                                    tipoFiltro);
                        });
                    }
                });
    }
    private void GestionaOnclickFecha(String prextext, TextView txtFechaDesde){
        String fecha="";
        if (prextext.contains("de")){
            fecha= VARIABLES.GET_FECHA_ACTUAL_STRING_dd_mm_yyy(30);
        }else{
            fecha= VARIABLES.GET_FECHA_ACTUAL_STRING_dd_mm_yyy();
        }
        txtFechaDesde.setText(prextext+""+fecha);
        txtFechaDesde.setHint(fecha);
        txtFechaDesde.setOnClickListener(v -> {
            new CustomDateTimePicker(this, (myCalendar, fecha_formateado) -> {
                if (fecha_formateado!=null){
                    txtFechaDesde.setText(prextext+""+fecha_formateado);
                    txtFechaDesde.setHint(fecha_formateado);
                } else{
                    txtFechaDesde.setError("Error Fecha");
                }
                return null;
            }, 0,0, true, false, false).Show();

        });

    }
    public void BuscarOpervidor(){

        if (edtNroPedido.getText().toString().length()==0) {//0133893
            UtilViewSnackBar.SnackBarDanger(this, ib_buscar_op, "Ingrese el numero de pedido");
            return;
        }
        if(viewSeguimientoPedido!=null){
            if (!viewSeguimientoPedido.getNum_op().equals(edtNroPedido.getText().toString())) {
                viewSeguimientoPedido=null;
            }
        }

        recyclerViewDetails.setVisibility(View.GONE);
        WS_SeguimientoOP ws_seguimientoOP=new WS_SeguimientoOP(this);
        ws_seguimientoOP.GetPedidoOP(edtNroPedido.getText().toString(), new WS_SeguimientoOP.MyListenerDetalle() {
            @Override
            public void ReultDetalle(boolean valor, ArrayList<ViewSeguimientoPedidoDetalle> dataDet) {

                listaDetalleOps=dataDet;
                if (dataDet.size()>0){
                    recyclerViewDetails.setVisibility(View.VISIBLE);
                    txtNombresCliente.setText(""+dataDet.get(0).getNombres());
                    codcliSeleccionado= dataDet.get(0).getCodcli();
                }
                ArrayList<ViewSeguimientoPedidoDetalle> dataDinamic=new ArrayList<>();
                dataDinamic.addAll(dataDet);

                double saldoTotal=0;
                for (ViewSeguimientoPedidoDetalle item : dataDet) {
                    if (item.getMovimiento().equals("OP")) {
                        saldoTotal+=item.getMonto_saldo();
                    }
                }

                adapterViewSeguimientoOpDetalle=new AdapterViewSeguimientoOpDetalle(
                        SeguimientoPedidoActivity.this, saldoTotal,   dataDinamic,null);
                recyclerViewDetails.setAdapter(adapterViewSeguimientoOpDetalle);
                recyclerViewDetails.setVisibility(View.VISIBLE);
                new ACG_EditText(SeguimientoPedidoActivity.this, inputSearch_documento).OnListen(new ACG_EditText.MyListener() {
                    @Override
                    public void OnChanged(String texto) {
                        dataDinamic.clear();
                        if (texto.length()>0) {
                            String textoPattern=texto.replace(" ",".*").toLowerCase();
                            Pattern pattern = Pattern.compile(".*"+textoPattern+".*");
                            for (ViewSeguimientoPedidoDetalle item : dataDet) {
                                if (pattern.matcher(item.getProducto().toLowerCase()).matches()) {
                                    dataDinamic.add(item);
                                }
                            }
                        }else{
                            dataDinamic.addAll(dataDet);
                        }
                        adapterViewSeguimientoOpDetalle.notifyDataSetChanged();
                    }
                });

            }
        });
    }
    private void GestionarResumenCabecera(){
        btnMostrarOcultarResumen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTryDialogOpsUltimoSelecconado(btnMostrarOcultarResumen, edtNroPedido.getText().toString());

            }
        });
    }

}