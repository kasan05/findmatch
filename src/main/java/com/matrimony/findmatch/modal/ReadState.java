package com.matrimony.findmatch.modal;

import java.util.Arrays;
import java.util.Optional;

public enum ReadState {
    READ,UNREAD;

    public static Optional<ReadState> getByValue(String value){
        return Arrays.stream(ReadState.values())
                .filter(x->x.name().equalsIgnoreCase(value))
                .findFirst();
    }
}
