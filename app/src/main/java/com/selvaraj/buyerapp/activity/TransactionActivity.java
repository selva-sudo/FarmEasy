package com.selvaraj.buyerapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.selvaraj.buyerapp.R;

public class TransactionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        TextView tvTotal = findViewById(R.id.tv_transact_amount);
        long price = getIntent().getLongExtra("Price", 0);
        TextView tvDue = findViewById(R.id.tv_due);
        Button btnPay = findViewById(R.id.calculate);
        tvDue.setText(String.valueOf(price));
        tvTotal.setText(String.valueOf(price));
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransactionActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
