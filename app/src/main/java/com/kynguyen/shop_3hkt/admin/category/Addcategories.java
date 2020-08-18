package com.kynguyen.shop_3hkt.admin.category;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kynguyen.shop_3hkt.R;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Addcategories extends AppCompatActivity {
  private EditText nameEt;
  private Button add;
  private DatabaseReference ref;
  private TextView addImage;
  private String checker, downloadImagesURL;
  private CircleImageView imageCatgory;
  private Uri ImagesUri;
  private StorageReference categoryImageRef;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_addcategories);
    categoryImageRef = FirebaseStorage.getInstance().getReference().child("Category Images");
    ref = FirebaseDatabase.getInstance().getReference();
    mapping();
    add.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        addCategory();
      }
    });

    addImage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        checker = "clicked";
        CropImage.activity(ImagesUri)
            .start(Addcategories.this);
      }
    });
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE  &&  resultCode==RESULT_OK  &&  data!=null)
    {
      CropImage.ActivityResult result = CropImage.getActivityResult(data);
      ImagesUri = result.getUri();
      imageCatgory.setImageURI(ImagesUri);
    }
    else
    {
      Toast.makeText(this, "Error, Try Again.", Toast.LENGTH_SHORT).show();

      startActivity(new Intent(Addcategories.this, Addcategories.class));
      finish();
    }
  }

  private void addCategory() {
    if (TextUtils.isEmpty(nameEt.getText().toString())){
      Toast.makeText(this, "Name is madatory", Toast.LENGTH_SHORT).show();
    } else {
      update();
    }
  }

  private void update() {
    final ProgressDialog loadingBar = new ProgressDialog(this);
    loadingBar.setTitle("Add New Category");
    loadingBar.setMessage("Dear Admin, Please wait while we are adding the credentials.");
    loadingBar.setCanceledOnTouchOutside(false);
    loadingBar.show();

    final StorageReference filePath = categoryImageRef.child(ImagesUri.getLastPathSegment());

    final UploadTask uploadTask = filePath.putFile(ImagesUri);

    uploadTask.addOnFailureListener(new OnFailureListener() {
      @Override
      public void onFailure(@NonNull Exception e) {
        String message = e.toString();
        Toast.makeText(Addcategories.this, "Error:" + message, Toast.LENGTH_SHORT).show();
        loadingBar.dismiss();
      }
    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
      @Override
      public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
        Toast.makeText(Addcategories.this, "Product Images uploaded successfully...", Toast.LENGTH_SHORT).show();
        Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
          @Override
          public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
            if (!task.isSuccessful()){
              throw task.getException();
            }
            downloadImagesURL = filePath.getDownloadUrl().toString();
            return filePath.getDownloadUrl();
          }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
          @Override
          public void onComplete(@NonNull Task<Uri> task) {
            if(task.isSuccessful()){
              downloadImagesURL = task.getResult().toString();
              Toast.makeText(Addcategories.this, "got the Product image URL Successfully..", Toast.LENGTH_SHORT).show();
              SaveCateInfotoDatabase();
            }
          }
        });
      }
    });
  }

  private void SaveCateInfotoDatabase() {
    String name = nameEt.getText().toString();
    String id = FirebaseDatabase.getInstance().getReference().child("categories").push().getKey();
    HashMap<String,Object> catedata = new HashMap<>();
    catedata.put("name", name);
    catedata.put("id", id);
    catedata.put("image",downloadImagesURL);
    ref = FirebaseDatabase.getInstance().getReference().child("categories");
    ref.child(id).setValue(catedata);
    Toast.makeText(this, "Add category successfully", Toast.LENGTH_SHORT).show();
    finish();
  }

  private void mapping() {
    nameEt = findViewById(R.id.name_category_ip);
    add = findViewById(R.id.add_category_btn);
    addImage = findViewById(R.id.add_image_category);
    imageCatgory = findViewById(R.id.image_category_change);
  }
}
