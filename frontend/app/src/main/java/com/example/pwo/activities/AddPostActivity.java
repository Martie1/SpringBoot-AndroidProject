package com.example.pwo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.pwo.network.models.SimpleResponse;
import com.example.pwo.utils.TokenManager;
import com.example.pwo.utils.validators.PostValidatorImpl;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPostActivity extends BaseActivity {
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
        getLayoutInflater().inflate(R.layout.activity_add_post, findViewById(R.id.main));
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
        btnAddPost.setOnClickListener(v -> performAddPost());
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_add_post);
    }

    private void performAddPost() {
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

        ApiClient.getInstance(getApplicationContext()).getApiService().addPost(postRequest).enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                Log.d("AddPostActivity", "Response code: " + response.code());

                if (response.isSuccessful() || response.code() == 201) {
                    if (response.body() != null) {
                        SimpleResponse simpleResponse = response.body();
                        Log.d("AddPostActivity", "Message: " + simpleResponse.getMessage());
                        showSuccess(simpleResponse.getMessage());
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Log.e("AddPostActivity", "Response body is null despite successful status code");
                        showError("Unexpected server response. Please try again.");
                    }
                } else if (response.code() == 403) {
                    Log.e("AddPostActivity", "Access denied: " + response.code());
                    showError("Access denied. Please check your permissions.");
                }
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                showError("The server is currently unavailable. Please try again later.");
            }
        });





    }
    private void showError(String message) {
        TextView errorTextView = findViewById(R.id.tvError);
        errorTextView.setText(message);
        errorTextView.setVisibility(View.VISIBLE);
    }

    private void showSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }


}