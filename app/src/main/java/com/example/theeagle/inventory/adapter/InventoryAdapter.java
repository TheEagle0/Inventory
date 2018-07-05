package com.example.theeagle.inventory.adapter;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.theeagle.inventory.R;
import com.example.theeagle.inventory.data.Contract;
import com.example.theeagle.inventory.models.ProductModel;
import com.example.theeagle.inventory.ui.EditorActivity;

import java.util.ArrayList;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.ViewHolder> {
    private ArrayList<ProductModel> dataList;
    private Context context;

    public InventoryAdapter(Context context, ArrayList<ProductModel> dataList) {
        this.dataList = dataList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view, parent, false);
        return new ViewHolder(view, context, dataList);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.productModel = dataList.get(position);
        holder.name.setText(holder.productModel.getProductName());
        holder.price.setText(String.format("Price %s", Double.toString(holder.productModel.getPrice())));
        String getQuantity=Integer.toString(holder.productModel.getQuantity());
        holder.quantity.setText(getQuantity);

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name, price, quantity;
        private Context context;
        private ProductModel productModel;
        private ArrayList<ProductModel> dataLists;
        private int newQuantity;

        private ViewHolder(View itemView, Context context, ArrayList<ProductModel> dataLists) {
            super(itemView);
            this.context = context;
            this.dataLists = dataLists;

            name = itemView.findViewById(R.id.product_name);
            price = itemView.findViewById(R.id.price);
            quantity = itemView.findViewById(R.id.quantity);
            itemView.setOnClickListener(this);
            quantity.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            ProductModel productModel1 = this.dataLists.get(position);
            if (v == itemView) {
                context.startActivity(new Intent(context, EditorActivity.class)
                        .putExtra("name", productModel1.getProductName())
                        .putExtra("price", productModel1.getPrice())
                        .putExtra("supplier name", productModel1.getSupplierName())
                        .putExtra("phone", productModel1.getPhoneNumber())
                        .putExtra("quantity", productModel1.getQuantity())
                        .putExtra("id", productModel1.getId()));
            } else {
                newQuantity = productModel1.getQuantity();
                newQuantity--;
                if (newQuantity >= 0) {
                    updateItem();
                } else {
                    Toast.makeText(context, "Out of stock", Toast.LENGTH_SHORT).show();
                }
            }
        }

        private void updateItem() {
            int position = getAdapterPosition();
            ProductModel productModel2 = this.dataLists.get(position);
            ContentValues values = new ContentValues();
            String name = productModel2.getProductName();
            double getPrice = productModel2.getPrice();
            String supplierName = productModel2.getSupplierName();
            String phoneNumber = productModel2.getPhoneNumber();
            Uri currentUri = ContentUris.withAppendedId(Contract.Product.CONTENT_URI, productModel2.getId());
            values.put(Contract.Product.PRODUCT_NAME, name);
            values.put(Contract.Product.PRICE, getPrice);
            values.put(Contract.Product.SUPPLIER_NAME, supplierName);
            values.put(Contract.Product.SUPPLIER_PHONE_NUMBER, phoneNumber);
            values.put(Contract.Product.QUANTITY, newQuantity);

            context.getContentResolver().update(currentUri, values, null, null);
        }

    }

}
