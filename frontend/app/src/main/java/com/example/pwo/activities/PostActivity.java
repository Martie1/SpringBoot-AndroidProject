package com.example.pwo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pwo.R;
import com.example.pwo.adapters.PostAdapter;
import com.example.pwo.classes.Post;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PostActivity extends AppCompatActivity implements PostAdapter.OnItemClickListener {
    private List<Post> posts;
    private int roomId = 1;
    private Button addPostButton;
    private PostAdapter adapter;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_post);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        if(intent.hasExtra("roomId")) {
            int roomId = intent.getIntExtra("roomId", 1);
            // fetchPosts(roomId) // fetch posts from the server
            posts = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                posts.add(new Post(i, new Date(), "Description " + i, "Name " + i, i, "Status " + i, roomId, i));
            }
        }
        else {
            Toast.makeText(this, "No Posts found in this room!", Toast.LENGTH_SHORT).show();
        }

        recyclerView = findViewById(R.id.recyclerView);
        adapter = new PostAdapter(posts, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        PostAdapter.setOnItemClickListener(this);

        addPostButton = findViewById(R.id.btnAddPost);
        addPostButton.setOnClickListener(v -> {
            Intent addPostIntent = new Intent(PostActivity.this, AddPostActivity.class);
            addPostIntent.putExtra("roomId", roomId);
            startActivityForResult(addPostIntent, 1);
        });
    }

    @Override
    public void onItemClick(Post post) {
        Toast.makeText(this, "Post " + post.getName() + " clicked!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                posts.add(new Post(posts.size(), new Date(), data.getStringExtra("title"), data.getStringExtra("description"), 0, "Status", roomId, 0));
                adapter.notifyDataSetChanged();
                // fetchPosts(roomId) // fetch posts from the server
            }
        }
    }

    private void fetchPosts(int roomId) {
        // fetch posts from the server
    }
}