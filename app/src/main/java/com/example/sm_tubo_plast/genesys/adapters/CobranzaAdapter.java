package com.example.sm_tubo_plast.genesys.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.util.GlobalFunctions;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

/*
import com.genesys.datatypes.Cobranzas;
import com.genesys.datatypes.cliente;
import com.genesys.datatypes.nomcli;*/

public class CobranzaAdapter extends BaseAdapter {
    
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
   
    
     public CobranzaAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
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
        String nombreCliente, totalSoles,totalDolares,totalLetraEntregar,totalLetraAceptar,codigoCliente;
        //int asignado;
        String asignado;
        
        DecimalFormat formateador = GlobalFunctions.formateador();
        
        if(convertView==null)
        vi = inflater.inflate(R.layout.item_cobranza_main, null);

        TextView txtCliente = (TextView)vi.findViewById(R.id.item_cobranza_tv_cliente); 
        TextView txtTotalSoles = (TextView)vi.findViewById(R.id.txt_view_total_soles);
        TextView txtTotalDolares = (TextView)vi.findViewById(R.id.txt_view_total_dolares);
        TextView txtTituloLetrasEntregar =  (TextView)vi.findViewById(R.id.txt_view_titulo_letra1);
        TextView txtTituloLetrasRecoger =  (TextView)vi.findViewById(R.id.txt_view_titulo_letra2);
        /*
        TextView txtTotalLetrasEntregar = (TextView)vi.findViewById(R.id.txt_cantidad_entregar); 
        TextView txtTotalLetrasAceptar = (TextView)vi.findViewById(R.id.txt_cantidad_aceptar); 
        	*/	
        HashMap<String, String> song = new HashMap<String, String>();
        song = data.get(position);
        
        codigoCliente = song.get("codigo");
        nombreCliente = song.get("cliente");
        totalSoles ="S/." + formateador.format(Double.parseDouble(song.get("total"))>0?Double.parseDouble(song.get("total")):0);
        totalDolares = "$." + formateador.format( Double.parseDouble(song.get("total_acuenta"))>0? Double.parseDouble(song.get("total_acuenta")):0 );
        String totalSaldoSoles = "S/." + formateador.format(Double.parseDouble(song.get("totalSaldoSoles")));
        String totalSaldoDolares = "$." + formateador.format(Double.parseDouble(song.get("totalSaldoDolares")));
        //asignado=Integer.parseInt(song.get("asignado"));
        asignado=(song.get("asignado"));
        
      //  Log.e("Cobranza",nombreCliente+"/"+asignado);
       
       // if(asignado == "1" ){
        
        if(asignado.equals("1") ){
        	vi.setBackgroundColor(activity.getResources().getColor(R.color.orange_A100));
        }else{ vi.setBackgroundColor(activity.getResources().getColor(R.color.white));  }
        
        totalLetraEntregar = song.get("entregar");
        totalLetraAceptar= song.get("aceptar");
         
        txtCliente.setText( codigoCliente+" - "+nombreCliente );
        txtTotalSoles.setText( totalSaldoSoles+" de "+ totalSoles);
        txtTotalDolares.setText( totalSaldoDolares+ " de "+ totalDolares);
        
        if(Integer.parseInt(totalLetraEntregar)>0){
        	txtTituloLetrasEntregar.setText("Letras por entregar. ");
        }else {
        	txtTituloLetrasEntregar.setText("");
        }
        
        if(Integer.parseInt(totalLetraAceptar)>0){
        	txtTituloLetrasRecoger.setText("Letras por recoger. ");
        }else {
        	txtTituloLetrasRecoger.setText("");
        }

        return vi;
    }
}
