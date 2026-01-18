package com.jdreyes.healthconnect_patient_service.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jdreyes.healthconnect_patient_service.model.entity.StorageProvider;

@Repository
public interface StorageRepository  extends JpaRepository<StorageProvider, UUID>{
    List<StorageProvider> findByActive(Boolean isActive);
    List<StorageProvider> findByNameAndActive(String name, Boolean isActive);
    Boolean existsByNameAndActive(String name, Boolean active);
    Boolean existsByIsDefaultAndActive(Boolean isDefault, Boolean isActive);
    List<StorageProvider> findByIsDefaultAndActive(Boolean isDefault, Boolean isActive);
}
