package com.agendamento_service.agendamento_service.mapper;

import com.agendamento_service.agendamento_service.dto.PacienteDTO;
import com.agendamento_service.agendamento_service.model.Paciente;
import org.springframework.stereotype.Component;

@Component
public class PacienteMapper {


    public PacienteDTO toDTO (Paciente paciente) {
        return PacienteDTO.builder()
                .nome(paciente.getNome())
                .cpf(paciente.getCpf())
                .idade(paciente.getIdade())
                .sexo(paciente.getSexo())
                .build();

    }

    public Paciente toEntity (PacienteDTO pacienteDTO) {
        return Paciente.builder()
                .nome(pacienteDTO.getNome())
                .cpf(pacienteDTO.getCpf())
                .idade(pacienteDTO.getIdade())
                .sexo(pacienteDTO.getSexo())
                .build();
    }

}
