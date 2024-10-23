package com.kamark.kamark.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Data
@Entity
@Table(name="idea")
public class Idea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 255, nullable = false)
    private String description;

    @Column(unique = false, nullable = true)
    private Integer likes;

    @Column(nullable = false)
    private String status;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    //one idea belongs to one user
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; //one user

    // one idea belongs to one room
    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room; // one room
}