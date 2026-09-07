package com.matrimony.findmatch.dto;


import org.springframework.web.multipart.MultipartFile;

public record BrokerDto(String name, String email, String password,
                        MultipartFile cameraPhoto, MultipartFile passport) {}