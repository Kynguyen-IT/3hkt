package com.kynguyen.shop_3hkt.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kynguyen.shop_3hkt.Interface.itemClickListener;
import com.kynguyen.shop_3hkt.R;

public class ShowProductsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public ImageView imageProduct;
    public TextView address, name, time, range, description;
    public ImageView heart_product;
    public itemClickListener listen;
    public RelativeLayout relativeLayout;

    public ShowProductsViewHolder(@NonNull View itemView) {
        super(itemView);
        imageProduct = itemView.findViewById(R.id.image_product_home);
        address = itemView.findViewById(R.id.address_product_home);
        name = itemView.findViewById(R.id.name_product_home);
        heart_product = itemView.findViewById(R.id.heart_product_home);
        time = itemView.findViewById(R.id.time_ship_product_home);
        range = itemView.findViewById(R.id.range_ship_product_home);
        description = itemView.findViewById(R.id.description_product_home);
        relativeLayout = itemView.findViewById(R.id.detail_product);
    }


    @Override
    public void onClick(View v) {
        listen.onClick(v, getAdapterPosition(), false);
    }

    public void setListen(itemClickListener listenr) {
        this.listen = listenr;
    }
}
