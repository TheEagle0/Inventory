package com.example.theeagle.inventory.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATA_BASE_NAME = "inventory";
    private static final int DATABASE_VERSION = 1;

     DatabaseHelper(Context context) {
        super(context, DATA_BASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_INVENTORY_TABLE = "CREATE TABLE " + Contract.Product.TABLE_NAME + "("
                + Contract.Product._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Contract.Product.PRODUCT_NAME + " TEXT, " +
                Contract.Product.PRICE + " REAL, " +
                Contract.Product.QUANTITY + " INTEGER, " +
                Contract.Product.SUPPLIER_NAME + " TEXT, " +
                Contract.Product.SUPPLIER_PHONE_NUMBER + " TEXT);";
        db.execSQL(SQL_CREATE_INVENTORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
