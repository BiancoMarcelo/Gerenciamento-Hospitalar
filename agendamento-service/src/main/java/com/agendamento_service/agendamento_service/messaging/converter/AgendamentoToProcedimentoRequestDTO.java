package com.agendamento_service.agendamento_service.messaging.converter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgendamentoToProcedimentoRequestDTO {

    private Long agendamentoId;

    private String cpfPaciente;

    private String prioridade;

    private String nomeProcedimento;

    private String nomeExame;

    @NotBlank(message = "Horario para agendar exame é obrigatório")
    private LocalDateTime horarioProcedimento;
}
