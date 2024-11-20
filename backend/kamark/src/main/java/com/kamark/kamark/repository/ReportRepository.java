package com.kamark.kamark.repository;

import com.kamark.kamark.entity.ReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<ReportEntity, Integer> {
    boolean existsByUserIdAndPostId(Integer userId, Integer postId);
    List<ReportEntity> findByPostRoomId(Integer roomId);
}
