package com.jdreyes.healthconnect_patient_service.business.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientDTO {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Lastname is required")
    private String lastName;

    @NotBlank(message = "DNI is required")
    @Pattern(regexp = "^[0-9]{3}-?[0-9]{6}-?[0-9]{4}[A-Z]$|\\d+", 
             message = "DNI format is not valid")
    private String dni;

    // @NotBlank(message = "MRN required")
    private String mrn;

    @NotNull(message = "Date of birth required")
    private LocalDate dob; 
}