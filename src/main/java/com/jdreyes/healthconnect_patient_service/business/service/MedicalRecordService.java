package com.jdreyes.healthconnect_patient_service.business.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jdreyes.healthconnect_patient_service.model.entity.DocumentType;
import com.jdreyes.healthconnect_patient_service.model.entity.MedicalRecord;
import com.jdreyes.healthconnect_patient_service.model.entity.Patient;
import com.jdreyes.healthconnect_patient_service.repository.DocumentTypeRepository;
import com.jdreyes.healthconnect_patient_service.repository.MedicalRecordRepository;
import com.jdreyes.healthconnect_patient_service.repository.PatientRepository;

import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MedicalRecordService  extends RabbitAuditService{

    @Getter
    private final String serviceName = "Medical Record Service";

    private final MedicalRecordRepository medicalRecordRepository;
    private final PatientRepository patientRepository;
    private final DocumentTypeRepository documentTypeRepository;
    private final StorageService storageService;

    

    public Optional<MedicalRecord> findById(UUID medicalRecordID) {
        logEvent("SEARCH", "DOCUMENT TYPE FIND BY ID - " + medicalRecordID.toString(), "INFO");  
        return this.medicalRecordRepository.findById(medicalRecordID);
    }

    @Autowired
    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository,
            PatientRepository patientRepository, DocumentTypeRepository documentTypeRepository,
            StorageService storageService) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.patientRepository = patientRepository;
        this.documentTypeRepository = documentTypeRepository;
        this.storageService = storageService;
    }

    @Transactional
    public MedicalRecord createRecord(UUID patientId, UUID documentTypeId, String diagnosis, MultipartFile file) throws Exception {
        
        // 1. Validar que el paciente existe
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        // 2. Validar el tipo de documento
        DocumentType docType = documentTypeRepository.findById(documentTypeId)
                .orElseThrow(() -> new RuntimeException("Document type invalid"));

        // 3. Subir archivo al Storage Service (Go)
        // Este método ya aplica el cifrado de capa Java con EncryptionUtils
        UUID storageAttachmentId = storageService.store(file.getInputStream(), file.getOriginalFilename());
        
        log.info("File uploaded to Storage Service with ID: {}", storageAttachmentId);

        // 4. Crear y guardar la entidad MedicalRecord
        MedicalRecord record = new MedicalRecord();
        record.setPatient(patient);
        record.setDocumentType(docType);
        record.setDiagnosis(diagnosis); // Se cifrará automáticamente por el EncryptionConverter
        record.setAttachmentId(storageAttachmentId);
        record.setOriginalFileName(file.getOriginalFilename());

        var saved =  medicalRecordRepository.save(record);
        logEvent("CREATE", "A NEW MEDICAL RECORD WAS CREATED", "INFO");
        return saved;
    }

    public byte[] downloadAttachment(UUID recordId) throws Exception {
        MedicalRecord record = medicalRecordRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Record not found"));

        if (record.getAttachmentId() == null) {
            throw new RuntimeException("This record has no attachment");
        }

        logEvent("SEARCH", "MEDICAL RECORD WAS RETURNED", "INFO"); 
        // Recupera de Go y descifra la capa Java
        return storageService.fetch(record.getAttachmentId());
    }
}
