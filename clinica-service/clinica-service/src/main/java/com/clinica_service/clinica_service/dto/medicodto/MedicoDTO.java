package com.clinica_service.clinica_service.dto.medicodto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicoDTO {

    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    private String nomeMedico;

    @NotBlank(message = "Especialidade é obrigatória")
    private String especialidade;
}
