package com.selvaraj.buyerapp.base;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import com.selvaraj.buyerapp.R;
import com.selvaraj.buyerapp.activity.LoginActivity;

public class UserActivity extends BaseActivity {

    public void showLogoutAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialog);
        builder.setTitle("Logout?");
        builder.setTitle("Are you sure to perform logout?!");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(UserActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.create();
        builder.show();

    }
}
