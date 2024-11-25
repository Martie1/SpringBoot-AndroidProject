package com.kamark.kamark.service.interfaces;

import com.kamark.kamark.dto.CreatePostDTO;
import com.kamark.kamark.dto.PostResponseDTO;
import com.kamark.kamark.entity.PostEntity;

import java.util.List;
import java.util.Optional;

public interface PostServiceInterface {

    PostResponseDTO getPostById(Integer id);
    List<PostResponseDTO> getPostsByRoomId(Integer roomId);
    PostEntity updatePost(Integer postId, PostResponseDTO postDTO, Integer userId);
    PostResponseDTO createPostAndReturnResponse(CreatePostDTO createPostDTO, Integer userId);
    boolean deletePost(Integer postId, Integer userId);

    public boolean updatePostStatus(Integer postId,  String status);

}
