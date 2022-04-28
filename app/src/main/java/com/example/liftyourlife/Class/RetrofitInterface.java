package com.example.liftyourlife.Class;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitInterface {
    @POST("/login")
    Call<User> userLogin(@Body HashMap<String,User> map);
    @POST("/signup")
    Call<Void> executeSignup (@Body HashMap<String, String> map);
}
