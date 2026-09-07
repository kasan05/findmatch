package com.matrimony.findmatch.exception;

public class UserNotVerifiedException extends RuntimeException{

    public UserNotVerifiedException(){
        super("User has not been verified");
    }
}
