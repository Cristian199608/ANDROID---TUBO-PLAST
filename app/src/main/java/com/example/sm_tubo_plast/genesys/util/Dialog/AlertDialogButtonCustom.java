package com.example.sm_tubo_plast.genesys.util.Dialog;

import android.graphics.Color;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;

import com.example.sm_tubo_plast.R;

public class AlertDialogButtonCustom {

    public static void setCustomButon(AlertDialog dialog){

        Button btn = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        LinearLayout.LayoutParams params=null;
        if(btn!=null){
            btn.setBackgroundResource(R.drawable.bg_dialog_button);
            btn.setTextColor(Color.WHITE);
            params =(LinearLayout.LayoutParams) btn.getLayoutParams();
            params.setMargins(5,10,5,1);
            btn.setLayoutParams(params);
        }

        Button btnNEG = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        if(btnNEG!=null){
            btnNEG.setBackgroundResource(R.drawable.bg_dialog_button_negative);
            btnNEG.setTextColor(Color.WHITE);
            params =(LinearLayout.LayoutParams) btnNEG.getLayoutParams();
            params.setMargins(5,10,5,1);
            btnNEG.setLayoutParams(params);
        }

        Button btnNEU = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);
        if(btnNEU!=null){
            btnNEU.setBackgroundResource(R.drawable.bg_dialog_button_neutral);
            btnNEU.setTextColor(Color.BLACK);
            params =(LinearLayout.LayoutParams) btnNEU.getLayoutParams();
            params.setMargins(5,10,5,1);
            btnNEU.setLayoutParams(params);
        }
    }
}

