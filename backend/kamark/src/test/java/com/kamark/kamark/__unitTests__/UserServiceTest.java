package com.kamark.kamark.service;

import com.kamark.kamark.dto.PostResponseDTO;
import com.kamark.kamark.dto.UserProfileDTO;
import com.kamark.kamark.entity.LikeEntity;
import com.kamark.kamark.entity.PostEntity;
import com.kamark.kamark.entity.UserEntity;
import com.kamark.kamark.repository.LikeRepository;
import com.kamark.kamark.repository.PostRepository;
import com.kamark.kamark.repository.UserRepository;
import com.kamark.kamark.entity.RoomEntity;
import com.kamark.kamark.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.webjars.NotFoundException;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserProfile_WhenUserExists() {
        // Arrange
        UserEntity user = new UserEntity();
        user.setId(1);
        user.setUsername("john_doe");
        user.setEmail("john.doe@example.com");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        // Act
        UserProfileDTO result = userService.getUserProfile(1);

        // Assert
        assertNotNull(result);
        assertEquals("john_doe", result.getUsername());
        assertEquals("john.doe@example.com", result.getEmail());

        verify(userRepository, times(1)).findById(1);
    }



    @Test
    void testUpdateUserProfile_WhenUserExists() {
        // Arrange
        UserEntity user = new UserEntity();
        user.setId(1);
        user.setUsername("john_doe");
        user.setEmail("john.doe@example.com");

        UserProfileDTO userProfileDTO = new UserProfileDTO();
        userProfileDTO.setUsername("new_username");
        userProfileDTO.setEmail("new.email@example.com");
        userProfileDTO.setPassword("new_password");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("new_password")).thenReturn("encoded_password");

        // Act
        boolean result = userService.updateUserProfile(1, userProfileDTO);

        // Assert
        assertTrue(result);
        assertEquals("new_username", user.getUsername());
        assertEquals("new.email@example.com", user.getEmail());
        assertEquals("encoded_password", user.getPassword());

        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testUpdateUserProfile_WhenUserDoesNotExist() {
        // Arrange
        UserProfileDTO userProfileDTO = new UserProfileDTO();
        userProfileDTO.setUsername("new_username");

        when(userRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> userService.updateUserProfile(1, userProfileDTO));

        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(0)).save(any());
    }

    @Test
    void testDeactivateAccount_WhenUserExists() {
        // Arrange
        UserEntity user = new UserEntity();
        user.setId(1);
        user.setStatus("active");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        // Act
        boolean result = userService.deactivateAccount(1);

        // Assert
        assertTrue(result);
        assertEquals("deactivated", user.getStatus());

        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testDeactivateAccount_WhenUserDoesNotExist() {
        // Arrange
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        // Act
        boolean result = userService.deactivateAccount(1);

        // Assert
        assertFalse(result);
        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(0)).save(any());
    }

    @Test
    void testGetUserPosts() {
        // Arrange
        UserEntity user = new UserEntity();
        user.setId(1);
        user.setUsername("john_doe");

        RoomEntity room = new RoomEntity();
        room.setId(1);
        room.setName("Test Room");

        PostEntity post1 = new PostEntity();
        post1.setId(1);
        post1.setName("Post 1");
        post1.setUser(user);
        post1.setRoom(room);
        post1.setLikes(Collections.emptySet());

        PostEntity post2 = new PostEntity();
        post2.setId(2);
        post2.setName("Post 2");
        post2.setUser(user);
        post2.setRoom(room);
        post2.setLikes(Collections.emptySet());

        when(postRepository.findByUserId(1)).thenReturn(Arrays.asList(post1, post2));

        // Act
        List<PostResponseDTO> result = userService.getUserPosts(1);

        // Assert
        assertEquals(2, result.size());
        assertEquals("Post 1", result.get(0).getName());
        assertEquals("Post 2", result.get(1).getName());
        assertEquals("john_doe", result.get(0).getUsername());
        assertEquals("john_doe", result.get(1).getUsername());

        verify(postRepository, times(1)).findByUserId(1);
    }

    @Test
    void testGetUserLikes() {
        // Arrange
        UserEntity user = new UserEntity();
        user.setId(1);
        user.setUsername("john_doe");

        PostEntity post1 = new PostEntity();
        post1.setId(1);
        post1.setName("Post 1");
        post1.setUser(user);
        post1.setRoom(new RoomEntity());
        post1.setLikes(Collections.emptySet());

        LikeEntity like1 = new LikeEntity();
        like1.setPost(post1);

        when(likeRepository.findByUserId(1)).thenReturn(Arrays.asList(like1));

        // Act
        List<PostResponseDTO> result = userService.getUserLikes(1);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Post 1", result.get(0).getName());
        assertEquals("john_doe", result.get(0).getUsername());

        verify(likeRepository, times(1)).findByUserId(1);
    }
}
