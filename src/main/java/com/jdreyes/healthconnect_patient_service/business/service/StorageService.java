package com.jdreyes.healthconnect_patient_service.business.service;

import java.io.InputStream;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.jdreyes.healthconnect_patient_service.business.interfaces.StorageBaseService;
import com.jdreyes.healthconnect_patient_service.utils.EncryptionUtils;

import lombok.Getter;
import lombok.Setter;

@Service
public class StorageService extends RabbitAuditService implements StorageBaseService {

    @Getter
    private final String serviceName = "Storage Service";

    private final RestTemplate restTemplate;

    @Value("${app.storage.service.url}")
    private String storageUrl;

    @Value("${app.storage.service.api-key}")
    private String apiKey;

    @Value("${app.storage.service.api-secret}")
    private String apiSecret;

    @Value("${app.storage.service.bucket-name}")
    private String bucketName;

    @Value("${app.security.encryption-key}")
    private String javaSecretKey;

    public StorageService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public UUID store(InputStream fileStream, String originalFileName) throws Exception {
        // 1. Leer InputStream a bytes
        byte[] rawData = fileStream.readAllBytes();

        // 2. Cifrar usando tu Utility (Capa Java)
        byte[] encryptedData = EncryptionUtils.encryptFile(rawData, javaSecretKey);

        // 3. Preparar Headers y enviar a Go
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-Key", apiKey);
        headers.set("X-API-Secret", apiSecret);
        headers.set("X-Bucket-Name", bucketName);
        headers.set("X-Original-Filename", originalFileName);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        HttpEntity<byte[]> requestEntity = new HttpEntity<>(encryptedData, headers);

        try{
            ResponseEntity<StorageResponse> response = restTemplate.exchange(
                    storageUrl + "/upload",
                    HttpMethod.POST,
                    requestEntity,
                    StorageResponse.class
            );

            if(response.getStatusCode().is4xxClientError()){
                throw new RuntimeException("A request error. Error when medical record file is upload:" + response.getBody().toString());
            }

            if(response.getStatusCode().is5xxServerError()){
                throw new RuntimeException("Server Error. Error when medical record file is upload:" + response.getBody().toString());
            }
            
            System.out.println("Storage Go server response: " + response.getBody());
            if (response.getBody() != null) {
                System.out.println("Attachment id: " + response.getBody().getId());
            }
            
            logEvent("FILE_UPLOAD", "UPLOADING FILE USING STORAGE SERVICE" , "INFO"); 
            return response.getBody().getId();
        }catch(Exception e){
            logEvent("FILE_UPLOAD", e.getLocalizedMessage() , "ERROR"); 
            throw e;
        }
    }

    @Override
    public byte[] fetch(UUID fileId) throws Exception {
        // 1. Descargar de Go
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-Key", apiKey);
        headers.set("X-API-Secret", apiSecret);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<byte[]> response = restTemplate.exchange(
                storageUrl + "/download/" + fileId,
                HttpMethod.GET,
                entity,
                byte[].class
        );

        logEvent("FILE_DOWNLOAD", "DOWNLOADING FILE USING STORAGE SERVICE" , "INFO"); 
        // 2. Descifrar usando tu Utility (Quitar capa Java)
        return EncryptionUtils.decryptFile(response.getBody(), javaSecretKey);
    }

    // DTO interno para mapear la respuesta de Go
    @Getter
    @Setter
    private static class StorageResponse {
        private UUID id;
    }
    
}
