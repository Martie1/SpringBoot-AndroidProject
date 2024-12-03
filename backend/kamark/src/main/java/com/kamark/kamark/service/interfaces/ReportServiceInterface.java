package com.kamark.kamark.service.interfaces;

import com.kamark.kamark.dto.PostResponseDTO;
import com.kamark.kamark.dto.ReportDTO;
import com.kamark.kamark.entity.ReportStatus;
import com.kamark.kamark.entity.ReportEntity;

import java.util.List;

public interface ReportServiceInterface {
    boolean reportPost(Integer postId, Integer userId, String reason);

    List<PostResponseDTO> getReportedPostsByRoomId(Integer roomId);
    public boolean updateReportsStatusByPostId(Integer postId, ReportStatus status);

    public List<ReportDTO> getReportsByPostId(Integer postId);

}
