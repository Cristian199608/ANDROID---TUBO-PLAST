package com.example.sm_tubo_plast.genesys.datatypes;

public class DB_DireccionClientes {

	private String codcli;
	public String getCodcli() {
		return codcli;
	}
	public void setCodcli(String codcli) {
		this.codcli = codcli;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getCoddep() {
		return coddep;
	}
	public void setCoddep(String coddep) {
		this.coddep = coddep;
	}
	public String getCodprv() {
		return codprv;
	}
	public void setCodprv(String codprv) {
		this.codprv = codprv;
	}
	public String getUbigeo() {
		return ubigeo;
	}
	public void setUbigeo(String ubigeo) {
		this.ubigeo = ubigeo;
	}
	public String getDes_corta() {
		return des_corta;
	}
	public void setDes_corta(String des_corta) {
		this.des_corta = des_corta;
	}
	private String estado;
	private int giroCliente;
	private double altitud;

	public String getLatitud() {
		if (latitud==null)latitud="0";
		if (latitud.length()==0)latitud="0";
		return latitud;
	}
	public void setLatitud(String latitud) {
		this.latitud = latitud;
	}
	public String getLongitud() {
		if (longitud==null) longitud="0";
		if (longitud.length()==0) longitud="0";
		return longitud;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public int getGiroCliente() {
		return giroCliente;
	}
	public void setGiroCliente(int giroCliente) {
		this.giroCliente = giroCliente;
	}

	public void setLongitud(String longitud) {
		this.longitud = longitud;
	}

	public double getAltitud() {
		return altitud;
	}

	public void setAltitud(double altitud) {
		this.altitud = altitud;
	}

	private String item;
	private String direccion;
	private String telefono;
	private String coddep;
	private String codprv;
	private String ubigeo;
	private String des_corta;
	private String latitud;
	private String longitud;
}
