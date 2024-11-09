package com.kamark.kamark.repository;

import com.kamark.kamark.entity.Like;
import com.kamark.kamark.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<Like, Integer> {
    int countByPostId(Integer postId); // counts all likes of post
    List<Like> findByUserId(Integer userId);
}
