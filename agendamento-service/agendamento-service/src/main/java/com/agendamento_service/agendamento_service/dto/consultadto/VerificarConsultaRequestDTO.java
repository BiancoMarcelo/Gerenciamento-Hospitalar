package com.agendamento_service.agendamento_service.dto.consultadto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerificarConsultaRequestDTO {

    private LocalDateTime horario;
    private String especialidade;
}
