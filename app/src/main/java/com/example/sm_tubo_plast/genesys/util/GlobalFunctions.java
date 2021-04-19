package com.example.sm_tubo_plast.genesys.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.datatypes.DBclasses;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.StringTokenizer;

public class GlobalFunctions {
	public static final int TOAST_DONE = 0;
	public static final int TOAST_WARNING = 1;
	public static final int TOAST_ERROR = 2;
	
	public static final int POSICION_TOP = 1;	
	public static final int POSICION_MIDDLE = 2;
	public static final int POSICION_BOTTOM = 3;
	
	public static final String md5(final String password) {
	    try {
	 
	        MessageDigest digest = MessageDigest
	                .getInstance("MD5");
	        digest.update(password.getBytes());
	        byte messageDigest[] = digest.digest();
	 
	        StringBuffer hexString = new StringBuffer();
	        for (int i = 0; i < messageDigest.length; i++) {
	            String h = Integer.toHexString(0xFF & messageDigest[i]);
	            while (h.length() < 2)
	                h = "0" + h;
	            hexString.append(h);
	        }
	        return hexString.toString();
	 
	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    }
	    return "";
	}
	
	public static String getFechaActual(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
    	Date date = new Date();
    	return dateFormat.format(date);
	}
	
	public static String getHoraActual(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss"); 
    	Date date = new Date();
    	return dateFormat.format(date);
	}
	
	public static String getFecha_configuracion(Context context){
		DBclasses dbclass = new DBclasses(context);
		return dbclass.getFecha2();
	}
	
	public static String getDia(){

    	String dia=(GlobalVar.calendar.get(Calendar.DAY_OF_MONTH))+"";
        if(dia.length()==1){
        	dia="0"+ dia;
        			
        }
    	//Log.w("DIA", dia);
    	return dia;
    	
	}
	
	public static String getMes(){

    	String MES=(GlobalVar.calendar.get(Calendar.MONTH)+1)+"";
    	 if(MES.length()==1){
    		 MES="0"+ MES;
         			
         }

    	//Log.w("DIA", dia);
    	return MES;
    	
	}

	
	public static Date ConvertFromStringToDate(String tx_date)
	{
		// "2013|4|11|11|4"  -> "year|month|day|hour|minute";
		if(tx_date.equals(""))
		{
			return null;
		}
		String[] arrayDate = tx_date.split("\\|");
		String dateString = arrayDate[0]+"-"+arrayDate[1]+"-"+arrayDate[2]+" "+arrayDate[3]+":"+arrayDate[4];
		String year = arrayDate[0];
		String month = arrayDate[1];
		String day = arrayDate[2];
		String hour = arrayDate[3];
		String min = arrayDate[4];
		
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	    Date convertedDate = new Date();
	    try {
	        convertedDate = dateFormat.parse(dateString);
	    } catch (ParseException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
	    System.out.println(convertedDate);
	    String result = String.valueOf(convertedDate);
	    
	    String resultDate = ConvertFromDateToString(convertedDate);
	    
	    System.out.println(result);
	    
		return convertedDate;
	}
	
	public static String ConvertFromDateToString(Date dt_date)
	{
		final Calendar cal = Calendar.getInstance();
		String tx_date = "";
		
		if(dt_date==null)
		{
			return tx_date;
		}
		cal.setTime(dt_date);
		
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH)+1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		
		int hour = cal.get(Calendar.HOUR_OF_DAY); // Calendar.HOUR for 12 and Calendar.HOUR_OF_DAY for 24
		int min = cal.get(Calendar.MINUTE);
		
		try {
			// "2013|4|11|11|4"  -> "year|month|day|hour|minute";
			tx_date = year+"-"+month+"-"+day+" "+hour+":"+min;
			return tx_date;
		} catch (Exception e) {
			// TODO: handle exception
			Log.w("ConvertFromDateToString", e.getMessage());	
		}
		
		return tx_date;
		
	}
	
	
	@SuppressLint("SimpleDateFormat")
	public static Date ConvertFromStringToDateSystem(String tx_date)
	{
		//String tx_date = "2010-10-15T09:27:37Z";  
		SimpleDateFormat  format = new SimpleDateFormat("yyyy-MM-dd HH:mm");  
		try {  
		    Date date = format.parse(tx_date);  
		    System.out.println(date);
		    return date;
		} catch (ParseException e) {  
		    // TODO Auto-generated catch block  
		    e.printStackTrace();  
		}
		return null;
	}
	
	public static String CurrentStringDateSystem()
	{
		Date dt_date = new Date();
		final Calendar cal = Calendar.getInstance();
		String tx_date = "";
		cal.setTime(dt_date);
		
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH)+1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		
		int hour = cal.get(Calendar.HOUR_OF_DAY); // Calendar.HOUR for 12 and Calendar.HOUR_OF_DAY for 24
		int min = cal.get(Calendar.MINUTE);
		
		try {
			// "yyyy-MM-dd HH:mm"
			tx_date = year+"-"+month+"-"+day+" "+hour+":"+min;
			return tx_date;
		} catch (Exception e) {
			// TODO: handle exception
			Log.w("CurrentStringDateSystem", e.getMessage());	
		}
		
		return tx_date;
	}
	
	public static String CurrentStringDateSystemSeg()
	{
		Date dt_date = new Date();
		final Calendar cal = Calendar.getInstance();
		String tx_date = "";
		cal.setTime(dt_date);
		
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH)+1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		
		int hour = cal.get(Calendar.HOUR_OF_DAY); // Calendar.HOUR for 12 and Calendar.HOUR_OF_DAY for 24
		int min = cal.get(Calendar.MINUTE);
		int seg = cal.get(Calendar.SECOND);
		
		try {
			// "yyyy-MM-dd HH:mm"
			tx_date = year+"-"+month+"-"+day+"-"+hour+"-"+min+"-"+seg;
			return tx_date;
		} catch (Exception e) {
			// TODO: handle exception
			Log.w("CurrentStringDateSystem", e.getMessage());	
		}
		
		return tx_date;
	}
	
	public static String RemoveLastCharacter(String tx_string, char tx_char)
	{
		if (tx_string.length() > 0 && tx_string.charAt(tx_string.length()-1)==tx_char) 
		{
			tx_string = tx_string.substring(0, tx_string.length()-1);
		    return tx_string;
		}
		else 
		{
		    return tx_string;
		}
	}
	
	public static String ReplaceAcutesHTML(String str) {

		str = str.replaceAll("&aacute;","�");
		str = str.replaceAll("&eacute;","�");
		str = str.replaceAll("&iacute;","�");
		str = str.replaceAll("&oacute;","�");
		str = str.replaceAll("&uacute;","�");
		str = str.replaceAll("&Aacute;","�");
		str = str.replaceAll("&Eacute;","�");
		str = str.replaceAll("&Iacute;","�");
		str = str.replaceAll("&Oacute;","�");
		str = str.replaceAll("&Uacute;","�");
		str = str.replaceAll("&ntilde;","�");
		str = str.replaceAll("&Ntilde;","�");

		return str;
		}
	
	public static String obtenerNombre(String entrada){
		StringTokenizer st = new StringTokenizer(entrada, "/");
       String nombre="";
    //   Log.w("ObtenerNombre",""+ st.toString() + "--" + st.countTokens() + "--"+ st.nextElement());
		   while(st.hasMoreTokens()) {
               
		   nombre = st.nextToken();

		   }
		Log.w("ObtenerNombre",""+ nombre.length()+ "--"+entrada.length());
	//	GlobalVar.NombreWEB = entrada.substring(1,6);
		Log.w("ObtenerNombre", GlobalVar.NombreWEB);
		return nombre;
		
	}
	
	
	public static String obtenerNombreWEB(String entrada){
		StringTokenizer st = new StringTokenizer(entrada, "/");
       String nombre="";
    //   Log.w("ObtenerNombre",""+ st.toString() + "--" + st.countTokens() + "--"+ st.nextElement());
		   while(st.hasMoreTokens()) {
               
		   nombre = st.nextToken();

		   }
		Log.w("ObtenerNombreWEB",""+ entrada.substring(1,(entrada.length()-nombre.length())));
		Log.w("aaaaaa",""+ entrada.substring(0,(entrada.length()-nombre.length())-1) );
	//	GlobalVar.NombreWEB = entrada.substring(1,(entrada.length()-nombre.length()));
	//	Log.w("ObtenerNombre", GlobalVar.NombreWEB);
		return entrada.substring(0,(entrada.length()-nombre.length())-1) ;
		
		
	}
	
	public static BigDecimal redondear(double val){
		 String r = val+"";
        BigDecimal big = new BigDecimal(r);
        big = big.setScale(2, RoundingMode.HALF_UP);
        return big;
	}
	
	public static String redondear(String decimal){
		String r = String.valueOf(decimal);
        BigDecimal big = new BigDecimal(r);
        big = big.setScale(2, RoundingMode.HALF_UP);
        return String.valueOf(big);
	}

	public static double redondear_toDouble(double decimal){
		String r = String.valueOf(decimal);
		BigDecimal big = new BigDecimal(r);
		big = big.setScale(2, RoundingMode.HALF_UP);
		return Double.parseDouble(String.valueOf(big));
	}
	public static double redondear_toDoubleFourDecimal(double decimal){
		String r = String.valueOf(decimal);
		BigDecimal big = new BigDecimal(r);
		big = big.setScale(4, RoundingMode.HALF_UP);
		return Double.parseDouble(String.valueOf(big));
	}
	
	public static String redondear_toString(double decimal){				
		String r = String.valueOf(decimal);
        BigDecimal big = new BigDecimal(r);
        big = big.setScale(2, RoundingMode.HALF_UP);        
        Log.v("redondear_toDouble", "out:"+big.toString());        
        return big.toString();
	}
	
	public static DecimalFormat formateador(){
		
		DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
		simbolos.setDecimalSeparator('.');
		simbolos.setGroupingSeparator(',');
		
		DecimalFormat formateador = new DecimalFormat("###,##0.00", simbolos);
		
		return formateador;
	}



	 //Prueba backup
	  public static void backupdDatabase(){
		    try {
			    File sd = Environment.getExternalStorageDirectory();
		    	
			    File data = Environment.getDataDirectory();
			    String packageName  = "com.genesys.fuerza_ventas";
			    String sourceDBName = "fuerzaventas";
			    String targetDBName = "saemovilesbkp";
			    if (sd.canWrite()) {
			    Date now = new Date();
				String currentDBPath = "data/" + packageName + "/databases/" + sourceDBName;
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm"); 
				String backupDBPath = "DCIM/" + targetDBName + dateFormat.format(now) + ".db";
		 
				File currentDB = new File(data, currentDBPath);
				File backupDB = new File(sd, backupDBPath);
		 
				Log.i("backup","backupDB=" + backupDB.getAbsolutePath());
				Log.i("backup","sourceDB=" + currentDB.getAbsolutePath());
		 
				FileChannel src = new FileInputStream(currentDB).getChannel();
				FileChannel dst = new FileOutputStream(backupDB).getChannel();
				
				dst.transferFrom(src, 0, src.size());
				
				src.close();
				dst.close();
				
			    }
			} catch (Exception e) {
				Log.i("Backup", e.toString());
			}
		}
	  //
	
	  public static String str_pad_zero(int numero,String number_pad){
		  
		  Formatter fmt = new Formatter();
		  
		  String formato = "%0"+number_pad+"d";
		  
		  String result = fmt.format(formato, numero).toString();
		  fmt.close();
		  
		  return result;
	  }
	  
	  
	  //Entrega un string que se obtiene de unir sec_promo + item_promo + agrup_promo
	  //Para los pedido_detalles's de tipo promocion (P)
	  //el string sigue la siguiente nomenclatura
	  //00120502   -------->    0012 |   05  |   02
	  //                      <-sec-> <-itm-> <-agrup->
	  public static String codigo_secuencia_promocion(int sec, int itm, int agrup){
		  
		  return "-"+str_pad_zero(sec, "4")+str_pad_zero(itm, "2")+str_pad_zero(agrup, "2");
		  
	  }
	  
	  
	  //Entrega un string que se obtiene de unir codigo_producto_salida + cantidad_de_bonif
	  //Para los pedido_detalles's de tipo venta (V)
	  //el string sigue la siguiente nomenclatura
	  //001248005   -------->    001248 |  005
	  //                         <-cip-> <-cant->
      public static String codigo_secuencia_promocion_productoVenta(int cip, int cant){
		  
		  return "-"+str_pad_zero(cip, "6")+str_pad_zero(cant, "3");
		  
	  }
 	  
      
      public static String stringToTokens(String texto){
    	  
    	    StringBuilder builder = new StringBuilder();
			StringTokenizer tokens= new StringTokenizer(texto);
			  
			while(tokens.hasMoreTokens()){
				builder.append(tokens.nextToken());
				builder.append("%");
			}
    	  
			return builder.toString();
      }
      
      public static void showCustomToast(Activity activity, String mensaje,int tipo,int position){
    		LayoutInflater inflater = activity.getLayoutInflater();
    		View view = inflater.inflate(R.layout.toast_personalizado, (ViewGroup)activity.findViewById(R.id.toast_personalizado));
    		
    		LinearLayout customToast = (LinearLayout) view.findViewById(R.id.toast_personalizado);
    		ImageView icon = (ImageView) view.findViewById(R.id.toast_icon);
    		TextView text = (TextView) view.findViewById(R.id.toast_text);
    		
    		text.setText(mensaje);
    		text.setGravity(Gravity.CENTER);
    		
    		switch (tipo) {
    		case TOAST_DONE:
    			customToast.setBackgroundResource(R.drawable.toast_done_container);
    			icon.setBackgroundResource(R.drawable.icon_done);
    			text.setTextColor(activity.getResources().getColor(R.color.green_500));
    			break;
    		case TOAST_WARNING:
    			customToast.setBackgroundResource(R.drawable.toast_warning_container);
    			icon.setBackgroundResource(R.drawable.icon_warning);
    			text.setTextColor(activity.getResources().getColor(R.color.orange_500));
    			break;
    		case TOAST_ERROR:
    			customToast.setBackgroundResource(R.drawable.toast_wrong_container);
    			icon.setBackgroundResource(R.drawable.icon_error);
    			text.setTextColor(activity.getResources().getColor(R.color.red_500));
    			break;
    		default:
    			break;
    		}
    		
    		Toast toast = new Toast(activity.getApplicationContext());
    		switch (position) {
    		case POSICION_TOP:
    			toast.setGravity(Gravity.TOP, 0,15);
    			break;
    		case POSICION_MIDDLE:
    			toast.setGravity(Gravity.CENTER, 0,15);
    			break;
    		case POSICION_BOTTOM:
    			toast.setGravity(Gravity.BOTTOM, 0,15);
    			break;
    		default:
    			toast.setGravity(Gravity.BOTTOM, 0,15);
    			break;
    		}
    		toast.setDuration(Toast.LENGTH_LONG);
    		toast.setView(view);
    		toast.show();
    	}
      
      public static void showCustomToastShort(Activity activity, String mensaje,int tipo,int position){
  		LayoutInflater inflater = activity.getLayoutInflater();
  		View view = inflater.inflate(R.layout.toast_personalizado, (ViewGroup)activity.findViewById(R.id.toast_personalizado));
  		
  		LinearLayout customToast = (LinearLayout) view.findViewById(R.id.toast_personalizado);
  		ImageView icon = (ImageView) view.findViewById(R.id.toast_icon);
  		TextView text = (TextView) view.findViewById(R.id.toast_text);
  		
  		text.setText(mensaje);
  		text.setGravity(Gravity.CENTER);
  		
  		switch (tipo) {
  		case TOAST_DONE:
  			customToast.setBackgroundResource(R.drawable.toast_done_container);
  			icon.setBackgroundResource(R.drawable.icon_done);
  			text.setTextColor(activity.getResources().getColor(R.color.green_500));
  			break;
  		case TOAST_WARNING:
  			customToast.setBackgroundResource(R.drawable.toast_warning_container);
  			icon.setBackgroundResource(R.drawable.icon_warning);
  			text.setTextColor(activity.getResources().getColor(R.color.orange_500));
  			break;
  		case TOAST_ERROR:
  			customToast.setBackgroundResource(R.drawable.toast_wrong_container);
  			icon.setBackgroundResource(R.drawable.icon_error);
  			text.setTextColor(activity.getResources().getColor(R.color.red_500));
  			break;
  		default:
  			break;
  		}
  		
  		Toast toast = new Toast(activity.getApplicationContext());
  		switch (position) {
  		case POSICION_TOP:
  			toast.setGravity(Gravity.TOP, 0,15);
  			break;
  		case POSICION_MIDDLE:
  			toast.setGravity(Gravity.CENTER, 0,15);
  			break;
  		case POSICION_BOTTOM:
  			toast.setGravity(Gravity.BOTTOM, 0,15);
  			break;
  		default:
  			toast.setGravity(Gravity.BOTTOM, 0,15);
  			break;
  		}
  		toast.setDuration(Toast.LENGTH_SHORT);
  		toast.setView(view);
  		toast.show();
  	}
	
      
      public static void backupdDatabase(Activity activity){
		    try {
			    File sd = Environment.getExternalStorageDirectory();
		    	
			    File data = Environment.getDataDirectory();
			    String packageName  = "com.genesys.fuerza_ventas";
			    String sourceDBName = "fuerzaventas";
			    String targetDBName = "saemovilesbkp";
			    if (sd.canWrite()) {
			    Date now = new Date();
				String currentDBPath = "data/" + packageName + "/databases/" + sourceDBName;
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm"); 
				String backupDBPath = "DCIM/" + targetDBName + dateFormat.format(now) + ".db";
		 
				File currentDB = new File(data, currentDBPath);
				File backupDB = new File(sd, backupDBPath);
		 
				Log.i("backup","backupDB=" + backupDB.getAbsolutePath());
				Log.i("backup","sourceDB=" + currentDB.getAbsolutePath());
		 
				FileChannel src = new FileInputStream(currentDB).getChannel();
				FileChannel dst = new FileOutputStream(backupDB).getChannel();
				
				dst.transferFrom(src, 0, src.size());
				
				src.close();
				dst.close();
				
				showCustomToast(activity, "BackUp generado", TOAST_DONE);
				
			    }
			} catch (Exception e) {
				Log.i("Backup", e.toString());
				showCustomToast(activity, "BackUp generado", TOAST_ERROR);
			}
		}
      
      
      public static void showCustomToast(Activity activity, String mensaje,int tipo){
    		LayoutInflater inflater = activity.getLayoutInflater();
    		View view = inflater.inflate(R.layout.toast_personalizado, (ViewGroup)activity.findViewById(R.id.toast_personalizado));
    		
    		LinearLayout customToast = (LinearLayout) view.findViewById(R.id.toast_personalizado);
    		ImageView icon = (ImageView) view.findViewById(R.id.toast_icon);
    		TextView text = (TextView) view.findViewById(R.id.toast_text);
    		
    		text.setText(mensaje);
    		text.setGravity(Gravity.CENTER);
    		
    		switch (tipo) {
    		case TOAST_DONE:
    			customToast.setBackgroundResource(R.drawable.toast_done_container);
    			icon.setBackgroundResource(R.drawable.icon_done);
    			text.setTextColor(activity.getResources().getColor(R.color.green_500));    			
    			break;
    		case TOAST_WARNING:
    			customToast.setBackgroundResource(R.drawable.toast_warning_container);
    			icon.setBackgroundResource(R.drawable.icon_warning);
    			text.setTextColor(activity.getResources().getColor(R.color.orange_500));    			
    			break;
    		case TOAST_ERROR:
    			customToast.setBackgroundResource(R.drawable.toast_wrong_container);
    			icon.setBackgroundResource(R.drawable.icon_error);
    			text.setTextColor(activity.getResources().getColor(R.color.red_500));    			
    			break;
    		default:
    			break;
    		}
    		
    		Toast toast = new Toast(activity.getApplicationContext());
    		toast.setGravity(Gravity.BOTTOM, 0,15);
    		toast.setDuration(Toast.LENGTH_LONG);
    		toast.setView(view);
    		toast.show();
    	}
}
