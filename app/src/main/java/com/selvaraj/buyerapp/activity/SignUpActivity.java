package com.selvaraj.buyerapp.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.selvaraj.buyerapp.BuildConfig;
import com.selvaraj.buyerapp.Interface.LoginListener;
import com.selvaraj.buyerapp.R;
import com.selvaraj.buyerapp.base.BaseActivity;
import com.selvaraj.buyerapp.base.BaseApplication;
import com.selvaraj.buyerapp.model.SaveUser;
import com.selvaraj.buyerapp.utils.LocationUtils;
import com.selvaraj.buyerapp.utils.Utilities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class SignUpActivity extends BaseActivity implements LoginListener, LocationUtils.LocationCallBack {
    private static final String PACKAGE = "package:";
    private String lastKnownLocation;
    private Handler handler;
    private ProgressBar progressBar;
    private TextInputEditText etName, etPassword, etEmail, etPhone;
    private String name, email, password, phone;
    private ConstraintLayout constraintLayoutSignUp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHomeInSupportActionBar(true);
        setContentView(R.layout.activity_sign_up);
        etEmail = findViewById(R.id.et_sign_up_email);
        etPassword = findViewById(R.id.et_sign_up_password);
        etPhone = findViewById(R.id.et_sign_up_phone);
        etName = findViewById(R.id.et_sign_up_name);
        Button btnSignUp = findViewById(R.id.btn_sign_up);
        progressBar=findViewById(R.id.progressBar_sign_up);
        progressBar.setVisibility(View.INVISIBLE);
        if (isPermissionGranted()) {
            handler = new Handler();
            FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            LocationUtils locationUtils = new LocationUtils(fusedLocationProviderClient, this, this);
            locationUtils.getLastLocation();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermission();
        }
        constraintLayoutSignUp = findViewById(R.id.cl_sign_up);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getInputs()) {
                    if (!Utilities.checkNetworkConnection()) {
                        showNoNetworkToast();
                        return;
                    }
                    progressBar.setVisibility(View.VISIBLE);
                    Date date = new Date();
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss", Locale.getDefault());
                    String strDate = dateFormat.format(date);
                    Location location = BaseApplication.getInstance().getUserManager().getVendorLocation();
                    double lat,lng;
                    lat=location.getLatitude();
                    lng=location.getLongitude();
                    final SaveUser saveUser = new SaveUser(email, name, phone, BaseApplication.getInstance().getUserManager().getLastKnownLocation(), strDate,lat,lng);
                    BaseApplication.getInstance().getFireBaseUtils().createAccount(saveUser, SignUpActivity.this, password, SignUpActivity.this);
                }
            }
        });
    }

    private boolean isPermissionGranted() {
        return (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                || (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }

    private boolean getInputs() {
        email = etEmail.getText().toString();
        password = etPassword.getText().toString();
        name = etName.getText().toString();
        phone = etPhone.getText().toString();
        if (email.isEmpty()) {
            etEmail.setError("Invalid email");
            return false;
        }
        if (password.isEmpty()) {
            etPassword.setError("Password too weak");
            return false;
        }
        if (name.isEmpty()) {
            etName.setError("Invalid input");
            return false;
        }
        if (phone.isEmpty()) {
            etPhone.setError("Enter valid phone");
            return false;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestPermission() {
        if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED)
                || (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED)) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        SignUpActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void getLocation() {
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        LocationUtils locationUtils = new LocationUtils(fusedLocationProviderClient, this, this);
        locationUtils.getLastLocation();
    }

    /**
     * Once permission denied the show toast message corresponding to the permission.
     */
    @OnPermissionDenied({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void onPermissionDenied() {
        Toast.makeText(this, R.string.permission_camera_denied, Toast.LENGTH_SHORT).show();
    }

    /**
     * Method displays the need of permission and create request if allow button is clicked.
     *
     * @param request contains list of requests.
     */
    @OnShowRationale({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void showRationaleForCameraAndStorage(PermissionRequest request) {
        showRationaleDialog(R.string.permission_camera_rationale, request);
    }

    /**
     * Navigate to settings when user checks never ask again.
     */
    @OnNeverAskAgain({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void onPermissionNeverAskAgain() {
        Snackbar.make(constraintLayoutSignUp, R.string.snack_bar_title, Snackbar.LENGTH_LONG).
                setAction(R.string.snack_bar_btn, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse(PACKAGE + BuildConfig.APPLICATION_ID));
                        startActivity(intent);
                    }
                }).show();
    }

    /**
     * Method to create alert dialog which contains the need of permission
     *
     * @param messageResId represents particular string resource to show message.
     * @param request      used to proceed or cancel based on user selection.
     */
    private void showRationaleDialog(@StringRes int messageResId, final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setPositiveButton(R.string.button_allow, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@androidx.annotation.NonNull DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton(R.string.button_deny, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@androidx.annotation.NonNull DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .setCancelable(false)
                .setMessage(messageResId)
                .show();
    }

    @Override
    public void onLocationReceived(final String location) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        lastKnownLocation = location;
                        BaseApplication.getInstance().getUserManager().setLastKnownLocation(lastKnownLocation);
                    }
                });
            }
        }).start();
    }

    @Override
    public void onLoginSuccess(boolean result) {
        if (result) {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(), "Account created Successfully!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            Snackbar.make(constraintLayoutSignUp, "Authentication failed", Snackbar.LENGTH_LONG).show();
        }
    }
}
