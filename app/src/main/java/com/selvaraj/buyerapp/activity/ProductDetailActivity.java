package com.selvaraj.buyerapp.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.selvaraj.buyerapp.R;
import com.selvaraj.buyerapp.base.BaseActivity;
import com.selvaraj.buyerapp.base.BaseApplication;
import com.selvaraj.buyerapp.model.Products;

import androidx.annotation.Nullable;

public class ProductDetailActivity extends BaseActivity {

    private TextView tvName, tvPrice, tvCount, tvAddress, tvDate,tvVendorName;
    private ImageButton ivMaps;
    private Products selectedProduct;
    private Button btnAddToCart,btnBuy;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        setHomeInSupportActionBar(true);
        selectedProduct = BaseApplication.getInstance().getUserManager().getSelectedProduct();
        initViews();
        populateViews();
        ivMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress("Getting vendor location.. Please wait!..");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(ProductDetailActivity.this,MapsActivity.class);
                        startActivity(intent);
                        hideProgress();
                    }
                },3000);
            }
        });
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetailActivity.this,CartActivity.class);
                startActivity(intent);
            }
        });
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProductDetailActivity.this,R.style.MyDialog);
                View view = getLayoutInflater().inflate(R.layout.alert_get_input,null);
                final EditText etQuantity = view.findViewById(R.id.et_product_quatity_get);
                builder.setView(view);
                builder.setTitle("Enter Quantity");
                builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        long price =(long) (Integer.parseInt(etQuantity.getText().toString())*Integer.parseInt(selectedProduct.getPrice()));
                        Intent intent = new Intent(ProductDetailActivity.this,TransactionActivity.class);
                        intent.putExtra("Price",price);
                        startActivity(intent);
                    }
                }).setNegativeButton("Cancel",null);
                builder.create();
                builder.show();
            }
        });
    }

    private void populateViews() {
        tvVendorName.setText(selectedProduct.getVendorName());
        tvName.setText(selectedProduct.getName());
        tvDate.setText(selectedProduct.getDate());
        tvPrice.setText(selectedProduct.getPrice());
        tvCount.setText(selectedProduct.getQuantity());
        tvAddress.setText(selectedProduct.getAddress());
    }

    private void initViews() {
        tvVendorName = findViewById(R.id.tv_vendor_name);
        tvName = findViewById(R.id.tv_product_name_detail);
        tvAddress = findViewById(R.id.tv_product_detail_address);
        tvCount = findViewById(R.id.tv_product_detail_quantity);
        tvPrice = findViewById(R.id.tv_product_quantity_price);
        tvDate = findViewById(R.id.tv_product_detail_doj);
        ivMaps = findViewById(R.id.ib_maps);
        btnAddToCart=findViewById(R.id.btn_add_to_cart);
        btnBuy = findViewById(R.id.btn_buy);
    }
}
