package com.kynguyen.shop_3hkt;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kynguyen.shop_3hkt.Model.Products;
import com.kynguyen.shop_3hkt.Prevalent.Prevalent;
import com.kynguyen.shop_3hkt.ViewHolder.ShowProductsViewHolder;
import com.squareup.picasso.Picasso;


public class SearchActivity extends AppCompatActivity {
    private RecyclerView recyclerViewProduct;

    private SearchView searchView;
    private boolean status;
    DatabaseReference productsRef, refSave;
    FontAwesome backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        productsRef = FirebaseDatabase.getInstance().getReference().child("products");
        mapping();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String search = searchView.getQuery().toString();
                FirebaseRecyclerOptions<Products> optionsProduct = new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(productsRef.orderByChild("name").startAt(search), Products.class).build();

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
                    }
                };
                recyclerViewProduct.setAdapter(adapterProduct);
                adapterProduct.startListening();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String search = searchView.getQuery().toString();
                Log.d("AAA", search);
                FirebaseRecyclerOptions<Products> optionsProduct = new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(productsRef.orderByChild("name").startAt(search), Products.class).build();

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
                                Intent intent = new Intent(SearchActivity.this, Profile_product.class);
                                intent.putExtra("pid", model.pid);
//                                intent.putExtra("addressUser", addressUser);
                                startActivity(intent);
                            }
                        });
                    }
                };
                recyclerViewProduct.setAdapter(adapterProduct);
                adapterProduct.startListening();
                return false;
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void mapping() {

        searchView = findViewById(R.id.searchBar);
        backBtn = findViewById(R.id.close_search);
        // list product
        recyclerViewProduct = findViewById(R.id.list_search_item);
        recyclerViewProduct.setHasFixedSize(true);
    }
}


//    private void search(String s) {
//        Query query = productsRef.orderByChild("name").startAt(s).endAt(s + "\uf8ff");
//
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.hasChildren()) {
//                    listProducts.clear();
//                    for (DataSnapshot dss : dataSnapshot.getChildren()) {
//                        final Products product = dss.getValue(Products.class);
//                        listProducts.add(product);
//                    }
//
////                    recyclerViewProduct.setAdapter(adapterProduct);
////                    adapterProduct.startListening();
////                    adapterProduct.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }