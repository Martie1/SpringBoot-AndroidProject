package com.example.pwo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pwo.R;
import com.example.pwo.utils.TokenParser;
import com.example.pwo.utils.UserSession;
import com.example.pwo.network.models.AuthResponse;
import com.example.pwo.network.ApiClient;
import com.example.pwo.network.models.ErrorResponse;
import com.example.pwo.network.models.LoginRequest; // Zmiana na LoginRequest
import com.example.pwo.utils.TokenManager;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import com.example.pwo.utils.validators.LoginValidator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private ImageView btnBack;
    private TextView tvRegister, tvError;
    TokenManager tokenManager;
    LoginValidator loginValidator;


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
        tvError = findViewById(R.id.tvError);
        btnBack = findViewById(R.id.backbutton);
        loginValidator = new LoginValidator();

        btnLogin.setOnClickListener(v -> performLogin());
        tokenManager = new TokenManager(getApplicationContext());
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

        //validate fields
        String emailError = loginValidator.validateEmail(email);
        String passwordError = loginValidator.validatePassword(password);

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

        LoginRequest loginRequest = new LoginRequest(email, password);

        ApiClient.getInstance(getApplicationContext()).getApiService().login(loginRequest).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse authResponse = response.body();

                    String accessToken = authResponse.getAccessToken();
                    String refreshToken = authResponse.getRefreshToken();
                    tokenManager.saveTokens(accessToken, refreshToken);

                    String role = TokenParser.extractRole(accessToken);
                    if (role != null) {
                        UserSession.getInstance().setRole(role);
                        Log.d("LoginActivity", "User role: " + role); // Logowanie roli na terminalu
                    } else {
                        Log.e("LoginActivity", "Failed to extract role from accessToken");
                        UserSession.getInstance().setRole("UNKNOWN");
                    }

                    Intent intent = new Intent(LoginActivity.this, RoomActivity.class);
                    //flags block returning back to main_activity after successfull login/register
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                } else handleErrorResponse(response);
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
                Log.d("LoginActivity", "Error JSON: " + errorJson);

                Gson gson = new Gson();
                ErrorResponse errorResponse = gson.fromJson(errorJson, ErrorResponse.class);

                if (errorResponse != null && errorResponse.getMessage() != null) {
                    String errorMessage = errorResponse.getMessage();
                    switch (response.code()) {
                        case 404:
                            errorMessage = "Email doesn't exist";
                            break;
                        case 401:
                            errorMessage = "Wrong password";
                            break;
                        case 500:
                            errorMessage = "We have problems with our servers. Please try again later.";
                            break;
                        default:

                            break;
                    }
                    tvError.setText("Error: " + errorMessage);
                    tvError.setVisibility(View.VISIBLE);
                } else {
                    Log.e("LoginActivity", "Parsed ErrorResponse is null or missing fields.");
                    tvError.setText("The server is currently unavailable. Please try again later.");
                    tvError.setVisibility(View.VISIBLE);
                }
            } else {
                tvError.setText("The server is currently unavailable. Please try again later.");
                tvError.setVisibility(View.VISIBLE);
            }
        } catch (IOException e) {
            Log.e("LoginActivity", "IO Exception while parsing error response", e);
            tvError.setText("The server is currently unavailable. Please try again later.");
            tvError.setVisibility(View.VISIBLE);
        } catch (JsonSyntaxException e) {
            Log.e("LoginActivity", "JSON syntax error in error response", e);
            tvError.setText("The server is currently unavailable. Please try again later.");
            tvError.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            Log.e("LoginActivity", "Unexpected error", e);
            tvError.setText("The server is currently unavailable. Please try again later.");
            tvError.setVisibility(View.VISIBLE);
        }
    }


}
