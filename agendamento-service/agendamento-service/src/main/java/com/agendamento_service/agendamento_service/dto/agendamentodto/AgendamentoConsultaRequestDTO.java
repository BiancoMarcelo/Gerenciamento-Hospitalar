package com.agendamento_service.agendamento_service.dto.agendamentodto;

import com.agendamento_service.agendamento_service.dto.pacientedto.PacienteDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Schema(description = "Dados para agendamento de consulta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgendamentoConsultaRequestDTO {

    @Schema(description = "Dados do paciente", required = true)
    @Valid
    @NotNull(message = "Paciente é obrigatório")
    private PacienteDTO paciente;

    @Schema(description = "Horário da consulta",
            example = "2025-01-15T10:00:00",
            required = true)
    @NotNull(message = "Horário é obrigatório")
    @Future(message = "Horário deve ser futuro")
    private LocalDateTime horario;


    @Schema(description = "Especialidade do médico",
            example = "Cardiologista",
            required = false)
    private String medico;
}
