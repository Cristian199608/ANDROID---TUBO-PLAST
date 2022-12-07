package com.example.sm_tubo_plast.genesys.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.sm_tubo_plast.genesys.util.VARIABLES;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DAO_Database extends SQLiteAssetHelper {
    public DAO_Database(Context context) {
        super(context, VARIABLES.DATABASA_NAME, null,  VARIABLES.DATABASA_VERSION);
    }
}
