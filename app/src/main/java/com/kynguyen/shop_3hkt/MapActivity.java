package com.kynguyen.shop_3hkt;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kynguyen.shop_3hkt.admin.Products.AddProduct;

import java.io.IOException;
import java.util.List;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {
  GoogleMap googleMap;
  SupportMapFragment supportMapFragment;
  SearchView searchView;
  Button get_address;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_map);

    searchView = findViewById(R.id.search_map);
    supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
    get_address = findViewById(R.id.get_Address);


    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String query) {
        String location = searchView.getQuery().toString();
        List<Address> addressList = null;

        if (location != null || location.equals("")){
          Geocoder geocoder = new Geocoder(MapActivity.this);
          try {
            addressList = geocoder.getFromLocationName(location,1);
          } catch (IOException e) {
            e.printStackTrace();
          }

          final Address address = addressList.get(0);
          LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
          googleMap.addMarker(new MarkerOptions().position(latLng).title(location));
          googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

          final String lat = Double.toString(address.getLatitude());
          final String lng = Double.toString(address.getLongitude());

          get_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent = new Intent(MapActivity.this, AddProduct.class);
              intent.putExtra("address", address.getAddressLine(0));
              intent.putExtra("lat", lat);
              intent.putExtra("lng", lng);
//              Toast.makeText(MapActivity.this, ""+address.getLatitude() +address.getLongitude() , Toast.LENGTH_SHORT).show();
              startActivity(intent);
              finish();
            }
          });

        }
        return false;
      }

      @Override
      public boolean onQueryTextChange(String newText) {
        return false;
      }
    });
    supportMapFragment.getMapAsync(this);
  }

  @Override
  public void onMapReady(GoogleMap googleMap) {
    this.googleMap = googleMap;
  }
}