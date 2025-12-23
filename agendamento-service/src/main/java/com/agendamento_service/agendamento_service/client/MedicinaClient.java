package com.agendamento_service.agendamento_service.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class MedicinaClient {

    private final RestTemplate restTemplate;

    @Value("${medicina.service.url:http://localhost:8083}")
    private String medicinaServiceUrl;

    public boolean exameExiste(String nomeExame) {
        String url = medicinaServiceUrl + "/api/exames/validar-exame/" + nomeExame;

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (HttpClientErrorException.NotFound e) {
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao comunicar com medicina-service");
        }
    }
}
