package com.kamark.kamark.repository;

import com.kamark.kamark.entity.LikeEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<LikeEntity, Integer> {
    Integer countByPostId(Integer postId);
    List<LikeEntity> findByUserId(Integer userId);

    @Modifying
    @Transactional
    int deleteByPostId(Integer postId);
    Optional<LikeEntity> findByPostIdAndUserId(Integer postId, Integer userId);
}
