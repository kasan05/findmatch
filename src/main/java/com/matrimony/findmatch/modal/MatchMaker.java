package com.matrimony.findmatch.modal;

import jakarta.persistence.*;

@Table(name = "match_maker")
@Entity
@PrimaryKeyJoinColumn(name="user_id")
public class MatchMaker extends User {

    private Country country;

    private Profession profession;

    private String passportPhoto;

    private Caste caste;

    private Religion religion;

    private boolean  lockedPhoto;

    private Gender gender;

    private MaritalStatus maritalStatus;

    public Caste getCaste() {
        return caste;
    }

    public void setCaste(Caste caste) {
        this.caste = caste;
    }

    public Religion getReligion() {
        return religion;
    }

    public void setReligion(Religion religion) {
        this.religion = religion;
    }

    public Profession getProfession() {
        return profession;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }
    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public boolean isLockedPhoto() {
        return lockedPhoto;
    }

    public void setLockedPhoto(boolean lockedPhoto) {
        this.lockedPhoto = lockedPhoto;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }
    public String getPassportPhoto() {
        return passportPhoto;
    }

    public void setPassportPhoto(String passportPhoto) {
        this.passportPhoto = passportPhoto;
    }
}
