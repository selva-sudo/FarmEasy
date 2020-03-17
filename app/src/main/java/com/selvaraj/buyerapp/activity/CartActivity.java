package com.selvaraj.buyerapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;
import com.selvaraj.buyerapp.R;
import com.selvaraj.buyerapp.base.BaseActivity;
import com.selvaraj.buyerapp.base.BaseApplication;
import com.selvaraj.buyerapp.model.Cart;
import com.selvaraj.buyerapp.model.Products;
import com.selvaraj.buyerapp.model.UserMessage;
import com.selvaraj.buyerapp.utils.FireBaseUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.Nullable;

public class CartActivity extends BaseActivity {
    private TextView tvName,tvAvailPrice,tvPricePerItem;
    private Button btnAdd;
    private EditText etQuantity;
    private Products selectedProduct;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        initViews();
        final long cash = BaseApplication.getInstance().getPreferenceManager().getAvailCash();
        tvAvailPrice.setText(getString(R.string.rs,String.valueOf(cash)));
        selectedProduct = BaseApplication.getInstance().getUserManager().getSelectedProduct();
        tvPricePerItem.setText(selectedProduct.getPrice());
        tvName.setText(selectedProduct.getName());
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(etQuantity.getText().toString());
                long total = (long)(quantity*Integer.parseInt(selectedProduct.getPrice()));
                long temp;
                if(total>cash){
                    Toast.makeText(CartActivity.this,"Cash unavailable",Toast.LENGTH_LONG).show();
                } else {
                    Date date = new Date();
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss", Locale.getDefault());
                    String strDate = dateFormat.format(date);
                    String name=BaseApplication.getInstance().getUserManager().getUserName();
                    Cart cart = new Cart(name,selectedProduct.getName(),etQuantity.getText().toString(),String.valueOf(total),strDate);
                    String id= new FireBaseUtils().userID;
                    FirebaseDatabase.getInstance().getReference("Users" + "/"+id +"/"+ "Cart").push().setValue(cart);
                    temp = cash-total;
                    BaseApplication.getInstance().getPreferenceManager().saveCash(temp);
                    Toast.makeText(CartActivity.this,"Product added on cart!",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(CartActivity.this,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }

            }
        });
    }

    private void initViews() {
        tvName=findViewById(R.id.tv_product_name_cart);
        tvAvailPrice = findViewById(R.id.tv_avail_cash);
        tvPricePerItem = findViewById(R.id.tv_cart_price);
        btnAdd = findViewById(R.id.btn_add_cart);
        etQuantity=findViewById(R.id.et_product_quatity);
    }
}
