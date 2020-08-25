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
import com.kynguyen.shop_3hkt.Adapter.ProductAdapter;
import com.kynguyen.shop_3hkt.Model.Products;
import com.kynguyen.shop_3hkt.Prevalent.Prevalent;
import com.kynguyen.shop_3hkt.ViewHolder.ShowProductsViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class SearchActivity extends AppCompatActivity {
    private RecyclerView recyclerViewProduct;
    private SearchView searchView;
    private boolean status;
    DatabaseReference productsRef, refSave;
    FontAwesome backBtn;
    private ProductAdapter adapterProduct;
    private ArrayList<Products> listProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        productsRef = FirebaseDatabase.getInstance().getReference().child("products");
        mapping();
        listProduct = getAllProducts();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ArrayList<Products> listFiltered = new ArrayList<>();
                String search = searchView.getQuery().toString();
                listFiltered = searchProducts(search, listProduct);
//                Log.d("AAA", search);

                adapterProduct = new ProductAdapter(getApplicationContext(), listFiltered);
                recyclerViewProduct.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerViewProduct.setAdapter(adapterProduct);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String search = searchView.getQuery().toString();
                Log.d("AAA", search);
                Query query = productsRef.orderByChild("name").startAt(search).endAt(search + "\uf8ff");
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

    private ArrayList<Products> getAllProducts() {
        final ArrayList<Products> list = new ArrayList<>();
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Products product = ds.getValue(Products.class);
//                    Log.d("BBB", product.getName() + " loaded");
                    list.add(product);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        productsRef.addValueEventListener(eventListener);
        return list;
    }

    private ArrayList<Products> searchProducts(String searchQuery, ArrayList<Products> list) {
        ArrayList<Products> output = new ArrayList<>();
        for (int index = 0; index < list.size(); index++) {
            Products product = list.get(index);
            String name = product.getName();
            Log.d("BBB", name);
            if (name.contains(searchQuery)) {
                Log.d("CCC", name + " filtered");
                output.add(product);
            }
        }
        return output;
    }

    private void mapping() {
        searchView = findViewById(R.id.searchBar);
        backBtn = findViewById(R.id.close_search);
        // list product
        recyclerViewProduct = findViewById(R.id.list_search_item);
        recyclerViewProduct.setHasFixedSize(true);
    }
}