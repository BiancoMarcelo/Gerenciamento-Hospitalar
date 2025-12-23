package com.clinica_service.clinica_service.dto.doencadto;

import com.clinica_service.clinica_service.model.Procedimento;
import com.clinica_service.clinica_service.model.Sintoma;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Schema(description = "Dados para Doenca Entity")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoencaDTO {

    @Schema(description = "Id da doença", required = false)
    private Long id;

    @Schema(description = "Nome da doença", required = true)
    @NotBlank(message = "Nome é obrigatório")
    private String nomeDoenca;

    @Schema(description = "Lista de Ids de sintomas", required = true)
    @NotEmpty(message = "Doença deve ter associado ao menos um sintoma!")
    private List<Long> sintomasIds;

    @Schema(description = "Lista de Ids de procedimentos", required = false)
    private List<Long> procedimentosIds;
}
