package com.kynguyen.shop_3hkt.admin.Orders;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kynguyen.shop_3hkt.FontAwesome;
import com.kynguyen.shop_3hkt.Model.Orders;
import com.kynguyen.shop_3hkt.Prevalent.Prevalent;
import com.kynguyen.shop_3hkt.R;
import com.kynguyen.shop_3hkt.ViewHolder.ShowOrderAdminViewHolder;
import com.kynguyen.shop_3hkt.admin.AdminActivity;
import com.squareup.picasso.Picasso;

public class AdminOrderActivity extends AppCompatActivity {
    private FontAwesome close;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference refOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order);
        mapping();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminOrderActivity.this, AdminActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        refOrder = FirebaseDatabase.getInstance().getReference().child("Orders");

        FirebaseRecyclerOptions<Orders> options = new FirebaseRecyclerOptions.Builder<Orders>()
                .setQuery(refOrder, Orders.class).build();


        FirebaseRecyclerAdapter<Orders, ShowOrderAdminViewHolder> adapter = new FirebaseRecyclerAdapter<Orders, ShowOrderAdminViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ShowOrderAdminViewHolder holder, int position, @NonNull final Orders model) {
                holder.nameTV.setText(model.getName());
                holder.totalTV.setText("Total: " + model.getTotal());
                String quantity = "(" + model.getQuantity() + " item)";
                if (Integer.parseInt(model.getQuantity()) > 1) {
                    quantity = "(" + model.getQuantity() + " items)";
                }
                holder.itemProductTV.setText(quantity);
                holder.addressTV.setText(model.getAddress());
                holder.phoneTV.setText(model.getPhone());
                holder.date_timeTV.setText("Date: " + model.getDateTime());
                holder.statusTV.setText("Status: " + model.status);
                Picasso.get().load(model.getImage()).fit().into(holder.image);
                holder.admin_order_ship.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        refOrder.child(model.getOrderId()).child("status").setValue("shipping");
                        Toast.makeText(AdminOrderActivity.this, "Order with id: " + model.getOrderId() + " status change to Shipping", Toast.LENGTH_SHORT).show();
                    }
                });
                holder.admin_order_finish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        refOrder.child(model.getOrderId()).child("status").setValue("finish");
                      Toast.makeText(AdminOrderActivity.this, "Order with id: " + model.getOrderId() + " status change to Finish", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @NonNull
            @Override
            public ShowOrderAdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_order_admin, parent, false);
                ShowOrderAdminViewHolder holder = new ShowOrderAdminViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void mapping() {
        close = findViewById(R.id.close_admin_order);
        recyclerView = findViewById(R.id.order_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }
}