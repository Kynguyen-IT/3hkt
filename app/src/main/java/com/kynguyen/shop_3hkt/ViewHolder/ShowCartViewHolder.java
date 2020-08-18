package com.kynguyen.shop_3hkt.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kynguyen.shop_3hkt.Intetfave.itemClickLitsner;
import com.kynguyen.shop_3hkt.R;

public class ShowCartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
  public ImageView imageView;
  public TextView price, address, name, quantity;
  public itemClickLitsner listenr;
  public ShowCartViewHolder(@NonNull View itemView) {
    super(itemView);
    imageView = itemView.findViewById(R.id.image_product_cart_holder);
    price = itemView.findViewById(R.id.price_product_cart_holder);
    address = itemView.findViewById(R.id.address_product_cart_holder);
    name = itemView.findViewById(R.id.name_product_cart_holder);
    quantity = itemView.findViewById(R.id.quantity_product_cart_holder);
  }


  @Override
  public void onClick(View v) {
    listenr.onClick(v, getAdapterPosition(), false);
  }

  public void setListenr(itemClickLitsner listenr) {
    this.listenr = listenr;
  }
}
