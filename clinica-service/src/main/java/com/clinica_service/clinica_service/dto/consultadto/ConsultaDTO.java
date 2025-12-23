package com.clinica_service.clinica_service.dto.consultadto;

import com.clinica_service.clinica_service.model.StatusConsulta;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "Dados para agendamento retorno de Consulta ao usuário")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsultaDTO {

    @Schema(description = " Id de agendamento", required = false)
    private Long agendamentoId;

    @Schema(description = "CPF do paciente", required = false)
    private String cpfPaciente;

    @Schema(description = "Nome do paciente", required = false)
    private String nomePaciente;

    @Schema(description = "Horário da consulta do paciente", required = false)
    private LocalDateTime horario;

    @Schema(description = "Especiliade médica escolhida para consulta do paciente", required = false)
    private String especialidadeMedico;

    @Schema(description = "Email de cadastro do paciente")
    private String email;

    @Schema(description = "Status da consulta",
            example = "ATENDIDA",
            required = false)
    private StatusConsulta status;
}
