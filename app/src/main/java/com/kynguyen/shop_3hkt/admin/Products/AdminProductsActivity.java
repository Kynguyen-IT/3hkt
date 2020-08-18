package com.kynguyen.shop_3hkt.admin.Products;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kynguyen.shop_3hkt.FontAwesome;
import com.kynguyen.shop_3hkt.Model.Products;
import com.kynguyen.shop_3hkt.R;
import com.kynguyen.shop_3hkt.ViewHolder.ShowProductsViewHolder;
import com.kynguyen.shop_3hkt.admin.AdminActivity;
import com.squareup.picasso.Picasso;

public class AdminProductsActivity extends AppCompatActivity {
  private FontAwesome close;
  private FloatingActionButton add;
  private RecyclerView recyclerView;
  private RecyclerView.LayoutManager layoutManager;
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

  @Override
  protected void onStart() {
    super.onStart();
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("products");
    FirebaseRecyclerOptions<Products> optionsProduct = new FirebaseRecyclerOptions.Builder<Products>()
        .setQuery(ref, Products.class).build();

    FirebaseRecyclerAdapter<Products, ShowProductsViewHolder> adapterProduct = new FirebaseRecyclerAdapter<Products, ShowProductsViewHolder>(optionsProduct) {
      @NonNull
      @Override
      public ShowProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewProduct = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_products_holder_view,parent,false);
        ShowProductsViewHolder holder = new ShowProductsViewHolder(viewProduct);
        return holder;
      }

      @Override
      protected void onBindViewHolder(@NonNull final ShowProductsViewHolder holder, int position, @NonNull final Products model) {
        holder.name.setText(model.getName());
        holder.address.setText(model.getAddress());
        Picasso.get().load(model.getImage()).fit().into(holder.imageProduct);
        holder.description.setText(model.getDescription());
        holder.relativeLayout.setVisibility(View.INVISIBLE);
      }
    };
    recyclerView.setAdapter(adapterProduct);
    adapterProduct.startListening();
  }

  private void mapping() {
    recyclerView= findViewById(R.id.product_list);
    recyclerView.setHasFixedSize(true);
    layoutManager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(layoutManager);

    close = findViewById(R.id.close_admin_product);
    add = findViewById(R.id.add_product_floating);
  }
}
