package com.example.fuerzaventaschema.genesys.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInstaller;
import android.util.Log;












import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.Security;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.Transport;
import javax.mail.util.ByteArrayDataSource;
import javax.mail.internet.MimeMultipart;
import javax.mail.Multipart;
import javax.mail.BodyPart;
import javax.activation.DataSource;
import javax.mail.internet.MimeBodyPart;
import javax.activation.FileDataSource;


/**
 * Created by vlademer on 08/09/2015.
 */
public class GmailSender extends javax.mail.Authenticator{
    private static String TAG = "GmailSender";
    private String user;
    private String password;

    private String[] mailTo;
    private String  mailFrom;

    private String mailSmtp_port;
    private String mailSocket_port;
    private String mailHost="smtp.gmail.com";

    private String mailSubjet;
    private String mailBody;

    private boolean autentification;
    private boolean debuggable;
    private Session session;
    private Multipart _multipart;
    private Multipart multipart;

    private String ruta;
    
    static {
        Security.addProvider(new com.example.fuerzaventaschema.genesys.util.JSSEProvider());
    }

    public GmailSender(String user, String password){
        this.user = user;
        this.password = password;
        _multipart = new MimeMultipart();
        multipart = new MimeMultipart();

        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.host", mailHost);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.socketFactory.fallback", "false");
        properties.setProperty("mail.smtp.quitwait", "false");
        session = Session.getDefaultInstance(properties, this);
    }
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(user, password);
    }

    public synchronized void sendMail(String subject, String body, String sender,String aliasSender, String recipients) throws Exception {
        try{
        	
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sender,aliasSender));
            if (recipients.indexOf(',') > 0){ message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients)); }
            else{ message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipients)); }
            message.setSubject(subject);
            /* para archivos planos unicamente, sin necesidad de usar multiparts
            DataHandler handler = new DataHandler(new ByteArrayDataSource(body.getBytes(), "text/plain"));
            message.setDataHandler(handler);
            */
            
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(body);
                        
        	multipart.addBodyPart(messageBodyPart);
        	messageBodyPart = new MimeBodyPart();            
            
            message.setContent(multipart);
            Transport.send(message);            
        	
            Log.i(TAG + " Mail", "Correo enviado");
        }catch(Exception e){
        	e.printStackTrace();
        }
    }

    public void addAttachment(String filename,String subject) throws Exception {
    	ruta = filename;
    	File inputFile = new File(filename);
    	/*
    	// Antes de adjuntar el fichero se comprime en zip    	
        String outputFilename = filename+".zip";
        File outputFile = new File(outputFilename);
        FileOutputStream fos = new FileOutputStream(outputFile);
        File inputFile = new File(filename);
        BufferedInputStream fis =  new BufferedInputStream(new FileInputStream(inputFile));

        ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(fos));
        try {

            byte[] buffer = new byte[1024];
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            int len1 = 0;
            while ((len1 = fis.read(buffer)) != -1) {
                stream.write(buffer, 0, len1);
            }

            byte[] bytes = stream.toByteArray();
            ZipEntry entry = new ZipEntry(outputFilename);
            zos.putNextEntry(entry);
            zos.write(bytes);
            zos.closeEntry();
        }catch(Exception e){
        	e.printStackTrace();
        } finally {
            zos.close();
            fos.close();
            fis.close();
        }
        */

        // Se adjunta el zip
        BodyPart messageBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(filename);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(inputFile.getName());
        multipart.addBodyPart(messageBodyPart);
    }
}
