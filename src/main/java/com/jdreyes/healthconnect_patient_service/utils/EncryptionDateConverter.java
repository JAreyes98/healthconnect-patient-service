package com.jdreyes.healthconnect_patient_service.utils;


import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Value;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class EncryptionDateConverter implements AttributeConverter<LocalDate, String> {

    private static String secretKey;

    // Inyectamos la clave desde application.yml o variables de entorno (AWS/Docker)
    @Value("${app.security.encryption-key}")
    public void setSecretKey(String key) {
        EncryptionDateConverter.secretKey = key.trim();
        System.out.println(key.trim());
    }

    @Override
    public String convertToDatabaseColumn(LocalDate attribute) {
        if (attribute == null) return null;
        // Convertimos la fecha a String antes de cifrar
        return EncryptionUtils.encrypt(attribute.toString(), EncryptionDateConverter.secretKey);
    }

    @Override
    public LocalDate convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        String decrypted = EncryptionUtils.decrypt(dbData, EncryptionDateConverter.secretKey);
        return LocalDate.parse(decrypted);
    }
}