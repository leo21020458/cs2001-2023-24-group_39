package com.brunel.videolearning.security;

/**
 * The name and password of "login elements" in each project are different.
 */
public class CredentialsForLogin {

     //In this project, users login via email, so I used "email" for spring security principal in this project .
    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "AccountCredentials [email=" + email + ", password=" + password + "]";
    }

}
