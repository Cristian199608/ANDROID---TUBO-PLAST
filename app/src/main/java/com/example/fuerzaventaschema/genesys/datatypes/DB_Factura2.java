package com.example.fuerzaventaschema.genesys.datatypes;

public class DB_Factura2 {

    public int secuencia;
    public String codpro ;
    public int item ;
    public String cantidad ;
    public String total ;
    public int getSecuencia() {
		return secuencia;
	}
	public void setSecuencia(int secuencia) {
		this.secuencia = secuencia;
	}
	public String getCodpro() {
		return codpro;
	}
	public void setCodpro(String codpro) {
		this.codpro = codpro;
	}
	public int getItem() {
		return item;
	}
	public void setItem(int item) {
		this.item = item;
	}
	public String getCantidad() {
		return cantidad;
	}
	public void setCantidad(String cantidad) {
		this.cantidad = cantidad;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getPreven() {
		return preven;
	}
	public void setPreven(String preven) {
		this.preven = preven;
	}
	public String getTotal_real() {
		return total_real;
	}
	public void setTotal_real(String total_real) {
		this.total_real = total_real;
	}
	public String preven ;
    public String total_real ;
}
