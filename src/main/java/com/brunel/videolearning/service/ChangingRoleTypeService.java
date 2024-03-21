package com.brunel.videolearning.service;


import com.brunel.videolearning.model.ChangingRoleType;
import com.brunel.videolearning.model.User;
import com.brunel.videolearning.repository.ChangingRoleTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChangingRoleTypeService {

    @Autowired
    ChangingRoleTypeRepository changingRoleTypeRepository;

    public ChangingRoleTypeService() {
        super();
    }

    /**
     Retrieve the current role type change application for a user.
     If no value is found, it means no role-type change application has been submitted.
     If a value is found, it means a role-type change application is currently being processed.
     *
     * @param user
     * @return
     */
    public Optional<ChangingRoleType> findChangingRoleTypeByUserAndApprovedTime(User user) {
        return changingRoleTypeRepository.findChangingRoleTypeByUserAndApprovedTime(user, null);
    }


    /**
     * When submitting a role-type change application, a user can only have one unapproved application at a time.
     * @param user
     * @param roRoleType
     * @return
     */
    public ChangingRoleType applyToChangeRoleType(User user, Integer roRoleType) {

        ChangingRoleType changingRoleType = new ChangingRoleType(user, roRoleType);
        changingRoleTypeRepository.save(changingRoleType);
        return changingRoleType;
    }


}
