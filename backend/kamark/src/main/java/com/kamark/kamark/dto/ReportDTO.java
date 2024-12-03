package com.kamark.kamark.dto;

import com.kamark.kamark.entity.ReportEntity;
import lombok.Data;


@Data
public class ReportDTO {
    private Integer id;
    private String reason;
    private Integer postId;
    private Integer userId;


    public ReportDTO(ReportEntity report) {
        this.id = report.getId();
        this.reason = report.getReason();
        this.postId = report.getPost().getId();
        this.userId = report.getUser().getId();
    }

}

