package com.pm.patientservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatientRequestDto {

    @NotBlank(message = "Name is Required")
    @Size(max=50, message = "Name should not exceed 50 characters")
    private String name;

    @NotBlank(message = "Email is Required")
    @Email(message = "Email should be Valid")
    private String email;

    @NotBlank(message = "Address is Required")
    private String address;

    @NotBlank(message = "Date of Birth is Required")
    private String dateOfBirth;


}
