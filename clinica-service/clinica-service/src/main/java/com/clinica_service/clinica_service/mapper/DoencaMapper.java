package com.clinica_service.clinica_service.mapper;

import com.clinica_service.clinica_service.dto.doencadto.DoencaDTO;
import com.clinica_service.clinica_service.model.Doenca;
import com.clinica_service.clinica_service.model.Procedimento;
import com.clinica_service.clinica_service.model.Sintoma;
import com.clinica_service.clinica_service.model.Tratamento;
import com.clinica_service.clinica_service.repository.ProcedimentoRepository;
import com.clinica_service.clinica_service.repository.SintomaRepository;
import com.clinica_service.clinica_service.repository.TratamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DoencaMapper {

    private final SintomaRepository sintomaRepository;
    private final ProcedimentoRepository procedimentoRepository;

    public Doenca toEntity(DoencaDTO doencaDTO) {

        List<Sintoma> sintomas = sintomaRepository.findAllById(doencaDTO.getSintomasIds());

        List<Procedimento> procedimentos = new ArrayList<>();
        if (doencaDTO.getProcedimentosIds() != null && !doencaDTO.getProcedimentosIds().isEmpty()) {
            procedimentos = procedimentoRepository.findAllById(doencaDTO.getProcedimentosIds());
        }

        return Doenca.builder()
                .nomeDoenca(doencaDTO.getNomeDoenca())
                .sintomas(sintomas)
                .procedimentosNecessarios(procedimentos)
                .build();
    }

    public DoencaDTO toDTO (Doenca doenca) {
        return DoencaDTO.builder()
                .id(doenca.getId())
                .nomeDoenca(doenca.getNomeDoenca())
                .sintomasIds(doenca.getSintomas().stream()
                        .map(Sintoma::getId)
                        .collect(Collectors.toList()))
                .procedimentosIds(doenca.getProcedimentosNecessarios() != null ?
                        doenca.getProcedimentosNecessarios().stream()
                        .map(Procedimento::getId)
                        .collect(Collectors.toList())
                        : new ArrayList<>())
                .build();
    }
}
