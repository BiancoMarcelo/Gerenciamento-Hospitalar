package com.medicina_service.medicina_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI medicinaServiceAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Medicina Service API")
                        .description("API para gerenciamento de exames e procedimentos cirúrgicos")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Seu Nome")
                                .email("seuemail@example.com")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8083")
                                .description("Servidor de Desenvolvimento")
                ));
    }
}
