package com.medicina_service.medicina_service.dto.modeldto;

import com.medicina_service.medicina_service.model.StatusAtendimento;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Schema(description = "Dados para Procedimento Entity")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcedimentoDTO {

    @Schema(description = "Número de Id do Procedimento", required = false)
    private Long id;

    @Schema(description = "Número do CPF do cliente", required = false)
    private String cpfPaciente;

    @Schema(description = "Nome do Procedimento", required = false)
    private String nomeProcedimento;

    @Schema(description = "Prioridade do Procedimento", required = false)
    private String prioridade;

    @Schema(description = "Horário do Procedimento", required = false)
    private LocalDateTime horarioProcedimento;

    @Schema(description = "Status de atendimento do  Procedimento",
            example = "AGENDADO",
            required = false)
    private StatusAtendimento statusAtendimento;
}
