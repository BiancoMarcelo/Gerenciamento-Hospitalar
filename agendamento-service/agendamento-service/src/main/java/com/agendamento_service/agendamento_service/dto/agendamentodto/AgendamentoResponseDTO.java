package com.agendamento_service.agendamento_service.dto.agendamentodto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgendamentoResponseDTO {

    @Schema(description = "Mensagem de retorno de confirmação de agendamento ao usuário", required = false)
    private String mensagem;

    @Schema(description = "Mensagem de retorno com código de agendamento ao usuário", required = false)
    private String codigo;


}
