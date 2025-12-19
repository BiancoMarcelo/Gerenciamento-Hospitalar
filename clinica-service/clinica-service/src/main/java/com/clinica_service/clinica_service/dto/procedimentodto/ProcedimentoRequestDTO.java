package com.clinica_service.clinica_service.dto.procedimentodto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcedimentoRequestDTO {

    @NotBlank(message = "Descrição do procedimento é obrigatória")
    private String descricaoProcedimento;

    private Boolean altaComplexidade;
}
