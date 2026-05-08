package com.pm.patientservice.controller;

import com.pm.patientservice.dto.PatientRequestDto;
import com.pm.patientservice.dto.PatientResponseDto;
import com.pm.patientservice.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
@Tag(name = "Patient", description = "Endpoints for managing patients")
public class PatientController {

    private final PatientService patientService;

    @GetMapping
    @Operation(summary = "Get All Patients", description = "Retrieve a list of all patients")
    public ResponseEntity<List<PatientResponseDto>> getPatient() {
        List<PatientResponseDto> patientResponseDtos = patientService.getPatient();
        if (patientResponseDtos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok().body(patientResponseDtos);
    }

    @PostMapping
    @Operation(summary = "Create Patient", description = "Create a new patient with the provided details")
    public ResponseEntity<PatientResponseDto> createPatient(@Valid @RequestBody PatientRequestDto patientRequestDto) {

        return new ResponseEntity<>(patientService.createPatient(patientRequestDto), HttpStatus.CREATED);

    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Patient", description = "Update the details of an existing patient by ID")
    public ResponseEntity<PatientResponseDto> updatePatient(@PathVariable UUID id, @Valid @RequestBody PatientRequestDto patientRequestDto){
        return ResponseEntity.ok().body(patientService.updatePatient(id, patientRequestDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Patient", description = "Delete an existing patient by ID")
    public ResponseEntity<String> deletePatient(@PathVariable UUID id) {
        patientService.deletePatient(id);
        return ResponseEntity.ok("Patient with id " + id + " deleted successfully");
    }
}
