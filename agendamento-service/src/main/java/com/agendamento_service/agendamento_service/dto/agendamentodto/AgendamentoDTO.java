package com.agendamento_service.agendamento_service.dto.agendamentodto;


import com.agendamento_service.agendamento_service.model.Paciente;
import com.agendamento_service.agendamento_service.model.TipoAgendamento;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Schema(description = "Dados de Agendamento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AgendamentoDTO {


    @Schema(description = "Id do agendamento", required = false)
    private Long id;

    @Schema(description = "Dados do paciente", required = false)
    private Paciente paciente;

    @Schema(description = "Horario do agendamento", required = false)
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime horario;

    @Schema(description = "Tipo de agendamento",
            example = "CONSULTA",
            required = false)
    private TipoAgendamento tipoAgendamento;

    @Schema(description = "Especialidade do médico",
            example = "Cardiologista",
            required = false)
    private String especialidade;

    @Schema(description = "Tipo de exame",
            example = "Coleta de sangue",
            required = false)
    private String tipoExame;
}
