package com.example.pwo.activities;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
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
import com.example.pwo.utils.TokenManager;
import com.google.android.material.navigation.NavigationBarMenu;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RoomActivity extends BaseActivity implements RoomAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private RoomAdapter adapter;
    private List<Room> rooms;
    TokenManager tokenManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_room, findViewById(R.id.main));

        View navigationBarMenu = findViewById(R.id.bottom_navigation);
        TokenManager tokenManager = new TokenManager(getApplicationContext());
        String token = tokenManager.getAccessToken();
        getRooms(token);
    }

    @Override
    public void onItemClick(Room room) {
        Intent intent = new Intent(RoomActivity.this, PostActivity.class);
        intent.putExtra("roomId", room.getId());
        startActivity(intent);
    }

    private void getRooms(String token){
        ApiClient.getInstance().getApiService().getRooms("Bearer "+ token).enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    rooms = response.body();
                    recyclerView = findViewById(R.id.room_recyclerview);
                    adapter = new RoomAdapter(rooms, RoomActivity.this);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(RoomActivity.this));

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