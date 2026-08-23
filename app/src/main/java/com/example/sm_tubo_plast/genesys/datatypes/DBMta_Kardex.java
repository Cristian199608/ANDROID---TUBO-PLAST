package com.example.sm_tubo_plast.genesys.datatypes;

public class DBMta_Kardex{
	
	private String kardex;
	private String codalm;
	private String codpro;
	private double stock;
	private double xtemp;
	private double transito;
	private double disponible;

	
	public String getKardex() {
		return kardex;
	}
	public void setKardex(String kardex) {
		this.kardex = kardex;
	}
	public String getCodalm() {
		return codalm;
	}
	public void setCodalm(String codalm) {
		this.codalm = codalm;
	}
	public String getCodpro() {
		return codpro;
	}
	public void setCodpro(String codpro) {
		this.codpro = codpro;
	}
	public double getStock() {
		return stock;
	}
	public void setStock(double stock) {
		this.stock = stock;
	}
	public double getXtemp() {
		return xtemp;
	}
	public void setXtemp(double xtemp) {
		this.xtemp = xtemp;
	}

	public double getTransito() {
		return transito;
	}

	public void setTransito(double transito) {
		this.transito = transito;
	}

	public double getDisponible() {
		return disponible;
	}

	public void setDisponible(double disponible) {
		this.disponible = disponible;
	}
}
