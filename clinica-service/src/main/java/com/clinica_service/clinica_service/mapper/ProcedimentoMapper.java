package com.clinica_service.clinica_service.mapper;

import com.clinica_service.clinica_service.dto.procedimentodto.ProcedimentoDTO;
import com.clinica_service.clinica_service.model.Procedimento;
import org.springframework.stereotype.Component;

@Component
public class ProcedimentoMapper {

    public ProcedimentoDTO toDTO(Procedimento procedimento, String cpfPaciente, Integer prioridadeMaxima) {
        return ProcedimentoDTO.builder()
                .cpfPaciente(cpfPaciente)
                .tipoProcedimento(procedimento.getDescricaoProcedimento())
                .prioridade(converterPrioridade(prioridadeMaxima))
                .build();
    }

    private String converterPrioridade(Integer prioridade) {
        return switch (prioridade) {
            case 1 -> "baixa";
            case 2 -> "média";
            case 3 -> "alta";
            case 4 -> "emergencial";
            default -> "padrão";
        };
    }
}
