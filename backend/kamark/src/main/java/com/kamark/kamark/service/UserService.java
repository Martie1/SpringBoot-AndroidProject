package com.kamark.kamark.service;

import com.kamark.kamark.dto.PostResponseDTO;
import com.kamark.kamark.dto.UserProfileDTO;
import com.kamark.kamark.entity.LikeEntity;
import com.kamark.kamark.entity.PostEntity;
import com.kamark.kamark.entity.UserEntity;
import com.kamark.kamark.exceptions.UserAlreadyExistsException;
import com.kamark.kamark.repository.LikeRepository;
import com.kamark.kamark.repository.PostRepository;
import com.kamark.kamark.repository.UserRepository;
import com.kamark.kamark.service.interfaces.UserServiceInterface;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

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
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with ID " + userId + " not found"));
        return new UserProfileDTO(user.getUsername(), user.getEmail());
    }


    public boolean updateUserProfile(Integer userId, UserProfileDTO userProfileDTO) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with ID " + userId + " not found"));

        if (userProfileDTO.getUsername() != null) {
            if (userRepository.findByUsername(userProfileDTO.getUsername()).isPresent()) {
                throw new UserAlreadyExistsException("Username " + userProfileDTO.getUsername() + " already taken");
            }
            user.setUsername(userProfileDTO.getUsername());
        }

        if (userProfileDTO.getEmail() != null) {
            if (userRepository.findByEmail(userProfileDTO.getEmail()).isPresent()) {
                throw new UserAlreadyExistsException("Email " + userProfileDTO.getEmail() + " already taken");
            }
            user.setEmail(userProfileDTO.getEmail());
        }
        if (userProfileDTO.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userProfileDTO.getPassword()));
        }

        userRepository.save(user);
        return true;
    }


    public boolean deactivateAccount(Integer userId) {
       UserEntity user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User with ID " + userId + " not found"));
        if(user.getStatus().equals("deactivated")){
            throw new NotFoundException("User with ID " + userId + " is already deactivated");
        }
        user.setStatus("deactivated");
        userRepository.save(user);
        return true;
    }



    public List<PostResponseDTO> getUserPosts(Integer userId) {
        List<PostEntity> posts = postRepository.findByUserId(userId);
        if(posts.isEmpty()){
            throw new NotFoundException("This user of id "+ userId+" hasn't published any posts yet ");
        }
        return posts.stream().map(this::mapToPostResponseDTO).collect(Collectors.toList());
    }

    public List<PostResponseDTO> getUserLikes(Integer userId) {
        List<LikeEntity> likes = likeRepository.findByUserId(userId);
        if(likes.isEmpty()){
            throw new NotFoundException("This user of id "+ userId+" hasn't liked any posts yet ");
        }
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

