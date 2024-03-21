package com.brunel.videolearning.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brunel.videolearning.model.User;
import com.brunel.videolearning.repository.UserRepository;
import com.brunel.videolearning.controller.exception.ResourceNotFoundException;

@Service
public class UserService {

    Logger logger = LoggerFactory.getLogger(UserService.class);


    @Autowired
    UserRepository userRepository;

    public UserService() {
        super();
    }


    public Optional<User> findByID(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    /**
     * update user password
     *
     * @param userId
     * @param newPassword
     * @return
     */
    public User updatePassword(Long userId, String newPassword) {

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        user.setPassword(newPassword);
        userRepository.save(user);

        return user;
    }

    /**
     * update user email
     *
     * @param userId
     * @param newEmail
     * @return
     */
    public User updateEmail(Long userId, String newEmail) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        user.setEmail(newEmail);
        userRepository.save(user);
        return user;
    }

    /**
     * update user firstname and lastname
     *
     * @param userId
     * @param firstName
     * @param lastName
     * @return
     */
    public User updateName(Long userId, String firstName, String lastName) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        userRepository.save(user);
        return user;
    }


    /**
     * update user head image
     *
     * @param userId
     * @param headImageNo
     * @return
     */
    public User updateHeadImage(Long userId, Integer headImageNo) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        user.setHeadImageNo(headImageNo);
        userRepository.save(user);
        return user;
    }


}
