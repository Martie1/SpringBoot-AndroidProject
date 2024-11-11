package com.kamark.kamark.controller;

import com.kamark.kamark.dto.PostResponseDTO;
import com.kamark.kamark.dto.UserProfileDTO;
import com.kamark.kamark.service.JWTUtils;
import com.kamark.kamark.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JWTUtils jwtUtils;

    @GetMapping("/profile")
    public ResponseEntity<UserProfileDTO> getUserProfile(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        String token = authHeader.substring(7);
        Integer userId = jwtUtils.extractUserId(token);

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        UserProfileDTO userProfile = userService.getUserProfile(userId);
        return ResponseEntity.ok(userProfile);
    }
    @PatchMapping("/profile")
    public ResponseEntity<String> updateUserProfile(
            @RequestBody UserProfileDTO userProfileDTO,
            @RequestHeader("Authorization") String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);
        Integer userId = jwtUtils.extractUserId(token);

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        boolean isUpdated = userService.updateUserProfile(userId, userProfileDTO);
        if (isUpdated) {
            return ResponseEntity.ok("Profile updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update profile");
        }
    }
    @DeleteMapping("/deactivate")
    public ResponseEntity<String> deactivateAccount(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);
        Integer userId = jwtUtils.extractUserId(token);

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        boolean isDeactivated = userService.deactivateAccount(userId);
        if (isDeactivated) {
            return ResponseEntity.ok("Account deactivated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to deactivate account");
        }
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostResponseDTO>> getUserPosts(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        Integer userId = jwtUtils.extractUserId(token);

        List<PostResponseDTO> userPosts = userService.getUserPosts(userId);
        return ResponseEntity.ok(userPosts);
    }

    @GetMapping("/likes")
    public ResponseEntity<List<PostResponseDTO>> getUserLikes(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        Integer userId = jwtUtils.extractUserId(token);

        List<PostResponseDTO> likedPosts = userService.getUserLikes(userId);
        return ResponseEntity.ok(likedPosts);
    }
}

