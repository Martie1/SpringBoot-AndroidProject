package com.kamark.kamark.service;

import com.kamark.kamark.dto.PostResponseDTO;
import com.kamark.kamark.dto.ReportDTO;
import com.kamark.kamark.dto.ReportPostDTO;
import com.kamark.kamark.entity.PostEntity;
import com.kamark.kamark.entity.ReportEntity;
import com.kamark.kamark.entity.UserEntity;
import com.kamark.kamark.exceptions.AlreadyExistsException;
import com.kamark.kamark.repository.LikeRepository;
import com.kamark.kamark.repository.PostRepository;
import com.kamark.kamark.repository.ReportRepository;
import com.kamark.kamark.repository.UserRepository;
import com.kamark.kamark.service.interfaces.ReportServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.Optional;
import com.kamark.kamark.entity.ReportStatus;
import org.webjars.NotFoundException;

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

    public List<ReportDTO> getReportsByPostId(Integer postId) {
        List<ReportEntity> reports = reportRepository.findByPostId(postId);
        if (reports.isEmpty()) {
            throw new NotFoundException("No reports found for post with id: " + postId);
        }
        return reports.stream().map(ReportDTO::new).collect(Collectors.toList());
    }


    public boolean reportPost(Integer postId, Integer userId, String reason) {
        PostEntity post = postRepository.findById(postId).orElseThrow(
                () -> new NotFoundException("Post not found with id: " + postId)
        );
        UserEntity user = userRepository.findById(userId).orElseThrow(
                () -> new UsernameNotFoundException("User not found with id: " + userId)
        );

        boolean alreadyReported = reportRepository.existsByUserIdAndPostId(userId, postId);
        if (alreadyReported) {
            throw new AlreadyExistsException("You already have reported this post");
        }

        // new report
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
        if(posts.isEmpty()){
            throw new NotFoundException("No posts found in room with id: " + roomId);
        }

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
            throw new NotFoundException("No reports found for post with id: " + postId);
        }
        reports.forEach(report -> report.setStatus(status));
        reportRepository.saveAll(reports);
        return true;
    }


    public boolean updateReportStatus(Integer reportId, ReportStatus status) {

        ReportEntity report = reportRepository.findById(reportId).orElseThrow(
                () -> new NotFoundException("Report not found with id: " + reportId)
        );
        report.setStatus(status);

        // report is "RESOLVED" then post is  "BLOCKED"
        if (status == ReportStatus.RESOLVED) {
            PostEntity post = report.getPost();
            post.setStatus("BLOCKED");
            postRepository.save(post);
        }

        //  report is "DISMISSED" then post is "ACTIVE"
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
        dto.setUserId(post.getUser().getId());
        dto.setUsername(post.getUser().getUsername());
        dto.setRoomId(post.getRoom().getId());
        dto.setLikeCount(likeRepository.countByPostId(post.getId()));

        return dto;
    }

}
