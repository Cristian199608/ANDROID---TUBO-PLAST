package com.example.sm_tubo_plast.genesys.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DAO_Database extends SQLiteAssetHelper {
    public static final String DATABASE_NAME = "fuerzaventas";
    public static final int DATABASE_VERSION = 1;

    public DAO_Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}