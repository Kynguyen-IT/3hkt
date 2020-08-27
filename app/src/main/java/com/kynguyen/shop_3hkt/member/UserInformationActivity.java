package com.kynguyen.shop_3hkt.member;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.kynguyen.shop_3hkt.FontAwesome;
import com.kynguyen.shop_3hkt.Model.User;
import com.kynguyen.shop_3hkt.Prevalent.Prevalent;
import com.kynguyen.shop_3hkt.R;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserInformationActivity extends AppCompatActivity {
    private FontAwesome close_user_information, change_avatar;
    private CircleImageView avatar_user;
    private TextView nameTV, emailTV, phoneTV, addressTV;
    private Button updateBtn;
    private String checker;
    private Uri photoUrl;
    private StorageReference storageProfilePictureRef;
    private StorageTask uploadTask;
    private String myUrl = "";
    private DatabaseReference refuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);
        mapping();

        storageProfilePictureRef = FirebaseStorage.getInstance().getReference().child("Profile pictures");
        // set User ui
        setUserUi();

        // back intent account fragment
        close_user_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // back intent update user
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserInformationActivity.this, UpdateUserActivity.class);
                startActivity(intent);
                finish();
            }
        });

        avatar_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker = "clicked";
                CropImage.activity(photoUrl)
                        .setAspectRatio(1, 1)
                        .start(UserInformationActivity.this);
            }
        });


        change_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
    }

    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Please wait, while we are updating your account information");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        if (photoUrl != null) {
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            final String Uid = user.getUid();
            final StorageReference fileRef = storageProfilePictureRef.child(Uid + "jpg");
            uploadTask = fileRef.putFile(photoUrl);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUrl = task.getResult();
                        myUrl = downloadUrl.toString();
                        progressDialog.dismiss();
                        DatabaseReference user = FirebaseDatabase.getInstance().getReference().child("users").child(Uid);
                        user.child("photoUrl").setValue(myUrl);

                        Toast.makeText(UserInformationActivity.this, "Profile info update successfully", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().getCurrentUser().reload();
                        finish();
                    } else {
                        Toast.makeText(UserInformationActivity.this, "Profile info update error", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            });

        } else {
            Toast.makeText(this, "image is not selected", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            photoUrl = result.getUri();
            avatar_user.setImageURI(photoUrl);
        } else {
            Toast.makeText(this, "Error, Try Again.", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(UserInformationActivity.this, UserInformationActivity.class));
            finish();
        }
    }

    private void setUserUi() {
        refuser = FirebaseDatabase.getInstance().getReference().child("users");
        refuser.child(Prevalent.currentOnLineUsers.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    User user = dataSnapshot.getValue(User.class);
                    Picasso.get().load(user.getPhotoUrl()).placeholder(R.drawable.profile).into(avatar_user);
                    nameTV.setText(user.getDisplayName());
                    emailTV.setText(user.getEmail());
                    phoneTV.setText(user.getPhone());
                    addressTV.setText(user.getAddress());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void mapping() {
        close_user_information = findViewById(R.id.close_user_information);
        avatar_user = findViewById(R.id.user_profile_image);
        // text view
        nameTV = findViewById(R.id.display_name_user_information);
        emailTV = findViewById(R.id.email_user_information);
        phoneTV = findViewById(R.id.phone_user_information);
        addressTV = findViewById(R.id.address_user_information);
        // button
        updateBtn = findViewById(R.id.edit_user);
        change_avatar = findViewById(R.id.change_avatar_user_information);
    }
}
