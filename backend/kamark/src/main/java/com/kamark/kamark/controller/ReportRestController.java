package com.kamark.kamark.controller;

import com.kamark.kamark.dto.ReportPostDTO;
import com.kamark.kamark.service.JWTUtils;
import com.kamark.kamark.service.ReportService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
public class ReportRestController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private JWTUtils jwtUtils;

    @PostMapping("/{postId}/report")
    public ResponseEntity<String> reportPost(
            @PathVariable Integer postId,
            @RequestBody @Valid ReportPostDTO reportPostDTO,
            @RequestHeader("Authorization") String authHeader) {

        Integer userId = jwtUtils.extractUserIdFromAuthorizationHeader(authHeader);

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        boolean isReported = reportService.reportPost(postId, userId, reportPostDTO.getReason());
        if (isReported) {
            return ResponseEntity.ok("Post reported successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to report post");
        }
    }
}
