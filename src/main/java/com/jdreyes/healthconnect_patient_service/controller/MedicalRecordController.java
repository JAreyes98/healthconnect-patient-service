package com.jdreyes.healthconnect_patient_service.controller;

import com.jdreyes.healthconnect_patient_service.business.service.MedicalRecordService;
import com.jdreyes.healthconnect_patient_service.model.entity.MedicalRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/medical-records")
@RequiredArgsConstructor
@Slf4j
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    /**
     * Crea un nuevo registro médico con un archivo adjunto.
     * El archivo se cifra en Java y se envía al Storage Service en Go.
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MedicalRecord> uploadRecord(
            @RequestPart("patientId") String patientId, // Recibir como String y convertir a UUID
            @RequestPart("documentTypeId") String documentTypeId,
            @RequestPart("diagnosis") String diagnosis,
            @RequestPart("file") MultipartFile file) {

        try {
            MedicalRecord savedRecord = medicalRecordService.createRecord(
                    UUID.fromString(patientId), 
                    UUID.fromString(documentTypeId), 
                    diagnosis, 
                    file);
            return new ResponseEntity<>(savedRecord, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error uploading record: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Descarga y descifra el archivo adjunto de un registro médico.
     */
    @GetMapping("/{id}/attachment")
    public ResponseEntity<byte[]> getAttachment(@PathVariable UUID id) {
        try {
            System.out.println(id);
            // 1. Obtener la metadata del registro para saber el nombre original
            MedicalRecord record = medicalRecordService.findById(id).orElseThrow(() -> new RuntimeException("Medical Record not found")); 
            byte[] fileData = medicalRecordService.downloadAttachment(id);
            
            // 2. Extraer el nombre original (ej: "radiografia.pdf")
            String fileName = record.getOriginalFileName(); 
            MediaType contentType = MediaTypeFactory.getMediaType(fileName)
                .orElse(MediaType.APPLICATION_OCTET_STREAM);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .contentType(contentType)
                    .body(fileData);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
            return ResponseEntity.notFound().build();
        }
    }
}