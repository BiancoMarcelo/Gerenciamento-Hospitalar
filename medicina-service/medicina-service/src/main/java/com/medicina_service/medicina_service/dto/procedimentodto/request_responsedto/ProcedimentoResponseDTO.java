package com.medicina_service.medicina_service.dto.procedimentodto.request_responsedto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcedimentoResponseDTO {

    private Long procedimentoId;

    private String mensagem;

    private LocalDateTime horarioProcedimento;
}
