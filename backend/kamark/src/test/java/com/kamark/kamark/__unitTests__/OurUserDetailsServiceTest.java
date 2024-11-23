//package com.kamark.kamark.service;
//
//import com.kamark.kamark.entity.UserEntity;
//import com.kamark.kamark.repository.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import java.util.Optional;
//
//class OurUserDetailsServiceTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @InjectMocks
//    private OurUserDetailsService ourUserDetailsService;
//
//    private UserEntity userEntity;
//
//    @BeforeEach
//    void setUp() {
//
//        MockitoAnnotations.openMocks(this);
//
//        userEntity = new UserEntity();
//        userEntity.setId(1);
//        userEntity.setEmail("testuser@example.com");
//        userEntity.setUsername("testuser");
//        userEntity.setPassword("password123");
//        userEntity.setRole("USER");
//    }
//
//    @Test
//    void testLoadUserById_Success() {
//
//        when(userRepository.findById(1)).thenReturn(Optional.of(userEntity));
//
//
//        UserDetails userDetails = ourUserDetailsService.loadUserById(1);
//
//
//        assertEquals("testuser", userDetails.getUsername());
//        assertEquals("testuser@example.com", userEntity.getEmail());
//        verify(userRepository, times(1)).findById(1);
//    }
//
//    @Test
//    void testLoadUserById_UserNotFound() {
//
//        when(userRepository.findById(1)).thenReturn(Optional.empty());
//
//
//        try {
//            ourUserDetailsService.loadUserById(1);
//        } catch (UsernameNotFoundException e) {
//            assertEquals("User not found with id: 1", e.getMessage());
//        }
//
//        verify(userRepository, times(1)).findById(1);
//    }
//
//    @Test
//    void testLoadUserByUsername_Success() {
//
//        when(userRepository.findByEmail("testuser@example.com")).thenReturn(Optional.of(userEntity));
//
//
//        UserDetails userDetails = ourUserDetailsService.loadUserByUsername("testuser@example.com");
//
//
//        assertEquals("testuser", userDetails.getUsername());
//        assertEquals("testuser@example.com", userEntity.getEmail());
//        verify(userRepository, times(1)).findByEmail("testuser@example.com");
//    }
//
//    @Test
//    void testLoadUserByUsername_UserNotFound() {
//
//        when(userRepository.findByEmail("testuser@example.com")).thenReturn(Optional.empty());
//
//
//        try {
//            ourUserDetailsService.loadUserByUsername("testuser@example.com");
//        } catch (UsernameNotFoundException e) {
//            assertEquals("User not found with email: testuser@example.com", e.getMessage());
//        }
//
//        verify(userRepository, times(1)).findByEmail("testuser@example.com");
//    }
//}
