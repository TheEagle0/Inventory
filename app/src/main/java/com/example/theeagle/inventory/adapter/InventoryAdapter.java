package com.example.theeagle.inventory.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.theeagle.inventory.R;
import com.example.theeagle.inventory.models.ProductModel;

import java.util.ArrayList;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.ViewHolder> {
    private ArrayList<ProductModel> dataList;
    private Context context;

    public InventoryAdapter(Context context,ArrayList<ProductModel> dataList) {
        this.dataList = dataList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view, parent, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.productModel = dataList.get(position);
        holder.name.setText(holder.productModel.getProductName());
        holder.price.setText("Price "+Double.toString(holder.productModel.getPrice()));
        holder.quantity.setText(Integer.toString(holder.productModel.getQuantity()));

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name, price, quantity;
        private Context context;
        private ProductModel productModel;

        private ViewHolder(View itemView, Context context) {
            super(itemView);
            this.context = context;

            name = itemView.findViewById(R.id.product_name);
            price = itemView.findViewById(R.id.price);
            quantity = itemView.findViewById(R.id.quantity);

        }
    }
}
