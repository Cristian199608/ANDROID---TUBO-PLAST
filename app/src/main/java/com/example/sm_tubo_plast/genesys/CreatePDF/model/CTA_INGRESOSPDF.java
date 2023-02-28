package com.example.sm_tubo_plast.genesys.CreatePDF.model;

public class CTA_INGRESOSPDF {
        public String serie_doc;
        public String numero_factura;
        public String feccom;
        public String fecha_despacho;
        public String fecha_vencimiento;
        public String codigo_equivalente;
        public String total;
        public String codcli;
        public String nomcli;
        public String deuda;
        public String obligacion;

        public String getObligacion() {
            return obligacion;
        }

        public void setObligacion(String obligacion) {
            this.obligacion = obligacion;
        }

        public String getDeuda() {
            return deuda;
        }

        public void setDeuda(String deuda) {
            this.deuda = deuda;
        }

        public String getCodcli() {
            return codcli;
        }

        public void setCodcli(String codcli) {
            this.codcli = codcli;
        }

        public String getNomcli() {
            return nomcli;
        }

        public void setNomcli(String nomcli) {
            this.nomcli = nomcli;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getSerie_doc() {
            return serie_doc;
        }

        public void setSerie_doc(String serie_doc) {
            this.serie_doc = serie_doc;
        }

        public String getNumero_factura() {
            return numero_factura;
        }

        public void setNumero_factura(String numero_factura) {
            this.numero_factura = numero_factura;
        }

        public String getFeccom() {
            return feccom;
        }

        public void setFeccom(String feccom) {
            this.feccom = feccom;
        }

        public String getFecha_despacho() {
            return fecha_despacho;
        }

        public void setFecha_despacho(String fecha_despacho) {
            this.fecha_despacho = fecha_despacho;
        }

        public String getFecha_vencimiento() {
            return fecha_vencimiento;
        }

        public void setFecha_vencimiento(String fecha_vencimiento) {
            this.fecha_vencimiento = fecha_vencimiento;
        }

        public String getCodigo_equivalente() {
            return codigo_equivalente;
        }

        public void setCodigo_equivalente(String codigo_equivalente) {
            this.codigo_equivalente = codigo_equivalente;
        }
}

