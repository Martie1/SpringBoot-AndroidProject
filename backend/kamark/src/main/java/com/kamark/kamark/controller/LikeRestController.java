package com.kamark.kamark.controller;

import com.kamark.kamark.service.JWTUtils;
import com.kamark.kamark.service.LikeService;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
public class LikeRestController {

    @Autowired
    private LikeService likeService;

    @Autowired
    private JWTUtils jwtUtils;

    @PostMapping("/{postId}/like")
    public ResponseEntity<String> likePost(@PathVariable Integer postId, @RequestHeader("Authorization") String token) {
        Integer userId = jwtUtils.extractUserId(token);
        if (token == null || token.contains(" ")) {
            throw new MalformedJwtException("Token contains whitespace or is invalid.");
        }
        boolean result = likeService.likePost(postId, userId);
        if (result) {
            return ResponseEntity.ok("Post liked successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to like post");
        }
    }

    @DeleteMapping("/{postId}/like")
    public ResponseEntity<String> unlikePost(@PathVariable Integer postId, @RequestHeader("Authorization") String token) {
        Integer userId = jwtUtils.extractUserId(token);
        boolean result = likeService.unlikePost(postId, userId);
        if (result) {
            return ResponseEntity.ok("Post unliked successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to unlike post");
        }
    }
}