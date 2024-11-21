package com.example.pwo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pwo.R;
import com.example.pwo.classes.Post;
import com.example.pwo.network.ApiClient;
import com.example.pwo.network.models.PostRequest;
import com.example.pwo.network.models.PostResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostEditActivity extends BaseActivity {
    private EditText etTitle, etDescription;
    private Button btnEdit;
    private Post post;
    private PostRequest postRequest;
    private int postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_post_edit, findViewById(R.id.main));

        Intent intent = getIntent();
        if(intent.hasExtra("postId")) {
            postId = intent.getIntExtra("postId", 1);
            fetchPost(postId);
        }else{
            finish();
        }
        
        etTitle = findViewById(R.id.PostTitle);
        etDescription = findViewById(R.id.PostDescription);
        btnEdit = findViewById(R.id.btnEdit);
        
        btnEdit.setOnClickListener(v -> {
            postRequest = new PostRequest(
                    etTitle.getText().toString(),
                    etDescription.getText().toString(),
                    post.getRoomId()
            );
            update();
        });
    }

    private void update() {
        ApiClient.getInstance(getApplicationContext()).getApiService().updatePost(postId, postRequest).enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(PostEditActivity.this, "Post updated successfully", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(PostEditActivity.this, "Failed to update post", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                Toast.makeText(PostEditActivity.this, "Failed to update post", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchPost(int postId) {
        ApiClient.getInstance(getApplicationContext()).getApiService().getPost(postId).enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if(response.isSuccessful()) {
                    post = response.body();
                    etTitle.setText(post.getName());
                    etDescription.setText(post.getDescription());
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Toast.makeText(PostEditActivity.this, "Failed to fetch post", Toast.LENGTH_SHORT).show();
            }
        });
    }
}