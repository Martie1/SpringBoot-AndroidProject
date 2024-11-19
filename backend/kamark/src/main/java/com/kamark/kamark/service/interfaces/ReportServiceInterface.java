package com.kamark.kamark.service.interfaces;

public interface ReportServiceInterface {
    boolean reportPost(Integer postId, Integer userId, String reason);
    Integer getReportCount(Integer postId);
}
