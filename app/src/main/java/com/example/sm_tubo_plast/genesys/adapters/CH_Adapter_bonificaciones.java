package com.example.sm_tubo_plast.genesys.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.BEAN.Model_bonificacion;

import java.util.ArrayList;

public class CH_Adapter_bonificaciones extends BaseAdapter {

	protected Activity activity;
	protected ArrayList<Model_bonificacion> listaBonificaciones;
	
	public CH_Adapter_bonificaciones(Activity activity, ArrayList<Model_bonificacion> listaBonificaciones){
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
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
			
		View item = convertView;
		ViewHolder holder;

		if (item == null) {
			LayoutInflater inflater = activity.getLayoutInflater();
			item = inflater.inflate(R.layout.ch_item_bonificacion, null);

			holder = new ViewHolder();

			holder.txtIsPrioridad = (TextView) item.findViewById(R.id.txtIsPrioridad);
			holder.txtDescripcion = (TextView) item.findViewById(R.id.tv_descripcionBonificacion);
			holder.txtPrecio = (TextView) item.findViewById(R.id.tv_precioBonificacion);
			holder.txtUnidadMedida = (TextView) item.findViewById(R.id.tv_unidadMedida);
			holder.txtEntrada = (TextView) item.findViewById(R.id.tvCantidad);
			holder.txtCantidadBonificada = (TextView) item.findViewById(R.id.tv_cantidadBonificacion);
			holder.txtFactorConversion = (TextView) item.findViewById(R.id.tv_factorConversion);
			holder.tvAddProductoPromo = (TextView) item.findViewById(R.id.tvAddProductoPromo);
			item.setTag(holder);

		} else {
			holder = (ViewHolder) item.getTag();
		}
		
		Model_bonificacion bonificacion = listaBonificaciones.get(position);
		holder.txtDescripcion.setText(bonificacion.getCodigo()+" - "+ bonificacion.getDescripcion());
		int cantidad = bonificacion.getCantidad();
		holder.txtCantidadBonificada.setText(cantidad+"");
		holder.txtPrecio.setText(bonificacion.getPrecio());
		holder.txtUnidadMedida.setText(""+bonificacion.getUnidadMedida());
		holder.txtFactorConversion.setText(""+bonificacion.getFactorConversion());
		holder.tvAddProductoPromo.setId(position);
		holder.tvAddProductoPromo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mylistener.onClikItem(v.getId());
			}
		});

		if (bonificacion.getPrioridad()==1){
			holder.txtIsPrioridad.setVisibility(View.VISIBLE);
		}
		else {
			holder.txtIsPrioridad.setVisibility(View.INVISIBLE);
		}

		return item;
	}
	
	public class ViewHolder {
		TextView txtIsPrioridad;
		TextView txtDescripcion,txtCantidadBonificada;
		TextView txtEntrada, txtPrecio, txtUnidadMedida;
		TextView txtFactorConversion, tvAddProductoPromo;
	}


	Mylistener mylistener;
	public void setOnMyCallback(Mylistener mylistener){
		this.mylistener=mylistener;
	}

	public interface Mylistener{
		void onClikItem(int position);
	}
}
