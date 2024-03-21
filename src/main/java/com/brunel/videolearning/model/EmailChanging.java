package com.brunel.videolearning.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Date;

/**
 * During the process of sending a verification code to an email address,
 * it is necessary to temporarily record what this verification code is.
 **/
@Entity
@Table
@EntityListeners(AuditingEntityListener.class)
public class EmailChanging implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank
    @Column(unique = true)
    @Email
    String email;

    //When sending a verification code to an email, store the code here.
    String verifyCode;

    //If it is not empty, it is the verification time for that email. If it is empty, it means the email has not been verified yet.
    Date emailVerifiedDate;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;


    public EmailChanging() {
        super();
    }
    public EmailChanging(String email, String verifyCode, Date emailVerifiedDate) {
        this.email = email;
        this.verifyCode = verifyCode;
        this.emailVerifiedDate = emailVerifiedDate;
    }

    @Override
    public String toString() {
        return "EmailChaning [id=" + id + ", email=" + email + ", verifyCode=" + verifyCode + ",emailVerifiedDate" + emailVerifiedDate + "]";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public Date getEmailVerifiedDate() {
        return emailVerifiedDate;
    }

    public void setEmailVerifiedDate(Date emailVerifiedDate) {
        this.emailVerifiedDate = emailVerifiedDate;
    }
}
