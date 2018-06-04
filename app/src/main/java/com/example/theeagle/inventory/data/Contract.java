package com.example.theeagle.inventory.data;

import android.provider.BaseColumns;

public class Contract {
    public Contract() {
    }

    public static final class Product implements BaseColumns{
        public static final String id=BaseColumns._ID;
        public static final String TABLE_NAME="product";
        public static final String productName = "product_name";
        public static final String price = "price";
        public static final String quantity = "quantity";
        public static final String supplierName = "supplier_name";
        public static final String supplierPhoneNumber = "supplier_phone_number";
    }
}
