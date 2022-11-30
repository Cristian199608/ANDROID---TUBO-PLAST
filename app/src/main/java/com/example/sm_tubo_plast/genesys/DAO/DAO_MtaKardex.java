package com.example.sm_tubo_plast.genesys.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.sm_tubo_plast.genesys.datatypes.DBMta_Kardex;

public class DAO_MtaKardex extends DAO_Database {
    private static final String TAG = "DAO_MtaKardex";
    public DAO_MtaKardex(Context context) {
        super(context);
    }

    public void UpdateItem(String codpro, double stock, double xtemp) {
        ContentValues up=new ContentValues();
        up.put("stock", stock);
        up.put("xtemp", xtemp);
        String[] arg= {codpro};
        SQLiteDatabase db=getWritableDatabase();
        long x=db.update("mta_kardex", up, "codpro = ?",  arg);
        Log.i(TAG, " actualizaddo "+codpro+" "+(x>0));
    }


    public DBMta_Kardex GetStockProducto( String codpro){
            String rawQuery;
            rawQuery = "select codalm, stock, xtemp  from mta_kardex where codpro='"+ codpro + "'";
            Log.d("ALERT-", " ." + rawQuery);

            DBMta_Kardex stock = null;

            SQLiteDatabase db = getReadableDatabase();
            Cursor cur = db.rawQuery(rawQuery, null);

            while (cur.moveToNext()) {
                stock=new DBMta_Kardex();
                    stock.setCodalm(cur.getString(cur.getColumnIndex("codalm")));
                    stock.setStock(cur.getDouble(cur.getColumnIndex("stock")));
                    stock.setXtemp(cur.getDouble(cur.getColumnIndex("xtemp")));
            }

            cur.close();
            db.close();

            return stock;
    }
}
