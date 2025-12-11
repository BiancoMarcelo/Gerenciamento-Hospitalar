package com.clinica_service.clinica_service.dto.consultadto.verificarconsulta;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerificarConsultaResponseDTO {

    private boolean disponivel;
    private String mensagem;
}
