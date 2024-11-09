package com.kamark.kamark.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.Set;

@Data
@Entity
@Table(name="post") //Post
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 255, nullable = false)
    private String description;

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

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Like> likes; //many likes

    @Column(name = "report_count", nullable = false)
    private Integer reportCount = 0;

}