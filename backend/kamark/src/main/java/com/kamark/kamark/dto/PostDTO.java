package com.kamark.kamark.dto;

import com.kamark.kamark.entity.Room;
import jakarta.persistence.Column;
import lombok.Data;

import java.util.Date;

@Data
public class PostDTO {
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
