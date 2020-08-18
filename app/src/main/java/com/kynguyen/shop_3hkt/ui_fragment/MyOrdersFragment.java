package com.kynguyen.shop_3hkt.ui_fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.kynguyen.shop_3hkt.PapreAdapter.MyOrders.MyOrdersPaperAdapter;
import com.kynguyen.shop_3hkt.R;

import io.paperdb.Paper;

public class MyOrdersFragment extends Fragment {
  private View viewMyOders;
  private TabLayout tabLayout;
  private ViewPager viewPager;
  private MyOrdersPaperAdapter myOrdersPaperAdapter;
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    viewMyOders = inflater.inflate(R.layout.fragment_myorders,container, false);
    Paper.init(viewMyOders.getContext());
    mapping();

    myOrdersPaperAdapter = new MyOrdersPaperAdapter(getParentFragmentManager(),tabLayout.getTabCount());
    viewPager.setAdapter(myOrdersPaperAdapter);
    tabLayout.setupWithViewPager(viewPager);
    return viewMyOders;
  }

  private void mapping() {
    tabLayout = viewMyOders.findViewById(R.id.tab_layout_myOrders);
    viewPager = viewMyOders.findViewById(R.id.view_pager_myOrders);
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }
}
