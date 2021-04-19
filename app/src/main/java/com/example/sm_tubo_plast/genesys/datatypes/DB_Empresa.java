package com.example.sm_tubo_plast.genesys.datatypes;

public class DB_Empresa {

	 public String empresa ;
     public String ruc;
     public String bd;
     public String usuario ;
     public String clave ;
     public String latitud ;
     public String longitud ;

     public String getEmpresa() {
		return empresa;
	}

	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}

	public String getRuc() {
		return ruc;
	}

	public void setRuc(String ruc) {
		this.ruc = ruc;
	}

	public String getBd() {
		return bd;
	}

	public void setBd(String bd) {
		this.bd = bd;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
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

	public DB_Empresa() {
         this.empresa = "";
         this.ruc = "";
         this.bd = "";
         this.usuario = "";
         this.clave = "";
         this.latitud = "";
         this.longitud = "";
     }

     public DB_Empresa(String empresa, String ruc, String bd, String usuario, String clave, String latitud, String longitud)
     {
         this.empresa = empresa;
         this.ruc =  ruc;
         this.bd = bd;
         this.usuario = usuario;
         this.clave = clave;
         this.latitud = latitud;
         this.longitud = longitud;
     }
}
