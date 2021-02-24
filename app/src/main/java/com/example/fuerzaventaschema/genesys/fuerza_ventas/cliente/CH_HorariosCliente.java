package com.example.fuerzaventaschema.genesys.fuerza_ventas.cliente;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.fuerzaventaschema.R;

public class CH_HorariosCliente extends Activity {
	final static int MENU_AGREGAR = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ch_activity_horario);
		
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
		menu.add(Menu.NONE, MENU_AGREGAR, Menu.NONE, "MenuAgregar").setIcon(R.drawable.icon_add_user_48).setTitle("Asignar horario").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return true;		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case MENU_AGREGAR:
			Intent intent = new Intent(CH_HorariosCliente.this,CH_AsignarHorario.class);
			startActivity(intent);
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}
