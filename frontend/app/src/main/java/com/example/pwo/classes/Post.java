package com.example.pwo.classes;

import java.util.Date;
import java.util.List;

public class Post {
    private int id;
    private Date createdAt;
    private String description;
    private String name;
    private int likes;
    private String status;
    private int room_id;
    private int user_id;

    public Post(int id, Date createdAt, String description, String name, int likes, String status, int room_id, int user_id) {
        this.id = id;
        this.createdAt = createdAt;
        this.description = description;
        this.name = name;
        this.likes = likes;
        this.status = status;
        this.room_id = room_id;
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getLikes() { return likes; }

    public String  getStatus() {
        return status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getUsername() {
        return User.getUsername(user_id);
    }

    public void setLikes(int i) {
        likes = i;
        // update likes in the database
    }
}

