package com.kynguyen.shop_3hkt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.kynguyen.shop_3hkt.member.UserInformationActivity;

public class SettingActivity extends AppCompatActivity {
    private FontAwesome close_Setting;
    private LinearLayout information_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mapping();
        close_Setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        information_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this, UserInformationActivity.class));
            }
        });
    }

    private void mapping() {
        close_Setting = findViewById(R.id.close_setting);
        information_user = findViewById(R.id.information_user_settings);
    }
}
