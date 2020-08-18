package com.kynguyen.shop_3hkt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {
  private TextView signIn;
  private EditText usenameET, emailET, passwordET, comfirmPassET;
  private Button signUpBtn;
  private ProgressDialog loadingBar;
  private FirebaseAuth mAuth;
  private DatabaseReference mDatabase;
  private User user;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sign_up);
    mapping();

    loadingBar = new ProgressDialog(SignUpActivity.this);

    // sign up create Account
    signUpBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        createAccount();
      }
    });

    // back sign in acctivity
    signIn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
        startActivity(intent);
        finish();
      }
    });

  }

  private void mapping() {
    signIn = (TextView) findViewById(R.id.sign_in_btn);
    usenameET = (EditText) findViewById(R.id.register_username_input);
    emailET = (EditText) findViewById(R.id.register_email_number_input);
    passwordET = (EditText) findViewById(R.id.register_password_input);
    comfirmPassET = (EditText) findViewById(R.id.register_confirm_password_input);
    signUpBtn = (Button) findViewById(R.id.register_btn);
  }

  private void createAccount() {
    final String name = usenameET.getText().toString().trim();
    final String email = emailET.getText().toString().trim();
    final String pass = passwordET.getText().toString().trim();
    String comfirmPass = comfirmPassET.getText().toString().trim();

    if (TextUtils.isEmpty(name)) {
      Toast.makeText(this, "Please write your name...", Toast.LENGTH_SHORT).show();
      return;
    }

    if (TextUtils.isEmpty(email)) {
      Toast.makeText(this, "Please write your phone...", Toast.LENGTH_SHORT).show();
      return;
    }

    if (TextUtils.isEmpty(pass)) {
      Toast.makeText(this, "Please write your password...", Toast.LENGTH_SHORT).show();
      return;
    }

    if (TextUtils.isEmpty(comfirmPass)) {
      Toast.makeText(this, "Please write your password...", Toast.LENGTH_SHORT).show();
      return;
    }

    if (pass.length() < 6) {
      Toast.makeText(this, " Password too short", Toast.LENGTH_SHORT).show();
    }
    mAuth = FirebaseAuth.getInstance();
    if (pass.equals(comfirmPass)) {
      mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
          if (task.isSuccessful()) {
//            loadingBar.setTitle("Create Account");
//            loadingBar.setMessage("Please wait, while we are checking the credentials.");
//            loadingBar.setCanceledOnTouchOutside(false);
//            loadingBar.show();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String Uid = user.getUid();
            String Email =user.getEmail();
//
           HashMap<String, Object> userdata = new HashMap<>();
              userdata.put("email", Email);
              userdata.put("uid", Uid);
              userdata.put("displayName", name);
              userdata.put("phone","");
              userdata.put("image", "");
            HashMap<String, Object> roledata = new HashMap<>();
              roledata.put("admin", false);
              roledata.put("member",true);
                mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child("users").child(Uid).setValue(userdata);
                mDatabase.child("users").child(Uid).child("role").setValue(roledata);


            startActivity(new Intent(SignUpActivity.this, SignInActivity.class));

            Toast.makeText(SignUpActivity.this, "Created Account successfully", Toast.LENGTH_SHORT).show();
          } else {
            Toast.makeText(SignUpActivity.this, "Create Account error", Toast.LENGTH_SHORT).show();
            loadingBar.dismiss();
          }
        }
      });
    }

  }

}