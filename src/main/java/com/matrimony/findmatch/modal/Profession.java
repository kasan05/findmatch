package com.matrimony.findmatch.modal;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public enum Profession {
    TEACHER,DOCTOR,ENGINEER,BUSINESSMAN,ACCOUNTANT,
    OTHER;

    public static Optional<Profession> getByValue(String professionVal){
        return Arrays.stream(Profession.values())
                .filter(profession-> profession.name().equalsIgnoreCase(professionVal))
                .findFirst();
    }

    public static List<String> getAll(){
        return Arrays.stream(Profession.values())
                .map(Profession::name)
                .map(profession->
                            Character.toUpperCase(profession.charAt(0))+profession.substring(1)
                )
                .toList();
    }
}
