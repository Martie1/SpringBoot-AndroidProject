package com.kamark.kamark.service;

import com.kamark.kamark.dto.RoomDTO;
import com.kamark.kamark.entity.Room;
import com.kamark.kamark.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;
    public List<RoomDTO> getAllRooms() {
        List<Room> rooms = roomRepository.findAll();
        return rooms.stream()
                .map(this::mapToDTO)
                .toList();
    }

    private RoomDTO mapToDTO(Room room) {
        RoomDTO dto = new RoomDTO();
        dto.setId(room.getId());
        dto.setName(room.getName());
        return dto;
    }
}