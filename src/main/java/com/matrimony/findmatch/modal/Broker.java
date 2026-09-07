package com.matrimony.findmatch.modal;

import jakarta.persistence.*;

@Table(name = "broker")
@Entity
@PrimaryKeyJoinColumn(name = "user_id")
public class Broker extends User {

    private boolean lockedPhoto;

    private String passportPhoto;

    public boolean isLockedPhoto() {
        return lockedPhoto;
    }

    public void setLockedPhoto(boolean lockedPhoto) {
        this.lockedPhoto = lockedPhoto;
    }

    public String getPassportPhoto() {
        return passportPhoto;
    }

    public void setPassportPhoto(String passportPhoto) {
        this.passportPhoto = passportPhoto;
    }


}
