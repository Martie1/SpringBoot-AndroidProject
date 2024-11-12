package com.example.pwo.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pwo.R;
import com.example.pwo.network.models.AuthResponse;
import com.example.pwo.network.ApiClient;
import com.example.pwo.network.models.ErrorResponse;
import com.example.pwo.network.models.LoginRequest; // Zmiana na LoginRequest
import com.example.pwo.utils.TokenManager;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private ImageView btnBack;
    private TextView tvRegister;
    TokenManager tokenManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EdgeToEdge.enable(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvSignUp);
        btnBack = findViewById(R.id.backbutton);
        btnLogin.setOnClickListener(v -> performLogin());
        TokenManager tokenManager = new TokenManager(getApplicationContext());
        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }

    private void performLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        LoginRequest loginRequest = new LoginRequest(email, password);

        ApiClient.getInstance().getApiService().login(loginRequest).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse authResponse = response.body();

                    String accessToken = authResponse.getAccessToken();
                    String refreshToken = authResponse.getRefreshToken();
                    tokenManager.saveTokens(accessToken, refreshToken);

                    Toast.makeText(LoginActivity.this, authResponse.getMessage(), Toast.LENGTH_SHORT).show();


                    Intent intent = new Intent(LoginActivity.this, RoomActivity.class);
                    //flags block returning back to main_activity after successfull login/register
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                } else {

                    try {
                        if (response.errorBody() != null) {
                            String errorJson = response.errorBody().string();
                            Log.d("LoginActivity", "Error JSON: " + errorJson);

                            Gson gson = new Gson();
                            ErrorResponse errorResponse = gson.fromJson(errorJson, ErrorResponse.class);

                            if (errorResponse != null && errorResponse.getMessage() != null) {
                                String errorMessage = errorResponse.getMessage();
                                Toast.makeText(LoginActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e("LoginActivity", "Parsed ErrorResponse is null or missing fields.");
                                Toast.makeText(LoginActivity.this, "Error: Unable to parse error message", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Unknown error occurred", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        Log.e("LoginActivity", "IO Exception while parsing error response", e);
                        Toast.makeText(LoginActivity.this, "Network issue occurred", Toast.LENGTH_SHORT).show();
                    } catch (JsonSyntaxException e) {
                        Log.e("LoginActivity", "JSON syntax error in error response", e);
                        Toast.makeText(LoginActivity.this, "Error in server response format", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Log.e("LoginActivity", "Unexpected error", e);
                        Toast.makeText(LoginActivity.this, "An unexpected error occurred", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
