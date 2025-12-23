package com.medicina_service.medicina_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "exames")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Exame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long agendamentoId;

    @Column(nullable = false)
    private String cpfPaciente;

    @Column(nullable = false)
    private String nomeExame;

    @Column(nullable = false)
    private String prioridade;

    @Column(nullable = false)
    private LocalDateTime horarioExame;

    @Column(nullable = false)
    private StatusAtendimento statusAtendimento;
}
