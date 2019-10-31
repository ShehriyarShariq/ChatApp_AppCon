package com.appcon.appconchatapp.network;

import android.app.Application;
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;

import androidx.lifecycle.MutableLiveData;

import com.appcon.appconchatapp.model.LocalContact;
import com.appcon.appconchatapp.model.Result;
import com.appcon.appconchatapp.utils.CONSTANTS;
import com.appcon.appconchatapp.utils.ServerAPI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivityNetworkRequestsSingleton {

    private MutableLiveData<ArrayList<String>> allValidPhoneNumbers = new MutableLiveData<>();

    private static MainActivityNetworkRequestsSingleton holder = new MainActivityNetworkRequestsSingleton();
    public static MainActivityNetworkRequestsSingleton getInstance(){
        return holder;
    }

    public MutableLiveData<ArrayList<String>> getAllValidPhoneNumbers() {
        return allValidPhoneNumbers;
    }

    private void setAllValidPhoneNumbers(ArrayList<String> allValidPhoneNumbers) {
        this.allValidPhoneNumbers.postValue(allValidPhoneNumbers);
    }

    public void getAllValidContactsRequest(HashMap<String, Object> contacts){
        final Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(CONSTANTS.USER_READ_URL)
                .addConverterFactory(GsonConverterFactory.create(gson));

        Retrofit retrofit = builder.build();

        ServerAPI server = retrofit.create(ServerAPI.class);
        Call<Result> call = server.getAlLValidContacts(contacts);

        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.body().isResultSuccessful()) {
                    Gson converterGson = new Gson();

                    String allValidContactsJSONString = converterGson.toJson(response.body().getMessage());
                    ArrayList<String> allValidPhoneNumbersList = converterGson.fromJson(allValidContactsJSONString, ArrayList.class);

                    setAllValidPhoneNumbers(allValidPhoneNumbersList);

                } else {

                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {

            }
        });
    }


}
