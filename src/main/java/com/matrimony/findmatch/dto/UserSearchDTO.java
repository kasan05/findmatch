package com.matrimony.findmatch.dto;

import com.matrimony.findmatch.modal.*;

import java.time.LocalDate;
import java.time.Period;

public class UserSearchDTO {

    private Long id;
    private String name;
    private String profession;
    private int age;
    private String country;
    private String caste;
    private String gender;
    private String friendRequestStatus;

    public UserSearchDTO(Long id,String friendRequestStatus) {
        this.friendRequestStatus = friendRequestStatus;
        this.id = id;
    }
    public UserSearchDTO(Long id, String name,String friendRequestStatus) {
        this.id = id;
        this.name = name;
        this.friendRequestStatus = friendRequestStatus;
    }
    public String getFriendRequestStatus() {
        return friendRequestStatus;
    }

    public void setFriendRequestStatus(String friendRequestStatus) {
        this.friendRequestStatus = friendRequestStatus;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    private String maritalStatus;
    public UserSearchDTO(){}
    public UserSearchDTO(Long id, String name, Profession profession, LocalDate dateOfBirth, Country country, Caste caste, Gender gender, MaritalStatus maritalStatus) {
        this.id = id;
        this.name = name;
        this.profession = profession.name();
        this.age = calculateAge(dateOfBirth);
        this.country = country.name();
        this.caste = caste.name();
        this.gender = gender.name();
        this.maritalStatus = maritalStatus.name();
    }
    public UserSearchDTO(Long id, Profession profession, LocalDate dateOfBirth, Country country, Caste caste, Gender gender, MaritalStatus maritalStatus) {
        this.id = id;
        this.profession = profession.name();
        this.age = calculateAge(dateOfBirth);
        this.country = country.name();
        this.caste = caste.name();
        this.gender = gender.name();
        this.maritalStatus = maritalStatus.name();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCaste() {
        return caste;
    }

    public void setCaste(String caste) {
        this.caste = caste;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
    private static int calculateAge(LocalDate birthDate) {
        LocalDate currentDate = LocalDate.now();
        if (birthDate != null) {
            return Period.between(birthDate, currentDate).getYears();
        }
        return 0;
    }

}
