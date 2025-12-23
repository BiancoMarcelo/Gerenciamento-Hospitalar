package com.medicina_service.medicina_service.dto.procedimentodto.request_responsedto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Schema(description = "Dados para Response do Procedimento Request")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcedimentoResponseDTO {

    @Schema(description = "Número de Id do Procedimento", required = true)
    private Long procedimentoId;

    @Schema(description = "Mensagem de retorno padrão", required = true)
    private String mensagem;
}
