package com.example.pwo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pwo.R;
import com.example.pwo.classes.Post;
import com.example.pwo.network.ApiClient;
import com.example.pwo.utils.TokenManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SinglePostActivity extends BaseActivity {

    private Post post;
    private TextView tvUsername;
    private TextView tvTitle;
    private TextView tvDescription;
    private TextView tvCreatedAt;
    private CompoundButton tvLikes;
    private int likes = 0;
    private ImageView ivRoomImage;
    TokenManager tokenManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_single_post, findViewById(R.id.main));

        tvUsername = findViewById(R.id.tvUsername);
        tvTitle = findViewById(R.id.tvTitle);
        tvDescription = findViewById(R.id.tvDescription);
        tvCreatedAt = findViewById(R.id.tvDate);
        tvLikes = findViewById(R.id.cbLike);
        ivRoomImage = findViewById(R.id.ivRoomImage);

        TokenManager tokenManager = new TokenManager(getApplicationContext());
        String token =tokenManager.getAccessToken();

        Intent intent = getIntent();
        if(intent.hasExtra("postId")) {
            int postId = intent.getIntExtra("postId", 1);
            fetchPost(postId,token);
        }
        tvLikes.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                likes += 1;
                post.setLikes(likes);
                buttonView.setText(String.valueOf(likes));
            }else{
                likes -= 1;
                post.setLikes(likes);
                buttonView.setText(String.valueOf(likes));
            }
        });
    }

    private void fetchPost(int postId,String token) {
        ApiClient.getInstance(getApplicationContext()).getApiService().getPost(postId).enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if(response.isSuccessful()) {
                    post = response.body();

                    tvUsername.setText(post.getUsername());
                    tvTitle.setText(post.getName());
                    tvDescription.setText(post.getDescription());
                    tvCreatedAt.setText(post.getCreatedAt().toString());
                    likes = post.getLikes();
                    tvLikes.setText(String.valueOf(likes));
                }
                else {
                    Log.e("SinglePostActivity", "Failed to fetch post with id: " + postId);
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Log.e("SinglePostActivity", "Failed to fetch post with id: " + postId);
            }
        });
    }
}