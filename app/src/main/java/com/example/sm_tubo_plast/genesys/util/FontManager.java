package com.example.sm_tubo_plast.genesys.util;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FontManager {
	public static final String ROOT = "fonts/";
	public static final String FONTAWESOME = ROOT + "fontawesome-webfont.ttf";

	public static Typeface getTypeface(Context context, String font) {
		return Typeface.createFromAsset(context.getAssets(), font);
	}
	
	//Marcar como un contenedor de icons (Le da typeface a todos los hijos)
	public static void markAsIconContainer(View v, Typeface typeface) {
        if (v instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) v;
            for (int i = 0; i < vg.getChildCount(); i++) {
                View child = vg.getChildAt(i);
                markAsIconContainer(child, typeface);
            }
        } else if (v instanceof TextView) {
            ((TextView) v).setTypeface(typeface);
        }
    }
}
