package com.clinica_service.clinica_service.dto.consultadto.verificarconsulta;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "Dados de retorno ao verificar uma consulta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerificarConsultaResponseDTO {

    @Schema(description = "Retorna com true se a Consulta esta disponível", required = false)
    private boolean disponivel;

    @Schema(description = "Retorna uma mensagem ao usuário", required = false)
    private String mensagem;
}
