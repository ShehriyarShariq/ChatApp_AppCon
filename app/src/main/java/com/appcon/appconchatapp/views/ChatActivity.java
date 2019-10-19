package com.appcon.appconchatapp.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.appcon.appconchatapp.R;
import com.appcon.appconchatapp.databinding.ActivityChatBinding;

public class ChatActivity extends AppCompatActivity {


    ActivityChatBinding activityChatBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


    }
}
