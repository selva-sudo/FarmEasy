package com.selvaraj.buyerapp.Interface;

import com.selvaraj.buyerapp.model.Cart;

import java.util.List;

public interface GetCartListener {
    void onSuccess(List<Cart> cartList);
}
