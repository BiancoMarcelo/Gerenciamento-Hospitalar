package com.agendamento_service.agendamento_service.dto.agendamentodto;


import com.agendamento_service.agendamento_service.model.Paciente;
import com.agendamento_service.agendamento_service.model.TipoAgendamento;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AgendamentoDTO {

    private Long id;

    private Paciente paciente;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime horario;

    private TipoAgendamento tipoAgendamento;

    private String especialidade;

    private String tipoExame;
}
