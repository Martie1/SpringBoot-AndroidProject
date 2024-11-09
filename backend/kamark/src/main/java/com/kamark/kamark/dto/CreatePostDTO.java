package com.kamark.kamark.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreatePostDTO { //also for update post
    private String name;
    private String description;
    private Integer roomId;
}
