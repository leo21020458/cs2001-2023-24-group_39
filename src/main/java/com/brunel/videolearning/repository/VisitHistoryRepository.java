package com.brunel.videolearning.repository;

import com.brunel.videolearning.model.VisitHistory;
import com.brunel.videolearning.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VisitHistoryRepository extends CrudRepository<VisitHistory, Long> {

    List<VisitHistory> findAllByUserOrderByIdDesc(User user);

}
