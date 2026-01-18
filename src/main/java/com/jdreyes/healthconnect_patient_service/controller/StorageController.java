package com.jdreyes.healthconnect_patient_service.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jdreyes.healthconnect_patient_service.business.dto.StorageProviderDto;
import com.jdreyes.healthconnect_patient_service.business.service.StorageProviderService;
import com.jdreyes.healthconnect_patient_service.model.entity.StorageProvider;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/storageprovider")
public class StorageController {
    @Autowired
    private final StorageProviderService storageProviderService;

    public StorageController(StorageProviderService storageProviderService) {
        this.storageProviderService = storageProviderService;
    }

    @PostMapping
    public ResponseEntity<StorageProvider> createStorage(@Valid @RequestBody StorageProviderDto dto) {
        Optional<StorageProvider> savedPatient = storageProviderService.save(dto);
        return new ResponseEntity<>(savedPatient.get(), HttpStatus.CREATED);
    }
    
    @PutMapping
    public ResponseEntity<StorageProvider> updateStorage(@Valid @RequestBody StorageProviderDto dto) {
        Optional<StorageProvider> updateStorage = storageProviderService.save(dto);
        return new ResponseEntity<>(updateStorage.get(), HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<StorageProvider>> getAll() {
        List<StorageProvider> patients = storageProviderService.findAll();
        return ResponseEntity.ok(patients);
    }
}
