package com.agendamento_service.agendamento_service.config;

import jakarta.annotation.PostConstruct;
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

    public static final String QUEUE_EXAME = "exame.queue";

    public static final String QUEUE_DELETE_CONSULTA = "delete.consulta.queue";

    public static final String QUEUE_DELETE_EXAME = "delete.exame.queue";

    @Bean
    public Queue consultaQueue() {
        return new Queue(QUEUE_CONSULTA, true);
    }

    @Bean
    public Queue exameQueue() {
        return new Queue(QUEUE_EXAME, true);
    }

    @Bean
    public Queue deleteConsultaQueue() {
        return new Queue(QUEUE_DELETE_CONSULTA, true);
    }

    @Bean
    public Queue deleteExameQueue() {
        return new Queue(QUEUE_DELETE_EXAME, true);
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
            System.out.println(" Filas criadas no RabbitMQ!");
        };

    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }
}
