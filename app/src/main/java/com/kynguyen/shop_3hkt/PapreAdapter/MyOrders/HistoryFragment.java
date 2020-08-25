package com.kynguyen.shop_3hkt.PapreAdapter.MyOrders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.kynguyen.shop_3hkt.Model.Orders;
import com.kynguyen.shop_3hkt.Prevalent.Prevalent;
import com.kynguyen.shop_3hkt.R;
import com.kynguyen.shop_3hkt.ViewHolder.ShowOrderAdminViewHolder;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

public class HistoryFragment extends Fragment {
    private View historyView;
    private RelativeLayout relativeLayout;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference ref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        historyView = inflater.inflate(R.layout.fragment_history, container, false);
        Paper.init(historyView.getContext());
        mapping();
        return historyView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Prevalent.currentOnLineUsers != null) {
            ref = FirebaseDatabase.getInstance().getReference().child("Orders");
            Query query = ref.orderByChild("uid").equalTo(Prevalent.currentOnLineUsers.getUid());

            FirebaseRecyclerOptions<Orders> options = new FirebaseRecyclerOptions.Builder<Orders>()
                    .setQuery(query, Orders.class).build();

            FirebaseRecyclerAdapter<Orders, ShowOrderAdminViewHolder> adapter = new FirebaseRecyclerAdapter<Orders, ShowOrderAdminViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull final ShowOrderAdminViewHolder holder, int position, @NonNull final Orders model) {
                    if (model.getStatus().equals("finish")) {
                        holder.nameTV.setText(model.getName());
                        holder.totalTV.setText("Total: " + model.getTotal());
                        holder.itemProductTV.setText("(" + model.getQuantity() + " item" + ")");
                        holder.addressTV.setText(model.getAddress());
                        holder.phoneTV.setText("-  " + model.getPhone());
                        holder.date_timeTV.setText("Date: " + model.getDateTime());
                        holder.statusTV.setText("Status: " + model.status);
                        Picasso.get().load(model.getImage()).fit().into(holder.image);
                    }else {
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

    private void mapping() {
        relativeLayout = historyView.findViewById(R.id.history_show_no_Order);
        layoutManager = new LinearLayoutManager(historyView.getContext());
        recyclerView = historyView.findViewById(R.id.list_history);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
    }
}