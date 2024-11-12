package com.kamark.kamark.service;

import com.kamark.kamark.dto.PostResponseDTO;
import com.kamark.kamark.dto.UserProfileDTO;
import com.kamark.kamark.entity.Like;
import com.kamark.kamark.entity.Post;
import com.kamark.kamark.entity.User;
import com.kamark.kamark.repository.LikeRepository;
import com.kamark.kamark.repository.PostRepository;
import com.kamark.kamark.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private LikeRepository likeRepository;


    public UserProfileDTO getUserProfile(Integer userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return null;
        }
        User user = userOptional.get();
        return new UserProfileDTO(user.getUsername(), user.getEmail());
    }


    public boolean updateUserProfile(Integer userId, UserProfileDTO userProfileDTO) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return false;
        }
        User user = userOptional.get();

        if (userProfileDTO.getUsername() != null) {
            user.setUsername(userProfileDTO.getUsername());
        }
        if (userProfileDTO.getEmail() != null) {
            user.setEmail(userProfileDTO.getEmail());
        }
        if (userProfileDTO.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userProfileDTO.getPassword()));
        }

        userRepository.save(user);
        return true;
    }


    public boolean deactivateAccount(Integer userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return false;
        }
        User user = userOptional.get();
        user.setStatus("deactivated");
        userRepository.save(user);
        return true;
    }

    public List<PostResponseDTO> getUserPosts(Integer userId) {
        List<Post> posts = postRepository.findByUserId(userId);
        return posts.stream().map(this::mapToPostResponseDTO).collect(Collectors.toList());
    }

    public List<PostResponseDTO> getUserLikes(Integer userId) {
        List<Like> likes = likeRepository.findByUserId(userId);
        return likes.stream()
                .map(like -> mapToPostResponseDTO(like.getPost()))
                .collect(Collectors.toList());
    }
    private PostResponseDTO mapToPostResponseDTO(Post post) {
        return new PostResponseDTO(
                post.getId(),
                post.getName(),
                post.getDescription(),
                post.getStatus(),
                post.getCreatedAt(),
                post.getUser().getUsername(),
                post.getLikes().size(),
                post.getRoom().getId(),
                post.getUser().getId()
        );
    }


}

