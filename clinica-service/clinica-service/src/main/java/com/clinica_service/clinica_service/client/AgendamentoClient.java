package com.clinica_service.clinica_service.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class AgendamentoClient {

    private final RestTemplate restTemplate;

    @Value("${agendamento.service.url:http://localhost:8081}")
    private String agendamentoServiceUrl;

    public boolean validarPaciente(String cpf) {

        try {
            String url = UriComponentsBuilder.fromHttpUrl(agendamentoServiceUrl + "/api/paciente/" + cpf)
                    .queryParam("cpf", cpf)
                    .toUriString();
            restTemplate.getForEntity(url, Void.class);
            return true;
        } catch (HttpClientErrorException.NotFound e) {
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao comunicar com agendamento-service");
        }
    }


}
