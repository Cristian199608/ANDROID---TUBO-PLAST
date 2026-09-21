package com.example.sm_tubo_plast.genesys.Retrofit.request.pedido.det;

public class RequestAnticipoPedidoSAP {
    String documento;
    double importe;

    public RequestAnticipoPedidoSAP(String documento, double importe) {
        this.documento = documento;
        this.importe = importe;
    }
}
