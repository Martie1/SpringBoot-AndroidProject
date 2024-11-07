package com.kamark.kamark.repository;

import com.kamark.kamark.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Integer> {
    @Override
    Optional<Post> findById(Integer integer);

    List<Post> findByRoomId(Integer roomId);
}
