package com.example.sm_tubo_plast.genesys.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;

import com.example.sm_tubo_plast.genesys.datatypes.DBSync_soap_manager;
import com.example.sm_tubo_plast.genesys.datatypes.DB_PosicionVendedor;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.Reportes.ReportesPedidosActivity;
import com.example.sm_tubo_plast.genesys.session.SessionManager;

import java.util.Calendar;


/**
 * When the alarm fires, this WakefulBroadcastReceiver receives the broadcast Intent 
 * and then starts the IntentService {@code SampleSchedulingService} to do some work.
 */
public class SampleAlarmReceiver extends BroadcastReceiver {
    // The app's AlarmManager, which provides access to the system alarm services.
    private AlarmManager alarmMgr;
    // The pending intent that is triggered when the alarm fires.
    private PendingIntent alarmIntent;
    private PendingIntent alarmIntentStop;
  
    @Override
    public void onReceive(Context context, Intent intent) {   
    	
    	Log.i("RECEIVED","LLAMADO");
    	
    	
    	if(intent.getExtras().get("com.genesys.fuerzaventas.requestCode").equals("principal")){
    		Log.i("PRINCIPAL", "llamado");

            String codven = new SessionManager(context).getCodigoVendedor();
    		
    		ConnectionDetector cd = new ConnectionDetector(context);
    		DBSync_soap_manager sm = new DBSync_soap_manager(context);
    		Location location = LocationUtils.getLatestLocation(context);
    		
    		DB_PosicionVendedor posVendedor;
    		
    		if(location != null){
    			
    			posVendedor = new DB_PosicionVendedor(codven,""+location.getLatitude(),""+location.getLongitude());
    			
    		}
    		else{
    			Log.i("sample Alarm receiver","Posicion Nulo");
    			posVendedor = new DB_PosicionVendedor(codven,"NULL","NULL");
    		}
    		
    		
    		if(cd.isConnectingToInternet()){
    			
    			try{
    				sm.enviarPosicionVendedor(posVendedor);
    			}
    			catch(Exception e){
    				Log.w("SampleAlarmReceiver","error al enviar posicion de vendedor: "+e);
    			}
    		}
    		else{
    			Log.w("SampleAlarmReceiver","Sin conexion a la ws");
    		}

    	}
    	
    	if(intent.getExtras().get("com.genesys.fuerzaventas.requestCode").equals("secundario")){
    		Log.i("SECUNDARIO", "llamado");
    		
    		boolean alarmUp = (PendingIntent.getBroadcast(context, 0, 
                    new Intent(context,SampleAlarmReceiver.class), 
                    PendingIntent.FLAG_NO_CREATE) != null);

            if (alarmUp)
            {
                Log.d("myTag", "Alarm is already active");
            }
            else{
            	Log.d("myTag", "Alarm is not already active");
            }
    		
            boolean alarmUpStop = (PendingIntent.getBroadcast(context, 1, 
                    new Intent(context,SampleAlarmReceiver.class), 
                    PendingIntent.FLAG_NO_CREATE) != null);

            if (alarmUpStop)
            {
                Log.d("myTag", "AlarmStop is already active");
            }
            else{
            	Log.d("myTag", "AlarmStop is not already active");
            }
            
            AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        	Intent intent_principal = new Intent(context, SampleAlarmReceiver.class);
            PendingIntent alarmIntent_principal = PendingIntent.getBroadcast(context, 0, intent_principal, 0);
            
            alarmMgr.cancel(alarmIntent_principal);
            alarmIntent_principal.cancel();
            Log.i("STOP ALARM INI", "alarm canceled");
            
            AlarmManager alarmMgr2 = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        	Intent intent_secundario = new Intent(context, SampleAlarmReceiver.class);
            PendingIntent alarmIntent_secundario = PendingIntent.getBroadcast(context, 1, intent_secundario, 0);
            
            alarmMgr2.cancel(alarmIntent_secundario);
            alarmIntent_secundario.cancel();
            Log.i("STOP ALARM STOP", "alarm canceled");
            
            // Disable {@code SampleBootReceiver} so that it doesn't automatically restart the 
            // alarm when the device is rebooted.
            ComponentName receiver = new ComponentName(context, SampleBootReceiver.class);
            PackageManager pm = context.getPackageManager();

            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP);
    		
    		
    		boolean alarmUp2 = (PendingIntent.getBroadcast(context, 0, 
                    new Intent(context,SampleAlarmReceiver.class), 
                    PendingIntent.FLAG_NO_CREATE) != null);

            if (alarmUp2)
            {
                Log.d("myTag", "Alarm is already active");
            }
            else{
            	Log.d("myTag", "Alarm is not already active");
            }
    		
            boolean alarmUpStop2 = (PendingIntent.getBroadcast(context, 1, 
                    new Intent(context,SampleAlarmReceiver.class), 
                    PendingIntent.FLAG_NO_CREATE) != null);

            if (alarmUpStop2)
            {
                Log.d("myTag", "AlarmStop is already active");
            }
            else{
            	Log.d("myTag", "AlarmStop is not already active");
            }
            
            //resetAlarm
            setAlarm(context);
    		
    	}
        
        //startWakefulService(context, service);
        // END_INCLUDE(alarm_onreceive)
    }

    // BEGIN_INCLUDE(set_alarm)
    /**
     * Sets a repeating alarm that runs once a day at approximately 8:30 a.m. When the
     * alarm fires, the app broadcasts an Intent to this WakefulBroadcastReceiver.
     * @param context
     */
    public void setAlarm(Context context) {
    	
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        
        Intent intent = new Intent(context, SampleAlarmReceiver.class);
        intent.putExtra("com.genesys.fuerzaventas.requestCode", "principal");
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        
        Intent intent_stop = new Intent(context, SampleAlarmReceiver.class);
        intent_stop.putExtra("com.genesys.fuerzaventas.requestCode", "secundario");
        alarmIntentStop = PendingIntent.getBroadcast(context, 1, intent_stop, 0);

        Calendar now = Calendar.getInstance();
        
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 00);
        
        Calendar calendarStop = Calendar.getInstance();
        calendarStop.set(Calendar.HOUR_OF_DAY, 18);
        calendarStop.set(Calendar.MINUTE, 00);
        
        Calendar calendar2 = (Calendar)calendarStop.clone();
        calendar2.add(Calendar.MINUTE,-5);
        
        if(now.before(calendar2)){
        	//Log.i("COMPARACION NOW",now.toString());
        	//Log.i("COMPARACION CALENDAR2", calendar2.toString());
        }
        else{
        	calendar.add(Calendar.DAY_OF_MONTH, 1);
        	calendarStop.add(Calendar.DAY_OF_MONTH, 1);
        }
        
        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP,  
                calendar.getTimeInMillis(), 15*60*1000, alarmIntent);
        Log.i("ALARMA INICIO","puesta para " + calendar.toString());
        
        alarmMgr.set(AlarmManager.RTC_WAKEUP,  
                calendarStop.getTimeInMillis(), alarmIntentStop);
        Log.i("ALARMA STOP","puesta para " + calendarStop.toString());
        
        //alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 10000, 60000, alarmIntent);
        
        // Enable {@code SampleBootReceiver} to automatically restart the alarm when the
        // device is rebooted.
        ComponentName receiver = new ComponentName(context, SampleBootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);           
    }
    // END_INCLUDE(set_alarm)
    
    /**
     * Cancels the alarm.
     * @param context
     */
    // BEGIN_INCLUDE(cancel_alarm)
    public void cancelAlarm(Context context) {
        // If the alarm has been set, cancel it.
        if (alarmMgr!= null) {
            alarmMgr.cancel(alarmIntent);
            alarmIntent.cancel();
        }
        
        // Disable {@code SampleBootReceiver} so that it doesn't automatically restart the 
        // alarm when the device is rebooted.
        /*ComponentName receiver = new ComponentName(context, SampleBootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);*/
    }
    // END_INCLUDE(cancel_alarm)
}
