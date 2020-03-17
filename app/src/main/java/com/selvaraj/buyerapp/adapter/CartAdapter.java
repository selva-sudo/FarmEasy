package com.selvaraj.buyerapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.selvaraj.buyerapp.R;
import com.selvaraj.buyerapp.model.Cart;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private List<Cart> cartList;

    public CartAdapter(List<Cart> cartList) {
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_cart, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Cart cart = cartList.get(i);
        viewHolder.tvProductQuantity.setText(cart.getProductQuantity());
        viewHolder.tvTotalPrice.setText(cart.getTotalPrice());
        viewHolder.tvProductName.setText(cart.getProductName());
        viewHolder.tvDateOfBuying.setText(cart.getDateOfBuying());
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvBuyerName, tvProductName, tvProductQuantity, tvTotalPrice, tvDateOfBuying;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tv_product_cart_name);
            tvProductQuantity = itemView.findViewById(R.id.tv_product_cart_quantity);
            tvTotalPrice = itemView.findViewById(R.id.tv_product_cart_price);
            tvDateOfBuying = itemView.findViewById(R.id.tv_product_cart_date);
        }
    }

    public void updateDetails(List<Cart> cartList) {
        this.cartList.addAll(cartList);
        notifyDataSetChanged();
    }
}
