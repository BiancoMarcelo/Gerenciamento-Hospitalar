package com.agendamento_service.agendamento_service.dto;

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
