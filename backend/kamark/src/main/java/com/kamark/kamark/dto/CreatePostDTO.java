package com.kamark.kamark.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class CreatePostDTO { //also for update post

    @NotBlank(message = "{NotBlank.createPostDTO.name}")
    @Size(min = 1, max = 40, message = "{Size.createPostDTO.name}")
    private String name;

    @NotBlank(message = "{NotBlank.createPostDTO.description}")
    @Size(min = 1, max = 2000, message = "{Size.createPostDTO.description}")
    private String description;

    private Integer roomId;
}
