package com.kynguyen.shop_3hkt;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kynguyen.shop_3hkt.Model.Role;
import com.kynguyen.shop_3hkt.Model.User;
import com.kynguyen.shop_3hkt.Prevalent.Prevalent;

public class MainActivity extends AppCompatActivity {
  private Animation top_anim, bottom_anim;
  private ImageView logo;
  private TextView title_view, description_view;
  private static int SPLASH_TIME_OUT = 3000;
  private String parentDbName = "User";
  private ProgressBar progressBar;
  private FirebaseAuth firebaseAuth;
  private String Uid;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
    setContentView(R.layout.activity_main);
    mapping();


    logo.setAnimation(top_anim);
    title_view.setAnimation(bottom_anim);
    description_view.setAnimation(bottom_anim);
    progressBar.setVisibility(View.VISIBLE);

    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
      }
    }, SPLASH_TIME_OUT);
  }


  @Override
  protected void onStart() {
    checkUserStatus();
    super.onStart();
  }


  private void checkUserStatus() {
    final FirebaseUser userStatus = firebaseAuth.getCurrentUser();
//    firebaseAuth.addAuthStateListener();
    if (userStatus != null) {
      Uid = userStatus.getUid();
      FirebaseAuth.getInstance().getCurrentUser().reload();
      DatabaseReference userRef = FirebaseDatabase.getInstance().getReference();
      userRef.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
          if (dataSnapshot.exists()) {
            User userdata = dataSnapshot.child("users").child(Uid).getValue(User.class);
            Prevalent.currentOnLineUsers = userdata;
            Role roleUser = dataSnapshot.child("users").child(Uid).child("role").getValue(Role.class);
            Prevalent.roleUser = roleUser;
          }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
      });

    }
  }

  private void mapping() {
    top_anim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
    bottom_anim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
    logo = (ImageView) findViewById(R.id.logo_app);
    title_view = (TextView) findViewById(R.id.title_main);
    description_view = (TextView) findViewById(R.id.description_main);
    progressBar = (ProgressBar) findViewById(R.id.loading_login);
    firebaseAuth = FirebaseAuth.getInstance();
  }
}
