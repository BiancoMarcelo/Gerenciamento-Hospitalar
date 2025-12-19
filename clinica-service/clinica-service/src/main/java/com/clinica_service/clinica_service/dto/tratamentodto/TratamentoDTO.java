package com.clinica_service.clinica_service.dto.tratamentodto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TratamentoDTO {

    private Long id;

    @NotBlank(message = "Descrição do tratamento é obrigatória")
    private String descricaoTratamento;
}
