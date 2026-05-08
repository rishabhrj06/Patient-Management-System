package com.pm.patientservice.mapper;

import com.pm.patientservice.dto.PatientRequestDto;
import com.pm.patientservice.dto.PatientResponseDto;
import com.pm.patientservice.model.Patient;
import java.time.LocalDate;

public class PatientMapper {

    public static PatientResponseDto toDto(Patient patient){
        return PatientResponseDto
                .builder()
                .id(patient.getId().toString())
                .name(patient.getName())
                .email(patient.getEmail())
                .address(patient.getAddress())
                .dateOfBirth(patient.getDateOfBirth().toString())
                .build();
    }

    public static Patient toPatient(PatientRequestDto patientRequestDto){
        return Patient
                .builder()
                .name(patientRequestDto.getName())
                .email(patientRequestDto.getEmail())
                .address(patientRequestDto.getAddress())
                .dateOfBirth(LocalDate.parse(patientRequestDto.getDateOfBirth()))
                .registeredDate(LocalDate.now())
                .build();
    }
}
