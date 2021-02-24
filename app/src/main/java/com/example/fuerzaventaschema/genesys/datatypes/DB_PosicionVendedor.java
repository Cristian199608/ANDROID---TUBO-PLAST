package com.example.fuerzaventaschema.genesys.datatypes;

public class DB_PosicionVendedor {

	public String codven;
    public String latitud;
    public String longitud;

     
	public String getCodven() {
		return codven;
	}

	public void setCodven(String codven) {
		this.codven = codven;
	}

	public String getLatitud() {
		return latitud;
	}

	public void setLatitud(String latitud) {
		this.latitud = latitud;
	}

	public String getLongitud() {
		return longitud;
	}

	public void setLongitud(String longitud) {
		this.longitud = longitud;
	}

	
	public DB_PosicionVendedor() {
        this.codven = "";
        this.latitud = "";
        this.longitud = "";
    }

    public DB_PosicionVendedor(String codven, String latitud, String longitud)
    {
        this.codven = codven;
        this.latitud =  latitud;
        this.longitud = longitud;
    }
}
