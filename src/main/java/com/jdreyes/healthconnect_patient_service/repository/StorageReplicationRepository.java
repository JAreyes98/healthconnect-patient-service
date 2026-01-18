package com.jdreyes.healthconnect_patient_service.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jdreyes.healthconnect_patient_service.model.entity.StorageReplication;

@Repository
public interface StorageReplicationRepository extends JpaRepository<StorageReplication, UUID>{
    List<StorageReplication> findByStorageProviderOrigin_StorageProviderId(UUID storageProviderId);
    List<StorageReplication> findByActive(Boolean active);
}
