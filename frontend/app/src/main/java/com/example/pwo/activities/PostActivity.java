package com.example.pwo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pwo.R;
import com.example.pwo.adapters.PostAdapter;
import com.example.pwo.classes.Post;
import com.example.pwo.classes.User;
import com.example.pwo.network.ApiClient;
import com.example.pwo.utils.TokenManager;
import com.example.pwo.utils.validators.LoginValidator;
import com.example.pwo.utils.validators.PostValidatorImpl;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostActivity extends BaseActivity implements PostAdapter.OnItemClickListener {
    private List<Post> posts;
    private int roomId = 1;
    private PostAdapter adapter;
    private RecyclerView recyclerView;
    TokenManager tokenManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_post, findViewById(R.id.main));
        TokenManager tokenManager = new TokenManager(getApplicationContext());
        String token =tokenManager.getAccessToken();


        Intent intent = getIntent();
        if(intent.hasExtra("roomId")) {
            int roomId = intent.getIntExtra("roomId", 1);
            fetchPosts(roomId,token);
        }
        else {
            Toast.makeText(this, "No Posts found in this room!", Toast.LENGTH_SHORT).show();
        }


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.getMenu().findItem(R.id.nav_add_post).setVisible(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_add_post) {
                Intent addPostIntent = new Intent(PostActivity.this, AddPostActivity.class);
                addPostIntent.putExtra("roomId", roomId);
                startActivityForResult(addPostIntent, 1);
                return true;
            }else if (item.getItemId() == R.id.nav_room) {
                Intent homeIntent = new Intent(PostActivity.this, RoomActivity.class);
                startActivity(homeIntent);
                return true;
            } else if(item.getItemId() == R.id.nav_profile) {
                Intent profileIntent = new Intent(PostActivity.this, ProfileActivity.class);
                startActivity(profileIntent);
                return true;
            }
            return false;
        });
        bottomNavigationView.setSelectedItemId(0);
    }

    @Override
    public void onItemClick(Post post) {
        Intent intent = new Intent(PostActivity.this, SinglePostActivity.class);
        intent.putExtra("postId", post.getId());
        intent.putExtra("roomId", roomId);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                posts.add(new Post(posts.size(), new Date(), data.getStringExtra("title"), data.getStringExtra("description"), 0, "Status", roomId, 0, "User"));
                adapter.notifyDataSetChanged();
                // fetchPosts(roomId) // fetch posts from the server
            }
        }
    }

    private void fetchPosts(int roomId,String token) {
        ApiClient.getInstance(getApplicationContext()).getApiService().getPosts(roomId).enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    posts = response.body();
                    recyclerView = findViewById(R.id.recyclerView);

                    Log.d("PostActivity", "onResponse: " + posts);

                    adapter = new PostAdapter(posts, PostActivity.this);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(PostActivity.this));
                    Log.d("adapter", "onResponse: " + adapter);
                    PostAdapter.setOnItemClickListener(PostActivity.this);
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