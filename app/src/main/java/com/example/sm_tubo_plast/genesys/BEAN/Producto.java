package com.example.sm_tubo_plast.genesys.BEAN;

import com.example.sm_tubo_plast.genesys.datatypes.DBMta_Kardex;

public class Producto {
	private String codigo;
	private String descripcion;
	private String desc_comercial;
	private String unidadMedida;
	private String tipoProducto;
	private String color;
	private double peso;
	private double precio_base;
	private DBMta_Kardex stockDetalle;

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
	public String getUnidadMedida() {
		return unidadMedida;
	}
	public void setUnidadMedida(String unidadMedida) {
		this.unidadMedida = unidadMedida;
	}
	public String getTipoProducto() {
		return tipoProducto;
	}
	public void setTipoProducto(String tipoProducto) {
		this.tipoProducto = tipoProducto;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}

	public double getPeso() {
		return peso;
	}

	public void setPeso(double peso) {
		this.peso = peso;
	}

	public double getPrecio_base() {
		return precio_base;
	}

	public void setPrecio_base(double precio_base) {
		this.precio_base = precio_base;
	}

	public String getDesc_comercial() {
		return desc_comercial;
	}

	public void setDesc_comercial(String desc_comercial) {
		this.desc_comercial = desc_comercial;
	}

	public DBMta_Kardex getStockDetalle() {
		return stockDetalle;
	}

	public void setStockDetalle(DBMta_Kardex stockDetalle) {
		this.stockDetalle = stockDetalle;
	}
}
