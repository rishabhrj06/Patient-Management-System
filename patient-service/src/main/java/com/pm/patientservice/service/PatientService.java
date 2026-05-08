package com.pm.patientservice.service;

import com.pm.patientservice.dto.PatientRequestDto;
import com.pm.patientservice.dto.PatientResponseDto;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

public interface PatientService {

    List<PatientResponseDto> getPatient();

    PatientResponseDto createPatient(PatientRequestDto patientRequestDto);

    PatientResponseDto updatePatient(UUID id, @Valid PatientRequestDto patientRequestDto);

    void deletePatient(UUID id);
}
