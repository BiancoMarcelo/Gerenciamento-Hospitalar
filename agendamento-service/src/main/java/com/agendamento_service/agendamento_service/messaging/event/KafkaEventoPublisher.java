package com.agendamento_service.agendamento_service.messaging.event;

import com.agendamento_service.agendamento_service.model.Agendamento;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaEventoPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPICO_HOSPITAL = "teste_hospital";

    public void publicarHistoricoAgendamento(Agendamento agendamento) {
        log.info("Publicando histórico no kafka para o paciente: {} ", agendamento.getPaciente().getNome());

        kafkaTemplate.send(TOPICO_HOSPITAL, agendamento);

        log.info("Evento de histórico enviado ao kafka com sucesso");
    }
}
