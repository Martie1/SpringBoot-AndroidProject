package com.example.pwo.network.repository;

import com.example.pwo.network.ApiClient;
import com.example.pwo.network.ApiService;
import com.example.pwo.network.models.AuthResponse;
import com.example.pwo.network.models.LoginRequest;
import com.example.pwo.network.models.RegisterRequest;


import retrofit2.Call;
import retrofit2.Callback;

//handles requests and responses related to Login and Register
public class AuthRepository {

    private final ApiService apiService;

    public AuthRepository() {
        apiService = ApiClient.getInstance().getApiService();
    }

    public void registerUser(RegisterRequest registerRequest, Callback<AuthResponse> callback) {
        Call<AuthResponse> call = apiService.register(registerRequest);
        call.enqueue(callback);
    }

    public void loginUser(LoginRequest loginRequest, Callback<AuthResponse> callback) {
        Call<AuthResponse> call = apiService.login(loginRequest);
        call.enqueue(callback);
    }
}
