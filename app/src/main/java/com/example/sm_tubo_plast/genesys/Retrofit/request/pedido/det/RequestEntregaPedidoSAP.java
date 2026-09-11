package com.example.sm_tubo_plast.genesys.Retrofit.request.pedido.det;

public class RequestEntregaPedidoSAP {

        private String fecha_entrega;
        private String codigo_punto_entrega;
        private String codigo_turno;
        private String codigo_prioridad;
        private String codigo_tipo_despacho;
        private boolean aplica_cliente_recoge;
        private boolean aplica_embalaje;
        private boolean aplica_servicio_instalacion;
        private String codigo_almacen;

        public RequestEntregaPedidoSAP() {
        }

        public RequestEntregaPedidoSAP(String fecha_entrega, String codigo_punto_entrega,
                       String codigo_turno, String codigo_prioridad,
                       String codigo_tipo_despacho,
                       boolean aplica_cliente_recoge,
                       boolean aplica_embalaje,
                       boolean aplica_servicio_instalacion,
                       String codigo_almacen) {

            this.fecha_entrega = fecha_entrega;
            this.codigo_punto_entrega = codigo_punto_entrega;
            this.codigo_turno = codigo_turno;
            this.codigo_prioridad = codigo_prioridad;
            this.codigo_tipo_despacho = codigo_tipo_despacho;
            this.aplica_cliente_recoge = aplica_cliente_recoge;
            this.aplica_embalaje = aplica_embalaje;
            this.aplica_servicio_instalacion = aplica_servicio_instalacion;
            this.codigo_almacen = codigo_almacen;
        }

    public String getFecha_entrega() {
        return fecha_entrega;
    }

    public void setFecha_entrega(String fecha_entrega) {
        this.fecha_entrega = fecha_entrega;
    }

    public String getCodigo_punto_entrega() {
        return codigo_punto_entrega;
    }

    @Deprecated //no se debe usar
    public void setCodigo_punto_entrega(String codigo_punto_entrega) {
        this.codigo_punto_entrega = codigo_punto_entrega;
    }

    public String getCodigo_turno() {
        return codigo_turno;
    }

    public void setCodigo_turno(String codigo_turno) {
        this.codigo_turno = codigo_turno;
    }

    public String getCodigo_prioridad() {
        return codigo_prioridad;
    }

    public void setCodigo_prioridad(String codigo_prioridad) {
        this.codigo_prioridad = codigo_prioridad;
    }

    public String getCodigo_tipo_despacho() {
        return codigo_tipo_despacho;
    }

    public void setCodigo_tipo_despacho(String codigo_tipo_despacho) {
        this.codigo_tipo_despacho = codigo_tipo_despacho;
    }

    public boolean isAplica_cliente_recoge() {
        return aplica_cliente_recoge;
    }

    public void setAplica_cliente_recoge(boolean aplica_cliente_recoge) {
        this.aplica_cliente_recoge = aplica_cliente_recoge;
    }

    public boolean isAplica_embalaje() {
        return aplica_embalaje;
    }

    public void setAplica_embalaje(boolean aplica_embalaje) {
        this.aplica_embalaje = aplica_embalaje;
    }

    public boolean isAplica_servicio_instalacion() {
        return aplica_servicio_instalacion;
    }

    public void setAplica_servicio_instalacion(boolean aplica_servicio_instalacion) {
        this.aplica_servicio_instalacion = aplica_servicio_instalacion;
    }

    public String getCodigo_almacen() {
        return codigo_almacen;
    }

    public void setCodigo_almacen(String codigo_almacen) {
        this.codigo_almacen = codigo_almacen;
    }
}