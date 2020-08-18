package com.kynguyen.shop_3hkt.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.kynguyen.shop_3hkt.R;

public class ShowCartDialogViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
  public TextView nameTVCartDialog , priceTVCartDialog, quantityTVCartDialog;
  public Button decreaseCartDialog, increaseCartDialog;
  public ShowCartDialogViewHolder(@NonNull View itemView) {
    super(itemView);
    nameTVCartDialog = itemView.findViewById(R.id.name_product_cart_dialog);
    priceTVCartDialog = itemView.findViewById(R.id.price_product_cart_dialog);
    quantityTVCartDialog = itemView.findViewById(R.id.quantity_value_cart_dialog);
    decreaseCartDialog = itemView.findViewById(R.id.decrease_cart_dialog);
    increaseCartDialog = itemView.findViewById(R.id.increase_cart_dialog);
  }

  @Override
  public void onClick(View v) {
  }
}
