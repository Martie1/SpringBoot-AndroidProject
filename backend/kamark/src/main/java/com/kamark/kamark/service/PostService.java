package com.kamark.kamark.service;

import com.kamark.kamark.dto.PostDTO;
import com.kamark.kamark.entity.Post;
import com.kamark.kamark.entity.Room;
import com.kamark.kamark.entity.User;
import com.kamark.kamark.repository.PostRepository;
import com.kamark.kamark.repository.RoomRepository;
import com.kamark.kamark.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PostService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private JWTUtils jwtUtils;

    //read
    public Optional<PostDTO> getPostById(Integer postId) {
        return postRepository.findById(postId).map(this::convertToPostDTO);
    }
    //create
    public boolean createPost(PostDTO postDTO) {
        Optional<User> userOptional = userRepository.findById(postDTO.getUserId());
        if (userOptional.isEmpty()) {
            return false;
        }

        Optional<Room> roomOptional = roomRepository.findById(postDTO.getRoomId());
        if (roomOptional.isEmpty()) {
            return false;
        }

        Post post = new Post();
        post.setName(postDTO.getName());
        post.setDescription(postDTO.getDescription());
        post.setStatus("alive");
        post.setUser(userOptional.get());
        post.setRoom(roomOptional.get());

        postRepository.save(post);
        return true;
    }

    //update by user who created it
    public Optional<Post> updatePost(Integer postId, PostDTO postDTO) {

        if (postId == null) {
            throw new IllegalArgumentException("Post ID must not be null");
        }
        Optional<Post> existingPostOptional = postRepository.findById(postId);
        if (existingPostOptional.isEmpty()) {
            return Optional.empty(); //if not exists, return null
        }

        Post existingPost = existingPostOptional.get();
        existingPost.setName(postDTO.getName());
        existingPost.setDescription(postDTO.getDescription());


        //implement after token usage
        /*

        Integer userId = jwtUtils.extractUserIdFromToken();

        if (!existingPost.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("User is not the owner of this post.");
        }*/

        Post updatedPost = postRepository.save(existingPost);
        return Optional.of(updatedPost);
    }

    //delete for owner of post, or admin
    public boolean deletePost(Integer postId) {
        Optional<Post> existingPostOptional = postRepository.findById(postId);
        if (existingPostOptional.isEmpty()) {
            return false;
        }

        Post existingPost = existingPostOptional.get();

        //
        /*
        Integer userId = jwtUtils.extractUserIdFromToken();

        // is the author or is admin
        if (!existingPost.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("User is not the owner of this post.");
        }
        */


        // delete post, literally deletes from db.
        postRepository.delete(existingPost);
        return true;
    }

    public PostDTO convertToPostDTO(Post post) {
        PostDTO postDTO = new PostDTO();
        postDTO.setName(post.getName());
        postDTO.setDescription(post.getDescription());
        postDTO.setUserId(post.getUser().getId());
        postDTO.setRoomId(post.getRoom().getId());
        postDTO.setStatus(post.getStatus());
        return postDTO;
    }

}
