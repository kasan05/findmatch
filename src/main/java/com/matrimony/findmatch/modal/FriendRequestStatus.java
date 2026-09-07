package com.matrimony.findmatch.modal;

import java.util.Arrays;
import java.util.Optional;

public enum FriendRequestStatus {
    PENDING,ACCEPTED,REJECTED;

    public static Optional<FriendRequestStatus> findByValue(String status){
        return Arrays.stream(FriendRequestStatus.values())
                .filter(f->f.name().equalsIgnoreCase(status))
                .findFirst();
    }
}
