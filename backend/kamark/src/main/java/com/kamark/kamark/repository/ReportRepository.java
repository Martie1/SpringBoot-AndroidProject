package com.kamark.kamark.repository;

import com.kamark.kamark.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Integer> {
    boolean existsByUserIdAndPostId(Integer userId, Integer postId);
}
