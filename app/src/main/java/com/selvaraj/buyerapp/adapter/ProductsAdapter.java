package com.selvaraj.buyerapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.selvaraj.buyerapp.Interface.RecyclerViewItemClickListener;
import com.selvaraj.buyerapp.R;
import com.selvaraj.buyerapp.base.BaseApplication;
import com.selvaraj.buyerapp.model.Products;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {
    private List<Products> productsList;
    private RecyclerViewItemClickListener listener;

    public ProductsAdapter(List<Products> productsList, RecyclerViewItemClickListener listener) {
        this.productsList = productsList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, null);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Products product = productsList.get(position);
        holder.tvProductName.setText(product.getName());
        holder.tvProductCount.setText(product.getQuantity());
        holder.tvProductPrice.setText(product.getPrice());
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public void updateDetails(List<Products> productList) {
        this.productsList=productList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvProductName, tvProductPrice, tvProductCount;

        public ViewHolder(@NonNull View itemView, final RecyclerViewItemClickListener listener) {
            super(itemView);
            tvProductCount = itemView.findViewById(R.id.tv_product_item_quantity);
            tvProductName = itemView.findViewById(R.id.tv_product_item_name);
            tvProductPrice = itemView.findViewById(R.id.tv_product_item_price);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(getAdapterPosition());
                }
            });
        }
    }
}
