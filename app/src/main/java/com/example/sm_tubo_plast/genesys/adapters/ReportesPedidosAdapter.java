package com.example.sm_tubo_plast.genesys.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.Reportes.ReportesPedidosActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class ReportesPedidosAdapter extends BaseAdapter {
    
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
   
    
    public  ReportesPedidosAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
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
            vi = inflater.inflate(R.layout.item_reporte_pedido, null);

        TextView nomcliente = (TextView)vi.findViewById(R.id.rpt_pedido_tv_cliente); // nro documento
        TextView tipopago = (TextView)vi.findViewById(R.id.rpt_pedido_tv_tipopago); // saldo
        TextView total = (TextView)vi.findViewById(R.id.rpt_pedido_tv_total); // total
        TextView numoc = (TextView)vi.findViewById(R.id.rpt_pedido_tv_oc_numero);
        TextView estado = (TextView)vi.findViewById(R.id.rpt_pedido_tv_estado);
 
    
        
        HashMap<String, String> song = new HashMap<String, String>();
        song = data.get(position);
        
        // Setting all values in listview
        nomcliente.setText(song.get(ReportesPedidosActivity.KEY_NOMCLIENTE));
        tipopago.setText(song.get(ReportesPedidosActivity.KEY_TIPOPAGO));
        total.setText(song.get(ReportesPedidosActivity.KEY_TOTAL));
        numoc.setText(song.get(ReportesPedidosActivity.KEY_NUMOC));
        estado.setText(song.get(ReportesPedidosActivity.KEY_ESTADO));
        return vi;
    }
}