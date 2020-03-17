package com.selvaraj.buyerapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.selvaraj.buyerapp.Interface.LoginListener;
import com.selvaraj.buyerapp.R;
import com.selvaraj.buyerapp.base.BaseActivity;
import com.selvaraj.buyerapp.base.BaseApplication;
import com.selvaraj.buyerapp.utils.Utilities;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public class LoginActivity extends BaseActivity implements LoginListener {
    private TextInputEditText etEmail, etPassword;
    private String email, password;
    private Button btnLogin;
    private TextView tvSignUp;
    private ProgressBar progressBar;
    private ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etEmail = findViewById(R.id.et_login_email);
        etPassword = findViewById(R.id.et_login_password);
        btnLogin = findViewById(R.id.btn_login);
        tvSignUp = findViewById(R.id.tv_sign_up);
        constraintLayout = findViewById(R.id.cl_login);
        progressBar = findViewById(R.id.progressBar_login);
        progressBar.setVisibility(View.INVISIBLE);
        initListeners();
    }

    private void initListeners() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(LoginActivity.this);
                if (getInputs()) {
                    checkLogin();
                }
            }
        });
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void checkLogin() {
        progressBar.setVisibility(View.VISIBLE);
        if (!Utilities.checkNetworkConnection()) {
            showNoNetworkToast();
            progressBar.setVisibility(View.INVISIBLE);
            return;
        }
        BaseApplication.getInstance().getFireBaseUtils().checkLogin(this, email, password, this);
    }

    private boolean getInputs() {
        email = etEmail.getText().toString();
        password = etPassword.getText().toString();
        if (email.isEmpty()) {
            etEmail.setError("Enter valid email");
            return false;
        }
        if (password.isEmpty()) {
            etPassword.setError("Enter valid password");
            return false;
        }
        return true;
    }

    @Override
    public void onLoginSuccess(boolean result) {

        if (result) {
            BaseApplication.getInstance().getPreferenceManager().saveLogin(email, password);
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            Snackbar.make(constraintLayout, "Invalid credentials!", Snackbar.LENGTH_LONG).show();
        }
        progressBar.setVisibility(View.INVISIBLE);

    }
}
