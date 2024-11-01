package com.example.pwo.activities;

import android.os.Bundle;
import android.content.Intent;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pwo.R;
import com.example.pwo.adapters.RoomAdapter;
import com.example.pwo.classes.Room;

import java.util.List;


public class RoomActivity extends AppCompatActivity implements RoomAdapter.OnItemClickListener {

    private List<Room> rooms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_room);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.room), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Get all rooms
        rooms = Room.getAllRooms();
        RecyclerView recyclerView = findViewById(R.id.room_recyclerview);
        RoomAdapter adapter = new RoomAdapter(rooms, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        RoomAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(Room room) {
        Intent intent = new Intent(RoomActivity.this, PostActivity.class);
        intent.putExtra("roomId", room.getId());
        startActivity(intent);
    }
}