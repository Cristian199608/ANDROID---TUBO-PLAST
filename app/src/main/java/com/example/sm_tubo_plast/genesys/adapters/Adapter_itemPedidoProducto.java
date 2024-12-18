package com.example.sm_tubo_plast.genesys.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.BEAN.ItemProducto;
import com.example.sm_tubo_plast.genesys.DAO.DAO_RegistroBonificaciones;
import com.example.sm_tubo_plast.genesys.util.SharePrefencia.PreferenciaConfiguracion;
import com.example.sm_tubo_plast.genesys.util.VARIABLES;

import java.util.ArrayList;

@SuppressLint("LongLogTag")
public class Adapter_itemPedidoProducto extends ArrayAdapter<ItemProducto>{
	Activity context;
	ArrayList<ItemProducto> datos;
	String oc_numero="";
	DAO_RegistroBonificaciones promo=new DAO_RegistroBonificaciones(getContext());
	double valor_cambio;
	PreferenciaConfiguracion preferenciaConfiguracion=null ;
	public Adapter_itemPedidoProducto(Activity context, ArrayList<ItemProducto> datos, String oc_numero, double valor_cambio) {
		super(context, R.layout.ch_item_pedido_producto, datos);
		this.context=context;
		this.datos=datos;
		this.oc_numero=oc_numero;
		this.valor_cambio=valor_cambio;
		this.preferenciaConfiguracion=new PreferenciaConfiguracion(context);

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
			holder.precioLista = (TextView)item.findViewById(R.id.precioLista);
			holder.cantidad = (TextView)item.findViewById(R.id.tv_cantidad);
			holder.precioUnitario = (TextView)item.findViewById(R.id.precioUnitario);
			holder.tv_subtotal = (TextView)item.findViewById(R.id.tv_subtotal);
			holder.percepcion = (TextView)item.findViewById(R.id.tv_serie);
			holder.tv_descuentoPorcentaje = (TextView)item.findViewById(R.id.tv_descuentoPorcentaje);
			holder.descuento = (TextView)item.findViewById(R.id.tv_numero);
			holder.tipoProducto = (TextView) item.findViewById(R.id.view_tipoProducto);
			holder.imgCampanaYellow = (TextView) item.findViewById(R.id.imgCampanaYellow);

 			item.setTag(holder); 			
		}else{			
			holder=(ViewHolder)item.getTag();			
		}
		
		String codigoProduc = datos.get(posicion).getCodprod();
		String descripcion	= datos.get(posicion).getDescripcionAnPreConcatenarBonif();
		double precioUnidad = datos.get(posicion).getPrecio();
		double precioLista	= datos.get(posicion).getPrecioLista();
		double cantidad		= datos.get(posicion).getCantidad();
		double subTotal	= datos.get(posicion).getSubtotal();
		double percepcion	= datos.get(posicion).getPercepcionPedido();
		double descuento	= datos.get(posicion).getDescuento();
		double porcentaje_desc	= datos.get(posicion).getPorcentaje_desc();
		String flagTipo		= datos.get(posicion).getFlagTipo();
		double precio_base	= datos.get(posicion).getPrecio_base();
		double porcenajeDescuentoMatriz= Double.parseDouble(VARIABLES.ParseDecimalTwo((((precio_base*valor_cambio) -precioLista)*100/(precio_base*valor_cambio))));
		double porcenajeDescuento= Double.parseDouble(VARIABLES.ParseDecimalTwo(porcentaje_desc));
		double porcenajeDescuentoExtra= Double.parseDouble(VARIABLES.ParseDecimalTwo(datos.get(posicion).getPorcentaje_desc_extra()));

		holder.descripcion.setText(codigoProduc+" - "+descripcion + (porcenajeDescuentoMatriz>0.0?"\nDesc a precio base "+porcenajeDescuentoMatriz+"%":""));
		holder.precioUnidad.setText(String.valueOf(precioUnidad));
		holder.precioLista.setText(String.valueOf(precioLista));
		holder.cantidad.setText(String.valueOf(cantidad));
		holder.tv_subtotal.setText(String.valueOf(subTotal));
		holder.precioUnitario.setText(VARIABLES.getStringFormaterThreeDecimal( precioUnidad));
		holder.percepcion.setText(String.valueOf(percepcion));
		holder.descuento.setText(VARIABLES.getStringFormaterThreeDecimal(descuento));
		holder.descuento.setVisibility(descuento>0.0?View.VISIBLE:View.GONE);
		holder.tv_descuentoPorcentaje.setText(""+porcenajeDescuento+"%"+(porcenajeDescuentoExtra>0?" + "+porcenajeDescuentoExtra+"%":""));
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
		TextView tv_subtotal, precioUnitario;
		TextView percepcion;
		TextView tv_descuentoPorcentaje, descuento;
		TextView tipoProducto;
		TextView imgCampanaYellow;
	}
}
