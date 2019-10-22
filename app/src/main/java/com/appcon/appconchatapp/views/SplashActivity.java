package com.appcon.appconchatapp.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.appcon.appconchatapp.R;
import com.appcon.appconchatapp.viewmodels.SplashActivityViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    Handler handler;

    LiveData<String> authToken;
    LiveData<Boolean> loginFailed;

    SplashActivityViewModel viewModel;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        viewModel = ViewModelProviders.of(this).get(SplashActivityViewModel.class);

        firebaseAuth = FirebaseAuth.getInstance();

        SharedPreferences sharedPreferences = getSharedPreferences("auth", MODE_PRIVATE);
        final String phoneNum = sharedPreferences.getString("phoneNum", "none");

        if(phoneNum.equals("none")){
            Timer timer = new Timer();
            handler = new Handler();
            TimerTask loginTimer = new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            loginFailed();
                        }
                    });
                }
            };
            timer.schedule(loginTimer, 1500);
        } else {
            sendLoginReq(phoneNum);
        }

        authToken = viewModel.getAuthToken();
        authToken.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String token) {
                if(!token.equals("none")){ // Valid Token
                    signInWithToken(token);
                }
            }
        });

        loginFailed = viewModel.getLoginFailed();
        loginFailed.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean failed) {
                if(failed){ // Login failed
                    loginFailed();
                }
            }
        });

    }

    private void sendLoginReq(String phoneNum){
        HashMap<String, String> credentials = new HashMap<>();
        credentials.put("phoneNum", phoneNum);

        viewModel.sendLoginRequest(credentials);
    }

    private void signInWithToken(String loginToken) {
        firebaseAuth.signInWithCustomToken(loginToken).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    loginSuccess();
                } else {
                    loginFailed();
                }
            }
        });
    }

    private void loginSuccess() {
        startActivity(new Intent(SplashActivity.this, ChatActivity.class));
        finish();
    }

    private void loginFailed(){
        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        finish();
    }
}