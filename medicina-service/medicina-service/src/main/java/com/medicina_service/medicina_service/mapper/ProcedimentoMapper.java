package com.medicina_service.medicina_service.mapper;

import com.medicina_service.medicina_service.dto.confirmacaodto.ConfirmacaoDeCriacaoResponseDTO;
import com.medicina_service.medicina_service.dto.modeldto.ExameDTO;
import com.medicina_service.medicina_service.dto.modeldto.ProcedimentoDTO;
import com.medicina_service.medicina_service.dto.procedimentodto.request_responsedto.ProcedimentoRequestDTO;
import com.medicina_service.medicina_service.dto.procedimentodto.request_responsedto.ProcedimentoResponseDTO;
import com.medicina_service.medicina_service.model.Exame;
import com.medicina_service.medicina_service.model.Procedimento;
import com.medicina_service.medicina_service.model.StatusAtendimento;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProcedimentoMapper {

    private final ModelMapper modelMapper;

    public Procedimento toProcedimentoCirurgicoEntity(ProcedimentoRequestDTO procedimentoRequestDTO) {
        return Procedimento.builder()
                .cpfPaciente(procedimentoRequestDTO.getCpfPaciente())
                .nomeProcedimento(procedimentoRequestDTO.getNomeProcedimento())
                .prioridade(procedimentoRequestDTO.getPrioridade())
                .statusAtendimento(StatusAtendimento.AGUARDANDO_AGENDAMENTO)
                .build();
    }

    public ProcedimentoResponseDTO toProcedimentoCirurgicoDTO(Procedimento procedimentoCirurgico) {
        return ProcedimentoResponseDTO.builder()
                .procedimentoId(procedimentoCirurgico.getId())
                .mensagem(String.format("Id do procedimento para pré agendamento criado com id: {" +
                        procedimentoCirurgico.getId() + "}, é necessário que o paciente agende o horário!"))
                .build();

    }

    public ConfirmacaoDeCriacaoResponseDTO toProcedimentoCirurgicoConfirmarHorarioDTO(Procedimento procedimentoCirurgico) {
        return ConfirmacaoDeCriacaoResponseDTO.builder()
                .mensagem(String.format("Id do procedimento criado com número: {" + procedimentoCirurgico.getId() + "}"))
                .horario(procedimentoCirurgico.getHorarioProcedimento())
                .build();

    }

    public Procedimento toEntity (ProcedimentoDTO procedimentoDTO) {
        return modelMapper.map(procedimentoDTO, Procedimento.class);
    }

    public ProcedimentoDTO toDTO (Procedimento procedimento) {
        return modelMapper.map(procedimento, ProcedimentoDTO.class);
    }


}
