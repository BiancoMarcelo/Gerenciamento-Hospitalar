package com.medicina_service.medicina_service.dto.confirmacaodto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfirmacaoDeCriacaoResponseDTO {

    private Long id;

    private LocalDateTime horario;

    private String mensagem;


}
