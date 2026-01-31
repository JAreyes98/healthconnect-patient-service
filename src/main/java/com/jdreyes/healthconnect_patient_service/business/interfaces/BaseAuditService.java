package com.jdreyes.healthconnect_patient_service.business.interfaces;

public interface BaseAuditService {
    void logEvent(String action, String details, String severity);
    String getServiceName();
}
