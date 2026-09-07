package com.matrimony.findmatch.util;

import com.matrimony.findmatch.dto.AppErrorResponse;
import com.matrimony.findmatch.exception.FileUploadException;
import com.matrimony.findmatch.exception.UserNotFoundException;
import com.matrimony.findmatch.exception.UserNotLoginException;
import com.matrimony.findmatch.exception.UserNotVerifiedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class AppControllerAdvice {

    @ExceptionHandler(exception = UserNotFoundException.class)
    public ResponseEntity<AppErrorResponse> handleUserNotFoundException(UserNotFoundException userNotFoundException){
        return new ResponseEntity<>(new AppErrorResponse(userNotFoundException.getMessage(),
                HttpStatus.NOT_FOUND.value()),HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(exception = UserNotVerifiedException.class)
    public ResponseEntity<AppErrorResponse> handleUserNotFoundException(UserNotVerifiedException userNotVerifiedException){
        return new ResponseEntity<>(new AppErrorResponse(userNotVerifiedException.getMessage(),
                HttpStatus.UNAUTHORIZED.value()),HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(exception = FileUploadException.class)
    public ResponseEntity<AppErrorResponse> handleUserNotFoundException(FileUploadException fileUploadException){
        return new ResponseEntity<>(new AppErrorResponse(fileUploadException.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value()),HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(exception = UserNotLoginException.class)
    public ResponseEntity<AppErrorResponse> handleUserNotFoundException(UserNotLoginException userNotLoginException){
        return new ResponseEntity<>(new AppErrorResponse(userNotLoginException.getMessage(),
                HttpStatus.UNAUTHORIZED.value()),HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(exception = MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException methodArgumentNotValidException){

        Map<String, String> errors = new HashMap<>();
        methodArgumentNotValidException.getBindingResult().getAllErrors()
                .forEach(error->{
                  String fieldName =   ((FieldError)error).getField();
                    String errorMessage= error.getDefaultMessage();
                    errors.put(fieldName,errorMessage);
                });

        return new ResponseEntity<Map<String, String>>(errors,HttpStatus.BAD_REQUEST);
    }

}
