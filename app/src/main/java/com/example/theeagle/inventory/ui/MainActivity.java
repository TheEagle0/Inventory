package com.example.theeagle.inventory.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.theeagle.inventory.R;
import com.example.theeagle.inventory.adapter.InventoryAdapter;
import com.example.theeagle.inventory.data.Contract.Product;
import com.example.theeagle.inventory.models.ProductModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {
    private static final int LOADER_ID = 0;
    String[] projection = {
            Product.ID,
            Product.PRODUCT_NAME,
            Product.PRICE,
            Product.QUANTITY,
            Product.SUPPLIER_NAME,
            Product.SUPPLIER_PHONE_NUMBER
    };
    private TextView textView;
    private FloatingActionButton floatingActionButton;
    private ArrayList<ProductModel> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initLoader();


    }

    private void initLoader() {
        getLoaderManager().initLoader(LOADER_ID, null, this);

    }


    private void initViews() {
        textView = findViewById(R.id.text_view);
        floatingActionButton = findViewById(R.id.floating_btn);
        RecyclerView recyclerView=findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        InventoryAdapter adapter =new InventoryAdapter(this,dataList);
        recyclerView.setAdapter(adapter);
        listeners();
    }

    private void listeners() {
        floatingActionButton.setOnClickListener(this);
    }

    private void insertProduct() {
        ContentValues values = new ContentValues();
        values.put(Product.PRODUCT_NAME, "LapTop");
        values.put(Product.PRICE, 1000.20);
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

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        return new CursorLoader(this, Product.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        dataList = new ArrayList<>();
        Cursor cursor = getContentResolver().query(Product.CONTENT_URI,
                projection,
                null,
                null,
                null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                ProductModel productModel = new ProductModel();
                int productName = cursor.getColumnIndexOrThrow(Product.PRODUCT_NAME);
                int price = cursor.getColumnIndexOrThrow(Product.PRICE);
                int quantity = cursor.getColumnIndexOrThrow(Product.QUANTITY);
                int supplierName = cursor.getColumnIndexOrThrow(Product.SUPPLIER_NAME);
                int supplierPhone = cursor.getColumnIndexOrThrow(Product.SUPPLIER_PHONE_NUMBER);

                productModel.setProductName(cursor.getString(productName));
                productModel.setPrice(cursor.getDouble(price));
                productModel.setQuantity(cursor.getInt(quantity));
                productModel.setSupplierName(cursor.getString(supplierName));
                productModel.setPhoneNumber(cursor.getString(supplierPhone));
                dataList.add(productModel);
            }
            cursor.close();
        }
        initViews();

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}
