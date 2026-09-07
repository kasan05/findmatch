package com.matrimony.findmatch.modal;

import java.util.Arrays;

public enum UserType {
    BROKER,MATCH_MAKER;

    public static boolean isValid(String value){
        if(value==null || value.isBlank()) return false;
        return Arrays.stream(UserType.values())
                .anyMatch(userType->userType.name().equalsIgnoreCase(value));
    }
}
