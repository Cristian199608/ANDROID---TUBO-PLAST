package com.example.sm_tubo_plast.genesys.service;

import android.content.Context;
import android.util.Log;

import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpsTransportSE;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

public class ServiceCall {
	private static final String SOAP_ACTION = "http://tempuri.org/";
	private static final String NAMESPACE = "http://tempuri.org/";
	
	private static final String SERVICE = "etcService.asmx";
	private static final String URL = "your.domain.etc";
	private static final int PORT = 0;
	private static final int TIMEOUT = 0;
	private boolean isResultVector = false;
	
	protected Object call(String soapAction, SoapSerializationEnvelope envelope){
		Object result = null;
		
		final HttpsTransportSE transportSE = new HttpsTransportSE(URL, PORT, SERVICE, TIMEOUT);
		
		HttpsURLConnection.setDefaultHostnameVerifier(new X509HostnameVerifier () {
			@Override
			public boolean verify(String host, SSLSession session) {
				return true;
			}
			@Override
			public void verify(String host, SSLSocket ssl) throws IOException { }
			@Override
			public void verify(String host, X509Certificate cert) throws SSLException { }
			@Override
			public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException { }			
		});
		
		HttpsURLConnection.setDefaultSSLSocketFactory(new SSLSocketFactory () {
			private SSLContext sslcontext = null;
			
			private SSLContext createEasySSLContext() throws IOException {
				try {
					SSLContext context = SSLContext.getInstance("TLS"); //or "BKS", or "SSL", it worked with TLS for us
					context.init(null, new TrustManager[] { new EasyX509TrustManager() }, null);
					return context;
				} catch (Exception e) {
					throw new IOException(e.getMessage());
				}
			}
			private SSLContext getSSLContext() throws IOException {
				if (this.sslcontext == null) {
					this.sslcontext = createEasySSLContext();
				}
				return this.sslcontext;
			}
			public Socket connectSocket(Socket sock, String host, int port, InetAddress localAddress, int localPort, HttpParams params) throws IOException, UnknownHostException, ConnectTimeoutException {
				
				int connTimeout = HttpConnectionParams.getConnectionTimeout(params);
				int soTimeout = HttpConnectionParams.getSoTimeout(params);
				InetSocketAddress remoteAddress = new InetSocketAddress(host, port);
				SSLSocket sslsock = (SSLSocket) ((sock != null) ? sock : createSocket());
				if ((localAddress != null) || (localPort > 0)) {
					if (localPort < 0) { // we need to bind explicitly
						localPort = 0; // indicates "any"
					}
					InetSocketAddress isa = new InetSocketAddress(localAddress, localPort);
					sslsock.bind(isa);
				}
				sslsock.connect(remoteAddress, connTimeout);
				sslsock.setSoTimeout(soTimeout);
				return sslsock;
			}
			public Socket createSocket() throws IOException {
				return getSSLContext().getSocketFactory().createSocket();
			}
			public boolean isSecure(Socket socket) throws IllegalArgumentException {
				return true;
			}			
			public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
				return getSSLContext().getSocketFactory().createSocket(socket, host, port, autoClose);
			}
			// -------------------------------------------------------------------
			// javadoc in org.apache.http.conn.scheme.SocketFactory says :
			// Both Object.equals() and Object.hashCode() must be overridden
			// for the correct operation of some connection managers
			// -------------------------------------------------------------------
			public boolean equals(Object obj) { return ((obj != null) && obj.getClass().equals( EasySSLSocketFactory.class)); }
			
			public int hashCode() { return EasySSLSocketFactory.class.hashCode(); }
			
			@Override
			public String[] getDefaultCipherSuites() { return null; }
			
			@Override
			public String[] getSupportedCipherSuites() { return null; }
			
			@Override
			public Socket createSocket(String host, int port) throws IOException, UnknownHostException { return null; }
			
			@Override
			public Socket createSocket(InetAddress host, int port) throws IOException { return null; }
			
			@Override
			public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException, UnknownHostException { return null; }
			
			@Override
			public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException { return null; }
		});
		
		transportSE.debug = false;
		//call and Parse Result.		
		try{			
			transportSE.call(soapAction, envelope); //the call itself
			
			if (!isResultVector){
				result = envelope.getResponse();
				Log.e("RESULT: ", result.toString());
			} else {
				result = envelope.bodyIn;
				Log.e("RESULT: ", envelope.bodyIn.toString());
			}
		} catch (final IOException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
        public Object CallGetRiasztas (String sn, Context ctx) {
		
                //we're using our own classes, "Riasztas" would be in english like "Alert"
//descomentar		Hashtable<Integer, Riasztas> riasztas = new Hashtable<Integer, Riasztas>();
		final String sGetRiasztas = "GetRiasztas";
		
		// Create the outgoing message
		final SoapObject requestObject = new SoapObject(NAMESPACE, sGetRiasztas);
		requestObject.addProperty("sn", sn); //adding some property
		
		// Create soap envelop .use version 1.1 of soap
		final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;
		
		// add the outgoing object as the request
		envelope.setOutputSoapObject(requestObject);
		
// SIGUIENTE LINEA COMENTADA ::*************************************
		
//descomentar		envelope.addMapping(NAMESPACE, Riasztas.RIASZTAS_CLASS.getSimpleName(), Riasztas.RIASZTAS_CLASS);
		// call and Parse Result.
		final Object response = this.call(SOAP_ACTION + sGetRiasztas, envelope);
		
		
		SoapObject soapObj = (SoapObject)response;
		
		if (response != null){
			for(int i = 0; i < soapObj.getPropertyCount(); ++i) {
				Log.e("LOOP:", String.valueOf(i));
				try {
					SoapObject so = (SoapObject) soapObj.getProperty(i);
//	descomentar				riasztas.put(i, new Riasztas(so));
				} catch (Exception e) {
					// TODO: handle exception
					Log.e("EROOR:", e.getMessage());
				}
			}
		}
		
		return null;
	//	return riasztas;
	}
}
