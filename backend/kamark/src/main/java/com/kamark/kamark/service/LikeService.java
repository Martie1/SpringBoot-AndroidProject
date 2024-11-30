package com.kamark.kamark.service;

import com.kamark.kamark.entity.LikeEntity;
import com.kamark.kamark.entity.PostEntity;
import com.kamark.kamark.entity.UserEntity;
import com.kamark.kamark.exceptions.AlreadyExistsException;
import com.kamark.kamark.repository.LikeRepository;
import com.kamark.kamark.repository.PostRepository;
import com.kamark.kamark.repository.UserRepository;
import com.kamark.kamark.service.interfaces.LikeInterface;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.Optional;

@Service
public class LikeService implements LikeInterface {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public LikeService(LikeRepository likeRepository, PostRepository postRepository, UserRepository userRepository) {
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }


    @Override
    public boolean likePost(Integer postId, Integer userId) {

        UserEntity user = userRepository.findById(userId).orElseThrow(
                () -> new UsernameNotFoundException("User not found with id: " + userId)
        );

        PostEntity post = postRepository.findById(postId).orElseThrow(
                () -> new NotFoundException("Post not found with id: " + postId)
        );
        if (likeRepository.findByPostIdAndUserId(postId, userId).isPresent()) {
            throw new AlreadyExistsException("You already liked this post");
        }

        LikeEntity like = new LikeEntity();
        like.setUser(user);
        like.setPost(post);
        likeRepository.save(like);
        return true;
    }

    @Override
    public boolean unlikePost(Integer postId, Integer userId) {
        LikeEntity existingLike = likeRepository.findByPostIdAndUserId(postId, userId).orElseThrow(
                () -> new NotFoundException("You have not liked this post yet to be able to unlike it")
        );
        likeRepository.delete(existingLike);
        return true;
    }
    @Override
    @Transactional //delete all records or nothing
    public boolean deleteLikesByPostId(Integer postId) {
        int deletedCount = likeRepository.deleteByPostId(postId);
        return deletedCount > 0;
    }
}