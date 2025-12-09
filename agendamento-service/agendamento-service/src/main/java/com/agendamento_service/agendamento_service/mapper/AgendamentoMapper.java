package com.agendamento_service.agendamento_service.mapper;


import com.agendamento_service.agendamento_service.dto.AgendamentoConsultaRequestDTO;
import com.agendamento_service.agendamento_service.dto.AgendamentoExameRequestDTO;
import com.agendamento_service.agendamento_service.dto.AgendamentoResponseDTO;
import com.agendamento_service.agendamento_service.model.Agendamento;
import com.agendamento_service.agendamento_service.model.Paciente;
import com.agendamento_service.agendamento_service.model.TipoAgendamento;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AgendamentoMapper {

    private final PacienteMapper pacienteMapper;

    public Agendamento toEntityConsulta (AgendamentoConsultaRequestDTO consultaRequestDTO, Paciente paciente) {
        return Agendamento.builder()
                .paciente(paciente)
                .horario(consultaRequestDTO.getHorario())
                .tipoAgendamento(TipoAgendamento.CONSULTA)
                .especialidade(consultaRequestDTO.getMedico())
                .build();
    }

    public Agendamento toEntityExame (AgendamentoExameRequestDTO agendamentoExameDTO, Paciente paciente) {
        return Agendamento.builder()
                .paciente(paciente)
                .horario(agendamentoExameDTO.getHorario())
                .tipoAgendamento(TipoAgendamento.CONSULTA)
                .tipoExame(agendamentoExameDTO.getExame())
                .build();
    }

}
