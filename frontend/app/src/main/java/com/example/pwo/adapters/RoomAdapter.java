package com.example.pwo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pwo.R;
import com.example.pwo.classes.Room;

import java.util.List;

import android.widget.ImageView;
import android.widget.TextView;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {
    private static List<Room> rooms;
    private Context context;
    private static OnItemClickListener listener;

    public RoomAdapter(List<Room> rooms, Context context) {
        this.rooms = rooms;
        this.context = context;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_room, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room room = rooms.get(position);
        int imageId = context.getResources().getIdentifier(room.getName(), "drawable", context.getPackageName());
        if(imageId == 0) {
            imageId = R.drawable.askadmin;
        }
        holder.roomImage.setImageResource(imageId);
        holder.roomName.setText(room.getName().replaceFirst("" + room.getName().charAt(0), ("" + room.getName().charAt(0)).toUpperCase()));
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Room room);
    }
    public static void setOnItemClickListener(OnItemClickListener listener) {
        RoomAdapter.listener = listener;
    }

    public static class RoomViewHolder extends RecyclerView.ViewHolder{
        private ImageView roomImage;
        private TextView roomName;
        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            roomImage = itemView.findViewById(R.id.imageView);
            roomName = itemView.findViewById(R.id.tvRoomName);
            itemView.setOnClickListener(v -> {
                if(listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(rooms.get(position));
                    }
                }
            });
        }
    }
}
