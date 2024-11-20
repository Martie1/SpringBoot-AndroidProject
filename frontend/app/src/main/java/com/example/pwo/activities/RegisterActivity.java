package com.example.pwo.activities;
import com.example.pwo.utils.TokenParser;
import com.example.pwo.utils.UserSession;
import com.example.pwo.utils.validators.RegisterValidator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pwo.R;
import com.example.pwo.network.models.AuthResponse;
import com.example.pwo.network.ApiClient;
import com.example.pwo.network.models.ErrorResponse;
import com.example.pwo.network.models.RegisterRequest;
import com.example.pwo.utils.TokenManager;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private EditText etUsername, etEmail, etPassword;
    private Button btnRegister;
    private ImageView btnBack;
    private TextView tvSignIn, tvError;
    TokenManager tokenManager;
    RegisterValidator registerValidator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        EdgeToEdge.enable(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnLogin);
        btnBack = findViewById(R.id.backbutton);
        tvSignIn = findViewById(R.id.tvSignUp);
        tvError = findViewById(R.id.tvError);
        registerValidator = new RegisterValidator();
        btnRegister.setOnClickListener(v -> performRegistration());
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
        });
        tvSignIn.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });
        tokenManager = new TokenManager(getApplicationContext());
    }

    private void performRegistration() {
        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        String usernameError = registerValidator.validateUsername(username);
        String emailError = registerValidator.validateEmail(email);
        String passwordError = registerValidator.validatePassword(password, username);
        if (usernameError != null) {
            tvError.setText(usernameError);
            tvError.setVisibility(View.VISIBLE);
            return;
        }

        if (emailError != null) {
            tvError.setText(emailError);
            tvError.setVisibility(View.VISIBLE);
            return;
        }

        if (passwordError != null) {
            tvError.setText(passwordError);
            tvError.setVisibility(View.VISIBLE);
            return;
        }
        tvError.setVisibility(View.GONE);


        RegisterRequest registerRequest = new RegisterRequest(username, email, password);

        ApiClient.getInstance(getApplicationContext()).getApiService().register(registerRequest).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse authResponse = response.body();

//jwt token now will be stored in android device
                    String accessToken = authResponse.getAccessToken();
                    String refreshToken = authResponse.getRefreshToken();
                    tokenManager.saveTokens(accessToken, refreshToken);

                    String role = TokenParser.extractRole(accessToken);
                    if (role != null) {
                        UserSession.getInstance().setRole(role);
                        Log.d("RegisterActivity", "User role: " + role);
                    } else {
                        Log.e("RegisterActivity", "Failed to extract role from accessToken");
                        UserSession.getInstance().setRole("UNKNOWN");
                    }
                    //roomActivity intent
                    Intent intent = new Intent(RegisterActivity.this, RoomActivity.class);

                    //flags block returning back to main_activity after successfull login/register
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                } else {
                    handleErrorResponse(response);
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                tvError.setText("The server is currently unavailable. Please try again later.");
                tvError.setVisibility(View.VISIBLE);

            }
        });
    }
    private void handleErrorResponse(Response<AuthResponse> response) {
        try {
            if (response.errorBody() != null) {
                String errorJson = response.errorBody().string();
                Log.d("RegisterActivity", "Error JSON: " + errorJson);

                Gson gson = new Gson();
                ErrorResponse errorResponse = gson.fromJson(errorJson, ErrorResponse.class);

                if (errorResponse != null && errorResponse.getMessage() != null) {
                    String errorMessage = errorResponse.getMessage();
                    if (response.code() == 409) {
                        tvError.setText("Error: " + errorMessage);
                        tvError.setVisibility(View.VISIBLE);
                    }

                } else {
                    Log.e("RegisterActivity", "Parsed ErrorResponse is null or missing fields.");
                    tvError.setText("The server is currently unavailable. Please try again later.");
                    tvError.setVisibility(View.VISIBLE);
                }
            } else {
                tvError.setText("The server is currently unavailable. Please try again later.");
                tvError.setVisibility(View.VISIBLE);
            }
        } catch (IOException e) {
            Log.e("RegisterActivity", "IO Exception while parsing error response", e);
            tvError.setText("The server is currently unavailable. Please try again later.");
            tvError.setVisibility(View.VISIBLE);
        } catch (JsonSyntaxException e) {
            Log.e("RegisterActivity", "JSON syntax error in error response", e);
            tvError.setText("The server is currently unavailable. Please try again later.");
            tvError.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            Log.e("RegisterActivity", "Unexpected error", e);
            tvError.setText("The server is currently unavailable. Please try again later.");
            tvError.setVisibility(View.VISIBLE);
        }
    }
}