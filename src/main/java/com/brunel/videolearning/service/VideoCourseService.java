package com.brunel.videolearning.service;

import com.brunel.videolearning.model.VideoCourse;
import com.brunel.videolearning.repository.VideoCourseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 Course Service
 Since this task is not under my responsibility, I have only used an id and name field to represent a course.
 */
@Service
public class VideoCourseService {

    Logger logger = LoggerFactory.getLogger(VideoCourseService.class);

    @Autowired
    VideoCourseRepository videoCourseRepository;

    public void addNewVideoCourse(VideoCourse newVideoCourse) {
        videoCourseRepository.save(newVideoCourse);
    }

    public Optional<VideoCourse> findById(Long videoCourseId) {
        return videoCourseRepository.findById(videoCourseId);

    }

}
