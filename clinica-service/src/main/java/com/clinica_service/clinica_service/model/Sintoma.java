package com.clinica_service.clinica_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sintomas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sintoma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String descricao;

    @Column(nullable = false)
    private Integer prioridade;

    @ManyToMany
    @JoinTable(
            name = "sintoma_tratamentos",
            joinColumns = @JoinColumn(name = "sintoma_id"),
            inverseJoinColumns = @JoinColumn(name = "tratamento_id")
    )
    @Builder.Default
    private List<Tratamento> tratamentosSugeridos = new ArrayList<>();
}
