package com.example.fuerzaventaschema.genesys.service;

import javax.net.ssl.X509TrustManager;
import javax.security.cert.CertificateException;
import javax.security.cert.X509Certificate;
public class EasyX509TrustManager implements X509TrustManager {
	
	private java.security.cert.X509Certificate[] acceptedIssuers = null;
	
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
    
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
    
    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
        return this.acceptedIssuers;
    }
	
    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException { }
	
    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException { }
}