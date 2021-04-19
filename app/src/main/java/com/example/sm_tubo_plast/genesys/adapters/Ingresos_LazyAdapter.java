package com.example.sm_tubo_plast.genesys.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.DetalleCtasCobrarActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class Ingresos_LazyAdapter extends BaseAdapter {
    
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    private double total = 0.0; 
   
    
    public Ingresos_LazyAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
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
        vi = inflater.inflate(R.layout.item_detalle_ingresos, null);

        TextView nrodoc = (TextView)vi.findViewById(R.id.ingresos_tv_username); // nro documento
        TextView saldo = (TextView)vi.findViewById(R.id.ingresos_tv_saldo); // saldo
        TextView acuenta = (TextView)vi.findViewById(R.id.ingresos_tv_acuenta); // total
        TextView fecha = (TextView)vi.findViewById(R.id.ingresos_tv_fecha);
        
        HashMap<String, String> song = new HashMap<String, String>();
        song = data.get(position);       
        
        if(song.get(DetalleCtasCobrarActivity.KEY_SECITM).substring(0,1).equals("M")){
        	vi.setBackgroundResource(R.drawable.lis_selector_ingresos_amortizados);
        	nrodoc.setTextColor(Color.parseColor("#040404"));
        	fecha.setTextColor(Color.parseColor("#040404"));
        }
        else{
        	vi.setBackgroundResource(R.drawable.list_selector);
        	nrodoc.setTextColor(Color.parseColor("#999999"));
        	fecha.setTextColor(Color.parseColor("#10bcc9"));
        }
        
        // Setting all values in listview
        nrodoc.setText(song.get(DetalleCtasCobrarActivity.KEY_NRODOC));
        saldo.setText(song.get(DetalleCtasCobrarActivity.KEY_SALDO));
        acuenta.setText(song.get(DetalleCtasCobrarActivity.KEY_ACUENTA));
        fecha.setText(song.get(DetalleCtasCobrarActivity.KEY_FECHA));
        
        return vi;
    }
}