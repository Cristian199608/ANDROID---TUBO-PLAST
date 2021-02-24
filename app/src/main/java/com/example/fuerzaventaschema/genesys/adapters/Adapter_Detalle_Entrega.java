package com.example.fuerzaventaschema.genesys.adapters;

import java.util.ArrayList;
import java.util.Calendar;

import com.example.fuerzaventaschema.R;
import com.example.fuerzaventaschema.genesys.datatypes.DB_Detalle_Entrega;
import com.google.gson.Gson;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

public class Adapter_Detalle_Entrega extends BaseAdapter {
	Gson json= new Gson();
	ArrayList<DB_Detalle_Entrega> arreglo;
	Activity activity;
	String[] listurnos= new String[]{"Mañana","Tarde"};
	int cantidadTotal;
	DB_Detalle_Entrega fila_detalle;
	private String oc_numero;
	private String cip;
	ArrayAdapter<String> adp;
	int cont;
	
	public Adapter_Detalle_Entrega(ArrayList<DB_Detalle_Entrega> arreglo, Activity activity,int cantidadTotal,
			String oc_numero, String cip) {
		
		this.arreglo = arreglo;
		this.activity=activity;
		this.cantidadTotal=cantidadTotal;
		this.oc_numero=oc_numero;
		this.cip=cip;
		
		adp= new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1,listurnos);

		cont=0;
		for(int i=0;i<arreglo.size();i++){
			cont=arreglo.get(i).getCantidad()+cont;
		}

	}

	@Override
	public int getCount() {
		return arreglo.size();
	}

	@Override
	public Object getItem(int position) {
		return arreglo.get(position);
	}

	@Override
	public long getItemId(int position) {
		return arreglo.get(position).getNroEntrega();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View item = convertView;
		final ViewHolder holder;

		//if (item == null) {
		
		holder = new ViewHolder();
		LayoutInflater inflater = activity.getLayoutInflater();
		item = inflater.inflate(R.layout.item_cant_entregas2, null);

		holder.etFecha = (EditText) item.findViewById(R.id.edt_fechaPedidoEntN);
		holder.spTurno=(Spinner)item.findViewById(R.id.sp_TurnoPedidoEntN);
		holder.etCantidad=(EditText)item.findViewById(R.id.edt_CantPedidoEntN);
		holder.btOK=(Button)item.findViewById(R.id.bt_EntregaOK);
		holder.llCant=(LinearLayout)item.findViewById(R.id.item_cant_LL4);
		holder.etCantTotal=(EditText)item.findViewById(R.id.edt_itemCantTotal);
		holder.etCantIng=(EditText)item.findViewById(R.id.edt_itemCantIng);
		holder.etCantFalt=(EditText)item.findViewById(R.id.edt_itemCantFalt);
		
		holder.ref=position;
		item.setTag(holder);
		
		/*} else {
		holder = (ViewHolder) item.getTag();
		
		}*/
		holder.spTurno.setAdapter(adp);
		
		if(position==arreglo.size()-1){
			holder.llCant.setVisibility(View.VISIBLE);

			holder.etCantTotal.setText(cantidadTotal+"");
			holder.etCantIng.setText(cont+"");
			holder.etCantFalt.setText(cantidadTotal-cont+"");
		}
		
		
		//si el arreglo esta "vacio"
		if(arreglo.get(position).getDt_fechaEntrega().equals("")){
				
			holder.etCantidad.setText(""+cantidadTotal/arreglo.size());
			holder.etFecha.setHint("FECHA "+(position+1));
			holder.etFecha.setBackgroundResource(R.color.blue_300);
			holder.etCantidad.setBackgroundResource(R.color.blue_300);
			
			if(holder.ref==arreglo.size()-1) {
				int d=(cantidadTotal/arreglo.size())*arreglo.size();
				int d2=cantidadTotal-d;	
				holder.etCantidad.setText(cantidadTotal/arreglo.size()+d2+"");
				
			}
		}
		
		//si el arreglo ya fue llenado anteriormente
		else{
			holder.etFecha.setText(arreglo.get(position).getDt_fechaEntrega());	
			holder.etCantidad.setText(arreglo.get(position).getCantidad()+"");
			
			if(arreglo.get(position).getCodTurno().equals("01")||arreglo.get(position).getCodTurno().equals("Ma�ana")){
				holder.spTurno.setSelection(0);
			}
			else {
				holder.spTurno.setSelection(1);
			}
			holder.etFecha.setHint("FECHA "+(position+1));			
			holder.etFecha.setBackgroundResource(R.color.green_300);
			holder.etCantidad.setBackgroundResource(R.color.green_300);
			holder.btOK.setEnabled(false);
		}

		//AGREGAR LA FECHA
		holder.etFecha.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				agregarFecha(holder.etFecha, holder);	
			}
		});
		
		
		holder.etCantidad.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				holder.btOK.setEnabled(true);
				holder.etCantidad.setBackgroundResource(R.color.blue_300);
			}
		});
		
		//AGREGAR FILA AL ARRAY
		
		holder.btOK.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(holder.etFecha.getText().toString().equals("")){
					holder.etFecha.setBackgroundResource(R.color.red_300);				
				}
				else{
					
					int j=1,h=1;
					
					fila_detalle= new DB_Detalle_Entrega();
					fila_detalle.setOc_numero(oc_numero);
					fila_detalle.setCip(cip);
					fila_detalle.setNroEntrega(holder.ref+1);
					fila_detalle.setDt_fechaEntrega(holder.etFecha.getText().toString());
					fila_detalle.setCantidad(Integer.parseInt(holder.etCantidad.getText().toString()));
					fila_detalle.setCodTurno(holder.spTurno.getSelectedItem().toString());
					fila_detalle.setEstado("P");
					
				
					/*for(int i=0;i<arreglo_detalle.size();i++){
						
						if(arreglo_detalle.get(i).getNroEntrega()-1==holder.ref){
							Log.w("SE","REPITE");						
							arreglo_detalle.set(i, fila_detalle);
							j=0;
							Log.w("ARREGLO",json.toJson(arreglo_detalle));
						}
					}
					
					if(j==1){
						Log.w("FILA AGREGADA",json.toJson(fila_detalle));
						arreglo_detalle.add(fila_detalle);
						Log.w("ARREGLO NUEVO",json.toJson(arreglo_detalle));
					}
					*/
					
					for(int i=0;i<arreglo.size();i++){	
						if(arreglo.get(i).getNroEntrega()-1==holder.ref){
							arreglo.set(i, fila_detalle);
							h=0;
							Log.w("ARREGLO TOTAL",json.toJson(arreglo));
						}
					}
					
					if(h==1){
						arreglo.add(fila_detalle);
					}
					
					/*Log.w("ARREGLO",json.toJson(arreglo));
					arreglo_detalle=arreglo;
					Log.w("ARREGLO DETALLE",json.toJson(arreglo_detalle));*/
					
					holder.etFecha.setBackgroundResource(R.color.green_300);
					holder.etCantidad.setBackgroundResource(R.color.green_300);
					holder.btOK.setEnabled(false);
					
					cont=0;
					for(int i=0;i<arreglo.size();i++){
						cont=arreglo.get(i).getCantidad()+cont;
					}
					Log.w("CANT ING",""+cont);
					
					notifyDataSetChanged();
				}			
			}
		});
		
		return item;
	}

	
	public class ViewHolder {

		EditText etFecha;
		Spinner spTurno;
		EditText etCantidad;
		Button btOK;
		int ref;
		LinearLayout llCant;
		EditText etCantTotal;
		EditText etCantIng;
		EditText etCantFalt;
	}
	
	public void agregarFecha(final EditText ef, final ViewHolder holder){
		final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Launch Date Picker Dialog
        DatePickerDialog dpd = new DatePickerDialog(activity, 
        		new DatePickerDialog.OnDateSetListener() {							
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
						int monthOfYear_true = monthOfYear +1 ;
						String pickerMonth=String.valueOf(monthOfYear_true),pickerDay=String.valueOf(dayOfMonth);
						if (dayOfMonth<10) {
							pickerDay = "0"+dayOfMonth;
						}
						if (monthOfYear_true<10) {
							pickerMonth = "0"+(monthOfYear_true);
						}
						holder.etFecha.setText(pickerDay+"/"+pickerMonth+"/"+year);
						holder.etFecha.setBackgroundResource(R.color.blue_300);
						holder.btOK.setEnabled(true);
	
					}
				}, year, month, day+1);
        dpd.show();
	}
	
}
