package com.example.pwo.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.pwo.activities.MainActivity;
import com.example.pwo.activities.ProfileActivity;
import com.example.pwo.network.ApiClient;
import com.example.pwo.network.models.AuthResponse;
import com.example.pwo.network.models.RefreshTokenRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TokenManager {
    private static final String PREF_NAME = "auth_prefs";
    private static final String ACCESS_TOKEN_KEY = "access_token";
    private static final String REFRESH_TOKEN_KEY = "refresh_token";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public TokenManager(Context context) {
        if (context != null) {
            this.context = context;
            this.sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            this.editor = sharedPreferences.edit();
        } else {
            throw new IllegalArgumentException("Context cannot be null");
        }
    }
    public void logout(Context context) {
        clearTokens();
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public void saveTokens(String accessToken, String refreshToken) {
        Log.d("TokenManager", "Saving Tokens");
        editor.putString(ACCESS_TOKEN_KEY, accessToken);
        editor.putString(REFRESH_TOKEN_KEY, refreshToken);
        editor.apply();
    }

    public String getAccessToken() {

        return sharedPreferences.getString(ACCESS_TOKEN_KEY, null);
    }

    public String getRefreshToken() {

        return sharedPreferences.getString(REFRESH_TOKEN_KEY, null);
    }

    public void clearTokens() {
        editor.clear().apply();
        Log.d("TokenManager", "Tokens cleared");
        Log.d("TokenManager", "Access Token: " + getAccessToken());
        Log.d("TokenManager", "Refresh Token: " + getRefreshToken());
    }
    public void refreshAccessToken(Callback<AuthResponse> callback) {
        String refreshToken = getRefreshToken();
        if (refreshToken != null) {
            RefreshTokenRequest request = new RefreshTokenRequest(refreshToken);
            ApiClient.getInstance(context).getApiService().refreshToken(request).enqueue(new Callback<AuthResponse>() {
                @Override
                public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        saveTokens(response.body().getAccessToken(), response.body().getRefreshToken());
                        callback.onResponse(call, response);
                    } else {
                        logout(context);
                        callback.onFailure(call, new Throwable("Failed to refresh token"));
                    }
                }

                @Override
                public void onFailure(Call<AuthResponse> call, Throwable t) {
                    logout(context);
                    callback.onFailure(call, t);
                }
            });
        } else {
            callback.onFailure(null, new Throwable("No refresh token available"));
        }
    }


}