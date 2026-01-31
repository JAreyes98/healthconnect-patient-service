package com.jdreyes.healthconnect_patient_service.business.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jdreyes.healthconnect_patient_service.business.dto.PatientDTO;
import com.jdreyes.healthconnect_patient_service.model.entity.Patient;
import com.jdreyes.healthconnect_patient_service.repository.PatientRepository;
import com.jdreyes.healthconnect_patient_service.utils.BlindIndexUtils;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.Getter;

@Service
@Transactional
public class PatientService extends RabbitAuditService{

    @Getter
    private final String serviceName = "Medical Record Service";

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private BlindIndexUtils blindIndexUtils;

    public Patient savePatient(PatientDTO dto) {
        // if (dto.getDni() == null || dto.getDni().isEmpty()) {
        //     throw new ValidationException("DNI is required.");
        // }

        Patient patient = new Patient();
        patient.setFirstName(dto.getFirstName());
        patient.setLastName(dto.getLastName());
        patient.setDni(dto.getDni());        
        patient.setDob(dto.getDob()); 
        String generatedMrn = this.blindIndexUtils.generateMRN(
            dto.getFirstName(), 
            dto.getLastName(), 
            dto.getDob(),
            dto.getDni()
        );

        if (patientRepository.existsByMrn(generatedMrn)) {
            throw new RuntimeException("Patient with this data (MRN: " + generatedMrn + ") is already registered.");
        }
        patient.setMrn(generatedMrn);
        var saved = patientRepository.save(patient);
        logEvent("CREATE", "A NEW PATIENT WAS CREATED", "INFO");
        return saved;
    }

    public Optional<Patient> deletePatient(String uuid){
        Optional<Patient> patient = patientRepository.findById(UUID.fromString(uuid));
        if(!patient.isPresent()){
            throw new EntityNotFoundException("Patient id not found: "  + uuid );
        }
        patientRepository.delete(patient.get());
        logEvent("DELETE", "PATIENT WAS CREATED: " + patient.get().getPatientId().toString(), "INFO");
        return patient;
    }
    

    public List<Patient> getAll(){
        logEvent("SEARCH", "PATIENT SEARCH - ALL: " , "INFO"); 
        return patientRepository.findAll();
    }

    public List<Patient> findPatientsByCriteria(String firstName, String lastName, LocalDate dob) {
    
        String fNameIdx = blindIndexUtils.generate(firstName);
        String lNameIdx = blindIndexUtils.generate(lastName);
        String dobIdx   = blindIndexUtils.generate(dob);
        
        // return patientRepository.findByFirstNameIndexAndLastNameIndex(
        //     fNameIdx, lNameIdx);
        logEvent("SEARCH", String.format("PATIENT SEARCH BY FIRST NAME, LAST NAME AND DOB (%s, %s,%s)", fNameIdx, lNameIdx, dobIdx), "INFO"); 
        return patientRepository.findByFirstNameIndexAndLastNameIndexAndDobIndex(
            fNameIdx, lNameIdx, dobIdx);
    }

    public Optional<Patient> findByDni(String dni){
        logEvent("SEARCH", "PATIENT SEARCH BY DNI: " + dni, "INFO"); 
        return patientRepository.findByDni(dni);
    }
}