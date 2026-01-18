package com.jdreyes.healthconnect_patient_service.business.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.UUID;

@Data
public class StorageResponse {
    @JsonProperty("id")
    private UUID id;
}