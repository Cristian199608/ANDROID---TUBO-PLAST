package com.example.fuerzaventaschema.genesys.BEAN;

public class ItemPedido {
	
	private String cod_pro;
	private int cantidad;
	private double und_medida;
	private double prec_unit;
	private double sub_total;
	
	
	public String getCod_pro() {
		return cod_pro;
	}
	public void setCod_pro(String cod_pro) {
		this.cod_pro = cod_pro;
	}
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	public double getUnd_medida() {
		return und_medida;
	}
	public void setUnd_medida(double und_medida) {
		this.und_medida = und_medida;
	}
	public double getPrec_unit() {
		return prec_unit;
	}
	public void setPrec_unit(double prec_unit) {
		this.prec_unit = prec_unit;
	}
	public double getSub_total() {
		return sub_total;
	}
	public void setSub_total(double sub_total) {
		this.sub_total = sub_total;
	}
	
	

}
