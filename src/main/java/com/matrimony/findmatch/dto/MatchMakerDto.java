package com.matrimony.findmatch.dto;

import com.matrimony.findmatch.modal.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public class MatchMakerDto {

    private Long id;

    @NotNull
    private String profession;

    @NotNull
    private String religion;

    @NotNull
    private String maritalStatus;

    @NotNull
    private String caste;

    @NotNull
    private String country;

    @NotNull
    private String gender;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    private List<MultipartFile> multipartFiles;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public MatchMakerDto(){}
    public MatchMakerDto(Caste caste, Religion religion, Country country, Profession profession, Gender gender,
                         MaritalStatus maritalStatus
                         ) {
        this.caste = caste==null?null:caste.name();
        this.religion = religion==null?null:religion.name();
        this.country = country==null?null: country.name();
        this.profession = profession==null?null:profession.name();
        this.gender = gender==null? null:gender.name();
        this.maritalStatus=maritalStatus==null?null:maritalStatus.name();
    }
    public MatchMakerDto(Caste caste, Religion religion, Country country, Profession profession, Gender gender,
                         MaritalStatus maritalStatus,String name
    ) {
        this.caste = caste==null?null:caste.name();
        this.religion = religion==null?null:religion.name();
        this.country = country==null?null: country.name();
        this.profession = profession==null?null:profession.name();
        this.gender = gender==null? null:gender.name();
        this.maritalStatus=maritalStatus==null?null:maritalStatus.name();
        this.name = name;
    }
    public MatchMakerDto(String caste, String religion, String country, String profession) {
        this.caste = caste;
        this.religion = religion;
        this.country = country;
        this.profession = profession;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getCaste() {
        return caste;
    }

    public void setCaste(String caste) {
        this.caste = caste;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }
    public List<MultipartFile> getMultipartFiles() {
        return multipartFiles;
    }
    public void setMultipartFiles(List<MultipartFile> multipartFiles) {
        this.multipartFiles = multipartFiles;
    }


}
