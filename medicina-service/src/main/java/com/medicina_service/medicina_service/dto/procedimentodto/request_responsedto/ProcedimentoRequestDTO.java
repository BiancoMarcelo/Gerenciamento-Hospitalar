package com.medicina_service.medicina_service.dto.procedimentodto.request_responsedto;

import com.medicina_service.medicina_service.model.StatusAtendimento;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Schema(description = "Dados para Procedimento Request")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcedimentoRequestDTO {

    @Schema(description = "Número de Id do Procedimento", required = false)
    private Long procedimentoId;

    @Schema(description = "Número de Id do Agendamento do Procedimento", required = false)
    private Long agendamentoId;

    @Schema(description = "Número do CPF do Paciente", required = false)
    private String cpfPaciente;

    @Schema(description = "Nome do Procedimento", required = false)
    private String nomeProcedimento;

    @Schema(description = "Nome do Procedimento", required = false)
    private String nomeExame;

    @Schema(description = "Prioridade do Procedimento", required = false)
    private String prioridade;

    @Schema(description = "Horario do Procedimento", required = false)
    private LocalDateTime horarioProcedimento;

    @Schema(description = "Status de atendiemento",
            example = "AGENDADO",
            required = false)
    private StatusAtendimento statusAtendimento;


}
