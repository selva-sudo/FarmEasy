package com.selvaraj.buyerapp.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.selvaraj.buyerapp.Interface.NetworkChangeListener;
import com.selvaraj.buyerapp.R;

/**
 * {@link BroadcastReceiver} class to check network state on activities.
 * The onReceive method this called when there is a connectivity change.
 */
public class NetworkChangeReceiver extends BroadcastReceiver {
    private NetworkChangeListener listener;

    public NetworkChangeReceiver(NetworkChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Object systemServiceObj = context.getSystemService(Context.CONNECTIVITY_SERVICE);
        ConnectivityManager connectivityManager = (ConnectivityManager) systemServiceObj;
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean networkIsAvailable = false;
        if (networkInfo != null) {
            if (networkInfo.isAvailable()) {
                networkIsAvailable = true;
            }
        }
        String networkMessage;
        if (networkIsAvailable) {
            networkMessage = context.getString(R.string.network_state_on);
        } else {
            networkMessage = context.getString(R.string.network_state_off);
        }
        listener.onNetworkChange(networkMessage);
    }
}
