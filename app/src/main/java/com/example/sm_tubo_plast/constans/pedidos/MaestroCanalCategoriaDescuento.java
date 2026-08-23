package com.example.sm_tubo_plast.constans.pedidos;

import java.util.ArrayList;
import java.util.HashMap;

public class MaestroCanalCategoriaDescuento {
    public static String NOMBRE_CANAL_FERRETERIA="FERRETERIAS";
    public static String NOMBRE_CANAL_INSTITUCIONAL="INSTITUCIONALES";
    public static String NOMBRE_CANAL_CLIENT="ATENCION AL CLIENTE";

    public static String KEY_NOMBRE_FERRETERIA_PRECIO="Lista de Venta Ferreterías";
    public static String KEY_NOMBRE_INSTITUCIO_PRECIO="Lista de Venta Institucional";

    String nombreLitaPrecio;
    String canal;
    String sub_canal;
    HashMap<String, Double> dsctoList;
    double dscto_pct;

    public MaestroCanalCategoriaDescuento(String nombreLitaPrecio, String canal, String sub_canal, double dscto_pct) {
        this.nombreLitaPrecio = nombreLitaPrecio;
        this.canal = canal;
        this.sub_canal = sub_canal;
        this.dscto_pct = dscto_pct;

        HashMap<String, Double> dsctoList=new HashMap<>();
        dsctoList.put("LGO", 15.50);
        dsctoList.put("TODOS", 11.50);
        dsctoList.put("ADD", 2.0);
    }

    public String getNombreLitaPrecio() {
        return nombreLitaPrecio;
    }

    public String getCanal() {
        return canal;
    }

    public String getSub_canal() {
        return sub_canal;
    }

    public double getDscto_pct() {
        return dscto_pct;
    }

    public static ArrayList<MaestroCanalCategoriaDescuento> getDataListDscto(){
        ArrayList<MaestroCanalCategoriaDescuento> lista =new ArrayList<>();
        lista.add(new MaestroCanalCategoriaDescuento(KEY_NOMBRE_FERRETERIA_PRECIO, NOMBRE_CANAL_FERRETERIA, "FERRETERIA C", 5.00));
        lista.add(new MaestroCanalCategoriaDescuento(KEY_NOMBRE_FERRETERIA_PRECIO,NOMBRE_CANAL_FERRETERIA, "FERRETERIA B", 6.50));
        lista.add(new MaestroCanalCategoriaDescuento(KEY_NOMBRE_FERRETERIA_PRECIO,NOMBRE_CANAL_FERRETERIA, "FERRETERIA A", 7.50));
        lista.add(new MaestroCanalCategoriaDescuento(KEY_NOMBRE_FERRETERIA_PRECIO,NOMBRE_CANAL_FERRETERIA, "MAYORISTA", 9.00));
        lista.add(new MaestroCanalCategoriaDescuento(KEY_NOMBRE_FERRETERIA_PRECIO,NOMBRE_CANAL_FERRETERIA, "DISTRIBUIDOR", 10.50));
        lista.add(new MaestroCanalCategoriaDescuento(KEY_NOMBRE_FERRETERIA_PRECIO,NOMBRE_CANAL_FERRETERIA, "REGIONAL", 10.50));

        lista.add(new MaestroCanalCategoriaDescuento(KEY_NOMBRE_INSTITUCIO_PRECIO,NOMBRE_CANAL_INSTITUCIONAL, "CONSTRUCTORA C", 5.00));
        lista.add(new MaestroCanalCategoriaDescuento(KEY_NOMBRE_INSTITUCIO_PRECIO,NOMBRE_CANAL_INSTITUCIONAL, "CONSTRUCTORA B", 5.50));
        lista.add(new MaestroCanalCategoriaDescuento(KEY_NOMBRE_INSTITUCIO_PRECIO,NOMBRE_CANAL_INSTITUCIONAL, "CONSTRUCTORA A", 10.00));
        lista.add(new MaestroCanalCategoriaDescuento(KEY_NOMBRE_INSTITUCIO_PRECIO,NOMBRE_CANAL_INSTITUCIONAL, "INMOBILIARIA", 15));
        return lista;
    }
}
