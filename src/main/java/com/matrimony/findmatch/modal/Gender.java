package com.matrimony.findmatch.modal;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public enum Gender {
    MALE,FEMALE;
    public static Optional<Gender> getByValue(String genderVal){
        return Arrays.stream(Gender.values())
                .filter(gender-> gender.name().equalsIgnoreCase(genderVal))
                .findFirst();
    }

    public static List<String> getAll(){
        return Arrays.stream(Gender.values())
                .map(Gender::name)
                .map(gender->
                        Character.toUpperCase(gender.charAt(0))+gender.substring(1)
                )
                .toList();
    }

}
