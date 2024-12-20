package com.kamark.kamark.controller;

import com.kamark.kamark.dto.RoomDTO;
import com.kamark.kamark.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomRestController {
    @Autowired
    private RoomService roomService;

    @GetMapping
    public ResponseEntity<List<RoomDTO>> getAllRooms(Authentication authentication,@RequestHeader("Authorization") String authHeader) {
        return new ResponseEntity<>(roomService.getAllRooms(), HttpStatus.OK);
    }
}