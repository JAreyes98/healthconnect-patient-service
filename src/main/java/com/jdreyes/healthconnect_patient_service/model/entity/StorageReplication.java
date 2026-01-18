package com.jdreyes.healthconnect_patient_service.model.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "storage_replication")
public class StorageReplication {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID storageReplicationId;
    
    @ManyToOne
    @JoinColumn(name = "origin_storage_provider_id")
    private StorageProvider storageProviderOrigin;
    
    @ManyToOne
    @JoinColumn(name = "destination_storage_provider_id")
    private StorageProvider storageProviderDestination;
    
    @Column(nullable = false)
    private Boolean active;

    @PrePersist
    public void prePersist() {
        if (active == null) {
            active = true;
        }
    }
}
