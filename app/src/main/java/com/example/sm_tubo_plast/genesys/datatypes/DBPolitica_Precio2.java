package com.example.sm_tubo_plast.genesys.datatypes;

public class DBPolitica_Precio2 {
	
	private int secuencia;
	private int item;
	private String codpro;
	private double prepro;
	private double prepo_unidad;
	private double precio_base;

	
	public double getPrepo_unidad() {
		return prepo_unidad;
	}
	public void setPrepo_unidad(double prepo_unidad) {
		this.prepo_unidad = prepo_unidad;
	}
	public int getSecuencia() {
		return secuencia;
	}
	public void setSecuencia(int secuencia) {
		this.secuencia = secuencia;
	}
	public int getItem() {
		return item;
	}
	public void setItem(int item) {
		this.item = item;
	}
	public String getCodpro() {
		return codpro;
	}
	public void setCodpro(String codpro) {
		this.codpro = codpro;
	}
	public double getPrepro() {
		return prepro;
	}
	public void setPrepro(double prepro) {
		this.prepro = prepro;
	}

	public double getPrecio_base() {
		return precio_base;
	}

	public void setPrecio_base(double precio_base) {
		this.precio_base = precio_base;
	}
}
