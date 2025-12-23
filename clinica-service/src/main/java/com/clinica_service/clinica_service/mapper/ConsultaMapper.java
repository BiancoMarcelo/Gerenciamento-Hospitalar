package com.clinica_service.clinica_service.mapper;

import com.clinica_service.clinica_service.dto.consultadto.ConsultaDTO;
import com.clinica_service.clinica_service.model.Consulta;
import com.clinica_service.clinica_service.model.StatusConsulta;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConsultaMapper {

    private final ModelMapper modelMapper;

    public Consulta toEntity(ConsultaDTO dto) {
        return Consulta.builder()
                .agendamentoId(dto.getAgendamentoId())
                .cpfPaciente(dto.getCpfPaciente())
                .nomePaciente(dto.getNomePaciente())
                .email(dto.getEmail())
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
                .status(consulta.getStatus())
                .build();
    }

    public ConsultaDTO toGenericDTO(Consulta consulta) {
        return modelMapper.map(consulta, ConsultaDTO.class);
    }

    public Consulta toGenericEntity(ConsultaDTO consultaDTO) {
        return modelMapper.map(consultaDTO, Consulta.class);
    }
}
