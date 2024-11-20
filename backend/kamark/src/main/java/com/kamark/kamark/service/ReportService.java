package com.kamark.kamark.service;

import com.kamark.kamark.dto.ReportPostDTO;
import com.kamark.kamark.entity.PostEntity;
import com.kamark.kamark.entity.ReportEntity;
import com.kamark.kamark.entity.UserEntity;
import com.kamark.kamark.repository.PostRepository;
import com.kamark.kamark.repository.ReportRepository;
import com.kamark.kamark.repository.UserRepository;
import com.kamark.kamark.service.interfaces.ReportServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;
import com.kamark.kamark.entity.ReportStatus;

@Service
public class ReportService implements ReportServiceInterface {

    private final ReportRepository reportRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public ReportService(ReportRepository reportRepository, PostRepository postRepository, UserRepository userRepository) {
        this.reportRepository = reportRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public boolean reportPost(Integer postId, Integer userId, String reason) {
        Optional<PostEntity> postOptional = postRepository.findById(postId);
        if (postOptional.isEmpty()) {
            return false;
        }
        PostEntity post = postOptional.get();

        boolean alreadyReported = reportRepository.existsByUserIdAndPostId(userId, postId);
        if (alreadyReported) {
            return false;
        }

        Optional<UserEntity> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return false;
        }
        UserEntity user = userOptional.get();

        // nowe zglosznie
        ReportEntity report = new ReportEntity();
        report.setUser(user);
        report.setPost(post);
        report.setReason(reason);
        reportRepository.save(report);

        // zwieksz licznik zgloszen
        post.setReportCount(post.getReportCount() + 1);
        postRepository.save(post);

        return true;
    }

    public Integer getReportCount(Integer postId) {
        return postRepository.findById(postId)
                .map(PostEntity::getReportCount)
                .orElse(0);
    }

    public List<ReportPostDTO> getAllReportPosts() {
        List<ReportEntity> reports = reportRepository.findAll();
        return reports.stream()
                .map(report -> new ReportPostDTO(
                        report.getPost().getId(),
                        report.getReason(),
                        report.getStatus()))
                .collect(Collectors.toList());
    }
    public boolean updateReportStatus(Integer reportId, ReportStatus status) {
        Optional<ReportEntity> reportOptional = reportRepository.findById(reportId);
        if (reportOptional.isEmpty()) {
            return false;
        }
        ReportEntity report = reportOptional.get();
        report.setStatus(status);

        // ststus reporta "RESOLVED", to posta na "BLOCKED"
        if (status == ReportStatus.RESOLVED) {
            PostEntity post = report.getPost();
            post.setStatus("BLOCKED");
            postRepository.save(post);
        }

        // ststus reporta "DISMISSED", to status posta na "ACTIVE"
        if (status == ReportStatus.DISMISSED) {
            PostEntity post = report.getPost();
            post.setStatus("ACTIVE");
            postRepository.save(post);
        }

        reportRepository.save(report);
        return true;
    }
}
