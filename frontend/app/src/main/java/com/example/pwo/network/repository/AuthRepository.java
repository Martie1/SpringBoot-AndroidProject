package com.example.pwo.network.repository;

import com.example.pwo.network.ApiClient;
import com.example.pwo.network.ApiService;
import com.example.pwo.network.model.LoginRequest;
import com.example.pwo.network.model.RegisterRequest;
import com.example.pwo.network.model.UserResponse;



import retrofit2.Call;
import retrofit2.Callback;

//handles requests and responses related to Login and Register
public class AuthRepository {

    private final ApiService apiService;

    public AuthRepository() {
        apiService = ApiClient.getInstance().getApiService();
    }

    public void registerUser(RegisterRequest registerRequest, Callback<UserResponse> callback) {
        Call<UserResponse> call = apiService.register(registerRequest);
        call.enqueue(callback);
    }

    public void loginUser(LoginRequest loginRequest, Callback<UserResponse> callback) {
        Call<UserResponse> call = apiService.login(loginRequest);
        call.enqueue(callback);
    }
}
