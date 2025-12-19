package com.agendamento_service.agendamento_service.dto.agendamentodto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgendamentoResponseDTO {

    private String mensagem;
    private String codigo;
}
