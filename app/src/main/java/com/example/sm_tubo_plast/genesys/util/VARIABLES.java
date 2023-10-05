package com.example.sm_tubo_plast.genesys.util;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.util.Patterns;

import com.example.sm_tubo_plast.genesys.fuerza_ventas.GestionVisita3Activity;
import com.example.sm_tubo_plast.genesys.service.ConnectionDetector;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

public class VARIABLES {

    public static final  int ID_ENVIO_FALLIDA=-1;
    public static final  int ID_ENVIO_EXITOSA=-2;
    public static final  String SEPARADOR_OBSERVACION="_#_";


    public static final  boolean isProduccion=false;
    public static final  boolean isProduccion_prueba=false;

    private static TimeZone GetTimeZone(){
        return TimeZone.getTimeZone("GMT-05");
    }
    public static String convertirFromLongToString(long longFecha) {
        Date date=new Date();
        date.setTime(longFecha);

        SimpleDateFormat fechaHora = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa");
        fechaHora.setTimeZone(GetTimeZone());
        return fechaHora.format(date);
    }

    public static final class ConfigDatabase {

//        private static final String DATABASA_NAME_OLD ="fuerzaventas";
        private static final String DATABASA_NAME_OLD   ="fuerzaventas_v1x";
        private static final String DATABASA_NAME       ="fuerzaventas_v2";
        private static final  int DATABASA_VERSION      =1;


        private static final String DATABASA_NAMEO_OLD_prueba   ="vacio";
        private static final String DATABASA_NAME_prueba        ="fuerzaventas_prueba";
        private static final  int DATABASA_VERSION_prueba       =1;

        public static String getDatabaseNameOld() {
            if(isProduccion) return DATABASA_NAME_OLD;
            else return DATABASA_NAMEO_OLD_prueba;
        }
        public static String getDatabaseName() {
            if(isProduccion) return DATABASA_NAME;
            else return DATABASA_NAME_prueba;
        }
        public static int getDatabaseVersion() {
            if(isProduccion) return DATABASA_VERSION;
            else return DATABASA_VERSION_prueba;
        }

    }


    public static final  String[] MESES = {"Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre"
            ,"Octubre","Noviembre","Diciembre"};

    //[sunat_exportar_reporte_ple_registro_copia]
    public static final TimeZone zona_horaria=TimeZone.getTimeZone("GMT-5");
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
    public  static String GET_HORA_ACTUAL_STRING(){
        Date date = new Date();
        SimpleDateFormat fechaHora = new SimpleDateFormat("HH:mm");

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

    public static String convertirFecha_from_long(long date)
    {
        SimpleDateFormat formatFecha = new SimpleDateFormat("yyy-MM-dd hh:mm a");
        formatFecha.setTimeZone(zona_horaria);
        String fecha = formatFecha.format(new Date(date));
        return fecha;
    }

    public static String convertirFecha_from_String(String date)
    {
        SimpleDateFormat formatterIn = new SimpleDateFormat("yyy-MM-dd HH:mm");
        String fecha= date;
        try {
            fecha = new SimpleDateFormat("yyy-MM-dd hh:mm a").format(formatterIn.parse(""+date));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return fecha;
    }

    public static long convertirFecha_to_longFromYYYY_MM_DD(String string_date)
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
    public static long convertirFecha_to_long(String string_date)
    {
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");

        try {
            Date d = f.parse(string_date);
            return d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }


    public static long convertirFechaHora_dd_mm_yyyy__HH_mm_to_long(String string_date)
    {
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        try {
            f.setTimeZone(zona_horaria);
            Date d = f.parse(string_date);

            return d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static long GetFechaActua_long(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT-14"));
        Date date = new Date();
        return date.getTime();
    }
    public static long getFechaHora_actual_long()
    {
        Date d = new Date();
        return d.getTime();
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
        /*if (latitud != 0.0 && longitud != 0.0) {
            try {
                ConnectionDetector cd=new ConnectionDetector(activity);
                if (cd.hasActiveInternetConnection(activity)) {
                    Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
                    List<Address> list = geocoder.getFromLocation(
                            latitud, longitud, 1);
                    if (!list.isEmpty()) {
                        Address DirCalle = list.get(0);
                        direccion_larga=DirCalle.getAddressLine(0);
                    }
                    if (direccion_larga.length()==0) direccion_larga=""+latitud+", "+longitud;
                }else{
                    direccion_larga="No se obtuvo la direccion pues, no estas conectado a internet";
                }
            } catch (IOException e) {
                direccion_larga="No se ha podido obtener la dirreción de su ubición.\nCoordenada: "+latitud+", "+longitud;
                e.printStackTrace();
            }
        }*/
        return direccion_larga;
    }

    public static int GetSegundosFrom_hh_mm_to_int(String hora) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        sdf.setTimeZone(zona_horaria);
        Date date = sdf.parse(hora);
        long millis = date.getTime();
        return (int)(millis/1000);
    }

    public static int GetSegundosFrom_hh_mm_to_int2(int hora, int minutos) throws ParseException {

        int h_segundos=hora*3600;
        int m_segundos=minutos*60;

        return h_segundos+m_segundos;
    }

    public  static String GET_FECHA_ACTUAL_STRING_dd_mm_yyy2(){
        Date date = new Date();
        SimpleDateFormat fechaHora = new SimpleDateFormat("dd/MM/yyyy");
        return fechaHora.format(date);
    }
    public  static String GET_FECHA_ACTUAL_STRING_dd_mm_yyy(){
        Date date = new Date();
        SimpleDateFormat fechaHora = new SimpleDateFormat("dd-MM-yyyy");
        return fechaHora.format(date);
    }
    public  static String GET_FECHA_DIA_INICIO_MES_STRING_dd_mm_yyy(){
        //Devuelve la fecha primer dia del mes actual
        Date date = new Date();
        SimpleDateFormat fechaHora = new SimpleDateFormat("01-MM-yyyy");
        return fechaHora.format(date);
    }
    public  static String GET_FECHA_ACTUAL_STRING_dd_mm_yyy(int restarDia){
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -restarDia);
        Date date =calendar.getTime();
        SimpleDateFormat fechaHora = new SimpleDateFormat("dd-MM-yyyy");
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

    public static long GetFechaLongFrom_yyyy_mm_dd(String fecha) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        try {
            formatter.setTimeZone(zona_horaria);
            Date d = formatter.parse(fecha);
            return d.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static  boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }


    public static String GetFechaActual(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT-14"));
        Date date = new Date();
        return dateFormat.format(date);
    }
    public static String GetFechaHoraActual(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd HH:mm");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT-14"));
        Date date = new Date();
        return dateFormat.format(date);
    }
    public static long sumarDiasFromFechaLong(long fecha, int nroDia)
    {
        long _1DiaMilesegundos=86400000;
        return  fecha+(nroDia*_1DiaMilesegundos);
    }

    public static String getStringFormaterThreeDecimal(double numero)
    {
       DecimalFormat formater = new DecimalFormat("#,##0.000");
       return  formater.format(numero);
    }
    public static double getDoubleFormaterFourDecimal(double numero)
    {
        DecimalFormat formater = new DecimalFormat("###0.0000");
        return  Double.parseDouble(formater.format(numero));
    }

    public static      DecimalFormat formater_thow_decimal = new DecimalFormat("#,##0.00");
    public static      DecimalFormat formater_one_decimal = new DecimalFormat("#,##0.0");

    public static  boolean isDate(String dateString){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date = formatter.parse(""+dateString);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public static  String InvertirFechaCustom(String fecha, String patronOld, String patronNew){
        String[] fechas= fecha.split(patronOld);
        return fechas[2]+patronNew+fechas[1]+patronNew+fechas[0];
    }
    public static ArrayList<String> GetListString(String texto, int canRequired){
        ArrayList<String> list=new ArrayList<>();
        String[] listaString= texto.split(SEPARADOR_OBSERVACION);
        Collections.addAll(list, listaString);
        if(list.size()<canRequired){
            for (int i = 0; i <canRequired-listaString.length; i++) {
                list.add("");
            }
        }
        return list;
    }

}

