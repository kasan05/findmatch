package com.matrimony.findmatch.dto;

import java.util.List;

public record FilterInput(
        List<String> selectedCountriesTemp,
        List<String> selectedCastesTemp,
        List<String> selectedProfessionsTemp,
        List<String> selectedReligionsTemp,
        List<String> ageArr
) {
}
