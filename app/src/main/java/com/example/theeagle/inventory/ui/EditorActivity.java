package com.example.theeagle.inventory.ui;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.theeagle.inventory.R;

public class EditorActivity extends AppCompatActivity implements View.OnClickListener {
    private TextInputEditText productName, price, quantity, supplierName, supplierPhoneNumber;
    private Button addInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        initViews();
        listeners();
    }


    private void initViews() {
        productName = findViewById(R.id.product_name);
        price = findViewById(R.id.price);
        quantity = findViewById(R.id.quantity);
        supplierName = findViewById(R.id.supplier_name);
        supplierPhoneNumber = findViewById(R.id.supplier_phone_number);
        addInfo = findViewById(R.id.add);
    }

    private void listeners() {
        addInfo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add:
                break;
        }
    }
}
