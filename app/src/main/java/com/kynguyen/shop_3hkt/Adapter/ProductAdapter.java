package com.kynguyen.shop_3hkt.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.kynguyen.shop_3hkt.Profile_product;
import com.kynguyen.shop_3hkt.R;
import com.kynguyen.shop_3hkt.ViewHolder.ShowProductsViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ShowProductsViewHolder> {
    private LayoutInflater mLayoutInflater;
    private ArrayList<Products> mList;
    private String addressUser;
    Context mContext;

    DatabaseReference refSave;

    public ProductAdapter(Context context, ArrayList<Products> list) {
        mLayoutInflater = LayoutInflater.from(context);
        mList = list;
        Intent intent = ((Activity) context).getIntent();
        addressUser = intent.getStringExtra("addressUser");
    }

    @NonNull
    @Override
    public ShowProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View viewProduct = mLayoutInflater.inflate(R.layout.list_products_holder_view, parent, false);
        return new ShowProductsViewHolder(viewProduct);
    }

    @Override
    public void onBindViewHolder(@NonNull final ShowProductsViewHolder holder, int position) {
        final Products model = mList.get(position);
        if (model != null) {
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

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, Profile_product.class);
                    intent.putExtra("pid", model.pid);
                    intent.putExtra("addressUser", addressUser);
                    mContext.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
