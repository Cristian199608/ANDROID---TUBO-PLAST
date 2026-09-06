package com.example.sm_tubo_plast.genesys.Retrofit.request.pedido;

import com.example.sm_tubo_plast.genesys.Retrofit.request.pedido.det.RequestAuditoriaPedidoSAP;
import com.example.sm_tubo_plast.genesys.Retrofit.request.pedido.det.RequestClientePedidoSAP;
import com.example.sm_tubo_plast.genesys.Retrofit.request.pedido.det.RequestComercialPedidoSAP;
import com.example.sm_tubo_plast.genesys.Retrofit.request.pedido.det.RequestContactoPedidoSAP;
import com.example.sm_tubo_plast.genesys.Retrofit.request.pedido.det.RequestDetallePedidoSAP;
import com.example.sm_tubo_plast.genesys.Retrofit.request.pedido.det.RequestEntregaPedidoSAP;
import com.example.sm_tubo_plast.genesys.Retrofit.request.pedido.det.RequestObservacionesPedidoSAP;
import com.example.sm_tubo_plast.genesys.Retrofit.request.pedido.det.RequestTotalesPedidoSAP;
import com.example.sm_tubo_plast.genesys.Retrofit.request.pedido.det.RequestTransportePedidoSAP;

import java.util.ArrayList;
import java.util.List;

public class RequestPedidoSAP {
    private String version_api;
    private String sistema_origen;
    private String oc_numero;
    private String tipo_registro;
    private String tipo_documento;
    private String fecha_pedido;
    private String estado;

    private RequestClientePedidoSAP cliente;
    private RequestEntregaPedidoSAP entrega;
    private RequestTransportePedidoSAP transporte;
    private RequestContactoPedidoSAP contacto;
    private RequestComercialPedidoSAP comercial;

    private int dias_vigencia;
    private String numero_orden_compra;
    private int numero_letras;

    private boolean aplica_pedido_anticipo;
    private List<Object> anticipos_aplicados;

    private boolean aplica_descuento;
    private boolean aplica_dsc_pronto_pago;
    private boolean aplica_dsc_siguiente_categoria;

    private boolean aplica_nota_credito;
    private List<Object> nota_credito_aplicado;

    private RequestTotalesPedidoSAP totales;
    private RequestObservacionesPedidoSAP observaciones;
    private RequestAuditoriaPedidoSAP auditoria;

    private List<RequestDetallePedidoSAP> detalles;

    public RequestPedidoSAP() {
        anticipos_aplicados = new ArrayList<>();
        nota_credito_aplicado = new ArrayList<>();
        detalles = new ArrayList<>();
    }

    public RequestPedidoSAP(
            String version_api,
            String sistema_origen,
            String oc_numero,
            String tipo_registro,
            String tipo_documento,
            String fecha_pedido,
            String estado,
            RequestClientePedidoSAP cliente,
            RequestEntregaPedidoSAP entrega,
            RequestTransportePedidoSAP transporte,
            RequestContactoPedidoSAP contacto,
            RequestComercialPedidoSAP comercial,
            int dias_vigencia,
            String numero_orden_compra,
            int numero_letras,
            boolean aplica_pedido_anticipo,
            List<Object> anticipos_aplicados,
            boolean aplica_descuento,
            boolean aplica_dsc_pronto_pago,
            boolean aplica_dsc_siguiente_categoria,
            boolean aplica_nota_credito,
            List<Object> nota_credito_aplicado,
            RequestTotalesPedidoSAP totales,
            RequestObservacionesPedidoSAP observaciones,
            RequestAuditoriaPedidoSAP auditoria,
            List<RequestDetallePedidoSAP> detalles
    ) {
        this.version_api = version_api;
        this.sistema_origen = sistema_origen;
        this.oc_numero = oc_numero;
        this.tipo_registro = tipo_registro;
        this.tipo_documento = tipo_documento;
        this.fecha_pedido = fecha_pedido;
        this.estado = estado;
        this.cliente = cliente;
        this.entrega = entrega;
        this.transporte = transporte;
        this.contacto = contacto;
        this.comercial = comercial;
        this.dias_vigencia = dias_vigencia;
        this.numero_orden_compra = numero_orden_compra;
        this.numero_letras = numero_letras;
        this.aplica_pedido_anticipo = aplica_pedido_anticipo;
        this.anticipos_aplicados = anticipos_aplicados;
        this.aplica_descuento = aplica_descuento;
        this.aplica_dsc_pronto_pago = aplica_dsc_pronto_pago;
        this.aplica_dsc_siguiente_categoria = aplica_dsc_siguiente_categoria;
        this.aplica_nota_credito = aplica_nota_credito;
        this.nota_credito_aplicado = nota_credito_aplicado;
        this.totales = totales;
        this.observaciones = observaciones;
        this.auditoria = auditoria;
        this.detalles = detalles;
    }

    // GETTERS Y SETTERS

    public String getVersion_api() {
        return version_api;
    }

    public void setVersion_api(String version_api) {
        this.version_api = version_api;
    }

    public String getSistema_origen() {
        return sistema_origen;
    }

    public void setSistema_origen(String sistema_origen) {
        this.sistema_origen = sistema_origen;
    }

    public String getOc_numero() {
        return oc_numero;
    }

    public void setOc_numero(String oc_numero) {
        this.oc_numero = oc_numero;
    }

    public String getTipo_registro() {
        return tipo_registro;
    }

    public void setTipo_registro(String tipo_registro) {
        this.tipo_registro = tipo_registro;
    }

    public String getTipo_documento() {
        return tipo_documento;
    }

    public void setTipo_documento(String tipo_documento) {
        this.tipo_documento = tipo_documento;
    }

    public String getFecha_pedido() {
        return fecha_pedido;
    }

    public void setFecha_pedido(String fecha_pedido) {
        this.fecha_pedido = fecha_pedido;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public RequestClientePedidoSAP getCliente() {
        return cliente;
    }

    public void setCliente(RequestClientePedidoSAP cliente) {
        this.cliente = cliente;
    }

    public RequestEntregaPedidoSAP getEntrega() {
        return entrega;
    }

    public void setEntrega(RequestEntregaPedidoSAP entrega) {
        this.entrega = entrega;
    }

    public RequestTransportePedidoSAP getTransporte() {
        return transporte;
    }

    public void setTransporte(RequestTransportePedidoSAP transporte) {
        this.transporte = transporte;
    }

    public RequestContactoPedidoSAP getContacto() {
        return contacto;
    }

    public void setContacto(RequestContactoPedidoSAP contacto) {
        this.contacto = contacto;
    }

    public RequestComercialPedidoSAP getComercial() {
        return comercial;
    }

    public void setComercial(RequestComercialPedidoSAP comercial) {
        this.comercial = comercial;
    }

    public int getDias_vigencia() {
        return dias_vigencia;
    }

    public void setDias_vigencia(int dias_vigencia) {
        this.dias_vigencia = dias_vigencia;
    }

    public String getNumero_orden_compra() {
        return numero_orden_compra;
    }

    public void setNumero_orden_compra(String numero_orden_compra) {
        this.numero_orden_compra = numero_orden_compra;
    }

    public int getNumero_letras() {
        return numero_letras;
    }

    public void setNumero_letras(int numero_letras) {
        this.numero_letras = numero_letras;
    }

    public boolean isAplica_pedido_anticipo() {
        return aplica_pedido_anticipo;
    }

    public void setAplica_pedido_anticipo(boolean aplica_pedido_anticipo) {
        this.aplica_pedido_anticipo = aplica_pedido_anticipo;
    }

    public List<Object> getAnticipos_aplicados() {
        return anticipos_aplicados;
    }

    public void setAnticipos_aplicados(List<Object> anticipos_aplicados) {
        this.anticipos_aplicados = anticipos_aplicados;
    }

    public boolean isAplica_descuento() {
        return aplica_descuento;
    }

    public void setAplica_descuento(boolean aplica_descuento) {
        this.aplica_descuento = aplica_descuento;
    }

    public boolean isAplica_dsc_pronto_pago() {
        return aplica_dsc_pronto_pago;
    }

    public void setAplica_dsc_pronto_pago(boolean aplica_dsc_pronto_pago) {
        this.aplica_dsc_pronto_pago = aplica_dsc_pronto_pago;
    }

    public boolean isAplica_dsc_siguiente_categoria() {
        return aplica_dsc_siguiente_categoria;
    }

    public void setAplica_dsc_siguiente_categoria(boolean aplica_dsc_siguiente_categoria) {
        this.aplica_dsc_siguiente_categoria = aplica_dsc_siguiente_categoria;
    }

    public boolean isAplica_nota_credito() {
        return aplica_nota_credito;
    }

    public void setAplica_nota_credito(boolean aplica_nota_credito) {
        this.aplica_nota_credito = aplica_nota_credito;
    }

    public List<Object> getNota_credito_aplicado() {
        return nota_credito_aplicado;
    }

    public void setNota_credito_aplicado(List<Object> nota_credito_aplicado) {
        this.nota_credito_aplicado = nota_credito_aplicado;
    }

    public RequestTotalesPedidoSAP getTotales() {
        return totales;
    }

    public void setTotales(RequestTotalesPedidoSAP totales) {
        this.totales = totales;
    }

    public RequestObservacionesPedidoSAP getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(RequestObservacionesPedidoSAP observaciones) {
        this.observaciones = observaciones;
    }

    public RequestAuditoriaPedidoSAP getAuditoria() {
        return auditoria;
    }

    public void setAuditoria(RequestAuditoriaPedidoSAP auditoria) {
        this.auditoria = auditoria;
    }

    public List<RequestDetallePedidoSAP> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<RequestDetallePedidoSAP> detalles) {
        this.detalles = detalles;
    }
}