package com.clinica_service.clinica_service.dto.procedimentodto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "Dados para Procedimento Entity")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcedimentoDTO {

    @Schema(description = "CPF do paciente", required = false)
    private String cpfPaciente;

    @Schema(description = "Tipo de procedimento",
            example = "Cirurgia no joelho",
            required = false)
    private String tipoProcedimento;

    @Schema(description = "Prioridade de atendimento",
            examples = {"Baixo", "Emergencial"},
            required = false)
    private String prioridade;
}
