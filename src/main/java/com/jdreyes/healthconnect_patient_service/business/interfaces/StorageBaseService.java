package com.jdreyes.healthconnect_patient_service.business.interfaces;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public abstract class StorageBaseService {
    /**
     * Procesa, cifra y envía un archivo al servicio de almacenamiento.
     * @return El UUID generado por el servicio de almacenamiento.
     */
    public abstract UUID store(InputStream inputStream, String originalFileName) throws Exception;

    /**
     * Recupera un archivo del servicio, lo descifra y devuelve los bytes originales.
     */
    public abstract byte[] fetch(UUID fileId) throws Exception;

    public void encryptAndStore(InputStream input, OutputStream output, String secretKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IOException {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        byte[] iv = new byte[12];
        new SecureRandom().nextBytes(iv);
        
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(secretKey.getBytes(), "AES"), new GCMParameterSpec(128, iv));
        
        output.write(iv); // Guardamos el IV al inicio del archivo
        try (CipherOutputStream cos = new CipherOutputStream(output, cipher)) {
            input.transferTo(cos); // Cifrado al vuelo (Streaming)
        }
    }
}
