package com.medicina_service.medicina_service.dto.modeldto;

import com.medicina_service.medicina_service.model.StatusAtendimento;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExameDTO {

    private Long id;
    private Long agendamentoId;
    private String cpfPaciente;
    private String nomeExame;
    private String prioridade;
    private LocalDateTime horarioExame;
    private StatusAtendimento statusAtendimento;
}
