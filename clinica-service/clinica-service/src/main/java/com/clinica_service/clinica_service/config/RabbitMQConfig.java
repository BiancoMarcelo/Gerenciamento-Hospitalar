package com.clinica_service.clinica_service.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_CONSULTA = "consulta.queue";

    public static final String QUEUE_PROCEDIMENTO = "procedimento.queue";

    @Bean
    public Queue consultaQueue() {
        return new Queue(QUEUE_CONSULTA, true);
    }

    @Bean
    public Queue procedimentoQueue() {
        return new Queue(QUEUE_PROCEDIMENTO, true);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }

    @Bean
    public ApplicationRunner runner(RabbitAdmin rabbitAdmin) {
        return args -> {
            rabbitAdmin.initialize();
            System.out.println("Filas da Clínica criadas no RabbitMQ");
        };
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }
}
