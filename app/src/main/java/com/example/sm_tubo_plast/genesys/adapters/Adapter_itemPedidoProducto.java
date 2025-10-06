package com.example.sm_tubo_plast.genesys.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.BEAN.ItemProducto;
import com.example.sm_tubo_plast.genesys.BEAN.Pedido_detalle2;
import com.example.sm_tubo_plast.genesys.DAO.DAO_Pedido_detalle2;
import com.example.sm_tubo_plast.genesys.DAO.DAO_RegistroBonificaciones;
import com.example.sm_tubo_plast.genesys.util.FormateadorNumero;
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
			holder.precioKiloDolar = (TextView)item.findViewById(R.id.tv_numero);
			holder.tipoProducto = (TextView) item.findViewById(R.id.view_tipoProducto);
			holder.imgCampanaYellow = (TextView) item.findViewById(R.id.imgCampanaYellow);
			holder.linearLayoutPromoComboContainer=(LinearLayout) item.findViewById(R.id.linearLayoutPromoComboContainer);

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
		double pesoTotal	= datos.get(posicion).getPeso();
		double porcentaje_desc	= datos.get(posicion).getPorcentaje_desc();
		String flagTipo		= datos.get(posicion).getFlagTipo();
		double precio_base	= datos.get(posicion).getPrecio_base();
		double porcenajeDescuentoMatriz= Double.parseDouble(VARIABLES.ParseDecimalTwo((((precio_base*valor_cambio) -precioLista)*100/(precio_base*valor_cambio))));
		double porcenajeDescuento= Double.parseDouble(VARIABLES.ParseDecimalTwo(porcentaje_desc));
		double porcenajeDescuentoExtra= Double.parseDouble(VARIABLES.ParseDecimalTwo(datos.get(posicion).getPorcentaje_desc_extra()));
		double precioKilo= getPrecioKilo_str(subTotal, pesoTotal, valor_cambio);

		holder.descripcion.setText(codigoProduc+" - "+descripcion + (porcenajeDescuentoMatriz>0.0?"\nDesc a precio base "+porcenajeDescuentoMatriz+"%":""));
		holder.precioUnidad.setText(String.valueOf(precioUnidad));
		holder.precioLista.setText(String.valueOf(precioLista));
		holder.cantidad.setText(String.valueOf(cantidad));
		holder.tv_subtotal.setText(String.valueOf(subTotal));
		holder.precioUnitario.setText(VARIABLES.getStringFormaterThreeDecimal( precioUnidad));
		holder.percepcion.setText(String.valueOf(percepcion));

		holder.precioKiloDolar.setText(VARIABLES.getStringFormaterThreeDecimal(precioKilo));

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
			if(promo.validarColorBono(secuencia_promocion)) item.setBackgroundColor(context.getResources().getColor(R.color.purple_200));
			else {
				int colorItem=R.color.light_green_300;
				if (datos.get(posicion).getSec_promo().length()>0) colorItem= R.color.orange_200;
				if (datos.get(posicion).getSec_promo_prioridad()>0) colorItem= R.color.orange_400;
				item.setBackgroundColor(context.getResources().getColor(colorItem));
			}
			//holder.descripcion.setTextColor(context.getResources().getColor(R.color.indigo_500));
		}	else item.setBackgroundColor(context.getResources().getColor(R.color.white));


		AddPromoComboBonificacionSiExiste(holder.linearLayoutPromoComboContainer, datos.get(posicion));

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
	private double getPrecioKilo_str(double precioNeto, double pesoTotalProducto, double tipoCambio) {
		return (precioNeto/pesoTotalProducto/tipoCambio);
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
		TextView tv_descuentoPorcentaje, precioKiloDolar;
		TextView tipoProducto;
		TextView imgCampanaYellow;
		LinearLayout linearLayoutPromoComboContainer;
	}


	private void AddPromoComboBonificacionSiExiste(LinearLayout linearLayoutPromoComboContainer, ItemProducto itemProd ){
		linearLayoutPromoComboContainer.removeAllViews();
		int secPromocion= itemProd.getSec_promo().length()>0? Integer.parseInt(itemProd.getSec_promo()): 0;
		int secPromoPrioridad = itemProd.getSec_promo_prioridad();
		if ((secPromocion<=0 && secPromoPrioridad<=0) || itemProd.getTipo().equals("V")){
			return;
		}
		if(secPromocion>0) AddPromoComboBonificacionView(linearLayoutPromoComboContainer, secPromocion, itemProd.getItem());
		if(secPromoPrioridad>0) AddPromoComboBonificacionView(linearLayoutPromoComboContainer, secPromoPrioridad, itemProd.getItem());

	}
	private void AddPromoComboBonificacionView(LinearLayout linearLayoutPromoComboContainer, int secPromocion, int salidaItem ){
		DAO_Pedido_detalle2 daopedido2=new DAO_Pedido_detalle2(context);
		ArrayList<Pedido_detalle2> listaPedido2=daopedido2.getDataView(oc_numero, secPromocion, salidaItem);
		for (Pedido_detalle2 pedido_detalle2 : listaPedido2) {
			View pedido2View = LayoutInflater.from(context).inflate(R.layout.item_pedido_detalle2, null);
			TextView tvComboPrductoInfo= pedido2View.findViewById(R.id.tvComboPrductoInfo);
			TextView tvDet2ComboUnidadMedida= pedido2View.findViewById(R.id.tvDet2ComboUnidadMedida);
			TextView tvDet2ComboCantidad= pedido2View.findViewById(R.id.tvDet2ComboCantidad);
			TextView tvMensajeValidadoStock= pedido2View.findViewById(R.id.tvMensajeValidadoStock);
			TextView tvDet2PrecioLista= pedido2View.findViewById(R.id.tvDet2PrecioLista);
			TextView tvDet2PrecioUnit= pedido2View.findViewById(R.id.tvDet2PrecioUnit);
			TextView tvDet2DsctPctj= pedido2View.findViewById(R.id.tvDet2DsctPctj);
			TextView tvDet2PkDolar= pedido2View.findViewById(R.id.tvDet2PkDolar);
			TextView tvDet2SubTotal= pedido2View.findViewById(R.id.tvDet2SubTotal);

			tvComboPrductoInfo.setText(pedido_detalle2.getCodpro()+" "+pedido_detalle2.getItemProducto().getDescripcion());
			tvDet2ComboUnidadMedida.setText(pedido_detalle2.getItemProducto().getDesunimed());
			tvDet2ComboCantidad.setText(""+pedido_detalle2.getCantidad());

			tvDet2PrecioLista.setText(VARIABLES.getStringFormaterThreeDecimal(pedido_detalle2.getPrecio_lista()));
			tvDet2PrecioUnit.setText(VARIABLES.getStringFormaterThreeDecimal(pedido_detalle2.getPrecio_unit()));
			tvDet2DsctPctj.setText(""+pedido_detalle2.getPctj_desc()+"% + "+pedido_detalle2.getPctj_extra()+"%");
			tvDet2PkDolar.setText(VARIABLES.getStringFormaterThreeDecimal(getPrecioKilo_str(pedido_detalle2.getPrecio_neto(), pedido_detalle2.getPeso_total(), valor_cambio)));
			tvDet2SubTotal.setText(VARIABLES.getStringFormaterThreeDecimal(pedido_detalle2.getPrecio_neto()));



			tvMensajeValidadoStock.setVisibility(View.GONE);
			linearLayoutPromoComboContainer.addView(pedido2View);
		}
	}
}
