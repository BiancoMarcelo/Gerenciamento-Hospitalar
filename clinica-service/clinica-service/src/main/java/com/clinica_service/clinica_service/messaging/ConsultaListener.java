package com.clinica_service.clinica_service.messaging;

import com.clinica_service.clinica_service.config.RabbitMQConfig;
import com.clinica_service.clinica_service.dto.consultadto.ConsultaDTO;
import com.clinica_service.clinica_service.service.ConsultaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConsultaListener {

    private final ConsultaService consultaService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_CONSULTA)
    public void receberConsulta(ConsultaDTO consultaDTO) {
        log.info("Mensagem recebida do RabbitMQ - Agendamento de Id: {} ", consultaDTO.getAgendamentoId());

        try {
            consultaService.processarConsultaAgendada(consultaDTO);
            log.info("Consulta processada com sucesso!");
        } catch (Exception e) {
            log.error("Erro ao processar a consulta: {}", e.getMessage(), e);
        }
    }


}
