package com.agendamento_service.agendamento_service.mapper;


import com.agendamento_service.agendamento_service.dto.agendamentodto.AgendamentoConsultaRequestDTO;
import com.agendamento_service.agendamento_service.dto.agendamentodto.AgendamentoDTO;
import com.agendamento_service.agendamento_service.dto.agendamentodto.AgendamentoExameRequestDTO;
import com.agendamento_service.agendamento_service.dto.agendamentodto.AgendamentoResponseDTO;
import com.agendamento_service.agendamento_service.model.Agendamento;
import com.agendamento_service.agendamento_service.model.Paciente;
import com.agendamento_service.agendamento_service.model.TipoAgendamento;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AgendamentoMapper {

    private final PacienteMapper pacienteMapper;
    private final ModelMapper modelMapper;

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
                    .tipoAgendamento(TipoAgendamento.EXAME)
                .tipoExame(agendamentoExameDTO.getExame())
                .build();
    }

    public AgendamentoResponseDTO toAgendamentoConsultaResponseDTO (Agendamento agendamento) {
        return AgendamentoResponseDTO.builder()
                .mensagem(String.format("O %s de %s foi marcado para %s ",
                        agendamento.getEspecialidade(),
                        agendamento.getPaciente().getNome(),
                        agendamento.getHorario()))
                .codigo(String.format("O código do seu exame é: %s", agendamento.getId()))
                .build();
    }

    public AgendamentoResponseDTO toAgendamentoExameResponseDTO (Agendamento agendamento) {
        return AgendamentoResponseDTO.builder()
                .mensagem(String.format("O %s de %s foi marcado para %s ",
                        agendamento.getTipoExame(),
                        agendamento.getPaciente().getNome(),
                        agendamento.getHorario()))
                .codigo(String.format("O código do seu exame é: %s", agendamento.getId()))
                .build();
    }

    public AgendamentoDTO toAgendamentoDTO (Agendamento agendamento) {
        return modelMapper.map(agendamento, AgendamentoDTO.class);
    }

    public Agendamento toAgendamentoEntity (AgendamentoDTO agendamentoDTO) {
        return modelMapper.map(agendamentoDTO, Agendamento.class);
    }



}
