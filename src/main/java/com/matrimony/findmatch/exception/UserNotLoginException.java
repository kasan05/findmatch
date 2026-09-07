package com.matrimony.findmatch.exception;

public class UserNotLoginException extends RuntimeException{

    public UserNotLoginException(){}
    public UserNotLoginException(String message){
        super("User Not Login Exception");
    }
}
