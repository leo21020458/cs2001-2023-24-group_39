package com.brunel.videolearning.service;

import com.brunel.videolearning.model.User;
import com.brunel.videolearning.model.VisitHistory;
import com.brunel.videolearning.repository.VisitHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * User Course Browsing History Service
 */
@Service
public class VisitHistoryService {

    @Autowired
    VisitHistoryRepository visitHistoryRepository;

    //User can retrieve browsing course visit history list.
    public List<VisitHistory> findAllByUserOrderByIdDesc(User user) {
        return visitHistoryRepository.findAllByUserOrderByIdDesc(user);
    }


    //When a user browses a course, the system should record their browsing history.
    // This method addVisitHistory should be called by my colleague.
    public VisitHistory addVisitHistory(VisitHistory visitHistory) {
        return visitHistoryRepository.save(visitHistory);
    }
}
