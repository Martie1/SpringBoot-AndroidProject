package com.example.pwo.network.model;

import com.google.gson.annotations.SerializedName;

//Login Request model
public class LoginRequest {
    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;
}
