package com.kynguyen.shop_3hkt.PapreAdapter.MyOrders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.kynguyen.shop_3hkt.R;

public class ongoingFragment extends Fragment {

  public ongoingFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_ongoing, container, false);
  }
}