package com.agendamento_service.agendamento_service.dto.consultadto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VerificarConsultaResponseDTO {
    private boolean disponivel;
    private String mensagem;
}
