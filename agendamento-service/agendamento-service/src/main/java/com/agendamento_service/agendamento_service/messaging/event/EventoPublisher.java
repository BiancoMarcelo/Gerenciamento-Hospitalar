package com.agendamento_service.agendamento_service.messaging.event;

import com.agendamento_service.agendamento_service.config.RabbitMQConfig;
import com.agendamento_service.agendamento_service.messaging.converter.AgendamentoToConsultaDTO;
import com.agendamento_service.agendamento_service.messaging.converter.AgendamentoToProcedimentoRequestDTO;
import com.agendamento_service.agendamento_service.model.Agendamento;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventoPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publicarConsulta(Agendamento agendamento) {
        log.info("Enviando consulta para fila: {} ", agendamento.getId());
        AgendamentoToConsultaDTO converterParaConsultaDTO = AgendamentoToConsultaDTO.builder()
                .agendamentoId(agendamento.getId())
                .cpfPaciente(agendamento.getPaciente().getCpf())
                .nomePaciente(agendamento.getPaciente().getNome())
                .horario(agendamento.getHorario())
                .especialidadeMedico(agendamento.getEspecialidade())
                .build();

        rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_CONSULTA, converterParaConsultaDTO);
        log.info("Consulta enviada com sucesso");
    }

    public void publicarExame(Agendamento agendamento) {
        log.info("Enviando exame para fila: {} ", agendamento.getId());

        AgendamentoToProcedimentoRequestDTO converterParaRequestDTO = AgendamentoToProcedimentoRequestDTO.builder()
                .cpfPaciente(agendamento.getPaciente().getCpf())
                .prioridade("padrão")
                .agendamentoId(agendamento.getId())
                .nomeProcedimento(agendamento.getTipoExame())
                .nomeExame(agendamento.getTipoExame())
                .horarioProcedimento(agendamento.getHorario())
                .build();
        rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_EXAME, converterParaRequestDTO);
        log.info("Exame enviado com sucesso");
    }


}
