package com.clinica_service.clinica_service.dto.consultadto.verificarconsulta;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "Dados para verificar uma consulta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerificarConsultaRequestDTO {

    @Schema(description = "Horario da consulta", required = true)
    @NotNull
    private LocalDateTime horario;

    @Schema(description = "Especialidade do médico",
            example = "Cardiologista",
            required = true)
    @NotBlank
    private String especialidade;
}
