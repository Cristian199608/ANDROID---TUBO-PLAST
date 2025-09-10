package com.example.sm_tubo_plast.genesys.datatypes;

import com.example.sm_tubo_plast.genesys.fuerza_ventas.PedidosActivity;
import com.example.sm_tubo_plast.genesys.util.VARIABLES;

public class DB_RegistroBonificaciones {

	private String oc_numero;
	private int item;
	private int secuenciaPromocion;
	private int agrupado;
	private String entrada;
	private int tipo_unimed_entrada;
	private String unimedEntrada; 
	private int cantidadEntrada;
	private double montoEntrada;
	private String salida;
	private int tipo_unimed_salida;
	private int cantidadSalida;
	private int acumulado;
	private int cantidadDisponible;
	private double montoDisponible;
	private int flagUltimo;
	
	private String codigoRegistro;
	private int cantidadTotal;
	private int saldoAnterior;
	private int cantidadEntregada;
	private int saldo;
	private String codigoAnterior;
	private String codigoVendedor;
	private String codigoCliente;
	
	
	public String getCodigoCliente() {
		return codigoCliente;
	}
	public void setCodigoCliente(String codigoCliente) {
		this.codigoCliente = codigoCliente;
	}
	public String getCodigoRegistro() {
		return codigoRegistro;
	}
	public void setCodigoRegistro(String codigoRegistro) {
		this.codigoRegistro = codigoRegistro;
	}
	public int getCantidadTotal() {
		return cantidadTotal;
	}
	public void setCantidadTotal(int cantidadTotal) {
		this.cantidadTotal = cantidadTotal;
	}
	public int getSaldoAnterior() {
		return saldoAnterior;
	}
	public void setSaldoAnterior(int saldoAnterior) {
		this.saldoAnterior = saldoAnterior;
	}
	public int getCantidadEntregada() {
		return cantidadEntregada;
	}
	public void setCantidadEntregada(int cantidadEntregada) {
		this.cantidadEntregada = cantidadEntregada;
	}
	public int getSaldo() {
		return saldo;
	}
	public void setSaldo(int saldo) {
		this.saldo = saldo;
	}
	public String getCodigoAnterior() {
		return codigoAnterior;
	}
	public void setCodigoAnterior(String codigoAnterior) {
		this.codigoAnterior = codigoAnterior;
	}
	public String getCodigoVendedor() {
		return codigoVendedor;
	}
	public void setCodigoVendedor(String codigoVendedor) {
		this.codigoVendedor = codigoVendedor;
	}
	public String getOc_numero() {
		return oc_numero;
	}
	public void setOc_numero(String oc_numero) {
		this.oc_numero = oc_numero;
	}
	public int getItem() {
		return item;
	}
	public void setItem(int item) {
		this.item = item;
	}
	public int getSecuenciaPromocion() {
		return secuenciaPromocion;
	}
	public void setSecuenciaPromocion(int secuenciaPromocion) {
		this.secuenciaPromocion = secuenciaPromocion;
	}
	public String getEntrada() {
		return entrada;
	}
	public void setEntrada(String entrada) {
		this.entrada = entrada;
	}
	public int getCantidadEntrada() {
		return cantidadEntrada;
	}
	public void setCantidadEntrada(int cantidadEntrada) {
		this.cantidadEntrada = cantidadEntrada;
	}
	public String getSalida() {
		return salida;
	}
	public void setSalida(String salida) {
		this.salida = salida;
	}
	public int getCantidadSalida() {
		return cantidadSalida;
	}
	public void setCantidadSalida(int cantidadSalida) {
		this.cantidadSalida = cantidadSalida;
	}
	public int getAcumulado() {
		return acumulado;
	}
	public void setAcumulado(int acumulado) {
		this.acumulado = acumulado;
	}
	public int getTipo_unimedEntrada() {
		return tipo_unimed_entrada;
	}
	public void setTipo_unimedEntrada(int tipo_unimedEntrada) {
		this.tipo_unimed_entrada = tipo_unimedEntrada;
	}
	public String getUnimedEntrada() {
		return unimedEntrada;
	}
	public void setUnimedEntrada(String unimedEntrada) {
		this.unimedEntrada = unimedEntrada;
	}
	public double getMontoEntrada() {
		return montoEntrada;
	}
	public void setMontoEntrada(double montoEntrada) {
		this.montoEntrada = montoEntrada;
	}
	public int getTipo_unimedSalida() {
		return tipo_unimed_salida;
	}
	public void setTipo_unimedSalida(int tipo_unimedSalida) {
		this.tipo_unimed_salida = tipo_unimedSalida;
	}
	public int getAgrupado() {
		return agrupado;
	}
	public void setAgrupado(int agrupado) {
		this.agrupado = agrupado;
	}
	public int getCantidadDisponible() {
		return cantidadDisponible;
	}
	public void setCantidadDisponible(int cantidadDisponible) {
		this.cantidadDisponible = cantidadDisponible;
	}
	public double getMontoDisponible() {
		return montoDisponible;
	}
	public void setMontoDisponible(double montoDisponible) {
		this.montoDisponible = montoDisponible;
	}
	public int getFlagUltimo() {
		return flagUltimo;
	}
	public void setFlagUltimo(int flagUltimo) {
		this.flagUltimo = flagUltimo;
	}

	public boolean convertirMonedaTo(String moneda, double tipoCambio){
		if(moneda.equals(PedidosActivity.MONEDA_SOLES_IN)){
			convertirMonedaToSoles(tipoCambio);
			return true;
		}else if(moneda.equals(PedidosActivity.MONEDA_DOLARES_IN)){
			convertirMonedaToDolar(tipoCambio);
			return true;
		}
		return false;
	}

	private void convertirMonedaToSoles(double tipoCambio){
		this.montoEntrada = VARIABLES.getDoubleFormaterThreeDecimal(this.montoEntrada* tipoCambio);
		this.montoDisponible = VARIABLES.getDoubleFormaterThreeDecimal(this.montoDisponible* tipoCambio);
	}
	private void convertirMonedaToDolar(double tipoCambio){
		this.montoEntrada = VARIABLES.getDoubleFormaterThreeDecimal(this.montoEntrada/ tipoCambio);
		this.montoDisponible = VARIABLES.getDoubleFormaterThreeDecimal(this.montoDisponible/ tipoCambio);
	}

	
}
