package com.jdreyes.healthconnect_patient_service.business.service;

import com.jdreyes.healthconnect_patient_service.model.entity.DocumentType;
import com.jdreyes.healthconnect_patient_service.repository.DocumentTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentTypeService {

    private final DocumentTypeRepository repository;

    public DocumentType create(DocumentType documentType) {
        return repository.save(documentType);
    }

    public List<DocumentType> getAllActive() {
        return repository.findByActiveTrue();
    }

    public DocumentType getById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Document Type not found"));
    }
}