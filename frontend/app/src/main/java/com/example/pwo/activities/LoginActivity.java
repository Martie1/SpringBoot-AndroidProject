package com.example.pwo.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pwo.R;
import com.example.pwo.network.models.AuthResponse;
import com.example.pwo.network.ApiClient;
import com.example.pwo.network.models.ErrorResponse;
import com.example.pwo.network.models.LoginRequest; // Zmiana na LoginRequest
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLoginSubmit);
        btnLogin.setOnClickListener(v -> performLogin());
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


                    String token = authResponse.getToken();
                    SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("auth_token", token);
                    editor.apply();


                    String savedToken = sharedPreferences.getString("auth_token", null);
                    Log.d("LoginActivity", "Saved Token: " + savedToken);

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