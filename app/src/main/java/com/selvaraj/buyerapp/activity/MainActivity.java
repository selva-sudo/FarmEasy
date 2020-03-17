package com.selvaraj.buyerapp.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;
import com.selvaraj.buyerapp.Interface.GetUserNameListener;
import com.selvaraj.buyerapp.R;
import com.selvaraj.buyerapp.adapter.TabAdapter;
import com.selvaraj.buyerapp.base.BaseApplication;
import com.selvaraj.buyerapp.base.UserActivity;
import com.selvaraj.buyerapp.fragments.ProductFragment;
import com.selvaraj.buyerapp.fragments.QueryFragment;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends UserActivity implements GetUserNameListener {
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar_tab);
        BaseApplication.getInstance().getFireBaseUtils().getAuthUserName(this);
        setSupportActionBar(toolbar);
        ViewPager viewPager = findViewById(R.id.view_pager_tab);
        TabLayout tabLayout = findViewById(R.id.tab_layout_follow);
        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager(), this);
        tabAdapter.addFragment(ProductFragment.newInstance(), "Products");
        tabAdapter.addFragment(QueryFragment.newInstance(), "Queries");
        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void hideKey() {
        hideKeyboard(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.menu_search);
        searchView = (SearchView) item.getActionView();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
        }
    }

    @Override
    public void onSuccess(String name) {

    }
}
