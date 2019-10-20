package com.appcon.appconchatapp.utils;

import java.util.HashMap;

import retrofit2.http.Body;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ServerAPI {

    @PUT("api/login")
    Call<> loginUser(@Body HashMap<String, String> credentials);

    @PUT("api/signup")
    Call<> signUpUser(@Body HashMap<String, String> credentials);

}
