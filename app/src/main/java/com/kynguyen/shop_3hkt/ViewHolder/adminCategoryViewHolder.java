package com.kynguyen.shop_3hkt.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kynguyen.shop_3hkt.Intetfave.itemClickLitsner;
import com.kynguyen.shop_3hkt.R;

public class adminCategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
  public TextView name_category;
  private itemClickLitsner listenr;

  public adminCategoryViewHolder(@NonNull View itemView) {
    super(itemView);
    name_category = itemView.findViewById(R.id.text_Category_view_holder);
  }

  @Override
  public void onClick(View v) {
    listenr.onClick(v, getAdapterPosition(), false);
  }

  public void setListenr(itemClickLitsner listenr) {
    this.listenr = listenr;
  }
}
