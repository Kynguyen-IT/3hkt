package com.kynguyen.shop_3hkt;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kynguyen.shop_3hkt.Model.Cart;
import com.kynguyen.shop_3hkt.Model.Products;
import com.kynguyen.shop_3hkt.Prevalent.Prevalent;
import com.kynguyen.shop_3hkt.ViewHolder.ShowCartDialogViewHolder;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class Profile_product extends AppCompatActivity {
  private Toolbar toolbar;
  private ImageView imageProduct, itemImagesProduct, save;
  private TextView nameProduct, addressProduct, dateProduct, priceProduct, descriptionProduct, quantity, itemNameProduct, itemPriceProduct, clearAll, badge, quantityCartFooter, quantityCartFooterDialog, badgeDiaLog;
  private FontAwesome close;
  private Button CheckOut , CheckOutDialog;
  private String productId = "";
  private DatabaseReference refProduct, refCart, refSave;
  private Button decrease, increase;
  private String getImage, getCateIdProduct, getPrice, addressUser;
  private FontAwesome closeDialogCart;
  private boolean status;
  private RelativeLayout footerCart, footerCartDialog;
  private int i = 0, resultQuantity = 0, getSumQuantity = 0, getTotalPrice, getQuantityCartDialog = 0;
  private Dialog dialog;
  private RecyclerView recyclerViewCartDialog;
  private RecyclerView.LayoutManager layoutManagerCartDialog;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profile_product);
    mapping();
    productId = getIntent().getStringExtra("pid");
    addressUser = getIntent().getStringExtra("addressUser");
    getProduct(productId);

    if (Prevalent.currentOnLineUsers != null){
      CheckQuantityProduct();
      checkSumQuantityPrice(getSumQuantity, getTotalPrice);
      checkSaveProduct();
    }

    decrease.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (Prevalent.currentOnLineUsers != null) {
          if (i > 0) {
            i -= 1;
            quantity.setText(String.valueOf(i));
            if (i == 0) {
              removeAddingToCart(productId);
            } else {
              addingToCart(productId,i);
            }
          } else {
            Log.d("src", "Value can't be less than 0");
          }
        } else {
          Intent intent = new Intent(Profile_product.this , SignInActivity.class);
          startActivity(intent);
        }

      }
    });

    increase.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (Prevalent.currentOnLineUsers != null) {
          i += 1;
          quantity.setText(String.valueOf(i));
          addingToCart(productId,i);
        } else {
          Intent intent = new Intent(Profile_product.this , SignInActivity.class);
          startActivity(intent);
        }
      }
    });

    close.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(Profile_product.this, HomeActivity.class);
        startActivity(intent);
      }
    });

    save.setOnClickListener(new View.OnClickListener() {
      @SuppressLint("ResourceAsColor")
      @Override
      public void onClick(View v) {
        if (Prevalent.currentOnLineUsers != null){
          String userUid = Prevalent.currentOnLineUsers.uid;
          if (status == true) {
            refSave.child("saves").child(productId).child(userUid).setValue(false);
            save.setImageResource(R.drawable.ic_save);
          } else {
            refSave.child("saves").child(productId).child(userUid).setValue(true);
            save.setImageResource(R.drawable.ic_saved);
          }
        } else {
          Intent intent = new Intent(Profile_product.this , SignInActivity.class);
          startActivity(intent);
        }

      }
    });

    footerCart.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        showDiaLogCart();
      }
    });

    CheckOut.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(Profile_product.this, CheckoutOrder.class);
        intent.putExtra("addressUser", addressUser);
        intent.putExtra("item",badgeDiaLog.getText().toString());
        intent.putExtra("total",quantityCartFooterDialog.getText().toString());
        startActivity(intent);
      }
    });
  }

  private void showDiaLogCart() {
    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    Window window = dialog.getWindow();
    window.getAttributes().windowAnimations = R.style.DialogAnimation;
    // mapping
    closeDialogCart = dialog.findViewById(R.id.close_dialog_cart);
    layoutManagerCartDialog = new LinearLayoutManager(window.getContext());
    recyclerViewCartDialog = dialog.findViewById(R.id.list_product_cart_dialog);
    recyclerViewCartDialog.setHasFixedSize(true);
    recyclerViewCartDialog.setLayoutManager(layoutManagerCartDialog);
    footerCartDialog = dialog.findViewById(R.id.footer_dialog_Cart);
    clearAll = dialog.findViewById(R.id.clear_all_product_dialog_cart);
    CheckOutDialog = dialog.findViewById(R.id.checkout_btn_dialog_cart);
    //

    CheckOutDialog.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(Profile_product.this, CheckoutOrder.class);
        intent.putExtra("addressUser", addressUser);
        intent.putExtra("item",badgeDiaLog.getText().toString());
        intent.putExtra("total",quantityCartFooterDialog.getText().toString());
        startActivity(intent);
      }
    });

    // clear all product
    clearAll.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (Prevalent.currentOnLineUsers != null){
          clearAllProduct();
        }
      }
    });
    // click footerCartDialog
    footerCartDialog.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dialog.dismiss();
      }
    });
    // show list Product cart

    if (Prevalent.currentOnLineUsers != null) {
      showProductCartDialog();
      checkSumQuantityPrice(getSumQuantity, getTotalPrice);
    }
    // set Text layout
    int width = ViewGroup.LayoutParams.MATCH_PARENT;
    int height = ViewGroup.LayoutParams.MATCH_PARENT;
    dialog.getWindow().setLayout(width, height);
    closeDialogCart.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dialog.dismiss();
      }
    });
    closeDialogCart.setVisibility(View.VISIBLE);
    dialog.setCancelable(true);
    dialog.show();
  }

  private void clearAllProduct() {
    refCart = FirebaseDatabase.getInstance().getReference().child("Cart List");
    refCart.child("User").child(Prevalent.currentOnLineUsers.getUid()).removeValue();
  }

  private void showProductCartDialog(){
      refCart = FirebaseDatabase.getInstance().getReference().child("Cart List").child("User")
          .child(Prevalent.currentOnLineUsers.getUid()).child("Products");
      FirebaseRecyclerOptions<Cart> optionsCartProduct = new FirebaseRecyclerOptions.Builder<Cart>()
          .setQuery(refCart, Cart.class).build();
      FirebaseRecyclerAdapter<Cart, ShowCartDialogViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, ShowCartDialogViewHolder>(optionsCartProduct) {
        @NonNull
        @Override
        public ShowCartDialogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
          View viewCartDiaLog = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_product_cart_dialog_holder_view,parent,false);
          ShowCartDialogViewHolder holder = new ShowCartDialogViewHolder(viewCartDiaLog);
          return holder;
        }

        @Override
        protected void onBindViewHolder(@NonNull final ShowCartDialogViewHolder holder, int position, @NonNull final Cart model) {
          holder.nameTVCartDialog.setText(model.getName());
          holder.priceTVCartDialog.setText(model.getPrice());
          holder.quantityTVCartDialog.setText(String.valueOf(model.getQuantity()));
          holder.decreaseCartDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              getQuantityCartDialog = model.getQuantity();
                getQuantityCartDialog -= 1;
          holder.quantityTVCartDialog.setText(String.valueOf(getQuantityCartDialog));
            if (getQuantityCartDialog == 0) {
                removeAddingToCart(model.getPid());
              }
              else {
                addingToCart(model.getPid() ,getQuantityCartDialog);
            }
            }
          });

          holder.increaseCartDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              getQuantityCartDialog += 1;
              addingToCart( model.getPid(),getQuantityCartDialog);
            }
          });
        }
      };
      recyclerViewCartDialog.setAdapter(adapter);
      adapter.startListening();
  }



  private void checkSaveProduct() {
    refSave = FirebaseDatabase.getInstance().getReference();
    refSave.child("saves").child(productId).child(Prevalent.currentOnLineUsers.getUid()).addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        if (dataSnapshot.exists()){
          if (dataSnapshot.getValue().equals(true)) {
            status = true;
            save.setImageResource(R.drawable.ic_saved);
          } else {
            status = false;
            HashMap<String,Object> savesproducts = new HashMap<>();
            savesproducts.put("pid", productId);
            savesproducts.put("name", nameProduct.getText().toString());
            savesproducts.put("address", addressProduct.getText().toString());
            savesproducts.put("image", getImage);

            refSave.child("saves").child(productId)
                .updateChildren(savesproducts).addOnCompleteListener(new OnCompleteListener<Void>() {
              @Override
              public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                }
              }
            });
            save.setImageResource(R.drawable.ic_save);
          }
        }

      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });
  }

  private void checkSumQuantityPrice(int q , int p){
    refCart.child("User").child(Prevalent.currentOnLineUsers.getUid())
        .child("Products").addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        int q  = 0;
        int p = 0;
        int getPriceCart = 0;
        for (DataSnapshot reslutCart : dataSnapshot.getChildren()) {
          String KeyProductCartId = reslutCart.getKey();
          Cart cart = dataSnapshot.child(KeyProductCartId).getValue(Cart.class);
          int quantityCart = cart.getQuantity();
          int priceCart = Integer.parseInt(cart.getPrice());
          q += quantityCart;
          getPriceCart += priceCart;
          p = q * getPriceCart;

          badge.setText(String.valueOf(q));
          quantityCartFooter.setText(String.valueOf(p+"đ"));

          badgeDiaLog.setText(String.valueOf(q));
          quantityCartFooterDialog.setText(String.valueOf(p + "đ"));
        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {
      }
    });
  }


  private void CheckQuantityProduct() {
    refCart = FirebaseDatabase.getInstance().getReference().child("Cart List");
    refCart.child("User").child(Prevalent.currentOnLineUsers.getUid())
        .child("Products").child(productId).child("quantity").addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        if (dataSnapshot.getValue() == null) {
          i = 0;
          quantity.setText(String.valueOf(i));
          footerCart.setVisibility(View.INVISIBLE);
        } else {
          i = Integer.parseInt(dataSnapshot.getValue().toString());
          quantity.setText(String.valueOf(i));
          footerCart.setVisibility(View.VISIBLE);
        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });
  }


  private void removeAddingToCart(String id) {
    refCart = FirebaseDatabase.getInstance().getReference().child("Cart List");

      refCart.child("User").child(Prevalent.currentOnLineUsers.getUid())
          .child("Products").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
          dataSnapshot.getRef().removeValue();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
      });
  }

  private void addingToCart(String id, int i) {
    String time, date;
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,yyyy");
    date = currentDate.format(calendar.getTime());
    SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
    time = currentTime.format(calendar.getTime());
    refCart = FirebaseDatabase.getInstance().getReference().child("Cart List");
    final HashMap<String, Object> cartMap = new HashMap<>();
    cartMap.put("pid", id);
    cartMap.put("cateId", getCateIdProduct);
    cartMap.put("name", nameProduct.getText().toString());
    cartMap.put("address", addressProduct.getText().toString());
    cartMap.put("description", descriptionProduct.getText().toString());
    cartMap.put("price", getPrice);
    cartMap.put("image", getImage);
    cartMap.put("date", date);
    cartMap.put("time", time);
    cartMap.put("quantity",i);
    cartMap.put("discount", "");

    refCart.child("User").child(Prevalent.currentOnLineUsers.getUid())
        .child("Products").child(id)
        .updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
      @Override
      public void onComplete(@NonNull Task<Void> task) {
        if (task.isSuccessful()) {
        }
      }
    });


  }

  private void getProduct(final String productId) {
    refProduct = FirebaseDatabase.getInstance().getReference().child("products");
    refProduct.child(productId).addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        if (dataSnapshot.exists()) {
          final Products product = dataSnapshot.getValue(Products.class);
          nameProduct.setText(product.getName());
          itemNameProduct.setText(product.getName());
          addressProduct.setText(product.getAddress());
          descriptionProduct.setText(product.getDescription());
          priceProduct.setText(product.getPrice() + "đ");
          itemPriceProduct.setText(product.getPrice() + "đ");
          Picasso.get().load(product.getImage()).into(itemImagesProduct);
          Picasso.get().load(product.getImage()).into(imageProduct);
          getImage = product.getImage();
          getCateIdProduct = product.getIdCategory();
          getPrice = product.getPrice();
        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });
  }

  private void mapping() {
    toolbar = findViewById(R.id.toolbar);
    close = findViewById(R.id.close_product_profile);
    save = findViewById(R.id.save_product_profile);
    nameProduct = findViewById(R.id.name_product_profile);
    addressProduct = findViewById(R.id.address_product_profile);
    dateProduct = findViewById(R.id.date_product_profile);
    priceProduct = findViewById(R.id.price_product_profile);
    descriptionProduct = findViewById(R.id.description_product_profile);
    imageProduct = findViewById(R.id.image_product_profile);
    // item product
    itemImagesProduct = findViewById(R.id.image_product_item_container_main_product_profile);
    itemNameProduct = findViewById(R.id.name_item_container_main_product_profile);
    itemPriceProduct = findViewById(R.id.price_item_container_main_product_profile);
    // quantity product
    decrease = findViewById(R.id.decrease_product_profile);
    increase = findViewById(R.id.increase_product_profile);
    quantity = findViewById(R.id.quantity_value_product_profile);
    // footer
    footerCart = findViewById(R.id.footer_product_profile);
    badge = findViewById(R.id.badge);
    quantityCartFooter = findViewById(R.id.quantity_cart_footer_product_profile);
    CheckOut = findViewById(R.id.checkout_btn_footer_product_profile);
    // dialog
    dialog = new Dialog(this);
    dialog.setContentView(R.layout.dialog_cart);
    quantityCartFooterDialog = dialog.findViewById(R.id.quantity_cart_dialog_cart_product_profile);
    badgeDiaLog = dialog.findViewById(R.id.badge_dialog);
  }
}