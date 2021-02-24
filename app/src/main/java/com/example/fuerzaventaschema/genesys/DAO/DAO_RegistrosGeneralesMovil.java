package com.example.fuerzaventaschema.genesys.DAO;

import java.util.ArrayList;

import com.example.fuerzaventaschema.genesys.BEAN.Almacen;
import com.example.fuerzaventaschema.genesys.BEAN.FormaPago;
import com.example.fuerzaventaschema.genesys.BEAN.LugarEntrega;
import com.example.fuerzaventaschema.genesys.BEAN.Nro_Letras;
import com.example.fuerzaventaschema.genesys.BEAN.RegistroGeneralMovil;
import com.example.fuerzaventaschema.genesys.BEAN.Turno;
import com.example.fuerzaventaschema.genesys.datatypes.DB_Turno;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DAO_RegistrosGeneralesMovil extends SQLiteAssetHelper {
	public static final String TAG = "DAO_Configuracion";
    public static final String DATABASE_NAME = "fuerzaventas";
    private static final int DATABASE_VERSION = 1;
    
    private static final String CONDICION_VENTA = "CV";
    private static final String FORMA_PAGO = "FP";
    
    Context context;

    public DAO_RegistrosGeneralesMovil(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }
    
    public ArrayList<RegistroGeneralMovil> getPrioridades() {
        String rawQuery = "SELECT * FROM registrosGeneralesMovil WHERE descripcion='PRIORIDAD'";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur = db.rawQuery(rawQuery, null);
        cur.moveToFirst();
        ArrayList<RegistroGeneralMovil> lista = new ArrayList<>();
        if (cur.moveToFirst()) {
            do {
            	RegistroGeneralMovil item = new RegistroGeneralMovil();
            	item.setCodDescripcion(cur.getString(0));
            	item.setDescripcion(cur.getString(1));
            	item.setCodValor(cur.getString(2));
            	item.setValor(cur.getString(3));
            	lista.add(item);
            } while (cur.moveToNext());
        }
        cur.close();
        db.close();
        return lista;
    }
    
    public ArrayList<RegistroGeneralMovil> getSucursal() {
        String rawQuery = "SELECT * FROM registrosGeneralesMovil WHERE descripcion='SUCURSAL'";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur = db.rawQuery(rawQuery, null);
        cur.moveToFirst();
        ArrayList<RegistroGeneralMovil> lista = new ArrayList<>();
        if (cur.moveToFirst()) {
            do {
            	RegistroGeneralMovil item = new RegistroGeneralMovil();
            	item.setCodDescripcion(cur.getString(0));
            	item.setDescripcion(cur.getString(1));
            	item.setCodValor(cur.getString(2));
            	item.setValor(cur.getString(3));
            	lista.add(item);
            } while (cur.moveToNext());
        }
        cur.close();
        db.close();
        return lista;
    }
    
    public ArrayList<LugarEntrega> getPuntoEntrega() {
        String rawQuery = "SELECT * FROM lugarEntrega";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur = db.rawQuery(rawQuery, null);
        cur.moveToFirst();
        ArrayList<LugarEntrega> lista = new ArrayList<>();
        if (cur.moveToFirst()) {
            do {
            	LugarEntrega item = new LugarEntrega();
            	item.setCodigoCliente(cur.getString(0));
            	item.setItemSucursal(cur.getString(1));
            	item.setCodigoLugar(cur.getString(2));
            	item.setDireccion(cur.getString(3));
            	item.setIndicadorDespacho(cur.getString(4));
            	item.setIndicadorCobranza(cur.getString(5));
            	item.setDireccionEntrega(cur.getString(6));            	
            	lista.add(item);
            } while (cur.moveToNext());
        }
        cur.close();
        db.close();
        return lista;
    }
    
    public ArrayList<RegistroGeneralMovil> getTipoDespacho() {
        String rawQuery = "SELECT * FROM registrosGeneralesMovil WHERE descripcion='TIPODESPACHO'";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur = db.rawQuery(rawQuery, null);
        cur.moveToFirst();
        ArrayList<RegistroGeneralMovil> lista = new ArrayList<>();
        if (cur.moveToFirst()) {
            do {
            	RegistroGeneralMovil item = new RegistroGeneralMovil();
            	item.setCodDescripcion(cur.getString(0));
            	item.setDescripcion(cur.getString(1));
            	item.setCodValor(cur.getString(2));
            	item.setValor(cur.getString(3));
            	lista.add(item);
            } while (cur.moveToNext());
        }
        cur.close();
        db.close();
        return lista;
    }
   
    public ArrayList<Almacen> getAlmacenes() {
        String rawQuery = "SELECT * FROM almacenes";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur = db.rawQuery(rawQuery, null);
        cur.moveToFirst();
        ArrayList<Almacen> lista = new ArrayList<>();
        if (cur.moveToFirst()) {
            do {
            	Almacen item = new Almacen();
            	item.setCodigoAlmacen(cur.getString(0));
            	item.setDescripcion(cur.getString(1));
            	item.setIdLocal(cur.getString(2));
            	item.setDireccion(cur.getString(3));
            	lista.add(item);
            } while (cur.moveToNext());
        }
        cur.close();
        db.close();
        return lista;
    }
    
    public ArrayList<Turno> getTurnos() {
        String rawQuery = "SELECT * FROM turno";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur = db.rawQuery(rawQuery, null);
        cur.moveToFirst();
        ArrayList<Turno> lista = new ArrayList<>();
        if (cur.moveToFirst()) {
            do {
            	Turno item = new Turno();
            	item.setCodTurno(cur.getString(0));
            	item.setTurno(cur.getString(1));
            	lista.add(item);
            } while (cur.moveToNext());
        }
        cur.close();
        db.close();
        return lista;
    }
    
    public ArrayList<FormaPago> getFormasPago(){		
		String rawQuery;		
		rawQuery = "SELECT * from forma_pago";
		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		
		ArrayList<FormaPago> lista = new ArrayList<>();
		
		cur.moveToFirst();
		while (!cur.isAfterLast()) {
			FormaPago formaPago = new FormaPago();
			formaPago.setCodigoFormaPago(cur.getString(0));
			formaPago.setDescripcionFormaPago(cur.getString(1));
			lista.add(formaPago);
			cur.moveToNext();
		}
		Log.w(TAG,"getFormasPago:"+lista.size());
		cur.close();
		db.close();
		return lista;
	}
    
    public ArrayList<FormaPago> getCondicionVenta(String codigoCliente){
		String rawQuery;		
		//rawQuery = "SELECT * from forma_pago where flagTipo = '"+CONDICION_VENTA+"' and codigoCliente = '"+codigoCliente+"'";// CV -> CondicionVenta
		rawQuery = "SELECT * from forma_pago where flagTipo = '"+FORMA_PAGO+"' and codigoCliente = ''";// CV -> CondicionVenta
		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		
		ArrayList<FormaPago> lista = new ArrayList<>();
		
		cur.moveToFirst();
		while (!cur.isAfterLast()) {
			FormaPago formaPago = new FormaPago();
			formaPago.setCodigoFormaPago(cur.getString(0));
			formaPago.setDescripcionFormaPago(cur.getString(1));
			lista.add(formaPago);
			cur.moveToNext();
		}
		Log.w(TAG,"getFormasPago:"+lista.size());
		cur.close();
		db.close();
		return lista;
	}
    
    
    public ArrayList<Nro_Letras> getNroLetras(){
		String rawQuery;		
		rawQuery = "SELECT * from NRO_LETRAS ";// CV -> CondicionVenta
		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		
		ArrayList<Nro_Letras> lista = new ArrayList<>();
		
		cur.moveToFirst();
		while (!cur.isAfterLast()) {
			Nro_Letras nroletras = new Nro_Letras();
			nroletras.setNumeroLetras(cur.getString(0));
			nroletras.setDescripcionNroLetras(cur.getString(1));
			lista.add(nroletras);
			cur.moveToNext();
		}
		Log.w(TAG,"getlistaNro_Letras:"+lista.size());
		cur.close();
		db.close();
		return lista;
	}
    
    public ArrayList<String> getMoneda(){
		String rawQuery = "SELECT codigoMoneda from moneda where descripcion in('nuevos soles','dolares americanos')";
		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		
		ArrayList<String> lista = new ArrayList<>();
		
		cur.moveToFirst();
		while (!cur.isAfterLast()) {
			lista.add(cur.getString(0));			
			cur.moveToNext();
		}
		
		Log.w(TAG,"getMoneda:"+lista.size());
		cur.close();
		db.close();
		return lista;
	}
    
    public ArrayList<RegistroGeneralMovil> getPrioridadDSC() {
        String rawQuery = "SELECT * FROM registrosGeneralesMovil WHERE descripcion='PRIORIDADSC'";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur = db.rawQuery(rawQuery, null);
        cur.moveToFirst();
        ArrayList<RegistroGeneralMovil> lista = new ArrayList<>();
        if (cur.moveToFirst()) {
            do {
            	RegistroGeneralMovil item = new RegistroGeneralMovil();
            	item.setCodDescripcion(cur.getString(0));
            	item.setDescripcion(cur.getString(1));
            	item.setCodValor(cur.getString(2));
            	item.setValor(cur.getString(3));
            	lista.add(item);
            } while (cur.moveToNext());
        }
        cur.close();
        db.close();
        return lista;
    }
    
    public ArrayList<RegistroGeneralMovil> getGeneraCambio() {
        String rawQuery = "SELECT * FROM registrosGeneralesMovil WHERE descripcion='GENERACAMBIO'";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur = db.rawQuery(rawQuery, null);
        cur.moveToFirst();
        ArrayList<RegistroGeneralMovil> lista = new ArrayList<>();
        if (cur.moveToFirst()) {
            do {
            	RegistroGeneralMovil item = new RegistroGeneralMovil();
            	item.setCodDescripcion(cur.getString(0));
            	item.setDescripcion(cur.getString(1));
            	item.setCodValor(cur.getString(2));
            	item.setValor(cur.getString(3));
            	lista.add(item);
            } while (cur.moveToNext());
        }
        cur.close();
        db.close();
        return lista;
    }
    
    public ArrayList<RegistroGeneralMovil> getRecojo() {
        String rawQuery = "SELECT * FROM registrosGeneralesMovil WHERE descripcion='RECOJO'";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur = db.rawQuery(rawQuery, null);
        cur.moveToFirst();
        ArrayList<RegistroGeneralMovil> lista = new ArrayList<>();
        if (cur.moveToFirst()) {
            do {
            	RegistroGeneralMovil item = new RegistroGeneralMovil();
            	item.setCodDescripcion(cur.getString(0));
            	item.setDescripcion(cur.getString(1));
            	item.setCodValor(cur.getString(2));
            	item.setValor(cur.getString(3));
            	lista.add(item);
            	Log.w(TAG, "recojo: "+cur.getString(2)+" "+cur.getString(3));
            } while (cur.moveToNext());
        }
        cur.close();
        db.close();
        return lista;
    }
    
    public ArrayList<RegistroGeneralMovil> getEnvase() {
        String rawQuery = "SELECT * FROM registrosGeneralesMovil WHERE descripcion='ENVASE'";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur = db.rawQuery(rawQuery, null);
        cur.moveToFirst();
        ArrayList<RegistroGeneralMovil> lista = new ArrayList<>();
        if (cur.moveToFirst()) {
            do {
            	RegistroGeneralMovil item = new RegistroGeneralMovil();
            	item.setCodDescripcion(cur.getString(0));
            	item.setDescripcion(cur.getString(1));
            	item.setCodValor(cur.getString(2));
            	item.setValor(cur.getString(3));
            	lista.add(item);
            	Log.w(TAG, "getEnvase: "+cur.getString(2)+" "+cur.getString(3));
            } while (cur.moveToNext());
        }
        cur.close();
        db.close();
        return lista;
    }
    
    public ArrayList<RegistroGeneralMovil> getContenido() {
        String rawQuery = "SELECT * FROM registrosGeneralesMovil WHERE descripcion='CONTENIDO'";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur = db.rawQuery(rawQuery, null);
        cur.moveToFirst();
        ArrayList<RegistroGeneralMovil> lista = new ArrayList<>();
        if (cur.moveToFirst()) {
            do {
            	RegistroGeneralMovil item = new RegistroGeneralMovil();
            	item.setCodDescripcion(cur.getString(0));
            	item.setDescripcion(cur.getString(1));
            	item.setCodValor(cur.getString(2));
            	item.setValor(cur.getString(3));
            	lista.add(item);
            	Log.w(TAG, "getContenido: "+cur.getString(2)+" "+cur.getString(3));
            } while (cur.moveToNext());
        }
        cur.close();
        db.close();
        return lista;
    }
    
    public boolean getHabilitaDescuentoCondicion(String condicionVenta) {
        String rawQuery = "SELECT * FROM registrosGeneralesMovil WHERE descripcion='DESCUENTO' AND codigoValor='"+condicionVenta+"'";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur = db.rawQuery(rawQuery, null);
        cur.moveToFirst();
        boolean flag = false;
        if (cur.moveToFirst()) {
            do {
            	flag= true;
            } while (cur.moveToNext());
        }
        cur.close();
        db.close();
        return flag;
    }
  

}
