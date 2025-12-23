package com.clinica_service.clinica_service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PrioridadeAtendimento {

    BAIXA(1, "baixa"),
    MEDIA(2, "media"),
    ALTA(3, "alta"),
    EMERGENCIAL(4, "emergencial");

    private final int nivel;
    private final String descricao;

    public static PrioridadeAtendimento doNivel(int nivel) {
        for (PrioridadeAtendimento p : values()) {
            if (p.nivel == nivel) {
                return p;
            }
        }
        return MEDIA;
    }

    public static PrioridadeAtendimento deDescricao(String descricao) {
        for (PrioridadeAtendimento p : values()) {
            if (p.descricao.equalsIgnoreCase(descricao)) {
                return p;
            }
        }
        return MEDIA;
    }

}
