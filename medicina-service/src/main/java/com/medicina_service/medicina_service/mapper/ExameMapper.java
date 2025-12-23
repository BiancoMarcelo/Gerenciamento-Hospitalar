package com.medicina_service.medicina_service.mapper;

import com.medicina_service.medicina_service.dto.confirmacaodto.ConfirmacaoDeCriacaoResponseDTO;
import com.medicina_service.medicina_service.dto.modeldto.ExameDTO;
import com.medicina_service.medicina_service.dto.procedimentodto.request_responsedto.ProcedimentoRequestDTO;
import com.medicina_service.medicina_service.dto.procedimentodto.request_responsedto.ProcedimentoResponseDTO;
import com.medicina_service.medicina_service.model.Exame;
import com.medicina_service.medicina_service.model.StatusAtendimento;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExameMapper {

    private final ModelMapper modelMapper;

    public Exame toExameEntity(ProcedimentoRequestDTO procedimentoRequestDTO) {
        return Exame.builder()
                .nomeExame(procedimentoRequestDTO.getNomeExame())
                .horarioExame(procedimentoRequestDTO.getHorarioProcedimento())
                .cpfPaciente(procedimentoRequestDTO.getCpfPaciente())
                .prioridade(procedimentoRequestDTO.getPrioridade())
                .statusAtendimento(StatusAtendimento.AGUARDANDO_AGENDAMENTO)
                .build();
    }

    public ProcedimentoResponseDTO toExameDTO(Exame exame) {
        return ProcedimentoResponseDTO.builder()
                .procedimentoId(exame.getId())
                .mensagem(String.format("Id do exame para pré agendamento criado com id: {}, é necessário que o paciente agende o horário! "
                        , exame.getId()))
                .build();

    }

    public ConfirmacaoDeCriacaoResponseDTO toConfirmacaoDeHorarioExame(Exame exame) {
        return ConfirmacaoDeCriacaoResponseDTO.builder()
                .id(exame.getId())
                .mensagem(String.format("Horario do procedimento cirúrgico agendado para %s", exame.getHorarioExame()))
                .build();

    }

    public Exame toEntity (ExameDTO exameDTO) {
        return modelMapper.map(exameDTO, Exame.class);
    }

    public ExameDTO toDTO (Exame exame) {
        return modelMapper.map(exame, ExameDTO.class);
    }
}
