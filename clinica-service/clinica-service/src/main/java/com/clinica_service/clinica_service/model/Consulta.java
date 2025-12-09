package com.clinica_service.clinica_service.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "consultas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String cpfPaciente;

    @Column(nullable = false)
    private String nomePaciente;

    @Column(nullable = false)
    private LocalDateTime horario;

    @Column(nullable = false)
    private String especialidadeMedico;

    @Column(nullable = false)
    private Long agendamentoId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusConsulta status;
}
