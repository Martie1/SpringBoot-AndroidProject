package com.kamark.kamark.service.interfaces;

import com.kamark.kamark.dto.PostResponseDTO;
import com.kamark.kamark.dto.UserProfileDTO;

import java.util.List;

public interface UserServiceInterface {
    UserProfileDTO getUserProfile(Integer userId);
    boolean updateUserProfile(Integer userId, UserProfileDTO userProfileDTO);
    boolean deactivateAccount(Integer userId);
    List<PostResponseDTO> getUserPosts(Integer userId);

    List<PostResponseDTO> getUserLikes(Integer userId);
}
