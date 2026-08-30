package com.example.sm_tubo_plast.genesys.BEAN;

public class FormaPago {
	String codigoFormaPago;
	String descripcionFormaPago;
	int dias_credito;

	public String getCodigoFormaPago() {
		return codigoFormaPago;
	}
	public void setCodigoFormaPago(String codigoFormaPago) {
		this.codigoFormaPago = codigoFormaPago;
	}
	public String getDescripcionFormaPago() {
		return descripcionFormaPago;
	}
	public void setDescripcionFormaPago(String descripcionFormaPago) {
		this.descripcionFormaPago = descripcionFormaPago;
	}

	public int getDias_credito() {
		return dias_credito;
	}

	public void setDias_credito(int dias_credito) {
		this.dias_credito = dias_credito;
	}
}
