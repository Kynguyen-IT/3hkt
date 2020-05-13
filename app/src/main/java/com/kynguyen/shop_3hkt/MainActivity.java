package com.kynguyen.shop_3hkt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
  private Animation top_anim , bottom_anim;
  private ImageView logo;
  private TextView title_view, description_view;
  private static int SPLASH_TIME_OUT = 3000;
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

    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
      }
    },SPLASH_TIME_OUT);
  }

  private void mapping() {
    top_anim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
    bottom_anim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
    logo = (ImageView) findViewById(R.id.logo_app);
    title_view = (TextView) findViewById(R.id.title_main);
    description_view = (TextView) findViewById(R.id.description_main);
  }
}
