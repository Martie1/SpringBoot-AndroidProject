package com.example.pwo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pwo.R;
import com.example.pwo.classes.Post;
import com.example.pwo.network.ApiClient;

import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportedPostsAdapter extends RecyclerView.Adapter<ReportedPostsAdapter.PostViewHolder> {
    private List<Post> posts;
    private DateFormat dateFormat;
    private WeakReference<Context> contextRef;
    private OnItemClickListener listener;

    public ReportedPostsAdapter(List<Post> posts, Context context) {
        this.posts = posts;
        this.contextRef = new WeakReference<>(context);
        this.dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        private TextView tvUsername;
        private TextView tvName;
        private TextView tvDescription;
        private TextView tvCreatedAt;
        private Button btnDismiss, btnResolve;
        private WeakReference<Context> contextRef;

        public PostViewHolder(@NonNull View itemView, WeakReference<Context> contextRef) {
            super(itemView);
            this.contextRef = contextRef;
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvName = itemView.findViewById(R.id.tvName);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvCreatedAt = itemView.findViewById(R.id.tvCreatedAt);
            btnDismiss = itemView.findViewById(R.id.btnDismiss);
            btnResolve = itemView.findViewById(R.id.btnResolve);

            if (listener != null) {
                itemView.setOnClickListener(v -> {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(posts.get(position));
                    }
                });
            }

            btnResolve.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Post post = posts.get(position);
                    resolveReport(post.getId());
                }
            });

            btnDismiss.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Post post = posts.get(position);
                    dismissReport(post.getId());
                }
            });
        }

        private void resolveReport(int postId) {
            Context context = contextRef.get();
            if (context != null) {
                ApiClient.getInstance(context).getApiService().resolveReport(postId).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(context, "Report resolved successfully!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Failed to resolve report.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

        private void dismissReport(int postId) {
            Context context = contextRef.get();
            if (context != null) {
                ApiClient.getInstance(context).getApiService().dismissReport(postId).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(context, "Report dismissed successfully!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Failed to dismiss report.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(contextRef.get()).inflate(R.layout.item_admin_post, parent, false);
        return new PostViewHolder(view, contextRef);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.tvUsername.setText(post.getUsername());
        holder.tvName.setText(post.getName());
        holder.tvDescription.setText(post.getDescription());
        holder.tvCreatedAt.setText(dateFormat.format(post.getCreatedAt()));
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Post post);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}