package com.clinica_service.clinica_service.dto.medicodto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Schema(description = "Dados para Medico Entity")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicoDTO {

    @Schema(description = "Id do médico cadastrado", required = false)
    private Long id;

    @Schema(description = "Nome do médico cadastrado", required = true)
    @NotBlank(message = "Nome é obrigatório")
    private String nomeMedico;

    @Schema(description = "Especialidade do médico",
            example = "PEDIATRA",
            required = true)
    @NotBlank(message = "Especialidade é obrigatória")
    private String especialidade;
}
