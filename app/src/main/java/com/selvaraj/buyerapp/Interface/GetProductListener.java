package com.selvaraj.buyerapp.Interface;

import com.selvaraj.buyerapp.model.Products;

import java.util.List;

public interface GetProductListener {
    void onSuccess(List<Products> productList);
}
