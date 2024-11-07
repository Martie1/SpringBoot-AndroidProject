package com.example.pwo.activities;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
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
import com.example.pwo.adapters.RoomAdapter;
import com.example.pwo.classes.Room;
import com.example.pwo.network.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RoomActivity extends AppCompatActivity implements RoomAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private RoomAdapter adapter;
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


        getRooms();
    }

    @Override
    public void onItemClick(Room room) {
        Intent intent = new Intent(RoomActivity.this, PostActivity.class);
        intent.putExtra("roomId", room.getId());
        startActivity(intent);
    }

    private void getRooms(){
        ApiClient.getInstance().getApiService().getRooms().enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    rooms = response.body();
                    recyclerView = findViewById(R.id.room_recyclerview);
                    adapter = new RoomAdapter(rooms, RoomActivity.this);
                    recyclerView.setLayoutManager(new LinearLayoutManager(RoomActivity.this));
                    recyclerView.setAdapter(adapter);

                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
                    recyclerView.addItemDecoration(dividerItemDecoration);
                    RoomAdapter.setOnItemClickListener(RoomActivity.this);
                }else{
                    Log.e("RoomActivity", "onResponse: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable t) {
                Log.e("RoomActivity", "onFailure: ", t);
            }
        });
    }
}