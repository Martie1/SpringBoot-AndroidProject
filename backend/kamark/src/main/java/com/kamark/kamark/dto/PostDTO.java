package com.kamark.kamark.dto;

import lombok.Data;

@Data
public class PostDTO {
    private String name;
    private String description;
    private Integer userId; //to be deleted when app use jwt tokens
    private Integer roomId;
    private Integer likes;
    private String status;
}
