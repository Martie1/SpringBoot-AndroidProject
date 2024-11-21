package com.kamark.kamark.service;

import com.kamark.kamark.dto.PostResponseDTO;
import com.kamark.kamark.dto.UserProfileDTO;
import com.kamark.kamark.entity.LikeEntity;
import com.kamark.kamark.entity.PostEntity;
import com.kamark.kamark.entity.UserEntity;
import com.kamark.kamark.repository.LikeRepository;
import com.kamark.kamark.repository.PostRepository;
import com.kamark.kamark.repository.UserRepository;
import com.kamark.kamark.service.interfaces.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserServiceInterface {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, PostRepository postRepository, LikeRepository likeRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
    }


    public UserProfileDTO getUserProfile(Integer userId) {
        Optional<UserEntity> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return null;
        }
        UserEntity user = userOptional.get();
        return new UserProfileDTO(user.getUsername(), user.getEmail());
    }


    public boolean updateUserProfile(Integer userId, UserProfileDTO userProfileDTO) {
        Optional<UserEntity> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return false;
        }
        UserEntity user = userOptional.get();

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
        Optional<UserEntity> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return false;
        }
        UserEntity user = userOptional.get();
        user.setStatus("deactivated");
        userRepository.save(user);
        return true;
    }

    public List<PostResponseDTO> getUserPosts(Integer userId) {
        List<PostEntity> posts = postRepository.findByUserId(userId);
        return posts.stream().map(this::mapToPostResponseDTO).collect(Collectors.toList());
    }

    public List<PostResponseDTO> getUserLikes(Integer userId) {
        List<LikeEntity> likes = likeRepository.findByUserId(userId);
        return likes.stream()
                .map(like -> mapToPostResponseDTO(like.getPost()))
                .collect(Collectors.toList());
    }
    private PostResponseDTO mapToPostResponseDTO(PostEntity post) {
        PostResponseDTO dto = new PostResponseDTO(
                post.getId(),
                post.getName(),
                post.getDescription(),
                post.getStatus(),
                post.getCreatedAt(),
                post.getUser().getUsername(),
                0,
                post.getRoom().getId(),
                post.getUser().getId()
        );

        dto.setLikeCount(likeRepository.countByPostId(post.getId()));

        return dto;
    }


}

