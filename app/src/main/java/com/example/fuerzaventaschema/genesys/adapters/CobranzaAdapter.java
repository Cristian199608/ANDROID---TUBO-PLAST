package com.example.fuerzaventaschema.genesys.adapters;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;


/*
import com.genesys.datatypes.Cobranzas;
import com.genesys.datatypes.cliente;
import com.genesys.datatypes.nomcli;*/
import com.example.fuerzaventaschema.R;
import com.example.fuerzaventaschema.genesys.util.GlobalFunctions;

import android.R.color;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

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
        
        TextView txtCodigo =  (TextView)vi.findViewById(R.id.item_cobranza_tv_codigo);
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
        totalSoles ="S/." + formateador.format(Double.parseDouble(song.get("total")));
        totalDolares = "$." + formateador.format(Double.parseDouble(song.get("total_acuenta")));
        //asignado=Integer.parseInt(song.get("asignado"));
        asignado=(song.get("asignado"));
        
      //  Log.e("Cobranza",nombreCliente+"/"+asignado);
       
       // if(asignado == "1" ){
        
        if(asignado.equals("1") ){
        	vi.setBackgroundColor(activity.getResources().getColor(R.color.orange_A100));
        }else{ vi.setBackgroundColor(activity.getResources().getColor(R.color.blue_grey_50));  }
        
        totalLetraEntregar = song.get("entregar");
        totalLetraAceptar= song.get("aceptar");
         
        txtCodigo.setText(codigoCliente);
        txtCliente.setText( nombreCliente );  
        txtTotalSoles.setText( totalSoles);
        txtTotalDolares.setText( totalDolares);
        
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
