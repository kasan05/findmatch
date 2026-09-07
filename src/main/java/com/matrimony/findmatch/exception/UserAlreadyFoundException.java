package com.matrimony.findmatch.exception;

public class UserAlreadyFoundException extends RuntimeException {

    public UserAlreadyFoundException() {
    }

    public UserAlreadyFoundException(String email) {
        super("User Already Found for email:"+email);
    }
}
