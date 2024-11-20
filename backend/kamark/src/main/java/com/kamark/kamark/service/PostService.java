package com.kamark.kamark.service;

import com.kamark.kamark.dto.CreatePostDTO;
import com.kamark.kamark.dto.PostResponseDTO;
import com.kamark.kamark.entity.PostEntity;
import com.kamark.kamark.entity.RoomEntity;
import com.kamark.kamark.entity.UserEntity;
import com.kamark.kamark.repository.LikeRepository;
import com.kamark.kamark.repository.PostRepository;
import com.kamark.kamark.repository.RoomRepository;
import com.kamark.kamark.repository.UserRepository;
import com.kamark.kamark.service.interfaces.PostServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService implements PostServiceInterface {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final LikeRepository likeRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository, RoomRepository roomRepository, LikeRepository likeRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.likeRepository = likeRepository;
    }

    public boolean createPost(CreatePostDTO createPostDTO, Integer userId) {
        Optional<UserEntity> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return false;
        }
        UserEntity user = userOptional.get();

        Optional<RoomEntity> roomOptional = roomRepository.findById(createPostDTO.getRoomId());
        if (roomOptional.isEmpty()) {
            return false;
        }
        RoomEntity room = roomOptional.get();

        PostEntity post = new PostEntity();
        post.setName(createPostDTO.getName());
        post.setDescription(createPostDTO.getDescription());
        post.setUser(user);
        post.setRoom(room);
        post.setCreatedAt(new Date());
        post.setStatus("ACTIVE");
        postRepository.save(post);
        return true;
    }

    public Optional<PostResponseDTO> getPostById(Integer id) {
        return postRepository.findById(id).map(this::mapToDTO);
    }

    public List<PostResponseDTO> getPostsByRoomId(Integer roomId) {
        List<PostEntity> posts = postRepository.findByRoomId(roomId);
        return posts.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<PostEntity> updatePost(Integer postId, PostResponseDTO postDTO, Integer userId) {
        Optional<PostEntity> existingPost = postRepository.findById(postId);
        if (existingPost.isPresent()) {
            PostEntity post = existingPost.get();

            if (!post.getUser().getId().equals(userId)) {
                return Optional.empty(); // Brak uprawnień
            }

            post.setName(postDTO.getName());
            post.setDescription(postDTO.getDescription());
            post.setUpdatedAt(new Date());
            postRepository.save(post);
            return Optional.of(post);
        }
        return Optional.empty();
    }

    public boolean deletePost(Integer postId, Integer userId) {
        Optional<PostEntity> existingPost = postRepository.findById(postId);
        if (existingPost.isPresent()) {
            PostEntity post = existingPost.get();

            if (!post.getUser().getId().equals(userId)) {
                return false;
            }

            postRepository.delete(post);
            return true;
        }
        return false;
    }

    private PostResponseDTO mapToDTO(PostEntity post) {
        PostResponseDTO dto = new PostResponseDTO();
        dto.setId(post.getId());
        dto.setName(post.getName());
        dto.setDescription(post.getDescription());
        dto.setStatus(post.getStatus());
        dto.setCreatedAt(post.getCreatedAt());

        if (post.getUser() != null) {
            dto.setUserId(post.getUser().getId());
            dto.setUsername(post.getUser().getUsername());
        }

        if (post.getRoom() != null) {
            dto.setRoomId(post.getRoom().getId());
        }

        dto.setLikeCount(likeRepository.countByPostId(post.getId()));

        return dto;
    }
    public boolean incrementReportCount(Integer postId) {
        Optional<PostEntity> postOptional = postRepository.findById(postId);
        if (postOptional.isPresent()) {
            PostEntity post = postOptional.get();
            post.setReportCount(post.getReportCount() + 1);
            postRepository.save(post);
            return true;
        }
        return false;
    }




}
