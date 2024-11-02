package com.kamark.kamark.dto;

import lombok.Data;

@Data
public class PostDTO {
    private String name;
    private String description;
    private Integer userId;
    private Integer roomId;
}
