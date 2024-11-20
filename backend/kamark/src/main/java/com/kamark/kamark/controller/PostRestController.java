package com.kamark.kamark.controller;
import com.kamark.kamark.dto.CreatePostDTO;
import com.kamark.kamark.dto.ErrorResponse;
import com.kamark.kamark.dto.PostResponseDTO;
import com.kamark.kamark.entity.PostEntity;
import com.kamark.kamark.service.JWTUtils;
import com.kamark.kamark.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
public class PostRestController {
    private static final Logger logger = LoggerFactory.getLogger(PostRestController.class);

    @Autowired
    private PostService postService;
    @Autowired
    private JWTUtils jwtUtils;

    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<PostResponseDTO>> getPostsByRoomId(@PathVariable Integer roomId,@RequestHeader("Authorization") String authHeader) {

        List<PostResponseDTO> posts = postService.getPostsByRoomId(roomId);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createPost(
            @RequestBody CreatePostDTO createPostDTO,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        Integer userId = jwtUtils.extractUserId(token);

        if (userId == null) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Invalid token");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        logger.info("User ID create: " + userId);

        Optional<PostResponseDTO> createdPost = postService.createPostAndReturnResponse(createPostDTO, userId);

        if (createdPost.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPost.get());
        } else {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Failed to create post");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> getPostById(@PathVariable Integer id,@RequestHeader("Authorization") String authHeader) {
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

        Optional<PostEntity> updatedPost = postService.updatePost(id, postResponseDTO, userId);
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

