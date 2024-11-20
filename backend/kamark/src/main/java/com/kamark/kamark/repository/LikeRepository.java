package com.kamark.kamark.repository;

import com.kamark.kamark.entity.Like;
import com.kamark.kamark.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Integer> {
    Integer countByPostId(Integer postId);
    List<Like> findByUserId(Integer userId);

    Optional<Like> findByPostIdAndUserId(Integer postId, Integer userId);
}
