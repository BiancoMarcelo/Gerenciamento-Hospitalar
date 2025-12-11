package com.clinica_service.clinica_service.dto.consultadto.atenderconsulta;

import com.clinica_service.clinica_service.dto.procedimentodto.ProcedimentoDTO;
import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AtenderConsultaResponseDTO {

    private Long atendimentoId;
    private List<String> possiveisDoencas;
    private List<String> tratamentosSugeridos;
    private List<ProcedimentoDTO> procedimentosNecessarios;
    private String mensagem;
}
