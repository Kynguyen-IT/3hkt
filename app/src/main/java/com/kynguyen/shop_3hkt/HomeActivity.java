package com.kynguyen.shop_3hkt;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kynguyen.shop_3hkt.Model.User;
import com.kynguyen.shop_3hkt.ui_fragment.AccountFragment;
import com.kynguyen.shop_3hkt.ui_fragment.HomeFragment;
import com.kynguyen.shop_3hkt.ui_fragment.MyOrdersFragment;
import com.kynguyen.shop_3hkt.ui_fragment.SavedFragment;

public class HomeActivity extends AppCompatActivity {
  private User user;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);
    BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
    bottomNavigationView.setOnNavigationItemSelectedListener(nav_listen);
    getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new HomeFragment()).commit();
  }


  private  BottomNavigationView.OnNavigationItemSelectedListener nav_listen = new BottomNavigationView.OnNavigationItemSelectedListener() {
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
      Fragment fragment = null;

      switch (item.getItemId()){
        case R.id.nav_shop:
          fragment = new HomeFragment();
          break;

        case R.id.nav_search:
          fragment = new SavedFragment();
          break;

        case R.id.nav_cart:
        fragment = new MyOrdersFragment();
        break;

        case R.id.nav_account:
          fragment = new AccountFragment();
          break;
      }
      getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commit();
      return true;
    }
  };
}
