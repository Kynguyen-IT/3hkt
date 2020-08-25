package com.kynguyen.shop_3hkt.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kynguyen.shop_3hkt.Model.Products;
import com.kynguyen.shop_3hkt.Prevalent.Prevalent;
import com.kynguyen.shop_3hkt.R;
import com.kynguyen.shop_3hkt.ViewHolder.ShowProductsViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ShowProductsViewHolder> {
    private LayoutInflater mLayoutInflater;
    private ArrayList<Products> mList;

    DatabaseReference refSave;

    public ProductAdapter(Context context, ArrayList<Products> list) {
        mLayoutInflater = LayoutInflater.from(context);
        mList = list;
    }

    @NonNull
    @Override
    public ShowProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewProduct = mLayoutInflater.inflate(R.layout.list_products_holder_view, parent, false);
        return new ShowProductsViewHolder(viewProduct);
    }

    @Override
    public void onBindViewHolder(@NonNull final ShowProductsViewHolder holder, int position) {
        if (mList != null) {
            Log.d("AAA", "LOAD ADAPTER");
            Products model = mList.get(position);
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
                            holder.heart_product.setImageResource(R.drawable.ic_save);
                        } else {
                            holder.heart_product.setImageResource(R.drawable.ic_saved);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
