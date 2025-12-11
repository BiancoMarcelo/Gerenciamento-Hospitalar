package com.clinica_service.clinica_service.dto.procedimentodto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcedimentoDTO {

    private String cpfPaciente;
    private String tipoProcedimento;
    private String prioridade;
}
