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
        rooms.add(new Room(5, "books"));
        rooms.add(new Room(6, "beauty"));
        rooms.add(new Room(7, "music"));
        rooms.add(new Room(8, "science"));
        rooms.add(new Room(9, "sports"));
        rooms.add(new Room(10, "wellness"));
        rooms.add(new Room(11, "travel"));
        rooms.add(new Room(12, "askadmin"));
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

}