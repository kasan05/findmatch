package com.matrimony.findmatch.exception;

public class FileUploadException extends RuntimeException{
    public FileUploadException(){}
    public FileUploadException(String message){
        super("File Upload Failure");
    }
}
