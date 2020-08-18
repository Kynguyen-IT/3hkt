package com.kynguyen.shop_3hkt.PapreAdapter.MyOrders;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class MyOrdersPaperAdapter extends FragmentStatePagerAdapter {
  private String listTab[] = {"Ongoing", "History", "Cart"};
  public MyOrdersPaperAdapter(FragmentManager fm, int tabCount) {
    super(fm);
  }

  @NonNull
  @Override
  public Fragment getItem(int position) {
    switch (position){
      case 0:
        return new ongoingFragment();
      case 1:
        return new HistoryFragment();
      case 2:
        return new CartFragment();
      default:
        return null;
    }
  }

  @Override
  public int getCount() {
    return listTab.length;
  }

  @Nullable
  @Override
  public CharSequence getPageTitle(int position) {
    return listTab[position];
  }
}
