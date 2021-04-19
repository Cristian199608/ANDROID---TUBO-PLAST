package com.example.sm_tubo_plast.genesys.datatypes;

public class DBChofer {

	private String codigo_chofer;
	public String getCodigo_chofer() {
		return codigo_chofer;
	}
	public void setCodigo_chofer(String codigo_chofer) {
		this.codigo_chofer = codigo_chofer;
	}
	public String getNombre_chofer() {
		return nombre_chofer;
	}
	public void setNombre_chofer(String nombre_chofer) {
		this.nombre_chofer = nombre_chofer;
	}
	public String getNro_brevete() {
		return nro_brevete;
	}
	public void setNro_brevete(String nro_brevete) {
		this.nro_brevete = nro_brevete;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public int getUsecod() {
		return usecod;
	}
	public void setUsecod(int usecod) {
		this.usecod = usecod;
	}
	private String nombre_chofer;
	private String nro_brevete;
	private String categoria;
	private int usecod;
}
