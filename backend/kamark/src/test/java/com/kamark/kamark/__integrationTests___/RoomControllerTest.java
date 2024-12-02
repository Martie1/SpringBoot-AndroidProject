package com.kamark.kamark.__integrationTests___;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RoomControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetRooms_InvalidToken() throws Exception {
        String invalidToken = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI5Iiwicm9sZXMiOlsiVVNFUiJdLCJpYXQiOjE3MzI5OTkwNTAswewImV4cCI6MTczMzAwMjY1MH0.ZyE9cTDXtD6mHaBYDy55wU8xntUSQREDnLHvGoS8-5JTe9p4yxT2TUGsEXXp0rA_rjBGzC01iIqe0g1-2tKEgQ";

        mockMvc.perform(get("/rooms")
                        .header("Authorization", invalidToken))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    assertTrue(content.contains("Invalid JWT signature"));
                });
    }

    @Test
    public void testGetRooms_ExpiredToken() throws Exception {
        String expiredToken = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI5Iiwicm9sZXMiOlsiVVNFUiJdLCJpYXQiOjE3MzI5OTkwNTAsImV4cCI6MTczMzAwMjY1MH0.ZyE9cTDXtD6mHaBYDy55wU8xntUSQREDnLHvGoS8-5JTe9p4yxT2TUGsEXXp0rA_rjBGzC01iIqe0g1-2tKEgQ";

        mockMvc.perform(get("/rooms")
                        .header("Authorization", expiredToken))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    assertTrue(content.contains("JWT expired"));
                });
    }

    @Test
    public void testGetRooms_NoToken() throws Exception {
        mockMvc.perform(get("/rooms"))
                .andExpect(status().isForbidden());
    }

}
