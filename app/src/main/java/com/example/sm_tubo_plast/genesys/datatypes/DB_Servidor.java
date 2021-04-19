package com.example.sm_tubo_plast.genesys.datatypes;

public class DB_Servidor {
	private String TX_SERV_servicioWeb;
    private String TX_SERV_servidorBD;
    private String TX_SERV_nombreBD ;
    private String TX_SERV_usuario;
    private String TX_SERV_contrasena ;
    private int IN_SERV_item ;
    
    public String getTX_SERV_servicioWeb() {
		return TX_SERV_servicioWeb;
	}

	public void setTX_SERV_servicioWeb(String tX_SERV_servicioWeb) {
		TX_SERV_servicioWeb = tX_SERV_servicioWeb;
	}

	public String getTX_SERV_servidorBD() {
		return TX_SERV_servidorBD;
	}

	public void setTX_SERV_servidorBD(String tX_SERV_servidorBD) {
		TX_SERV_servidorBD = tX_SERV_servidorBD;
	}

	public String getTX_SERV_nombreBD() {
		return TX_SERV_nombreBD;
	}

	public void setTX_SERV_nombreBD(String tX_SERV_nombreBD) {
		TX_SERV_nombreBD = tX_SERV_nombreBD;
	}

	public String getTX_SERV_usuario() {
		return TX_SERV_usuario;
	}

	public void setTX_SERV_usuario(String tX_SERV_usuario) {
		TX_SERV_usuario = tX_SERV_usuario;
	}

	public String getTX_SERV_contrasena() {
		return TX_SERV_contrasena;
	}

	public void setTX_SERV_contrasena(String tX_SERV_contrasena) {
		TX_SERV_contrasena = tX_SERV_contrasena;
	}

	public int getIN_SERV_item() {
		return IN_SERV_item;
	}

	public void setIN_SERV_item(int iN_SERV_item) {
		IN_SERV_item = iN_SERV_item;
	}

	public int getIN_SERV_codigo_ID() {
		return IN_SERV_codigo_ID;
	}

	public void setIN_SERV_codigo_ID(int iN_SERV_codigo_ID) {
		IN_SERV_codigo_ID = iN_SERV_codigo_ID;
	}

	private int IN_SERV_codigo_ID ;

    public DB_Servidor() {
        this.TX_SERV_servicioWeb = "";
        this.TX_SERV_servidorBD = "";
        this.TX_SERV_nombreBD = "";
        this.TX_SERV_usuario = "";
        this.TX_SERV_contrasena = "";
        this.IN_SERV_item = 0;
        this.IN_SERV_codigo_ID = 0;
    }
    
}
