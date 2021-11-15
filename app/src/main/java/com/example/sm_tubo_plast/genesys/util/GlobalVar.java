package com.example.sm_tubo_plast.genesys.util;

import java.util.Calendar;

public class GlobalVar {

	public static final String PACKAGE_NAME="com.example.sm_tubo_plast";
	public static final String DATABASE_NAME="fuerzaventas";

	public static final int INTERNET = 1;
	public static final int LOCAL = 0;
	
	public static Calendar calendar = Calendar.getInstance();
	public static String urlService;
 	//public static String nombreService= GlobalFunctions.obtenerNombre(urlService);
	public static String NombreWEB;
	
	public static String direccion_servicio;
	public static String direccion_servicio_local;
	public static boolean servicio_principal_activo;
	public static boolean servicio_secundario_activo;
	public static int id_servicio;
	public static boolean autoActualizacionStock;

	public static final class SAN_OPCIONES {
		public static final String ASOCIACION_EDUCATIVA ="ASOCIACION EDUCATIVA";
		public static final String TIPO_DE_EDUCACION ="TIPO DE EDUCACION";
		public static final String ORDEN_RELIGIOSA ="ORDEN RELIGIOSA";
		public static final String MODELO_EDUCATIVO ="MODELO EDUCATIVO";
		public static final String PERFIL_DECISOR ="PERFIL DECISOR";
		public static final String TIPO_DE_INGLES ="TIPO DE INGLES";
		public static final String USO_DE_PLATAFORMAS ="USO DE PLATAFORMAS";
		public static final String USARIO_COMPARTIR ="USARIO COMPARTIR";
		public static final String TIPO_VISITA ="TIPO VISITA";
		public static final String TIPO_ACTIVIDD ="TIPO ACTIVIDAD";
	}

	public  static int CODIGO_VISITA_CLIENTE =100;
	
}
