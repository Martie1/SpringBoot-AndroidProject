package com.kamark.kamark.repository;

import com.kamark.kamark.entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository  extends JpaRepository<RoomEntity, Integer> {
    boolean existsByName(String name);
}
