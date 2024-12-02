package com.kamark.kamark.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kamark.kamark.dto.RegisterRequest;
import com.kamark.kamark.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.stream.Stream;



@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
    }
    private static Stream<RegisterRequest> provideTestData() {
        return IntStream.range(1, 11).mapToObj(i -> {
            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.setEmail("testuser" + i + "@example.com");
            registerRequest.setUsername("testuser" + i);
            registerRequest.setPassword("Password" + i + "!");
            return registerRequest;
        });
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    public void testRegisterUser_Loop(RegisterRequest registerRequest) throws Exception {
        MvcResult result = mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())  // Oczekiwany status 200
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains("User Registered Successfully"));
        assertTrue(content.contains("accessToken"));
        assertTrue(content.contains("refreshToken"));
    }

    @Test
    public void testRegisterUser_EmailAlreadyExists() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("testuser@example.com");
        registerRequest.setUsername("testuser");
        registerRequest.setPassword("TestPassword123!");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk());

        RegisterRequest duplicateEmailRequest = new RegisterRequest();
        duplicateEmailRequest.setEmail("testuser@example.com");
        duplicateEmailRequest.setUsername("anotheruser");
        duplicateEmailRequest.setPassword("AnotherPassword123!");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicateEmailRequest)))
                .andExpect(status().isConflict());
    }

    @Test
    public void testRegisterUser_UsernameAlreadyExists() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("testuser@example.com");
        registerRequest.setUsername("testuser");
        registerRequest.setPassword("TestPassword123!");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk());

        RegisterRequest duplicateUsernameRequest = new RegisterRequest();
        duplicateUsernameRequest.setEmail("anotheruser@example.com");
        duplicateUsernameRequest.setUsername("testuser");
        duplicateUsernameRequest.setPassword("AnotherPassword123!");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicateUsernameRequest)))
                .andExpect(status().isConflict());
    }
}
