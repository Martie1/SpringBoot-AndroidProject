package com.kamark.kamark.repository;

import com.kamark.kamark.entity.LikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<LikeEntity, Integer> {
    Integer countByPostId(Integer postId);
    List<LikeEntity> findByUserId(Integer userId);

    Optional<LikeEntity> findByPostIdAndUserId(Integer postId, Integer userId);
}
