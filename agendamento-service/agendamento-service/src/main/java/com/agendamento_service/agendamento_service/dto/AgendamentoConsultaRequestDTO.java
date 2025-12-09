package com.agendamento_service.agendamento_service.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class AgendamentoConsultaRequestDTO {

    @Valid
    @NotNull(message = "Paciente é obrigatório")
    private PacienteDTO paciente;

    @NotNull(message = "Horário é obrigatório")
    @Future(message = "Horário deve ser futuro")
    private LocalDateTime horario;

    @NotBlank(message = "Especialidade do médico é obrigatória")
    private String medico;
}
