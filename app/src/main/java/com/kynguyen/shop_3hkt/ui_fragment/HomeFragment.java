package com.kynguyen.shop_3hkt.ui_fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kynguyen.shop_3hkt.MainActivity;
import com.kynguyen.shop_3hkt.Model.Categories;
import com.kynguyen.shop_3hkt.Model.Products;
import com.kynguyen.shop_3hkt.Prevalent.Constants;
import com.kynguyen.shop_3hkt.Prevalent.FetchAddressIntentService;
import com.kynguyen.shop_3hkt.Prevalent.Prevalent;
import com.kynguyen.shop_3hkt.Profile_product;
import com.kynguyen.shop_3hkt.R;
import com.kynguyen.shop_3hkt.ViewHolder.ShowCateViewHolder;
import com.kynguyen.shop_3hkt.ViewHolder.ShowProductsViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class HomeFragment extends Fragment {
  private ImageSlider imageSlider;
  private View viewHome;
  private TextView titleHome;
  private RecyclerView recyclerViewCate, recyclerViewProduct;
  private RecyclerView.LayoutManager layoutManagercate, layoutManagerProduct;
  private static final int REQUAST_CODE_LOCATION_PREMISSION = 1;
  private ResultReceiver resultReceiver;
  private DatabaseReference refCate, refProduct, refSave;
  private double latitude, longitude;
  private boolean status;
  private String addressUser;
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    viewHome = inflater.inflate(R.layout.fragment_home,container, false);
    Paper.init(viewHome.getContext());

    resultReceiver = new AddressResultReceiver(new Handler());
    mapping();

    // show slider images
    showSlider();
    LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(viewHome.getContext(), LinearLayoutManager.HORIZONTAL, false);
    recyclerViewCate.setLayoutManager(horizontalLayoutManager);

    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUAST_CODE_LOCATION_PREMISSION);
    } else {
      getCurrentLotion();
    }
    return viewHome;
  }




  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == REQUAST_CODE_LOCATION_PREMISSION && grantResults.length > 0) {
      if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        getCurrentLotion();
      } else {
        Toast.makeText(getContext(), "Permission denied!", Toast.LENGTH_SHORT).show();
      }
    }
  }

  @SuppressLint("MissingPermission")
  private void getCurrentLotion() {
    final LocationRequest locationRequest = new LocationRequest();
    locationRequest.setInterval(10000);
    locationRequest.setFastestInterval(3000);
    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    LocationServices.getFusedLocationProviderClient(this.getActivity()).requestLocationUpdates(locationRequest, new LocationCallback() {
      @Override
      public void onLocationResult(LocationResult locationResult) {
        super.onLocationResult(locationResult);
        LocationServices.getFusedLocationProviderClient(getContext()).removeLocationUpdates(this);
        if (locationResult != null && locationResult.getLocations().size() > 0) {
          int latestLocationIndex = locationResult.getLocations().size() - 1;
           latitude = locationResult.getLocations().get(latestLocationIndex).getLatitude();
           longitude = locationResult.getLocations().get(latestLocationIndex).getLongitude();

          Location location = new Location("providerNA");
          location.setLatitude(latitude);
          location.setLongitude(longitude);
          fetchAddressFromLatLong(location);
        }
      }
    }, Looper.getMainLooper());
  }

  private class AddressResultReceiver extends ResultReceiver {
    public AddressResultReceiver(Handler handler) {
      super(handler);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
      super.onReceiveResult(resultCode, resultData);
      if (resultCode == Constants.SUCCESS_RESULT){
        titleHome.setText(resultData.getString(Constants.RESULT_DATA_KEY));
        addressUser = resultData.getString(Constants.RESULT_DATA_KEY);
      } else {
        Toast.makeText(getActivity(), resultData.getString(Constants.RESULT_DATA_KEY), Toast.LENGTH_SHORT).show();
      }
    }
  }

  private void fetchAddressFromLatLong(Location location){
    Intent intent = new Intent(getActivity(), FetchAddressIntentService.class);
    intent.putExtra(Constants.RECEIVER, resultReceiver);
    intent.putExtra(Constants.LOCATION_DATA_EXTRA, location);
    getActivity().startService(intent);
  }

  @Override
  public void onStart() {
    super.onStart();
    refCate = FirebaseDatabase.getInstance().getReference().child("categories");
    FirebaseRecyclerOptions<Categories> optionsCate = new FirebaseRecyclerOptions.Builder<Categories>()
        .setQuery(refCate, Categories.class).build();

    FirebaseRecyclerAdapter<Categories, ShowCateViewHolder> adapterCate = new FirebaseRecyclerAdapter <Categories, ShowCateViewHolder>(optionsCate){
      @NonNull
      @Override
      public ShowCateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewCate = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_cates_home_holder_view,parent,false);
        ShowCateViewHolder holder = new ShowCateViewHolder(viewCate);
        return holder;
      }

      @Override
      protected void onBindViewHolder(@NonNull ShowCateViewHolder holder, int position, @NonNull Categories model) {
        holder.textView.setText(model.getName());
        Picasso.get().load(model.getImage()).fit().into(holder.imageView);
      }
    };
    recyclerViewCate.setAdapter(adapterCate);
    adapterCate.startListening();
    // products
    refProduct = FirebaseDatabase.getInstance().getReference().child("products");
    FirebaseRecyclerOptions<Products> optionsProduct = new FirebaseRecyclerOptions.Builder<Products>()
        .setQuery(refProduct, Products.class).build();

    FirebaseRecyclerAdapter<Products, ShowProductsViewHolder> adapterProduct = new FirebaseRecyclerAdapter<Products, ShowProductsViewHolder>(optionsProduct) {
      @NonNull
      @Override
      public ShowProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewProduct = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_products_holder_view,parent,false);
        ShowProductsViewHolder holder = new ShowProductsViewHolder(viewProduct);
        return holder;
      }

      @Override
      protected void onBindViewHolder(@NonNull final ShowProductsViewHolder holder, int position, @NonNull final Products model) {
        holder.name.setText(model.getName());
        holder.address.setText(model.getAddress());
        Picasso.get().load(model.getImage()).fit().into(holder.imageProduct);
        holder.description.setText(model.getDescription());

        refSave = FirebaseDatabase.getInstance().getReference();

        if(Prevalent.currentOnLineUsers != null){
          refSave.child("saves").child(model.pid).child(Prevalent.currentOnLineUsers.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              if (dataSnapshot.getValue() == null){
                status = false;
                holder.heart_product.setImageResource(R.drawable.ic_save);
              } else {
                status = true;
                holder.heart_product.setImageResource(R.drawable.ic_saved);
              }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
          });
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Intent intent = new Intent(viewHome.getContext(), Profile_product.class);
            intent.putExtra("pid", model.pid);
            intent.putExtra("addressUser", addressUser);
            startActivity(intent);
          }
        });
      }
    };
    recyclerViewProduct.setAdapter(adapterProduct);
    adapterProduct.startListening();
  }

  @SuppressLint("ResourceType")
  private void showSlider() {
    List<SlideModel> slideModels = new ArrayList<>();
    slideModels.add(new SlideModel(R.drawable.imageslide1));
    slideModels.add(new SlideModel(R.drawable.imageslide2));
    slideModels.add(new SlideModel(R.drawable.imageslide3));
    slideModels.add(new SlideModel(R.drawable.imageslide4));
    slideModels.add(new SlideModel(R.drawable.imageslide5));
    slideModels.add(new SlideModel(R.drawable.imageslide6));
    slideModels.add(new SlideModel(R.drawable.imageslide7));
    imageSlider.setImageList(slideModels, true);
  }

  private void mapping() {
    layoutManagercate = new LinearLayoutManager(getContext());
    recyclerViewCate = viewHome.findViewById(R.id.list_Cates_home);
    recyclerViewCate.setHasFixedSize(true);
    recyclerViewCate.setLayoutManager(layoutManagercate);
    // list product
    layoutManagerProduct = new LinearLayoutManager(getContext());
    recyclerViewProduct = viewHome.findViewById(R.id.list_product);
    recyclerViewProduct.setHasFixedSize(true);
    recyclerViewProduct.setLayoutManager(layoutManagerProduct);
    // -------------------------
    imageSlider = viewHome.findViewById(R.id.image_slider);
    titleHome = viewHome.findViewById(R.id.title_home);
  }
}
