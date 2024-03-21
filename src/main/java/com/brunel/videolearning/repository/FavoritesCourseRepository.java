package com.brunel.videolearning.repository;

import com.brunel.videolearning.model.FavoritesCourse;
import com.brunel.videolearning.model.User;
import com.brunel.videolearning.model.VideoCourse;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface FavoritesCourseRepository extends CrudRepository<FavoritesCourse, Long> {
    List<FavoritesCourse> findAllByUser(User user);

    @Transactional
    @Modifying
    void deleteFavoritesCourseByUserAndVideoCourse(User user, VideoCourse videoCourse);

}
