package com.brunel.videolearning.model;

import jakarta.persistence.*;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Date;

/**
 When a user applies for a role change, store the application.
 Set the approval time field to the approval time once approved.
 If the approval time field of an application is empty, it means the application has not been approved yet.
 A user cannot have two unapproved applications simultaneously.
 */
@Entity
@Table
@EntityListeners(AuditingEntityListener.class)
public class ChangingRoleType implements Serializable {


    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn
    User user;  //Applicant user

    //apply to change role type to
    @Range(min = 1, max = 3)
    Integer toRoleType;

    //When was it approved? If it's empty, it means it hasn't been approved yet.
    Date approvedTime;


    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;


    public ChangingRoleType() {
        super();
    }

    public ChangingRoleType(User user, Integer toRoleType) {
        this.user = user;
        this.toRoleType = toRoleType;
        this.approvedTime = approvedTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getToRoleType() {
        return toRoleType;
    }

    public void setToRoleType(Integer toRoleType) {
        this.toRoleType = toRoleType;
    }

    public Date getApprovedTime() {
        return approvedTime;
    }

    public void setApprovedTime(Date approvedTime) {
        this.approvedTime = approvedTime;
    }

    @Override
    public String toString() {
        return "ChangingRoleType [id=" + id + ", user=" + user.getId() + ", toRoleType="  +toRoleType + "]";
    }
}
