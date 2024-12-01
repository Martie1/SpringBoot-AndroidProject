package com.kamark.kamark.service;

import com.kamark.kamark.entity.LikeEntity;
import com.kamark.kamark.entity.PostEntity;
import com.kamark.kamark.entity.UserEntity;
import com.kamark.kamark.exceptions.AlreadyExistsException;
import com.kamark.kamark.repository.LikeRepository;
import com.kamark.kamark.repository.PostRepository;
import com.kamark.kamark.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.webjars.NotFoundException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LikeServiceTest {

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    private LikeService likeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        likeService = new LikeService(likeRepository, postRepository, userRepository);
    }

    @Test
    void testLikePost_successful() {
        Integer postId = 1;
        Integer userId = 1;

        UserEntity user = new UserEntity();
        PostEntity post = new PostEntity();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(likeRepository.findByPostIdAndUserId(postId, userId)).thenReturn(Optional.empty());

        boolean result = likeService.likePost(postId, userId);

        assertTrue(result);
        verify(likeRepository, times(1)).save(any(LikeEntity.class));
    }

    @Test
    void testLikePost_userNotFound() {
        Integer postId = 1;
        Integer userId = 1;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            likeService.likePost(postId, userId);
        });

        assertEquals("User not found with id: " + userId, exception.getMessage());
    }

    @Test
    void testLikePost_postNotFound() {
        Integer postId = 1;
        Integer userId = 1;

        UserEntity user = new UserEntity();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            likeService.likePost(postId, userId);
        });

        assertEquals("Post not found with id: " + postId, exception.getMessage());
    }

    @Test
    void testLikePost_alreadyLiked() {
        Integer postId = 1;
        Integer userId = 1;

        UserEntity user = new UserEntity();
        PostEntity post = new PostEntity();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(likeRepository.findByPostIdAndUserId(postId, userId)).thenReturn(Optional.of(new LikeEntity()));

        AlreadyExistsException exception = assertThrows(AlreadyExistsException.class, () -> {
            likeService.likePost(postId, userId);
        });

        assertEquals("You already liked this post", exception.getMessage());
    }

    @Test
    void testUnlikePost_successful() {
        Integer postId = 1;
        Integer userId = 1;

        LikeEntity existingLike = new LikeEntity();

        when(likeRepository.findByPostIdAndUserId(postId, userId)).thenReturn(Optional.of(existingLike));

        boolean result = likeService.unlikePost(postId, userId);

        assertTrue(result);
        verify(likeRepository, times(1)).delete(existingLike);
    }

    @Test
    void testUnlikePost_notLiked() {
        Integer postId = 1;
        Integer userId = 1;

        when(likeRepository.findByPostIdAndUserId(postId, userId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            likeService.unlikePost(postId, userId);
        });

        assertEquals("You have not liked this post yet to be able to unlike it", exception.getMessage());
    }

    @Test
    void testDeleteLikesByPostId_successful() {
        Integer postId = 1;

        when(likeRepository.deleteByPostId(postId)).thenReturn(1);

        boolean result = likeService.deleteLikesByPostId(postId);

        assertTrue(result);
        verify(likeRepository, times(1)).deleteByPostId(postId);
    }

    @Test
    void testDeleteLikesByPostId_noLikesDeleted() {
        Integer postId = 1;

        when(likeRepository.deleteByPostId(postId)).thenReturn(0);

        boolean result = likeService.deleteLikesByPostId(postId);

        assertFalse(result);
        verify(likeRepository, times(1)).deleteByPostId(postId);
    }
}
