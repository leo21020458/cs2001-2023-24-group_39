package com.brunel.videolearning.service;

import com.brunel.videolearning.model.FavoritesCourse;
import com.brunel.videolearning.model.User;
import com.brunel.videolearning.model.VideoCourse;
import com.brunel.videolearning.repository.FavoritesCourseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 Users can bookmark courses and un-bookmark them.
 The implementation involves using a database table to store all courses bookmarked by a user.
 When user needs to un-bookmark a course, the corresponding bookmark record is deleted from the table.
 */
@Service
public class FavoritesCourseService {
    Logger logger = LoggerFactory.getLogger(FavoritesCourseService.class);

    @Autowired
    FavoritesCourseRepository favoritesCourseRepository;

    //Add a course to a person's bookmark list.
    public void addFavoritesCourse(FavoritesCourse favoritesCourse) {
        favoritesCourseRepository.save(favoritesCourse);
    }

    //Remove a course that a person likes from their bookmark list.
    public void removeFavoritesCourse(User user, VideoCourse videoCourse) {
        favoritesCourseRepository.deleteFavoritesCourseByUserAndVideoCourse(user,videoCourse);

    }

    //Retrieve all bookmarked courses in a person's bookmark list.
    public List<FavoritesCourse> findAllByUser(User user) {
        return favoritesCourseRepository.findAllByUser(user);
    }
}
