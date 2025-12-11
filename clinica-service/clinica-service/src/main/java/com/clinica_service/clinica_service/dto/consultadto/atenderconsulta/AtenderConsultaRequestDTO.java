package com.clinica_service.clinica_service.dto.consultadto.atenderconsulta;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AtenderConsultaRequestDTO {

    @NotBlank(message = "CPF é obrigatório")
    private String cpfPaciente;

    private LocalDateTime horario;

    private Long codigoConsulta;

    @NotEmpty(message = "Deve informar pelo menos 1 sintoma")
    private List<String> sintomas;
}