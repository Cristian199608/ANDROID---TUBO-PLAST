package com.example.fuerzaventaschema.genesys.DAO;

import java.util.ArrayList;

import com.example.fuerzaventaschema.genesys.BEAN.FichaCliente;
import com.example.fuerzaventaschema.genesys.datatypes.DBtables;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

@SuppressLint("LongLogTag")
public class DAO_RegistroCliente extends SQLiteAssetHelper {
	public static final String TAG = "DAO_RegistroCliente";
    public static final String DATABASE_NAME = "fuerzaventas";
    private static final int DATABASE_VERSION = 1;
    
        
    Context context;

    public DAO_RegistroCliente(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }
    

	public boolean guardarFichaCliente(FichaCliente fichaCliente) {
		try {
			SQLiteDatabase db = getWritableDatabase();

			ContentValues reg = new ContentValues();
			reg.put(DBtables.FichaCliente.NROANALISIS, fichaCliente.getNroAnalisis());
			reg.put(DBtables.FichaCliente.TIPO, fichaCliente.getTipo());
			reg.put(DBtables.FichaCliente.RAZON_SOCIAL, fichaCliente.getRazonSocial());
			reg.put(DBtables.FichaCliente.NOMBRE_COMERCIAL, fichaCliente.getNombreComercial());
			reg.put(DBtables.FichaCliente.GIRO_NEGOCIO, fichaCliente.getGiroNegocio());
			reg.put(DBtables.FichaCliente.DIRECCION_FISCAL, fichaCliente.getDireccionFiscal());
			reg.put(DBtables.FichaCliente.EMAIL_FACTELEC, fichaCliente.getEmailFactElec());
			reg.put(DBtables.FichaCliente.EMAIL, fichaCliente.getEmail());
			reg.put(DBtables.FichaCliente.TELEFONO1, fichaCliente.getTelefono1());
			reg.put(DBtables.FichaCliente.TELEFONO2, fichaCliente.getTelefono2());
			reg.put(DBtables.FichaCliente.TELEFONO3, fichaCliente.getTelefono3());
			reg.put(DBtables.FichaCliente.IMPRESION_ELEC, fichaCliente.getImpresionElectronica());
			reg.put(DBtables.FichaCliente.APERCEPTOR, fichaCliente.getaPerceptor());
			reg.put(DBtables.FichaCliente.ARETENEDOR, fichaCliente.getaRetenedor());
			reg.put(DBtables.FichaCliente.CATEGORIA, fichaCliente.getCategoria());
			reg.put(DBtables.FichaCliente.PACK_PEGAMENTO, fichaCliente.getPackPegamento());
			reg.put(DBtables.FichaCliente.FCONSTITUCION, fichaCliente.getfConstitucion());
			reg.put(DBtables.FichaCliente.FANIVERSARIO, fichaCliente.getfAnimersario());
			reg.put(DBtables.FichaCliente.FREGISTRO, fichaCliente.getfRegistro());
			reg.put(DBtables.FichaCliente.LIC_MUNICIPAL, fichaCliente.getLicMunicipal());
			reg.put(DBtables.FichaCliente.TIPO_SECTOR, fichaCliente.getTipoSector());
			reg.put(DBtables.FichaCliente.IBC, fichaCliente.getIBC());
			reg.put(DBtables.FichaCliente.UNIDAD_NEGOCIO, fichaCliente.getUnidadNegocio());
			reg.put(DBtables.FichaCliente.DESCUENTO, fichaCliente.getDescuento());
			reg.put(DBtables.FichaCliente.SECTOR, fichaCliente.getSector());
			reg.put(DBtables.FichaCliente.PAIS, fichaCliente.getPais());
			reg.put(DBtables.FichaCliente.CIIU, fichaCliente.getCIIU());
			reg.put(DBtables.FichaCliente.NRUC_EXTERIOR, fichaCliente.getnRucExterior());
			reg.put(DBtables.FichaCliente.TIPOLOGIA, fichaCliente.getTipologia());
			reg.put(DBtables.FichaCliente.PROCEDENCIA, fichaCliente.getProcedencia());
			reg.put(DBtables.FichaCliente.LOCAL, fichaCliente.getLocal());
			reg.put(DBtables.FichaCliente.VINCULADA, fichaCliente.getVinculada());
			reg.put(DBtables.FichaCliente.MUESTRARIO, fichaCliente.getMuestrario());
			reg.put(DBtables.FichaCliente.INFOCORP, fichaCliente.getInfocorp());
			reg.put(DBtables.FichaCliente.MONEDA_FACTURACION, fichaCliente.getMonedaFacturacion());
			reg.put(DBtables.FichaCliente.MONEDA_LIMITE_CREDITO, fichaCliente.getMonedaLimiteCredito());
			reg.put(DBtables.FichaCliente.LIMITE_CREDITO, fichaCliente.getLimiteCredito());
			reg.put(DBtables.FichaCliente.DIAS_PLAZO, fichaCliente.getDiasPlazo());
			reg.put(DBtables.FichaCliente.TIENE_SUCURSALES, fichaCliente.getTieneSucursales());
			reg.put(DBtables.FichaCliente.ESTADO, fichaCliente.getEstado());
						
			db.insert(DBtables.FichaCliente.TAG, null, reg);
			db.close();			
			Log.i(TAG+":guardarFichaCliente:", "FichaCliente guardada");
			return true;
		} catch (SQLException ex) {
			ex.printStackTrace();
			return false;			
		}
	}
    
    public int getMaxNumeroCliente() {
        String rawQuery = "SELECT MAX(nroAnalisis) FROM fichaCliente";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur = db.rawQuery(rawQuery, null);
        cur.moveToFirst();
        
        int numero = 0;
        if (cur.moveToFirst()) {
            do {
            	numero = cur.getInt(0);            	
            } while (cur.moveToNext());
        }
        
        cur.close();
        db.close();
        return numero;
    }
    
    public boolean estaRegistrado(String numero) {
        String rawQuery = "SELECT nroAnalisis FROM fichaCliente where nroAnalisis='"+numero+"'";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur = db.rawQuery(rawQuery, null);
        cur.moveToFirst();
        
        boolean flag = false;
        if (cur.moveToFirst()) {
            do {
            	flag = true;
            } while (cur.moveToNext());
        }
        
        cur.close();
        db.close();
        return flag;
    }
    
   
}
