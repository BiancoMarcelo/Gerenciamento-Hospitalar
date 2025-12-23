package com.clinica_service.clinica_service.dto.consultadto.atenderconsulta;

import com.clinica_service.clinica_service.model.TipoConsulta;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Dados para atender uma consulta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AtenderConsultaRequestDTO {

    @Schema(description = "CPF do paciente", required = true)
    @NotBlank(message = "CPF é obrigatório")
    private String cpfPaciente;

    @Schema(description = "Horario da consulta", required = false)
    private LocalDateTime horario;

    @Schema(description = "Código da consulta", required = false)
    private Long codigoConsulta;

    @Schema(description = "Prioridade da consulta", required = false)
    private int prioridade;

    @Schema(description = "Tipo de consulta",
            example = "PEDIATRIA",
            required = false)
    private String tipoConsulta;

    @Schema(description = "Sintomas do paciente", required = true)
    @NotEmpty(message = "Deve informar pelo menos 1 sintoma")
    private List<String> sintomas;
}