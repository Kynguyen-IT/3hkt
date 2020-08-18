package com.kynguyen.shop_3hkt.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.kynguyen.shop_3hkt.HomeActivity;
import com.kynguyen.shop_3hkt.R;
import com.kynguyen.shop_3hkt.admin.Orders.AdminOrderActivity;
import com.kynguyen.shop_3hkt.admin.Products.AdminProductsActivity;
import com.kynguyen.shop_3hkt.admin.category.AdminCategoryActivity;

public class AdminActivity extends AppCompatActivity {
    private TextView back_shopTE;
    private RelativeLayout categoryActivity, ordersActivity, productsActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        mapping();

        back_shopTE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        categoryActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminActivity.this, AdminCategoryActivity.class));
            }
        });

        productsActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminActivity.this, AdminProductsActivity.class));
            }
        });

        ordersActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminActivity.this, AdminOrderActivity.class));
            }
        });

    }

    private void mapping() {
        back_shopTE = findViewById(R.id.back_shop);
        categoryActivity = findViewById(R.id.category_container);
        ordersActivity = findViewById(R.id.Order_container);
        productsActivity = findViewById(R.id.product_container);
    }
}
