package com.kamark.kamark.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportPostDTO {
    private Integer postId;
    private String reason;
}
