package com.example.sm_tubo_plast.genesys.CreatePDF.pdf_html.util;

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;

public class UtilImagen {

    public static String getImagenString(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 10, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String imgageBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
//        String image = "data:image/png;base64," + imgageBase64;
        String image = "" + imgageBase64;
      //  Log.i("UTILVIEW", "getImageString :: "+image);
        //createImage(bitmap);

        return image;
    }


}
