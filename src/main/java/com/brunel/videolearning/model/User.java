package com.brunel.videolearning.model;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.*;

import org.hibernate.validator.constraints.Range;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * User model
 */

@Entity
@Table(name = "Users")
@EntityListeners(AuditingEntityListener.class)
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    public final static int TYPE_NONE = 0;
    public final static int TYPE_STUDENT = 1;
    public final static int TYPE_TEACHER = 2;
    public final static int TYPE_ADMIN = 3;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank
    String firstName;

    @NotBlank
    String lastName;


    //不论是新增还是修改email，都需要验证邮箱地址
    @NotBlank
    @Column(unique = true)
    @Email
    String email;

    @NotBlank
    String password;

    //头像编号，不可以小于1，默认1号头像
    @Min(1)
    int headImageNo = 1;

    //是否被禁言。1表示被禁言，0表示正常。默认是0
    @Range(min = 0, max = 1)
    int banStatus = 0;

    @Range(min = 0, max = 1)
    int userType;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;


    public User() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * 这个静态方法给 注册新用户的方法使用，只需要4个必填字段就可以生成一个新用户了
     */
    public static User createANewUser(String firstName, String lastName, String email, String password) {

        int headImageNo = 1; //注册的时候默认头像是1号
        int banStatus = 0;   //注册的时候默认未被禁言
        //注册的时候默认就是学生
        return new User(firstName, lastName, email, password, headImageNo, banStatus, TYPE_STUDENT);
    }


    //新加入的属性，需要在这个构造放里面加入，因为从数据库查出的每个记录，都会自动调用这个new User的构造方法；如果加入这些新字段，这些字段不会被传出去（不会被new到User对象里面去）
    public User(String firstName, String lastName, String email, String password, int headImageNo, int banStatus, int userType) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.headImageNo = headImageNo;
        this.banStatus = banStatus;
        this.userType = userType;
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


    public String getFirstName() {
        return firstName;
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }


    public int getHeadImageNo() {
        return headImageNo;
    }

    public void setHeadImageNo(int headImageNo) {
        this.headImageNo = headImageNo;
    }

    public int getBanStatus() {
        return banStatus;
    }

    public void setBanStatus(int banStatus) {
        this.banStatus = banStatus;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + ", password=" + password + ",headImageNo" + headImageNo + " ,banStatus" + banStatus + ", userType="
                + userType + "]";
    }


}
