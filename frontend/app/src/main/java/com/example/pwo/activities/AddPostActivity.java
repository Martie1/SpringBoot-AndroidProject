package com.example.pwo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pwo.R;
import com.example.pwo.network.ApiClient;
import com.example.pwo.network.models.ErrorResponse;
import com.example.pwo.network.models.PostRequest;
import com.example.pwo.network.models.PostResponse;
import com.example.pwo.utils.TokenManager;
import com.example.pwo.utils.validators.PostValidatorImpl;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPostActivity extends AppCompatActivity {
    private Button btnAddPost;
    private EditText etPostTitle;
    private EditText etPostDescription;
    private TextView tvError;
    private int roomId;
    TokenManager tokenManager;
    PostValidatorImpl postValidator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_post);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        tokenManager = new TokenManager(getApplicationContext());
        String token = tokenManager.getAccessToken();
        Intent intent = getIntent();
        if (intent.hasExtra("roomId")) {
            roomId = intent.getIntExtra("roomId", 1);
        } else {
            showError("No room found!");
            return;
        }
        postValidator = new PostValidatorImpl();
        tvError = findViewById(R.id.tvError);
        etPostTitle = findViewById(R.id.PostTitle);
        etPostDescription = findViewById(R.id.PostDescription);
        btnAddPost = findViewById(R.id.btnAddPost);
        btnAddPost.setOnClickListener(v -> performAddPost(token));
    }

    private void performAddPost(String token) {
        String title = etPostTitle.getText().toString().trim();
        String description = etPostDescription.getText().toString().trim();

        String titleError = postValidator.validatePostTitle(title);
        String descriptionError = postValidator.validatePostDescription(description);

        if (titleError != null) {
            showError(titleError);
            return;
        }

        if (descriptionError != null) {
            showError(descriptionError);
            return;
        }

        PostRequest postRequest = new PostRequest(title, description, roomId);

        ApiClient.getInstance(getApplicationContext()).getApiService().addPost(postRequest).enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                Log.d("AddPostActivity", "Response code: " + response.code());

                if (response.isSuccessful() || response.code() == 201) {
                    if (response.body() != null) {
                        Log.d("AddPostActivity", "Post created successfully: " + response.body().getName());
                        finish();
                    } else {
                        Log.e("AddPostActivity", "Response body is null despite successful status code");
                        showError("Unexpected server response. Please try again.");
                    }
                } else {
                    handleErrorResponse(response);
                }
            }


            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                showError("The server is currently unavailable. Please try again later.");
            }
        });
    }

    private void handleErrorResponse(Response<PostResponse> response) {
        try {
            if (response.errorBody() != null) {
                String errorJson = response.errorBody().string();
                Log.d("AddPostActivity", "Error JSON: " + errorJson);

                Gson gson = new Gson();
                ErrorResponse errorResponse = gson.fromJson(errorJson, ErrorResponse.class);

                if (errorResponse != null && errorResponse.getMessage() != null) {
                    showError("Error: " + errorResponse.getMessage());
                } else {
                    Log.e("AddPostActivity", "Parsed ErrorResponse is null or missing fields.");
                    showError("The server is currently unavailable. Please try again later.");
                }
            } else {
                showError("The server is currently unavailable. Please try again later.");
            }
        } catch (IOException e) {
            Log.e("AddPostActivity", "IO Exception while parsing error response", e);
            showError("The server is currently unavailable. Please try again later.");
        } catch (JsonSyntaxException e) {
            Log.e("AddPostActivity", "JSON syntax error in error response", e);
            showError("The server is currently unavailable. Please try again later.");
        } catch (Exception e) {
            Log.e("AddPostActivity", "Unexpected error", e);
            showError("The server is currently unavailable. Please try again later.");
        }
    }

    private void showError(String message) {
        tvError.setText(message);
        tvError.setVisibility(View.VISIBLE);
    }
}