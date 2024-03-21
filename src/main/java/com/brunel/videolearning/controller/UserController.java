package com.brunel.videolearning.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import com.brunel.videolearning.model.*;
import com.brunel.videolearning.service.*;
import com.brunel.videolearning.controller.response.ServerResponse;
import com.brunel.videolearning.dto.ChangeEmailDTO;
import com.brunel.videolearning.dto.ChangePasswordDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.brunel.videolearning.dto.UserPostDTO;

@RestController
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;
    @Autowired
    EmailChangingService emailChangingService;
    @Autowired
    VisitHistoryService visitHistoryService;
    @Autowired
    FavoritesCourseService favoritesCourseService;
    @Autowired
    ChangingRoleTypeService changingRoleTypeService;
    @Autowired
    VideoCourseService videoCourseService;


    /**
     * Check if the email of the current logged-in user still exists in the database.
     * If the user does not exist, the 'data' attribute of the ServerResponse is empty.
     * If the user exists, the 'data' attribute of the ServerResponse is the User.
     * @param email
     * @return
     */
    private ServerResponse checkEmailStillInDatabase(String email) {
        Optional<User> curOpUser = userService.findByEmail(email);
        User curUser = null;
        ServerResponse serverResponse =null;
        if (curOpUser.isEmpty()) {
            serverResponse= ServerResponse.fail(1, "Warning ,this logon user's email "+email + " is not in database!");
            return serverResponse;
        }else  {
            curUser = curOpUser.get();
            serverResponse = ServerResponse.ok("",curUser);
            return serverResponse;
        }
    }
    // 1. GET /login  is managed by LoginFilter

    /**
     * Get information about the current logged-in user
     * @param principal This parameter is a feature of Spring Security, from which the username of the logged-in user can be obtained. Here it has been customized to be the user's email, uniquely representing a user.
     * @return
     */
    @GetMapping("/user/getCurrentLogonUserInfo")
    public ResponseEntity<ServerResponse> getCurrentLogonUser(Principal principal) {
        String curUserEmail = principal.getName();
        User curUser = null;

        ServerResponse serverResponse = checkEmailStillInDatabase(curUserEmail);
        if (serverResponse.getCode()!=0) {
            return new ResponseEntity<ServerResponse>(serverResponse, HttpStatus.BAD_REQUEST);
        }else {
            curUser = (User) serverResponse.getData();
        }

        serverResponse = ServerResponse.ok(0, "Successfully retrieved information about the current logged-in user", curUser);
        return new ResponseEntity<ServerResponse>(serverResponse, HttpStatus.OK);
    }


    /**
     * Check if this email can be registered. Return true if it can be registered, and false if it cannot.
     * This functionality can be used to check if an email can be used before registering.
     */
    @GetMapping("/user/checkEmailCanBeRegister")
    public ResponseEntity<ServerResponse> checkEmailCanBeRegister(@RequestParam String email) {
        boolean canBeRegisterable = false;
        ServerResponse serverResponse = null;
        Optional<User> opUser = userService.findByEmail(email);

        if (opUser.isPresent()) {
            serverResponse = ServerResponse.ok(0, "This email is registered", false);
            return new ResponseEntity<ServerResponse>(serverResponse, HttpStatus.OK);
        }else {
            serverResponse = ServerResponse.ok(0, "This email is not registered yet", true);
            return new ResponseEntity<ServerResponse>(serverResponse, HttpStatus.OK);
        }
    }

    /**
     * When a logged-in user changes their email, they need to send an email verification code.
     */

    @GetMapping("/user/sendEmailVerifyCodeForLogonUser")
    public ResponseEntity<ServerResponse> sendEmailVerifyCode(Principal principal, @RequestParam String newEmail) {

        String curUserEmail = principal.getName();
        User curUser = null;

        ServerResponse serverResponse = checkEmailStillInDatabase(curUserEmail);
        if (serverResponse.getCode()!=0) {
            return new ResponseEntity<ServerResponse>(serverResponse, HttpStatus.BAD_REQUEST);
        }else {
            curUser = (User) serverResponse.getData();
        }

        if (newEmail.isEmpty()) {
            serverResponse = ServerResponse.fail(1, "The new email cannot be empty.");
            return new ResponseEntity<ServerResponse>(serverResponse, HttpStatus.BAD_REQUEST);
        }

        if (newEmail.equals(curUserEmail)) {
            serverResponse = ServerResponse.fail(1, "The new email cannot be the same as the current email.");
            return new ResponseEntity<ServerResponse>(serverResponse, HttpStatus.BAD_REQUEST);
        }

        if (emailChangingService.isVerified(newEmail)) {
            serverResponse = ServerResponse.fail(1, "The new email has already been verified, so there is no need to send the verification code again.");
            return new ResponseEntity<ServerResponse>(serverResponse, HttpStatus.BAD_REQUEST);
        }

        emailChangingService.sendEmailVerifyCode(newEmail);
        serverResponse = ServerResponse.ok(1, "The verification code has been sent to the email.", null);
        return new ResponseEntity<ServerResponse>(serverResponse, HttpStatus.OK);

    }

    /**
     * Change the email used by the currently logged-in user.
     * @param principal currently logged-in user.
     * @param changeEmailDTO  The frontend passes in two values encapsulated inside this DTO: String newEmail and String verifyCode.
     * @return
     */
    @PostMapping("/user/changeEmailForLogonUser")
    public ResponseEntity<ServerResponse> changeEmail(Principal principal, @RequestBody ChangeEmailDTO changeEmailDTO) {
        String curUserEmail = principal.getName();

        String newEmail = changeEmailDTO.getNewEmail();
        String verifyCode = changeEmailDTO.getVerifyCode();

        if (!emailChangingService.verify(newEmail, verifyCode)) {
            ServerResponse serverResponse = ServerResponse.fail(1, " unsuccessful changing email  , verifyCode is " + verifyCode + "  , may be incorrect verify code entered");
            return new ResponseEntity<ServerResponse>(serverResponse, HttpStatus.BAD_REQUEST);
        }

        User curUser = null;
        ServerResponse serverResponse = checkEmailStillInDatabase(curUserEmail);
        if (serverResponse.getCode()!=0) {
            return new ResponseEntity<ServerResponse>(serverResponse, HttpStatus.BAD_REQUEST);
        }else {
            curUser = (User) serverResponse.getData();
        }

        Optional<EmailChanging> opEmailChanging = emailChangingService.findByEmailAndVerifyCodeAndEmailVerifiedDateNull(newEmail, verifyCode);
        if (opEmailChanging.isEmpty() ) {
            serverResponse = ServerResponse.fail(1, " unsuccessful changing email  , verifyCode is " + verifyCode + "  , may be incorrect verify code entered");
            return new ResponseEntity<ServerResponse>(serverResponse, HttpStatus.BAD_REQUEST);
        }

        EmailChanging emailChanging = opEmailChanging.get();
        //update email changing record attribute emailVerifyDate to now time.
        emailChangingService.updateEmailVerifyDateToNow(emailChanging);
        //update user email
        userService.updateEmail(curUser.getId(), newEmail);
        //将新的用户对象发还发给前端
        Optional<User> opUser = userService.findByID(curUser.getId());

        serverResponse = ServerResponse.ok("Email successfully modified.", opUser);
        return new ResponseEntity<ServerResponse>(serverResponse, HttpStatus.OK);

    }

    /**
     * Changing the password for the currently logged-in user.
     * @param principal  currently logged-in user
     * @param changePasswordDTO
     * @return
     */
    @PostMapping("/user/updatePassword")
    public ResponseEntity<ServerResponse> updatePassword(Principal principal, @RequestBody ChangePasswordDTO changePasswordDTO) {
        String curUserEmail = principal.getName();

        String inputOldPassword = changePasswordDTO.getOldPassword();
        String inputNewPassword1 = changePasswordDTO.getNewPassword1();
        String inputNewPassword2 = changePasswordDTO.getNewPassword2();

        String email = principal.getName();

        User curUser = null;
        ServerResponse serverResponse = checkEmailStillInDatabase(curUserEmail);
        if (serverResponse.getCode()!=0) {
            return new ResponseEntity<ServerResponse>(serverResponse, HttpStatus.BAD_REQUEST);
        }else {
            curUser = (User) serverResponse.getData();
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        //The BCrypt algorithm for comparing whether the old and new passwords
        // are the same uses the matches method inside the encoder,
        // with the input parameters being the plaintext old password and the encrypted password from the database.
        boolean isSame = encoder.matches(inputOldPassword, curUser.getPassword());
        if (!isSame) {
            serverResponse = ServerResponse.fail(1, "The old password entered is incorrect.");
            return new ResponseEntity<ServerResponse>(serverResponse, HttpStatus.BAD_REQUEST);
        }

        if (!inputNewPassword1.equals(inputNewPassword2)) {
            serverResponse = ServerResponse.fail(1, "The new passwords entered twice do not match.");
            return new ResponseEntity<ServerResponse>(serverResponse, HttpStatus.BAD_REQUEST);
        }

        if (encoder.matches(inputNewPassword1, curUser.getPassword())) {
            serverResponse = ServerResponse.fail(1, "The entered new and old passwords are the same, no need to change the password.");
            return new ResponseEntity<ServerResponse>(serverResponse, HttpStatus.BAD_REQUEST);
        }

        String encryptedNewPassword = encoder.encode(inputNewPassword1);
        userService.updatePassword(curUser.getId(), encryptedNewPassword);

        serverResponse = ServerResponse.ok(1, "Password successfully changed.", null);
        return new ResponseEntity<ServerResponse>(serverResponse, HttpStatus.OK);

    }

    /**
     * Change name.
     * @param principal
     * @param userDTO It contains firstName and lastName.
     * @return
     */
    @PostMapping("/user/updateName")
    public ResponseEntity<ServerResponse> updateName(Principal principal, @RequestBody UserPostDTO userDTO) {

        String curUserEmail = principal.getName();

        User curUser = null;
        ServerResponse serverResponse = checkEmailStillInDatabase(curUserEmail);
        if (serverResponse.getCode()!=0) {
            return new ResponseEntity<ServerResponse>(serverResponse, HttpStatus.BAD_REQUEST);
        }else {
            curUser = (User) serverResponse.getData();
        }

        String fisrtName = userDTO.getFirstName();
        String lastName = userDTO.getLastName();

        if ((fisrtName.isEmpty()) || (lastName.isEmpty())) {
            serverResponse = ServerResponse.fail(1, "Firstname or lastname can not be empty. ");
            return new ResponseEntity<ServerResponse>(serverResponse, HttpStatus.BAD_REQUEST);
        }

        userService.updateName(curUser.getId(), fisrtName, lastName);

        Optional<User> opUser = userService.findByID(curUser.getId());
        serverResponse = ServerResponse.ok(1,"Name successfully changed.", opUser);
        return new ResponseEntity<ServerResponse>(serverResponse, HttpStatus.OK);

    }

    /**
     * Change avatar. Avatars are represented by numbers.
     * The front-end UI provides x avatars, each avatar's filename is named with its corresponding id.
     * @param principal
     * @param headImage  Avatars id
     * @return
     */
    @GetMapping("/user/changeHeadImage")
    public ResponseEntity<ServerResponse> changeHeadImage(Principal principal, @RequestParam Integer headImage) {

        String curUserEmail = principal.getName();

        User curUser = null;
        ServerResponse serverResponse = checkEmailStillInDatabase(curUserEmail);
        if (serverResponse.getCode()!=0) {
            return new ResponseEntity<ServerResponse>(serverResponse, HttpStatus.BAD_REQUEST);
        }else {
            curUser = (User) serverResponse.getData();
        }

        userService.updateHeadImage(curUser.getId(),headImage);

        Optional<User> opUser = userService.findByID(curUser.getId());
        serverResponse = ServerResponse.ok(1,"Avatar successfully changed.", opUser);
        return new ResponseEntity<ServerResponse>(serverResponse, HttpStatus.OK);
    }


    /**
     * Get browsing history of the currently logged-in user.
     * @param principal
     * @return
     */
    @GetMapping("/user/getVisitHistory")
    public ResponseEntity<ServerResponse> getVisitHistory(Principal principal) {

        String curUserEmail = principal.getName();

        User curUser = null;
        ServerResponse serverResponse = checkEmailStillInDatabase(curUserEmail);
        if (serverResponse.getCode()!=0) {
            return new ResponseEntity<ServerResponse>(serverResponse, HttpStatus.BAD_REQUEST);
        }else {
            curUser = (User) serverResponse.getData();
        }

        List<VisitHistory> list = visitHistoryService.findAllByUserOrderByIdDesc(curUser);
        serverResponse = ServerResponse.ok(0,"Successfully retrieved the user's browsing history.", list);
        return new ResponseEntity<ServerResponse>(serverResponse, HttpStatus.OK);

    }

    /**
     * Get the list of courses in the favorites folder of the currently logged-in user.
     * @param principal
     * @return
     */
    @GetMapping("/user/getFavoritesCourse")
    public ResponseEntity<ServerResponse> getFavoritesCourse(Principal principal) {

        String curUserEmail = principal.getName();

        User curUser = null;
        ServerResponse serverResponse = checkEmailStillInDatabase(curUserEmail);
        if (serverResponse.getCode()!=0) {
            return new ResponseEntity<ServerResponse>(serverResponse, HttpStatus.BAD_REQUEST);
        }else {
            curUser = (User) serverResponse.getData();
        }

        List<FavoritesCourse> list = favoritesCourseService.findAllByUser(curUser);
        serverResponse = ServerResponse.ok(0,"Successfully retrieved the list of courses in the favorites folder", list);
        return new ResponseEntity<ServerResponse>(serverResponse, HttpStatus.OK);

    }

    /**
     * The currently logged-in user un-bookmark a course.
     * @param principal
     * @param videoCouserId  un-bookmark course id
     * @return
     */
    @GetMapping("/user/removeFavoritesCourse")
    public ResponseEntity<ServerResponse> removeFavoritesCourse(Principal principal ,  @RequestParam Long videoCouserId) {

        String curUserEmail = principal.getName();

        User curUser = null;
        ServerResponse serverResponse = checkEmailStillInDatabase(curUserEmail);
        if (serverResponse.getCode()!=0) {
            return new ResponseEntity<ServerResponse>(serverResponse, HttpStatus.BAD_REQUEST);
        }else {
            curUser = (User) serverResponse.getData();
        }


        Optional<VideoCourse> needRmove = videoCourseService.findById(videoCouserId);
        if (needRmove.isEmpty()) {
             serverResponse = ServerResponse.fail(1, "The course does not exist " + videoCouserId );
            return new ResponseEntity<ServerResponse>(serverResponse, HttpStatus.BAD_REQUEST);
        }

        favoritesCourseService.removeFavoritesCourse(curUser,needRmove.get());
        //Reload the bookmark folder and return it to the front-end UI.
        List<FavoritesCourse> list = favoritesCourseService.findAllByUser(curUser);
        serverResponse = ServerResponse.ok(0,"Un-bookmark successful, returning the updated list of courses in the bookmark folder.", list);
        return new ResponseEntity<ServerResponse>(serverResponse, HttpStatus.OK);

    }

    /**
     * The currently logged-in user applying for a role change.
     * @param principal
     * @param toRoleType   role change target
     * @return
     */
    @GetMapping("/user/applyToChangeRoleType")
    public ResponseEntity<ServerResponse> applyToChangeRoleType(Principal principal ,  @RequestParam Integer  toRoleType) {

        String curUserEmail = principal.getName();

        User curUser = null;
        ServerResponse serverResponse = checkEmailStillInDatabase(curUserEmail);
        if (serverResponse.getCode()!=0) {
            return new ResponseEntity<ServerResponse>(serverResponse, HttpStatus.BAD_REQUEST);
        }else {
            curUser = (User) serverResponse.getData();
        }


        if ((toRoleType <1) || (toRoleType>3)) {
            serverResponse = ServerResponse.fail(1, "The target role value should be within the range of 1 to 3.");
            return new ResponseEntity<ServerResponse>(serverResponse, HttpStatus.BAD_REQUEST);
        }

        if (toRoleType == curUser.getUserType()) {
            serverResponse = ServerResponse.fail(1, "The current user is already in this role, no need to apply for a role change.");
            return new ResponseEntity<ServerResponse>(serverResponse, HttpStatus.BAD_REQUEST);
        }

        Optional<ChangingRoleType> mayBeHaveApplied = changingRoleTypeService.findChangingRoleTypeByUserAndApprovedTime(curUser);
        if (mayBeHaveApplied.isPresent()) {
            serverResponse = ServerResponse.fail(1, "The current user has already submitted an application for a role change, no need to apply again.");
            return new ResponseEntity<ServerResponse>(serverResponse, HttpStatus.BAD_REQUEST);
        }

        //Record the role change application in the database.
        ChangingRoleType apply = changingRoleTypeService.applyToChangeRoleType(curUser, toRoleType);
        serverResponse = ServerResponse.ok(0,"Role change application submitted.", apply);
        return new ResponseEntity<ServerResponse>(serverResponse, HttpStatus.OK);

    }


}
