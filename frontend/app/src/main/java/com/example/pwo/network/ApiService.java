package com.example.pwo.network;
import com.example.pwo.classes.Post;
import com.example.pwo.classes.Room;
import com.example.pwo.classes.User;
import com.example.pwo.network.models.AuthResponse;
import com.example.pwo.network.models.LoginRequest;
import com.example.pwo.network.models.PostRequest;
import com.example.pwo.network.models.ReportRequest;
import com.example.pwo.network.models.PostResponse;
import com.example.pwo.network.models.RefreshTokenRequest;
import com.example.pwo.network.models.RegisterRequest;
import com.example.pwo.network.models.SimpleResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Body;
import retrofit2.http.PUT;
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
    Call<SimpleResponse> addPost(@Body PostRequest postRequest);

    @GET("/api/user/posts")
    Call<List<Post>> getUserPosts();

    @POST("/api/posts/{postId}/report")
    Call<Void> reportPost(
            @Path("postId") int postId,
            @Body ReportRequest reportRequest,
            @Header("Authorization") String authHeader
    );

    @GET("/api/admin/{roomId}/posts")
    Call<List<Post>> getReportedPosts(@Path("roomId") int roomId);

    @POST("/api/admin/{postId}/resolve")
    Call<SimpleResponse> resolveReport(@Path("postId") int postId);

    @POST("/api/admin/{postId}/dismiss")
    Call<SimpleResponse> dismissReport(@Path("postId") int postId);


    @POST("/api/posts/{postId}/like")
    Call<Void> CreateLike(@Path("postId") int postId);

    @DELETE("/api/posts/{postId}/like")
    Call<Void> DeleteLike(@Path("postId") int postId);

    @GET("/api/user/likes")
    Call<List<Post>> getLikes();

    @PUT("/api/posts/{postId}")
    Call<PostResponse> updatePost(@Path("postId") int postId, @Body PostRequest postRequest);

    @DELETE("/api/posts/{postId}")
    Call<Void> deletePost(@Path("postId") int postId);
}


