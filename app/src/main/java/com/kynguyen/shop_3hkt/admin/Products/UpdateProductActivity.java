package com.kynguyen.shop_3hkt.admin.Products;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kynguyen.shop_3hkt.MapActivity;
import com.kynguyen.shop_3hkt.Model.Categories;
import com.kynguyen.shop_3hkt.Model.Products;
import com.kynguyen.shop_3hkt.R;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static java.lang.String.valueOf;

public class UpdateProductActivity extends AppCompatActivity {
    private String pid;
    private EditText nameET, descriptionET, priceET, addressET;
    private Button updateProduct;
    private TextView updateImage;
    private CircleImageView imageProduct;
    private String downloadImagesURL, lat, lng;
    private Uri ImagesUri;
    private Spinner spinner_items;
    private DatabaseReference ref;
    private StorageReference productImageRef;
    private SharedPreferences sharedPreferences;
    private String getIdCate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);
        mapping();

        getProductAndUpdateUi();

        saveData(nameET, "name");
        saveData(descriptionET, "desc");
        saveData(priceET, "price");

        addressET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateProductActivity.this, MapActivity.class);
                startActivity(intent);
                finish();
            }
        });

        addItemsSpinner();
        // add product
        updateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAllItemProduct();
            }
        });
        // add  Images;
        updateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity(ImagesUri).start(UpdateProductActivity.this);
            }
        });
    }

    private void getProductAndUpdateUi() {
        ref.child("products").child(pid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Products product = dataSnapshot.getValue(Products.class);
                    nameET.setText(product.getName());
                    descriptionET.setText(product.getDescription());
                    priceET.setText(product.getPrice());
                    addressET.setText(product.getAddress());
                    Picasso.get().load(product.getImage()).fit().into(imageProduct);
//                    imageProduct.setImageURI(Uri.parse(product.getImage()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("AAA", "Failed to read value.", databaseError.toException());
            }
        });
    }

    private void saveData(EditText editText, final String key) {
        editText.setText(sharedPreferences.getString(key, ""));
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                sharedPreferences.edit().putString(key, s.toString()).commit();

            }
        });
    }

    private void addAllItemProduct() {
        String name = nameET.getText().toString().trim();
        String description = descriptionET.getText().toString().trim();
        String price = priceET.getText().toString().trim();
        String address = addressET.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(UpdateProductActivity.this, "Please write your Name...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(description)) {
            Toast.makeText(UpdateProductActivity.this, "Please write your Description...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(price)) {
            Toast.makeText(UpdateProductActivity.this, "Please write your Price...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(address)) {
            Toast.makeText(UpdateProductActivity.this, "Please write your Address...", Toast.LENGTH_SHORT).show()
            ;
        } else {
            StoreProductInformation();
        }
    }

    private void StoreProductInformation() {
        final ProgressDialog loadingBar = new ProgressDialog(this);
        loadingBar.setTitle("Updating Product");
        loadingBar.setMessage("Dear Admin, Please wait while we are adding the credentials.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
        String getImage = sharedPreferences.getString("image", String.valueOf(ImagesUri));

        final StorageReference filePath = productImageRef.child(String.valueOf(Uri.parse(getImage).getLastPathSegment()));

        final UploadTask uploadTask = filePath.putFile(Uri.parse(getImage));

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(UpdateProductActivity.this, "Error:" + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(UpdateProductActivity.this, "Product Images uploaded successfully...", Toast.LENGTH_SHORT).show();
                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        downloadImagesURL = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            downloadImagesURL = task.getResult().toString();
                            Toast.makeText(UpdateProductActivity.this, "got the Product image URL Successfully..", Toast.LENGTH_SHORT).show();
                            SaveProductInfoToDatabase();
                        }
                    }
                });
            }
        });
    }

    private void SaveProductInfoToDatabase() {
        HashMap<String, Object> productData = new HashMap<>();
        productData.put("pid", pid);
        productData.put("name", nameET.getText().toString());
        productData.put("address", addressET.getText().toString());
        productData.put("description", descriptionET.getText().toString());
        productData.put("price", priceET.getText().toString());
        productData.put("idCategory", getIdCate);
        productData.put("lat", lat);
        productData.put("lng", lng);
        productData.put("image", downloadImagesURL);

        ref.child("products").child(pid).setValue(productData);

        sharedPreferences.edit().clear().apply();
        startActivity(new Intent(UpdateProductActivity.this, AdminProductsActivity.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            ImagesUri = result.getUri();

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("image", valueOf(ImagesUri));
            editor.apply();

            imageProduct.setImageURI(Uri.parse(sharedPreferences.getString("image", String.valueOf(ImagesUri))));
            imageProduct.invalidate();

        } else {
            Toast.makeText(this, "Error, Try Again.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(UpdateProductActivity.this, AddProduct.class));
            finish();
        }
    }

    private void addItemsSpinner() {
        ref.child("categories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final ArrayList<Categories> cate = new ArrayList<Categories>();
                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    String idCate = areaSnapshot.getKey();
                    Categories categories = dataSnapshot.child(idCate).getValue(Categories.class);
                    cate.add(new Categories(idCate, categories.getName()));
                }

                ArrayAdapter<Categories> arrayAdapter = new ArrayAdapter<Categories>(UpdateProductActivity.this, R.layout.support_simple_spinner_dropdown_item, cate);
                arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                spinner_items.setAdapter(arrayAdapter);

                spinner_items.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Categories categories = (Categories) parent.getItemAtPosition(position);
                        getIdCate = (String) categories.getId();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void mapping() {
        pid = getIntent().getStringExtra("pid");
        lat = getIntent().getStringExtra("lat");
        lng = getIntent().getStringExtra("lng");

        productImageRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        ref = FirebaseDatabase.getInstance().getReference();

        nameET = findViewById(R.id.name_product_update);
        descriptionET = findViewById(R.id.description_product_update);
        priceET = findViewById(R.id.price_product_update);
        updateProduct = findViewById(R.id.update_product);
        updateImage = findViewById(R.id.update_image_product);
        imageProduct = findViewById(R.id.image_product_update);
        addressET = findViewById(R.id.address_product_update);
        spinner_items = findViewById(R.id.dynamic_spinner_update);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }
}