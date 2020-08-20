package com.kynguyen.shop_3hkt.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kynguyen.shop_3hkt.Intetfave.itemClickLitsner;
import com.kynguyen.shop_3hkt.R;

public class ShowOrderAdminViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public ImageView image;
    public TextView nameTV, phoneTV, statusTV, addressTV, date_timeTV, itemProductTV, totalTV;
    public itemClickLitsner listenr;
    public Button admin_order_ship, admin_order_finish;

    public ShowOrderAdminViewHolder(@NonNull View itemView) {
        super(itemView);
        nameTV = itemView.findViewById(R.id.name_user_order);
        phoneTV = itemView.findViewById(R.id.phone_Order);
        statusTV = itemView.findViewById(R.id.status_user_order);
        addressTV = itemView.findViewById(R.id.address_user_order);
        date_timeTV = itemView.findViewById(R.id.date_time_user_order);
        itemProductTV = itemView.findViewById(R.id.item_user_order);
        totalTV = itemView.findViewById(R.id.total_user_order);
        image = itemView.findViewById(R.id.image_user_order);
        admin_order_ship = itemView.findViewById(R.id.admin_order_ship);
        admin_order_finish = itemView.findViewById(R.id.admin_order_finish);
    }

    @Override
    public void onClick(View v) {
        listenr.onClick(v, getAdapterPosition(), false);
    }

    public void setListenr(itemClickLitsner listenr) {
        this.listenr = listenr;
    }
}
