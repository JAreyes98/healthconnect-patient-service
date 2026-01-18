package com.jdreyes.healthconnect_patient_service.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class BlindIndexUtils {
    private HmacUtils hmacUtils;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    @Value("${app.security.encryption-key}")
    public void setSecretKey(String key) {
        this.hmacUtils = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, key);
    }

    public String generate(String input) {
        if (input == null) return null;
        
        String normalized = input.trim().toLowerCase();
        return this.hmacUtils.hmacHex(normalized);
    }

    public String generate(LocalDate date) {
        if (date == null) return null;
        String dateStr = date.format(DATE_FORMATTER);
        return this.generate(dateStr);
    }

    public String generateMRN(String firstName, String lastName, LocalDate dob, String dni) {
        String rawSource = (firstName + lastName + dob.toString() + dni).toLowerCase().replaceAll("\\s+", "");
        String hash = org.apache.commons.codec.digest.DigestUtils.sha256Hex(rawSource);
        return hash.substring(0, 10).toUpperCase();
    }
}