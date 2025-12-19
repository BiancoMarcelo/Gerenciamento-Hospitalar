package com.medicina_service.medicina_service.messaging;

import com.medicina_service.medicina_service.config.RabbitMQConfig;
import com.medicina_service.medicina_service.dto.procedimentodto.request_responsedto.ProcedimentoRequestDTO;
import com.medicina_service.medicina_service.service.ExameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExameListener {

    private final ExameService exameService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_EXAME)
    public void receberExame(ProcedimentoRequestDTO procedimentoRequestDTO) {
        log.info("Mensagem recebida do RabbitMQ - Procedimento do CPF: {} ", procedimentoRequestDTO.getCpfPaciente());

        try {
            exameService.processarProcedimentoDaClinica(procedimentoRequestDTO);
            log.info("Consulta processada com sucesso!");
        } catch (Exception e) {
            log.error("Erro ao processar a consulta: {}", e.getMessage(), e);
        }
    }
}
