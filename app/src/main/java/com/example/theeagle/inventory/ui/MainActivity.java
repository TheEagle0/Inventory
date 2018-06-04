package com.example.theeagle.inventory.ui;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.theeagle.inventory.R;
import com.example.theeagle.inventory.data.Contract.Product;
import com.example.theeagle.inventory.data.DatabaseHelper;
public class MainActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper = new DatabaseHelper(this);
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        displayData();
    }

    private void initViews() {
        textView = findViewById(R.id.text_view);
    }

    private void displayData() {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        String[] projection = {
                Product.id,
                Product.productName,
                Product.price,
                Product.quantity,
                Product.supplierName,
                Product.supplierPhoneNumber
        };
        Cursor cursor = database.query(Product.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);
        if (cursor.getCount()!=0){
        textView.setText(String.format(getString(R.string.container),cursor.getCount()));}
        else {
            textView.setText(R.string.empty_state);
        }
        cursor.close();
    }

    private void insertProduct() {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Product.productName, "LapTop");
        values.put(Product.price, 1000);
        values.put(Product.quantity, 11);
        values.put(Product.supplierName, "Apple");
        values.put(Product.supplierPhoneNumber, "0125120235");
        database.insert(Product.TABLE_NAME, null, values);
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
}
