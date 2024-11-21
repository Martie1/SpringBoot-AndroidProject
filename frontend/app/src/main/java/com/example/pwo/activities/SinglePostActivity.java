package com.example.pwo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pwo.R;
import com.example.pwo.classes.Post;
import com.example.pwo.network.ApiClient;
import com.example.pwo.utils.TokenManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SinglePostActivity extends BaseActivity {
    private DateFormat dateFormat;

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
        dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.UK);

        tvUsername = findViewById(R.id.tvUsername);
        tvTitle = findViewById(R.id.tvTitle);
        tvDescription = findViewById(R.id.tvDescription);
        tvCreatedAt = findViewById(R.id.tvDate);
        tvLikes = findViewById(R.id.cbLike);
        ivRoomImage = findViewById(R.id.ivRoomImage);
        tokenManager = new TokenManager(getApplicationContext());
        String token =tokenManager.getAccessToken();

        Intent intent = getIntent();
        if(intent.hasExtra("postId")) {
            int postId = intent.getIntExtra("postId", 1);
            fetchPost(postId,token);
            fetchLikes();
        }
        tvLikes.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                likes += 1;
                post.setLikeCount(likes);
                buttonView.setText(String.valueOf(likes));
                createLike(post.getId());
            }else{
                likes -= 1;
                post.setLikeCount(likes);
                buttonView.setText(String.valueOf(likes));
                deleteLike(post.getId());
            }
        });
    }

    private void fetchLikes(){
        ApiClient.getInstance(getApplicationContext()).getApiService().getLikes().enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    List<Post> posts = response.body();
                    if(post != null) {
                        for(Post p : posts) {
                            if(p.getId() == post.getId()) {
                                likes -= 1;
                                tvLikes.setChecked(true);
                            }
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Log.e("SinglePostActivity", "Failed to fetch likes");
            }
        });
    }

    private void deleteLike(int id) {
        ApiClient.getInstance(getApplicationContext()).getApiService().DeleteLike(id).enqueue(new retrofit2.Callback<Void>() {
            @Override
            public void onResponse(retrofit2.Call<Void> call, retrofit2.Response<Void> response) {
                if(response.isSuccessful()) {
                    Log.d("PostAdapter", "onResponse: Deleted like");
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Void> call, Throwable t) {
                Toast.makeText(SinglePostActivity.this, "Failed to delete like", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createLike(int id) {
        ApiClient.getInstance(getApplicationContext()).getApiService().CreateLike(id).enqueue(new retrofit2.Callback<Void>() {
            @Override
            public void onResponse(retrofit2.Call<Void> call, retrofit2.Response<Void> response) {
                if(response.isSuccessful()) {
                    Log.d("PostAdapter", "onResponse: Liked post");
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Void> call, Throwable t) {
                Toast.makeText(SinglePostActivity.this, "Failed to like post", Toast.LENGTH_SHORT).show();
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
                    tvCreatedAt.setText(dateFormat.format(post.getCreatedAt()));
                    likes = post.getLikeCount();
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