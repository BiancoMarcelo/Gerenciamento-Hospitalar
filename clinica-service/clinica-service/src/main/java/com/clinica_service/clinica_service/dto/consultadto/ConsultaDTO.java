package com.clinica_service.clinica_service.dto.consultadto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsultaDTO {

    private Long agendamentoId;
    private String cpfPaciente;
    private String nomePaciente;
    private LocalDateTime horario;
    private String especialidadeMedico;
}
