package com.example.fuerzaventaschema.genesys.datatypes;

public class DB_Almacenes {

    private String codalm;
    private String desalm;
    private String id_local;
    private String local;
     
	public String getCodalm() {
		return codalm;
	}
	public void setCodalm(String codalm) {
		this.codalm = codalm;
	}
	public String getDesalm() {
		return desalm;
	}
	public void setDesalm(String desalm) {
		this.desalm = desalm;
	}
	public String getId_local() {
		return id_local;
	}
	public void setId_local(String id_local) {
		this.id_local = id_local;
	}	
	
	public String getLocal() {
		return local;
	}
	public void setLocal(String local) {
		this.local = local;
	}
	@Override
	public String toString() {
		return this.desalm;
	}

	

}
