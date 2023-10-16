package com.example.sm_tubo_plast.genesys.DAO;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.sm_tubo_plast.genesys.BEAN.BEAN_AgendaCalendario;
import com.example.sm_tubo_plast.genesys.BEAN.San_Visitas;
import com.example.sm_tubo_plast.genesys.datatypes.DBclasses;
import com.example.sm_tubo_plast.genesys.datatypes.DBtables;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.GestionVisita3Activity;

import java.util.ArrayList;

public class DAO_San_Visitas {

    private static final String TAG = "DAO_San_Visitas";
    public static  String TIPO_PLANIFICADA = "A_PLANIFICADA";
    public static  String TIPO_EJECUTADA = "B_EJECUTADA";
    public  static  ArrayList<San_Visitas> getSan_VisitasByFecha(DBclasses dBclasses, String CODVEN, String codigo_crm, String fecha){

        String where1="";
        String where2="";

        if (fecha.length()==10){//anio-mes-dia

            where1 = "where  "+ DBtables.San_Visitas.Cod_Colegio+" like '"+codigo_crm+"' " +
                    "and  "+DBtables.San_Visitas.Cod_Promotor+" = '"+CODVEN+"' "+
                    "and "+DBtables.San_Visitas.Fecha_planificada+" like '"+fecha+"%' "+
                    "and "+DBtables.San_Visitas.Estado+" = '"+San_Visitas.ESTADO_VISITA_LIBRE+"' "+
                    "order by datetime("+DBtables.San_Visitas.Fecha_planificada+"), "+DBtables.San_Visitas.Cod_Colegio+"  asc ";

            where2 = "where  "+DBtables.San_Visitas.Cod_Colegio+" like '"+codigo_crm+"' " +
                    "and  "+DBtables.San_Visitas.Cod_Promotor+" = '"+CODVEN+"' "+
                    "and  "+DBtables.San_Visitas.Fecha_Ejecutada+" like '"+fecha+"%'  "+
                    "order by  datetime("+DBtables.San_Visitas.Fecha_Ejecutada+"), "+DBtables.San_Visitas.Cod_Colegio+"  asc ";

        }else{//anio-mes
            where1 = "where  "+DBtables.San_Visitas.Cod_Colegio+" like '"+codigo_crm+"' " +
                    "and  "+DBtables.San_Visitas.Cod_Promotor+" = '"+CODVEN+"' "+
                    "and  "+DBtables.San_Visitas.Fecha_planificada+" like '"+fecha+"%' "+
                    "order by  datetime("+DBtables.San_Visitas.Fecha_planificada+"), "+DBtables.San_Visitas.Cod_Colegio+"  asc ";


        }

        return DBSan_Visitas(dBclasses, where1, where2);
    }



    public  static  ArrayList<San_Visitas> getSan_Visitas(DBclasses dBclasses,String CODVEN, String codigo_crm){
        String where1 = "where  "+DBtables.San_Visitas.Cod_Colegio+" like '"+codigo_crm+"' "+
                "and  "+DBtables.San_Visitas.Cod_Promotor+" = '"+CODVEN+"' "+
                "order by  datetime("+DBtables.San_Visitas.Fecha_planificada+") asc ";
        return DBSan_Visitas(dBclasses, where1, "");
    }

    public  static  ArrayList<San_Visitas> getSan_VisitasByOc_numero(DBclasses dBclasses,String Oc_numero){
        String where1 = "where  "+DBtables.San_Visitas.oc_numero_visitado+" = '"+Oc_numero+"' "+
                "or  substr("+DBtables.San_Visitas.oc_numero_visitar+", -"+Oc_numero.length()+") = '"+Oc_numero+"' ";

        return DBSan_Visitas(dBclasses, where1, "");
    }


    public  static BEAN_AgendaCalendario getCant_San_VisitasByFecha(DBclasses dBclasses, String CODVEN, String codigo_crm, String fecha){


        String whereEjecutada = "where  "+DBtables.San_Visitas.Cod_Colegio+" like '"+codigo_crm+"' " +
                "and  "+DBtables.San_Visitas.Fecha_Ejecutada+" like '"+fecha+"%'"+
                //"and  "+DBtables.San_Visitas.Fecha_Ejecutada+" like '"+fecha+"%' "+
                "and  "+DBtables.San_Visitas.Cod_Promotor+" = '"+CODVEN+"' ";

        String wherePlanificada = "where  "+DBtables.San_Visitas.Cod_Colegio+" like '"+codigo_crm+"' " +
                "and  "+DBtables.San_Visitas.Fecha_planificada+" like '"+fecha+"%' "+
                "and  "+DBtables.San_Visitas.Estado+" ='"+San_Visitas.ESTADO_VISITA_LIBRE+"' "+
                "and  "+DBtables.San_Visitas.Cod_Promotor+" = '"+CODVEN+"' ";



        BEAN_AgendaCalendario agenda =new BEAN_AgendaCalendario();
        String sql="SELECT * from ( " +
                "(select count(st1.Estado) "+
                " from "+DBtables.San_Visitas.TAG+" st1 "+whereEjecutada+" and  "+DBtables.San_Visitas.Estado+" like '%' ) as V_cant_ejecutada, "+
                "(select count(st2.Estado) "+
                " from "+DBtables.San_Visitas.TAG+" st2 "+wherePlanificada+" and  "+DBtables.San_Visitas.Estado+" like '%' ) as V_cant_proxima_visita "+//completada
                ") row_count ";



        SQLiteDatabase db=dBclasses.getReadableDatabase();
        Cursor cursor=db.rawQuery(sql, null, null);
        while (cursor.moveToNext()){
            agenda=new BEAN_AgendaCalendario();
            //Visitas
            agenda.setCant_ejecutada(cursor.getInt(0));
            agenda.setCant_proxima_visita(cursor.getInt(1));
            //Talleres
            //agenda.setT_cant_programada(cursor.getInt(2));
            //Cantidad Cumpleaños
            //agenda.setCant_cumpleano(cursor.getInt(3));
        }
        cursor.close();
        db.close();
        return agenda;
    }
    //SELECT * from ( (select count(st1.Estado)  from SAN_VISITAS st1 where  Cod_Colegio like '%' and
    // Fecha_Ejecutada like '2019-10-30%' and  Cod_Promotor = '00000470'  and  Estado like '%' ) as V_cant_ejecutada,
    // (select count(st2.Estado)  from SAN_VISITAS st2 where  Cod_Colegio like '%' and
    // Fecha_proxima_visita like '2019-10-30%' and  Cod_Promotor = '00000470'  and  Estado like '%' )
    // as V_cant_proxima_visita, (select count(ta1.Estado)  from san_talleres ta2 where  Codigo_Institucion like '%'
    // and  Fecha_Inicio like '2019-10-30%' and  Codigo_Promotor = '00000470'  and  Estado like '%' ) as T_cant_programado ) row_count

    public  static  ArrayList<San_Visitas> DBSan_Visitas(DBclasses dBclasses, String where1, String where2){

        String _TAG="DBSan_Visitas:: ";

        ArrayList<San_Visitas> LISTA=new ArrayList<>();


        String sql1="SELECT " +
                "st."+ DBtables.San_Visitas.Grupo_Campaña+", "+
                "st."+ DBtables.San_Visitas.Cod_Promotor+", "+
                "st."+ DBtables.San_Visitas.Promotor+", "+
                "st."+ DBtables.San_Visitas.Cod_Colegio+", "+
                "st."+ DBtables.San_Visitas.descripcion_Colegio+", "+
                "st."+ DBtables.San_Visitas.Ejecutivo_Descripcion+", "+
                "st."+ DBtables.San_Visitas.cargo_Descripción+", "+
                "st."+ DBtables.San_Visitas.Fecha_Ejecutada+", "+
                "st."+ DBtables.San_Visitas.Fecha_planificada+", "+
                "st."+ DBtables.San_Visitas.Hora_inicio_ejecución+", "+
                "st."+ DBtables.San_Visitas.Hora_Fin_Ejecución+", "+
                "st."+ DBtables.San_Visitas.fecha_de_modificación+", "+
                "st."+ DBtables.San_Visitas.Estado+", "+
                "st."+ DBtables.San_Visitas.Actividad+", "+
                "st."+ DBtables.San_Visitas.Detalle+", "+
                "st."+ DBtables.San_Visitas.Actividad_Proxima+", "+
                "st."+ DBtables.San_Visitas.Detalle_Proximo+", "+
                "st."+ DBtables.San_Visitas.Comentario_actividad+", "+
                "st."+ DBtables.San_Visitas.Fecha_proxima_visita+", "+
                "st."+ DBtables.San_Visitas.id_rrhh+", "+
                "st."+ DBtables.San_Visitas.latitud+", "+
                "st."+ DBtables.San_Visitas.longitud+", "+
                "st."+ DBtables.San_Visitas.oc_numero_visitado+", "+
                "st."+ DBtables.San_Visitas.oc_numero_visitar+", "+
                "st."+ DBtables.San_Visitas.id_contacto+", "+
                "case st."+DBtables.San_Visitas.Estado+" when '"+GestionVisita3Activity.ESTADO_VISITA_Libre+"' then  '"+TIPO_PLANIFICADA+"' else '"+TIPO_EJECUTADA+"' end AS TIPO_VISTA, "+
                "st."+ DBtables.San_Visitas.tipo_visita+", "+
                "st."+ DBtables.San_Visitas.altitud+", "+
                " ifnull( st."+ DBtables.San_Visitas.descripcion_anulacion+", '') as descripcion_anulacion, "+
                "ifnull(st.item, 1) as item, "+
                "id "+
                " from "+DBtables.San_Visitas.TAG+" st "+
                ""+where1;

        if (where2.length()>0){
            String sql2="SELECT " +
                    "st."+ DBtables.San_Visitas.Grupo_Campaña+", "+
                    "st."+ DBtables.San_Visitas.Cod_Promotor+", "+
                    "st."+ DBtables.San_Visitas.Promotor+", "+
                    "st."+ DBtables.San_Visitas.Cod_Colegio+", "+
                    "st."+ DBtables.San_Visitas.descripcion_Colegio+", "+
                    "st."+ DBtables.San_Visitas.Ejecutivo_Descripcion+", "+
                    "st."+ DBtables.San_Visitas.cargo_Descripción+", "+
                    "st."+ DBtables.San_Visitas.Fecha_Ejecutada+", "+
                    "st."+ DBtables.San_Visitas.Fecha_planificada+", "+
                    "st."+ DBtables.San_Visitas.Hora_inicio_ejecución+", "+
                    "st."+ DBtables.San_Visitas.Hora_Fin_Ejecución+", "+
                    "st."+ DBtables.San_Visitas.fecha_de_modificación+", "+
                    "st."+ DBtables.San_Visitas.Estado+", "+
                    "st."+ DBtables.San_Visitas.Actividad+", "+
                    "st."+ DBtables.San_Visitas.Detalle+", "+
                    "st."+ DBtables.San_Visitas.Actividad_Proxima+", "+
                    "st."+ DBtables.San_Visitas.Detalle_Proximo+", "+
                    "st."+ DBtables.San_Visitas.Comentario_actividad+", "+
                    "st."+ DBtables.San_Visitas.Fecha_proxima_visita+", "+
                    "st."+ DBtables.San_Visitas.id_rrhh+", "+
                    "st."+ DBtables.San_Visitas.latitud+", "+
                    "st."+ DBtables.San_Visitas.longitud+", "+
                    "st."+ DBtables.San_Visitas.oc_numero_visitado+", "+
                    "st."+ DBtables.San_Visitas.oc_numero_visitar+", "+
                    "st."+ DBtables.San_Visitas.id_contacto+", "+
                    " '"+TIPO_EJECUTADA+"' AS TIPO_VISTA, "+
                    "st."+ DBtables.San_Visitas.tipo_visita+", "+
                    "st."+ DBtables.San_Visitas.altitud+", "+
                    " ifnull( st."+ DBtables.San_Visitas.descripcion_anulacion+", '') as descripcion_anulacion, "+
                    "ifnull(st.item, 1) as item, "+
                    "id "+
                    " from "+DBtables.San_Visitas.TAG+" st "+
                    ""+where2;

            sql1=" select * from ( select * from ("+sql1+") union all select * from ("+sql2+") ) order by  TIPO_VISTA asc";
        }


        try {
            Log.i(TAG, _TAG+"SQl: "+sql1);
            SQLiteDatabase db=dBclasses.getReadableDatabase();
            Cursor cursor=db.rawQuery(sql1, null, null);

            String fecha_descripcion="";
            while (cursor.moveToNext()){
                San_Visitas san_t=new San_Visitas();

                san_t.setId(cursor.getInt(cursor.getColumnIndex("id")));
                san_t.setGrupo_Campaña(cursor.getString(0));
                san_t.setCod_Promotor(cursor.getString(1));
                san_t.setPromotor(cursor.getString(2));
                san_t.setCod_Colegio(cursor.getString(3));
                san_t.setDescripcion_Colegio(cursor.getString(4));
                san_t.setEjecutivo_Descripcion(cursor.getString(5));
                san_t.setCargo_Descripción(cursor.getString(6));
                san_t.setFecha_Ejecutada(cursor.getString(7));
                san_t.setFecha_planificada(cursor.getString(8));
                san_t.setHora_inicio_ejecución(cursor.getString(9));
                san_t.setHora_Fin_Ejecución(cursor.getString(10));
                san_t.setFecha_de_modificación(cursor.getString(11));
                san_t.setEstado(cursor.getString(12));
                san_t.setActividad(cursor.getString(13));
                san_t.setDetalle(cursor.getString(14));
                san_t.setActividad_Proxima(cursor.getString(15));
                san_t.setDetalle_Proximo(cursor.getString(16));
                san_t.setComentario_actividad(cursor.getString(17));
                san_t.setFecha_proxima_visita(cursor.getString(18));
                san_t.setId_rrhh(cursor.getString(19));
                san_t.setLatitud(cursor.getString(20));
                san_t.setLongitud(cursor.getString(21));
                san_t.setOc_numero_visitado(cursor.getString(22));
                san_t.setOc_numero_visitar(cursor.getString(23));
                san_t.setId_contacto(cursor.getInt(24));
                san_t.setTipo_vista(cursor.getString(25));
                san_t.setTipo_visita(cursor.getString(26));
                san_t.setAltitud(cursor.getDouble(cursor.getColumnIndex("altitud")));
                san_t.setDescripcion_anulacion(cursor.getString(cursor.getColumnIndex("descripcion_anulacion")));
                san_t.setItem(cursor.getInt(cursor.getColumnIndex("item")));

                String comparaeFecha=san_t.getFecha_planificada();

                if (!fecha_descripcion.equals(comparaeFecha.substring(0, 10))){
                    fecha_descripcion=""+comparaeFecha.substring(0, 10);
                    san_t.setDescripcion_fecha(fecha_descripcion);
                }
                else san_t.setDescripcion_fecha("");


                LISTA.add(san_t);
            }
            cursor.close();
            db.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return LISTA;
    }

    public  static  ArrayList<San_Visitas> GetVisitasByOc_numero(DBclasses dBclasses, String oc_numero, String TIPO_GESTION){

        String _TAG="GetVisitasByOc_numero:: ";
        String addWhere="";
        if (TIPO_GESTION.equals(GestionVisita3Activity.PROGRAMACION_VISITA)){//programar
            addWhere=DBtables.San_Visitas.oc_numero_visitar+"='"+oc_numero+"'";
        }
        else if (TIPO_GESTION.equals(GestionVisita3Activity.VISITA_PLANIFICADA)
                || TIPO_GESTION.equals(GestionVisita3Activity.MODIFICAR_PROGRAMACION)
                || TIPO_GESTION.equals(GestionVisita3Activity.RE_PROGRAMACION_VISITA)){//visitar
            addWhere=DBtables.San_Visitas.oc_numero_visitar+"='"+oc_numero+"'";
        }else{// visitado
            addWhere="(st."+DBtables.San_Visitas.oc_numero_visitado+"='"+oc_numero+"' or st."+DBtables.San_Visitas.oc_numero_visitar+"='"+oc_numero+"')";
        }
        ArrayList<San_Visitas> LISTA=new ArrayList<>();

        String sql1="SELECT " +
                "st."+ DBtables.San_Visitas.Grupo_Campaña+", "+
                "st."+ DBtables.San_Visitas.Cod_Promotor+", "+
                "st."+ DBtables.San_Visitas.Promotor+", "+
                "st."+ DBtables.San_Visitas.Cod_Colegio+", "+
                "st."+ DBtables.San_Visitas.descripcion_Colegio+", "+
                "st."+ DBtables.San_Visitas.Ejecutivo_Descripcion+", "+
                "st."+ DBtables.San_Visitas.cargo_Descripción+", "+
                "st."+ DBtables.San_Visitas.Fecha_Ejecutada+", "+
                "st."+ DBtables.San_Visitas.Fecha_planificada+", "+
                "st."+ DBtables.San_Visitas.Hora_inicio_ejecución+", "+
                "st."+ DBtables.San_Visitas.Hora_Fin_Ejecución+", "+
                "st."+ DBtables.San_Visitas.fecha_de_modificación+", "+
                "st."+ DBtables.San_Visitas.Estado+", "+
                "st."+ DBtables.San_Visitas.Actividad+", "+
                "st."+ DBtables.San_Visitas.Detalle+", "+
                "st."+ DBtables.San_Visitas.Actividad_Proxima+", "+
                "st."+ DBtables.San_Visitas.Detalle_Proximo+", "+
                "st."+ DBtables.San_Visitas.Comentario_actividad+", "+
                "st."+ DBtables.San_Visitas.Fecha_proxima_visita+", "+
                "st."+ DBtables.San_Visitas.id_rrhh+", "+
                "st."+ DBtables.San_Visitas.tipo_visita+", "+
                "st."+ DBtables.San_Visitas.latitud+", "+
                "st."+ DBtables.San_Visitas.longitud+", "+
                "st."+ DBtables.San_Visitas.distancia+", "+
                "st."+ DBtables.San_Visitas.oc_numero_visitado+", "+
                "st."+ DBtables.San_Visitas.oc_numero_visitar+", "+
                "st."+ DBtables.San_Visitas.id_contacto+", "+
                "st."+ DBtables.San_Visitas.ID+", "+
                " '"+TIPO_PLANIFICADA+"' AS TIPO_VISTA, "+
                "st."+ DBtables.San_Visitas.altitud+" "+
                " from "+DBtables.San_Visitas.TAG+" st "+
                "where "+addWhere+" ORDER BY "+DBtables.San_Visitas.ID+" ASC";

        try {
            Log.i(TAG, _TAG+"SQl: "+sql1);
            SQLiteDatabase db=dBclasses.getReadableDatabase();
            Cursor cursor=db.rawQuery(sql1, null, null);

            String fecha_descripcion="";
            while (cursor.moveToNext()){
                San_Visitas san_t=new San_Visitas();

                san_t.setGrupo_Campaña(cursor.getString(0));
                san_t.setCod_Promotor(cursor.getString(1));
                san_t.setPromotor(cursor.getString(2));
                san_t.setCod_Colegio(cursor.getString(3));
                san_t.setDescripcion_Colegio(cursor.getString(4));
                san_t.setEjecutivo_Descripcion(cursor.getString(5));
                san_t.setCargo_Descripción(cursor.getString(6));
                san_t.setFecha_Ejecutada(cursor.getString(7));
                san_t.setFecha_planificada(cursor.getString(8));
                san_t.setHora_inicio_ejecución(cursor.getString(9));
                san_t.setHora_Fin_Ejecución(cursor.getString(10));
                san_t.setFecha_de_modificación(cursor.getString(11));
                san_t.setEstado(cursor.getString(12));
                san_t.setActividad(cursor.getString(13));
                san_t.setDetalle(cursor.getString(14));
                san_t.setActividad_Proxima(cursor.getString(15));
                san_t.setDetalle_Proximo(cursor.getString(16));
                san_t.setComentario_actividad(cursor.getString(17));
                san_t.setFecha_proxima_visita(cursor.getString(18));
                san_t.setId_rrhh(cursor.getString(19));
                san_t.setTipo_visita(cursor.getString(20));
                san_t.setLatitud(cursor.getString(21));
                san_t.setLongitud(cursor.getString(22));
                san_t.setDistancia(cursor.getInt(23));
                san_t.setOc_numero_visitado(cursor.getString(24));
                san_t.setOc_numero_visitar(cursor.getString(25));
                san_t.setId_contacto(cursor.getInt(26));
                san_t.setId(cursor.getInt(27));
                san_t.setTipo_vista(cursor.getString(28));
                san_t.setAltitud(cursor.getDouble(cursor.getColumnIndex( DBtables.San_Visitas.altitud)));

                LISTA.add(san_t);
            }
            cursor.close();
            db.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return LISTA;
    }

    public static boolean LlenarlistaTabla_San_Visitas(SQLiteDatabase DB, ArrayList<San_Visitas> lista){

        try {
            for (San_Visitas sanVisita:lista){
                if (!LlenarTabla_San_Visitas(DB, sanVisita)){
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean LlenarTabla_San_Visitas(SQLiteDatabase DB, San_Visitas sanVisita) {
        try {
            final String STAG = "LlenarTabla_San_Visitas:: ";
            ContentValues cv = new ContentValues();
            cv.put(DBtables.San_Visitas.Grupo_Campaña,  sanVisita.getGrupo_Campaña());
            cv.put(DBtables.San_Visitas.Cod_Promotor,  sanVisita.getCod_Promotor());
            cv.put(DBtables.San_Visitas.Promotor,  sanVisita.getPromotor());
            cv.put(DBtables.San_Visitas.Cod_Colegio,  sanVisita.getCod_Colegio());
            cv.put(DBtables.San_Visitas.descripcion_Colegio,  sanVisita.getDescripcion_Colegio());
            cv.put(DBtables.San_Visitas.Ejecutivo_Descripcion,  sanVisita.getEjecutivo_Descripcion());
            cv.put(DBtables.San_Visitas.cargo_Descripción,  sanVisita.getCargo_Descripción());
            cv.put(DBtables.San_Visitas.Fecha_Ejecutada,  sanVisita.getFecha_Ejecutada());
            cv.put(DBtables.San_Visitas.Fecha_planificada,  sanVisita.getFecha_planificada());
            cv.put(DBtables.San_Visitas.Fecha_proxima_visita,  sanVisita.getFecha_proxima_visita());
            cv.put(DBtables.San_Visitas.Hora_inicio_ejecución,  sanVisita.getHora_inicio_ejecución());
            cv.put(DBtables.San_Visitas.Hora_Fin_Ejecución,  sanVisita.getHora_Fin_Ejecución());
            cv.put(DBtables.San_Visitas.fecha_de_modificación,  sanVisita.getFecha_de_modificación());
            cv.put(DBtables.San_Visitas.Estado,  sanVisita.getEstado());
            cv.put(DBtables.San_Visitas.Actividad,  sanVisita.getActividad());
            cv.put(DBtables.San_Visitas.Detalle,  sanVisita.getDetalle());
            cv.put(DBtables.San_Visitas.Actividad_Proxima,  sanVisita.getActividad_Proxima());
            cv.put(DBtables.San_Visitas.Detalle_Proximo,  sanVisita.getDetalle_Proximo());
            cv.put(DBtables.San_Visitas.Comentario_actividad,  sanVisita.getComentario_actividad());
            cv.put(DBtables.San_Visitas.id_rrhh,  sanVisita.getId_rrhh());
            cv.put(DBtables.San_Visitas.tipo_visita,  sanVisita.getTipo_visita());
            cv.put(DBtables.San_Visitas.latitud,  sanVisita.getLatitud());
            cv.put(DBtables.San_Visitas.longitud,  sanVisita.getLongitud());
            cv.put(DBtables.San_Visitas.altitud,  sanVisita.getAltitud());
            cv.put(DBtables.San_Visitas.distancia,  sanVisita.getDistancia());
            cv.put(DBtables.San_Visitas.oc_numero_visitado,  sanVisita.getOc_numero_visitado());
            cv.put(DBtables.San_Visitas.oc_numero_visitar,  sanVisita.getOc_numero_visitar());
            cv.put(DBtables.San_Visitas.item,  sanVisita.getItem());
            cv.put(DBtables.San_Visitas.id_contacto,  sanVisita.getId_contacto());
            long insertCant =DB.insert(DBtables.San_Visitas.TAG,null, cv);
            return insertCant>0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean UpdateListaVisitaEjecucion(SQLiteDatabase DB, ArrayList<San_Visitas> lista){
        try {
            for (San_Visitas sanVisita:lista) {
                if(!UpdateVisitaEjecucion(DB, sanVisita)){
                    return false;
                }
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public static boolean UpdateVisitaEjecucion(SQLiteDatabase DB, San_Visitas sanVisita){
        try {
            if (!isSan_VisitasByID(DB, sanVisita.getId())) {
                return LlenarTabla_San_Visitas(DB, sanVisita);
            }else{
                String where = " " + DBtables.San_Visitas.ID + " = ? and  length(ifnull(descripcion_anulacion, ''))==0 ";
                String[] args = {"" + sanVisita.getId()};
                ContentValues cv = new ContentValues();
                cv.put(DBtables.San_Visitas.Grupo_Campaña,  sanVisita.getGrupo_Campaña());
                cv.put(DBtables.San_Visitas.Cod_Promotor,  sanVisita.getCod_Promotor());
                cv.put(DBtables.San_Visitas.Promotor,  sanVisita.getPromotor());
                cv.put(DBtables.San_Visitas.Cod_Colegio,  sanVisita.getCod_Colegio());
                cv.put(DBtables.San_Visitas.descripcion_Colegio,  sanVisita.getDescripcion_Colegio());
                cv.put(DBtables.San_Visitas.Ejecutivo_Descripcion,  sanVisita.getEjecutivo_Descripcion());
                cv.put(DBtables.San_Visitas.cargo_Descripción,  sanVisita.getCargo_Descripción());
                cv.put(DBtables.San_Visitas.Fecha_Ejecutada,  sanVisita.getFecha_Ejecutada());
                cv.put(DBtables.San_Visitas.Fecha_planificada,  sanVisita.getFecha_planificada());
                cv.put(DBtables.San_Visitas.Fecha_proxima_visita,  sanVisita.getFecha_proxima_visita());
                cv.put(DBtables.San_Visitas.Hora_inicio_ejecución,  sanVisita.getHora_inicio_ejecución());
                cv.put(DBtables.San_Visitas.Hora_Fin_Ejecución,  sanVisita.getHora_Fin_Ejecución());
                cv.put(DBtables.San_Visitas.fecha_de_modificación,  sanVisita.getFecha_de_modificación());
                cv.put(DBtables.San_Visitas.Estado,  sanVisita.getEstado());
                cv.put(DBtables.San_Visitas.Actividad,  sanVisita.getActividad());
                cv.put(DBtables.San_Visitas.Detalle,  sanVisita.getDetalle());
                cv.put(DBtables.San_Visitas.Actividad_Proxima,  sanVisita.getActividad_Proxima());
                cv.put(DBtables.San_Visitas.Detalle_Proximo,  sanVisita.getDetalle_Proximo());
                cv.put(DBtables.San_Visitas.Comentario_actividad,  sanVisita.getComentario_actividad());
                cv.put(DBtables.San_Visitas.id_rrhh,  sanVisita.getId_rrhh());
                cv.put(DBtables.San_Visitas.tipo_visita,  sanVisita.getTipo_visita());
                cv.put(DBtables.San_Visitas.latitud,  sanVisita.getLatitud());
                cv.put(DBtables.San_Visitas.longitud,  sanVisita.getLongitud());
                if (sanVisita.getAltitud()>0){
                    cv.put(DBtables.San_Visitas.altitud,  sanVisita.getAltitud());
                }
                if (sanVisita.getDistancia()>0){
                    cv.put(DBtables.San_Visitas.distancia,  sanVisita.getDistancia()); //NO MODIFICABLE
                }
                cv.put(DBtables.San_Visitas.oc_numero_visitado,  sanVisita.getOc_numero_visitado());
                cv.put(DBtables.San_Visitas.oc_numero_visitar,  sanVisita.getOc_numero_visitar());
                cv.put(DBtables.San_Visitas.id_contacto,  sanVisita.getId_contacto());

                long i = DB.update(DBtables.San_Visitas.TAG, cv, where, args);
                return i>0;
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public  static boolean isVisitado_la_planificacion(SQLiteDatabase db, String oc_numero_visitar){

        String sql="select v.oc_numero_visitado from "+DBtables.San_Visitas.TAG+" v "+
                "where v.oc_numero_visitar='"+oc_numero_visitar+"' ";

        Cursor cursor=db.rawQuery(sql, null, null);

        int cantidad= cursor.getCount();
        if (cursor.moveToNext()){
            cantidad=cursor.getString(0).length();
        }
        cursor.close();
        db.close();
        return cantidad>0;
    }

    public  static San_Visitas getSan_VisitasByFecha(SQLiteDatabase db, String codcli, String sitio_enfa){

        String sql="SELECT ifnull((" +
                "select v.Fecha_Ejecutada   " +
                "from "+DBtables.San_Visitas.TAG+" v inner join pedido_cabecera pc on v.oc_numero_visitado=pc.oc_numero " +
                "where pc.cod_cli='"+codcli+"' and pc.sitio_enfa ='"+sitio_enfa+"' " +
                "ORDER BY datetime(v.Fecha_Ejecutada) DESC LIMIT 1" +
                "), '') as fecha_ultima_visitada," +

                "ifnull((" +
                "select v.Fecha_planificada   " +
                "from "+DBtables.San_Visitas.TAG+" v inner join pedido_cabecera pc on v.oc_numero_visitar=pc.oc_numero " +
                "where pc.cod_cli='"+codcli+"' and pc.sitio_enfa ='"+sitio_enfa+"' " +
                "and LENGTH(v.oc_numero_visitar)>0 and LENGTH(v.oc_numero_visitado)=0  and LENGTH(ifnull(v.descripcion_anulacion, ''))=0 " +
                "ORDER BY datetime(v.Fecha_planificada) asc LIMIT 1" +
                "), '') as fecha_proxima_visita";

        Cursor cursor=db.rawQuery(sql, null, null);

        San_Visitas san_visitas=null;
        if (cursor.moveToNext()){
            san_visitas=new San_Visitas();
            san_visitas.setFecha_Ejecutada(cursor.getString(cursor.getColumnIndex("fecha_ultima_visitada")));
            san_visitas.setFecha_proxima_visita(cursor.getString(cursor.getColumnIndex("fecha_proxima_visita")));
        }
        cursor.close();
        db.close();
        return san_visitas;
    }

    public  static  San_Visitas getSan_VisitaByOc_numero(DBclasses dBclasses,String Oc_numero){
        String where1 = "where  "+DBtables.San_Visitas.oc_numero_visitado+" = '"+Oc_numero+"' ";

        ArrayList<San_Visitas> lll= DBSan_Visitas(dBclasses, where1, "");
        San_Visitas visita= lll.size()>0?lll.get(0):null;
        return visita;
    }
    public  static  San_Visitas getSan_VisitarByOc_numero(DBclasses dBclasses,String Oc_numero){
        String where1 = "where  "+DBtables.San_Visitas.oc_numero_visitar+" = '"+Oc_numero+"' ";

        ArrayList<San_Visitas> lll= DBSan_Visitas(dBclasses, where1, "");
        San_Visitas visita= lll.size()>0?lll.get(0):null;
        return visita;
    }

    public  static  boolean isSan_VisitasByID(SQLiteDatabase _db,int id_visita){

        String sql="select * from "+DBtables.San_Visitas.TAG+" v "+
                "where  v."+DBtables.San_Visitas.ID+" = "+id_visita+" ";

        Cursor cursor=_db.rawQuery(sql, null, null);

        int cantidad= cursor.getCount();

        cursor.close();
        return cantidad>0;
    }

    public  static  boolean isSan_VisitasByOc_visitadoAndOc_numero_visitar(SQLiteDatabase _db,String oc_visitado, String oc_visitar){

        String sql="select * from "+DBtables.San_Visitas.TAG+" v "+
                "where  v."+DBtables.San_Visitas.oc_numero_visitado+" = '"+oc_visitado+"' " +
                "and v."+DBtables.San_Visitas.oc_numero_visitar+" = '"+oc_visitar+"' ";

        Cursor cursor=_db.rawQuery(sql, null, null);

        int cantidad= cursor.getCount();

        cursor.close();
        return cantidad>0;

    }

    public  static  boolean setComentarioAnulacionVisita(SQLiteDatabase _db,String oc_visitar, String descripcion_anulacion){

        try {
            String where = " " + DBtables.San_Visitas.oc_numero_visitar + " = ?";
            String[] args = {"" + oc_visitar};


            ContentValues nre=new ContentValues();
            nre.put(DBtables.San_Visitas.descripcion_anulacion,descripcion_anulacion );
            _db.update(DBtables.San_Visitas.TAG, nre, where, args);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }


    public  static  int getMaxItemByOc_numero(SQLiteDatabase _db,String oc_visitar){

        try {
            String sql="select max(v.item) from "+DBtables.San_Visitas.TAG+" v "+
                    "where  substr(v."+DBtables.San_Visitas.oc_numero_visitar+", -"+oc_visitar.length()+") = '"+oc_visitar+"' ";

            Cursor cursor=_db.rawQuery(sql, null, null);
            int cantidad=0;
            while (cursor.moveToNext()){
                cantidad=cursor.getInt(0);
            }
            cursor.close();
            return cantidad;
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }

    }

    public  static  String tieneReporgramadoVisita(SQLiteDatabase _db,int id, String oc_visitar){

        try {
            String  fecha_nueva="";
            String sql="select * from "+DBtables.San_Visitas.TAG+" v "+
                    "where  v."+DBtables.San_Visitas.oc_numero_visitar+" = '"+GestionVisita3Activity.OC_NUMERO_REPROGRAMADO+"'||'"+oc_visitar+"' ";

            Cursor cursor=_db.rawQuery(sql, null, null);
            if (cursor.getCount()>0 /*|| oc_visitar.contains(GestionVisita3Activity.OC_NUMERO_REPROGRAMADO)*/){
                sql="select Fecha_planificada as fecha_nueva_visita from "+DBtables.San_Visitas.TAG+" v " +
                        "where  v."+DBtables.San_Visitas.oc_numero_visitar+" = '"+oc_visitar+"' and id!= "+id;
                Cursor cursor2=_db.rawQuery(sql, null, null);
                if (cursor2.moveToNext()){
                    fecha_nueva= cursor2.getString(0);
                }
                cursor2.close();
            }
            cursor.close();
            return fecha_nueva;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    public  static  boolean setComentarioReprogramacionVisita(SQLiteDatabase _db,String oc_visitar,  String descripcion_anulacion, String nueva_fech_y_nueva_hora){
        try {
            int maxItem=getMaxItemByOc_numero(_db, oc_visitar);
            String where = " " + DBtables.San_Visitas.oc_numero_visitar + " = ? and  length(ifnull(descripcion_anulacion, ''))==0 and item = ?";
            String[] args = {"" + oc_visitar, ""+maxItem};

            ContentValues nre=new ContentValues();
            nre.put(DBtables.San_Visitas.descripcion_anulacion,descripcion_anulacion );
            nre.put(DBtables.San_Visitas.oc_numero_visitar, "RE-"+oc_visitar);
            nre.put(DBtables.San_Visitas.Fecha_proxima_visita, nueva_fech_y_nueva_hora);
            long dd=_db.update(DBtables.San_Visitas.TAG, nre, where, args);
            return dd>0;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    public  static  boolean updateFechaProximaVisitaAlVisitado(SQLiteDatabase _db,String oc_visitado, String nueva_fech_y_nueva_hora){
        try {

            String where = " " + DBtables.San_Visitas.oc_numero_visitado + " = ? ";
            String[] args = {"" + oc_visitado};

            ContentValues nre=new ContentValues();
            nre.put(DBtables.San_Visitas.Fecha_proxima_visita, nueva_fech_y_nueva_hora);
            _db.update(DBtables.San_Visitas.TAG, nre, where, args);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }


}
/*
SELECT st.Grupo_Campaña, st.Cod_Promotor, st.Promotor, st.Cod_Colegio, st.descripcion_Colegio, st.Ejecutivo_Descripcion,
st.cargo_Descripción, st.Fecha_Ejecutada, st.Fecha_proxima_visita, st.Hora_inicio_ejecución, st.Hora_Fin_Ejecución,
st.fecha_de_modificación, st.Estado, st.Actividad, st.Detalle, st.Actividad_Proxima, st.Detalle_Proximo,
st.Comentario_actividad  from SAN_VISITAS st where  Cod_Colegio like '%' and  Cod_Promotor = '00001093'
and  Fecha_Ejecutada like '2019-11-20%' order by  datetime(Fecha_proxima_visita), Cod_Colegio  asc  union all
SELECT st.Grupo_Campaña, st.Cod_Promotor, st.Promotor, st.Cod_Colegio, st.descripcion_Colegio, st.Ejecutivo_Descripcion,
st.cargo_Descripción, st.Fecha_Ejecutada, st.Fecha_proxima_visita, st.Hora_inicio_ejecución, st.Hora_Fin_Ejecución,
st.fecha_de_modificación, st.Estado, st.Actividad, st.Detalle, st.Actividad_Proxima, st.Detalle_Proximo,
st.Comentario_actividad  from SAN_VISITAS st where  Cod_Colegio like '%' and  Cod_Promotor = '00001093'
and Fecha_proxima_visita like '2019-11-20%' order by  datetime(Fecha_proxima_visita), Cod_Colegio  asc
 */
