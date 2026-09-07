package com.matrimony.findmatch.dto;

import java.util.List;

public record SearchResultsDTO(
    List<UserSearchDTO> searchDTOS,
    long total,
    int offset,
    int limit){}
