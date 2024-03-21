package com.brunel.videolearning.repository;

import com.brunel.videolearning.model.EmailChanging;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface EmailChangingRepository  extends CrudRepository<EmailChanging, Long> {
    EmailChanging findByEmail(String email);

    Optional<EmailChanging> findByEmailAndVerifyCodeAndEmailVerifiedDateNull(String email, String verifyCode);


}
