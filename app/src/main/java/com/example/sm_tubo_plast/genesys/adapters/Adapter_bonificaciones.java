package com.example.sm_tubo_plast.genesys.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.BEAN.Model_bonificacion;
import com.example.sm_tubo_plast.genesys.datatypes.DBclasses;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class Adapter_bonificaciones extends BaseAdapter {

	protected Activity activity;
	protected ArrayList<Model_bonificacion> listaBonificaciones;
	
	public Adapter_bonificaciones(Activity activity, ArrayList<Model_bonificacion> listaBonificaciones){
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
		return Integer.parseInt(listaBonificaciones.get(position).getCodigo());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
			
		View item = convertView;
		ViewHolder holder;

		if (item == null) {
			LayoutInflater inflater = activity.getLayoutInflater();
			item = inflater.inflate(R.layout.item_bonificacion, null);

			holder = new ViewHolder();

			holder.txtCodigo = (TextView) item.findViewById(R.id.tv_codigoBonificacion);
			holder.txtDescripcion = (TextView) item.findViewById(R.id.tv_descripcionBonificacion);
			holder.txtEntrada = (TextView) item.findViewById(R.id.tvCantidad);
			holder.txtPrecio = (TextView) item.findViewById(R.id.tv_precioBonificacion);
			holder.txtUnidadMedida = (TextView) item.findViewById(R.id.txtUnidadMedida);
			holder.txtUnidadMedida2 = (TextView) item.findViewById(R.id.txtUnidadMedida2);
			holder.txtStock = (TextView) item.findViewById(R.id.tv_unidadMedida);
			holder.txtStock2 = (TextView) item.findViewById(R.id.tv_factorConversion);
			holder.txtCantidadBonificada = (TextView) item.findViewById(R.id.tv_saldoPendiente);
			item.setTag(holder);

		} else {

			holder = (ViewHolder) item.getTag();

		}
		
		Model_bonificacion bonificacion = listaBonificaciones.get(position);
		holder.txtCodigo.setText(bonificacion.getCodigo());
		holder.txtDescripcion.setText(bonificacion.getDescripcion());
		int cantidad = bonificacion.getCantidad();
		holder.txtCantidadBonificada.setText(cantidad+"");
		holder.txtPrecio.setText(bonificacion.getPrecio());
		
		String undMedidas[];
		DBclasses dbclass = new DBclasses(activity);
		undMedidas = dbclass.getUndmedidas(bonificacion.getCodigo());
		
		if(bonificacion.getStock() == 0 ){
			holder.txtStock.setTextColor(Color.parseColor("#FF6347"));
			holder.txtStock2.setVisibility(View.INVISIBLE);
			holder.txtUnidadMedida2.setVisibility(View.INVISIBLE);
			holder.txtUnidadMedida.setText(undMedidas[0]);
			//item.setBackgroundResource(R.drawable.list_selector_no_stock);
		}else{
			holder.txtStock.setTextColor(Color.parseColor("#343434"));
			if (undMedidas.length == 1) {
				holder.txtStock.setText(""+bonificacion.getStock());
				holder.txtStock2.setVisibility(View.INVISIBLE);
				holder.txtUnidadMedida.setText(undMedidas[0]);
				holder.txtUnidadMedida2.setVisibility(View.INVISIBLE);
			} else {
				holder.txtStock.setText(""+ (redondear((bonificacion.getStock())
								/ bonificacion.getFactorConversion())));
				holder.txtStock2.setText(""
						+ ((bonificacion.getStock()) % bonificacion.getFactorConversion()));
				holder.txtUnidadMedida.setText(undMedidas[0]);
				holder.txtUnidadMedida2.setText(undMedidas[1]);
			}
			item.setBackgroundResource(R.drawable.list_selector);
		}
		
		Log.d("Adapter_bonificaciones","codigo: "+bonificacion.getCodigo());
		Log.d("Adapter_bonificaciones","getDescripcion: "+bonificacion.getDescripcion());
		Log.d("Adapter_bonificaciones","getCantidadBonif: "+bonificacion.getCantidad());
		Log.d("Adapter_bonificaciones","cantidad: "+bonificacion.getCodigo());
		Log.d("Adapter_bonificaciones","getPrecio: "+bonificacion.getPrecio());
		Log.d("Adapter_bonificaciones","codigo: "+bonificacion.getCodigo()); 
		Log.d("Adapter_bonificaciones","codigo: "+bonificacion.getCodigo()); 
		
		return item;
	}
	
	private BigDecimal redondear(int val) {
		String r = val + "";
		BigDecimal big = new BigDecimal(r);
		big = big.setScale(0, RoundingMode.HALF_DOWN);
		return big;
	}

	public class ViewHolder {
		TextView txtCodigo;
		TextView txtDescripcion,txtCantidadBonificada;
		TextView txtEntrada, txtPrecio, txtUnidadMedida,txtUnidadMedida2;
		TextView txtStock,txtStock2;
	}

}
