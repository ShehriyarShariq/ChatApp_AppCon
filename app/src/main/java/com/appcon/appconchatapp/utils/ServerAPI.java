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
    @PUT("api/user/get_conversations")
    Call<Result> getUserConversations();

    @PUT("api/user/get_friends")
    Call<Result> getAddedFriends(@Body ArrayList<String> phoneNumbers);


}
