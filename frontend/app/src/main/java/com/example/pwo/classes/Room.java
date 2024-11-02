package com.example.pwo.classes;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private int id;
    private String name;

    public Room(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        rooms.add(new Room(1, "gardening"));
        rooms.add(new Room(2, "cosplay"));
        rooms.add(new Room(3, "halloween"));
        rooms.add(new Room(4, "gaming"));
        rooms.add(new Room(5, "Room 5"));
        return rooms;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

}