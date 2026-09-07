package com.matrimony.findmatch.modal;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public enum Religion {
    CHRISTIAN,HINDU,MUSLIM,BUDDHIST,OTHER;

    public static Optional<Religion> getByValue(String religionVal){
        return Arrays.stream(Religion.values())
                .filter(religion-> religion.name().equalsIgnoreCase(religionVal))
                .findFirst();
    }

    public static List<String> getAll(){
        return Arrays.stream(Religion.values())
                .map(Religion::name)
                .map(religion->
                        Character.toUpperCase(religion.charAt(0))+religion.substring(1)
                )
                .toList();
    }
}