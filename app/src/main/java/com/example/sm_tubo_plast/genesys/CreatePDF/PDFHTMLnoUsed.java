package com.example.sm_tubo_plast.genesys.CreatePDF;

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;

public class PDFHTMLnoUsed {
    public String GenerateHtml(){
        String tbWidth = "1040px";

        String html="";
        html+="<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
                "\t<title> PDF Custom</title>\n" +
                "\n" +
                "<style type=\"text/css\">\n" +
                "\tbody{\n" +
                "\t\twidth: 1040px;\n" +
                "\t }\n" +
                "\t .font_secondary_text{\n" +
                "\t \tfont-size:17px;\n" +
                "\t }\n" +
                "\t .font_primary_text{\n" +
                "\t \tfont-size:20px;\n" +
                "\t }\n" +
                "\t .font_terceario_text{\n" +
                "\t \tfont-size:15px;\n" +
                "\t }\n" +
                "\t .contorno{\n" +
                "\toutline: red solid thin\n" +
                "\t }\n" +
                "\t .custom_border {\n" +
                "\t  border: 2px solid black;\n" +
                "\t  border-radius: 3px;\n" +
                "\t}\n" +
                "\t .upper_case {\n" +
                "\t  text-transform: uppercase;\n" +
                "\t}\n" +
                "\n" +
                "\t .contorno_black{\n" +
                "\tborder-radius: 2px, 2px, 2px, 2px;\n" +
                "\tborder-color: black;\n" +
                "\t }\n" +
                "\n" +
                ".custom_table {\n" +
                "  font-family: \"Trebuchet MS\", Arial, Helvetica, sans-serif;\n" +
                "  border-collapse: collapse;\n" +
                "  width: 100%;\n" +
                "}\n" +
                "\n" +
                ".custom_table td, .custom_table th {\n" +
                "  border: 1px solid #ddd;\n" +
                "  padding: 8px;\n" +
                "}\n" +
                "\n" +
                ".custom_table tr:nth-child(even){background-color: #f2f2f2;}\n" +
                "\n" +
                "\n" +
                ".custom_table th {\n" +
                "  padding-top: 12px;\n" +
                "  padding-bottom: 12px;\n" +
                "  text-align: left;\n" +
                "  background-color: #bdbdbd;\n" +
                "  color: black;\n" +
                "}\n" +
                "\n" +
                "</style>\n" +
                "\n" +
                "\n" +
                "</head>\n" +
                "<body style=\"width: 1060px;\">\n" +
                " \n" +
                "\n";

        return html;

    }
    public static String getImagenString(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 1, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String imgageBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
//        String image = "data:image/png;base64," + imgageBase64;
        String image = "" + imgageBase64;
        Log.i("UTILVIEW", "getImageString :: "+image);
        //createImage(bitmap);
        return image;
    }

}
