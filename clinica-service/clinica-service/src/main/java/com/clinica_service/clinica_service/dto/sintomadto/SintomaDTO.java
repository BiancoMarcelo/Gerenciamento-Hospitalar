package com.clinica_service.clinica_service.dto.sintomadto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SintomaDTO {

    private Long id;

    @NotBlank(message = "Descrição é obrigatória")
    private String descricao;

    @NotNull(message = "Prioridade é obrigatória")
    @Min(value = 1, message = "Prioridade mínima: 1")
    @Max(value = 4, message = "Prioridade máxima: 4")
    private Integer prioridade;
}
