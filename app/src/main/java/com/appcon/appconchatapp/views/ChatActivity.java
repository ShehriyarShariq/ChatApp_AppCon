package com.appcon.appconchatapp.views;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.provider.FontRequest;
import androidx.databinding.DataBindingUtil;
import androidx.emoji.text.EmojiCompat;
import androidx.emoji.text.FontRequestEmojiCompatConfig;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.appcon.appconchatapp.R;
import com.appcon.appconchatapp.adapters.ChatMessagesListAdapter;
import com.appcon.appconchatapp.databinding.ActivityChatBinding;
import com.appcon.appconchatapp.model.Message;
import com.appcon.appconchatapp.model.TextMessage;
import com.appcon.appconchatapp.viewmodels.ChatActivityViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.EmojiPopup;
import com.vanniktech.emoji.google.GoogleEmojiProvider;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    ActivityChatBinding binding;
    ChatActivityViewModel viewModel;

    EmojiPopup emojiPopup;

    ArrayList<Message> messages;
    ChatMessagesListAdapter chatMessagesListAdapter;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EmojiManager.install(new GoogleEmojiProvider());

        firebaseAuth = FirebaseAuth.getInstance();

        viewModel = ViewModelProviders.of(this).get(ChatActivityViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat);

        emojiPopup = EmojiPopup.Builder.fromRootView(binding.getRoot()).build(binding.textInp);

        messages = new ArrayList<>();
        chatMessagesListAdapter = new ChatMessagesListAdapter(this, messages);

        binding.chatList.setHasFixedSize(true);
        LinearLayoutManager chatMessagesListLinearLayoutManager = new LinearLayoutManager(this);
        chatMessagesListLinearLayoutManager.setItemPrefetchEnabled(true);
        chatMessagesListLinearLayoutManager.setStackFromEnd(true);
        binding.chatList.setLayoutManager(chatMessagesListLinearLayoutManager);

        binding.chatList.setAdapter(chatMessagesListAdapter);

        // Open profile
        binding.profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // Set Convsation Name and User current status (Online or Last Seen At @Time)
        binding.name.setText("Friend Name");
        binding.status.setText("Online");

        // Open game menu
        binding.gamesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.gameMenu.getVisibility() != View.VISIBLE){
                    binding.gameMenu.setVisibility(View.VISIBLE);
                } else {
                    binding.gameMenu.setVisibility(View.GONE);
                }
            }
        });

        // Initiate Video Call
        binding.videoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Coming soon dialog

                TextMessage msg = new TextMessage("messageID", "user02", "Ali", firebaseAuth.getCurrentUser().getPhoneNumber(), "10:03 pm", binding.textInp.getText().toString());
                messages.add(msg);
                chatMessagesListAdapter.refreshMessagesList(messages);
                binding.textInp.setText("");

                clearTextInpFocus(v);
                binding.chatList.scrollToPosition(messages.size() - 1);
            }
        });

        // Initiate Audio Call
        binding.callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Coming soon dialog

                TextMessage msg = new TextMessage("messageID", "user01", "Ali", firebaseAuth.getCurrentUser().getPhoneNumber(), "10:03 pm", binding.textInp.getText().toString());
                messages.add(msg);
                chatMessagesListAdapter.refreshMessagesList(messages);
                binding.textInp.setText("");

                clearTextInpFocus(v);
                binding.chatList.scrollToPosition(messages.size() - 1);
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
                if(binding.additionalMenu.getVisibility() != View.VISIBLE){
                    binding.additionalMenu.setVisibility(View.VISIBLE);
                } else {
                    binding.additionalMenu.setVisibility(View.GONE);
                }
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


        binding.textInp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 0){ // Audio btn
                    binding.audioOrSendBtnImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_mic));
                } else { // Send btn
                    binding.audioOrSendBtnImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_send));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

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

        // Popup emoji keyboard
        binding.addEmojiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emojiPopup.toggle();
            }
        });

        // Send audio or msg
        binding.audioOrSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.textInp.getText().length() == 0){ // Audio Btn

                } else { // Send Btn
                    TextMessage msg = new TextMessage("messageID", firebaseAuth.getUid(), firebaseAuth.getCurrentUser().getDisplayName(), firebaseAuth.getCurrentUser().getPhoneNumber(), "10:03 pm", binding.textInp.getText().toString());
                    messages.add(msg);
                    chatMessagesListAdapter.refreshMessagesList(messages);
                    binding.textInp.setText("");

                    clearTextInpFocus(v);

                    binding.chatList.scrollToPosition(messages.size() - 1);
                }
            }
        });
    }

    private void clearTextInpFocus(View v){
        binding.textInp.clearFocus();
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}
