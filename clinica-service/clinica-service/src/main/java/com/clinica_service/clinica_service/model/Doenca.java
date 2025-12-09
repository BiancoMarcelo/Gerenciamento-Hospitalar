package com.clinica_service.clinica_service.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "doencas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doenca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @ManyToMany
    @JoinTable(
            name = "doenca_sintomas",
            joinColumns = @JoinColumn(name = "doenca_id"),
            inverseJoinColumns = @JoinColumn(name = "sintoma_id")
    )
    @Builder.Default
    private List<Sintoma> sintomas = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "doenca_procedimentos",
            joinColumns = @JoinColumn(name = "doenca_id"),
            inverseJoinColumns = @JoinColumn(name = "procedimento_id")
    )
    @Builder.Default
    private List<Procedimento> procedimentosNecessarios = new ArrayList<>();
}
