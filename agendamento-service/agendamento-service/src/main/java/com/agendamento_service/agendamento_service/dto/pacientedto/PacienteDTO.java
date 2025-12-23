package com.agendamento_service.agendamento_service.dto.pacientedto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PacienteDTO {

    @Schema(description = "Id do Paciente criado", required = false)
    private Long id;

    @Schema(description = "Nome do paciente", required = true)
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    private String nome;

    @Schema(description = "CPF do paciente", required = true)
    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "CPF inválido")
    private String cpf;

    @Schema(description = "Idade do paciente", required = true)
    @NotNull(message = "Idade é obrigatória")
    @Min(value = 1, message = "Idade deve ser maior que 0")
    private Integer idade;

    @Schema(description = "Sexo do paciente", required = true)
    @NotBlank(message = "Sexo é obrigatório")
    private String sexo;
}
