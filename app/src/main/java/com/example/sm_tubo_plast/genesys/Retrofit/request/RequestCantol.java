package com.example.sm_tubo_plast.genesys.Retrofit.request;

public class RequestCantol {
    public  static String login(String user, String pass){
        String cadenaReques="{\n" +
                "  \"usuario_login\": \""+user+"\",\n" +
                "  \"password_login\": \""+pass+"\"\n" +
                "}";
        return cadenaReques;
    }


}
