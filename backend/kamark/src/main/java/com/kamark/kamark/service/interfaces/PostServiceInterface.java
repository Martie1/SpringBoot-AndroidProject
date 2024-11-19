package com.kamark.kamark.service.interfaces;

import com.kamark.kamark.dto.CreatePostDTO;
import com.kamark.kamark.dto.PostResponseDTO;
import com.kamark.kamark.entity.Post;

import java.util.List;
import java.util.Optional;

public interface PostServiceInterface {
    boolean createPost(CreatePostDTO createPostDTO, Integer userId);
    Optional<PostResponseDTO> getPostById(Integer id);
    List<PostResponseDTO> getPostsByRoomId(Integer roomId);
    Optional<Post> updatePost(Integer postId, PostResponseDTO postDTO, Integer userId);
    boolean deletePost(Integer postId, Integer userId);
    boolean incrementReportCount(Integer postId);
}
