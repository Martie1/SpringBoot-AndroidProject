package com.kamark.kamark.controller;
import com.kamark.kamark.dto.PostDTO;
import com.kamark.kamark.entity.Post;
import com.kamark.kamark.entity.Room;
import com.kamark.kamark.entity.User;
import com.kamark.kamark.repository.PostRepository;
import com.kamark.kamark.repository.RoomRepository;
import com.kamark.kamark.repository.UserRepository;
import com.kamark.kamark.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    @Autowired
    private PostService postService;

    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<PostDTO>> getPostsByRoomId(@PathVariable Integer roomId) {
        List<PostDTO> posts = postService.getPostsByRoomId(roomId);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> createPost(@RequestBody PostDTO postDTO) {
        boolean isCreated = postService.createPost(postDTO);
        if (isCreated) {
            return new ResponseEntity<>("A post has been successfully created", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Failed to create post", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable Integer id) {
        Optional<PostDTO> postDTO = postService.getPostById(id);
        return postDTO.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @PutMapping("/{id}")
    public ResponseEntity<String> updatePost(@PathVariable Integer id, @RequestBody PostDTO postDTO) {
        Optional<Post> updatedPost = postService.updatePost(id, postDTO);
        if (updatedPost.isPresent()) {
            return new ResponseEntity<>("The post has been successfully updated", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Post not found", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Integer id) {

            boolean isDeleted = postService.deletePost(id);
            if (isDeleted) {
                return new ResponseEntity<>("The post has been successfully deleted", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Post not found", HttpStatus.NOT_FOUND);
            }
       /* } catch (AccessDeniedException e) {
            return new ResponseEntity<>("You are not authorized to delete this post", HttpStatus.FORBIDDEN);
        }*/
        }


    }

