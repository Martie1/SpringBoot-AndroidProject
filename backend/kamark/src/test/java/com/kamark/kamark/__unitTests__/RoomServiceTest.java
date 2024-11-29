package com.kamark.kamark.service;

import com.kamark.kamark.dto.RoomDTO;
import com.kamark.kamark.entity.RoomEntity;
import com.kamark.kamark.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private RoomService roomService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllRooms_WhenRoomsExist() {
        // Arrange
        RoomEntity room1 = new RoomEntity();
        room1.setId(1);
        room1.setName("Room A");

        RoomEntity room2 = new RoomEntity();
        room2.setId(2);
        room2.setName("Room B");

        when(roomRepository.findAll()).thenReturn(Arrays.asList(room1, room2));

        // Act
        List<RoomDTO> result = roomService.getAllRooms();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Room A", result.get(0).getName());
        assertEquals("Room B", result.get(1).getName());


        verify(roomRepository, times(1)).findAll();
    }

    @Test
    void testGetAllRooms_WhenNoRoomsExist() {
        // Arrange
        when(roomRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<RoomDTO> result = roomService.getAllRooms();

        // Assert
        assertEquals(0, result.size());
        verify(roomRepository, times(1)).findAll();
    }
}
