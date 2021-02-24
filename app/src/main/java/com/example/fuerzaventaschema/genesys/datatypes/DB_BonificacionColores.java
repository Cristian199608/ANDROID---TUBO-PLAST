package com.example.fuerzaventaschema.genesys.datatypes;

public class DB_BonificacionColores {
	private String secuencia;
	private String item;
	private String cc_artic;
	private String Producto;
	
	
	public DB_BonificacionColores(String secuencia, String item,
			String cc_artic, String producto) {
		
		this.secuencia = secuencia;
		this.item = item;
		this.cc_artic = cc_artic;
		Producto = producto;
		
	}
	
	public DB_BonificacionColores() {		
	}

	public String getSecuencia() {
		return secuencia;
	}
	public void setSecuencia(String secuencia) {
		this.secuencia = secuencia;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public String getCc_artic() {
		return cc_artic;
	}
	public void setCc_artic(String cc_artic) {
		this.cc_artic = cc_artic;
	}
	public String getProducto() {
		return Producto;
	}
	public void setProducto(String producto) {
		Producto = producto;
	}
	
	
}
