package com.medicina_service.medicina_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "procedimentos_cirurgicos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Procedimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long agendamentoId;

    @Column(nullable = false)
    private String cpfPaciente;

    @Column(nullable = false)
    private String nomeProcedimento;

    @Column(nullable = false)
    private String prioridade;

    @Column(nullable = false)
    private LocalDateTime horarioProcedimento;

    @Enumerated
    @Column(nullable = false)
    private StatusAtendimento statusAtendimento;
}
