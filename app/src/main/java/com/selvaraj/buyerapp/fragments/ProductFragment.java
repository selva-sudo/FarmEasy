package com.selvaraj.buyerapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.selvaraj.buyerapp.Interface.GetProductListener;
import com.selvaraj.buyerapp.Interface.OnFarmerListCompleteListener;
import com.selvaraj.buyerapp.Interface.RecyclerViewItemClickListener;
import com.selvaraj.buyerapp.R;
import com.selvaraj.buyerapp.activity.CartListActivity;
import com.selvaraj.buyerapp.activity.MainActivity;
import com.selvaraj.buyerapp.activity.ProductDetailActivity;
import com.selvaraj.buyerapp.activity.ProfileActivity;
import com.selvaraj.buyerapp.adapter.ProductsAdapter;
import com.selvaraj.buyerapp.base.BaseApplication;
import com.selvaraj.buyerapp.model.Products;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ProductFragment extends Fragment implements GetProductListener, SearchView.OnQueryTextListener, SearchView.OnCloseListener, OnFarmerListCompleteListener {

    private RecyclerView rvProducts;
    private Context context;
    private List<Products> productsList = new ArrayList<>();
    private ImageView ivLoading;
    private ProductsAdapter productsAdapter;

    public ProductFragment() {
    }


    public static ProductFragment newInstance() {
        return new ProductFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvProducts = view.findViewById(R.id.rv_product);
        ivLoading = view.findViewById(R.id.iv_product_loading);
        ivLoading.setVisibility(View.VISIBLE);
        BaseApplication.getInstance().getFireBaseUtils().getFarmersList(this);
        initRecyclerView();
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        rvProducts.setLayoutManager(linearLayoutManager);
        rvProducts.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        rvProducts.addItemDecoration(itemDecoration);
        productsAdapter = new ProductsAdapter(productsList, new RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(context, ProductDetailActivity.class);
                BaseApplication.getInstance().getUserManager().setSelectedProduct(productsList.get(position));
                startActivity(intent);
            }
        });
        rvProducts.setAdapter(productsAdapter);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onSuccess(List<Products> productList) {
        this.productsList=productList;
        if (productList != null) {
            productsAdapter.updateDetails(productList);
        } else {
            Toast.makeText(context, "No new items", Toast.LENGTH_LONG).show();
        }
        ivLoading.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onClose() {
        productsAdapter.updateDetails(productsList);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String newText) {
        newText = newText.toLowerCase();
        List<Products> products = new ArrayList<>();
        for (Products product : productsList) {
            String name = product.getName();
            if (name.contains(newText)) {
                products.add(product);
            }
        }
        productsAdapter.updateDetails(products);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_cart:
                Intent intent = new Intent(context, CartListActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_profile:
                intent = new Intent(context, ProfileActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_logout:
                ((MainActivity)context).showLogoutAlert();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toLowerCase();
        List<Products> products = new ArrayList<>();
        for (Products product : productsList) {
            String name = product.getName();
            if (name.contains(newText)) {
                products.add(product);
            }
        }
        productsAdapter.updateDetails(products);
        return true;
    }

    @Override
    public void onComplete() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                BaseApplication.getInstance().getFireBaseUtils().getProductListDetails(ProductFragment.this);
            }
        });
    }
}
