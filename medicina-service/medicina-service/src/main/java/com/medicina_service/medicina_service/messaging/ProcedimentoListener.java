package com.medicina_service.medicina_service.messaging;

import com.medicina_service.medicina_service.config.RabbitMQConfig;
import com.medicina_service.medicina_service.dto.procedimentodto.request_responsedto.ProcedimentoRequestDTO;
import com.medicina_service.medicina_service.service.ProcedimentoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProcedimentoListener {

    private final ProcedimentoService procedimentoService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_PROCEDIMENTO)
    public void receberProcedimento(ProcedimentoRequestDTO procedimentoRequestDTO) {
        log.info("Mensagem recebida do RabbitMQ - Procedimento do CPF: {} ", procedimentoRequestDTO.getCpfPaciente());

        try {
            procedimentoService.processarProcedimentoDaClinica(procedimentoRequestDTO);
            log.info("Consulta processada com sucesso!");
        } catch (Exception e) {
            log.error("Erro ao processar a consulta: {}", e.getMessage(), e);
        }
    }


}
