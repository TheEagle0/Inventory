package com.example.theeagle.inventory.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
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
            setTitle("Update Product");
            productNameET.setText(intent.getStringExtra("name"));
            priceET.setText((Double.toString(intent.getDoubleExtra("price", 0))));
            quantityET.setText(Integer.toString(intent.getIntExtra("quantity", 0)));
            supplierNameET.setText(intent.getStringExtra("supplier name"));
            supplierPhoneNumberET.setText(intent.getStringExtra("phone"));
            int id = intent.getIntExtra("id", 0);
            currentUri = ContentUris.withAppendedId(Contract.Product.CONTENT_URI, id);
        } else {
            setTitle("Add Product");
        }
    }


    private void initViews() {
        productNameET = findViewById(R.id.product_name);
        priceET = findViewById(R.id.price);
        quantityET = findViewById(R.id.quantity);
        supplierNameET = findViewById(R.id.supplier_name);
        supplierPhoneNumberET = findViewById(R.id.supplier_phone_number);
        increase = findViewById(R.id.increase_quantity);
        decrease = findViewById(R.id.decrease_quantity);
    }

    private void listeners() {
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
        switch (item.getItemId()) {
            case R.id.delete:
                showDeleteConfirmationDialog();
                break;
            case R.id.call:
                callSupplier(supplierPhoneNumberET.getText().toString());
                break;
            case R.id.done:
                saveProduct();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void callSupplier(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }

    private void deleteProduct() {
        int deletedId = getContentResolver().delete(currentUri, Contract.Product.TABLE_NAME, null);
        if (deletedId == 0) {
            Toast.makeText(this, R.string.data_notremoved, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.data_removed, Toast.LENGTH_SHORT).show();
            finish();
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
                priceET.setError(getString(R.string.price_error));
            }
            values.put(Contract.Product.SUPPLIER_NAME, supplierName);
            values.put(Contract.Product.SUPPLIER_PHONE_NUMBER, phoneNumber);
            quantity = Integer.parseInt(quantityMain);
            values.put(Contract.Product.QUANTITY, quantity);
        } else {
            Toast.makeText(this, R.string.empty_field, Toast.LENGTH_SHORT).show();
        }

        if (intent.hasExtra("name")) {
            int rowAffected = getContentResolver().update(currentUri, values, null, null);
            if (rowAffected == 0) {
                Toast.makeText(this, R.string.data_not_updated, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.data_updated, Toast.LENGTH_SHORT).show();
            }
        } else {

            Uri newUri = getContentResolver().insert(Contract.Product.CONTENT_URI, values);
            if (newUri == null) {
                Toast.makeText(this, R.string.insertion_faild, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.data_inserted, Toast.LENGTH_SHORT).show();
                finish();
            }
        }

    }

    private void showDeleteConfirmationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_message);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteProduct();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (!intent.hasExtra("name")) {
            MenuItem menuItem = menu.findItem(R.id.delete);
            menuItem.setVisible(false);
        }
        return true;
    }
}
