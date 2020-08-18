package com.kynguyen.shop_3hkt.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kynguyen.shop_3hkt.Intetfave.itemClickLitsner;
import com.kynguyen.shop_3hkt.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShowCateViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
  public CircleImageView imageView;
  public TextView textView;
  private itemClickLitsner listenr;
  public ShowCateViewHolder(@NonNull View itemView) {
    super(itemView);
    imageView = itemView.findViewById(R.id.image_cates_home);
    textView = itemView.findViewById(R.id.name_cates_home);
  }

  @Override
  public void onClick(View v) {
    listenr.onClick(v, getAdapterPosition(), false);
  }

  public void setListenr(itemClickLitsner listenr) {
    this.listenr = listenr;
  }
}
