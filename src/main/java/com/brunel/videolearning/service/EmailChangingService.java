package com.brunel.videolearning.service;

import com.brunel.videolearning.model.EmailChanging;
import com.brunel.videolearning.repository.EmailChangingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.Random;

/**
 To ensure the accuracy of the user's email, the system needs to send a verification code to the email address, a task handled by this service.
 Since I am not yet able to write the code for sending the verification code to the email, I have skipped that action.
 For now, let's define that if the verification code value input is 1234, the system considers it as correct.s
 */
@Service
public class EmailChangingService {
    Logger logger = LoggerFactory.getLogger(EmailChangingService.class);

    @Autowired
    EmailChangingRepository emailChangingRepository;

    public EmailChangingService() {
        super();
    }


    // If the email already exists in the table and the emailVerifiedDate field is not empty,
    // it indicates that the email has already been verified.
    // In the case where an email has already been verified, a verification code cannot be sent again.
    public boolean isVerified(String email) {

        Optional<EmailChanging> opEmailChanging = findByEmail(email);
        if (opEmailChanging.isEmpty()) {
            return false;
        }
        EmailChanging emailChanging = opEmailChanging.get();
        if (emailChanging.getEmailVerifiedDate() == null) {
            return false;
        }
        return true;
    }

    /**
     * When an email address has not been verified, an email can be sent, returning true;
     * when an email address has already been verified, an email cannot be sent, returning false.
     * @param newEmail
     * @return
     */
    public boolean sendEmailVerifyCode(String newEmail) {
        //If it has already been verified, there is no need to send a verification code.
        if (isVerified(newEmail)) {
            return false;
        }

        Random random = new Random();
        int code = random.nextInt(9000) + 1000;
        String emailChangingToken = String.valueOf(code);

        //If the record table already has this email address but the final verification has not been completed,
        // a new verification code should be updated, returning true.
        Optional<EmailChanging> opEmailChanging = findByEmail(newEmail);
        if (opEmailChanging.isPresent()) {
            EmailChanging emailChanging = opEmailChanging.get();
            if ((emailChanging.getVerifyCode() != null) && (emailChanging.getEmailVerifiedDate() == null)) {
                emailChanging.setVerifyCode(emailChangingToken);
                emailChangingRepository.save(emailChanging);
                sendVerifyCodeOut(newEmail, emailChangingToken);
                return true;
            }
        }


        //If the record table does not have this email address, indicating that the address has not been verified,
        // we will generate a new verification code for this address.
        EmailChanging emailChanging = new EmailChanging(newEmail, emailChangingToken, null);
        emailChangingRepository.save(emailChanging);
        sendVerifyCodeOut(newEmail, emailChangingToken);
        return true;
    }

    /**
     * Send an email verification code to the newEmail address.
     * @param newEmail
     * @param verifyCode
     */
    private void sendVerifyCodeOut(String newEmail, String verifyCode) {
        logger.info(" Send an email verification code: "+verifyCode +" to the newEmail address: "+newEmail);
        //I do not know how to write the email sending functionality yet, so I will skip this part.
    }


    /**
     * Verify if the email verification code passed in is correct.
     * Since I do not know how to write the email sending code, the program is temporarily set up to consider 1234 as a correct verification code.
     * @param verifyCode
     * @return
     */
    public boolean verify(String email, String verifyCode) {
        Optional<EmailChanging> opEmailChanging = findByEmail(email);
        if (opEmailChanging.isPresent()) {
            EmailChanging thisEmailChanging = opEmailChanging.get();
            //If the verifyCode passed in is the same as the one recorded in the database, then the verification is considered successful.
            if (thisEmailChanging.getVerifyCode().equals(verifyCode)) {
                return true;
            } else {
                // the program is temporarily set up to consider 1234 as a correct verification code.
                if (verifyCode.equals("1234")) {
                    return true;
                }
            }
        } else {
            logger.warn("The email address passed in has not been sent a verification code before");
        }
        return false;
    }


    //Retrieve the email verification code sending status for the provided email address.
    private Optional<EmailChanging> findByEmail(String email) {
        EmailChanging emailChanging = emailChangingRepository.findByEmail(email);
        return Optional.ofNullable(emailChanging);
    }

    //update status to verifyed

    public Optional<EmailChanging> findByEmailAndVerifyCodeAndEmailVerifiedDateNull(String email,String verifyCode) {
        return emailChangingRepository.findByEmailAndVerifyCodeAndEmailVerifiedDateNull(email,verifyCode);
    }

    //update record emailVerifyDate to now time.
    public void updateEmailVerifyDateToNow(EmailChanging emailChanging) {
        emailChanging.setEmailVerifiedDate(new Date());
        emailChangingRepository.save(emailChanging);
    }

}
