package com.appcon.appconchatapp.views;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.provider.FontRequest;
import androidx.databinding.DataBindingUtil;
import androidx.emoji.text.EmojiCompat;
import androidx.emoji.text.FontRequestEmojiCompatConfig;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.appcon.appconchatapp.R;
import com.appcon.appconchatapp.adapters.ChatMessagesListAdapter;
import com.appcon.appconchatapp.databinding.ActivityChatBinding;
import com.appcon.appconchatapp.model.AudioMessage;
import com.appcon.appconchatapp.model.Message;
import com.appcon.appconchatapp.model.TextMessage;
import com.appcon.appconchatapp.viewmodels.ChatActivityViewModel;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.collection.LLRBNode;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.EmojiPopup;
import com.vanniktech.emoji.google.GoogleEmojiProvider;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private static final int RECORD_AUDIO_PERMISSION_REQUEST = 200;
    private static final int IMAGE_PICKER_REQUEST = 001;
    private static final int ATTACH_DOCS_REQUEST = 010;
    private static final int ATTACH_AUDIO_REQUEST = 011;

    ActivityChatBinding binding;
    ChatActivityViewModel viewModel;

    EmojiPopup emojiPopup;

    ArrayList<Message> messages;
    ChatMessagesListAdapter chatMessagesListAdapter;

    FirebaseAuth firebaseAuth;
    StorageReference firebaseStorage;

    LiveData<Boolean> startAudioRecord;

    boolean lastAudioRecState = false;
    boolean isAudioBtnLongPressed = false;
    boolean isAudioPlaying = false;
    boolean isAudioPaused = false;

    boolean isSendAudio = false;
    boolean isSendImage = false;
    boolean isSendDoc = false;

    private MediaRecorder recorder = null;
    private MediaPlayer player = null;

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    String fileName;

    private Handler mHandler;

    LiveData<HashMap<String, String>> downloadUrl;

    ArrayList<Message> pendingMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EmojiManager.install(new GoogleEmojiProvider());

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance().getReference();

        viewModel = ViewModelProviders.of(this).get(ChatActivityViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat);

        emojiPopup = EmojiPopup.Builder.fromRootView(binding.getRoot()).build(binding.textInp);

        ImagePickerActivity.clearCache(this);

        pendingMessages = new ArrayList<>();
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
            }
        });

        // Initiate Audio Call
        binding.callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Coming soon dialog
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

        binding.attachmentsDocsBtnLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("application/pdf");
                intent.addCategory(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, ATTACH_DOCS_REQUEST);
            }
        });

        binding.attachmentsImgCameraBtnLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withActivity(ChatActivity.this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            launchCameraIntent();
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
            }
        });

        binding.attachmentsImgGalleryBtnLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withActivity(ChatActivity.this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            launchGalleryIntent();
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
            }
        });

        binding.attachmentsAudioBtnLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("audio/wav");
                intent.addCategory(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, ATTACH_AUDIO_REQUEST);
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

        binding.addImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withActivity(ChatActivity.this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            showImagePickerOptions();
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
            }
        });

        // Send audio or msg
        binding.audioOrSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.textInp.getText().length() > 0){ // Send btn
                    TextMessage msg = new TextMessage("messageID", firebaseAuth.getUid(), firebaseAuth.getCurrentUser().getDisplayName(), firebaseAuth.getCurrentUser().getPhoneNumber(), "10:03 pm", binding.textInp.getText().toString());
                    messages.add(msg);
                    chatMessagesListAdapter.refreshMessagesList(messages);
                    binding.textInp.setText("");

                    clearTextInpFocus(v);

                    binding.chatList.scrollToPosition(messages.size() - 1);
                } else if(isSendAudio){ // Send Audio Btn

                    File file = new File(fileName);
                    AudioMessage audioMessage = new AudioMessage(String.valueOf((Math.random() * 1000000)), firebaseAuth.getCurrentUser().getUid(), firebaseAuth.getCurrentUser().getDisplayName(), firebaseAuth.getCurrentUser().getPhoneNumber(), "time", "url", getFormattedAudioDuration(player.getDuration() / 1000), file.length());
                    pendingMessages.add(audioMessage);

                    isSendAudio = false;
                    binding.audioOrSendBtnImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_mic));
                    binding.audioPlaybackBar.setVisibility(View.GONE);
                    binding.defaultTxtInpBar.setVisibility(View.VISIBLE);
                    fileName = null;
                    isAudioPlaying = false;
                    isAudioPaused = false;
                    binding.audioLength.setText("00:00");
                    binding.audioSeekbar.setProgress(0);
                    binding.playbackAudioBtnImg.setImageResource(R.drawable.ic_play);

                } else if(isSendImage){
                    isSendImage = false;
                    binding.selecedImgPrevLayout.setVisibility(View.GONE);
                    binding.defaultBottomBar.setVisibility(View.VISIBLE);
                    binding.defaultTxtInpBar.setVisibility(View.VISIBLE);

                    String imgTxtMsg = binding.imgTextInp.getText().toString();
                    binding.imgTextInp.setText("");

                    binding.selectedImg.setImageURI(null);
                    binding.audioOrSendBtnImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_mic));



                } else { // Audio Btn
                    Toast.makeText(ChatActivity.this, "Hold down to record audio", Toast.LENGTH_SHORT).show();
                }
            }
        });

        downloadUrl = viewModel.getDownloadUrl();
        downloadUrl.observe(this, new Observer<HashMap<String, String>>() {
            @Override
            public void onChanged(HashMap<String, String> downloadUrls) {
                if(!downloadUrls.isEmpty()){

                }
            }
        });

        startAudioRecord = viewModel.getStartAudioRecord();
        startAudioRecord.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean started) {
                if(started){
                    lastAudioRecState = true;
                    binding.audioOrSendBtn.setCardBackgroundColor(getResources().getColor(R.color.chat_list_item_delete));

                    Toast.makeText(ChatActivity.this, "Recording...", Toast.LENGTH_SHORT).show();
                    startRecording();
                } else if(lastAudioRecState) {
                    lastAudioRecState = false;
                    binding.audioOrSendBtn.setCardBackgroundColor(getResources().getColor(R.color.chat_list_item_select));

                    stopRecording();

                    binding.defaultTxtInpBar.setVisibility(View.GONE);
                    binding.audioPlaybackBar.setVisibility(View.VISIBLE);
                    binding.audioOrSendBtnImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_send));
                }
            }
        });

        binding.audioOrSendBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(!isSendAudio){
                    isAudioBtnLongPressed = true;

                    ActivityCompat.requestPermissions(ChatActivity.this, permissions, RECORD_AUDIO_PERMISSION_REQUEST);

                    if(ContextCompat.checkSelfPermission(ChatActivity.this, permissions[0]) == PackageManager.PERMISSION_GRANTED){
                        viewModel.startAudioRecord();

                        // Record to the external cache directory for visibility
                        fileName = getExternalCacheDir().getAbsolutePath();
                        fileName += "/audio" + (new Date()).getTime() + "_" + ((int) (Math.random() * 1000)) + ".3gp";
                    }
                }
                return true;
            }
        });

        binding.audioOrSendBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    if(isAudioBtnLongPressed && !isSendAudio){
                        isAudioBtnLongPressed = false;
                        viewModel.stopAudioRecord();
                        isSendAudio = true;
                    }
                }

                return false;
            }
        });

        binding.discardAudioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSendAudio = false;
                binding.audioOrSendBtnImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_mic));
                binding.audioPlaybackBar.setVisibility(View.GONE);
                binding.defaultTxtInpBar.setVisibility(View.VISIBLE);
                fileName = null;
                isAudioPlaying = false;
                isAudioPaused = false;
                binding.audioLength.setText("00:00");
                binding.audioSeekbar.setProgress(0);
                binding.playbackAudioBtnImg.setImageResource(R.drawable.ic_play);
            }
        });

        binding.playbackAudioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isAudioPlaying){
                    startPlaying();
                    isAudioPlaying = true;
                    isAudioPaused = false;
                    binding.playbackAudioBtnImg.setImageResource(R.drawable.ic_pause);


                    binding.audioLength.setText(getFormattedAudioDuration(player.getDuration() / 1000));
                    binding.audioSeekbar.setMax(player.getDuration() / 1000);

                    mHandler = new Handler();
                    //Make sure you update Seekbar on UI thread
                    ChatActivity.this.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            if(player != null){
                                int mCurrentPosition = player.getCurrentPosition() / 1000;
                                binding.audioSeekbar.setProgress(mCurrentPosition);
                            }
                            mHandler.postDelayed(this, 1000);
                        }
                    });

                } else {
                    pausePlaying();
                    binding.playbackAudioBtnImg.setImageResource(R.drawable.ic_play);
                    isAudioPlaying = false;
                    isAudioPaused = true;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
//                try {
//                    // You can update this bitmap to your server
//                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                    Matrix matrix = new Matrix();

                    matrix.postRotate(90);

//                    Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

                    binding.defaultBottomBar.setVisibility(View.GONE);
                    binding.selecedImgPrevLayout.setVisibility(View.VISIBLE);
                    binding.audioOrSendBtnImg.setImageResource(R.drawable.ic_send);

                    Glide.with(ChatActivity.this)
                            .load(uri)
                            .into(binding.selectedImg);

                    isSendImage = true;
            }
        } else if(requestCode == ATTACH_DOCS_REQUEST){
            if(resultCode == Activity.RESULT_OK){
                Uri uri = data.getData();

                binding.additionalMenu.setVisibility(View.GONE);
                binding.defaultTxtInpBar.setVisibility(View.GONE);
                binding.selectedDocumentLayout.setVisibility(View.VISIBLE);

                isSendDoc = true;
            }
        } else if(requestCode == ATTACH_AUDIO_REQUEST){
            if(resultCode == Activity.RESULT_OK){
                Uri uri = data.getData();

                binding.additionalMenu.setVisibility(View.GONE);
                binding.defaultTxtInpBar.setVisibility(View.GONE);
                binding.selectedAudioLayout.setVisibility(View.VISIBLE);

                isSendAudio = true;
            }
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }

        if (player != null) {
            player.release();
            player = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case RECORD_AUDIO_PERMISSION_REQUEST:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();

    }

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        if(!isAudioPlaying && !isAudioPaused){
            player = new MediaPlayer();
            try {
                player.setDataSource(fileName);
                player.prepare();
                player.start();
            } catch (IOException e) {

            }
        } else if(isAudioPaused) {
            player.start();
        }
    }

    private void pausePlaying(){
        if(player != null){
            player.pause();
        }
    }

    private void stopPlaying() {
        player.release();
        player = null;
    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {

        }

        recorder.start();
    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
    }

    private void clearTextInpFocus(View v){
        binding.textInp.clearFocus();
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    private String getFormattedAudioDuration(int time){
        int secs = time % 60;
        int mins = (int)((time - secs) / 60);

        String secsStr = secs < 10 ? "0" + secs : "" + secs;
        String minsStr = mins < 10 ? "0" + mins : "" + mins;

        return minsStr + ":" + secsStr;
    }

    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(this, new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        });
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, IMAGE_PICKER_REQUEST);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, IMAGE_PICKER_REQUEST);
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Grant Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", this.getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    public void uploadPendingMessages(HashMap<String, String> downloadURLs){

    }
}
