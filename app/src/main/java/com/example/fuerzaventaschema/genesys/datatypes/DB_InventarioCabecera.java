package com.example.fuerzaventaschema.genesys.datatypes;

public class DB_InventarioCabecera {

	 public int secuencia;
     public String id_local;
     public String codalm;
     public String distribucion;
	
     public int getSecuencia() {
		return secuencia;
	 }
	 public void setSecuencia(int secuencia) {
		this.secuencia = secuencia;
	 }
	 public String getId_local() {
		return id_local;
	 }
	 public void setId_local(String id_local) {
		this.id_local = id_local;
	 }
	 public String getCodalm() {
		return codalm;
	 }
	 public void setCodalm(String codalm) {
		this.codalm = codalm;
     }
	 public String getDistribucion() {
		return distribucion;
	 }
	 public void setDistribucion(String distribucion) {
		this.distribucion = distribucion;
	 }
     
     
}
