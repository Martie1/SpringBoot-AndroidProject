package com.kamark.kamark.service.interfaces;

public interface LikeInterface {
    boolean likePost(Integer postId, Integer userId);
    boolean unlikePost(Integer postId, Integer userId);


    boolean deleteLikesByPostId(Integer postId);
}