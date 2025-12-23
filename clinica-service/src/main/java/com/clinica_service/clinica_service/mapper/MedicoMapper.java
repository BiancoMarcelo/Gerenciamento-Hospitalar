package com.clinica_service.clinica_service.mapper;

import com.clinica_service.clinica_service.dto.medicodto.MedicoDTO;
import com.clinica_service.clinica_service.model.Medico;
import org.springframework.stereotype.Component;

@Component
public class MedicoMapper {

    public Medico toEntity(MedicoDTO dto) {
        return Medico.builder()
                .nomeMedico(dto.getNomeMedico())
                .especialidade(dto.getEspecialidade())
                .build();
    }

    public MedicoDTO toDTO(Medico medico) {
        return MedicoDTO.builder()
                .id(medico.getId())
                .nomeMedico(medico.getNomeMedico())
                .especialidade(medico.getEspecialidade())
                .build();
    }
}
