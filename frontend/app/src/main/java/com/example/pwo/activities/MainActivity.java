package com.example.pwo.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pwo.R;
import com.example.pwo.network.ApiClient;
import com.example.pwo.network.models.AuthResponse;
import com.example.pwo.network.models.RefreshTokenRequest;
import com.example.pwo.utils.TokenManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity{
    private Button btnLogin;
    private Button btnRegister;
    private TokenManager tokenManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        EdgeToEdge.enable(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tokenManager = new TokenManager(getApplicationContext()); // Initialize tokenManager here
        String refreshToken = tokenManager.getRefreshToken();
        Log.d("MainActivity", "refreshToken onCreate: " + refreshToken);


       //jwt validate feature will be added here
        if (refreshToken != null) {
            validateRefreshToken(refreshToken);
        }

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
        });
        btnRegister= findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
            startActivity(intent);
        });


    }
    private void validateRefreshToken(String refreshToken) {
        RefreshTokenRequest request = new RefreshTokenRequest(refreshToken);
        ApiClient.getInstance(getApplicationContext()).getApiService().refreshToken(request).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    tokenManager.saveTokens(response.body().getAccessToken(), response.body().getRefreshToken());
                    Log.d("MainActivity", "accessToken onResponse: " + response.body().getAccessToken());
                    Log.d("MainActivity", "refreshToken onResponse: " + response.body().getRefreshToken());
                    Intent intent = new Intent(MainActivity.this, RoomActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Log.d("MainActivity", "onFailure: " + t.getMessage());
            }
        });
    }
}
