package com.kynguyen.shop_3hkt.PapreAdapter.MyOrders;

import android.content.Intent;
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
import com.kynguyen.shop_3hkt.Model.Cart;
import com.kynguyen.shop_3hkt.Prevalent.Prevalent;
import com.kynguyen.shop_3hkt.Profile_product;
import com.kynguyen.shop_3hkt.R;
import com.kynguyen.shop_3hkt.ViewHolder.ShowCartViewHolder;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

public class CartFragment extends Fragment {
  private View cartView;
  private RecyclerView recyclerViewListCart;
  private RelativeLayout cart_show_no_Order;
  private RecyclerView.LayoutManager layoutManagerListCart;
  private DatabaseReference refCart;
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    cartView = inflater.inflate(R.layout.fragment_cart, container, false);
    Paper.init(cartView.getContext());
    mapping();
    return cartView;
  }


  @Override
  public void onStart() {
    super.onStart();
    if (Prevalent.currentOnLineUsers != null) {
      refCart = FirebaseDatabase.getInstance().getReference().child("Cart List").child("User")
          .child(Prevalent.currentOnLineUsers.getUid()).child("Products");
      FirebaseRecyclerOptions<Cart> optionsCartProduct = new FirebaseRecyclerOptions.Builder<Cart>()
          .setQuery(refCart, Cart.class).build();

      FirebaseRecyclerAdapter<Cart, ShowCartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, ShowCartViewHolder>(optionsCartProduct) {
        @NonNull
        @Override
        public ShowCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
          View viewCart = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_cart_holder_view, parent, false);
          ShowCartViewHolder holder = new ShowCartViewHolder(viewCart);
          return holder;
        }

        @Override
        protected void onBindViewHolder(@NonNull ShowCartViewHolder holder, int position, @NonNull final Cart model) {
          holder.name.setText(model.getName());
          holder.price.setText(model.getPrice() + "đ");
          holder.quantity.setText("(" + model.getQuantity() + " item)");
          holder.address.setText(model.getAddress());
          Picasso.get().load(model.getImage()).fit().into(holder.imageView);
          cart_show_no_Order.setVisibility(View.INVISIBLE);

          holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent = new Intent(cartView.getContext(), Profile_product.class);
              intent.putExtra("pid", model.pid);
              startActivity(intent);
            }
          });
        }

      };
      recyclerViewListCart.setAdapter(adapter);
      adapter.startListening();

    }
  }

  private void mapping() {
    layoutManagerListCart = new LinearLayoutManager(cartView.getContext());
    recyclerViewListCart = cartView.findViewById(R.id.list_cart_product_holder_View);
    recyclerViewListCart.setHasFixedSize(true);
    recyclerViewListCart.setLayoutManager(layoutManagerListCart);
    cart_show_no_Order = cartView.findViewById(R.id.cart_show_no_Order);
  }


}