package com.kamark.kamark.service;

import com.kamark.kamark.entity.Post;
import com.kamark.kamark.entity.Report;
import com.kamark.kamark.entity.User;
import com.kamark.kamark.repository.PostRepository;
import com.kamark.kamark.repository.ReportRepository;
import com.kamark.kamark.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    public boolean reportPost(Integer postId, Integer userId, String reason) {
        // Sprawdzenie, czy post istnieje
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isEmpty()) {
            return false; // Post nie istnieje
        }
        Post post = postOptional.get();

        // Sprawdzenie, czy użytkownik już zgłosił ten post
        boolean alreadyReported = reportRepository.existsByUserIdAndPostId(userId, postId);
        if (alreadyReported) {
            return false; // Użytkownik już zgłosił ten post
        }

        // Tworzenie nowego zgłoszenia
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return false; // Użytkownik nie istnieje
        }
        User user = userOptional.get();

        Report report = new Report();
        report.setUser(user);
        report.setPost(post);
        report.setReason(reason);

        reportRepository.save(report);

        // Aktualizacja liczby zgłoszeń w poście
        post.setReportCount(post.getReportCount() + 1);
        postRepository.save(post);

        return true;
    }
}
