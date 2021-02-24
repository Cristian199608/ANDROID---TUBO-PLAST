package com.example.fuerzaventaschema.genesys.datatypes;

public class DB_DistribucionAlmacen {

	private String id_local;
	private String codalm;
	private int id_disalm;
	private String descripcion;
	
	
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
	public int getId_disalm() {
		return id_disalm;
	}
	public void setId_disalm(int id_disalm) {
		this.id_disalm = id_disalm;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	
	@Override
	public String toString() {
		return this.descripcion;
	}

}
