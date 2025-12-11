package com.clinica_service.clinica_service.dto.doencadto;

import com.clinica_service.clinica_service.model.Procedimento;
import com.clinica_service.clinica_service.model.Sintoma;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoencaDTO {

    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    private String nomeDoenca;

    @NotEmpty(message = "Doença deve ter associado ao menos um sintoma!")
    private List<Long> sintomasIds;

    private List<Long> procedimentosIds;
}
