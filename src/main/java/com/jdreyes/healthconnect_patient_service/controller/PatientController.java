package com.jdreyes.healthconnect_patient_service.controller;

import com.jdreyes.healthconnect_patient_service.business.dto.PatientDTO;
import com.jdreyes.healthconnect_patient_service.business.service.PatientService;
import com.jdreyes.healthconnect_patient_service.model.entity.Patient;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @PostMapping
    public ResponseEntity<Patient> createPatient(@Valid @RequestBody PatientDTO patientDTO) {
        Patient savedPatient = patientService.savePatient(patientDTO);
        return new ResponseEntity<>(savedPatient, HttpStatus.CREATED);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Patient>> searchPatients(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dob) {
        
        List<Patient> patients = patientService.findPatientsByCriteria(firstName, lastName, dob);
        return ResponseEntity.ok(patients);
    }

    @GetMapping
    public ResponseEntity<List<Patient>> getAll() {
        List<Patient> patients = patientService.getAll();
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/dni/{dni}")
    public ResponseEntity<Patient> getByDni(@PathVariable String dni) {
        return patientService.findByDni(dni)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping()
    public ResponseEntity<Patient> deleteByUUID(@RequestParam(name = "uuid") String uuid) {
        return patientService.deletePatient(uuid)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}