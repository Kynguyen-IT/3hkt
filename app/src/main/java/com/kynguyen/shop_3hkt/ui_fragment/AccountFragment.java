package com.kynguyen.shop_3hkt.ui_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kynguyen.shop_3hkt.HomeActivity;
import com.kynguyen.shop_3hkt.MainActivity;
import com.kynguyen.shop_3hkt.Model.Role;
import com.kynguyen.shop_3hkt.Model.User;
import com.kynguyen.shop_3hkt.Prevalent.Prevalent;
import com.kynguyen.shop_3hkt.R;
import com.kynguyen.shop_3hkt.SettingActivity;
import com.kynguyen.shop_3hkt.SignInActivity;
import com.kynguyen.shop_3hkt.admin.AdminActivity;
import com.kynguyen.shop_3hkt.member.UserInformationActivity;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class AccountFragment extends Fragment {
    private Button loginBtn, logoutBtn;
    private TextView usernameView;
    private CircleImageView profileImageView;
    private RelativeLayout header_account, Admin_account, setting_account;
    private View viewAccount;
    private FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewAccount = inflater.inflate(R.layout.fragment_account, container, false);
        Paper.init(viewAccount.getContext());
        firebaseAuth = FirebaseAuth.getInstance();

        mapping();
        checkUserLogin();


        usernameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(viewAccount.getContext(), UserInformationActivity.class);
                startActivity(intent);
            }
        });


        // login user
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSignIn(v);
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout(v);
//                FirebaseAuth.getInstance().signOut();
//
//                Intent intent = new Intent(viewAccount.getContext(), HomeActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
            }
        });

        Admin_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(viewAccount.getContext(), AdminActivity.class);
                startActivity(intent);
            }
        });

        setting_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(viewAccount.getContext(), SettingActivity.class);
                startActivity(intent);
            }
        });

        return viewAccount;
    }

    private void checkUserLogin() {
        final FirebaseUser userStatus = firebaseAuth.getCurrentUser();
        if (userStatus != null) {
//            Prevalent.setCurrentOnLineUsers(firebaseAuth.getCurrentUser());
            final String Uid = userStatus.getUid();
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
                        usernameView.setText(userdata.getDisplayName());
                        Picasso.get().load(userdata.getPhotoUrl()).placeholder(R.drawable.profile).into(profileImageView);
                        header_account.setPadding(0, 80, 0, 0);
                        loginBtn.setVisibility(View.INVISIBLE);
                        logoutBtn.setVisibility(View.VISIBLE);
                        // check role user
                        if (roleUser.getAdmin() == false) {
                            Admin_account.setVisibility(View.INVISIBLE);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
//            usernameView.setText(Prevalent.currentOnLineUsers.getDisplayName());
//            Picasso.get().load(Prevalent.currentOnLineUsers.getPhotoUrl()).placeholder(R.drawable.profile).into(profileImageView);
//            header_account.setPadding(0, 80, 0, 0);
//            loginBtn.setVisibility(View.INVISIBLE);
//            logoutBtn.setVisibility(View.VISIBLE);
//            // check role user
//            if (Prevalent.roleUser.getAdmin() == false) {
//                Admin_account.setVisibility(View.INVISIBLE);
//            }
        } else {
            logoutBtn.setVisibility(View.INVISIBLE);
            Admin_account.setVisibility(View.INVISIBLE);
        }

//        if (Prevalent.currentOnLineUsers != null) {
//            FirebaseAuth.getInstance().getCurrentUser().reload();
//            usernameView.setText(Prevalent.currentOnLineUsers.getDisplayName());
//            Picasso.get().load(Prevalent.currentOnLineUsers.getPhotoUrl()).placeholder(R.drawable.profile).into(profileImageView);
//            header_account.setPadding(0, 80, 0, 0);
//            loginBtn.setVisibility(View.INVISIBLE);
//            logoutBtn.setVisibility(View.VISIBLE);
//            // check role user
//            if (Prevalent.roleUser.getAdmin() == false) {
//                Admin_account.setVisibility(View.INVISIBLE);
//            }
//        }
    }

    private void mapping() {
        usernameView = viewAccount.findViewById(R.id.user_profile_name);
        profileImageView = viewAccount.findViewById(R.id.user_profile_image);
        header_account = viewAccount.findViewById(R.id.header_account);
        loginBtn = viewAccount.findViewById(R.id.login_btn);
        logoutBtn = viewAccount.findViewById(R.id.logout_btn);
        Admin_account = viewAccount.findViewById(R.id.admin_account);
        setting_account = viewAccount.findViewById(R.id.setting_account);
    }


    // intent to sign
    public void goToSignIn(View v) {
        Intent intent = new Intent(getActivity().getApplication(), SignInActivity.class);
        startActivity(intent);
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        GoogleSignIn.getClient(getActivity(), new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build())
                .signOut().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Intent intent = new Intent(viewAccount.getContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Sign Out Failure", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
