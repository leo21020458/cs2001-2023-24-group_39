package com.brunel.videolearning.dto;

public class ChangeEmailDTO {


    String newEmail;
    String verifyCode;

    public ChangeEmailDTO(String newEmail, String verifyCode) {
        this.newEmail = newEmail;
        this.verifyCode = verifyCode;
    }

    public String getNewEmail() {
        return newEmail;
    }

    public void setNewEmail(String newEmail) {
        this.newEmail = newEmail;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }
}
