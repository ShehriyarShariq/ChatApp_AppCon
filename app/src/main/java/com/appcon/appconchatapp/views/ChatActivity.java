package com.appcon.appconchatapp.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.provider.FontRequest;
import androidx.databinding.DataBindingUtil;
import androidx.emoji.text.EmojiCompat;
import androidx.emoji.text.FontRequestEmojiCompatConfig;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.appcon.appconchatapp.R;
import com.appcon.appconchatapp.databinding.ActivityChatBinding;
import com.appcon.appconchatapp.viewmodels.ChatActivityViewModel;

public class ChatActivity extends AppCompatActivity {


    ActivityChatBinding binding;
    ChatActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FontRequest fontRequest = new FontRequest(
                "com.example.fontprovider",
                "com.example",
                "emoji compat Font Query",
                R.array.com_google_android_gms_fonts_certs);
        EmojiCompat.Config config = new FontRequestEmojiCompatConfig(this, fontRequest);
        EmojiCompat.init(config);

        setContentView(R.layout.activity_chat);

        viewModel = ViewModelProviders.of(this).get(ChatActivityViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat);

        // Open profile
        binding.profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // Set Convsation Name and User current status (Online or Last Seen At @Time)
        binding.name.setText("Friend Name");
        binding.status.setText("Online");

        // Opem game menu
        binding.gamesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // Initiate Video Call
        binding.videoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // Initiate Audio Call
        binding.callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // Open more options menu
        binding.moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // Get out of selection
        binding.msgSelectionBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // Decrypt encrypted message
        binding.selectedDecryptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // Delete message from local storage
        binding.selectedDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // Open selected msg more options menu
        binding.selectedMoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // Open attachments menu
        binding.addMoreAltBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // Check if text input has focus to change view
        binding.textInp.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    binding.addMoreAltBtn.setVisibility(View.GONE);
                    binding.addImageBtn.setVisibility(View.GONE);
                } else {
                    binding.addMoreAltBtn.setVisibility(View.VISIBLE);
                    binding.addImageBtn.setVisibility(View.VISIBLE);
                }
            }
        });

        binding.textInpFocusNone.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    if(binding.textInp.isFocused()){
                        Rect outRect = new Rect();
                        binding.textInp.getGlobalVisibleRect(outRect);
                        if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                            binding.textInp.clearFocus();
                            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        }
                    }
                }
                return false;
            }
        });





    }
}
