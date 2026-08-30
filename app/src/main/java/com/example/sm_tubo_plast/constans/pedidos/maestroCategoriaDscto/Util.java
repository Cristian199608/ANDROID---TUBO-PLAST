package com.example.sm_tubo_plast.constans.pedidos.maestroCategoriaDscto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Util {
    public static MaestroCategoriaDescuento getDataParaTodoBy(String nombrePrincipal,
                                                              String categoria,
                                                              double dscto_pct){
        ArrayList<Opcion> opciones = new ArrayList<>(
                Collections.singletonList(
                        new Opcion(
                                "DESCUENTO CATEGORIA "+categoria,
                                new Condicion(
                                        Collections.singletonList("TODOS"),
                                        new ArrayList<String>(),
                                        0+dscto_pct,
                                        null
                                )
                        )
                )
        );

        return new MaestroCategoriaDescuento(
                categoria,
                ""+nombrePrincipal,
                ""+categoria,
                null,
                opciones
        );

    }
    public static MaestroCategoriaDescuento getDataREGIONAL(boolean isAddAdicional){
        Opcion adicional = new Opcion(
                "DESCUENTO ADICIONAL SOLO MARCA CANTOL",
                new Condicion(
                        Collections.singletonList("CANTOL"),
                        new ArrayList<>(),
                        2.00,
                        new Condicion(
                                Collections.singletonList("CONTADO"),
                                new ArrayList<>(),
                                //Collections.singletonList("CONTADO"),
                                2.00,
                                null
                                )
                )
        );

        ArrayList<Opcion> opciones = new ArrayList<>(
                Arrays.asList(
                        new Opcion("DESCUENTO CATEGORIA MARCA (todas, excepto marca LGO)",
                                new Condicion(
                                        Collections.singletonList("TODOS"),
                                        Collections.singletonList("LGO"),
                                        11.50,
                                        null
                                )
                        ),
                        new Opcion("DESCUENTO CATEGORIA MARCA LGO",
                                new Condicion(
                                        Collections.singletonList("LGO"),
                                        new ArrayList<String>(),
                                        15.50,
                                        null
                                )
                        )
                )
        );

        return new MaestroCategoriaDescuento(
                "REGIONAL"+isAddAdicional,
                    MaestroCategoriaDescuento.NOMBRE_CANAL_FERRETERIA,
                    "REGIONAL",
                    isAddAdicional?adicional: null,
                    opciones
            );

    }

    public static MaestroCategoriaDescuento getDataCONSORCIO_INMOBILIARIO(){
        ArrayList<Opcion> opciones = new ArrayList<>(
                Arrays.asList(
                        new Opcion("DESCUENTO CATEGORIA GENERAL",
                                new Condicion(
                                        Collections.singletonList("TODOS"),
                                        new ArrayList<>(),
                                        20.00,
                                        null
                                )
                        ),
                        new Opcion("DESCUENTO ADICIONAL SOLO MARCA CANTOL",
                                new Condicion(
                                        Collections.singletonList("CANTOL"),
                                        new ArrayList<>(),
                                        2.00,
                                        null
                                )
                        )
                )
        );

        return new MaestroCategoriaDescuento(
                "CONSORCIO INMOBILIARIO",
                MaestroCategoriaDescuento.NOMBRE_CANAL_INSTITUCIONAL,
                "CONSORCIO INMOBILIARIO",
                null,
                opciones
        );

    }

}
