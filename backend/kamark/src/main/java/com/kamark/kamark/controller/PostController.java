package com.kamark.kamark.controller;
import com.kamark.kamark.dto.PostDTO;
import com.kamark.kamark.entity.Post;
import com.kamark.kamark.entity.Room;
import com.kamark.kamark.entity.User;
import com.kamark.kamark.repository.PostRepository;
import com.kamark.kamark.repository.RoomRepository;
import com.kamark.kamark.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/post")
public class PostController {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    //create a post
    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody PostDTO postDTO) {

        Optional<User> userOptional = userRepository.findById(postDTO.getUserId());
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);//if not exists
        }

        Optional<Room> roomOptional = roomRepository.findById(postDTO.getRoomId());
        if (roomOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); //if not exists
        }

        Post post = new Post();
        post.setName(postDTO.getName());
        post.setDescription(postDTO.getDescription());
        post.setUser(userOptional.get());
        post.setRoom(roomOptional.get());

        Post savedPost = postRepository.save(post);
        return new ResponseEntity<>(savedPost, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    //get, read
    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Integer id) {
        Optional<Post> post = postRepository.findById(id);
        return post.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    //update
    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Integer id, @RequestBody Post postDetails) {
        Optional<Post> postOptional = postRepository.findById(id);

        if (postOptional.isPresent()) {
            Post postToUpdate = postOptional.get();
            postToUpdate.setName(postDetails.getName());
            postToUpdate.setDescription(postDetails.getDescription());
            postToUpdate.setLikes(postDetails.getLikes());
            postToUpdate.setStatus(postDetails.getStatus());
            postToUpdate.setUser(postDetails.getUser());
            postToUpdate.setRoom(postDetails.getRoom());

            Post updatedPost = postRepository.save(postToUpdate);
            return new ResponseEntity<>(updatedPost, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Integer id) {
        if (postRepository.existsById(id)) {
            postRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
