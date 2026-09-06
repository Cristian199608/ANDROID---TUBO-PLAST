package com.example.sm_tubo_plast.genesys.Retrofit.request.pedido.det;

public class RequetDescuentoPedidoSAP {

    private String tipo;
    private double porcentaje;
    private double importe_descuento;

    public RequetDescuentoPedidoSAP() {
    }

    public RequetDescuentoPedidoSAP(String tipo,
                                    double porcentaje,
                                    double importe_descuento) {

        this.tipo = tipo;
        this.porcentaje = porcentaje;
        this.importe_descuento = importe_descuento;
    }

    // getters y setters
}