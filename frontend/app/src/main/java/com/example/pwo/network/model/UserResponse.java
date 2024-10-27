package com.example.pwo.network.model;

import com.google.gson.annotations.SerializedName;

//UserResponse is returned after register/login. Register automatically logs in also.
public class UserResponse {

    //felt cute, might delete this field later :)
    @SerializedName("id")
    private String userId;

    //token for authorization purposes, role will be retrieved from the token
    @SerializedName("token")
    private String token;


}
