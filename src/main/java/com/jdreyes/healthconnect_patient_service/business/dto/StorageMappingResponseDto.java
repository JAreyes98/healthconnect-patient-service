package com.jdreyes.healthconnect_patient_service.business.dto;

import java.util.List;
import java.util.UUID;

import com.jdreyes.healthconnect_patient_service.model.entity.StorageProvider;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StorageMappingResponseDto {
    private StorageProvider origin;
    private List<StorageProvider> destinations;

    // Constructores, Getters y Setters
    public StorageMappingResponseDto(StorageProvider origin, List<StorageProvider> destinations) {
        this.origin = origin;
        this.destinations = destinations;
    }
}