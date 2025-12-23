package com.medicina_service.medicina_service.dto.confirmacaodto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Schema(description = "Dados para Confirmação de Criação de Procedimento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfirmacaoDeCriacaoRequestDTO {

    @Schema(description = "Número de Id do Procedimento", required = true)
    @NotNull(message = "Número do procedimento ou exame não pode ser vazio")
    private Long procedimentoId;

    @Schema(description = "Horario do procedimento", required = true)
    @NotNull(message = "Horário do procedimento ou exame não pode ser vazio")
    private LocalDateTime horario;

}
