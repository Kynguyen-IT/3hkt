package com.kynguyen.shop_3hkt.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kynguyen.shop_3hkt.R;

public class ShowProductOrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
  public TextView quantityAndNameTV, priceTV;
  public ShowProductOrderViewHolder(@NonNull View itemView) {
    super(itemView);
    quantityAndNameTV = (TextView) itemView.findViewById(R.id.quantity_and_name_product_order_holder_view);
    priceTV = (TextView) itemView.findViewById(R.id.price_product_order_holder_view);
  }

  @Override
  public void onClick(View v) {

  }
}
