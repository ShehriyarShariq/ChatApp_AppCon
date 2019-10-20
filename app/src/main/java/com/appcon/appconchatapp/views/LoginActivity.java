package com.appcon.appconchatapp.views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.appcon.appconchatapp.R;
import com.appcon.appconchatapp.adapters.FeaturesImagesViewPagerAdapter;
import com.appcon.appconchatapp.databinding.ActivityLoginBinding;
import com.appcon.appconchatapp.model.User;
import com.appcon.appconchatapp.utils.CONSTANTS;
import com.appcon.appconchatapp.utils.LoaderDialog;
import com.appcon.appconchatapp.viewmodels.LoginActivityViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private final int PHONE_VERIFICATION_REQ = 111;

    ActivityLoginBinding binding;
    LoginActivityViewModel viewModel;

    LiveData<String> authToken;
    LiveData<Boolean> loginFailed, accCreationSuccess, accCreationFailed;

    FirebaseAuth firebaseAuth;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    LoaderDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        sharedPreferences = getSharedPreferences("auth", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        boolean firstTime = sharedPreferences.getBoolean("firstTime", true);

        progressDialog = new LoaderDialog(this, CONSTANTS.PROCESS_LOADER);

        if(firstTime){
            binding.defaultLayout.setVisibility(View.GONE);
            binding.imageFeatures.setVisibility(View.VISIBLE);

            final ArrayList<Integer> allImageResources = new ArrayList<>();
            allImageResources.add(R.drawable.social_feature_screen);
            allImageResources.add(R.drawable.games_feature_screen);
            allImageResources.add(R.drawable.security_feature_screen);


            binding.tabs.addTab(binding.tabs.newTab());
            binding.tabs.addTab(binding.tabs.newTab());
            binding.tabs.addTab(binding.tabs.newTab());

            FeaturesImagesViewPagerAdapter featuresImagesViewPagerAdapter = new FeaturesImagesViewPagerAdapter(this, allImageResources);
            binding.imagesPager.setAdapter(featuresImagesViewPagerAdapter);

            binding.imagesPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    binding.tabs.selectTab(binding.tabs.getTabAt(position));

                    if(position == (allImageResources.size() - 1)){
                        binding.finishBtn.setVisibility(View.VISIBLE);
                    } else {
                        binding.finishBtn.setVisibility(View.GONE);
                    }


                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            binding.finishBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    binding.imageFeatures.setVisibility(View.GONE);
                    binding.defaultLayout.setVisibility(View.VISIBLE);

                    editor.putBoolean("firstTime", false);
                    editor.commit();

                    loadDefaultLayout();
                }
            });
        } else {
            loadDefaultLayout();
        }
    }

    private void loadDefaultLayout(){
        firebaseAuth = FirebaseAuth.getInstance();

        viewModel = ViewModelProviders.of(this).get(LoginActivityViewModel.class);

        authToken = viewModel.getAuthToken();
        loginFailed = viewModel.getLoginFailed();
        accCreationSuccess = viewModel.getAccCreationSuccess();
        accCreationFailed = viewModel.getAccCreationFailed();

        authToken.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String token) {
                if(!token.equals("none")){ // Valid login
                    phoneVerificationCheck("+92" + binding.phoneNumInp.getText().toString().trim());
                }
            }
        });

        loginFailed.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean failed) { // Failed to login
                if(failed){
                    loginFailed();
                }
            }
        });

        accCreationSuccess.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean success) { // New Account Creation Success
                if(success){
                    phoneVerificationCheck("+92" + binding.phoneNumberInputNew.getText().toString().trim());
                }
            }
        });

        accCreationFailed.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean failed) { // New Account Creation Failed
                if(failed){
                    loginFailed();
                }
            }
        });

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNum = binding.phoneNumInp.getText().toString().trim();
                if(isPhoneNumValid(phoneNum)){ // If phone number is valid
                    progressDialog.showDialog();
                    HashMap<String, String> credentials = new HashMap<>();
                    credentials.put("phoneNum", "+92" + phoneNum);

                    // Send login request
                    viewModel.sendLoginRequest(credentials);
                } else {
                    progressDialog.hideDialog();
                    Toast.makeText(LoginActivity.this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();
                }

            }
        });

        binding.signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNum = binding.phoneNumberInputNew.getText().toString().trim();
                if(isPhoneNumValid(phoneNum) && !binding.displayNameInputNew.equals("")){ // If phone number is valid
                    // Create new user and send appropriate hashmap
                    // Auth map and DB map
                    progressDialog.showDialog();
                    User newUser = new User(
                            "",
                            binding.displayNameInputNew.getText().toString(),
                            "+92" + binding.phoneNumberInputNew.getText().toString(),
                            "profilePicture.jpg",
                            0,
                            "Hi, I am available on ConnectMe...",
                            "Offline");

                    viewModel.sendAccCreationRequest(newUser.newUserMaps());
                } else {
                    progressDialog.hideDialog();
                    Toast.makeText(LoginActivity.this, "Invalid Entry!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Switch to signup layout
        binding.createNewUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.loginLayout.setVisibility(View.GONE);
                binding.signupLayout.setVisibility(View.VISIBLE);
            }
        });

        // Switch to login layout
        binding.alreadyHaveAnAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.signupLayout.setVisibility(View.GONE);
                binding.loginLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PHONE_VERIFICATION_REQ){
            if(resultCode == Activity.RESULT_OK){ // Phone verification success
                phoneVerifSuccess(data.getStringExtra("phoneNum"));
            } else {
                Toast.makeText(LoginActivity.this, "Verification Failed!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void phoneVerificationCheck(String phoneNum) {
        Intent verificationActivity = new Intent(LoginActivity.this, PhoneVerificationActivity.class);
        verificationActivity.putExtra("phoneNum", phoneNum);

        startActivityForResult(verificationActivity, PHONE_VERIFICATION_REQ);
    }

    private void phoneVerifSuccess(String phoneNum){
        editor.putString("phoneNum", phoneNum);
        editor.commit();

        loginSuccess();
    }

    private void loginSuccess() {
        progressDialog.hideDialog();
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    private void loginFailed(){
        progressDialog.hideDialog();
        Toast.makeText(LoginActivity.this, "LogIn Failed!", Toast.LENGTH_SHORT).show();
    }

    // Validating provided phone number
    private boolean isPhoneNumValid(String phoneNum) {
        boolean isValid = true;

        if (phoneNum.length() != 10) {
            isValid = false;
        }

        for (int i = 0; i < phoneNum.length(); i++) {
            if ((phoneNum.charAt(i) > '9') && (phoneNum.charAt(i) < '0')) {
                isValid = false;
            }
        }

        return isValid;
    }
}
