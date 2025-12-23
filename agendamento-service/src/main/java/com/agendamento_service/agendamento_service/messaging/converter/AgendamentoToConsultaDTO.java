package com.agendamento_service.agendamento_service.messaging.converter;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgendamentoToConsultaDTO {

    private Long agendamentoId;
    private String cpfPaciente;
    private String nomePaciente;
    private LocalDateTime horario;
    private String especialidadeMedico;
}
