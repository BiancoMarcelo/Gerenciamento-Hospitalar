package com.clinica_service.clinica_service.messaging;

import com.clinica_service.clinica_service.config.RabbitMQConfig;
import com.clinica_service.clinica_service.dto.procedimentodto.ProcedimentoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProcedimentoPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void enviarProcedimento(ProcedimentoDTO procedimentoDTO) {
        log.info("Enviando procedimento para o centro cirúrgico: {}", procedimentoDTO.getTipoProcedimento());

        rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_PROCEDIMENTO, procedimentoDTO);

        log.info("Procedimento enviado com sucesso!");
    }
}
