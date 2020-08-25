package com.kynguyen.shop_3hkt.PapreAdapter.MyOrders;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.kynguyen.shop_3hkt.FontAwesome;
import com.kynguyen.shop_3hkt.Model.Orders;
import com.kynguyen.shop_3hkt.Model.Products;
import com.kynguyen.shop_3hkt.Prevalent.Prevalent;
import com.kynguyen.shop_3hkt.R;
import com.kynguyen.shop_3hkt.ViewHolder.ShowCartViewHolder;
import com.kynguyen.shop_3hkt.ViewHolder.ShowOrderAdminViewHolder;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

public class ongoingFragment extends Fragment {
    private View ongoingView;
    private RelativeLayout relativeLayout;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference refOrder;
    private Dialog dialog;
    private TextView title_dialog;
    private FontAwesome close;
    private DatabaseReference refProductOngoing;
    private RecyclerView recyclerViewDialog;
    private RecyclerView.LayoutManager layoutManagerDialog;
    private String orderId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ongoingView = inflater.inflate(R.layout.fragment_ongoing, container, false);
        Paper.init(ongoingView.getContext());
        mapping();
        return ongoingView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Prevalent.currentOnLineUsers != null) {
            refOrder = FirebaseDatabase.getInstance().getReference().child("Orders");
            Query query = refOrder.orderByChild("uid").equalTo(Prevalent.currentOnLineUsers.getUid());

            final FirebaseRecyclerOptions<Orders> options = new FirebaseRecyclerOptions.Builder<Orders>()
                    .setQuery(query, Orders.class).build();

            FirebaseRecyclerAdapter<Orders, ShowOrderAdminViewHolder> adapter = new FirebaseRecyclerAdapter<Orders, ShowOrderAdminViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull final ShowOrderAdminViewHolder holder, int position, @NonNull final Orders model) {
                    if (model.getStatus().equals("shipping")) {
                        holder.nameTV.setText(model.getName());
                        holder.totalTV.setText("Total: " + model.getTotal());
                        holder.itemProductTV.setText("(" + model.getQuantity() + " item" + ")");
                        holder.addressTV.setText(model.getAddress());
                        holder.phoneTV.setText(model.getPhone());
                        holder.date_timeTV.setText("Date: " + model.getDateTime());
                        holder.statusTV.setText("Status: " + model.status);
                        Picasso.get().load(model.getImage()).fit().into(holder.image);
                        relativeLayout.setVisibility(View.INVISIBLE);
                        holder.admin_order_ship.setVisibility(View.GONE);
                        holder.admin_order_finish.setVisibility(View.GONE);

                        orderId = model.getOrderId();

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showDiaLogProduct();
                            }
                        });
                    } else {
                        holder.itemView.setVisibility(View.GONE);
                        holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                    }
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

    }

    private void showDiaLogProduct() {
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Window window = dialog.getWindow();
        window.getAttributes().windowAnimations = R.style.DialogAnimation;
        title_dialog = dialog.findViewById(R.id.title_dialog_myorder);
        close = dialog.findViewById(R.id.close_dialog_myorder);

        layoutManagerDialog = new LinearLayoutManager(window.getContext());
        recyclerViewDialog = dialog.findViewById(R.id.list_product_MyOrder);
        recyclerViewDialog.setHasFixedSize(true);
        recyclerViewDialog.setLayoutManager(layoutManagerDialog);

        title_dialog.setText("Products Ongoing");
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        // show product ongoing
        if (Prevalent.currentOnLineUsers != null) {
            showProductDialog();
        }

        // set Text layout
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setLayout(width, height);
        dialog.setCancelable(true);
        dialog.show();
    }

    private void showProductDialog() {
        refProductOngoing = FirebaseDatabase.getInstance().getReference().child("Orders").child(orderId).child("products");
        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
            .setQuery(refProductOngoing, Products.class).build();

        FirebaseRecyclerAdapter<Products, ShowCartViewHolder> adapter = new FirebaseRecyclerAdapter<Products, ShowCartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ShowCartViewHolder holder, int position, @NonNull Products model) {
                String quantity = "(" + model.getQuantity() + " item)";
                if (model.getQuantity() > 1) {
                    quantity = "(" + model.getQuantity() + " items)";
                }
                holder.name.setText(model.getName());
                holder.price.setText(model.getPrice() + "đ");
                holder.quantity.setText(quantity);
                holder.address.setText(model.getAddress());
                Picasso.get().load(model.getImage()).fit().into(holder.imageView);
                holder.status.setText("Shipping");
                holder.status.setTextColor(getResources().getColor(R.color.green));
                holder.status.setVisibility(View.VISIBLE);
//
//                holder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(cartView.getContext(), Profile_product.class);
//                        intent.putExtra("pid", model.pid);
//                        startActivity(intent);
//                    }
//                });
            }

            @NonNull
            @Override
            public ShowCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View viewCart = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_cart_holder_view, parent, false);
                ShowCartViewHolder holder = new ShowCartViewHolder(viewCart);
                return holder;
            }
        };
        recyclerViewDialog.setAdapter(adapter);
        adapter.startListening();
    }

    private void mapping() {
        relativeLayout = ongoingView.findViewById(R.id.ongoing_show_no_Order);
        layoutManager = new LinearLayoutManager(ongoingView.getContext());
        recyclerView = ongoingView.findViewById(R.id.list_ongoing);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        dialog = new Dialog(ongoingView.getContext());
        dialog.setContentView(R.layout.dialog_myorder);
    }
}