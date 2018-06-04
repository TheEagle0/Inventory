package com.example.theeagle.inventory.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATA_BASE_NAME = "inventory";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATA_BASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_INVENTORY_TABLE = "CREATE TABLE " + Contract.Product.TABLE_NAME + "("
                + Contract.Product.id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Contract.Product.productName + " TEXT, " +
                Contract.Product.price + " INTEGER, " +
                Contract.Product.quantity + " INTEGER, " +
                Contract.Product.supplierName + " TEXT, " +
                Contract.Product.supplierPhoneNumber + " TEXT);";
        db.execSQL(SQL_CREATE_INVENTORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
