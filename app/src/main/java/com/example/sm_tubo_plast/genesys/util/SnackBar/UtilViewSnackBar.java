package com.example.sm_tubo_plast.genesys.util.SnackBar;

import android.app.Activity;
import android.view.View;

import com.example.sm_tubo_plast.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class UtilViewSnackBar {
    public static void SnackBarDanger(Activity activity, View view, String mensaje){
        Snackbar snackbar= Snackbar.make(
                view,
                ""+mensaje,
                BaseTransientBottomBar.LENGTH_SHORT
        );
        snackbar.setBackgroundTint(activity.getResources().getColor(R.color.red_600));
        snackbar.show();
    }
    public static void SnackBarWarning(Activity activity, View view, String mensaje){
        Snackbar snackbar= Snackbar.make(
                view,
                ""+mensaje,
                BaseTransientBottomBar.LENGTH_SHORT
        );
        snackbar.setBackgroundTint(activity.getResources().getColor(R.color.yellow_800));
        snackbar.show();
    }
    public static void SnackBarSuccess(Activity activity, View view, String mensaje){
        Snackbar snackbar= Snackbar.make(
                view,
                ""+mensaje,
                BaseTransientBottomBar.LENGTH_SHORT
        );
        snackbar.setBackgroundTint(activity.getResources().getColor(R.color.teal_500));
        snackbar.show();
    }
}
