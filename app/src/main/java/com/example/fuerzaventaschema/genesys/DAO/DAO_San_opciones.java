package com.example.fuerzaventaschema.genesys.DAO;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.fuerzaventaschema.genesys.datatypes.DBclasses;
import com.example.fuerzaventaschema.genesys.datatypes.DBtables;

import java.util.ArrayList;

public class DAO_San_opciones {
    private static final String TAG = "DAO_San_opciones";
    public static ArrayList<String>  getListaOpcionesByInstancia(DBclasses dBclasses, String instacia) {
        String S_TAG="getListaOpcionesByInstancia(...) ";
        ArrayList<String> listaPerfilDecisor=new ArrayList<>();
        try {

            String SQL="select " +
                    "so."+ DBtables.San_Opciones.id_opcion+", "+
                    "so."+DBtables.San_Opciones.codigo_crm+", "+
                    "so."+DBtables.San_Opciones.instancia+", "+
                    "so."+DBtables.San_Opciones.opciones+" "+
                    "from "+DBtables.San_Opciones.TAG+" so "+
                    "where so."+DBtables.San_Opciones.instancia+" = '"+instacia+"' "+
                    "order by so."+DBtables.San_Opciones.opciones+" asc ";

            Log.i(TAG, S_TAG+" sql "+SQL);
            SQLiteDatabase dbREad=dBclasses.getReadableDatabase();
            Cursor cursor=dbREad.rawQuery(SQL, null);
            listaPerfilDecisor.add( "-Seleccione- ("+cursor.getCount()+")");
            while (cursor.moveToNext()){
                listaPerfilDecisor.add(cursor.getString(3));
            }
            cursor.close();
            dbREad.close();

        }catch (SQLException e){
            e.printStackTrace();
        }
        return listaPerfilDecisor;

    }

    public static ArrayList<String>  getListaOpcionesByInstanciaByCodigo(DBclasses dBclasses, String codigo) {
        String S_TAG="getListaOpcionesByInstancia(...) ";
        ArrayList<String> listaPerfilDecisor=new ArrayList<>();
        try {

            String SQL="select " +
                    "so."+DBtables.San_Opciones.instancia+" "+
                    "from "+DBtables.San_Opciones.TAG+" so "+
                    "where so."+DBtables.San_Opciones.codigo_crm+" = '"+codigo+"' "+
                    "group by "+DBtables.San_Opciones.instancia+" order by so."+DBtables.San_Opciones.codigo_crm+" asc ";

            Log.i(TAG, S_TAG+" sql "+SQL);
            SQLiteDatabase dbREad=dBclasses.getReadableDatabase();
            Cursor cursor=dbREad.rawQuery(SQL, null);
            listaPerfilDecisor.add( "-Seleccione- ("+cursor.getCount()+")");
            while (cursor.moveToNext()){
                listaPerfilDecisor.add(cursor.getString(0));
            }
            cursor.close();
            dbREad.close();

        }catch (SQLException e){
            e.printStackTrace();
        }
        return listaPerfilDecisor;

    }

}
