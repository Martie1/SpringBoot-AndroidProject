package com.kamark.kamark.controller;

import com.kamark.kamark.dto.PostResponseDTO;
import com.kamark.kamark.dto.ReportPostDTO;
import com.kamark.kamark.dto.SimpleResponse;
import com.kamark.kamark.entity.ReportStatus;
import com.kamark.kamark.service.PostService;
import com.kamark.kamark.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminRestController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private PostService postService;


    @GetMapping("/{roomId}/posts")
    public ResponseEntity<List<PostResponseDTO>> getAllReportedPostsByRoom(@PathVariable Integer roomId) {
        List<PostResponseDTO> reports = reportService.getReportedPostsByRoomId(roomId);
        return ResponseEntity.ok(reports);
    }

    @PostMapping("/{postId}/resolve") //resolve all reports connected to this post
    public ResponseEntity<SimpleResponse> resolveReport(@PathVariable Integer postId) {
        boolean reportsUpdated = reportService.updateReportsStatusByPostId(postId, ReportStatus.RESOLVED);

        if (reportsUpdated) {
            boolean postUpdated = postService.updatePostStatus(postId, "BLOCKED");

            if (postUpdated) {
                SimpleResponse response = new SimpleResponse(
                        HttpStatus.OK.value(),
                        "All reports marked as resolved and post status changed to BLOCKED"
                );
                return ResponseEntity.ok(response);
            } else {
                SimpleResponse response = new SimpleResponse(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Reports resolved, but failed to change post status to BLOCKED"
                );
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } else {
            SimpleResponse response = new SimpleResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    "No reports found for the given post"
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }



    @PostMapping("/{postId}/dismiss")
    public ResponseEntity<SimpleResponse> dismissReport(@PathVariable Integer postId) {
        boolean reportsUpdated = reportService.updateReportsStatusByPostId(postId, ReportStatus.DISMISSED);
        if (reportsUpdated) {

            boolean postUpdated = postService.updatePostStatus(postId, "ACTIVE");

            if (postUpdated) {
                SimpleResponse response = new SimpleResponse(
                        HttpStatus.OK.value(),
                        "All reports dismissed and post status changed to ACTIVE"
                );
                return ResponseEntity.ok(response);
            } else {
                SimpleResponse response = new SimpleResponse(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Reports dismissed, but failed to change post status to ACTIVE"
                );
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } else {
            SimpleResponse response = new SimpleResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    "No reports found for the given post"
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

}
