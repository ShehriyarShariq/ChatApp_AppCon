package com.appcon.appconchatapp.views;

import android.Manifest;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.appcon.appconchatapp.R;
import com.appcon.appconchatapp.adapters.ConvosAndCallsPageAdapter;
import com.appcon.appconchatapp.databinding.ActivityMainBinding;
import com.appcon.appconchatapp.listeners.ChatsFragmentListener;
import com.appcon.appconchatapp.model.Chat;
import com.appcon.appconchatapp.model.LocalContact;
import com.appcon.appconchatapp.viewmodels.MainActivityViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final static int OPEN_SETTINGS_REQUEST_CODE = 001;

    ConvosAndCallsPageAdapter convosAndCallsPageAdapter;

    ChatsFragmentListener chatsFragmentListener;

    MainActivityViewModel viewModel;
    ActivityMainBinding binding;

    int currentPage;

    LiveData<ArrayList<LocalContact>> allLocalContactsLive;
    LiveData<ArrayList<HashMap<String, String>>> allValidContactsLive;
    ArrayList<LocalContact> allLocalContacts;
    ArrayList<String> allLocalPhoneNumbers;
    ArrayList<LocalContact> allValidContacts;

    // Requesting permission to RECORD_AUDIO
    private String [] permissions = {Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

        allLocalContacts = new ArrayList<>();
        allValidContacts = new ArrayList<>();

        Dexter.withActivity(MainActivity.this)
        .withPermissions(permissions)
        .withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.areAllPermissionsGranted()) {
                    viewModel.getAllContactsFromPhone();
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

        allLocalContactsLive = viewModel.getAllLocalContacts();
        allLocalContactsLive.observe(this, new Observer<ArrayList<LocalContact>>() {
            @Override
            public void onChanged(ArrayList<LocalContact> localContacts) {
                if(localContacts.size() > 0){
                    allLocalContacts = localContacts;

                    HashMap<String, Object> contacts = new HashMap<>();
                    contacts.put("userID", firebaseAuth.getCurrentUser().getUid());

                    allLocalPhoneNumbers = new ArrayList<>();
                    for(LocalContact contact : allLocalContacts){
                        allLocalPhoneNumbers.add(contact.getPhoneNumber());
                    }
                    contacts.put("allContacts", allLocalPhoneNumbers);

                    viewModel.getAllValidContactsRequest(contacts);
                }
            }
        });

        allValidContactsLive = viewModel.getAllValidPhoneNumbers();
        allValidContactsLive.observe(this, new Observer<ArrayList<HashMap<String, String>>>() {
            @Override
            public void onChanged(ArrayList<HashMap<String, String>> contacts) {
                if(contacts.size() > 0){
                    for(HashMap<String, String> contact : contacts){
                        allValidContacts.add(allLocalContacts.get(allLocalPhoneNumbers.indexOf(contact.get("phoneNum"))));
                        allValidContacts.get(allValidContacts.size() - 1).setUid(contact.get("id"));
                    }
                }
            }
        });

        chatsFragmentListener = new ChatsFragmentListener() {
            @Override
            public void OnChatLongPress(int pos) {
                binding.defaultTopBar.setVisibility(View.GONE);
                binding.selectedChatTopBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void OnClearSelectionBtnPress() {

            }

            @Override
            public void updateSelected() {
                ArrayList<Chat> selectedConvos = convosAndCallsPageAdapter.getSelectedConvos();

                if(selectedConvos.size() == 0){
                    binding.defaultTopBar.setVisibility(View.VISIBLE);
                    binding.selectedChatTopBar.setVisibility(View.GONE);
                }

                binding.selectedCount.setText("" + selectedConvos.size());
            }
        };

        convosAndCallsPageAdapter = new ConvosAndCallsPageAdapter(getSupportFragmentManager(), chatsFragmentListener);

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.selectedChatTopBar.setVisibility(View.GONE);
                binding.defaultTopBar.setVisibility(View.VISIBLE);

                convosAndCallsPageAdapter.clearAllSelection();
            }
        });

        binding.viewPager.setAdapter(convosAndCallsPageAdapter);

        currentPage = 0;

        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                binding.tabs.selectTab(binding.tabs.getTabAt(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if(state == ViewPager.SCROLL_STATE_SETTLING){
                    if((binding.viewPager.getCurrentItem() == 0) && (currentPage == 1)){
                        currentPage = 0;
                        binding.tabSelector.setTranslationX(0);
                    } else if((binding.viewPager.getCurrentItem() == 1) && (currentPage == 0)){
                        currentPage = 1;
                        binding.tabSelector.setTranslationX(binding.tabSelector.getWidth() - convertDpToPx(20));
                    }
                }

            }
        });

        binding.tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0){
                    currentPage = 0;
                    binding.tabSelector.setTranslationX(0);
                } else if(tab.getPosition() == 1){
                    currentPage = 1;
                    binding.tabSelector.setTranslationX(binding.tabSelector.getWidth() - convertDpToPx(20));
                }

                binding.viewPager.setCurrentItem(tab.getPosition(), true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        binding.newBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> map = new HashMap<>();
                map.put("content", String.valueOf(new Date().getTime()));
                map.put("sentBy", firebaseAuth.getCurrentUser().getUid());
                map.put("timeStamp", "timeStamp");
                map.put("type", "text");

                FirebaseDatabase.getInstance().getReference().child("messages").child("chatID").push().setValue(map);
            }
        });
    }

    public float convertDpToPx(float dp) {
        return dp * getResources().getDisplayMetrics().density;
    }



    public void syncContacts(){

    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Grant Permissions");
        builder.setMessage("To continue further, this app requires access to contacts.");
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
        startActivityForResult(intent, OPEN_SETTINGS_REQUEST_CODE);
    }
}