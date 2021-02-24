package com.example.fuerzaventaschema.genesys.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
public class ConvertStreamToString {
	
	String string = "";
	
	public ConvertStreamToString() { }
	
	public ConvertStreamToString(InputStream is) {
		setConvertedString(is);
    }
	
	public String getConvertedString () {
		return this.string;
	}
	
	protected void setConvertedString(InputStream is) {
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
 
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            this.string = sb.toString();
        } catch (IOException e) {
            this.string = "Error: " + e.getMessage();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
            	string = "Error: " + e.getMessage();
            }
        }
	}
	
}