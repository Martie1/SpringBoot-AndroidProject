package com.kamark.kamark.service;

import com.kamark.kamark.dto.CreatePostDTO;
import com.kamark.kamark.dto.PostResponseDTO;
import com.kamark.kamark.entity.PostEntity;
import com.kamark.kamark.entity.ReportStatus;
import com.kamark.kamark.entity.RoomEntity;
import com.kamark.kamark.entity.UserEntity;
import com.kamark.kamark.exceptions.UserAccessDeniedException;
import com.kamark.kamark.repository.LikeRepository;
import com.kamark.kamark.repository.PostRepository;
import com.kamark.kamark.repository.RoomRepository;
import com.kamark.kamark.repository.UserRepository;
import com.kamark.kamark.service.interfaces.PostServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.nio.file.AccessDeniedException;
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

    public PostResponseDTO createPostAndReturnResponse(CreatePostDTO createPostDTO, Integer userId) {

        UserEntity user = userRepository.findById(userId).orElseThrow(
                () -> new UsernameNotFoundException("User not found with id: " + userId)
        );

        RoomEntity room = roomRepository.findById(createPostDTO.getRoomId()).orElseThrow(
                () -> new NotFoundException("Room not found with id: " + createPostDTO.getRoomId())
        );

        PostEntity post = new PostEntity();
        post.setName(createPostDTO.getName());
        post.setDescription(createPostDTO.getDescription());
        post.setUser(user);
        post.setRoom(room);
        post.setCreatedAt(new Date());
        post.setStatus("ACTIVE");

        PostEntity savedPost = postRepository.save(post);

        PostResponseDTO responseDTO = mapToDTO(savedPost);
        return responseDTO;
    }


    public PostResponseDTO getPostById(Integer id) {
        PostEntity post = postRepository.findById(id).orElseThrow(
               () -> new NotFoundException("Post not found with id "+ id));
        return mapToDTO(post);
    }

    public List<PostResponseDTO> getPostsByRoomId(Integer roomId) {
            RoomEntity room = roomRepository.findById(roomId).orElseThrow(
                    () -> new NotFoundException("Room not found with id: " + roomId)
            );
            List<PostEntity> posts = postRepository.findByRoomId(roomId);
            if(posts.isEmpty()){
                throw new NotFoundException("No posts found for room with id: " + roomId);
            }
            return posts.stream()
                    .filter(post -> !"BLOCKED".equals(post.getStatus()))
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());
    }



    public PostEntity updatePost(Integer postId, PostResponseDTO postDTO, Integer userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(
                () -> new UsernameNotFoundException("User not found with id: " + userId)
        );
        PostEntity post = postRepository.findById(postId).orElseThrow(
                () -> new NotFoundException("Post not found with id: " + postId)
        );

        if (!post.getUser().getId().equals(userId)) {
                throw new UserAccessDeniedException("Post doesn't belong to this user.");
        }

            post.setName(postDTO.getName());
            post.setDescription(postDTO.getDescription());
            post.setUpdatedAt(new Date());
            postRepository.save(post);
            return post;
    }



    public boolean deletePost(Integer postId, Integer userId) {
        PostEntity post = postRepository.findById(postId).orElseThrow(
                () -> new NotFoundException("Post not found with id: " + postId)
        );
        UserEntity user = userRepository.findById(userId).orElseThrow(
                () -> new UsernameNotFoundException("User not found with id: " + userId)
        );

        if (!post.getUser().getId().equals(userId)) {
             throw new UserAccessDeniedException("Post doesn't belong to this user.");
        }
        postRepository.delete(post);
        return true;
    }
    public boolean updatePostStatus(Integer postId,String status) {
        PostEntity post = postRepository.findById(postId).orElseThrow(
                () -> new NotFoundException("Post not found with id: " + postId)
        );
        post.setStatus(status);
        postRepository.save(post);
        return true;

    }
    private PostResponseDTO mapToDTO(PostEntity post) {
        PostResponseDTO dto = new PostResponseDTO();
        dto.setId(post.getId());
        dto.setName(post.getName());
        dto.setDescription(post.getDescription());
        dto.setStatus(post.getStatus());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUserId(post.getUser().getId());
        dto.setUsername(post.getUser().getUsername());
        dto.setRoomId(post.getRoom().getId());
        dto.setLikeCount(likeRepository.countByPostId(post.getId()));

        return dto;
    }





}
