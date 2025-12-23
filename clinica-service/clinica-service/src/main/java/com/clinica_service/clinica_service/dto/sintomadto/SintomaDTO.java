package com.clinica_service.clinica_service.dto.sintomadto;

import com.clinica_service.clinica_service.model.Tratamento;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Schema(description = "Dados para Sintoma Entity")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SintomaDTO {

    @Schema(description = "Id do Sintoma cadastrado", required = false)
    private Long id;

    @Schema(description = "Descrição do sintoma", required = true)
    @NotBlank(message = "Descrição é obrigatória")
    private String descricao;

    @Schema(description = "Prioridade do sintoma", required = true)
    @NotNull(message = "Prioridade é obrigatória")
    @Min(value = 1, message = "Prioridade mínima: 1")
    @Max(value = 4, message = "Prioridade máxima: 4")
    private Integer prioridade;

    @Schema(description = "Lista de tratamentos baseados nos sintomas", required = false)
    List<Tratamento> tratamentos;
}
