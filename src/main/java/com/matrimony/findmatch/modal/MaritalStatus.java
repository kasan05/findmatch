package com.matrimony.findmatch.modal;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public enum MaritalStatus {
SINGLE,DIVORCED,WIDOWED,SEPARATED;

    public static Optional<MaritalStatus> getByValue(String maritalStatusVal){
        return Arrays.stream(MaritalStatus.values())
                .filter(maritalstatus-> maritalstatus.name().equalsIgnoreCase(maritalStatusVal))
                .findFirst();
    }

    public static List<String > getAll(){
        return Arrays.stream(MaritalStatus.values())
                .map(MaritalStatus::name)
                .map(maritalStatus->
                        Character.toUpperCase(maritalStatus.charAt(0))+maritalStatus.substring(1)
                )
                .toList();
    }
}