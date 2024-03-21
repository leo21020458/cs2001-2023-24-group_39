package com.brunel.videolearning.repository;

import com.brunel.videolearning.model.ChangingRoleType;
import com.brunel.videolearning.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.Optional;

public interface ChangingRoleTypeRepository extends CrudRepository<ChangingRoleType, Long> {

    Optional<ChangingRoleType> findChangingRoleTypeByUserAndApprovedTime(User user, Date approvedTime );

}
