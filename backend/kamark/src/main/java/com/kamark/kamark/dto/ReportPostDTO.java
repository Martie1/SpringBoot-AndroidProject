package com.kamark.kamark.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.kamark.kamark.entity.ReportStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportPostDTO {
    private Integer postId;
    private String reason;
    private ReportStatus status;
}