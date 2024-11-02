package com.example.pwo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pwo.R;
import com.example.pwo.classes.Post;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private static List<Post> posts;
    private Context context;
    private static OnItemClickListener listener;

    public PostAdapter(List<Post> posts, Context context) {
        this.posts = posts;
        this.context = context;
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        private TextView tvUsername;
        private TextView tvName;
        private TextView tvDescription;
        private TextView tvLikes;
        private TextView tvCreatedAt;
        private CompoundButton cbLike;
        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvName = itemView.findViewById(R.id.tvName);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvCreatedAt = itemView.findViewById(R.id.tvCreatedAt);
            cbLike = itemView.findViewById(R.id.cbLike);
            if(listener != null) {
                itemView.setOnClickListener(v -> {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(posts.get(position));
                    }
                });
            }
            cbLike.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if(isChecked) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Post post = posts.get(position);
                        post.setLikes(post.getLikes() + 1);
                        buttonView.setText(String.valueOf(post.getLikes()));
                    }
                }
                if(!isChecked) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Post post = posts.get(position);
                        post.setLikes(post.getLikes() - 1);
                        buttonView.setText(String.valueOf(post.getLikes()));
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.tvUsername.setText(post.getUsername());
        holder.tvName.setText(post.getName());
        holder.tvDescription.setText(post.getDescription());
        holder.tvCreatedAt.setText(post.getCreatedAt().toString());
        holder.cbLike.setText(String.valueOf(post.getLikes()));
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Post post);
    }

    public static void setOnItemClickListener(OnItemClickListener listener) {
        PostAdapter.listener = listener;
    }
}
