package com.kamark.kamark.service;

import com.kamark.kamark.dto.CreateRoomDTO;
import com.kamark.kamark.dto.RoomDTO;
import com.kamark.kamark.entity.RoomEntity;
import com.kamark.kamark.repository.RoomRepository;
import com.kamark.kamark.service.interfaces.RoomServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;

@Service
public class RoomService implements RoomServiceInterface {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }
    public List<RoomDTO> getAllRooms() {
        List<RoomEntity> rooms = roomRepository.findAll();
        return rooms.stream()
                .map(this::mapToDTO)
                .toList();
    }
    public RoomEntity createRoom(CreateRoomDTO roomDTO) {
        if(roomRepository.existsByName(roomDTO.getName())) {
            throw new IllegalArgumentException("Room with name " + roomDTO.getName() + " already exists");
        }
        RoomEntity room = new RoomEntity();
        room.setName(roomDTO.getName());
        return roomRepository.save(room);
    }

    private RoomDTO mapToDTO(RoomEntity room) {
        RoomDTO dto = new RoomDTO();
        dto.setId(room.getId());
        dto.setName(room.getName());
        return dto;
    }
}