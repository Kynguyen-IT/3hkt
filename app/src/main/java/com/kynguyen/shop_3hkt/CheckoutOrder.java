package com.kynguyen.shop_3hkt;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kynguyen.shop_3hkt.Model.Cart;
import com.kynguyen.shop_3hkt.Model.Orders;
import com.kynguyen.shop_3hkt.Prevalent.Prevalent;
import com.kynguyen.shop_3hkt.ViewHolder.ShowProductOrderViewHolder;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class CheckoutOrder extends AppCompatActivity {
    private ImageView imageView;
    private TextView nameTV, addressTV, phoneTV, time_and_date_TV, itemOrderSummaryTV, totalPriceOrderTV, UserOrderTV, statusOrder;
    private FontAwesome close;
    private String addressUser, time, date, item, total, date_time;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private DatabaseReference refOrder, refCart, ref;
    private Button orderBtn, continueShoppingBtn;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_order);
        addressUser = getIntent().getStringExtra("addressUser");
        item = getIntent().getStringExtra("item");
        total = getIntent().getStringExtra("total");
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM/dd/yyyy");
        date = currentDate.format(calendar.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        time = currentTime.format(calendar.getTime());


        mapping();
        showData();
        refOrder = FirebaseDatabase.getInstance().getReference();
        ref = FirebaseDatabase.getInstance().getReference().child("Cart List").child("User")
                .child(Prevalent.currentOnLineUsers.getUid())
                .child("Products");


        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String order = getInformationAndDataOrder();
                showDiaLogOrder(order);
            }
        });
    }

    private String getInformationAndDataOrder() {
        final String idOrder = refOrder.child("Orders").push().getKey();
        final HashMap<String, Object> cartMap = new HashMap<>();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    Cart cart = snap.getValue(Cart.class);
                    Log.d("AAA", cart.getName() + " - " + cart.getPid());
                    String key = refOrder.child("Orders").child(idOrder)
                            .child("products").push().getKey();
                    cartMap.put(key, cart);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        HashMap<String, Object> data = new HashMap<>();
        data.put("orderId", idOrder);
        data.put("name", Prevalent.currentOnLineUsers.getDisplayName());
        data.put("phone", Prevalent.currentOnLineUsers.getPhone());
        data.put("image", Prevalent.currentOnLineUsers.getPhotoUrl());
        data.put("status", "pending");
        data.put("dateTime", time + " " + date);
        data.put("address", addressUser);
        data.put("quantity", item);
        data.put("total", total);
        data.put("uid", Prevalent.currentOnLineUsers.getUid());
        refOrder.child("Orders").child(idOrder).setValue(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        refOrder.child("Orders").child(idOrder).child("products").setValue(cartMap);
                    }
                });
        refOrder.child("Cart List").child("User")
                .child(Prevalent.currentOnLineUsers.getUid()).removeValue();
        return idOrder;
    }

    private void showDiaLogOrder(String orderId) {
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Window window = dialog.getWindow();
        window.getAttributes().windowAnimations = R.style.DialogAnimation;

        // mapping dia log

        UserOrderTV = dialog.findViewById(R.id.your_order);
        statusOrder = dialog.findViewById(R.id.status_order);
        continueShoppingBtn = dialog.findViewById(R.id.continue_shopping);

        if (Prevalent.currentOnLineUsers != null) {
//            FirebaseDatabase.getInstance().getReference().child("Orders")
//                    .child(Prevalent.currentOnLineUsers.getUid())
//                    .child(orderId)
//                    .addValueEventListener(new ValueEventListener() {
            FirebaseDatabase.getInstance().getReference().child("Orders")
                    .child(orderId)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Orders order = dataSnapshot.getValue(Orders.class);
                            UserOrderTV.setText("Your order id is " + order.getOrderId());
                            statusOrder.setText("Order Status:" + order.getStatus());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }

//        UserOrderTV.setText("Your order id is " + Prevalent.currentOnLineUsers.uid);
////        statusOrder.setText("Order Status:" + "Pending");

        continueShoppingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(CheckoutOrder.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // set Text layout
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setLayout(width, height);
        dialog.setCancelable(true);
        dialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        refCart = FirebaseDatabase.getInstance().getReference().child("Cart List").child("User")
                .child(Prevalent.currentOnLineUsers.getUid()).child("Products");
        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(refCart, Cart.class).build();

        FirebaseRecyclerAdapter<Cart, ShowProductOrderViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, ShowProductOrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ShowProductOrderViewHolder holder, int position, @NonNull Cart model) {
                holder.quantityAndNameTV.setText(model.getQuantity() + " * " + model.getName());
                holder.priceTV.setText(model.getPrice() + "đ");
            }

            @NonNull
            @Override
            public ShowProductOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_product_order, parent, false);
                ShowProductOrderViewHolder holder = new ShowProductOrderViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void showData() {
        if (Prevalent.currentOnLineUsers != null) {
            Picasso.get().load(Prevalent.currentOnLineUsers.photoUrl).fit().into(imageView);
            nameTV.setText(Prevalent.currentOnLineUsers.displayName);
            phoneTV.setText("-  " + Prevalent.currentOnLineUsers.phone);
            addressTV.setText(addressUser);
            time_and_date_TV.setText(time + " - Today " + date);

            itemOrderSummaryTV.setText("You have " + item + " items in your shoping cart.");
            totalPriceOrderTV.setText("Total: " + total);
        }
    }

    private void mapping() {
        recyclerView = findViewById(R.id.list_product_order_);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        imageView = findViewById(R.id.image_user);
        nameTV = findViewById(R.id.name_user);
        addressTV = findViewById(R.id.address_user);
        phoneTV = findViewById(R.id.phone_user);
        close = findViewById(R.id.close_checkout_order);
        time_and_date_TV = findViewById(R.id.time_date_order);
        itemOrderSummaryTV = findViewById(R.id.item_order_summary);
        totalPriceOrderTV = findViewById(R.id.total_price_order);
        orderBtn = findViewById(R.id.order_btn);

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_order);
    }
}