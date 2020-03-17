package com.selvaraj.buyerapp.base;

import android.app.Application;

import com.selvaraj.buyerapp.utils.FireBaseUtils;
import com.selvaraj.buyerapp.utils.PreferenceUtils;

public class BaseApplication extends Application {
    private static BaseApplication instance;
    private UserManager userManager;
    private FireBaseUtils fireBaseUtils;
    private PreferenceUtils preferenceManager;

    @Override
    public void onCreate() {
        super.onCreate();
        userManager = new UserManager();
        instance = this;
        fireBaseUtils = new FireBaseUtils();
        preferenceManager = new PreferenceUtils();
    }

    public static BaseApplication getInstance() {
        return instance;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public FireBaseUtils getFireBaseUtils() {
        return new FireBaseUtils();
    }

    public PreferenceUtils getPreferenceManager() {
        return preferenceManager;
    }
}
