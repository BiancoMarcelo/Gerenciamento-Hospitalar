package com.medicina_service.medicina_service.dto.confirmacaodto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfirmacaoDeCriacaoRequestDTO {

    @NotNull(message = "Número do procedimento ou exame não pode ser vazio")
    private Long procedimentoId;

    @NotNull(message = "Horário do procedimento ou exame não pode ser vazio")
    private LocalDateTime horario;

}
