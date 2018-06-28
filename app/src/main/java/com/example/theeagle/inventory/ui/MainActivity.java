package com.example.theeagle.inventory.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.theeagle.inventory.R;
import com.example.theeagle.inventory.data.Contract.Product;
import com.example.theeagle.inventory.data.DatabaseHelper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private DatabaseHelper databaseHelper = new DatabaseHelper(this);
    private TextView textView;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        listeners();

    }

    @Override
    protected void onStart() {
        super.onStart();
        displayData();
    }

    private void initViews() {
        textView = findViewById(R.id.text_view);
        floatingActionButton = findViewById(R.id.floating_btn);
    }

    private void listeners() {
        floatingActionButton.setOnClickListener(this);
    }

    private void displayData() {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        String[] projection = {
                Product.ID,
                Product.PRODUCT_NAME,
                Product.PRICE,
                Product.QUANTITY,
                Product.SUPPLIER_NAME,
                Product.SUPPLIER_PHONE_NUMBER
        };
        Cursor cursor = database.query(Product.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);
        if (cursor.getCount() != 0) {
            textView.setText(String.format(getString(R.string.container), cursor.getCount()));
            try {
                int idColumnIndex = cursor.getColumnIndex(Product.ID);
                int productNameColumnIndex = cursor.getColumnIndex(Product.PRODUCT_NAME);
                int priceColumnIndex = cursor.getColumnIndex(Product.PRICE);
                int quantityColumnIndex = cursor.getColumnIndex(Product.QUANTITY);
                int supplierNameColumnIndex = cursor.getColumnIndex(Product.SUPPLIER_NAME);
                int supplierPhoneNumberColumnIndex = cursor.getColumnIndex(Product.SUPPLIER_PHONE_NUMBER);
                while (cursor.moveToNext()) {
                    int productId = cursor.getInt(idColumnIndex);
                    String productName = cursor.getString(productNameColumnIndex);
                    int price = cursor.getInt(priceColumnIndex);
                    int quantity = cursor.getInt(quantityColumnIndex);
                    String supplierName = cursor.getString(supplierNameColumnIndex);
                    String supplierPhoneNumber = cursor.getString(supplierPhoneNumberColumnIndex);
                    textView.append("ID =" + productId + "name= " + productName + "PRICE =" + price +
                            "QUANTITY= " + quantity + "SUPPLIER_NAME= " + supplierName +
                            "SUPPLIER_PHONE_NUMBER= " + supplierPhoneNumber + "\n");
                }

            } finally {
                cursor.close();
            }
        } else {
            textView.setText(R.string.empty_state);
        }
    }

    private void insertProduct() {
        ContentValues values = new ContentValues();
        values.put(Product.PRODUCT_NAME, "LapTop");
        values.put(Product.PRICE, 1000);
        values.put(Product.QUANTITY, 11);
        values.put(Product.SUPPLIER_NAME, "Apple");
        values.put(Product.SUPPLIER_PHONE_NUMBER, "0125120235");
        Uri newUri = getContentResolver().insert(Product.CONTENT_URI, values);
        if (newUri == null) {
            Toast.makeText(this, "Insertion Failed", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(this, "Data Inserted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        insertProduct();
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.floating_btn:
                startActivity(new Intent(this, EditorActivity.class));
        }
    }
}
