package com.selvaraj.buyerapp.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.selvaraj.buyerapp.Interface.NetworkChangeListener;
import com.selvaraj.buyerapp.R;
import com.selvaraj.buyerapp.utils.NetworkChangeReceiver;


public class BaseActivity extends AppCompatActivity implements NetworkChangeListener {

    private IntentFilter intentFilter = new IntentFilter();
    private NetworkChangeReceiver networkChangeReceiver;
    private ProgressDialog progress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.setPriority(100);
        progress = new ProgressDialog(this);
        networkChangeReceiver = new NetworkChangeReceiver(this);
    }

    public void showProgress(String msg) {
        progress.setMessage(msg);
        progress.show();
    }

    public void hideProgress() {
        progress.cancel();
    }

    /**
     * Method to set home menu in action bar.
     *
     * @param shouldSet contains boolean value.
     */
    public void setHomeInSupportActionBar(boolean shouldSet) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(shouldSet); // Set common home menu.
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(networkChangeReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkChangeReceiver);
    }

    @Override
    public void onNetworkChange(String networkState) {
        showToast(networkState);
    }

    /**
     * This method will show no network toast
     * It get called when  calls onNoNetwork callback.
     */
    public void showNoNetworkToast() {
        Toast.makeText(this, getString(R.string.no_network_toast), Toast.LENGTH_LONG).show();
    }

    /**
     * Method called from any activity that extents this.
     * To show the toast message.
     *
     * @param msg contains message that should displayed on toast.
     */
    public void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
