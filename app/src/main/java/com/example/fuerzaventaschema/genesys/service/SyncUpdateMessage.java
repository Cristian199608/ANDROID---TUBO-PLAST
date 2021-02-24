package com.example.fuerzaventaschema.genesys.service;

public final class SyncUpdateMessage {
    
    // message codes
    public static final int NO_CONNECTION = 1;
    public static final int SYNC_SUCCESSFUL = 2;
    public static final int SYNC_STARTED = 3;
    public static final int SYNC_CUSTOM_ERROR = 4;
    
    private int messageCode;
    private Object what;
    
    public SyncUpdateMessage(int messageCode, Object what){
        this.messageCode = messageCode;
        this.what = what;
    }
    
    public int getMessageCode(){
        return this.messageCode;
    }
    
    public Object getWhat(){
        return this.what;
    }
}