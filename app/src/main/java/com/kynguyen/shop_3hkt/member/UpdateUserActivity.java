package com.kynguyen.shop_3hkt.member;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kynguyen.shop_3hkt.FontAwesome;
import com.kynguyen.shop_3hkt.Prevalent.Prevalent;
import com.kynguyen.shop_3hkt.R;

import java.util.HashMap;

public class UpdateUserActivity extends AppCompatActivity {
  private EditText nameET, EmailET, phoneET, addressET;
  private Button update_btn;
  private DatabaseReference userRef;
  private FontAwesome close_activity;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_update_user);
    FirebaseAuth.getInstance().getCurrentUser().reload();
    mapping();
    setTextUiUser();
    close_activity.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });
    update_btn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        CheckTextInput();

      }
    });
  }

  private void CheckTextInput() {
    if (TextUtils.isEmpty(nameET.getText().toString())){
      Toast.makeText(this, "Name is madatory", Toast.LENGTH_SHORT).show();
    } else if (TextUtils.isEmpty(phoneET.getText().toString())){
      Toast.makeText(this, "Phone is madatory", Toast.LENGTH_SHORT).show();
    } else if (TextUtils.isEmpty(addressET.getText().toString())){
      Toast.makeText(this, "address is madatory", Toast.LENGTH_SHORT).show();
    } else {
      update_user_database();
    }
  }

  private void update_user_database() {
    String name = nameET.getText().toString();
    String phone = phoneET.getText().toString();
    String address = addressET.getText().toString();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    HashMap<String,Object> userdata = new  HashMap<>();
    userdata.put("displayName", name);
    userdata.put("phone",phone);
    userdata.put("address", address);
    userRef = FirebaseDatabase.getInstance().getReference();
    userRef.child("users").child(uid).updateChildren(userdata);
    Toast.makeText(this, "update successfully", Toast.LENGTH_SHORT).show();
    Intent intent = new Intent(UpdateUserActivity.this, UserInformationActivity.class);
    startActivity(intent);
    finish();
  }

  private void setTextUiUser() {
    nameET.setText(Prevalent.currentOnLineUsers.getDisplayName());
    EmailET.setText(Prevalent.currentOnLineUsers.getEmail());
    phoneET.setText(Prevalent.currentOnLineUsers.getPhone());
    addressET.setText(Prevalent.currentOnLineUsers.getAddress());
  }

  private void mapping() {
    nameET = findViewById(R.id.name_update_user);
    EmailET = findViewById(R.id.email_update_user);
    phoneET = findViewById(R.id.phone_update_user);
    addressET = findViewById(R.id.address_update_user);
    update_btn = findViewById(R.id.update_user);
    close_activity = findViewById(R.id.close_update_user);
  }
}
