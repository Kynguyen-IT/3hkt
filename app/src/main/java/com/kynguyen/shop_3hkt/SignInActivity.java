package com.kynguyen.shop_3hkt;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import io.paperdb.Paper;

public class SignInActivity extends AppCompatActivity {
  private TextView signUp_btn;
  private EditText emailET, passET;
  private Button signInBtn;
  private String parentDbName = "User";
  private ProgressDialog loadingBar;
  private CheckBox cbRememberMe;
  private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
  private SignInButton login_goolge_btn;
  private int RC_SIGN_IN = 100;
  GoogleSignInClient mGoogleSignInClient;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_signin);
    mapping();

    mAuth = FirebaseAuth.getInstance();
    Paper.init(this);
    // intent sign up
    signUp_btn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
        startActivity(intent);
        finish();
      }
    });

    // sign In
    signInBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        loginUser();
      }
    });

    // Configure Google Sign In
    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build();
      mGoogleSignInClient = GoogleSignIn.getClient(this,gso);

    // sign in google
    login_goolge_btn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
      }
    });

  }

  private void mapping() {
    signUp_btn = (TextView) findViewById(R.id.sign_up_btn);
    emailET = (EditText) findViewById(R.id.login_email_input);
    passET = (EditText) findViewById(R.id.login_password_input);
    signInBtn = (Button) findViewById(R.id.login_btn);
    loadingBar = new ProgressDialog(this);
    cbRememberMe = (CheckBox) findViewById(R.id.remember_me_checkbox);
    login_goolge_btn = findViewById(R.id.login_google);
  }

  private void loginUser() {
    String email = emailET.getText().toString().trim();
    String password = passET.getText().toString().trim();

    if (TextUtils.isEmpty(email)){
      Toast.makeText(this,"Please write your Phone...",Toast.LENGTH_SHORT).show();
    }

    if (TextUtils.isEmpty(password)){
      Toast.makeText(this,"Please write your Password...",Toast.LENGTH_SHORT).show();
    }
    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
      @Override
      public void onComplete(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()){
          FirebaseUser user = mAuth.getCurrentUser();
          loadingBar.setTitle("Login");
          loadingBar.setMessage("Please wait, while we are checking the credentials.");
          loadingBar.setCanceledOnTouchOutside(false);
          loadingBar.show();
          Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
          intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
          startActivity(intent);
          Toast.makeText(SignInActivity.this,"login successfully",Toast.LENGTH_SHORT).show();
          finish();
        } else {
          Toast.makeText(SignInActivity.this,"login error",Toast.LENGTH_SHORT).show();
          loadingBar.dismiss();
        }
      }
    });

  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
    if (requestCode == RC_SIGN_IN) {
      Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
      try {
        // Google Sign In was successful, authenticate with Firebase
        GoogleSignInAccount account = task.getResult(ApiException.class);
        firebaseAuthWithGoogle(account.getIdToken());
      } catch (ApiException e) {
        // Google Sign In failed, update UI appropriately
        Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

      }
    }
  }

  private void firebaseAuthWithGoogle(String idToken) {
    AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
    mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
              @Override
              public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                  // Sign in success, update UI with the signed-in user's information
                  FirebaseUser user = mAuth.getCurrentUser();
                    String Uid = user.getUid();
                    String Email =user.getEmail();
                    String name = user.getDisplayName();
                    String image = user.getPhotoUrl().toString();

                    // add user to firebase database
                    HashMap<String, Object> userdata = new HashMap<>();
                    userdata.put("email", Email);
                    userdata.put("uid", Uid);
                    userdata.put("displayName", name);
                    userdata.put("phone","");
                    userdata.put("photoUrl", image);
                    HashMap<String, Object> roledata = new HashMap<>();
                    roledata.put("admin", true);
                    roledata.put("member",true);
                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    mDatabase.child("users").child(Uid).setValue(userdata).addOnFailureListener(new OnFailureListener() {
                      @Override
                      public void onFailure(@NonNull Exception e) {
                        Log.d("data users", e.getLocalizedMessage());
                      }
                    })  ;
                    mDatabase.child("users").child(Uid).child("role").setValue(roledata).addOnFailureListener(new OnFailureListener() {
                      @Override
                      public void onFailure(@NonNull Exception e) {
                        Log.d("data role", e.getLocalizedMessage());
                      }
                    });

                    // intent to home activity
                      FirebaseAuth.getInstance().getCurrentUser().reload();
                      Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                      startActivity(intent);
                      finish();
                } else {
                  // If sign in fails, display a message to the user.
                  Toast.makeText(SignInActivity.this, "signInWithCredential:failure", Toast.LENGTH_SHORT).show();
                }

              }
            }).addOnFailureListener(new OnFailureListener() {
      @Override
      public void onFailure(@NonNull Exception e) {
        Toast.makeText(SignInActivity.this, "error", Toast.LENGTH_SHORT).show();
      }
    });
  }




}
