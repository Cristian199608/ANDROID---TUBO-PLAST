package com.example.sm_tubo_plast.genesys.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.datatypes.DBCta_Ingresos;
import com.example.sm_tubo_plast.genesys.util.VARIABLES;

import java.util.ArrayList;


public class Cobranza_LazyAdapter extends BaseAdapter {
    
    private Activity activity;
    private ArrayList<DBCta_Ingresos> data;
    private static LayoutInflater inflater=null;
   private String codigoVendedor;
    
    public Cobranza_LazyAdapter(Activity a, String codigoVendedor, ArrayList<DBCta_Ingresos> d) {
        activity = a;
        data=d;
        this.codigoVendedor=codigoVendedor;
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
        TextView txtEstadoVencimiento = (TextView)vi.findViewById(R.id.txtEstadoVencimiento);
        TextView txtTipoSaldo = (TextView)vi.findViewById(R.id.txtTipoSaldo);

        /*	TextView secuencia = (TextView)vi.findViewById(R.id.item_ctas_cobrar_tv_secuencia);
        	TextView saldo_virtual = (TextView)vi.findViewById(R.id.tv_saldo_virtual);
         */

        DBCta_Ingresos song = data.get(position);
        String estado = song.getEstado_cobranza();
        
        // Setting all values in listview
        tipo_documento.setText(song.getCoddoc()+" "+song.getCodmon()+""+song.getTotal() );
        numero_documento.setText(song.getSerie_doc()+"-"+song.getNumero_factura());
        fecha_emision.setText("Fecha Emisión "+song.getFeccom());//fecha compra=fecha entrega
        fecha_vencimiento.setText("Fecha Vencimiento "+song.getFecha_vencimiento());
        total_documento.setText(song.getCodmon() + Double.parseDouble(song.getSaldo()) + "");
        if (!codigoVendedor.equals(song.getUsername())) {
            usuario.setBackground(activity.getResources().getDrawable(R.drawable.edittext_rounded_corners_cicle_purple));
        }else{
            usuario.setBackground(null);
        }
        usuario.setText("Usuario: "+song.getUsername());

        estadoCobranza.setText("Tipo: "+estado);
        estadoObservacion.setText("**"+song.getObservaciones());

        if(song.getCodmon().equals("$.")){
        	 tipoMoneda.setText("Dólares");
        } else {
        	 tipoMoneda.setText("Soles"); 
        }


        if(estado.equals("LIBRE") ){ 
            vi.setBackgroundColor(activity.getResources().getColor(R.color.white));
            if (Double.parseDouble(song.getSaldo())<0){
                vi.setBackgroundColor(activity.getResources().getColor(R.color.red_50));
            }
        }else if(estado.equals("ACEPTAR LT") ){
        	vi.setBackgroundColor(activity.getResources().getColor(R.color.red_200));
        	
        }else if(estado.equals("ENTREGAR LT") ){ 
        	vi.setBackgroundColor(activity.getResources().getColor(R.color.yellow_300));  
        
        }else if(estado.equals("COBRAR") ){
        	vi.setBackgroundColor(activity.getResources().getColor(R.color.cyan_100));
        }

        long lonVencimiento=VARIABLES.convertirFecha_to_longFromYYYY_MM_DD(song.getFecha_vencimiento());
        long lonActual=VARIABLES.GetFechaActua_long();
        if (lonVencimiento>=lonActual){
            txtTipoSaldo.setText("Obligación");
            txtEstadoVencimiento.setText("Estado: Por vencer");
            txtEstadoVencimiento.setTextColor(activity.getResources().getColor(R.color.primaryColor));
        }else{
            txtTipoSaldo.setText("Deuda");
            txtEstadoVencimiento.setText("Estado: Vencido");
            txtEstadoVencimiento.setTextColor(activity.getResources().getColor(R.color.red_700));
        }
        
//        if (tipo_documento.getText().toString().equals("LETRA")) {
        	nroUnicoBanco.setText(""+song.getNroUnicoBanco());
//		}else{
//			lblnroUnicoBanco.setVisibility(View.GONE);
//			nroUnicoBanco.setVisibility(View.GONE);
//		}



        return vi;
    }
}