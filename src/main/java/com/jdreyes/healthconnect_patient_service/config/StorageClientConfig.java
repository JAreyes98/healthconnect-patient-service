package com.jdreyes.healthconnect_patient_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class StorageClientConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}