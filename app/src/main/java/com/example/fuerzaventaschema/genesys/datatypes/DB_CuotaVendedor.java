package com.example.fuerzaventaschema.genesys.datatypes;

public class DB_CuotaVendedor {
	
	private int secuencia ;
    private String codven ;
    private String cuota ;
    private String nomven;
    private String ventas;
    private String devoluciones;
    private String ventas_efectivas;
    private String porcentajeParticipacion;
    private String porcentaje_Avance;
    
	public String getNomven() {
		return nomven;
	}
	public void setNomven(String nomven) {
		this.nomven = nomven;
	}
	public String getVentas() {
		return ventas;
	}
	public void setVentas(String ventas) {
		this.ventas = ventas;
	}
	public String getDevoluciones() {
		return devoluciones;
	}
	public void setDevoluciones(String devoluciones) {
		this.devoluciones = devoluciones;
	}
	public String getVentas_efectivas() {
		return ventas_efectivas;
	}
	public void setVentas_efectivas(String ventas_efectivas) {
		this.ventas_efectivas = ventas_efectivas;
	}
	public String getPorcentajeParticipacion() {
		return porcentajeParticipacion;
	}
	public void setPorcentajeParticipacion(String porcentajeParticipacion) {
		this.porcentajeParticipacion = porcentajeParticipacion;
	}
	public String getPorcentaje_Avance() {
		return porcentaje_Avance;
	}
	public void setPorcentaje_Avance(String porcentaje_Avance) {
		this.porcentaje_Avance = porcentaje_Avance;
	}
	public int getSecuencia() {
		return secuencia;
	}
	public void setSecuencia(int secuencia) {
		this.secuencia = secuencia;
	}
	public String getCodven() {
		return codven;
	}
	public void setCodven(String codven) {
		this.codven = codven;
	}
	public String getCuota() {
		return cuota;
	}
	public void setCuota(String cuota) {
		this.cuota = cuota;
	}
	
	
	public DB_CuotaVendedor(){
		
		this.secuencia = 0;
		this.codven = "";
		this.cuota = "";
		this.codven = "";
		this.ventas = "";
		this.devoluciones = "";
		this.ventas_efectivas = "";
		this.porcentajeParticipacion = "";
		this.porcentaje_Avance = "";
		
	}
    
    
}
