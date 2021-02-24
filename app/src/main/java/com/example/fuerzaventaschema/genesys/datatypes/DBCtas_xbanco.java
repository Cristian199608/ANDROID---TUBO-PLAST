package com.example.fuerzaventaschema.genesys.datatypes;

public class DBCtas_xbanco {

	private String secuencia;
	private String codban;
	private int item;
	private String codmon;
	private String cta_cte;
	private String estado;
	private String cuenta;
	
	public String getCuenta() {
		return cuenta;
	}
	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}
	public String getSecuencia() {
		return secuencia;
	}
	public void setSecuencia(String secuencia) {
		this.secuencia = secuencia;
	}
	public String getCodban() {
		return codban;
	}
	public void setCodban(String codban) {
		this.codban = codban;
	}
	public int getItem() {
		return item;
	}
	public void setItem(int item) {
		this.item = item;
	}
	public String getCodmon() {
		return codmon;
	}
	public void setCodmon(String codmon) {
		this.codmon = codmon;
	}
	public String getCta_cte() {
		return cta_cte;
	}
	public void setCta_cte(String cta_cte) {
		this.cta_cte = cta_cte;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
}
