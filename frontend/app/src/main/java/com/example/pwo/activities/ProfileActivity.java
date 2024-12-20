package com.example.pwo.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pwo.R;
import com.example.pwo.adapters.PostAdapter;
import com.example.pwo.classes.Post;
import com.example.pwo.classes.User;
import com.example.pwo.network.ApiClient;
import com.example.pwo.utils.TokenManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends BaseActivity implements PostAdapter.OnItemClickListener {
    private User user;
    private TextView tvUsername;
    private TextView tvEmail;
    private Button btnLogout;
    TokenManager tokenManager;
    private List<Post> posts;
    private PostAdapter adapter;
    private RecyclerView recyclerView;
    private int[] likedPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_profile, findViewById(R.id.main));

        tvUsername = findViewById(R.id.tvUsername);
        tvEmail = findViewById(R.id.tvEmail);
        btnLogout = findViewById(R.id.btnLogout);
        tokenManager = new TokenManager(getApplicationContext());

        posts = new ArrayList<>();
        setUpRecyclerView();

        fetchPosts();
        fetchUserDetails();


        btnLogout.setOnClickListener(v -> logout());
    }



    private void logout(){
        tokenManager.clearTokens();
        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    private void setUpRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new PostAdapter(posts, ProfileActivity.this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ProfileActivity.this));
        PostAdapter.setOnItemClickListener(ProfileActivity.this);
    }

    private void fetchUserDetails() {
        ApiClient.getInstance(getApplicationContext()).getApiService().getUser().enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    user = response.body();
                    tvUsername.setText(user.getUsername());
                    tvEmail.setText(user.getEmail());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                fetchPosts();
            }
        }
    }

    @Override
    public void onItemClick(Post post) {
        Intent intent = new Intent(ProfileActivity.this,PostEditActivity.class);
        intent.putExtra("postId", post.getId());
        startActivityForResult(intent, 1);
    }

    private void fetchPosts() {
        ApiClient.getInstance(getApplicationContext()).getApiService().getUserPosts().enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    posts = response.body();
                    adapter.setPosts(posts);
                    adapter.notifyDataSetChanged();
                    fetchLikes();
                } else {
                    Log.e("PostActivity", "onResponse: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Log.e("PostActivity", "onFailure: ", t);
            }
        });
    }

    private void fetchLikes(){
        ApiClient.getInstance(getApplicationContext()).getApiService().getLikes().enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    if(response.body().size() == 0){
                        return;
                    }
                    likedPosts = new int[response.body().size()];
                    for(int i = 0; i < response.body().size(); i++){
                        likedPosts[i] = response.body().get(i).getId();
                    }
                    adapter.setLikedPosts(likedPosts);
                    Log.d("PostActivity", "onResponse: " + likedPosts);
                }else{
                    Log.e("PostActivity", "onResponse: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Log.e("PostActivity", "onFailure: ", t);
            }
        });
    }
}