package com.appcon.appconchatapp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.appcon.appconchatapp.model.Result;
import com.appcon.appconchatapp.utils.CONSTANTS;
import com.appcon.appconchatapp.utils.ServerAPI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivityViewModel extends AndroidViewModel {

    private MutableLiveData<String> authToken = new MutableLiveData<>();
    private MutableLiveData<Boolean> loginFailed = new MutableLiveData<>();
    private MutableLiveData<Boolean> accCreationFailed = new MutableLiveData<>();

    public LoginActivityViewModel(@NonNull Application application) {
        super(application);

        setAuthToken("none");
        setLoginFailed(false);
        setAccCreationFailed(false);
    }

    public MutableLiveData<String> getAuthToken() {
        return authToken;
    }

    private void setAuthToken(String authTokenStr) {
        authToken.postValue(authTokenStr);
    }

    public MutableLiveData<Boolean> getLoginFailed() {
        return loginFailed;
    }

    private void setLoginFailed(boolean loginFailedBool) {
        loginFailed.postValue(loginFailedBool);
    }

    public MutableLiveData<Boolean> getAccCreationFailed() {
        return accCreationFailed;
    }

    private void setAccCreationFailed(boolean accCreationFailedBool) {
        accCreationFailed.postValue(accCreationFailedBool);
    }

    public void sendLoginRequest(HashMap<String, String> credentials){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(CONSTANTS.AUTH_URL)
                .addConverterFactory(GsonConverterFactory.create(gson));

        Retrofit retrofit = builder.build();

        ServerAPI server = retrofit.create(ServerAPI.class);
        Call<Result> call = server.loginUser(credentials);

        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.body().isResultSuccessful()) {
                    // Get back Custom token
                    setAuthToken(response.body().getMessage().toString());
                } else {
                    setLoginFailed(true);
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                setLoginFailed(true);
            }
        });
    }

    public void sendAccCreationRequest(HashMap<String, Object> authAndDBMap){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(CONSTANTS.AUTH_URL)
                .addConverterFactory(GsonConverterFactory.create(gson));

        Retrofit retrofit = builder.build();

        ServerAPI server = retrofit.create(ServerAPI.class);
        Call<Result> call = server.signUpUser(authAndDBMap);

        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.body().isResultSuccessful()) {
                    // Get back Custom token
                    setAuthToken(response.body().getMessage().toString());
                } else {
                    setAccCreationFailed(true);
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                setAccCreationFailed(true);
            }
        });
    }
}
