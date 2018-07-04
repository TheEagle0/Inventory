package com.example.theeagle.inventory.ui;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.theeagle.inventory.R;
import com.example.theeagle.inventory.data.Contract;

public class EditorActivity extends AppCompatActivity implements View.OnClickListener {
    private TextInputEditText productNameET, priceET, quantityET, supplierNameET, supplierPhoneNumberET;
    private Button addInfo;
    private ImageButton increase, decrease;
    private int quantity = 0;
    private Uri currentUri;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        initViews();
        getData();
        listeners();
    }

    @SuppressLint("SetTextI18n")
    private void getData() {
        intent = getIntent();
        if (intent.hasExtra("name")) {
            addInfo.setText("Update");
            setTitle("Update Product");
            productNameET.setText(intent.getStringExtra("name"));
            priceET.setText((Double.toString(intent.getDoubleExtra("price", 0))));
            quantityET.setText(Integer.toString(intent.getIntExtra("quantity", 0)));
            supplierNameET.setText(intent.getStringExtra("supplier name"));
            supplierPhoneNumberET.setText(intent.getStringExtra("phone"));
            int id = intent.getIntExtra("id", 0);
            currentUri = ContentUris.withAppendedId(Contract.Product.CONTENT_URI, id);
        } else {
            addInfo.setText("Add");
            setTitle("Add Product");
        }
    }


    private void initViews() {
        productNameET = findViewById(R.id.product_name);
        priceET = findViewById(R.id.price);
        quantityET = findViewById(R.id.quantity);
        supplierNameET = findViewById(R.id.supplier_name);
        supplierPhoneNumberET = findViewById(R.id.supplier_phone_number);
        addInfo = findViewById(R.id.add);
        increase = findViewById(R.id.increase_quantity);
        decrease = findViewById(R.id.decrease_quantity);
    }

    private void listeners() {
        addInfo.setOnClickListener(this);
        increase.setOnClickListener(this);
        decrease.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add:
                saveProduct();
                break;
            case R.id.decrease_quantity:
                String quantityDecrease = quantityET.getText().toString();
                if (!quantityDecrease.isEmpty()) {
                    quantity = Integer.parseInt(quantityDecrease);
                    if (quantity == 0) {
                        quantityET.setError("more");
                    } else {
                        quantity--;
                        quantityET.setText(Integer.toString(quantity));
                    }
                } else {
                    quantity = 0;
                    quantityET.setText(Integer.toString(quantity));
                }

                break;
            case R.id.increase_quantity:
                String quantityAdd = quantityET.getText().toString();
                if (!quantityAdd.isEmpty()) {
                    quantity = Integer.parseInt(quantityAdd);
                    quantity++;
                    quantityET.setText(Integer.toString(quantity));
                } else {
                    quantity = 1;
                    quantityET.setText(Integer.toString(quantity));
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        deleteProduct();
        return super.onOptionsItemSelected(item);
    }

    private void deleteProduct(){
        int deletedId=getContentResolver().delete(currentUri,Contract.Product.TABLE_NAME,null);
        if (deletedId==0){
            Toast.makeText(this, "Date Still thier", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Data removed", Toast.LENGTH_SHORT).show();
        }
}
    private void saveProduct() {
        ContentValues values = new ContentValues();
        String name = productNameET.getText().toString();
        String getPrice = priceET.getText().toString();
        String supplierName = supplierNameET.getText().toString();
        String phoneNumber = supplierPhoneNumberET.getText().toString();
        String quantityMain = quantityET.getText().toString();

        if (!name.isEmpty() && !getPrice.isEmpty() && !supplierName.isEmpty() && !phoneNumber.isEmpty() && !quantityMain.isEmpty()) {
            values.put(Contract.Product.PRODUCT_NAME, name);
            double price = Double.parseDouble(getPrice);
            if (price != 0) {
                values.put(Contract.Product.PRICE, price);

            } else {
                priceET.setError("price must be more than0");
            }
            values.put(Contract.Product.SUPPLIER_NAME, supplierName);
            values.put(Contract.Product.SUPPLIER_PHONE_NUMBER, phoneNumber);
            quantity = Integer.parseInt(quantityMain);
            values.put(Contract.Product.QUANTITY, quantity);
        } else {
            quantityET.setError("Please enter valid number");
            productNameET.setError("Please enter a valid name");
            priceET.setError("Please enter a valid price");
            supplierNameET.setError("Please enter a valid name");
            supplierPhoneNumberET.setError("Please enter a valid phone number");
        }

        if (intent.hasExtra("name")) {
            int rowAffected = getContentResolver().update(currentUri, values, null, null);
            if (rowAffected == 0) {
                Toast.makeText(this, "No data changed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Data Updated", Toast.LENGTH_SHORT).show();
            }
        } else {

            Uri newUri = getContentResolver().insert(Contract.Product.CONTENT_URI, values);
            if (newUri == null) {
                Toast.makeText(this, "Insertion Failed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Data Inserted", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

    }
}
