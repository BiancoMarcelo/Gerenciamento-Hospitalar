package com.medicina_service.medicina_service.dto.confirmacaodto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "Dados de Response da Confirmação de Criação de Procedimento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConfirmacaoDeCriacaoResponseDTO {

    @Schema(description = "Número de Id do Procedimento", required = false)
    private Long id;

    @Schema(description = "Horário do Procedimento", required = false)
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime horario;

    @Schema(description = "Mensagem padrão do Procedimento", required = false)
    private String mensagem;


}
