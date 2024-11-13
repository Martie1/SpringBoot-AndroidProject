package com.example.pwo.network.models;

public class PostRequest {
    private String name;
    private String description;
    private Integer roomId;

    public PostRequest(String name, String description, Integer roomId) {
        this.name = name;
        this.description = description;
        this.roomId = roomId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }
}