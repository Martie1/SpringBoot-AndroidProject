package com.kamark.kamark.service;

import com.kamark.kamark.dto.PostResponseDTO;
import com.kamark.kamark.dto.ReportPostDTO;
import com.kamark.kamark.entity.PostEntity;
import com.kamark.kamark.entity.ReportEntity;
import com.kamark.kamark.entity.UserEntity;
import com.kamark.kamark.repository.LikeRepository;
import com.kamark.kamark.repository.PostRepository;
import com.kamark.kamark.repository.ReportRepository;
import com.kamark.kamark.repository.UserRepository;
import com.kamark.kamark.service.interfaces.ReportServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.Optional;
import com.kamark.kamark.entity.ReportStatus;

@Service
public class ReportService implements ReportServiceInterface {

    private final ReportRepository reportRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;

    public ReportService(ReportRepository reportRepository, PostRepository postRepository, UserRepository userRepository,LikeRepository likeRepository){
        this.reportRepository = reportRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.likeRepository = likeRepository;
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

        postRepository.save(post);

        return true;
    }


    public List<PostResponseDTO> getReportedPostsByRoomId(Integer roomId) {
        List<PostEntity> posts = postRepository.findByRoomId(roomId);

        return posts.stream()
                .filter(post -> post.getReports() != null && !post.getReports().isEmpty())
                .filter(post -> post.getReports().stream()
                        .anyMatch(report -> report.getStatus() == ReportStatus.PENDING))
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public boolean updateReportsStatusByPostId(Integer postId, ReportStatus status) {

        List<ReportEntity> reports = reportRepository.findByPostId(postId);
        if (reports.isEmpty()) {
            return false;
        }
        reports.forEach(report -> report.setStatus(status));
        reportRepository.saveAll(reports);
        return true;
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
    private PostResponseDTO mapToDTO(PostEntity post) {
        PostResponseDTO dto = new PostResponseDTO();
        dto.setId(post.getId());
        dto.setName(post.getName());
        dto.setDescription(post.getDescription());
        dto.setStatus(post.getStatus());
        dto.setCreatedAt(post.getCreatedAt());

        if (post.getUser() != null) {
            dto.setUserId(post.getUser().getId());
            dto.setUsername(post.getUser().getUsername());
        }

        if (post.getRoom() != null) {
            dto.setRoomId(post.getRoom().getId());
        }

        dto.setLikeCount(likeRepository.countByPostId(post.getId()));

        return dto;
    }

}
