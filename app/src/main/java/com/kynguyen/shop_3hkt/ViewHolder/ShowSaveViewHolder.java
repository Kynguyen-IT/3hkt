package com.kynguyen.shop_3hkt.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kynguyen.shop_3hkt.Intetfave.itemClickLitsner;
import com.kynguyen.shop_3hkt.R;

public class ShowSaveViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener {
  public ImageView image;
  public TextView name,address;
  public itemClickLitsner listenr;
  public RelativeLayout relativeLayout;
  public ShowSaveViewHolder(@NonNull View itemView) {
    super(itemView);
    image = itemView.findViewById(R.id.image_product_save_holder);
    name = itemView.findViewById(R.id.name_product_save_holder);
    address = itemView.findViewById(R.id.address_product_save_holder);
    relativeLayout = itemView.findViewById(R.id.box_saves);
  }

  @Override
  public void onClick(View v) {
    listenr.onClick(v, getAdapterPosition(), false);
  }

  public void setListenr(itemClickLitsner listenr) {
    this.listenr = listenr;
  }
}
