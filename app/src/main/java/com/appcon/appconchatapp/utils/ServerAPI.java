package com.appcon.appconchatapp.utils;

import com.appcon.appconchatapp.model.Result;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ServerAPI {

    // AUTH
    @PUT("api/auth/login")
    Call<Result> loginUser(@Body HashMap<String, String> credentials);

    @PUT("api/auth/signup")
    Call<Result> signUpUser(@Body HashMap<String, Object> authAndDBMap);


    // UTILS
    @PUT("api/utils/get_user_profile")
    Call<Result> getUserProfile(@Body HashMap<String, String> user);

    @PUT("api/utils/get_friends_stories")
    Call<Result> getFriendsStories(@Body ArrayList<String> friends);


    // USER_READ
    @PUT("api/user/get_valid_contacts")
    Call<Result> getAlLValidContacts(@Body HashMap<String, Object> contacts);

    @PUT("api/user/get_conversations")
    Call<Result> getUserConversations(@Body HashMap<String, String> user);


    // USER_WRITE
    @PUT("api/user/create_user_chats")
    Call<Result> addNewChats(@Body HashMap<String, Object> chats);

}
