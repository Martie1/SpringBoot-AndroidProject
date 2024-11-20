package com.kamark.kamark.repository;

import com.kamark.kamark.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<PostEntity, Integer> {
    @Override
    Optional<PostEntity> findById(Integer integer);
    List<PostEntity> findByUserId(Integer userId);
    List<PostEntity> findByRoomId(Integer roomId);
}
