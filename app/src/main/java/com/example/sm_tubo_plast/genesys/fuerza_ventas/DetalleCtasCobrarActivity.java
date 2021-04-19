package com.example.sm_tubo_plast.genesys.fuerza_ventas;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.adapters.Ingresos_LazyAdapter;
import com.example.sm_tubo_plast.genesys.datatypes.DBIngresos;
import com.example.sm_tubo_plast.genesys.datatypes.DBclasses;
import com.example.sm_tubo_plast.genesys.util.GlobalFunctions;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class DetalleCtasCobrarActivity extends AppCompatActivity {

    static final String KEY_SECUENCIA = "secuencia";

    public static final String KEY_ACUENTA = "thumb_url";

    public static final String KEY_SALDO = "nombre";
    public    static final String KEY_FECHA = "estado";
    public static final String KEY_NRODOC = "codigo";
    public static final String KEY_CODMON = "codmon";
    public static final String KEY_SECITM = "secitm";

    String sec_cta_ingresos;
    String codcli;
    String nomcli;

    DBclasses obj_dbclasses;
    ArrayList<HashMap<String, String>> detalle_ctas_cobrar=new ArrayList<HashMap<String,String>>();
    ListView list;
    Ingresos_LazyAdapter ingresos_cta_adapter;

    String numfactura;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_ctas_cobrar);


        DecimalFormat formateador = GlobalFunctions.formateador();

        Bundle bundle = getIntent().getExtras();
        numfactura = ""+bundle.getString("numfactura");
        String total = ""+bundle.getString("TOTAL");
        String acuenta_total = ""+bundle.getString("ACUENTA_TOTAL");
        String secue= ""+bundle.getString("SECUENCIA");

        String saldo = ""+bundle.getString("SALDO");

        String saldo_virtual = ""+bundle.getString("SALDO_VIRTUAL");

        setTitle("Nro Documento: "+numfactura);


        obj_dbclasses= new DBclasses(getApplicationContext());
        Button btn_numdoc = (Button)findViewById(R.id.detalle_btn_numdoc);
        btn_numdoc.setText("Nro doc: "+numfactura);

        Button btn_total = (Button)findViewById(R.id.detalle_btn_total);
        btn_total.setText("Total : S/."+formateador.format(Double.parseDouble(total)));

        Button btn_acuenta_total = (Button)findViewById(R.id.detalle_btn_acuenta);
        btn_acuenta_total.setText("Acuenta Total : S/."+formateador.format(Double.parseDouble(acuenta_total)));

        Button btn_saldo_total = (Button)findViewById(R.id.detalle_btn_saldo);
        btn_saldo_total.setText("Saldo : S/."+formateador.format(Double.parseDouble(saldo_virtual)));

        ArrayList<DBIngresos>  lista_ingresos= new ArrayList<DBIngresos>();
        lista_ingresos= obj_dbclasses.ObtenerDetalleCtaXCobrar2(secue);


        Iterator<DBIngresos> it=lista_ingresos.iterator();


        while ( it.hasNext() ) {
            Object objeto = it.next();
            DBIngresos cta = (DBIngresos)objeto;


            HashMap<String, String> map = new HashMap<String, String>();
            //map.put(KEY_CODCLI,obtenerPersona[i].getCodcli());

            if(cta.getSecitm().substring(0, 1).equals("M")){

                map.put(KEY_NRODOC,"Usuario"+ obj_dbclasses.obtenerUsernameByCodven(cta.getUsername()));

            }
            else{

                map.put(KEY_NRODOC,"Usuario"+ cta.getUsername());

            }


            map.put(KEY_CODMON,cta.getCodmon());

            if(cta.getCodmon().equals("01")){
                map.put(KEY_ACUENTA,"Acuenta: $"+ formateador.format(Double.parseDouble(cta.getAcuenta())));
            }
            else
                map.put(KEY_ACUENTA,"Acuenta: S/."+ formateador.format(Double.parseDouble(cta.getAcuenta())));

            map.put(KEY_SALDO, "Saldo: S/."+formateador.format(Double.parseDouble(cta.getSaldo())));

            map.put(KEY_FECHA, "Fecha: "+ cta.getFecpag());

            map.put(KEY_SECUENCIA, cta.getSecuencia());
            map.put(KEY_SECITM, cta.getSecitm());


            detalle_ctas_cobrar.add(map);


        }
        list=(ListView)findViewById(R.id.lv_detalle_ingresos);


        ingresos_cta_adapter = new Ingresos_LazyAdapter(this, detalle_ctas_cobrar);
        list.setAdapter(ingresos_cta_adapter);

    }

}
