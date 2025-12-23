package com.agendamento_service.agendamento_service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class AgendamentoServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AgendamentoServiceApplication.class, args);
	}

}
