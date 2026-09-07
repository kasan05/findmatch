package com.matrimony.findmatch.modal;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public enum Caste {
    KOVIARS,NALAVAR,PALLAR,KARAIYAR,BRAHMIN,VELLALAR,
    CHETTIES,PARAIYAR,CHANDARS,
    AMBATTAR,THACHCHAR,COLLAR,OTHER;

    public static Optional<Caste> getByValue(String casteVal){
        return Arrays.stream(Caste.values())
                .filter(caste-> caste.name().equalsIgnoreCase(casteVal))
                .findFirst();
    }
    public static List<String> getAll(){
        return Arrays.stream(Caste.values())
                .map(Caste::name)
                .map(caste->
                        Character.toUpperCase(caste.charAt(0))+caste.substring(1)
                )
                .toList();

    }

}
