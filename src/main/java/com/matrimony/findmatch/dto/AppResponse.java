package com.matrimony.findmatch.dto;

public class AppResponse<T> {
    T data;
    public AppResponse(){}
    public AppResponse(T data, String message) {
        this.data = data;
        this.message = message;
    }

    String message;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }



}
