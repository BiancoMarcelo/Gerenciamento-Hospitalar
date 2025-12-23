package com.clinica_service.clinica_service.dto.procedimentodto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Schema(description = "Dados para Procedimento Request")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcedimentoRequestDTO {

    @Schema(description = "Descrição do procedimento",
            example = "Cirurgia",
            required = false)
    @NotBlank(message = "Descrição do procedimento é obrigatória")
    private String descricaoProcedimento;

    @Schema(description = "Retorna se o procedimento é de alta complexidade ou não", required = false)
    private Boolean altaComplexidade;
}
