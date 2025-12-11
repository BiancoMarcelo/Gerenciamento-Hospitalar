package com.clinica_service.clinica_service.dto.consultadto.verificarconsulta;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerificarConsultaRequestDTO {

    @NotNull
    private LocalDateTime horario;

    @NotBlank
    private String especialidade;
}
