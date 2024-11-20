package com.kamark.kamark.controller;

import com.kamark.kamark.dto.PostResponseDTO;
import com.kamark.kamark.dto.ReportPostDTO;
import com.kamark.kamark.entity.ReportStatus;
import com.kamark.kamark.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminRestController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/{roomId}/posts")
    public ResponseEntity<List<PostResponseDTO>> getAllReportedPostsByRoom(@PathVariable Integer roomId) {
        List<PostResponseDTO> reports = reportService.getReportedPostsByRoomId(roomId);
        return ResponseEntity.ok(reports);
    }

    @PostMapping("/reports/{reportId}/resolve")
    public ResponseEntity<String> resolveReport(@PathVariable Integer reportId) {
        boolean updated = reportService.updateReportStatus(reportId, ReportStatus.RESOLVED);
        if (updated) {
            return ResponseEntity.ok("Report marked as resolved and post status changed to BLOCKE");
        }
        return ResponseEntity.badRequest().body("Report not found");
    }

    @PostMapping("/reports/{reportId}/dismiss")
    public ResponseEntity<String> dismissReport(@PathVariable Integer reportId) {
        boolean updated = reportService.updateReportStatus(reportId, ReportStatus.DISMISSED);
        if (updated) {
            return ResponseEntity.ok("Report dismissed and post status changed to ACTIVE");
        }
        return ResponseEntity.badRequest().body("Report not found");
    }
}
