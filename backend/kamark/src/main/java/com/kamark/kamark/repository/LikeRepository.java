package com.kamark.kamark.repository;

import com.kamark.kamark.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Integer> {
    int countByPostId(Integer postId); // counts all likes of post
}
