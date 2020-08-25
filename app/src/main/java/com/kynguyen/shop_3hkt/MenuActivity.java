package com.kynguyen.shop_3hkt;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kynguyen.shop_3hkt.Model.Products;
import com.kynguyen.shop_3hkt.Prevalent.Prevalent;
import com.kynguyen.shop_3hkt.ViewHolder.ShowProductsViewHolder;
import com.squareup.picasso.Picasso;

public class MenuActivity extends AppCompatActivity {
    private FontAwesome close;
    private TextView title;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private String idcate, nameCate, addressUser;
    private DatabaseReference refProduct, refSave;
    private boolean status;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        idcate = getIntent().getStringExtra("idCate");
        nameCate = getIntent().getStringExtra("nameCate");
        mapping();
        title.setText("Menu " + nameCate);
        addressUser = getIntent().getStringExtra("addressUser");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Query query = FirebaseDatabase.getInstance().getReference().child("products").orderByChild("idCategory").equalTo(idcate);
        FirebaseRecyclerOptions<Products> optionsProduct = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(query, Products.class).build();

        FirebaseRecyclerAdapter<Products, ShowProductsViewHolder> adapterProduct = new FirebaseRecyclerAdapter<Products, ShowProductsViewHolder>(optionsProduct) {
            @NonNull
            @Override
            public ShowProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View viewProduct = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_products_holder_view, parent, false);
                ShowProductsViewHolder holder = new ShowProductsViewHolder(viewProduct);
                return holder;
            }

            @Override
            protected void onBindViewHolder(@NonNull final ShowProductsViewHolder holder, int position, @NonNull final Products model) {
                holder.name.setText(model.getName());
                holder.address.setText(model.getAddress());
                Picasso.get().load(model.getImage()).fit().into(holder.imageProduct);
                holder.description.setText(model.getDescription());

                refSave = FirebaseDatabase.getInstance().getReference();

                if (Prevalent.currentOnLineUsers != null) {
                    refSave.child("saves").child(model.pid).child(Prevalent.currentOnLineUsers.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() == null) {
                                status = false;
                                holder.heart_product.setImageResource(R.drawable.ic_save);
                            } else {
                                status = true;
                                holder.heart_product.setImageResource(R.drawable.ic_saved);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MenuActivity.this, Profile_product.class);
                        intent.putExtra("pid", model.pid);
                        intent.putExtra("addressUser", addressUser);
                        startActivity(intent);
                    }
                });
                progressBar.setVisibility(View.INVISIBLE);
            }
        };
        recyclerView.setAdapter(adapterProduct);
        adapterProduct.startListening();
//        refProduct = FirebaseDatabase.getInstance().getReference().child("products");
//        FirebaseRecyclerOptions<Products> optionsProduct = new FirebaseRecyclerOptions.Builder<Products>()
//                .setQuery(refProduct, Products.class).build();
//
//        FirebaseRecyclerAdapter<Products, ShowProductsViewHolder> adapterProduct = new FirebaseRecyclerAdapter<Products, ShowProductsViewHolder>(optionsProduct) {
//            @NonNull
//            @Override
//            public ShowProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View viewProduct = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_products_holder_view, parent, false);
//                ShowProductsViewHolder holder = new ShowProductsViewHolder(viewProduct);
//                return holder;
//            }
//
//            @Override
//            protected void onBindViewHolder(@NonNull final ShowProductsViewHolder holder, int position, @NonNull final Products model) {
//                refProduct.child(model.pid).child("idCategory").addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        if (dataSnapshot.exists()) {
//                            if (dataSnapshot.getValue().equals(idcate)) {
//                                holder.name.setText(model.getName());
//                                holder.address.setText(model.getAddress());
//                                Picasso.get().load(model.getImage()).fit().into(holder.imageProduct);
//                                holder.description.setText(model.getDescription());
//
//                                refSave = FirebaseDatabase.getInstance().getReference();
//
//                                if (Prevalent.currentOnLineUsers != null) {
//                                    refSave.child("saves").child(model.pid).child(Prevalent.currentOnLineUsers.getUid()).addValueEventListener(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                            if (dataSnapshot.getValue() == null) {
//                                                status = false;
//                                                holder.heart_product.setImageResource(R.drawable.ic_save);
//                                            } else {
//                                                status = true;
//                                                holder.heart_product.setImageResource(R.drawable.ic_saved);
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                        }
//                                    });
//                                }
//                                holder.itemView.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        Intent intent = new Intent(MenuActivity.this, Profile_product.class);
//                                        intent.putExtra("pid", model.pid);
//                                        intent.putExtra("addressUser", addressUser);
//                                        startActivity(intent);
//                                    }
//                                });
//                            }
//                        }
//                        progressBar.setVisibility(View.INVISIBLE);
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//            }
//        };
//        recyclerView.setAdapter(adapterProduct);
//        adapterProduct.startListening();
    }

    private void mapping() {
        recyclerView = findViewById(R.id.menu_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        progressBar = findViewById(R.id.loading_menu_product);
        close = findViewById(R.id.close_menu);
        title = findViewById(R.id.title_menu);
    }

}