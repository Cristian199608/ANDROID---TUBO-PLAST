package com.example.fuerzaventaschema.genesys.adapters;


import java.util.ArrayList;


import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.fuerzaventaschema.R;
import com.example.fuerzaventaschema.genesys.BEAN.ItemProducto;

public class PedidoAdapter extends ArrayAdapter<ItemProducto>{
	
	Activity context;
	ArrayList<ItemProducto> datos;

	public PedidoAdapter(Activity context, ArrayList<ItemProducto> datos) {
		
		super(context, R.layout.list_item_producto, datos);
		this.context=context;
		this.datos=datos;
	}
	
	public View getView(int position, View convertview, ViewGroup parent){			
			
			View item=convertview;
			ViewHolder holder;
			
			if(item==null){
				LayoutInflater inflater=context.getLayoutInflater();
				item=inflater.inflate(R.layout.list_item_producto, null);
				
				holder=new ViewHolder();
				
				holder.Codigo=(TextView)item.findViewById(R.id.adapter_txtCodigo);
	 			holder.Descripcion=(TextView)item.findViewById(R.id.adapter_txtDescripcion);
	 			holder.Precio=(TextView)item.findViewById(R.id.adapter_txtPrecio); 
	 			holder.Cantidad=(TextView)item.findViewById(R.id.adapter_txtCantidad); 
	 			holder.Subtotal=(TextView)item.findViewById(R.id.adapter_txtSubtotal); 
	 			holder.UndMedida=(TextView)item.findViewById(R.id.adapter_txtUndmedida);
	 			holder.percepcion=(TextView)item.findViewById(R.id.adapter_txtPercepcion);
	 			
	 			item.setTag(holder);
	 			
			}else{				
				holder=(ViewHolder)item.getTag();				
			}
			
			double subtotal = datos.get(position).getSubtotal();
			int cantidad = datos.get(position).getCantidad();
			double precio = Math.round((subtotal/cantidad)*100)/100.0;
			String tipo = datos.get(position).getTipo();
			double percepcion = Math.round((datos.get(position).getPercepcionPedido()*100))/100.0;
			
			subtotal = Math.round(subtotal*100)/100.0;
			
			if(tipo.equals("C") || tipo.equals("M")){
				holder.Descripcion.setTextColor(Color.parseColor("#FF6347"));
				holder.Descripcion.setText(datos.get(position).getDescripcion());
				holder.Codigo.setTextColor(Color.parseColor("#FF6347"));
				holder.Codigo.setText(datos.get(position).getCodprod().trim());
			}else{
				holder.Descripcion.setTextColor(Color.parseColor("#000000"));
				holder.Descripcion.setText(datos.get(position).getDescripcion());
				holder.Codigo.setTextColor(Color.parseColor("#000000"));
				//holder.Codigo.setText(datos.get(position).getCodProveedor().trim());
				holder.Codigo.setText(datos.get(position).getCodprod().trim());
			}				
			
			holder.Precio.setText(""+precio);
			holder.Cantidad.setText(""+cantidad);
			holder.UndMedida.setText("("+datos.get(position).getDesunimed()+"): ");
			holder.percepcion.setText(""+percepcion);			
			holder.Subtotal.setText(""+subtotal);
			
			return item;
	}
	
	
public class ViewHolder{
	TextView Codigo;
	TextView Descripcion;
	TextView Precio;
	TextView Cantidad;
	TextView Subtotal;
	TextView UndMedida;
	TextView percepcion;
} 
	

}
