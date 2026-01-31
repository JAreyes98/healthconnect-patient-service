package com.jdreyes.healthconnect_patient_service.business.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jdreyes.healthconnect_patient_service.business.interfaces.BaseAuditService;
import com.jdreyes.healthconnect_patient_service.config.RabbitMQConfig;

@Service
public abstract class RabbitAuditService implements BaseAuditService {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void logEvent(String action, String details, String severity) {
        Map<String, Object> message = new HashMap<>();
        message.put("timestamp", LocalDateTime.now().toString());
        message.put("service", getServiceName());
        message.put("action", action);
        message.put("details", details);
        message.put("severity", severity);
        
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.EXCHANGE, 
            RabbitMQConfig.ROUTING_KEY, 
            message
        );
    }
    
}
