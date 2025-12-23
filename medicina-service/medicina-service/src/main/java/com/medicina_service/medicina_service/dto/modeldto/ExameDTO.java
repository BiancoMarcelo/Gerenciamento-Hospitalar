package com.medicina_service.medicina_service.dto.modeldto;

import com.medicina_service.medicina_service.model.StatusAtendimento;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Schema(description = "Dados do Exame Entity")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExameDTO {

    @Schema(description = "Número de Id do Procedimento", required = false)
    private Long id;

    @Schema(description = "Número de Id de agendamdento do Procedimento", required = false)
    private Long agendamentoId;

    @Schema(description = "Número do CPF do Paciente", required = false)
    private String cpfPaciente;

    @Schema(description = "Nome do exame escolhido", required = false)
    private String nomeExame;

    @Schema(description = "Prioridade de atendimento do procedimento", required = false)
    private String prioridade;

    @Schema(description = "Horario do Exame", required = false)
    private LocalDateTime horarioExame;

    @Schema(description = "Status de Atendimento do Exame",
            example = "AGENDADO",
            required = false)
    private StatusAtendimento statusAtendimento;
}
