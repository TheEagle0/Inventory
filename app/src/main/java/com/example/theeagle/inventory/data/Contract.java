package com.example.theeagle.inventory.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class Contract {
    public Contract() {
    }

    public static final class Product implements BaseColumns {
        public static final String TABLE_NAME = "product";
        public static final String PRODUCT_NAME = "product_name";
        public static final String PRICE = "PRICE";
        public static final String QUANTITY = "QUANTITY";
        public static final String SUPPLIER_NAME = "supplier_name";
        public static final String SUPPLIER_PHONE_NUMBER = "supplier_phone_number";
        static final String AUTHORITY = "com.example.theeagle.inventory";
        static final String SCHEMA = "content://";
        static final String INVENTORY_PATH = "product";
        static final Uri BASE_URI = Uri.parse(SCHEMA + AUTHORITY);
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI, INVENTORY_PATH);
        static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + INVENTORY_PATH;
        static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + INVENTORY_PATH;


    }
}
