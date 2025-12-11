package com.clinica_service.clinica_service.mapper;

import com.clinica_service.clinica_service.dto.consultadto.ConsultaDTO;
import com.clinica_service.clinica_service.model.Consulta;
import com.clinica_service.clinica_service.model.StatusConsulta;
import org.springframework.stereotype.Component;

@Component
public class ConsultaMapper {

    public Consulta toEntity(ConsultaDTO dto) {
        return Consulta.builder()
                .agendamentoId(dto.getAgendamentoId())
                .cpfPaciente(dto.getCpfPaciente())
                .nomePaciente(dto.getNomePaciente())
                .horario(dto.getHorario())
                .especialidadeMedico(dto.getEspecialidadeMedico())
                .status(StatusConsulta.AGENDADA)
                .build();
    }

    public ConsultaDTO toDTO(Consulta consulta) {
        return ConsultaDTO.builder()
                .agendamentoId(consulta.getAgendamentoId())
                .cpfPaciente(consulta.getCpfPaciente())
                .nomePaciente(consulta.getNomePaciente())
                .horario(consulta.getHorario())
                .especialidadeMedico(consulta.getEspecialidadeMedico())
                .build();
    }
}
