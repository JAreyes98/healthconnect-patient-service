package com.jdreyes.healthconnect_patient_service.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jdreyes.healthconnect_patient_service.business.service.DocumentTypeService;
import com.jdreyes.healthconnect_patient_service.model.entity.DocumentType;

@RestController
@RequestMapping("/api/v1/doctype")
public class DocTypeController {
    
    private final DocumentTypeService service;

    @Autowired
    public DocTypeController(final DocumentTypeService service){
        this.service = service;
    }

    
    @PostMapping
    public ResponseEntity<DocumentType> create(@RequestBody DocumentType documentType) {
        return new ResponseEntity<>(service.create(documentType), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<DocumentType>> getAll() {
        return ResponseEntity.ok(service.getAllActive());
    }
}
