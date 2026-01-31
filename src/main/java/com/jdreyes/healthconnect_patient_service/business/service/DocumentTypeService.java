package com.jdreyes.healthconnect_patient_service.business.service;

import com.jdreyes.healthconnect_patient_service.model.entity.DocumentType;
import com.jdreyes.healthconnect_patient_service.repository.DocumentTypeRepository;

import lombok.Getter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DocumentTypeService extends RabbitAuditService{

    @Getter
    private final String serviceName = "Document Type Service";

    private final DocumentTypeRepository repository;

    @Autowired
    public DocumentTypeService(DocumentTypeRepository repository) {
        this.repository = repository;
    }

    public DocumentType create(DocumentType documentType) { 
        var created = repository.save(documentType);
        logEvent("CREATE", "A NEW DOCUMENT TYPE CREATING WAS CREATED", "INFO");
        return created;
    }

    public List<DocumentType> getAllActive() {
        logEvent("SEARCH", "DOCUMENT TYPE - ACTIVES LIST WAS RETURNED", "INFO");        
        return repository.findByActiveTrue();
    }

    public DocumentType getById(UUID id) {
        logEvent("SEARCH", "DOCUMENT TYPE FIND BY ID - " + id.toString(), "INFO");  
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Document Type not found"));
    }
}