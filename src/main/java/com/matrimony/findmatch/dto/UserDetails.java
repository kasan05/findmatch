package com.matrimony.findmatch.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.matrimony.findmatch.modal.UserType;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record UserDetails(

        @NotNull
        String name,
        @NotNull
        String email,
        String password,

        @NotNull
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        String datOfBirth,
                          MultipartFile cameraPhoto,MultipartFile passport,
                          UserType userType) {
}
