package com.example.pwo.network;
import com.example.pwo.classes.Post;
import com.example.pwo.classes.Room;
import com.example.pwo.classes.User;
import com.example.pwo.network.models.AuthResponse;
import com.example.pwo.network.models.LoginRequest;
import com.example.pwo.network.models.PostRequest;
import com.example.pwo.network.models.PostResponse;
import com.example.pwo.network.models.RefreshTokenRequest;
import com.example.pwo.network.models.RegisterRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Body;
import retrofit2.http.Path;


public interface ApiService {
    @POST("/auth/register")
    Call<AuthResponse> register(@Body RegisterRequest registerRequest);

    @POST("/auth/login")
    Call<AuthResponse> login(@Body LoginRequest loginRequest);

    @POST("/auth/refresh")
    Call<AuthResponse> refreshToken(@Body RefreshTokenRequest refreshTokenRequest);

    @GET("/api/rooms")
    Call<List<Room>> getRooms();

    @GET("/api/posts/room/{id}")
    Call<List<Post>> getPosts(@Path("id") int id);

    @GET("/api/posts/{id}")
    Call<Post> getPost(@Path("id") int id);

    @GET("/api/user/profile")
    Call<User> getUser();

    @POST("/api/posts")
    Call<PostResponse> addPost(@Body PostRequest postRequest);
}
