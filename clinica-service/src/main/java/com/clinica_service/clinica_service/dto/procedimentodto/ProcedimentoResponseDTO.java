package com.clinica_service.clinica_service.dto.procedimentodto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "Dados para Response de Procedimento Request")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcedimentoResponseDTO {

    @Schema(description = "Id do procedimento", required = false)
    private Long id;

    @Schema(description = "Descrição do procedimento a ser realizado", required = false)
    private String descricaoProcedimento;

    @Schema(description = "Retorna se o procedimento é de alta complexidade", required = false)
    private Boolean altaComplexidade;
}
