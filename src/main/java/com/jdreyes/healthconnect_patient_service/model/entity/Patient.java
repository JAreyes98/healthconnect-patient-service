package com.jdreyes.healthconnect_patient_service.model.entity;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.jdreyes.healthconnect_patient_service.model.listener.PatientIndexListener;
import com.jdreyes.healthconnect_patient_service.utils.EncryptionConverter;
import com.jdreyes.healthconnect_patient_service.utils.EncryptionDateConverter;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "patients")
@EntityListeners(PatientIndexListener.class)
@Getter
@Setter
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID patientId;

    @Column(nullable = false)
    @Convert(converter = EncryptionConverter.class)
    private String firstName;

    @Column(nullable = false)
    private String firstNameIndex;

    @Column(nullable = false)
    @Convert(converter = EncryptionConverter.class)
    private String lastName;

    @Column(nullable = false)
    private String lastNameIndex;

    @Column(nullable = false)
    @Convert(converter = EncryptionDateConverter.class)
    private LocalDate dob;

    @Column(nullable = false)
    private String dobIndex;

    @Column(nullable = false, unique = true)
    private String mrn;

    @Convert(converter = EncryptionConverter.class)
    @Column(name = "dni", nullable = true)
    private String dni;

    @Column(nullable = false)
    private Boolean active;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<MedicalRecord> records;

    @PrePersist
    public void prePersist() {
        if (active == null) {
            active = true;
        }
    }

}