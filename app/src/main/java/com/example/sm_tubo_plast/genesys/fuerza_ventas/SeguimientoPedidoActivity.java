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

public class SeguimientoPedidoActivity extends AppCompatActivity {
    public static final String TAG = "SeguimientoPedidoActivity";

    String codven="", codcliSeleccionado;
    ImageButton ib_seleccionar_cliente, ib_numero_pedido,ib_buscar_op;
    TextView txtNombresCliente, tvVerPdf;
    EditText inputSearch_documento, edtOrdenCompra, edtNroPedido;
    RecyclerView recyclerViewDetails;
    AdapterViewSeguimientoOpDetalle adapterViewSeguimientoOpDetalle;
    Button btnMostrarOcultarResumen;

    ViewSeguimientoPedido viewSeguimientoPedido=null;
    ArrayList<ViewSeguimientoPedidoDetalle> listaDetalleOps=new ArrayList<>();

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
        ib_numero_pedido=findViewById(R.id.ib_numero_pedido);
        ib_buscar_op=findViewById(R.id.ib_buscar_op);
        recyclerViewDetails =findViewById(R.id.recyclerView);
        btnMostrarOcultarResumen=findViewById(R.id.btnMostrarOcultarResumen);
        inputSearch_documento=findViewById(R.id.inputSearch_documento);
        tvVerPdf=findViewById(R.id.tvVerPdf);
        recyclerViewDetails.setLayoutManager(new LinearLayoutManager(this));



        ib_seleccionar_cliente.setOnClickListener(v -> {
            Intent intent=new Intent(this, ClientesActivity.class);
            intent.putExtra("codven", codven);
            intent.putExtra("ORIGEN", TAG);
            intent.putExtra("REQUEST_TYPPE", ClientesActivity.REQUEST_SELECCION_CLIENTE);
            startActivityForResult(intent , ClientesActivity.REQUEST_SELECCION_CLIENTE_CODE);
        });
        ib_buscar_op.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartBusquedaOrdenCompraPedido(v, edtNroPedido.getText().toString());
            }
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
        codcliSeleccionado="";
        txtNombresCliente.setText("");
        edtOrdenCompra.setText("");
        edtNroPedido.setText("");
        inputSearch_documento.setText("");
        viewSeguimientoPedido=null;

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

    public void  GestionarBusquedaOrdenCompraPedido(View view){
        StartBusquedaOrdenCompraPedido(view, "");
    }
    public void  StartBusquedaOrdenCompraPedido(View view, String reqestNumero_op){
        Dialog dialogo = new Dialog(this);
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

        dialogo.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialogo.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogo.setContentView(R.layout.dialogo_orden_compra_pedido);

        Window window = dialogo.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        //wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);

        dialogo.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);


        dialogo.setCancelable(true);
        EditText _edtOrdenCompra= dialogo.findViewById(R.id._edtOrdenCompra);
        TextView txtFechaDesde= dialogo.findViewById(R.id.txtFechaDesde);
        TextView txtFechaHasta= dialogo.findViewById(R.id.txtFechaHasta);
        ImageButton ibBuscarOrdenCompra= dialogo.findViewById(R.id.ibBuscarOrdenCompra);
        Button txt_cancelar= dialogo.findViewById(R.id.txt_cancelar);
        EditText edtBuscarResultado= dialogo.findViewById(R.id.edtBuscarResultado);
        RecyclerView recyclerViewDialog= dialogo.findViewById(R.id.recyclerView);
        recyclerViewDialog.setLayoutManager(new LinearLayoutManager(this));

        _edtOrdenCompra.setText( this.edtOrdenCompra.getText().toString());

        txt_cancelar.setOnClickListener(v -> dialogo.dismiss());
        GestionaOnclickFecha("de ", txtFechaDesde);
        GestionaOnclickFecha("a ", txtFechaHasta);
        if (reqestNumero_op.length()>0){
            try {
                dialogo.findViewById(R.id.l1).setVisibility(View.GONE);
                dialogo.findViewById(R.id.l2).setVisibility(View.GONE);
            }catch (Exception e){
                e.printStackTrace();
                UtilViewSnackBar.SnackBarWarning(this,view, "Error, no se ha podido ocultar algunas vistas");
            }
        }

        ibBuscarOrdenCompra.setOnClickListener(v -> {//0199631, 0198238
            WS_SeguimientoOP ws_seguimientoOP=new WS_SeguimientoOP(this);
            if (viewSeguimientoPedido!=null && reqestNumero_op.length()>0){
                if(viewSeguimientoPedido.getNum_op().trim().equals(reqestNumero_op.trim())){
                    ws_seguimientoOP.setDataCargada(viewSeguimientoPedido);
                }else{
                    viewSeguimientoPedido=null;
                    listaDetalleOps.clear();
                    recyclerViewDetails.setVisibility(View.GONE);
                }


            }
            ws_seguimientoOP.GetOrdenCompraPedido(finalCodcliRequest, _edtOrdenCompra.getText().toString(), txtFechaDesde.getHint().toString(),
                    txtFechaHasta.getHint().toString(), ""+reqestNumero_op, new WS_SeguimientoOP.MyListener() {
                @Override
                public void Reult(boolean valor, ArrayList<ViewSeguimientoPedido> data) {
                    ArrayList<ViewSeguimientoPedido> dataMov=new ArrayList<>();
                    dataMov.addAll(data);
                    edtBuscarResultado.setHint("Buscar resultados ("+dataMov.size()+" OP's)");
                    AdapterViewSeguimientoOp adapterViewSeguimientoOp=new AdapterViewSeguimientoOp(SeguimientoPedidoActivity.this,  dataMov,null);
                    recyclerViewDialog.setAdapter(adapterViewSeguimientoOp);
                    new ACG_EditText(SeguimientoPedidoActivity.this, edtBuscarResultado).OnListen(texto -> {
                        dataMov.clear();
                        for (int i = 0; i < data.size(); i++) {
                            if (data.get(i).getNum_op().toLowerCase().contains(texto.toLowerCase())
                                    || data.get(i).getNum_op().toLowerCase().contains(texto.toLowerCase())
                                    || data.get(i).getFecha_rect().toLowerCase().contains(texto.toLowerCase())
                                    || data.get(i).getOrden_compra().toLowerCase().contains(texto.toLowerCase())
                            ){
                                dataMov.add(data.get(i));
                            }
                        }
                        adapterViewSeguimientoOp.notifyDataSetChanged();
                    });


                    adapterViewSeguimientoOp.setOnClickListenerOP(v1 -> {

                        viewSeguimientoPedido=dataMov.get(v1.getId());
                        edtNroPedido.setText(dataMov.get(v1.getId()).getNum_op());
                        edtOrdenCompra.setText(dataMov.get(v1.getId()).getOrden_compra());
                        dialogo.dismiss();

                        BuscarOpervidor();
                    });
                }
            });
        });
        dialogo.create();
        dialogo.show();

        ibBuscarOrdenCompra.performClick();
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
                            for (ViewSeguimientoPedidoDetalle item : dataDet) {
                                if (item.getProducto().toLowerCase().contains(texto.toLowerCase())){
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
                StartBusquedaOrdenCompraPedido(btnMostrarOcultarResumen, edtNroPedido.getText().toString());

            }
        });
    }
}