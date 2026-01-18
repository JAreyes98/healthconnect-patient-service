package com.jdreyes.healthconnect_patient_service.model.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jdreyes.healthconnect_patient_service.model.entity.Patient;
import com.jdreyes.healthconnect_patient_service.utils.BlindIndexUtils;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

@Component
public class PatientIndexListener {

    private static BlindIndexUtils blindIndexUtils;

    @Autowired
    public void init(BlindIndexUtils utils) {
        PatientIndexListener.blindIndexUtils = utils;
    }

    @PrePersist
    @PreUpdate
    public void beforeSave(Patient patient) {
        if (patient.getFirstName() != null) {
            patient.setFirstNameIndex(blindIndexUtils.generate(patient.getFirstName()));
        }
        if (patient.getLastName() != null) {
            patient.setLastNameIndex(blindIndexUtils.generate(patient.getLastName()));
        }
        if (patient.getDob() != null) {
            patient.setDobIndex(blindIndexUtils.generate(patient.getDob()));
        }
    }
}