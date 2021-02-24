package com.example.fuerzaventaschema.genesys.BEAN;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class model_bonificacion {
	private String codigo;
	private String descripcion;
	private String entrada;
	private String descripcionEntrada;
	private String precio;
	private String unidadMedidaAlmacen;	
	private String unidadMedida;
	private String unidadMedida2;
	private int factorConversion;
	private int stock;
	private int stock2;
	private int cantidad;
	private boolean checked;
		
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getEntrada() {
		return entrada;
	}
	public void setEntrada(String entrada) {
		this.entrada = entrada;
	}
	
	public String getPrecio() {
		return precio;
	}
	public void setPrecio(String precio) {
		this.precio = precio;
	}
	public String getUnidadMedidaAlmacen() {
		return unidadMedidaAlmacen;
	}
	public void setUnidadMedidaAlmacen(String unidadMedidaAlmacen) {
		this.unidadMedidaAlmacen = unidadMedidaAlmacen;
	}
	
	public String getUnidadMedida() {
		return unidadMedida;
	}
	public void setUnidadMedida(String unidadMedida) {
		this.unidadMedida = unidadMedida;
	}
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
	public int getStock2() {
		return stock2;
	}
	public void setStock2(int stock2) {
		this.stock2 = stock2;
	}
	public String getUnidadMedida2() {
		return unidadMedida2;
	}
	public void setUnidadMedida2(String unidadMedida2) {
		this.unidadMedida2 = unidadMedida2;
	}
	public int getFactorConversion() {
		return factorConversion;
	}
	public void setFactorConversion(int factorConversion) {
		this.factorConversion = factorConversion;
	}
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public String getDescripcionEntrada() {
		return descripcionEntrada;
	}
	public void setDescripcionEntrada(String descripcionEntrada) {
		this.descripcionEntrada = descripcionEntrada;
	}
	
	
	
}
