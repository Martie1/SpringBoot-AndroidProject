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

public class AddPostActivity extends AppCompatActivity {
    private Button btnAddPost;
    private String title;
    private String description;
    private int roomId;
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

        Intent intent = getIntent();
        if(intent.hasExtra("roomId")) {
            roomId = intent.getIntExtra("roomId", 1);
        }
        else {
            Toast.makeText(this, "No room found!", Toast.LENGTH_SHORT).show();
        }

        btnAddPost = findViewById(R.id.btnAddPost);
        btnAddPost.setOnClickListener(v -> {
            validateTitleAndDescription();
        });
    }

    private void validateTitleAndDescription() {
        title = ((EditText) findViewById(R.id.PostTitle)).getText().toString();
        description = ((EditText) findViewById(R.id.PostDescription)).getText().toString();
        if(title.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Title and Description cannot be empty!", Toast.LENGTH_SHORT).show();
        }
        else {
            //addPost(title, description);
            Intent resultIntent = new Intent();
            resultIntent.putExtra("title", title);
            resultIntent.putExtra("description", description);
            setResult(RESULT_OK, resultIntent);
            finish();
        }
    }

    private void addPost(String title, String description) {
        // Add post to the server
    }
}