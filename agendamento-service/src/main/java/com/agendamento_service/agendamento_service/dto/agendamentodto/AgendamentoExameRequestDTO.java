package com.agendamento_service.agendamento_service.dto.agendamentodto;

import com.agendamento_service.agendamento_service.dto.pacientedto.PacienteDTO;
import io.swagger.v3.oas.annotations.media.Schema;
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
public class AgendamentoExameRequestDTO {

    @Schema(description = "Dados do paciente", required = true)
    @Valid
    @NotNull(message = "Paciente é obrigatório")
    private PacienteDTO paciente;

    @Schema(description = "Horario de agendamento", required = true)
    @NotNull(message = "Horário é obrigatório")
    @Future(message = "Horário deve ser futuro")
    private LocalDateTime horario;

    @Schema(description = "Tipo de exame a ser agendado", required = true)
    @NotBlank(message = "Tipo de exame é obrigatório")
    private String exame;
}
