package com.example.fuerzaventaschema.genesys.adapters;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fuerzaventaschema.R;
import com.example.fuerzaventaschema.genesys.BEAN.ItemProducto;
import com.example.fuerzaventaschema.genesys.DAO.DAO_PromocionDetalle;
import com.example.fuerzaventaschema.genesys.DAO.DAO_RegistroBonificaciones;
import com.example.fuerzaventaschema.genesys.fuerza_ventas.PedidosActivity;
import com.example.fuerzaventaschema.genesys.util.VARIABLES;

@SuppressLint("LongLogTag")
public class Adapter_itemPedidoProducto extends ArrayAdapter<ItemProducto>{
	Activity context;
	ArrayList<ItemProducto> datos;
	String oc_numero="";
	DAO_RegistroBonificaciones promo=new DAO_RegistroBonificaciones(getContext());
	double valor_cambio;

	public Adapter_itemPedidoProducto(Activity context, ArrayList<ItemProducto> datos, String oc_numero, double valor_cambio) {
		super(context, R.layout.ch_item_pedido_producto, datos);
		this.context=context;
		this.datos=datos;
		this.oc_numero=oc_numero;
		this.valor_cambio=valor_cambio;


	}
	

	public View getView(int posicion, View convertview, ViewGroup parent){

		View item=convertview;
		ViewHolder holder;
		
		if(item==null){
			LayoutInflater inflater=context.getLayoutInflater();
			item=inflater.inflate(R.layout.ch_item_pedido_producto, null);
			
			holder=new ViewHolder();
//			holder.codigoProducto = (TextView) item.findViewById(R.id.tv_codigoProductoD);
			holder.descripcion = (TextView) item.findViewById(R.id.tv_descripcionD);
			holder.bonificacion = (TextView) item.findViewById(R.id.tv_bonificacion);			
			holder.precioUnidad = (TextView)item.findViewById(R.id.tv_cantidadDevolucion);
			holder.precioLista = (TextView)item.findViewById(R.id.tv_loteDevolucion);
			holder.cantidad = (TextView)item.findViewById(R.id.tv_cantidad);
			holder.totalVenta = (TextView)item.findViewById(R.id.tv_tipoDocumento);
			holder.percepcion = (TextView)item.findViewById(R.id.tv_serie);
			holder.descuento = (TextView)item.findViewById(R.id.tv_numero);
			holder.tipoProducto = (TextView) item.findViewById(R.id.view_tipoProducto);
			holder.imgCampanaYellow = (TextView) item.findViewById(R.id.imgCampanaYellow);

 			item.setTag(holder); 			
		}else{			
			holder=(ViewHolder)item.getTag();			
		}
		
		String codigoProduc = datos.get(posicion).getCodprod();
		String descripcion	= datos.get(posicion).getDescripcion();		
		double precioUnidad = datos.get(posicion).getPrecio();
		double precioLista	= datos.get(posicion).getPrecioLista();
		double cantidad		= datos.get(posicion).getCantidad();
		double totalVenta	= datos.get(posicion).getSubtotal();
		double percepcion	= datos.get(posicion).getPercepcionPedido();
		double descuento	= datos.get(posicion).getDescuento();
		String flagTipo		= datos.get(posicion).getFlagTipo();
		double precio_base	= datos.get(posicion).getPrecio_base();
		double porcenajeDescuentoMatriz= Double.parseDouble(VARIABLES.ParseDecimalTwo((((precio_base*valor_cambio) -precioLista)*100/(precio_base*valor_cambio))));
		double porcenajeDescuento= Double.parseDouble(VARIABLES.ParseDecimalTwo((descuento*100/precioLista)));

		//--(precio_base*valor_cambio -precioLista)*100/precio_base*valor_cambio)));
//		select ((14.87*3.59)-13.8797)*100/14.87*3.59
		holder.descripcion.setText(codigoProduc+" "+descripcion + ("\nDesc "+porcenajeDescuentoMatriz+"%")+(porcenajeDescuento>0.00?" + Desc "+porcenajeDescuento+"% sobre lista":""));
		holder.precioUnidad.setText(String.valueOf(precioUnidad));
		holder.precioLista.setText(String.valueOf(precioLista));
		holder.cantidad.setText(String.valueOf(cantidad));
		holder.totalVenta.setText(String.valueOf(totalVenta));
		holder.percepcion.setText(String.valueOf(percepcion));
		holder.descuento.setText(String.valueOf(descuento));
		holder.imgCampanaYellow.setVisibility(porcenajeDescuento>3.00?View.VISIBLE:View.GONE);
		OnClickCustom(holder.imgCampanaYellow);
		holder.tipoProducto.setText(""+(datos.size()-posicion));
		if(flagTipo == null){
			holder.tipoProducto.setBackgroundColor(Color.parseColor("#212121"));
		}else{
			holder.tipoProducto.setBackgroundColor(Color.parseColor(flagTipo));
		}
	
		if (codigoProduc.substring(0, 1).equals("B")) {
			//holder.bonificacion.setVisibility(View.VISIBLE);
			String secuencia_promocion=promo.getSecuenciaporEntrada(codigoProduc, oc_numero);
			//Log.w("ADAPTER ITEM PROD- CODIGO",""+sec);
			if(promo.validarColorBono(secuencia_promocion)) item.setBackgroundColor(context.getResources().getColor(R.color.yellow_100));
			else item.setBackgroundColor(context.getResources().getColor(R.color.light_green_300));
			//holder.descripcion.setTextColor(context.getResources().getColor(R.color.indigo_500));
		}	else item.setBackgroundColor(context.getResources().getColor(R.color.white));



		return item;
	}
	private void OnClickCustom(View view){
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast toast = Toast.makeText(context,"Producto guardado con observación, pues el descuento supera a 3%", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
		});
	}
	
	public class ViewHolder{
//		TextView codigoProducto;
		TextView descripcion;
		TextView bonificacion;
		TextView precioUnidad;
		TextView precioLista;
		TextView cantidad;
		TextView totalVenta;
		TextView percepcion;
		TextView descuento;	
		TextView tipoProducto;
		TextView imgCampanaYellow;
	}
}
