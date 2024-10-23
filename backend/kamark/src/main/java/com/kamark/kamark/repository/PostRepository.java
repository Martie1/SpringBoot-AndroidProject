package com.kamark.kamark.repository;

import com.kamark.kamark.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {
}
