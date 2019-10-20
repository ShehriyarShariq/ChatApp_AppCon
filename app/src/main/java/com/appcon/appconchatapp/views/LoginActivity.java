package com.appcon.appconchatapp.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.appcon.appconchatapp.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences sharedPreferences = getSharedPreferences("auth", MODE_PRIVATE);
        String phoneNum = sharedPreferences.getString("phoneNum", "none");

        if(phoneNum.equals("none")){

        } else {}

    }
}
