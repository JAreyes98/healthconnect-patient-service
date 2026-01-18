package com.jdreyes.healthconnect_patient_service.business.service;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jdreyes.healthconnect_patient_service.business.dto.StorageMappingResponseDto;
import com.jdreyes.healthconnect_patient_service.business.dto.StorageReplicationDto;
import com.jdreyes.healthconnect_patient_service.model.entity.StorageProvider;
import com.jdreyes.healthconnect_patient_service.model.entity.StorageReplication;
import com.jdreyes.healthconnect_patient_service.repository.StorageReplicationRepository;
import com.jdreyes.healthconnect_patient_service.repository.StorageRepository;

@Service
public class StorageReplicationService {

    private final StorageReplicationRepository replicationRepository;
    private final StorageRepository storageRepository;

    public StorageReplicationService(StorageReplicationRepository replicationRepository,
            StorageRepository storageRepository) {
        this.replicationRepository = replicationRepository;
        this.storageRepository = storageRepository;
    }

    public StorageReplication createReplication(StorageReplicationDto dto) {
        var storageOrigin = storageRepository.findById(UUID.fromString(dto.getStorageProviderOrigin()))
                .orElseThrow(() -> new RuntimeException("Storage Origin not found"));
        
        var storageDestin = storageRepository.findById(UUID.fromString(dto.getStorageProviderDestination()))
                .orElseThrow(() -> new RuntimeException("Storage Destination not found"));

        StorageReplication replication = new StorageReplication();
        replication.setStorageProviderOrigin(storageOrigin);
        replication.setStorageProviderDestination(storageDestin);
        
        return replicationRepository.save(replication);
    }

    public StorageReplication updateReplication(String uuid, StorageReplicationDto dto) {
        var storageOrigin = storageRepository.findById(UUID.fromString(dto.getStorageProviderOrigin()))
                .orElseThrow(() -> new RuntimeException("Storage Origin not found"));
        
        var storageDestin = storageRepository.findById(UUID.fromString(dto.getStorageProviderDestination()))
                .orElseThrow(() -> new RuntimeException("Storage Destination not found"));

        var replication = replicationRepository.findById(UUID.fromString(uuid)).orElseThrow(() -> new RuntimeException("Replication UUID not found"));
        replication.setStorageProviderOrigin(storageOrigin);
        replication.setStorageProviderDestination(storageDestin);
        
        return replicationRepository.save(replication);
    }

    public StorageReplication inactivateReplication(String uuid) {
        var replication = replicationRepository.findById(UUID.fromString(uuid))
                .orElseThrow(() -> new RuntimeException("Replication UUID not found"));
        replication.setActive(false);
        replicationRepository.save(replication);
        return replication;
    }

    public StorageMappingResponseDto getDestinationsByOriginId(UUID originId) {
        var origin = storageRepository.findById(originId)
                .orElseThrow(() -> new RuntimeException("Origin not found"));

        List<StorageReplication> replications = replicationRepository.findByStorageProviderOrigin_StorageProviderId(originId);

        List<StorageProvider> destinations = replications.stream()
                .map(StorageReplication::getStorageProviderDestination)
                .toList();

        return new StorageMappingResponseDto(origin, destinations);
    }

    public List<StorageMappingResponseDto> getAllOriginsWithDestinations() {
        List<StorageReplication> allReplications = replicationRepository.findByActive(true);

        return allReplications.stream()
            .collect(Collectors.groupingBy(StorageReplication::getStorageProviderOrigin))
            .entrySet().stream()
            .map(entry -> {
                List<StorageProvider> destinations = entry.getValue().stream()
                    .map(StorageReplication::getStorageProviderDestination)
                    .toList();
                
                return new StorageMappingResponseDto(entry.getKey(), destinations);
            })
            .toList();
    }
}
