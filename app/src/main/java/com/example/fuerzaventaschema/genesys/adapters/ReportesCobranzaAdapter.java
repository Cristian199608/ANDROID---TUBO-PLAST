package com.example.fuerzaventaschema.genesys.adapters;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import com.example.fuerzaventaschema.R;
import com.example.fuerzaventaschema.genesys.fuerza_ventas.Reportes.ReportesCobranzaActivity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ReportesCobranzaAdapter extends BaseAdapter {
    
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
   
    
     public ReportesCobranzaAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
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
        vi = inflater.inflate(R.layout.item_reporte_cobranza, null);

        TextView cliente = (TextView)vi.findViewById(R.id.rpt_cobranza_tv_cliente); // nro documento
        TextView cantidad = (TextView)vi.findViewById(R.id.rpt_cobranza_tv_cantidad); // saldo
        TextView total = (TextView)vi.findViewById(R.id.rpt_cobranza_tv_total2); // total
        TextView secuencia = (TextView)vi.findViewById(R.id.rpt_cobranza_tv_secuencia);
        TextView mensaje = (TextView)vi.findViewById(R.id.rpt_cobranza_tv_estado);
        TextView factura = (TextView)vi.findViewById(R.id.rpt_cobranza_tv_factura);
        TextView serie = (TextView)vi.findViewById(R.id.rpt_cobranza_tv_serie);
        ImageView foto = (ImageView)vi.findViewById(R.id.rpt_cobranza_tv_flecha);
        TextView tipo_documento = (TextView)vi.findViewById(R.id.rpt_cobranza_tv_tipo);
    
        
        HashMap<String, String> song = new HashMap<String, String>();
        song = data.get(position);
        
        // Setting all values in listview
        if(song.get("flag").toString().equals("E")){
      		foto.setBackgroundColor(Color.rgb(46, 178, 0));
      	}
      	else if(song.get("flag").toString().equals("I")){
      		foto.setBackgroundColor(Color.rgb(255,255,0));
      	}
      	else if(song.get("flag").toString().equals("P")){
      		foto.setBackgroundColor(Color.rgb(255, 30, 30));
      		mensaje.setTextColor(Color.rgb(255, 30, 30));
      	}
      	else if(song.get("flag").toString().equals("T")){
      		foto.setBackgroundColor(Color.rgb(75, 0, 130));
      		mensaje.setTextColor(Color.rgb(75, 0, 130));
      	}
      	
        
        if(song.get(ReportesCobranzaActivity.KEY_ESTADO).equals("A")){
        	cliente.setTextColor(Color.parseColor("#FF0000"));
        }
        else if(song.get(ReportesCobranzaActivity.KEY_ESTADO).equals("L")){
        	cliente.setTextColor(Color.parseColor("#04B431"));
        }
        else{
        	cliente.setTextColor(Color.parseColor("#040404"));
        }
        
        cliente.setText(song.get(ReportesCobranzaActivity.KEY_CLIENTE));
        cantidad.setText(song.get(ReportesCobranzaActivity.KEY_CANTIDAD));
        total.setText(song.get(ReportesCobranzaActivity.KEY_TOTAL));
        secuencia.setText(song.get(ReportesCobranzaActivity.KEY_SECUENCIA));
        mensaje.setText(song.get(ReportesCobranzaActivity.KEY_MENSAJE));
        serie.setText(song.get(ReportesCobranzaActivity.KEY_SERIE));
        factura.setText(song.get("factura"));
        tipo_documento.setText(song.get("tipo_documento"));
        
        return vi;
    }
}
