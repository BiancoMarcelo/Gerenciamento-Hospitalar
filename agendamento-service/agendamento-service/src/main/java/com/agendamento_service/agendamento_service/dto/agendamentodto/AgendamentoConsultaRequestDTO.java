package com.agendamento_service.agendamento_service.dto.agendamentodto;

import com.agendamento_service.agendamento_service.dto.pacientedto.PacienteDTO;
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

    private String medico;

    private String exame;
}
