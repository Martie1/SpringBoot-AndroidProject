package com.kamark.kamark.service;

import com.kamark.kamark.entity.Like;
import com.kamark.kamark.entity.Post;
import com.kamark.kamark.entity.User;
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
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return false;
        }
        User user = userOptional.get();

        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isEmpty()) {
            return false;
        }
        Post post = postOptional.get();

        Like like = new Like();
        like.setUser(user);
        like.setPost(post);
        likeRepository.save(like);
        return true;
    }

    @Override
    public boolean unlikePost(Integer postId, Integer userId) {
        Optional<Like> likeOptional = likeRepository.findByPostIdAndUserId(postId, userId);
        if (likeOptional.isEmpty()) {
            return false;
        }
        Like like = likeOptional.get();
        likeRepository.delete(like);
        return true;
    }
}