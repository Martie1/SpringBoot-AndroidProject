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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SinglePostActivity extends AppCompatActivity {

    private Post post;
    private TextView tvUsername;
    private TextView tvTitle;
    private TextView tvDescription;
    private TextView tvCreatedAt;
    private CompoundButton tvLikes;
    private int likes = 0;
    private ImageView ivRoomImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_single_post);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvUsername = findViewById(R.id.tvUsername);
        tvTitle = findViewById(R.id.tvTitle);
        tvDescription = findViewById(R.id.tvDescription);
        tvCreatedAt = findViewById(R.id.tvDate);
        tvLikes = findViewById(R.id.cbLike);
        ivRoomImage = findViewById(R.id.ivRoomImage);

        Intent intent = getIntent();
        if(intent.hasExtra("postId")) {
            int postId = intent.getIntExtra("postId", 1);
            fetchPost(postId);
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

    private void fetchPost(int postId) {
        ApiClient.getInstance().getApiService().getPost(postId).enqueue(new Callback<Post>() {
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