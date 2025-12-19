package com.medicina_service.medicina_service.dto.procedimentodto.request_responsedto;

import com.medicina_service.medicina_service.model.StatusAtendimento;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcedimentoRequestDTO {

    private Long procedimentoId;

    private Long agendamentoId;

    private String cpfPaciente;

    private String nomeProcedimento;

    private String nomeExame;

    private String prioridade;

    private LocalDateTime horarioProcedimento;

    private StatusAtendimento statusAtendimento;


}
