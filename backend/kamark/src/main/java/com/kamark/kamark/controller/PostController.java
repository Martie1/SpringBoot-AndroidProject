package com.kamark.kamark.controller;
import com.kamark.kamark.dto.CreatePostDTO;
import com.kamark.kamark.dto.PostResponseDTO;
import com.kamark.kamark.entity.Post;
import com.kamark.kamark.service.JWTUtils;
import com.kamark.kamark.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    @Autowired
    private PostService postService;
    @Autowired
    private JWTUtils jwtUtils;

    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<PostResponseDTO>> getPostsByRoomId(@PathVariable Integer roomId) {
        List<PostResponseDTO> posts = postService.getPostsByRoomId(roomId);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> createPost(
            @RequestBody CreatePostDTO createPostDTO,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        Integer userId = jwtUtils.extractUserId(token);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token");
        }

        boolean isCreated = postService.createPost(createPostDTO, userId);
        if (isCreated) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Post has been successfully created");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create post");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> getPostById(@PathVariable Integer id) {
        Optional<PostResponseDTO> postDTO = postService.getPostById(id);
        return postDTO.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @PutMapping("/{id}")
    public ResponseEntity<String> updatePost(
            @PathVariable Integer id,
            @RequestBody PostResponseDTO postResponseDTO,
            @RequestHeader("Authorization") String authHeader) {


        String token = authHeader.substring(7);
        Integer userId = jwtUtils.extractUserId(token);

        Optional<Post> updatedPost = postService.updatePost(id, postResponseDTO, userId);
        if (updatedPost.isPresent()) {
            return ResponseEntity.ok("The post has been successfully updated");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found or unauthorized");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(
            @PathVariable Integer id,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        Integer userId = jwtUtils.extractUserId(token);

        boolean isDeleted = postService.deletePost(id, userId);
        if (isDeleted) {
            return ResponseEntity.ok("The post has been successfully deleted");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found or unauthorized");
        }
    }

    }

