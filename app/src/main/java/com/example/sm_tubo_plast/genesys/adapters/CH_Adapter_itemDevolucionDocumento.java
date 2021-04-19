package com.example.sm_tubo_plast.genesys.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sm_tubo_plast.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CH_Adapter_itemDevolucionDocumento extends BaseAdapter {

	protected Activity activity;
	protected ArrayList<HashMap<String, Object>> listaDocumentos;
	
	public CH_Adapter_itemDevolucionDocumento(Activity activity, ArrayList<HashMap<String, Object>> listaDocumentos){
		this.activity = activity;
		this.listaDocumentos = listaDocumentos;
	}
	
	@Override
	public int getCount() {
		return listaDocumentos.size();
	}

	@Override
	public Object getItem(int position) {
		return listaDocumentos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
			
		View item = convertView;
		ViewHolder holder;

		if (item == null) {
			LayoutInflater inflater = activity.getLayoutInflater();
			item = inflater.inflate(R.layout.ch_item_devolucion_documentos, null);

			holder = new ViewHolder();

			holder.tv_serie 	= (TextView) item.findViewById(R.id.tv_serie);
			holder.tv_numero 	= (TextView) item.findViewById(R.id.tv_numero);
			holder.tv_tipo 		= (TextView) item.findViewById(R.id.tv_tipo);
			holder.tv_cantidad  = (TextView) item.findViewById(R.id.tv_cantidad);
			holder.tv_lote		= (TextView) item.findViewById(R.id.tv_lote);
			holder.tv_fecha 	= (TextView) item.findViewById(R.id.tv_fecha);			
			item.setTag(holder);

		} else {
			holder = (ViewHolder) item.getTag();
		}
		
		Map<String, Object> documento = listaDocumentos.get(position);		
		holder.tv_serie.setText(documento.get("serie").toString());
		holder.tv_numero.setText(documento.get("numeroDocumento").toString());
		holder.tv_tipo.setText(documento.get("tipoDocumento").toString());
		holder.tv_cantidad.setText(documento.get("fq_pedida").toString());
		holder.tv_lote.setText(documento.get("codigoLote").toString());
		holder.tv_fecha.setText(documento.get("fechaDocumento").toString());		
		
		return item;
	}
	
	public class ViewHolder {
		TextView tv_serie,tv_numero;
		TextView tv_tipo,tv_cantidad,tv_lote;		
		TextView tv_fecha;
	}

}
