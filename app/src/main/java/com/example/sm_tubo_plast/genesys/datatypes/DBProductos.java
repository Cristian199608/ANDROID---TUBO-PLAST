package com.example.sm_tubo_plast.genesys.datatypes;

public class DBProductos {
	private int _id;
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	private String codpro;
	private String despro;
	private String abrevpro;
    private String grupo;
    private String familia;
    private String sub_familia;
    private String ean13;
    private String cod_rapido;
    private String codunimed;
    private String codunimed_almacen;
    private double factor_conversion;
    private String afecto;
    private String estado;
    private double peso;
    private String foto;
    private String linea_negocio;
    private int flg_bonificacion;

    
	public String getLinea_negocio() {
		return linea_negocio;
	}
	public void setLinea_negocio(String linea_negocio) {
		this.linea_negocio = linea_negocio;
	}
	public String getCodpro() {
		return codpro;
	}
	public void setCodpro(String codpro) {
		this.codpro = codpro;
	}
	public String getDespro() {
		return despro;
	}
	public void setDespro(String despro) {
		this.despro = despro;
	}
	public String getAbrevpro() {
		return abrevpro;
	}
	public void setAbrevpro(String abrevpro) {
		this.abrevpro = abrevpro;
	}
	public String getGrupo() {
		return grupo;
	}
	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}
	public String getFamilia() {
		return familia;
	}
	public void setFamilia(String familia) {
		this.familia = familia;
	}
	public String getSub_familia() {
		return sub_familia;
	}
	public void setSub_familia(String sub_familia) {
		this.sub_familia = sub_familia;
	}
	public String getEan13() {
		return ean13;
	}
	public void setEan13(String ean13) {
		this.ean13 = ean13;
	}
	public String getCod_rapido() {
		return cod_rapido;
	}
	public void setCod_rapido(String cod_rapido) {
		this.cod_rapido = cod_rapido;
	}
	public String getCodunimed() {
		return codunimed;
	}
	public void setCodunimed(String codunimed) {
		this.codunimed = codunimed;
	}
	public String getCodunimed_almacen() {
		return codunimed_almacen;
	}
	public void setCodunimed_almacen(String codunimed_almacen) {
		this.codunimed_almacen = codunimed_almacen;
	}
	public double getFactor_conversion() {
		return factor_conversion;
	}
	public void setFactor_conversion(double factor_conversion) {
		this.factor_conversion = factor_conversion;
	}
	public String getAfecto() {
		return afecto;
	}
	public void setAfecto(String afecto) {
		this.afecto = afecto;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public double getPeso() {
		return peso;
	}
	public void setPeso(double peso) {
		this.peso = peso;
	}
	public String getFoto() {
		return foto;
	}
	public void setFoto(String bs) {
		this.foto = bs;
	}

	public int getFlg_bonificacion() {
		return flg_bonificacion;
	}

	public void setFlg_bonificacion(int flg_bonificacion) {
		this.flg_bonificacion = flg_bonificacion;
	}
	public boolean isFlg_bonificacion() {
		return flg_bonificacion==1;
	}
}
