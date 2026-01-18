package com.jdreyes.healthconnect_patient_service.model.entity;

import java.util.UUID;

import com.jdreyes.healthconnect_patient_service.utils.EncryptionConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "storage_provider")
public class StorageProvider {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID storageProviderId;

    @Column(nullable = false, length = 100)
    private String name;

    @Convert(converter = EncryptionConverter.class)
    @Column(nullable = false, length = 2500)
    private String path;

    @Convert(converter = EncryptionConverter.class)
    @Column(nullable = true)
    private String credentials;

    @Column(nullable = false)
    private Boolean active;

    @Column(nullable = false)
    private Boolean isDefault;

    @PrePersist
    public void prePersist() {
        if (active == null) {
            active = true;
        }
    }
}
