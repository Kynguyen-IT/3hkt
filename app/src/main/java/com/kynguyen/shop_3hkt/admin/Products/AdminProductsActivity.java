package com.kynguyen.shop_3hkt.admin.Products;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kynguyen.shop_3hkt.FontAwesome;
import com.kynguyen.shop_3hkt.R;
import com.kynguyen.shop_3hkt.admin.AdminActivity;

public class AdminProductsActivity extends AppCompatActivity {
  private FontAwesome close;
  private FloatingActionButton add;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_admin_products);
    mapping();
    close.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(AdminProductsActivity.this , AdminActivity.class));
      }
    });
    add.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(AdminProductsActivity.this, AddProduct.class));
      }
    });
  }

  private void mapping() {
    close = findViewById(R.id.close_admin_product);
    add = findViewById(R.id.add_product_floating);
  }
}
