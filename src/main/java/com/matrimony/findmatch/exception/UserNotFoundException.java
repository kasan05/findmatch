package com.matrimony.findmatch.exception;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(){}
    public UserNotFoundException(String email){
        super("User Not Found For Email:"+email);
    }
    public UserNotFoundException(Long id){
        super("User Not Found For Id:"+id);
    }
}
