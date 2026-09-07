package com.matrimony.findmatch.dto;

import com.matrimony.findmatch.modal.*;

import java.util.List;

public record PersonalData(List<String> professions,
                           List<String> religions,
                           List<String> castes,
                           List<String> countries,
                           List<String> genders,
                           List<String> maritalStatuses) {
}
