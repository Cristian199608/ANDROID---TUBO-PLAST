package com.example.sm_tubo_plast.genesys.adapters;

import android.app.Activity;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.DAO.DAO_RegistroBonificaciones;
import com.example.sm_tubo_plast.genesys.datatypes.DB_Pedido_Detalle_Promocion;
import com.example.sm_tubo_plast.genesys.util.GlobalFunctions;
import com.google.gson.Gson;

import java.util.ArrayList;

public class Adapter_Bonificacion_Colores extends BaseAdapter{

	protected Activity activity;
	//ArrayList<String> listaColores= new ArrayList<String>();
	ArrayList<DB_Pedido_Detalle_Promocion> listaColores= new ArrayList<DB_Pedido_Detalle_Promocion>();
	private String[] arrTemp;
	int cont=0;
	DB_Pedido_Detalle_Promocion detallepromo;
	private String oc_numero;
	private String cip;
	private String secuencia;
	private int cantidadTotal;
	ArrayList<Integer> poscolor;
	ArrayList<Integer> posvacio;
	ArrayList<Integer> contador;
	DAO_RegistroBonificaciones promo;
	ArrayList<DB_Pedido_Detalle_Promocion> arreglodetpromo;
	int u=0;

	Gson json= new Gson();
	
	public Adapter_Bonificacion_Colores(Activity activity,ArrayList<DB_Pedido_Detalle_Promocion> listaColores,
			String oc_numero, String cip, String secuencia, int cantidadTotal,
			ArrayList<DB_Pedido_Detalle_Promocion> arreglodetpromo){
		
		
		this.activity = activity;
		this.listaColores=listaColores;
		this.oc_numero=oc_numero;
		this.cip=cip;
		this.secuencia=secuencia;
		this.cantidadTotal=cantidadTotal;
		poscolor= new ArrayList<Integer>();
		posvacio= new ArrayList<Integer>();
		contador= new ArrayList<Integer>();
		//arraycont =new ArrayList<Contador>();
		arrTemp = new String[listaColores.size()];
		this.arreglodetpromo=arreglodetpromo;
		promo=new DAO_RegistroBonificaciones(activity);	
		
		for(int i=0;i<listaColores.size();i++){
			cont=listaColores.get(i).getCantidad()+cont;
		}
		
	}
	
	@Override
	public int getCount() {
		return listaColores.size();
	}

	@Override
	public Object getItem(int position) {
		return listaColores.get(position);
	}

	@Override
	public long getItemId(int position) {

		return Integer.parseInt(listaColores.get(position).getCc_artic());
		
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View item = convertView;
		final ViewHolder holder;

		//if (item == null) {
			
			holder = new ViewHolder();
			LayoutInflater inflater = activity.getLayoutInflater();
			item = inflater.inflate(R.layout.item_bonificacion_colores, null);
			holder.txtProducto = (TextView) item.findViewById(R.id.tv_bonificacionColoresProducto);
			holder.etCantidad = (EditText) item.findViewById(R.id.et_bonificacionColoresCantidad);
			holder.btOK=(Button)item.findViewById(R.id.bt_bonificacionColores);
				
			item.setTag(holder);

		//} else {
			//holder = (ViewHolder) item.getTag();
			
		//}
			
		holder.ref=position;
	
		holder.txtProducto.setText(listaColores.get(position).getDescripcion());
		holder.txtProducto.setHint(listaColores.get(position).getCc_artic());
		holder.etCantidad.setText(listaColores.get(position).getCantidad()+"");
	

		if(Integer.parseInt(holder.etCantidad.getText().toString())>0) {
			holder.etCantidad.setBackgroundResource(R.color.green_300);
			holder.btOK.setEnabled(false);
			
		}
		else holder.etCantidad.setBackgroundResource(R.color.blue_700);
		
				
		holder.etCantidad.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
				if(u==0){
				int cant;
				if(s.equals("")) cant=0;
				else{
					cant=Integer.parseInt(s.toString());	
				}
				cont=cont-cant;
				Log.w("CANTIDAD ACUMULADA TV",""+cont+"START: "+start+ "COUNT"+count+"AFTER"+after);
				}
				u=1;
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			
					holder.etCantidad.setBackgroundResource(R.color.blue_700);
					holder.btOK.setEnabled(true);

			}
		});
		
		holder.etCantidad.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {	
				holder.etCantidad.setText("");
			}
		});
		
	

		holder.btOK.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String st=holder.etCantidad.getText().toString();
				int cantidad;
		
				if(st.isEmpty()||st.equalsIgnoreCase("0")){
					cantidad=0;
					Toast.makeText(activity, "REGISTRE DATOS MAYOR A 0 O PRECIONAR EL BOTON BORRAR DATOS Y VUELVA A INGRESAR", Toast.LENGTH_SHORT).show();
				}
				else{
					
					cantidad=Integer.parseInt(st);
				}	
					if(cont+cantidad>cantidadTotal){
						GlobalFunctions.showCustomToastShort(activity, "Solo le quedan "+ (cantidadTotal-cont)+" Productos", GlobalFunctions.TOAST_ERROR, GlobalFunctions.POSICION_BOTTOM);

						//Toast.makeText(activity, "Solo le quedan "+ (cantidadTotal-cont)+" Productos", Toast.LENGTH_SHORT).show();
					}
					else{
						
						//Log.w("LISTA COLORES X: ",json.toJson(listaColores));
						
						int j=1,h=1;
						detallepromo= new DB_Pedido_Detalle_Promocion();

						detallepromo.setOc_numero(oc_numero);
						detallepromo.setCip(cip);
						detallepromo.setCc_artic(holder.txtProducto.getHint().toString()+"");
						detallepromo.setDescripcion(holder.txtProducto.getText().toString());
						detallepromo.setSecuencia(secuencia);
						detallepromo.setCantidad(cantidad);
							
						for(int i=0;i<arreglodetpromo.size();i++){
							if(arreglodetpromo.get(i).getDescripcion().equals(holder.txtProducto.getText().toString())){
								Log.w("SE REPITE ",""+holder.txtProducto.getText().toString());
								arreglodetpromo.set(i, detallepromo);
								h=0;
							}
						}
						
						for(int i=0;i<listaColores.size();i++){
							if(listaColores.get(i).getDescripcion().equals(holder.txtProducto.getText().toString())){
								listaColores.set(i, detallepromo);
								j=0;
							}
						}
						
						if(h==1){
							arreglodetpromo.add(detallepromo);						
						}
						
						if(j==1){

							listaColores.add(detallepromo);
						}
						
						cont=0;
						for(int i=0;i<listaColores.size();i++){
							cont=listaColores.get(i).getCantidad()+cont;
						}
		
						Log.w("CANTIDAD ACUMULADA",""+cont);
						
						final Toast toast = Toast.makeText(activity, "TOTAL: "+cont, Toast.LENGTH_SHORT);
					    toast.show();

					    Handler handler = new Handler();
					        handler.postDelayed(new Runnable() {
					           @Override
					           public void run() {
					               toast.cancel(); 
					           }
					    }, 1000);
					        
						if(cantidad>0){
							holder.etCantidad.setBackgroundResource(R.color.green_300);
							holder.btOK.setEnabled(false);
						
						}
						Log.w("FILA AGREGADA AL ARRAY",json.toJson(arreglodetpromo));
						u=0;	
						}
				}					

		});
		
		return item;
	}
	
	public class ViewHolder {
		
		TextView txtProducto;
		EditText etCantidad;
		Button btOK;
		int ref;	
		
	}
}

