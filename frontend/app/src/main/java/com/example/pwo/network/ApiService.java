package com.example.pwo.network;
import com.example.pwo.network.model.LoginRequest;
import com.example.pwo.network.model.RegisterRequest;
import com.example.pwo.network.model.UserResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Body;
import retrofit2.http.Path;

//mapping frontend requests for backend routes
//ApiService will be used by retrofit.
public interface ApiService {
    @POST("register")
    Call<UserResponse> register(@Body RegisterRequest registerRequest);

    @POST("login")
    Call<UserResponse> login(@Body LoginRequest loginRequest);
}
