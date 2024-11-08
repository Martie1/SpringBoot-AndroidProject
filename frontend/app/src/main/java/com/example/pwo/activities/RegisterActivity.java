package com.example.pwo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pwo.R;
import com.example.pwo.network.models.AuthResponse;
import com.example.pwo.network.ApiClient;
import com.example.pwo.network.models.ErrorResponse;
import com.example.pwo.network.models.RegisterRequest;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private EditText etUsername, etEmail, etPassword;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_register, findViewById(R.id.main));

        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegisterSubmit);
        btnRegister.setOnClickListener(v -> performRegistration());
    }

    private void performRegistration() {
        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        RegisterRequest registerRequest = new RegisterRequest(username, email, password);

        ApiClient.getInstance().getApiService().register(registerRequest).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse authResponse = response.body();

//jwt token now will be stored in android device
                    String token = authResponse.getToken();
                    SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("auth_token", token);
                    editor.apply();

                    //check for token saved
                    String savedToken = sharedPreferences.getString("auth_token", null);
                    Log.d("RegisterActivity", "Saved Token: " + savedToken);

                    Toast.makeText(RegisterActivity.this, authResponse.getMessage(), Toast.LENGTH_SHORT).show();

                    //roomActivity intent
                    Intent intent = new Intent(RegisterActivity.this, RoomActivity.class);

                    //flags block returning back to main_activity after successfull login/register
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                } else {

                    try {
                        if (response.errorBody() != null) {
                            String errorJson = response.errorBody().string();
                            Log.d("RegisterActivity", "Error JSON: " + errorJson);


                            Gson gson = new Gson();
                            ErrorResponse errorResponse = gson.fromJson(errorJson, ErrorResponse.class);

                            if (errorResponse != null && errorResponse.getMessage() != null) {
                                String errorMessage = errorResponse.getMessage();
                                Toast.makeText(RegisterActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e("RegisterActivity", "Parsed ErrorResponse is null or missing fields.");
                                Toast.makeText(RegisterActivity.this, "Error: Unable to parse error message", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(RegisterActivity.this, "Unknown error occurred", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        Log.e("RegisterActivity", "IO Exception while parsing error response", e);
                        Toast.makeText(RegisterActivity.this, "Network issue occurred", Toast.LENGTH_SHORT).show();
                    } catch (JsonSyntaxException e) {
                        Log.e("RegisterActivity", "JSON syntax error in error response", e);
                        Toast.makeText(RegisterActivity.this, "Error in server response format", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Log.e("RegisterActivity", "Unexpected error", e);
                        Toast.makeText(RegisterActivity.this, "An unexpected error occurred", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {

                Toast.makeText(RegisterActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}