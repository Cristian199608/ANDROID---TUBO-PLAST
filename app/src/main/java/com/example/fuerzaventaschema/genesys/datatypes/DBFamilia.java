package com.example.fuerzaventaschema.genesys.datatypes;

public class DBFamilia {
	
	private String secuencia;
	private String familia;
	
	public String getSecuencia() {
		return secuencia;
	}
	public void setSecuencia(String secuencia) {
		this.secuencia = secuencia;
	}
	public String getFamilia() {
		return familia;
	}
	public void setFamilia(String familia) {
		this.familia = familia;
	}
	
	@Override
	public String toString() {
	    return this.familia;
	}
}
