package com.medicina_service.medicina_service.dto.modeldto;

import com.medicina_service.medicina_service.model.StatusAtendimento;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcedimentoDTO {

    private Long id;

    private String cpfPaciente;

    private String nomeProcedimento;

    private String prioridade;

    private LocalDateTime horarioProcedimento;

    private StatusAtendimento statusAtendimento;
}
