package com.jdreyes.healthconnect_patient_service.config;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "audit_exchange"; 
    public static final String QUEUE = "audit_queue";
    public static final String ROUTING_KEY = "audit.routing.key";

    @Bean
    public TopicExchange auditExchange() {
        // Esto le dice a Spring: "Asegúrate de que este exchange existe en el server"
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Queue auditQueue() {
        return new Queue(QUEUE, true);
    }

    @Bean
    public Binding binding(Queue auditQueue, TopicExchange auditExchange) {
        return BindingBuilder.bind(auditQueue).to(auditExchange).with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}