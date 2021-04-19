package com.example.sm_tubo_plast.genesys.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.sm_tubo_plast.R;

import java.util.ArrayList;

public class CH_Adapter_itemDevolucionProducto extends ArrayAdapter<ModelDevolucionProducto> {
	Activity context;
	ArrayList<ModelDevolucionProducto> datos;
	
	public CH_Adapter_itemDevolucionProducto(Activity context, ArrayList<ModelDevolucionProducto> datos) {		
		super(context, R.layout.ch_item_pedido_producto, datos);
		this.context=context;
		this.datos=datos;
	}
	
	public class ViewHolder{
		TextView codigoProducto;
		TextView descripcionProducto;
		TextView cantidad;
		TextView lote;
		TextView fechaDocumento;
		TextView motivo;
		TextView expectativa;
		TextView tipoDocumento;
		TextView serie;
		TextView numero;		
	}
	
	public View getView(int posicion, View convertview, ViewGroup parent){

		View item=convertview;
		ViewHolder holder;
		
		if(item==null){
			LayoutInflater inflater=context.getLayoutInflater();
			item=inflater.inflate(R.layout.ch_item_devolucion_producto, null);
			
			holder=new ViewHolder();
			holder.codigoProducto = (TextView) item.findViewById(R.id.tv_codigoProductoD);
			holder.descripcionProducto = (TextView) item.findViewById(R.id.tv_descripcionD);
			holder.cantidad = (TextView) item.findViewById(R.id.tv_cantidadDevolucion);
			holder.lote = (TextView) item.findViewById(R.id.tv_loteDevolucion);			
			//holder.fechaDocumento = (TextView)item.findViewById(R.id.);
			holder.motivo = (TextView)item.findViewById(R.id.tv_motivo);
			holder.expectativa = (TextView)item.findViewById(R.id.tv_expectativa);
			holder.tipoDocumento = (TextView)item.findViewById(R.id.tv_tipoDocumento);
			holder.serie = (TextView)item.findViewById(R.id.tv_serie);
			holder.numero = (TextView)item.findViewById(R.id.tv_numero);			
			 			
 			item.setTag(holder); 			
		}else{			
			holder=(ViewHolder)item.getTag();			
		}
		
		String codigoProducto= datos.get(posicion).getCodigoProducto();
		String descripcion	= datos.get(posicion).getDescripcionProducto();
		String cantidad 	= datos.get(posicion).getCantidad();
		String lote			= datos.get(posicion).getLote();
		String fecha		= datos.get(posicion).getFechaDocumento();
		String motivo		= datos.get(posicion).getMotivo();
		String expectativa	= datos.get(posicion).getExpectativa();
		String tipoDocumento= datos.get(posicion).getTipoDocumento();
		String serie		= datos.get(posicion).getSerie();
		String numero		= datos.get(posicion).getNumero();
		
		holder.codigoProducto.setText(codigoProducto);
		holder.descripcionProducto.setText(descripcion);
		holder.cantidad.setText(cantidad);
		holder.lote.setText(lote);
		//holder.fechaDocumento.setText(fecha);
		holder.motivo.setText(motivo);
		holder.expectativa.setText(expectativa);
		holder.tipoDocumento.setText(tipoDocumento);
		holder.serie.setText(serie);
		holder.numero.setText(numero);
			
		return item;
	}

}
