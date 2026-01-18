package com.jdreyes.healthconnect_patient_service.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jdreyes.healthconnect_patient_service.model.entity.DocumentType;
@Repository
public interface DocumentTypeRepository extends JpaRepository<DocumentType, UUID> {
    List<DocumentType> findByActiveTrue();
}
