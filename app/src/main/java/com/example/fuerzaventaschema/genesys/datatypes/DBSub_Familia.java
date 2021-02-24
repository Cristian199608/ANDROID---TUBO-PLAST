package com.example.fuerzaventaschema.genesys.datatypes;

public class DBSub_Familia {
	
	private String secuencia;
	private String sub_familia;
	private String des_familia;
	
	
	public String getSecuencia() {
		return secuencia;
	}
	public void setSecuencia(String secuencia) {
		this.secuencia = secuencia;
	}
	public String getSub_familia() {
		return sub_familia;
	}
	public void setSub_familia(String sub_familia) {
		this.sub_familia = sub_familia;
	}
	public String getDes_familia() {
		return des_familia;
	}
	public void setDes_familia(String des_familia) {
		this.des_familia = des_familia;
	}
	
	@Override
	public String toString() {
	    return this.des_familia;
	}
	

}
