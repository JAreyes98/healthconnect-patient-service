package com.jdreyes.healthconnect_patient_service.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jdreyes.healthconnect_patient_service.model.entity.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {
    
    List<Patient> findByFirstNameIndexAndLastNameIndex(String firstNameIndex, String lastNameIndex);

    List<Patient> findByFirstNameIndexAndLastNameIndexAndDobIndex(String firstNameIndex, String lastNameIndex, String dobIndex);
    
    List<Patient> findByDobIndex(String dobIndex);
    
    Optional<Patient> findByDni(String dni);
    
    Optional<Patient> findByMrn(String mrn);
    
    Boolean existsByMrn(String mrn);
}