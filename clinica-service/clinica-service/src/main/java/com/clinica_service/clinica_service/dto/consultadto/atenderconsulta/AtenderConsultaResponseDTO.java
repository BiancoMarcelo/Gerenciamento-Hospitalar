package com.clinica_service.clinica_service.dto.consultadto.atenderconsulta;

import com.clinica_service.clinica_service.dto.procedimentodto.ProcedimentoDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.List;

@Schema(description = "Dados de retorno ao atender uma consulta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AtenderConsultaResponseDTO {

    @Schema(description = "Retorna o id do atendimento", required = false)
    private Long atendimentoId;

    @Schema(description = "Retorna uma lista de possíveis doenças do paciente", required = false)
    private List<String> possiveisDoencas;

    @Schema(description = "Retorna tratamentos sugeridos com base nos sintomas apresentados", required = false)
    private List<String> tratamentosSugeridos;

    @Schema(description = "Retorna uma lista com os procedimentos necessários", required = false)
    private List<ProcedimentoDTO> procedimentosNecessarios;

    @Schema(description = "Retorna uma mensagem de confirmação ao usuário", required = false)
    private String mensagem;
}
