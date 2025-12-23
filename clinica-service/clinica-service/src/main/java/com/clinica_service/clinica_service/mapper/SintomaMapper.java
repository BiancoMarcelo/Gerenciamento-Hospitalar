package com.clinica_service.clinica_service.mapper;

import com.clinica_service.clinica_service.dto.sintomadto.SintomaDTO;
import com.clinica_service.clinica_service.model.Sintoma;
import org.springframework.stereotype.Component;

@Component
public class SintomaMapper {

    public Sintoma toEntity(SintomaDTO dto) {
        return Sintoma.builder()
                .descricao(dto.getDescricao())
                .prioridade(dto.getPrioridade())
                .build();
    }

    public SintomaDTO toDTO(Sintoma sintoma) {
        return SintomaDTO.builder()
                .id(sintoma.getId())
                .descricao(sintoma.getDescricao())
                .prioridade(sintoma.getPrioridade())
                .tratamentos(sintoma.getTratamentosSugeridos())
                .build();
    }
}
