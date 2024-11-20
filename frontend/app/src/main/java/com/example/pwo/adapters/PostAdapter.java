package com.example.pwo.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pwo.R;
import com.example.pwo.activities.ReportActivity;
import com.example.pwo.classes.Post;
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
//            cbLike.setOnCheckedChangeListener((buttonView, isChecked) -> {
//                int position = getAdapterPosition();
//                if (position != RecyclerView.NO_POSITION) {
//                    Post post = posts.get(position);
//                    // Uruchomienie LikeActivity
//                    Intent intent = new Intent(itemView.getContext(), LikeActivity.class);
//                    intent.putExtra("postId", post.getId());
//                    itemView.getContext().startActivity(intent);
//                }
//            });

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
