package com.kamark.kamark.service;

import com.kamark.kamark.dto.ReportPostDTO;
import com.kamark.kamark.entity.Post;
import com.kamark.kamark.entity.Report;
import com.kamark.kamark.entity.User;
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
import java.util.Optional;

@Service
public class ReportService implements ReportServiceInterface {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    public boolean reportPost(Integer postId, Integer userId, String reason) {
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isEmpty()) {
            return false;
        }
        Post post = postOptional.get();

        boolean alreadyReported = reportRepository.existsByUserIdAndPostId(userId, postId);
        if (alreadyReported) {
            return false;
        }

        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return false;
        }
        User user = userOptional.get();

        // nowe zglosznie
        Report report = new Report();
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
                .map(Post::getReportCount)
                .orElse(0);
    }

    public List<ReportPostDTO> getAllReportPosts() {
        List<Report> reports = reportRepository.findAll();
        return reports.stream()
                .map(report -> new ReportPostDTO(
                        report.getPost().getId(),
                        report.getReason(),
                        report.getStatus()))
                .collect(Collectors.toList());
    }
    public boolean updateReportStatus(Integer reportId, ReportStatus status) {
        Optional<Report> reportOptional = reportRepository.findById(reportId);
        if (reportOptional.isEmpty()) {
            return false;
        }
        Report report = reportOptional.get();
        report.setStatus(status);

        // ststus reporta "RESOLVED", to posta na "BLOCKED"
        if (status == ReportStatus.RESOLVED) {
            Post post = report.getPost();
            post.setStatus("BLOCKED");
            postRepository.save(post);
        }

        // ststus reporta "DISMISSED", to status posta na "ACTIVE"
        if (status == ReportStatus.DISMISSED) {
            Post post = report.getPost();
            post.setStatus("ACTIVE");
            postRepository.save(post);
        }

        reportRepository.save(report);
        return true;
    }
}
