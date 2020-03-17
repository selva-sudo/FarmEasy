package com.selvaraj.buyerapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.selvaraj.buyerapp.Interface.GetCartListener;
import com.selvaraj.buyerapp.R;
import com.selvaraj.buyerapp.adapter.CartAdapter;
import com.selvaraj.buyerapp.base.BaseApplication;
import com.selvaraj.buyerapp.base.UserActivity;
import com.selvaraj.buyerapp.model.Cart;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CartListActivity extends UserActivity implements GetCartListener, SearchView.OnQueryTextListener, SearchView.OnCloseListener {

    private RecyclerView rvCart;
    private CartAdapter cartAdapter;
    private SearchView searchView;
    private List<Cart> cartList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list);
        setHomeInSupportActionBar(true);
        rvCart = findViewById(R.id.rv_cart);
        initRecyclerView();
        BaseApplication.getInstance().getFireBaseUtils().getCartList(this);
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvCart.setLayoutManager(linearLayoutManager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvCart.addItemDecoration(itemDecoration);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        rvCart.setItemAnimator(itemAnimator);
        cartAdapter = new CartAdapter(cartList);
        rvCart.setAdapter(cartAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        MenuItem cart = menu.findItem(R.id.menu_cart);
        cart.setVisible(false);
        this.invalidateOptionsMenu();
        MenuItem menuItem = menu.findItem(R.id.menu_search);
        searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_profile:
                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_logout:
                showLogoutAlert();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    @Override
    public void onSuccess(List<Cart> cartList) {
        if (cartList.size() != 0) {
            this.cartList = cartList;
            cartAdapter.updateDetails(cartList);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!searchView.isIconified()) {
            cartAdapter.updateDetails(cartList);
            searchView.setIconified(true);
        }
    }

    @Override
    public boolean onClose() {
        cartAdapter.updateDetails(cartList);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String newText) {
        newText = newText.toLowerCase();
        List<Cart> cart = new ArrayList<>();
        for (Cart cart1 : cartList) {
            String name = cart1.getProductName();
            if (name.contains(newText)) {
                cart.add(cart1);
            }
        }
        cartAdapter.updateDetails(cart);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toLowerCase();
        List<Cart> cart = new ArrayList<>();
        for (Cart cart1 : cartList) {
            String name = cart1.getProductName();
            if (name.contains(newText)) {
                cart.add(cart1);
            }
        }
        cartAdapter.updateDetails(cart);
        return true;
    }
}
