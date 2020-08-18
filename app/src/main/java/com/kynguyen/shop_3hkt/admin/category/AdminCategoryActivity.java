package com.kynguyen.shop_3hkt.admin.category;

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
import com.kynguyen.shop_3hkt.Model.Categories;
import com.kynguyen.shop_3hkt.R;
import com.kynguyen.shop_3hkt.ViewHolder.adminCategoryViewHolder;
import com.kynguyen.shop_3hkt.admin.AdminActivity;

public class AdminCategoryActivity extends AppCompatActivity {
  private FontAwesome close;
  private FloatingActionButton add;
  private RecyclerView recyclerView;
  private RecyclerView.LayoutManager layoutManager;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_admin_category);
    mapping();
    close.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(AdminCategoryActivity.this, AdminActivity.class));
      }
    });

    add.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(AdminCategoryActivity.this, Addcategories.class));
      }
    });

  }

  @Override
  protected void onStart() {
    super.onStart();

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("categories");
    FirebaseRecyclerOptions<Categories> options = new FirebaseRecyclerOptions.Builder<Categories>()
        .setQuery(ref, Categories.class).build();

    FirebaseRecyclerAdapter<Categories, adminCategoryViewHolder> adapter = new FirebaseRecyclerAdapter<Categories, adminCategoryViewHolder>(options){
      @NonNull
      @Override
      public adminCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_item_category,parent,false);
        adminCategoryViewHolder holder = new adminCategoryViewHolder(view);
        return holder;
      }

      @Override
      protected void onBindViewHolder(@NonNull adminCategoryViewHolder holder, int position, @NonNull Categories model) {
        holder.name_category.setText(model.getName());
      }
    };
    recyclerView.setAdapter(adapter);
    adapter.startListening();
  }

  private void mapping() {
    recyclerView= findViewById(R.id.category_list);
    recyclerView.setHasFixedSize(true);
    layoutManager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(layoutManager);
    close = findViewById(R.id.close_admin_category);
    add = findViewById(R.id.add_categories_floating);
  }
}
