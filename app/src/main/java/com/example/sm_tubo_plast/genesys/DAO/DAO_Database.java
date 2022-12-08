package com.example.sm_tubo_plast.genesys.DAO;

import android.content.Context;

import com.example.sm_tubo_plast.genesys.util.VARIABLES;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DAO_Database extends SQLiteAssetHelper {
    public DAO_Database(Context context) {
        super(context, VARIABLES.ConfigDatabase.getDatabaseName(), null,  VARIABLES.ConfigDatabase.getDatabaseVersion());
    }
}
