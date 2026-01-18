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

@Service
@Transactional
public class PatientService {

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
        return patientRepository.save(patient);
    }

    public Optional<Patient> deletePatient(String uuid){
        Optional<Patient> patient = patientRepository.findById(UUID.fromString(uuid));
        if(!patient.isPresent()){
            throw new EntityNotFoundException("Patient id not found: "  + uuid );
        }
        patientRepository.delete(patient.get());
        return patient;
    }
    

    public List<Patient> getAll(){
        return patientRepository.findAll();
    }

    public List<Patient> findPatientsByCriteria(String firstName, String lastName, LocalDate dob) {
    
        String fNameIdx = blindIndexUtils.generate(firstName);
        String lNameIdx = blindIndexUtils.generate(lastName);
        String dobIdx   = blindIndexUtils.generate(dob);
        
        // return patientRepository.findByFirstNameIndexAndLastNameIndex(
        //     fNameIdx, lNameIdx);
        return patientRepository.findByFirstNameIndexAndLastNameIndexAndDobIndex(
            fNameIdx, lNameIdx, dobIdx);
    }

    public Optional<Patient> findByDni(String dni){
        return patientRepository.findByDni(dni);
    }
}