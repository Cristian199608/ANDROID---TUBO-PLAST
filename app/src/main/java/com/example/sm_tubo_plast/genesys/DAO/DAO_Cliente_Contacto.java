package com.example.sm_tubo_plast.genesys.DAO;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.sm_tubo_plast.genesys.BEAN.Cliente_Contacto;
import com.example.sm_tubo_plast.genesys.datatypes.DBclasses;
import com.example.sm_tubo_plast.genesys.datatypes.DBtables;

import java.util.ArrayList;

public class DAO_Cliente_Contacto {

    public static final String TAG = "DAO_Cliente_Contacto";
    public static final String TABLA_CLEINTE_CONTACTO = DBtables.CLiente_Contacto.TAG;


    public ArrayList<Cliente_Contacto> getClientesPendientesAll(DBclasses dBclasses) {
        String rawQuery = "SELECT cc.codcli,cc.id_contacto,cc.nombre_contacto,cc.dni,cc.telefono,cc.celular,cc.email,cc.estado,cc.flag " +
                "FROM "+TABLA_CLEINTE_CONTACTO+" cc inner join cliente c on cc.codcli=c.codcli " +
                " WHERE  cc.flag='P' ";
        Log.i(TAG, rawQuery);

        SQLiteDatabase db = dBclasses.getReadableDatabase();
        Cursor cursor = db.rawQuery(rawQuery, null);

        ArrayList<Cliente_Contacto> lista=new ArrayList<>();

        while (cursor.moveToNext()){
            Cliente_Contacto contacto=new Cliente_Contacto();
            contacto.setCodcli(cursor.getString(cursor.getColumnIndex("codcli")));
            contacto.setId_contacto(cursor.getInt(cursor.getColumnIndex("id_contacto")));
            contacto.setNombre_contacto(cursor.getString(cursor.getColumnIndex("nombre_contacto")));
            contacto.setDni(cursor.getString(cursor.getColumnIndex("dni")));
            contacto.setTelefono(cursor.getString(cursor.getColumnIndex("telefono")));
            contacto.setCelular(cursor.getString(cursor.getColumnIndex("celular")));
            contacto.setEmail(cursor.getString(cursor.getColumnIndex("email")));
            contacto.setEstado(cursor.getString(cursor.getColumnIndex("estado")));
            contacto.setFlag(cursor.getString(cursor.getColumnIndex("flag")));
            lista.add(contacto);
        }
        cursor.close();
        db.close();
        return lista;
    }



    public ArrayList<Cliente_Contacto> getClienteContactoByID(DBclasses dBclasses, String codigoCliente, int idcontacto) {
        String rawQuery = "SELECT cc.codcli,cc.id_contacto,cc.nombre_contacto,cc.dni,cc.telefono,cc.celular,cc.email,cc.estado,cc.flag, ifnull(cargo, '') as cargo " +
                "FROM "+TABLA_CLEINTE_CONTACTO+" cc inner join cliente c on cc.codcli=c.codcli " +
                " WHERE cc.codcli like '"+codigoCliente+"' and (cc.id_contacto = "+idcontacto+" or 0 ="+idcontacto+" ) order by cc.nombre_contacto asc";
        Log.i(TAG, rawQuery);

        SQLiteDatabase db = dBclasses.getReadableDatabase();
        Cursor cursor = db.rawQuery(rawQuery, null);

        ArrayList<Cliente_Contacto> lista=new ArrayList<>();

       while (cursor.moveToNext()){
           Cliente_Contacto contacto=new Cliente_Contacto();
           contacto.setCodcli(cursor.getString(cursor.getColumnIndex("codcli")));
           contacto.setId_contacto(cursor.getInt(cursor.getColumnIndex("id_contacto")));
           contacto.setNombre_contacto(cursor.getString(cursor.getColumnIndex("nombre_contacto")));
           contacto.setDni(cursor.getString(cursor.getColumnIndex("dni")));
           contacto.setTelefono(cursor.getString(cursor.getColumnIndex("telefono")));
           contacto.setCelular(cursor.getString(cursor.getColumnIndex("celular")));
           contacto.setEmail(cursor.getString(cursor.getColumnIndex("email")));
           contacto.setCargo(cursor.getString(cursor.getColumnIndex("cargo")));
           contacto.setEstado(cursor.getString(cursor.getColumnIndex("estado")));
           contacto.setFlag(cursor.getString(cursor.getColumnIndex("flag")));
           lista.add(contacto);
       }
        cursor.close();
        db.close();
        return lista;
    }

    public int getNextIdClienteContacto(DBclasses dBclasses, String codigoCliente) {
        String rawQuery = "SELECT ifnull(max(cc.id_contacto), 0)+1 " +
                "FROM "+TABLA_CLEINTE_CONTACTO+" cc  " +" WHERE cc.codcli like '"+codigoCliente+"'";
        Log.i(TAG, rawQuery);

        SQLiteDatabase db = dBclasses.getReadableDatabase();
        Cursor cursor = db.rawQuery(rawQuery, null);

        int nextId=1;
        while (cursor.moveToNext()){
            nextId=cursor.getInt(0);
        }
        cursor.close();
        db.close();

        return nextId;
    }

    public boolean getClienteContactoByDNI(DBclasses dBclasses, String codigoCliente, String dni_contacto) {
        if (dni_contacto.length()==0){
            return false;
        }
        String rawQuery = "SELECT *  " +
                "FROM "+TABLA_CLEINTE_CONTACTO+" cc  " +" WHERE cc.codcli = '"+codigoCliente+"' and cc.dni ='"+dni_contacto+"'";
        Log.i(TAG, rawQuery);

        SQLiteDatabase db = dBclasses.getReadableDatabase();
        Cursor cursor = db.rawQuery(rawQuery, null);

        int nextId=cursor.getCount();

        cursor.close();
        db.close();

        return nextId>0;
    }


    public boolean Crear_Contacto(DBclasses dBclasses, Cliente_Contacto contacto) {

        try{
            ContentValues nre=new ContentValues();
            nre.put(DBtables.CLiente_Contacto.codcli, contacto.getCodcli());
            nre.put(DBtables.CLiente_Contacto.id_contacto, contacto.getId_contacto());
            nre.put(DBtables.CLiente_Contacto.nombre_contacto, contacto.getNombre_contacto());
            nre.put(DBtables.CLiente_Contacto.dni, contacto.getDni());
            nre.put(DBtables.CLiente_Contacto.telefono, contacto.getTelefono());
            nre.put(DBtables.CLiente_Contacto.celular, contacto.getCelular());
            nre.put(DBtables.CLiente_Contacto.email, contacto.getEmail());
            nre.put(DBtables.CLiente_Contacto.cargo, contacto.getCargo());
            nre.put(DBtables.CLiente_Contacto.estado, contacto.getEstado());
            nre.put(DBtables.CLiente_Contacto.flag, contacto.getFlag());

            SQLiteDatabase db = dBclasses.getReadableDatabase();
            db.insert(DBtables.CLiente_Contacto.TAG, null, nre);
            db.close();

            return  true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizar_cliente_contacto_flag(DBclasses dBclasses, String codcli, String dni, String flag) {

        try{
            String where = "codcli = ? and dni = ?";
            String[] args = { codcli, dni };

            ContentValues nre=new ContentValues();
            nre.put(DBtables.CLiente_Contacto.flag, flag);

            SQLiteDatabase db = dBclasses.getReadableDatabase();
            db.update(DBtables.CLiente_Contacto.TAG,  nre, where, args);
            db.close();

            return  true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

}
