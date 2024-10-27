package com.example.pwo.network.model;
import com.google.gson.annotations.SerializedName;

//register request model
public class RegisterRequest {
    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;

    @SerializedName("email")
    private String email;
}
