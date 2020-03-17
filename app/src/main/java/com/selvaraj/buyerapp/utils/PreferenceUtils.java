package com.selvaraj.buyerapp.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.selvaraj.buyerapp.base.BaseApplication;

public class PreferenceUtils {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public PreferenceUtils() {
        preferences = PreferenceManager.getDefaultSharedPreferences(BaseApplication.getInstance());
        editor = preferences.edit();
    }

    public void savePayments() {
        long temp = 0;
        long totalAmount = preferences.getLong("TotalAmount", temp);
        if (totalAmount == 0) {
            editor.putLong("TotalAmount", 50000);
            editor.apply();
        }
    }

    public long getAvailCash() {
        long temp = 0;
        return preferences.getLong("TotalAmount", temp);
    }

    public void saveCash(long cash) {
        editor.putLong("TotalAmount", cash);
        editor.apply();
    }

    public void saveLogin(String email, String password) {
        editor.putString("Email", email);
        editor.putString("Password", password);
        editor.apply();
    }

    public boolean isLoggedIn() {
        String email = preferences.getString("Email", "");
        String password = preferences.getString("Password", "");
        if (email == null) {
            return false;
        }
        BaseApplication.getInstance().getUserManager().setUserEmail(email);
        BaseApplication.getInstance().getUserManager().setUserPassword(password);
        return true;
    }

    public void clearPreference() {
        editor.clear();
        editor.apply();
    }
}
