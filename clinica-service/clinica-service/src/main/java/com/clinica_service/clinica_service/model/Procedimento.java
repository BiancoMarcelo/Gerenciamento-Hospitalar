package com.clinica_service.clinica_service.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "procedimentos")
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
    private String descricaoProcedimento;

    private Boolean altaComplexidade;


}
