package com.kamark.kamark.controller;

import com.kamark.kamark.dto.ReportPostDTO;
import com.kamark.kamark.entity.Post;
import com.kamark.kamark.repository.PostRepository;
import com.kamark.kamark.service.ReportService;
import com.kamark.kamark.service.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional; // Dla Optional


@RestController
@RequestMapping("/api/posts")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private JWTUtils jwtUtils;

    // Zgłaszanie posta
    @PostMapping("/{postId}/report")
    public ResponseEntity<String> reportPost(
            @PathVariable Integer postId,
            @RequestBody ReportPostDTO reportPostDTO,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);  // Usuwanie prefiksu "Bearer " z tokenu
        Integer userId = jwtUtils.extractUserId(token);

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token");
        }

        // Wywołanie metody serwisowej do zgłoszenia posta
        boolean isReported = reportService.reportPost(postId, userId, reportPostDTO.getReason());

        if (isReported) {
            return ResponseEntity.ok("Post has been successfully reported");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Post already reported or invalid data");
        }
    }

    // Pobieranie liczby zgłoszeń dla posta
    @GetMapping("/{id}/report-count")
    public ResponseEntity<Integer> getReportCount(@PathVariable Integer id) {
        Optional<Post> postOptional = postRepository.findById(id);
        if (postOptional.isPresent()) {
            return ResponseEntity.ok(postOptional.get().getReportCount());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
