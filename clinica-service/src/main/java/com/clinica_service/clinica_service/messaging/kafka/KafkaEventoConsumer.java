package com.clinica_service.clinica_service.messaging.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaEventoConsumer {

    @KafkaListener(topics = "teste-hospital", groupId = "clinica-group")
    public void consumirEventoAgendamento(Object mensagem) {
        log.info("NOVO EVENTO RECEBIDO NO KAFKA");
        log.info("Conteúdo do evento: {}", mensagem.toString());
    }

}
