package com.appcon.appconchatapp.utils;

import com.appcon.appconchatapp.model.Result;

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
    Call<Result> signUpUser(@Body HashMap<String, String> credentials);



    // UTILS
    @PUT("api/utils/get_user_profile")
    Call<Result> getUserProfile(@Body HashMap<String, String> user);

    @PUT("api/utils/get_friends")
    Call<Result> getAddedFriends(@Body HashMap<String, String> phoneNumbers);



    // USER_READ
    @PUT("api/user/get_conversations")
    Call<Result> getUserConversations();

}
