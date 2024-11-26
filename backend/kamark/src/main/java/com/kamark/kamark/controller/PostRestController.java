package com.kamark.kamark.controller;
import com.kamark.kamark.dto.CreatePostDTO;
import com.kamark.kamark.dto.ErrorResponse;
import com.kamark.kamark.dto.PostResponseDTO;
import com.kamark.kamark.dto.SimpleResponse;
import com.kamark.kamark.entity.PostEntity;
import com.kamark.kamark.service.JWTUtils;
import com.kamark.kamark.service.LikeService;
import com.kamark.kamark.service.PostService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostRestController {
    private static final Logger logger = LoggerFactory.getLogger(PostRestController.class);

    @Autowired
    private PostService postService;
    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private LikeService likeService;

    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<PostResponseDTO>> getPostsByRoomId(@PathVariable Integer roomId,@RequestHeader("Authorization") String authHeader) {

        List<PostResponseDTO> posts = postService.getPostsByRoomId(roomId);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createPost(
            @RequestBody @Valid CreatePostDTO createPostDTO,
            @RequestHeader("Authorization") String authHeader) {

        Integer userId = jwtUtils.extractUserIdFromAuthorizationHeader(authHeader);

        if (userId == null) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Invalid token");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }


        logger.info("User ID create: " + userId);

        PostResponseDTO createdPost = postService.createPostAndReturnResponse(createPostDTO, userId);

        SimpleResponse response = new SimpleResponse(HttpStatus.CREATED.value(), "Post created successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }


    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> getPostById(@PathVariable Integer id,@RequestHeader("Authorization") String authHeader) {
        PostResponseDTO postDTO = postService.getPostById(id);
        return new ResponseEntity<>(postDTO, HttpStatus.OK);
    }
    @PutMapping("/{id}")
    public ResponseEntity<SimpleResponse> updatePost(
            @PathVariable Integer id,
            @RequestBody PostResponseDTO postResponseDTO,
            @RequestHeader("Authorization") String authHeader) {

        Integer userId = jwtUtils.extractUserIdFromAuthorizationHeader(authHeader);

        PostEntity updatedPost = postService.updatePost(id, postResponseDTO, userId);

            SimpleResponse response = new SimpleResponse(
                    HttpStatus.OK.value(),
                    "The post has been successfully updated");
            return ResponseEntity.ok(response);

    }



    @DeleteMapping("/{id}")
    public ResponseEntity<SimpleResponse> deletePost(
            @PathVariable Integer id,
            @RequestHeader("Authorization") String authHeader) {

        Integer userId = jwtUtils.extractUserIdFromAuthorizationHeader(authHeader);

        boolean likesDeleted = likeService.deleteLikesByPostId(id);
        boolean isDeleted = postService.deletePost(id, userId);

        if (likesDeleted &&isDeleted ){
            SimpleResponse response = new SimpleResponse(
                    HttpStatus.OK.value(),
                    "The post has been successfully deleted"
            );
            return ResponseEntity.ok(response);
        } else {
            SimpleResponse response = new SimpleResponse(
                    HttpStatus.NOT_FOUND.value(),
                    "Post not found or unauthorized"
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }


}

