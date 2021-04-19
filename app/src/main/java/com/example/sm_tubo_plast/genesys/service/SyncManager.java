package com.example.sm_tubo_plast.genesys.service;

import android.util.Log;

import java.util.Observable;


public class SyncManager extends Observable{
	 private static SyncManager instance = new SyncManager();

	    public static SyncManager getInstance() {
	        return instance;
	    }
	    private SyncManager() {
	    }
	    
	    private boolean syncInProgress = false;
	    
	    public void sync(final String param1, final String param2, final int param3){
	        
	        if (syncInProgress)
	            return;
	        
	        // example of a custom error message
	        if (param2.equals("")) {
	            setChanged();
	            notifyObservers(new SyncUpdateMessage(SyncUpdateMessage.SYNC_CUSTOM_ERROR,
	                    null));
	            return;
	        }
	        
	        // set flag
	        syncInProgress = true;
	        notifyObservers(new SyncUpdateMessage(SyncUpdateMessage.SYNC_STARTED,
	                null));
	        
	        // all good, begin process
	        new Thread(new Runnable() {

	            @Override
	            public void run() {
                    Log.w("SERVICIO","Threaddddd");
	                setChanged();
	                notifyObservers(new SyncUpdateMessage(
	                        SyncUpdateMessage.SYNC_SUCCESSFUL, "Servicio"));

	                // release flag
	                syncInProgress = false;
	            }
	        }).start();
	    }
	    
	}