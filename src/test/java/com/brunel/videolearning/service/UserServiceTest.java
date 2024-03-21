package com.brunel.videolearning.service;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

class UserServiceTest {

    @Autowired
    UserService userService;

    @Test
    @Order(1)
    void findByEmail() {
    }

    @Test
    void findByID() {
        userService.findByID(1L);
    }

    @Test
    void testFindByID() {
    }



    @Test
    void updatePassword() {
    }

    @Test
    void updateEmail() {
    }

    @Test
    void updateName() {
    }

    @Test
    void updateHeadImage() {
    }
}