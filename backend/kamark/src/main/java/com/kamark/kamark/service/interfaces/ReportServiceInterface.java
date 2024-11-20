package com.kamark.kamark.service.interfaces;

import com.kamark.kamark.dto.PostResponseDTO;

import java.util.List;

public interface ReportServiceInterface {
    boolean reportPost(Integer postId, Integer userId, String reason);

    List<PostResponseDTO> getReportedPostsByRoomId(Integer roomId);

}
