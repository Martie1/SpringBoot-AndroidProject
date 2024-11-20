package com.kamark.kamark.service;

import com.kamark.kamark.entity.LikeEntity;
import com.kamark.kamark.entity.PostEntity;
import com.kamark.kamark.entity.UserEntity;
import com.kamark.kamark.repository.LikeRepository;
import com.kamark.kamark.repository.PostRepository;
import com.kamark.kamark.repository.UserRepository;
import com.kamark.kamark.service.interfaces.LikeInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LikeService implements LikeInterface {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean likePost(Integer postId, Integer userId) {
        Optional<UserEntity> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return false;
        }
        UserEntity user = userOptional.get();

        Optional<PostEntity> postOptional = postRepository.findById(postId);
        if (postOptional.isEmpty()) {
            return false;
        }
        Optional<LikeEntity> existingLike = likeRepository.findByPostIdAndUserId(postId, userId);
        if (existingLike.isPresent()) {
            return false;
        }
        PostEntity post = postOptional.get();

        LikeEntity like = new LikeEntity();
        like.setUser(user);
        like.setPost(post);
        likeRepository.save(like);
        return true;
    }

    @Override
    public boolean unlikePost(Integer postId, Integer userId) {
        Optional<LikeEntity> likeOptional = likeRepository.findByPostIdAndUserId(postId, userId);
        if (likeOptional.isEmpty()) {
            return false;
        }
        LikeEntity like = likeOptional.get();
        likeRepository.delete(like);
        return true;
    }
}