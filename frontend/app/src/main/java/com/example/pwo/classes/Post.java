package com.example.pwo.classes;

import java.util.Date;

public class Post {
    private int id;
    private Date createdAt;
    private String description;
    private String name;
    private int likeCount;
    private String status;
    private int roomId;
    private int userId;
    private String username;
    private boolean liked;

    public Post(int id, Date createdAt, String description, String name, int likeCount, String status, int room_id, int userId, String username) {
        this.id = id;
        this.createdAt = createdAt;
        this.description = description;
        this.name = name;
        this.likeCount = likeCount;
        this.status = status;
        this.roomId = room_id;
        this.userId = userId;
        this.username = username;
    }

    public Post() {}

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getLikeCount() { return likeCount; }

    public String  getStatus() {
        return status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getUsername() {
        return username;
    }

    public void setLikeCount(int i) {
        likeCount = i;
    }

    public int getUserId() {
        return userId;
    }

    public int getId() {
        return id;
    }

    public void setLiked(boolean b) {
        liked = b;
    }

    public boolean isLiked() {
        return liked;
    }

    public Integer getRoomId() {
        return roomId;
    }
}

