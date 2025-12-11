package com.clinica_service.clinica_service.dto.procedimentodto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcedimentoCriadoResponseDTO {

    private String mensagem;
    private String codigoExame;
}
