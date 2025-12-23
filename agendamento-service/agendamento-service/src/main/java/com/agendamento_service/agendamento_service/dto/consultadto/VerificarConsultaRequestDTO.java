package com.agendamento_service.agendamento_service.dto.consultadto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerificarConsultaRequestDTO {

    @Schema(description = "Verifica o horário de uma consulta no MS do clinica-service", required = false)
    private LocalDateTime horario;

    @Schema(description = "Verifica a especialidade de uma consulta no MS do clinica-service", required = false)
    private String especialidade;
}
