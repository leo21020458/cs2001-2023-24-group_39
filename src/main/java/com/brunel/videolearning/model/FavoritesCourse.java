package com.brunel.videolearning.model;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Date;

/**
 * User's course bookmark folder
 */
@Entity
@Table
@EntityListeners(AuditingEntityListener.class)
public class FavoritesCourse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn
    User user;

    @ManyToOne
    @JoinColumn
    VideoCourse videoCourse;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;

    public FavoritesCourse() {
        super();
    }

    public FavoritesCourse(User user, VideoCourse videoCourse) {
        this.user = user;
        this.videoCourse = videoCourse;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {        return id;    }
    public void setId(Long id) {        this.id = id;    }

    public VideoCourse getVideoCourse() {
        return videoCourse;
    }

    public void setVideoCourse(VideoCourse videoCourse) {
        this.videoCourse = videoCourse;
    }
}
