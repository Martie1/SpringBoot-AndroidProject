package com.kamark.kamark.service;

import com.kamark.kamark.entity.Room;
import com.kamark.kamark.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;
    public List<Room> getAllRooms() {
        return roomRepository.findAll();  // Zwracamy bezpośrednio encje Room
    }
}