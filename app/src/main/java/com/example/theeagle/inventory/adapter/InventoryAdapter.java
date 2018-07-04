package com.example.theeagle.inventory.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.theeagle.inventory.R;
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
        holder.price.setText("Price " + Double.toString(holder.productModel.getPrice()));
        holder.quantity.setText(Integer.toString(holder.productModel.getQuantity()));

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

        private ViewHolder(View itemView, Context context, ArrayList<ProductModel> dataLists) {
            super(itemView);
            this.context = context;
            this.dataLists = dataLists;

            name = itemView.findViewById(R.id.product_name);
            price = itemView.findViewById(R.id.price);
            quantity = itemView.findViewById(R.id.quantity);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            ProductModel productModel1 = this.dataLists.get(position);
            context.startActivity(new Intent(context, EditorActivity.class)
                    .putExtra("name", productModel1.getProductName())
                    .putExtra("price", productModel1.getPrice())
                    .putExtra("supplier name", productModel1.getSupplierName())
                    .putExtra("phone", productModel1.getPhoneNumber())
                    .putExtra("quantity", productModel1.getQuantity())
                    .putExtra("id", productModel1.getId()));
        }
    }
}
