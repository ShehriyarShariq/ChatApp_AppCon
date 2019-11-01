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

    private MutableLiveData<ArrayList<HashMap<String, String>>> allValidPhoneNumbers = new MutableLiveData<>();
    private MutableLiveData<Boolean> addedNewChats = new MutableLiveData<>();

    private static MainActivityNetworkRequestsSingleton holder = new MainActivityNetworkRequestsSingleton();
    public static MainActivityNetworkRequestsSingleton getInstance(){
        return holder;
    }

    public MutableLiveData<ArrayList<HashMap<String, String>>> getAllValidPhoneNumbers() {
        return allValidPhoneNumbers;
    }

    private void setAllValidPhoneNumbers(ArrayList<HashMap<String, String>> allValidPhoneNumbers) {
        this.allValidPhoneNumbers.postValue(allValidPhoneNumbers);
    }

    public MutableLiveData<Boolean> getAddedNewChats() {
        return addedNewChats;
    }

    public void setAddedNewChats(boolean addedNewChats) {
        this.addedNewChats.postValue(addedNewChats);
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
                    ArrayList<Object> allValidPhoneNumbersList = converterGson.fromJson(allValidContactsJSONString, ArrayList.class);

                    ArrayList<HashMap<String, String>> allValidPhoneNumbers = new ArrayList<>();
                    for(Object object : allValidPhoneNumbersList){
                        String contactJSONString = converterGson.toJson(object);
                        HashMap<String, String> contactMap = converterGson.fromJson(contactJSONString, HashMap.class);
                        allValidPhoneNumbers.add(contactMap);
                    }

                    setAllValidPhoneNumbers(allValidPhoneNumbers);

                } else {

                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {

            }
        });
    }

    public void addNewChatsRequest(HashMap<String, Object> chats){
        final Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(CONSTANTS.USER_WRITE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson));

        Retrofit retrofit = builder.build();

        ServerAPI server = retrofit.create(ServerAPI.class);
        Call<Result> call = server.addNewChats(chats);

        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.body().isResultSuccessful()) {
                    setAddedNewChats(true);
                } else {
                    setAddedNewChats(false);
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {

            }
        });
    }


}
