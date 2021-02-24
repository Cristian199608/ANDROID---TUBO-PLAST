package com.example.fuerzaventaschema.genesys.BEAN;

import android.content.Context;


import com.example.fuerzaventaschema.R;
import com.example.fuerzaventaschema.genesys.DAO.DAO_San_Visitas;
import com.example.fuerzaventaschema.genesys.datatypes.DBclasses;
import com.example.fuerzaventaschema.genesys.util.VARIABLES;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class BEAN_AgendaCalendario {

    String Mes;
    String fecha;
    int dia;
    int cant_ejecutada;
    int cant_proxima_visita;
    int T_cant_programada;
    int Cant_cumpleano;
    boolean fechaActual;
    int fondoColor;

    public String getMes() {
        return Mes;
    }

    public void setMes(String mes) {
        Mes = mes;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public int getCant_ejecutada() {
        return cant_ejecutada;
    }

    public void setCant_ejecutada(int cant_ejecutada) {
        this.cant_ejecutada = cant_ejecutada;
    }

    public int getCant_proxima_visita() {
        return cant_proxima_visita;
    }

    public void setCant_proxima_visita(int cant_proxima_visita) {
        this.cant_proxima_visita = cant_proxima_visita;
    }

    public int getT_cant_programada() {
        return T_cant_programada;
    }

    public void setT_cant_programada(int t_cant_programada) {
        T_cant_programada = t_cant_programada;
    }

    public boolean isFechaActual() {
        return fechaActual;
    }

    public void setFechaActual(boolean fechaActual) {
        this.fechaActual = fechaActual;
    }

    public int getFondoColor() {
        return fondoColor;
    }

    public void setFondoColor(int fondoColor) {
        this.fondoColor = fondoColor;
    }

    public int getCant_cumpleano() {
        return Cant_cumpleano;
    }

    public void setCant_cumpleano(int Cant_cumpleano) {
        this.Cant_cumpleano = Cant_cumpleano;
    }

    public ArrayList<BEAN_AgendaCalendario> ObtenerDiasByMes(DBclasses dBclasses, String codven, Context Context, int anio, int nroMes){

        ArrayList<BEAN_AgendaCalendario>  lista_agenda=new ArrayList<>();

        Calendar cal = Calendar.getInstance();
        cal.set(anio,nroMes,1);
        int diaSemana =  cal.get(Calendar.DAY_OF_WEEK);
//        int nroMes =  cal.get(Calendar.MONTH);
        int diaMes =  cal.get(Calendar.DAY_OF_MONTH);
        int diaFIN = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        BEAN_AgendaCalendario agenda=null;
        for (int i=1; i<diaSemana;i++){
            agenda=new BEAN_AgendaCalendario();
            agenda.setDia(0);
            lista_agenda.add(agenda);
        }

        int indexWarngin=0;
        for (int i=0; i<diaFIN;i++){

            agenda=new BEAN_AgendaCalendario();


            agenda.setDia((i+1));
            String fMes="0"+(nroMes+1);
            String fDia="0"+(i+1);
            agenda.setFecha(anio+"-"+(fMes.substring(fMes.length() -2))+"-"+(fDia.substring(fDia.length() -2)));
            agenda.setFechaActual(false);
            agenda.setFondoColor(0);

            try {

                Calendar fechaHOY = Calendar.getInstance();

                String s_fechaHOY =fechaHOY.get(Calendar.YEAR)+"-"+fechaHOY.get(Calendar.MONTH)+"-"+fechaHOY.get(Calendar.DAY_OF_MONTH);
                String s_fechaFor =anio+"-"+nroMes+"-"+agenda.getDia();

                if (s_fechaHOY.equals(s_fechaFor)){
                    agenda.setFechaActual(true);
                    indexWarngin=5;
                }

                BEAN_AgendaCalendario DAOagenda= DAO_San_Visitas.getCant_San_VisitasByFecha(dBclasses, codven,"%",agenda.getFecha());

                agenda.setCant_cumpleano(DAOagenda.getCant_cumpleano());
                if (DAOagenda.getCant_ejecutada()+DAOagenda.getCant_proxima_visita()+DAOagenda.getT_cant_programada()>0){
                    agenda.setCant_ejecutada(DAOagenda.getCant_ejecutada());
                    agenda.setCant_proxima_visita(DAOagenda.getCant_proxima_visita());
                    agenda.setT_cant_programada(DAOagenda.getT_cant_programada());

                    Date datePromedio=VARIABLES.parseDATA(agenda.getFecha());
                    Date dateH= VARIABLES.parseDATA(fechaHOY.get(Calendar.YEAR)+"-"+(fechaHOY.get(Calendar.MONTH)+1)+"-"+fechaHOY.get(Calendar.DAY_OF_MONTH));
                    if (datePromedio.compareTo(dateH)>0){
                        if (indexWarngin>0)agenda.setFondoColor(Context.getResources().getColor(R.color.orange_100));
                        else agenda.setFondoColor(Context.getResources().getColor(R.color.green_100));
                    }
                    else if (datePromedio.compareTo(dateH)==0){
                        agenda.setFondoColor(Context.getResources().getColor(R.color.red_100));
                    }else{
                        agenda.setFondoColor(Context.getResources().getColor(R.color.blue_grey_100));
                    }
                }
                indexWarngin--;
//                for (int a=0; a<lista_visitas.size();a++){
//
//                    Date datePromedio=VARIABLES.parseDATA(lista_visitas.get(a).getFecha_proxima_visita());
//                    Date dateExacto=VARIABLES.parseDATA_Exacto(lista_visitas.get(a).getFecha_proxima_visita());
//                    long d=dateExacto.getTime();
//
//
//                    Calendar ca3 = Calendar.getInstance();
//                    ca3.setTime(datePromedio);
//                    int anioP=ca3.get(Calendar.YEAR);
//                    int mesP=ca3.get(Calendar.MONTH);
//                    int diaP=ca3.get(Calendar.DAY_OF_MONTH);
//
//                    Log.i(TAG, "diaP es "+diaP+", mesP es "+mesP+", anio2 es "+anioP);
//
//                    if (anioP==anio && mesP==nroMes && diaP==agenda.getDia()){
//
//                        if (lista_visitas.get(a).getEstado().equalsIgnoreCase("libre")){
//                            agenda.setCant_programado(agenda.getCant_programado()+1);
//                        }else agenda.setCant_completado(agenda.getCant_completado()+1);
//
//                        lista_visitas.remove(a);
//                        a--;
//
//                        Date dateH=VARIABLES.parseDATA(fechaHOY.get(Calendar.YEAR)+"-"+(fechaHOY.get(Calendar.MONTH)+1)+"-"+fechaHOY.get(Calendar.DAY_OF_MONTH));
//                        if (datePromedio.compareTo(dateH)>0){
//                            if (indexWarngin>0)agenda.setFondoColor(Context.getResources().getColor(R.color.orange_100));
//                            else agenda.setFondoColor(Context.getResources().getColor(R.color.green_100));
//                        }
//                        else if (datePromedio.compareTo(dateH)==0){
//                            Date date=new Date();
//                            if (dateExacto.compareTo(date)>=0){
//                                agenda.setFondoColor(Context.getResources().getColor(R.color.red_100));
//                            }else{
//                                agenda.setFondoColor(Context.getResources().getColor(R.color.blue_grey_100));
//                            }
//                        }else{
//                            agenda.setFondoColor(Context.getResources().getColor(R.color.blue_grey_100));
//                        }
//
//                    }
//
//                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
            catch (Exception e){
                e.printStackTrace();
            }
            lista_agenda.add(agenda);
        }
        return  lista_agenda;

    }
}
