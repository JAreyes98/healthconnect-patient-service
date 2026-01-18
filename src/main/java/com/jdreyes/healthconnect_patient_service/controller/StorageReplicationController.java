package com.jdreyes.healthconnect_patient_service.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jdreyes.healthconnect_patient_service.business.dto.StorageMappingResponseDto;
import com.jdreyes.healthconnect_patient_service.business.dto.StorageReplicationDto;
import com.jdreyes.healthconnect_patient_service.business.service.StorageReplicationService;
import com.jdreyes.healthconnect_patient_service.model.entity.StorageReplication;

@RestController
@RequestMapping("/api/v1/replication")
public class StorageReplicationController {
    private final StorageReplicationService storageReplicationService;

    public StorageReplicationController(StorageReplicationService storageReplicationService) {
        this.storageReplicationService = storageReplicationService;
    }

    @PostMapping()
    public ResponseEntity<StorageReplication> saveReplication(@RequestBody StorageReplicationDto dto) {
        StorageReplication response = storageReplicationService.createReplication(dto);
        return ResponseEntity.ok(response);
    }

    @PutMapping()
    public ResponseEntity<StorageReplication> updateReplication(@RequestParam String uuid, @RequestBody StorageReplicationDto dto) {
        StorageReplication response = storageReplicationService.updateReplication(uuid, dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping()
    public ResponseEntity<List<StorageMappingResponseDto>> getAllOrigins() {
        
        List<StorageMappingResponseDto> response = storageReplicationService.getAllOriginsWithDestinations();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<StorageMappingResponseDto> findByOrigin(@RequestParam String uuid) {
        StorageMappingResponseDto response = storageReplicationService.getDestinationsByOriginId(UUID.fromString(uuid));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping()
    public ResponseEntity<StorageReplication> deleteByReplicationID(@RequestParam String uuid) {
        StorageReplication response = storageReplicationService.inactivateReplication(uuid);
        return ResponseEntity.ok(response);
    }
}
