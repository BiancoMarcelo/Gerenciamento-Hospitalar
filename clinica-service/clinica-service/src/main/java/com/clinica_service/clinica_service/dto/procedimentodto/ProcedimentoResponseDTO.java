package com.clinica_service.clinica_service.dto.procedimentodto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcedimentoResponseDTO {

    private Long id;
    private String descricaoProcedimento;
    private Boolean altaComplexidade;
}
