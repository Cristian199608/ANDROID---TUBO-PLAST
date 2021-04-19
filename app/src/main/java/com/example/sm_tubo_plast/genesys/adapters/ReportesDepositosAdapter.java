package com.example.sm_tubo_plast.genesys.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.Reportes.ReportesDepositoActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class ReportesDepositosAdapter  extends BaseAdapter {
    
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
   
    
     public ReportesDepositosAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
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
            vi = inflater.inflate(R.layout.item_reporte_deposito, null);

        TextView nomban = (TextView)vi.findViewById(R.id.rpt_deposito_tv_nombanco); // nro documento
        TextView numope = (TextView)vi.findViewById(R.id.rpt_deposito_tv_numoperacion); // saldo
        TextView total = (TextView)vi.findViewById(R.id.rpt_deposito_tv_total); // total
        TextView cod = (TextView)vi.findViewById(R.id.rpt_deposito_tv_coddeposito);
        TextView estado = (TextView)vi.findViewById(R.id.rpt_deposito_tv_estado);
 
    
        
        HashMap<String, String> song = new HashMap<String, String>();
        song = data.get(position);
        
        // Setting all values in listview
        nomban.setText(song.get(ReportesDepositoActivity.KEY_NOM_BANCO));
        numope.setText(song.get(ReportesDepositoActivity.KEY_NUM_OPE));
        total.setText(song.get(ReportesDepositoActivity.KEY_TOTAL));
        cod.setText(song.get(ReportesDepositoActivity.KEY_COD));
        estado.setText(song.get(ReportesDepositoActivity.KEY_ESTADO));
        return vi;
    }
}
