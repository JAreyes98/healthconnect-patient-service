package com.jdreyes.healthconnect_patient_service.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Component
@Converter
public class EncryptionConverter implements AttributeConverter<String, String> {

    private static String secretKey;

    // Inyectamos la clave desde application.yml o variables de entorno (AWS/Docker)
    @Value("${app.security.encryption-key}")
    public void setSecretKey(String key) {
        EncryptionConverter.secretKey = key.trim();
        System.out.println(key.trim());
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) return null;
        
        return EncryptionUtils.encrypt(attribute, secretKey);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        return EncryptionUtils.decrypt(dbData, secretKey);
    }

    

    
}