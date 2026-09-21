package com.example.sm_tubo_plast.genesys.fuerza_ventas.cliente.AnticiposCliente;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.BEAN.PedidoAnticipoDetalle;
import com.example.sm_tubo_plast.genesys.DAO.DAO_PedidoAnticipoDetalle;
import com.example.sm_tubo_plast.genesys.datatypes.DBCta_Ingresos;
import com.example.sm_tubo_plast.genesys.datatypes.DBclasses;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.cliente.AnticiposCliente.adapter.AnticiposClienteAdapter;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.cliente.AnticiposCliente.beanView.AnticiposClienteUtil;
import com.example.sm_tubo_plast.genesys.util.GlobalFunctions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;

public class AnticiposClienteActivity extends AppCompatActivity {
    private static final String TAG = "AnticiposClienteActivity";
    public static void startActivity(Activity activity, String oc_numero, String codcli){
        Intent intent = new Intent(
                activity,
                AnticiposClienteActivity.class
        );
        intent.putExtra("oc_numero", oc_numero);
        intent.putExtra("codcli", codcli);
        activity.startActivityForResult(intent, 1001);
    }


    String oc_numero, codcli;
    DBclasses dBclasses;
    DAO_PedidoAnticipoDetalle daoPedidoAnticipoDetalle;

    private RecyclerView recyclerView;
    private AnticiposClienteAdapter adapter;
    private ArrayList<AnticiposClienteUtil> listaAnticipos =new ArrayList<>();
    private TextView tvTotalAnticipos;
    private Button btnFinalizar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anticipos_cliente);
        Bundle param=getIntent().getExtras();
        oc_numero =param.getString("oc_numero", null);
        codcli =param.getString("codcli", null);
        config();
        data();
    }

    private void config(){
        dBclasses=new DBclasses(this);
        daoPedidoAnticipoDetalle=new DAO_PedidoAnticipoDetalle(dBclasses);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                goBackgroundNotify();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void goBackgroundNotify(){
        setResult(RESULT_OK);
        finish();
    }

    private void data(){
        recyclerView = findViewById(R.id.recyclerAnticipos);
        tvTotalAnticipos = findViewById(R.id.tvTotalAnticipos);
        btnFinalizar =  findViewById(R.id.btnFinalizar);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AnticiposClienteAdapter(this, listaAnticipos, new AnticiposClienteAdapter.MyCallback() {
            @Override
            public boolean resultGuardar(AnticiposClienteUtil item, boolean add) {
                calcularTotal();
                return guardarAnticipoDB(item, add);
            }
        });
        recyclerView.setAdapter(adapter);
        eventos();
        cargarAnticipos();
    }
    private void  eventos(){
        btnFinalizar.setOnClickListener(v->{
            goBackgroundNotify();
        });
    }


    private void cargarAnticipos() {
        listaAnticipos.clear();
//        ArrayList<DBCta_Ingresos> list = dBclasses.VerificarCtasXCobrar(codcli);
        ArrayList<DBCta_Ingresos> list = dBclasses.verificarCtasXCobrarAnticipos(codcli);
        for (int i = 0; i < list.size(); i++) {
            DBCta_Ingresos item= list.get(i);
            listaAnticipos.add(new AnticiposClienteUtil(
                    oc_numero,
                    item.getSerie_doc(),
                    item.getNumero_factura(),
                    Double.parseDouble(item.getTotal()),
                    item.getObservacion()
            ));
        }

        cargarSeleccionados();
        adapter.notifyDataSetChanged();
        calcularTotal();
        if(listaAnticipos.size()==0){
            new AlertDialog.Builder(this)
                    .setMessage("No hay anticipos")
                    .setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).create().show();
        }
    }

    private void cargarSeleccionados() {
        HashMap<String, Double> guardados = PedidoAnticipoDetalle.Companion.obtenerAnticiposPedido(daoPedidoAnticipoDetalle.getDataBy(oc_numero));
        for (int i = 0; i < listaAnticipos.size(); i++) {
            String key =listaAnticipos.get(i).getSerie_doc() +"|" +listaAnticipos.get(i).getNumero_doc();
            if (guardados.containsKey(key)) {
                double montoGuardado = guardados.get(key);
                listaAnticipos.get(i).setSeleccionado(true);
                listaAnticipos.get(i).setMontoSeleccionado(
                        montoGuardado
                );
            }
        }
    }

    private void calcularTotal() {
        double total = 0.00;
        for (AnticiposClienteUtil item : listaAnticipos) {
            if (item.isSeleccionado()) {
                total += item.getMontoSeleccionado();
            }
        }
        tvTotalAnticipos.setText(String.format(Locale.US,"S/ %.2f",total));
    }
    private boolean guardarAnticipoDB(AnticiposClienteUtil item, boolean add){
        daoPedidoAnticipoDetalle.deleteBy(oc_numero, item.getSerie_doc(),item.getNumero_doc());
        if(!add){
            return true;
        }
        PedidoAnticipoDetalle ddd=new PedidoAnticipoDetalle();
        ddd.setOc_numero(oc_numero);
        ddd.setSerie_doc(item.getSerie_doc());
        ddd.setNumero_doc(item.getNumero_doc());
        ddd.setMonto(item.getMontoSeleccionado());
        ArrayList<PedidoAnticipoDetalle> lis=new ArrayList<>();
        lis.add(ddd);
        boolean isOK= daoPedidoAnticipoDetalle.insertAll(null,  lis);
        if(isOK){
            GlobalFunctions.showCustomToast(this, "Anticipo guardado", GlobalFunctions.TOAST_DONE);
        }
        return isOK;
    }
}