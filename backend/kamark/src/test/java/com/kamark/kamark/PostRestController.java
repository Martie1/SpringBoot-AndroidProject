
package com.kamark.kamark;

import com.kamark.kamark.dto.CreatePostDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class PostRestControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void shouldGetPostById() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/posts/1")
						.header("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwicm9sZXMiOlsiVVNFUiJdLCJpYXQiOjE3MzIwOTQ2MTQsImV4cCI6MTczMjE4MTAxNH0.eikZ-B2152wsDtbwUKcSWNBP1elCYloN5uI32sMCyydV3GFXpSgamHacmOEjQII6dYw5QXGMUk4wJcx22j64nw"))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
	}

	@Test
	void shouldCreatePostSuccessfully() throws Exception {

		CreatePostDTO createPostDTO = new CreatePostDTO("test Post", "testowany post", 3);

		// na json zamiemian
		String createPostDTOJson = objectMapper.writeValueAsString(createPostDTO);

		//  POST
		mockMvc.perform(MockMvcRequestBuilders.post("/api/posts")
						.contentType(MediaType.APPLICATION_JSON)
						.header("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwicm9sZXMiOlsiVVNFUiJdLCJpYXQiOjE3MzIwOTQ2MTQsImV4cCI6MTczMjE4MTAxNH0.eikZ-B2152wsDtbwUKcSWNBP1elCYloN5uI32sMCyydV3GFXpSgamHacmOEjQII6dYw5QXGMUk4wJcx22j64nw")
						.content(createPostDTOJson))  // rzkaxnaie JSON w  zadaniu
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.content().string("Post has been successfully created"));
	}


}
