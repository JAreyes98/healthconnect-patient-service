package com.jdreyes.healthconnect_patient_service.business.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StorageReplicationDto {
    

    @NotBlank(message = "Storage Provider Origin is required")
    private String replicationUuid;

    @NotBlank(message = "Storage Provider Origin is required")
    private String storageProviderOrigin;

    @NotBlank(message = "Storage Provider Destinatio is required")
    private String storageProviderDestination;
}
