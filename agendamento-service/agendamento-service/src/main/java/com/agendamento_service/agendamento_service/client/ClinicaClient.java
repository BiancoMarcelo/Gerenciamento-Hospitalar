package com.agendamento_service.agendamento_service.client;

import com.agendamento_service.agendamento_service.dto.consultadto.VerificarConsultaRequestDTO;
import com.agendamento_service.agendamento_service.dto.consultadto.VerificarConsultaResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClinicaClient {

    private final RestTemplate restTemplate;

    @Value("${clinica.service.url:http://localhost:8082}")
    private String clinicaServiceUrl;

    public boolean verificarDisponibilidadeMedico(String especialidade, java.time.LocalDateTime horario) {
        log.info("Verificando disponibilidade na Clínica - Especialidade: {}, Horário: {}",
                especialidade, horario);

        String url = clinicaServiceUrl + "/api/clinica/verificar-disponibilidade";

        VerificarConsultaRequestDTO request = VerificarConsultaRequestDTO.builder()
                .especialidade(especialidade)
                .horario(horario)
                .build();

        try {
            ResponseEntity<VerificarConsultaResponseDTO> response = restTemplate.postForEntity(
                    url,
                    request,
                    VerificarConsultaResponseDTO.class
            );

            VerificarConsultaResponseDTO body = response.getBody();

            if (body != null && body.isDisponivel()) {
                log.info("Médico disponível na Clínica");
                return true;
            }

            log.warn("Médico ocupado na Clínica: {}", body != null ? body.getMensagem() : "");
            return false;

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.CONFLICT) {
                log.warn("Conflito de horário na Clínica");
                return false;
            }

            log.error("Erro ao comunicar com Clínica: {}", e.getMessage());
            throw new RuntimeException("Erro ao verificar disponibilidade na Clínica");
        } catch (Exception e) {
            log.error("Erro inesperado ao comunicar com Clínica: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao comunicar com serviço da Clínica");
        }
    }
}
