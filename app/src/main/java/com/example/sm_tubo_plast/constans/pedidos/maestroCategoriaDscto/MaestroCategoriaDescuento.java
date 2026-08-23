package com.example.sm_tubo_plast.constans.pedidos.maestroCategoriaDscto;

import java.util.ArrayList;
import java.util.List;

public class MaestroCategoriaDescuento {
    public static String NOMBRE_CANAL_FERRETERIA="FERRETERIAS";
    public static String NOMBRE_CANAL_INSTITUCIONAL="INSTITUCIONALES";
    public static String NOMBRE_CANAL_CLIENT="ATENCION AL CLIENTE";

    public static String KEY_NOMBRE_FERRETERIA_PRECIO="Lista de Venta Ferreterías";
    public static String KEY_NOMBRE_INSTITUCIO_PRECIO="Lista de Venta Institucional";


    private String keyUnico;
    private String canal;
        private String categoria;
        private Opcion adicional;
        private ArrayList<Opcion> opciones;

        public MaestroCategoriaDescuento(String keyUnico, String canal, String categoria,
                                  Opcion adicional, ArrayList<Opcion> opciones) {
            this.keyUnico=keyUnico;
            this.canal = canal;
            this.categoria = categoria;
            this.adicional = adicional;
            this.opciones = opciones;
        }

    public String getKeyUnico() {
        return keyUnico;
    }

    public String getCanal() {
            return canal;
        }

        public String getCategoria() {
            return categoria;
        }

        public Opcion getAdicional() {
            return adicional;
        }
        public List<Opcion> getOpciones() {
            return opciones;
        }

    public static ArrayList<MaestroCategoriaDescuento> getDataListDscto(){
        ArrayList<MaestroCategoriaDescuento> lista =new ArrayList<>();
        lista.add(Util.getDataParaTodoBy(NOMBRE_CANAL_FERRETERIA, "Sin Categoria",0.0));
        lista.add(Util.getDataParaTodoBy(NOMBRE_CANAL_FERRETERIA, "FERRETERIA C",5.00));
        lista.add(Util.getDataParaTodoBy(NOMBRE_CANAL_FERRETERIA, "FERRETERIA B",6.50));
        lista.add(Util.getDataParaTodoBy(NOMBRE_CANAL_FERRETERIA, "FERRETERIA A",7.50));
        lista.add(Util.getDataParaTodoBy(NOMBRE_CANAL_FERRETERIA, "MAYORISTA",9.00));
        lista.add(Util.getDataParaTodoBy(NOMBRE_CANAL_FERRETERIA, "DISTRIBUIDOR",10.50));
        lista.add(Util.getDataREGIONAL(false));
        lista.add(Util.getDataREGIONAL(true));

        lista.add(Util.getDataParaTodoBy(NOMBRE_CANAL_INSTITUCIONAL, "Sin Categoria",0.0));
        lista.add(Util.getDataParaTodoBy(NOMBRE_CANAL_INSTITUCIONAL, "CONSTRUCTORA C", 5.00));
        lista.add(Util.getDataParaTodoBy(NOMBRE_CANAL_INSTITUCIONAL, "CONSTRUCTORA B", 5.50));
        lista.add(Util.getDataParaTodoBy(NOMBRE_CANAL_INSTITUCIONAL, "CONSTRUCTORA A", 10.00));
        lista.add(Util.getDataParaTodoBy(NOMBRE_CANAL_INSTITUCIONAL, "INMOBILIARIA", 15));
        lista.add(Util.getDataCONSORCIO_INMOBILIARIO());
        return lista;
    }
    }