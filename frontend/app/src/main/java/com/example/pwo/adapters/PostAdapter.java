package com.example.pwo.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pwo.R;
import com.example.pwo.activities.ReportActivity;
import com.example.pwo.classes.Post;
import com.example.pwo.network.ApiClient;
import com.example.pwo.network.ApiService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private static List<Post> posts;
    private DateFormat dateFormat;
    private Context context;
    private static OnItemClickListener listener;


    public PostAdapter(List<Post> posts, Context context) {
        PostAdapter.posts = posts;
        this.context = context;
        this.dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.UK);
    }

    public void setPosts(List<Post> posts) {
        PostAdapter.posts = posts;
        notifyDataSetChanged();
    }

    public void setLikedPosts(int[] likedPosts) {
        for (int i = 0; i < posts.size(); i++) {
            for (int likedPost : likedPosts) {
                if (posts.get(i).getId() == likedPost) {
                    posts.get(i).setLiked(true);
                }
            }
        }
        notifyDataSetChanged();
    }

    /*public void onSetLikedPosts(@NonNull PostViewHolder holder,int position){
        if(posts.get(position).isLiked()){
            holder.cbLike.setChecked(true);
        }
    }*/



    public static class PostViewHolder extends RecyclerView.ViewHolder {
        private TextView tvUsername;
        private TextView tvName;
        private TextView tvDescription;
        private TextView tvLikes;
        private TextView tvCreatedAt;
        private CompoundButton cbLike;
        private Button btnReport;
        private ApiService apiService;
        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            this.apiService = apiService;
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvName = itemView.findViewById(R.id.tvName);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvCreatedAt = itemView.findViewById(R.id.tvCreatedAt);
            cbLike = itemView.findViewById(R.id.cbLike);
            btnReport = itemView.findViewById(R.id.btnReport);
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
                        createLike(post.getId());
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
                        deleteLike(post.getId());
                    }
                }
            });

            btnReport.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Post post = posts.get(position);
                    // titaj jest  intent do ReportActivity i pzheakzania postId
                    Intent intent = new Intent(itemView.getContext(), ReportActivity.class);
                    intent.putExtra("postId", post.getId());
                    itemView.getContext().startActivity(intent);
                }
            });
        }

        private void deleteLike(int id) {
            ApiClient.getInstance(itemView.getContext()).getApiService().DeleteLike(id).enqueue(new retrofit2.Callback<Void>() {
                @Override
                public void onResponse(retrofit2.Call<Void> call, retrofit2.Response<Void> response) {
                    if(response.isSuccessful()) {
                        Log.d("PostAdapter", "onResponse: Deleted like");
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<Void> call, Throwable t) {
                    Toast.makeText(itemView.getContext(), "Failed to delete like", Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void createLike(int id) {
            ApiClient.getInstance(itemView.getContext()).getApiService().CreateLike(id).enqueue(new retrofit2.Callback<Void>() {
                @Override
                public void onResponse(retrofit2.Call<Void> call, retrofit2.Response<Void> response) {
                    if(response.isSuccessful()) {
                        Log.d("PostAdapter", "onResponse: Liked post");
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<Void> call, Throwable t) {
                    Toast.makeText(itemView.getContext(), "Failed to like post", Toast.LENGTH_SHORT).show();
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
        holder.tvCreatedAt.setText(dateFormat.format(post.getCreatedAt()));
        holder.cbLike.setText(String.valueOf(post.getLikes()));
        holder.cbLike.setChecked(post.isLiked());
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
