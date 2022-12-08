package com.example.sm_tubo_plast.genesys.fuerza_ventas;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.BEAN.ViewSeguimientoPedido;
import com.example.sm_tubo_plast.genesys.BEAN.ViewSeguimientoPedidoDetalle;
import com.example.sm_tubo_plast.genesys.adapters.AdapterViewSeguimientoOp;
import com.example.sm_tubo_plast.genesys.adapters.AdapterViewSeguimientoOpDetalle;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.Dialog.MyAlertDialogFragment;
import com.example.sm_tubo_plast.genesys.service.WS_SeguimientoOP;
import com.example.sm_tubo_plast.genesys.util.CustomDateTimePicker;
import com.example.sm_tubo_plast.genesys.util.EditTex.ACG_EditText;
import com.example.sm_tubo_plast.genesys.util.SharePrefencia.PreferenciaPrincipal;
import com.example.sm_tubo_plast.genesys.util.SnackBar.UtilViewSnackBar;
import com.example.sm_tubo_plast.genesys.util.UtilView;
import com.example.sm_tubo_plast.genesys.util.VARIABLES;

import java.util.ArrayList;
import java.util.Calendar;

public class SeguimientoPedidoActivity extends AppCompatActivity {
    public static final String TAG = "SeguimientoPedidoActivity";
    private static final int PETICION_SEGUIMIENTO_PEDIDO_CLIENTE = 10;
    String codven="", codcliSeleccionado;
    ImageButton ib_seleccionar_cliente, ib_numero_pedido,ib_buscar_op;
    TextView txtNombresCliente;
    EditText inputSearch_documento, edtOrdenCompra, edtNroPedido;
    RecyclerView recyclerView;
    Button btnMostrarOcultarResumen;

    ViewSeguimientoPedido viewSeguimientoPedido=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seguimiento_pedido);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("SEGUIMIENTO DE PEDIDO");
        codven= new PreferenciaPrincipal(this).getCodigoVendedor();

        edtOrdenCompra=findViewById(R.id.edtOrdenCompra);
        edtNroPedido=findViewById(R.id.edtNroPedido);
        txtNombresCliente=findViewById(R.id.txtNombresCliente);
        ib_seleccionar_cliente=findViewById(R.id.ib_seleccionar_cliente);
        ib_numero_pedido=findViewById(R.id.ib_numero_pedido);
        ib_buscar_op=findViewById(R.id.ib_buscar_op);
        recyclerView=findViewById(R.id.recyclerView);
        btnMostrarOcultarResumen=findViewById(R.id.btnMostrarOcultarResumen);
        inputSearch_documento=findViewById(R.id.inputSearch_documento);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        ib_seleccionar_cliente.setOnClickListener(v -> {
            Intent intent=new Intent(this, ClientesActivity.class);
            intent.putExtra("codven", codven);
            intent.putExtra("ORIGEN", TAG);
            startActivityForResult(intent , PETICION_SEGUIMIENTO_PEDIDO_CLIENTE);
        });
        ib_buscar_op.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewSeguimientoPedido=null;
                BuscarOpervidor();
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
            if (requestCode==PETICION_SEGUIMIENTO_PEDIDO_CLIENTE){
                codcliSeleccionado 	= data.getStringExtra("codcli");
                String nomcli 	= data.getStringExtra("nomcli");
                txtNombresCliente.setText(nomcli);
            }
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

        if (codcliSeleccionado==null){
            UtilViewSnackBar.SnackBarWarning(this,view, "Seleccione un cliente");
            return;
        }
        else if (codcliSeleccionado.length()==0){
            UtilViewSnackBar.SnackBarWarning(this,view, "Seleccione un cliente válido");
            return;
        }
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
        RecyclerView recyclerView= dialogo.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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

        ibBuscarOrdenCompra.setOnClickListener(v -> {
            WS_SeguimientoOP ws_seguimientoOP=new WS_SeguimientoOP(this);
            if (viewSeguimientoPedido!=null && reqestNumero_op.length()>0){
                ws_seguimientoOP.setDataCargada(viewSeguimientoPedido);
            }
            ws_seguimientoOP.GetOrdenCompraPedido(codcliSeleccionado, _edtOrdenCompra.getText().toString(), txtFechaDesde.getHint().toString(),
                    txtFechaHasta.getHint().toString(), ""+reqestNumero_op, new WS_SeguimientoOP.MyListener() {
                @Override
                public void Reult(boolean valor, ArrayList<ViewSeguimientoPedido> data) {
                    ArrayList<ViewSeguimientoPedido> dataMov=new ArrayList<>();
                    dataMov.addAll(data);
                    AdapterViewSeguimientoOp adapterViewSeguimientoOp=new AdapterViewSeguimientoOp(SeguimientoPedidoActivity.this,  dataMov,null);
                    recyclerView.setAdapter(adapterViewSeguimientoOp);
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
                    if (reqestNumero_op.length()>0 && dataMov.size()>0 ){
                        viewSeguimientoPedido=dataMov.get(0);
                    }
                    adapterViewSeguimientoOp.setOnClickListenerOP(v1 -> {
                        if (reqestNumero_op.length()>0){
                            dialogo.dismiss();
                            return;
                        }
                        viewSeguimientoPedido=dataMov.get(v1.getId());
                        edtNroPedido.setText(dataMov.get(v1.getId()).getNum_op());
                        edtOrdenCompra.setText(dataMov.get(v1.getId()).getOrden_compra());
                        dialogo.dismiss();
                        ib_buscar_op.performClick();
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
        recyclerView.setVisibility(View.GONE);
        WS_SeguimientoOP ws_seguimientoOP=new WS_SeguimientoOP(this);
        ws_seguimientoOP.GetPedidoOP(edtNroPedido.getText().toString(), new WS_SeguimientoOP.MyListenerDetalle() {
            @Override
            public void ReultDetalle(boolean valor, ArrayList<ViewSeguimientoPedidoDetalle> dataDet) {
                if (dataDet.size()>0){
                    recyclerView.setVisibility(View.VISIBLE);
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

                AdapterViewSeguimientoOpDetalle adapterViewSeguimientoOpDetalle=new AdapterViewSeguimientoOpDetalle(
                        SeguimientoPedidoActivity.this, saldoTotal,   dataDinamic,null);
                recyclerView.setAdapter(adapterViewSeguimientoOpDetalle);
                recyclerView.setVisibility(View.VISIBLE);
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