package com.clinica_service.clinica_service.mapper;


import com.clinica_service.clinica_service.dto.tratamentodto.TratamentoDTO;
import com.clinica_service.clinica_service.model.Tratamento;
import org.springframework.stereotype.Component;

@Component
public class TratamentoMapper {

    public Tratamento toEntity(TratamentoDTO tratamentoDTO) {
        return Tratamento.builder()
                .id(tratamentoDTO.getId())
                .descricaoTratamento(tratamentoDTO.getDescricaoTratamento())
                .build();
    }

    public TratamentoDTO toDTO(Tratamento tratamento) {
        return TratamentoDTO.builder()
                .id(tratamento.getId())
                .descricaoTratamento(tratamento.getDescricaoTratamento())
                .build();
    }
}
