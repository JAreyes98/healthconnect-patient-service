package com.jdreyes.healthconnect_patient_service.business.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StorageProviderDto {
    
    @NotBlank(message = "Provider's Name is required")
    private String name;
    
    @NotBlank(message = "Provider's storage path is required")
    private String path;
    
    @NotBlank(message = "Provider's credentials are required")
    private String credentials;
    

    private Boolean active;

    private Boolean isDefault;

}
