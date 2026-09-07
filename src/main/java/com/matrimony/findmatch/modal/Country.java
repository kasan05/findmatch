package com.matrimony.findmatch.modal;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public enum Country {
    SRILANKA,
    GERMANY,
    UK,
    FRANCE,
    SWEDEN,
    DENMARK,
    SWITZERLAND,
    CANADA,
    AUSTRALIA,
    INDIA,
    SINGAPORE,
    AMERICA,
    EUROPE,
    OTHER;

    public static Optional<Country> getByValue(String countryVal){
        return Arrays.stream(Country.values())
                .filter(country-> country.name().equalsIgnoreCase(countryVal))
                .findFirst();
    }

    public static List<String> getAll(){
        return Arrays.stream(Country.values())
                .map(Country::name)
                .map(country->
                        Character.toUpperCase(country.charAt(0))+country.substring(1)
                )
                .toList();
    }

}
