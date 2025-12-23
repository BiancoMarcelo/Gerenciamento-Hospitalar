package com.clinica_service.clinica_service.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JoinFormula;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "atendimentos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Atendimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "consulta_id", nullable = false, unique = true)
    private Consulta consulta;

    @ManyToMany
    @JoinTable(
            name = "atendimento_sintomas",
            joinColumns = @JoinColumn(name = "atendimento_id"),
            inverseJoinColumns = @JoinColumn(name = "sintoma_id")
    )
    private List<Sintoma> sintomas = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "atendimento_doencas",
            joinColumns = @JoinColumn(name = "atendimento_id"),
            inverseJoinColumns = @JoinColumn(name = "doenca_id")
    )
    @Builder.Default
    private List<Doenca> possiveisDoencas = new ArrayList<>();




}
