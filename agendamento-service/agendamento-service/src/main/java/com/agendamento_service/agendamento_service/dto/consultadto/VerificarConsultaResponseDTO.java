package com.agendamento_service.agendamento_service.dto.consultadto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VerificarConsultaResponseDTO {

    @Schema(description = "Retorno com a informação se o horário de uma consulta no MS do clinica-service esta disponível", required = false)
    private boolean disponivel;

    @Schema(description = "Retorno com a mensagem se o horário de uma consulta no MS do clinica-service esta disponível", required = false)
    private String mensagem;
}
