package com.example.pwo.network;
import com.example.pwo.classes.Room;
import com.example.pwo.network.models.AuthResponse;
import com.example.pwo.network.models.LoginRequest;
import com.example.pwo.network.models.RegisterRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Body;

//mapping frontend requests for backend routes
//ApiService will be used by retrofit.
public interface ApiService {
    @POST("auth/register")
    Call<AuthResponse> register(@Body RegisterRequest registerRequest);

    @POST("auth/login")
    Call<AuthResponse> login(@Body LoginRequest loginRequest);

    @GET("api/rooms")
    Call<List<Room>> getRooms();
}
