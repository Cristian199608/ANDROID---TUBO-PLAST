package com.example.sm_tubo_plast.genesys.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.CuentasXCobrarActivity2;

import java.util.ArrayList;
import java.util.HashMap;


public class Cobranza_LazyAdapter extends BaseAdapter {
    
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
   
    
    public Cobranza_LazyAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
     
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.item_cta_cobrar, null);

        TextView tipo_documento = (TextView)vi.findViewById(R.id.txt_view_tipo_documento); // nro documento
        TextView numero_documento = (TextView)vi.findViewById(R.id.txt_numero_documento); // Fecha del documento
        TextView fecha_emision = (TextView)vi.findViewById(R.id.txt_fecha_emision); // total
        TextView fecha_vencimiento = (TextView)vi.findViewById(R.id.txt_fecha_vencimiento);
        TextView total_documento = (TextView)vi.findViewById(R.id.txt_total_documento); // total
        TextView usuario = (TextView)vi.findViewById(R.id.txtid_usuario);
        TextView estadoCobranza = (TextView)vi.findViewById(R.id.txtEstadoCobranza);
        TextView estadoObservacion = (TextView)vi.findViewById(R.id.txt_observacion);
        TextView tipoMoneda = (TextView)vi.findViewById(R.id.txt_tipo_de_moneda);
        TextView nroUnicoBanco = (TextView)vi.findViewById(R.id.tv_nroUnicoBanco); //NroUnicoBanco
        TextView lblnroUnicoBanco = (TextView)vi.findViewById(R.id.lbl_nroUnicoBanco); 
        
        /*	TextView secuencia = (TextView)vi.findViewById(R.id.item_ctas_cobrar_tv_secuencia);
        	TextView saldo_virtual = (TextView)vi.findViewById(R.id.tv_saldo_virtual);
         */
         
        HashMap<String, String> song = new HashMap<String, String>();
        song = data.get(position);
        String estado = song.get("Estado"); 
        
        // Setting all values in listview
        tipo_documento.setText(song.get(CuentasXCobrarActivity2.KEY_TIPO_DOCUMENTO));
        numero_documento.setText(song.get(CuentasXCobrarActivity2.KEY_NUMERO)+" ");
        fecha_emision.setText(song.get(CuentasXCobrarActivity2.KEY_FECHA_E));
        fecha_vencimiento.setText(song.get(CuentasXCobrarActivity2.KEY_FECHA_V));
        total_documento.setText(song.get(CuentasXCobrarActivity2.KEY_SALDO_TOTAL));
        usuario.setText(song.get(CuentasXCobrarActivity2.KEY_USUARIO)); 
        estadoCobranza.setText(estado);
        estadoObservacion.setText("**"+song.get("Observaciones")); 
        
        if(song.get("Moneda").equals("$.")){
        	 tipoMoneda.setText("D�lares"); 
        } else {
        	 tipoMoneda.setText("Soles"); 
        }
      
        if(estado.equals("LIBRE") ){ 
        	vi.setBackgroundColor(activity.getResources().getColor(R.color.white));
        }else if(estado.equals("ACEPTAR LT") ){
        	vi.setBackgroundColor(activity.getResources().getColor(R.color.red_200));
        	
        }else if(estado.equals("ENTREGAR LT") ){ 
        	vi.setBackgroundColor(activity.getResources().getColor(R.color.yellow_300));  
        
        }else if(estado.equals("COBRAR") ){
        	vi.setBackgroundColor(activity.getResources().getColor(R.color.cyan_300));  
        }
        
        if (tipo_documento.getText().toString().equals("LETRA")) {
        	nroUnicoBanco.setText(""+song.get("NroUnicoBanco"));
		}else{
			lblnroUnicoBanco.setVisibility(View.GONE);
			nroUnicoBanco.setVisibility(View.GONE);
		}
        return vi;
    }
}