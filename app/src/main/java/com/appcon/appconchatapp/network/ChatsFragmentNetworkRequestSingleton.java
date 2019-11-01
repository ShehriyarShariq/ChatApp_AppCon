package com.appcon.appconchatapp.network;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.appcon.appconchatapp.model.Result;
import com.appcon.appconchatapp.utils.CONSTANTS;
import com.appcon.appconchatapp.utils.ServerAPI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatsFragmentNetworkRequestSingleton {

    DatabaseReference firebaseDatabase;
    FirebaseAuth firebaseAuth;

    private MutableLiveData<ArrayList<String>> allChats = new MutableLiveData<>();

    ArrayList<String> allChatsList;

    public ChatsFragmentNetworkRequestSingleton() {
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        allChatsList = new ArrayList<>();
    }

    private static ChatsFragmentNetworkRequestSingleton holder = new ChatsFragmentNetworkRequestSingleton();
    public static ChatsFragmentNetworkRequestSingleton getInstance(){
        return holder;
    }

    public MutableLiveData<ArrayList<String>> getAllChats() {
        return allChats;
    }

    private void setAllChats(ArrayList<String> allChats) {
        this.allChats.postValue(allChats);
    }

    public void getAllConversationsRequest(){
        firebaseDatabase.child("users").child(firebaseAuth.getCurrentUser().getUid()).child("chats").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(!dataSnapshot.getKey().equals("chatID")){
                    allChatsList.add(dataSnapshot.getKey());
                    setAllChats(allChatsList);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getUserDetails(String userID){
//        firebaseDatabase.child("users").child(userID)
    }

}
