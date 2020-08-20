package com.kynguyen.shop_3hkt.PapreAdapter.MyOrders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kynguyen.shop_3hkt.R;

import io.paperdb.Paper;

public class ongoingFragment extends Fragment {
  private View ongoingView;
  private RelativeLayout relativeLayout;
  private RecyclerView recyclerView;
  private RecyclerView.LayoutManager layoutManager;
  private DatabaseReference refOrder;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    ongoingView = inflater.inflate(R.layout.fragment_cart, container, false);
    Paper.init(ongoingView.getContext());
    mapping();
    return ongoingView;
  }

  @Override
  public void onStart() {
    super.onStart();
//    String key = FirebaseDatabase.getInstance().getReference().child("Orders").child("Products").push().getKey();
    refOrder = FirebaseDatabase.getInstance().getReference().child("Orders").child("Products");
    refOrder.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        for (DataSnapshot value: dataSnapshot.getChildren()){
          String id = value.getKey();
              Toast.makeText(ongoingView.getContext(), ""+id.toString(), Toast.LENGTH_LONG).show();

        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });

//    FirebaseRecyclerOptions<Orders> options = new FirebaseRecyclerOptions.Builder<Orders>()
//        .setQuery(refOrder, Orders.class).build();
//
//
//    FirebaseRecyclerAdapter<Orders, ShowOrderAdminViewHolder> adapter = new FirebaseRecyclerAdapter<Orders, ShowOrderAdminViewHolder>(options) {
//      @Override
//      protected void onBindViewHolder(@NonNull final ShowOrderAdminViewHolder holder, int position, @NonNull final Orders model) {
//        holder.nameTV.setText(model.getName());
//        holder.totalTV.setText("Total: "+model.getTotal());
//        holder.itemProductTV.setText("(" + model.getQuantity() +"item" +")");
//        holder.addressTV.setText(model.getAddress());
//        holder.phoneTV.setText("-  " + model.getPhone());
//        holder.date_timeTV.setText("Date: "+model.getDateTime());
//        holder.statusTV.setText("Status: "+model.status);
//        Picasso.get().load(model.getImage()).fit().into(holder.image);
//
//      }
//
//      @NonNull
//      @Override
//      public ShowOrderAdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_order_admin,parent,false);
//        ShowOrderAdminViewHolder holder = new ShowOrderAdminViewHolder(view);
//        return holder;
//      }
//    };
//
//    recyclerView.setAdapter(adapter);
//    adapter.startListening();
  }

  private void mapping() {
    relativeLayout = ongoingView.findViewById(R.id.ongoing_show_no_Order);
    layoutManager = new LinearLayoutManager(ongoingView.getContext());
    recyclerView = ongoingView.findViewById(R.id.list_cart_product_holder_View);
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(layoutManager);
  }
}