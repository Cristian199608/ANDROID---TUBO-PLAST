package com.example.sm_tubo_plast.genesys.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import androidx.core.content.ContextCompat;

import com.example.sm_tubo_plast.R;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import me.piruin.quickaction.ActionItem;
import me.piruin.quickaction.QuickAction;

public class UtilView {

    public static void Efectos(Context context, View view, int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ColorDrawable drawableMask=new ColorDrawable(ContextCompat.getColor(context, R.color.grey_200));
            RippleDrawable rippleDrawablenew = null;
            rippleDrawablenew =new RippleDrawable(
                    ColorStateList.valueOf(ContextCompat.getColor(context, color)), null, null);
            view.setBackground(rippleDrawablenew);
            view.setClickable(true);
        }
    }

    public static void MENSAJES(Context context,String titutlo, String mensaje, int error, boolean calcelable){

        if (error==1 && mensaje.length()==0) mensaje="Verifica tu conexion a internet\ny vuelva a intentar de nuevo.";
        else if (error==2 && mensaje.length()==0 ) mensaje="No hay conexion al servidor.\nVuelva a intentar mas tarde.";
        android.app.AlertDialog.Builder dialog=new android.app.AlertDialog.Builder(context);
        if (titutlo!=null) dialog.setTitle(titutlo);
        dialog.setCancelable(calcelable);
        dialog.setMessage(mensaje);
        dialog.setPositiveButton("OK", null);
        dialog.create();
        dialog.show();
    }

    public static CheckBox GetCheckBoxEnvioSistema(Activity activity){
        CheckBox ne=new CheckBox(activity);
        ne.setText("Enviar al sistema");
        ne.setChecked(true);
        return  ne;
    }
    public static void MENSAJE_simple_finish(final Activity activity, String titulo, String mensaje, final boolean finish){
        android.app.AlertDialog.Builder dialog=new android.app.AlertDialog.Builder(activity);
        dialog.setTitle(titulo);
        dialog.setMessage(mensaje);
        dialog.setPositiveButton("Ok",null);
        dialog.create();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(finish) activity.finish();
            }
        });
        dialog.show();
    }

    public static ArrayAdapter<String> LLENAR_SPINNER_SIMPLE(Context context, ArrayList<String> lista){

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(context, R.layout.spinner_item, lista);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        return adapter;
    }
    public static void MENSAJE_simple_finish_return_intent(Activity activity, String titulo, String mensaje, boolean finish){
        android.app.AlertDialog.Builder dialog=new android.app.AlertDialog.Builder(activity);
        dialog.setTitle(titulo);
        dialog.setMessage(mensaje);
        dialog.setPositiveButton("Ok",null);
        dialog.create();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(finish){
                    Intent data=new Intent();
                    activity.setResult(activity.RESULT_OK, data);
                    activity.finish();
                }
            }
        });
        dialog.show();
    }
    public static void MENSAJE_simple(Context context, String titulo, String mensaje){
        android.app.AlertDialog.Builder dialog=new android.app.AlertDialog.Builder(context);
        dialog.setTitle(titulo);
        dialog.setMessage(mensaje);
        dialog.setPositiveButton("Entendido", null);
        dialog.create();
        dialog.show();
    }

    public static void Efectos_default(Context context,  View view){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            RippleDrawable rippleDrawablenew = null;
            rippleDrawablenew =new RippleDrawable(
                    ColorStateList.valueOf(ContextCompat.getColor(context, R.color.grey_200)), null, null);
            view.setBackground(rippleDrawablenew);
            view.setClickable(true);
        }
    }

    public static ArrayAdapter<String> LLENAR_SPINNER_SIMPLE2(Context context,String[] MESES ){

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(context, R.layout.spinner_item, MESES);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        return adapter;
    }

    public static  int[] getColorsSwipe(){
        int[] colorSwipe= {
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light};
        return colorSwipe;
    }


    public static void OperServicioMapas(final Context context, View anchor, final LatLng latLng, int orientacion){
        final int ID_google_maps=0;
        int ID_WAZE=1;
        ActionItem infoItem1 = new ActionItem(ID_google_maps, "Google Maps", R.drawable.imagen_google_maps);
        ActionItem infoItem2 = new ActionItem(ID_WAZE, "Waze Maps", R.drawable.imagen_waze);

        QuickAction mQuickAction = new QuickAction(context, orientacion);
        mQuickAction.setColorRes(R.color.white);
        mQuickAction.setTextColorRes(R.color.primary_text);
        mQuickAction.addActionItem(infoItem1);
        mQuickAction.addActionItem(infoItem2);
        mQuickAction.show(anchor);

        mQuickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
            @Override
            public void onItemClick(ActionItem item) {
                IntentTerceros intentTerceros =new IntentTerceros(context);
                int pos = item.getActionId();
                if (pos==ID_google_maps) intentTerceros.IntentGoogleMaps(latLng);
                else intentTerceros.IntentWazeMaps(latLng);
            }
        });
    }
}
