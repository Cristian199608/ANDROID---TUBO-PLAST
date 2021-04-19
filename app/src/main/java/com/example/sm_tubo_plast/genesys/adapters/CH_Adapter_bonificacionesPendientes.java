package com.example.sm_tubo_plast.genesys.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sm_tubo_plast.R;

import java.util.ArrayList;

public class CH_Adapter_bonificacionesPendientes extends BaseAdapter {

	protected Activity activity;
	protected ArrayList<ModelBonificacionPendiente> listaBonificaciones;
	
	public CH_Adapter_bonificacionesPendientes(Activity activity, ArrayList<ModelBonificacionPendiente> listaBonificaciones){
		this.activity = activity;
		this.listaBonificaciones = listaBonificaciones;
	}
	
	@Override
	public int getCount() {
		return listaBonificaciones.size();
	}

	@Override
	public Object getItem(int position) {
		return listaBonificaciones.get(position);
	}

	@Override
	public long getItemId(int position) {
		return Long.parseLong(listaBonificaciones.get(position).getCodigoRegistro());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
			
		View item = convertView;
		ViewHolder holder;

		if (item == null) {
			LayoutInflater inflater = activity.getLayoutInflater();
			item = inflater.inflate(R.layout.ch_item_bonificacion_pendiente, null);

			holder = new ViewHolder();

			holder.tvPromocion = (TextView) item.findViewById(R.id.tvPromocion);
			holder.tvCodigoSalida = (TextView) item.findViewById(R.id.tv_codigoSalida);
			holder.tvDescripcionSalida = (TextView) item.findViewById(R.id.tv_descripcionSalida);
			holder.tvSaldo = (TextView) item.findViewById(R.id.tv_saldoPendiente);
			holder.tvUnidadMedida = (TextView) item.findViewById(R.id.tv_unidadMedida);			
						
			item.setTag(holder);

		} else {
			holder = (ViewHolder) item.getTag();
		}
		
		ModelBonificacionPendiente bonificacion = listaBonificaciones.get(position);
		holder.tvPromocion.setText(""+bonificacion.getPromocion());
		holder.tvCodigoSalida.setText(""+bonificacion.getCodigoSalida());		
		holder.tvDescripcionSalida.setText(""+bonificacion.getDescripcionSalida());
		holder.tvSaldo.setText(""+bonificacion.getSaldo());
		holder.tvUnidadMedida.setText(""+bonificacion.getUnidadMedida());		
		
		return item;
	}
	
	public class ViewHolder {
		TextView tvPromocion;
		TextView tvCodigoSalida, tvDescripcionSalida, tvSaldo;
		TextView tvUnidadMedida;
		
	}

}
