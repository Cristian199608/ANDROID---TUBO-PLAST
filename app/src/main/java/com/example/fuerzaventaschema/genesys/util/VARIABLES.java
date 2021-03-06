package com.example.fuerzaventaschema.genesys.util;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VARIABLES {

    public static final  String[] MESES = {"Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre"
            ,"Octubre","Noviembre","Diciembre"};

    //[sunat_exportar_reporte_ple_registro_copia]
    public static final String getNOMBRE_MES(int Mes) {
        return MESES[Mes];
    }


    public static final String getNombreSemana(int nD){
        String letraD="";
        switch (nD){
            case 1: letraD = "Domingo";
                break;
            case 2: letraD = "Lunes";
                break;
            case 3: letraD = "Martes";
                break;
            case 4: letraD = "Miércoles";
                break;
            case 5: letraD = "Jueves";
                break;
            case 6: letraD = "Viernes";
                break;
            case 7: letraD = "Sábado";
                break;

        }
        return letraD;
    }

    public static String FromTime_parseTIME_Exacto12hs(String tiempo) {
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm");
        String cc= ""+tiempo;
        try {
            cc = new SimpleDateFormat("hh:mm a").format(formatter.parse(""+tiempo));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cc;
    }


    public static String ParseDecimalFour(double numero){
        DecimalFormat df = new DecimalFormat("#.0000");
        return df.format(numero);
    }
    public static String ParseDecimalTwo(double numero){
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(numero);
    }
    private static String ParseDecimalFourText(double numero){
        DecimalFormat df = new DecimalFormat("#,###.0000");
        return df.format(numero);
    }

    public  static String GET_FECHA_ACTUAL_STRING(){
        Date date = new Date();
        SimpleDateFormat fechaHora = new SimpleDateFormat("yyyy-MM-dd");

        date.getTime();
        return fechaHora.format(date);
    }
    public static String  ConvertirKmToString(int metrosAll){
        String uniddad_medida="";
        if (metrosAll>0){
            int km=metrosAll/1000;
            int metros=metrosAll%1000;
            uniddad_medida=(km>0?(km+" km "):" ")+(metros>0?(metros+" m"):"");
        }else{
            uniddad_medida=metrosAll+" m";
        }
        return uniddad_medida;

    }

    public static long convertirFecha_to_long(String string_date)
    {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date d = f.parse(string_date);
            return d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static boolean IsDouble(String numero)
    {
        try {
            Double.parseDouble(numero);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean IsLatitudValido(String numero)
    {
        try {
            double num=Double.parseDouble(numero);
            if (num!=0){
                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String returnFechaDescripcion(String fecha){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date  = formatter.parse(fecha);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int dia_semana= calendar.get(Calendar.DAY_OF_WEEK);
            int dia= calendar.get(Calendar.DAY_OF_MONTH);
            int mes= calendar.get(Calendar.MONTH);
            int anio= calendar.get(Calendar.YEAR);
            return VARIABLES.getNombreSemana(dia_semana)+", "+dia+" de "+ VARIABLES.getNOMBRE_MES(mes)+" del "+anio;
        } catch (ParseException e) {
            e.printStackTrace();
            return fecha;
        }
    }
    //RETORNDA Date a partir de dateString
    public static Date parseDATA(String dateString) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.parse(""+dateString);
    }

    public static String OBTENER_DESCRIPCION_DIRECCIION_from_CORDENADA(Activity activity, double latitud, double longitud) {
        String direccion_larga="";
        //Obtener la direccion de la calle a partir de la latitud y la longitud
        if (latitud != 0.0 && longitud != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        latitud, longitud, 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);
                    direccion_larga=DirCalle.getAddressLine(0);
                }
                if (direccion_larga.length()==0) direccion_larga=""+latitud+", "+longitud;
            } catch (IOException e) {
                direccion_larga="No se ha podido obtener la dirreción de su ubición.\nCoordenada: "+latitud+", "+longitud;
                e.printStackTrace();
            }
        }
        return direccion_larga;
    }

    public static int GetSegundosFrom_hh_mm_to_int(String hora) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date date = sdf.parse(hora);
        long millis = date.getTime();
        return (int)(millis/1000);
    }

    public  static String GET_FECHA_ACTUAL_STRING_dd_mm_yyy2(){
        Date date = new Date();
        SimpleDateFormat fechaHora = new SimpleDateFormat("dd/MM/yyyy");
        return fechaHora.format(date);
    }

    public static String GetFechaStringFrom_dd_mm_yyyy_TO_yyyy_mm_dd(String fecha) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String cc= ""+fecha;
        try {
            cc = new SimpleDateFormat("yyyy-MM-dd").format(formatter.parse(""+fecha));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cc;
    }



}

