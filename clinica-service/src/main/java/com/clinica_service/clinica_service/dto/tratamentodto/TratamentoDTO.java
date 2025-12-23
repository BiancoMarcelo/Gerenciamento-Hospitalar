package com.clinica_service.clinica_service.dto.tratamentodto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Schema(description = "Dados para Tratamento Entity")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TratamentoDTO {

    @Schema(description = "Id do tratamento cadastrado", required = false)
    private Long id;

    @Schema(description = "Descrição do tratamento cadastrado", required = true)
    @NotBlank(message = "Descrição do tratamento é obrigatória")
    private String descricaoTratamento;
}
