package com.kamark.kamark.repository;

import com.kamark.kamark.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository  extends JpaRepository<Room, Integer> {
}
