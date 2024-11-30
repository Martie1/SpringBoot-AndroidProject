package com.kamark.kamark.service;

import com.kamark.kamark.dto.PostResponseDTO;
import com.kamark.kamark.entity.PostEntity;
import com.kamark.kamark.entity.ReportEntity;
import com.kamark.kamark.entity.UserEntity;
import com.kamark.kamark.entity.ReportStatus;
import com.kamark.kamark.exceptions.AlreadyExistsException;
import com.kamark.kamark.repository.LikeRepository;
import com.kamark.kamark.repository.PostRepository;
import com.kamark.kamark.repository.ReportRepository;
import com.kamark.kamark.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.webjars.NotFoundException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReportServiceTest {

    @Mock
    private ReportRepository reportRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private LikeRepository likeRepository;

    @InjectMocks
    private ReportService reportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetReportsByPostId_WhenReportsExist() {
        // Arrange
        Integer postId = 1;
        ReportEntity report = new ReportEntity();
        report.setId(1);

        when(reportRepository.findByPostId(postId)).thenReturn(Arrays.asList(report));

        // Act
        List<ReportEntity> result = reportService.getReportsByPostId(postId);

        // Assert
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
        verify(reportRepository, times(1)).findByPostId(postId);
    }

    @Test
    void testGetReportsByPostId_WhenNoReportsExist() {
        // Arrange
        Integer postId = 1;
        when(reportRepository.findByPostId(postId)).thenReturn(Collections.emptyList());

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class, () -> reportService.getReportsByPostId(postId));
        assertEquals("No reports found for post with id: " + postId, exception.getMessage());
        verify(reportRepository, times(1)).findByPostId(postId);
    }

    @Test
    void testReportPost_WhenNotReported() {
        // Arrange
        Integer postId = 1, userId = 1;
        String reason = "Spam";

        PostEntity post = new PostEntity();
        post.setId(postId);
        UserEntity user = new UserEntity();
        user.setId(userId);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(reportRepository.existsByUserIdAndPostId(userId, postId)).thenReturn(false);

        // Act
        boolean result = reportService.reportPost(postId, userId, reason);

        // Assert
        assertTrue(result);
        verify(reportRepository, times(1)).save(any(ReportEntity.class));
        verify(postRepository, times(1)).save(post);
    }

    @Test
    void testReportPost_WhenAlreadyReported() {
        // Arrange
        Integer postId = 1, userId = 1;
        String reason = "Spam";

        PostEntity post = new PostEntity();
        post.setId(postId);
        UserEntity user = new UserEntity();
        user.setId(userId);

        // Ustawiamy, że raport już istnieje
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(reportRepository.existsByUserIdAndPostId(userId, postId)).thenReturn(true);  // Raport już istnieje

        // Act & Assert
        AlreadyExistsException exception = assertThrows(AlreadyExistsException.class,
                () -> reportService.reportPost(postId, userId, reason));
        assertEquals("You already have reported this post", exception.getMessage());
        verify(reportRepository, never()).save(any(ReportEntity.class));  // Sprawdzamy, że zapis raportu nie miał miejsca
    }


    @Test
    void testUpdateReportsStatusByPostId_WhenReportsExist() {
        // Arrange
        Integer postId = 1;
        ReportStatus status = ReportStatus.RESOLVED;

        ReportEntity report = new ReportEntity();
        report.setId(1);
        report.setStatus(ReportStatus.PENDING);

        when(reportRepository.findByPostId(postId)).thenReturn(Arrays.asList(report));

        // Act
        boolean result = reportService.updateReportsStatusByPostId(postId, status);

        // Assert
        assertTrue(result);
        assertEquals(ReportStatus.RESOLVED, report.getStatus());
        verify(reportRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testUpdateReportsStatusByPostId_WhenNoReportsExist() {
        // Arrange
        Integer postId = 1;
        when(reportRepository.findByPostId(postId)).thenReturn(Collections.emptyList());

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> reportService.updateReportsStatusByPostId(postId, ReportStatus.RESOLVED));
        assertEquals("No reports found for post with id: " + postId, exception.getMessage());
        verify(reportRepository, never()).saveAll(anyList());
    }

    @Test
    void testUpdateReportStatus_WhenReportExists() {
        // Arrange
        Integer reportId = 1;
        ReportStatus status = ReportStatus.RESOLVED;

        ReportEntity report = new ReportEntity();
        report.setId(reportId);
        PostEntity post = new PostEntity();
        post.setStatus("ACTIVE");
        report.setPost(post);

        when(reportRepository.findById(reportId)).thenReturn(Optional.of(report));

        // Act
        boolean result = reportService.updateReportStatus(reportId, status);

        // Assert
        assertTrue(result);
        assertEquals(ReportStatus.RESOLVED, report.getStatus());
        assertEquals("BLOCKED", post.getStatus());
        verify(reportRepository, times(1)).save(report);
        verify(postRepository, times(1)).save(post);
    }

    @Test
    void testUpdateReportStatus_WhenReportNotFound() {
        // Arrange
        Integer reportId = 1;
        when(reportRepository.findById(reportId)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> reportService.updateReportStatus(reportId, ReportStatus.RESOLVED));
        assertEquals("Report not found with id: " + reportId, exception.getMessage());
        verify(reportRepository, never()).save(any(ReportEntity.class));
    }
}
