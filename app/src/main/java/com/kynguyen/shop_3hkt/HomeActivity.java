package com.kynguyen.shop_3hkt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.icu.text.SelectFormat;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kynguyen.shop_3hkt.ui_fragment.AccountFragment;
import com.kynguyen.shop_3hkt.ui_fragment.CartFragment;
import com.kynguyen.shop_3hkt.ui_fragment.HomeFragment;
import com.kynguyen.shop_3hkt.ui_fragment.SearchFragment;

public class HomeActivity extends AppCompatActivity {

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
          fragment = new SearchFragment();
          break;

        case R.id.nav_cart:
        fragment = new CartFragment();
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
