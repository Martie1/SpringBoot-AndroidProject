package com.kamark.kamark.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDTO {
    private Integer id;
    private String name;
    private String description;
    private String status;
    private Date createdAt;
    private String username;
    private Integer likeCount;
    private Integer roomId;
    private Integer userId;
}
